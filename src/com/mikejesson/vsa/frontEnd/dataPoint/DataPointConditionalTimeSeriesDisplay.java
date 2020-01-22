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
public class DataPointConditionalTimeSeriesDisplay extends AbstractDataPointConditionalSeriesDisplay {
	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries (should match the order of velocitiesSets)
	 * @param controlVelocitiesSets The set of velocities to be used for the condition
	 * @param valuesSets The values to be displayed when the condition is met (should match controlVelocitiesSets in size and order)
	 * @throws Exception 
	 * 
	 */
	public DataPointConditionalTimeSeriesDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Vector<Double>> controlVelocitiesSets, Vector<Vector<Double>> valuesSets, boolean includeMinima, int measurementFrequency, int conditionalSeriesLengthInSeconds, boolean useLowPassFilter) throws Exception {
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
	public DataPointConditionalTimeSeriesDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Vector<Double>> controlVelocitiesSets, Vector<Vector<Double>> valuesSets1, Vector<Vector<Double>> valuesSets2, ValuesCombinationMethod vcm, boolean includeMinima, int measurementFrequency, int conditionalSeriesLengthInSeconds, boolean useLowPassFilter) throws Exception {
		super(dataSetId, title, yCoords, zCoords, controlVelocitiesSets, valuesSets1, valuesSets2, vcm, includeMinima, measurementFrequency, conditionalSeriesLengthInSeconds, useLowPassFilter);
	}

	@Override
	protected void calculateConditionalSeries(Vector<Double[][]> data, Hashtable<Integer, Ensemble> ensembles, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Integer> centralIndices, Vector<Double> values1, Vector<Double> values2, int conditionalSeriesLength, int measurementFrequency) {
		if (mUseLowPassFilter) {
			calculateConditionalSeriesFiltered(data, ensembles, yCoords, zCoords, centralIndices, values1, values2, conditionalSeriesLength, measurementFrequency);
		} else {
			calculateConditionalSeriesUnfiltered(data, ensembles, yCoords, zCoords, centralIndices, values1, values2, conditionalSeriesLength, measurementFrequency);
		}
	}
	
	protected void calculateConditionalSeriesUnfiltered(Vector<Double[][]> data, Hashtable<Integer, Ensemble> ensembles, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Integer> centralIndices, Vector<Double> values1, Vector<Double> values2, int conditionalSeriesLength, int measurementFrequency) {
		int numberOfIndices = centralIndices.size();
		int numberOfValues = values1.size();
		Double[][] ensembleSeries = new Double[2 * conditionalSeriesLength - 1][2];
		double mean1 = MAJFCMaths.mean(values1);
		double mean2 = 0;
		double stDev1 = MAJFCMaths.standardDeviation(values1);
		double stDev2 = 0;
		
		if (values2 != null) {
			mean2 = MAJFCMaths.mean(values2);
			stDev2 = MAJFCMaths.standardDeviation(values2);
		}
		
		// For each of the values in the set of conditional series centred on the centralIndices, calculate the ensemble average
		for (int relativeValueIndex = (-conditionalSeriesLength) + 1, ensembleEntryIndex = 0; relativeValueIndex < conditionalSeriesLength; ++relativeValueIndex, ++ensembleEntryIndex) {
			double ensembleEntrySum = 0d;
			int ensembleAverageDivisor = 0;
			
			for (int conditionalSeriesIndex = 0; conditionalSeriesIndex < numberOfIndices; ++conditionalSeriesIndex)
			{
				int centralIndex = centralIndices.elementAt(conditionalSeriesIndex);
				int valueIndex = centralIndex + relativeValueIndex;
				Ensemble ensemble = ensembles.get(centralIndex);
				if (ensemble == null) {
					ensemble = new Ensemble(centralIndex);
					ensembles.put(centralIndex, ensemble);
				}
				
				if (valueIndex >= 0 && valueIndex < numberOfValues) {
					if (values2 == null) {
						double value1 = (values1.elementAt(valueIndex) - mean1)/stDev1;
						ensemble.add(value1);
						ensembleEntrySum += value1;
					} else {
						double value1 = (values1.elementAt(valueIndex) - mean1);///stDev1;
						double value2 = (values2.elementAt(valueIndex) - mean2);///stDev2;
						Double combinedValue = getValueCombinationValue(value1, value2);
						
						if (combinedValue.equals(Double.NaN)) {
							continue;
						}
						
						ensemble.add(combinedValue);
						ensembleEntrySum += combinedValue;
					}

					++ensembleAverageDivisor;
				}
			}
			
			ensembleSeries[ensembleEntryIndex][TIME_INDEX] = relativeValueIndex/((double) measurementFrequency);
			ensembleSeries[ensembleEntryIndex][VALUE_INDEX] = ensembleEntrySum/ensembleAverageDivisor;
		}
		
//		Vector<Double> ensembleValues = new Vector<Double>(ensembleSeries.length);
//		for (int i = 0; i < ensembleSeries.length; ++i) {
//			ensembleValues.add(ensembleSeries[i][VALUE_INDEX]);
//		}
//		
//		Vector<Double> filteredEnsembleValues = MAJFCMaths.lowPassFilter(ensembleValues, 2, 1d/(double) measurementFrequency);
//		
//		// Re-write the filtered values to the array
//		for (int i = 0; i < ensembleSeries.length; ++i) {
//			ensembleSeries[i][VALUE_INDEX] = filteredEnsembleValues.get(i);
//		}

		data.add(ensembleSeries);
		
		int numberOfGaps = numberOfIndices - 1;
		Vector<Double> gaps = new Vector<Double>(numberOfGaps > 0 ? numberOfGaps : 0);
		
		// Return the mean time between central indices
		for (int i = 1; i < numberOfIndices; ++i) {
			double gap = (double) (centralIndices.get(i) - centralIndices.get(i - 1))/measurementFrequency;
			
			if (gap > 1) {
				gaps.add(gap);
			}
		}

		double mean = MAJFCMaths.mean(gaps);
		double stdev = MAJFCMaths.standardDeviation(gaps);
		System.out.println(mean + " " + stdev);
	}
	
	protected void calculateConditionalSeriesFiltered(Vector<Double[][]> data, Hashtable<Integer, Ensemble> ensembles, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Integer> centralIndices, Vector<Double> values1, Vector<Double> values2, int conditionalSeriesLength, int measurementFrequency) {
		int numberOfEnsembles = centralIndices.size();
		int numberOfValues = values1.size();
		Double[][] ensembleSeries = new Double[2 * conditionalSeriesLength - 1][2];
		double mean1 = MAJFCMaths.mean(values1);
		double mean2 = 0;
		double stDev1 = MAJFCMaths.standardDeviation(values1);
		double stDev2 = 0;
		
		if (values2 != null) {
			mean2 = MAJFCMaths.mean(values2);
			stDev2 = MAJFCMaths.standardDeviation(values2);
		}
		
		Vector<Vector<Double>> ensembleList = new Vector<Vector<Double>>(numberOfEnsembles);
		
		// Get the ensemble series and low-pass filter them
		for (int conditionalSeriesIndex = 0; conditionalSeriesIndex < numberOfEnsembles; ++conditionalSeriesIndex) {
			int centralIndex = centralIndices.elementAt(conditionalSeriesIndex);
			Ensemble ensemble = new Ensemble(centralIndex);
			ensembles.put(centralIndex, ensemble);
			Vector<Double> ensembleVector = new Vector<Double>(conditionalSeriesLength);
			ensembleList.add(ensembleVector);
			
			for (int relativeValueIndex = (-conditionalSeriesLength) + 1, ensembleEntryIndex = 0; relativeValueIndex < conditionalSeriesLength; ++relativeValueIndex, ++ensembleEntryIndex) {
				int valueIndex = centralIndex + relativeValueIndex;
				Double ensembleValue = Double.NaN;
				
				if (valueIndex >= 0 && valueIndex < numberOfValues) {
					if (values2 == null) {
						ensembleValue = (values1.elementAt(valueIndex) - mean1)/stDev1;
					} else {
						double value1 = (values1.elementAt(valueIndex) - mean1);///stDev1;
						double value2 = (values2.elementAt(valueIndex) - mean2);///stDev2;
						ensembleValue = getValueCombinationValue(value1, value2);
					}
				}
				
				ensemble.add(ensembleValue);
				ensembleVector.add(ensembleValue);
			}
			
			ensembleVector = MAJFCMaths.lowPassFilter(ensembleVector, 1, 1d/((double)measurementFrequency));
		}
		
		// For each of the values in the set of conditional series centred on the centralIndices, calculate the ensemble average
		for (int ensembleEntryIndex = 0; ensembleEntryIndex < ensembleSeries.length; ++ensembleEntryIndex) {
			double ensembleEntrySum = 0d;
			int ensembleAverageDivisor = 0;
			
			for (int ensembleIndex = 0; ensembleIndex < numberOfEnsembles; ++ensembleIndex) {
				Double value = ensembleList.elementAt(ensembleIndex).get(ensembleEntryIndex);
				
				if (value.equals(Double.NaN) == false) {
					ensembleEntrySum += value;
					++ensembleAverageDivisor;
				}
			}
			
			ensembleSeries[ensembleEntryIndex][TIME_INDEX] = ensembleEntryIndex/((double) measurementFrequency);
			ensembleSeries[ensembleEntryIndex][VALUE_INDEX] = ensembleEntrySum/ensembleAverageDivisor;
		}

		data.add(ensembleSeries);
		
		int numberOfGaps = numberOfEnsembles - 1;
		Vector<Double> gaps = new Vector<Double>(numberOfGaps > 0 ? numberOfGaps : 0);
		
		// Return the mean time between central indices
		for (int i = 1; i < numberOfEnsembles; ++i) {
			double gap = (double) (centralIndices.get(i) - centralIndices.get(i - 1))/measurementFrequency;
			
			if (gap > 1) {
				gaps.add(gap);
			}
		}

		double mean = MAJFCMaths.mean(gaps);
		double stdev = MAJFCMaths.standardDeviation(gaps);
		System.out.println(mean + " " + stdev);
	}
}
