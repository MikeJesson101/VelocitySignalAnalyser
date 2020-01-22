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
import java.io.FileInputStream;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import java.util.HashMap;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetail;
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
public class CobraDataPointImporter extends AbstractDataPointImporter {
	private final HashMap<Integer, String> mDeviceTypeLookup;
	
	/**
	 * Constructor 
	 */
	public CobraDataPointImporter() {
		mDeviceTypeLookup = new HashMap<Integer, String>(17);
		mDeviceTypeLookup.put(0, "Cobra");
		mDeviceTypeLookup.put(4, "Four-Hole Cobra");
		mDeviceTypeLookup.put(5, "Five-Hole Cobra");
		mDeviceTypeLookup.put(13, "Thirteen-Hole ECA");
		mDeviceTypeLookup.put(1000, "DP Module");
		mDeviceTypeLookup.put(1015, "15-Channel DP Module");
		mDeviceTypeLookup.put(1016, "16-Channel DP Module");
		mDeviceTypeLookup.put(1032, "32-Channel DP Module");
		mDeviceTypeLookup.put(1064, "64-Channel DP Module");
		mDeviceTypeLookup.put(1128, "128-Channel DP Module");
		mDeviceTypeLookup.put(1256, "256-Channel DP Module");
		mDeviceTypeLookup.put(8000, "Force Balance");
		mDeviceTypeLookup.put(8010, "JR3 Balance");
		mDeviceTypeLookup.put(8011, "JR3 Balance (A/D)");
		mDeviceTypeLookup.put(8012, "JR3 Balance (DSP)");
		mDeviceTypeLookup.put(8020, "Aeroelastic Base Balance");
		mDeviceTypeLookup.put(10000, "Analogue Data Device");
	}

