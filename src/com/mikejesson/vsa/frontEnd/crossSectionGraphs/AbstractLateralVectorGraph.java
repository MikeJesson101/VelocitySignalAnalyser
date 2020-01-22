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
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.RangeType;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYDataSet;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractLateralVectorGraph extends MAJFCStackedPanelWithFrame {
	protected Integer[][] mCoords;
	protected double[][] mLateralVectors;
	private ScaleableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	protected AbstractDataSetUniqueId mDataSetId;
	private MyXYDataSet mXYDataSet;
	
	protected final int Y_COORD_INDEX = 0;
	protected final int Z_COORD_INDEX = 1;
	
	protected final int Y_COMPONENT_INDEX = 0;
	protected final int Z_COMPONENT_INDEX = 1;
	protected final int MAGNITUDE_INDEX = 2;
	
	private final AffineTransform mTransformer = new AffineTransform();
	private final int mPointMarkerScaleFactor;
	
	/**
	 * Constructor
	 * @param dataSetId The unique data set id of the data set displayed in this graph
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public AbstractLateralVectorGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, String title, String xAxisTitle, String yAxisTitle) throws HeadlessException {
		this(dataSetId, parent, 10, title, xAxisTitle, yAxisTitle);
	}
	
	/**
	 * Constructor
	 * @param dataSetId The unique data set id of the data set displayed in this graph
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public AbstractLateralVectorGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, int pointMarkerScaleFactor, String title, String xAxisTitle, String yAxisTitle) throws HeadlessException {
		super(parent, new GridBagLayout());
		
		mDataSetId = dataSetId;
		mPointMarkerScaleFactor = pointMarkerScaleFactor;
		
		setupData();
		
		buildGUI(title, xAxisTitle, yAxisTitle);
		
		showInFrame(title + " (" + mDataSetId.getFullDisplayString() + ')', null, DAFrame.getFrame());
	}
	
	private void setupData() {
		try {
			mCoords = DAFrame.getBackEndAPI().getUnsortedDataPointCoordinates(mDataSetId);
		} catch (BackEndAPIException theException) {
			MAJFCLogger.log("Failed to get unsorted coordinates in AbstractLateralVectorGraph::constructor");
			return;
		}
		
		prepareDataArray();
	}

	@Override
	public void updateDisplay() {
		setupData();
		
		mChartPanel.updateDisplay();
		super.updateDisplay();
	}
	
	/**
	 * Gets the preferred size
	 * @return The preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width * 0.8);
		int height = (int) width/4;

		return new Dimension(width, height);
	}
	
	/**
	 * Sets up the values in the data array, calculating the depth-averaged velocity at each y-coordinate
	 */
	protected abstract void prepareDataArray();
	
	/**
	 * Builds the GUI
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 */
	private void buildGUI(String title, String xAxisTitle, String yAxisTitle) {
		setLayout(new GridBagLayout());
		
		NumberAxis xAxis = new NumberAxis(xAxisTitle);
//		xAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
		xAxis.setRangeType(RangeType.POSITIVE);

		NumberAxis yAxis = new NumberAxis(yAxisTitle);
//		yAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
		yAxis.setRangeType(RangeType.POSITIVE);

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer() {
		    /**
		     * Returns a shape used to represent a data item.
		     * @param series The series index
		     * @param item  The item index
		     * @return The shape (never <code>null</code>).
		     */
			@Override
		    public Shape getItemShape(int series, int item) {
		        return getPointMarker(series, item);
		    }			
		};
		theRenderer.setBaseLinesVisible(false);
		theRenderer.setSeriesPaint(0, Color.BLACK);
					
		int leftBankPosition = 0, rightBankPosition = 0, waterDepth = 0;

		try {
			leftBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue();
			rightBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue();
			waterDepth = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_WATER_DEPTH).intValue();
		} catch (Exception e) {
		}

		mXYDataSet = new MyXYDataSet(leftBankPosition, rightBankPosition, 0, waterDepth);
		mTheChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, new XYPlot(mXYDataSet, xAxis, yAxis, theRenderer), false);
		mChartPanel = new ScaleableChartPanel(mDataSetId, this, mTheChart, mXYDataSet, true, xAxisTitle, yAxisTitle, true);
		
		int x = 0;
		int y = 0;
		add(mChartPanel.getGUI(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

//		mTheChart.getXYPlot().getDomainAxis().configure();
		mTheChart.getXYPlot().getRangeAxis().configure();

		validate();
	}

	/**
	 * Gets the marker to draw at the point
	 * @param series The series index
	 * @param item  The item index
	 * @return The marker shape to draw
	 */
	private Shape getPointMarker(int series, int item) {
		int stemLength = (int) (mXYDataSet.getMagnitude(series, item) * mPointMarkerScaleFactor);
		int headLength = 5;
		int[] xPoints = {0, stemLength, stemLength, stemLength + headLength, stemLength, stemLength};
		int[] yPoints = {0, 0, 2, 0, -2, 0};
		
		Polygon arrow = new Polygon(xPoints, yPoints, xPoints.length);
		mTransformer.setToRotation(mXYDataSet.getYComponent(series, item), -mXYDataSet.getZComponent(series, item));

		return mTransformer.createTransformedShape(arrow);
	}
	
	public class MyXYDataSet extends ScaleableXYDataSet {
		public MyXYDataSet(int unscaledXMin, int unscaledXMax, int unscaledYMin, int unscaledYMax) {
			super(unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}

		@Override
		/**
		 * ScaleableXYDataset implementation
		 */
		public int getItemCount(int series) {
			return mCoords.length;
		}
		
		@Override
		/**
		 * ScaleableXYDataset implementation
		 */
		public double getTheXValue(int series, int item) {
			return mCoords[item][Y_COORD_INDEX];
		}
	
		@Override
		/**
		 * ScaleableXYDataset implementation
		 */
		public double getTheYValue(int series, int item) {
			return mCoords[item][Z_COORD_INDEX];
		}
		
		/**
		 * Gets the magnitude of the vector for the given series item.
		 * @param series The series index
		 * @param item The item index
		 * @return The magnitude of the vector
		 */
		public double getMagnitude(int series, int item) {
			return mLateralVectors[item][MAGNITUDE_INDEX];
		}
		
		/**
		 * Gets the y-component of the vector for the given series item.
		 * @param series The series index
		 * @param item The item index
		 * @return The y-component of the vector
		 */
		public double getYComponent(int series, int item) {
			if (mMirroredAboutVertical) {
				return -mLateralVectors[item][Y_COMPONENT_INDEX];
			}
			
			return mLateralVectors[item][Y_COMPONENT_INDEX];
		}		
		
		/**
		 * Gets the z-component of the vector for the given series item.
		 * @param series The series index
		 * @param item The item index
		 * @return The z-component of the vector
		 */
		public double getZComponent(int series, int item) {
			return mLateralVectors[item][Z_COMPONENT_INDEX];
		}
	}
}
