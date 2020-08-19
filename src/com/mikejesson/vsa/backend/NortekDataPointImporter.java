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


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.majfc.helpers.MAJFCTools.MAJFCToolsEoFException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.backend.GenericDataPointImporter.ImportCommand;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.MyAbstractDocHandler;

import qdxml.QDParser;



/**
 * @author MAJ727
 *
 */
public class NortekDataPointImporter extends AbstractDataPointImporter {
	private final int X_INDEX = 0;
	private final int Y_INDEX = 1;
	private final int Z1_INDEX = 2;
	private final int Z2_INDEX = 3;

	/**
	 * Constructor 
	 */
	public NortekDataPointImporter() {
	}
	
	/**
	 * Imports point data from a Vectrino file. The file must have an associated .hdr header file with the same name
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param dataSet The data set to add the point to
	 * @param delimiter The delimiter used in the data file
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromNDVFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
		final int X_VELOCITY_INDEX = 0;
		final int Y_VELOCITY_INDEX = 1;
		final int Z1_VELOCITY_INDEX = 2;
		final int Z2_VELOCITY_INDEX = 3;
		final int X_CORRELATION_INDEX = 4;
		final int Y_CORRELATION_INDEX = 5;
		final int Z1_CORRELATION_INDEX = 6;
		final int Z2_CORRELATION_INDEX = 7;
		final int X_SNR_INDEX = 8;
		final int Y_SNR_INDEX = 9;
		final int Z1_SNR_INDEX = 10;
		final int Z2_SNR_INDEX = 11;

		String dataFileName = importDetails.mFile.getAbsolutePath();
		Integer[] fieldIndices = new Integer[Z2_SNR_INDEX + 1];
		// We need this as the first fields in the data file are optional, so we may need to index relative to the last field
		Integer[] reverseFieldIndices = new Integer[fieldIndices.length];
		int maxReverseFieldIndex = -1;
		
		DataSetConfig configData = dataSet.getConfigData();
		double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
		String probeType = "";
		String probeId = "";
		Double measurementsPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
		
		// Read the header file to ascertain the position of the velocity data in the data file
		BufferedReader bufferedFileReader = null;
		try{
			String headerFileName = dataFileName.substring(0, dataFileName.lastIndexOf('.')) + DADefinitions.NDV_HEADER_FILE_EXTENSION;
			FileReader headerFileReader = new FileReader(headerFileName);
			bufferedFileReader = new BufferedReader(headerFileReader);
			boolean inDataFieldList = false;
			int numberOfFields = 0;
			String line;
			
			while ((line = bufferedFileReader.readLine()) != null){
				// Ignore blank lines
				if (line.length() == 0) {
					continue;
				}

				// Check if this is a Vectrino-II file
				if (line.contains(DADefinitions.VECTRINO_II_INSTRUMENT_NAME_TAG) && (line.contains(DADefinitions.VECTRINO_II_INSTRUMENT_NAME) || line.contains(DADefinitions.VECTRINO_PROFILER_INSTRUMENT_NAME))) {
					headerFileReader.close();
					return importDataPointDataFromConvertedVectrinoIIFile(dataSet, importDetails);
				}
				
				// Extract sampling rate
				if (line.contains(DADefinitions.NDV_SAMPLING_RATE_HEADER_IDENTIFIER)) {
					String samplingRateStr = extractValueFromHeaderLine(DADefinitions.NDV_SAMPLING_RATE_HEADER_IDENTIFIER, line);

					StringTokenizer tokenizer = new StringTokenizer(samplingRateStr, DADefinitions.VECTRINO_DATA_FILE_DELIMITER);
					samplingRate = Double.parseDouble(tokenizer.nextToken());
					
					// There may be units appended to the value
					if (tokenizer.countTokens() == 2) {
						String units = tokenizer.nextToken();
						if (units.equals(DADefinitions.KILOHERTZ)) {
							samplingRate *= 1000;
						}
					}

					dataSet.setSamplingRate(samplingRate);
					
					continue;
				}
				
//				if (line.toString().contains(importDetails.mFile.getName())) {
				if (line.contains(DADefinitions.NDV_DATA_FILE_EXTENSION)) {
					inDataFieldList = true;
					continue;
				}
				
				if (line.contains(DADefinitions.NDV_PROBE_TYPE_IDENTIFIER) && probeType.length() == 0) {
					probeType = extractValueFromHeaderLine(DADefinitions.NDV_PROBE_TYPE_IDENTIFIER, line);
					
					continue;
				}

				if (line.contains(DADefinitions.NDV_PROBE_ID_IDENTIFIER)) {
					String thisProbeId = extractValueFromHeaderLine(DADefinitions.NDV_PROBE_ID_IDENTIFIER, line.toString());
					probeId += probeId.length() == 0 ? thisProbeId : '/' + thisProbeId;

					continue;
				}

				// Only carry on if we are in the data field list part of the header file
				if (inDataFieldList == false) {
					continue;
				}
				
				// We should have a list of at least 3 space-separated values.
				// The field name may span multiple values, so assume:
				// 	<field index> <name part 1> <name part 2>... ...<name final part> <units>
				StringTokenizer tokenizer = new StringTokenizer(line.toString(), DADefinitions.VECTRINO_DATA_FILE_DELIMITER);
				if (tokenizer.countTokens() < 2){
					break;
				}
				
				int fieldIndex = -1;
				++numberOfFields;
				
				try {
					fieldIndex = MAJFCTools.parseInt(tokenizer.nextToken()) - 1;
				} catch (Exception e) {
					continue;
				}
				
				StringBuffer fieldNameBuffer = new StringBuffer(tokenizer.nextToken());
				String units = null;
				
				while (tokenizer.hasMoreTokens()) {
					units = tokenizer.nextToken();
					
					// If there are more tokens then "units" is actually another field name part
					if (tokenizer.hasMoreTokens()) {
						fieldNameBuffer.append(' ');
						fieldNameBuffer.append(units);
					}
				}
				
				String fieldName = fieldNameBuffer.toString();
				
				if (fieldName.startsWith(DADefinitions.NDV_X_VELOCITY_HEADER_IDENTIFIER)) {
					fieldIndices[X_VELOCITY_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Y_VELOCITY_HEADER_IDENTIFIER)) {
					fieldIndices[Y_VELOCITY_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Z1_VELOCITY_HEADER_IDENTIFIER)) {
					fieldIndices[Z1_VELOCITY_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Z2_VELOCITY_HEADER_IDENTIFIER)) {
					fieldIndices[Z2_VELOCITY_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_X_CORRELATION_HEADER_IDENTIFIER)) {
					fieldIndices[X_CORRELATION_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Y_CORRELATION_HEADER_IDENTIFIER)) {
					fieldIndices[Y_CORRELATION_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Z1_CORRELATION_HEADER_IDENTIFIER)) {
					fieldIndices[Z1_CORRELATION_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Z2_CORRELATION_HEADER_IDENTIFIER)) {
					fieldIndices[Z2_CORRELATION_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_X_SNR_HEADER_IDENTIFIER)) {
					fieldIndices[X_SNR_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Y_SNR_HEADER_IDENTIFIER)) {
					fieldIndices[Y_SNR_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Z1_SNR_HEADER_IDENTIFIER)) {
					fieldIndices[Z1_SNR_INDEX] = fieldIndex;
				} else if (fieldName.startsWith(DADefinitions.NDV_Z2_SNR_HEADER_IDENTIFIER)) {
					fieldIndices[Z2_SNR_INDEX] = fieldIndex;
				}
			}	
			
			bufferedFileReader.close();
			
			// Check that we have all the necessary field indices
			for (int i = 0; i < fieldIndices.length; ++i) {
				if (fieldIndices[i] == null) {
					// If there's only one z-velocity, the z2 indices can legitimately be null
					if (i == Z2_VELOCITY_INDEX || i == Z2_CORRELATION_INDEX || i == Z2_SNR_INDEX) {
						reverseFieldIndices[i] = -1;
						continue;
					}
					
					throw new BackEndAPIException(null, null);
				}
				
				reverseFieldIndices[i] = numberOfFields - fieldIndices[i] - 1;
				
				if (reverseFieldIndices[i] > maxReverseFieldIndex) {
					maxReverseFieldIndex = reverseFieldIndices[i];
				}
			}
		} catch (Exception e) {
			try {
				bufferedFileReader.close();
			} catch (IOException e1) {
			}
			throw new BackEndAPIException(DAStrings.getString(DAStrings.VECTRINO_HEADER_FILE_MISSING_TITLE), DAStrings.getString(DAStrings.VECTRINO_HEADER_FILE_MISSING_MSG) + dataFileName);
		}

		ProbeDetail probeDetails = new ProbeDetail();
		probeDetails.set(BackEndAPI.PD_KEY_PROBE_TYPE, probeType);
		probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, probeId);
		probeDetails.set(BackEndAPI.PD_KEY_SAMPLING_RATE, String.valueOf(samplingRate));

		Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>(10);

		// Now read the Vectrino data file
		try {
			DataPoint dataPoint = dataSet.createNewDataPoint(importDetails.getFirstYCoord(), importDetails.getFirstZCoord(), importDetails, 0, dataSet);
			FileReader fileReader = new FileReader(importDetails.mFile);
			bufferedFileReader = new BufferedReader(fileReader);
			String readLine;
			int numberOfMeasurements = 0;
			
			while ((readLine = bufferedFileReader.readLine()) != null){
				// Ignore blank lines
				if (readLine.length() == 0) {
					continue;
				}
				
				// We should have a list of at least (maxVelocityIndex + 1) values separated by delimiter (...+ 1 as indices start at 0)
				StringTokenizer tokenizer = new StringTokenizer(readLine, DADefinitions.VECTRINO_DATA_FILE_DELIMITER);
				int reverseCountStartValue = tokenizer.countTokens() - 1;
				
				if (tokenizer.countTokens() <= maxReverseFieldIndex){
					fileReader.close();
					throw new BackEndAPIException(DAStrings.getString(DAStrings.INVALID_LINE_IN_VECTRINO_DATA_FILE_TITLE), DAStrings.getString(DAStrings.INVALID_LINE_IN_VECTRINO_DATA_FILE_MSG) + importDetails.mFile.getAbsolutePath());
				}
				
				// Do this here (rather than after the increment of numberOfMeasurements) so that we only create a new data point
				// if there are measurements to go in it
				if (numberOfMeasurements > 0 && measurementsPerSplit.equals(Double.NaN) == false && (numberOfMeasurements % measurementsPerSplit == 0)) {
					addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
					dataPoint = createNewDPForSplit(dataSet, dataPoint, (int) (measurementsPerSplit/samplingRate), importDetails, 0, probeDetails);
				}

				int fieldNumber = 0;
				Double xVelocity = null, yVelocity = null, z1Velocity = null, z2Velocity = null;
				double xCorrelation = DADefinitions.INVALID_SNR_OR_CORRELATION, yCorrelation = DADefinitions.INVALID_SNR_OR_CORRELATION, z1Correlation = DADefinitions.INVALID_SNR_OR_CORRELATION, z2Correlation = DADefinitions.INVALID_SNR_OR_CORRELATION;
				double xSNR = DADefinitions.INVALID_SNR_OR_CORRELATION, ySNR = DADefinitions.INVALID_SNR_OR_CORRELATION, z1SNR = DADefinitions.INVALID_SNR_OR_CORRELATION, z2SNR = DADefinitions.INVALID_SNR_OR_CORRELATION;
				
				while (tokenizer.hasMoreTokens()) {
					String data = tokenizer.nextToken();
					
					if (fieldNumber == reverseCountStartValue - reverseFieldIndices[X_VELOCITY_INDEX]) {
						xVelocity = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Y_VELOCITY_INDEX]) {
						yVelocity = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Z1_VELOCITY_INDEX]) {
						z1Velocity = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Z2_VELOCITY_INDEX]) {
						z2Velocity = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[X_CORRELATION_INDEX]) {
						xCorrelation = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Y_CORRELATION_INDEX]) {
						yCorrelation = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Z1_CORRELATION_INDEX]) {
						z1Correlation = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Z2_CORRELATION_INDEX]) {
						z2Correlation = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[X_SNR_INDEX]) {
						xSNR = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Y_SNR_INDEX]) {
						ySNR = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Z1_SNR_INDEX]) {
						z1SNR = Double.valueOf(data);
					} else if (fieldNumber == reverseCountStartValue - reverseFieldIndices[Z2_SNR_INDEX]) {
						z2SNR = Double.valueOf(data);
					}
					
					++fieldNumber;
				}
				
				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, xVelocity, xCorrelation, xSNR, DADefinitions.INVALID_SNR_OR_CORRELATION);
				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, yVelocity, yCorrelation, ySNR, DADefinitions.INVALID_SNR_OR_CORRELATION);
				
				// Depending on the file there may only be one z value
				if (z2Velocity != null) {
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, (z1Velocity + z2Velocity)/2, (z1Correlation + z2Correlation)/2, (z1SNR + z2SNR)/2, calculateWDiff(z1Velocity, z2Velocity));
				} else {
					dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, z1Velocity, z1Correlation, z1SNR, DADefinitions.INVALID_SNR_OR_CORRELATION);
				}

				++numberOfMeasurements;
			}

			bufferedFileReader.close();
			
			dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
			dataPoint.setProbeDetails(probeDetails);
			dataPoint.calculate();
			dataPoint.clearDetails();
			addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
			
			return addedDataPoints;
		}
		catch (Exception theException){
			try {
				bufferedFileReader.close();
			} catch (IOException e) {
			}
			
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}
	
	private String extractValueFromHeaderLine(String fieldIdentifier, String line) {
		String value = line.substring(fieldIdentifier.length()).trim();
		
		return value;
	}

	/**
	 * Imports point data from a Vector VNO file.
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param dataSet The data set to add the point to
	 * @param delimiter The delimiter used in the data file
	 * @return 
	 * @throws BackEndAPIException 
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromNortekSingleProbeBinaryFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		DataPoint dataPoint = dataSet.createNewDataPoint(importDetails.getFirstYCoord(), importDetails.getFirstZCoord(), importDetails, 0, dataSet);
		Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>(10);
	
		try{
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(importDetails.mFile));

			byte[] dataBlockId;
			
			try {
				dataBlockId = MAJFCTools.readBytesFromFile(bufferedInputStream, 2);
			} catch (MAJFCToolsEoFException theException) {
				// If the end of the file is reached here then that isn't a problem, just finish reading 
				dataBlockId = null;
			}
			
			// Check this is a VNO file
			if (dataBlockId != null && (dataBlockId[0] != -91 || dataBlockId[1] != 05)) {
				bufferedInputStream.close();
				throw new Exception("Unexpected Block Id - are you sure this is a Vectrino file and not a PolySync file?");
			}
			
			int numberOfMeasurements = 0;
			byte status = 0;
			DataSetConfig configData = dataSet.getConfigData();
			double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
			Double measurementsPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
			boolean burstMode = false;
			int measurementInterval = 1;
			int noise[] = new int[Z2_INDEX + 1];
			ProbeDetail probeDetails = new ProbeDetail();
			
			// Now read the data blocks
			while (dataBlockId != null) {
				int blockId = dataBlockId[1];
				byte[] dataBlock;
				
				try {
					switch (blockId) {
						case 0x05:
							// Read the hardware configuration header
							// If any of the hardware configuration header data is required, the code is already in importDataPointDataFromVectrinoVNOFile
							byte[] hardwareConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, 46);
							{
								String currentProbeId = probeDetails.get(BackEndAPI.PD_KEY_PROBE_ID);
								StringBuffer sbProbeId = new StringBuffer(currentProbeId.length() == 0 ? currentProbeId : currentProbeId + "/" );
								final int PROBE_ID_OFFSET = 2;
								final int PROBE_ID_LENGTH = 14;
								
								for (int i = 0; i < PROBE_ID_LENGTH; ++i) {
									sbProbeId.append((char) hardwareConfigurationHeader[i + PROBE_ID_OFFSET]);
								}
								
								probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, sbProbeId.toString().trim());
							}
							break;
							
						case 0x04:
							// Read the head configuration
							// If any of the head configuration header data is required, the code is already in importDataPointDataFromVectrinoVNOFile
							byte[] headConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, 222);
							{
//								int probeType = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {headConfigurationHeader[6], headConfigurationHeader[7]});
								String currentProbeId = probeDetails.get(BackEndAPI.PD_KEY_PROBE_ID);
								StringBuffer sbProbeId = new StringBuffer(currentProbeId.length() == 0 ? currentProbeId : currentProbeId + "/" );
								final int PROBE_ID_OFFSET = 8;
								final int PROBE_ID_LENGTH = 12;
								
								for (int i = 0; i < PROBE_ID_LENGTH; ++i) {
									sbProbeId.append((char) headConfigurationHeader[i + PROBE_ID_OFFSET]);
								}
								
								probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, sbProbeId.toString().trim());
							}
							break;
							
						case 0x00:
							// Read the user configuration
							// If any of the user configuration header data is required, the code is already in importDataPointDataFromVectrinoVNOFile
							byte[] userConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, 510);
							samplingRate = parseUserConfigurationHeaderForSamplingRate(userConfigurationHeader, dataSet, probeDetails.get(BackEndAPI.PD_KEY_PROBE_ID));
							probeDetails.set(BackEndAPI.PD_KEY_SAMPLING_RATE, String.valueOf(samplingRate));
							measurementInterval = MAJFCTools.makeSignedIntFromBytes(new byte[] {userConfigurationHeader[36], userConfigurationHeader[37]});
							burstMode = isBurstMode(userConfigurationHeader);
							break;
						
						case 0x50:
							// Vectrino velocity header block
							dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 40);

							noise[X_INDEX] = dataBlock[10];
							noise[Y_INDEX] = dataBlock[11];
							noise[Z1_INDEX] = dataBlock[12];
							noise[Z2_INDEX] = dataBlock[13];
							break;

						case 0x51:
							// Vectrino velocity data block
							dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 20);
				
							{
								VelocityDataBlockData vdbd = extractVNOVelocityDataBlockData(dataBlock);
								
								// Do this here (rather than after the increment of numberOfMeasurements) so that we only create a new data point
								// if there are measurements to go in it
								if (numberOfMeasurements > 0 && measurementsPerSplit.equals(Double.NaN) == false && (numberOfMeasurements % measurementsPerSplit == 0)) {
									addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
									dataPoint = createNewDPForSplit(dataSet, dataPoint, (int) (measurementsPerSplit/samplingRate), importDetails, 0, probeDetails);
								}
				
								// All data values have been read correctly
								for (int i = 0; i < Z2_INDEX + 1; ++i) {
									switch (i) {
										case X_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, vdbd.mVelocities[X_INDEX], vdbd.mCorrelations[X_INDEX], calculateSNR(vdbd.mAmplitudes[X_INDEX], noise[X_INDEX]), DADefinitions.INVALID_SNR_OR_CORRELATION);
											break;
										case Y_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, vdbd.mVelocities[Y_INDEX], vdbd.mCorrelations[Y_INDEX], calculateSNR(vdbd.mAmplitudes[Y_INDEX], noise[Y_INDEX]), DADefinitions.INVALID_SNR_OR_CORRELATION);
											break;
										case Z1_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, (vdbd.mVelocities[Z1_INDEX] + vdbd.mVelocities[Z2_INDEX])/2, (vdbd.mCorrelations[Z1_INDEX] + vdbd.mCorrelations[Z2_INDEX])/2, (calculateSNR(vdbd.mAmplitudes[Z1_INDEX], (double) noise[Z1_INDEX]) + calculateSNR(vdbd.mAmplitudes[Z2_INDEX], (double) noise[Z2_INDEX]))/2, calculateWDiff(vdbd.mVelocities[Z1_INDEX], vdbd.mVelocities[Z2_INDEX]));
										default:
									}
								}
							}
							
							++numberOfMeasurements;
							break;
							
						case 0x10:
							// Vector Velocity Data Block
							dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 22);
	
							{
								VelocityDataBlockData vdbd = extractVECVelocityDataBlockData(dataBlock, status);
								
								// Do this here (rather than after the increment of numberOfMeasurements) so that we only create a new data point
								// if there are measurements to go in it
								if (numberOfMeasurements > 0 && measurementsPerSplit.equals(Double.NaN) == false && (numberOfMeasurements % measurementsPerSplit == 0)) {
									addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
									dataPoint = createNewDPForSplit(dataSet, dataPoint, (int) (measurementsPerSplit/samplingRate), importDetails, 0, probeDetails);
								}
								
								// All data values have been read correctly
								for (int i = 0; i < Z2_INDEX; ++i) {
									switch (i) {
										case X_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, vdbd.mVelocities[X_INDEX], vdbd.mCorrelations[X_INDEX], calculateSNR(vdbd.mAmplitudes[X_INDEX], (double) noise[X_INDEX]), DADefinitions.INVALID_SNR_OR_CORRELATION);
											break;
										case Y_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, vdbd.mVelocities[Y_INDEX], vdbd.mCorrelations[Y_INDEX], calculateSNR(vdbd.mAmplitudes[Y_INDEX], (double) noise[Y_INDEX]), DADefinitions.INVALID_SNR_OR_CORRELATION);
											break;
										case Z1_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, vdbd.mVelocities[Z1_INDEX], vdbd.mCorrelations[Z1_INDEX], calculateSNR(vdbd.mAmplitudes[Z1_INDEX], (double) noise[Z1_INDEX]), DADefinitions.INVALID_SNR_OR_CORRELATION);
										default:
									}
								}
							}
								
							++numberOfMeasurements;
							
							break;
							
						case 0x11:
							// System Data Block (remember we've already read the Sync and Id bytes, read the remainder of the 28)
							dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 26);
							status = dataBlock[21];
							
							break;
							
						case 0x12:
							// Vector
							// Velocity Data Header Block (remember we've already read the Sync and Id bytes, read the remainder of the 42)
							// If in burst mode, this is the start of a new burst.
							dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 40);

							noise[X_INDEX] = dataBlock[14];
							noise[Y_INDEX] = dataBlock[15];
							noise[Z1_INDEX] = dataBlock[16];
							noise[Z2_INDEX] = dataBlock[17];
							
							// Manual split overrides burst mode
							if (numberOfMeasurements == 0 || burstMode == false || measurementsPerSplit.equals(Double.NaN) == false) {
								break;
							}
							
							addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
							dataPoint = createNewDPForSplit(dataSet, dataPoint, (int) measurementInterval, importDetails, 0, probeDetails);
							
							break;
						default:
							MAJFCLogger.log("Unknown Id " + blockId + " in Vector vec File.");
							
							dataBlockId = findNextSyncAndId(bufferedInputStream);
							
							continue;
//							throw new BackEndAPIException("Import Failed!", "Unknown Id " + blockId + " in Vector vec File.");
					}
					
					dataBlockId = MAJFCTools.readBytesFromFile(bufferedInputStream, 2);
				} catch (MAJFCToolsEoFException theException) {
					// If the end of the file is reached here then that isn't a problem, just finish reading 
					dataBlockId = null;
				}
			}

			bufferedInputStream.close();

			// All data read, now add the data points to the data set
			dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
			dataPoint.setProbeDetails(probeDetails);
			dataPoint.calculate();
			dataPoint.clearDetails();
			addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
			
			return addedDataPoints;		}
		catch (Exception theException){
			theException.printStackTrace();
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}
	
	private byte[] findNextSyncAndId(BufferedInputStream bufferedInputStream) throws Exception {
		return findNextSyncAndId(bufferedInputStream, 0, 0);
	}
	
	private byte[] findNextSyncAndId(BufferedInputStream bufferedInputStream, int includeXPrecedingBytes) throws Exception {
		return findNextSyncAndId(bufferedInputStream, includeXPrecedingBytes, 0);
	}
	
	private byte[] findNextSyncAndId(BufferedInputStream bufferedInputStream, int includeXPrecedingBytes, int includeXFollowingBytes) throws Exception {
		byte nextByte = 0x0;
		Vector<Byte> readBytes = new Vector<Byte>(10);
		int numberOfReadBytes = 0;
		int numberOfBytesToReturn = includeXPrecedingBytes + 2; //Math.min(includeXPrecedingBytes + 2, numberOfReadBytes);
		
		// Read until we find a 0xA5, but read at least the required number of preceding bytes plus the sync and id.
		// If 0xA5 is read before the correct number of bytes have been read, ignore it and keep reading.
//		while (nextByte != 0xA5 || numberOfReadBytes < (numberOfBytesToReturn - 1)) {
		while (nextByte != (byte) 0xA5 || numberOfReadBytes < (numberOfBytesToReturn - 1)) {
			 nextByte = MAJFCTools.readBytesFromFile(bufferedInputStream, 1)[0];
			 readBytes.add(nextByte);
			 ++numberOfReadBytes;
		}

		readBytes.add(MAJFCTools.readBytesFromFile(bufferedInputStream, 1)[0]);
		++numberOfReadBytes;
		
		for (int i = 0; i < includeXFollowingBytes; ++i) {
			 nextByte = MAJFCTools.readBytesFromFile(bufferedInputStream, 1)[0];
			 readBytes.add(nextByte);
			 ++numberOfReadBytes;
		}

		// We need to increase numberOfBytesToReturn by the required number of following bytes for the copy of the actual
		// bytes needed.
		numberOfBytesToReturn += includeXFollowingBytes;
		byte[] returnBytes = new byte[numberOfBytesToReturn];
		
		for (int i = 0; i < numberOfBytesToReturn; ++i) {
			returnBytes[i] = readBytes.elementAt(numberOfReadBytes - (numberOfBytesToReturn - i));
		}
		
		return returnBytes;
	}
	
	private boolean isBurstMode(byte[] userConfigurationHeader) {
		return (userConfigurationHeader[21] & 0x02) == 0;
	}

	/**
	 * Extracts the sampling rate from the User Configuration Header and sets it in the data set
	 * @param uch
	 * @param dataSet
	 * @param conversionNumerator
	 * @return the sampling rate
	 * @throws Exception
	 */
	private double parseUserConfigurationHeaderForSamplingRate(byte[] uch, DataSet dataSet, String probeId) throws Exception {
		double conversionNumerator = 1d;
		
		if (probeId.startsWith(DADefinitions.PROBE_TYPE_VECTRINO)) {
			conversionNumerator = 50000d;
		} else if (probeId.startsWith(DADefinitions.PROBE_TYPE_VECTOR)) {
			conversionNumerator = 512;
		} 
		
		long avgInterval = MAJFCTools.makeSignedLongFromBytes(new byte[] {uch[14], uch[15]});
		double samplingRate = conversionNumerator/avgInterval;
		
		dataSet.setSamplingRate(samplingRate);
		
		return samplingRate;
	}
	
