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


import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;

import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
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
public class ScaledDepthAveragedUVGraph extends AbstractCrossSectionDerivativeGraph {
	private DataSetConfig mDataSetConfig;
	private double mScaleFactor;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public ScaledDepthAveragedUVGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent) throws HeadlessException {
		super(parent, new Object[] { dataSetId }, DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_GRAPH_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_GRAPH_X_AXIS_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_GRAPH_Y_AXIS_TITLE), null);
	}
	
	/**
	 * Gets the preferred size
	 * @return The preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width * 0.8);
		int height = (int) width/2;

		return new Dimension(width, height);
	}
	
	@Override
	/**
	 * Does initialisation stuff required as first call in constructor. Any override of this method should call the superclass
	 * version before anything else
	 * @param initialisationObjects Any objects needed for initialisation. First should be the unique data set id of the data
	 * set displayed in this graph.
	 */
	protected void initialise(Object[] initialisationObjects) {
		try {
			mDataSetConfig = DAFrame.getBackEndAPI().getConfigData((AbstractDataSetUniqueId) initialisationObjects[0]);
		} catch (Exception e) {
		}		
		
		super.initialise(initialisationObjects);
	}

	@Override
	/**
	 * The calculation step in which the mData value is set from prepareDataArray
	 * @see frontEnd.crossSectionGraphs.AbstractCrossSectionGraph#prepareDataArrayStep
	 */
	protected void prepareDataArrayStep(AbstractDataSetUniqueId dataSetId, int dataIndex, int yCoord, Vector<Integer> zCoordsList) {
		int numberOfVelocities = zCoordsList.size();
		
		Vector<Double> uTimesV = new Vector<Double>(numberOfVelocities);
		for (int j = 0; j < numberOfVelocities; ++j) {
			int zCoord = zCoordsList.elementAt(j);
				
			try {
				uTimesV.add(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY) * DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY));
			} catch (BackEndAPIException theException) {
					
			}
		}

		// Limit upper cell size by standard cell size rather than water depth 
//		double depthAveragedUTimesV = MAJFCMaths.integralAverage(uTimesV, zCoordsList, 0, mWaterDepth);
		int topZ = zCoordsList.lastElement();
		int penultimateZ = numberOfVelocities > 1 ? zCoordsList.elementAt(numberOfVelocities - 2) : topZ; 
		int upperBound = zCoordsList.lastElement() + (topZ - penultimateZ)/2;
		int lowerBound = mDataSetConfig.getBoundaryZAt(yCoord);
		double depthAveragedUTimesV = MAJFCMaths.integralAverage(uTimesV, zCoordsList, lowerBound, upperBound);
		double crossSectionMeanVelocity = DAFrame.getFrame().getChannelMeanVelocityForScaling(dataSetId);

		// Scale factor roe * H (water density * water depth)
		double scaleFactor = ((mDataSetConfig.get(BackEndAPI.DSC_KEY_WATER_DEPTH) - lowerBound)/(mDataSetConfig.get(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR))) * DADefinitions.WATER_DENSITY_RHO;

		mData[dataIndex][Y_COORD_INDEX] = yCoord;
		mData[dataIndex][DATUM_INDEX] = scaleFactor * depthAveragedUTimesV/crossSectionMeanVelocity;
	}
}
