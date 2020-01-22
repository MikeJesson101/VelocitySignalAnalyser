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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.mikejesson.majfc.guiComponents.MAJFCButton;
import com.mikejesson.majfc.guiComponents.MAJFCNumberTextAreaPanel;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel.MAJFCTabContents;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndCallBackAdapter;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.ComponentTabsFrame;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointDetailDisplay extends MAJFCStackedPanelWithFrame {
	private final DataPointDetailDisplayFrame mFrame;
	private JPanel mTablePanelInterior = new JPanel(new GridBagLayout());
	private JPanel mBottomPanelInterior = new JPanel(new GridBagLayout());
	private JScrollPane mTablePanel = new JScrollPane(mTablePanelInterior);
	private ComponentTabsFrame mTimeSeriesGraphsPanel;
	private JTabbedPane mTopPanel = new JTabbedPane(JTabbedPane.BOTTOM);
	private JScrollPane mBottomPanel = new JScrollPane(mBottomPanelInterior);
	private JLabel mYCoordLabel = new JLabel(DAStrings.getString(DAStrings.Y_COORD_LABEL));
	private JLabel mZCoordLabel = new JLabel(DAStrings.getString(DAStrings.Z_COORD_LABEL));
	private JLabel mUVFluctuationsMeanLabel = new JLabel(DAStrings.getString(DAStrings.UV_FLUCTUATIONS_MEAN_LABEL));
	private JLabel mUWFluctuationsMeanLabel = new JLabel(DAStrings.getString(DAStrings.UW_FLUCTUATIONS_MEAN_LABEL));
	private JLabel mProbeRotationInXZPlaneTheta = new JLabel(DAStrings.getString(DAStrings.XZ_PLANE_ROTATION_THETA_LABEL));
	private JLabel mProbeRotationInYZPlanePhi = new JLabel(DAStrings.getString(DAStrings.YZ_PLANE_ROTATION_PHI_LABEL));
	private JLabel mProbeRotationInXYPlaneAlpha = new JLabel(DAStrings.getString(DAStrings.XY_PLANE_ROTATION_ALPHA_LABEL));
	private SummaryDataPanel mUSummary = new SummaryDataPanel(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL));
	private SummaryDataPanel mVSummary = new SummaryDataPanel(DAStrings.getString(DAStrings.V_VELOCITIES_LABEL));
	private SummaryDataPanel mWSummary = new SummaryDataPanel(DAStrings.getString(DAStrings.W_VELOCITIES_LABEL));
	
	private MAJFCPanel mExtractPanel;
	private JTextPane mTrimWarning;
	private MAJFCNumberTextAreaPanel mTrimStartTime;
	private MAJFCNumberTextAreaPanel mTrimEndTime;
	private MAJFCButton mTrimTimeSeries;
	
	private final BackEndCallBackAdapter mBackEndCallBackAdapter;
	
	private double mSamplingRate;
	
	private Hashtable<String, DataPointDetailFieldDisplay> mChartPanelLookup = new Hashtable<String, DataPointDetailFieldDisplay>();
	private final AbstractDataSetUniqueId mDataSetId;
	
	private int mYCoord;
	private int mZCoord;
	private DataPointSummary mDataPointSummary = new DataPointSummary();
	
	private class SummaryDataPanel extends MAJFCPanel {
		private JLabel mTitle;
		private JLabel mMeanLabel = new JLabel(DAStrings.getString(DAStrings.MEAN_LABEL) + ": ");
		private JLabel mMeanValue = new JLabel("0.0");
		private JLabel mStDevLabel = new JLabel(DAStrings.getString(DAStrings.STDEV_LABEL) + ": ");
		private JLabel mStDevValue = new JLabel("0.0");
		private JLabel mFilteredMeanLabel = new JLabel(DAStrings.getString(DAStrings.FILTERED_MEAN_LABEL) + ": ");
		private JLabel mFilteredMeanValue = new JLabel("0.0");
		private JLabel mFilteredStDevLabel = new JLabel(DAStrings.getString(DAStrings.FILTERED_STDEV_LABEL) + ": ");
		private JLabel mFilteredStDevValue = new JLabel("0.0");
		private JLabel mPercentGoodLabel = new JLabel(DAStrings.getString(DAStrings.PERCENTAGE_OF_VELOCITIES_GOOD_LABEL) + ": ");
		private JLabel mPercentGoodValue = new JLabel("0.0");
		private JLabel mMeanCorrLabel = new JLabel(DAStrings.getString(DAStrings.MEAN_CORRELATION_LABEL) + ": ");
		private JLabel mMeanCorrValue = new JLabel("0.0");
		private JLabel mMeanSNRLabel = new JLabel(DAStrings.getString(DAStrings.MEAN_SNR_LABEL) + ": ");
		private JLabel mMeanSNRValue = new JLabel("0.0");

		SummaryDataPanel(String title) {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createEtchedBorder());
			
			mTitle = new JLabel(title);

			int y = 0;
			add(mTitle, MAJFCTools.createGridBagConstraint(0, y++, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 0, 5, 0, 0, 0));
			add(mMeanLabel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mMeanValue, MAJFCTools.createGridBagConstraint(1, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mStDevLabel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mStDevValue, MAJFCTools.createGridBagConstraint(1, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mFilteredMeanLabel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mFilteredMeanValue, MAJFCTools.createGridBagConstraint(1, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mFilteredStDevLabel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mFilteredStDevValue, MAJFCTools.createGridBagConstraint(1, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mPercentGoodLabel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mPercentGoodValue, MAJFCTools.createGridBagConstraint(1, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mMeanCorrLabel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mMeanCorrValue, MAJFCTools.createGridBagConstraint(1, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mMeanSNRLabel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
			add(mMeanSNRValue, MAJFCTools.createGridBagConstraint(1, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, 0, 5, 5, 5, 0, 0));
		}
	}
	
	/**
	 * Constructor
	 * @param yCoord The y-coordinate of this data point
	 * @param zCoord The z-coordinate of this data point
	 * @param dataPointSummaries The summary data for this point
	 * @param excludeLevel The exclude level used for this point 
	 * @param dataPointDetails The data for this data point
	 */
	public DataPointDetailDisplay(AbstractDataSetUniqueId dataSetId, DataPointDetailDisplayFrame frame, int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails) {
		super(new GridBagLayout());
		
		mDataSetId = dataSetId;
		mFrame = frame;
		mBackEndCallBackAdapter = new BackEndCallBackAdapter() {
			@Override
			public void onDataPointTrimmed(AbstractDataSetUniqueId uniqueId, DataPointSummary dataPointSummaries, DataPointDetail dataPointDetails) {
				mTopPanel.removeAll();
				setDetails(mYCoord, mZCoord, dataPointSummaries, dataPointDetails);
				
				DAFrame.getFrame().getBackEndAPICallBackAdapter().onDataPointTrimmed(uniqueId, dataPointSummaries, dataPointDetails);
			}
		};
		
		buildGUI();
		setDetails(yCoord, zCoord, dataPointSummaries, dataPointDetails);
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		buildBottomPanel();
		
		int y = 0;
		add(mTopPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		add(mBottomPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 0.7, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}
	
	/**
	 * Builds the bottom panel
	 */
	private void buildBottomPanel() {
		JPanel infoPanel = new JPanel(new GridBagLayout());
		
		int x = 0, y = 0;
		infoPanel.add(mYCoordLabel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 20, 0, 0, 0, 0, 0));
		infoPanel.add(mZCoordLabel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		
		y = 0;
		infoPanel.add(mUVFluctuationsMeanLabel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 20, 0, 0, 0, 0, 0));
		infoPanel.add(mUWFluctuationsMeanLabel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		
		y = 0;
		infoPanel.add(mProbeRotationInXZPlaneTheta, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 20, 0, 0, 0, 0, 0));
		infoPanel.add(mProbeRotationInYZPlanePhi, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		infoPanel.add(mProbeRotationInXYPlaneAlpha, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		
		x = 0;
		infoPanel.add(mUSummary, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 20, 0, 0, 0, 0, 0));
		infoPanel.add(mVSummary, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 20, 10, 0, 0, 0, 0));
		infoPanel.add(mWSummary, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, 20, 10, 0, 0, 0, 0));
		
		mExtractPanel = buildExtractPanel();
		
		x = 0;
		y = 0;
		mBottomPanelInterior.add(infoPanel, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0, 10, 5, 10, 0, 0));
		mBottomPanelInterior.add(mExtractPanel, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 0.5, 1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0, 10, 5, 10, 0, 0));
		
		refresh();
	}
	
	private MAJFCPanel buildExtractPanel() {
		MAJFCPanel extractPanel = new MAJFCPanel(new GridBagLayout());
		
		mTrimWarning = new JTextPane() {
			@Override
			public Dimension getPreferredSize() {
				Dimension parentDimension = mFrame.getPreferredSize();
				return new Dimension((int) (((double) parentDimension.width)/2.5), super.getPreferredSize().height);
			}
		};
		String trimWarningString = DAStrings.getString(DAStrings.TRIM_WARNING);
		mTrimWarning.setText(trimWarningString);
		mTrimStartTime = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.TRIM_START_POINT_LABEL), 0, 1000, 0, 1);
		mTrimEndTime = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.TRIM_END_POINT_LABEL), 0, 1000, 0, 1);
		mTrimTimeSeries = new MAJFCButton(DAStrings.getString(DAStrings.TRIM_LABEL));
		mTrimTimeSeries.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				trimTimeSeries(mTrimStartTime.getValue(), mTrimEndTime.getValue());
			}
		});
		StyledDocument trimWarningPaneStyle = mTrimWarning.getStyledDocument();
		SimpleAttributeSet trimWarningPaneAttributes = new SimpleAttributeSet();
		StyleConstants.setAlignment(trimWarningPaneAttributes, StyleConstants.ALIGN_JUSTIFIED);
		SimpleAttributeSet trimWarningPaneAttributes2 = new SimpleAttributeSet();
		StyleConstants.setLeftIndent(trimWarningPaneAttributes2, 10);
