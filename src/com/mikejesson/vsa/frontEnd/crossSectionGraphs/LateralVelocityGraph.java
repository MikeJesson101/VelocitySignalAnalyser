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

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class LateralVelocityGraph extends AbstractLateralVectorGraph {
	private double mScaleFactor;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public LateralVelocityGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent) throws HeadlessException {
		super(dataSetId, parent, DAStrings.getString(DAStrings.LATERAL_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.CROSS_SECTION_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.CROSS_SECTION_GRID_Y_AXIS_TITLE));
	}

	@Override
	/**
	 * @see AbstractCrossSectionGraph#prepareDataArray
	 */
	protected void prepareDataArray() {
		mLateralVectors = new double[mCoords.length][3];
		double crossSectionMeanVelocity = DAFrame.getFrame().getChannelMeanVelocityForScaling(mDataSetId);
		mScaleFactor = 100;//.mTheChart.getXYPlot().get.getDomainAxis().
		DataPointSummaryIndex yVelocityIndex = BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY;
		DataPointSummaryIndex zVelocityIndex = BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY;

		for (int i = 0; i < mCoords.length; ++i) {
			try {
				mLateralVectors[i][Y_COMPONENT_INDEX] = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[i][Y_COORD_INDEX], mCoords[i][Z_COORD_INDEX], yVelocityIndex)/crossSectionMeanVelocity;
				mLateralVectors[i][Z_COMPONENT_INDEX] = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[i][Y_COORD_INDEX], mCoords[i][Z_COORD_INDEX], zVelocityIndex)/crossSectionMeanVelocity;
				mLateralVectors[i][MAGNITUDE_INDEX] = mScaleFactor * Math.sqrt(Math.pow(mLateralVectors[i][Y_COMPONENT_INDEX], 2) + Math.pow(mLateralVectors[i][Z_COMPONENT_INDEX], 2));
			} catch (BackEndAPIException theException) {
				mLateralVectors[i][Y_COMPONENT_INDEX] = 0;
				mLateralVectors[i][Z_COMPONENT_INDEX] = 0;
				mLateralVectors[i][MAGNITUDE_INDEX] = 0;
			}
		}
	}
}
