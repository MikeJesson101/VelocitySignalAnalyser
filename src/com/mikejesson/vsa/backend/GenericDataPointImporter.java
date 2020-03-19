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
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author MAJ727
 *
 */
public class GenericDataPointImporter extends AbstractDataPointImporter {
	/**
	 * Constructor 
	 */
	public GenericDataPointImporter() {
	}

	/**
	 * Imports single U measurements from a CSV file.
	 * The file should consist of lines of 3 delimiter-separated values, e.g.:
	 * 		y-coord,z-coord,U
	 * 
	 * Loads the data point and then saves it to a temporary file (otherwise all velocity details have to be held
	 * on the heap, causing memory errors when importing lots of files at once
	 * 
	 * @param file The file to import from
	 * @param yCoords A list to add the extracted y-coordinates to
	 * @param zCoords A list to add the extracted y-coordinates to
	 * @param dataSet The data set to add the point to
	 * @param delimiter The delimiter used in the data file
	 * @throws BackEndAPIException
	 */
	public void importSingleUMeasurementsFromCSV(File file, Vector<Integer> yCoords, Vector<Integer> zCoords, DataSet dataSet, char delimiter) throws BackEndAPIException{
		try{
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedFileReader = new BufferedReader(fileReader);
			int readChar;
			
			while ((readChar = bufferedFileReader.read()) != -1){
				StringBuffer values = new StringBuffer();
				char theChar = (char)readChar;
				while (theChar != MAJFCTools.SYSTEM_NEW_LINE_CHAR
						&& !Character.toString(theChar).equals(MAJFCTools.SYSTEM_NEW_LINE_STRING)){
					if (Character.isDigit(theChar) || (theChar == '-') || (theChar == '.') || (theChar == delimiter)){
						values.append(theChar);
					} else if (theChar == MAJFCTools.SYSTEM_CARRIAGE_RETURN_CHAR){
						// Discard the carriage return
					} else {
						bufferedFileReader.close();
						throw new BackEndAPIException(DAStrings.getString(DAStrings.INVALID_CHARACTER_IN_CSV_FILE_TITLE), DAStrings.getString(DAStrings.INVALID_CHARACTER_IN_CSV_FILE_MSG) + ' ' + file.getAbsolutePath());
					}
					
					theChar = (char)bufferedFileReader.read();
				}
			
				// Ignore blank lines
				if (values.length() == 0) {
					continue;
				}
				
				// We should have a list of 3 comma or space-separated values
				StringTokenizer tokenizer = new StringTokenizer(values.toString(), MAJFCTools.stringValueOf(delimiter));
				if (tokenizer.countTokens() != 3){
					bufferedFileReader.close();
					throw new BackEndAPIException(DAStrings.getString(DAStrings.INVALID_CHARACTER_IN_CSV_FILE_TITLE), DAStrings.getString(DAStrings.INVALID_CHARACTER_IN_CSV_FILE_MSG) + file.getAbsolutePath());
				}

				Integer yCoord = MAJFCTools.parseInt(tokenizer.nextToken());
				Integer zCoord = MAJFCTools.parseInt(tokenizer.nextToken());
				
				DataPoint dataPoint = dataSet.createNewDataPoint(yCoord, zCoord, null, 0, dataSet);

				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, MAJFCTools.parseDouble(tokenizer.nextToken()));
				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, 0);
				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, 0);

				dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				dataPoint.calculate();
				dataPoint.clearDetails();
				
