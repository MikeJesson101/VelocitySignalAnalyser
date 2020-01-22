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

package com.mikejesson.vsa.frontEnd.dataPoint;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;

import com.mikejesson.majfc.guiComponents.MAJFCNumberTextAreaPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCLinkedGUIComponentsAction;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.majfc.helpers.MAJFCMaths.MAJFCMathsPSDWindowType;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYDataSet;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointPowerSpectrumDisplay extends MAJFCStackedPanelWithFrame implements PropertyChangeListener, ItemListener {
	private PSDScaleableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private double[][][] mData;
	private Vector<String> mLegendEntries;
	private MyDataSet mChartDataSet;
	private final AbstractDataSetUniqueId mDataSetId;
	private MAJFCNumberTextAreaPanel mFiveThirdsCentre;
	private JCheckBox mShowFiveThirdsLine;
	private JMenuItem mExportSNoughtsButton;
	
	private static final int FREQUENCY_INDEX = 0;
	private static final int POWER_INDEX = 1;

	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries (should match the order of velocitiesSets)
	 * @param velocitiesSets The velocity measurements for the first component of the data points to display power spectra for
	 * @param velocitiesSets2 The velocity measurements for the second component of the data points to display power spectra for
	 * @throws Exception 
	 * 
	 */
	public DataPointPowerSpectrumDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<String> legendEntries, Vector<Vector<Double>> velocitiesSets) throws Exception {
		super(new GridBagLayout());
		
		mLegendEntries = legendEntries;
		mDataSetId = dataSetId;
	
		DataSetConfig configData = BackEndAPI.getBackEndAPI().getConfigData(dataSetId); 
		double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
		int thePSDType = configData.get(BackEndAPI.DSC_KEY_PSD_TYPE).intValue();
		int thePSDWindow = configData.get(BackEndAPI.DSC_KEY_PSD_WINDOW).intValue();
		int numberOfBartlettWindows = configData.get(BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS).intValue(); 
		int numberOfVelocitiesSets = velocitiesSets.size();

		MAJFCMathsPSDWindowType windowType = MAJFCMaths.PSD_WINDOW_TYPE_NONE;
		int overlap = 0;
		
		if (thePSDType == BackEndAPI.PSD_WELCH.getIntIndex()) {
			overlap = configData.get(BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP).intValue();
			
			if (thePSDWindow == MAJFCMaths.PSD_WINDOW_TYPE_BARTLETT.getIntIndex()) {
				windowType = MAJFCMaths.PSD_WINDOW_TYPE_BARTLETT;
			} else if (thePSDWindow == MAJFCMaths.PSD_WINDOW_TYPE_HAMMING.getIntIndex()) {
				windowType = MAJFCMaths.PSD_WINDOW_TYPE_HAMMING;
			}
		}

		mData = new double[numberOfVelocitiesSets + 1][][];
		double xMax = Double.MIN_VALUE, xMin = Double.MAX_VALUE, yMax = Double.MIN_NORMAL, yMin = Double.MAX_VALUE;
		
		// -5/3 line
		mData[mData.length - 1] = new double[2][2];

		for (int velocitiesSetsIndex = 0; velocitiesSetsIndex < numberOfVelocitiesSets; ++velocitiesSetsIndex) {
			Vector<Double> velocities = velocitiesSets.elementAt(velocitiesSetsIndex);
			Vector<Double> powerSpectrum;
			
			if (thePSDType == BackEndAPI.PSD_WELCH.getIntIndex()) {
				powerSpectrum = MAJFCMaths.estimatePowerSpectrumWelch(velocities, samplingRate, numberOfBartlettWindows, overlap, windowType);
			} else {
				powerSpectrum = MAJFCMaths.estimatePowerSpectrumBartlett(velocities, samplingRate, numberOfBartlettWindows);
			}
			
			int numberOfValues = powerSpectrum.size() - 1;
			
			// Smooth the series
//			fds1MagnitudeSquareds = MAJFCMaths.lowPassFilter(fds1MagnitudeSquareds, 0.05, 1);
			
			mData[velocitiesSetsIndex] = new double[numberOfValues][2];
			// The PSD function returns up to the Nyquist frequency, so the number of samples is twice the size of the returned vector.
			double deltaFreq = samplingRate/(numberOfValues * 2);
			
			for (int i = 0, psIndex = 1; i < numberOfValues; ++i, ++psIndex) {
				double frequency = psIndex * deltaFreq;
				
				double power = powerSpectrum.elementAt(psIndex);
				
				mData[velocitiesSetsIndex][i][FREQUENCY_INDEX] = frequency;

//					power = (1d/Math.pow(stDev1,2)) * Math.pow(frequencyDomainSignal1.get(fdsIndex).magnitude(), 2) / (2 * Math.PI);
//					power = (1d/numberOfValues) *(mean1/(2 * Math.PI)) * fds1MagnitudeSquareds.get(fdsIndex);
//				power = (1d/Math.pow(numberOfValues, 2)) * fds1MagnitudeSquareds.get(psIndex);
//					power = fds1MagnitudeSquareds.get(fdsIndex);
//					power = (frequency/Math.pow(stDev1,2)) * Math.pow(frequencyDomainSignal1.get(fdsIndex).magnitude(), 2) / (2 * Math.PI);
//					power /= Math.pow(stDev1,2);
				
				mData[velocitiesSetsIndex][i][POWER_INDEX] = Math.max(power, Double.MIN_VALUE);
//				System.out.println("freq:\t" + mData[velocitiesSetsIndex][i][FREQUENCY_INDEX] + "\tpower:\t" + mData[velocitiesSetsIndex][i][POWER_INDEX]);				
				
				xMin = Math.min(xMin, frequency);
				xMax = Math.max(xMax, frequency);
				yMin = Math.min(yMin, power);
				yMax = Math.max(yMax, power);
			}
		}

		mChartDataSet = new MyDataSet(xMin, xMax, yMin, yMax);

		buildGUI(numberOfBartlettWindows);
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI(int numberOfBartlettWindows) {
		setBorder(BorderFactory.createEtchedBorder());
		
		String yAxisLabel = DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_Y_AXIS_LABEL, String.valueOf(numberOfBartlettWindows));
		NumberAxis xAxis = new LogarithmicAxis(DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL));