	/**
	 * Imports point data from a Cobra *.tha file.
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param dataSet The data set to add the point to
	 * @param delimiter The delimiter used in the data file
	 * @throws BackEndAPIException 
	 */
	public Vector<DataPointCoordinates> importDataPointDataFromCobraTHxFile(int probeIndex, DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		validateMainAndFixedProbeIndices(importDetails);
		
		DataSet theDataSet = probeIndex != importDetails.mFixedProbeIndex ? dataSet : getFixedProbeDataSet(importDetails.mMainProbeIndex, importDetails.mFixedProbeIndex, importDetails.mCoordsList, dataSet);
		DataPointCoordinates coords = importDetails.mCoordsList.get(probeIndex);
		DataPoint dataPoint = theDataSet.createNewDataPoint(coords.getY(), coords.getZ(), importDetails, probeIndex, theDataSet);
		Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>(10);
		ProbeDetail probeDetails = new ProbeDetail();
		
		try {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(importDetails.mFile));

			byte[] fileFormatBytes = MAJFCTools.readBytesFromFile(bufferedInputStream, 4);
			int fileFormat =  MAJFCTools.makeSignedIntFromBytes(fileFormatBytes);
			   
			CobraHeader header = new CobraHeader();
			
			if (fileFormat == 1 || fileFormat == 6401) {
				ByteBuffer headerBytes = ByteBuffer.wrap(MAJFCTools.readBytesFromFile(bufferedInputStream, 61));
				header.probeID = headerBytes.getInt();
				MAJFCLogger.log("Probe Id: " + header.probeID);
				header.dataType = 100;
				header.numSamples = headerBytes.getInt();
				header.blockSize = headerBytes.getInt();
				header.dataRate = headerBytes.getDouble();
				header.hasPref = headerBytes.get() > 0;
								   
				if (header.numSamples == 0) {
//								    	  header.numSamples = NumSamplesPerChannel(fid,numChannels);
				}
			}  else if (fileFormat == 2) {
				ByteBuffer headerBytes = ByteBuffer.wrap(MAJFCTools.readBytesFromFile(bufferedInputStream, 61));
				header.probeID = headerBytes.getInt();
				header.dataType = 100;
				header.numSamples = headerBytes.getInt();
				header.blockSize = headerBytes.getInt();
				header.dataRate = headerBytes.getDouble();
				header.Pbaro = headerBytes.getDouble();
				header.meanTemperature = headerBytes.getDouble();
				header.hasPref = headerBytes.get() > 0;

				if (header.numSamples == 0) {
//   header.numSamples = header.numSamplesPerChannel(fid,header.numChannels);
				}
			} else if (fileFormat == 3) {
				ByteBuffer headerBytes = ByteBuffer.wrap(MAJFCTools.readBytesFromFile(bufferedInputStream, 61));
				headerBytes.order(ByteOrder.LITTLE_ENDIAN);
				header.deviceType = headerBytes.getInt();
				header.deviceID = headerBytes.getInt();
				header.dateType = headerBytes.getInt();
				header.year = headerBytes.getShort();
				header.month = headerBytes.getShort();
				header.dayOfWeek = headerBytes.getShort();
				header.day = headerBytes.getShort();
				header.hour = headerBytes.getShort();
				header.minute = headerBytes.getShort();
				header.second = headerBytes.getShort();
				header.milliSecond = headerBytes.getShort();
				header.numSamples = headerBytes.getInt();
				header.blockSize = headerBytes.getInt();
				header.dataRate = headerBytes.getDouble();
				header.Pbaro = headerBytes.getDouble();
				header.meanTemperature = headerBytes.getDouble();
				header.hasPref = headerBytes.get() > 0;

				if (header.numSamples == 0) {
				//   header.numSamples = header.numSamplesPerChannel(fid,header.numChannels);
				}
			} else if (fileFormat == 101) {
				ByteBuffer headerBytes = ByteBuffer.wrap(MAJFCTools.readBytesFromFile(bufferedInputStream, 61));
				header.dateType = headerBytes.getInt();
				header.dateOrder = headerBytes.getInt();
				header.numChannels = headerBytes.getInt();
				header.numSamples = headerBytes.getInt();
				header.blockSize = headerBytes.getInt();
				header.dataRate = headerBytes.getDouble();
				header.year = headerBytes.getShort();
				header.month = headerBytes.getShort();
				header.dayOfWeek = headerBytes.getShort();
				header.day = headerBytes.getShort();
				header.hour = headerBytes.getShort();
				header.minute = headerBytes.getShort();
				header.second = headerBytes.getShort();
				header.milliSecond = headerBytes.getShort();
				if (header.numSamples == 0) {
				//   header.numSamples = header.numSamplesPerChannel(fid,header.numChannels);
				}
			} else if (fileFormat == 102) {
				return addedDataPoints;
//headerSize = headerBytes.getInt();
//header.dateType = headerBytes.getInt();
//header.dateOrder = headerBytes.getInt();
//header.numChannels = headerBytes.getInt();
//header.numSamples = headerBytes.getInt();
//blockSize = headerBytes.getInt();
//header.dataRate = headerBytes.getDouble();
//header.year = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//header.month = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//header.dayOfWeek = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//header.day = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//header.hour = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//header.minute = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//header.second = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//header.milliSecond = MAJFCTools.makeSignedIntFromBytes(extractBytes(headerBytes, headerBytesIndex, 1));
//for n = 1:header.numChannels
//   labelLength = headerBytes.getInt();
//   chanLabels(n) = { char( fread(fid,labelLength,UCHAR)" ) };
//}
//for n = 1:header.numChannels
//   unitAbrLength = headerBytes.getInt();
//   chanUnitAbrs(n) = { char( fread(fid,unitAbrLength,UCHAR)" ) };
//}
//
//if (header.numSamples == 0) {
//   header.numSamples = header.numSamplesPerChannel(fid,header.numChannels);
//}
//			   
			} else if (fileFormat == 201) {
				return addedDataPoints;
//header.deviceType = headerBytes.getInt();
//header.deviceID = headerBytes.getInt();
//header.deviceName = sprintf("%s %0.3i" + header.deviceTypeShortName(header.deviceType), header.deviceID);
//header.dateType = headerBytes.getInt();
//header.dateOrder = headerBytes.getInt();
//header.numChannels = headerBytes.getInt();
//header.numSamples = headerBytes.getInt();
//blockSize = headerBytes.getInt();
//header.dataRate = headerBytes.getDouble();
//header.year = headerBytes.getShort();
//header.month = headerBytes.getShort();
//header.dayOfWeek = headerBytes.getShort();
//header.day = headerBytes.getShort();
//header.hour = headerBytes.getShort();
//header.minute = headerBytes.getShort();
//header.second = headerBytes.getShort();
//header.milliSecond = headerBytes.getShort();
//for n = 1:header.numChannels; chanLabels(n) = {sprintf("Ch %d",n)}; }
//for n = 1:header.numChannels; chanUnitAbrs(n) = {""}; }
//
//if (header.numSamples == 0) {
//   header.numSamples = header.numSamplesPerChannel(fid,header.numChannels);
//}
			} else if (fileFormat == 202) {
				return addedDataPoints;
//				   headerSize = headerBytes.getInt();
//header.deviceNameLength = headerBytes.getInt();
//header.deviceName = char( fread(fid,header.deviceNameLength,UCHAR)" );
//header.deviceType = headerBytes.getInt();
//header.deviceID = headerBytes.getInt();
//header.dateType = headerBytes.getInt();
//header.dateOrder = headerBytes.getInt();
//header.numChannels = headerBytes.getInt();
//header.numSamples = headerBytes.getInt();
//blockSize = headerBytes.getInt();
//header.dataRate = headerBytes.getDouble();
//header.year = headerBytes.getShort();
//header.month = headerBytes.getShort();
//header.dayOfWeek = headerBytes.getShort();
//header.day = headerBytes.getShort();
//header.hour = headerBytes.getShort();
//header.minute = headerBytes.getShort();
//header.second = headerBytes.getShort();
//header.milliSecond = headerBytes.getShort();
////				  for n = 1:header.numChannels
//   labelLength = headerBytes.getInt();
//   chanLabels(n) = { char( fread(fid,labelLength,UCHAR)" ) };
//}
//for n = 1:header.numChannels
//   unitAbrLength = headerBytes.getInt();
//   chanUnitAbrs(n) = { char( fread(fid,unitAbrLength,UCHAR)" ) };
//}
//
//if (header.numSamples == 0)
//   header.numSamples = header.numSamplesPerChannel(fid,header.numChannels);
//}
			} else {
				MAJFCLogger.log("File format not supported!	");
				return addedDataPoints;     
			}
			   
			probeDetails.set(BackEndAPI.PD_KEY_PROBE_TYPE, mDeviceTypeLookup.get((int) header.deviceType));
			probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, String.valueOf(header.deviceID));
			probeDetails.set(BackEndAPI.PD_KEY_SAMPLING_RATE, String.valueOf(header.dataRate));
			theDataSet.setSamplingRate(header.dataRate);
			
