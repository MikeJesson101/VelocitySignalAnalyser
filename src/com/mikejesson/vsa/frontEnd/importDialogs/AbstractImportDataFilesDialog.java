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

package com.mikejesson.vsa.frontEnd.importDialogs;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import com.mikejesson.majfc.guiComponents.MAJFCAbstractCallbackButton;
import com.mikejesson.majfc.guiComponents.MAJFCDialogButton;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCNumberExtractorTokenizer;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DAProgressInterface;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;
import com.mikejesson.vsa.widgits.NumberCellRenderer;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractImportDataFilesDialog extends JDialog implements ActionListener {
	protected AbstractDataSetUniqueId mDataSetId;
	protected DataSetType mDataSetType;
	private MAJFCDialogButton mImportAllButton;
	private MAJFCDialogButton mImportSelectedButton;
	private MAJFCDialogButton mCancelButton;
	private MAJFCAbstractCallbackButton<File> mChooseDirectoryButton;
	private JPanel mButtonPanel;
	private Vector<FileToImportDetails> mFilesInDirectory = new Vector<FileToImportDetails>();
	private JPanel mTopPanel;
	private JScrollPane mChosenFilesHolderScrollPane;
	private JTable mChosenFilesTable;
	private MyDataModel mDataModel;
	private RowSorterDataModel mRowSorterDataModel;
	private JProgressBar mProgressBar;
	protected MAJFCPanel mCustomPanel;

	protected int[] mAllowedNumbersOfTokens = { 2, 5 };
	private final int NUMBER_OF_TOKENS_IF_NO_ROTATION_ANGLES_INDEX = 0;
	private final int NUMBER_OF_TOKENS_WITH_ROTATION_ANGLES_INDEX = 0;
	
	/**
	 * @param dataSetId The data set this is for
	 * @param parent the parent frame
	 * @param modal is this modal?
	 * @throws BackEndAPIException 
	 */
	public AbstractImportDataFilesDialog(AbstractDataSetUniqueId dataSetId, Frame parent, String title, boolean modal) throws BackEndAPIException {
		this(dataSetId, parent, title, DAStrings.getString(DAStrings.CHOOSE_DIRECTORY), modal);
	}
	
	/**
	 * @param dataSetId The data set this is for
	 * @param parent the parent frame
	 * @param modal is this modal?
	 * @throws BackEndAPIException 
	 */
	public AbstractImportDataFilesDialog(AbstractDataSetUniqueId dataSetId, Frame parent, String title, String chooseButtonText, boolean modal) throws BackEndAPIException {
		super(parent, modal);
		
		mDataSetId = dataSetId;
		mDataSetType = BackEndAPI.getBackEndAPI().getDataSetType(mDataSetId);
		
		initialise();
		setTitle(title);
		
		buildGUI(chooseButtonText);
		
		setGUIStates();
		
		File directory = new File(DAFrame.getFrame().getCurrentDataSetConfig().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY));
		directoryChosen(directory);
		
		validate();
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	protected void initialise() {
	}

	/**
	 * Sets enabling of the GUI components
	 */
	protected void setGUIStates() {
		mTopPanel.setVisible(mFilesInDirectory.size() > 0);
		mImportAllButton.setEnabled(mFilesInDirectory.size() > 0);
		mImportSelectedButton.setEnabled(mChosenFilesTable.getSelectedRowCount() > 0);
		mCancelButton.setEnabled(true);
		mChooseDirectoryButton.setEnabled(true);
		
		validate();
		pack();
	}

	@Override
	/**
	 * Gets the preferred size, restricting the height to 600
	 * @return The adjusted preferred size
	 */
	public Dimension getPreferredSize() {
		int height = super.getPreferredSize().height;
		int width = super.getPreferredSize().width;;

		int tableWidth = mFilesInDirectory.size() > 0 ? DATools.resizeColumnWidth(mChosenFilesTable) : 1;
		width = Math.max(width, tableWidth + 38);
		
		Container parent = getParent();
		Point location = getLocation();
			
		int y = parent.getLocation().y + (parent.getSize().height/2) - height/2;
		y = y < 0 ? 0 : y;
			
		setLocation(location.x, y);
				
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int maxHeight = (int) (screenSize.height * 0.9);
		int maxWidth = (int) (screenSize.width * 0.9);

		return new Dimension(width < maxWidth ? width : maxWidth, height < maxHeight ? height : maxHeight);
	}

	/**
	 * Builds the GUI for this dialog
	 */
	private void buildGUI(String chooseButtonText) {
		setLayout(new GridBagLayout());
		
		mImportAllButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.IMPORT_ALL), this);
		mImportSelectedButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.IMPORT_SELECTED), this);
		mCancelButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.CANCEL), this);
		mChooseDirectoryButton = new MAJFCAbstractCallbackButton<File>(chooseButtonText, this) {
			@Override
			public void callback(File file) {
				directoryChosen(file);
			}
		};
		mCancelButton.setPreferredSize(new Dimension(75, 25));
		
		mDataModel = getDataModel();
		mChosenFilesTable = new JTable(mDataModel);
		mChosenFilesTable.setDefaultRenderer(Object.class, new NumberCellRenderer());
		
		MySelectionListener selectionListener = new MySelectionListener(mChosenFilesTable);
		mChosenFilesTable.getSelectionModel().addListSelectionListener(selectionListener);
		mChosenFilesTable.getColumnModel().getSelectionModel().addListSelectionListener(selectionListener);
		   
		mRowSorterDataModel = new RowSorterDataModel();
		List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(mDataModel.LAST_MODIFIED_COL_INDEX, SortOrder.DESCENDING));
