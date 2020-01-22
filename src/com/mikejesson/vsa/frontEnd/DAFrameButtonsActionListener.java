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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportCSVDataFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportConvertedNortekNDVDataFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportConvertedPolySyncDataFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportMultiProbeBinaryDataFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportMultiRunCSVDataFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportMultiRunMultiProbeBinaryDataFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportMultiRunSingleProbeBinaryDataFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportSingleProbeBinaryFilesDialog;
import com.mikejesson.vsa.frontEnd.importDialogs.ImportSingleUMeasurementDataFileDialog;
import com.mikejesson.vsa.widgits.DAStrings;




/**
 * @author MAJ727
 *
 */
public class DAFrameButtonsActionListener implements ActionListener {

	/**
	 * 
	 */
	public DAFrameButtonsActionListener() {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent theEvent) {
		String actionName = (String)((AbstractButton)theEvent.getSource()).getAction().getValue(Action.NAME);
		
		if (actionName.equals(DAStrings.getString(DAStrings.OPEN_DATA_SET))) {
			openFile();
		} else if (actionName.equals(DAStrings.getString(DAStrings.NEW_SINGLE_PROBE_DATA_SET))) {
			saveToFile(BackEndAPI.DST_SINGLE_PROBE, null, true, 1);
		} else if (actionName.equals(DAStrings.getString(DAStrings.NEW_MULTIPLE_PROBE_DATA_SET))) {
			saveToFile(BackEndAPI.DST_MULTI_PROBE, null, true, 2);
		} else if (actionName.equals(DAStrings.getString(DAStrings.NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET))) {
			saveToFile(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE, null, true, 1);
		} else if (actionName.equals(DAStrings.getString(DAStrings.NEW_MULTI_RUN_MULTI_PROBE_DATA_SET))) {
			saveToFile(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE, null, true, 1);
		} else if (actionName.equals(DAStrings.getString(DAStrings.CALCULATE_DPS_REYNOLDS_STRESSES))) {
			calculateDataPointSummaryField(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN);
		} else if (actionName.equals(DAStrings.getString(DAStrings.CALCULATE_DPS_QH_DATA))) {
			calculateDataPointSummaryField(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS);
		} else if (actionName.equals(DAStrings.getString(DAStrings.CALCULATE_DPS_TKE_DATA))) {
			calculateDataPointSummaryField(BackEndAPI.DPS_KEY_TKE);
		} else if (actionName.equals(DAStrings.getString(DAStrings.CALCULATE_DPS_FIXED_PROBE_CORRELATIONS))) {
			calculateDataPointSummaryField(BackEndAPI.DPS_KEY_U_TKE_FLUX);
		} else if (actionName.equals(DAStrings.getString(DAStrings.CONFIGURATION))) {
			new ConfigurationDialog(DAFrame.getFrame(), true, true);
		} else if (actionName.equals(DAStrings.getString(DAStrings.CALCULATE_DPS_ALL_LABEL))) {
			calculateDataPointSummaryField(null);
		} else if (actionName.equals(DAStrings.getString(DAStrings.ABOUT_BUTTON_LABEL))) {
			new AboutDialog(DAFrame.getFrame());
		} else {
			DataSetType dataSetType;
			try {
				dataSetType = BackEndAPI.getBackEndAPI().getDataSetType(DAFrame.getFrame().getCurrentDataSetId());
			} catch (BackEndAPIException e) {
//				e.printStackTrace();
				dataSetType = BackEndAPI.DST_SINGLE_PROBE;
			}

			try {
				if (actionName.equals(DAStrings.getString(DAStrings.SAVE_DATA_SET))) {
					saveToFile(dataSetType, DAFrame.getFrame().getCurrentDataSetFile(), false, -999, DAFrame.getFrame().getCurrentDataSetConfig());
				} else if (actionName.equals(DAStrings.getString(DAStrings.IMPORT_CSV_FILES))) {
					if (dataSetType.equals(BackEndAPI.DST_SINGLE_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_PROBE)) {
						new ImportCSVDataFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), dataSetType, DAFrame.getFrame(), true);
					} else if (dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE)) {
						new ImportMultiRunCSVDataFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
					}
				} else if (actionName.equals(DAStrings.getString(DAStrings.IMPORT_BINARY_DATA_FILES_BUTTON_LABEL))) {
					if (dataSetType.equals(BackEndAPI.DST_SINGLE_PROBE)) {
						new ImportSingleProbeBinaryFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
					} else if (dataSetType.equals(BackEndAPI.DST_MULTI_PROBE)) {
						new ImportMultiProbeBinaryDataFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
					} else if (dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE)) {
						new ImportMultiRunSingleProbeBinaryDataFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
					} else if (dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE)) {
						new ImportMultiRunMultiProbeBinaryDataFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
					}
				} else if (actionName.equals(DAStrings.getString(DAStrings.IMPORT_CONVERTED_VNO_FILES))) {
					if (dataSetType.equals(BackEndAPI.DST_SINGLE_PROBE)) {
						new ImportConvertedNortekNDVDataFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
					} else if (dataSetType.equals(BackEndAPI.DST_MULTI_PROBE)) {
						 new ImportConvertedPolySyncDataFilesDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
					}
				} else if (actionName.equals(DAStrings.getString(DAStrings.IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL))) {
					 new ImportSingleUMeasurementDataFileDialog(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), true);
				}
			} catch (BackEndAPIException theException) {
	         	JOptionPane.showMessageDialog(DAFrame.getFrame(), theException.getMessage(), theException.getTitle(), JOptionPane.ERROR_MESSAGE);
			}
		}	
	}

	/**
	 * Brings up a dialog to allow user to choose file name for the data set and saves the data set
	 * @param defaultFile The default file to be selected (or null)
	 * @param isNewDataSet True if this is a new data set
	 * @param numberOfProbes The number of probes this data set is for. Only used if isNewDataSet is true.
	 */
	private void saveToFile(DataSetType dataSetType, File defaultFile, boolean isNewDataSet, int numberOfProbes) {
		saveToFile(dataSetType, defaultFile, isNewDataSet, numberOfProbes, DAFrame.getFrame().getDefaultDataSetConfiguration());
	}
	
	/**
	 * Brings up a dialog to allow user to choose file name for the data set and saves the data set
	 * @param defaultFile The default file to be selected (or null)
	 * @param isNewDataSet True if this is a new data set
	 * @param numberOfProbes The number of probes this data set is for. Only used if isNewDataSet is true.
	 */
	private void saveToFile(DataSetType dataSetType, File defaultFile, boolean isNewDataSet, int numberOfProbes, DataSetConfig dsc) {
		SaveFileChooser fileChooser = new SaveFileChooser(dsc.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY));
		
		if (isNewDataSet) {
			fileChooser.setDialogTitle(DAStrings.getString(DAStrings.NEW_DATA_SET_FILENAME_DIALOG_TITLE));
		}
		
		fileChooser.setSelectedFile(defaultFile);
		
		int returnVal = fileChooser.showSaveDialog(DAFrame.getFrame());
		
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            if (isNewDataSet) {
            	// If this file is already in use for an open data set then fail
            	if (DAFrame.getFrame().dataSetForFileIsOpen(file)) {
					JOptionPane.showMessageDialog(DAFrame.getFrame(), DAStrings.getString(DAStrings.ERROR_DATA_SET_ALREADY_OPEN_TITLE), DAStrings.getString(DAStrings.ERROR_DATA_SET_ALREADY_OPEN_MSG), JOptionPane.ERROR_MESSAGE);
					return;
				}
	
            	try {
            		DataSetConfig defaultDSConfig = DAFrame.getFrame().getDefaultDataSetConfiguration();
            		defaultDSConfig.set(BackEndAPI.DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET, Double.valueOf(numberOfProbes));
            		
            		DAFrame.getBackEndAPI().createNewDataSet(dataSetType, DAFrame.getFrame().getDefaultDataSetConfiguration(), file, DAFrame.getFrame().getBackEndAPICallBackAdapter());
  		      	} catch (BackEndAPIException theException) {
  		      		JOptionPane.showMessageDialog(DAFrame.getFrame(), theException.getMessage(), theException.getTitle(), JOptionPane.ERROR_MESSAGE);
  		      	}
            }

            DAFrame.getFrame().saveToFile(file);
        }
	}
	
	/**
	 * Brings up a dialog to allow the user to open an existing data file
	 */
	private void openFile(){
		FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Data Sets", "majds");
		JFileChooser fileChooser = new JFileChooser(DAFrame.getFrame().getDefaultDataSetConfiguration().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY));
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		int returnVal = fileChooser.showOpenDialog(DAFrame.getFrame());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try{
            	DAFrame.getBackEndAPI().openDataSet(file, DAFrame.getFrame().getBackEndAPICallBackAdapter());	
            }
            catch (BackEndAPIException theException){
            	JOptionPane.showMessageDialog(DAFrame.getFrame(), theException.getMessage(), theException.getTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
	}


	public void calculateDataPointSummaryField(DataPointSummaryIndex dpsIndex) {
		new DataPointDataCalculationProgressTask(DAStrings.getString(DAStrings.CALCULATING_DATA_POINT_SUMMARY_FIELDS_TITLE), dpsIndex);
	}
	
	/**
	 * Class for the worker thread to do all the stuff required for saving the data set
	 */
	private class DataPointDataCalculationProgressTask extends DAProgressDialog {
		private DataPointSummaryIndex mDPSIndex;
		
		/**
		 * 
		 * @param parent
		 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
		 * @param title
		 */
		public DataPointDataCalculationProgressTask(String title, DataPointSummaryIndex dpsIndex) {
			super(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), title);
			
			mDPSIndex = dpsIndex;
			
			setVisible();
		}

		@Override
		/**
		 * The stuff done by this task.
		 */
		protected Void doInBackground() throws Exception {
			DAFrame.getBackEndAPI().calculateDataPointSummaryField(getDataSetId(), mDPSIndex, DAFrame.getFrame().getBackEndAPICallBackAdapter(), this);
			
			return null;
		}
	}
}


