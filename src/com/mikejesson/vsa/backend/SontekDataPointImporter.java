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
import java.nio.ShortBuffer;
import java.util.Vector;

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



/**
 * @author MAJ727
 *
 */
public class SontekDataPointImporter extends AbstractDataPointImporter {
	/**
	 * Constructor 
	 */
	public SontekDataPointImporter() {
	}
	
	/**
	 * Imports point data from a Sontek ADV file.
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
	public Vector<DataPointCoordinates> importDataPointDataFromSontekSingleProbeBinaryFile(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		final int UC_SOFTWARE_VERSION_LENGTH = 8;
		final int UC_PROBE_STATUS_LENGTH = 8;
		final int UC_SPARE_1_LENGTH = 6;
		final int UC_SPARE_2_LENGTH = 2;
		final ByteOrder ENDIAN = ByteOrder.LITTLE_ENDIAN;
		
		DataPoint dataPoint = dataSet.createNewDataPoint(importDetails.getFirstYCoord(), importDetails.getFirstZCoord(), importDetails, 0, dataSet);
		Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>(10);
	
		try{
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(importDetails.mFile));

			byte[] userSetup = MAJFCTools.readBytesFromFile(bufferedInputStream, 324);
			int offset = 0;

			// First 8 bytes are software version...
			StringBuffer sbSoftwareVersion = new StringBuffer(UC_SOFTWARE_VERSION_LENGTH);
			for (int i = 0; i < UC_SOFTWARE_VERSION_LENGTH; ++i) {
				sbSoftwareVersion.append((char) userSetup[offset++]);
			}

			// Next 8 bytes are date-timestamp. 2 bytes for year, then 1 byte each for day, month, minute, hour, hundreths of seconds, seconds
			int year = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getShort(); // C int
			int day = (int) userSetup[offset++];
			int month = (int) userSetup[offset++];
			int minute = (int) userSetup[offset++];
			int hour = (int) userSetup[offset++];
			int hundreths = (int) userSetup[offset++];
			int seconds = (int) userSetup[offset++];
			
			StringBuffer sbProbeStatus = new StringBuffer(UC_PROBE_STATUS_LENGTH);
			for (int i = 0; i < UC_PROBE_STATUS_LENGTH; ++i) {
				sbProbeStatus.append((int) userSetup[offset++]);
			}
			
			int cpuSoftwareVerNum = userSetup[offset++];
			int dspSoftwareVerNum = userSetup[offset++];
			int probeTypeCode = userSetup[offset++];
			int sensorOrientation = userSetup[offset++];
			
			boolean compassInstalled = userSetup[offset++] == 1;
			boolean recorderInstalled = userSetup[offset++] == 1;
			boolean temperatureInstalled = userSetup[offset++] == 1;
			boolean pressureInstalled = userSetup[offset++] == 1;
			int pressureScale = ByteBuffer.wrap(new byte[] {	
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getInt(); // C long
			int pressureOffset = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getInt(); // C long
			int compassOffset = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getShort(); // C int

			StringBuffer sbSpare1 = new StringBuffer(UC_SPARE_1_LENGTH);
			for (int i = 0; i < UC_SPARE_1_LENGTH; ++i) {
				sbSpare1.append((char) userSetup[offset++]);
			}
			
			int unitsSystem = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getShort(); // C int
			float temperature = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getFloat();
			float salinity = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getFloat();
			float speedOfSound = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getFloat();
			float samplingRate = ByteBuffer.wrap(new byte[] {
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++],
					userSetup[offset++]}).order(ENDIAN).getFloat();

			ProbeDetail probeDetails = new ProbeDetail();
			String probeType = "Unrecognised Sontek";
			
			switch (probeTypeCode) {
				case 0:
					probeType = "10 MHz, 5cm";
					break;
				case 1:
					probeType = "10 MHz, 10cm";
					break;
				case 2:
					probeType = "OCEAN";
					break;
			}
			
			probeDetails.set(BackEndAPI.PD_KEY_PROBE_TYPE, probeType);
			probeDetails.set(BackEndAPI.PD_KEY_SAMPLING_RATE, String.valueOf(samplingRate));
			
			byte[] probeConfig = MAJFCTools.readBytesFromFile(bufferedInputStream, 254);
			final int PC_SPARE_1_LENGTH = 10;
			final int PC_PROBE_SERIAL_NUMBER_LENGTH = 6;
			offset = PC_SPARE_1_LENGTH;
			StringBuffer sbProbeSerialNumber = new StringBuffer(PC_PROBE_SERIAL_NUMBER_LENGTH);
			for (int i = 0; i < PC_PROBE_SERIAL_NUMBER_LENGTH; ++i) {
				sbProbeSerialNumber.append((char) probeConfig[offset++]);
			}
			probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, sbProbeSerialNumber.toString());

			DataSetConfig configData = dataSet.getConfigData();
			Double measurementsPerSplit = configData.get(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
			int numberOfMeasurements = 0;
			
			// Now read the data blocks
			while (true) {
				byte[] dataBlock = null;
				@SuppressWarnings("unused")
				byte[] compassBlock = null;
				@SuppressWarnings("unused")
				byte[] tempAndPressureBlock = null;
				
				try {
					dataBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 20);
					
					if (compassInstalled) {
						compassBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 6);
					}
					
					if (temperatureInstalled || pressureInstalled) {
						tempAndPressureBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 6);
					}
				} catch (MAJFCToolsEoFException eof) {
					// No problem, we've just got to the end of the file
					break;
				}

				// First part of data block is a short - sample number.
				byte[] sampleNumber = new byte[] {dataBlock[0], dataBlock[1]};
				double sN = MAJFCTools.makeUnsignedIntFromBytes(sampleNumber);
				byte[] velocities = new byte[12];
				for (int i = 0; i < velocities.length; ++i) {
					velocities[i] = dataBlock[i];
				}
				FloatBuffer dataBlockAsFloats = ByteBuffer.wrap(velocities).order(ByteOrder.BIG_ENDIAN).asFloatBuffer();
				float xVelocity = dataBlockAsFloats.get(0);
				byte[] testBytes = { velocities[7], velocities[6], velocities[5], velocities[4]};
				int test = MAJFCTools.makeSignedIntFromBytes(testBytes);
				float yVelocity = dataBlockAsFloats.get(1);
				float zVelocity = dataBlockAsFloats.get(2);

				double xAmp = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[14]});
				double yAmp = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[15]});
				double zAmp = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[16]});

				double xCorr = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[17]});
				double yCorr = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[18]});
				double zCorr = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[19]});

				// Do this here (rather than after the increment of numberOfMeasurements) so that we only create a new data point
				// if there are measurements to go in it
				if (numberOfMeasurements > 0 && measurementsPerSplit.equals(Double.NaN) == false && (numberOfMeasurements % measurementsPerSplit == 0)) {
					addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
					dataPoint = createNewDPForSplit(dataSet, dataPoint, (int) (measurementsPerSplit/samplingRate), importDetails, 0, probeDetails);
				}
				
				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, xVelocity, xCorr, xAmp, DADefinitions.INVALID_SNR_OR_CORRELATION);
				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, yVelocity, yCorr, yAmp, DADefinitions.INVALID_SNR_OR_CORRELATION);
				dataPoint.addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, zVelocity, zCorr, zAmp, DADefinitions.INVALID_SNR_OR_CORRELATION);
					
				++numberOfMeasurements;
				
				// Event Counter - scrap
				try {
					@SuppressWarnings("unused")
					byte[] eventCounterBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 2);
				} catch (MAJFCToolsEoFException eof) {
					// No problem, we've just got to the end of the file
					break;
				}
			}

			bufferedInputStream.close();

			// All data read, now add the data points to the data set
			dataSet.addDataPointImportedFromFileNoCheck(dataPoint);
			dataPoint.setProbeDetails(probeDetails);
			dataPoint.calculate();
			dataPoint.clearDetails();
			addedDataPoints.add(new DataPointCoordinates(dataPoint.getYCoord(), dataPoint.getZCoord()));
			
			return addedDataPoints;
		} catch (Exception theException){
			theException.printStackTrace();
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}
	
	
	/**
	 * Imports a Sontek ADV multi-run data file
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
	public void importMultiRunDataPointDataFromSontekSingleProbeBinaryFile(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {                           
		importMultiRunDataPointData(dataSet, importDetails, new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return importDataPointDataFromSontekSingleProbeBinaryFile(dataSet, importDetails);
			}
		});
	}
}