	/**
	 * Imports point data from a PolySync VNO file.
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param dataSet The data set to add the point to
	 * @param delimiter The delimiter used in the data file
	 * @param mainProbeIndex The index (starting at 0 for the first probe) of the main probe. This is the probe whose coordinates are used to identify the fixed probe within its data set
	 * @param fixedProbeIndex The index (starting at 0 for the first probe) of the fixed probe, or negative if there is no fixed probe (coords must be of length > 2 if this is non-negative)
	 * @throws BackEndAPIException if mainProbeIndex >= coords.length, or fixedProbeIndex >= coords.length, or mainProbeIndex == fixedProbeIndex, or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromPolySyncVNOFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		validateMainAndFixedProbeIndices(importDetails);
		
		FixedProbeDataSet fixedProbeDataSet = getFixedProbeDataSet(importDetails.mMainProbeIndex, importDetails.mFixedProbeIndex, importDetails.mCoordsList, dataSet);
		Hashtable<Integer, DataPoint> dataPointLookup = new Hashtable<Integer, DataPoint>();
		Vector<ProbeDetail> probeDetailsList = null;
		
		try{
			// Read the file header
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(importDetails.mFile));
			@SuppressWarnings("unused")
			byte[] fileType = MAJFCTools.readBytesFromFile(bufferedInputStream, 16); 
			@SuppressWarnings("unused")
			long fileVersion = MAJFCTools.readUnsignedNumberFromFile(bufferedInputStream, 2); 
			int numberOfProbes = (int) MAJFCTools.readUnsignedNumberFromFile(bufferedInputStream, 2);

			if (numberOfProbes != importDetails.mCoordsList.size()) {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE), DAStrings.getString(DAStrings.INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG) + importDetails.getFirstYCoord() + '-' + importDetails.getFirstZCoord());
			}
			
			Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>();
			for (int probeIndex = 0; probeIndex < numberOfProbes; ++probeIndex) {
				DataPointCoordinates coord = importDetails.mCoordsList.get(probeIndex);
				DataSet theDataSet = dataSet;
				
				// If this is the fixed probe data point, it is referenced using the main probe coordinates
				if (probeIndex == importDetails.mFixedProbeIndex) {
					coord = importDetails.mCoordsList.get(importDetails.mMainProbeIndex);
					theDataSet = fixedProbeDataSet;
				}
				
				DataPoint dataPoint = theDataSet.createNewDataPoint(coord.getY(), coord.getZ(), importDetails, probeIndex, theDataSet);
				dataPointLookup.put(probeIndex, dataPoint);
				// Maintain a list of added data points for return
				addedDataPoints.add(probeIndex == importDetails.mFixedProbeIndex ? null : new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
			}

			@SuppressWarnings("unused")
			long startTime = MAJFCTools.readUnsignedNumberFromFile(bufferedInputStream, 8);
			@SuppressWarnings("unused")
			long configStringBytesPerChar = MAJFCTools.readUnsignedNumberFromFile(bufferedInputStream, 2); 
			int configStringLengthInBytes = (int) MAJFCTools.readUnsignedNumberFromFile(bufferedInputStream, 4) * 2; 

			byte[] configString = MAJFCTools.readBytesFromFile(bufferedInputStream, (int) configStringLengthInBytes);
			
			StringBuffer configStr = new StringBuffer(configString.length);
			
			for (int i = 0; i < configString.length; ++i) {
				configStr.append((char) configString[i]);
			}

			probeDetailsList = parsePolySyncConfig(importDetails.mFile);
			dataSet.setSamplingRate(Double.parseDouble(probeDetailsList.firstElement().get(BackEndAPI.PD_KEY_SAMPLING_RATE)));
			
			DataSetConfig configData = dataSet.getConfigData();
			double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
			Double measurementsPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);

			int[] numberOfMeasurements = new int[(int) numberOfProbes];
			int noise[] = new int[Z2_INDEX + 1];
			ProbeDetail probeDetails = new ProbeDetail();
			final int HEADER_BLOCKS_BEFORE_ID = 12;

			// Now read the data blocks
			while (true) {
				byte[] headerBlock;
				
				try {
					headerBlock = findNextSyncAndId(bufferedInputStream, HEADER_BLOCKS_BEFORE_ID);
				} catch (MAJFCToolsEoFException theException) {
					// If the end of the file is reached here then that isn't a problem, just finish reading 
					break;
				}
				
				int headerBlockIndex = 0;
				int probeIndex = -1;
//				long blockTime = -1;
				int blockSizeInBytes = -1;

				if (headerBlock.length == HEADER_BLOCKS_BEFORE_ID + 2) {
					probeIndex = MAJFCTools.makeUnsignedIntFromBytes(new byte[] { headerBlock[headerBlockIndex++], headerBlock[headerBlockIndex++] });
				
					// Skip block time
					headerBlockIndex += 8;
//					byte[] blockTimeBytes = new byte[8];
//					
//					for (int i = 0; i < blockTimeBytes.length; ++i) {
//						blockTimeBytes[i] = headerBlock[headerBlockIndex++];
//					}
//					blockTime = MAJFCTools.makeSignedLongFromBytes(blockTimeBytes);

					blockSizeInBytes = MAJFCTools.makeSignedIntFromBytes(new byte[] { headerBlock[headerBlockIndex++], headerBlock[headerBlockIndex++] }) * 2 - 2;
				}

				int blockId = headerBlock[headerBlock.length - 1];

				byte[] dataBlock;

				try {
					switch (blockId) {
						case (byte) 0x05:
							// Read the hardware configuration header
							// If any of the hardware configuration header data is required, the code is already in importDataPointDataFromVectrinoVNOFile
							byte[] hardwareConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, blockSizeInBytes); //46
							StringBuffer sbProbeId = new StringBuffer();
							final int PROBE_ID_OFFSET = 2;
							final int PROBE_ID_LENGTH = 14;
							
							for (int i = 0; i < PROBE_ID_LENGTH; ++i) {
								sbProbeId.append((char) hardwareConfigurationHeader[i + PROBE_ID_OFFSET]);
							}
							
							probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, sbProbeId.toString().trim());
							break;
							
						case (byte) 0x04:
							// Read the head configuration
							// If any of the head configuration header data is required, the code is already in importDataPointDataFromVectrinoVNOFile
							@SuppressWarnings("unused")
							byte[] headConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, blockSizeInBytes); //222
							break;
							
						case (byte) 0x50:
							// Vectrino velocity header block
							dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, blockSizeInBytes); //40

							noise[X_INDEX] = dataBlock[10];
							noise[Y_INDEX] = dataBlock[11];
							noise[Z1_INDEX] = dataBlock[12];
							noise[Z2_INDEX] = dataBlock[13];
							break;

						case (byte) 0x51:
							// Vectrino velocity data block
							dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, blockSizeInBytes); //20
				
							{
								VelocityDataBlockData vdbd = extractVNOVelocityDataBlockData(dataBlock);

								// All data values have been read correctly
								DataPoint dataPoint = dataPointLookup.get(probeIndex);
								
								// If we're splitting the data up, check whether we need to create a new data point
								if (numberOfMeasurements[probeIndex] > 0 && measurementsPerSplit.equals(Double.NaN) == false && (numberOfMeasurements[probeIndex] % measurementsPerSplit == 0)) {
									dataPoint.setProbeDetails(probeDetailsList.elementAt(probeIndex));
									
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
									
									dataPointLookup.put(probeIndex, dataPoint);
									// Maintain a list of added data points for return
									if (probeIndex != importDetails.mFixedProbeIndex) {
										addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
									}
								}
								
								if (dataPoint == null) {
									throw new Exception();
								}
								
								for (int i = 0; i < Z2_INDEX + 1; ++i) {
									switch (i) {
										case X_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, vdbd.mVelocities[X_INDEX], vdbd.mCorrelations[X_INDEX], calculateSNR(vdbd.mAmplitudes[X_INDEX], noise[X_INDEX]), DADefinitions.INVALID_SNR_OR_CORRELATION);
											break;
										case Y_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, vdbd.mVelocities[Y_INDEX], vdbd.mCorrelations[Y_INDEX], calculateSNR(vdbd.mAmplitudes[Y_INDEX], noise[Y_INDEX]), DADefinitions.INVALID_SNR_OR_CORRELATION);
											break;
										case Z1_INDEX:
											dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, (vdbd.mVelocities[Z1_INDEX] + vdbd.mVelocities[Z2_INDEX])/2, (vdbd.mCorrelations[Z1_INDEX] + vdbd.mCorrelations[Z2_INDEX])/2, (calculateSNR(vdbd.mAmplitudes[Z1_INDEX], (double) noise[Z1_INDEX]) + calculateSNR(vdbd.mAmplitudes[Z2_INDEX], (double) noise[Z2_INDEX]))/2, calculateWDiff(vdbd.mVelocities[Z1_INDEX], vdbd.mVelocities[Z2_INDEX]));
										default:
									}
								}
								
								++numberOfMeasurements[probeIndex];
							}

							break;
							
						default:
							MAJFCLogger.log("Unknown Id " + blockId + " in PolySync vno File.");
							continue;
					}
				} catch (MAJFCToolsEoFException theException) {
					// If the end of the file is reached here then that isn't a problem, just finish reading 
				}
			}
			
			bufferedInputStream.close();

			// All data read, now add the data points to the data set
			Integer dataPointIndex = 0;
			DataPoint dataPoint = dataPointLookup.get(dataPointIndex);

			while (dataPoint != null) {
				dataPoint.setProbeDetails(probeDetailsList.elementAt(dataPointIndex));

				if (dataPointIndex == importDetails.mFixedProbeIndex) {
					fixedProbeDataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				} else {
					dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				}

				dataPoint = dataPointLookup.get(++dataPointIndex);
			}
			
			// Need a separate loop for this so as to ensure that the fixed probe data has been read and stored as well to allow
			// the correlation calculations to complete correctly
			dataPointIndex = 0;
			dataPoint = dataPointLookup.get(dataPointIndex);
			
			while (dataPoint != null) {
				// Fixed probe data points should have their calculations done via their corresponding mobile probe data point
				if (dataPointIndex != importDetails.mFixedProbeIndex) {
					dataPoint.calculate();
					dataPoint.clearDetails();
				}
				
				dataPoint = dataPointLookup.get(++dataPointIndex);
			}
			
			return addedDataPoints;
		}
		catch (Exception theException){
			theException.printStackTrace();
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + " (" + dataSet.makeKey(importDetails.getFirstYCoord(), importDetails.getFirstZCoord()) + ")\n\"" + theException.getMessage() + "\"");
		}
	}
	
	private Vector<ProbeDetail> parsePolySyncConfig(File file) throws BackEndAPIException {
		try{
			PolySyncConfigDocHandler docHandler = new PolySyncConfigDocHandler();

			FileReader fileReader = new FileReader(file);
			QDParser.parse(docHandler, fileReader, 34);

			fileReader.close();
			
			return docHandler.mProbeDetails;
		}
		catch (Exception theException){
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}
	
	/**
	 * Handles the events from the XML parser
	 * 
	 * @author MAJ727
	 */
	private class PolySyncConfigDocHandler extends MyAbstractDocHandler {
		private Vector<ProbeDetail> mProbeDetails;
		private String mSamplingRate = "1";
		private String mProbeType = "";
		
