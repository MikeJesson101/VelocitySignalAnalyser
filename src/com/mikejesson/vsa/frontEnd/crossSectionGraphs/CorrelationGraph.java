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

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class CorrelationGraph extends AbstractCrossSectionColourCodedGraph {
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public CorrelationGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex theReynoldsStressDataPointSummaryIndex, String title, String legendText) throws HeadlessException {
		super(dataSetId, parent, 50, theReynoldsStressDataPointSummaryIndex, title, legendText, false);
		showInFrame(title);
	}

	/**
	 * Gets the specified datum for the specified point from the specified data set
	 * {@link AbstractCrossSectionColourCodedGraph#getDatumAt(AbstractDataSetUniqueId, DataPointSummaryIndex, int, int)}
	 */
	protected double getDatumAt(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex dpsIndex, int yCoord, int zCoord) throws BackEndAPIException {
		double sigma1 = 1d, sigma2 = 1d;
		
		if (dpsIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN)) {
			sigma1 = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
			sigma2 = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
		} else if (dpsIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN)) {
			sigma1 = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
			sigma2 = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
		} else if (dpsIndex.equals(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN)) {
			sigma1 = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
			sigma2 = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
		}
		
		return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, mDPSIndex) / (sigma1 * sigma2);
	}

	@Override
	protected void showTheVerticalSectionGraph() {
		if (mVerticalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.VERTICAL_UV_CORRELATION_GRAPH_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_UV_CORRELATION_GRAPH_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_UV_CORRELATION_GRAPH_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.VERTICAL_UV_CORRELATION_GRAPH_LEGEND_KEY_LABEL);
			
			if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN)) {
			}  else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_UW_CORRELATION_GRAPH_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_UW_CORRELATION_GRAPH_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_UW_CORRELATION_GRAPH_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_UW_CORRELATION_GRAPH_LEGEND_KEY_LABEL);
			}
			
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
			mHorizontalSectionGraph = new SectionGraph(this, SectionGraph.HORIZONTAL_SECTION, mDataSetId, getXYZDataSet(), null, DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL),new WindowAdapter() {
				@Override
				/**
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mHorizontalSectionGraph = null;
				}
			});
		} else {
			//mHorizontalSectionVelocityGraph.addOrRemoveSeries(mClickEntityYCoord.intValue());
		}
	}

	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		String gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_CORRELATION_GRAPH_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_CORRELATION_GRAPH_X_AXIS_TITLE);
		String yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_CORRELATION_GRAPH_Y_AXIS_TITLE);

		if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN)) {
			gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_UW_CORRELATION_GRAPH_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_UW_CORRELATION_GRAPH_X_AXIS_TITLE);
			yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_UW_CORRELATION_GRAPH_Y_AXIS_TITLE);
		}
		
		Vector<String> graphLabels = new Vector<String>(3);
		graphLabels.add(gridTitle);
		graphLabels.add(xAxisTitle);
		graphLabels.add(yAxisTitle);
		
		return graphLabels;
	}
}