//		StyleConstants.setAlignment(trimWarningPaneAttributes2, new TabSet(new TabStop[] {new TabStop(5)}));
		
		int endOfFirstParagraph = trimWarningString.indexOf("\n\n");
		trimWarningPaneStyle.setParagraphAttributes(0, endOfFirstParagraph, trimWarningPaneAttributes, false);
		trimWarningPaneStyle.setParagraphAttributes(endOfFirstParagraph + 1, trimWarningPaneStyle.getLength(), trimWarningPaneAttributes2, false);
		
		mTrimWarning.setEditable(false);
		mTrimWarning.setFont(mTrimStartTime.getFont());
		mTrimWarning.setBackground(mTrimStartTime.getBackground());

		int x = 0, y = 0;
		extractPanel.add(mTrimWarning, MAJFCTools.createGridBagConstraint(x, y++, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 10, 0, 5, 0, 0, 0));
		extractPanel.add(new JPanel(), MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 5, 0, 0, 0));
		extractPanel.add(mTrimStartTime, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0, 5, 5, 0, 0));
		extractPanel.add(mTrimEndTime, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 5, 5, 0, 0, 0));
		extractPanel.add(new JPanel(), MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 5, 0, 0, 0));
		
		x = 0;
		extractPanel.add(mTrimTimeSeries, MAJFCTools.createGridBagConstraint(x, y++, GridBagConstraints.REMAINDER, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0, 5, 0, 0, 0));

		return extractPanel;
	}

	private void trimTimeSeries(Double startTime, Double endTime) {
		if (startTime >= endTime) {
			return;
		}
		
		int startIndex = (int) Math.floor(startTime * mSamplingRate);
		int endIndex = (int) Math.ceil(endTime * mSamplingRate) - 1;
		
		try {
			BackEndAPI.getBackEndAPI().trimDataPoint(mDataSetId, mBackEndCallBackAdapter, mYCoord, mZCoord, startIndex, endIndex);
		} catch (BackEndAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sets data point details to be displayed
	 * @param yCoord The y-coordinate of this data point
	 * @param zCoord The z-coordinate of this data point
	 * @param dataPointSummaries The summary data for this point
	 * @param excludeLevel The exclude level used for this point 
	 * @param dataPointDetails The data for this data point
	 */
	public void setDetails(int yCoord, int zCoord, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail) {
		Vector<DataPointSummary> dataPointSummaries = new Vector<BackEndAPI.DataPointSummary>(1);
		dataPointSummaries.add(dataPointSummary);
		
		Vector<DataPointDetail> dataPointDetails = new Vector<BackEndAPI.DataPointDetail>(1);
		dataPointDetails.add(dataPointDetail);
		
		setDetails(yCoord, zCoord, dataPointSummaries, dataPointDetails);
	}
	
	/**
	 * Sets data point details to be displayed
	 * @param yCoord The y-coordinate of this data point
	 * @param zCoord The z-coordinate of this data point
	 * @param dataPointSummaries The summary data for this point
	 * @param excludeLevel The exclude level used for this point 
	 * @param dataPointDetails The data for this data point
	 */
	public void setDetails(int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails) {
		mYCoord = yCoord;
		mZCoord = zCoord;
		mDataPointSummary = dataPointSummaries.firstElement();
		
		mTablePanelInterior.removeAll();
		
		// Measured velocities
		int x = 0;
		int numberOfTimeSeries = dataPointDetails.size();
		Vector<Integer> synchIndices = new Vector<Integer>(numberOfTimeSeries);
		Vector<Vector<Double>> timeSeriesList = new Vector<Vector<Double>>(numberOfTimeSeries);
		
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			synchIndices.add(dataPointSummaries.elementAt(i).get(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX).intValue());
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_RAW_X_VELOCITIES));
		}
		
		DataPointDetailFieldDisplay dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.PROBE_X_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.PROBE_X_VELOCITIES_LABEL), dpdfd);
		
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.PROBE_Y_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.PROBE_Y_VELOCITIES_LABEL), dpdfd);
		
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.PROBE_Z_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.PROBE_Z_VELOCITIES_LABEL), dpdfd);
		
		// Translated velocities
