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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.widgits.DAStrings;


/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointQuadrantHoleVelocityProductDisplay extends MAJFCPanel implements XYDataset {
	private ChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private double[] mData;
	
	/**
	 * @param title The title (column header) for this display
	 * @param uPrimevPrimeProducts The list of data for to display
	 */
	public DataPointQuadrantHoleVelocityProductDisplay(String title, Vector<Double> uPrimevPrimeProducts) {
		int numberOfData = uPrimevPrimeProducts.size();
		mData = new double[numberOfData];
		
		for (int i = 0; i < numberOfData; ++i) {
			mData[i] = uPrimevPrimeProducts.elementAt(i);
		}

		buildGUI(title);
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI(String title) {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());
		
		NumberAxis xAxis = new NumberAxis();//DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_X_AXIS_LABEL));
		//xAxis.setTickUnit(new NumberTickUnit(0.2, NumberFormat.getNumberInstance()));

		NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_Y_AXIS_LABEL));
		yAxis.setTickUnit(new NumberTickUnit(10, NumberFormat.getNumberInstance()));

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		theRenderer.setSeriesShapesVisible(0, false);
		
		XYPlot thePlot = new XYPlot(this, xAxis, yAxis, theRenderer);
		mTheChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, thePlot, false);
		
		mChartPanel = new ChartPanel(mTheChart);

		add(mChartPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public DomainOrder getDomainOrder() {
		return null;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public int getItemCount(int series) {
		return mData.length;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public Number getX(int series, int item) {
		return getXValue(series, item);
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public double getXValue(int series, int item) {
		return item;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public Number getY(int series, int item) {
		return getYValue(series, item);
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public double getYValue(int series, int item) {
		return mData[item];
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public int getSeriesCount() {
		return 1;
	}

	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public Comparable getSeriesKey(int arg0) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public int indexOf(Comparable arg0) {
		return 0;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public void addChangeListener(DatasetChangeListener arg0) {
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public DatasetGroup getGroup() {
		return null;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public void removeChangeListener(DatasetChangeListener arg0) {
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public void setGroup(DatasetGroup arg0) {
	}
}
