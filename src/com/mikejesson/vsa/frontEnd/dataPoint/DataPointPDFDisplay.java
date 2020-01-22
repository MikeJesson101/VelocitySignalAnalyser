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

import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCMaths;
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
public class DataPointPDFDisplay extends MAJFCStackedPanelWithFrame {
	private ExportableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private double[][][] mData;
	private Vector<String> mLegendEntries;
	private MyDataSet mDataSet;
	private final CoordinateAxisIdentifer mComponent;
	
	private static final int VELOCITY_INDEX = 0;
	private static final int PDF_INDEX = 1;

	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries
	 * @param uVelocitiesSets The u-velocity measurements for the data points
	 * 
	 */
	public DataPointPDFDisplay(CoordinateAxisIdentifer component, Vector<String> legendEntries, Vector<Vector<Double>> uVelocitiesSets) {
		super(new GridBagLayout());
		
		mLegendEntries = legendEntries;
		mDataSet = new MyDataSet();
		mComponent = component;
		
		int numberOfVelocitiesSets = uVelocitiesSets.size();
		
		mData = new double[numberOfVelocitiesSets * 2][][];
		
		for (int velocitiesSetsIndex = 0; velocitiesSetsIndex < numberOfVelocitiesSets; ++velocitiesSetsIndex) {
			Vector<Double> velocities = uVelocitiesSets.elementAt(velocitiesSetsIndex);
			LinkedList<Double[]> pdf = MAJFCMaths.probabilityDensityFunction(velocities, true, 100);
			double mean = MAJFCMaths.mean(velocities);
			double stDev = MAJFCMaths.standardDeviation(velocities);

			// Strip values for which PDF is 0
			int numberOfValues = pdf.size();
			
			for (int i = 0; i < numberOfValues; ++i) {
				if (pdf.get(i)[1] <= 0 || pdf.get(i)[0] == 0) {
					//pdf.get(i)[1] = 0.000001;
					
					pdf.remove(i);
					// Decrement these as list will have reduced in size and later elements will have moved up one
					--i;
					--numberOfValues;
				}
			}
			
			numberOfValues = pdf.size();
			mData[velocitiesSetsIndex] = new double[numberOfValues][2];
			mData[velocitiesSetsIndex + numberOfVelocitiesSets] = new double[numberOfValues][2];
			
			for (int i = 0; i < numberOfValues; ++i) {
				mData[velocitiesSetsIndex][i][VELOCITY_INDEX] = pdf.get(i)[0] - mean;
				mData[velocitiesSetsIndex][i][PDF_INDEX] = pdf.get(i)[1];

				// Gaussian for comparison
				mData[velocitiesSetsIndex + numberOfVelocitiesSets][i][VELOCITY_INDEX] = pdf.get(i)[0] - mean;
				mData[velocitiesSetsIndex + numberOfVelocitiesSets][i][PDF_INDEX] = (1.0/(stDev * Math.sqrt(2.0 * Math.PI))) * Math.pow(Math.E, -(1.0/2.0) * Math.pow((pdf.get(i)[0] - mean)/stDev, 2));
//				mData[velocitiesSetsIndex + numberOfVelocitiesSets][i][PDF_INDEX] = (1.0/(1.0 * Math.sqrt(2.0 * Math.PI))) * Math.pow(Math.E, -(1.0/2.0) * Math.pow((pdf.get(i)[0] - mean)/stDev, 2));
//				mData[velocitiesSetsIndex + numberOfVelocitiesSets][i][PDF_INDEX] = Math.pow(Math.E, -(1.0/2.0) * Math.pow((pdf.get(i)[0] - mean)/stDev, 2))/numberOfValues;
			}
		}
		
		buildGUI();
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		String title = DAStrings.getString(DAStrings.U_PDF_GRAPH_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.U_PDF_GRAPH_X_AXIS_LABEL);
		String yAxisTitle = DAStrings.getString(DAStrings.U_PDF_GRAPH_Y_AXIS_LABEL);
		
		if (mComponent.equals(BackEndAPI.Y_AXIS_OR_V_VELOCITY)) {
			title = DAStrings.getString(DAStrings.V_PDF_GRAPH_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.V_PDF_GRAPH_X_AXIS_LABEL);
			yAxisTitle = DAStrings.getString(DAStrings.V_PDF_GRAPH_Y_AXIS_LABEL);
		} else if (mComponent.equals(BackEndAPI.Z_AXIS_OR_W_VELOCITY)) {
			title = DAStrings.getString(DAStrings.W_PDF_GRAPH_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.W_PDF_GRAPH_X_AXIS_LABEL);	
			yAxisTitle = DAStrings.getString(DAStrings.W_PDF_GRAPH_Y_AXIS_LABEL);
		}

		NumberAxis xAxis = new NumberAxis(xAxisTitle);
		NumberAxis yAxis = new LogarithmicAxis(yAxisTitle);

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
			return mData[series][item][VELOCITY_INDEX];
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			return mData[series][item][PDF_INDEX];
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			int numberOfLegendEntries = mLegendEntries.size();
			
			if (series >= numberOfLegendEntries) {
				return DAStrings.getString(DAStrings.GAUSSIAN, mLegendEntries.elementAt(series - numberOfLegendEntries));
			}
			
			return mLegendEntries.elementAt(series);
		}
	}
}
