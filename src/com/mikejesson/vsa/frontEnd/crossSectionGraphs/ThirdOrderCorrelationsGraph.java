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
public class ThirdOrderCorrelationsGraph extends AbstractCrossSectionColourCodedGraph {
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public ThirdOrderCorrelationsGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex dpsIndex, String frameTitle, String graphTitle, String legendText) throws HeadlessException {
		super(dataSetId, parent, 10, dpsIndex, graphTitle, legendText, false);
		showInFrame(frameTitle);
	}

	@Override
	protected void showTheVerticalSectionGraph() {
		if (mVerticalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL);
			
			if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN)) {
			}  else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN)) {
				gridTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_W_PRIME_GRID_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_W_PRIME_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_W_PRIME_GRID_Y_AXIS_TITLE);
				legendText = DAStrings.getString(DAStrings.VERTICAL_U_PRIME_W_PRIME_GRID_LEGEND_KEY_LABEL);
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
	}

	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		return null;		
	}
}
