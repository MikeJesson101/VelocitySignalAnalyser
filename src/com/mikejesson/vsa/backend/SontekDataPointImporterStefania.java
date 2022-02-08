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
public class SontekDataPointImporterStefania extends AbstractDataPointImporter {
	/**
	 * Constructor 
	 */
	public SontekDataPointImporterStefania() {
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
		final int SOFTWARE_VERSION_LENGTH = 8;
		final int SOFTWARE_VERSION_OFFSET = 0;
		final int PROBE_ID_LENGTH = 6;
		final int PROBE_ID_OFFSET = 24 + 7;
		
		DataPoint dataPoint = dataSet.createNewDataPoint(importDetails.getFirstYCoord(), importDetails.getFirstZCoord(), importDetails, 0, dataSet);
		Vector<DataPointCoordinates> addedDataPoints = new Vector<BackEndAPI.DataPointCoordinates>(10);
	
		try{
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(importDetails.mFile));

			byte[] userSetup = MAJFCTools.readBytesFromFile(bufferedInputStream, 332);

			// First 8 bytes are software version...
			StringBuffer sbSoftwareVersion = new StringBuffer(SOFTWARE_VERSION_LENGTH);
			for (int i = 0; i < SOFTWARE_VERSION_LENGTH; ++i) {
				sbSoftwareVersion.append((char) userSetup[i + SOFTWARE_VERSION_OFFSET]);
			}

			// .. 8 bytes of date-time
					StringBuffer sbDateTime = new StringBuffer(SOFTWARE_VERSION_LENGTH);
					for (int i = 0; i < SOFTWARE_VERSION_LENGTH; ++i) {
						sbDateTime.append((char) userSetup[i + 8]);
					}

			int probeTypeCode = userSetup[35];
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
			double samplingRate = ByteBuffer.wrap(new byte[] {userSetup[67], userSetup[68], userSetup[69], userSetup[70]}).order(ByteOrder.LITTLE_ENDIAN).getFloat();

			probeDetails.set(BackEndAPI.PD_KEY_SAMPLING_RATE, String.valueOf(samplingRate));
			
			boolean compassInstalled = userSetup[36] == 1;
			boolean temperatureInstalled = userSetup[38] == 1;
			boolean pressureInstalled = userSetup[39] == 1;

			byte[] probeConfig = MAJFCTools.readBytesFromFile(bufferedInputStream, 278);
			StringBuffer sbProbeId = new StringBuffer(PROBE_ID_LENGTH);
			for (int i = 0; i < PROBE_ID_LENGTH; ++i) {
				sbProbeId.append((char) probeConfig[i + PROBE_ID_OFFSET]);
			}
			probeDetails.set(BackEndAPI.PD_KEY_PROBE_ID, sbProbeId.toString());

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
				byte[] velocities = new byte[12];
				for (int i = 0; i < velocities.length; ++i) {
					velocities[i] = dataBlock[i + 2];
				}
				FloatBuffer dataBlockAsFloats = ByteBuffer.wrap(velocities).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
				float xVelocity = dataBlockAsFloats.get(0);
				float yVelocity = dataBlockAsFloats.get(1);
				float zVelocity = dataBlockAsFloats.get(2);

				double xAmp = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[15]});
				double yAmp = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[16]});
				double zAmp = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[17]});

				double xCorr = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[18]});
				double yCorr = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[19]});
				double zCorr = MAJFCTools.makeUnsignedIntFromBytes(new byte[] {dataBlock[20]});

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
				@SuppressWarnings("unused")
				byte[] eventCounterBlock = MAJFCTools.readBytesFromFile(bufferedInputStream, 2);
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
