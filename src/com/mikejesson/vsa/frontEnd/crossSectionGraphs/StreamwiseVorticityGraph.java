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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools.MAJFCToolsException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class StreamwiseVorticityGraph extends AbstractCrossSectionColourCodedGraph {
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public StreamwiseVorticityGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent) throws HeadlessException {
		super(dataSetId, parent, 50, null, DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_GRID_TITLE), DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_GRID_LEGEND_TEXT), true);
		showInFrame(DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_GRID_FRAME_TITLE)  + " (" + mDataSetId.getFullDisplayString() + ')');
	}
	
	/**
	 * Sets up the values in the data array, calculating the depth-averaged velocity at each y-coordinate
	 * @see AbstractCrossSectionColourCodedGraph#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, DataPointSummaryIndex, int, int, int)
	 */
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) {
		Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedDWByDyMinusDVByDz;
		
		try {
			yThenZIndexedDWByDyMinusDVByDz = calculatePerkinsEpsilon(dataSetId, sortedYCoords, sortedZCoordsSets);
		} catch (BackEndAPIException e) {
			e.printStackTrace();
			return;
		} catch (MAJFCToolsException e) {
			e.printStackTrace();
			return;
		}
		
		// Now we have y-indexed then z-indexed lookup of E = (dW/dy - dV/dz)
		int numberOfYCoords = sortedYCoords.size();
		Vector<Double> dataValues = new Vector<Double>(1000);
		
		for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
			Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
			Vector<Integer> sortedZCoords = sortedZCoordsSets.elementAt(yCoordIndex);
			int numberOfZCoords = sortedZCoords.size();
			Hashtable<Integer , Double> zIndexedDWByDyMinusDVByDz = yThenZIndexedDWByDyMinusDVByDz.get(yCoord);
			
			for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
				Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
				Double[] datum = new Double[3];
				
				datum[yCoordIndexInDataArray] = yCoord.doubleValue();
				datum[zCoordIndexInDataArray] = zCoord.doubleValue();
				datum[dataIndexInDataArray] = zIndexedDWByDyMinusDVByDz.get(zCoord);
				dataValues.add(datum[dataIndexInDataArray]);
				
				data.add(datum);
			}
		}
		
		mMean = MAJFCMaths.mean(dataValues);
	}

	/**
	 * Calculates the epsilon term in the vorticity equation as in Perkins' 1970 paper, The Formation of Streamwise Vorticity in Turbulent Flow,
	 * Journal of Fluid Mechanics, 44(4)
	 * @param dataSetId
	 * @param sortedYCoords
	 * @param sortedZCoordsSets
	 * @param data
	 * @param yCoordIndexInDataArray
	 * @param zCoordIndexInDataArray
	 * @param dataIndexInDataArray
	 * @return A y-coordinate then z-coordinate indexed lookup of epsilon values
	 * @throws BackEndAPIException
	 * @throws MAJFCToolsException
	 */
	public static Hashtable<Integer, Hashtable<Integer, Double>> calculatePerkinsEpsilon(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets) throws BackEndAPIException, MAJFCToolsException {
		int numberOfYCoords = sortedYCoords.size();
		Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedDWByDyMinusDVByDz = new Hashtable<Integer, Hashtable<Integer,Double>>(100);

		// Calculate dV/dz
		for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
			Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
			Vector<Integer> sortedZCoords = sortedZCoordsSets.elementAt(yCoordIndex);
			int numberOfZCoords = sortedZCoords.size();
			Vector<Double> capitalVsForThisY = new Vector<Double>(numberOfZCoords);
			
			for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
				Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
				capitalVsForThisY.add(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY));
			}
			
			Vector<Double> dVByDzsForThisY = MAJFCMaths.derivativesIntegerPositions(capitalVsForThisY, sortedZCoords);
			
			// Put these dV/dzs into a lookup for future use
			Hashtable<Integer , Double> zIndexedDWByDyMinusDVByDz = new Hashtable<Integer, Double>(20); 
			yThenZIndexedDWByDyMinusDVByDz.put(yCoord, zIndexedDWByDyMinusDVByDz);

			// sortedZCoords and dVByDzsForThisY should correspond, so just us zCoordIndex
			for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
				Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
				zIndexedDWByDyMinusDVByDz.put(zCoord, dVByDzsForThisY.elementAt(zCoordIndex));
			}
		}
		
		// Calculate dW/dy
		Hashtable<Integer, Vector<Integer>> zCoordIndexedLookup = DAFrame.getBackEndAPI().getZCoordIndexedSortedDataPointCoordinates(dataSetId);
		
		// ...collect all the W values for each z-coordinate then do the d/dy.
		Enumeration<Integer> zCoordsEnum = zCoordIndexedLookup.keys();
		
		// Go through all the z-coordinates
		while (zCoordsEnum.hasMoreElements()) {
			Integer zCoord = zCoordsEnum.nextElement();
			Vector<Integer> yCoordsForThisZ = zCoordIndexedLookup.get(zCoord);
			Vector<Double> capitalWsForThisZ = new Vector<Double>(1000);
			numberOfYCoords = yCoordsForThisZ.size();
			
			// Go through the y-coordinates for which data points exist for this z-coordinate
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = yCoordsForThisZ.elementAt(yCoordIndex);
				capitalWsForThisZ.add(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY));
			}
			
			// At this point we should have a full list of W values at this z-coordinate, corresponding
			// to the y-coordinates in yCoords
			Vector<Double> dWByDysForThisZ = MAJFCMaths.derivativesIntegerPositions(capitalWsForThisZ, yCoordsForThisZ);
			
			// Put these dW/dys into the lookup
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = yCoordsForThisZ.elementAt(yCoordIndex);
				Hashtable<Integer , Double> zIndexedDWByDyMinusDVByDz = yThenZIndexedDWByDyMinusDVByDz.get(yCoord);
				double dVByDz = zIndexedDWByDyMinusDVByDz.remove(zCoord);
				zIndexedDWByDyMinusDVByDz.put(zCoord, dWByDysForThisZ.elementAt(yCoordIndex) - dVByDz);
			}
		}
		
		return yThenZIndexedDWByDyMinusDVByDz;
	}
	
	@Override
	/**
	 * Gets the specified datum for the specified point from the specified data set
	 * {@link AbstractCrossSectionColourCodedGraph#getDatumAt(AbstractDataSetUniqueId, DataPointSummaryIndex, int, int)}
	 */
	protected double getDatumAt(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex dpsIndex, int yCoord, int zCoord) throws BackEndAPIException {
		return 0;
	}

	@Override
	protected void showTheVerticalSectionGraph() {
		if (mVerticalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_GRID_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_LEGEND_KEY_LABEL);

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
	}
	
	@Override
	protected Vector<String> getDepthAveragedGraphLabels() {
		String gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE);
		String yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE);

		Vector<String> graphLabels = new Vector<String>(3);
		graphLabels.add(gridTitle);
		graphLabels.add(xAxisTitle);
		graphLabels.add(yAxisTitle);
		
		return graphLabels;
	}
}
