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

import java.text.NumberFormat;

import java.util.Vector;

import javax.swing.BorderFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CoordinateAxisIdentifer;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ExportableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.XYDataSetAdapter;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointQuadrantHoleDisplay extends MAJFCStackedPanelWithFrame {
	private MAJFCTabbedPanel mTabbedPanel;

	private ExportableChartPanel mQHChartPanel;
	private JFreeChart mTheQHChart;
	private double[][][] mQHData;
	private Vector<String> mQHLegendEntries;
	private QHDataSet mQHDataSet;
	
	private ExportableChartPanel mProportionAtHoleSizeChartPanel;
	private JFreeChart mTheProportionAtHoleSizeChart;
	private double[][][] mProportionAtHoleSizeData;
	private Vector<String> mProportionAtHoleSizeLegendEntries;
	private ProportionAtHoleSizeDataSet mProportionAtHoleSizeDataSet;
	
	private ExportableChartPanel mDurationAtHoleSizeChartPanel;
	private JFreeChart mTheDurationAtHoleSizeChart;
	private double[][][] mDurationAtHoleSizeData;
	private Vector<String> mDurationLegendEntries;
	private DurationAtHoleSizeDataSet mDurationAtHoleSizeDataSet;
	
	private static final int HOLE_SIZE_INDEX = 0;
	private static final int SHEAR_STRESS_INDEX = 1;
	private static final int PROPORTION_AT_HOLE_SIZE_INDEX = 1;
	private static final int DURATION_AT_HOLE_SIZE_INDEX = 1;
		
	private static final int NUMBER_OF_QUADRANTS = 4;
	
	private static final int QUADRANT_1 = 0;
	private static final int QUADRANT_2 = 1;
	private static final int QUADRANT_3 = 2;
	private static final int QUADRANT_4 = 3;
	
	private static final int UPPER_HOLE_SIZE_VALUE = 20;
	private static final int LOWER_HOLE_SIZE_VALUE = 0;
	private static final int NUMBER_OF_HOLE_SIZES = (UPPER_HOLE_SIZE_VALUE - LOWER_HOLE_SIZE_VALUE) + 1;

	/**
	 * @param title The title (column header) for this display
	 * @param uVelocitiesSets The u-velocity measurements for the data point
	 * @param wOrVVelocitiesSets The w-velocity measurements for the data point
	 * 
	 */
	public DataPointQuadrantHoleDisplay(AbstractDataSetUniqueId dataSet, CoordinateAxisIdentifer secondAxis, MAJFCStackedPanelWithFrame parent, String title, Vector<String> legendEntries, Vector<Vector<Double>> uVelocitiesSets, Vector<Vector<Double>> wOrVVelocitiesSets, boolean normalise) {
		super(parent, new GridBagLayout());
		
		mQHLegendEntries = legendEntries;
		mQHDataSet = new QHDataSet();
		mProportionAtHoleSizeDataSet = new ProportionAtHoleSizeDataSet();
		mDurationAtHoleSizeDataSet = new DurationAtHoleSizeDataSet();
		
		int numberOfVelocitiesSets = Math.min(uVelocitiesSets.size(), wOrVVelocitiesSets.size());
		
		mQHData = new double[numberOfVelocitiesSets][NUMBER_OF_HOLE_SIZES * NUMBER_OF_QUADRANTS][2];
		mProportionAtHoleSizeData = new double[numberOfVelocitiesSets * NUMBER_OF_QUADRANTS][NUMBER_OF_HOLE_SIZES][2];
		mDurationAtHoleSizeData = new double[numberOfVelocitiesSets * NUMBER_OF_QUADRANTS][NUMBER_OF_HOLE_SIZES][2];

		double maxShearStressSum = 0;
		double totalShearStress = 0;

		for (int velocitiesSetsIndex = 0; velocitiesSetsIndex < numberOfVelocitiesSets; ++velocitiesSetsIndex) {
			Vector<Double> uVelocities = uVelocitiesSets.elementAt(velocitiesSetsIndex);
			Vector<Double> wOrVVelocities = wOrVVelocitiesSets.elementAt(velocitiesSetsIndex);
			
			int numberOfVelocities = Math.min(uVelocities.size(), wOrVVelocities.size());
			double uBar = MAJFCMaths.mean(uVelocities);
			double wOrVBar = MAJFCMaths.mean(wOrVVelocities);
			double stDevProduct = MAJFCMaths.standardDeviation(uVelocities) * MAJFCMaths.standardDeviation(wOrVVelocities);

			boolean firstPass = true;		
			
			// Start at minimum hole size as this will give maximum values for normalisation
			for (int holeSize = LOWER_HOLE_SIZE_VALUE, pointIndex = 0; holeSize <= UPPER_HOLE_SIZE_VALUE; ++holeSize, ++pointIndex) {
				double holeLimit = holeSize * stDevProduct;
		
				Double[] shearStressSums = new Double[] { 0d, 0d, 0d, 0d };
				Double[] durationSums = new Double[] { 0d, 0d, 0d, 0d };
				
				for (int j = 0; j < numberOfVelocities; ++j) {
					double uPrime = uVelocities.elementAt(j) - uBar;
					double wOrVPrime = wOrVVelocities.elementAt(j) - wOrVBar;
					double shearStress = uPrime * wOrVPrime;
				
					if (Math.abs(shearStress) > holeLimit) {
						
						if (normalise) {
							shearStress /= stDevProduct;
						}
						
						int quadrant = findQuadrant(uPrime, wOrVPrime);

						shearStressSums[quadrant] += shearStress;
						++durationSums[quadrant];
					}
				}

				// First pass will always give the highest values for the shear stress sums
				if (firstPass) {
					for (int i = 0; i < NUMBER_OF_QUADRANTS; ++i) {
						maxShearStressSum = Math.max(maxShearStressSum, Math.abs(shearStressSums[i]));
						totalShearStress += shearStressSums[i];
					}
				}
				
				// Add the points to the data so that they read clockwise
				int pointIndexPlusOne = pointIndex + 1;
				// Quadrant 1
				mQHData[velocitiesSetsIndex][pointIndex][HOLE_SIZE_INDEX] = holeSize;
				mQHData[velocitiesSetsIndex][pointIndex][SHEAR_STRESS_INDEX] = shearStressSums[QUADRANT_1];
				// Quadrant 4
				mQHData[velocitiesSetsIndex][2 * NUMBER_OF_HOLE_SIZES - pointIndexPlusOne][HOLE_SIZE_INDEX] = holeSize;
				mQHData[velocitiesSetsIndex][2 * NUMBER_OF_HOLE_SIZES - pointIndexPlusOne][SHEAR_STRESS_INDEX] = shearStressSums[QUADRANT_4];
				// Quadrant 3
				mQHData[velocitiesSetsIndex][pointIndex + 2 * NUMBER_OF_HOLE_SIZES][HOLE_SIZE_INDEX] = -holeSize;
				mQHData[velocitiesSetsIndex][pointIndex + 2 * NUMBER_OF_HOLE_SIZES][SHEAR_STRESS_INDEX] = -shearStressSums[QUADRANT_3];
				// Quadrant 2
				mQHData[velocitiesSetsIndex][4 * NUMBER_OF_HOLE_SIZES - pointIndexPlusOne][HOLE_SIZE_INDEX] = -holeSize;
				mQHData[velocitiesSetsIndex][4 * NUMBER_OF_HOLE_SIZES - pointIndexPlusOne][SHEAR_STRESS_INDEX] = -shearStressSums[QUADRANT_2];
				
				for (int quadrantIndex = 0; quadrantIndex < NUMBER_OF_QUADRANTS; ++quadrantIndex) {
					mProportionAtHoleSizeData[velocitiesSetsIndex * NUMBER_OF_QUADRANTS + quadrantIndex][pointIndex][HOLE_SIZE_INDEX] = holeSize;
					mProportionAtHoleSizeData[velocitiesSetsIndex * NUMBER_OF_QUADRANTS + quadrantIndex][pointIndex][PROPORTION_AT_HOLE_SIZE_INDEX] = 100 * (shearStressSums[quadrantIndex]/totalShearStress);
					//mProportionAtHoleSizeData[velocitiesSetsIndex * NUMBER_OF_QUADRANTS + quadrantIndex][pointIndex][PROPORTION_AT_HOLE_SIZE_INDEX] = Math.abs(shearStressSums[quadrantIndex]) < 0.03 * Math.abs(maxShearStressSum) ? 0 : 100 * (shearStressSums[quadrantIndex]/totalShearStress);

					mDurationAtHoleSizeData[velocitiesSetsIndex * NUMBER_OF_QUADRANTS + quadrantIndex][pointIndex][HOLE_SIZE_INDEX] = holeSize;
					mDurationAtHoleSizeData[velocitiesSetsIndex * NUMBER_OF_QUADRANTS + quadrantIndex][pointIndex][DURATION_AT_HOLE_SIZE_INDEX] = 100 * (durationSums[quadrantIndex]/numberOfVelocities);
				}

				firstPass = false;
			} // Hole size
		} // Velocities sets
		
		// Now that we've been through all the data sets we know the maxShearStressSum and need to scale the data
		for (int velocitiesSetsIndex = 0; velocitiesSetsIndex < numberOfVelocitiesSets; ++velocitiesSetsIndex) {
			for (int pointIndex = 0; pointIndex < mQHData[velocitiesSetsIndex].length; ++pointIndex) {
				mQHData[velocitiesSetsIndex][pointIndex][SHEAR_STRESS_INDEX] /= maxShearStressSum;
			}
		}
		
		buildGUI();
	}

	private int findQuadrant(double uPrime, double wOrVPrime) {
		int quadrant = -1;
		
		if (uPrime > 0) {
			if (wOrVPrime > 0) {
				quadrant = QUADRANT_1;
			} else {
				quadrant = QUADRANT_4;
			}
		} else {
			if (wOrVPrime > 0) {
				quadrant = QUADRANT_2;
			} else {
				quadrant = QUADRANT_3;
			}
		}

		return quadrant;
	}
	
	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		NumberAxis xAxisForQHChart = new NumberAxis(DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_X_AXIS_LABEL));
		xAxisForQHChart.setTickUnit(new NumberTickUnit(5, NumberFormat.getIntegerInstance()));

		NumberAxis yAxisForQHChart = new NumberAxis(DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_Y_AXIS_LABEL));
		yAxisForQHChart.setTickUnit(new NumberTickUnit(0.1, NumberFormat.getNumberInstance()));

		DefaultXYItemRenderer theRendererForQHChart = new DefaultXYItemRenderer();
		theRendererForQHChart.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mQHDataSet.getSeriesCount(); ++i) {
			theRendererForQHChart.setSeriesShapesVisible(i, false);
		}
		
		XYPlot thePlotForQHChart = new XYPlot(mQHDataSet, xAxisForQHChart, yAxisForQHChart, theRendererForQHChart);
		thePlotForQHChart.setRangeZeroBaselineVisible(true);
		thePlotForQHChart.setDomainZeroBaselineVisible(true);
		
		mTheQHChart = new JFreeChart(DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, thePlotForQHChart, true);
		
		mQHChartPanel = new ExportableChartPanel(this, mTheQHChart);

		add(mQHChartPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

		// Proportion at hole size chart
		NumberAxis xAxisForProportionAtHoleSizeChart = new NumberAxis(DAStrings.getString(DAStrings.QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL));
		xAxisForProportionAtHoleSizeChart.setTickUnit(new NumberTickUnit(1, NumberFormat.getIntegerInstance()));

		NumberAxis yAxisForProportionAtHoleSizeChart = new NumberAxis(DAStrings.getString(DAStrings.QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL));
//		yAxisForProportionAtHoleSizeChart(new NumberTickUnit(0.1, NumberFormat.getNumberInstance()));

		DefaultXYItemRenderer theRendererForProportionAtHoleSizeChart = new DefaultXYItemRenderer();
		theRendererForProportionAtHoleSizeChart.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mProportionAtHoleSizeDataSet.getSeriesCount(); ++i) {
			theRendererForProportionAtHoleSizeChart.setSeriesShapesVisible(i, false);
		}
		
		XYPlot thePlotForProportionAtHoleSizeChart = new XYPlot(mProportionAtHoleSizeDataSet, xAxisForProportionAtHoleSizeChart, yAxisForProportionAtHoleSizeChart, theRendererForProportionAtHoleSizeChart);
		thePlotForProportionAtHoleSizeChart.setRangeZeroBaselineVisible(true);
		thePlotForProportionAtHoleSizeChart.setDomainZeroBaselineVisible(true);
		
		mTheProportionAtHoleSizeChart = new JFreeChart(DAStrings.getString(DAStrings.QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, thePlotForProportionAtHoleSizeChart, true);
		
		mProportionAtHoleSizeChartPanel = new ExportableChartPanel(this, mTheProportionAtHoleSizeChart);

		// Duration at hole size chart
		NumberAxis xAxisForDurationAtHoleSizeChart = new NumberAxis(DAStrings.getString(DAStrings.QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL));
		xAxisForDurationAtHoleSizeChart.setTickUnit(new NumberTickUnit(1, NumberFormat.getIntegerInstance()));

		NumberAxis yAxisForDurationAtHoleSizeChart = new NumberAxis(DAStrings.getString(DAStrings.QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL));
//		yAxisForDurationAtHoleSizeChart(new NumberTickUnit(0.1, NumberFormat.getNumberInstance()));

		DefaultXYItemRenderer theRendererForDurationAtHoleSizeChart = new DefaultXYItemRenderer();
		theRendererForDurationAtHoleSizeChart.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mDurationAtHoleSizeDataSet.getSeriesCount(); ++i) {
			theRendererForDurationAtHoleSizeChart.setSeriesShapesVisible(i, false);
		}
		
		XYPlot thePlotForDurationAtHoleSizeChart = new XYPlot(mDurationAtHoleSizeDataSet, xAxisForDurationAtHoleSizeChart, yAxisForDurationAtHoleSizeChart, theRendererForDurationAtHoleSizeChart);
		thePlotForDurationAtHoleSizeChart.setRangeZeroBaselineVisible(true);
		thePlotForDurationAtHoleSizeChart.setDomainZeroBaselineVisible(true);
		
		mTheDurationAtHoleSizeChart = new JFreeChart(DAStrings.getString(DAStrings.QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, thePlotForDurationAtHoleSizeChart, true);
		
		mDurationAtHoleSizeChartPanel = new ExportableChartPanel(this, mTheDurationAtHoleSizeChart);

		mTabbedPanel = new MAJFCTabbedPanel();
		mTabbedPanel.add(DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_TAB_LABEL), mQHChartPanel);
		mTabbedPanel.add(DAStrings.getString(DAStrings.QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TAB_LABEL), mProportionAtHoleSizeChartPanel);
		mTabbedPanel.add(DAStrings.getString(DAStrings.QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TAB_LABEL), mDurationAtHoleSizeChartPanel);
		
		add(mTabbedPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}

	private class QHDataSet extends XYDataSetAdapter {
		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mQHData[series].length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mQHData.length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getXValue(int series, int item) {
			return mQHData[series][item][HOLE_SIZE_INDEX];
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			return mQHData[series][item][SHEAR_STRESS_INDEX];
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return mQHLegendEntries.elementAt(series);
		}
	}
	
	private class ProportionAtHoleSizeDataSet extends XYDataSetAdapter {
		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mProportionAtHoleSizeData[series].length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mProportionAtHoleSizeData.length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getXValue(int series, int item) {
			return mProportionAtHoleSizeData[series][item][HOLE_SIZE_INDEX];
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			return mProportionAtHoleSizeData[series][item][PROPORTION_AT_HOLE_SIZE_INDEX];
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			int qhLegendIndex = series/NUMBER_OF_QUADRANTS;
			int quadrantIndex = series % NUMBER_OF_QUADRANTS + 1;
			
			return mQHLegendEntries.elementAt(qhLegendIndex) + " - " + quadrantIndex;
			//return mProportionAtHoleSizeLegendEntries.elementAt(series);
		}
	}

	private class DurationAtHoleSizeDataSet extends XYDataSetAdapter {
		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mDurationAtHoleSizeData[series].length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mDurationAtHoleSizeData.length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getXValue(int series, int item) {
			return mDurationAtHoleSizeData[series][item][HOLE_SIZE_INDEX];
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			return mDurationAtHoleSizeData[series][item][DURATION_AT_HOLE_SIZE_INDEX];
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			int qhLegendIndex = series/NUMBER_OF_QUADRANTS;
			int quadrantIndex = series % NUMBER_OF_QUADRANTS + 1;
			
			return mQHLegendEntries.elementAt(qhLegendIndex) + " - " + quadrantIndex;
			//return mDurationAtHoleSizeLegendEntries.elementAt(series);
		}
	}
}

