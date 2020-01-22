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

package com.mikejesson.vsa.widgits.basicAndAbstractGraphs;


import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowListener;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.title.LegendTitle;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCComparableNumber;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYDataSet;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractMultiSeriesGraph extends MAJFCStackedPanelWithFrame {
	private Hashtable<Number, Vector<AMSGDataPoint>> mOrderedSeriesDataList;
	private Vector<MAJFCComparableNumber> mDataSeries;
	private Hashtable<Number, MAJFCComparableNumber> mDataSeriesLookup;
	private MAJFCPanel mChartPanel;
	private MyScaleableXYDataSet mChartDataSet;
	private JFreeChart mTheChart;
	private final String mLegendKeyLabel;
	private MAJFCPanel mCustomPanel;
	private final boolean mAutoConfigureAxes;
	protected AbstractDataSetUniqueId mDataSetId;
	
	protected class AMSGDataPoint {
		private final double mX;
		private final double mY;
		
		public AMSGDataPoint(double x, double y) {
			mX = x;
			mY = y;
		}

		/**
		 * @return The x-coordinate
		 */
		public double getX() {
			return mX;
		}

		/**
		 * @return The y-coordinate
		 */
		public double getY() {
			return mY;
		}
	}
	
	/**
	 * Constructor
	 * @param initialisationObjects Objects needed for initialisation before main construction takes place (as defined by child class)
	 * @param seriesId The id of the series to display when graph is first created
	 * @param title The title for the graph
	 * @param xAxisTitle The x-axis title for the graph
	 * @param yAxisTitle The y-axis title for the graph
	 * @param legendKeyLabel The label for the legend key
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public AbstractMultiSeriesGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, Number seriesId, String title, String xAxisTitle, String yAxisTitle, String legendKeyLabel, WindowListener windowListener) throws HeadlessException {
		this(dataSetId, parent, initialisationObjects, seriesId, title, xAxisTitle, yAxisTitle, legendKeyLabel, windowListener, true);
	}
	
	/**
	 * Constructor
	 * @param initialisationObjects Objects needed for initialisation before main construction takes place (as defined by child class)
	 * @param seriesId The id of the series to display when graph is first created
	 * @param title The title for the graph
	 * @param xAxisTitle The x-axis title for the graph
	 * @param yAxisTitle The y-axis title for the graph
	 * @param legendKeyLabel The label for the legend key
	 * @param windowListener The window listener for this display
	 * @param autoConfigureAxes If false, axes are not automatically configured when series are added
	 * @throws HeadlessException
	 */
	public AbstractMultiSeriesGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, Number seriesId, String title, String xAxisTitle, String yAxisTitle, String legendKeyLabel, WindowListener windowListener, boolean autoConfigureAxes) throws HeadlessException {
		super(parent, new GridBagLayout());
		
		initialise(initialisationObjects);

		mDataSetId = dataSetId;
		mAutoConfigureAxes = autoConfigureAxes;
		mLegendKeyLabel = legendKeyLabel;

		setupData();
		
		mDataSeries = new Vector<MAJFCComparableNumber>();
		mDataSeriesLookup = new Hashtable<Number, MAJFCComparableNumber>();
	
		buildGUI(title, xAxisTitle, yAxisTitle);
		
		// TODO Sort this out
		if (seriesId != null) {
			addOrRemoveSeries(seriesId);
		}

		showInFrame(title + " (" + mDataSetId.getFullDisplayString() + ')', windowListener, DAFrame.getFrame());
	}
	
	private void setupData() {
		try {
			mOrderedSeriesDataList = getOrderedSeriesDataList();
		} catch (Exception theException) {
			theException.printStackTrace();
			return;
		}
	}
	
	@Override
	public void updateDisplay() {
		setupData();
		mTheChart.fireChartChanged();
		
		super.updateDisplay();
	}

	protected abstract void initialise(Object[] initialisationObjects);
	protected abstract double[] getAxisRanges();
	
	/**
	 * Gets the sorted list of data, a hashtable whose keys are the identifiers used to label the series, and whose elements are vectors
	 * containing the ids of items within that series. Together, these allow a lookup of x-values indexed by seriesId and itemId.
	 * For example, the keys may be y-coordinates, with the elements being vectors containing the z-coordinates for which there
	 * are data points at that y-coordinate. Thus by looking up the element (z-coordinate list) for a given key (y-coordinate), getXValueAt
	 * can be used to plot the values of any given point datum value over a vertical section
	 * @see AbstractMultiSeriesGraph#getXValueAt(int, int)
	 * @return The hashtable described above
	 * @throws BackEndAPIException
	 */
	protected abstract Hashtable<Number, Vector<AMSGDataPoint>> getOrderedSeriesDataList() throws BackEndAPIException;
	
	/**
	 * Builds the GUI
	 * @param graphTitle The title for the graph
	 * @param xAxisTitle The x-axis title for the graph
	 * @param yAxisTitle The y-axis title for the graph
	 */
	private void buildGUI(String graphTitle, String xAxisTitle, String yAxisTitle) {
		setLayout(new GridBagLayout());
		
		NumberAxis xAxis = new NumberAxis(xAxisTitle);
		NumberAxis yAxis = new NumberAxis(yAxisTitle);

		customiseAxes(xAxis, yAxis);
		
		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		//theRenderer.setBaseLinesVisible(false);
		theRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		mChartDataSet = new MyScaleableXYDataSet(this);
		XYPlot thePlot = new XYPlot(mChartDataSet, xAxis, yAxis, theRenderer);
		mTheChart = new JFreeChart(graphTitle, JFreeChart.DEFAULT_TITLE_FONT, thePlot, false);
		mTheChart.addLegend(new LegendTitle(mTheChart.getXYPlot()));
	
		mCustomPanel = buildCustomPanel();
		
		mChartPanel = new ScaleableChartPanel(mDataSetId, this, mTheChart, mChartDataSet, true, xAxisTitle, yAxisTitle, false, false, false, false, true).getGUI();
		
		int x = 0;
		int y = 0;
		add(mChartPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		if (mCustomPanel != null) {
			add(mCustomPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_END, GridBagConstraints.NONE, 0, 5, 10, 5, 0, 0));
		}
		
		validate();
	}
	
	protected abstract MAJFCPanel buildCustomPanel();
	
	/**
	 * Customise the axes
	 * @param xAxis The x-axis to customise
	 * @param yAxis The y-axis to customise
	 */
	protected abstract void customiseAxes(NumberAxis xAxis, NumberAxis yAxis);

	/**
	 * Adds another series to this graph
	 * @param seriesId The y-coordinate of the series
	 */
	public void addOrRemoveSeries(Number seriesId) {
		MAJFCComparableNumber series = mDataSeriesLookup.remove(seriesId);
		
		if (series == null) {
			series = new MAJFCComparableNumber(seriesId);
			mDataSeries.add(series);
			mDataSeriesLookup.put(seriesId, series);
		} else {
			mDataSeries.removeElement(series);
		}

		Collections.sort(mDataSeries);
		
		if (mAutoConfigureAxes) {
			mTheChart.getXYPlot().getDomainAxis().configure();
			mTheChart.getXYPlot().getRangeAxis().configure();
		}
		
		updateDisplay();
	}

	private Vector<AMSGDataPoint> getDataPointsListForSeries(int series) {
		Number key = mDataSeries.elementAt(series).mNumber;
		Vector<AMSGDataPoint> dataPoints = mOrderedSeriesDataList.get(key.intValue());
		
		if (dataPoints == null) {
			dataPoints = mOrderedSeriesDataList.get(key.doubleValue());
		}
		
		return dataPoints;
	}
	
	/**
	 * Sets the data set id
	 * @param dataSetId The new data set id
	 */
	public void setDataSetId(AbstractDataSetUniqueId dataSetId) {
		mDataSetId = dataSetId;
	}
	
	private class MyScaleableXYDataSet extends ScaleableXYDataSet {
		public MyScaleableXYDataSet(AbstractMultiSeriesGraph parent) {
			super(parent.getAxisRanges());
		}
	
		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return getDataPointsListForSeries(series).size();
		}
	
		@Override
		/**
		 * XYDataset implementation
		 */
		public double getTheXValue(int series, int item) {
			return getDataPointsListForSeries(series).elementAt(item).getX();
		}
	
		@Override
		/**
		 * ScaleableXYDataset implementation
		 */
		public double getTheYValue(int series, int item) {
			return getDataPointsListForSeries(series).elementAt(item).getY();
		}
	
		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mDataSeries.size();
		}
	
		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return mLegendKeyLabel + MAJFCTools.formatNumber(mDataSeries.elementAt(series).getNumber().doubleValue(), 2, false, false);
		}
	}
}
