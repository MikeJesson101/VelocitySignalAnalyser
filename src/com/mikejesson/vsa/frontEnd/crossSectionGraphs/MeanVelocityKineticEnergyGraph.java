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
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;




/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class MeanVelocityKineticEnergyGraph extends AbstractCrossSectionColourCodedGraph {
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public MeanVelocityKineticEnergyGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex dpsIndex, String graphTitle, String legendText) throws HeadlessException {
		super(dataSetId, parent, 10, dpsIndex, graphTitle, legendText, false);
	}
	
	@Override
	/**
	 * {@link AbstractCrossSectionColourCodedGraph#getDatumAt(AbstractDataSetUniqueId, DataPointSummaryIndex, int, int) }
	 */
	protected double getDatumAt(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex dpsIndex, int yCoord, int zCoord) throws BackEndAPIException {
		return (1d/2) * DADefinitions.WATER_DENSITY_RHO * Math.pow(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, mDPSIndex), 2);
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
		return null;		
	}
}