		private PolySyncConfigDocHandler() {
			mProbeDetails = new Vector<BackEndAPI.ProbeDetail>(5);
		}

		@Override
		public void startElement(String elem, Hashtable<String, String> h) {
			super.startElement(elem, h);
			
			if (elem.equals(DADefinitions.XML_POLYSYNC_CONFIGURATION_TAG)) {
				mProbeType = h.get(DADefinitions.XML_POLYSYNC_PROBE_TYPE_TAG);
			}
		}
		
		@Override
		public void elementValue(String value) {
			if (mStartElement.equals(DADefinitions.XML_POLYSYNC_SAMPLING_RATE_TAG)) {
				mSamplingRate = value;
			} else if (mStartElement.equals(DADefinitions.XML_POLYSYNC_SERIAL_NUMBER_TAG)) {
				ProbeDetail probeDetail = new ProbeDetail();
				probeDetail.set(BackEndAPI.PD_KEY_PROBE_TYPE, mProbeType);
				probeDetail.set(BackEndAPI.PD_KEY_PROBE_ID, value.trim());
				probeDetail.set(BackEndAPI.PD_KEY_SAMPLING_RATE, mSamplingRate);
				mProbeDetails.add(probeDetail);
			}
		}
	}

	/**
	 * Imports an NDV multi-run data file
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
	public void importMultiRunDataPointDataFromNDVFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return importDataPointDataFromNDVFile(dataSet, importDetails);
			}
		});
	}
	
	/**
	 * Imports a Vectrino multi-run data file
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
	public void importMultiRunDataPointDataFromNortekSingleProbeBinaryFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return importDataPointDataFromNortekSingleProbeBinaryFile(dataSet, importDetails);
			}
		});
	}
	
	/**
	 * Imports a PolySync multi-run data file
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
	public void importMultiRunDataPointDataFromPolySyncVNOFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				Vector<DataPointCoordinates> createdDataPoints = importDataPointDataFromPolySyncVNOFile(dataSet, importDetails);
				
				return createdDataPoints;
			}
		});
	}
	
	/**
	 * Imports a VectrinoII Matlab multi-run data file
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
	public void importMultiRunDataPointDataFromVectrinoIIMatFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return importDataPointDataFromVectrinoIIMatFile(dataSet, importDetails);
			}
		});
	}

	/**
	 * Imports a VectrinoII raw multi-run data file
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
	public void importMultiRunDataPointDataFromVectrinoIIRawFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return importDataPointDataFromVectrinoIIRawFile(dataSet, importDetails);
			}
		});
	}

	private VelocityDataBlockData extractVNOVelocityDataBlockData(byte[] dataBlock) throws Exception {
		int dataBlockByteIndex = 0;
		VelocityDataBlockData vdbd = new VelocityDataBlockData();
		
		// First datum in the block is the Status (1 byte) which contains scaling information...
		vdbd.mStatus = dataBlock[dataBlockByteIndex++];
		vdbd.mScaleFactor = (vdbd.mStatus & DADefinitions.VNO_SCALING_FACTOR_BIT_MASK) > 0 ? DADefinitions.VNO_SCALING_BIT_SET_FACTOR : DADefinitions.VNO_SCALING_BIT_NOT_SET_FACTOR;
		
		// ...then measurement count (1 byte)...
		vdbd.mMeasurementCount = dataBlock[dataBlockByteIndex++];
		
		// ...then the velocities (u, v, w1, w2) (2 bytes each - LSB first)...
		for (int i = 0; i < vdbd.mVelocities.length; ++i) {
			byte[] velocityBytes = {dataBlock[dataBlockByteIndex++], dataBlock[dataBlockByteIndex++]};
			long velocity = MAJFCTools.makeSignedLongFromBytes(velocityBytes);
			vdbd.mVelocities[i] = ((double) velocity)/vdbd.mScaleFactor;
		}

		//  ...then the amplitudes (1 byte each) for the SNRs (if used: snr = 20d * Math.log10(amplitude))...
		for (int i = 0; i < vdbd.mAmplitudes.length; ++i) {
			vdbd.mAmplitudes[i] = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[dataBlockByteIndex++]});
		}
		
		//  ...then the correlations (1 byte each)...
		for (int i = 0; i < vdbd.mCorrelations.length; ++i) {
			vdbd.mCorrelations[i] = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[dataBlockByteIndex++]});
		}

		// ...and finally the checksum
		byte[] checksumBytes = {dataBlock[dataBlockByteIndex++], dataBlock[dataBlockByteIndex++]};
		long checksum = MAJFCTools.makeSignedLongFromBytes(checksumBytes);
		long calculatedChecksum = 0;//0xb58c;
		for (int i = 0; i < dataBlock.length - 2; ++i) {
			calculatedChecksum += dataBlock[i];
		}
		
		if (checksum != calculatedChecksum) {
			//TODO checksum error
			//Logger.log("Checksum Error");
		}

		return vdbd;
	}
	
	private VelocityDataBlockData extractVECVelocityDataBlockData(byte[] dataBlock, byte status) throws Exception {
		int dataBlockByteIndex = 0;
		VelocityDataBlockData vdbd = new VelocityDataBlockData();
		vdbd.mScaleFactor = (status & DADefinitions.VEC_SCALING_FACTOR_BIT_MASK) > 0 ? DADefinitions.VNO_SCALING_BIT_SET_FACTOR : DADefinitions.VNO_SCALING_BIT_NOT_SET_FACTOR;
		
		// The Sync and Id are no longer passed in as they are used to determine the type of the block read
		// First datum in the data block is Sync (1 byte)...
		//vdbd.mSync = dataBlock[dataBlockByteIndex++];
		
		// ...then Id...
		//vdbd.mId = dataBlock[dataBlockByteIndex++];
		
		// ...ignore 1 byte...
		++dataBlockByteIndex;
		
		// ...then measurement count (1 byte)...
		vdbd.mMeasurementCount = dataBlock[dataBlockByteIndex++];
		
		// ...ignore 6 bytes...
		dataBlockByteIndex += 6;

		// ...then the velocities (u, v, w) (2 bytes each - LSB first)...
		// (Vector only has three velocity measurements, not the four of Vectrino)
		final int NUMBER_OF_COMPONENTS = vdbd.mVelocities.length - 1;
		for (int i = 0; i < NUMBER_OF_COMPONENTS; ++i) {
			byte[] velocityBytes = {dataBlock[dataBlockByteIndex++], dataBlock[dataBlockByteIndex++]};
			long velocity = MAJFCTools.makeSignedLongFromBytes(velocityBytes);
			vdbd.mVelocities[i] = ((double) velocity)/vdbd.mScaleFactor;
		}

		//  ...then the amplitudes (1 byte each) for the SNRs (if used: snr = 20d * Math.log10(amplitude/noise))...
		for (int i = 0; i < NUMBER_OF_COMPONENTS; ++i) {
			vdbd.mAmplitudes[i] = dataBlock[dataBlockByteIndex++];
		}
		
		//  ...then the correlations (1 byte each)...
		for (int i = 0; i < NUMBER_OF_COMPONENTS; ++i) {
			vdbd.mCorrelations[i] = dataBlock[dataBlockByteIndex++];
		}

		// ...and finally the checksum
		byte[] checksumBytes = {dataBlock[dataBlockByteIndex++], dataBlock[dataBlockByteIndex++]};
		long checksum = MAJFCTools.makeSignedLongFromBytes(checksumBytes);
		long calculatedChecksum = 0;//0xb58c;
		for (int i = 0; i < dataBlock.length - 2; ++i) {
			calculatedChecksum += dataBlock[i];
		}
		
		if (checksum != calculatedChecksum) {
			//TODO checksum error
			//Logger.log("Checksum Error");
		}

		return vdbd;
	}
	
	/**
	 * Calculates the Singal to Noise Ratio in dB
	 * @param amplitude The signal amplitude
	 * @param noise The noise amplitude
	 * @return SNR in dB
	 */
	private double calculateSNR(double amplitude, double noise) {
		if (noise == 0) {
			return DADefinitions.INVALID_SNR_OR_CORRELATION;
		}
		
		return 20d * Math.log10(amplitude/noise);
	}
	
