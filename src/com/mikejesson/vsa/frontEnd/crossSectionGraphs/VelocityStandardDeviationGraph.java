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
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CrossSectionDataIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class VelocityStandardDeviationGraph extends AbstractCrossSectionColourCodedGraph {
	private SectionGraph mVerticalSectionVelocityStDevGraph;

	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public VelocityStandardDeviationGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex theStDevDataPointSummaryIndex, String chartTitle, String legendText) throws HeadlessException {
		super(dataSetId, parent, 50, theStDevDataPointSummaryIndex, chartTitle, legendText, false);
	}
	
	/**
	 * Sets up the values in the data array, calculating the depth-averaged velocity at each y-coordinate
	 * @see AbstractCrossSectionColourCodedGraph#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, DataPointSummaryIndex, int, int, int)
	 */
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoords, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) {
		int numberOfYCoords = sortedYCoords.size();
		double meanVelocity = 1.0;
		CrossSectionDataIndex meanVelocityIndex = BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_U_VELOCITY; 
		
		if (mDPSIndex.equals(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_ST_DEV)) {
			meanVelocityIndex = BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_V_VELOCITY;
		} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_ST_DEV)) {
			meanVelocityIndex = BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_W_VELOCITY;
		}

		try {
			meanVelocity = BackEndAPI.getBackEndAPI().getCrossSectionDataField(dataSetId, meanVelocityIndex);
		} catch (BackEndAPIException theException) {
			MAJFCLogger.log(theException.getMessage());
			theException.printStackTrace();
		}

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
					double pointStDev = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, mDPSIndex);
					datum[dataIndexInDataArray] = pointStDev/meanVelocity;
				} catch (BackEndAPIException theException) {
					
				}
				
				data.add(datum);
			}
		}
	}

	@Override
	protected void showTheVerticalSectionGraph() {
		String gridTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_U_ST_DEV_GRID_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_U_ST_DEV_GRID_X_AXIS_TITLE);
		String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_U_ST_DEV_GRID_Y_AXIS_TITLE);
		String legendText = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_U_ST_DEV_GRID_LEGEND_KEY_LABEL);
		
		if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)) {
		} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)) {
			gridTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_V_ST_DEV_GRID_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_V_ST_DEV_GRID_X_AXIS_TITLE);
			yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_V_ST_DEV_GRID_Y_AXIS_TITLE);
			legendText = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_V_ST_DEV_GRID_LEGEND_KEY_LABEL);			
		} else if (mDPSIndex.equals(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)) {
			gridTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_W_ST_DEV_GRID_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_W_ST_DEV_GRID_X_AXIS_TITLE);
			yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_W_ST_DEV_GRID_Y_AXIS_TITLE);
			legendText = DAStrings.getString(DAStrings.VERTICAL_VELOCITY_W_ST_DEV_GRID_LEGEND_KEY_LABEL);			
		}
		
		if (mVerticalSectionVelocityStDevGraph == null) {
			mVerticalSectionVelocityStDevGraph = new SectionGraph(this, SectionGraph.VERTICAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
				@Override
				/**
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mVerticalSectionVelocityStDevGraph = null;
				}
			});
		} else {
			mVerticalSectionVelocityStDevGraph.addOrRemoveSeries(307);
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
