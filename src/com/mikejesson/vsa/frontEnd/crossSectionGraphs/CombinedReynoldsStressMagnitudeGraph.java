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

import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class CombinedReynoldsStressMagnitudeGraph extends AbstractCrossSectionColourCodedGraph {
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 * @throws BackEndAPIException 
	 */
	public CombinedReynoldsStressMagnitudeGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex theReynoldsStressDataPointSummaryIndex, String title, String legendText) throws HeadlessException, BackEndAPIException {
		super(dataSetId, parent, 50, theReynoldsStressDataPointSummaryIndex, BackEndAPI.getBackEndAPI().getCrossSectionDataField(dataSetId, BackEndAPI.CSD_KEY_MEAN_BOUNDARY_SHEAR), title, legendText, false);
		showInFrame(DAStrings.getString(DAStrings.COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE));
	}

	/**
	 * Gets the specified datum for the specified point from the specified data set
	 * {@link AbstractCrossSectionColourCodedGraph#getDatumAt(AbstractDataSetUniqueId, DataPointSummaryIndex, int, int)}
	 */
	protected double getDatumAt(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex dpsIndex, int yCoord, int zCoord) throws BackEndAPIException {
		double horizontalRS = - DADefinitions.WATER_DENSITY_RHO * DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN);
		double verticalRS = - DADefinitions.WATER_DENSITY_RHO * DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN);
		
		return Math.sqrt(Math.pow(horizontalRS, 2) + Math.pow(verticalRS, 2));
	}

	@Override
	protected void showTheVerticalSectionGraph() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void showTheHorizontalSectionGraph() {
	}

	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		String gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_X_AXIS_TITLE);
		String yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_Y_AXIS_TITLE);

		Vector<String> graphLabels = new Vector<String>(3);
		graphLabels.add(gridTitle);
		graphLabels.add(xAxisTitle);
		graphLabels.add(yAxisTitle);
			
		return graphLabels;
	}
}