	/**
	 * Calculates "wDiff" the difference between the z-axis (w) velocities calculated from different sources (e.g. the 
	 * two beams of an ADV)
	 * @param z1Velocity
	 * @param z2Velocity
	 * @return
	 */
	private double calculateWDiff(double z1Velocity, double z2Velocity) {
//		return 100d * Math.abs(z1Velocity - z2Velocity)/z1Velocity;
		return Math.abs(z1Velocity - z2Velocity);
	}
	
	private class VelocityDataBlockData {
		@SuppressWarnings("unused")
		private int mMeasurementCount;
		private int mStatus, mScaleFactor;
		private double[] mVelocities = new double[Z2_INDEX + 1], mAmplitudes = new double[Z2_INDEX + 1], mCorrelations = new double[Z2_INDEX + 1];
	}
	
	/**
	 * Imports point data from a converted PolySync file. The file must have an associated .tem.txt temperature file with the same name
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param dataSet The data set to add the point to
	 * @param delimiter The delimiter used in the data file
	 * @param fixedProbeIndex The index (starting at 0 for the first probe) of the fixed probe, or negative if there is no fixed probe (coords must be of length > 2 if this is non-negative)
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importDataPointDataFromConvertedPolySyncFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		FixedProbeDataSet fixedProbeDataSet = getFixedProbeDataSet(importDetails.mMainProbeIndex, importDetails.mFixedProbeIndex, importDetails.mCoordsList, dataSet);
		String dataFileName = importDetails.mFile.getAbsolutePath();
		int numberOfDataColumns = -1;
		int[] dataPositions = {-1, -1, -1, -1, -1};
		final int PROBE_NUMBER_DATA_POSITION_INDEX = 0;
		final int X_DATA_POSITION_INDEX = PROBE_NUMBER_DATA_POSITION_INDEX + 1;
		final int Y_DATA_POSITION_INDEX = X_DATA_POSITION_INDEX + 1;
		final int Z1_DATA_POSITION_INDEX = Y_DATA_POSITION_INDEX + 1;
		final int Z2_DATA_POSITION_INDEX = Z1_DATA_POSITION_INDEX + 1;
		Hashtable<Integer, DataPoint> dataPointLookup = new Hashtable<Integer, DataPoint>();
	
		// Read the second line of the file to ascertain the position of the velocity data in the data file
		try {
			FileReader fileReader = new FileReader(dataFileName);
			BufferedReader bufferedFileReader = new BufferedReader(fileReader);
			int readChar;
			int lineNumber = 0;
			boolean probeNumberErrorReported = false;
			
			while ((readChar = bufferedFileReader.read()) != -1) {
				StringBuffer line = new StringBuffer();
				char theChar = (char)readChar;
				while (theChar != MAJFCTools.SYSTEM_NEW_LINE_CHAR && Character.toString(theChar).equals(MAJFCTools.SYSTEM_NEW_LINE_STRING) == false){
					if (theChar == MAJFCTools.SYSTEM_CARRIAGE_RETURN_CHAR){
						// Discard the carriage return
					} else {
						line.append(theChar);
					}
					
					theChar = (char) bufferedFileReader.read();
				}
			
				// Ignore blank lines
				if (line.length() == 0) {
					continue;
				}
			
				++lineNumber;
				
				if (lineNumber < 2) {
					continue;
				}

				StringTokenizer tokenizer = new StringTokenizer(line.toString(), DADefinitions.POLYSYNC_DATA_FILE_DELIMITER);
				
				if (lineNumber == 2) {
					// This should be the column headings
					numberOfDataColumns = tokenizer.countTokens();
					
					int column = 0;
					while (tokenizer.hasMoreTokens()) {
						String columnHeading = tokenizer.nextToken();
						
						if (columnHeading.equals(DADefinitions.POLYSYNC_PROBE_NUMBER_COLUMN_HEADING)) {
							dataPositions[PROBE_NUMBER_DATA_POSITION_INDEX] = column;
						} else if (columnHeading.equals(DADefinitions.POLYSYNC_X_COLUMN_HEADING)) {
							dataPositions[X_DATA_POSITION_INDEX] = column;
						} else if (columnHeading.equals(DADefinitions.POLYSYNC_Y_COLUMN_HEADING)) {
							dataPositions[Y_DATA_POSITION_INDEX] = column;
						} else if (columnHeading.equals(DADefinitions.POLYSYNC_Z1_COLUMN_HEADING)) {
							dataPositions[Z1_DATA_POSITION_INDEX] = column;
						} else if (columnHeading.equals(DADefinitions.POLYSYNC_Z2_COLUMN_HEADING)) {
							dataPositions[Z2_DATA_POSITION_INDEX] = column;
						}
						
						++column;
					}
					
					// Check that we've found all four data headings
					for (int i = 0; i < dataPositions.length; ++i) {
						if (dataPositions[i] == -1) {
							bufferedFileReader.close();
							throw new BackEndAPIException(DAStrings.getString(DAStrings.POLYSYNC_COLUMN_HEADINGS_MISSING_TITLE), DAStrings.getString(DAStrings.POLYSYNC_COLUMN_HEADINGS_MISSING_MSG) + dataFileName);
						}
					}
					
					continue;
				}
				
				// If we've got here then the column headings should have been read correctly, so we can be reasonably sure we're
				// actually looking at a PolySync velocity file
				if (tokenizer.countTokens() != numberOfDataColumns) {
					bufferedFileReader.close();
					throw new BackEndAPIException(DAStrings.getString(DAStrings.POLYSYNC_CORRUPT_LINE_TITLE), DAStrings.getString(DAStrings.POLYSYNC_CORRUPT_LINE_MSG) + lineNumber);
				}
				
				String[] data = new String[numberOfDataColumns];
			
				// Copy the data into an array for access
				for (int i = 0; i < numberOfDataColumns; ++i) {
					data[i] = tokenizer.nextToken();
				}

				Double[] dataValues = new Double[dataPositions.length];
				
				for (int i = 0; i < dataPositions.length; ++i) {
					String dataItem = data[dataPositions[i]];
					dataValues[i] = -1.0;
					
					try {
						dataValues[i] = Double.parseDouble(dataItem);
					} catch (Exception e) {
						bufferedFileReader.close();
						throw new BackEndAPIException(DAStrings.getString(DAStrings.POLYSYNC_CORRUPT_LINE_TITLE), DAStrings.getString(DAStrings.POLYSYNC_CORRUPT_LINE_MSG) + lineNumber);
					}
				}
				
				// All data values have been read correctly
				int probeIndex = dataValues[PROBE_NUMBER_DATA_POSITION_INDEX].intValue();
				
				// If there are more probes in the file than expected ignore extra probes and write a warning
				if (probeIndex >= importDetails.mCoordsList.size()) {
					if (probeNumberErrorReported == false) {
						MAJFCLogger.log("More probes than expected in data file for point: " + importDetails.getFirstYCoord() + '-' + importDetails.getFirstZCoord());
						probeNumberErrorReported = true;
					}
					
					continue;
				}
				
				DataPoint dataPoint = dataPointLookup.get(probeIndex);
				
				if (dataPoint == null) {
					// If this is for the fixed probe, label the data point with the mobile probe coordinates
					if (probeIndex == importDetails.mFixedProbeIndex) {
						int mobileProbeIndex = importDetails.mFixedProbeIndex == 0 ? 1 : 0;
						dataPoint = dataSet.createNewDataPoint(importDetails.mCoordsList.get(mobileProbeIndex).getY(), importDetails.mCoordsList.get(mobileProbeIndex).getZ(), importDetails, probeIndex, dataSet);
					} else {
						dataPoint = dataSet.createNewDataPoint(importDetails.mCoordsList.get(probeIndex).getY(), importDetails.mCoordsList.get(probeIndex).getZ(), importDetails, probeIndex, dataSet);
					}
					
					dataPointLookup.put(probeIndex, dataPoint);
				}
				
				for (int i = 0; i < dataPositions.length; ++i) {
					switch (i) {
						case X_DATA_POSITION_INDEX:
							dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, dataValues[X_DATA_POSITION_INDEX]);
							break;
						case Y_DATA_POSITION_INDEX:
							dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, dataValues[Y_DATA_POSITION_INDEX]);
							break;
						case Z1_DATA_POSITION_INDEX:
							dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, (dataValues[Z1_DATA_POSITION_INDEX] + dataValues[Z2_DATA_POSITION_INDEX])/2);
						default:
					}
				}
			}	
			
			
			Integer dataPointIndex = 0;
			// Need a separate loop for this so as to ensure that the fixed probe data has been read and stored before doing any calculation
			DataPoint dataPoint = dataPointLookup.get(importDetails.mFixedProbeIndex);
			fixedProbeDataSet.addDataPointImportedFromFileNoCheck(dataPoint);
			dataPoint.calculate(false);
			dataPoint.clearDetails();
			
			while (dataPoint != null) {
				if (dataPointIndex == importDetails.mFixedProbeIndex) {
					dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
					dataPoint.calculate(false);
					dataPoint.clearDetails();
				}	
				
				dataPoint = dataPointLookup.get(++dataPointIndex);
			}
			
			bufferedFileReader.close();
			
			//dataSet.loadFromFileComplete();
		} catch (Exception theException) {
			theException.printStackTrace();
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}

	/**
	 * Imports point data from a Vector VNO file.
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param dataSet The data set to add the point to
	 * @param delimiter The delimiter used in the data file
	 * @return 
	 * @throws BackEndAPIException 
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromConvertedVectrinoIIFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		String dataFileName = importDetails.mFile.getAbsolutePath();
		int cellSize = 1;
		int numberOfDataPoints = 1;
		
		int NUMBER_OF_FIELDS = 0;
		final int X_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Y_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Z1_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Z2_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_1_CORRELATION_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_2_CORRELATION_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_3_CORRELATION_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_4_CORRELATION_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_1_SNR_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_2_SNR_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_3_SNR_INDEX = NUMBER_OF_FIELDS++;
		final int BEAM_4_SNR_INDEX = NUMBER_OF_FIELDS++;
		
		Hashtable<String, Integer> fieldIndexLookup = new Hashtable<String, Integer>(NUMBER_OF_FIELDS);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_X_VELOCITIES, X_VELOCITY_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_Y_VELOCITIES, Y_VELOCITY_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_Z1_VELOCITIES, Z1_VELOCITY_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_Z2_VELOCITIES, Z2_VELOCITY_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_1_CORRELATIONS, BEAM_1_CORRELATION_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_2_CORRELATIONS, BEAM_2_CORRELATION_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_3_CORRELATIONS, BEAM_3_CORRELATION_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_4_CORRELATIONS, BEAM_4_CORRELATION_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_1_SNRS, BEAM_1_SNR_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_2_SNRS, BEAM_2_SNR_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_3_SNRS, BEAM_3_SNR_INDEX);
		fieldIndexLookup.put(DADefinitions.VECTRINO_II_BEAM_4_SNRS, BEAM_4_SNR_INDEX);
		
		// Read the header file for setup information
		BufferedReader bufferedFileReader = null;
		String line = null;
		try{
			String headerFileName = dataFileName.substring(0, dataFileName.lastIndexOf('.')) + DADefinitions.NDV_HEADER_FILE_EXTENSION;
			FileReader headerFileReader = new FileReader(headerFileName);
			bufferedFileReader = new BufferedReader(headerFileReader);
			
			while ((line = bufferedFileReader.readLine()) != null) {
				// Ignore blank lines
				if (line.length() == 0) {
					continue;
				}
			
				StringTokenizer tokenizer = new StringTokenizer(line.toString(), DADefinitions.VECTRINO_II_HDR_FILE_DELIMITER);
				
				if (tokenizer.countTokens() != 2) {
					continue;
				}
				
				String tag = tokenizer.nextToken();
				double value;
				try {
					value = Double.parseDouble(tokenizer.nextToken());
				} catch (Exception e) {
					continue;
				}
				
				if (tag.startsWith(DADefinitions.VECTRINO_II_SAMPLING_RATE_TAG)) {
					dataSet.setSamplingRate(value);
				} else if (tag.startsWith(DADefinitions.VECTRINO_II_CELL_SIZE_TAG)) {
					cellSize = (int) Math.ceil(value * DADefinitions.VECTRINO_II_CELL_SIZE_SCALAR);
				} else if (tag.startsWith(DADefinitions.VECTRINO_II_NUMBER_OF_CELLS_TAG)) {
					numberOfDataPoints = (int) value;
				}
			}	
			
			bufferedFileReader.close();
		} catch (Exception e) {
			try {
				bufferedFileReader.close();
			} catch (IOException e1) {
			}
			
			MAJFCLogger.log(line);
			throw new BackEndAPIException(DAStrings.getString(DAStrings.VECTRINO_HEADER_FILE_MISSING_TITLE), DAStrings.getString(DAStrings.VECTRINO_HEADER_FILE_MISSING_MSG) + dataFileName);
		}

		Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>(numberOfDataPoints);
		Vector<DataPoint> dataPoints = new Vector<DataPoint>(numberOfDataPoints);
		
		for (int i = 0; i < numberOfDataPoints; ++i) {
			dataPoints.add(dataSet.createNewDataPoint(importDetails.getFirstYCoord(), importDetails.getFirstZCoord() - (cellSize * i), importDetails, 0, dataSet));
		}

		// Now read the data file
		try {
			FileReader fileReader = new FileReader(importDetails.mFile);
			bufferedFileReader = new BufferedReader(fileReader);
			LinkedList<Vector<Double>> data = new LinkedList<Vector<Double>>();
			
			while ((line = bufferedFileReader.readLine()) != null) {
				// Ignore blank lines
				if (line.length() == 0) {
					continue;
				}
				
				StringTokenizer tokenizer = new StringTokenizer(line.toString(), DADefinitions.VECTRINO_II_DAT_FILE_DELIMITER);
				
				if (tokenizer.countTokens() != 2) {
					continue;
				}
				
				String tag = tokenizer.nextToken();
				String values = tokenizer.nextToken();

				if (tag.contains(DADefinitions.VECTRINO_II_X_VELOCITIES)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_X_VELOCITIES), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_Y_VELOCITIES)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_Y_VELOCITIES), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_Z1_VELOCITIES)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_Z1_VELOCITIES), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_Z2_VELOCITIES)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_Z2_VELOCITIES), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_1_CORRELATIONS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_1_CORRELATIONS), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_2_CORRELATIONS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_2_CORRELATIONS), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_3_CORRELATIONS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_3_CORRELATIONS), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_4_CORRELATIONS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_4_CORRELATIONS), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_1_SNRS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_1_SNRS), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_2_SNRS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_2_SNRS), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_3_SNRS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_3_SNRS), extractVectrinoIIValues(values, numberOfDataPoints));
				} else if (tag.contains(DADefinitions.VECTRINO_II_BEAM_4_SNRS)) {
					data.add(fieldIndexLookup.get(DADefinitions.VECTRINO_II_BEAM_4_SNRS), extractVectrinoIIValues(values, numberOfDataPoints));
				} 

				// If all fields are filled then add the data to the data points and move to the next measurements
				if (data.size() == fieldIndexLookup.size()) {
					for (int dataPointIndex = 0; dataPointIndex < numberOfDataPoints; ++dataPointIndex) {
						DataPoint dataPoint = dataPoints.elementAt(dataPointIndex);

						for (int componentIndex = 0; componentIndex < Z2_VELOCITY_INDEX; ++componentIndex) {
							if (componentIndex == X_VELOCITY_INDEX) {
								dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, data.get(componentIndex).elementAt(dataPointIndex), data.get(BEAM_1_CORRELATION_INDEX).elementAt(dataPointIndex), data.get(BEAM_1_SNR_INDEX).elementAt(dataPointIndex), DADefinitions.INVALID_SNR_OR_CORRELATION);
							} else if (componentIndex == Y_VELOCITY_INDEX) {
								dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, data.get(componentIndex).elementAt(dataPointIndex), data.get(BEAM_2_CORRELATION_INDEX).elementAt(dataPointIndex), data.get(BEAM_2_SNR_INDEX).elementAt(dataPointIndex), DADefinitions.INVALID_SNR_OR_CORRELATION);
							} else {
								double z1Velocity = data.get(Z1_VELOCITY_INDEX).elementAt(dataPointIndex);
								double z2Velocity = data.get(Z2_VELOCITY_INDEX).elementAt(dataPointIndex);
								double zVel = (z1Velocity + z2Velocity)/2d;
								double beam4Corr = (data.get(BEAM_3_CORRELATION_INDEX).elementAt(dataPointIndex) + data.get(BEAM_4_CORRELATION_INDEX).elementAt(dataPointIndex))/2d;
								double beam4SNR = (data.get(BEAM_3_SNR_INDEX).elementAt(dataPointIndex) + data.get(BEAM_4_SNR_INDEX).elementAt(dataPointIndex))/2d;
								double wDiff = calculateWDiff(z1Velocity, z2Velocity);
								dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, zVel, beam4Corr, beam4SNR, wDiff);
							}
						}
					}
					
					data.clear();
				}
			}

			bufferedFileReader.close();

			for (int dataPointIndex = 0; dataPointIndex < numberOfDataPoints; ++dataPointIndex) {
				DataPoint dataPoint = dataPoints.elementAt(dataPointIndex);
				dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				dataPoint.calculate();
				dataPoint.clearDetails();
				addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
			}
			
			return addedDataPoints;
		}
		catch (Exception theException){
			try {
				bufferedFileReader.close();
			} catch (IOException e) {
			}
			
			MAJFCLogger.log(line);
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}
	
	/**
	 * Extracts the values from a Vectrino II values array.
	 * @param values
	 * @param expectedNumberOfValues
	 * @return
	 * @throws Exception
	 */
	private Vector<Double> extractVectrinoIIValues(String values, int expectedNumberOfValues) throws Exception {
		values = values.substring(values.indexOf('[') + 1, values.indexOf(']'));
		StringTokenizer st = new StringTokenizer(values, " \t");
		
		if (st.countTokens() != expectedNumberOfValues) {
			throw new Exception();
		}

		Vector<Double> doubleValues = new Vector<Double>(expectedNumberOfValues);
		for (int i = 0; i < expectedNumberOfValues; ++i) {
			doubleValues.add(MAJFCTools.parseDouble(st.nextToken()));
		}
		
		return doubleValues;
	}

