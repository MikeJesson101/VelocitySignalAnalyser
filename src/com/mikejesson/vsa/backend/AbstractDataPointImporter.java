/**
 * 
 */
package com.mikejesson.vsa.backend;


import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.backend.GenericDataPointImporter.ImportCommand;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;


/**
 * @author Mike
 *
 */
public class AbstractDataPointImporter {
	
	/**
	 * Gets the fixed probe data set for the specified data set, at the coordinates specified by coords[fixedProbeIndex].
	 * @param fixedProbeIndex The index of the fixed probe with the list of probes being imported
	 * @param coords The coordinates list for the file being imported
	 * @param dataSet The main data set to get the fixed probe data set for
	 * @return The appropriate fixed probe data set, or null if there is no fixed probe data set
	 * @throws BackEndAPIException if fixedProbeIndex is outside the length of coords
	 */
	protected FixedProbeDataSet getFixedProbeDataSet(int mainProbeIndex, int fixedProbeIndex, LinkedList<DataPointCoordinates> coords, DataSet dataSet) throws BackEndAPIException {
		if (fixedProbeIndex < 0) {
			MAJFCLogger.log("No fixed probe index specified for data file: " + coords.get(mainProbeIndex).toString());
			return null;
		}
		
		if (fixedProbeIndex >= coords.size()) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.INVALID_FIXED_PROBE_INDEX_ERROR_TITLE), DAStrings.getString(DAStrings.INVALID_FIXED_PROBE_INDEX_ERROR_MSG));
		}

		return dataSet.getFixedProbeDataSet(coords.get(fixedProbeIndex));
	}
	
	protected DataPoint createNewDPForSplit(DataSet dataSet, DataPoint currentDataPoint, int yCoordIncrement, GenericImportDetails importDetails, int probeIndex, ProbeDetail probeDetails) throws BackEndAPIException {
		yCoordIncrement = Math.max(1, yCoordIncrement);
		dataSet.addDataPointImportedFromFileNoCheck(currentDataPoint);
		currentDataPoint.calculate();
		currentDataPoint.clearDetails();
		currentDataPoint.setProbeDetails(probeDetails);
		
		return dataSet.createNewDataPoint(currentDataPoint.getYCoord() + yCoordIncrement, currentDataPoint.getZCoord(), importDetails, probeIndex, dataSet);
	}
	
	protected void validateMainAndFixedProbeIndices(GenericImportDetails importDetails) throws BackEndAPIException {
		if (importDetails.mMainProbeIndex < 0 || importDetails.mMainProbeIndex >= importDetails.mCoordsList.size()) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.INVALID_MAIN_PROBE_INDEX_ERROR_TITLE), DAStrings.getString(DAStrings.INVALID_MAIN_PROBE_INDEX_ERROR_MSG));
		}
		
		if (importDetails.mFixedProbeIndex >= 0 && importDetails.mMainProbeIndex == importDetails.mFixedProbeIndex) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_TITLE), DAStrings.getString(DAStrings.INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_MSG));
		}
	}
	
	/**
	 * Imports a multi-run data file
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
	protected void importMultiRunDataPointData(MultiRunMeanValueDataSet dataSet, GenericImportDetails importDetails, ImportCommand importCommand) throws BackEndAPIException {                           
		StringTokenizer st = new StringTokenizer(importDetails.mFile.getName(), DADefinitions.COORDINATE_SEPARATOR);
		int runIndex = Integer.parseInt(st.nextToken());
		
		DataSet runDataSet = dataSet.getRunDataSet(runIndex);
		
		// If we are splitting a large file then there may be additional co-ordinates to those in importDetails.mCoordsList
		Vector<DataPointCoordinates> addedCoordinates = importCommand.execute(runDataSet, importDetails);
		
		// This should occur only if this is a fixed probe data point.
		if (addedCoordinates == null) {
			return;
		}
		
		// Add a dummy data point to the top data set - the values will be updated from the run dataset data points
		// later
		int numberOfDataPoints = addedCoordinates.size(); 
		for (int i = 0; i < numberOfDataPoints; ++i) {
			DataPointCoordinates coords = addedCoordinates.get(i);
			
			// Coords may be null if the datapoint added was a fixed probe datapoint
			if (coords == null) {
				continue;
			}
			
			dataSet.addMeanValueDataPoint(addedCoordinates.get(i).getY(), addedCoordinates.get(i).getZ(), importDetails);
		}
	}
}
