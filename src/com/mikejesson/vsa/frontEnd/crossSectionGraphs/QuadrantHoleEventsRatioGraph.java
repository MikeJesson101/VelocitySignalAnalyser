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

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import org.jfree.chart.JFreeChart;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ColourCodedChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.InvertibleValueChartPanel;




/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class QuadrantHoleEventsRatioGraph extends AbstractCrossSectionColourCodedGraph {
	private boolean mScale;

	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public QuadrantHoleEventsRatioGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex dpsIndex, String frameTitle, String graphTitle, String legendText) throws HeadlessException {
		this(dataSetId, parent, dpsIndex, frameTitle, graphTitle, legendText, false);
	}
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public QuadrantHoleEventsRatioGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex dpsIndex, String frameTitle, String graphTitle, String legendText, boolean scale) throws HeadlessException {
		super(dataSetId, parent, new Object[] { scale }, 20, dpsIndex, graphTitle, legendText, false);//dpsIndex.equals(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO));
		showInFrame(frameTitle + '(' + mDataSetId.getFullDisplayString() + ')');
	}
	
	/**
	 * Does anything that needs doing before the main body of the parent constructor is called
	 * This should be called at start of any override method in child classes
	 */
	protected void initialise(Object[] initialisationObjects) {
		super.initialise(initialisationObjects);
		mScale = (Boolean) initialisationObjects[0];
	}

	@Override
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
		double datum = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, mDPSIndex);
		MyInvertibleRatioChartPanel chartPanel = (MyInvertibleRatioChartPanel) getChartPanel();
		
		if (chartPanel != null && chartPanel.displayInverted()) {
			datum = 1d/datum;
		}
		
		if (mScale == false) {
			return datum;
		}
		
		double stDevProduct;
		
		if (mDPSIndex.equals(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO)
				|| mDPSIndex.equals(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO)
				|| mDPSIndex.equals(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO)) {
			try {
				stDevProduct = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)
								* DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
			} catch (BackEndAPIException e) {
				e.printStackTrace();
				return -Double.MAX_VALUE;
			}
		} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO)
				|| mDPSIndex.equals(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO)
				|| mDPSIndex.equals(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO)) {
			try {
				stDevProduct = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)
								* DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
			} catch (BackEndAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -Double.MAX_VALUE;
			}
		} else {
			return -Double.MAX_VALUE;
		}
		
		return Math.abs((datum - 1) * stDevProduct);
	}
	
	@Override
	/**
	 * {@link AbstractCrossSectionColourCodedGraph#getColourCodedChartPanel(JFreeChart, MyXYZDataSet, int, String)}
	 */
	protected ColourCodedChartPanel constructColourCodedChartPanel(MAJFCStackedPanelWithFrame frameParent, JFreeChart theChart, MyXYZDataSet xyzDataSet, int steps, String legendText, boolean dataIsVectorInY) {
		return new MyInvertibleRatioChartPanel(mDataSetId, this, theChart, xyzDataSet, 10, 10, steps, legendText, dataIsVectorInY);
	}
	
	@Override
	protected void showTheVerticalSectionGraph() {
		if (mVerticalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL);
			
			mVerticalSectionGraph = new SectionGraph(this, SectionGraph.VERTICAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
				@Override
				/**	
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mVerticalSectionGraph = null;
				}
			});
		}else {
			mVerticalSectionGraph.addOrRemoveSeries(307);
		}
	}

	@Override
	protected void showTheHorizontalSectionGraph() {
		if (mHorizontalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL);
			
			mHorizontalSectionGraph = new SectionGraph(this, SectionGraph.HORIZONTAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
				@Override
				/**	
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mHorizontalSectionGraph = null;
				}
			});
		}else {
			mHorizontalSectionGraph.addOrRemoveSeries(307);
		}
	}

	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		return null;
	}
	
	private class MyInvertibleRatioChartPanel extends InvertibleValueChartPanel {
		public MyInvertibleRatioChartPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, JFreeChart theChart, MyXYZDataSet scaleableDataSet, int nonDimensionlessBlockHeight, int nonDimensionlessBlockWidth, int steps, String legendText, boolean dataIsVectorInY) {
			super(dataSetId, parent, theChart, scaleableDataSet, nonDimensionlessBlockHeight, nonDimensionlessBlockWidth, steps, legendText, dataIsVectorInY);
		}
		
		@Override
		protected void updateChartDataArray() {
			updateDataArray();
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
			
		}
	}
}
