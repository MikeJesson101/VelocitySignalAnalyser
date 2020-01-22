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


import java.io.File;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetailIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndCallbackInterface;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DAProgressInterface;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetConfigChangedFlags;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetail;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;


/**
 * @author MAJ727
 *
 */
public class MultiRunMeanValueDataSet extends DataSet {
	private Hashtable<Integer, LinkedDataSet> mRunDataSetsLookup = new Hashtable<Integer, LinkedDataSet>();
	private Vector<LinkedDataSet> mRunDataSets = new Vector<LinkedDataSet>();
	private Hashtable<String, DataPoint> mNewImportsLookup = new Hashtable<String, DataPoint>();

	/**
	 * Constructor - creates a new multi-run data set
	 * @param parentDataSetFile The parent data set's file
	 * @param coords The coordinates of the fixed probe
	 * @throws BackEndAPIException
	 */
	public MultiRunMeanValueDataSet(File dataSetFile, DataSetConfig configData, DataSetType dataSetType, int numberOfRuns) throws BackEndAPIException {
		super(dataSetFile, configData, dataSetType);
	}
	
	/**
	 * Constructor - creates a new fixed probe data set from the specified data file
	 * @param dataSetFile The data set's file
	 * @throws BackEndAPIException
	 */
	public MultiRunMeanValueDataSet(File dataSetFile, DataSetType dataSetType) throws Exception {
		super(dataSetFile, dataSetType);

		mRunDataSets = loadRunDataSets(dataSetFile);
		
		int numberOfRunDataSets = mRunDataSets.size();
		
		for (int i = 0; i < numberOfRunDataSets; ++i) {
			MultiRunRunDataSet runDataSet = (MultiRunRunDataSet) mRunDataSets.elementAt(i);
			mRunDataSetsLookup.put(runDataSet.getRunIndex(), runDataSet);
		}
	}
	
	/**
	 * Loads and creates the run datasets for the specified parent data set
	 * @param parentDataSetFile The parent data set's file
	 * @return A list of the run datasets for the parent DataSet
	 */
	public static Vector<LinkedDataSet> loadRunDataSets(File meanValuesDataSetFile) throws BackEndAPIException {
		Vector<LinkedDataSet> runDataSets = new Vector<LinkedDataSet>();
		
		File meanValuesDir = new File(makeDirectoryName(meanValuesDataSetFile.getAbsolutePath()));
		File[] fileList = meanValuesDir.listFiles();
		
		if (fileList == null) {
			return runDataSets;
		}
		
		for (int i = 0; i < fileList.length; ++i) {
			if (fileList[i].isDirectory() == true) {
				continue;
			}
			
			if (fileList[i].getName().indexOf(DADefinitions.MULTI_RUN_FILENAME_INSERT) < 0) {
				continue;
			}
			
			try {
				runDataSets.add(new MultiRunRunDataSet(fileList[i]));
			} catch (Exception theException) {
				continue;
			}
		}
		
		return runDataSets;
	}

	/**
	 * Gets the number of the runs for this dataset
	 * @return The number of the runs for this dataset
	 */
	public int getNumberOfRuns() {
		return mRunDataSets.size();
	}

	/**
	 * Gets the run data set for this data set for the specified run index
	 * @param runIndex The index of the required run
	 * @return The run data set
	 * @throws BackEndAPIException 
	 * @throws IOException 
	 */
	public LinkedDataSet getRunDataSet(int runIndex) throws BackEndAPIException {
		synchronized (mDataLock) {
			LinkedDataSet runDataSet = mRunDataSetsLookup.get(runIndex);
			
			if (runDataSet == null) {
				runDataSet = new MultiRunRunDataSet(mDataSetFile, runIndex, mConfigData);
				mRunDataSetsLookup.put(runIndex, runDataSet);
				mRunDataSets.add(runDataSet);
				
				runDataSet.saveToFile(DAFrame.getFrame().getBackEndAPICallBackAdapter(), null);
			}
			
			return runDataSet;
		}
	}
	