	/**
	 * Imports point data from a Vectrino II Matlab file.
	 * 
	 * @param file The file to import from
	 * @param importDetails The details of the setup for import
	 * @return 
	 * @throws BackEndAPIException 
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromVectrinoIIMatFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		int NUMBER_OF_FIELDS = 0;
		final int X_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Y_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		@SuppressWarnings("unused")
		final int Z1_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Z2_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
	
		try {
			MatFileReader mfr = new MatFileReader(importDetails.mFile);

			MLStructure configArray = (MLStructure) mfr.getMLArray(DADefinitions.VECTRINO_II_MATLAB_CONFIG_ARRAY);
			double samplingRate = ((MLDouble) configArray.getField(DADefinitions.VECTRINO_II_MATLAB_SAMPLING_RATE)).get(0);
			dataSet.setSamplingRate(samplingRate);
			
			double cellSize = ((MLDouble) configArray.getField(DADefinitions.VECTRINO_II_MATLAB_CELL_SIZE)).get(0);
			cellSize = (int) Math.ceil(cellSize * DADefinitions.VECTRINO_II_CELL_SIZE_SCALAR);
			
			int numberOfDataPoints = ((MLDouble) configArray.getField(DADefinitions.VECTRINO_II_MATLAB_NUMBER_OF_CELLS)).get(0).intValue();
			
			MLStructure dataArray = (MLStructure) mfr.getMLArray(DADefinitions.VECTRINO_II_MATLAB_DATA_ARRAY);
			MLDouble xVelocities = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_X_VELOCITIES);
			MLDouble yVelocities = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_Y_VELOCITIES);
			MLDouble z1Velocities = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_Z1_VELOCITIES);
			MLDouble z2Velocities = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_Z2_VELOCITIES);
			MLDouble beam1Correlations = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_1_CORRELATIONS);
			MLDouble beam2Correlations = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_2_CORRELATIONS);
			MLDouble beam3Correlations = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_3_CORRELATIONS);
			MLDouble beam4Correlations = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_4_CORRELATIONS);
			MLDouble beam1SNRs = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_1_SNRS);
			MLDouble beam2SNRs = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_2_SNRS);
			MLDouble beam3SNRs = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_3_SNRS);
			MLDouble beam4SNRs = (MLDouble) dataArray.getField(DADefinitions.VECTRINO_II_MATLAB_BEAM_4_SNRS);
			
			Vector<DataPointCoordinates> addedDataPoints = new Vector<DataPointCoordinates>(numberOfDataPoints);
			Vector<DataPoint> dataPoints = new Vector<DataPoint>(numberOfDataPoints);
			int numberOfMeasurements = xVelocities.getM();
			
			for (int dataPointIndex = 0; dataPointIndex < numberOfDataPoints; ++dataPointIndex) {
				DataPoint dataPoint = dataSet.createNewDataPoint(importDetails.getFirstYCoord(), importDetails.getFirstZCoord() - ((int) cellSize * dataPointIndex), importDetails, 0, dataSet);
				dataPoints.add(dataPoint);

				for (int measurementIndex = 0; measurementIndex < numberOfMeasurements; ++measurementIndex) {
					for (int componentIndex = 0; componentIndex < Z2_VELOCITY_INDEX; ++componentIndex) {
						if (componentIndex == X_VELOCITY_INDEX) {
							dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, xVelocities.get(measurementIndex, dataPointIndex), beam1Correlations.get(measurementIndex, dataPointIndex), beam1SNRs.get(measurementIndex, dataPointIndex), DADefinitions.INVALID_SNR_OR_CORRELATION);
						} else if (componentIndex == Y_VELOCITY_INDEX) {
							dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, yVelocities.get(measurementIndex, dataPointIndex), beam2Correlations.get(measurementIndex, dataPointIndex), beam2SNRs.get(measurementIndex, dataPointIndex), DADefinitions.INVALID_SNR_OR_CORRELATION);
						} else {
							double z1Velocity = z1Velocities.get(measurementIndex, dataPointIndex);
							double z2Velocity = z2Velocities.get(measurementIndex, dataPointIndex);
							double zVel = (z1Velocity + z2Velocity)/2d;
							double beam4Corr = (beam3Correlations.get(measurementIndex, dataPointIndex) + beam4Correlations.get(measurementIndex, dataPointIndex))/2d;
							double beam4SNR = (beam3SNRs.get(measurementIndex, dataPointIndex) + beam4SNRs.get(measurementIndex, dataPointIndex))/2d;
							double wDiff = calculateWDiff(z1Velocity, z2Velocity);
							dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, zVel, beam4Corr, beam4SNR, wDiff);
						}
					}
				}
			}

			for (int dataPointIndex = 0; dataPointIndex < numberOfDataPoints; ++dataPointIndex) {
				DataPoint dataPoint = dataPoints.elementAt(dataPointIndex);
				dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				dataPoint.calculate();
				dataPoint.clearDetails();
				addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
			}
			
			return addedDataPoints;
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}
	

	/**
	 * Imports point data from a Vectrino II .raw file.
	 * 
	 * @param file The file to import from
	 * @param importDetails The details of the setup for import
	 * @return 
	 * @throws BackEndAPIException 
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromVectrinoIIRawFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		try {
			DataSetConfig configData = dataSet.getConfigData();
			double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
//			Double measurementsPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);

			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(importDetails.mFile));

			byte[] protocolHeader;
			
			try {
				protocolHeader = findNextSyncAndId(bufferedInputStream, 0, 6);
			} catch (MAJFCToolsEoFException theException) {
				// If the end of the file is reached here then that isn't a problem, just finish reading 
				protocolHeader = null;
			}
			
			int numberOfCells = 0;
			double cellSize = 10d;
			Vector<DataPoint> dataPoints = null;
			final int NUMBER_OF_BEAMS = 4;
			Vector<Vector<Integer>> noise = new Vector<Vector<Integer>>(NUMBER_OF_BEAMS);
			
			while (protocolHeader != null) {
				switch (protocolHeader[2]) {
					case (byte) 0x06:
						// As far as I can tell, we can only determine the block type from the data size!
						int dataSize = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {protocolHeader[4], protocolHeader[5]});
						switch (dataSize) {
							case 184: {
									// User configuration
									byte[] userConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, dataSize);
									samplingRate = MAJFCTools.makeSignedLongFromBytes(new byte[] {userConfigurationHeader[20], userConfigurationHeader[21], userConfigurationHeader[22], userConfigurationHeader[23]});
									dataSet.setSamplingRate(samplingRate);

									numberOfCells = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {userConfigurationHeader[52], userConfigurationHeader[53]});
									cellSize = MAJFCTools.makeSignedLongFromBytes(new byte[] {userConfigurationHeader[56], userConfigurationHeader[57], userConfigurationHeader[58], userConfigurationHeader[59]});
									cellSize = (int) Math.ceil(cellSize * DADefinitions.VECTRINO_II_CELL_SIZE_SCALAR);
									
									dataPoints = new Vector<DataPoint>(numberOfCells);
									for (int dataPointIndex = 0; dataPointIndex < numberOfCells; ++dataPointIndex) {
										DataPoint dataPoint = dataSet.createNewDataPoint(importDetails.getFirstYCoord(), importDetails.getFirstZCoord() - ((int) cellSize * dataPointIndex), importDetails, 0, dataSet);
										dataPoints.add(dataPoint);
									}
								}
								break;
							case 104: {
									// Hardware configuration
//									byte[] hardwareConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, dataSize);
								}
								break;
							case 224: {
									// Head configuration
//									byte[] headConfigurationHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, dataSize);
								}
								break;
						}
						break;
					case (byte) 0x00:
						switch (protocolHeader[3]) {
							case (byte) 0x50: {
									// Velocity data header
									byte[] velocityDataHeader = MAJFCTools.readBytesFromFile(bufferedInputStream, 24 + 12 * numberOfCells);
									
									noise.removeAllElements();
									for (int i = 0; i < NUMBER_OF_BEAMS; ++i) {
										noise.add(new Vector<Integer>(numberOfCells));
									}
									
									int byteIndex = 24;
									for (int beamIndex = 0; beamIndex < NUMBER_OF_BEAMS; ++beamIndex) {
										for (int i = 0; i < numberOfCells; ++i) {
											noise.get(beamIndex).add(MAJFCTools.makeUnsignedIntFromBytes(new byte[] {velocityDataHeader[byteIndex], velocityDataHeader[byteIndex]}));
											byteIndex += 2;
										}
									}
								}
								break;
							case (byte) 0x51: {
									// Velocity data
									byte[] velocityData = MAJFCTools.readBytesFromFile(bufferedInputStream, 16 + 21 * numberOfCells);
									double velocityScalar = Math.pow(10, MAJFCTools.makeSignedIntFromBytes(new byte[] {velocityData[3]}));
									
									for (int dataPointIndex = 0; dataPointIndex < numberOfCells; ++dataPointIndex) {
										DataPoint dataPoint = dataPoints.get(dataPointIndex);
										
										int uByteIndex = 16 + 2 * dataPointIndex;
										int vByteIndex = uByteIndex + 2 * numberOfCells;
										int w1ByteIndex = uByteIndex + 4 * numberOfCells;
										int w2ByteIndex = uByteIndex + 6 * numberOfCells;
										double u = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[uByteIndex], velocityData[uByteIndex + 1]}) * velocityScalar;
										double v = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[vByteIndex], velocityData[vByteIndex + 1]}) * velocityScalar;
										double w1 = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[w1ByteIndex], velocityData[w1ByteIndex + 1]}) * velocityScalar;
										double w2 = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[w2ByteIndex], velocityData[w2ByteIndex + 1]}) * velocityScalar;

										int uAmpByteIndex = 16 + 8 * numberOfCells + 2 * dataPointIndex;
										int vAmpByteIndex = uAmpByteIndex + 2 * numberOfCells;
										int w1AmpByteIndex = uAmpByteIndex + 4 * numberOfCells;
										int w2AmpByteIndex = uAmpByteIndex + 6 * numberOfCells;
										double uAmp = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[uAmpByteIndex], velocityData[uAmpByteIndex + 1]});
										double vAmp = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[vAmpByteIndex], velocityData[vAmpByteIndex + 1]});
										double w1Amp = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[w1AmpByteIndex], velocityData[w1AmpByteIndex + 1]});
										double w2Amp = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[w2AmpByteIndex], velocityData[w2AmpByteIndex + 1]});

										int uCorrByteIndex = 16 + 16 * numberOfCells + dataPointIndex;
										int vCorrByteIndex = uCorrByteIndex + 2 * numberOfCells;
										int w1CorrByteIndex = uCorrByteIndex + 4 * numberOfCells;
										int w2CorrByteIndex = uCorrByteIndex + 6 * numberOfCells;
										double uCorr = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[uCorrByteIndex]});
										double vCorr = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[vCorrByteIndex]});
										double w1Corr = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[w1CorrByteIndex]});
										double w2Corr = MAJFCTools.makeSignedLongFromBytes(new byte[] {velocityData[w2CorrByteIndex]});

										dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, u, uCorr, calculateSNR(uAmp, noise.get(0).get(dataPointIndex)), Math.abs(w1 - w2));
										dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, v, vCorr, calculateSNR(vAmp, noise.get(1).get(dataPointIndex)), Math.abs(w1 - w2));
										dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, (w1 + w2)/2d, (w1Corr + w2Corr)/2d, calculateSNR((w1Amp + w2Amp)/2d, (noise.get(2).get(dataPointIndex) + noise.get(3).get(dataPointIndex))/2d), Math.abs(w1 - w2));
									}
								}
								break;
							case (byte) 0x61:
								// Bottom check data
								break;
							case (byte) 0x62:
								// Beam check data
								break;
						}
						break;
				}

			}

			Vector<DataPointCoordinates> addedDataPoints = new Vector<DataPointCoordinates>(numberOfCells);

			for (int dataPointIndex = 0; dataPointIndex < numberOfCells; ++dataPointIndex) {
				DataPoint dataPoint = dataPoints.elementAt(dataPointIndex);
				dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
				dataPoint.calculate();
				dataPoint.clearDetails();
				addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
			}
			
			return addedDataPoints;
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_SET_OPEN_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}
}
