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

package com.mikejesson.vsa.frontEnd.crossSectionGraphs;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowListener;

import java.text.NumberFormat;

import java.util.Vector;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.title.LegendTitle;

import org.jfree.data.DomainOrder;
import org.jfree.data.RangeType;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ExportableChartPanel;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractCrossSectionGraph extends MAJFCStackedPanelWithFrame implements XYDataset {
	private Vector<Integer> mSortedYCoords = new Vector<Integer>();
	private Vector<Vector<Integer>> mSortedZCoordsSets = new Vector<Vector<Integer>>();
	protected double[][] mData;
	private ExportableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private XYPlot mThePlot;
	private AbstractDataSetUniqueId mDataSetId;
	
	protected final int Y_COORD_INDEX = 0;
	protected final int DATUM_INDEX = 1;
	
	/**
	 * Constructor
	 * @param initialisationObjects Objects required to initialise before main constructor is called. First should be the 
	 * unique data set id of the data set displayed in this graph
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionGraph(MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, String title, String xAxisTitle, String yAxisTitle, WindowListener windowListener) throws HeadlessException {
		this(parent, initialisationObjects, title, xAxisTitle, yAxisTitle, windowListener, true);
	}
	
	/**
	 * Constructor
	 * @param initialisationObjects Objects required to initialise before main constructor is called. First should be the 
	 * unique data set id of the data set displayed in this graph
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionGraph(MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, String title, String xAxisTitle, String yAxisTitle, WindowListener windowListener, boolean showFrame) throws HeadlessException {
		super(parent, new GridBagLayout());
		
		initialise(initialisationObjects);

		try {
			DAFrame.getBackEndAPI().getSortedDataPointCoordinates(mDataSetId, mSortedYCoords, mSortedZCoordsSets);
		} catch (BackEndAPIException theException) {
			MAJFCLogger.log("Failed to get sorted coordinates in VerticalSectionVelocityGraph::constructor");
			return;
		}
		
		prepareDataArray(mDataSetId, mSortedYCoords, mSortedZCoordsSets);
		
		buildGUI(title, xAxisTitle, yAxisTitle);
		
		if (showFrame) {
			showInFrame(title + '(' + mDataSetId.getFullDisplayString() + ')', windowListener, DAFrame.getFrame());
		}
	}
	
	/**
	 * Does initialisation stuff required as first call in constructor. Any override of this method should call the superclass
	 * version before anything else
	 * @param initialisationObjects Any objects needed for initialisation. First should be the unique data set id of the
	 * data set displayed in this graph
	 */
	protected void initialise(Object[] initialisationObjects) {
		mDataSetId = (AbstractDataSetUniqueId) initialisationObjects[0]; 
	}
	
	/**
	 * Gets the preferred size
	 * @return The preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width * 0.8);
		int height = (int) width/2;

		return new Dimension(width, height);
	}
	
	/**
	 * Sets up the values in the data array (mData), calculating the depth-averaged velocity at each y-coordinate
	 * @param dataSetId The unique data set id of the data set displayed in this graph
	 * @param sortedYCoords The sorted y-coordinates
	 * @param sortedZCoordsSets A list of sets of sorted z-coordinates, with each entry corresponding to the same indexed y-coordinate in sortedYCoords
	 */
	protected abstract void prepareDataArray(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets);
	
	/**
	 * Builds the GUI
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 */
	protected void buildGUI(String title, String xAxisTitle, String yAxisTitle) {
		setLayout(new GridBagLayout());
		
		NumberAxis xAxis = new NumberAxis(xAxisTitle);
		xAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
		xAxis.setRangeType(RangeType.POSITIVE);
		
		NumberAxis yAxis = new NumberAxis(yAxisTitle);
		//yAxis.setRangeType(RangeType.POSITIVE);

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		//theRenderer.setBaseLinesVisible(false);
			
		mThePlot = new XYPlot(this, xAxis, yAxis, theRenderer);
		mTheChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, mThePlot, false);
		mTheChart.addLegend(new LegendTitle(mTheChart.getXYPlot()));

		mChartPanel = new ExportableChartPanel(this, mTheChart);
		
		int x = 0;
		int y = 0;
		add(mChartPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		
		setAxisRanges();

		validate();
	}
	
	protected void addSecondaryAxis(int series, NumberAxis axis, XYDataset dataSet) {
		mThePlot.setRangeAxis(series, axis);
		mThePlot.setDataset(series, dataSet);
		mThePlot.mapDatasetToRangeAxis(series, series);

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		theRenderer.setSeriesPaint(series, Color.MAGENTA);
		//mThePlot.setRangeZeroBaselineVisible(true);
		mThePlot.setRenderer(series, theRenderer);
	}
	
	/**
	 * Sets the ranges for the graph axes
	 */
	private void setAxisRanges() {
		try {
			int leftBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue();
			int rightBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue();

			mThePlot.getDomainAxis().setRange(leftBankPosition, rightBankPosition);

			int numberOfRangeAxes = mThePlot.getRangeAxisCount();
			
			for (int i = 0; i < numberOfRangeAxes; ++i) {
				mThePlot.getRangeAxis(i).configure();
			}
		} catch (Exception e) {
		}
		
		repaint();
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
		return ((Double) getXValue(series, item)).intValue();
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public double getXValue(int series, int item) {
		return mData[item][Y_COORD_INDEX];
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public Number getY(int series, int item) {
		return ((Double) getYValue(series, item)).intValue();
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public double getYValue(int series, int item) {
		return mData[item][DATUM_INDEX];
	}
	
	@Override
	/**
	 * XYDataset implementation
	 */
	public int getSeriesCount() {
		return mData[0].length - 1;
	}

	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public Comparable getSeriesKey(int series) {
		return "series";
	}

	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public int indexOf(Comparable seriesKey) {
		return 0;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public void addChangeListener(DatasetChangeListener listener) {
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
	public void removeChangeListener(DatasetChangeListener listener) {
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public void setGroup(DatasetGroup group) {
	}
	
	public AbstractDataSetUniqueId getDataSetId() {
		return mDataSetId;
	}
}