			DataSetConfig configData = theDataSet.getConfigData();
			Double measurementsPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
			
			if (fileFormat == 1 || fileFormat == 6401 || fileFormat == 2 || fileFormat == 3) {
				   // Seek to required start position
//if (offset > header.numSamples) {
//   MAJFCLogger.log("Offset larger than number of samples in file!\n");
//   return;
//}
//blockStartSample = floor(offset/header.blockSize)*header.blockSize;
				if (header.hasPref) {
				//   fseek(fid, blockStartSample*4*5, "cof");
				} else {
				//   fseek(fid, blockStartSample*4*4, "cof");
				}

				int numBlocks = (int) (header.numSamples/header.blockSize);
				int numberOfMeasurements = 0;

				for (int blockNum = 0; blockNum < numBlocks; ++blockNum) {
					final int SIZE_OF_FLOAT32 = 4;
					int numberOfParameters = header.hasPref ? 5 : 4;
					int bytesPerParameterPerBlock = (int) (header.blockSize * SIZE_OF_FLOAT32);
					FloatBuffer block = ByteBuffer.wrap(MAJFCTools.readBytesFromFile(bufferedInputStream, (int) (bytesPerParameterPerBlock * numberOfParameters))).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
					float[][] parameters = new float[numberOfParameters][(int) header.blockSize];
			    	  
					for (int parameterIndex = 0; parameterIndex < numberOfParameters; ++parameterIndex) {
						block.get(parameters[parameterIndex]);
					}
		    		
					for (int i = 0; i < header.blockSize; ++i) {
						// Do this here (rather than after the increment of numberOfMeasurements) so that we only create a new data point
						// if there are measurements to go in it
						if (numberOfMeasurements > 0 && measurementsPerSplit.equals(Double.NaN) == false && (numberOfMeasurements % measurementsPerSplit == 0)) {
							addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
							dataPoint = createNewDPForSplit(theDataSet, dataPoint, (int) (measurementsPerSplit/header.dataRate), importDetails, probeIndex, probeDetails);
						}

//						if (parameters[0][i] == 0) {
//							continue;
//						}
						
						dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, parameters[0][i]);
						dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, parameters[1][i]);
						dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, parameters[2][i]);
						dataPoint.addMeasuredPressure(parameters[3][i]);
						if (header.hasPref) {
//							dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_P_REF_COMPONENT, MAJFCTools.makeSignedLongFromBytes(pRef));
						}
			    		  
