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

import java.util.Enumeration;
import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class DepthAveragedReynoldsStressGraph extends AbstractCrossSectionGraph {
	private final int U_PRIME_V_PRIME_MEAN_INDEX = 1;
	private final int U_PRIME_W_PRIME_MEAN_INDEX = 2;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public DepthAveragedReynoldsStressGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent) throws HeadlessException {
		super(parent, new Object[] {dataSetId}, DAStrings.getString(DAStrings.DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE), null);
	}
// TODO Add derivatives
	@Override
	/**
	 * @see AbstractCrossSectionGraph#prepareDataArray
	 */
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoords) {
		mData = new double[sortedYCoords.size()][3];
		Enumeration<Integer> yCoords = sortedYCoords.elements();
		Enumeration<Vector<Integer>> zCoordVectors = sortedZCoords.elements();
		
		for (int i = 0; i < mData.length; ++i) {
			double uPrimevPrimeSum = 0;
			double uPrimewPrimeSum = 0;
			double crossSectionMeanVelocitySquared = Math.pow(DAFrame.getFrame().getChannelMeanVelocityForScaling(dataSetId), 2);
			int yCoord = yCoords.nextElement();
			Vector<Integer> zCoords = zCoordVectors.nextElement();
			int numberOfDataItems = zCoords.size();
			
			for (int j = 0; j < numberOfDataItems; ++j) {
				int zCoord = zCoords.elementAt(j);

				try {
					uPrimevPrimeSum += DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN);
					uPrimewPrimeSum += DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN);
				} catch (BackEndAPIException theException) {

				}
			}

			try {
				double fluidDensity = DAFrame.getBackEndAPI().getConfigData(dataSetId).get(BackEndAPI.DSC_KEY_FLUID_DENSITY);
				mData[i][Y_COORD_INDEX] = yCoord;
				mData[i][U_PRIME_V_PRIME_MEAN_INDEX] = -fluidDensity * uPrimevPrimeSum/numberOfDataItems/crossSectionMeanVelocitySquared;
				mData[i][U_PRIME_W_PRIME_MEAN_INDEX] = -fluidDensity * uPrimewPrimeSum/numberOfDataItems/crossSectionMeanVelocitySquared;
			} catch (BackEndAPIException theException) {
				mData[i][U_PRIME_V_PRIME_MEAN_INDEX] = -999;
				mData[i][U_PRIME_W_PRIME_MEAN_INDEX] = -999;
			}

		}
	}
	
	@Override
	/**
	 * XYDataset implementation
	 */
	public double getYValue(int series, int item) {
		switch (series) {
			case 0:
				return mData[item][U_PRIME_V_PRIME_MEAN_INDEX];
			case 1:
				return mData[item][U_PRIME_W_PRIME_MEAN_INDEX];
			default:
				return -99999;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public Comparable getSeriesKey(int series) {
		switch (series) {
			case 0:
				return DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_LEGEND_TEXT);
			case 1:
				return DAStrings.getString(DAStrings.HORIZONTAL_REYNOLDS_STRESS_LEGEND_TEXT);
			default:
				return "";
		}
	}

}
