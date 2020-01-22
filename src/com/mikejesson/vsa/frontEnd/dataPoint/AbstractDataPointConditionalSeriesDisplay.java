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



import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCIndex;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ExportableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.XYDataSetAdapter;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractDataPointConditionalSeriesDisplay extends MAJFCStackedPanelWithFrame {
	protected final AbstractDataSetUniqueId mDataSetId;
	private ExportableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private Vector<Double[][]> mData;
	private Vector<Integer> mYCoords, mZCoords;
	private Vector<String> mLegendEntries;
	private MyDataSet mDataSet;
	protected final ValuesCombinationMethod mValuesCombinationMethod;
	private boolean mSplitByAccelOrDecel;
	protected boolean mUseLowPassFilter;
	private Double mFullSeriesCorrelation;
	private Double mEnsembleCorrelation;
	private Double mRandomEnsembleCorrelation;
	private Vector<Double> mMeanRandomEnsembleCorrelationByVelocitySet;
	private Vector<Double> mMeanTrueEnsembleCorrelationByVelocitySet;
	private Vector<Double> mStDevOfEnsembleCorrelationByVelocitySet;
	private Vector<Hashtable<Integer, Ensemble>> ensemblesByVelocitySet;
	
	protected static final int TIME_INDEX = 0;
	protected static final int VALUE_INDEX = 1;
	protected final int MEASUREMENT_FREQUENCY;
	protected final int CONDITIONAL_SERIES_LENGTH_IN_SECONDS; // window around peak (seconds)
	protected final int CONDITIONAL_SERIES_LENGTH;// = CONDITIONAL_SERIES_LENGTH_IN_SECONDS * MEASUREMENT_FREQUENCY;

	private int NUMBER_OF_ENSEMBLE_SETS = 0;
	private final int PEAK_ACCEL_ENSEMBLE_SET_INDEX = NUMBER_OF_ENSEMBLE_SETS++;
	private final int TROUGH_ACCEL_ENSEMBLE_SET_INDEX = NUMBER_OF_ENSEMBLE_SETS++;
	private final int PEAK_DECEL_ENSEMBLE_SET_INDEX = NUMBER_OF_ENSEMBLE_SETS++;
	private final int TROUGH_DECEL_ENSEMBLE_SET_INDEX = NUMBER_OF_ENSEMBLE_SETS++;

	private final String[] CATEGORY_LABELS = new String[] { " peak accel", " trough accel", " peak decel", " trough decel"};
	
	public static final ValuesCombinationMethod MULTIPLY = new ValuesCombinationMethod();
	public static final ValuesCombinationMethod MULTIPLY_WITH_QH_Q1 = new ValuesCombinationMethod();
	public static final ValuesCombinationMethod MULTIPLY_WITH_QH_Q2 = new ValuesCombinationMethod();
	public static final ValuesCombinationMethod MULTIPLY_WITH_QH_Q3 = new ValuesCombinationMethod();
	public static final ValuesCombinationMethod MULTIPLY_WITH_QH_Q4 = new ValuesCombinationMethod();
	
	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries (should match the order of velocitiesSets)
	 * @param controlVelocitiesSets The set of velocities to be used for the condition
	 * @param valuesSets The values to be displayed when the condition is met (should match controlVelocitiesSets in size and order)
	 * @throws Exception 
	 * 
	 */
	public AbstractDataPointConditionalSeriesDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Vector<Double>> controlVelocitiesSets, Vector<Vector<Double>> valuesSets, boolean includeMinima, int measurementFrequency, int conditionalSeriesLengthInSeconds, boolean useLowPassFilter) throws Exception {
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
	public AbstractDataPointConditionalSeriesDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Vector<Double>> controlVelocitiesSets, Vector<Vector<Double>> valuesSets1, Vector<Vector<Double>> valuesSets2, ValuesCombinationMethod vcm, boolean splitByAccelOrDecel, int measurementFrequency, int conditionalSeriesLengthInSeconds, boolean useLowPassFilter) throws Exception {
		super(new GridBagLayout());
		
		mDataSetId = dataSetId;
		mValuesCombinationMethod = vcm;
		mSplitByAccelOrDecel = splitByAccelOrDecel;
		mUseLowPassFilter = useLowPassFilter;
		mYCoords = yCoords;
		mZCoords = zCoords;
		mDataSet = new MyDataSet();
		MEASUREMENT_FREQUENCY = measurementFrequency;
		CONDITIONAL_SERIES_LENGTH_IN_SECONDS = conditionalSeriesLengthInSeconds;
		CONDITIONAL_SERIES_LENGTH = CONDITIONAL_SERIES_LENGTH_IN_SECONDS * MEASUREMENT_FREQUENCY;
		final double PERCENTILE_LIMIT = 98.0;

		int numberOfSets = controlVelocitiesSets.size();
		
		if (yCoords.size() != numberOfSets
				|| zCoords.size() != numberOfSets) {
			throw new Exception("Mismatch on coords");
		}
		
		if (valuesSets1 == null) {
			throw new Exception("null valuesSets1");	
		}
		
		if (valuesSets1.size() != numberOfSets || (valuesSets2 != null && valuesSets2.size() != numberOfSets)) {
			throw new Exception("Mismatch");
		}
		
		mData = new Vector<Double[][]>(numberOfSets);
		mLegendEntries = new Vector<String>(numberOfSets);
		ensemblesByVelocitySet = new Vector<Hashtable<Integer, Ensemble>>(numberOfSets);
		
		for (int setsIndex = 0; setsIndex < numberOfSets; ++setsIndex) {
			mLegendEntries.add(DATools.makeDataPointIdentifier(yCoords.elementAt(setsIndex), zCoords.elementAt(setsIndex)));
			
			// Find control limits
			Vector<Double> controlVelocities = controlVelocitiesSets.elementAt(setsIndex);
//			Vector<Double> controlVelocitiesDerivatives = MAJFCMaths.derivatives(MAJFCMaths.lowPassFilter(controlVelocities, 10, 1d/measurementFrequency), MEASUREMENT_FREQUENCY);
			Vector<Double> controlVelocitiesDerivatives = MAJFCMaths.derivatives(controlVelocities, MEASUREMENT_FREQUENCY);
			final int pdfGranularity = 100;
			LinkedList<Double[]> controlPDF = MAJFCMaths.probabilityDensityFunction(controlVelocities, true, pdfGranularity);
			
			Double integralSum = 0d, minLimit = null, maxLimit = null;
			double conditionLimit = (100d - PERCENTILE_LIMIT)/100d;
			
			for (int pdfIndex = 1; pdfIndex < pdfGranularity; ++pdfIndex) {
				double pdfVariableValue = controlPDF.get(pdfIndex)[MAJFCMaths.PDF_VARIABLE_VALUE_INDEX];
				integralSum += (pdfVariableValue - controlPDF.get(pdfIndex - 1)[MAJFCMaths.PDF_VARIABLE_VALUE_INDEX]) * controlPDF.get(pdfIndex)[MAJFCMaths.PDF_PDF_VALUE_INDEX];
				
				if (minLimit == null && integralSum > conditionLimit) {
					minLimit = pdfVariableValue;
				}
				
				if (integralSum > 1 - conditionLimit) {
					maxLimit = pdfVariableValue;
					break;
				}
			}

			int numberOfValues = controlVelocities.size();

			Vector<Double> values1 = valuesSets1.elementAt(setsIndex);

			if (values1.size() != numberOfValues) {
				throw new Exception("Mismatch - values1 at " + setsIndex);
			}
			
			Vector<Double> values2 = null;
			
			if (valuesSets2 != null) {
				values2 = valuesSets2.elementAt(setsIndex);
				
				if (values2.size() != numberOfValues) {
					throw new Exception("Mismatch - values2 at " + setsIndex);
				}
			}
			
			final int numberOfEnsembleSets = 4;
			int[] lastEnsembleStartIndices = new int[numberOfEnsembleSets];
			
			Vector<Vector<Integer>> ensembleStartIndicesLists = new Vector<Vector<Integer>>(4);
			for (int i = 0; i < numberOfEnsembleSets; ++i) {
				ensembleStartIndicesLists.add(new Vector<Integer>(20));
				lastEnsembleStartIndices[i] = Integer.MIN_VALUE;
			}
			
			// Find the peaks and troughs for the conditional series
			for (int valueIndex = 0; valueIndex < numberOfValues; ++valueIndex) {
				double controlVelocity = controlVelocities.elementAt(valueIndex);
				double controlVelocityDerivative = controlVelocitiesDerivatives.elementAt(valueIndex);
				int ensembleSetIndex = -1;
				
				if (controlVelocity < minLimit) {
					if (mSplitByAccelOrDecel == false || controlVelocityDerivative < 0) {
						ensembleSetIndex = TROUGH_DECEL_ENSEMBLE_SET_INDEX;
					} else {
						ensembleSetIndex = TROUGH_ACCEL_ENSEMBLE_SET_INDEX;
					}
				} else if (controlVelocity > maxLimit) {
					if (mSplitByAccelOrDecel == false || controlVelocityDerivative > 0) {
						ensembleSetIndex = PEAK_ACCEL_ENSEMBLE_SET_INDEX;
					} else {
						ensembleSetIndex = PEAK_DECEL_ENSEMBLE_SET_INDEX;
					}
				}
				
				if (ensembleSetIndex != -1) {
//					if (valueIndex < (lastEnsembleStartIndices[ensembleSetIndex] + (100d/(100d - PERCENTILE_LIMIT)))) {
//						continue;
//					}
					
					ensembleStartIndicesLists.elementAt(ensembleSetIndex).add(valueIndex);
					lastEnsembleStartIndices[ensembleSetIndex] = valueIndex;
				}
			}
			
			Hashtable<Integer, Ensemble> ensemblesForThisVelocitySet = new Hashtable<Integer, Ensemble>(100);
			
			for (int categoryIndex = 0; categoryIndex < NUMBER_OF_ENSEMBLE_SETS; ++categoryIndex) {
				calculateConditionalSeries(mData, ensemblesForThisVelocitySet, mYCoords, mZCoords, ensembleStartIndicesLists.elementAt(categoryIndex), values1, values2, CONDITIONAL_SERIES_LENGTH, MEASUREMENT_FREQUENCY);
			}
			
			ensemblesByVelocitySet.add(ensemblesForThisVelocitySet);
		}

		// If are two series, calculate the correlation between the full series and ensemble series
		if (numberOfSets == 2) {
			calculateCorrelations(valuesSets1.elementAt(0), valuesSets1.elementAt(1), ensemblesByVelocitySet);
		} else {
			mFullSeriesCorrelation = null;
			mEnsembleCorrelation = null;
			mRandomEnsembleCorrelation = null;
			mMeanRandomEnsembleCorrelationByVelocitySet = null;
			mMeanTrueEnsembleCorrelationByVelocitySet = null;
			mStDevOfEnsembleCorrelationByVelocitySet = null;
		}
		
		ensemblesByVelocitySet.clear();
		
		buildGUI();
	}

	private void calculateCorrelations(Vector<Double> values1, Vector<Double> values2, Vector<Hashtable<Integer, Ensemble>> ensemblesByVelocitySet) {
		// The ensemble series correlation is normalised by the full series standard deviations, rather than
		// being a true correlation
		double stDev1 = MAJFCMaths.standardDeviation(values1);
		double stDev2 = MAJFCMaths.standardDeviation(values2);
		mFullSeriesCorrelation = MAJFCMaths.correlation(values1, values2);
		
		mEnsembleCorrelation = calculateCorrelationFromData(mData, stDev1, stDev2);
		
		Hashtable<Integer, Ensemble> ensembles1 = ensemblesByVelocitySet.elementAt(0);
		Hashtable<Integer, Ensemble> ensembles2 = ensemblesByVelocitySet.elementAt(1);
		
		mRandomEnsembleCorrelation = calculateRandomCorrelationBetweenVelocitySets(values1, values2, ensembles1.size());
		
		mMeanRandomEnsembleCorrelationByVelocitySet = new Vector<Double>(2);
//		mMeanRandomEnsembleCorrelationByVelocitySet.add(calculateRandomEnsembleMeanCorrelationsForVelocitySet(values1, ensembles1.size()));
//		mMeanRandomEnsembleCorrelationByVelocitySet.add(calculateRandomEnsembleMeanCorrelationsForVelocitySet(values2, ensembles2.size()));
		mMeanRandomEnsembleCorrelationByVelocitySet.add(1d);
		mMeanRandomEnsembleCorrelationByVelocitySet.add(2d);
		
		mMeanTrueEnsembleCorrelationByVelocitySet = new Vector<Double>(2);
//		mMeanTrueEnsembleCorrelationByVelocitySet.add(calculateTrueEnsembleMeanCorrelationForVelocitySet(ensembles1, stDev1));
//		mMeanTrueEnsembleCorrelationByVelocitySet.add(calculateTrueEnsembleMeanCorrelationForVelocitySet(ensembles2, stDev2));
		mMeanTrueEnsembleCorrelationByVelocitySet.add(1d);
		mMeanTrueEnsembleCorrelationByVelocitySet.add(2d);
		
		mStDevOfEnsembleCorrelationByVelocitySet = new Vector<Double>(2);
	
	}

	private double calculateRandomCorrelationBetweenVelocitySets(Vector<Double> values1, Vector<Double> values2, int numberOfTrueEnsembles) {
		final int numberOfValues = values1.size();
		Vector<Integer> randomCentralIndices = selectRandomCentralIndices(numberOfTrueEnsembles, numberOfValues);
		
		Hashtable<Integer, Ensemble> ensembles = new Hashtable<Integer, Ensemble>(100);
		
		Vector<Double[][]> randomData = new Vector<Double[][]>(2);
		calculateConditionalSeries(randomData, ensembles, null, null, randomCentralIndices, values1, null, CONDITIONAL_SERIES_LENGTH, MEASUREMENT_FREQUENCY);
		calculateConditionalSeries(randomData, ensembles, null, null, randomCentralIndices, values2, null, CONDITIONAL_SERIES_LENGTH, MEASUREMENT_FREQUENCY);
		
		return calculateCorrelationFromData(randomData, MAJFCMaths.standardDeviation(values1), MAJFCMaths.standardDeviation(values2));
	}
	
	private Vector<Integer> selectRandomCentralIndices(int numberToSelect, int numberOfValuesInVelocitySet) {
		Vector<Integer> randomCentralIndices = new Vector<Integer>(numberToSelect);
		
		// Randomly select central indices
		for (int i = 0; i < numberToSelect; ++i) {
			randomCentralIndices.add((int) (((double) numberOfValuesInVelocitySet) * Math.random()));
		}

		return randomCentralIndices;
	}

	/**
	 * 
	 * @param data The ensemble velocity measurements held in an array
	 * @param stDev1 The standard deviation of the full data set 1
	 * @param stDev2 The standard deviation of the full data set 2
	 * @return The correlation of the ensembles whose values are in data, but scaled by the full set standard deviation rather
	 * than the ensemble standard deviations
	 */
	private double calculateCorrelationFromData(Vector<Double[][]> data, double stDev1, double stDev2) {
		// All size checks should already have been carried out
		// values1 and values2 should be the same length
		int numberOfVelocities = data.elementAt(0).length;
		Vector<Double> ensemble1 = new Vector<Double>(numberOfVelocities);
		Vector<Double> ensemble2 = new Vector<Double>(numberOfVelocities);

		for (int i = 0; i < numberOfVelocities; ++i) {
			ensemble1.add(data.elementAt(0)[i][VALUE_INDEX]);
			ensemble2.add(data.elementAt(1)[i][VALUE_INDEX]);
		}
		
		double ensembleStDev1 = MAJFCMaths.standardDeviation(ensemble1);
		double ensembleStDev2 = MAJFCMaths.standardDeviation(ensemble2);
		
		return MAJFCMaths.correlation(ensemble1, ensemble2) * (ensembleStDev1 * ensembleStDev2)/(stDev1 * stDev2);
	}
	
	private double calculateTrueEnsembleMeanCorrelationForVelocitySet(Hashtable<Integer, Ensemble> ensembles, double stDev1) {
		int numberOfEnsembles = ensembles.size();
		Vector<Double> correlations = new Vector<Double>(numberOfEnsembles * (numberOfEnsembles - 1));
		Vector<Ensemble> ensembleList = new Vector<Ensemble>(numberOfEnsembles);
		Enumeration<Ensemble> ensembleEnum = ensembles.elements();
		
		while (ensembleEnum.hasMoreElements()) {
			ensembleList.add(ensembleEnum.nextElement());
		}
		
		for (int i = 0; i < numberOfEnsembles; ++i) {
			Ensemble ensemble1 = ensembleList.elementAt(i);
			double ensembleStDev1 = MAJFCMaths.standardDeviation(ensemble1);
			
			for (int j = 0; j < numberOfEnsembles; ++j) {
				if (i == j) {
					continue;
				}
				
				Ensemble ensemble2 = ensembleList.elementAt(j);
				double ensembleStDev2 = MAJFCMaths.standardDeviation(ensemble2);
				
				correlations.add(MAJFCMaths.correlation(ensemble1, ensemble2) * (ensembleStDev1 * ensembleStDev2)/(stDev1 * stDev1));
			}
		}
		
		return MAJFCMaths.mean(correlations); 
	}
	
	private double calculateRandomEnsembleMeanCorrelationsForVelocitySet(Vector<Double> values1, int numberOfTrueEnsembles) {
		Vector<Double[][]> randomData = new Vector<Double[][]>(2);
		Hashtable<Integer, Ensemble> ensembles = new Hashtable<Integer, Ensemble>(numberOfTrueEnsembles);
		Vector<Integer> randomCentralIndices = selectRandomCentralIndices(numberOfTrueEnsembles, values1.size());
		
		calculateConditionalSeries(randomData, ensembles, null, null,  randomCentralIndices, values1, null, CONDITIONAL_SERIES_LENGTH, MEASUREMENT_FREQUENCY);
		
		return calculateTrueEnsembleMeanCorrelationForVelocitySet(ensembles, MAJFCMaths.standardDeviation(values1));
	}

	/**
	 * 
	 * @param data
	 * @param centralIndices
	 * @param values1
	 * @param values2
	 * @param conditionalSeriesLength
	 * @param measurementFrequency
	 */
	protected abstract void calculateConditionalSeries(Vector<Double[][]> data, Hashtable<Integer, Ensemble> ensembles, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Integer> centralIndices, Vector<Double> values1, Vector<Double> values2, int conditionalSeriesLength, int measurementFrequency);	
	
	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_X_AXIS_LABEL));
		NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_Y_AXIS_LABEL));

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		theRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mDataSet.getSeriesCount(); ++i) {
			theRenderer.setSeriesShapesVisible(i, false);
		}
		
		XYPlot thePlot = new XYPlot(mDataSet, xAxis, yAxis, theRenderer);
		thePlot.setRangeZeroBaselineVisible(true);
		thePlot.setDomainZeroBaselineVisible(true);
		
		mTheChart = new JFreeChart(DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, thePlot, mLegendEntries != null && mLegendEntries.size() > 0);
		
		mChartPanel = new ExportableChartPanel(this, mTheChart);
		
		add(mChartPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

		if (mFullSeriesCorrelation != null) {
			MAJFCPanel correlationsPanel = new MAJFCPanel(new GridBagLayout());
			int x = 0, y = 0;
			correlationsPanel.add(new JLabel(DAStrings.getString(DAStrings.FULL_CORRELATION) + MAJFCTools.formatNumber(mFullSeriesCorrelation, 4, true)), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
			correlationsPanel.add(new JLabel(DAStrings.getString(DAStrings.ENSEMBLE_CORRELATION) + MAJFCTools.formatNumber(mEnsembleCorrelation, 4, true)), MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 3, 0, 0, 0, 0, 0));
			correlationsPanel.add(new JLabel(DAStrings.getString(DAStrings.RANDOM_ENSEMBLE_CORRELATION) + MAJFCTools.formatNumber(mRandomEnsembleCorrelation, 4, true)), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 3, 0, 0, 0, 0, 0));
			
			x = 0;
			correlationsPanel.add(new JLabel(DAStrings.getString(DAStrings.MEAN_TRUE_ENSEMBLE_CORRELATION_SET_1) + MAJFCTools.formatNumber(mMeanTrueEnsembleCorrelationByVelocitySet.elementAt(0), 4, true)), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 3, 0, 0, 0, 0, 0));
			correlationsPanel.add(new JLabel(DAStrings.getString(DAStrings.MEAN_TRUE_ENSEMBLE_CORRELATION_SET_2) + MAJFCTools.formatNumber(mMeanTrueEnsembleCorrelationByVelocitySet.elementAt(1), 4, true)), MAJFCTools.createGridBagConstraint(x++, y--, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 3, 0, 0, 0, 0, 0));

			correlationsPanel.add(new JLabel(DAStrings.getString(DAStrings.MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_1) + MAJFCTools.formatNumber(mMeanRandomEnsembleCorrelationByVelocitySet.elementAt(0), 4, true)), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 3, 0, 0, 0, 0, 0));
			correlationsPanel.add(new JLabel(DAStrings.getString(DAStrings.MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_2) + MAJFCTools.formatNumber(mMeanRandomEnsembleCorrelationByVelocitySet.elementAt(1), 4, true)), MAJFCTools.createGridBagConstraint(x, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 3, 0, 0, 0, 0, 0));

