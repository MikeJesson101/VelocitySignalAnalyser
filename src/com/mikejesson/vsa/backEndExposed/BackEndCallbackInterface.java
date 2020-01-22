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

package com.mikejesson.vsa.backEndExposed;

import java.io.File;
import java.util.Vector;

import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;


/**
 * @author MAJ727
 *
 */
public interface BackEndCallbackInterface {
	/**
	 * Data set created successfully
	 * @param dataSetId The id of the data set
	 * @param dataSetFile The file opened
	 */
	public void onNewDataSetCreated(AbstractDataSetUniqueId dataSetId, File dataSetFile);
	
	/**
	 * Data set opened successfully
	 * @param dataSetId The id of the data set
	 * @param dataSetFile The file opened
	 * @param configData The configuration data for the data point in an array indexed by the BackEndAPI.DSC_KEY_... keys
	 */
	public void onDataSetOpened(AbstractDataSetUniqueId dataSetId, File dataSetFile, DataSetConfig configData);
	
	/**
	 * Data set saved successfully
	 * @param dataSetId The id of the data set
	 * @param oldUniqueId The old id (pre-save) of the data set - this may have changed
	 * @param dataSetFile The file the data set was saved in
	 */
	public void onDataSetSaved(AbstractDataSetUniqueId dataSetId, AbstractDataSetUniqueId oldUniqueId, File dataSetFile);
	
	/**
	 * Data point successfully added
	 * @param dataSetId The id of the data set
	 * @param zCoord The y-coordinate of the data point
	 * @param yCoord The z-coordinate of the data point
	 */
	public void onDataPointAdded(AbstractDataSetUniqueId dataSetId, int yCoord, int zCoord);
	
	/**
	 * Bulk load of data point details successful
	 * @param requestId The id of this load request
	 * @param uniqueId The id of the data set to load data points for
	 * @param zCoords The y-coordinates of the data points
	 * @param yCoords The z-coordinates of the data points
	 * @param dataPointSummaries The data summaries for the data points
	 * @param dataPointDetails The data for the data point in an array of arrays of vectors index by the
	 * BackEndAPI.DPD_KEY_... keys
	 */
	public void onDataPointDetailsLoaded(int requestId,	AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords,	Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails);
	
	/**
	 * Data point details successfully loaded, along with their linked counterparts
	 * @param requestId The id of this load request
	 * @param dataSetIds The ids of the data sets from which each point is loaded (correspond to the entries in dataPointSummaries and dataPointDetails)
	 * @param dataSetTypes The types of the data sets from which each point is loaded (correspond to the entries in dataPointSummaries and dataPointDetails) 
	 * @param zCoord The y-coordinate of the data point
	 * @param yCoord The z-coordinate of the data point
	 * @param dataPointSummaries The data summaries for this data point and its linked data points
	 * @param dataPointDetails The data details for this data point and its linked data points
	 */
	public void onDataPointDetailsWithLinkedDPsLoaded(int requestId, Vector<AbstractDataSetUniqueId> dataSetIds, Vector<DataSetType> dataSetTypes, int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails);
	
	/**
	 * Data point details successfully cleared
	 * @param dataSetId The id of the data set
	 * @param zCoord The y-coordinate of the data point
	 * @param yCoord The z-coordinate of the data point
	 */
	public void onDataPointDetailsCleared(AbstractDataSetUniqueId dataSetId, int yCoord, int zCoord);
	
	/**
	 * Configuration data successfully set
	 * @param dataSetId The id of the data set
	 */
	public void onConfigDataSet(AbstractDataSetUniqueId dataSetId);

	/**
	 * Data set closed successfully
	 * @param dataSetId The id of the data set that was closed
	 */
	public void onDataSetClosed(AbstractDataSetUniqueId dataSetId);

