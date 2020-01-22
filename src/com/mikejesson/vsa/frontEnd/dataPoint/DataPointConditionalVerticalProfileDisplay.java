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

package com.mikejesson.vsa.frontEnd.dataPoint;


import java.util.Hashtable;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;


/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointConditionalVerticalProfileDisplay extends AbstractDataPointConditionalSeriesDisplay {
	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries (should match the order of velocitiesSets)
	 * @param controlVelocitiesSets The set of velocities to be used for the condition
	 * @param valuesSets The values to be displayed when the condition is met (should match controlVelocitiesSets in size and order)
	 * @throws Exception 
	 * 
	 */
	public DataPointConditionalVerticalProfileDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Vector<Double>> controlVelocitiesSets, Vector<Vector<Double>> valuesSets, boolean includeMinima, int measurementFrequency, int conditionalSeriesLengthInSeconds, boolean useLowPassFilter) throws Exception {
		this(dataSetId, title, yCoords, zCoords, controlVelocitiesSets, valuesSets, null, MULTIPLY, includeMinima, measurementFrequency, conditionalSeriesLengthInSeconds, useLowPassFilter);
	}
	
	/**
	 * Constructs the conditional time series display (displays the values when the condition is met)
	 * If two sets of values are provided, the product of corresponding elements is shown
	 * 
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries (should match the order of velocitiesSets)
	 * @param controlVelocitiesSets The set of velocities to be used for the condition
	 * @param valuesSets1 The first set of values to be displayed when the condition is met (should match controlVelocitiesSets in size and order). Must not be null.
	 * @param valuesSets2 The second set of values to be displayed when the condition is met (should match controlVelocitiesSets in size and order). Can be null.
	 * @throws Exception 
	 * 
	 */
	public DataPointConditionalVerticalProfileDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Vector<Double>> controlVelocitiesSets, Vector<Vector<Double>> valuesSets1, Vector<Vector<Double>> valuesSets2, ValuesCombinationMethod vcm, boolean includeMinima, int measurementFrequency, int conditionalSeriesLengthInSeconds, boolean useLowPassFilter) throws Exception {
		super(dataSetId, title, yCoords, zCoords, controlVelocitiesSets, valuesSets1, valuesSets2, vcm, includeMinima, measurementFrequency, conditionalSeriesLengthInSeconds, useLowPassFilter);
	}
	
	@Override
	/**
	 * 
	 * @param data
	 * @param centralIndices
	 * @param values1
	 * @param values2
	 * @param conditionalSeriesLength
	 * @param measurementFrequency
	 */
	protected void calculateConditionalSeries(Vector<Double[][]> data, Hashtable<Integer, Ensemble> ensembles, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Integer> centralIndices, Vector<Double> values1, Vector<Double> values2, int conditionalSeriesLength, int measurementFrequency) {
		int numberOfIndices = centralIndices.size();
		int numberOfValues = values1.size();
		Double[][] conditionalSeries = new Double[2 * conditionalSeriesLength - 1][2];
		double mean1 = MAJFCMaths.mean(values1);
		double mean2 = 0;
		
		if (values2 != null) {
			mean2 = MAJFCMaths.mean(values2);
		}
		
		for (int conditionalSeriesIndex = 0; conditionalSeriesIndex < numberOfIndices; ++conditionalSeriesIndex) {
			int yCoord = yCoords.elementAt(conditionalSeriesIndex);
//			Hashtable<Integer, Vector<Integer>> yIndexedCoordLookup = DAFrame.getBackEndAPI().getYCoordIndexedSortedDataPointCoordinates(mDataSetId);
//			Vector<Integer> zCoordsForThisYCoord = yIndexedCoordLookup.get(yCoord);
//			int numberOfZCoordsForThisYCoord = zCoordsForThisYCoord.size();
			
//			for (int zCoordsIndex = 0; zCoordsIndex < numberOfZCoordsForThisYCoord; ++zCoordsIndex) {
//				double value1 = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, yCoord, zCoordsForThisYCoord.elementAt(zCoordsIndex), BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
//				;
//				}
//			}
				
//			conditionalSeries[ensembleEntryIndex][TIME_INDEX] = relativeValueIndex/((double) measurementFrequency);
//			conditionalSeries[ensembleEntryIndex][VALUE_INDEX] = ensembleEntrySum/ensembleAverageDivisor;
		}
		
//		data.add(ensembleSeries);
	}
}
