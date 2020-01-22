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

package com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import org.jfree.chart.axis.NumberAxis;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTranslatingDropDownPanel;
import com.mikejesson.majfc.helpers.MAJFCIndex;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.AbstractMultiSeriesGraph;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYZDataSet;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class SectionGraph extends AbstractMultiSeriesGraph {
	private MAJFCTranslatingDropDownPanel<Double> mCoordChooser;
	private Vector<Double> mCoordChooserOptions;
	private Vector<String> mCoordChooserDisplayList;
	private ScaleableXYZDataSet mDataSet;
	private SectionTypeIdentifier mSectionType;
	
	private double mChartXMin;
	private double mChartXMax;
	private double mChartYMin;
	private double mChartYMax;
	
	public static final SectionTypeIdentifier VERTICAL_SECTION = new SectionTypeIdentifier(0);
	public static final SectionTypeIdentifier HORIZONTAL_SECTION = new SectionTypeIdentifier(1);
	
	/**
	 * Constructor
	 * @param sectionType The type (vertical or horizontal) of section. One of the {@link SectionTypeIdentifier} objects defined in this class
	 * @param coord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param xAxisTitle The x-axis title for the graph
	 * @param yAxisTitle The y-axis title for the graph
	 * @param legendKeyLabel The label for the legend key
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public SectionGraph(MAJFCStackedPanelWithFrame parent, SectionTypeIdentifier sectionType, AbstractDataSetUniqueId dataSetId, ScaleableXYZDataSet dataSet, Number coord, String title, String xAxisTitle, String yAxisTitle, String legendKeyLabel, WindowListener windowListener) throws HeadlessException {
		super(dataSetId, parent, new Object[] {dataSet, sectionType}, coord, title, xAxisTitle, yAxisTitle, legendKeyLabel, windowListener);
	}
	
	@Override
	/**
	 * Initialise those objects that need to be initialised before the parent constructor can start
	 * @param initialisationObjects The required objects to initialise member objects
	 */
	protected void initialise(Object[] initialisationObjects) {
		mDataSet = (ScaleableXYZDataSet) initialisationObjects[0];
		mSectionType = (SectionTypeIdentifier) initialisationObjects[1];
	}
	
	@Override
	protected Hashtable<Number, Vector<AMSGDataPoint>> getOrderedSeriesDataList() throws BackEndAPIException {
		try {
			Hashtable<Number, Vector<AMSGDataPoint>> orderedSeriesDataList = new Hashtable<Number, Vector<AMSGDataPoint>>(4);
			mCoordChooserOptions  = new Vector<Double>();
			int numberOfSeries = mDataSet.getSeriesCount();
			mChartXMin = Double.MAX_VALUE;
			mChartXMax = Double.MIN_VALUE;
			mChartYMin = Double.MAX_VALUE;
			mChartYMax = Double.MIN_VALUE;

			for (int series = 0; series < numberOfSeries; ++series) {
				int numberOfItems = mDataSet.getItemCount(series);
				
				for (int item = 0; item < numberOfItems; ++item) {
					double yCoord = mDataSet.getXValue(series, item);
					double zCoord = mDataSet.getYValue(series, item);
					double datum = mDataSet.getZValue(series, item);
					double lookupIndex = yCoord;
					double amsgPointX = datum;
					double amsgPointY = zCoord;
					
					if (mSectionType.equals(HORIZONTAL_SECTION)) {
						lookupIndex = zCoord;
						amsgPointX = yCoord;
						amsgPointY = datum;
					}
					
					Vector<AMSGDataPoint> dataPoints;
					
					if ((dataPoints = orderedSeriesDataList.get(lookupIndex)) == null) {
						dataPoints = new Vector<AMSGDataPoint>(50);
						orderedSeriesDataList.put(lookupIndex, dataPoints);
						mCoordChooserOptions.add(lookupIndex);
					}
					
					dataPoints.add(new AMSGDataPoint(amsgPointX, amsgPointY));
					
					mChartXMin = Math.min(mChartXMin, amsgPointX);
					mChartXMax = Math.max(mChartXMax, amsgPointX);
					mChartYMin = Math.min(mChartYMin, amsgPointY);
					mChartYMax = Math.max(mChartYMax, amsgPointY);
				}
			}

			if (mSectionType.equals(VERTICAL_SECTION)) {
				mChartYMin = 0;
				mChartYMax = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_WATER_DEPTH).intValue();
			} else {
				mChartXMin = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue();
				mChartXMax = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue();
			}
			
			Collections.sort(mCoordChooserOptions);

			int numberOfOptions = orderedSeriesDataList.size();
			mCoordChooserDisplayList = new Vector<String>(numberOfOptions);
			for (int i = 0; i < numberOfOptions; ++i){
				mCoordChooserDisplayList.add(MAJFCTools.formatNumber(mCoordChooserOptions.elementAt(i).doubleValue(), 2, false));
			}
			
			return orderedSeriesDataList;
		} catch (BackEndAPIException theException) {
			MAJFCLogger.log("Failed to prepare data in AbstractVerticalSectionGraph::getOrderedSeriesDataList");
			throw theException;
		}
	}
	
	@Override
	/**
	 * Builds the custom panel placed at the bottom of the other panels
	 */
	protected MAJFCPanel buildCustomPanel() {
		Vector<Integer> sortedCoords = new Vector<Integer>();
		MAJFCPanel coordChooserPanel = new MAJFCPanel(new GridBagLayout());
		
		if (sortedCoords != null) {
			String coordChooserLabel = DAStrings.getString(DAStrings.VERTICAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL);

			if (mSectionType.equals(HORIZONTAL_SECTION)) {
				coordChooserLabel = DAStrings.getString(DAStrings.HORIZONTAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL);
			}
			
			mCoordChooser = new MAJFCTranslatingDropDownPanel<Double>(coordChooserLabel, mCoordChooserOptions, mCoordChooserDisplayList, mCoordChooserOptions.firstElement());

			mCoordChooser.addActionListener(new ActionListener() {
				/**
				 * ActionListener implementation
				 */
				@Override
				public void actionPerformed(ActionEvent theEvent) {
					if (mCoordChooser.isSource(theEvent)) {
						Double selectedItem = mCoordChooser.getSelectedItem();
						if (selectedItem != null) {
							addOrRemoveSeries(selectedItem);
							
							mCoordChooser.setSelectedItem(selectedItem);
						}
					}
				}
			});

			int x = 0;
			int y = 0;
			coordChooserPanel.add(mCoordChooser, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.NONE, 0, 0, 3, 0, 0, 0));
		}
		
		return coordChooserPanel;
	}

	@Override
	public void updateDisplay() {
		// Need to call this first so all data has been setup
		super.updateDisplay();

		mCoordChooser.fillDropDownWithOptions(mCoordChooserOptions, mCoordChooserDisplayList, mCoordChooserOptions.firstElement());
	}
	
	@Override
	protected double[] getAxisRanges() {
		return new double[] { mChartXMin, mChartXMax, mChartYMin, mChartYMax };
	}
	
	@Override
	/**
	 * Customise the axes
	 * @param xAxis The x-axis to customise
	 * @param yAxis The y-axis to customise
	 */
	protected void customiseAxes(NumberAxis xAxis, NumberAxis yAxis) {
//		if (Math.max(Math.abs(mChartXMax), Math.abs(mChartXMin)) < 10) {
//			xAxis.setTickUnit(new NumberTickUnit(0.1, NumberFormat.getNumberInstance()));
//			xAxis.setRangeType(RangeType.POSITIVE);
//		}

//		yAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getNumberInstance()));
//		yAxis.setRangeType(RangeType.POSITIVE);
	}

	/**
	 * Helper class
	 * @author Mike
	 *
	 */
	private static class SectionTypeIdentifier extends MAJFCIndex {
		public SectionTypeIdentifier(int termId) {
			super(termId);
		}
	}
}