	/**
	 * Adds a data point to this data set. !!!Overwrites an existing data point at these coordinates!!!
	 * Maintains the mData vector in order (by y-coordinate then z-coordinate)
	 * @param dataPoint the data point to add
	 */
	public void addMeanValueDataPoint(int yCoord, int zCoord, GenericImportDetails importDetails) throws BackEndAPIException {
		DataPoint dataPoint = createNewDataPoint(yCoord, zCoord, importDetails, 0, this);

		dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, 0);
		dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, 0);
		dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, 0);

		synchronized (mDataLock) {
			mNewImportsLookup.put(makeKey(yCoord, zCoord), dataPoint);
			addDataPointImportedFromFileNoCheck(dataPoint);	
		}
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
			Enumeration<LinkedDataSet> runDataSets = mRunDataSetsLookup.elements();
			
			while (runDataSets.hasMoreElements()) {
//				fixedProbeDataSets.nextElement().saveToFile(mParent, mFrontEndInterface);
				DataSet runDataSet = runDataSets.nextElement();
				
//				if (runDataSet.getDataPointCount() == 0) {
//					runDataSet.delete();
//					mRunDataSetsLookup.remove(arg0)
//				}
				runDataSet.saveToFile(frontEndInterface, progressInterface);
			}
		}
		catch (Exception theException){
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_SAVING_DATA_SET_DIALOG_TITLE), DAStrings.getString(DAStrings.ERROR_SAVING_DATA_SET_DIALOG_MSG) + "\n\"" + theException.getMessage() + "\"");
		}

		super.saveToFile(frontEndInterface, progressInterface);
	}
	
	/**
	 * Gets a list of the run datasets for this mean value dataset
	 * @return The list of fixed probe data sets for this dataset
	 */
	public Vector<LinkedDataSet> getRunDataSets() {
		return mRunDataSets;
	}
	
	/**
	 * Gets the ids of the run data set ids for this data set
	 * @return The ids of the linked data sets for this data set
	 */
	public Vector<AbstractDataSetUniqueId> getRunDataSetIds() {
		synchronized (mDataLock) {
			int numberOfRunDataSets = mRunDataSets.size();
			Vector<AbstractDataSetUniqueId> runDataSetIds = new Vector<AbstractDataSetUniqueId>(numberOfRunDataSets);
			
			for (int i = 0; i < numberOfRunDataSets; ++i) {
				runDataSetIds.add(mRunDataSets.elementAt(i).getUniqueId());
			}
			
			return runDataSetIds;
		}
	}
	
	/**
	 * Gets a list of the run datasets for this mean value dataset
	 * @return The list of fixed probe data sets for this dataset
	 */
	@Override
	public Vector<LinkedDataSet> getAllLinkedDataSets() {
		Vector<LinkedDataSet> linkedDataSets = super.getAllLinkedDataSets();
		
		if (mRunDataSets != null) {
			linkedDataSets.addAll(mRunDataSets);
			
			int numberOfRuns = mRunDataSets.size();
			
			for (int i = 0; i < numberOfRuns; ++i) {
				linkedDataSets.addAll(mRunDataSets.elementAt(i).getAllLinkedDataSets());
			}
		}
		
		return linkedDataSets;
	}
	
	/**
	 * Gets the ids of the linked (run) data sets for this data set
	 * @return The ids of the linked data sets for this data set
	 */
	public Vector<AbstractDataSetUniqueId> getAllLinkedDataSetIds() {
		synchronized (mDataLock) {
			Vector<AbstractDataSetUniqueId> runDataSetIds = super.getAllLinkedDataSetIds();
			int numberOfRunDataSets = mRunDataSets.size();
			
			for (int i = 0; i < numberOfRunDataSets; ++i) {
				LinkedDataSet runDataSet = mRunDataSets.elementAt(i);
				runDataSetIds.add(runDataSet.getUniqueId());
				
				Vector<AbstractDataSetUniqueId> runLinkedDataSetIds = runDataSet.getAllLinkedDataSetIds();
				int numberOfRunLinkedDataSets = runLinkedDataSetIds.size();
				for (int j = 0; j < numberOfRunLinkedDataSets; ++j) {
					runDataSetIds.add(runLinkedDataSetIds.elementAt(j));
				}
			}
			
			return runDataSetIds;
		}
	}	
	
	/**
	 * @see DataSet#importFromFileComplete()
	 */
	@Override
	public void importFromFileComplete() {
		synchronized (mDataLock) {
			super.importFromFileComplete();
			calculateMeanValues(false);
			mNewImportsLookup.clear();
		}
	}

	/**
	 * Calculates the values for the mean-value datapoint.
	 * @param recalculateForAllPoints If false, values are only recalculated for those points which are known to have changed
	 */
	private void calculateMeanValues(boolean recalculateForAllPoints) {
		int numberOfRunDataSets = mRunDataSets.size();
		Vector<Integer> sortedYCoords = new Vector<Integer>();
		Vector<Vector<Integer>> sortedZCoordsLists = new Vector<Vector<Integer>>(100);
		getSortedDataPointCoordinates(sortedYCoords, sortedZCoordsLists);
		int numberOfYCoords = sortedYCoords.size();
		
		for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
			int yCoord = sortedYCoords.elementAt(yCoordIndex);
			Vector<Integer> sortedZCoords = sortedZCoordsLists.elementAt(yCoordIndex);
			int numberOfZCoords = sortedZCoords.size();
			
			for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
				int zCoord = sortedZCoords.elementAt(zCoordIndex);
				System.out.println("calculateMeanValues: " + yCoord + " " + zCoord);
				
				// Only do the points that have changed
				if ((recalculateForAllPoints == false) && (mNewImportsLookup.get(makeKey(yCoord, zCoord)) == null)) {
					continue;
				}
				
				DataPoint meanValueDataPoint = getDataPoint(yCoord, zCoord);
				Vector<DataPointSummaryIndex> summaryFieldKeys = null;
				int numberOfSummaryFields = 0;
				
				Vector<DataPointSummary> dataPointSummaryList = new Vector<DataPointSummary>(numberOfRunDataSets);
				Vector<DataPointDetail> dataPointDetailList = new Vector<DataPointDetail>(numberOfRunDataSets);
				Vector<Integer> validRunDataSetIndices = new Vector<Integer>(numberOfRunDataSets);
				String probeType = null;
				String probeId = null;
				String samplingRate = null;
				
				// Load data point summaries and details, and get a list of the data point summary keys
				// Load the probe details and check whether they are consistent
				for (int runIndex = 0; runIndex < numberOfRunDataSets; ++runIndex) {
					MultiRunRunDataSet runDataSet = (MultiRunRunDataSet) mRunDataSets.elementAt(runIndex); 
					dataPointSummaryList.add(new DataPointSummary());
					dataPointDetailList.add(new DataPointDetail());
					
					double runDataSetSamplingRate = runDataSet.getConfigData().get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
					if (mConfigData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE) != runDataSetSamplingRate) {
						try {
							setSamplingRate(runDataSetSamplingRate);
						} catch (BackEndAPIException e) {
							e.printStackTrace();
						}
					}
					
					try {
						runDataSet.loadDataPointDetails(yCoord, zCoord, dataPointSummaryList.elementAt(runIndex), dataPointDetailList.elementAt(runIndex), true);
//						runDataSet.clearDataPointDetails(yCoord, zCoord);
						probeType = checkForCompatibleProbeDetail(runDataSet, yCoord, zCoord, probeType, BackEndAPI.PD_KEY_PROBE_TYPE);
						probeId = checkForCompatibleProbeDetail(runDataSet, yCoord, zCoord, probeId, BackEndAPI.PD_KEY_PROBE_ID);
						samplingRate = checkForCompatibleProbeDetail(runDataSet, yCoord, zCoord, samplingRate, BackEndAPI.PD_KEY_SAMPLING_RATE);
						
						validRunDataSetIndices.add(runIndex);
					} catch (BackEndAPIException e) {
						continue;
					}
						
					if (summaryFieldKeys == null) {
						summaryFieldKeys = dataPointSummaryList.elementAt(runIndex).getKeys();
						numberOfSummaryFields = summaryFieldKeys.size();
					}
				}
				
				numberOfRunDataSets = validRunDataSetIndices.size();
				
				// Now add the time-series data
				int numberOfVelocities = 0;
				Vector<Double> uMeanTimeSeries = new Vector<Double>();
				Vector<Double> vMeanTimeSeries = new Vector<Double>();
				Vector<Double> wMeanTimeSeries = new Vector<Double>();
				Vector<Double> pressureMeanTimeSeries = new Vector<Double>();
				Vector<Integer> validValuesCounts = new Vector<Integer>();
				
				// Find the index to use for synchronising the runs
				int minSynchronisingIndex = Integer.MAX_VALUE, maxSynchronisingIndex = Integer.MIN_VALUE;
				Vector<Integer> synchronisingIndices = new Vector<Integer>(numberOfRunDataSets);
				
				for (int runIndexIndex = 0; runIndexIndex < numberOfRunDataSets; ++runIndexIndex) {
					int runIndex = validRunDataSetIndices.elementAt(runIndexIndex);
					Vector<Double> uTimeSeries = dataPointDetailList.elementAt(runIndex).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
					numberOfVelocities = uTimeSeries.size();

					synchronisingIndices.add(Math.max(dataPointSummaryList.elementAt(runIndex).get(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX).intValue(), 0));

					minSynchronisingIndex = Math.min(minSynchronisingIndex, synchronisingIndices.lastElement());
					maxSynchronisingIndex = Math.max(synchronisingIndices.lastElement(), maxSynchronisingIndex);
				} 
				
				int minUsableSeriesLength = Integer.MAX_VALUE;

				for (int runIndexIndex = 0; runIndexIndex < numberOfRunDataSets; ++runIndexIndex) {
					int runIndex = validRunDataSetIndices.elementAt(runIndexIndex);
					DataPointDetail dataPointDetail = dataPointDetailList.elementAt(runIndex);
					Vector<Double> uTimeSeries = dataPointDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
					Vector<Double> vTimeSeries = dataPointDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
					Vector<Double> wTimeSeries = dataPointDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
					Vector<Double> pressureTimeSeries = dataPointDetail.get(BackEndAPI.DPD_KEY_PRESSURE);
					boolean hasPressure = pressureTimeSeries.size() > 0;
					
					numberOfVelocities = uTimeSeries.size();
					
					int headSynchronisationOffset = synchronisingIndices.elementAt(runIndexIndex) - minSynchronisingIndex;
					int tailSynchronisationOffset = numberOfVelocities - (maxSynchronisingIndex - synchronisingIndices.elementAt(runIndexIndex));
					int usableSeriesLength = tailSynchronisationOffset - headSynchronisationOffset;
					
					minUsableSeriesLength = Math.min(minUsableSeriesLength, usableSeriesLength);

					for (int runSeriesIndex = headSynchronisationOffset, meanSeriesIndex = 0; runSeriesIndex < tailSynchronisationOffset && meanSeriesIndex < minUsableSeriesLength; runSeriesIndex++, ++meanSeriesIndex) {
						double uSum = 0, vSum = 0, wSum = 0, pressureSum = 0;
						double uValue = uTimeSeries.elementAt(runSeriesIndex);
						double vValue = vTimeSeries.elementAt(runSeriesIndex);
						double wValue = wTimeSeries.elementAt(runSeriesIndex);
						double pValue = hasPressure ? pressureTimeSeries.elementAt(runSeriesIndex) : Double.NaN;
						
						int validValuesSum = 0;
						
						if (runIndexIndex == 0) {
							uMeanTimeSeries.add(0d);
							vMeanTimeSeries.add(0d);
							wMeanTimeSeries.add(0d);
							
							if (hasPressure) {
								pressureMeanTimeSeries.add(0d);
							}
							
							validValuesCounts.add(0);
						}
						
						// Values of precisely zero are invalid
//						if (uValue != 0d) {
							uSum = uMeanTimeSeries.elementAt(meanSeriesIndex);
							vSum = vMeanTimeSeries.elementAt(meanSeriesIndex);
							wSum = wMeanTimeSeries.elementAt(meanSeriesIndex);
							if (hasPressure) {
								pressureSum = pressureMeanTimeSeries.elementAt(meanSeriesIndex);
							}
							
							validValuesSum = validValuesCounts.elementAt(meanSeriesIndex);
							
							uMeanTimeSeries.set(meanSeriesIndex, uSum + uValue);
							vMeanTimeSeries.set(meanSeriesIndex, vSum + vValue);
							wMeanTimeSeries.set(meanSeriesIndex, wSum + wValue);
							if (hasPressure) {
								pressureMeanTimeSeries.set(meanSeriesIndex, pressureSum + pValue);
							}
							
							validValuesCounts.set(meanSeriesIndex, ++validValuesSum);
//						}
					}
					
					// We've finished with this dataPointDetail so clear it to save memory.
					dataPointDetailList.set(runIndex, null);
					dataPointDetail = null;
				}
				
				// Write the run-mean values to the mean value dataset
				numberOfVelocities = uMeanTimeSeries.size();
				meanValueDataPoint.clearDetails();
				for (int i = 0; i < minUsableSeriesLength; i++) {
					try {
						double validValuesCount = (double) validValuesCounts.elementAt(i);
						double uMeanValue = validValuesCount == 0 ? 0 : uMeanTimeSeries.elementAt(i)/validValuesCount;
						double vMeanValue = validValuesCount == 0 ? 0 : vMeanTimeSeries.elementAt(i)/validValuesCount;
						double wMeanValue = validValuesCount == 0 ? 0 : wMeanTimeSeries.elementAt(i)/validValuesCount;
						meanValueDataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, uMeanValue, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION, uMeanValue, uMeanValue, uMeanValue);
						meanValueDataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, vMeanValue, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION, vMeanValue, vMeanValue, vMeanValue);;
						meanValueDataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, wMeanValue, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION, wMeanValue, wMeanValue, wMeanValue);
						
