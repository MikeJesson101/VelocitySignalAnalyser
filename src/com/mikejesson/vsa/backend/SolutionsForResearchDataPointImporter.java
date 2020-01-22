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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.backend.GenericDataPointImporter.ImportCommand;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;

/**
 * @author MAJ727
 *
 */
public class SolutionsForResearchDataPointImporter extends AbstractDataPointImporter {

	/**
	 * Constructor 
	 */
	public SolutionsForResearchDataPointImporter() {
	}
	
	/**
	 * Imports measured pressures from the UoB DPMS.
	 * 
	 * @param file The file to import from
	 * @param importDetails The import details
	 * @throws BackEndAPIException if mainProbeIndex >= coords.length, or fixedProbeIndex >= coords.length, or mainProbeIndex == fixedProbeIndex, or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromUoBDPMSFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		validateMainAndFixedProbeIndices(importDetails);
		
		FixedProbeDataSet fixedProbeDataSet = getFixedProbeDataSet(importDetails.mMainProbeIndex, importDetails.mFixedProbeIndex, importDetails.mCoordsList, dataSet);
		int numberOfProbes = importDetails.mCoordsList.size();
		Vector<DataPoint> dataPoints = new Vector<DataPoint>(numberOfProbes);
		Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>(10);
		
		try{
			for (int probeIndex = 0; probeIndex < numberOfProbes; ++probeIndex) {
				DataPointCoordinates coord = importDetails.mCoordsList.get(probeIndex);
				DataSet theDataSet = dataSet;
				
				// If this is the fixed probe data point, it is referenced using the main probe coordinates
				if (probeIndex == importDetails.mFixedProbeIndex) {
					coord = importDetails.mCoordsList.get(importDetails.mMainProbeIndex);
					theDataSet = fixedProbeDataSet;
				}
				
				DataPoint dataPoint = theDataSet.createNewDataPoint(coord.getY(), coord.getZ(), importDetails, probeIndex, theDataSet);
				dataPoints.add(dataPoint);

				// Maintain a list of added data points for return
				if (probeIndex != importDetails.mFixedProbeIndex) {
					addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
				}
			}
			
			FileReader fileReader = new FileReader(importDetails.mFile);
			BufferedReader bufferedFileReader = new BufferedReader(fileReader);
			int[] numberOfMeasurements = new int[numberOfProbes];
			DataSetConfig configData = dataSet.getConfigData();
			double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
			Double measurementsPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
			String line;
			boolean inZeroReadings = false;
			double[] meanZeroReadings = new double[numberOfProbes];
			Vector<Vector<Double>> zeroReadingsLists = new Vector<Vector<Double>>(numberOfProbes);
			for (int probeIndex = 0; probeIndex < numberOfProbes; ++probeIndex) {
				zeroReadingsLists.add(new Vector<Double>());
			}
			
			while ((line = bufferedFileReader.readLine()) != null) {
				// Strip leading white-space
				String theValues = line.trim();
				
				// Ignore blank lines
				if (theValues.length() == 0) {
					continue;
				}
				
				if (theValues.startsWith(DADefinitions.SFR_ZERO_READINGS)) {
					// The file should start with a list of zero readings.
					inZeroReadings = true;
					
					continue;
				} else if (theValues.startsWith(DADefinitions.SFR_READINGS)) {
					// The actual readings follow the zero readings. Work out the mean zero reading for each probe for
					// application to the readings.
					inZeroReadings = false;
					
					for (int i = 0; i < numberOfProbes; ++i) {
						meanZeroReadings[i] = MAJFCMaths.mean(zeroReadingsLists.elementAt(i));
					}
					
					continue;
				}
				
				final int NUMBER_OF_COMPONENTS = 1;
				// We should have a list of NUMBER_OF_COMPONENTS * numberOfProbes + 1 comma separated values (+1 as there is an index column)
				StringTokenizer tokenizer = new StringTokenizer(theValues, MAJFCTools.stringValueOf(importDetails.mDelimiter));
				int numberOfTokens = tokenizer.countTokens();
				
				if ((numberOfTokens != (NUMBER_OF_COMPONENTS * numberOfProbes) + 1) && (numberOfTokens != numberOfProbes + 1)) {
					fileReader.close();
					bufferedFileReader.close();
					throw new BackEndAPIException(DAStrings.getString(DAStrings.INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE), DAStrings.getString(DAStrings.INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG) + importDetails.mFile.getAbsolutePath());
				}

				// Scrap the index column
				tokenizer.nextToken();
				
				for (int probeIndex = 0; probeIndex < numberOfProbes; ++probeIndex) {
					Double pressure = MAJFCTools.parseDouble(tokenizer.nextToken());

					if (pressure.equals(Double.NaN)) {
						// Discard lines of invalid data
						continue;
					}

					if (inZeroReadings) {
						zeroReadingsLists.elementAt(probeIndex).add(pressure);
						
						continue;
					}

					DataPoint dataPoint = dataPoints.elementAt(probeIndex);
					
					// Do this here (rather than after the increment of numberOfMeasurements) so that we only create a new data point
					// if there are measurements to go in it
					// If we're splitting the data up, check whether we need to create a new data point
					if (numberOfMeasurements[probeIndex] > 0 && measurementsPerSplit.equals(Double.NaN)  == false && (numberOfMeasurements[probeIndex] % measurementsPerSplit == 0)) {
						if (probeIndex == importDetails.mFixedProbeIndex) {
							fixedProbeDataSet.addDataPointImportedFromFileNoCheck(dataPoint);
						} else {
							dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
						}
						
						// If we have a fixed probe, do its calculate via the main probe, but only once both are full
						if (importDetails.mFixedProbeIndex != -1) {
							if (probeIndex == importDetails.mMainProbeIndex) {
								if (numberOfMeasurements[importDetails.mFixedProbeIndex] > numberOfMeasurements[probeIndex]) {
									dataPoint.calculate();
									dataPoint.clearDetails();
								}
							} else if (probeIndex == importDetails.mFixedProbeIndex) {
								if (numberOfMeasurements[importDetails.mMainProbeIndex] > numberOfMeasurements[probeIndex]) {
									// The main probe data point has already been added to the data set, so the one in
									// dataPointLookup is the new one for the next split - retrieve the old one
									DataPoint mainProbeDP = dataSet.getDataPoint(dataPoint.getYCoord(), dataPoint.getZCoord());
									mainProbeDP.calculate();
									mainProbeDP.clearDetails();
								}
							}
						} else {
							dataPoint.calculate();
							dataPoint.clearDetails();
						}
						
						// If this is a fixed probe data point it is referenced using the main probe co-ordinates
						DataPointCoordinates coord = probeIndex == importDetails.mFixedProbeIndex ? importDetails.mCoordsList.get(importDetails.mMainProbeIndex) : importDetails.mCoordsList.get(probeIndex);
						
						dataPoint = dataSet.createNewDataPoint(coord.getY() + (int) (numberOfMeasurements[probeIndex]/samplingRate), coord.getZ(), importDetails, probeIndex, probeIndex == importDetails.mFixedProbeIndex ? fixedProbeDataSet : dataSet);
						dataPoints.set(probeIndex, dataPoint);
						
						// Maintain a list of added data points for return
						if (probeIndex != importDetails.mFixedProbeIndex) {
							addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
						}
					}
					
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, pressure - meanZeroReadings[probeIndex]);
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, 0);
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, 0);
					
					++numberOfMeasurements[probeIndex];
				}
			}

			bufferedFileReader.close();

			// All data read, now add the data points to the data set
			for (int dataPointIndex = 0; dataPointIndex < numberOfProbes; ++dataPointIndex) {
				DataPoint dataPoint = dataPoints.elementAt(dataPointIndex);
				
				if (dataPointIndex == importDetails.mFixedProbeIndex) {
					fixedProbeDataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				} else {
					dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				}
			}
			
			// Need a separate loop for this so as to ensure that the synch parent probe data has been read and stored before doing any calculation
			DataPoint dataPoint = null;
			
			if (importDetails.mSynchParentProbeIndex != -1) {
				dataPoint = dataPoints.elementAt(importDetails.mSynchParentProbeIndex);
				dataPoint.calculate(false);
				dataPoint.clearDetails();
			}
			
			for (int dataPointIndex = 0; dataPointIndex < numberOfProbes; ++dataPointIndex) {
				dataPoint = dataPoints.elementAt(dataPointIndex);
				
				// Synch parent probe data points should have their calculations done first, fixed probe data points should have their calculation done via the corresponding mobile probe data point
				if (dataPointIndex != importDetails.mFixedProbeIndex && dataPointIndex != importDetails.mSynchParentProbeIndex) {
					dataPoint.calculate(false);
					dataPoint.clearDetails();
				}
			}
			
			return addedDataPoints;
		}
		catch (Exception theException){
			theException.printStackTrace();
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}

	/**
	 * Imports a CSV multi-run data file
	 * 
	 * @param file The file to import
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta
	 * @param phi
	 * @param alpha
	 * @param dataSet
	 * @param delimiter
	 * @throws BackEndAPIException
	 */
	public void importMultiRunDataPointDataFromUoBDPMSFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return importDataPointDataFromUoBDPMSFile(dataSet, importDetails);
			}
		});
	}
}
