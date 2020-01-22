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

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.jfree.chart.axis.NumberAxis;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools.MAJFCToolsException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.XYDataSetAdapter;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractCrossSectionDerivativeGraph extends AbstractCrossSectionGraph {
	private double[][] mDerivativeData;
	private final int DERIVATIVE_SERIES_ID = 1;
	
	/**
	 * Constructor
	 * @param dataSetId The unique data set id of the data set displayed in this graph
	 * @param initialisationObjects Objects required to initialise before main constructor is called. First should be the 
	 * unique data set id of the data set displayed in this graph
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionDerivativeGraph(MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, String title, String xAxisTitle, String yAxisTitle, WindowListener windowListener) throws HeadlessException {
		this(parent, initialisationObjects, title, xAxisTitle, yAxisTitle, windowListener, true);
	}
	
	/**
	 * Constructor
	 * @param dataSetId The unique data set id of the data set displayed in this graph
	 * @param initialisationObjects Objects required to initialise before main constructor is called. First should be the 
	 * unique data set id of the data set displayed in this graph
	 * @param title The graph title
	 * @param xAxisTitle The x-axis title
	 * @param yAxisTitle The y-axis title
	 * @param windowListener The window listener for this display
	 * @param showFrame Set true to show this in its own frame
	 * @throws HeadlessException
	 */
	public AbstractCrossSectionDerivativeGraph(MAJFCStackedPanelWithFrame parent, Object[] initialisationObjects, String title, String xAxisTitle, String yAxisTitle, WindowListener windowListener, boolean showFrame) throws HeadlessException {
		super(parent, initialisationObjects, title, xAxisTitle, yAxisTitle, windowListener, showFrame);
	}
	
	@Override
	/**
	 * @see AbstractCrossSectionGraph#prepareDataArray
	 */
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets) {
		mData = new double[sortedYCoords.size()][2];
		mDerivativeData = new double[mData.length][2];
		Enumeration<Integer> yCoords = sortedYCoords.elements();
		Enumeration<Vector<Integer>> zCoordVectors = sortedZCoordsSets.elements();
		Vector<Double> data = new Vector<Double>(mData.length), positions = new Vector<Double>(mData.length);
		Vector<Double> derivatives;
		
		for (int i = 0; i < mData.length; ++i) {
			int yCoord = yCoords.nextElement();
			
			prepareDataArrayStep(dataSetId, i, yCoord, zCoordVectors.nextElement());
			data.add(mData[i][DATUM_INDEX]);
			positions.add(mData[i][Y_COORD_INDEX]);
		}
		
		try {
			derivatives = MAJFCMaths.derivatives(data, positions);
		} catch (MAJFCToolsException e) {
			JOptionPane.showMessageDialog(DAFrame.getFrame(), e.getMessage(), e.getMessage(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			derivatives = new Vector<Double>(mData.length);
			
			for (int i = 0; i < mData.length; ++i) {
				derivatives.addElement(0d);
			}
		}
		
		for (int i = 0; i < mData.length; ++i) {
			mDerivativeData[i][Y_COORD_INDEX] = positions.elementAt(i);
			// y-coordinate is in mm. Convert to m for the derivative
			mDerivativeData[i][DATUM_INDEX] = 1000 * derivatives.elementAt(i);
		}
	}

	/**
	 * The calculation step in which the mData value is set from prepareDataArray
	 * @see AbstractCrossSectionGraph#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector)
	 * @param dataSetId The id of the data set the graph is for
	 * @param dataIndex The index in the mData array in which to put the value
	 * @param yCoord The y-coordinate this mData entry is for
	 * @param zCoordsList The z-coordinates for which there are data points at yCoord
	 */
	protected abstract void prepareDataArrayStep(AbstractDataSetUniqueId dataSetId, int dataIndex, int yCoord, Vector<Integer> zCoordsList);

	@Override
	/**
	 * @see AbstractCrossSectionGraph#buildGUI
	 */
	protected void buildGUI(String title, String xAxisTitle, String yAxisTitle) {
		super.buildGUI(title, xAxisTitle, yAxisTitle);
		NumberAxis axis = new NumberAxis(DAStrings.getString(DAStrings.DERIVATIVE_GRID_SECONDARY_Y_AXIS_TITLE));
		XYDataSetAdapter dataSet = new XYDataSetAdapter() {
			
			@Override
			public double getYValue(int series, int item) {
				return mDerivativeData[item][DATUM_INDEX];
			}
			
			@Override
			public double getXValue(int series, int item) {
				return mDerivativeData[item][Y_COORD_INDEX];
			}
			
			@Override
			public int getSeriesCount() {
				return 1;
			}
			
			@SuppressWarnings("rawtypes")
			@Override
			/**
			 * XYDataset implementation
			 */
			public Comparable getSeriesKey(int series) {
				return "derivative";
			}

			@Override
			public int getItemCount(int series) {
				return mDerivativeData.length;
			}
		};
		
		addSecondaryAxis(DERIVATIVE_SERIES_ID, axis, dataSet);
	}
}