//						uMeanTimeSeries.set(i, uMeanValue);
//						vMeanTimeSeries.set(i, vMeanValue);
//						wMeanTimeSeries.set(i, wMeanValue);
//						
						if (pressureMeanTimeSeries.size() > 0) {
							double pMeanValue = validValuesCount == 0 ? 0 : pressureMeanTimeSeries.elementAt(i)/validValuesCount;
							meanValueDataPoint.addMeasuredPressure(pMeanValue);
//							
//							pressureMeanTimeSeries.set(i, pMeanValue);
						}
						
					} catch (BackEndAPIException e) {
							// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				// Don't calculate for the children here as we've already done it above if necessary
				meanValueDataPoint.calculate(false);
				
				// Override the run-mean values for each field with the mean of the run values
				for (int summaryFieldIndex = 0; summaryFieldIndex < numberOfSummaryFields; ++summaryFieldIndex) {
					DataPointSummaryIndex field = summaryFieldKeys.elementAt(summaryFieldIndex);
					
					if (overWriteSummaryField(field) == false) {
						continue;
					}
					
					double fieldSum = 0;
					
					for (int runIndexIndex = 0; runIndexIndex < numberOfRunDataSets; ++runIndexIndex) {
						fieldSum += dataPointSummaryList.elementAt(validRunDataSetIndices.elementAt(runIndexIndex)).get(field);
					}
					
					meanValueDataPoint.setSummaryDataField(field, numberOfRunDataSets == 0 ? 0 : fieldSum/((double) numberOfRunDataSets));
				}	
				
//				meanValueDataPoint.setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_U, Collections.max(uMeanTimeSeries)/numberOfRunDataSets);
//				meanValueDataPoint.setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_V, Collections.max(vMeanTimeSeries)/numberOfRunDataSets);
//				meanValueDataPoint.setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_W, Collections.max(wMeanTimeSeries)/numberOfRunDataSets);

				ProbeDetail probeDetails = new ProbeDetail();
				probeDetails.set(BackEndAPI.PD_KEY_PROBE_TYPE, probeType);
				probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, probeId);
				probeDetails.set(BackEndAPI.PD_KEY_SAMPLING_RATE, samplingRate);
				
				meanValueDataPoint.setProbeDetails(probeDetails);
				
				saveDataPointToTempFile(yCoord, zCoord);
				meanValueDataPoint.clearDetails();
			}
		}
	}

	private boolean overWriteSummaryField(DataPointSummaryIndex field) {
		if (field.equals(BackEndAPI.DPS_KEY_MAXIMUM_U) || field.equals(BackEndAPI.DPS_KEY_MAXIMUM_V) || field.equals(BackEndAPI.DPS_KEY_MAXIMUM_W)) {
			return false;
		}
		
		return true;
	}

	private String checkForCompatibleProbeDetail(MultiRunRunDataSet runDataSet, int yCoord, int zCoord, String currentValue, ProbeDetailIndex index) {
		final String VARIOUS = DAStrings.getString(DAStrings.VARIOUS);
		
		if (currentValue != null && currentValue.equals(VARIOUS)) {
			return currentValue;
		}
		
		String theValue;
		try {
			theValue = runDataSet.getProbeDetailsDataField(yCoord, zCoord, index);
		} catch (BackEndAPIException e) {
			return VARIOUS;
		}
		
		if (currentValue == null) {
			return theValue;
		}
		
		if (currentValue.equals(theValue) == false) {
			 return VARIOUS;
		}
		
		// theValue and currentValue should be the same
		return currentValue;
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
			DataPoint dataPoint = getDataPoint(yCoord, zCoord);
			return dataPoint.getSummaryDataField(field);
		}
	}
	
	@Override
	protected void additionalCalculations() {
		calculateMeanValues(true);
	}

	@Override
	protected boolean shouldDataPointsBeReloaded(DataSetConfigChangedFlags dataFieldsChangedFlags) {
		if (dataFieldsChangedFlags == null) {
			return false;
		}
		
		return super.shouldDataPointsBeReloaded(dataFieldsChangedFlags) || (dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE) == true)
				|| (dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION) == true)
				|| (dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS) == true)
				|| (dataFieldsChangedFlags.get(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE) == true);
	}
	
}
