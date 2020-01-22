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

import com.mikejesson.majfc.helpers.MAJFCLogger;
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
public class BackEndCallBackAdapter implements BackEndCallbackInterface {
	@Override
	public void onConfigDataSet(AbstractDataSetUniqueId dataSetId) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onConfigDataSet");
	}

	@Override
	public void onDataPointAdded(AbstractDataSetUniqueId dataSetId, int yCoord, int zCoord) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointAdded");
	}

	@Override
	public void onDataPointDetailsCleared(AbstractDataSetUniqueId dataSetId, int yCoord, int zCoord) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointDetailsCleared");
	}

	@Override
	public void onDataPointDetailsLoaded(int requestId, AbstractDataSetUniqueId dataSetId,	Vector<Integer> yCoords, Vector<Integer> zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointDetailsLoaded");
	}
	
	@Override
	public void onDataPointDetailsWithLinkedDPsLoaded(int requestId, Vector<AbstractDataSetUniqueId> dataSetIds, Vector<DataSetType> dataSetTypes, int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries,	Vector<DataPointDetail> dataPointDetails) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointDetailsWithLinkedDPsLoaded");
	}
	
	@Override
	public void onDataSetClosed(AbstractDataSetUniqueId dataSetId) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataSetClosed");
	}

	@Override
	public void onDataSetOpened(AbstractDataSetUniqueId dataSetId, File dataSetFile, DataSetConfig configData) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataSetOpened");
	}

	@Override
	public void onDataSetSaved(AbstractDataSetUniqueId dataSetId, AbstractDataSetUniqueId oldUniqueId, File dataSetFile) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataSetSaved");
	}

	@Override
	public void onNewDataSetCreated(AbstractDataSetUniqueId dataSetId, File dataSetFile) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onNewDataSetCreated");
	}

	@Override
	public void onCorrelationsCalculated(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Double> uCorrelations, Vector<Double> vCorrelations, Vector<Double> wCorrelations) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onUCorrelationsCalculated");
	}

	@Override
	public void onPseudoCorrelationsCalculated(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Double> uCorrelations, Vector<Double> vCorrelations, Vector<Double> wCorrelations) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onUCorrelationsCalculated");
	}

	@Override
	public void onDataPointsRemoved(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointsRemoved");
	}
	
	@Override
	public void onDataPointSummaryFieldCalculated(AbstractDataSetUniqueId dataSetId, Vector<DataPointSummaryIndex> calculatedDPSFields) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointSummaryFieldCalculated");
	}

	@Override
	public void onDataPointDataRecalculated(AbstractDataSetUniqueId dataSetId, int progress, int yCoord, int zCoord) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointDataRecalculated");
	}

	@Override
	public void onRotationCorrectionBatchCreated(AbstractDataSetUniqueId dataSetId)	{
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onRotationCorrectionBatchCreated");
	}

	@Override
	public void onRotationCorrectionBatchesCreatedFromFile(AbstractDataSetUniqueId dataSetId) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onRotationCorrectionBatchesCreatedFromFile");
	}
	
	@Override
	public void onSummaryDataRecalculated(AbstractDataSetUniqueId dataSetId){
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onSummaryDataRecalculated");
	}

	@Override
	public void onDataPointTrimmed(AbstractDataSetUniqueId uniqueId, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail) {
		MAJFCLogger.log("Unimplemented BackEndCallbackInterface callback method onDataPointTrimmed");
	}
}
