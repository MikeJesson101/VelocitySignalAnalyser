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

package com.mikejesson.vsa.backend;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetConfigStringItemIndex;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetailIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndCallbackInterface;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CrossSectionDataIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DAProgressInterface;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetConfigChangedFlags;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetConfigIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.backEndExposed.DataSetConfig.DataSetConfigDocHandler;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.frontEnd.ConfigurationPanel;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.CoordinateIndexedLinkedList;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.MyAbstractDocHandler;

import qdxml.DocHandler;
import qdxml.QDParser;

/**
 * @author MAJ727
 *
 */
public class DataSet {
	private CoordinateIndexedLinkedList<DataPoint> mData = new CoordinateIndexedLinkedList<DataPoint>();
	private Hashtable<String, DataPoint> mDataHash = new Hashtable<String, DataPoint>();
	protected DataSetConfig mConfigData = new DataSetConfig();
	protected Object mDataLock = new Object();
	protected File mDataSetFile;
	private double[] mCrossSectionData = new double[BackEndAPI.getNumberOfCrossSectionDataKeys()];
	private Integer[][] mUnsortedCoords = new Integer[0][0];
	private Hashtable<Integer, Hashtable<Integer, Integer>> mYIndexedCoordsLookup;
	private Vector<Integer> mSortedYCoords = new Vector<Integer>();
	private Vector<Integer> mSortedZCoords = new Vector<Integer>();
	private Vector<Vector<Integer>> mSortedZCoordsSets = new Vector<Vector<Integer>>();;
	private Hashtable<Integer, Vector<Integer>> mYIndexedSortedCoordsLookup = new Hashtable<Integer, Vector<Integer>>();
	private Hashtable<Integer, Vector<Integer>> mZIndexedSortedCoordsLookup = new Hashtable<Integer, Vector<Integer>>();
	private Hashtable<DataPointCoordinates, LinkedDataSet> mFixedProbeDataSetsLookup = new Hashtable<DataPointCoordinates, LinkedDataSet>();
	private Vector<LinkedDataSet> mFixedProbeDataSets = new Vector<LinkedDataSet>();
	protected DataSetUniqueId mUniqueId;
	// TODO Don't like how this is done and these DataPointCreationError things are used
	private String mDataPointCreationErrorTitle;
	private String mDataPointCreationErrorMsg;
	protected Hashtable<DataPointSummaryIndex, DataPointSummaryIndex> mMissingDataPointSummaryIndicesLookup = new Hashtable<DataPointSummaryIndex, DataPointSummaryIndex>(BackEndAPI.getNumberOfDataPointSummaryKeys());
	protected final DataSetType mDataSetType;
	
	public static DataSet openDataSetFromFile(final File file) throws BackEndAPIException {
		try {
			if (file.length() > 0) {
				MyDataSetTypeDocHandler docHandler = new MyDataSetTypeDocHandler();
				BufferedReader fileReader = new BufferedReader(new FileReader(file));
				QDParser.parse(docHandler, fileReader);

				fileReader.close();
				
				if (isMultiRunMean(docHandler.mNewDataSetType)) {
					return new MultiRunMeanValueDataSet(file, docHandler.mNewDataSetType);
				} else {
					return new DataSet(file, docHandler.mNewDataSetType);
				}
			}
		} catch (BackEndAPIException theException){
			throw theException;
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
		
		return null;
	}

	public static boolean isMultiRunMean(DataSetType dataSetType) {
		return dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE);
	}
	
	public static boolean isMultiRunRun(DataSetType dataSetType) {
		return dataSetType.equals(BackEndAPI.DST_MULTI_RUN_RUN);
	}

	public boolean autoTrim() {
		return Double.isNaN(mConfigData.get(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH)) == false;
	}
	
	static class MyDataSetTypeDocHandler extends MyAbstractDocHandler {
		private DataSetType mNewDataSetType = BackEndAPI.DST_SINGLE_PROBE;
		
		@Override
		public void elementValue(String value) {
			if (mStartElement.equals(DADefinitions.XML_DATA_SET_TYPE)) {
				mNewDataSetType = BackEndAPI.getDataSetType(value);
						
				QDParser.finishNow();
				
			}
		}
	};

	protected DataSet(DataSetType dataSetType) {
		mUniqueId = null;
		mDataSetType = dataSetType;
	}
	
	/**
	 * Creates the data set from a saved file
	 * @param file The file in which the data set is saved
	 * @param configData The configuration data for this new data set 
	 * @throws BackEndAPIException
	 */
	public DataSet(File file, DataSetConfig configData, DataSetType dataSetType) throws BackEndAPIException {
		this(file, dataSetType);
		setConfigData(configData);
	}

