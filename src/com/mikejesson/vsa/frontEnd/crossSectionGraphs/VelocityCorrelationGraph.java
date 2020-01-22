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
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class VelocityCorrelationGraph extends AbstractCrossSectionColourCodedGraph {
	private Vector<Integer> mYCoords;
	private Vector<Integer> mZCoords;
	private Vector<Double> mCorrelations;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public VelocityCorrelationGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Double> correlations, String graphTitle) throws HeadlessException {
		super(dataSetId, parent, new Object[] {yCoords, zCoords, correlations}, 50, null, graphTitle, DAStrings.getString(DAStrings.VELOCITY_CORRELATION_GRAPH_LEGEND_TEXT), false);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initialise(Object[] initialisationObjects) {
		mYCoords = (Vector<Integer>) initialisationObjects[0];
		mZCoords = (Vector<Integer>) initialisationObjects[1];
		mCorrelations = (Vector<Double>) initialisationObjects[2];
	}
	
	@Override
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex DPSIndex, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoords, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) {
		// Bit of a bodge as we don't use the coords passed in
		int numberOfCoords = mYCoords.size();
		data.removeAllElements();
		
		for (int coordIndex = 1; coordIndex < numberOfCoords; ++coordIndex) {
			Integer yCoord = mYCoords.elementAt(coordIndex);
			Integer zCoord = mZCoords.elementAt(coordIndex);
			Double[] datum = new Double[3];
				
			datum[yCoordIndexInDataArray] = yCoord.doubleValue();
			datum[zCoordIndexInDataArray] = zCoord.doubleValue();
			datum[dataIndexInDataArray] = mCorrelations.elementAt(coordIndex - 1);
				
			data.add(datum);
		}
		
		mMean = MAJFCMaths.mean(mCorrelations);
	}
	
	@Override
	protected void showTheVerticalSectionGraph() {
		if (mVerticalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.VELOCITY_CORRELATION_GRAPH_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL);

			mVerticalSectionGraph = new SectionGraph(this, SectionGraph.VERTICAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
				@Override
				/**	
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mVerticalSectionGraph = null;
				}
			});
		} else {
			mVerticalSectionGraph.addOrRemoveSeries(307);
		}
	}

	@Override
	protected void showTheHorizontalSectionGraph() {
		if (mHorizontalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.VELOCITY_CORRELATION_GRAPH_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL);

			mVerticalSectionGraph = new SectionGraph(this, SectionGraph.HORIZONTAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
				@Override
				/**	
				 * WindowListener implementation
				 */
				public void windowClosing(WindowEvent theEvent) {
					mHorizontalSectionGraph = null;
				}
			});
		} else {
			mHorizontalSectionGraph.addOrRemoveSeries(307);
		}
	}

	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		String gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE);
		String yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE);

		Vector<String> graphLabels = new Vector<String>(3);
		graphLabels.add(gridTitle);
		graphLabels.add(xAxisTitle);
		graphLabels.add(yAxisTitle);
		
		return graphLabels;
	}
}