	/**
	 * Velocity component correlations calculated successfully
	 * @param dataSetId The id of the data set that was used
	 * @param yCoords The y-coordinates of the data points for which correlations have been calculated
	 * @param zCoords The y-coordinates of the data points for which correlations have been calculated
	 * @param uCorrelations The calculated correlations between the u velocities of the first data point and each of the subsequent data points (will have length yCoords.length - 1 == zCoords.length -1)
	 * @param vCorrelations The calculated correlations between the v velocities of the first data point and each of the subsequent data points (will have length yCoords.length - 1 == zCoords.length -1)
	 * @param wCorrelations The calculated correlations between the w velocities of the first data point and each of the subsequent data points (will have length yCoords.length - 1 == zCoords.length -1)
	 */
	public void onCorrelationsCalculated(AbstractDataSetUniqueId dataSetId, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Double> uCorrelations, Vector<Double> vCorrelations, Vector<Double> wCorrelations);

	/**
	 * Velocity component pseudo-correlations calculated successfully
	 * @param dataSetId The id of the data set that was used
	 * @param yCoords The y-coordinates of the data points for which correlations have been calculated
	 * @param zCoords The y-coordinates of the data points for which correlations have been calculated
	 * @param uCorrelations The calculated correlations between the u velocities of the first data point and each of the subsequent data points (will have length yCoords.length - 1 == zCoords.length -1)
	 * @param vCorrelations The calculated correlations between the v velocities of the first data point and each of the subsequent data points (will have length yCoords.length - 1 == zCoords.length -1)
	 * @param wCorrelations The calculated correlations between the w velocities of the first data point and each of the subsequent data points (will have length yCoords.length - 1 == zCoords.length -1)
	 */
	public void onPseudoCorrelationsCalculated(AbstractDataSetUniqueId dataSetId, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Double> uCorrelations, Vector<Double> vCorrelations, Vector<Double> wCorrelations);

	/**
	 * Data points removed correctly
	 * @param dataSetId The id of the data set that was used
	 * @param yCoords The y-coordinates of the removed data points. This may contain null values if not all data points requested for removal were removed.
	 * @param zCoords The z-coordinates of the removed data points. This may contain null values if not all data points requested for removal were removed.
	 */
	public void onDataPointsRemoved(AbstractDataSetUniqueId dataSetId, Vector<Integer> yCoords, Vector<Integer> zCoords);

	/**
	 * Data point summary field calculated correctly
	 * @param dataSetId The id of the data set that was used
	 * @param calculatedDPSFields The summary fields which have been calculated
	 */
	public void onDataPointSummaryFieldCalculated(AbstractDataSetUniqueId dataSetId, Vector<DataPointSummaryIndex> calculatedDPSFields);

	/**
	 * Data point data has been recalculated for a data point
	 * @param dataSetId The id of the data set that was used
	 * @param progress The progress through the set of data points for which the recalculation is being performed (percentage, 0-100)
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 */
	public void onDataPointDataRecalculated(AbstractDataSetUniqueId dataSetId, int progress, int yCoord, int zCoord);

	/**
	 * Rotation correction batch created successfully
	 * @param dataSetId The id of the data set that was used
	 */
	public void onRotationCorrectionBatchCreated(AbstractDataSetUniqueId dataSetId);

	/**
	 * Rotation correction batches successfully created from a file
	 * @param dataSetId The id of the data set that was used
	 */
	public void onRotationCorrectionBatchesCreatedFromFile(AbstractDataSetUniqueId dataSetId);
	
	/**
	 * Summary data has been recalculated
	 * @param dataSetId The id of the data set that was used
	 */
	public void onSummaryDataRecalculated(AbstractDataSetUniqueId dataSetId);

	/**
	 * A data point has been trimmed
	 * @param uniqueId
	 * @param dataPointDetails
	 */
	public void onDataPointTrimmed(AbstractDataSetUniqueId uniqueId, DataPointSummary dataPointSummary, DataPointDetail dataPointDetails);
}
