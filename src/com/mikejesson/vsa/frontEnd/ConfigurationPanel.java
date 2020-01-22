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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mikejesson.majfc.guiComponents.MAJFCAbstractFileOrDirChooserButton;
import com.mikejesson.majfc.guiComponents.MAJFCCheckBox;
import com.mikejesson.majfc.guiComponents.MAJFCDialogButton;
import com.mikejesson.majfc.guiComponents.MAJFCNumberTextAreaPanel;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCTranslatingDropDownPanel;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetConfigChangedFlags;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.backend.DAFileOutputStringBuffer;
import com.mikejesson.vsa.backend.DataSet;
import com.mikejesson.vsa.frontEnd.importDialogs.ProbeSetupPanel;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class ConfigurationPanel extends MAJFCPanel implements ActionListener, ItemListener, PropertyChangeListener, DocumentListener {
	private DataSetConfigChangedFlags mDataFieldChanged = new DataSetConfigChangedFlags();

	private MAJFCDialogButton mOkButton;
	private MAJFCDialogButton mCancelButton;
	private MAJFCTranslatingDropDownPanel<Integer> mLengthUnitsGUI;
	private MAJFCTranslatingDropDownPanel<Integer> mVelocityUnitsGUI;
	private MAJFCNumberTextAreaPanel mLeftBankPositionGUI;
	private MAJFCNumberTextAreaPanel mRightBankPositionGUI;
	private MAJFCAbstractFileOrDirChooserButton mBoundaryDefinitionFilenameGUI;
	private MAJFCNumberTextAreaPanel mWaterDepthGUI;
	private MAJFCNumberTextAreaPanel mMeasuredDischargeGUI;
	private MAJFCNumberTextAreaPanel mBedSlopeGUI;
	private JCheckBox mUseBinaryFileFormat;
	private MAJFCTranslatingDropDownPanel<Character> mCSVDataFileDelimiter;
	private MAJFCTranslatingDropDownPanel<Character> mCSVFileDecimalSeparator;
	private JLabel mDefaultVSADataFilePathLabel;
	private MAJFCAbstractFileOrDirChooserButton mDefaultVSADataFilePath;
	private JLabel mDefaultRawDataFilePathLabel;
	private MAJFCAbstractFileOrDirChooserButton mDefaultRawDataFilePath;
	private JLabel mDefaultFileExportFilePathLabel;
	private MAJFCAbstractFileOrDirChooserButton mDefaultFileExportFilePath;
	private JButton mUseDataFilePathForAll;
	private MAJFCTranslatingDropDownPanel<Integer> mPreFilterTypeGUI;
	private MAJFCTranslatingDropDownPanel<Integer> mDespikingFilterTypeGUI;
	private JLabel mDespikingFilterReference;
	private final String[] mDespikingFilterReferences = { DAStrings.getString(DAStrings.DESPIKING_FILTER_NONE_REFERENCE),
			DAStrings.getString(DAStrings.DESPIKING_FILTER_EXCLUDE_LEVEL_REFERENCE), 
			DAStrings.getString(DAStrings.DESPIKING_FILTER_VELOCITY_CORRELATION_REFERENCE), 
			DAStrings.getString(DAStrings.DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_REFERENCE),
			DAStrings.getString(DAStrings.DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_REFERENCE),
			DAStrings.getString(DAStrings.DESPIKING_FILTER_CORRELATION_AND_SNR_REFERENCE),
			DAStrings.getString(DAStrings.DESPIKING_FILTER_MOVING_AVERAGE_REFERENCE),
			DAStrings.getString(DAStrings.DESPIKING_FILTER_W_DIFF_REFERENCE)};
	private MAJFCNumberTextAreaPanel mExcludeLevelGUI;
	private MAJFCNumberTextAreaPanel mModifiedPSTAutoSafeLevelC1GUI;
	private MAJFCNumberTextAreaPanel mModifiedPSTAutoExcludeLevelC2GUI;
	private MAJFCTranslatingDropDownPanel<Integer> mInvalidValueReplacementMethodGUI;
	private MAJFCNumberTextAreaPanel mInvalidValueReplacementPolynomialOrderGUI;
	private MAJFCTranslatingDropDownPanel<Integer> mCSTypeGUI;
	private MAJFCNumberTextAreaPanel mSamplingRateGUI;
	private MAJFCNumberTextAreaPanel mLimitingCorrelationGUI;
	private MAJFCNumberTextAreaPanel mLimitingSNRGUI;
	private MAJFCCheckBox mUsePercentageForCorrAndSNRFilter;
	private MAJFCNumberTextAreaPanel mLimitingWDiffGUI;
	private MAJFCNumberTextAreaPanel mMovingAverageWindowSizeGUI;
	private MAJFCNumberTextAreaPanel mStandardCellWidthGUI;
	private MAJFCNumberTextAreaPanel mStandardCellHeightGUI;
	private ButtonGroup mMultiRunSynchTypeSelectionGroup;
	private JLabel mMultiRunSynchDefaultsLabel;
	private JRadioButton mSynchNoneGUI;
	private JRadioButton mSynchMaxValueGUI;
	private JRadioButton mSynchLimitingValueGUI;
	private MAJFCNumberTextAreaPanel mSynchLimitingValueSetterGUI;
	private MAJFCNumberTextAreaPanel mSynchIgnoreFirstXSecondsSetterGUI;
	private JCheckBox mAutoTrimGUI;
	private MAJFCNumberTextAreaPanel mATTrimStartBySetterGUI;
	private MAJFCNumberTextAreaPanel mATPriorLengthSetterGUI;
	private MAJFCNumberTextAreaPanel mATSampleLengthSetterGUI;
	private MAJFCTranslatingDropDownPanel<Double> mSynchLimitingValueDirectionGUI;
	private JCheckBox mSplitLargeFilesGUI;
	private MAJFCNumberTextAreaPanel mLargeFileCountsPerSplitGUI;
	private MAJFCTranslatingDropDownPanel<Integer> mPSDTypeGUI;
	private MAJFCTranslatingDropDownPanel<Integer> mPSDWindowGUI;
	private MAJFCNumberTextAreaPanel mBartlettWindowsGUI;
	private MAJFCNumberTextAreaPanel mPSDWelchWindowOverlapGUI;
	private MAJFCTranslatingDropDownPanel<Integer> mWaveletTypeGUI;
	private MAJFCTranslatingDropDownPanel<Integer> mWaveletTransformTypeGUI;
	private JCheckBox mWaveletTransformScaleByInstPower;
	private ProbeSetupPanel mProbeSetup;
	private MAJFCCheckBox mInvertXAxis;
	private MAJFCCheckBox mInvertYAxis;
	private MAJFCCheckBox mInvertZAxis;
	private MAJFCCheckBox mLockDataSet;
	private JLabel mCSVFileFormatLabel;
	private JTextField mCSVFileFormatGUI;
	
	private JTabbedPane mTabbedPane;
	
	private int NUMBER_OF_TABS = 0;
	@SuppressWarnings("unused")
	private final int GENERAL_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;
	@SuppressWarnings("unused")
	private final int DATA_SET_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;
//	private final int DATA_POINT_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;
	@SuppressWarnings("unused")
	private final int PROBE_SETUP_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;
	private final int FILTERING_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;
	@SuppressWarnings("unused")
	private final int MULTI_RUN_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;
	@SuppressWarnings("unused")
	private final int LARGE_FILES_AND_MISC_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;
	@SuppressWarnings("unused")
	private final int SPECTRAL_ANALYSIS_CONFIG_TAB_INDEX = NUMBER_OF_TABS++;

	// Used if shown as part of a data set (values specific to that dataset)
	private AbstractDataSetUniqueId mDataSetId;
	private final DataSetType mDataSetType;
	private final DataSetOverviewDisplay mParentDataSetDisplay;
	
	// Used if shown in a dialog (for setting default values)
	private final ConfigurationDialog mParentDialog;
	private final boolean mIsDialog;
		
	/**
	 * The constructor used for a panel to be shown for a particular dataset.
	 * @param dataSetId The id of the dataset the configuration is for
	 * @param dataSetType The type of dataset the configuration is for
	 * @param parentDataSetDisplay The dataset overview display for the dataset the configuration is for
	 */
	public ConfigurationPanel(AbstractDataSetUniqueId dataSetId, DataSetType dataSetType, DataSetOverviewDisplay parentDataSetDisplay) {
		this(dataSetId, dataSetType, parentDataSetDisplay, null, false);
	}
	
	/**
	 * The constructor used for a panel to be shown in the default configuration dialog.
	 * @param parentDialog The dialog box this is being shown in
	 * @param allowCancel Can the dialog be cancelled?
	 */
	public ConfigurationPanel(ConfigurationDialog parentDialog, boolean allowCancel) {
		this(null, null, null, parentDialog, allowCancel);
	}
	
	/**
	 * The constructor
	 * @param parent the parent frame
	 * @param modal is this dialog modal?
	 */
	private ConfigurationPanel(AbstractDataSetUniqueId dataSetId, DataSetType dataSetType, DataSetOverviewDisplay parentDataSetDisplay, ConfigurationDialog parentDialog, boolean allowCancel) {
		super(new GridBagLayout());

		mDataSetId = dataSetId;
		mParentDataSetDisplay = parentDataSetDisplay;
		mDataSetType = dataSetType;
		
		mParentDialog = parentDialog;
		mIsDialog = mParentDialog != null;
		
		buildGUI();
		
		enableTabs();
		mCancelButton.setEnabled(allowCancel);
		
		validate();
	}
	
	/**
	 * Enables/Disables tabs according to what should be available for this data set type (or everything enabled for default configuration setup)
	 */
	private void enableTabs() {
//		mTabbedPane.setEnabledAt(mTabbedPane.indexOfTab(DAStrings.getString(DAStrings.GENERAL_CONFIG_TAB_LABEL)), mIsDialog);
//		mTabbedPane.add(DAStrings.getString(DAStrings.DATA_SET_CONFIG_TAB_LABEL), dataSetConfig);
//		mTabbedPane.add(DAStrings.getString(DAStrings.DATA_POINT_CONFIG_TAB_LABEL), dataPointConfig);
		mTabbedPane.setEnabledAt(mTabbedPane.indexOfTab(DAStrings.getString(DAStrings.PROBE_SETUP_CONFIG_TAB_LABEL)), mIsDialog);
//		mTabbedPane.add(DAStrings.getString(DAStrings.FILTERING_CONFIG_TAB_LABEL), filteringConfig);
//		mTabbedPane.setEnabledAt(mTabbedPane.indexOfTab(DAStrings.getString(DAStrings.MULTI_RUN_CONFIG_TAB_LABEL)), mIsDialog || DataSet.isMultiRun(mDataSetType));
//		mTabbedPane.add(DAStrings.getString(DAStrings.LARGE_FILES_AND_MISC_CONFIG_TAB_LABEL), largeFilesConfig);
		mDespikingFilterReference.setVisible(mIsDialog == false);
		
		if (mIsDialog == false) {
			mTabbedPane.setSelectedIndex(mTabbedPane.indexOfTab(DAStrings.getString(DAStrings.DATA_SET_CONFIG_TAB_LABEL)));
		}
	}
	
	public void updateDisplay() {
		setGUIStates();
	}
	
	@Override
	/**
	 * Sets the GUI states based on which fields have changed
	 */
	protected void setGUIStates() {
		int selectedPreFilter = mPreFilterTypeGUI.getSelectedItem();
		int selectedDespikingFilter = mDespikingFilterTypeGUI.getSelectedItem();
		boolean dataSetEmpty = true;
		try {
			dataSetEmpty = (mIsDialog == false && BackEndAPI.getBackEndAPI().getUnsortedDataPointCoordinates(mDataSetId).length > 0) == false;
		} catch (BackEndAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mExcludeLevelGUI.setEnabled(selectedPreFilter == BackEndAPI.DFT_EXCLUDE_LEVEL.getIntIndex() || selectedDespikingFilter == BackEndAPI.DFT_EXCLUDE_LEVEL.getIntIndex());
		mModifiedPSTAutoSafeLevelC1GUI.setEnabled(selectedDespikingFilter == BackEndAPI.DFT_MODIFIED_PHASE_SPACE_THRESHOLDING.getIntIndex());
		mModifiedPSTAutoExcludeLevelC2GUI.setEnabled(mModifiedPSTAutoSafeLevelC1GUI.isEnabled());
		mInvalidValueReplacementMethodGUI.setEnabled(selectedDespikingFilter != BackEndAPI.DFT_NONE.getIntIndex() && selectedDespikingFilter != BackEndAPI.DFT_MOVING_AVERAGE.getIntIndex());
		mInvalidValueReplacementPolynomialOrderGUI.setEnabled(mInvalidValueReplacementMethodGUI.isEnabled() && mInvalidValueReplacementMethodGUI.getSelectedItem() == BackEndAPI.PRM_12PP_INTERPOLATION.getIntIndex());
		mCSTypeGUI.setEnabled(selectedDespikingFilter == BackEndAPI.DFT_PHASE_SPACE_THRESHOLDING.getIntIndex() || selectedDespikingFilter == BackEndAPI.DFT_MODIFIED_PHASE_SPACE_THRESHOLDING.getIntIndex());
//		mSamplingRateGUI.setEnabled(selectedFilter == BackEndAPI.DFT_PHASE_SPACE_THRESHOLDING.getIntIndex() || selectedFilter == BackEndAPI.DFT_MODIFIED_PHASE_SPACE_THRESHOLDING.getIntIndex() || selectedFilter == BackEndAPI.DFT_VELOCITY_CORRELATION.getIntIndex());
		mLimitingCorrelationGUI.setEnabled(selectedPreFilter == BackEndAPI.DFT_CORRELATION_AND_SNR.getIntIndex() || selectedDespikingFilter == BackEndAPI.DFT_CORRELATION_AND_SNR.getIntIndex());
		mLimitingSNRGUI.setEnabled(mLimitingCorrelationGUI.isEnabled());
		mUsePercentageForCorrAndSNRFilter.setEnabled(mLimitingSNRGUI.isEnabled());
		mLimitingWDiffGUI.setEnabled(selectedPreFilter == BackEndAPI.DFT_W_DIFF.getIntIndex() || selectedDespikingFilter == BackEndAPI.DFT_W_DIFF.getIntIndex());
		mMovingAverageWindowSizeGUI.setEnabled(selectedDespikingFilter == BackEndAPI.DFT_MOVING_AVERAGE.getIntIndex());
		mAutoTrimGUI.setEnabled(dataSetEmpty);
		mATTrimStartBySetterGUI.setEnabled(mAutoTrimGUI.isSelected() && mAutoTrimGUI.isEnabled());
		mATPriorLengthSetterGUI.setEnabled(mSynchNoneGUI.isSelected() == false && mATTrimStartBySetterGUI.isEnabled());
		mATSampleLengthSetterGUI.setEnabled(mATTrimStartBySetterGUI.isEnabled());
		mSynchNoneGUI.setEnabled(mIsDialog || (dataSetEmpty && DataSet.isMultiRunMean(mDataSetType)));
		mSynchMaxValueGUI.setEnabled(mSynchNoneGUI.isEnabled());
		mSynchLimitingValueGUI.setEnabled(mSynchNoneGUI.isEnabled());
		mSynchLimitingValueSetterGUI.setEnabled(mSynchLimitingValueGUI.isEnabled() && mSynchLimitingValueGUI.isSelected());
		mSynchLimitingValueDirectionGUI.setEnabled(mSynchLimitingValueSetterGUI.isEnabled());
		mSynchIgnoreFirstXSecondsSetterGUI.setEnabled(mSynchLimitingValueGUI.isEnabled() && mSynchNoneGUI.isSelected() == false);
//		mSplitLargeFilesGUI.setEnabled(mDataSet.getDataPointCount() == 0);
//		mLargeFileCountsPerSplitGUI.setEnabled(mSplitLargeFilesGUI.isEnabled() && mSplitLargeFilesGUI.isSelected());
		mLargeFileCountsPerSplitGUI.setEnabled(mSplitLargeFilesGUI.isSelected());
		mPSDWindowGUI.setEnabled(mPSDTypeGUI.getSelectedItem() == BackEndAPI.PSD_WELCH.getIntIndex());
		mPSDWelchWindowOverlapGUI.setEnabled(mPSDTypeGUI.getSelectedItem() == BackEndAPI.PSD_WELCH.getIntIndex());
		mWaveletTypeGUI.setEnabled(mWaveletTransformTypeGUI.getSelectedItem() == BackEndAPI.WTT_FWT.getIntIndex());
		mOkButton.setEnabled((mIsDialog || mDataFieldChanged.changeHasOccured()) && componentValuesValid());
//		mDespikingFilterReference.setVisible(mTabbedPane.getSelectedIndex() == FILTERING_CONFIG_TAB_INDEX);
	}

	/**
	 * Build the GUI components for this dialog
	 */
	private void buildGUI() {
		setLayout(new GridBagLayout());
		
		mTabbedPane = new JTabbedPane();
		mTabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				setDespikingFilterReferenceText();
			}
		});
		JPanel generalConfig = new JPanel(new GridBagLayout());
		JPanel dataSetConfig = new JPanel(new GridBagLayout());
