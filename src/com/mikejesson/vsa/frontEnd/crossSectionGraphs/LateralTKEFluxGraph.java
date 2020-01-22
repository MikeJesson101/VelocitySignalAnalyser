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
public class LateralTKEFluxGraph extends AbstractLateralVectorGraph {
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public LateralTKEFluxGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent) throws HeadlessException {
		super(dataSetId, parent, 10, DAStrings.getString(DAStrings.LATERAL_TKE_FLUX_GRID_TITLE), DAStrings.getString(DAStrings.CROSS_SECTION_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.CROSS_SECTION_GRID_Y_AXIS_TITLE));
	}

	@Override
	/**
	 * @see AbstractCrossSectionGraph#prepareDataArray
	 */
	protected void prepareDataArray() {
		mLateralVectors = new double[mCoords.length][3];
		DataPointSummaryIndex yFluxIndex = BackEndAPI.DPS_KEY_V_TKE_FLUX;
		DataPointSummaryIndex zFluxIndex = BackEndAPI.DPS_KEY_W_TKE_FLUX;
		double meanMagnitudeSum = 0;
		
		for (int i = 0; i < mCoords.length; ++i) {
			try {
				mLateralVectors[i][Y_COMPONENT_INDEX] = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[i][Y_COORD_INDEX], mCoords[i][Z_COORD_INDEX], yFluxIndex);
				mLateralVectors[i][Z_COMPONENT_INDEX] = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[i][Y_COORD_INDEX], mCoords[i][Z_COORD_INDEX], zFluxIndex);
				mLateralVectors[i][MAGNITUDE_INDEX] = Math.sqrt(Math.pow(mLateralVectors[i][Y_COMPONENT_INDEX], 2) + Math.pow(mLateralVectors[i][Z_COMPONENT_INDEX], 2));
			} catch (BackEndAPIException theException) {
				mLateralVectors[i][Y_COMPONENT_INDEX] = 0;
				mLateralVectors[i][Z_COMPONENT_INDEX] = 0;
				mLateralVectors[i][MAGNITUDE_INDEX] = 0;
			}
			
			meanMagnitudeSum += mLateralVectors[i][MAGNITUDE_INDEX]; 
		}

		// Scale the magnitudes
		double meanMagnitude = meanMagnitudeSum/mCoords.length;
		for (int i = 0; i < mCoords.length; ++i) {
			mLateralVectors[i][MAGNITUDE_INDEX] /= meanMagnitude;
		}
	}
}