//		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL));
		NumberAxis yAxis = new LogarithmicAxis(yAxisLabel);

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		theRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mChartDataSet.getSeriesCount(); ++i) {
			theRenderer.setSeriesShapesVisible(i, false);
		}
		
		XYPlot thePlot = new XYPlot(mChartDataSet, xAxis, yAxis, theRenderer);
		thePlot.setRangeZeroBaselineVisible(true);
		thePlot.setDomainZeroBaselineVisible(true);
		
		mTheChart = new JFreeChart(DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, thePlot, mLegendEntries != null && mLegendEntries.size() > 0);
		
		mChartPanel = new PSDScaleableChartPanel(mDataSetId, this, mTheChart, mChartDataSet, true, DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL), yAxisLabel, false);

		mFiveThirdsCentre = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.FIVE_THIRDS_LINE_CENTRE), 0, 1000, 500, 0, this);
		mFiveThirdsCentre.setEnabled(false);
		mShowFiveThirdsLine = new JCheckBox(DAStrings.getString(DAStrings.SHOW_FIVE_THIRDS_LINE));
		mShowFiveThirdsLine.addItemListener(this);
		
		int x = 0;
		int y = 0;
		add(mChartPanel.getGUI(), MAJFCTools.createGridBagConstraint(x, y++, 2, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		add(mFiveThirdsCentre, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		add(mShowFiveThirdsLine, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
	}

	private class MyDataSet extends ScaleableXYDataSet {
		public MyDataSet(double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			super(unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mData[series].length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mShowFiveThirdsLine != null && mShowFiveThirdsLine.isSelected() ? mData.length : mData.length - 1;
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return mLegendEntries.elementAt(series);
		}

		@Override
		public double getTheXValue(int series, int item) {
			return mData[series][item][FREQUENCY_INDEX];
		}

		@Override
		public double getTheYValue(int series, int item) {
			return mData[series][item][POWER_INDEX];
		}
	}

	/**
	 * PropertyChangeListener implementation
	 */
	@Override
	public void propertyChange(PropertyChangeEvent theEvent) {
		if (mFiveThirdsCentre.isSource(theEvent)) {
			showFiveThirdsLine(false);
		}
		
	}

	/**
	 * ItemListener implementation
	 * @param theEvent
	 */
	@Override
	public void itemStateChanged(ItemEvent theEvent) {
		Object oSource = theEvent.getSource();
		
		if (oSource.equals(mShowFiveThirdsLine)) {
			mFiveThirdsCentre.setEnabled(mShowFiveThirdsLine.isSelected());
			
			if (mShowFiveThirdsLine.isSelected()) {
				showFiveThirdsLine(true);
			} else {
				hideFiveThirdsLine();
			}
		}
		
	}

	private void showFiveThirdsLine(boolean recentre) {
		int valueIndexMax = mData[0].length - 1;
		double startFrequency = mData[0][0][FREQUENCY_INDEX];
		double endFrequency = mData[0][valueIndexMax][FREQUENCY_INDEX];
		double centreFrequency = mFiveThirdsCentre.getValue();
		if (recentre && (centreFrequency < startFrequency || centreFrequency > endFrequency)) {
			centreFrequency = (startFrequency + endFrequency)/2;
		}

		mFiveThirdsCentre.setMinimumValue(startFrequency);
		mFiveThirdsCentre.setValue(centreFrequency);
		mFiveThirdsCentre.setMaximumValue(endFrequency);

		int centreIndex = 0;
		double frequency = -1;
		
		do {
			frequency = mData[0][centreIndex++][FREQUENCY_INDEX];
		} while (frequency < centreFrequency);
		
		double centrePower = mData[0][Math.min(centreIndex, valueIndexMax)][POWER_INDEX];
		int centrePowerSmoothingWindowStart = Math.max(0, centreIndex - 2);
		int centrePowerSmoothingWindowEnd = Math.min(valueIndexMax, centreIndex + 2);
		
		for (int i = centrePowerSmoothingWindowStart; i < centrePowerSmoothingWindowEnd + 1; ++i) {
			centrePower += mData[0][i][POWER_INDEX];
		}
		
		centrePower /= (centrePowerSmoothingWindowEnd - centrePowerSmoothingWindowStart + 1);
		
		final double MINUS_FIVE_THIRDS = -5d/3d;
		
		mData[mData.length - 1][0][FREQUENCY_INDEX] = startFrequency;
		mData[mData.length - 1][0][POWER_INDEX] = (centrePower/Math.pow(centreFrequency, MINUS_FIVE_THIRDS)) * Math.pow(startFrequency, MINUS_FIVE_THIRDS);
		mData[mData.length - 1][1][FREQUENCY_INDEX] = endFrequency;
		mData[mData.length - 1][1][POWER_INDEX] = (centrePower/Math.pow(centreFrequency, MINUS_FIVE_THIRDS)) * Math.pow(endFrequency, MINUS_FIVE_THIRDS);
		
		mLegendEntries.add(DAStrings.getString(DAStrings.FIVE_THIRDS_LINE_LEGEND_ENTRY));

		mChartPanel.updateDisplay();
	}

	private void hideFiveThirdsLine() {
		mLegendEntries.remove(mLegendEntries.size() - 1);

		mChartPanel.updateDisplay();
	}

	private class PSDScaleableChartPanel extends ScaleableChartPanel {
		private PSDScaleableChartPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYDataSet scaleableDataSet, boolean buildGUI, String dimensionedXAxisTitle, String dimensionedYAxisTitle, boolean dataIsVectorInY) {
			super(dataSetId, holderFrame, theChart, scaleableDataSet, buildGUI, dimensionedXAxisTitle, dimensionedYAxisTitle, dataIsVectorInY);
		}
		
		@Override
		/**
		 * Create the right-click context menu
		 * @param properties Flag to show/hide properties menu item
		 * @param save Flag to show/hide save menu item
		 * @param print Flag to show/hide print menu item
		 * @param zoom Flag to show/hide zoom menu item
		 */
		protected JPopupMenu createPopupMenu(boolean properties, boolean copy, boolean save, boolean print, boolean zoom) {
			JPopupMenu theMenu = super.createPopupMenu(false, copy, save, print, false);
			theMenu.addSeparator();
			
			mExportSNoughtsButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.EXPORT_PSD_S_NOUGHTS_BUTTON_LABEL), null, DAStrings.getString(DAStrings.EXPORT_PSD_S_NOUGHTS_BUTTON_DESC), mExportSNoughtsButton, this);
			
			theMenu.add(mExportSNoughtsButton);
			
			return theMenu;
		}
		
		@Override
		public void actionPerformed(ActionEvent theEvent) {
			Action action = ((AbstractButton) theEvent.getSource()).getAction();
			
			if (action == null) {
				super.actionPerformed(theEvent);
				return;
			}

			String actionId = (String) action.getValue(Action.SHORT_DESCRIPTION);
			
			if (actionId.equals(DAStrings.getString(DAStrings.EXPORT_PSD_S_NOUGHTS_BUTTON_DESC))) {
				exportSNoughtsAsTable();
			} else {
				super.actionPerformed(theEvent);
			}
		}
		
		private void exportSNoughtsAsTable() {
			Vector<Vector<Vector<Double>>> outputSeriesList = new Vector<Vector<Vector<Double>>>(1);
			int numberOfDataSeries = mChartDataSet.getSeriesCount();
			Vector<Vector<Double>> outputDataPoints = new Vector<Vector<Double>>(numberOfDataSeries);
			Vector<String> columnTitles = new Vector<String>(3);
			
			for (int dataSeriesIndex = 0; dataSeriesIndex < numberOfDataSeries; ++dataSeriesIndex) {
				Vector<Double> coordsAndSNought = new Vector<Double>(3);
				String seriesKeyString = mChartDataSet.getSeriesKey(dataSeriesIndex).toString();
				String yColumnTitle = DAStrings.getString(DAStrings.Y_COLUMN_TITLE);
				String zColumnTitle = DAStrings.getString(DAStrings.Z_COLUMN_TITLE);
				String sNoughtColumnTitle = DAStrings.getString(DAStrings.S_NOUGHT_COLUMN_TITLE);
				
				if (seriesKeyString.length() > 0) {
					Vector<Double> coords = DATools.getCoordsFromDataPointIdentifier(seriesKeyString);
					
					coordsAndSNought.add(coords.get(0));
					coordsAndSNought.add(coords.get(1));
					coordsAndSNought.add(mChartDataSet.getTheYValue(dataSeriesIndex, 0));
				}
				
				columnTitles.add(yColumnTitle);
				columnTitles.add(zColumnTitle);
				columnTitles.add(sNoughtColumnTitle);
				
				outputDataPoints.add(coordsAndSNought);
			}

			outputSeriesList.add(outputDataPoints);
			
			displayAsTable(getChart().getTitle().getText(), outputSeriesList, columnTitles);
		}
	}
	

}
