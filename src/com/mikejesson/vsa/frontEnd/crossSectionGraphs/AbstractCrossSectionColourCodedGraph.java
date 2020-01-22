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





import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Vector;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;

import org.jfree.data.RangeType;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ColourCodedChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYZDataSet;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractCrossSectionColourCodedGraph extends MAJFCStackedPanelWithFrame {
	private Vector<Integer> mSortedYCoords = new Vector<Integer>();
	private Vector<Vector<Integer>> mSortedZCoords = new Vector<Vector<Integer>>();
	private Vector<Double[]> mData;
	private MyXYZDataSet mXYZDataSet;
	private ColourCodedChartPanel mChartPanel;
	private JFreeChart mTheChart;
	protected AbstractDataSetUniqueId mDataSetId;
	protected final DataPointSummaryIndex mDPSIndex;
	protected double mMean;
	protected final double mScalerForDimensionlessValues;

	protected SectionGraph mVerticalSectionGraph;
	protected SectionGraph mHorizontalSectionGraph;
	protected DepthAveragedGraphFrame mDepthAveragedGraph;

	private final int Y_COORD_INDEX = 0;
	private final int Z_COORD_INDEX = 1;
	protected final int DATA_INDEX = 2;
	
	/**
	 * Constructor
	 * @param dataSetId The unique id of the data set this graph is for
	 * @param steps The number of steps to use in the colour coding
	 * @param theDataPointSummaryIndex The DataPointSummaryIndex to use when preparing the data array
	 * @param chartTitle The title for the graph and the frame holding the graph
	 * @param legendText The text for the legend
	 * @param dataIsVectorInY If true then the sign of the data is switched when the data are mirrored about the vertical
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionColourCodedGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, int steps, DataPointSummaryIndex theDataPointSummaryIndex, String chartTitle, String legendText, boolean dataIsVectorInY) throws HeadlessException {
		this(dataSetId, parent, null, steps, theDataPointSummaryIndex, 1.0, chartTitle, legendText, dataIsVectorInY);
	}

	/**
	 * Constructor
	 * @param dataSetId The unique id of the data set this graph is for
	 * @param steps The number of steps to use in the colour coding
	 * @param theDataPointSummaryIndex The DataPointSummaryIndex to use when preparing the data array
	 * @param scalerForDimensionlessValues The scaling factor to be used when displaying with dimensionless values
	 * @param chartTitle The title for the graph and the frame holding the graph
	 * @param legendText The text for the legend
	 * @param dataIsVectorInY If true then the sign of the data is switched when the data are mirrored about the vertical
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionColourCodedGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, int steps, DataPointSummaryIndex theDataPointSummaryIndex, double scalerForDimensionlessValues, String chartTitle, String legendText, boolean dataIsVectorInY) throws HeadlessException {
		this(dataSetId, parent, null, steps, theDataPointSummaryIndex, scalerForDimensionlessValues, chartTitle, legendText, dataIsVectorInY);
	}
	
	/**
	 * Constructor
	 * @param dataSetId The unique id of the data set this graph is for
	 * @param initialisationObjects The initialisation objects which need to be set before the rest of the constructor is called.
	 * Override {@link AbstractCrossSectionColourCodedGraph#initialise(Object[])} to use the objects 
	 * @param steps The number of steps to use in the colour coding
	 * @param theDataPointSummaryIndex The DataPointSummaryIndex to use when preparing the data array
	 * @param chartTitle The title for the graph
	 * @param legendText The text for the legend
	 * @param dataIsVectorInY If true then the sign of the data is switched if the data are mirrored about the vertical
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionColourCodedGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, int steps, DataPointSummaryIndex theDataPointSummaryIndex, String chartTitle, String legendText, boolean dataIsVectorInY) throws HeadlessException {
		this(dataSetId, parent, initialisationObjects, steps, theDataPointSummaryIndex, 1.0, chartTitle, legendText, dataIsVectorInY);
	}
	
	/**
	 * Constructor
	 * @param dataSetId The unique id of the data set this graph is for
	 * @param initialisationObjects The initialisation objects which need to be set before the rest of the constructor is called.
	 * Override {@link AbstractCrossSectionColourCodedGraph#initialise(Object[])} to use the objects 
	 * @param steps The number of steps to use in the colour coding
	 * @param theDataPointSummaryIndex The DataPointSummaryIndex to use when preparing the data array
	 * @param scalerForDimensionlessValues The scaling factor to be used when displaying with dimensionless values
	 * @param chartTitle The title for the graph
	 * @param legendText The text for the legend
	 * @param dataIsVectorInY If true then the sign of the data is switched if the data are mirrored about the vertical
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionColourCodedGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, int steps, DataPointSummaryIndex theDataPointSummaryIndex, double scalerForDimensionlessValues, String chartTitle, String legendText, boolean dataIsVectorInY) throws HeadlessException {
		super(parent, new GridBagLayout());
		
		initialise(initialisationObjects);
		
		mDataSetId = dataSetId;
		mData = new Vector<Double[]>(1000);
		mDPSIndex = theDataPointSummaryIndex;
		mScalerForDimensionlessValues = scalerForDimensionlessValues;
		
		setupData();
		
		buildGUI(chartTitle, steps, legendText, dataIsVectorInY);
	}
	
	private void setupData() {
		try {
			DAFrame.getBackEndAPI().getSortedDataPointCoordinates(mDataSetId, mSortedYCoords, mSortedZCoords);
		} catch (BackEndAPIException theException) {
			MAJFCLogger.log("Failed to get sorted coordinates in AbstractCrossSectionColourCodedGraph::constructor");
			return;
		}
		
		prepareDataArray(mDataSetId, mDPSIndex, mSortedYCoords, mSortedZCoords, mData, Y_COORD_INDEX, Z_COORD_INDEX, DATA_INDEX);
	}
	
	/**
	 * Does anything that needs doing before the main body of the parent constructor is called
	 * This should be called at start of any override method in child classes
	 */
	protected void initialise(Object[] initialisationObjects) {
	}
	
	protected abstract void showTheVerticalSectionGraph();
	protected abstract void showTheHorizontalSectionGraph();
	protected abstract Vector<String> getDepthAveragedGraphLabels();
		
	/**
	 * Builds the GUI
	 * @param title The title for the graph
	 * @param steps The number of steps to use in the colour coding
	 * @param legendText The legend text for the graph
	 * @param dataIsVectorInY If true then the sign of the data is changed if the data are mirrored about the vertical
	 */
	private void buildGUI(String title, int steps, String legendText, boolean dataIsVectorInY) {
		setLayout(new GridBagLayout());
		
		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.CROSS_SECTION_GRID_X_AXIS_TITLE));