	/**
	 * Creates the data set from a saved file
	 * @param file The file in which the data set is saved
	 * @param dataSetType The type of the data set.
	 * @throws BackEndAPIException
	 */
	public DataSet(File file, DataSetType dataSetType) throws BackEndAPIException {
		try {
			// Clear any old temporary files from the temporary directory
			String dataSetDirName = makeDirectoryName(file.getAbsolutePath());
			String dataSetTempDirName = makeTemporaryDirectoryName(dataSetDirName);
			File dataSetTempDir = new File(dataSetTempDirName);
			
			if (dataSetTempDir.exists()) {
				clearTemporaryDirectory(dataSetTempDir);
			}
			
			initialiseConfig();
			
			// Check whether file is empty (i.e unsaved new dataset)
			if (file.length() > 0) {
				DataSetDocHandler docHandler = new DataSetDocHandler();
				BufferedReader fileReader = new BufferedReader(new FileReader(file));
				QDParser.parse(docHandler, fileReader);
	
				fileReader.close();
			}
			
			mDataSetType = dataSetType;
			mDataSetFile = file; 
			mUniqueId = new DataSetUniqueId();

			loadFixedProbeDataSets();
		} catch (BackEndAPIException theException){
			mDataSetFile = null;
			throw theException;
		} catch (Exception theException) {
			mDataSetFile = null;
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG), theException);
		}
	}

	/**
	 * Set any values which should be non-zero by default in case they are not stored in old versions of
	 * the configuration
	 */
	private void initialiseConfig() {
		mConfigData.set(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE, 0d);
		mConfigData.set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE, -1d);
		mConfigData.set(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS, 0d);
		mConfigData.set(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT, Double.NaN);
		mConfigData.set(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT, DADefinitions.USING_XML_FILE_FORMAT);
		mConfigData.set(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER, DADefinitions.USING_ABSOLUTE_VALUE_FOR_CORR_AND_SNR_FILTER);
		mConfigData.set(BackEndAPI.DSC_KEY_AT_TRIM_START_BY, Double.NaN);
		mConfigData.set(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH, Double.NaN);
		mConfigData.set(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH, Double.NaN);
		mConfigData.set(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER, 1d);
	}

	/**
	 * Handles the events from the XML parser
	 * 
	 * @author MAJ727
	 */
	protected class DataSetDocHandler extends DataSetConfigDocHandler {
		private DataPoint mDataPoint;
		
		private DataSetDocHandler() {
			super(mConfigData);
		}
		
		@Override
		public void startDocument() {
			synchronized (mDataLock) {
				mData.clear();
				mDataHash.clear();
				
				mDataPoint = null;
				
				mDataPointCreationErrorTitle = null;
				mDataPointCreationErrorMsg = null;
				
				super.startDocument();
			}
		}
 	  
		@Override
		public void startElement(String elem, Hashtable<String, String> h) {
			if (elem.equals(DADefinitions.XML_DATA_POINT)){
				try {
					addDataPointNoCheck(h);
				} catch (BackEndAPIException theException){
					MAJFCLogger.log("dodgy point" + h.toString());
				}
			} else if (elem.equals(DADefinitions.XML_DATA_POINT_X_COMPONENT)
						|| elem.equals(DADefinitions.XML_DATA_POINT_Y_COMPONENT)
						|| elem.equals(DADefinitions.XML_DATA_POINT_Z_COMPONENT)){
				try {
					synchronized (mDataLock) {
						mDataPoint = getDataPoint(h.get(DADefinitions.XML_Y_COORD), h.get(DADefinitions.XML_Z_COORD));
						mDataPoint.addVelocityComponentFromDataSetSummaryXML(elem, h);
					}
				} catch (BackEndAPIException theException){
					try {
						removeDataPoint(MAJFCTools.parseInt(h.get(DADefinitions.XML_Y_COORD)), MAJFCTools.parseInt(h.get(DADefinitions.XML_Z_COORD)));
					} catch (Exception e) {
						MAJFCLogger.log("Unable to remove incomplete data point: " + h.toString() + '\n' + theException.getTitle() + '\n' + theException.getMessage());
					}
					MAJFCLogger.log("No data point for: " + h.toString() + '\n' + theException.getTitle() + '\n' + theException.getMessage());
				}
			} else if (elem.equals(DADefinitions.XML_PROBE_DETAILS)) {
				ProbeDetail probeDetails = new ProbeDetail();
				probeDetails.set(BackEndAPI.PD_KEY_PROBE_TYPE, h.get(DADefinitions.XML_PD_PROBE_TYPE));
				probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, h.get(DADefinitions.XML_PD_PROBE_ID));
				probeDetails.set(BackEndAPI.PD_KEY_SAMPLING_RATE, h.get(DADefinitions.XML_PD_SAMPLING_RATE));
				
				mDataPoint.setProbeDetails(probeDetails);
			}
			
			super.startElement(elem, h);
		}
		
		/**
		 * 
		 */
		@Override
		public void endDocument() {
			if (mDataPointCreationErrorTitle != null) {
		       	JOptionPane.showMessageDialog(DAFrame.getFrame(), mDataPointCreationErrorMsg, mDataPointCreationErrorTitle, JOptionPane.ERROR_MESSAGE);
			}

			synchronized (mDataLock) {
				super.endDocument();
				updateCoordinateLists();
				recalculateCrossSectionMeanValues();
			}
		}
	}
	
	/**
	 * Adds a data point to the list of data points
	 * Maintains the mData list in order (by y-coordinate then z-coordinate)
	 * @param dataPoint The data point to add
	 */
	private void addDataPointToList(DataPoint dataPoint) {
		synchronized (mDataLock) {
			mData.addCoordinateIndexedObjectToList(dataPoint);
		}
	}
	
	/**
	 * Removes a data point from the Vector data store
	 * @param dataPoint the point to remove
	 */
	private void removeFromVector(DataPoint dataPoint) {
		synchronized (mDataLock) {
			int numberOfElements = mData.size();
			
			for (int i = 0; i < numberOfElements; ++i) {
				if (mData.get(i).equals(dataPoint)) {
					mData.remove(i);
					return;
				}
			}
		}
	}

	/**
	 * Adds a data point to this data set. !!!Overwrites an existing data point at these coordinates!!!
	 * Maintains the mData vector in order (by y-coordinate then z-coordinate)
	 * @param dataPoint the data point to add
	 */
	public void addDataPointImportedFromFileNoCheck(DataPoint dataPoint) throws BackEndAPIException{
		synchronized (mDataLock){
			// If this is an existing data point remove the old version from the list and delete its file
			DataPoint removedDP = mDataHash.remove(makeKey(dataPoint.getYCoord(), dataPoint.getZCoord()));
			
			if (removedDP != null) {
				removeFromVector(dataPoint);
			}
			
			addDataPointToList(dataPoint);
			mDataHash.put(makeKey(dataPoint.getYCoord(), dataPoint.getZCoord()), dataPoint);
//			saveDataPointToTempFile(dataPoint.getYCoord(), dataPoint.getZCoord());
			//updateCoordinateLists();
		}
	}
	
	/**
	 * Adds a data point to this data set. !!!Overwrites an existing data point at these coordinates!!!
	 * Maintains the mData vector in order (by y-coordinate then z-coordinate)
	 * @param parameters the parameters for the data point
	 */
	private void addDataPointNoCheck(Hashtable<String, String> parameters) throws BackEndAPIException {
		DataPoint dataPoint = createNewDataPoint(parameters);
		
		synchronized (mDataLock){
			if (mDataHash.remove(makeKey(dataPoint.getYCoord(), dataPoint.getZCoord())) != null) {
				removeFromVector(dataPoint);
			}
			addDataPointToList(dataPoint);
			mDataHash.put(makeKey(dataPoint.getYCoord(), dataPoint.getZCoord()), dataPoint);
			//updateCoordinateLists();
		}
	}

	/**
	 * Creates a dataset (should be used rather than the constructor as it can be overridden)
	 * 
	 * @param parameters
	 * @return
	 * @throws BackEndAPIException
	 */
	protected DataPoint createNewDataPoint(Hashtable<String, String> parameters) throws BackEndAPIException {
		return new DataPoint(parameters, this);
	}
	
	/**
	 * Creates a dataset (should be used rather than the constructor as it can be overridden)
	 * 
	 * @param yCoord
	 * @param zCoord
	 * @param theta
	 * @param phi
	 * @param alpha
	 * @param dataSet
	 * @return
	 */
	public DataPoint createNewDataPoint(int yCoord, int zCoord, GenericImportDetails importDetails, int probeIndex, DataSet dataSet) {
		return new DataPoint(yCoord, zCoord, importDetails, probeIndex, dataSet);
	}

	/**
	 * Remove the data point at the given coordinates
	 * @param yCoord
	 * @param zCoord
	 */
	public void removeDataPoint(int yCoord, int zCoord) throws BackEndAPIException {
		synchronized (mDataLock){
			DataPoint removedDP = mDataHash.remove(makeKey(yCoord, zCoord));

			if (removedDP != null){
				mData.remove(removedDP);
				updateCoordinateLists();
				
				Vector<LinkedDataSet> linkedDataSets = getAllLinkedDataSets();
				int numberOfLinkedDataSets = linkedDataSets.size();
				
				for (int i = 0; i < numberOfLinkedDataSets; ++i) {
					try {
						linkedDataSets.elementAt(i).removeDataPoint(yCoord, zCoord);
					} catch (BackEndAPIException theException) {
						// Do this silently as not all fixed probe data sets will contain data for this point
					}
				}
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_TITLE), DAStrings.getString(DAStrings.REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_MSG, yCoord, zCoord));
			}
			
			recalculateCrossSectionMeanValues();
		}
	}
	
	/**
	 * Makes the hashtable lookup key from the coordinates
	 * @param yCoord
	 * @param zCoord
	 * @return the key
	 */
	public String makeKey(int yCoord, int zCoord){
		return new String(yCoord + "-" + zCoord);
	}

	/**
	 * Makes the hashtable lookup key from the coordinates
	 * @param yCoord
	 * @param zCoord
	 * @return the key
	 */
	private String makeKey(String yCoord, String zCoord){
		return yCoord + "-" + zCoord;
	}
	
	/**
	 * Saves the current data set to the specified file
	 * Moves any files from the temp directory and deletes unused files in the main data point data directory
	 * @param file The filename to save to
	 * @param frame The parent frame for any dialog thrown up
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
 	 * @param progressInterface The interface used to report progress
	 */
	public void saveToFile(File file, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException{
		// If the new file is not the same as the old one, we need to copy all the child directories
		if (mDataSetFile.equals(file) == false) {
			File oldDir = new File(makeDirectoryName(mDataSetFile.getAbsolutePath()));
			File newDir = new File(makeDirectoryName(file.getAbsolutePath()));
			
			MAJFCTools.copyDirectory(oldDir, newDir, false, true);
			
			mDataSetFile = file;
		}
		
		saveToFile(frontEndInterface, progressInterface);
	}

	
	/**
	 * 
	 * @param frame
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @param progressInterface The interface used to report progress
	 * @throws BackEndAPIException
	 */
	protected void saveToFile(BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		try{
			synchronized (mDataLock) {
				int numberOfFixedProbeDataSets = mFixedProbeDataSets.size();
				
				for (int i = 0; i < numberOfFixedProbeDataSets; ++ i) {
//					fixedProbeDataSets.nextElement().saveToFile(mParent, mFrontEndInterface);
					LinkedDataSet fixedProbeDataSet = mFixedProbeDataSets.elementAt(i);
					fixedProbeDataSet.setParentDataSetFile(mDataSetFile);
					fixedProbeDataSet.saveToFile(frontEndInterface, progressInterface);
				}
			}

			String dataSetFileName = mDataSetFile.getAbsolutePath();
			String dataSetDirName = makeDirectoryName(dataSetFileName);
			File dataSetDir = new File(dataSetDirName);
			String dataSetTempDirName = makeTemporaryDirectoryName(dataSetDirName);
			File dataSetTempDir = new File(dataSetTempDirName);

			if (getDataPointCount() == 0) {
				mDataSetFile.delete();
				dataSetTempDir.delete();
				dataSetDir.delete();
			}
			FileWriter fileWriter = new FileWriter(mDataSetFile);
			writeXMLRepresentation(fileWriter);
			fileWriter.close();
			
			// Write data point files
			if (dataSetDir.exists() == false) {
				dataSetDir.mkdirs();
			}
			
			if (dataSetTempDir.exists() == false) {
				dataSetTempDir.mkdirs();
			}
		
			File[] oldDataPointFiles = dataSetDir.listFiles();
			Hashtable<String, File> dataPointFilesToKeep = new Hashtable<String, File>();

			synchronized (mDataLock) {
				int iterations = 0;
				int dataPointCount = mData.size();
				int lastProgress = 0;

				for (int i = 0; i < dataPointCount; ++i) {
					DataPoint dataPoint = mData.get(i);
					dataPoint.saveToFullFile(dataSetDirName, makeTemporaryDirectoryName(dataSetDirName));
					// This is an existing data point so add it to our list of files to keep
	
					String dataPointFilename = dataPoint.makeDataPointFilename(dataSetDirName);
					dataPointFilesToKeep.put(dataPointFilename, mDataSetFile);
					
					int progress = iterations++*100/dataPointCount;
	   	          	 
	            	if (progress != lastProgress) {
	            		lastProgress = progress;
	            		
	            		// progressInterface may be null for fixed probe data sets
	            		if (progressInterface != null) {
	            			progressInterface.setProgress(progress, dataPointFilename);
	            		}
	            	}
				}
			}
			
			// Remove existing data point files that are obsolete, but check that they are data point files first!
			for (int i = 0; i < oldDataPointFiles.length; ++i) {
				if (oldDataPointFiles[i].getAbsolutePath().endsWith(DADefinitions.FILE_EXTENSION_DATA_POINT)
						&& dataPointFilesToKeep.remove(oldDataPointFiles[i].getAbsolutePath()) == null) {
					oldDataPointFiles[i].delete();
				}
			}
			
			clearTemporaryDirectory(dataSetTempDir);
		}
		catch (Exception theException){
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_SAVING_DATA_SET_DIALOG_TITLE), DAStrings.getString(DAStrings.ERROR_SAVING_DATA_SET_DIALOG_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
		
		MAJFCLogger.log("DataSet::saveToFile" + mDataSetFile.getAbsolutePath());
		
		onSaved(frontEndInterface);
	}

	/**
	 * Data set saved successfully
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	protected void onSaved(BackEndCallbackInterface frontEndInterface) {
		// Data set may have been saved in a new file, in which case it will have a new unique Id...
		DataSetUniqueId oldUniqueId = mUniqueId;
		mUniqueId = new DataSetUniqueId();
		
		BackEndAPI.getBackEndAPI().onDataSetSaved(mUniqueId, oldUniqueId, this, mDataSetFile, frontEndInterface);
	}
	
	/**
	 * Clears the files from the specified temporary directory
	 * @param dataSetTempDir The temporary directory holding the files
	 */
	private void clearTemporaryDirectory(File dataSetTempDir) {
		// Remove all files from the temporary directory (this should be empty anyway)
		File[] oldDataPointTempFiles = dataSetTempDir.listFiles();
		
		for (int i = 0; i < oldDataPointTempFiles.length; ++i) {
			oldDataPointTempFiles[i].delete();
			MAJFCLogger.log("Removing dodgy temporary file " + oldDataPointTempFiles[i].getAbsolutePath());
		}			
	}

	/**
	 * Makes the name of the directory used for the data set
	 * @param dataSetFileName The file name for the data set
	 * @return The name of the directory for the data set
	 */
	public static String makeDirectoryName(String dataSetFileName) {
		return stripLastExtension(dataSetFileName);
	}

	/**
	 * Strips the last extension from a filename
	 * @param fileName The file name to strip
	 * @return The stripped filename or null if there is no extension
	 */
	protected static String stripLastExtension(String fileName) {
		int lastIndexOfSeparator = fileName.lastIndexOf('.');
		
		if (lastIndexOfSeparator < 0) {
			return null;
		}
		
		return fileName.substring(0, lastIndexOfSeparator);
	}

	/**
	 * Makes the name of the temporary directory used for the data set
	 * @param dataSetDirName The directory name for the data set
	 * @return The name of the temporary directory for the data set
	 */
	public String makeTemporaryDirectoryName() {
		String dataSetFileName = mDataSetFile.getAbsolutePath();
		String dataPointDirName = makeDirectoryName(dataSetFileName);
		return makeTemporaryDirectoryName(dataPointDirName);
	}

	/**
	 * Makes the name of the temporary directory used for the data set
	 * @param dataSetDirName The directory name for the data set
	 * @return The name of the temporary directory for the data set
	 */
	private String makeTemporaryDirectoryName(String dataSetDirName) {
		return dataSetDirName + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + DADefinitions.TEMP_DIR_NAME;
	}

	/**
	 * Saves the specified data point to a temporary file
	 * @param yCoord
	 * @param zCoord
	 */
	protected void saveDataPointToTempFile(int yCoord, int zCoord) {
		String dataSetFileName = mDataSetFile.getAbsolutePath();
		String dataPointDirName = dataSetFileName.substring(0, dataSetFileName.lastIndexOf('.'));
		String tempDataPointDirName = makeTemporaryDirectoryName(dataPointDirName);

		DataPoint dataPoint = getDataPoint(yCoord, zCoord);
		dataPoint.saveToTempFile(tempDataPointDirName);
	}

	/**
	 * Write the XML representation of this data set to a file
	 * @param writer The file (writer) to write to
	 * @throws IOException 
	 */
	private void writeXMLRepresentation(FileWriter writer) throws IOException {
		// <data_set>
		// <exclude_level>
		// 0.5
		// </exclude_level>
		// <left_bank_position>
		// 0.5
		// </left_bank_position>
		// <right_bank_position>
		// 0.5
		// </right_bank_position>
		// <water_depth>
		// 0.5
		// </water_depth>
		// <data_point .../>
		// .
		// .
		// .
		// <data_point .../>
		// </data_set>
		
		DAFileOutputStringBuffer theXML = new DAFileOutputStringBuffer();
		theXML.append('<');
		theXML.append(DADefinitions.XML_DATA_SET);
		theXML.append('>');
		theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);

		theXML.append(DAFileOutputStringBuffer.makeXMLNode(DADefinitions.XML_DATA_SET_TYPE, mDataSetType.getIntIndex()));

		theXML.append(ConfigurationPanel.getConfigXML(mConfigData));
		
		writer.write(theXML.toString());
		theXML.clear();
				
		synchronized (mDataLock){
			ListIterator<DataPoint> dataPoints = mData.listIterator();
			
			while (dataPoints.hasNext()){
				writer.write(dataPoints.next().getXMLSummaryRepresentation());
			}
		}
		
		theXML.append("</");
		theXML.append(DADefinitions.XML_DATA_SET);
		theXML.append('>');
		theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);

		writer.write(theXML.toString());
	}

	/**
	 * returns the number of data points in this data set
	 * @return
	 */
	public int getDataPointCount() {
		synchronized (mDataLock) {
			return mData.size();
		}
	}

	/**
	 * Gets the unsorted coordinates of all data points in this data set
	 * @return the coordinates as a nx2 array {{x1, y1}, {x2, y2}, ...}
	 */
	public Integer[][] getUnsortedDataPointCoordinates() {
		synchronized (mDataLock) {
			return mUnsortedCoords;
		}
	}
	
	/**
	 * Gets the sorted coordinates of all data points in this data set. The z-coordinate vector contains vectors of z-coordinates
	 * for each point at the y-coordinate in the corresponding position of the y-coordinate vector.
	 * @param sortedYCoords The vector to copy the sorted X coordinates into
	 * @param sortedZCoords The vector of vectors to copy the sorted Y coordinates into
	 */
	public void getSortedDataPointCoordinates(Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoords) {
		synchronized (mDataLock) {
			sortedYCoords.removeAllElements();
			sortedZCoords.removeAllElements();
			sortedYCoords.addAll(mSortedYCoords);
			sortedZCoords.addAll(mSortedZCoordsSets);
		}
	}

	/**
	 * Gets the sorted coordinates of all data points in this data set in the form of a hashtable indexed by the y-coordinate,
	 * containing vectors of z-coordinates for which points exist at that y-coordinate
	 * @return The sorted coordinates
	 */
	@SuppressWarnings("unchecked")
	public Hashtable<Integer, Vector<Integer>> getYCoordIndexedSortedDataPointCoordinates() {
		synchronized (mDataLock) {
			return (Hashtable<Integer, Vector<Integer>>) mYIndexedSortedCoordsLookup.clone();
		}
	}

	/**
	 * Gets the sorted coordinates of all data points in this data set in the form of a hashtable indexed by the z-coordinate,
	 * containing vectors of y-coordinates for which points exist at that z-coordinate
	 * @return The sorted coordinates
	 */
	@SuppressWarnings("unchecked")
	public Hashtable<Integer, Vector<Integer>> getZCoordIndexedSortedDataPointCoordinates() {
		synchronized (mDataLock) {
			return (Hashtable<Integer, Vector<Integer>>) mZIndexedSortedCoordsLookup.clone();
		}
	}

	/**
	 * Gets a list of all the z-coordinates, sorted into order
	 * @return The sorted coordinates
	 */
	@SuppressWarnings("unchecked")
	public Vector<Integer> getSortedListOfAllZCoords() {
		synchronized (mDataLock) {
			return (Vector<Integer>) mSortedZCoords.clone();
		}
	}

	/**
	 * Updates the unsorted and sorted coordinate lists
	 */
	protected void updateCoordinateLists() {
		synchronized (mDataLock) {
			int numberOfPoints = mData.size();
			mUnsortedCoords = new Integer[numberOfPoints][2];
			mYIndexedCoordsLookup = new Hashtable<Integer, Hashtable<Integer,Integer>>();
			
			for (int i = 0; i < numberOfPoints; ++i) {
				DataPoint dataPoint = mData.get(i);
				int yCoord = dataPoint.getYCoord();
				int zCoord = dataPoint.getZCoord();
				
				mUnsortedCoords[i][0] = yCoord;
				mUnsortedCoords[i][1] = zCoord;
				
				Hashtable<Integer, Integer> zCoordsLookup = mYIndexedCoordsLookup.get(yCoord);
				if (zCoordsLookup == null) {
					zCoordsLookup = new Hashtable<Integer, Integer>();
					mYIndexedCoordsLookup.put(yCoord, zCoordsLookup);
				}
				
				zCoordsLookup.put(zCoord, zCoord);
			}
			
			mSortedZCoordsSets.removeAllElements();
			mSortedYCoords.removeAllElements();
			mSortedZCoords.removeAllElements();
			mYIndexedSortedCoordsLookup.clear();
			mZIndexedSortedCoordsLookup.clear();
			
			if (mUnsortedCoords.length == 0) {
				return;
			}
			
			Vector<Integer> zCoordsForThisYCoord = new Vector<Integer>();
			int lastYCoord = mUnsortedCoords[0][0];
			
			// This should give us a vector of vectors which contain the z-coordinates for which data points exist at the corresponding
			// y-coordinate.
			for (int i = 0; i < mUnsortedCoords.length; ++i) {
				int yCoord = mUnsortedCoords[i][0];
				int zCoord = mUnsortedCoords[i][1];
				
				if (yCoord != lastYCoord) {
					// We're moving on to the next y-coordinate
					if (zCoordsForThisYCoord.size() > 0) {
						mSortedZCoordsSets.add(zCoordsForThisYCoord);
						mSortedYCoords.add(lastYCoord);
						mYIndexedSortedCoordsLookup.put(lastYCoord, zCoordsForThisYCoord);
						zCoordsForThisYCoord = new Vector<Integer>();
						lastYCoord = yCoord;
					}
				}
				
				zCoordsForThisYCoord.add(zCoord);
	
				// Add the last lot of coordinates
				if ((i == mUnsortedCoords.length - 1) && (zCoordsForThisYCoord.size() > 0)) {
					mSortedZCoordsSets.add(zCoordsForThisYCoord);
					mSortedYCoords.add(yCoord);
					mYIndexedSortedCoordsLookup.put(lastYCoord, zCoordsForThisYCoord);
				}
				
				Vector<Integer> yCoordsForThisZCoord = mZIndexedSortedCoordsLookup.get(zCoord);
				
				if (yCoordsForThisZCoord == null) {
					yCoordsForThisZCoord = new Vector<Integer>();
					mZIndexedSortedCoordsLookup.put(zCoord, yCoordsForThisZCoord);
					mSortedZCoords.addElement(zCoord);
				}
				
				yCoordsForThisZCoord.add(yCoord);
			}
			
			Collections.sort(mSortedZCoords);
		}
	}
	
	/**
	 * Clears out all the data
	 */
	public void clearAllDataPoints() throws BackEndAPIException{
		synchronized (mDataLock){
			mData.clear();
			mDataHash.clear();
			updateCoordinateLists();
		}
		
	}

	/**
	 * Load the details (all velocity measurements) for this data point
	 * @param yCoord The point's y-coordinate
	 * @param zCoord The point's z-coordinate
	 * @param dataPointSummary The DataPointSummary object into which the data point's summary data is put
	 * @param dataPointDetails The hashtable into which the data point's detail data is put
	 */
	public void loadDataPointDetails(int yCoord, int zCoord, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail) throws BackEndAPIException {
		loadDataPointDetails(yCoord, zCoord, dataPointSummary, dataPointDetail, false);
	}
	
	/**
	 * Load the details (all velocity measurements) for this data point
	 * @param yCoord The point's y-coordinate
	 * @param zCoord The point's z-coordinate
	 * @param dataPointSummary The DataPointSummary object into which the data point's summary data is put
	 * @param dataPointDetails The hashtable into which the data point's detail data is put
	 */
	public void loadDataPointDetails(int yCoord, int zCoord, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail, boolean recalculate) throws BackEndAPIException{
		DataPoint dataPoint;
		
		synchronized (mDataLock) {
			dataPoint = getDataPoint(yCoord, zCoord);
			
			if (dataPoint == null) {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.NO_SUCH_DATA_POINT_TITLE), DAStrings.getString(DAStrings.NO_SUCH_DATA_POINT_MSG));
			}
			
			if (recalculate) {
				dataPoint.loadDetailsAndRecalculateSummaryData(mDataSetFile);
			} else {
				dataPoint.loadDetails(mDataSetFile);
			}
			
			dataPoint.copySummary(dataPointSummary);	
			dataPoint.copyDetail(dataPointDetail);
				
			// Clear the data point of its detail data to save heap space
			dataPoint.clearDetails();
		}
	}

	/**
	 * @param key The key for the required configuration data (one of BackEndAPI.DSC_KEY_...)
	 * @return The configuration data specified by the key
	 */
	public DataSetConfig getConfigData() {
		DataSetConfig configData = new DataSetConfig();
		mConfigData.copyInto(configData);
		return configData;
	}

	/**
	 * Set configuration data
	 * @param configData The new configuration data
	 */
	public DataSetConfigChangedFlags setConfigData(DataSetConfig configData) throws BackEndAPIException {
		DataSetConfigChangedFlags configChangedFlags = new DataSetConfigChangedFlags();
		Vector<DataSetConfigIndex> keys = configData.getKeys();
		int numberOfKeys = keys.size();

		for (int i = 0; i < numberOfKeys; ++i) {
			DataSetConfigIndex key = keys.elementAt(i);
			Double newValue = configData.get(key);
			
			if (mConfigData.get(key).equals(newValue)) {
				continue;
			}
			
			mConfigData.set(key, newValue);
			configChangedFlags.set(key, true);
		}
		
		mConfigData.setCoords(configData.getCoords());
		
		Vector<DataSetConfigStringItemIndex> siKeys = configData.getStringItemKeys();
		numberOfKeys = siKeys.size();

		for (int i = 0; i < numberOfKeys; ++i) {
			DataSetConfigStringItemIndex key = siKeys.elementAt(i);
			String newValue = configData.get(key);
			String oldValue = mConfigData.get(key);
			
			if (oldValue != null && oldValue.equals(newValue)) {
				continue;
			}
			
			mConfigData.set(key, newValue);
			configChangedFlags.set(key, true);
			
			if (key.equals(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME)) {
				mConfigData.loadBoundaryCoords();
			}
		}
		
		synchronized (mDataLock) {
			Vector<LinkedDataSet> linkedDataSets = getAllLinkedDataSets(); 
			int numberOfLinkedDataSets = linkedDataSets.size();
			
			for (int i = 0 ; i < numberOfLinkedDataSets; ++i) {
				linkedDataSets.elementAt(i).setConfigData(configData);
			}
		}
		
		return configChangedFlags;
	}

	/**
	 * Recalculates summary data (should be used when configuration data is changed, or a summary data field is calculated)
	 * Loads all data point data for the current data set in order to recalculate summary data.
	 * However, as this is a slow process, it only does this if certain fields (such as exclude level) have changed,
	 * otherwise the summary data is recalculated without reloading the data point data.
	 * @param dataFieldsChangedFlages A set of flags (indexed by the BackEndAPI.DSC_KEY_... keys) indicating which
	 * configuration data has changed
	 * @param frame The frame to show a progress dialog in (null for no dialog)
 	 * @param forceAdditionalCalculation Flag to indicate whether additionalCalculations should be called, irrespective of whether data point
 	 * 										values have been reloaded here.
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
 	 * @param progressInterface The interface to report progress
	 */
	public void recalculateSummaryData(DataSetConfigChangedFlags dataFieldsChangedFlags, boolean forceAdditionalCalcs, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException{
		synchronized (mDataLock) {
			if (dataFieldsChangedFlags != null && shouldDataPointsBeReloaded(dataFieldsChangedFlags)) {
				ListIterator<DataPoint> dataPoints = mData.listIterator();
				int iterations = 0;
				int dataPointCount = mData.size();
				int lastProgress = 0;
			
				while (dataPoints.hasNext()) {
					DataPoint dataPoint = dataPoints.next();
					
					dataPoint.loadDetailsAndRecalculateSummaryData(mDataSetFile);
				
					dataPoint.clearDetails();

					if (progressInterface != null) {
						int progress = iterations++*100/dataPointCount;
	   	          	 
		            	if (progress != lastProgress) {
		            		lastProgress = progress;
		            		progressInterface.setProgress(progress, null);
		            	}
					}
				}

				additionalCalculations();
			} else if (forceAdditionalCalcs && isMultiRunMean(mDataSetType)) {
				additionalCalculations();
			}

			recalculateCrossSectionMeanValues();
		}
	
		if (frontEndInterface != null) {
			frontEndInterface.onSummaryDataRecalculated(mUniqueId);
		}
	}

	/**
	 * Allows child classes to define their own calculations
	 */
	protected void additionalCalculations() {
	}

	protected boolean shouldDataPointsBeReloaded(DataSetConfigChangedFlags dataFieldsChangedFlags) {
		if (dataFieldsChangedFlags == null) {
			return false;
		}
		
		return (dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_EXCLUDE_LEVEL) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_CS_TYPE) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_SAMPLING_RATE) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_LIMITING_CORRELATION) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_LIMITING_SNR) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_LIMITING_W_DIFF) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_X_AXIS_INVERTED) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_Y_AXIS_INVERTED) == true
						|| dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED) == true);
	}
	
	/**
	 * Calculates the cross-section mean velocity using the channel configuration data and data point mean velocities.
	 */
	protected void recalculateCrossSectionMeanValues() {
		final int leftBankPosition = mConfigData.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue();
		final int rightBankPosition = mConfigData.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue();
		final int waterDepth = mConfigData.get(BackEndAPI.DSC_KEY_WATER_DEPTH).intValue();
		final double bedSlope = mConfigData.get(BackEndAPI.DSC_KEY_BED_SLOPE);

		Vector<Integer> zCoordsForThisYCoord;
		int previousYCoord = leftBankPosition;
		int yCoordCount = mSortedZCoordsSets.size();
		double xDischargeIntegral = 0, yDischargeIntegral = 0, zDischargeIntegral = 0;
		double rmsUIntegral = 0, rmsVIntegral = 0, rmsWIntegral = 0; 
		double uPrimeRMSIntegral = 0, vPrimeRMSIntegral = 0, wPrimeRMSIntegral = 0;

		for (int yIndex = 0; yIndex < yCoordCount; ++yIndex) {
			zCoordsForThisYCoord = mSortedZCoordsSets.elementAt(yIndex);
			int thisYCoord = mSortedYCoords.elementAt(yIndex);
			int nextYCoord = (yIndex + 1) == yCoordCount ? rightBankPosition : mSortedYCoords.elementAt(yIndex + 1);
			
			// The area covered by this point velocity is the square surrounding the point which extends to halfway to the next point
			// in each direction. Unless the adjacent point is the channel bed, a channel wall or the water surface, in which case
			// the square extends to that boundary
			int leftAreaScaleFactor = yIndex == 0 ? 1 : 2;
			int rightAreaScaleFactor = (yIndex + 1) == yCoordCount ? 1 : 2;
			double xWidth =  ((double)(thisYCoord - previousYCoord))/leftAreaScaleFactor +  ((double)(nextYCoord - thisYCoord))/rightAreaScaleFactor;
			
			int zCoordCount = zCoordsForThisYCoord.size();
			double previousZCoord = mConfigData.getBoundaryZAt(thisYCoord);

			for (int zIndex = 0; zIndex < zCoordCount; ++zIndex) {
				int thisZCoord = zCoordsForThisYCoord.elementAt(zIndex);
				int nextZCoord = (zIndex + 1) == zCoordCount ? waterDepth : zCoordsForThisYCoord.elementAt(zIndex + 1);
				int bottomAreaScaleFactor = zIndex == 0 ? 1 : 2;
				int topAreaScaleFactor = (zIndex + 1) == zCoordCount ? 1 : 2;
				double yHeight = ((double)(thisZCoord - previousZCoord))/bottomAreaScaleFactor + ((double)(nextZCoord - thisZCoord))/topAreaScaleFactor;
				double cellCrossSection = xWidth * yHeight;

				try {
					double ftrcU = getSummaryDataField(thisYCoord, thisZCoord, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					double ftrcV = getSummaryDataField(thisYCoord, thisZCoord, BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					double ftrcW = getSummaryDataField(thisYCoord, thisZCoord, BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					
					xDischargeIntegral += cellCrossSection * ftrcU;
					yDischargeIntegral += cellCrossSection * ftrcV;
					zDischargeIntegral += cellCrossSection * ftrcW;

					rmsUIntegral += cellCrossSection * Math.pow(ftrcU, 2);
					rmsVIntegral += cellCrossSection * Math.pow(ftrcV, 2);
					rmsWIntegral += cellCrossSection * Math.pow(ftrcW, 2);
					
					uPrimeRMSIntegral += cellCrossSection * getSummaryDataField(thisYCoord, thisZCoord, BackEndAPI.DPS_KEY_RMS_U_PRIME);
					vPrimeRMSIntegral += cellCrossSection * getSummaryDataField(thisYCoord, thisZCoord, BackEndAPI.DPS_KEY_RMS_V_PRIME);
					wPrimeRMSIntegral += cellCrossSection * getSummaryDataField(thisYCoord, thisZCoord, BackEndAPI.DPS_KEY_RMS_W_PRIME);
				}
				catch (BackEndAPIException theException) {
					MAJFCLogger.log("Error while calculating discharge & channel mean velocity " + thisYCoord + ' ' + thisZCoord);
				}
				
				previousZCoord = thisZCoord;
			}
			
			previousYCoord = thisYCoord;
		}
		
		// Config value for the area will be in m, so need to convert to the units of the data set.
		double crossSectionArea = mConfigData.get(BackEndAPI.DSC_KEY_CROSS_SECTIONAL_AREA) * Math.pow(mConfigData.get(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR), 2);
		mCrossSectionData[BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_U_VELOCITY.getIntIndex()] = xDischargeIntegral/crossSectionArea;
		mCrossSectionData[BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_V_VELOCITY.getIntIndex()] = yDischargeIntegral/crossSectionArea;
		mCrossSectionData[BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_W_VELOCITY.getIntIndex()] = zDischargeIntegral/crossSectionArea;

		mCrossSectionData[BackEndAPI.CSD_KEY_RMS_FTRC_U.getIntIndex()] = Math.sqrt(rmsUIntegral/crossSectionArea);
		mCrossSectionData[BackEndAPI.CSD_KEY_RMS_FTRC_V.getIntIndex()] = Math.sqrt(rmsVIntegral/crossSectionArea);
		mCrossSectionData[BackEndAPI.CSD_KEY_RMS_FTRC_W.getIntIndex()] = Math.sqrt(rmsWIntegral/crossSectionArea);
		
		mCrossSectionData[BackEndAPI.CSD_KEY_MEAN_RMS_U_PRIME.getIntIndex()] = uPrimeRMSIntegral/crossSectionArea;
		mCrossSectionData[BackEndAPI.CSD_KEY_MEAN_RMS_V_PRIME.getIntIndex()] = vPrimeRMSIntegral/crossSectionArea;
		mCrossSectionData[BackEndAPI.CSD_KEY_MEAN_RMS_W_PRIME.getIntIndex()] = wPrimeRMSIntegral/crossSectionArea;
		
		// R = A/P
		double wettedPerimeter = mConfigData.get(BackEndAPI.DSC_KEY_WETTED_PERIMETER);
		double hydraulicRadius = crossSectionArea/wettedPerimeter; 
		mCrossSectionData[BackEndAPI.CSD_KEY_MEAN_BOUNDARY_SHEAR.getIntIndex()] = DADefinitions.WATER_DENSITY_RHO * DADefinitions.GRAVITATIONAL_ACCELERATION_G * hydraulicRadius * bedSlope;

		Vector<LinkedDataSet> linkedDataSets = getAllLinkedDataSets();
		int numberOfLinkedDataSets = linkedDataSets.size();
				
		for (int i = 0; i < numberOfLinkedDataSets; ++i) {
			linkedDataSets.elementAt(i).recalculateCrossSectionMeanValues();
		}
	}
	
	/**
	 * Clears the detailed data for the specified data point.
	 * Used to save heap space
	 * @param yCoord
	 * @param zCoord
	 */
	public void clearDataPointDetails(int yCoord, int zCoord) throws BackEndAPIException {
		synchronized (mDataLock){
			DataPoint dataPoint = getDataPoint(yCoord, zCoord);
			
			if (dataPoint == null) {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.NO_SUCH_DATA_POINT_TITLE), DAStrings.getString(DAStrings.NO_SUCH_DATA_POINT_MSG));
			}
			
			dataPoint.clearDetails();
		}
	}

	/**
	 * Gets the specified field for the specified data point from the point's summary data
	 * @param yCoord The y-coordinate of the point
	 * @param zCoord The z-coordinate of the point
	 * @param field The field identifier (one of the BackEndAPI.DPS_KEY_... keys)
	 * @return The value for the specified field for the specified data point
	 * @throws BackEndAPIException
	 */
	public double getSummaryDataField(int yCoord, int zCoord, DataPointSummaryIndex field) throws BackEndAPIException {
		synchronized (mDataLock) {
			try {
				DataPoint dataPoint = getDataPoint(yCoord, zCoord);
				return dataPoint.getSummaryDataField(field);
			} catch (Exception e) {
				throw new BackEndAPIException("DataSet::getSummaryDataField", e.getMessage());
			}
		}
	}
	
	/**
	 * Gets the specified field for the specified data point from the point's probe details
	 * @param yCoord The y-coordinate of the point
	 * @param zCoord The z-coordinate of the point
	 * @param field The field identifier (one of the BackEndAPI.DPS_KEY_... keys)
	 * @return The value for the specified field for the specified data point
	 * @throws BackEndAPIException
	 */
	public String getProbeDetailsDataField(int yCoord, int zCoord, ProbeDetailIndex field) throws BackEndAPIException {
		synchronized (mDataLock) {
			try {
				DataPoint dataPoint = getDataPoint(yCoord, zCoord);
				return dataPoint.getProbeDetailsDataField(field);
			} catch (Exception e) {
				throw new BackEndAPIException("DataSet::getProbeDetailsDataField", e.getMessage());
			}
		}
	}
	
	/**
	 * Gets the specified cross-section data field
	 * @param field The field identifier
	 * @return The value for the specified field
	 */
	public double getCrossSectionDataField(CrossSectionDataIndex field) {
		synchronized (mDataLock) {
			return mCrossSectionData[field.getIntIndex()];
		}
	}

	/**
	 * Gets the fixed probe data set for this data set for the probe at the specified coordinates
	 * @param coords The coordinates of the fixed probe
	 * @return The fixed probe data set
	 * @throws BackEndAPIException 
	 */
	public FixedProbeDataSet getFixedProbeDataSet(DataPointCoordinates coords) throws BackEndAPIException {
		synchronized (mDataLock) {
			FixedProbeDataSet fixedProbeDataSet = (FixedProbeDataSet) mFixedProbeDataSetsLookup.get(coords);
			
			if (fixedProbeDataSet == null) {
				return addFixedProbeDataSet(coords);
			}
			
			return fixedProbeDataSet;
		}
	}
	
	private FixedProbeDataSet addFixedProbeDataSet(DataPointCoordinates coords) throws BackEndAPIException {
		FixedProbeDataSet fixedProbeDataSet = makeFixedProbeDataSet(coords);
		mFixedProbeDataSetsLookup.put(coords, fixedProbeDataSet);
		mFixedProbeDataSets.add(fixedProbeDataSet);
		
		return fixedProbeDataSet;
	}

	/**
	 * Creates a fixed probe dataset. This only exists to allow overloading by child classes - it should be called via 
	 * {@link DataSet#addFixedProbeDataSet(DataPointCoordinates)}
	 * @param coords
	 * @return
	 * @throws BackEndAPIException
	 */
	protected FixedProbeDataSet makeFixedProbeDataSet(DataPointCoordinates coords) throws BackEndAPIException {
		return new FixedProbeDataSet(mDataSetFile, coords, mConfigData);
	}
	
	/**
	 * Loads the fixed probe data sets for this data set
	 * @throws BackEndAPIException 
	 */
	public void loadFixedProbeDataSets() throws BackEndAPIException {
		synchronized (mDataLock) {
			mFixedProbeDataSets = FixedProbeDataSet.loadFixedProbeDataSets(mDataSetFile, mDataSetType);
			int numberOfFixedProbeDataSets = mFixedProbeDataSets.size();
			
			for (int i = 0; i < numberOfFixedProbeDataSets; ++i) {
				FixedProbeDataSet fixedProbeDataSet = (FixedProbeDataSet) mFixedProbeDataSets.elementAt(i);
				mFixedProbeDataSetsLookup.put(fixedProbeDataSet.getCoordinates(), fixedProbeDataSet);
			}
		}
	}
	
	/**
	 * Gets the unique identifier for this data set
	 * @return The unique identifier for this data set
	 */
	public DataSetUniqueId getUniqueId() {
		return mUniqueId;
	}
	
	/**
	 * Gets the data set type (@see {@link BackEndAPI#DST_SINGLE_PROBE})
	 * @return The date set type
	 */
	public DataSetType getDataSetType() {
		return mDataSetType;
	}
	
	/**
	 * Inner class providing a unique identifier for data sets
	 * @author MAJ727
	 *
	 */
	protected class DataSetUniqueId extends AbstractDataSetUniqueId {
		DataSetUniqueId() {
			super(mDataSetFile.getAbsolutePath());
		}
	}

	/**
	 * Gets a list of the fixed probe data sets for this data set
	 * @return The list of fixed probe data sets for this data set
	 */
	@SuppressWarnings("unchecked")
	public Vector<LinkedDataSet> getAllLinkedDataSets() {
		return (Vector<LinkedDataSet>) mFixedProbeDataSets.clone();
	}
	
	/**
	 * Gets the ids of the linked (fixed probe) data sets for this data set
	 * @return The ids of the linked data sets for this data set
	 */
	public Vector<AbstractDataSetUniqueId> getAllLinkedDataSetIds() {
		Vector<AbstractDataSetUniqueId> fixedProbeDataSetIds = new Vector<AbstractDataSetUniqueId>();
		
		synchronized (mDataLock) {
			int numberOfFixedProbeDataSets = mFixedProbeDataSets.size();
			
			for (int i = 0; i < numberOfFixedProbeDataSets; ++i) {
				fixedProbeDataSetIds.add(mFixedProbeDataSets.elementAt(i).getUniqueId());
			}
		}
		
		return fixedProbeDataSetIds;
	}

	/**
	 * Sets that a data point summary field is missing from the data point data (field has not been calculated)
	 * @param dpsIndex The key of the missing field
	 */
	public void setMissingDataPointSummaryFieldKey(DataPointSummaryIndex dpsIndex) {
		synchronized (mDataLock) {
			mMissingDataPointSummaryIndicesLookup.put(dpsIndex, dpsIndex);
		}
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Gets the lookup of keys of missing data point summary fields
	 * @return The lookup of keys or null if the lookup has not been initialised (in which case it should
	 * be assumed that all keys are missing) 
	 */
	public Hashtable<DataPointSummaryIndex, DataPointSummaryIndex> getMissingDataPointSummaryFieldKeys() {
		synchronized (mDataLock) {
			return (Hashtable<DataPointSummaryIndex, DataPointSummaryIndex>) mMissingDataPointSummaryIndicesLookup.clone();
		}
	}
	
	/**
	 * Calculate the specified data point summary field and any related fields
	 * @param dpsIndex The field to calculate (use BackEndAPI.DPS_KEY_U_TKE_FLUX for any third order correlation stuff)
	 * @param frontEndInterface
	 * @param progressInterface The interface to report progress
	 * @return The indices of the data point summary fields calculated
	 * @throws BackEndAPIException
	 */
	public Vector<DataPointSummaryIndex> calculateDataPointSummaryField(DataPointSummaryIndex dpsIndex, Frame frame, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		// TODO implementation
		Vector<DataPointSummaryIndex> calculatedFields = new Vector<DataPointSummaryIndex>(5);
		int numberOfCalculatedFields = 0;
		
		// If dpsIndex == null then calculate everything
		synchronized (mDataLock) {
			if (dpsIndex == null
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN)) {
				calculatedFields.add(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN);
				calculatedFields.add(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN);
				calculatedFields.add(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN);
			} 
			
			if (dpsIndex == null
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO)) {
				calculatedFields.add(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO);
				calculatedFields.add(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO);
				calculatedFields.add(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO);
				calculatedFields.add(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS);
				calculatedFields.add(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO);
				calculatedFields.add(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO);
				calculatedFields.add(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO);
			} 

			if (dpsIndex == null
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_RMS_U_PRIME)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_RMS_V_PRIME)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_RMS_W_PRIME)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_TKE)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_U_TKE_FLUX)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_V_TKE_FLUX)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_W_TKE_FLUX)) {
				calculatedFields.add(BackEndAPI.DPS_KEY_RMS_U_PRIME);
				calculatedFields.add(BackEndAPI.DPS_KEY_RMS_V_PRIME);
				calculatedFields.add(BackEndAPI.DPS_KEY_RMS_W_PRIME);
				calculatedFields.add(BackEndAPI.DPS_KEY_TKE);
				calculatedFields.add(BackEndAPI.DPS_KEY_U_TKE_FLUX);
				calculatedFields.add(BackEndAPI.DPS_KEY_V_TKE_FLUX);
				calculatedFields.add(BackEndAPI.DPS_KEY_W_TKE_FLUX);
			} 

			if (dpsIndex == null
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION)
				|| dpsIndex.equals(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION)) {
				calculatedFields.add(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION);
				calculatedFields.add(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION);
				calculatedFields.add(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION);
			}

			numberOfCalculatedFields = calculatedFields.size();
			
			for (int i = 0; i < numberOfCalculatedFields; ++i) {
				mMissingDataPointSummaryIndicesLookup.remove(calculatedFields.elementAt(i));
			}
			
			Vector<LinkedDataSet> linkedDataSets = getAllLinkedDataSets();
			int numberOfLinkedDataSets = linkedDataSets.size();
			
			for (int i = 0; i < numberOfLinkedDataSets; ++i) {
				linkedDataSets.elementAt(i).setMissingDataPointSummaryFieldKeys(mMissingDataPointSummaryIndicesLookup);
			}
			
			int numberOfDataPoints = getDataPointCount();
			int progress = 0;
			
			for (int i = 0; i < numberOfDataPoints; ++i) {
				DataPoint dataPoint = mData.get(i);
				dataPoint.loadDetailsAndRecalculateSummaryData(mDataSetFile);
				dataPoint.clearDetails();
				progress = (int) ((((double) (i + 1))/numberOfDataPoints)*100);
				progressInterface.setProgress(progress, "");
			}
		}	
			
		if (numberOfCalculatedFields > 0) {
			recalculateSummaryData(null, true, frontEndInterface, progressInterface);
		}
		
		return calculatedFields;
	}

	/**
	 * Creates a rotation correction batch from the specified data points and calculates their rotation corrections
	 * @param batchNumber The batch number for this batch
	 * @param yCoords The y-coordinates of the data points to assign to this batch
	 * @param zCoords The z-coordinates of the data points to assign to this batch
	 * @param frontEndInterface The interface to report to the front end
	 * @param progressInterface The interface used to report progress
	 * @throws BackEndAPIException
	 */
	public void createRotationCorrectionBatch(int batchNumber, Vector<Integer> yCoords, Vector<Integer> zCoords, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		int numberOfDataPoints = yCoords.size();
		Vector<DataPoint> dataPoints = new Vector<DataPoint>(numberOfDataPoints);
		double uBarSum = 0, vBarSum = 0, wBarSum = 0;
		int yCoord = -1, zCoord = -1;
		
		try {
			synchronized (mDataLock) {
				// Get all the data first to check it's available before changing any data
				for (int i = 0; i < numberOfDataPoints; ++i) {
					yCoord = yCoords.elementAt(i);
					zCoord = zCoords.elementAt(i);
					DataPoint dataPoint = getDataPoint(yCoord, zCoord); 
					uBarSum += dataPoint.getSummaryDataField(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_VELOCITY);
					vBarSum += dataPoint.getSummaryDataField(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_VELOCITY);
					wBarSum += dataPoint.getSummaryDataField(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_VELOCITY);
					dataPoints.add(dataPoint);
				}
			}
			
			double[] minimisationAngles = {0, 0, 0};
			MAJFCMaths.rotateToMinimise(uBarSum, vBarSum, wBarSum, -50, +50, minimisationAngles);
			
			for (int i = 0; i < numberOfDataPoints; ++i) {
				yCoord = yCoords.elementAt(i);
				zCoord = zCoords.elementAt(i);
				
				progressInterface.setProgress((int) (100 * (((double) i)/numberOfDataPoints)), makeKey(yCoord, zCoord));
				synchronized (mDataLock) {
					dataPoints.elementAt(i).setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_NUMBER, batchNumber);
					dataPoints.elementAt(i).setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_THETA_ROTATION_CORRECTION, minimisationAngles[0]);
					dataPoints.elementAt(i).setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_ALPHA_ROTATION_CORRECTION, minimisationAngles[1]);
					dataPoints.elementAt(i).setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_PHI_ROTATION_CORRECTION, minimisationAngles[2]);
					dataPoints.elementAt(i).loadDetailsAndRecalculateSummaryData(mDataSetFile);
					dataPoints.elementAt(i).clearDetails();
				}
			}
		} catch (BackEndAPIException theException) {
			throw theException;
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_TITLE), DAStrings.getString(DAStrings.ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_MSG, yCoord, zCoord));
		}
	}
		
	/**
	 * Create rotation correction batches from the batch.majrcb file in the data set's main directory
	 * batches.majrcb must have format:
	 * <batches>
	 * 		<batch batch_number="xx">
	 * 			<y_min>int</y_min>
	 * 			<y_max>int</y_max>
	 * 			<z_min>int</z_min>
	 * 			<z_max>int</z_max>
	 * 		</batch>
	 * 			...
	 * 		<batch batch_number="yy">
	 * 			<y_min>int</y_min>
	 * 			<y_max>int</y_max>
	 * 			<z_min>int</z_min>
	 * 			<z_max>int</z_max>
	 * 		</batch>
	 * </batches>  
	 * @throws BackEndAPIException
	 * @param frontEndInterface The interface to report to the front end
	 * @param progressInterface The interface used to report progress
	 */
	public void createRotationCorrectionBatchesFromFile(BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		String dataSetDirectory = makeDirectoryName(mDataSetFile.getAbsolutePath());
		StringBuffer batchesFile = new StringBuffer(dataSetDirectory);
		batchesFile.append(MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR);
		batchesFile.append(DADefinitions.ROTATION_CORRECTION_BATCHES_FILENAME);
		
		try {
			DocHandler docHandler = new MyRCBatchesDocHandler(frontEndInterface, progressInterface);
			BufferedReader fileReader = new BufferedReader(new FileReader(batchesFile.toString()));
			QDParser.parse(docHandler, fileReader);

			fileReader.close();
		} catch (FileNotFoundException theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ROTATION_CORRECTION_BATCHES_FILE_MISSING_TITLE), DAStrings.getString(DAStrings.ROTATION_CORRECTION_BATCHES_FILE_MISSING_MSG, dataSetDirectory));
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_TITLE), DAStrings.getString(DAStrings.ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_MSG, batchesFile.toString(), theException.getMessage()));
		} 
		
	}
	
	/**
	 * Handles the events from the XML parser
	 * 
	 * @author MAJ727
	 */
	private class MyRCBatchesDocHandler extends MyAbstractDocHandler {
		private String mBatchNumber;
		private Vector<Integer> mYMins, mYMaxes, mZMins, mZMaxes;
		private final BackEndCallbackInterface mFrontEndInterface;
		private final DAProgressInterface mProgressInterface;
		
		/**
		 * Constructor
		 * 
	 	 * @param frontEndInterface The interface to report to the front end
	 	 * @param progressInterface The interface used to report progress
		 */
		private MyRCBatchesDocHandler(BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) {
			super();
			mFrontEndInterface = frontEndInterface;
			mProgressInterface = progressInterface;
		}
		
		@Override
		public void startElement(String element, Hashtable<String, String> attributes) {
			super.startElement(element, attributes);
			
			if (element.equals(DADefinitions.XML_RCB_FILE_BATCH)) {
				mBatchNumber = attributes.get(DADefinitions.XML_RCB_FILE_BATCH_NUMBER);
				mYMins = new Vector<Integer>(5);
				mYMaxes = new Vector<Integer>(5);
				mZMins = new Vector<Integer>(5);
				mZMaxes = new Vector<Integer>(5);
			}
		}

		@Override
		public void elementValue(String value) {
			if (value.trim().isEmpty()) {
				return;
			}
			
			if (mStartElement.equals(DADefinitions.XML_RCB_FILE_Y_MIN)) {
				try {
					mYMins.add(MAJFCTools.parseInt(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (mStartElement.equals(DADefinitions.XML_RCB_FILE_Y_MAX)) {
				try {
					mYMaxes.add(MAJFCTools.parseInt(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (mStartElement.equals(DADefinitions.XML_RCB_FILE_Z_MIN)) {
				try {
					mZMins.add(MAJFCTools.parseInt(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (mStartElement.equals(DADefinitions.XML_RCB_FILE_Z_MAX)) {
				try {
					mZMaxes.add(MAJFCTools.parseInt(value));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void endElement(String element) throws BackEndAPIException {
			if (element.equals(DADefinitions.XML_RCB_FILE_BATCH)) {
				try {
					createRotationCorrectionBatch(MAJFCTools.parseInt(mBatchNumber), mYMins, mYMaxes, mZMins, mZMaxes, mFrontEndInterface, mProgressInterface);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Creates a rotation correction batch from all data points in the data set whose coordinates fall between
	 * the specified limits. The limits are passed in as a list of minimum y-coordinates, a list of maximum y-coordinates,
	 * a list of minimum z-coordinates and a list of maximum z-coordinates, with the i-th element in each list together representing
	 * the i-th set of limiting values.
	 * @param batchNumber The batch number for this batch
	 * @param yMins List of the minimum limit y-coordinates
	 * @param yMaxes List of the maximum limit y-coordinates
	 * @param zMins List of the minimum limit z-coordinates
	 * @param zMaxes List of the maximum limit z-coordinates
	 * @param frontEndInterface The interface to report to the front end
	 * @param progressInterface The interface used to report progress
	 * @throws BackEndAPIException
	 */
	private void createRotationCorrectionBatch(int batchNumber, Vector<Integer> yMins, Vector<Integer> yMaxes, Vector<Integer> zMins, Vector<Integer> zMaxes, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		synchronized (mDataLock) {
			Vector<Integer> yCoords = new Vector<Integer>(mData.size()), zCoords = new Vector<Integer>(mData.size());
			int numberOfYCoordsLists = mSortedYCoords.size();
			int numberOfLimitSets = yMins.size();
			
			// Check all the limit sets are complete (i.e. we have a yMin, yMax, zMin, zMax for all sets)
			if (yMaxes.size() != numberOfLimitSets || zMins.size() != numberOfLimitSets || zMaxes.size() != numberOfLimitSets) {
				throw new BackEndAPIException("", "");
			}
			
			for (int yIndex = 0; yIndex < numberOfYCoordsLists; ++yIndex) {
				int yCoord = mSortedYCoords.elementAt(yIndex);
				
				for (int limitSetIndex = 0; limitSetIndex < numberOfLimitSets; ++limitSetIndex) {
					if (yCoord < yMins.elementAt(limitSetIndex) || yCoord > yMaxes.elementAt(limitSetIndex)) {
						continue;
					}
				
					Vector<Integer> zCorrespondingCoords = mSortedZCoordsSets.elementAt(yIndex);
					int numberOfZCoords = zCorrespondingCoords.size();
					
					for (int zIndex = 0; zIndex < numberOfZCoords; ++zIndex) {
						int zCoord = zCorrespondingCoords.elementAt(zIndex);
						
						if (zCoord < zMins.elementAt(limitSetIndex) || zCoord > zMaxes.elementAt(limitSetIndex)) {
							continue;
						}
						
						// This coordinate pair is within yMin->yMax and zMin->zMax
						yCoords.add(yCoord);
						zCoords.add(zCoord);
					}
				}
			}
			
			createRotationCorrectionBatch(batchNumber, yCoords, zCoords, frontEndInterface, progressInterface);
		}
		
	}
	
	/**
	 * All files for this import session have been read
	 */
	public void importFromFileComplete() {
		updateCoordinateLists();
		
		synchronized (mDataLock) {
			Vector<LinkedDataSet> linkedDataSets = getAllLinkedDataSets();
			int numberOfLinkedDataSets = linkedDataSets.size();
			
			for (int i = 0; i < numberOfLinkedDataSets; ++i) {
				linkedDataSets.elementAt(i).updateCoordinateLists();
			}
		}
	}
	
	/**
	 * Gets the data point at the specified co-ordinates
	 * @return The required data point
	 */
	protected DataPoint getDataPoint(String yCoord, String zCoord) {
		return mDataHash.get(makeKey(yCoord, zCoord));
	}
	
	/**
	 * Gets the data point at the specified co-ordinates
	 * @return The required data point
	 */
	protected DataPoint getDataPoint(int yCoord, int zCoord) {
		return mDataHash.get(makeKey(yCoord, zCoord));
	}

	/**
	 * Trims a data point to the specified range
	 * @param yCoord
	 * @param zCoord
	 * @param startIndex
	 * @param endIndex
	 * @throws BackEndAPIException 
	 */
	public void trimDataPoint(int yCoord, int zCoord, int startIndex, int endIndex, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail) throws BackEndAPIException {
		synchronized (mDataLock) {
			Vector<LinkedDataSet> linkedDataSets = getAllLinkedDataSets();
			int numberOfLinkedDataSets = linkedDataSets.size();
			
			for (int i = 0; i < numberOfLinkedDataSets; ++i) {
				linkedDataSets.elementAt(i).trimDataPoint(yCoord, zCoord, startIndex, endIndex, dataPointSummary, dataPointDetail);
			}

			DataPoint dataPoint = getDataPoint(yCoord, zCoord);
			dataPoint.trim(mDataSetFile, startIndex, endIndex, dataPointSummary, dataPointDetail);
		}
	}
	
	/**
	 * Sets the sampling rate
	 * @param samplingRate
	 * @throws BackEndAPIException
	 */
	protected void setSamplingRate(double samplingRate) throws BackEndAPIException {
		DataSetConfig dsc = getConfigData();
		if (dsc.get(BackEndAPI.DSC_KEY_SAMPLING_RATE) == samplingRate) {
			return;
		}
		
		dsc.set(BackEndAPI.DSC_KEY_SAMPLING_RATE, samplingRate);
		DataSetConfigChangedFlags configChangedFlags = setConfigData(dsc);
		if (shouldDataPointsBeReloaded(configChangedFlags)) {
			recalculateSummaryData(configChangedFlags, false, null, null);
		}
	}

}
