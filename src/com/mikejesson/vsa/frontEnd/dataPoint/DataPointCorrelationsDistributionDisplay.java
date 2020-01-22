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
import com.mikejesson.majfc.helpers.MAJFCSorted3DList;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ExportableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.XYDataSetAdapter;


/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointCorrelationsDistributionDisplay extends MAJFCStackedPanelWithFrame {
	private ExportableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private Vector<String> mLegendEntries;
	private MyDataSet mDataSet;
	private MAJFCSorted3DList mVelocities;
	
	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries
	 * @param uVelocitiesSets The u-velocity measurements for the data points
	 * @throws Exception 
	 * 
	 */
	public DataPointCorrelationsDistributionDisplay(Vector<String> legendEntries, Vector<Double> uCorrelations, Vector<Double> vCorrelations, Vector<Double> wCorrelations) throws Exception {
		super(new GridBagLayout());
		
		mLegendEntries = legendEntries;
		mDataSet = new MyDataSet();
		
		mVelocities = new MAJFCSorted3DList(uCorrelations, vCorrelations, wCorrelations);
		
		buildGUI();
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		String title = DAStrings.getString(DAStrings.CORRELATIONS_DISTRIBUTION_GRAPH_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.CORRELATIONS_DISTRIBUTION_GRAPH_X_AXIS_LABEL);
		String yAxisTitle = DAStrings.getString(DAStrings.CORRELATIONS_DISTRIBUTION_GRAPH_Y_AXIS_LABEL);

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
			return mVelocities.size();
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return 2;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getXValue(int series, int item) {
			return mVelocities.getU(item);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			if (series == 0) {
				return Math.abs(mVelocities.getV(item));
			} else if (series == 1) {
				return Math.abs(mVelocities.getW(item));
			} else {
				return -Double.NaN;
			}
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
