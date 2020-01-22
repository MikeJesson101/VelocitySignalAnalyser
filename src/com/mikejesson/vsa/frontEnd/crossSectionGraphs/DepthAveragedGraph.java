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
import java.awt.event.WindowListener;

import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYZDataSet;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class DepthAveragedGraph extends AbstractCrossSectionDerivativeGraph {
	private ScaleableXYZDataSet mXYZDataSet;
	private Boolean mUseStandardCellSize;
	
	/**
	 * Constructor
	 * @param dataSetId
	 * @param parent
	 * @param dataSet
	 * @param useStandardCellSize
	 * @param title
	 * @param xAxisTitle
	 * @param yAxisTitle
	 * @param windowListener
	 * @throws HeadlessException
	 */
	public DepthAveragedGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, ScaleableXYZDataSet dataSet, Boolean useStandardCellSize, String title, String xAxisTitle, String yAxisTitle, WindowListener windowListener) throws HeadlessException {
		this(dataSetId, parent, dataSet, useStandardCellSize, title, xAxisTitle, yAxisTitle, windowListener, true);
	}
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public DepthAveragedGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, ScaleableXYZDataSet dataSet, Boolean useStandardCellSize, String title, String xAxisTitle, String yAxisTitle, WindowListener windowListener, boolean showFrame) throws HeadlessException {
		super(parent, new Object[] { dataSetId, dataSet, useStandardCellSize }, title, xAxisTitle, yAxisTitle, windowListener, showFrame);
	}

	@Override
	protected void initialise(Object[] initialisationObjects) {
		super.initialise(initialisationObjects);
		
		int i = 0;
		mXYZDataSet = (ScaleableXYZDataSet) initialisationObjects[++i];
		mUseStandardCellSize = (Boolean) initialisationObjects[++i];
	}

	@Override
	/**
	 * The calculation step in which the mData value is set from prepareDataArray
	 * @see frontEnd.crossSectionGraphs.AbstractCrossSectionGraph#prepareDataArrayStep
	 */
	protected void prepareDataArrayStep(AbstractDataSetUniqueId dataSetId, int dataIndex, int yCoord, Vector<Integer> zCoordsList) {
		int numberOfData = mXYZDataSet.getItemCount(0);
		zCoordsList.removeAllElements();
		
		Vector<Double> data = new Vector<Double>(numberOfData);
		for (int j = 0; j < numberOfData; ++j) {
			double datumYCoord = mXYZDataSet.getXValue(0, j); 
			
			if (datumYCoord != yCoord) {
				continue;
			}
			
			int zCoord = (int) mXYZDataSet.getYValue(0, j);
			data.add(mXYZDataSet.getZValue(0, j));
			zCoordsList.add(zCoord);
		}

		double depthAveragedValue = Double.NaN;
		try {
			DataSetConfig dataSetConfig = DAFrame.getBackEndAPI().getConfigData(dataSetId);
			int waterDepth = dataSetConfig.get(BackEndAPI.DSC_KEY_WATER_DEPTH).intValue();
			int lowerBound = mUseStandardCellSize == false ? dataSetConfig.getBoundaryZAt(yCoord)
					: Math.max(0, zCoordsList.firstElement().intValue() - (dataSetConfig.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT).intValue()/2));
			int upperBound = mUseStandardCellSize == false ? waterDepth
					: Math.min(waterDepth, zCoordsList.lastElement().intValue() + (dataSetConfig.get(BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT).intValue()/2));

			depthAveragedValue = MAJFCMaths.integralAverage(data, zCoordsList, lowerBound, upperBound);
		} catch (Exception e) {
		}

		mData[dataIndex][Y_COORD_INDEX] = yCoord;
		mData[dataIndex][DATUM_INDEX] = depthAveragedValue;
	}
}
