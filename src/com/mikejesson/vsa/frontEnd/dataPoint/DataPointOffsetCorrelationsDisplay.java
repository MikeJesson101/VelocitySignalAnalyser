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
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CoordinateAxisIdentifer;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ExportableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.XYDataSetAdapter;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointOffsetCorrelationsDisplay extends MAJFCStackedPanelWithFrame {
	private ExportableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private double[][][] mData;
	private Vector<String> mLegendEntries;
	private MyDataSet mDataSet;
	private final CoordinateAxisIdentifer mComponent;
	
	private static final int SHIFT_INDEX = 0;
	private static final int CORRELATION_INDEX = 1;

	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries
	 * @param uVelocitiesSets The u-velocity measurements for the data points
	 * 
	 */
	public DataPointOffsetCorrelationsDisplay(CoordinateAxisIdentifer component, Vector<String> legendEntries, Vector<Double> offsetCorrelations, Vector<Double> times) {
		super(new GridBagLayout());
		
		mLegendEntries = legendEntries;
		mDataSet = new MyDataSet();
		mComponent = component;
		
		int numberOfValues = offsetCorrelations.size();
		mData = new double[1][numberOfValues][2];
			
		for (int i = 0; i < numberOfValues; ++i) {
			mData[0][i][SHIFT_INDEX] = times.get(i);
			mData[0][i][CORRELATION_INDEX] = offsetCorrelations.get(i);
		}
		
		buildGUI();
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		String title = DAStrings.getString(DAStrings.U_OFFSET_CORRELATIONS_GRAPH_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.U_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL);
		String yAxisTitle = DAStrings.getString(DAStrings.U_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL);
		
		if (mComponent.equals(BackEndAPI.Y_AXIS_OR_V_VELOCITY)) {
			title = DAStrings.getString(DAStrings.V_OFFSET_CORRELATIONS_GRAPH_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.V_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL);
			yAxisTitle = DAStrings.getString(DAStrings.V_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL);
		} else if (mComponent.equals(BackEndAPI.Z_AXIS_OR_W_VELOCITY)) {
			title = DAStrings.getString(DAStrings.W_OFFSET_CORRELATIONS_GRAPH_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.W_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL);	
			yAxisTitle = DAStrings.getString(DAStrings.W_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL);
		}

		NumberAxis xAxis = new NumberAxis(xAxisTitle);
		NumberAxis yAxis = new NumberAxis(yAxisTitle);

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		theRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mDataSet.getSeriesCount(); ++i) {
			theRenderer.setSeriesShapesVisible(i, false);
		}
		
		XYPlot thePlot = new XYPlot(mDataSet, xAxis, yAxis, theRenderer);
		thePlot.setRangeZeroBaselineVisible(true);
		thePlot.setDomainZeroBaselineVisible(true);
		
		mTheChart = new JFreeChart(title , JFreeChart.DEFAULT_TITLE_FONT, thePlot, true);
		
		mChartPanel = new ExportableChartPanel(this, mTheChart);

		add(mChartPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}

	private class MyDataSet extends XYDataSetAdapter {
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

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getXValue(int series, int item) {
			return mData[series][item][SHIFT_INDEX];
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			return mData[series][item][CORRELATION_INDEX];
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return mLegendEntries.elementAt(series);
		}
	}
}