//		JPanel dataPointConfig = new JPanel(new GridBagLayout());
		JPanel probeConfig = new JPanel(new GridBagLayout());
		JPanel filteringConfig = new JPanel(new GridBagLayout());
		JPanel largeFilesConfig = new JPanel(new GridBagLayout());

		mOkButton = new MAJFCDialogButton(mIsDialog ? DAStrings.getString(DAStrings.OK) : DAStrings.getString(DAStrings.APPLY), this);
		mCancelButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.CANCEL), this);
		mCancelButton.setPreferredSize(new Dimension(75, 25));
		mCancelButton.setVisible(mIsDialog);
		
		DataSetConfig defaultConfigValues = mIsDialog ? DAFrame.getFrame().getDefaultDataSetConfiguration() : DAFrame.getFrame().getCurrentDataSetConfig();
		mLeftBankPositionGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.LEFT_BANK_POSITION_LABEL), -100000, 100000, defaultConfigValues.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION), 0, this);
		mRightBankPositionGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.RIGHT_BANK_POSITION_LABEL), -100000, 100000, defaultConfigValues.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION), 0, this);
		String boundaryDefFilename = defaultConfigValues.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME);
		boundaryDefFilename = boundaryDefFilename == null ? DAStrings.getString(DAStrings.SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL) : boundaryDefFilename;
		mBoundaryDefinitionFilenameGUI = new MAJFCAbstractFileOrDirChooserButton(boundaryDefFilename, this) {
			@Override
			public void callback(File file) {
				mBoundaryDefinitionFilenameGUI.setFileOrDir(file.getAbsolutePath());
				mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME, true);
				
				childChanged();
			}
		};

		mWaterDepthGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.WATER_DEPTH_POSITION_LABEL), 0, 100000, defaultConfigValues.get(BackEndAPI.DSC_KEY_WATER_DEPTH), 0, this);
		mMeasuredDischargeGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.MEASURED_DISCHARGE_LABEL), 0, 1000, defaultConfigValues.get(BackEndAPI.DSC_KEY_MEASURED_DISCHARGE), 4, this);
		mBedSlopeGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.BED_SLOPE_LABEL), 0, 1, defaultConfigValues.get(BackEndAPI.DSC_KEY_BED_SLOPE), 4, this);
		boolean useBinaryFileFormat = defaultConfigValues.get(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT).equals(DADefinitions.USING_BINARY_FILE_FORMAT);
		mUseBinaryFileFormat = new JCheckBox(DAStrings.getString(DAStrings.USE_BINARY_FILE_FORMAT_LABEL));
		mUseBinaryFileFormat.addItemListener(this);
		
		mLengthUnitsGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.DATA_SET_LENGTH_UNITS_LABEL), DAStrings.getString(DAStrings.DATA_SET_LENGTH_UNIT_OPTIONS), defaultConfigValues.get(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR).intValue());
		mLengthUnitsGUI.addActionListener(this);
		mVelocityUnitsGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.DATA_FILE_VELOCITY_UNITS_LABEL), DAStrings.getString(DAStrings.DATA_FILE_VELOCITY_UNIT_OPTIONS), defaultConfigValues.get(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR).intValue());
		mVelocityUnitsGUI.addActionListener(this);
		
		mCSVDataFileDelimiter = new MAJFCTranslatingDropDownPanel<Character>(DAStrings.getString(DAStrings.DATA_FILE_DELIMITER_LABEL), DAStrings.getString(DAStrings.DATA_FILE_DELIMITER_OPTIONS), defaultConfigValues.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER).charAt(0));
		mCSVDataFileDelimiter.addActionListener(this);
		
		mCSVFileDecimalSeparator = new MAJFCTranslatingDropDownPanel<Character>(DAStrings.getString(DAStrings.DATA_FILE_CSV_DECIMAL_SEPARATOR_LABEL), DAStrings.getString(DAStrings.DATA_FILE_CSV_DECIMAL_SEPARATOR_OPTIONS), defaultConfigValues.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR).charAt(0));
		mCSVFileDecimalSeparator.addActionListener(this);
		
		mDefaultVSADataFilePathLabel = new JLabel(DAStrings.getString(DAStrings.DEFAULT_DATA_FILE_PATH_LABEL));
		mDefaultVSADataFilePath = new MAJFCAbstractFileOrDirChooserButton(defaultConfigValues.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY));
		mDefaultVSADataFilePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String oldDir = mDefaultVSADataFilePath.getFileOrDir();
				DATools.chooseDirectory(DAStrings.getString(DAStrings.SELECT_DATA_FILE_PATH_DIALOG_TITLE), oldDir, mDefaultVSADataFilePath);
				
				if (oldDir.equals(mDefaultVSADataFilePath.getFileOrDir()) == false) {
					mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY, true);
					setGUIStates();
				}
			}
		});
		
		mDefaultRawDataFilePathLabel = new JLabel(DAStrings.getString(DAStrings.DEFAULT_CSV_FILE_PATH_LABEL));
		mDefaultRawDataFilePath = new MAJFCAbstractFileOrDirChooserButton(defaultConfigValues.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY));
		mDefaultRawDataFilePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String oldDir = mDefaultRawDataFilePath.getFileOrDir();
				DATools.chooseDirectory(DAStrings.getString(DAStrings.SELECT_CSV_FILE_PATH_DIALOG_TITLE), oldDir, mDefaultRawDataFilePath);
				
				if (oldDir.equals(mDefaultRawDataFilePath.getFileOrDir()) == false) {
					mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY, true);
					setGUIStates();
				}
			}
		});
		
		mDefaultFileExportFilePathLabel = new JLabel(DAStrings.getString(DAStrings.DEFAULT_EXPORT_FILE_PATH_LABEL));
		mDefaultFileExportFilePath = new MAJFCAbstractFileOrDirChooserButton(defaultConfigValues.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY));
		mDefaultFileExportFilePath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String oldDir = mDefaultFileExportFilePath.getFileOrDir();
				DATools.chooseDirectory(DAStrings.getString(DAStrings.SELECT_EXPORT_FILE_PATH_DIALOG_TITLE), oldDir, mDefaultFileExportFilePath);
				
				if (oldDir.equals(mDefaultFileExportFilePath.getFileOrDir()) == false) {
					mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY, true);
					setGUIStates();
				}
			}
		});

		mUseDataFilePathForAll = new JButton(DAStrings.getString(DAStrings.USE_DATA_FILE_PATH_FOR_ALL_LABEL));
		mUseDataFilePathForAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
 				String oldDir1 = mDefaultRawDataFilePath.getFileOrDir();
				String oldDir2 = mDefaultFileExportFilePath.getFileOrDir();
				String newDir = mDefaultVSADataFilePath.getFileOrDir();
				
				if (oldDir1.equals(newDir) == false || oldDir2.equals(newDir) == false) {
					mDefaultRawDataFilePath.setFileOrDir(newDir);
					mDefaultFileExportFilePath.setFileOrDir(newDir);
					
					mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY, true);
					setGUIStates();
				}
			}
		});
		
		// Filtering
		mPreFilterTypeGUI  = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.PRE_FILTER_TYPE_LABEL), getPreFilterOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE).intValue());
		mPreFilterTypeGUI.addActionListener(this);
		
		mDespikingFilterTypeGUI  = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.DESPIKING_FILTER_TYPE_LABEL), getDespikingFilterOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE).intValue());
		mDespikingFilterTypeGUI.addActionListener(this);
		
		mDespikingFilterReference = new JLabel(" ");
		mExcludeLevelGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.EXCLUDE_LEVEL_LABEL), 0, 5, defaultConfigValues.get(BackEndAPI.DSC_KEY_EXCLUDE_LEVEL), 1, this);
		mInvalidValueReplacementMethodGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.PST_REPLACEMENT_METHOD_LABEL), getPSTReplacementMethodOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD).intValue());
		mInvalidValueReplacementMethodGUI.addActionListener(this);
		mInvalidValueReplacementPolynomialOrderGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.PST_REPLACEMENT_POLYNOMIAL_ORDER_LABEL), 0, 5, defaultConfigValues.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER).intValue(), 0, this);
		
		mCSTypeGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.CS_TYPE_LABEL), getCSTypeOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_CS_TYPE).intValue());
		mCSTypeGUI.addActionListener(this);
		
		mModifiedPSTAutoSafeLevelC1GUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.MODIFIED_PST_AUTO_SAFE_LEVEL_C1_LABEL), 0, 5, defaultConfigValues.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1), 3, this);
		mModifiedPSTAutoExcludeLevelC2GUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_LABEL), 0, 5, defaultConfigValues.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2), 3, this);
		mSamplingRateGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.SAMPLING_RATE_LABEL), 0, 10000, defaultConfigValues.get(BackEndAPI.DSC_KEY_SAMPLING_RATE), 0, this);
		mLimitingCorrelationGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.LIMITING_CORRELATION_LABEL), 0, 100, defaultConfigValues.get(BackEndAPI.DSC_KEY_LIMITING_CORRELATION), 0, this);
		mLimitingSNRGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.LIMITING_SNR_LABEL), 0, 100, defaultConfigValues.get(BackEndAPI.DSC_KEY_LIMITING_SNR), 0, this);
		boolean usePercentageForCorrAndSNR = defaultConfigValues.get(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER).equals(DADefinitions.USING_BINARY_FILE_FORMAT);
		mUsePercentageForCorrAndSNRFilter = new MAJFCCheckBox(DAStrings.getString(DAStrings.USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_LABEL));
		mUsePercentageForCorrAndSNRFilter.addItemListener(this);
		
		mLimitingWDiffGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.LIMITING_W_DIFF_LABEL), 0, 100, defaultConfigValues.get(BackEndAPI.DSC_KEY_LIMITING_W_DIFF), 0, this);
		mMovingAverageWindowSizeGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.MOVING_AVERAGE_WINDOW_SIZE_LABEL), 0, 5000, defaultConfigValues.get(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE), 0, this);
		mStandardCellWidthGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.STANDARD_CELL_WIDTH_LABEL), 0, 100, defaultConfigValues.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_WIDTH), 10, this);
		mStandardCellHeightGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.STANDARD_CELL_HEIGHT_LABEL), 0, 100, defaultConfigValues.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT), 10, this);

		// Multi-Run Synchronisation
		JPanel multiRunAndATConfig = new JPanel(new GridBagLayout());
		mMultiRunSynchTypeSelectionGroup = new ButtonGroup();
		mMultiRunSynchDefaultsLabel = new JLabel(DAStrings.getString(DAStrings.MULTI_RUN_SYNCH_LABEL));
		mSynchNoneGUI = new JRadioButton(DAStrings.getString(DAStrings.SYNCH_NONE_LABEL));
		mSynchNoneGUI.addItemListener(this);
		mMultiRunSynchTypeSelectionGroup.add(mSynchNoneGUI);
		mSynchMaxValueGUI = new JRadioButton(DAStrings.getString(DAStrings.SYNCH_MAX_LABEL));
		mSynchMaxValueGUI.addItemListener(this);
		mMultiRunSynchTypeSelectionGroup.add(mSynchMaxValueGUI);
		mSynchLimitingValueGUI = new JRadioButton(DAStrings.getString(DAStrings.SYNCH_LIMITING_VALUE_LABEL));
		mSynchLimitingValueGUI.addItemListener(this);
		mMultiRunSynchTypeSelectionGroup.add(mSynchLimitingValueGUI);
		
		Double limitingValue = defaultConfigValues.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE);
		Double ignoreFirstXSecondsValue = defaultConfigValues.get(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS);
		boolean synchronise = limitingValue.doubleValue() != -1;
		boolean usingLimitingValue = synchronise && limitingValue.equals(Double.NaN) == false;
		mSynchLimitingValueSetterGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.SYNCH_LIMITING_VALUE_SETTER_LABEL), -5000, 5000, usingLimitingValue ? limitingValue : 0, 4, this);
		mSynchIgnoreFirstXSecondsSetterGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.SYNCH_IGNORE_FIRST_X_SECONDS_SETTER_LABEL), -5000, 5000, usingLimitingValue ? ignoreFirstXSecondsValue : 0, 0, this);
		mSynchLimitingValueDirectionGUI = new MAJFCTranslatingDropDownPanel<Double>(DAStrings.getString(DAStrings.SYNCH_LIMITING_VALUE_DIRECTION_LABEL), DAStrings.getString(DAStrings.SYNCH_LIMITING_VALUE_DIRECTION_OPTIONS), defaultConfigValues.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION));
		mSynchLimitingValueDirectionGUI.addActionListener(this);
		
		Double trimStartBy = defaultConfigValues.get(BackEndAPI.DSC_KEY_AT_TRIM_START_BY);
		Double priorLength = defaultConfigValues.get(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH);
		Double sampleLength = defaultConfigValues.get(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH);
		boolean usingAT = priorLength.equals(Double.NaN) == false;
		boolean trimStart = trimStartBy.equals(Double.NaN) == false && trimStartBy > 0;
		mAutoTrimGUI = new JCheckBox(DAStrings.getString(DAStrings.AT_PRIOR_LENGTH_CHECKBOX_LABEL));
		mAutoTrimGUI.addItemListener(this);
		mATTrimStartBySetterGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.AT_TRIM_START_BY_SETTER_LABEL), 0, 5000, usingAT && trimStart ? trimStartBy : 0, 1, this);
		mATPriorLengthSetterGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.AT_PRIOR_LENGTH_SETTER_LABEL), 0, 5000, usingAT ? priorLength : 0, 1, this);
		mATSampleLengthSetterGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.AT_SAMPLE_LENGTH_SETTER_LABEL), 0, 500000, usingAT ? sampleLength : 0, 0, this);

		Double largeFileMeasPerSplit = defaultConfigValues.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
		boolean splitLargeFiles = largeFileMeasPerSplit.equals(Double.NaN) == false;
		mSplitLargeFilesGUI = new JCheckBox(DAStrings.getString(DAStrings.SPLIT_LARGE_FILES_LABEL));
		mSplitLargeFilesGUI.addItemListener(this);
		mLargeFileCountsPerSplitGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.LARGE_FILES_NUMBER_OF_MEASUREMENTS_PER_SPLIT), 1, DADefinitions.LARGE_FILES_MAX_MEAS_PER_SPLIT, splitLargeFiles ? largeFileMeasPerSplit : 50000, 0, this);

		// Spectral analysis
		JPanel spectralAnalysisConfig = new JPanel(new GridBagLayout());
		mPSDTypeGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.PSD_TYPE_LABEL), getPSDTypeOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_PSD_TYPE).intValue());
		mPSDTypeGUI.addActionListener(this);
		mPSDWindowGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.PSD_WINDOW_LABEL), getPSDWindowOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_PSD_TYPE).intValue());
		mPSDWindowGUI.addActionListener(this);
		mBartlettWindowsGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.NUMBER_OF_BARTLETT_WINDOWS_LABEL), 1, 50, defaultConfigValues.get(BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS), 0, this);
		mPSDWelchWindowOverlapGUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.PSD_WELCH_WINDOW_OVERLAP_LABEL), 1, 100, defaultConfigValues.get(BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP), 0, this);
		mWaveletTransformTypeGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_TYPE_LABEL), getWaveletTransformTypeOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE).intValue());
		mWaveletTransformTypeGUI.addActionListener(this);
		mWaveletTypeGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.WAVELET_TYPE_LABEL), getWaveletTypeOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_WAVELET_TYPE).intValue());
		mWaveletTypeGUI.addActionListener(this);
