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
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;




/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class TKEFluxGraph extends AbstractCrossSectionColourCodedGraph {
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public TKEFluxGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex dpsIndex, String graphTitle) throws HeadlessException {
		super(dataSetId, parent, 50, dpsIndex, graphTitle, DAStrings.getString(DAStrings.TKE_FLUX_GRAPH_LEGEND_TEXT), dpsIndex.equals(BackEndAPI.DPS_KEY_V_TKE_FLUX));
	}

	@Override
	protected void showTheVerticalSectionGraph() {
		if (mVerticalSectionGraph == null) {
			String gridTitle = "", xAxisTitle = "", yAxisTitle = "", legendText = "";

			if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_TKE_FLUX)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL);
			} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_V_TKE_FLUX)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL);
			} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_W_TKE_FLUX)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL);
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
		} else {
			mVerticalSectionGraph.addOrRemoveSeries(307);
		}
	}

	@Override
	protected void showTheHorizontalSectionGraph() {
		if (mHorizontalSectionGraph == null) {
			String gridTitle = "", xAxisTitle = "", yAxisTitle = "", legendText = "";

			if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_TKE_FLUX)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL);
			} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_V_TKE_FLUX)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL);
			} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_W_TKE_FLUX)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL);
			}
			
			mHorizontalSectionGraph = new SectionGraph(this, SectionGraph.HORIZONTAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
				@Override
				/**	
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mHorizontalSectionGraph = null;
				}
			});
		} else {
			mHorizontalSectionGraph.addOrRemoveSeries(307);
		}	
	}

	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		String gridTitle = "", xAxisTitle = "", yAxisTitle = "";

		if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_TKE_FLUX)) {
			gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_U_TKE_FLUX_GRID_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_U_TKE_FLUX_GRID_X_AXIS_TITLE);
			yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_U_TKE_FLUX_GRID_Y_AXIS_TITLE);
		} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_V_TKE_FLUX)) {
			gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_V_TKE_FLUX_GRID_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_V_TKE_FLUX_GRID_X_AXIS_TITLE);
			yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_V_TKE_FLUX_GRID_Y_AXIS_TITLE);
		} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_W_TKE_FLUX)) {
			gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_W_TKE_FLUX_GRID_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_W_TKE_FLUX_GRID_X_AXIS_TITLE);
			yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_W_TKE_FLUX_GRID_Y_AXIS_TITLE);
		}

		Vector<String> graphLabels = new Vector<String>(3);
		graphLabels.add(gridTitle);
		graphLabels.add(xAxisTitle);
		graphLabels.add(yAxisTitle);
		
		return graphLabels;
	}
}