//		Vector<Double> vels = dataPointDetails.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
//		Vector<Double> highPassVels = MAJFCMaths.highPassFilter(vels, true, 20000d/(1000d*2d*Math.PI), 1d/20000d);
//		dpdfd = new DataPointDetailFieldDisplay(DAStrings.getString(DAStrings.PROBE_X_VELOCITIES_LABEL), highPassVels);
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.U_UNFILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.U_UNFILTERED_VELOCITIES_LABEL), dpdfd);
		
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.V_UNFILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.V_UNFILTERED_VELOCITIES_LABEL), dpdfd);
		
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.W_UNFILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.W_UNFILTERED_VELOCITIES_LABEL), dpdfd);
		
		// Filtered, translated and rotation corrected (if applied) velocities
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.U_FILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.U_FILTERED_VELOCITIES_LABEL), dpdfd);
		
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.V_FILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.V_FILTERED_VELOCITIES_LABEL), dpdfd);
		
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
		}
		
		dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.W_FILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList);
		mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
		mChartPanelLookup.put(DAStrings.getString(DAStrings.W_FILTERED_VELOCITIES_LABEL), dpdfd);
		
		// x Correlation (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS).size() > 0
				&& dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS).firstElement().equals(Double.NaN) == false) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS));
			}

			dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.X_CORRELATIONS_LABEL), synchIndices, timeSeriesList);
			mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
			mChartPanelLookup.put(DAStrings.getString(DAStrings.X_CORRELATIONS_LABEL), dpdfd);
		}
		
		// x SNR (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS).size() > 0
				&& dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS).firstElement().equals(Double.NaN) == false) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS));
			}

			dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.X_SNRS_LABEL), synchIndices, timeSeriesList);
			mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
			mChartPanelLookup.put(DAStrings.getString(DAStrings.X_SNRS_LABEL), dpdfd);
		}
		
		// x Correlation (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS).size() > 0
				&& dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS).firstElement().equals(Double.NaN) == false) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS));
			}

			dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.Y_CORRELATIONS_LABEL), synchIndices, timeSeriesList);
			mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
			mChartPanelLookup.put(DAStrings.getString(DAStrings.Y_CORRELATIONS_LABEL), dpdfd);
		}
		
		// x SNR (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS).size() > 0
				&& dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS).firstElement().equals(Double.NaN) == false) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS));
			}

			dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.Y_SNRS_LABEL), synchIndices, timeSeriesList);
			mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
			mChartPanelLookup.put(DAStrings.getString(DAStrings.Y_SNRS_LABEL), dpdfd);
		}
		
		// x Correlation (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS).size() > 0
				&& dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS).firstElement().equals(Double.NaN) == false) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS));
			}

			dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.Z_CORRELATIONS_LABEL), synchIndices, timeSeriesList);
			mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
			mChartPanelLookup.put(DAStrings.getString(DAStrings.Z_CORRELATIONS_LABEL), dpdfd);
		}
		
		// x SNR (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS).size() > 0
				&& dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS).firstElement().equals(Double.NaN) == false) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS));
			}

			dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.Z_SNRS_LABEL), synchIndices, timeSeriesList);
			mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
			mChartPanelLookup.put(DAStrings.getString(DAStrings.Z_SNRS_LABEL), dpdfd);
		}
		
		// Pressure (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_PRESSURE).size() > 0) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_PRESSURE));
			}
			
			dpdfd = new DataPointDetailFieldDisplay(mDataSetId, DAStrings.getString(DAStrings.PRESSURES_LABEL), synchIndices, timeSeriesList);
			mTablePanelInterior.add(dpdfd, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.VERTICAL, 0, 0, 0, 0, 0, 0));
			mChartPanelLookup.put(DAStrings.getString(DAStrings.PRESSURES_LABEL), dpdfd);
		}
		
		try {
			mSamplingRate = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
		} catch (BackEndAPIException e) {
			mSamplingRate = 1;
		}
		double numberOfVelocities = (double) timeSeriesList.firstElement().size();
		mTrimStartTime.setMaximumValue(numberOfVelocities/((double) mSamplingRate));
		mTrimEndTime.setMaximumValue(numberOfVelocities/((double) mSamplingRate));
		
		buildTimeSeriesGraphPanel();
		mTopPanel.add(DAStrings.getString(DAStrings.DATA_POINT_DATA_TABLE_TAB_LABEL), mTablePanel);