//		mWaveletTransformTypeGUI = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_TYPE_LABEL), getWaveletTransformTypeOptions(), defaultConfigValues.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE).intValue());
//		mWaveletTransformTypeGUI.addActionListener(this);
		mWaveletTransformScaleByInstPower = new JCheckBox(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_SCALE_BY_INST_POWER_LABEL));
		mWaveletTransformScaleByInstPower.addItemListener(this);

		mInvertXAxis = new MAJFCCheckBox(DAStrings.getString(DAStrings.INVERT_X_AXIS_LABEL));
		mInvertYAxis = new MAJFCCheckBox(DAStrings.getString(DAStrings.INVERT_Y_AXIS_LABEL));
		mInvertZAxis = new MAJFCCheckBox(DAStrings.getString(DAStrings.INVERT_Z_AXIS_LABEL));
		mInvertXAxis.addItemListener(this);
		mInvertYAxis.addItemListener(this);
		mInvertZAxis.addItemListener(this);
		
		mLockDataSet = new MAJFCCheckBox(DAStrings.getString(DAStrings.LOCK_DATA_SET_LABEL));
		mLockDataSet.addItemListener(this);

		// Do this here otherwise setGUIStates gets called before all components have been instantiated
		mInvertXAxis.setSelected(defaultConfigValues.get(BackEndAPI.DSC_KEY_X_AXIS_INVERTED) != 0);
		mInvertYAxis.setSelected(defaultConfigValues.get(BackEndAPI.DSC_KEY_Y_AXIS_INVERTED) != 0);
		mInvertZAxis.setSelected(defaultConfigValues.get(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED) != 0);
		mSynchNoneGUI.setSelected(!synchronise);
		mSynchLimitingValueGUI.setSelected(synchronise && usingLimitingValue);
		mSynchMaxValueGUI.setSelected(synchronise && !usingLimitingValue);
		mAutoTrimGUI.setSelected(usingAT);
		mSplitLargeFilesGUI.setSelected(splitLargeFiles);
		mUseBinaryFileFormat.setSelected(useBinaryFileFormat);
		mUsePercentageForCorrAndSNRFilter.setSelected(usePercentageForCorrAndSNR);
		mWaveletTransformScaleByInstPower.setSelected(defaultConfigValues.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER) != 0);

		mCSVFileFormatLabel = new JLabel(DAStrings.getString(DAStrings.CSV_FILE_FORMAT));
		String theCSVFileFormat = defaultConfigValues.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT);
		mCSVFileFormatGUI = new JTextField(theCSVFileFormat == null || theCSVFileFormat.length() == 0 ? DAStrings.getString(DAStrings.ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL) : theCSVFileFormat);
		mCSVFileFormatGUI.getDocument().addDocumentListener(this);
		
		mProbeSetup = new ProbeSetupPanel(this);

		int y = 0;
		int x = 0;
		Vector<MAJFCPanel> generalConfigPanels = new Vector<MAJFCPanel>();
		generalConfigPanels.add(new MAJFCPanel(new GridBagLayout()));
		generalConfigPanels.lastElement().add(mCSVDataFileDelimiter, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		generalConfigPanels.lastElement().add(mCSVFileDecimalSeparator, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));

		x = 0;
		y = 0;
		generalConfigPanels.add(new MAJFCPanel(new GridBagLayout()));
		generalConfigPanels.lastElement().add(mCSVFileFormatLabel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		generalConfigPanels.lastElement().add(mCSVFileFormatGUI, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		y = 0;
		generalConfigPanels.add(new MAJFCPanel(new GridBagLayout()));
		generalConfigPanels.lastElement().add(mDefaultVSADataFilePathLabel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 0, 5, 0, 0, 3, 3));
		generalConfigPanels.lastElement().add(mDefaultVSADataFilePath, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		generalConfigPanels.lastElement().add(mDefaultRawDataFilePathLabel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 0, 5, 0, 0, 3, 3));
		generalConfigPanels.lastElement().add(mDefaultRawDataFilePath, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		generalConfigPanels.lastElement().add(mDefaultFileExportFilePathLabel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 0, 5, 0, 0, 3, 3));
		generalConfigPanels.lastElement().add(mDefaultFileExportFilePath, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));

		x = 0;
		y = 0;
		generalConfigPanels.add(new MAJFCPanel(new GridBagLayout()));
		generalConfigPanels.lastElement().add(mUseDataFilePathForAll, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));

		x = 0;
		y = 0;
		int numberOfGCPanels = generalConfigPanels.size();
		for (int i = 0; i < numberOfGCPanels; ++i) {
			generalConfig.add(generalConfigPanels.elementAt(i), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		}
		
		MAJFCPanel dataSetConfigTopPanel = new MAJFCPanel(new GridBagLayout());
		x = 0;
		y = 0;
		dataSetConfigTopPanel.add(mVelocityUnitsGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		dataSetConfigTopPanel.add(mLengthUnitsGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		dataSetConfigTopPanel.add(mLeftBankPositionGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		dataSetConfigTopPanel.add(mRightBankPositionGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		dataSetConfigTopPanel.add(mWaterDepthGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		dataSetConfigTopPanel.add(mMeasuredDischargeGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		dataSetConfigTopPanel.add(mBedSlopeGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		dataSetConfigTopPanel.add(mBoundaryDefinitionFilenameGUI, MAJFCTools.createGridBagConstraint(x, y++, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		dataSetConfigTopPanel.add(mUseBinaryFileFormat, MAJFCTools.createGridBagConstraint(x, y++, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		MAJFCPanel invertPanel = new MAJFCPanel(new GridBagLayout());
		invertPanel.add(mInvertXAxis, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 0, 0, 0, 0));
		invertPanel.add(mInvertYAxis, MAJFCTools.createGridBagConstraint(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 0, 0, 0, 0));
		invertPanel.add(mInvertZAxis, MAJFCTools.createGridBagConstraint(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 0, 0, 0, 0));
		invertPanel.add(new JLabel(DAStrings.getString(DAStrings.INVERT_AXES_TEXT)), MAJFCTools.createGridBagConstraint(0, 1, 3, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 0, 0, 0, 0));
		x = 0;
		y = 0;
		dataSetConfig.add(dataSetConfigTopPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 0, 0, 0, 0));
		dataSetConfig.add(invertPanel, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 0, 0, 0, 0));

		x = 0;
		y = 0;
		probeConfig.add(mProbeSetup, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		y = 0;
		filteringConfig.add(mPreFilterTypeGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		filteringConfig.add(mDespikingFilterTypeGUI, MAJFCTools.createGridBagConstraint(x--, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0;
		filteringConfig.add(mInvalidValueReplacementMethodGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		filteringConfig.add(mInvalidValueReplacementPolynomialOrderGUI, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0;
		filteringConfig.add(mCSTypeGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0;
		filteringConfig.add(mModifiedPSTAutoSafeLevelC1GUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		filteringConfig.add(mModifiedPSTAutoExcludeLevelC2GUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x= 0;
		filteringConfig.add(mLimitingCorrelationGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		filteringConfig.add(mLimitingSNRGUI, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0;
		filteringConfig.add(mUsePercentageForCorrAndSNRFilter, MAJFCTools.createGridBagConstraint(x, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x= 0;
		filteringConfig.add(mExcludeLevelGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		filteringConfig.add(mLimitingWDiffGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x= 0;
		filteringConfig.add(mMovingAverageWindowSizeGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		filteringConfig.add(mSamplingRateGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0; y = 0;
		MAJFCPanel multiRunPanel = new MAJFCPanel(new GridBagLayout());
		multiRunPanel.add(mMultiRunSynchDefaultsLabel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		multiRunPanel.add(mSynchNoneGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		multiRunPanel.add(mSynchMaxValueGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		multiRunPanel.add(mSynchLimitingValueGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		multiRunPanel.add(mSynchLimitingValueSetterGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		multiRunPanel.add(mSynchLimitingValueDirectionGUI, MAJFCTools.createGridBagConstraint(x++, y--, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		multiRunPanel.add(mSynchIgnoreFirstXSecondsSetterGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0; y = 0;
		MAJFCPanel autoTrimPanel = new MAJFCPanel(new GridBagLayout());
		autoTrimPanel.add(mAutoTrimGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		autoTrimPanel.add(mATTrimStartBySetterGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		autoTrimPanel.add(mATPriorLengthSetterGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		autoTrimPanel.add(mATSampleLengthSetterGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0; y = 0;
		multiRunAndATConfig.add(multiRunPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		multiRunAndATConfig.add(autoTrimPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0; y = 0;
		largeFilesConfig.add(mSplitLargeFilesGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		largeFilesConfig.add(mLargeFileCountsPerSplitGUI, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0; y = 0;
		spectralAnalysisConfig.add(mWaveletTransformTypeGUI, MAJFCTools.createGridBagConstraint(x++, y, 2, 1, 0.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x++;
		spectralAnalysisConfig.add(mWaveletTypeGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		spectralAnalysisConfig.add(mWaveletTransformScaleByInstPower, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0;
		spectralAnalysisConfig.add(mPSDTypeGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		spectralAnalysisConfig.add(mBartlettWindowsGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		spectralAnalysisConfig.add(mPSDWindowGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		spectralAnalysisConfig.add(mPSDWelchWindowOverlapGUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		buttonPanel.add(mOkButton, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		buttonPanel.add(mCancelButton, MAJFCTools.createGridBagConstraint(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		
//		if (mIsDialog) {
			mTabbedPane.add(DAStrings.getString(DAStrings.GENERAL_CONFIG_TAB_LABEL), generalConfig);
			mTabbedPane.add(DAStrings.getString(DAStrings.DATA_SET_CONFIG_TAB_LABEL), dataSetConfig);
//			mTabbedPane.add(DAStrings.getString(DAStrings.DATA_POINT_CONFIG_TAB_LABEL), dataPointConfig);
			mTabbedPane.add(DAStrings.getString(DAStrings.PROBE_SETUP_CONFIG_TAB_LABEL), probeConfig);
			mTabbedPane.add(DAStrings.getString(DAStrings.FILTERING_CONFIG_TAB_LABEL), filteringConfig);
			mTabbedPane.add(DAStrings.getString(DAStrings.MULTI_RUN_CONFIG_TAB_LABEL), multiRunAndATConfig);
			mTabbedPane.add(DAStrings.getString(DAStrings.LARGE_FILES_AND_MISC_CONFIG_TAB_LABEL), largeFilesConfig);
			mTabbedPane.add(DAStrings.getString(DAStrings.SPECTRAL_ANALYSIS_CONFIG_TAB_LABEL), spectralAnalysisConfig);

//		} else {
//			x = 0;
//			y = 0;
//			add(dataSetConfig, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
//			add(filteringConfig, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
//			add(multiRunConfig, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
//		}
	
		x = 0;
		y = 0;
		add(mTabbedPane, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		add(mDespikingFilterReference, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		add(buttonPanel, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
	}	

	/**
	 * Sets the configuration data fields
	 * @param configData The new configuration data
	 * @return an object showing which config data has changed
	 */
	public void setConfigData(DataSetConfig configData) {
		mExcludeLevelGUI.setValue(configData.get(BackEndAPI.DSC_KEY_EXCLUDE_LEVEL));
		mModifiedPSTAutoSafeLevelC1GUI.setValue(configData.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1));
		mModifiedPSTAutoExcludeLevelC2GUI.setValue(configData.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2));
		mInvalidValueReplacementMethodGUI.setSelectedItem(configData.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD).intValue());
		mInvalidValueReplacementPolynomialOrderGUI.setValue(configData.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER));
		mCSTypeGUI.setSelectedItem(configData.get(BackEndAPI.DSC_KEY_CS_TYPE).intValue());
		mSamplingRateGUI.setValue(configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE));
		mLimitingCorrelationGUI.setValue(configData.get(BackEndAPI.DSC_KEY_LIMITING_CORRELATION));
		mLimitingSNRGUI.setValue(configData.get(BackEndAPI.DSC_KEY_LIMITING_SNR));
		boolean usePercentageForCorrAndSNRFilter = configData.get(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER).equals(DADefinitions.USING_PERCENTAGE_FOR_CORR_AND_SNR_FILTER);
		mUsePercentageForCorrAndSNRFilter.setSelectedReportChange(usePercentageForCorrAndSNRFilter);
		mLimitingWDiffGUI.setValue(configData.get(BackEndAPI.DSC_KEY_LIMITING_W_DIFF));
		mMovingAverageWindowSizeGUI.setValue(configData.get(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE));
		mLeftBankPositionGUI.setValue(configData.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION));
		mRightBankPositionGUI.setValue(configData.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION));
		mWaterDepthGUI.setValue(configData.get(BackEndAPI.DSC_KEY_WATER_DEPTH));
		mLengthUnitsGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR).intValue()));
		mVelocityUnitsGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR).intValue()));
		mMeasuredDischargeGUI.setValue(configData.get(BackEndAPI.DSC_KEY_MEASURED_DISCHARGE));
		mBedSlopeGUI.setValue(configData.get(BackEndAPI.DSC_KEY_BED_SLOPE));
		mInvertXAxis.setSelectedReportChange(configData.get(BackEndAPI.DSC_KEY_X_AXIS_INVERTED) == 0 ? false : true);
		mInvertYAxis.setSelectedReportChange(configData.get(BackEndAPI.DSC_KEY_Y_AXIS_INVERTED) == 0 ? false : true);
		mInvertZAxis.setSelectedReportChange(configData.get(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED) == 0 ? false : true);
		mProbeSetup.fixNumberOfProbes(configData.get(BackEndAPI.DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET).intValue());
		
		mLockDataSet.setSelectedReportChange(configData.get(BackEndAPI.DSC_KEY_DATA_SET_LOCKED) == 0 ? false : true);
		mPreFilterTypeGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE).intValue()));
		mDespikingFilterTypeGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE).intValue()));
		mStandardCellWidthGUI.setValue(configData.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_WIDTH));
		mStandardCellHeightGUI.setValue(configData.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT));
		
		Double limitingValue = configData.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE);
		Double ignoreFirstXSecondsValue = configData.get(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS);
		boolean synchronise = limitingValue.doubleValue() != -1;
		boolean usingLimitingValue = synchronise == true && limitingValue.equals(Double.NaN) == false;
		mSynchNoneGUI.setSelected(!synchronise);
		mSynchLimitingValueGUI.setSelected(synchronise && usingLimitingValue);
		mSynchMaxValueGUI.setSelected(synchronise && !usingLimitingValue);
		mSynchIgnoreFirstXSecondsSetterGUI.setValue(ignoreFirstXSecondsValue);
		mSynchLimitingValueSetterGUI.setValue(usingLimitingValue ? limitingValue : 0);
		mSynchLimitingValueDirectionGUI.setSelectedItem(configData.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION));
		
		Double trimStartBy = configData.get(BackEndAPI.DSC_KEY_AT_TRIM_START_BY);
		Double priorLength = configData.get(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH);
		Double sampleLength = configData.get(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH);
		boolean usingAT = priorLength.equals(Double.NaN) == false;
		boolean trimStart = trimStartBy.equals(Double.NaN) == false && trimStartBy > 0;
		mAutoTrimGUI.setSelected(usingAT);
		mATTrimStartBySetterGUI.setValue(usingAT && trimStart ? trimStartBy : 0);
		mATPriorLengthSetterGUI.setValue(usingAT ? priorLength : 0);
		mATSampleLengthSetterGUI.setValue(usingAT ? sampleLength : 0);
		
		Double largeFileMeasPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
		boolean splitLargeFiles = largeFileMeasPerSplit.equals(Double.NaN) == false;
		mSplitLargeFilesGUI.setSelected(splitLargeFiles);
		mLargeFileCountsPerSplitGUI.setValue(splitLargeFiles ? largeFileMeasPerSplit : 20000);
		
		boolean useBinaryFileFormat = configData.get(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT).equals(DADefinitions.USING_BINARY_FILE_FORMAT);
		mUseBinaryFileFormat.setSelected(useBinaryFileFormat);
		
		mPSDTypeGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_PSD_TYPE).intValue()));
		mPSDWindowGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_PSD_WINDOW).intValue()));
		mBartlettWindowsGUI.setValue(configData.get(BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS));
		mPSDWelchWindowOverlapGUI.setValue(configData.get(BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP));
		mWaveletTypeGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_WAVELET_TYPE).intValue()));
		mWaveletTransformTypeGUI.setSelectedItem(new Integer(configData.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE).intValue()));
		mWaveletTransformScaleByInstPower.setSelected(configData.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER) == 1d);
		
		String boundaryDefinitionFilename = configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME);
		mBoundaryDefinitionFilenameGUI.setFileOrDir(boundaryDefinitionFilename == null ? DAStrings.getString(DAStrings.SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL) : boundaryDefinitionFilename);

		String theCSVFileFormat = configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT);
		mCSVFileFormatGUI.setText(theCSVFileFormat == null || theCSVFileFormat.length() == 0 ? DAStrings.getString(DAStrings.ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL) : theCSVFileFormat);
		
		mCSVDataFileDelimiter.setSelectedItem(configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER).charAt(0));
		mCSVFileDecimalSeparator.setSelectedItem(configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR).charAt(0));
		
		mDefaultRawDataFilePath.setFileOrDir(configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY));
		mDefaultVSADataFilePath.setFileOrDir(configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY));
		mDefaultFileExportFilePath.setFileOrDir(configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY));
	}

	/**
	 * ItemListener implementation
	 * @param theEvent
	 */
	@Override
	public void itemStateChanged(ItemEvent theEvent) {
		Object source = theEvent.getSource();
		
		if (source == mInvertXAxis) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_X_AXIS_INVERTED, true);
		} else if (source == mInvertYAxis) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED, true);
		} else if (source == mInvertZAxis) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED, true);
		} else if (source == mLockDataSet) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_DATA_SET_LOCKED, true);
		} else if (source == mSynchNoneGUI || source == mSynchLimitingValueGUI || source == mSynchMaxValueGUI) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE, true);
		} else if (source == mMovingAverageWindowSizeGUI) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE, true);
		} else if (source == mAutoTrimGUI) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_AT_TRIM_START_BY, true);
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH, true);
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH, true);
		} else if (source == mSplitLargeFilesGUI) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT, true);
		} else if (source == mUseBinaryFileFormat) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT, true);
		} else if (source == mUsePercentageForCorrAndSNRFilter) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER, true);
		} else if (source == mWaveletTransformScaleByInstPower) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER, true);
		}
				
		childChanged();
		setGUIStates();
	}
	
	/**
	 * ActionListener implementation
	 */
	@Override
	public void actionPerformed(ActionEvent theEvent) {
		Object source = theEvent.getSource();
		
		if (source.equals(mBoundaryDefinitionFilenameGUI)) {
			DATools.chooseFile(DAStrings.getString(DAStrings.SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL), mBoundaryDefinitionFilenameGUI.getFileOrDir(), mBoundaryDefinitionFilenameGUI, new FileNameExtensionFilter(DAStrings.getString(DAStrings.BOUNDARY_DEFINITION_FILE_FILTER_TEXT), DADefinitions.FILE_EXTENSION_BOUNDARY_DEFINITION));
			return;
		} else if (source.equals(mOkButton)){
			if (componentValuesValid() == false){
				return;
			}
			
			if (mIsDialog) {
				DAFrame.getFrame().setDefaultDataSetConfiguration(getConfigData());
				
				MAJFCLogger.log("writing to config file", 25);
				DAFrame.getFrame().writeToConfigFile();
				mParentDialog.setVisible(false);
			} else {
				try {
					BackEndAPI.getBackEndAPI().setConfigData(mDataSetId, getConfigData());
					new RecalculateSummaryDataTask();
				} catch (BackEndAPIException theException) {
					MAJFCLogger.log(theException.getMessage());
					theException.printStackTrace();
				}
				
				mParentDataSetDisplay.configChanged();
				resetDataFieldChangedFlags();
			}
		} else if (source.equals(mCancelButton)) {
			mParentDialog.setVisible(false);
		} else if (mLengthUnitsGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR, true);
		} else if (mVelocityUnitsGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR, true);
		} else if (mInvalidValueReplacementMethodGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD, true);
		} else if (mInvalidValueReplacementPolynomialOrderGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER, true);
		} else if (mCSTypeGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_CS_TYPE, true);
		} else if (mPreFilterTypeGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE, true);
		} else if (mDespikingFilterTypeGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE, true);
			setDespikingFilterReferenceText();
		} else if (mSynchLimitingValueDirectionGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION, true);
		} else if (mPSDTypeGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_PSD_TYPE, true);
		} else if (mPSDWindowGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_PSD_WINDOW, true);
		} else if (mWaveletTypeGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_WAVELET_TYPE, true);
		} else if (mWaveletTransformTypeGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE, true);
		} else if (mCSVDataFileDelimiter.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER, true);
		} else if (mCSVFileDecimalSeparator.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR, true);
		}
		
		setGUIStates();
	}
	
	@Override
	/**
	 * PropertyChangeListener implementation
	 * @param theEvent The PropertyChangeEvent
	 */
	public void propertyChange(PropertyChangeEvent theEvent) {
		if (mLeftBankPositionGUI.isSource(theEvent) || mRightBankPositionGUI.isSource(theEvent)) {
			mProbeSetup.setLeftBankPos(mLeftBankPositionGUI.getValue());
			mProbeSetup.setRightBankPos(mRightBankPositionGUI.getValue());
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION, true);
		} else if (mWaterDepthGUI.isSource(theEvent)) {
			mProbeSetup.setWaterDepth(mRightBankPositionGUI.getValue());
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_WATER_DEPTH, true);
		} else if (mMeasuredDischargeGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_MEASURED_DISCHARGE, true);
		} else if (mModifiedPSTAutoSafeLevelC1GUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1, true);
		} else if (mModifiedPSTAutoExcludeLevelC2GUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2, true);
		} else if (mSamplingRateGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_SAMPLING_RATE, true);
		} else if (mPSDWelchWindowOverlapGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP, true);
		} else if (mBartlettWindowsGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS, true);
		} else if (mLargeFileCountsPerSplitGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT, true);
		} else if (mSynchLimitingValueSetterGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE, true);
		} else if (mSynchIgnoreFirstXSecondsSetterGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS, true);
		} else if (mATTrimStartBySetterGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_AT_TRIM_START_BY, true);
		} else if (mATPriorLengthSetterGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH, true);
		} else if (mATSampleLengthSetterGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH, true);
		} else if (mInvalidValueReplacementPolynomialOrderGUI.isSource(theEvent)) {
			mDataFieldChanged.set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER, true);
		}
		
		childChanged();
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) {
		mDataFieldChanged.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT, true);
		setGUIStates();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changedUpdate(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changedUpdate(e);
	}
	
	private void setDespikingFilterReferenceText() {
		if (mTabbedPane.getSelectedIndex() == FILTERING_CONFIG_TAB_INDEX) {
			mDespikingFilterReference.setText(mDespikingFilterReferences[mDespikingFilterTypeGUI.getSelectedItem()]);
		} else {
			mDespikingFilterReference.setText(" ");
		}
	}


	/**
	 * Gets the configuration data
	 * @return The value of the field
	 */
	public DataSetConfig getConfigData() {
		DataSetConfig configData = DAFrame.getFrame().getDefaultDataSetConfiguration();
		configData.set(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR, mLengthUnitsGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR, mVelocityUnitsGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_EXCLUDE_LEVEL, mExcludeLevelGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION, mLeftBankPositionGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION, mRightBankPositionGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_WATER_DEPTH, mWaterDepthGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_MEASURED_DISCHARGE, mMeasuredDischargeGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_X_AXIS_INVERTED, mInvertXAxis.isSelected() ? 1.0 : 0.0);
		configData.set(BackEndAPI.DSC_KEY_Y_AXIS_INVERTED, mInvertYAxis.isSelected() ? 1.0 : 0.0);
		configData.set(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED, mInvertZAxis.isSelected() ? 1.0 : 0.0);
		configData.set(BackEndAPI.DSC_KEY_BED_SLOPE, mBedSlopeGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_DATA_SET_LOCKED, mLockDataSet.isSelected() ? 1.0 : 0.0);
		configData.set(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE, mPreFilterTypeGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE, mDespikingFilterTypeGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1, mModifiedPSTAutoSafeLevelC1GUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2, mModifiedPSTAutoExcludeLevelC2GUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD, mInvalidValueReplacementMethodGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER, mInvalidValueReplacementPolynomialOrderGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_CS_TYPE, mCSTypeGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_SAMPLING_RATE, mSamplingRateGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_LIMITING_CORRELATION, mLimitingCorrelationGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_LIMITING_SNR, mLimitingSNRGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER, mUsePercentageForCorrAndSNRFilter.isSelected() ? DADefinitions.USING_PERCENTAGE_FOR_CORR_AND_SNR_FILTER : DADefinitions.USING_ABSOLUTE_VALUE_FOR_CORR_AND_SNR_FILTER);
		configData.set(BackEndAPI.DSC_KEY_LIMITING_W_DIFF, mLimitingWDiffGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE, mMovingAverageWindowSizeGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_DEFAULT_CELL_WIDTH, mStandardCellWidthGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT, mStandardCellHeightGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS, mSynchIgnoreFirstXSecondsSetterGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE, mSynchNoneGUI.isSelected() ? -1 : mSynchLimitingValueGUI.isSelected() ? mSynchLimitingValueSetterGUI.getValue() : Double.NaN);
		configData.set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION, mSynchLimitingValueDirectionGUI.getSelectedItem());
		configData.set(BackEndAPI.DSC_KEY_AT_TRIM_START_BY, mAutoTrimGUI.isSelected() ? mATTrimStartBySetterGUI.getValue() : Double.NaN);
		configData.set(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH, mAutoTrimGUI.isSelected() ? mATPriorLengthSetterGUI.getValue() : Double.NaN);
		configData.set(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH, mAutoTrimGUI.isSelected() ? mATSampleLengthSetterGUI.getValue() : Double.NaN);
		configData.set(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT, mSplitLargeFilesGUI.isSelected() ? mLargeFileCountsPerSplitGUI.getValue() : Double.NaN);
		configData.set(BackEndAPI.DSC_KEY_PSD_TYPE, mPSDTypeGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_PSD_WINDOW, mPSDWindowGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS, mBartlettWindowsGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP, mPSDWelchWindowOverlapGUI.getValue());
		configData.set(BackEndAPI.DSC_KEY_WAVELET_TYPE, mWaveletTypeGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE, mWaveletTransformTypeGUI.getSelectedItem().doubleValue());
		configData.set(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER, mWaveletTransformScaleByInstPower.isSelected() ? 1d : 0d);
		configData.set(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT, mUseBinaryFileFormat.isSelected() ? DADefinitions.USING_BINARY_FILE_FORMAT : DADefinitions.USING_XML_FILE_FORMAT);
		configData.set(BackEndAPI.DSC_KEY_MAIN_PROBE_INDEX, (double) mProbeSetup.getMainProbeIndex());
		configData.set(BackEndAPI.DSC_KEY_FIXED_PROBE_INDEX, (double) mProbeSetup.getFixedProbeIndex());
		configData.set(BackEndAPI.DSC_KEY_SYNCH_PROBE_INDEX, (double) mProbeSetup.getSynchProbeIndex());

		LinkedList<DataPointCoordinates> dpcs = mProbeSetup.getProbeCoords(0, 0);
		configData.set(BackEndAPI.DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET, (double) dpcs.size());
		configData.setCoords(dpcs);

		configData.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME, mBoundaryDefinitionFilenameGUI.getFileOrDir());
		configData.loadBoundaryCoords();
		// Do this as the bank positions may have been changed using the values in the boundary definition file.
		mLeftBankPositionGUI.setValue(configData.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION));
		mRightBankPositionGUI.setValue(configData.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION));
		
		configData.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT, mCSVFileFormatGUI.getText());
		configData.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER, mCSVDataFileDelimiter.getSelectedItem().toString());
		configData.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR, mCSVFileDecimalSeparator.getSelectedItem().toString());
		
		configData.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY, mDefaultVSADataFilePath.getFileOrDir());
		configData.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY, mDefaultRawDataFilePath.getFileOrDir());
		configData.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY, mDefaultFileExportFilePath.getFileOrDir());

		configData.calculateCrossSectionDimensions();
		
		return configData;
	}
	
	/**
	 * Helper class
	 */
	private class RecalculateSummaryDataTask extends DAProgressDialog {
		private RecalculateSummaryDataTask() {
			super(mDataSetId, DAFrame.getFrame(), DAStrings.getString(DAStrings.RECALCULATING_DATA_PROGRESS_TITLE));
			setVisible();
		}

		@Override
		protected Void doInBackground() throws Exception {
			BackEndAPI.getBackEndAPI().recalculateSummaryData(getDataSetId(), getDataFieldChangedFlags(), null, this);
			
			return null;
		}
	};

	/**
	 * Gets a set of flags (indexed by the BackEndAPI.DSC_KEY_... keys) indicating which configuration data has changed
	 * @return A set of flags
	 */
	private DataSetConfigChangedFlags getDataFieldChangedFlags() {
		DataSetConfigChangedFlags configChangedFlags = new DataSetConfigChangedFlags();
		mDataFieldChanged.copyInto(configChangedFlags);

		return configChangedFlags;
	}
	
	/**
	 * Resets all data field changed flags to false
	 */
	public void resetDataFieldChangedFlags() {
		mDataFieldChanged.reset();
		mOkButton.setEnabled(false);
	}
	
	/**
	 * Sets the data set id
	 * @param newDataSetId The new data set id
	 */
	public void setDataSetId(AbstractDataSetUniqueId newDataSetId) {
		mDataSetId = newDataSetId;
	}

	/**
	 * 
	 * @return The options string for CS Type Method drop-downs
	 */
	public static String getCSTypeOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DAStrings.getString(DAStrings.CS_TYPE_STANDARD_DEVIATION));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.CS_STANDARD_DEVIATION);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.CS_MEDIAN_ABSOLUTE_DEVIATION);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.CS_TYPE_MEAN_ABSOLUTE_DEVIATION));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.CS_MEAN_ABSOLUTE_DEVIATION);
		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}

	/**
	 * 
	 * @return The options string for PST Replacement Method drop-downs
	 */
	static String getPSTReplacementMethodOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