						++numberOfMeasurements;
					}
				}
			} else if (fileFormat == 101 || fileFormat == 102 || fileFormat == 201 || fileFormat == 202) {
					return addedDataPoints;
//// Seek to required start position
//if (offset > numSamples) {
//   MAJFCLogger.log("Offset larger than number of samples in file!\n");
//   return;
//}
//blockStartSample = floor(offset/blockSize)*blockSize;
//fseek(fid, blockStartSample*numChannels*4, "cof");
//
//if (numSamplesToProcess == 0) {
//   numBlocks = numSamples / blockSize;
//} else {
//   numBlocks = floor(numSamplesToProcess/blockSize);
//}
//
//if (dataOrder == 0) { // not Interleaved
//   varargout{1} = zeros(numBlocks*blockSize, numChannels);
//   for blockNum = 1:numBlocks
//      index1 = (blockNum-1) * blockSize + 1;
//      index2 = blockNum * blockSize;
//
//      varargout{1}(index1:index2,:) = fread(fid,[blockSize,numChannels],"float32");
//   }
//				} else if (dataOrder == 1) { // doInterleaved
//   varargout{1} = fread(fid,[numChannels,blockSize],"float32")";
//			    }
			   
			}   
			
			// Close file
			bufferedInputStream.close();

			// All data read, now add the data points to the data set
			theDataSet.addDataPointImportedFromFileNoCheck(dataPoint);
			dataPoint.setProbeDetails(probeDetails);
			dataPoint.calculate();
			dataPoint.clearDetails();
			
			addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));

			return addedDataPoints;
		} catch (Exception theException) {
			theException.printStackTrace();
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}

	/**
	 * Imports a Cobra THA multi-run data file
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
	public void importMultiRunDataPointDataFromCobraTHxFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				String fileName = importDetails.mFile.getName();
				char probeId = fileName.charAt(fileName.length() - 1);
				int probeIndex = Character.getNumericValue(probeId) - Character.getNumericValue('A');
				
				if (probeIndex >= importDetails.mCoordsList.size()) {
					throw new BackEndAPIException("Unrecognised probe id", "Probe index too big: " + probeIndex);
				}
				
				Vector<DataPointCoordinates> createdDataPoints = importDataPointDataFromCobraTHxFile(probeIndex, dataSet, importDetails);
				
				// If this is the fixed probe then we don't need to do any of the stuff after this. Also, don't return any coordinates, otherwise the
				// point will be added to the mean value dataset.
				if (probeIndex == importDetails.mFixedProbeIndex) {
					return null;
				}
				
				return createdDataPoints;
			}
		});
	}
	
	private class CobraHeader {
		long dateOrder;
		long numChannels;
		long dateType;
		long deviceID;
		long deviceType;
		long probeID;
		long dataType;
		long numSamples;
		long blockSize;
		double dataRate;
		boolean hasPref;
		double Pbaro;
		double meanTemperature;
		int year;
		int month;
		int dayOfWeek;
		int day;
		int hour;
		int minute;
		int second;
		int milliSecond;
	}
}
