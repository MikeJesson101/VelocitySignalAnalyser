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

import java.util.Vector;

import javax.swing.BorderFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYDataSet;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointSignalCorrelationAndSNRDisplay extends MAJFCStackedPanelWithFrame {
	private ScaleableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private double[][][] mData;
	private Vector<String> mLegendEntries;
	private MyDataSet mChartDataSet;
	private final AbstractDataSetUniqueId mDataSetId;
	
	private static final int TIME_INDEX = 0;
	private static final int DATUM_INDEX = 1;
	
	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries (should match the order of velocitiesSets)
	 * @param correlationsSets The velocity measurements for the first component of the data points to display power spectra for
	 * @param snrsSets The velocity measurements for the second component of the data points to display power spectra for
	 * @throws Exception 
	 * 
	 */
	public DataPointSignalCorrelationAndSNRDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<String> legendEntries, Vector<Vector<Double>> correlationsSets, Vector<Vector<Double>> snrsSets) throws Exception {
		super(new GridBagLayout());
		
		mLegendEntries = legendEntries;
		mDataSetId = dataSetId;
	
		double samplingRate = BackEndAPI.getBackEndAPI().getConfigData(dataSetId).get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
		int numberOfDataSets = correlationsSets.size();
		
		if (numberOfDataSets != snrsSets.size()) {
			throw new Exception("Mismatch in DataPointSignalCorrelationAndSNRDisplay constructor");
		}
		
		mData = new double[numberOfDataSets * 2][][];
		double xMax = Double.MIN_VALUE, xMin = Double.MAX_VALUE, yMax = Double.MIN_NORMAL, yMin = Double.MAX_VALUE;
		
		for (int dataSetIndex = 0; dataSetIndex < numberOfDataSets; ++dataSetIndex) {
			Vector<Double> correlations = correlationsSets.elementAt(dataSetIndex);
			Vector<Double> snrs = snrsSets.elementAt(dataSetIndex);
			int numberOfData = correlations.size();
			int snrDataSetIndex = dataSetIndex + numberOfDataSets;
			
			mData[dataSetIndex] = new double[numberOfData][3];
			mData[snrDataSetIndex] = new double[numberOfData][3];
			
			for (int i = 0; i < numberOfData; ++i) {
				mData[dataSetIndex][i][TIME_INDEX] = ((double) i)/samplingRate;
				mData[dataSetIndex][i][DATUM_INDEX] = correlations.elementAt(i);
				mData[snrDataSetIndex][i][TIME_INDEX] = ((double) i)/samplingRate;
				mData[snrDataSetIndex][i][DATUM_INDEX] = snrs.elementAt(i);
				
				xMin = Math.min(xMin, mData[dataSetIndex][i][TIME_INDEX]);
				xMax = Math.max(xMax, mData[dataSetIndex][i][TIME_INDEX]);
				yMin = Math.min(yMin, mData[dataSetIndex][i][DATUM_INDEX]);
				yMax = Math.max(yMax, mData[dataSetIndex][i][DATUM_INDEX]);
				yMin = Math.min(yMin, mData[snrDataSetIndex][i][DATUM_INDEX]);
				yMax = Math.max(yMax, mData[snrDataSetIndex][i][DATUM_INDEX]);
			}
		}

		mChartDataSet = new MyDataSet(xMin, xMax, yMin, yMax);

		buildGUI();
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL));
		NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL));

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		theRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mChartDataSet.getSeriesCount(); ++i) {
			theRenderer.setSeriesShapesVisible(i, false);
		}
		
		XYPlot thePlot = new XYPlot(mChartDataSet, xAxis, yAxis, theRenderer);
		thePlot.setRangeZeroBaselineVisible(true);
		thePlot.setDomainZeroBaselineVisible(true);
		
		mTheChart = new JFreeChart(DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, thePlot, mLegendEntries != null && mLegendEntries.size() > 0);
		
		mChartPanel = new ScaleableChartPanel(mDataSetId, this, mTheChart, mChartDataSet, true, DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL), DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL), false);

		add(mChartPanel.getGUI(), MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}

	private class MyDataSet extends ScaleableXYDataSet {
		public MyDataSet(double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			super(unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mData[series].length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mData.length;
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			int numberOfLegendEntries = mLegendEntries.size();
			return series < numberOfLegendEntries ? mLegendEntries.elementAt(series) + DAStrings.getString(DAStrings.SIGNAL_CORRELATION_LEGEND_TEXT) : mLegendEntries.elementAt(series - numberOfLegendEntries) + DAStrings.getString(DAStrings.SNR_LEGEND_TEXT);
		}

		@Override
		public double getTheXValue(int series, int item) {
			return mData[series][item][TIME_INDEX];
		}

		@Override
		public double getTheYValue(int series, int item) {
			return mData[series][item][DATUM_INDEX];
		}
	}
}