//		stringBuffer.append(DAStrings.getString(DAStrings.PST_REPLACEMENT_METHOD_NONE));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.PRM_NONE);
//		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PST_REPLACEMENT_METHOD_LINEAR_INTERPOLATION));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.PRM_LINEAR_INTERPOLATION);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PST_REPLACEMENT_METHOD_LAST_GOOD_VALUE));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.PRM_LAST_GOOD_VALUE);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PST_REPLACEMENT_METHOD_12PP_INTERPOLATION));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.PRM_12PP_INTERPOLATION);
		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}

	static String getPreFilterOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_NONE));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_NONE);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_REMOVE_ZEROES_LEVEL));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_REMOVE_ZEROES);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_EXCLUDE_LEVEL));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_EXCLUDE_LEVEL);
		stringBuffer.append(';');
//		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_VELOCITY_CORRELATION));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.DFT_VELOCITY_CORRELATION);
//		stringBuffer.append(';');
//		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.DFT_PHASE_SPACE_THRESHOLDING);
//		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_CORRELATION_AND_SNR));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_CORRELATION_AND_SNR);
		stringBuffer.append(';');
//		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.DFT_MODIFIED_PHASE_SPACE_THRESHOLDING);
//		stringBuffer.append(';');
//		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_50PT_MOVING_AVERAGE));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.DFT_50PT_MOVING_AVERAGE);
//		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_W_DIFF));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_W_DIFF);
		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}

	static String getDespikingFilterOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_NONE));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_NONE);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_EXCLUDE_LEVEL));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_EXCLUDE_LEVEL);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_VELOCITY_CORRELATION));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_VELOCITY_CORRELATION);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_PHASE_SPACE_THRESHOLDING);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_CORRELATION_AND_SNR));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_CORRELATION_AND_SNR);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_MODIFIED_PHASE_SPACE_THRESHOLDING);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_MOVING_AVERAGE));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_MOVING_AVERAGE);
		stringBuffer.append(';');stringBuffer.append(DAStrings.getString(DAStrings.DESPIKING_FILTER_W_DIFF));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.DFT_W_DIFF);
		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}

	static String getWaveletTypeOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB02));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB02);
		stringBuffer.append(';');
