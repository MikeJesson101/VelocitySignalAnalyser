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
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class ReynoldsStressGraph extends AbstractCrossSectionColourCodedGraph {
	private double mScaleFactor = 1d;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 * @throws BackEndAPIException 
	 */
	public ReynoldsStressGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, Boolean scaleByMinusWaterDensity, DataPointSummaryIndex theReynoldsStressDataPointSummaryIndex, String title, String legendText) throws HeadlessException, BackEndAPIException {
		this(dataSetId, parent, scaleByMinusWaterDensity, theReynoldsStressDataPointSummaryIndex, title, legendText, true);
	}
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 * @throws BackEndAPIException 
	 */
	public ReynoldsStressGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, Boolean scaleByMinusWaterDensity, DataPointSummaryIndex theReynoldsStressDataPointSummaryIndex, String title, String legendText, boolean showInFrame) throws HeadlessException, BackEndAPIException {
		super(dataSetId, parent, new Object[] { scaleByMinusWaterDensity }, 50, theReynoldsStressDataPointSummaryIndex, BackEndAPI.getBackEndAPI().getCrossSectionDataField(dataSetId, BackEndAPI.CSD_KEY_MEAN_BOUNDARY_SHEAR), title, legendText, theReynoldsStressDataPointSummaryIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN) | theReynoldsStressDataPointSummaryIndex.equals(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN));

		if (showInFrame) {
			showInFrame(title + '(' + mDataSetId.getFullDisplayString() + ')');
		}
	}

	/**
	 * Does anything that needs doing before the main body of the parent constructor is called
	 * This should be called at start of any override method in child classes
	 */
	protected void initialise(Object[] initialisationObjects) {
		super.initialise(initialisationObjects);
		
		if ((Boolean) initialisationObjects[0] == true) {
			try {
				double fluidDensity = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_FLUID_DENSITY);
				mScaleFactor = - fluidDensity;
			} catch (BackEndAPIException theException) {
				mScaleFactor = 1.0;
			}

		} else {
			mScaleFactor = 1.0;
		}
	}
	
	/**
	 * Gets the specified datum for the specified point from the specified data set
	 * {@link AbstractCrossSectionColourCodedGraph#getDatumAt(AbstractDataSetUniqueId, DataPointSummaryIndex, int, int)}
	 */
	protected double getDatumAt(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex dpsIndex, int yCoord, int zCoord) throws BackEndAPIException {
		return mScaleFactor * DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, mDPSIndex);
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
		if (mHorizontalSectionGraph == null) {
			mHorizontalSectionGraph = new SectionGraph(this, SectionGraph.HORIZONTAL_SECTION, mDataSetId, getXYZDataSet(), null, DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL),new WindowAdapter() {
				@Override
				/**
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mHorizontalSectionGraph = null;
				}
			});
		} else {
			//mHorizontalSectionVelocityGraph.addOrRemoveSeries(mClickEntityYCoord.intValue());
		}
	}

	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		String gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE);
		String yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE);

		if (mDPSIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN)) {
			gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_TITLE);
			xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE);
			yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE);
		}

		Vector<String> graphLabels = new Vector<String>(3);
		graphLabels.add(gridTitle);
		graphLabels.add(xAxisTitle);
		graphLabels.add(yAxisTitle);
		
		return graphLabels;
	}
}