//		sortKeys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
		TableRowSorter<RowSorterDataModel> sorter = new TableRowSorter<RowSorterDataModel>(mRowSorterDataModel);
		sorter.setSortKeys(sortKeys);
		mChosenFilesTable.setRowSorter(sorter);
		sorter.setComparator(mDataModel.FILENAME_COL_INDEX, new Comparator<String>() {
			@Override
			public int compare(String first, String second) {
				MAJFCNumberExtractorTokenizer st1 = new MAJFCNumberExtractorTokenizer(first, "-");
				MAJFCNumberExtractorTokenizer st2 = new MAJFCNumberExtractorTokenizer(second, "-");
				
				int numberOfTokens1 = st1.countTokens();
				int numberOfTokens2 = st2.countTokens();
				
				if (numberOfTokens1 != numberOfTokens2
					|| (numberOfTokens1 != mAllowedNumbersOfTokens[NUMBER_OF_TOKENS_IF_NO_ROTATION_ANGLES_INDEX] && numberOfTokens1 != mAllowedNumbersOfTokens[NUMBER_OF_TOKENS_WITH_ROTATION_ANGLES_INDEX])) { 
					return first.compareTo(second);
				}
				
				int numberOfTokensForCoords = mAllowedNumbersOfTokens[NUMBER_OF_TOKENS_IF_NO_ROTATION_ANGLES_INDEX];
				int[] coords1 = new int[numberOfTokensForCoords];
				int[] coords2 = new int[numberOfTokensForCoords];
				
				for (int i = 0; i < coords1.length; ++i) {
					coords1[i] = Integer.valueOf(st1.nextToken());
					coords2[i] = Integer.valueOf(st2.nextToken());
				}
				
//				int[] sortOrder = new int[numberOfTokensForCoords];
//				for (int i = 0; i < coords1.length; ++i) {
//					sortOrder[i] = (i + 1) % numberOfTokensForCoords;
//				}
				
				// Check coordinates
				for (int i = 0; i < numberOfTokensForCoords; ++i) {
					int coord1 = coords1[i];//sortOrder[i]];
					int coord2 = coords2[i];//sortOrder[i]];
					int result = coord1 - coord2;
	
					if (result != 0) {
						return result;
					}
				}

				return first.compareTo(second);
			}
		});
		
		mChosenFilesHolderScrollPane = new JScrollPane(mChosenFilesTable);
		
		mTopPanel = new JPanel(new GridBagLayout());
		mTopPanel.add(mChosenFilesTable.getTableHeader(), MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		mTopPanel.add(mChosenFilesHolderScrollPane, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 5, 5, 5, 5, 3, 3));

		mProgressBar = new JProgressBar(0, 100);
		mProgressBar.setVisible(false);
		mProgressBar.setStringPainted(true);
		
		JPanel bottomButtons = new JPanel(new GridBagLayout());
		int x = 0;
		bottomButtons.add(mImportAllButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		bottomButtons.add(mImportSelectedButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		bottomButtons.add(mCancelButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		mButtonPanel = new JPanel(new GridBagLayout());
		mButtonPanel.add(mChooseDirectoryButton, MAJFCTools.createGridBagConstraint(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		mButtonPanel.add(new JPanel(), MAJFCTools.createGridBagConstraint(x++, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		mButtonPanel.add(bottomButtons, MAJFCTools.createGridBagConstraint(x++, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		mButtonPanel.add(new JPanel(), MAJFCTools.createGridBagConstraint(x++, 1, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		
		mCustomPanel = buildCustomPanel();
		
		int y = 0;
		add(mTopPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 5, 5, 5, 5, 3, 3));
		
		if (mCustomPanel != null) {
			add(mCustomPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 0.3, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 5, 5, 5, 5, 3, 3));
		}
		
		add(mButtonPanel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		add(mProgressBar, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
	}

	protected MyDataModel getDataModel() {
		return new MyDataModel();
	}
	
	/**
	 * Allows child classes to add a custom panel above the buttons
	 * @return The custom panel or null for no custom panel
	 */
	protected MAJFCPanel buildCustomPanel() {
		return null;
	}
	
	/**
	 * Helper class to handle selections in the table of chosen files
	 * @author mikefedora
	 *
	 */
	private class MySelectionListener implements ListSelectionListener {
        @SuppressWarnings("unused")
		private final JTable mTable;

        /**
         * Constructor
         * @param table The table we are listening for selections on
         */
        MySelectionListener(JTable table) {
        	mTable = table;
        }
        
		@Override
		/**
		 * List selection has changed
		 * @param theEvent The list selection event
		 */
		public void valueChanged(ListSelectionEvent theEvent) {
			setGUIStates();
		}
	}

	/**
	 * Adds another chosen file to the list
	 * @param file The file to be imported
	 */
	protected void addChosenFile(File file) {
		// Filename should be of format "<yCoord>-<zCoord>;<comment>.<extension>"
		// or "<yCoord>-<zCoord>-<yzPlaneRotation>-<xzPlaneRotation>-<xyPlaneRotation>;<comment>.<extension>"
		// (The ; is not required if there is no comment).
		String filename = file.getName();
		
		// Strip extension
		filename = filename.substring(0, filename.lastIndexOf('.'));
		
		try {
			StringTokenizer tokenizerForComments = new StringTokenizer(filename, ";");
			String coordsToken = tokenizerForComments.nextToken();
			MAJFCNumberExtractorTokenizer tokenizerForCoords = new MAJFCNumberExtractorTokenizer(coordsToken, "-");
			
			String comments = "";
			
			if (tokenizerForComments.hasMoreTokens()) {
				comments = tokenizerForComments.nextToken();
			}

			int tokenCount = tokenizerForCoords.countTokens();
			boolean numberOfTokensIsValid = false;
			int numberOfTokensForCoords = mAllowedNumbersOfTokens[NUMBER_OF_TOKENS_IF_NO_ROTATION_ANGLES_INDEX];
			
			for (int i = 0; i < mAllowedNumbersOfTokens.length; ++i) {
				if (tokenCount == mAllowedNumbersOfTokens[i]) {
					numberOfTokensIsValid = true;
					break;
				}
			}
			
			if (numberOfTokensIsValid == false) {
				throw new Exception();
			}
			
			if (tokenCount == 1) {
				mFilesInDirectory.add(createFileToImportDetails(new String[] {"1","1"}, 0, 0, 0, "", file));
				return;
			} 
		
			String[] coords = new String[numberOfTokensForCoords];
			
			for (int i = 0; i < numberOfTokensForCoords; ++i) {
				coords[i] = tokenizerForCoords.nextToken();
			}
			
			if (tokenCount == numberOfTokensForCoords) {
				mFilesInDirectory.add(createFileToImportDetails(coords, 0, 0, 0, comments, file));
			} else {
				String[] rotations = {tokenizerForCoords.nextToken(), tokenizerForCoords.nextToken(), tokenizerForCoords.nextToken()};
				
				// If the rotations start with a number or . them accept but strip off anything else
				StringBuffer theStrippedRotation = new StringBuffer();
				
				for (int j = 0; j < rotations.length; ++j){
					char firstChar = rotations[j].charAt(0);
					
					if (Character.isDigit(firstChar) == false && firstChar != '.') {
						throw new Exception();
					}
					
					for (int i = 0; i < rotations[j].length(); ++i) {
						char theChar = rotations[j].charAt(i);
						if (Character.isDigit(theChar) || theChar == '.') {
							theStrippedRotation.append(theChar);
						} else {
							continue;
						}
					}
					
					rotations[j] = theStrippedRotation.toString();
					theStrippedRotation.setLength(0);
				}

				mFilesInDirectory.add(createFileToImportDetails(coords, MAJFCTools.parseDouble(rotations[0]), MAJFCTools.parseDouble(rotations[1]), MAJFCTools.parseDouble(rotations[2]), comments, file));
			}
		} catch (Exception theException) {
			return;
		}
	}

	/**
	 * @param coords The coordinates of this data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param comment Any comment attached to the filename
	 * @param file The file to import the details from
	 * @param coordsIndexOffset Offset (from zero) of the first coordinate in coords
	 */ 
	protected FileToImportDetails createFileToImportDetails(String[] coords, double theta, double phi, double alpha, String comment, File file) {
		return new FileToImportDetails(coords, theta, phi, alpha, comment, file);
	}

	@Override
	/**
	 * Validate and pack the dialog
	 */
	public void pack() {
		mChosenFilesHolderScrollPane.validate();
		mTopPanel.validate();
		validate();
		super.pack();
	}

	@Override
	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent theEvent) {
		Object source = theEvent.getSource();
		
		if (source.equals(mImportAllButton)){
			if (mFilesInDirectory.size() == 0) {
				return;
			}
			
			importFiles(mFilesInDirectory);
		} else if (source.equals(mImportSelectedButton)) {
			if (mChosenFilesTable.getSelectedRowCount() == 0) {
				return;
			}
			
			Vector<FileToImportDetails> filesToImport = new Vector<FileToImportDetails>();
			int[] selectedRows = mChosenFilesTable.getSelectedRows();
			
			for (int i = 0; i < selectedRows.length; ++i) {
				filesToImport.add(mFilesInDirectory.elementAt(mChosenFilesTable.convertRowIndexToModel(selectedRows[i])));
			}
			
			importFiles(filesToImport);
		} else if (source.equals(mCancelButton)) {
			setVisible(false);
		} else if (source.equals(mChooseDirectoryButton)) {
			DATools.chooseDirectory(null, DAFrame.getFrame().getCurrentDataSetConfig().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY), mChooseDirectoryButton);
		}
	}
	   
	private void importFiles(Vector<FileToImportDetails> filesToImport) {
    	mProgressBar.setVisible(true);
    	mChooseDirectoryButton.setVisible(false);
     	mButtonPanel.setVisible(false);
     	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    	pack();

    	DAFrame.getFrame().fileImportStarted();
    	
    	FileLoaderTask fileLoader = new FileLoaderTask(filesToImport);
    	fileLoader.addPropertyChangeListener(new PropertyChangeListener() {
    		@Override
    		public void propertyChange(PropertyChangeEvent theEvent) {
    	        if ("progress" == theEvent.getPropertyName()) {
    	            int progress = (Integer)theEvent.getNewValue();
    	            mProgressBar.setValue(progress);
    	        } 
    	    }
    	});
    	fileLoader.execute();
	}
	
	/**
	 * Helper class to run loading of files in the background to allow GUI to update
	 * @author MAJ727
	 *
	 */
	private class FileLoaderTask extends SwingWorker<Void, Void> implements DAProgressInterface {
		Vector<FileToImportDetails> mFilesToImport;
		   
		FileLoaderTask(Vector<FileToImportDetails> filesToImport) {
			mFilesToImport = filesToImport;
		}
	    
		@Override
		/**
		 * The stuff done in the background
		 */
		public Void doInBackground() {
			int iterations = 0;
			int fileCount = mFilesToImport.size();
			int lastProgress = 0;
			Enumeration<FileToImportDetails> filesToImport = mFilesToImport.elements();
			long importStartTime = System.currentTimeMillis();			
			while (filesToImport.hasMoreElements()) {
				FileToImportDetails fileToImport = filesToImport.nextElement(); 
            	
				try {
					importDataPointDataFromFile(fileToImport.mFile, fileToImport.mYCoord, fileToImport.mZCoord, fileToImport.mYZPlaneRotationTheta, fileToImport.mXZPlaneRotationPhi, fileToImport.mXYPlaneRotationAlpha, DAFrame.getFrame().getCurrentDataSetConfig().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER).charAt(0), DAFrame.getFrame().getCurrentDataSetConfig().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR).charAt(0));
            	}
            	catch (BackEndAPIException theException) {
                   	JOptionPane.showMessageDialog(DAFrame.getFrame(), theException.getMessage(), theException.getTitle(), JOptionPane.ERROR_MESSAGE);
                   	cancel(true);
                   	done();
                   	return null;
            	}
            	
            	int progress = iterations++*100/fileCount;
            	          	 
            	if (progress != lastProgress) {
            		lastProgress = progress;
            		setProgress(progress);
            	}
 			}
			
			long importEndTime = System.currentTimeMillis();
			MAJFCLogger.logTiming("Import time: " + ((importEndTime - importStartTime)/1000d));

			return null;
		}

	    @Override
	    /**
	     * Stuff to do when the background stuff is complete
	     */
	    public void done() {
	    	try {
	    		BackEndAPI.getBackEndAPI().importFromFilesComplete(mDataSetId, DAFrame.getFrame().getBackEndAPICallBackAdapter(), this);
	    	} catch (BackEndAPIException theException) {
	    		
	    	}
	    	
	    	DAFrame.getFrame().fileImportFinished(mDataSetId);
	    	DAFrame.getFrame().getBackEndAPICallBackAdapter().onConfigDataSet(mDataSetId);
	    	
	    	setVisible(false);
	    }
		
		@Override
		public void setProgress(int progress, String message) {
			setProgress(progress);
		}
	}

	/**
	 * Imports point data from a file
	 * 
	 * @param the file to import from
	 * @param the y-coordinate of the data point
	 * @param the z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 */
	public abstract void importDataPointDataFromFile(File file, int yCoord, int zCoord, double theta, double phi, double alpha, char delimiter, char decimalSeparator) throws BackEndAPIException;
	
	/**
	 * Is this an allowed file type for this import?
	 * @return true if it is
	 */
	protected abstract boolean isAllowedFile(File file);
	
	/**
	 * A directory has been chosen
	 */
	private void directoryChosen(File directory) {
      	File[] files = directory.listFiles();
       	clearFilesToImportList();
       	
       	if (files != null) {
	       	for (int i = 0; i < files.length; ++i) {
	       		if (isAllowedFile(files[i])) {
	       			addChosenFile(files[i]);
	       		}
	       	}
       	}
		
       	mDataModel.fireTableDataChanged();
		
//       	DATools.resizeColumnWidth(mChosenFilesTable);
       	
		setGUIStates();

		validate();
		pack();	
	}
	
	/**
	 * Clears the list of files to import and the relevant GUI components
	 */
	private void clearFilesToImportList() {
       	mFilesInDirectory.removeAllElements();
 
       	pack();
	}
	
	/**
	 * Inner class providing a data model for the table
	 * @author MAJ727
	 *
	 */
	protected class MyDataModel extends AbstractTableModel {
		private final int FILENAME_COL_INDEX = 0;
		private final int FILE_EXTENSION_COL_INDEX = 1;
		private final int Y_COORD_COL_INDEX = 2;
		private final int Z_COORD_COL_INDEX = 3;
		protected final int LAST_MODIFIED_COL_INDEX = 4;
		private final int XZ_PLANE_ROTATION_THETA_COL_INDEX = 5;
		private final int YZ_PLANE_ROTATION_THETA_COL_INDEX = 6;
		private final int XY_PLANE_ROTATION_THETA_COL_INDEX = 7;
		private final int COMMENTS_COL_INDEX = 8;
	
		private final String[] mColTitles = {	
				DAStrings.getString(DAStrings.FILENAME_LABEL),
				DAStrings.getString(DAStrings.FILE_EXTENSION_LABEL),
				DAStrings.getString(DAStrings.Y_COORD_LABEL),
				DAStrings.getString(DAStrings.Z_COORD_LABEL),
				DAStrings.getString(DAStrings.LAST_MODIFIED_LABEL),
				DAStrings.getString(DAStrings.XZ_PLANE_ROTATION_THETA_LABEL),
				DAStrings.getString(DAStrings.YZ_PLANE_ROTATION_PHI_LABEL),
				DAStrings.getString(DAStrings.XY_PLANE_ROTATION_ALPHA_LABEL),
				DAStrings.getString(DAStrings.COMMENTS_LABEL) };
		
		/**
		* Gets the column title
		* @return the column title
		*/
		public String getColumnName(int i){
			return mColTitles[i];
		}
		
		@Override
		/**
		 * Gets the number of columns in the table
		 * @return The number of columns in the table
		 */
		public int getColumnCount() {
			return mColTitles.length;
		}

		@Override
		/**
		 * Gets the number of rows in the table
		 * @return The number of rows in the table
		 */
		public int getRowCount() {
			return mFilesInDirectory.size();
		}

		@Override
		/**
		 * Gets the data for the specified cell in the table
		 * @param row The row of the required data
		 * @param column The column of the required data
		 * @return The data for the cell at row-column
		 */
		public Object getValueAt(int row, int column) {
			FileToImportDetails fileDetails = mFilesInDirectory.elementAt(row);
			
			switch (column) {
				case FILENAME_COL_INDEX: {
					String fileName = fileDetails.mFile.getName();
					return fileName.substring(0, fileName.lastIndexOf('.'));
				}
				case FILE_EXTENSION_COL_INDEX: {
					String fileName = fileDetails.mFile.getName();
					return fileName.substring(fileName.lastIndexOf('.'));
				}
				case Y_COORD_COL_INDEX:
					return fileDetails.mYCoord;
				case Z_COORD_COL_INDEX:
					return fileDetails.mZCoord;
				case LAST_MODIFIED_COL_INDEX:
					return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(fileDetails.mFile.lastModified()));
				case XZ_PLANE_ROTATION_THETA_COL_INDEX:
					return fileDetails.mYZPlaneRotationTheta;
				case YZ_PLANE_ROTATION_THETA_COL_INDEX:
					return fileDetails.mXZPlaneRotationPhi;
				case XY_PLANE_ROTATION_THETA_COL_INDEX:
					return fileDetails.mXYPlaneRotationAlpha;
				case COMMENTS_COL_INDEX:
					return fileDetails.mComment;
			}
			
			return "Oops";
		}
		
		protected Vector<FileToImportDetails> getFilesInDirectory() {
			return mFilesInDirectory;
		}
	}

	/**
	 * Inner class providing a data model for the table row sorter
	 * @author MAJ727
	 *
	 */
	protected class RowSorterDataModel extends MyDataModel {
		@Override
		/**
		 * Gets the data for the specified cell in the table
		 * @param row The row of the required data
		 * @param column The column of the required data
		 * @return The data for the cell at row-column
		 */
		public Object getValueAt(int row, int column) {
			if (column == LAST_MODIFIED_COL_INDEX) {
				FileToImportDetails fileDetails = mFilesInDirectory.elementAt(row);
				return fileDetails.mFile.lastModified();
			}
			
			return super.getValueAt(row, column);
		}
	}
	
	/**
	 * Helper class to hold file details
	 * @author mikefedora
	 *
	 */
	protected class FileToImportDetails {
		protected final int mYCoord;
		protected final int mZCoord;
		protected final double mYZPlaneRotationTheta;
		protected final double mXZPlaneRotationPhi;
		protected final double mXYPlaneRotationAlpha;
		protected final String mComment;
		protected final File mFile;
		
		/**
		 * Legacy - only used for import of single CSV files
		 * @param yCoord The y-coordinate of this data point
		 * @param zCoord The z-coordinate of this data point
		 * @param theta The probe rotation in the xz plane for this measurement
		 * @param phi The probe rotation in the yz plane for this measurement
		 * @param alpha The probe rotation in the xy plane for this measurement
		 * @param file The file to import the details from
		 */ 
		FileToImportDetails(int yCoord, int zCoord, double theta, double phi, double alpha, File file) {
			this(new String[] { String.valueOf(yCoord), String.valueOf(zCoord) }, alpha, alpha, alpha, "", file, 0);
		}

		/**
		 * @param coords The coordinates of this data point
		 * @param theta The probe rotation in the xz plane for this measurement
		 * @param phi The probe rotation in the yz plane for this measurement
		 * @param alpha The probe rotation in the xy plane for this measurement
		 * @param comment Any comment attached to the filename
		 * @param file The file to import the details from
		 */ 
		FileToImportDetails(String[] coords, double theta, double phi, double alpha, String comment, File file) {
			this(coords, theta, phi, alpha, comment, file, 0);
		}
		
		/**
		 * @param coords The coordinates of this data point
		 * @param theta The probe rotation in the xz plane for this measurement
		 * @param phi The probe rotation in the yz plane for this measurement
		 * @param alpha The probe rotation in the xy plane for this measurement
		 * @param comment Any comment attached to the filename
		 * @param file The file to import the details from
		 * @param coordsIndexOffset Offset (from zero) of the first coordinate in coords
		 */ 
		FileToImportDetails(String[] coords, double theta, double phi, double alpha, String comment, File file, int coordsIndexOffset) {
			mYCoord = Integer.parseInt(coords[0 + coordsIndexOffset]);
			mZCoord = Integer.parseInt(coords[1 + coordsIndexOffset]);
			mYZPlaneRotationTheta = theta;
			mXZPlaneRotationPhi = phi;
			mXYPlaneRotationAlpha = alpha;
			mComment = comment;
			mFile = file;
		}
	}
}