//		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_HAAR02_ORTOHOGONAL));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.WT_HAAR02_ORTHO);
//		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB04));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB04);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB06));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB06);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB08));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB08);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB10));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB10);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB12));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB12);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB14));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB14);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB16));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB16);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB18));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB18);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_DAUB20));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_DAUB20);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_LEGE02));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_LEGE02);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_LEGE04));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_LEGE04);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_LEGE06));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WT_LEGE06);
		stringBuffer.append(';');
//		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TYPE_COIF06));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.WT_COIF06);
//		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}

	static String getWaveletTransformTypeOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
//		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_TYPE_DFT));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.WTT_DFT);
//		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_TYPE_FWT));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WTT_FWT);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_TYPE_CWT));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.WTT_CWT);
		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}
	
	static String getPSDTypeOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
//		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_TYPE_DFT));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.WTT_DFT);
//		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PSD_TYPE_BARTLETT));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.PSD_BARTLETT);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PSD_TYPE_WELCH));
		stringBuffer.append(':');
		stringBuffer.append(BackEndAPI.PSD_WELCH);
		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}
	
	static String getPSDWindowOptions() {
		// "<option 1>:<scale factor 1>;<option 2>:<scale factor 2>;..."
		StringBuffer stringBuffer = new StringBuffer();
//		stringBuffer.append(DAStrings.getString(DAStrings.WAVELET_TRANSFORM_TYPE_DFT));
//		stringBuffer.append(':');
//		stringBuffer.append(BackEndAPI.WTT_DFT);
//		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PSD_WINDOW_NONE));
		stringBuffer.append(':');
		stringBuffer.append(MAJFCMaths.PSD_WINDOW_TYPE_NONE);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PSD_WINDOW_BARTLETT));
		stringBuffer.append(':');
		stringBuffer.append(MAJFCMaths.PSD_WINDOW_TYPE_BARTLETT);
		stringBuffer.append(';');
		stringBuffer.append(DAStrings.getString(DAStrings.PSD_WINDOW_HAMMING));
		stringBuffer.append(':');
		stringBuffer.append(MAJFCMaths.PSD_WINDOW_TYPE_HAMMING);
		stringBuffer.append(';');
		
		return stringBuffer.toString();
	}
	
	/**
	 * Gets the XML to represent the DataSetConfig object passed in
	 * @param configData
	 * @return
	 */
	public static String getConfigXML(DataSetConfig configData) {
		StringBuffer configString = new StringBuffer();
		
		configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_DEFAULT_DATA_FILE_PATH, configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY)));
		configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_DEFAULT_CSV_FILE_PATH, configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY)));
		configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_DEFAULT_MATLAB_EXPORT_FILE_PATH, configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY)));
		String boundaryDefinitionFilename = configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME);
		configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_BOUNDARY_DEFINITION_FILENAME, boundaryDefinitionFilename != null ? boundaryDefinitionFilename : ""));
		String theCSVFileFormat = configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT);
		configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_CSV_FILE_FORMAT, theCSVFileFormat != null ? theCSVFileFormat : ""));
		String theCSVFileDelimiter = configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER);
		if (theCSVFileDelimiter.equals("\t")) {
			configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_DATA_FILE_DELIMITER, DADefinitions.XML_TAB_ALIAS));
		} else {
			configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_DATA_FILE_DELIMITER, theCSVFileDelimiter));
		}
		configString.append(MAJFCTools.makeXMLNode(DADefinitions.XML_CSV_FILE_DECIMAL_SEPARATOR, configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR)));
				
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_PRE_FILTER_TYPE, configData.get(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_DESPIKING_FILTER_TYPE, configData.get(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_EXCLUDE_LEVEL, configData.get(BackEndAPI.DSC_KEY_EXCLUDE_LEVEL)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_MODIFIED_PST_AUTO_SAFE_LEVEL_C1, configData.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2, configData.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_PST_REPLACEMENT_METHOD, configData.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_PST_REPLACEMENT_POLYNOMIAL_ORDER, configData.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_CS_TYPE, configData.get(BackEndAPI.DSC_KEY_CS_TYPE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_SAMPLING_RATE, configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_LIMITING_CORRELATION, configData.get(BackEndAPI.DSC_KEY_LIMITING_CORRELATION)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_LIMITING_SNR, configData.get(BackEndAPI.DSC_KEY_LIMITING_SNR)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER, configData.get(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_LIMITING_W_DIFF, configData.get(BackEndAPI.DSC_KEY_LIMITING_W_DIFF)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_MOVING_AVERAGE_WINDOW_SIZE, configData.get(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_DATA_SET_LENGTH_SCALE_FACTOR, configData.get(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_DATA_FILE_VELOCITY_SCALE_FACTOR, configData.get(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_LEFT_BANK_POSITION, configData.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_RIGHT_BANK_POSITION, configData.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_WATER_DEPTH, configData.get(BackEndAPI.DSC_KEY_WATER_DEPTH)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_MEASURED_DISCHARGE, configData.get(BackEndAPI.DSC_KEY_MEASURED_DISCHARGE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_BED_SLOPE, configData.get(BackEndAPI.DSC_KEY_BED_SLOPE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_STANDARD_CELL_WIDTH, configData.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_WIDTH)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_STANDARD_CELL_HEIGHT, configData.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_SYNCH_IGNORE_FIRST_X_SECONDS, configData.get(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_SYNCH_LIMITING_VALUE, configData.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_SYNCH_LIMITING_VALUE_DIRECTION, configData.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_AT_TRIM_START_BY, configData.get(BackEndAPI.DSC_KEY_AT_TRIM_START_BY)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_AT_PRIOR_LENGTH, configData.get(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_AT_SAMPLE_LENGTH, configData.get(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_LARGE_FILE_MEAS_PER_SPLIT, configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_USE_BINARY_FILE_FORMAT, configData.get(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_NUMBER_OF_PROBES, configData.get(BackEndAPI.DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_PSD_TYPE, configData.get(BackEndAPI.DSC_KEY_PSD_TYPE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_PSD_WINDOW, configData.get(BackEndAPI.DSC_KEY_PSD_WINDOW)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_NUMBER_OF_BARTLETT_WINDOWS, configData.get(BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_PSD_WELCH_WINDOW_OVERLAP, configData.get(BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_WAVELET_TYPE, configData.get(BackEndAPI.DSC_KEY_WAVELET_TYPE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_WAVELET_TRANSFORM_TYPE, configData.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_WAVELET_TRANSFORM_SCALE_BY_INST_POWER, configData.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_INVERT_X_AXIS, configData.get(BackEndAPI.DSC_KEY_X_AXIS_INVERTED)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_INVERT_Y_AXIS, configData.get(BackEndAPI.DSC_KEY_Y_AXIS_INVERTED)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_INVERT_Z_AXIS, configData.get(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_DATA_SET_LOCKED, configData.get(BackEndAPI.DSC_KEY_DATA_SET_LOCKED)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_MAIN_PROBE_INDEX, configData.get(BackEndAPI.DSC_KEY_MAIN_PROBE_INDEX)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_FIXED_PROBE_INDEX, configData.get(BackEndAPI.DSC_KEY_FIXED_PROBE_INDEX)));
		configString.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_SYNCH_PROBE_INDEX, configData.get(BackEndAPI.DSC_KEY_SYNCH_PROBE_INDEX)));

		configString.append(MAJFCTools.makeXMLStartTag(DADefinitions.XML_PROBE_SETUP, true));
		
		LinkedList<DataPointCoordinates> coordsList = configData.getCoords();
		int numberOfProbes = coordsList.size();

		for (int i = 0; i < numberOfProbes; ++i) {
			DataPointCoordinates coords = coordsList.get(i);
			configString.append(MAJFCTools.makeXMLStartTag(DADefinitions.XML_PROBE_SETUP_COORDS_XML_TAG, false));
			configString.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Y_COORD, coords.getY()));
			configString.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Z_COORD, coords.getZ()));
			configString.append(MAJFCTools.makeXMLEndTag(DADefinitions.XML_PROBE_SETUP_COORDS_XML_TAG, false));
		}
		
		configString.append(MAJFCTools.makeXMLEndTag(DADefinitions.XML_PROBE_SETUP, true));
		
		return configString.toString();
	}
}
