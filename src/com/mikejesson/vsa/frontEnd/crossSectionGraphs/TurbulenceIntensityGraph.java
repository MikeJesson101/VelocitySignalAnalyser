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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.jfree.chart.JFreeChart;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ColourCodedChartPanel;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class TurbulenceIntensityGraph extends AbstractCrossSectionColourCodedGraph {
	private JCheckBox mScaleByQOverA;
	private double mScaledByQOverAMean;
	private double mScaledByLocalVelocityMean;
	private Vector<Double[]> mTIsScaledByLocalVelocity;
	private final String mScaledByQOverARawChartTitle;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public TurbulenceIntensityGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex theTurbulenceIntensityDataPointSummaryIndex, String standardTIChartTitle, String scaledByQOverAChartTitle) throws HeadlessException {
		super(dataSetId, parent, 50, theTurbulenceIntensityDataPointSummaryIndex, standardTIChartTitle, DAStrings.getString(DAStrings.TURBULENCE_INTENSITY_GRAPH_LEGEND_TEXT), false);
		
		mScaledByQOverARawChartTitle = scaledByQOverAChartTitle;
	}
	
	/**
	 * Sets up the values in the data array, calculating the depth-averaged velocity at each y-coordinate
	 * @see AbstractCrossSectionColourCodedGraph#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, DataPointSummaryIndex, int, int, int)
	 */
	/**
	 * Sets up the values in the data array used for the XYZDataset implementation
	 * @param dataSetId The id of the data set to use data from
	 * @param sortedYCoords The sorted y-coordinates for the data set
	 * @param sortedZCoords The sorted z-coordinates for the data set - a set of vectors, each containing the sorted z-coordinates for the points
	 * which exist at the y-coordinate at the corresponding position in sortedYCoords
	 * @param data The data used by the XYZDataset implementation. A vector of Double[3], with each array holding a y-coordinate, z-coordinate and data value.
	 * The indices for each of these are specified by yCoordIndexInDataArray, zCoordIndexInDataArray and dataIndexInDataArray 
	 * @param yCoordIndexInDataArray See above
	 * @param zCoordIndexInDataArray See above
	 * @param dataIndexInDataArray See above
	 */
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex DPSIndex, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoords, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) {
		// This will get the standard deviation, and set mMean to the mean standard deviation.
		super.prepareDataArray(dataSetId, mDPSIndex, sortedYCoords, sortedZCoords, data, yCoordIndexInDataArray, zCoordIndexInDataArray, dataIndexInDataArray);
		mScaledByQOverAMean = mMean/DAFrame.getFrame().getChannelMeanVelocityForScaling(mDataSetId);
		
		DataPointSummaryIndex localVelocityDPSIndex = mDPSIndex.equals(BackEndAPI.DPS_KEY_RMS_U_PRIME) ? BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY
				: mDPSIndex.equals(BackEndAPI.DPS_KEY_RMS_V_PRIME) ? BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY
						: BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY;

		int numberOfData = data.size();
		Vector<Double[]> localVelocities = new Vector<Double[]>(numberOfData);
		super.prepareDataArray(dataSetId, localVelocityDPSIndex, sortedYCoords, sortedZCoords, localVelocities, yCoordIndexInDataArray, zCoordIndexInDataArray, dataIndexInDataArray);
		// mMean is now the mean of the velocities!

		mTIsScaledByLocalVelocity = new Vector<Double[]>(numberOfData);
		mScaledByLocalVelocityMean = 0;
		
		for (int i = 0; i < numberOfData; ++i) {
			Double[] dataPoint = data.get(i);
			Double[] scaledDataPoint = {dataPoint[0], dataPoint[1], dataPoint[dataIndexInDataArray]/localVelocities.get(i)[dataIndexInDataArray] };
			mScaledByLocalVelocityMean += scaledDataPoint[dataIndexInDataArray];
			mTIsScaledByLocalVelocity.add(scaledDataPoint); 
		}
		
		mScaledByLocalVelocityMean /= numberOfData;
	}

	@Override
	protected void showTheVerticalSectionGraph() {
		if (mVerticalSectionGraph == null) {
			String gridTitle = DAStrings.getString(DAStrings.TURBULENCE_INTENSITY_GRID_TITLE);
			String xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE);
			String yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY__GRID_Y_AXIS_TITLE);
			String legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_LEGEND_KEY_LABEL);

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
		String gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_TITLE);
		String xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE);
		String yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_Y_AXIS_TITLE);

		Vector<String> graphLabels = new Vector<String>(3);
		graphLabels.add(gridTitle);
		graphLabels.add(xAxisTitle);
		graphLabels.add(yAxisTitle);
		
		return graphLabels;
	}
	
	/**
	 * Constructs the chart panel to show in this display
	 * @param theChart The chart to show in the chart panel
	 * @param xyzDataSet The dataset to display in the graph
	 * @param steps The number of steps to use in the colour coding
	 * @param legendText The legend text for the graph
	 * @return The ColourCodedChartPanel for this display
	 */
	@Override
	protected ColourCodedChartPanel constructColourCodedChartPanel(final MAJFCStackedPanelWithFrame frameParent, JFreeChart theChart, MyXYZDataSet xyzDataSet, int steps, String legendText, boolean dataIsVectorInY) {
		return new TIColourCodedChartPanel(this, theChart, xyzDataSet, 10, 10, steps, legendText, dataIsVectorInY);
	}
	
	private class TIColourCodedChartPanel extends ACSColourCodedChartPanel {
		private TIColourCodedChartPanel(MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYZDataSet scaleableDataSet, double nonDimensionlessBlockHeight, double nonDimensionlessBlockWidth, int steps, String legendText, boolean dataIsVectorInY) {
			super(holderFrame, theChart, scaleableDataSet, nonDimensionlessBlockHeight, nonDimensionlessBlockWidth, steps, legendText, dataIsVectorInY);
		}

		/**
		 * Gets additional components for the chart. Override this to add extras
		 * @return A list of the components to add
		 */
		@Override
		protected Vector<JComponent> getAdditionalComponents() {
			Vector<JComponent> additionalComponents = new Vector<JComponent>(1);
			mScaleByQOverA = new JCheckBox(DAStrings.getString(DAStrings.TI_SCALE_BY_Q_OVER_A_LABEL)); 
			mScaleByQOverA.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					updateDisplay();
				}
			});
			
			additionalComponents.add(mScaleByQOverA);
		
			return additionalComponents;
		}
		
		/**
		 * Gets the raw chart title
		 * @return The raw chart title
		 */
		protected String getRawChartTitle() {
			if (mScaleByQOverA.isSelected()) {
				return mScaledByQOverARawChartTitle;
			}
			
			return super.getRawChartTitle();
		}
	}

	/**
	 * Constructs the MyXYZDataSet to use for this display the
	 * @param leftBankPosition
	 * @param rightBankPosition
	 * @param bedPosition
	 * @param waterDepth
	 * @return The MyXYZDataSet to use for this display the
	 */
	protected MyXYZDataSet constructMyXYZDataSet(int leftBankPosition, int rightBankPosition, int bedPosition, int waterDepth) {
		return new TI_XYZDataSet(mMean, mScalerForDimensionlessValues, leftBankPosition, rightBankPosition, bedPosition, waterDepth);
	}

	class TI_XYZDataSet extends MyXYZDataSet {
		public TI_XYZDataSet(double mean, double scalerForDimensionlessValues, int unscaledXMin, int unscaledXMax, int unscaledYMin, int unscaledYMax) {
			super(mean, scalerForDimensionlessValues, unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}

		@Override
		/**
		 * XYZDataset implementation
		 */
		public double getTheZValue(int series, int item) {
			if (mScaleByQOverA.isSelected()) {
				return super.getTheZValue(series, item);
			} else {
				return mTIsScaledByLocalVelocity.elementAt(item)[DATA_INDEX];
			}
		}
		
		/**
		 * Gets the mean value of the data held in this dataset (unless overloaded, this is as set in the constructor)
		 * @return The mean value
		 */
		@Override
		protected double getMean() {
			if (mScaleByQOverA.isSelected()) {
				return mScaledByQOverAMean;
			} else {
				return mScaledByLocalVelocityMean;
			}
		}
	}
}