//			mStDevOfEnsembleCorrelationByVelocitySet = new Vector<Double>(2);

			add(correlationsPanel, MAJFCTools.createGridBagConstraint(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 5, 5, 5, 5, 0, 0));
		}
	}

	protected double getValueCombinationValue(double value1, double value2) {
		if (mValuesCombinationMethod.equals(MULTIPLY)
			|| (mValuesCombinationMethod.equals(MULTIPLY_WITH_QH_Q1) && (value1 > 0 && value2 > 0))
			|| (mValuesCombinationMethod.equals(MULTIPLY_WITH_QH_Q2) && (value1 < 0 && value2 > 0))
			|| (mValuesCombinationMethod.equals(MULTIPLY_WITH_QH_Q3) && (value1 < 0 && value2 < 0))
			|| (mValuesCombinationMethod.equals(MULTIPLY_WITH_QH_Q4) && (value1 > 0 && value2 < 0))) {
			return value1 * value2;
		}
		
		return Double.NaN;
	}
	
	private class MyDataSet extends XYDataSetAdapter {
		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mData.elementAt(series).length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mData.size();
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getXValue(int series, int item) {
			try {
			return mData.elementAt(series)[item][TIME_INDEX];
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
			return 0d;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			return mData.elementAt(series)[item][VALUE_INDEX];
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return mLegendEntries.elementAt(series/4) + CATEGORY_LABELS[series % 4];
		}
	}
	
	protected class Ensemble extends Vector<Double> {
		private final int mIndex;
		
		Ensemble(int index, int size) {
			super(size);
			mIndex = index;
		}

		Ensemble(int index) {
			mIndex = index;
		}
		
		public int getIndex() {
			return mIndex;
		}
	}
	
	protected static class ValuesCombinationMethod extends MAJFCIndex {
		private static int sIndex = 0;
		
		private ValuesCombinationMethod() {
			super(sIndex++);
		}
	}
}