//		buildQuadrantHoleDisplays();
//		buildPowerSpectrumDisplay();

		refresh();
	}
	
	private void buildTimeSeriesGraphPanel() {
		MAJFCPanel uVelocityGraphs = new MAJFCPanel(new GridBagLayout());
		
		int x = 0, y = 0;
		uVelocityGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.U_UNFILTERED_VELOCITIES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		uVelocityGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.U_FILTERED_VELOCITIES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
			
		MAJFCPanel vVelocityGraphs = new MAJFCPanel(new GridBagLayout());
		
		x = 0;
		y = 0;
		vVelocityGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.V_UNFILTERED_VELOCITIES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		vVelocityGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.V_FILTERED_VELOCITIES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		
		MAJFCPanel wVelocityGraphs = new MAJFCPanel(new GridBagLayout());
		
		x = 0;
		y = 0;
		wVelocityGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.W_UNFILTERED_VELOCITIES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		wVelocityGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.W_FILTERED_VELOCITIES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

		MAJFCPanel pressureGraphs = null;
		
		if (mChartPanelLookup.get(DAStrings.getString(DAStrings.PRESSURES_LABEL)) != null) {
			pressureGraphs = new MAJFCPanel(new GridBagLayout());
		
			x = 0;
			y = 0;
			pressureGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.PRESSURES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
			pressureGraphs.add(mChartPanelLookup.get(DAStrings.getString(DAStrings.PRESSURES_LABEL)).getChartPanel(), MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		}
		
		if (pressureGraphs == null) {
			mTimeSeriesGraphsPanel = new ComponentTabsFrame(this, uVelocityGraphs, vVelocityGraphs, wVelocityGraphs);
		} else {
			mTimeSeriesGraphsPanel = new ComponentTabsFrame(this, uVelocityGraphs, vVelocityGraphs, wVelocityGraphs, new MAJFCTabContents(DAStrings.getString(DAStrings.PRESSURES_LABEL), pressureGraphs));
		}

		mTopPanel.add(DAStrings.getString(DAStrings.DATA_POINT_GRAPHS_TAB_LABEL), mTimeSeriesGraphsPanel);
	}

	/**
	 * Refresh the GUI
	 */
	private void refresh() {
		mYCoordLabel.setText(DAStrings.getString(DAStrings.Y_COORD_LABEL) + ": " + mYCoord);
		mZCoordLabel.setText(DAStrings.getString(DAStrings.Z_COORD_LABEL) + ": " + mZCoord);
		
		mUVFluctuationsMeanLabel.setText(DAStrings.getString(DAStrings.UV_FLUCTUATIONS_MEAN_LABEL) + DAFrame.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN), 6, true));
		mUWFluctuationsMeanLabel.setText(DAStrings.getString(DAStrings.UW_FLUCTUATIONS_MEAN_LABEL) + DAFrame.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN), 6, true));
		
		mProbeRotationInXZPlaneTheta.setText(DAStrings.getString(DAStrings.XZ_PLANE_ROTATION_THETA_LABEL) + ": " + mDataPointSummary.get(BackEndAPI.DPS_KEY_XZ_PLANE_ROTATION_THETA));
		mProbeRotationInYZPlanePhi.setText(DAStrings.getString(DAStrings.YZ_PLANE_ROTATION_PHI_LABEL) + ": " + mDataPointSummary.get(BackEndAPI.DPS_KEY_YZ_PLANE_ROTATION_PHI));
		mProbeRotationInXYPlaneAlpha.setText(DAStrings.getString(DAStrings.XY_PLANE_ROTATION_ALPHA_LABEL) + ": " + mDataPointSummary.get(BackEndAPI.DPS_KEY_XY_PLANE_ROTATION_ALPHA));

		mUSummary.mMeanValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_U_MEAN_TRANSLATED_VELOCITY), 3, true));
		mUSummary.mStDevValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_U_TRANSLATED_ST_DEV), 3, true));
		mUSummary.mFilteredMeanValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY), 3, true));
		mUSummary.mFilteredStDevValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV), 3, true));
		mUSummary.mPercentGoodValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_PERCENTAGE_OF_U_VELOCITIES_GOOD), 3, true));
		mUSummary.mMeanCorrValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_X_MEAN_CORRELATION), 3, true));
		mUSummary.mMeanSNRValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_X_MEAN_SNR), 3, true));
		
		mVSummary.mMeanValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_V_MEAN_TRANSLATED_VELOCITY), 3, true));
		mVSummary.mStDevValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_V_TRANSLATED_ST_DEV), 3, true));
		mVSummary.mFilteredMeanValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY), 3, true));
		mVSummary.mFilteredStDevValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV), 3, true));
		mVSummary.mPercentGoodValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_PERCENTAGE_OF_V_VELOCITIES_GOOD), 3, true));
		mVSummary.mMeanCorrValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_Y_MEAN_CORRELATION), 3, true));
		mVSummary.mMeanSNRValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_Y_MEAN_SNR), 3, true));
		
		mWSummary.mMeanValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_W_MEAN_TRANSLATED_VELOCITY), 3, true));
		mWSummary.mStDevValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_W_TRANSLATED_ST_DEV), 3, true));
		mWSummary.mFilteredMeanValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY), 3, true));
		mWSummary.mFilteredStDevValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV), 3, true));
		mWSummary.mPercentGoodValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_PERCENTAGE_OF_W_VELOCITIES_GOOD), 3, true));
		mWSummary.mMeanCorrValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_Z_MEAN_CORRELATION), 3, true));
		mWSummary.mMeanSNRValue.setText(MAJFCTools.formatNumber(mDataPointSummary.get(BackEndAPI.DPS_KEY_Z_MEAN_SNR), 3, true));
		
		validate();
		repaint();
	}
	
	/**
	 * Clears the data from this panel
	 */
	public void clearData() {
		mYCoord = 0;
		mZCoord = 0;
		
		mDataPointSummary = new DataPointSummary();

		mTablePanelInterior.removeAll();

		refresh();
	}
}
