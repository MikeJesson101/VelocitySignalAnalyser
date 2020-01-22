// This file is part of MAJ's Velocity Signal Analyser 
// Copyright (C) 2009 - 2016 Michael Jesson
// 
// MAJ's Velocity Signal Analyser is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; either version 3
// of the License, or (at your option) any later version.
// 
// MAJ's Velocity Signal Analyser is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with MAJ's Velocity Signal Analyser.  If not, see <http://www.gnu.org/licenses/>.

package com.mikejesson.vsa.frontEnd;

import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.mikejesson.majfc.guiComponents.MAJFCAbstractButtonTab;
import com.mikejesson.majfc.guiComponents.MAJFCButton;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.helpers.MAJFCLinkedGUIComponentsAction;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndCallBackAdapter;
import com.mikejesson.vsa.backEndExposed.BackEndCallbackInterface;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.DataSetConfig.DataSetConfigDocHandler;
import com.mikejesson.vsa.backend.DAFileOutputStringBuffer;
import com.mikejesson.vsa.frontEnd.DataSetOverviewDisplay;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;

import qdxml.DocHandler;
import qdxml.QDParser;


/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DAFrame extends JFrame {
	private static DAFrame sMe;
	private static BackEndAPI sBackEndAPI;
	private MAJFCPanel mFramePanel;
	
	private JTabbedPane mDataSetViewsHolder;
	private Hashtable<AbstractDataSetUniqueId, DataSetOverviewDisplay> mCrossSectionOverviewDisplaysLookup = new Hashtable<AbstractDataSetUniqueId, DataSetOverviewDisplay>();
	private Hashtable<DataSetOverviewDisplay, AbstractDataSetUniqueId> mCrossSectionOverviewDisplaysReverseLookup = new Hashtable<DataSetOverviewDisplay, AbstractDataSetUniqueId>();
	private Hashtable<AbstractDataSetUniqueId, File> mDataSetFilesLookup = new Hashtable<AbstractDataSetUniqueId, File>();
	private Hashtable<File, AbstractDataSetUniqueId> mDataSetFilesReverseLookup = new Hashtable<File, AbstractDataSetUniqueId>();
	
	private String mAbsoluteConfigFilePath;
	private static boolean mNoConfigFile;
	private DataSetConfig mDefaultDataSetConfig;

	private String mStringsFile;
	
	private boolean mImportingFiles = false;
	
	private final MyBackEndCallBackAdapter mBackEndCallBackAdapter = new MyBackEndCallBackAdapter();

	JMenu mDataSetMenu;
	JMenu mDataPointMenu;
	
	// The buttons
	AbstractButton[] mNewSingleProbeDataSetButtons = {new JMenuItem(),	new MAJFCButton()};
	AbstractButton[] mNewMultipleProbeDataSetButtons = {new JMenuItem(),	new MAJFCButton()};
	AbstractButton[] mNewMultiRunSingleProbeDataSetButtons = {new JMenuItem(),	new MAJFCButton()};
	AbstractButton[] mNewMultiRunMultiProbeDataSetButtons = {new JMenuItem(),	new MAJFCButton()};
	AbstractButton[] mSaveDataSetButtons = {new JMenuItem(), new MAJFCButton()};
	AbstractButton[] mOpenDataSetButtons = {new JMenuItem(), new MAJFCButton()};
	AbstractButton[] mImportCSVDataFilesButtons = {new JMenuItem(), new MAJFCButton()};
	AbstractButton[] mImportSingleProbeBinaryDataFilesButtons = {new JMenuItem(), new MAJFCButton()};
	AbstractButton[] mImportConvertedVNODataFilesButtons = {new JMenuItem(), new MAJFCButton()};
	AbstractButton[] mImportSingleUMeasurementsFromFileButtons = {new JMenuItem(), new MAJFCButton()};
	JMenu mDataPointSummaryDataCalculations = new JMenu(DAStrings.getString(DAStrings.CALCULATE_DATA_POINT_SUMMARY_DATA));
	AbstractButton[][] mDPSDataCalculationsButtons = { 	{ new JMenuItem() },
														{ new JMenuItem() },
														{ new JMenuItem() },
														{ new JMenuItem() },
														{ new JMenuItem() } }; 
	AbstractButton[] mConfigurationButtons = { new JMenuItem() };
	AbstractButton[] mAboutButtons = { new JMenuItem() };
	
	JToolBar mDataSetButtons = new JToolBar();
	JToolBar mDataImportButtons = new JToolBar();

	/**
	 * Gets the only instance of DAFrame
	 * 
	 * @return the only instance of DAFrame
	 */
	public static DAFrame getFrame(){
		if (sMe == null){
			sMe = new DAFrame();

			new AboutDialog(DAFrame.getFrame());
			
			if (mNoConfigFile) {
				new ConfigurationDialog(DAFrame.getFrame(), true, false);
			}
		}

		return sMe;
	}
	
	/**
	 * Hide superclass constructor
	 * 
	 * @throws HeadlessException
	 */
	private DAFrame() throws HeadlessException {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				DataAnalyser.killVSA();
			}
		});
	
		setTitle("Velocity Signal Analyser (v" + DADefinitions.VERSION +")");
		
		List<Image> iconsList = new Vector<Image>();
		iconsList.add(DataAnalyser.getImage("frame_icon_16.png"));
		iconsList.add(DataAnalyser.getImage("frame_icon_32.png"));
		iconsList.add(DataAnalyser.getImage("frame_icon_64.png"));
		setIconImages(iconsList);
		
		// If this is an application, exit when frame is closed
		if (!DataAnalyser.sIsApplet)
			setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		initialise();

		buildGUI();
		
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width, 500);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		
		sBackEndAPI = BackEndAPI.getBackEndAPI();
	}
	
	/**
	 * Builds the GUI
	 */
	private void buildGUI() {
		setLayout(new GridBagLayout());
		mFramePanel = new MAJFCPanel(new GridBagLayout());
		
		buildMenuAndButtonBar();
		buildBottomPanel();
		
		add(mFramePanel, MAJFCTools.createGridBagConstraint(0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0));
		
		setGUIStates();	
	}

	/**
	 * Sets up system specific stuff
	 */
	private void initialise() {		
		mDefaultDataSetConfig = new DataSetConfig();
		
		mStringsFile = getConfigFileDirectoryPath() + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + DADefinitions.STRINGS_FILE_NAME; 
		
		File file = new File(mStringsFile);
		
		if (file.exists() != false){
			try {
				DAStrings.updateStringsFromFile(file);
			} catch (FileNotFoundException e) {
				MAJFCLogger.log("No strings file.");
			} catch (IOException e) {
				MAJFCLogger.log("Error reading strings file");
			}
		}
	}
	
	/**
	 * Gets the directory the configuration file should be in.
	 * Creates the directory if it does not exist.
	 * 
	 * @return The absolute path to the config file directory
	 */
	String getConfigFileDirectoryPath() {
		String configFileDirectoryPath = null;
		
		if (MAJFCTools.SYSTEM_OS.contains("Linux")){
			MAJFCLogger.log("Linux OS detected", 50);
			configFileDirectoryPath = MAJFCTools.SYSTEM_USER_HOME_DIR;
		} else{
			// Windows, presumably
			MAJFCLogger.log("Windows OS detected", 50);
			configFileDirectoryPath = System.getenv("APPDATA");
		}
		
		configFileDirectoryPath = configFileDirectoryPath + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + DADefinitions.CONFIG_DIR_NAME + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR;
		
		File file = new File(configFileDirectoryPath);

		if (file.exists() == false) {
			MAJFCLogger.log("Configuration file directory not found. Trying to create " + configFileDirectoryPath, 50);

			if (file.mkdirs() == false) {
				MAJFCLogger.log("Failed to create configuration directory.");
				return null;
			}
			
			MAJFCLogger.log("Created configuration file directory " + configFileDirectoryPath, 50);
		}

		MAJFCLogger.log("Using configuration file directory: " + configFileDirectoryPath, 50);
		
		return configFileDirectoryPath;
	}

	/**
	 * Gets the file name of the configuration file.
	 * 
	 * @return The file name of the configuration file.
	 */
	String getConfigFileName() {
		return mAbsoluteConfigFilePath.substring(mAbsoluteConfigFilePath.lastIndexOf(MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR) + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR.length());
	}
	
	/**
	 * Configuration file set
	 */
	void onConfigFileSpecified(String configFileName) {
		mAbsoluteConfigFilePath = getConfigFileDirectoryPath() + configFileName; 
		
		File file = new File(mAbsoluteConfigFilePath);
		mNoConfigFile = file.exists() == false;
		
		if (mNoConfigFile) {
			try{
				file.createNewFile();
			} catch (IOException theException){
				MAJFCLogger.log("Failed to create configuration file.");
				mAbsoluteConfigFilePath = null;
			}
		} else {
			readFromConfigFile();
		}
	}

	/**
	 * Reads the configuration from the configuration file
	 */
	private void readFromConfigFile(){
		if (mAbsoluteConfigFilePath == null){
			return;
		}
		
		File configFile = new File(mAbsoluteConfigFilePath);

		if (configFile.canRead() == false){
			MAJFCLogger.log("Configuration file not readable");
			return;
		}
		
		try{
			DocHandler docHandler = new DataSetConfigDocHandler(mDefaultDataSetConfig);
			FileReader fileReader = new FileReader(configFile);
			QDParser.parse(docHandler, fileReader);

			fileReader.close();
		}
		catch (Exception theException){
			MAJFCLogger.log("Failed to read from config file: " + theException.getMessage());
		}	
	}

	/**
	 * Gets the BackEndAPI instance
	 * @return The BackEndAPI instance
	 */
	public static BackEndAPI getBackEndAPI(){
		return sBackEndAPI;
	}
	
	/**
	 * Build the bottom panel
	 */
	private void buildBottomPanel() {
		mDataSetViewsHolder = new JTabbedPane();
		mDataSetViewsHolder.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setGUIStates();
			}
		});
		
		int y = 1;
		mFramePanel.add(mDataSetViewsHolder, MAJFCTools.createGridBagConstraint(0, y++, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0) );
	}

	/**
	 * Builds the top menu and top button bar
	 */
	private void buildMenuAndButtonBar(){
		mDataSetButtons.setFloatable(false);
		mDataImportButtons.setFloatable(false);
		
		// Data set functionality
		mDataSetMenu = new JMenu(DAStrings.getString(DAStrings.DATA_SET));
		
		DAFrameButtonsActionListener actionListener = new DAFrameButtonsActionListener();
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.OPEN_DATA_SET), DataAnalyser.getImageIcon("book_open.png"), DAStrings.getString(DAStrings.OPEN_DATA_SET_BUTTON_DESC), mOpenDataSetButtons, actionListener);
		
		mDataSetMenu.add(mOpenDataSetButtons[0]);
		mDataSetButtons.add(mOpenDataSetButtons[1]);

		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SAVE_DATA_SET), DataAnalyser.getImageIcon("disk.png"), DAStrings.getString(DAStrings.SAVE_DATA_SET_BUTTON_DESC), mSaveDataSetButtons, actionListener);
		
		mDataSetMenu.add(mSaveDataSetButtons[0]);
		mDataSetButtons.add(mSaveDataSetButtons[1]);
		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.NEW_SINGLE_PROBE_DATA_SET), DataAnalyser.getImageIcon("new_single_probe.png"), DAStrings.getString(DAStrings.NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC), mNewSingleProbeDataSetButtons, actionListener);
		
		mDataSetMenu.add(mNewSingleProbeDataSetButtons[0]);
		mDataSetButtons.add(mNewSingleProbeDataSetButtons[1]);
		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.NEW_MULTIPLE_PROBE_DATA_SET), DataAnalyser.getImageIcon("new_multiple_probe.png"), DAStrings.getString(DAStrings.NEW_MULTIPLE_PROBE_DATA_SET_BUTTON_DESC), mNewMultipleProbeDataSetButtons, actionListener);
		
		mDataSetMenu.add(mNewMultipleProbeDataSetButtons[0]);
		mDataSetButtons.add(mNewMultipleProbeDataSetButtons[1]);
		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET), DataAnalyser.getImageIcon("new_multi_run_single_probe_dataset.png"), DAStrings.getString(DAStrings.NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_BUTTON_DESC), mNewMultiRunSingleProbeDataSetButtons, actionListener);

		mDataSetMenu.add(mNewMultiRunSingleProbeDataSetButtons[0]);
		mDataSetButtons.add(mNewMultiRunSingleProbeDataSetButtons[1]);
		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.NEW_MULTI_RUN_MULTI_PROBE_DATA_SET), DataAnalyser.getImageIcon("new_multi_run_multi_probe_dataset.png"), DAStrings.getString(DAStrings.NEW_MULTI_RUN_MULTI_PROBE_DATA_SET_BUTTON_DESC), mNewMultiRunMultiProbeDataSetButtons, actionListener);

		mDataSetMenu.add(mNewMultiRunMultiProbeDataSetButtons[0]);
		mDataSetButtons.add(mNewMultiRunMultiProbeDataSetButtons[1]);
		
		// Data point functionality
		mDataPointMenu = new JMenu(DAStrings.getString(DAStrings.DATA_POINTS));
		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.IMPORT_CSV_FILES), DataAnalyser.getImageIcon("import_multiple_green.png"), DAStrings.getString(DAStrings.IMPORT_CSV_FILES_BUTTON_DESC), mImportCSVDataFilesButtons, actionListener);
		
		mDataPointMenu.add(mImportCSVDataFilesButtons[0]);
		mDataImportButtons.add(mImportCSVDataFilesButtons[1]);
		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.IMPORT_BINARY_DATA_FILES_BUTTON_LABEL), DataAnalyser.getImageIcon("import_multiple_orange.png"), DAStrings.getString(DAStrings.IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC), mImportSingleProbeBinaryDataFilesButtons, actionListener);
		
		mDataPointMenu.add(mImportSingleProbeBinaryDataFilesButtons[0]);
		mDataImportButtons.add(mImportSingleProbeBinaryDataFilesButtons[1]);

		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.IMPORT_CONVERTED_VNO_FILES), DataAnalyser.getImageIcon("import_multiple_blue.png"), DAStrings.getString(DAStrings.IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC), mImportConvertedVNODataFilesButtons, actionListener);
		
		mDataPointMenu.add(mImportConvertedVNODataFilesButtons[0]);
		mDataImportButtons.add(mImportConvertedVNODataFilesButtons[1]);

		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL), DataAnalyser.getImageIcon("arrow_down_blue.png"), DAStrings.getString(DAStrings.IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_DESC), mImportSingleUMeasurementsFromFileButtons, actionListener);
		
		mDataPointMenu.add(mImportSingleUMeasurementsFromFileButtons[0]);
		mDataImportButtons.add(mImportSingleUMeasurementsFromFileButtons[1]);

		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_DPS_REYNOLDS_STRESSES), null, DAStrings.getString(DAStrings.CALCULATE_DPS_REYNOLDS_STRESSES_DESC), mDPSDataCalculationsButtons[0], actionListener);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_DPS_QH_DATA), null, DAStrings.getString(DAStrings.CALCULATE_DPS_QH_DATA_DESC), mDPSDataCalculationsButtons[1], actionListener);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_DPS_TKE_DATA), null, DAStrings.getString(DAStrings.CALCULATE_DPS_TKE_DATA_DESC), mDPSDataCalculationsButtons[2], actionListener);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_DPS_FIXED_PROBE_CORRELATIONS), null, DAStrings.getString(DAStrings.CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_DESC), mDPSDataCalculationsButtons[3], actionListener);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_DPS_ALL_LABEL), null, DAStrings.getString(DAStrings.CALCULATE_DPS_ALL_DESC), mDPSDataCalculationsButtons[4], actionListener);
		
		mDataPointSummaryDataCalculations.add(mDPSDataCalculationsButtons[0][0]);
		mDataPointSummaryDataCalculations.add(mDPSDataCalculationsButtons[1][0]);
		mDataPointSummaryDataCalculations.add(mDPSDataCalculationsButtons[2][0]);
		mDataPointSummaryDataCalculations.add(mDPSDataCalculationsButtons[3][0]);
		mDataPointSummaryDataCalculations.addSeparator();
		mDataPointSummaryDataCalculations.add(mDPSDataCalculationsButtons[4][0]);
		mDataPointMenu.add(mDataPointSummaryDataCalculations);
		//buttonBar.add(mDataPointSummaryCalculationButtons[1]);

		// Options functionality
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CONFIGURATION), DataAnalyser.getImageIcon("wrench.png"), DAStrings.getString(DAStrings.CONFIGURATION_BUTTON_DESC), mConfigurationButtons, actionListener);
//		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CONFIGURATION), null, DAStrings.getString(DAStrings.CONFIGURATION_BUTTON_DESC), mConfigurationButtons, actionListener);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.ABOUT_BUTTON_LABEL), DataAnalyser.getImageIcon("about.png"), DAStrings.getString(DAStrings.ABOUT_BUTTON_DESC), mAboutButtons, actionListener);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new GridBagLayout());
		int x = 0;
		menuBar.add(mDataSetMenu, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, 1, 1, 1, 1, 0, 0 ));
		menuBar.add(mDataPointMenu, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, 1, 1, 1, 1, 0, 0 ));
		menuBar.add(mConfigurationButtons[0], MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, 1, 1, 1, 1, 0, 0 ));
		menuBar.add(mAboutButtons[0], MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, 1, 1, 1, 1, 0, 0 ));
		menuBar.add(new MAJFCPanel(), MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 1.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, 1, 1, 1, 1, 0, 0 ));

		setJMenuBar(menuBar);
				
		x = 0;
		mFramePanel.add(mDataSetButtons, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, 1, 1, 1, 1, 0, 0 ));
		mFramePanel.add(mDataImportButtons, MAJFCTools.createGridBagConstraint(x, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, 1, 1, 1, 1, 0, 0 ));
	}
	
	/**
	 * Hide superclass constructor
	 * 
	 * @param arg0
	 */
	private DAFrame(GraphicsConfiguration arg0) {
		super(arg0);
	}

	/**
	 * Hide superclass constructor
	 * 
	 * @param arg0
	 * @throws HeadlessException
	 */
	private DAFrame(String arg0) throws HeadlessException {
		super(arg0);
	}

	/**
	 * Hide superclass constructor
	 * 
	 * @param arg0
	 * @param arg1
	 */
	private DAFrame(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
	}

	/**
	 * Enable the buttons (and menu items) as specified by the parameters
	 */
	public void setGUIStates(){
		boolean dataSetOpen = mCrossSectionOverviewDisplaysLookup.size() > 0;
		
		for (int i = 0; i < mNewSingleProbeDataSetButtons.length; ++i){
			mNewSingleProbeDataSetButtons[i].setEnabled(true);
		}

		for (int i = 0; i < mNewMultipleProbeDataSetButtons.length; ++i){
			mNewMultipleProbeDataSetButtons[i].setEnabled(true);
		}

		for (int i = 0; i < mNewMultiRunSingleProbeDataSetButtons.length; ++i){
			mNewMultiRunSingleProbeDataSetButtons[i].setEnabled(true);
		}

		for (int i = 0; i < mNewMultiRunMultiProbeDataSetButtons.length; ++i){
			mNewMultiRunMultiProbeDataSetButtons[i].setEnabled(true);
		}

		for (int i = 0; i < mSaveDataSetButtons.length; ++i){
			mSaveDataSetButtons[i].setEnabled(dataSetOpen && getCurrentCrossSectionOverViewDisplay().hasChanged());
		}
		
		for (int i = 0; i < mOpenDataSetButtons.length; ++i){
			mOpenDataSetButtons[i].setEnabled(true);
		}

		DataSetType dataSetType = BackEndAPI.DST_SINGLE_PROBE;
		boolean dataSetUnlocked = false;
		boolean dataSetIsMultiRun = false;
		
		if (sBackEndAPI != null && dataSetOpen) {
			try {
				dataSetType = sBackEndAPI.getDataSetType(getCurrentDataSetId());
			} catch (BackEndAPIException theException) {
				theException.printStackTrace();
				dataSetType = BackEndAPI.DST_SINGLE_PROBE;
			}

			try {
				dataSetUnlocked = sBackEndAPI != null && (sBackEndAPI.dataSetIsLocked(getCurrentDataSetId()) == false);
			} catch (BackEndAPIException theException) {
				theException.printStackTrace();
				dataSetUnlocked = false;
			}
			
			dataSetIsMultiRun = dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE);
		}
		
		for (int i = 0; i < mImportCSVDataFilesButtons.length; ++i){
			mImportCSVDataFilesButtons[i].setEnabled(dataSetOpen & dataSetUnlocked);
			mImportCSVDataFilesButtons[i].setVisible(mImportCSVDataFilesButtons[i].isEnabled());
		}
		
		for (int i = 0; i < mImportSingleProbeBinaryDataFilesButtons.length; ++i){
			if (dataSetType.equals(BackEndAPI.DST_SINGLE_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE)) {
				mImportSingleProbeBinaryDataFilesButtons[1].setToolTipText(DAStrings.getString(DAStrings.IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC));
			} else if (dataSetType.equals(BackEndAPI.DST_MULTI_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE)) {
				mImportSingleProbeBinaryDataFilesButtons[1].setToolTipText(DAStrings.getString(DAStrings.IMPORT_MULTI_PROBE_BINARY_FILES_BUTTON_DESC));
			}
			
			mImportSingleProbeBinaryDataFilesButtons[i].setEnabled(dataSetOpen & dataSetUnlocked);
			mImportSingleProbeBinaryDataFilesButtons[i].setVisible(mImportSingleProbeBinaryDataFilesButtons[i].isEnabled());
		}
				
		for (int i = 0; i < mImportConvertedVNODataFilesButtons.length; ++i){
			if (dataSetType.equals(BackEndAPI.DST_SINGLE_PROBE)) {
				mImportConvertedVNODataFilesButtons[1].setToolTipText(DAStrings.getString(DAStrings.IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC));
			} else if (dataSetType.equals(BackEndAPI.DST_MULTI_PROBE)) {
				mImportConvertedVNODataFilesButtons[1].setToolTipText(DAStrings.getString(DAStrings.IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_BUTTON_DESC));
			}

			mImportConvertedVNODataFilesButtons[i].setEnabled(dataSetOpen & dataSetUnlocked & (dataSetIsMultiRun == false));
			mImportConvertedVNODataFilesButtons[i].setVisible(mImportConvertedVNODataFilesButtons[i].isEnabled());
		}
				
		for (int i = 0; i < mImportSingleUMeasurementsFromFileButtons.length; ++i){
			mImportSingleUMeasurementsFromFileButtons[i].setEnabled(dataSetOpen & dataSetUnlocked & (dataSetIsMultiRun == false));
			mImportSingleUMeasurementsFromFileButtons[i].setVisible(mImportSingleUMeasurementsFromFileButtons[i].isEnabled());
		}
				
		mDataPointMenu.setEnabled(dataSetOpen & dataSetUnlocked);
		
		if (mDataPointMenu.isEnabled()) {
			setDPMenuGUIStates();
		}
		
		mDataImportButtons.setVisible(dataSetOpen);
		
		mDataSetViewsHolder.setEnabled(dataSetOpen);
	}

	/**
	 * Enables/disables mDataPointMenu menu items appropriately (disables those for which the quantities have already been calculated)
	 */
	private void setDPMenuGUIStates() {
		boolean[] enableMenuItem = new boolean[mDPSDataCalculationsButtons.length];
		
		for (int i = 0; i < mDPSDataCalculationsButtons.length; ++i) {
			enableMenuItem[i] = true;
		}
		
		try {
			Hashtable<DataPointSummaryIndex, DataPointSummaryIndex> missingDataPointSummaryFieldKeys = BackEndAPI.getBackEndAPI().getMissingDataPointSummaryFieldKeys(getCurrentDataSetId());
			
			if (missingDataPointSummaryFieldKeys != null) {
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN) == false
						|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN) == false
						|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN) == false) {
					enableMenuItem[0] = false;
				}
				
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false) {
					enableMenuItem[1] = false;
				}
				
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_U_PRIME) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_V_PRIME) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_W_PRIME) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_TKE) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_U_TKE_FLUX) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_V_TKE_FLUX) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_W_TKE_FLUX) == false) {
					enableMenuItem[2] = false;
				}
				
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION) == false) {
					enableMenuItem[3] = false;
				}
			}
		} catch (BackEndAPIException e) {
			e.printStackTrace();
		}

		boolean enableAll = false;
		int lengthMinusOne = mDPSDataCalculationsButtons.length - 1; 
		for (int i = 0; i < lengthMinusOne; ++i) {
			mDPSDataCalculationsButtons[i][0].setEnabled(enableMenuItem[i]);
			enableAll |= enableMenuItem[i];
		}
		
		mDPSDataCalculationsButtons[lengthMinusOne][0].setEnabled(enableAll);
	}
	
	public class MyBackEndCallBackAdapter extends BackEndCallBackAdapter {
		@Override
		/**
		 * Data point added successfully
		 * @param dataSetId The data set id
		 * @param yCoord The y-coordinate of the added point
		 * @param zCoord The z-coordinate of the added point
		 */
		public void onDataPointAdded(AbstractDataSetUniqueId dataSetId, int yCoord, int zCoord) {
			getCrossSectionOverViewDisplayForDataSet(dataSetId).setDataSetChanged(true);
			
			setGUIStates();
	
			updateDisplay(dataSetId);
		}
	
		@Override
		/**
		 * New data set created successfully
		 * @param dataSetId The data set id
		 */
		public void onNewDataSetCreated(AbstractDataSetUniqueId dataSetId, File dataSetFile) {
			createAndAddDataSetViews(dataSetId, dataSetFile, mDefaultDataSetConfig);
	
			getCrossSectionOverViewDisplayForDataSet(dataSetId).setDataSetChanged(false);
	
			setGUIStates();
	
			updateDisplay(dataSetId);
		}

		@Override
		/**
		 * Data set successfully closed
		 * @param dataSetId The id of the data set closed
		 */
		public void onDataSetClosed(AbstractDataSetUniqueId dataSetId) {
			removeDataSetViews(dataSetId);
		}
		
		@Override
		/**
		 * Configuration data has been set in the back end
		 * @param dataSetId The data set id
		 * @param field The data field that has been set
		 */
		public void onConfigDataSet(AbstractDataSetUniqueId dataSetId) {
			try {
				getCrossSectionOverViewDisplayForDataSet(dataSetId).setConfigData(BackEndAPI.getBackEndAPI().getConfigData(dataSetId));
			} catch (BackEndAPIException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Summary data has been recalculated
		 * @param dataSetId The id of the data set that was used
		 */
		public void onSummaryDataRecalculated(AbstractDataSetUniqueId dataSetId){
			getCrossSectionOverViewDisplayForDataSet(dataSetId).updateDisplay();
		}

		@Override
		/**
		 * @param dataSetId The data set id
		 * @param dataSetFile The data set file
		 * @param shearStressMeasurements The shear stress measurements for the data set
		 */
		public void onDataSetOpened(AbstractDataSetUniqueId dataSetId, File dataSetFile, DataSetConfig configData) {
			createAndAddDataSetViews(dataSetId, dataSetFile, configData);
			
			DataSetOverviewDisplay overviewDisplay = getCrossSectionOverViewDisplayForDataSet(dataSetId);
			overviewDisplay.setDataSetChanged(false);
			
			setGUIStates();
			
			// Do this before building the graphs as it sets bounds for the axis ranges
			updateDisplay(dataSetId);
		}

		/**
		 * Data set saved successfully
		 * @param newDataSetId The data set id
		 * @param oldDataSetId The data set id before the save (may have changed)
		 * @param dataSetFile The data set file
		 */
		@Override
		public void onDataSetSaved(AbstractDataSetUniqueId newDataSetId, AbstractDataSetUniqueId oldDataSetId, File dataSetFile) {
			DataSetOverviewDisplay overviewDisplay = getCrossSectionOverViewDisplayForDataSet(oldDataSetId);
			overviewDisplay.setDataSetChanged(false);

			if (newDataSetId.equals(oldDataSetId) == false) {
				overviewDisplay.setDataSetId(newDataSetId);
	
				removeDataSetViews(oldDataSetId);
				addDataSetViews(newDataSetId, dataSetFile, overviewDisplay);
			}
			
			setGUIStates();
			
			// Do this before building the graphs as it sets bounds for the axis ranges
			updateDisplay(newDataSetId);
		}

		@Override
		/**
		 * BackEndAPICallbackInterface implementation
		 */
		public void onDataPointsRemoved(AbstractDataSetUniqueId dataSetId, Vector<Integer> yCoords, Vector<Integer> zCoords) {
			getCrossSectionOverViewDisplayForDataSet(dataSetId).setDataSetChanged(true);
			setGUIStates();
			updateDisplay(dataSetId);
		}

		@Override
		/**
		 * BackEndAPICallbackInterface implementation
		 */
		public void onDataPointSummaryFieldCalculated(AbstractDataSetUniqueId dataSetId, Vector<DataPointSummaryIndex> calculatedDPSFields) {
			getCrossSectionOverViewDisplayForDataSet(dataSetId).setDataSetChanged(true);
			setGUIStates();
			updateDisplay(dataSetId);
		}
		
		@Override
		/**
		 * BackEndAPICallbackInterface implementation
		 */
		public void onDataPointDataRecalculated(AbstractDataSetUniqueId dataSetId, int progress, int yCoord, int zCoord) {
			getCrossSectionOverViewDisplayForDataSet(dataSetId).setDataSetChanged(true);
			setGUIStates();
			updateDisplay(dataSetId);
		}
		
		@Override
		/**
		 * BackEndAPICallbackInterface implementation
		 */
		public void onRotationCorrectionBatchCreated(AbstractDataSetUniqueId dataSetId) {
			getCurrentCrossSectionOverViewDisplay().setDataSetChanged(true);
			setGUIStates();
			//mMessageBar.setText("Done");
			updateDisplay(dataSetId);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		/**
		 * BackEndAPICallbackInterface implementation
		 */
		public void onRotationCorrectionBatchesCreatedFromFile(AbstractDataSetUniqueId dataSetId) {
			onRotationCorrectionBatchCreated(dataSetId);
		}
		
		@Override
		public void onDataPointTrimmed(AbstractDataSetUniqueId dataSetId, DataPointSummary dataPointSummaries, DataPointDetail dataPointDetails) {
			getCrossSectionOverViewDisplayForDataSet(dataSetId).setDataSetChanged(true);
			setGUIStates();
			updateDisplay(dataSetId);
		}
	}
	
	/**
	 * Adds the views for the specified data set, setting the configuration data too
	 * @param dataSetId The data set to add views for
	 * @param dataSetFile The file for the data set
	 * @param configData The configuration data (or null if none is to be set)
	 */
	private void createAndAddDataSetViews(AbstractDataSetUniqueId dataSetId, File dataSetFile, DataSetConfig configData) {
		DataSetOverviewDisplay overviewDisplay = new DataSetOverviewDisplay(dataSetId);
		overviewDisplay.setConfigData(configData);
		
		addDataSetViews(dataSetId, dataSetFile, overviewDisplay);
	}
	
	/**
	 * Adds the views for the specified data set, setting the configuration data too
	 * @param dataSetId The data set to add views for
	 * @param dataSetFile The file for the data set
	 * @param config The configuration data (or null if none is to be set)
	 */
	private void addDataSetViews(AbstractDataSetUniqueId dataSetId, File dataSetFile, DataSetOverviewDisplay overviewDisplay) {
		mCrossSectionOverviewDisplaysLookup.put(dataSetId, overviewDisplay);
		mCrossSectionOverviewDisplaysReverseLookup.put(overviewDisplay, dataSetId);
		mDataSetFilesLookup.put(dataSetId, dataSetFile);
		mDataSetFilesReverseLookup.put(dataSetFile, dataSetId);
		
		int tabsCount = mDataSetViewsHolder.getTabCount();
		mDataSetViewsHolder.remove(overviewDisplay);
		mDataSetViewsHolder.add(overviewDisplay, dataSetId.getFullDisplayString());
		mDataSetViewsHolder.setSelectedComponent(overviewDisplay);
		mDataSetViewsHolder.setTabComponentAt(tabsCount, new MyButtonTab(dataSetId.getFullDisplayString(), dataSetId));
	}

	/**
	 * Removes the views for the specified data set
	 * @param dataSetId The data set to add views for
	 * @param fixedProbeDataSetIds The ids of the fixed probe data sets for this data set
	 */
	private void removeDataSetViews(AbstractDataSetUniqueId dataSetId) {
		DataSetOverviewDisplay overviewDisplay = mCrossSectionOverviewDisplaysLookup.remove(dataSetId);
		mDataSetFilesReverseLookup.remove(mDataSetFilesLookup.get(dataSetId));
		mDataSetFilesLookup.remove(dataSetId);
		
		if (overviewDisplay != null) {
			mCrossSectionOverviewDisplaysReverseLookup.remove(overviewDisplay);
		}

		mDataSetViewsHolder.remove(overviewDisplay);
		
		setGUIStates();
	}

	public class MyButtonTab extends MAJFCAbstractButtonTab {
		public final AbstractDataSetUniqueId mDataSetId;
		
		/**
		 * Constructor
		 * @param title The title to display on the tab
		 * @param dataSetId The id of the data set associated with this tab
		 */
		private MyButtonTab(String title, AbstractDataSetUniqueId dataSetId) {
			super(title, DataAnalyser.getImageIcon("close.gif"));
			
			mDataSetId = dataSetId;
		}

		/**
		 * Tab button pressed
		 */
		protected void onButtonPressed() {
			try {
				getBackEndAPI().closeDataSet(mDataSetId, mBackEndCallBackAdapter);
			} catch (BackEndAPIException theException) {
				theException.printStackTrace();
			}
		}
	}

	/**
	 * Updates the display, reloading data set info, etc.
	 * @param dataSetId The id of the data set to update the display for
	 */
	public void updateDisplay(AbstractDataSetUniqueId dataSetId) {
		mCrossSectionOverviewDisplaysLookup.get(dataSetId).updateDisplay();
	}

	/**
	 * Gets the currently shown CrossSectionOverViewDisplay
	 * @return The currently shown CrossSectionOverViewDisplay
	 */
	private DataSetOverviewDisplay getCurrentCrossSectionOverViewDisplay() {
		return (DataSetOverviewDisplay) mDataSetViewsHolder.getSelectedComponent();
	}
	
	/**
	 * Gets the id of the currently shown data set
	 * @return The id of the currently shown data set
	 */
	public AbstractDataSetUniqueId getCurrentDataSetId() {		
		if (mCrossSectionOverviewDisplaysReverseLookup.size() == 0) {
			return null;
		}
		
		return mCrossSectionOverviewDisplaysReverseLookup.get(getCurrentCrossSectionOverViewDisplay());
	}	
	
	/**
	 * Gets the file of the currently shown data set
	 * @return The file for the currently shown data set
	 */
	public File getCurrentDataSetFile() {
		if (mDataSetFilesLookup.size() == 0) {
			return null;
		}
		
		return mDataSetFilesLookup.get(getCurrentDataSetId());
	}
	
	/**
	 * Checks whether the specified file is the file for an already open data set
	 * @param file The file to check
	 * @return True if a data set is open for the specified file
	 */
	public boolean dataSetForFileIsOpen(File file) {
		return mDataSetFilesReverseLookup.get(file) != null;
	}
	
	/**
	 * Gets the CrossSectionOverViewDisplay for the specified data set
	 * @param dataSetId The id of the data set
	 * @return The CrossSectionOverViewDisplay for the specified data set
	 */
	private DataSetOverviewDisplay getCrossSectionOverViewDisplayForDataSet(AbstractDataSetUniqueId dataSetId) {
		return mCrossSectionOverviewDisplaysLookup.get(dataSetId);
	}

	/**
	 * Sets the default data set configuration values
	 * @param excludeLevel The new default data set configuration values
	 */
	public void setDefaultDataSetConfiguration(DataSetConfig defaultDataSetConfiguration) {
		mDefaultDataSetConfig = defaultDataSetConfiguration;
	}
	
	/**
	 * Gets the default data set configuration values
	 * @return The default data set configuration values
	 */
	public DataSetConfig getDefaultDataSetConfiguration() {
		DataSetConfig dsc = new DataSetConfig();
		mDefaultDataSetConfig.copyInto(dsc);
		
		return dsc;
	}
	
	/**
	 * Gets the configuration data for the current data set, or the default configuration if no data set is open
	 */
	public DataSetConfig getCurrentDataSetConfig() {
		AbstractDataSetUniqueId currentId = getCurrentDataSetId();
		
		if (currentId == null) {
			return getDefaultDataSetConfiguration();
		}
		
		try {
			return getBackEndAPI().getConfigData(currentId);
		} catch (BackEndAPIException e) {
			return getDefaultDataSetConfiguration();
		}
	}
	
	/**
	 * Called to indicate the start of a file import session
	 */
	public void fileImportStarted() {
		mImportingFiles = true;
	}

	/**
	 * Called to indicate the end of a file import session
	 * @param dataSetId The id of the data set
	 */
	public void fileImportFinished(AbstractDataSetUniqueId dataSetId) {
		mImportingFiles = false;
		
		updateDisplay(dataSetId);
	}

	/**
	 * Is a file import currently in progress?
	 * @return True if it is
	 */
	public boolean fileImportInProgress() {
		return mImportingFiles;
	}

	/**
	 * Saves the current data set to a file
	 * @param file The file to save to
	 */
	public void saveToFile(File file) {
       	AbstractDataSetUniqueId currentDataSetId = getCurrentDataSetId();
       	new SaveToFileTask(currentDataSetId, file, mBackEndCallBackAdapter);	
 	}
	
	/**
	 * Helper class
	 */
	private class SaveToFileTask extends DAProgressDialog {
		private final File mFile;
		
		private SaveToFileTask(AbstractDataSetUniqueId dataSetId, File file, BackEndCallbackInterface frontEndInterface) {
			super(dataSetId, DAFrame.getFrame(), DAStrings.getString(DAStrings.SAVING_DATA_SET) + " (" + dataSetId.getShortDisplayString() + ')');
			
			mFile = file;
			
			setVisible();
		}

		@Override
		protected Void doInBackground() throws Exception {
			DAFrame.getBackEndAPI().saveDataSet(getDataSetId(), mFile, mBackEndCallBackAdapter, this);
			
			return null;
		}
	};
	
	/**
	 * Creates a rotation correction batch from the points specified by the y- and z-coordinates passed in
	 * @param dataSetId The data set to make the batch in
	 * @param yCoords The y-coordinates of the points to include in the batch
	 * @param zCoords The z-coordinates of the points to include in the batch (these must correspond to the y-coordinates in yCoords)
	 */
	public void createRotationCorrectionBatch(AbstractDataSetUniqueId dataSetId, Vector<Integer> yCoords, Vector<Integer> zCoords) {
		new CreateRotationCorrectionBatchTask(dataSetId, yCoords, zCoords);
	}
	
	/**
	 * Helper class
	 */
	private class CreateRotationCorrectionBatchTask extends DAProgressDialog {
		Vector<Integer> mYCoords;
		Vector<Integer> mZCoords;
		
		private CreateRotationCorrectionBatchTask(AbstractDataSetUniqueId dataSetId, Vector<Integer> yCoords, Vector<Integer> zCoords) {
			super(dataSetId, DAFrame.getFrame(), DAStrings.getString(DAStrings.CREATING_RC_BATCH_PROGRESS_TITLE));

			mYCoords = yCoords;
			mZCoords = zCoords;
			
			setVisible();
		}

		@Override
		protected Void doInBackground() throws Exception {
			getBackEndAPI().createRotationCorrectionBatch(getDataSetId(), mYCoords, mZCoords, mBackEndCallBackAdapter, this);
			
			return null;
		}
	};

	/**
	 * Creates a set of rotation correction batches from the batch.majrcb file in the data set's main directory
	 * @param dataSetId The data set to make the batches in
	 */
	public void createRotationCorrectionBatchesFromFile(AbstractDataSetUniqueId dataSetId) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		new CreateRotationCorrectionBatchFromFileTask(dataSetId);
	}

	/**
	 * Helper class
	 */
	private class CreateRotationCorrectionBatchFromFileTask extends DAProgressDialog {
		private CreateRotationCorrectionBatchFromFileTask(AbstractDataSetUniqueId dataSetId) {
			super(dataSetId, DAFrame.getFrame(), DAStrings.getString(DAStrings.CREATING_RC_BATCHES_PROGRESS_TITLE));
			setVisible();
		}

		protected Void doInBackground() throws Exception {
			MAJFCLogger.logTiming("RC from file started");
			getBackEndAPI().createRotationCorrectionBatchesFromFile(getDataSetId(), mBackEndCallBackAdapter, this);
			MAJFCLogger.logTiming("RC from file finished");
			
			return null;
		}
	};

	/**
	 * Gets the channel mean velocity to be used for scaling purposes (assumes the data set is not for a fixed probe)
	 * @return Either Q/A or the integrated mean from the point velocities
	 */
	public double getChannelMeanVelocityForScaling(AbstractDataSetUniqueId dataSetId) {
		try {
			return getChannelMeanVelocityForScaling(dataSetId, sBackEndAPI.getDataSetType(dataSetId));
		} catch (BackEndAPIException e) {
			e.printStackTrace();
			return getChannelMeanVelocityForScaling(dataSetId, BackEndAPI.DST_SINGLE_PROBE);
		}
	}
	
	/**
	 * Gets the channel mean velocity to be used for scaling purposes
	 * @param dataSetId The id of the data set to get it for
	 * @param forFixedProbeDataSet Set true if this is for a fixed probe data set (in which case the integrated mean is returned, otherwise Q/A is returned)
	 * @return Either Q/A or the integrated mean from the point velocities
	 */
	public double getChannelMeanVelocityForScaling(AbstractDataSetUniqueId dataSetId, DataSetType dataSetType) {
		if (dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_RUN) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_RUN_FIXED_PROBE)) {
			return 1;
		}
		
		if (dataSetType.equals(BackEndAPI.DST_FIXED_PROBE)) {
			try {
				return DAFrame.getBackEndAPI().getCrossSectionDataField(dataSetId, BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_U_VELOCITY);
			} catch (Exception theException) {
				theException.printStackTrace();
				return 1;
			}
		}
		
		return getQOverA();
	}
	
	/**
	 * Gets the mean U for the channel as calculated from Q/A
	 * @return Q/A
	 */
	private double getQOverA() {
		try {
			DataSetConfig configData = BackEndAPI.getBackEndAPI().getConfigData(getCurrentDataSetId());
			double A = configData.get(BackEndAPI.DSC_KEY_CROSS_SECTIONAL_AREA);
			double Q = configData.get(BackEndAPI.DSC_KEY_MEASURED_DISCHARGE);
			
			return A == 0 ? Double.NaN : Q/A;
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
	}
	
	/**
	 * Wrapper around MAJFCTools method to allow string setting
	 * 
	 * Returns a nicely formatted number
	 * @param number The number to format
	 * @param decimal places The number of decimal places to use
	 * @param includeTrailingZeroes If true then the full number of decimal places is shown, filling with zeroes if necessary. If false
	 * then the number is terminated after the last non-zero decimal place  
	 * @return number formatted to the specified number of decimal places
	 */
	public static String formatNumber(double number, int decimalPlaces, boolean includeTrailingZeroes) {
		String theString = MAJFCTools.formatNumber(number, decimalPlaces, includeTrailingZeroes);
		
		if (theString == null) {
			return DAStrings.getString(DAStrings.NOT_YET_CALCULATED);
		}
		
		return theString;
	}

	public MyBackEndCallBackAdapter getBackEndAPICallBackAdapter() {
		return mBackEndCallBackAdapter;
	}

	/**
	 * Writes the configuration settings to the configuration file
	 */
	public void writeToConfigFile(){
		if (mAbsoluteConfigFilePath == null){
			return;
		}
		
		File configFile = new File(mAbsoluteConfigFilePath);
		
		if (configFile.canWrite() == false){
			MAJFCLogger.log("Configuration file not writeable");
			return;
		}
		
		DAFileOutputStringBuffer configString = new DAFileOutputStringBuffer();
		
		configString.append('<');
		configString.append(DADefinitions.CONFIG_MAIN_XML_TAG);
		configString.append('>');
		configString.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);

		configString.append(ConfigurationPanel.getConfigXML(mDefaultDataSetConfig));
		
		configString.append("</");
		configString.append(DADefinitions.CONFIG_MAIN_XML_TAG);
		configString.append('>');
		
		try{
			FileWriter fileWriter = new FileWriter(configFile);
			fileWriter.write(configString.toString());
			fileWriter.close();
		} catch (IOException theException){
			MAJFCLogger.log("Failed to write config string to file: " + configString);
		}
	}
}