				yCoords.add(yCoord);
				zCoords.add(zCoord);
			}

			bufferedFileReader.close();

			dataSet.importFromFileComplete();
		}
		catch (Exception theException){
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}

	/**
	 * Imports measured velocities for the data point from a CSV file.
	 * The file should consist of lines of 3 delimiter-separated values, e.g.:
	 * 		1111,22222,333
	 * 		44,55,666
	 * Loads the data point and then saves it to a temporary file (otherwise all velocity details have to be held
	 * on the heap, causing memory errors when importing lots of files at once
	 * 
	 * @param file The file to import from
	 * @param importDetails The import details
	 * @throws BackEndAPIException if mainProbeIndex >= coords.length, or fixedProbeIndex >= coords.length, or mainProbeIndex == fixedProbeIndex, or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromCSVFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
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
			String theCSVFormat = configData.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT).trim();
			if (theCSVFormat.length() == 0 || theCSVFormat.equals(DAStrings.getString(DAStrings.ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL))) {
				theCSVFormat = "x,y,z";
			}
			StringTokenizer stCSVFormat = new StringTokenizer(theCSVFormat, ",");
			int numberOfFields = stCSVFormat.countTokens();
			Vector<String> theCSVFields = new Vector<String>(numberOfFields);
			
			while (stCSVFormat.hasMoreTokens()) {
				theCSVFields.add(stCSVFormat.nextToken().trim());
			}
			
			String line;
			
			while ((line = bufferedFileReader.readLine()) != null) {
				// Strip leading white-space
				String theValues = line.trim();
				
				// Ignore blank lines
				if (theValues.length() == 0) {
					continue;
				}
				
				// We should have a list of NUMBER_OF_COMPONENTS * numberOfProbes comma or space-separated values,
				// or numberOfProbes values if the file only contains u values
				StringTokenizer tokenizer = new StringTokenizer(theValues, MAJFCTools.stringValueOf(importDetails.mDelimiter));
				int numberOfTokens = tokenizer.countTokens();
				
				if ((numberOfTokens != numberOfFields * numberOfProbes) && (numberOfTokens != numberOfProbes)) {
					fileReader.close();
					bufferedFileReader.close();
					throw new BackEndAPIException(DAStrings.getString(DAStrings.INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE), DAStrings.getString(DAStrings.INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG) + importDetails.mFile.getAbsolutePath());
				}
				
				// Do we only have u values?
				boolean uOnly = numberOfProbes == numberOfTokens;
				if (uOnly) {
					numberOfFields = 1;
				}
				
				// List of velocity, SNR and correlation measurements in the x, y and z directions.
				final int NUMBER_OF_COMPONENTS = 3;
				Vector<Vector<Double>> velocities = new Vector<Vector<Double>>(NUMBER_OF_COMPONENTS);
				Vector<Vector<Double>> theSNRs = new Vector<Vector<Double>>(NUMBER_OF_COMPONENTS);
				Vector<Vector<Double>> correlations = new Vector<Vector<Double>>(NUMBER_OF_COMPONENTS);
				
				for (int i = 0; i < NUMBER_OF_COMPONENTS; ++i) {
					velocities.add(new Vector<Double>(5));
					theSNRs.add(new Vector<Double>(5));
					correlations.add(new Vector<Double>(5));
				}
				
				boolean invalidLine = false;
				
				for (int probeIndex = 0; probeIndex < numberOfProbes; ++probeIndex) {
					for (int fieldIndex = 0; fieldIndex < numberOfFields; ++fieldIndex) {
						Double fieldValue = MAJFCTools.parseDouble(tokenizer.nextToken(), importDetails.mDecimalSeparator);

						if (fieldValue.equals(Double.NaN)) {
							// Discard lines of invalid data
							invalidLine = true;
							break;
						}
						
						int componentIndex = -1;
						String fieldCode = uOnly ? Character.toString(DADefinitions.FIELD_COMPONENT_SPECIFIER_U) : theCSVFields.get(fieldIndex);
						// Field codes are x, y, z, xs, ys, zs, xc, yc, zc
						switch (fieldCode.charAt(0)) {
							case DADefinitions.FIELD_COMPONENT_SPECIFIER_IGNORE:
							case DADefinitions.FIELD_SPECIFIER_SNR:
							case DADefinitions.FIELD_SPECIFIER_CORR:
								continue;
							case DADefinitions.FIELD_COMPONENT_SPECIFIER_U:
								componentIndex = 0;
								break;
							case DADefinitions.FIELD_COMPONENT_SPECIFIER_V:
								componentIndex = 1;
								break;
							case DADefinitions.FIELD_COMPONENT_SPECIFIER_W:
								componentIndex = 2;
								break;
							default:
								throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + "Invalid CSV file format: " + theCSVFormat + "\"");									
						}
						
						if (fieldCode.length() == 1) {
							// This is a velocity
							velocities.get(componentIndex).add(componentIndex > 0 && uOnly ? 0d : fieldValue);
						} else {
							switch (fieldCode.charAt(1)) {
								case DADefinitions.FIELD_SPECIFIER_SNR:
									theSNRs.get(componentIndex).add(fieldValue);
									break;
								case DADefinitions.FIELD_SPECIFIER_CORR:
									correlations.get(componentIndex).add(fieldValue);
									break;
								default:
									throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + "Invalid CSV file format: " + theCSVFormat + "\"");									
							}
						}
	
					}
					
					if (invalidLine) {
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

					double wDiff = velocities.get(2).size() != 2 ? DADefinitions.INVALID_SNR_OR_CORRELATION : Math.abs(velocities.get(2).get(0) - velocities.get(2).get(1));
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, MAJFCMaths.mean(velocities.get(0)), theSNRs.get(0).size() == 0 ? DADefinitions.INVALID_SNR_OR_CORRELATION : MAJFCMaths.mean(theSNRs.get(0)), correlations.get(0).size() == 0 ? DADefinitions.INVALID_SNR_OR_CORRELATION : MAJFCMaths.mean(correlations.get(0)), wDiff);
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, uOnly ? 0d : MAJFCMaths.mean(velocities.get(1)), theSNRs.get(1).size() == 0 ? DADefinitions.INVALID_SNR_OR_CORRELATION : MAJFCMaths.mean(theSNRs.get(1)), correlations.get(1).size() == 0 ? DADefinitions.INVALID_SNR_OR_CORRELATION : MAJFCMaths.mean(correlations.get(1)), wDiff);
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, uOnly ? 0d : MAJFCMaths.mean(velocities.get(2)), theSNRs.get(2).size() == 0 ? DADefinitions.INVALID_SNR_OR_CORRELATION : MAJFCMaths.mean(theSNRs.get(2)), correlations.get(2).size() == 0 ? DADefinitions.INVALID_SNR_OR_CORRELATION : MAJFCMaths.mean(correlations.get(2)), wDiff);
					
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
			
			// Need a separate loop for this so as to ensure that the fixed probe data has been read and stored as well to allow
			// the correlation calculations to complete correctly
			for (int dataPointIndex = 0; dataPointIndex < numberOfProbes; ++dataPointIndex) {
				DataPoint dataPoint = dataPoints.elementAt(dataPointIndex);
				
				// Fixed probe data points should have their calculations done via their corresponding mobile probe data point
				if (dataPointIndex != importDetails.mFixedProbeIndex) {
					dataPoint.calculate();
					dataPoint.clearDetails();
				}
			}
			
			return addedDataPoints;
		} catch (Exception theException){
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
	public void importMultiRunDataPointDataFromCSVFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return importDataPointDataFromCSVFile(dataSet, importDetails);
			}
		});
	}
	
	/**
	 * Helper class
	 */
	public class GenericImportDetails {
		final File mFile;
		final LinkedList<DataPointCoordinates> mCoordsList;
		final double mTheta;
		final double mPhi;
		final double mAlpha;
		final char mDelimiter;
		final char mDecimalSeparator;
		final int mMainProbeIndex;
		final int mFixedProbeIndex;
		final int mSynchParentProbeIndex;
		
		public GenericImportDetails(File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, char delimiter, int mainProbeIndex, int fixedProbeIndex) {
			this(file, coordsList, theta, phi, alpha, delimiter, '.', mainProbeIndex, fixedProbeIndex, -1);
		}
		
		public GenericImportDetails(File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, char delimiter, int mainProbeIndex, int fixedProbeIndex, int synchParentProbeIndex) {
			this(file, coordsList, theta, phi, alpha, delimiter, '.', mainProbeIndex, fixedProbeIndex, synchParentProbeIndex);
		}
	
		public GenericImportDetails(File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, char delimiter, char decimalSeparator, int mainProbeIndex, int fixedProbeIndex) {
			this(file, coordsList, theta, phi, alpha, delimiter, decimalSeparator, mainProbeIndex, fixedProbeIndex, -1);
		}
	
		public GenericImportDetails(File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, char delimiter, char decimalSeparator, int mainProbeIndex, int fixedProbeIndex, int synchParentProbeIndex) {
			mFile = file;
			mCoordsList = coordsList;
			mTheta = theta;
			mPhi = phi;
			mAlpha = alpha;
			mDelimiter = delimiter;
			mDecimalSeparator = decimalSeparator;
			mMainProbeIndex = mainProbeIndex;
			mFixedProbeIndex = fixedProbeIndex;
			mSynchParentProbeIndex = synchParentProbeIndex;
		}
		
		public int getFirstYCoord() {
			return mCoordsList.getFirst().getY();
		}
		
		public int getFirstZCoord() {
			return mCoordsList.getFirst().getZ();
		}

		public File getFile() {
			return mFile;
		}

	}

	public interface ImportCommand {
		public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException;
	}
}