//		xAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
		xAxis.setRangeType(RangeType.POSITIVE);

		NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.CROSS_SECTION_GRID_Y_AXIS_TITLE));
//		yAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getNumberInstance()));
		yAxis.setRangeType(RangeType.POSITIVE);

		int leftBankPosition = 0, rightBankPosition = 0, waterDepth = 0;

		try {
			leftBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue();
			rightBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue();
			waterDepth = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_WATER_DEPTH).intValue();
		} catch (Exception e) {
		}

		mXYZDataSet = constructMyXYZDataSet(leftBankPosition, rightBankPosition, 0, waterDepth);
		mTheChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, new XYPlot(mXYZDataSet, xAxis, yAxis, null), false);
		mChartPanel = constructColourCodedChartPanel(this, mTheChart, mXYZDataSet, steps, legendText, dataIsVectorInY);
		mTheChart.getXYPlot().setRenderer(mChartPanel.getRenderer());
	
		int x = 0;
		int y = 0;
		add(mChartPanel.getGUI(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		
		mTheChart.getXYPlot().getDomainAxis().configure();
		mTheChart.getXYPlot().getRangeAxis().configure();

		validate();
		updateDisplay();
	}
	
	/**
	 * Constructs the MyXYZDataSet to use for this display the
	 * @param leftBankPosition
	 * @param rightBankPosition
	 * @param bedPosition
	 * @param waterDepth
	 * @return The MyXYZDataSet to use for this display the
	 */
	protected MyXYZDataSet constructMyXYZDataSet(int leftBankPosition, int rightBankPosition, int bedPosition, int waterDepth) {
		return new MyXYZDataSet(mMean, mScalerForDimensionlessValues, leftBankPosition, rightBankPosition, bedPosition, waterDepth);
	}

	/**
	 * Constructs the chart panel to show in this display
	 * @param theChart The chart to show in the chart panel
	 * @param xyzDataSet The dataset to display in the graph
	 * @param steps The number of steps to use in the colour coding
	 * @param legendText The legend text for the graph
	 * @return The ColourCodedChartPanel for this display
	 */
	protected ColourCodedChartPanel constructColourCodedChartPanel(final MAJFCStackedPanelWithFrame frameParent, JFreeChart theChart, MyXYZDataSet xyzDataSet, int steps, String legendText, boolean dataIsVectorInY) {
		return new ACSColourCodedChartPanel(this, theChart, xyzDataSet, 10, 10, steps, legendText, dataIsVectorInY);
	}
	
	protected class ACSColourCodedChartPanel extends ColourCodedChartPanel {
		protected ACSColourCodedChartPanel(MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYZDataSet scaleableDataSet, double nonDimensionlessBlockHeight, double nonDimensionlessBlockWidth, int steps, String legendText, boolean dataIsVectorInY) {
			super(mDataSetId, holderFrame, theChart, scaleableDataSet, nonDimensionlessBlockHeight, nonDimensionlessBlockWidth, steps, legendText, dataIsVectorInY);
		}
		
		@Override
		protected void showVerticalSectionGraph() {
			showTheVerticalSectionGraph();
		}

		@Override
		protected void showHorizontalSectionGraph() {
			showTheHorizontalSectionGraph();
		}

		@Override
		protected void showDepthAveragedGraph() {
			if (mDepthAveragedGraph == null) {
				Vector<String> graphLabels = getDepthAveragedGraphLabels();

				if (graphLabels == null || graphLabels.size() != 3) {
					return;
				}
				
				MAJFCTabbedPanel tabbedPanel = new MAJFCTabbedPanel();
				tabbedPanel.addTab(DAStrings.getString(DAStrings.POPULATED_CELL_DEPTH_AVERAGED_GRAPH_LABEL), new DepthAveragedGraph(mDataSetId, getHolderFrame(), getXYZDataSet(), true, graphLabels.elementAt(0), graphLabels.elementAt(1), graphLabels.elementAt(2), null, false));
				tabbedPanel.addTab(DAStrings.getString(DAStrings.EXTRAPOLATED_DEPTH_AVERAGED_GRAPH_LABEL), new DepthAveragedGraph(mDataSetId, getHolderFrame(), getXYZDataSet(), false, graphLabels.elementAt(0), graphLabels.elementAt(1), graphLabels.elementAt(2), null, false));
				
				mDepthAveragedGraph = new DepthAveragedGraphFrame(mDataSetId, getHolderFrame(), tabbedPanel, new WindowAdapter() {
					@Override
					/**
					 * WindowListener implementation
					 */
					public void windowClosing(WindowEvent theEvent) {
						mDepthAveragedGraph = null;
					}
				});
			}
		}
	}
	
	/**
	 * Helper class
	 * 
	 */
	private class DepthAveragedGraphFrame extends MAJFCStackedPanelWithFrame {
		public DepthAveragedGraphFrame(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, MAJFCTabbedPanel tabbedPanel, WindowListener windowListener) {
			super(parent, new GridBagLayout());

			add(tabbedPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

			validate();
			showInFrame("blah").addWindowListener(windowListener);
		}
	}
	
	@Override
	public void updateDisplay() {
		try {
			setupData();
			mChartPanel.updateDisplay();
		} catch (NullPointerException e) {
		}
	}
	
	/**
	 * Gets the preferred size
	 * @return The preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width * 0.9);
		int height = (int) width/4;

		return new Dimension(width, height);
	}

	/**
	 * Gets a copy of the data array
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Vector<Double[]> getDataArray() {
		return (Vector<Double[]>) mData.clone();
	}
	
	/**
	 * Sets up the values in the data array used for the XYZDataset implementation
	 * @param dataSetId The id of the data set to use data from
	 * @param sortedYCoords The sorted y-coordinates for the data set
	 * @param sortedZCoords The sorted z-coordinates for the data set - a set of vectors, each containing the sorted z-coordinates for the points
	 * which exist at the y-coordinate at the corresponding position in sortedYCoords
	 * @param data The data used by the XYZDataset implementation. A vector of Double[3], with each array holding a y-coordinate, z-coordinate and data value.
	 * The indices for each of these are specified by yCoordIndexInDataArray, zCoordIndexInDataArray and dataIndexInDataArray 
	 * @param yCoordIndexInDataArray See above
	 * @param zCoordIndexInDataArray See above
	 * @param dataIndexInDataArray See above
	 */
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex DPSIndex, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoords, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) {
		int numberOfYCoords = sortedYCoords.size();
		Vector<Double> dataValues = new Vector<Double>(1000);
		data.removeAllElements();
		
		for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
			Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
			Vector<Integer> zCoords = sortedZCoords.elementAt(yCoordIndex);
			int numberOfVelocities = zCoords.size();
			
			for (int zCoordIndex = 0; zCoordIndex < numberOfVelocities; ++zCoordIndex) {
				Integer zCoord = zCoords.elementAt(zCoordIndex);
				Double[] datum = new Double[3];
				
				datum[yCoordIndexInDataArray] = yCoord.doubleValue();
				datum[zCoordIndexInDataArray] = zCoord.doubleValue();
		
				try {
					datum[dataIndexInDataArray] = getDatumAt(dataSetId, DPSIndex, yCoord, zCoord);
					dataValues.add(datum[dataIndexInDataArray]);
				} catch (BackEndAPIException theException) {
					
				}
				
				data.add(datum);
			}
		}
		
		mMean = MAJFCMaths.mean(dataValues);
	}
	
	/**
	 * Update the data array
	 */
	protected void updateDataArray() {
		prepareDataArray(mDataSetId, mDPSIndex, mSortedYCoords, mSortedZCoords, mData, Y_COORD_INDEX, Z_COORD_INDEX, DATA_INDEX);
		updateDisplay();
	}

	/**
	 * Gets the datum to be placed in the "z" part of the data array for the given coordinates.
	 * Simply returns the value for the specified DPSIndex at the specified coordinates in the specified data set.
	 * Override this if something vaguely clever has to be done.
	 * @param dataSetId The id of the data set to use data from
	 * @param dpsIndex The DataPointSummaryIndex of the data to use
	 * @param yCoord The y-coordinate
	 * @param zCoord The z-coordinate
	 * @return
	 */
	protected double getDatumAt(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex dpsIndex, int yCoord, int zCoord) throws BackEndAPIException {
		return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, dpsIndex);
	}
	
	/**
	 * Gets the XYZDataSet used for this display
	 * @return
	 */
	protected ScaleableXYZDataSet getXYZDataSet() {
		return mChartPanel.getXYZDataSet();
	}	
	/**
	 * Gets the chart panel used for this display
	 * @return
	 */
	protected ColourCodedChartPanel getChartPanel() {
		return mChartPanel;
	}
	
	protected class MyXYZDataSet extends ScaleableXYZDataSet {
		public MyXYZDataSet(double mean, double scalerForDimensionlessValues, int unscaledXMin, int unscaledXMax, int unscaledYMin, int unscaledYMax) {
			super(mean, scalerForDimensionlessValues, unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}
	
		@Override
		/**
		 * ScaleableXYDataSet implementation
		 */
		public int getItemCount(int series) {
			return mData.size();
		}
	
		@Override
		/**
		 * ScaleableXYDataSet implementation
		 */
		public double getTheXValue(int series, int item) {
			return mData.elementAt(item)[Y_COORD_INDEX];
		}
	
		@Override
		/**
		 * ScaleableXYDataSet implementation
		 */
		public double getTheYValue(int series, int item) {
			return mData.elementAt(item)[Z_COORD_INDEX];
		}

		@Override
		/**
		 * XYZDataset implementation
		 */
		public double getTheZValue(int series, int item) {
			try {
				return mData.elementAt(item)[DATA_INDEX];
			} catch (NullPointerException e) {
				MAJFCLogger.log("" + series + ' ' + item);
				return Double.NaN;
			}
		}
	}
}
