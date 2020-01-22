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

package com.mikejesson.vsa.frontEnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import org.jfree.data.Range;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel.MAJFCTabContents;
import com.mikejesson.majfc.helpers.MAJFCLinkedGUIComponentsAction;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndCallBackAdapter;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CoordinateAxisIdentifer;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.backend.DataSet;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.VelocityCorrelationGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.VelocityPseudoCorrelationGraph;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointConditionalTimeSeriesDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointSignalCorrelationAndSNRDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointCorrelationsDistributionDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointDetailDataModel;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointDetailDisplayFrame;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointOffsetCorrelationsDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointPDFDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointPowerSpectrumDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointQuadrantHoleDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointWDiffDisplay;
import com.mikejesson.vsa.frontEnd.dataPoint.DataPointWaveletAnalysisDisplay;
import com.mikejesson.vsa.widgits.ComponentTabsFrame;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;
import com.mikejesson.vsa.widgits.NumberCellRenderer;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYZDataSet;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataSetDataPointsOverviewDisplay extends MAJFCStackedPanelWithFrame implements ActionListener {
	private JTable mDataPointsOverviewDataTable;
	private JTable mVelocityDataTable;
	private MyJPopupMenu mRightClickContextMenu;
	private JMenuItem mRemoveDataPointsMenuItem;
	private JMenuItem mViewDataPointDetailsMenuItem;
	private JMenuItem mSelectionToolMenuItem;
	private JMenuItem mCreateRotationCorrectionBatchesFromFileMenuItem;
	private JMenuItem mCreateRotationCorrectionBatchMenuItem;
	private JMenuItem mCalculateCorrelationsMenuItem;
	private JMenuItem mCalculatePseudoCorrelationsMenuItem;
	private JMenuItem mShowUPrimeWPrimeQuadrantHoleAnalysisMenuItem;
	private JMenuItem mShowNormalisedUPrimeWPrimeQuadrantHoleAnalysisMenuItem;
	private JMenuItem mShowUPrimeVPrimeQuadrantHoleAnalysisMenuItem;
	private JMenuItem mShowNormalisedUPrimeVPrimeQuadrantHoleAnalysisMenuItem;
	private JMenuItem mShowPowerSpectrumMenuItem;
	private JMenuItem mShowWaveletAnalysisMenuItem;
	private JMenuItem mShowSignalCorrelationAndSNRMenuItem;
	private JMenuItem mShowWDiffMenuItem;
	private JMenuItem mShowPDFMenuItem;
	private JMenuItem mShowConditionalTimeSeriesMenuItem;
	private JMenuItem mShowOffsetCorrelationsMenuItem;
	private JMenuItem mShowCorrelationsDistributionMenuItem;
	private JMenu mSaveToFileMenu;
	private JMenuItem mSaveFTRCToASCIIFileMenuItem;
	private JMenuItem mSaveAllToASCIIFileMenuItem;
	private JMenuItem mSaveFTRCToMatlabFileMenuItem;
	private JMenuItem mSaveAllToMatlabFileMenuItem;
	private JMenu mSendToMatlabMenu;
	private JMenuItem mSendAllToMatlabMenuItem;
	private JMenuItem mSendFTRCToMatlabMenuItem;
	private DataPointOverviewDataModel mDataPointOverviewDataModel = new DataPointOverviewDataModel();
	private VelocitiesDetailsDataModel mVelocitiesDetailsDataModel = new VelocitiesDetailsDataModel();
	private JSplitPane mSplitPanel;
	private ComponentTabsFrame mVelocityCrossSectionGraphsHolder;
	private DataSetOverviewChartPanel mUVelocityCrossSectionGraph;
	private DataSetOverviewChartPanel mVVelocityCrossSectionGraph;
	private DataSetOverviewChartPanel mWVelocityCrossSectionGraph;
	private MAJFCPanel mTablesPanel;
	private AbstractDataSetUniqueId mDataSetId;
	private MyBackEndAPICallbackInterface mBackEndAPICallbackInterface = new MyBackEndAPICallbackInterface();
	private int mDataPointLoadRequestId = 0;
	private Hashtable<Integer, String> mDataPointLoadRequestLookup = new Hashtable<Integer, String>();
	private final DataSetType mDataSetType;
	private final DataSetDataPointsOverviewDisplay mMe;
	private DataPointDetailDisplayFrame mDataPointDetailDisplay;
	
	// The frame to display when the TableActionProgressTask completes
	private JFrame mLoadTaskFrame;
	
	/**
	 * Constructs the display
	 */
	public DataSetDataPointsOverviewDisplay(AbstractDataSetUniqueId dataSetId, DataSetType dataSetType) {
		super(new GridBagLayout());
		
		mDataSetId = dataSetId;
		mDataSetType = dataSetType;
		mDataPointDetailDisplay = new DataPointDetailDisplayFrame(mDataSetId);
		mMe = this;
		
		buildGUI();
	}
	
	/**
	 * Builds the GUI
	 */
	private void buildGUI() {
		mRightClickContextMenu = new MyJPopupMenu();
		mViewDataPointDetailsMenuItem = new JMenuItem();
		mRemoveDataPointsMenuItem = new JMenuItem();
		mSelectionToolMenuItem = new JMenuItem();
		mCreateRotationCorrectionBatchMenuItem = new JMenuItem();
		mCreateRotationCorrectionBatchesFromFileMenuItem = new JMenuItem();
		mCalculateCorrelationsMenuItem = new JMenuItem();
		mCalculatePseudoCorrelationsMenuItem = new JMenuItem();
		mShowUPrimeWPrimeQuadrantHoleAnalysisMenuItem = new JMenuItem();
		mShowNormalisedUPrimeWPrimeQuadrantHoleAnalysisMenuItem = new JMenuItem();
		mShowUPrimeVPrimeQuadrantHoleAnalysisMenuItem = new JMenuItem();
		mShowNormalisedUPrimeVPrimeQuadrantHoleAnalysisMenuItem = new JMenuItem();
		mShowPowerSpectrumMenuItem = new JMenuItem();
		mShowWaveletAnalysisMenuItem = new JMenuItem();
		mShowSignalCorrelationAndSNRMenuItem = new JMenuItem();
		mShowWDiffMenuItem = new JMenuItem();
		mShowPDFMenuItem = new JMenuItem();
		mShowConditionalTimeSeriesMenuItem = new JMenuItem();
		mShowOffsetCorrelationsMenuItem = new JMenuItem();
		mShowCorrelationsDistributionMenuItem = new JMenuItem();
		mSaveToFileMenu = new JMenu(DAStrings.getString(DAStrings.SAVE_DETAILS_TO_FILE_MENU_LABEL));
		mSaveFTRCToASCIIFileMenuItem = new JMenuItem();
		mSaveAllToASCIIFileMenuItem = new JMenuItem();
		mSaveFTRCToMatlabFileMenuItem = new JMenuItem();
		mSaveAllToMatlabFileMenuItem = new JMenuItem();
		
		mSendToMatlabMenu = new JMenu(DAStrings.getString(DAStrings.SEND_DETAILS_TO_MATLAB_MENU_LABEL));
		mSendAllToMatlabMenuItem = new JMenuItem();
		mSendFTRCToMatlabMenuItem = new JMenuItem();
		
		mRightClickContextMenu.add(mViewDataPointDetailsMenuItem);
		mRightClickContextMenu.add(mRemoveDataPointsMenuItem);
		mRightClickContextMenu.add(mShowSignalCorrelationAndSNRMenuItem);
		mRightClickContextMenu.add(mShowWDiffMenuItem);
		mRightClickContextMenu.add(mSelectionToolMenuItem);
		mRightClickContextMenu.add(mCreateRotationCorrectionBatchMenuItem);
		mRightClickContextMenu.add(mCreateRotationCorrectionBatchesFromFileMenuItem);
		mRightClickContextMenu.add(mCalculateCorrelationsMenuItem);
		mRightClickContextMenu.add(mCalculatePseudoCorrelationsMenuItem);
		mRightClickContextMenu.add(mShowUPrimeWPrimeQuadrantHoleAnalysisMenuItem);
		mRightClickContextMenu.add(mShowNormalisedUPrimeWPrimeQuadrantHoleAnalysisMenuItem);
		mRightClickContextMenu.add(mShowUPrimeVPrimeQuadrantHoleAnalysisMenuItem);
		mRightClickContextMenu.add(mShowNormalisedUPrimeVPrimeQuadrantHoleAnalysisMenuItem);
		mRightClickContextMenu.add(mShowPowerSpectrumMenuItem);
		mRightClickContextMenu.add(mShowWaveletAnalysisMenuItem);
		mRightClickContextMenu.add(mShowPDFMenuItem);
		mRightClickContextMenu.add(mShowConditionalTimeSeriesMenuItem);
		mRightClickContextMenu.add(mShowOffsetCorrelationsMenuItem);
		mRightClickContextMenu.add(mShowCorrelationsDistributionMenuItem);
		mRightClickContextMenu.add(mSaveToFileMenu);
		mSaveToFileMenu.add(mSaveFTRCToASCIIFileMenuItem);
		mSaveToFileMenu.add(mSaveAllToASCIIFileMenuItem);
		mSaveToFileMenu.add(mSaveFTRCToMatlabFileMenuItem);
		mSaveToFileMenu.add(mSaveAllToMatlabFileMenuItem);
		mRightClickContextMenu.add(mSendToMatlabMenu);
		mSendToMatlabMenu.add(mSendAllToMatlabMenuItem);
		mSendToMatlabMenu.add(mSendFTRCToMatlabMenuItem);
		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_DESC), mViewDataPointDetailsMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.REMOVE_DATA_POINTS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.REMOVE_DATA_POINTS_BUTTON_DESC), mRemoveDataPointsMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SELECTION_TOOL_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SELECTION_TOOL_BUTTON_DESC), mSelectionToolMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.CREATE_ROTATION_CORRECTION_BATCH_BUTTON_DESC), mCreateRotationCorrectionBatchMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_DESC), mCreateRotationCorrectionBatchesFromFileMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_CORRELATION_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.CALCULATE_CORRELATION_BUTTON_DESC), mCalculateCorrelationsMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.CALCULATE_PSEUDO_CORRELATION_BUTTON_DESC), mCalculatePseudoCorrelationsMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC), mShowUPrimeWPrimeQuadrantHoleAnalysisMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC), mShowNormalisedUPrimeWPrimeQuadrantHoleAnalysisMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC), mShowUPrimeVPrimeQuadrantHoleAnalysisMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC), mShowNormalisedUPrimeVPrimeQuadrantHoleAnalysisMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_SPECTRAL_DISTRIBUTION_BUTTON_DESC), mShowPowerSpectrumMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_WAVELET_ANALYSIS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_WAVELET_ANALYSIS_BUTTON_DESC), mShowWaveletAnalysisMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_DESC), mShowSignalCorrelationAndSNRMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_W_DIFF_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_W_DIFF_BUTTON_DESC), mShowWDiffMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_PDF_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_PDF_BUTTON_DESC), mShowPDFMenuItem, this);		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_CONDITIONAL_TIME_SERIES_BUTTON_DESC), mShowConditionalTimeSeriesMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_OFFSET_CORRELATIONS_BUTTON_DESC), mShowOffsetCorrelationsMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_DESC), mShowCorrelationsDistributionMenuItem, this);
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_DESC), mSaveFTRCToASCIIFileMenuItem, this);		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_DESC), mSaveAllToASCIIFileMenuItem, this);		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_DESC), mSaveFTRCToMatlabFileMenuItem, this);		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_DESC), mSaveAllToMatlabFileMenuItem, this);		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_DESC), mSendFTRCToMatlabMenuItem, this);		
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL), DataAnalyser.getImageIcon(""), DAStrings.getString(DAStrings.SEND_ALL_DETAILS_TO_MATLAB_BUTTON_DESC), mSendAllToMatlabMenuItem, this);		
		
		mDataPointsOverviewDataTable = new JTable(mDataPointOverviewDataModel);
		mDataPointsOverviewDataTable.setDefaultRenderer(Object.class, new NumberCellRenderer());
		mDataPointsOverviewDataTable.setFillsViewportHeight(true);
		mDataPointsOverviewDataTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent theEvent) {
				if ((theEvent.getModifiers() & MouseEvent.BUTTON3_MASK) > 0) {
//					mCalculateUCorrelationMenuItem.setEnabled(mDataPointsOverviewDataTable.getSelectedRowCount() > 1);
					mRightClickContextMenu.show(mDataPointsOverviewDataTable, theEvent.getX(), theEvent.getY());
					return;
				}

				if (theEvent.getClickCount() == 2) {
					tableAction(DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_LABEL));
				}
				
//				int row = mDataPointsOverviewDataTable.convertRowIndexToModel(mDataPointsOverviewDataTable.getSelectedRow());
//				int yCoord = ((Integer) (mVelocitiesDetailsDataModel.getNumberValueAt(row, 0))).intValue();
//				int zCoord = ((Integer) (mVelocitiesDetailsDataModel.getNumberValueAt(row, 1))).intValue();
//				
//				loadAndOpenDataPoint(yCoord, zCoord);
			}
		});

		mVelocityDataTable = new JTable(mVelocitiesDetailsDataModel);
		mVelocityDataTable.setDefaultRenderer(Object.class, new NumberCellRenderer());
		mVelocityDataTable.setFillsViewportHeight(true);
		mVelocityDataTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent theEvent) {
				if ((theEvent.getModifiers() & MouseEvent.BUTTON3_MASK) > 0) {
//					mCalculateUCorrelationMenuItem.setEnabled(mVelocityDataTable.getSelectedRowCount() > 1);
					mRightClickContextMenu.show(mVelocityDataTable, theEvent.getX(), theEvent.getY());
					return;
				}

				if (theEvent.getClickCount() == 2) {
					tableAction(DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_LABEL));
				}
//				
//				int row = mVelocityDataTable.convertRowIndexToModel(mVelocityDataTable.getSelectedRow());
//				int yCoord = ((Integer) (mVelocitiesDetailsDataModel.getNumberValueAt(row, 0))).intValue();
//				int zCoord = ((Integer) (mVelocitiesDetailsDataModel.getNumberValueAt(row, 1))).intValue();
//				
//				loadAndOpenDataPoint(yCoord, zCoord);
			}
		});
		
		List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		TableRowSorter<VelocitiesDetailsDataModel> sorter = new TableRowSorter<VelocitiesDetailsDataModel>(mVelocitiesDetailsDataModel);
		sorter.setSortKeys(sortKeys);
		mDataPointsOverviewDataTable.setRowSorter(sorter);
		mVelocityDataTable.setRowSorter(sorter);
		sorter.setComparator(0, new Comparator<Integer>() {
			@Override
			public int compare(Integer first, Integer second) {
				return first - second;
			}
		});
		sorter.setComparator(1, new Comparator<Integer>() {
			@Override
			public int compare(Integer first, Integer second) {
				return first - second;
			}
		});
	
		String legendText = DAStrings.getString(DAStrings.U_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT);
		int leftBankPosition = 0, rightBankPosition = 0, waterDepth = 0;

		try {
			leftBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue();
			rightBankPosition = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue();
			waterDepth = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_WATER_DEPTH).intValue();
		} catch (Exception e) {
		}
		
		String rawChartTitle = DAStrings.getString(DAStrings.U_VELOCITY_CROSS_SECTION_GRID_TITLE);
		mUVelocityCrossSectionGraph = new DataSetOverviewChartPanel(mDataSetId, new VelocitiesXYZDataSet(leftBankPosition, rightBankPosition, 0, waterDepth) {
			@Override
			public double getTheZValue(int series, int item) {
				return ((Double) mVelocitiesDetailsDataModel.getNumberValueAt(item, mVelocitiesDetailsDataModel.U_MEAN_COLUMN_INDEX))/DAFrame.getFrame().getChannelMeanVelocityForScaling(mDataSetId, mDataSetType);
			}
		}, rawChartTitle, BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_U_VELOCITY, BackEndAPI.CSD_KEY_MEAN_RMS_U_PRIME, legendText, mDataSetType, false);
		
		legendText = DAStrings.getString(DAStrings.V_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT);
		rawChartTitle = DAStrings.getString(DAStrings.V_VELOCITY_CROSS_SECTION_GRID_TITLE);
		mVVelocityCrossSectionGraph = new DataSetOverviewChartPanel(mDataSetId, new VelocitiesXYZDataSet(leftBankPosition, rightBankPosition, 0, waterDepth) {
			@Override
			public double getTheZValue(int series, int item) {
				return ((Double) mVelocitiesDetailsDataModel.getNumberValueAt(item, mVelocitiesDetailsDataModel.V_MEAN_COLUMN_INDEX))/DAFrame.getFrame().getChannelMeanVelocityForScaling(mDataSetId, mDataSetType);
			}
		}, rawChartTitle, BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_V_VELOCITY, BackEndAPI.CSD_KEY_RMS_FTRC_V, legendText, mDataSetType, true);
		
		legendText = DAStrings.getString(DAStrings.W_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT);
		rawChartTitle = DAStrings.getString(DAStrings.W_VELOCITY_CROSS_SECTION_GRID_TITLE);
		mWVelocityCrossSectionGraph = new DataSetOverviewChartPanel(mDataSetId, new VelocitiesXYZDataSet(leftBankPosition, rightBankPosition, 0, waterDepth) {
			@Override
			public double getTheZValue(int series, int item) {
				return ((Double) mVelocitiesDetailsDataModel.getNumberValueAt(item, mVelocitiesDetailsDataModel.W_MEAN_COLUMN_INDEX))/DAFrame.getFrame().getChannelMeanVelocityForScaling(mDataSetId, mDataSetType);
			}
		}, rawChartTitle, BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_W_VELOCITY, BackEndAPI.CSD_KEY_RMS_FTRC_W, legendText, mDataSetType, false);
		
		mVelocityCrossSectionGraphsHolder = new ComponentTabsFrame(this, mUVelocityCrossSectionGraph, mVVelocityCrossSectionGraph, mWVelocityCrossSectionGraph);
				
		int y = 0;
		MAJFCPanel dpOverviewTablePanel = new MAJFCPanel(new GridBagLayout());
		JScrollPane dpOverviewTableScroll = new JScrollPane(mDataPointsOverviewDataTable);
		dpOverviewTablePanel.add(mDataPointsOverviewDataTable.getTableHeader(), MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));
		dpOverviewTablePanel.add(dpOverviewTableScroll, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

		y = 0;
		MAJFCPanel velocitiesTablePanel = new MAJFCPanel(new GridBagLayout());
		JScrollPane velocitiesTableScroll = new JScrollPane(mVelocityDataTable);
		velocitiesTablePanel.add(mVelocityDataTable.getTableHeader(), MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));
		velocitiesTablePanel.add(velocitiesTableScroll, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

		JTabbedPane tablesTabs = new JTabbedPane();
		tablesTabs.setTabPlacement(JTabbedPane.BOTTOM);
		tablesTabs.add(DAStrings.getString(DAStrings.DATA_POINT_OVERVIEW_TABLE_TAB), dpOverviewTablePanel);
		tablesTabs.add(DAStrings.getString(DAStrings.VELOCITY_DETAILS_TABLE_TAB), velocitiesTablePanel);
		
		mTablesPanel = new MAJFCPanel(new GridBagLayout());
		mTablesPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		mTablesPanel.add(tablesTabs, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 5, 5, 5, 5, 0, 0));

		mSplitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mVelocityCrossSectionGraphsHolder, mTablesPanel);
		mSplitPanel.setResizeWeight(0.7);

		y = 0;
//		mSplitPanel.add(mVelocityCrossSectionGraphsHolder, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 0.7, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 10, 5, 0, 5, 0, 0));
//		mSplitPanel.add(mTablesPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 10, 5, 0, 5, 0, 0));
		
		add(mSplitPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}

	/**
	 * Calculates the correlation of the u-velocities for the specified data points 
	 * @param yCoords The y-coordinates of the points to do the calculation for
	 * @param zCoords The z-coordinates of the points to do the calculation for
	 */
	private void calculateCorrelations(Vector<Integer> yCoords, Vector<Integer> zCoords) {
		try {
			BackEndAPI.getBackEndAPI().calculateCorrelationBetweenComponentsOfDataPoints(mDataSetId, yCoords, zCoords, mBackEndAPICallbackInterface);
		} catch (BackEndAPIException theException) {
			theException.printStackTrace();
		}
	}
	
	/**
	 * Calculates the correlation of the u-velocities for the specified data points 
	 * @param yCoords The y-coordinates of the points to do the calculation for
	 * @param zCoords The z-coordinates of the points to do the calculation for
	 */
	private void calculatePseudoCorrelations(Vector<Integer> yCoords, Vector<Integer> zCoords) {
		try {
			BackEndAPI.getBackEndAPI().calculatePseudoCorrelationBetweenComponentsOfDataPoints(mDataSetId, yCoords, zCoords, mBackEndAPICallbackInterface);
		} catch (BackEndAPIException theException) {
			theException.printStackTrace();
		}
	}

	/**
	 * Removes the specified data points from the data set 
	 * @param yCoords The y-coordinates of the points to remove
	 * @param zCoords The z-coordinates of the points to remove
	 */
	private void removeDataPoints(Vector<Integer> yCoords, Vector<Integer> zCoords) {
		try {
			BackEndAPI.getBackEndAPI().removeDataPoints(mDataSetId, yCoords, zCoords, mBackEndAPICallbackInterface);
		} catch (BackEndAPIException theException) {
			theException.printStackTrace();
		}
	}

	/**
	 * Loads details for the specified data points and opens the data point display
	 * @param yCoords The y-coordinates of the data points
	 * @param zCoords The z-coordinates of the data points
	 */
	private void loadAndOpenDataPoints(Vector<Integer> yCoords, Vector<Integer> zCoords) {
		int numberOfDPs = yCoords.size();
		
		for (int i = 0; i < numberOfDPs; i++) {
			loadAndOpenDataPoint(yCoords.elementAt(i), zCoords.elementAt(i));
		}
	}
	
	/**
	 * Loads details for the specified data point and opens the data point display
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 */
	private void loadAndOpenDataPoint(int yCoord, int zCoord) {
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			if (DataAnalyser.sShowAllMRRuns && (mDataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE) || mDataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE))) {
				DAFrame.getBackEndAPI().loadDataPointDetailsWithLinkedDPs(0, mDataSetId, yCoord, zCoord, mBackEndAPICallbackInterface);
			} else {
				DAFrame.getBackEndAPI().loadDataPointDetails(0, mDataSetId, yCoord, zCoord, mBackEndAPICallbackInterface);
			}
		} catch (Exception theException) {
			MAJFCLogger.log(theException.getMessage());
			theException.printStackTrace();
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	/**
	 * Updates the display
	 */
	public void updateDisplay() {
		if (DAFrame.getBackEndAPI() != null) {
			try {
				mDataPointOverviewDataModel.mCoords = DAFrame.getBackEndAPI().getUnsortedDataPointCoordinates(mDataSetId);
				mVelocitiesDetailsDataModel.mCoords = DAFrame.getBackEndAPI().getUnsortedDataPointCoordinates(mDataSetId);
			} catch (BackEndAPIException theException) {
				MAJFCLogger.log("Failed to get coordinates in DataSetSummaryDisplay::updateDisplay.");
				MAJFCLogger.log(theException.getMessage());
				theException.printStackTrace();
			}
		}
		
		mVelocitiesDetailsDataModel.fireTableDataChanged();

		mUVelocityCrossSectionGraph.updateDisplay();
		mVVelocityCrossSectionGraph.updateDisplay();
		mWVelocityCrossSectionGraph.updateDisplay();
		
		repaint();
	}
	
	/**
	 * Inner class providing a data model for the data point overview table
	 * @author MAJ727
	 *
	 */
	public class DataPointOverviewDataModel extends AbstractTableModel {
		public final Integer Y_COORD_COLUMN_INDEX = 0;
		public final Integer Z_COORD_COLUMN_INDEX = 1;

		private Integer[][] mCoords = {};
		private final String[] mColTitles = {	DAStrings.getString(DAStrings.Y_COORD_LABEL),
												DAStrings.getString(DAStrings.Z_COORD_LABEL),
												DAStrings.getString(DAStrings.U_FILTERED_MEAN_LABEL),
												DAStrings.getString(DAStrings.U_FILTERED_STDEV_LABEL),
												DAStrings.getString(DAStrings.V_FILTERED_MEAN_LABEL),
												DAStrings.getString(DAStrings.V_FILTERED_STDEV_LABEL),
												DAStrings.getString(DAStrings.W_FILTERED_MEAN_LABEL),
												DAStrings.getString(DAStrings.W_FILTERED_STDEV_LABEL),
												DAStrings.getString(DAStrings.DATA_POINT_OVERVIEW_TABLE_PROBE_TYPE_COL_HEADER),
												DAStrings.getString(DAStrings.DATA_POINT_OVERVIEW_TABLE_PROBE_ID_COL_HEADER),
												DAStrings.getString(DAStrings.DATA_POINT_OVERVIEW_TABLE_SAMPLING_RATE_COL_HEADER) };
		
		/**
		 * Gets the column title
		 * @return the column title
		 */
		public String getColumnName(int i){
			return mColTitles[i];
		}
		
		/**
		 * Gets the number of columns in the model/grid
		 * @return the number of columns in the model/grid
		 */
		@Override
		public int getColumnCount() {
			return mColTitles.length;
		}

		/**
		 * Gets the number of rows in the model/grid
		 * @return the number of rows in the model/grid
		 */
		@Override
		public int getRowCount() {
			return mCoords.length;
		}

		/**
		 * Gets the value to display in the grid at the given cell
		 * @param rowIndex The row number of the cell the value is required for
		 * @param columnIndex The column number of the cell the value is required for
		 * @return the value to display in the specified cell
		 */
		public Object getNumberValueAt(int rowIndex, int columnIndex) {
			try {
				switch (columnIndex){
					case 2:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					case 3:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
					case 4:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					case 5:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
					case 6:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					case 7:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
					case 8:
						return DAFrame.getBackEndAPI().getDataPointProbeDetailsFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.PD_KEY_PROBE_TYPE);
					case 9:
						return DAFrame.getBackEndAPI().getDataPointProbeDetailsFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.PD_KEY_PROBE_ID);
					case 10:
						return DAFrame.getBackEndAPI().getDataPointProbeDetailsFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.PD_KEY_SAMPLING_RATE);
					default:
						return mCoords[rowIndex][columnIndex];
				}
			} catch (BackEndAPIException theException) {
				MAJFCLogger.log(theException.getMessage());
				theException.printStackTrace();
				return -999999;
			}
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * Gets the value to display in the grid at the given cell
		 * @param rowIndex The row number of the cell the value is required for
		 * @param columnIndex The column number of the cell the value is required for
		 * @return the value to display in the specified cell, formatted
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object value = getNumberValueAt(rowIndex, columnIndex);
			Class valueClass = value.getClass();
			
			if (valueClass.equals(String.class)) {
				return value;
			} else if (valueClass.equals(Integer.class)) {
				return value;
			} else if (valueClass.equals(Double.class)) {
				return MAJFCTools.formatNumber((Double) value, 4, true);
			} 

			return "Unknown Type";
		}
			
		public boolean isEmpty() {
			return mCoords.length == 0;
		}
	}

	/**
	 * Inner class providing a data model for the velocities table
	 * @author MAJ727
	 *
	 */
	public class VelocitiesDetailsDataModel extends AbstractTableModel {
		public final Integer Y_COORD_COLUMN_INDEX = 0;
		public final Integer Z_COORD_COLUMN_INDEX = 1;
		
		public final Integer U_MEAN_COLUMN_INDEX = 4;
		public final Integer V_MEAN_COLUMN_INDEX = 8;
		public final Integer W_MEAN_COLUMN_INDEX = 12;

		private Integer[][] mCoords = {};
		private final String[] mColTitles = {	DAStrings.getString(DAStrings.Y_COORD_LABEL),
												DAStrings.getString(DAStrings.Z_COORD_LABEL),
												DAStrings.getString(DAStrings.U_MEAN_LABEL),
												DAStrings.getString(DAStrings.U_STDEV_LABEL),
												DAStrings.getString(DAStrings.U_FILTERED_MEAN_LABEL),
												DAStrings.getString(DAStrings.U_FILTERED_STDEV_LABEL),
												DAStrings.getString(DAStrings.V_MEAN_LABEL),
												DAStrings.getString(DAStrings.V_STDEV_LABEL),
												DAStrings.getString(DAStrings.V_FILTERED_MEAN_LABEL),
												DAStrings.getString(DAStrings.V_FILTERED_STDEV_LABEL),
												DAStrings.getString(DAStrings.W_MEAN_LABEL),
												DAStrings.getString(DAStrings.W_STDEV_LABEL),
												DAStrings.getString(DAStrings.W_FILTERED_MEAN_LABEL),
												DAStrings.getString(DAStrings.W_FILTERED_STDEV_LABEL),
												DAStrings.getString(DAStrings.BATCH_THETA_ROTATION_CORRECTION_COLUMN_TITLE),
												DAStrings.getString(DAStrings.BATCH_ALPHA_ROTATION_CORRECTION_COLUMN_TITLE),
												DAStrings.getString(DAStrings.BATCH_PHI_ROTATION_CORRECTION_COLUMN_TITLE),
												DAStrings.getString(DAStrings.BATCH_ROTATION_CORRECTION_BATCH_NUMBER_COLUMN_TITLE) };
		
		/**
		 * Gets the column title
		 * @return the column title
		 */
		public String getColumnName(int i){
			return mColTitles[i];
		}
		
		/**
		 * Gets the number of columns in the model/grid
		 * @return the number of columns in the model/grid
		 */
		@Override
		public int getColumnCount() {
			return mColTitles.length;
		}

		/**
		 * Gets the number of rows in the model/grid
		 * @return the number of rows in the model/grid
		 */
		@Override
		public int getRowCount() {
			return mCoords.length;
		}

		/**
		 * Gets the value to display in the grid at the given cell
		 * @param rowIndex The row number of the cell the value is required for
		 * @param columnIndex The column number of the cell the value is required for
		 * @return the value to display in the specified cell
		 */
		public Object getNumberValueAt(int rowIndex, int columnIndex) {
			try {
				switch (columnIndex){
					case 2:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_U_MEAN_TRANSLATED_VELOCITY);
					case 3:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_U_TRANSLATED_ST_DEV);
					case 4:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					case 5:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
					case 6:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_V_MEAN_TRANSLATED_VELOCITY);
					case 7:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_V_TRANSLATED_ST_DEV);
					case 8:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					case 9:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
					case 10:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_W_MEAN_TRANSLATED_VELOCITY);
					case 11:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_W_TRANSLATED_ST_DEV);
					case 12:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					case 13:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
					case 14:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_BATCH_THETA_ROTATION_CORRECTION);
					case 15:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_BATCH_ALPHA_ROTATION_CORRECTION);
					case 16:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_BATCH_PHI_ROTATION_CORRECTION);
					case 17:
						return DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(mDataSetId, mCoords[rowIndex][Y_COORD_COLUMN_INDEX], mCoords[rowIndex][Z_COORD_COLUMN_INDEX], BackEndAPI.DPS_KEY_BATCH_NUMBER);
					default:
						return mCoords[rowIndex][columnIndex];
				}
			} catch (BackEndAPIException theException) {
				MAJFCLogger.log(theException.getMessage());
				theException.printStackTrace();
				return -999999;
			}
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * Gets the value to display in the grid at the given cell
		 * @param rowIndex The row number of the cell the value is required for
		 * @param columnIndex The column number of the cell the value is required for
		 * @return the value to display in the specified cell, formatted
		 */
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object value = getNumberValueAt(rowIndex, columnIndex);
			Class valueClass = value.getClass();
			
			if (valueClass.equals(Integer.class)) {
				return value;
			} else if (valueClass.equals(Double.class)) {
				return MAJFCTools.formatNumber((Double) value, 4, true);
			} 

			return "Unknown Type";
		}
			
		@Override
		public void fireTableDataChanged() {
			int xMax = 0;
			int xMin = 0;
			int yMax = 0;
			int yMin = 0;
			Range xRange = null;
			Range yRange = null;
			
			for (int i = 0; i < getRowCount(); ++i) {
				int iX = (Integer) getNumberValueAt(i, Y_COORD_COLUMN_INDEX);
				xMax = iX > xMax ? iX : xMax;
				xMin = iX < xMin ? iX : xMin;
				xRange = new Range(xMin, xMax);

				int iY = (Integer) getNumberValueAt(i, Z_COORD_COLUMN_INDEX);
				yMax = iY > yMax ? iY : yMax;
				yMin = iY < yMin ? iY : yMin;
				yRange = new Range(yMin, yMax);
			}
			
			mUVelocityCrossSectionGraph.setRanges(xRange, yRange);
			mVVelocityCrossSectionGraph.setRanges(xRange, yRange);
			mWVelocityCrossSectionGraph.setRanges(xRange, yRange);
			
			super.fireTableDataChanged();
		}
		
		public boolean isEmpty() {
			return mCoords.length == 0;
		}
	}
	
	private abstract class VelocitiesXYZDataSet extends ScaleableXYZDataSet {
		private int[][] mBoundaryCoords;
		private final int BOUNDARY_COORDS_Y_COORD = 0;
		private final int BOUNDARY_COORDS_Z_COORD = 1;
		
		public VelocitiesXYZDataSet(int unscaledXMin, int unscaledXMax,	int unscaledYMin, int unscaledYMax) {
			super(1, 1, unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
			importBoundaryCoords();
		}

		private void importBoundaryCoords() {
			LinkedHashMap<Integer, Integer> boundaryCoords;
			try {
				boundaryCoords = DAFrame.getBackEndAPI().getConfigData(mDataSetId).getBoundaryCoords();
				int i = 0;
				mBoundaryCoords = new int[boundaryCoords.size()][2];
				
				for (Entry<Integer, Integer> coords : boundaryCoords.entrySet()) {
					mBoundaryCoords[i][BOUNDARY_COORDS_Y_COORD] = coords.getKey();
					mBoundaryCoords[i++][BOUNDARY_COORDS_Z_COORD] = coords.getValue();
				}
			} catch (BackEndAPIException e) {
			}
		}

		@Override
		public int getSeriesCount() {
			return 2;
		}
		
		@Override
		/**
		 * ScaleableXYDataset implementation
		 */
		public int getItemCount(int series) {
			return series == 0 ? mVelocitiesDetailsDataModel.getRowCount() : mBoundaryCoords.length;
		}

		@Override
		/**
		 * ScaleableXYDataset implementation
		 */
		public double getTheXValue(int series, int item) {
			return series == 0 ? (Integer) mVelocitiesDetailsDataModel.getNumberValueAt(item, mVelocitiesDetailsDataModel.Y_COORD_COLUMN_INDEX) : mBoundaryCoords[item][BOUNDARY_COORDS_Y_COORD];
		}

		@Override
		/**
		 * ScaleableXYDataset implementation
		 */
		public double getTheYValue(int series, int item) {
			return series == 0 ? (Integer) mVelocitiesDetailsDataModel.getNumberValueAt(item, mVelocitiesDetailsDataModel.Z_COORD_COLUMN_INDEX) : mBoundaryCoords[item][BOUNDARY_COORDS_Z_COORD];
		}
		
		@Override
		/**
		 * Scal
		 */
		public double getZValue(int series, int item) {
			return series == 0 ? super.getZValue(series, item) : 1d;
		}
		
		/**
		 * Sets the ranges for the two axes
		 * @param domainAxisXMin
		 * @param domainAxisXMax
		 * @param rangeAxisYMin
		 * @param rangeAxisYMax
		 */
		@Override
		protected void setNonDimensionlessAxisRange(int domainAxisXMin, int domainAxisXMax, int rangeAxisYMin, int rangeAxisYMax) {
			super.setNonDimensionlessAxisRange(domainAxisXMin, domainAxisXMax, rangeAxisYMin, rangeAxisYMax);
			
			importBoundaryCoords();
		}
	}
	
	/**
	 * Sets the data set id
	 * @param dataSetId The new data set id
	 */
	public void setDataSetId(AbstractDataSetUniqueId dataSetId) {
		mDataSetId = dataSetId;
		mUVelocityCrossSectionGraph.setDataSetId(mDataSetId);
		mVVelocityCrossSectionGraph.setDataSetId(mDataSetId);
		mWVelocityCrossSectionGraph.setDataSetId(mDataSetId);
	}
	
	private class MyBackEndAPICallbackInterface extends BackEndCallBackAdapter {
		@Override
		public void onCorrelationsCalculated(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Double> uCorrelations, Vector<Double> vCorrelations, Vector<Double> wCorrelations) {
			ComponentTabsFrame ctp = new ComponentTabsFrame(mMe,
					new VelocityCorrelationGraph(mDataSetId, null, yCoords, zCoords, uCorrelations, DAStrings.getString(DAStrings.U_VELOCITY_CORRELATIONS_GRAPH_TITLE)),
					new VelocityCorrelationGraph(mDataSetId, null, yCoords, zCoords, vCorrelations, DAStrings.getString(DAStrings.V_VELOCITY_CORRELATIONS_GRAPH_TITLE)),
					new VelocityCorrelationGraph(mDataSetId, null, yCoords, zCoords, wCorrelations, DAStrings.getString(DAStrings.W_VELOCITY_CORRELATIONS_GRAPH_TITLE)));

			mLoadTaskFrame = ctp.showInFrame(DAStrings.getString(DAStrings.VELOCITY_CORRELATIONS_GRAPH_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
		}
		
		@Override
		public void onPseudoCorrelationsCalculated(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<Double> uCorrelations, Vector<Double> vCorrelations, Vector<Double> wCorrelations) {
			ComponentTabsFrame ctp = new ComponentTabsFrame(mMe,
					new VelocityPseudoCorrelationGraph(mDataSetId, null, yCoords, zCoords, uCorrelations, DAStrings.getString(DAStrings.U_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE)),
					new VelocityPseudoCorrelationGraph(mDataSetId, null, yCoords, zCoords, vCorrelations, DAStrings.getString(DAStrings.V_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE)),
					new VelocityPseudoCorrelationGraph(mDataSetId, null, yCoords, zCoords, wCorrelations, DAStrings.getString(DAStrings.W_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE)));

			mLoadTaskFrame = ctp.showInFrame(DAStrings.getString(DAStrings.VELOCITY_PSEUDO_CORRELATIONS_GRAPH_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
		}
		
		@Override
		public void onDataPointDetailsLoaded(int requestId, AbstractDataSetUniqueId dataSetId,	Vector<Integer> yCoords, Vector<Integer> zCoords, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails) {
			String command = mDataPointLoadRequestLookup.get(requestId);
			
			if (command == null || command.equals(DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				for (int i = 0; i < numberOfDataPoints; ++i) {
					mLoadTaskFrame = mDataPointDetailDisplay.addDataPoint(yCoords.elementAt(i), zCoords.elementAt(i), dataPointSummaries, dataPointDetails);
				}
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL)) || command.equals(DAStrings.getString(DAStrings.SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))
					|| command.equals(DAStrings.getString(DAStrings.SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL)) || command.equals(DAStrings.getString(DAStrings.SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				Vector<String> legendEntries = new Vector<String>(numberOfDataPoints);
				Vector<Vector<Double>> uVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wOrVVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				CoordinateAxisIdentifer secondAxis = command.equals(DAStrings.getString(DAStrings.SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))
				|| command.equals(DAStrings.getString(DAStrings.SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL)) ? BackEndAPI.Z_AXIS_OR_W_VELOCITY : BackEndAPI.Y_AXIS_OR_V_VELOCITY;
				
				for (int i = 0; i < numberOfDataPoints; ++i) {
					legendEntries.add(DATools.makeDataPointIdentifier(yCoords.elementAt(i), zCoords.elementAt(i)));
					uVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
					if (secondAxis.equals(BackEndAPI.Z_AXIS_OR_W_VELOCITY)) {
						wOrVVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
					} else {
						wOrVVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
					}
				}
				
				boolean normalise = command.equals(DAStrings.getString(DAStrings.SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL)) || command.equals(DAStrings.getString(DAStrings.SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL)); 
				DataPointQuadrantHoleDisplay quadrantHoleDisplay = new DataPointQuadrantHoleDisplay(mDataSetId, secondAxis, mMe, DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_TITLE), legendEntries, uVelocitiesSets, wOrVVelocitiesSets, normalise);

				String frameTitle = normalise ? DAStrings.getString(DAStrings.UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE) : DAStrings.getString(DAStrings.UW_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE);
				if (command.equals(DAStrings.getString(DAStrings.SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))) {
					frameTitle = normalise ? DAStrings.getString(DAStrings.UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE) : DAStrings.getString(DAStrings.UV_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE);
				}
				
				mLoadTaskFrame = quadrantHoleDisplay.showInFrame(frameTitle);
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				Vector<String> legendEntries = new Vector<String>(numberOfDataPoints);
				Vector<Vector<Double>> uVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> vVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				
				for (int i = 0; i < numberOfDataPoints; ++i) {
					legendEntries.add(DATools.makeDataPointIdentifier(yCoords.elementAt(i), zCoords.elementAt(i)));
					uVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
					vVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
					wVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
				}
				
				ComponentTabsFrame powerSpectrumDisplay;
				try {
					powerSpectrumDisplay = new ComponentTabsFrame(mMe, new DataPointPowerSpectrumDisplay(mDataSetId, DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_TITLE), legendEntries, uVelocitiesSets),
																		new DataPointPowerSpectrumDisplay(mDataSetId, DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_TITLE), legendEntries, vVelocitiesSets),
																		new DataPointPowerSpectrumDisplay(mDataSetId, DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_TITLE), legendEntries, wVelocitiesSets));
				} catch (Exception e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					e.printStackTrace();
					return;
				}

				mLoadTaskFrame = powerSpectrumDisplay.showInFrame(DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_FRAME_TITLE) + " (" + mDataSetId.getShortDisplayString() + ')');
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_WAVELET_ANALYSIS_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				Vector<String> legendEntries = new Vector<String>(numberOfDataPoints);
				Vector<Vector<Double>> uVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> vVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				
				for (int i = 0; i < numberOfDataPoints; ++i) {
					legendEntries.add(DATools.makeDataPointIdentifier(yCoords.elementAt(i), zCoords.elementAt(i)));
					uVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
					vVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
					wVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
				}
				
				ComponentTabsFrame waveletAnalysisDisplay;
				try {
					waveletAnalysisDisplay = new ComponentTabsFrame(mMe, new DataPointWaveletAnalysisDisplay(mDataSetId, legendEntries, uVelocitiesSets.firstElement()),
																			new DataPointWaveletAnalysisDisplay(mDataSetId, legendEntries, vVelocitiesSets.firstElement()),
																			new DataPointWaveletAnalysisDisplay(mDataSetId, legendEntries, wVelocitiesSets.firstElement()));
				} catch (Exception e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					e.printStackTrace();
					return;
				}

				mLoadTaskFrame = waveletAnalysisDisplay.showInFrame(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_GRAPH_FRAME_TITLE) + " (" + mDataSetId.getShortDisplayString() + ')');
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				Vector<String> legendEntries = new Vector<String>(numberOfDataPoints);
				Vector<Vector<Double>> uCorrelationsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> uSNRsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> vCorrelationsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> vSNRsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wCorrelationsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wSNRsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				
				for (int i = 0; i < numberOfDataPoints; ++i) {
					legendEntries.add(DATools.makeDataPointIdentifier(yCoords.elementAt(i), zCoords.elementAt(i)));
					uCorrelationsSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS));
					uSNRsSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS));
					vCorrelationsSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS));
					vSNRsSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS));
					wCorrelationsSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS));
					wSNRsSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS));
				}
				
				ComponentTabsFrame correlationAndSNRDisplay;
				try {
					correlationAndSNRDisplay = new ComponentTabsFrame(mMe, new DataPointSignalCorrelationAndSNRDisplay(mDataSetId, DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE), legendEntries, uCorrelationsSets, uSNRsSets),
																			new DataPointSignalCorrelationAndSNRDisplay(mDataSetId, DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE), legendEntries, vCorrelationsSets, vSNRsSets),
																			new DataPointSignalCorrelationAndSNRDisplay(mDataSetId, DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE), legendEntries, wCorrelationsSets, wSNRsSets));
				} catch (Exception e) { 	
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					e.printStackTrace();
					return;
				}

				mLoadTaskFrame = correlationAndSNRDisplay.showInFrame(DAStrings.getString(DAStrings.SIGNAL_CORRELATION_AND_SNR_GRAPH_FRAME_TITLE) + " (" + mDataSetId.getShortDisplayString() + ')');
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_W_DIFF_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				Vector<String> legendEntries = new Vector<String>(numberOfDataPoints);
				Vector<Vector<Double>> zVelocities = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wDiffSets = new Vector<Vector<Double>>(numberOfDataPoints);
				
				for (int i = 0; i < numberOfDataPoints; ++i) {
					legendEntries.add(DATools.makeDataPointIdentifier(yCoords.elementAt(i), zCoords.elementAt(i)));
					wDiffSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_W_DIFF));
					zVelocities.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
				}
				
				ComponentTabsFrame wDiffDisplay;
				try {
					wDiffDisplay = new ComponentTabsFrame(mMe, new DataPointWDiffDisplay(mDataSetId, DAStrings.getString(DAStrings.W_DIFF_GRAPH_TITLE), legendEntries, wDiffSets, zVelocities),
																			new DataPointWDiffDisplay(mDataSetId, DAStrings.getString(DAStrings.W_DIFF_GRAPH_TITLE), legendEntries, wDiffSets, zVelocities),
																			new DataPointWDiffDisplay(mDataSetId, DAStrings.getString(DAStrings.W_DIFF_GRAPH_TITLE), legendEntries, wDiffSets, zVelocities));
				} catch (Exception e) { 	
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					e.printStackTrace();
					return;
				}

				mLoadTaskFrame = wDiffDisplay.showInFrame(DAStrings.getString(DAStrings.W_DIFF_GRAPH_FRAME_TITLE) + " (" + mDataSetId.getShortDisplayString() + ')');
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_PDF_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				Vector<String> legendEntries = new Vector<String>(numberOfDataPoints);
				Vector<Vector<Double>> uVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> vVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> uPrimeWPrimePairsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> uPrimeVPrimePairsSets = new Vector<Vector<Double>>(numberOfDataPoints);
				
				for (int i = 0; i < numberOfDataPoints; ++i) {
					legendEntries.add(DATools.makeDataPointIdentifier(yCoords.elementAt(i), zCoords.elementAt(i)));
					Vector<Double> uVelocities = dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
					Vector<Double> vVelocities = dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
					Vector<Double> wVelocities = dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
					int numberOfVelocities = uVelocities.size();
					double uBar = dataPointSummaries.elementAt(i).get(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					double vBar = dataPointSummaries.elementAt(i).get(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					double wBar = dataPointSummaries.elementAt(i).get(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					Vector<Double> uPrimeVPrimePairs = new Vector<Double>(numberOfVelocities);
					Vector<Double> uPrimeWPrimePairs = new Vector<Double>(numberOfVelocities);
					double uPrimeVPrimeBar = dataPointSummaries.elementAt(i).get(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN);
					double uPrimeWPrimeBar = dataPointSummaries.elementAt(i).get(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN);
					
					for (int j = 0; j < numberOfVelocities ; ++j) {
						uPrimeWPrimePairs.add((uVelocities.elementAt(j) - uBar) * (wVelocities.elementAt(j) - wBar)/uPrimeWPrimeBar); 
						uPrimeVPrimePairs.add((uVelocities.elementAt(j) - uBar) * (vVelocities.elementAt(j) - vBar)/uPrimeVPrimeBar); 
					}
					
					uPrimeWPrimePairsSets.add(uPrimeWPrimePairs);
					uPrimeVPrimePairsSets.add(uPrimeVPrimePairs);
					
					uVelocitiesSets.add(uVelocities);
					vVelocitiesSets.add(vVelocities);
					wVelocitiesSets.add(wVelocities);
				}
				
				ComponentTabsFrame pdfDisplay = new ComponentTabsFrame(mMe,
												new DataPointPDFDisplay(BackEndAPI.X_AXIS_OR_U_VELOCITY, legendEntries, uVelocitiesSets),
												new DataPointPDFDisplay(BackEndAPI.Y_AXIS_OR_V_VELOCITY, legendEntries, vVelocitiesSets),
												new DataPointPDFDisplay(BackEndAPI.Z_AXIS_OR_W_VELOCITY, legendEntries, wVelocitiesSets),
												new MAJFCTabContents("blah uw", new DataPointPDFDisplay(BackEndAPI.Z_AXIS_OR_W_VELOCITY, legendEntries, uPrimeWPrimePairsSets)),
												new MAJFCTabContents("blah uv", new DataPointPDFDisplay(BackEndAPI.Y_AXIS_OR_V_VELOCITY, legendEntries, uPrimeVPrimePairsSets)));

				mLoadTaskFrame = pdfDisplay.showInFrame(DAStrings.getString(DAStrings.PDF_GRAPH_FRAME_TITLE) + mDataSetId.getShortDisplayString());
//				mLoadTaskFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				Vector<Vector<Double>> uVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> vVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
				Vector<Vector<Double>> wVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);

				for (int i = 0; i < numberOfDataPoints; ++i) {
					uVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
					vVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
					wVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
				}
				
				ComponentTabsFrame conditionalTimeSeriesDisplay;
				try {
					boolean splitByAccelDecel = false;
					int measurementFrequency = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_SAMPLING_RATE).intValue();
					int conditionalTimeSeriesLengthInSeconds = 4;
					
					conditionalTimeSeriesDisplay = new ComponentTabsFrame(mMe,
											new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, splitByAccelDecel, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false),
											new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, vVelocitiesSets, splitByAccelDecel, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false),
											new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, wVelocitiesSets, splitByAccelDecel, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false),
											new MAJFCTabContents(DAStrings.getString(DAStrings.U_FILTERED_VELOCITIES_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, splitByAccelDecel, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
											new MAJFCTabContents(DAStrings.getString(DAStrings.V_FILTERED_VELOCITIES_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, vVelocitiesSets, splitByAccelDecel, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
											new MAJFCTabContents(DAStrings.getString(DAStrings.W_FILTERED_VELOCITIES_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, wVelocitiesSets, splitByAccelDecel, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)));
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_1_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q1, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_2_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q2, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_3_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q3, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_4_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q4, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_1_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q1, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_2_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q2, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_3_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q3, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_4_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q4, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, false)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_1_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q1, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_2_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q2, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_3_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q3, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_4_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q4, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_1_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q1, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_2_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q2, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_3_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q3, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)),
//											new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.QUADRANT_4_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q4, includeMinima, measurementFrequency, conditionalTimeSeriesLengthInSeconds, true)));
				} catch (Exception e) {
					e.printStackTrace();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}

				mLoadTaskFrame = conditionalTimeSeriesDisplay.showInFrame(DAStrings.getString(DAStrings.CONDITIONAL_TIME_SERIES_GRAPH_FRAME_TITLE) + " (" + mDataSetId.getShortDisplayString() + ')');
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				
				if (numberOfDataPoints > 2) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				
//				Vector<Vector<Double>> uVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
//				Vector<Vector<Double>> vVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
//				Vector<Vector<Double>> wVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
//
//				for (int i = 0; i < numberOfDataPoints; ++i) {
//					uVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
//					vVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
//					wVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
//				}
				
				Vector<Double> uVelocities1 = dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
				Vector<Double> uVelocities2 = numberOfDataPoints == 1 ? uVelocities1 : dataPointDetails.elementAt(1).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
				Vector<Double> vVelocities1 = dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
				Vector<Double> vVelocities2 = numberOfDataPoints == 1 ? vVelocities1 : dataPointDetails.elementAt(1).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
				Vector<Double> wVelocities1 = dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
				Vector<Double> wVelocities2 = numberOfDataPoints == 1 ? wVelocities1 : dataPointDetails.elementAt(1).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
				
				int numberOfVelocities = Math.min(uVelocities1.size(), uVelocities2.size());
				final double numberOfSecondsToShift = 0.25;
				int shiftStep;
				try {
					shiftStep = (int) (numberOfSecondsToShift * DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_SAMPLING_RATE).intValue());
				} catch (BackEndAPIException e) {
					e.printStackTrace();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}

				final int numberOfShifts = (numberOfVelocities/shiftStep) - 1;

				Vector<Double> uCorrelations = new Vector<Double>(numberOfShifts);
				Vector<Double> wCorrelations = new Vector<Double>(numberOfShifts);
				Vector<Double> vCorrelations = new Vector<Double>(numberOfShifts);
				Vector<Double> times = new Vector<Double>(numberOfShifts);
				
				for (int shiftIndex = 0; shiftIndex < numberOfShifts; ++shiftIndex) {
					double correlation = MAJFCMaths.correlation(uVelocities1, uVelocities2.subList(shiftIndex * shiftStep, uVelocities2.size()));
					uCorrelations.add(correlation);

					correlation = MAJFCMaths.correlation(vVelocities1, vVelocities2.subList(shiftIndex * shiftStep, uVelocities2.size()));
					vCorrelations.add(correlation);
					
					correlation = MAJFCMaths.correlation(wVelocities1, wVelocities2.subList(shiftIndex * shiftStep, uVelocities2.size()));
					wCorrelations.add(correlation);

					times.add((double) shiftIndex * numberOfSecondsToShift);
					
					System.out.println("Shift " + times.lastElement() + " correlation " + uCorrelations.lastElement() + "-" + vCorrelations.lastElement() + "-" + wCorrelations.lastElement());
				}

//Fast-Fourier Transform
//				LinkedList<MAJFCMaths.ComplexNumber> fftUCorrelations = MAJFCMaths.fastFourierTransform(new LinkedList<Double>(uCorrelations));
//				
//				uCorrelations.removeAllElements();
//				
//				for (int j = 0; j < fftUCorrelations.size(); ++j) {
//					uCorrelations.add(fftUCorrelations.get(j).magnitude());
//				}
//				LinkedList<MAJFCMaths.ComplexNumber> fftVCorrelations = MAJFCMaths.fastFourierTransform(new LinkedList<Double>(vCorrelations));
//				
//				vCorrelations.removeAllElements();
//				
//				for (int j = 0; j < fftVCorrelations.size(); ++j) {
//					vCorrelations.add(fftVCorrelations.get(j).magnitude());
//				}
//				LinkedList<MAJFCMaths.ComplexNumber> fftWCorrelations = MAJFCMaths.fastFourierTransform(new LinkedList<Double>(wCorrelations));
//				
//				wCorrelations.removeAllElements();
//				
//				for (int j = 0; j < fftWCorrelations.size(); ++j) {
//					wCorrelations.add(fftWCorrelations.get(j).magnitude());
//				}

				ComponentTabsFrame offsetCorrelationsDisplay;
				try {
					Vector<String> legendEntries = new Vector<String>(1);
					legendEntries.add("blah");
					offsetCorrelationsDisplay = new ComponentTabsFrame(mMe,
											new DataPointOffsetCorrelationsDisplay(BackEndAPI.X_AXIS_OR_U_VELOCITY, legendEntries, uCorrelations, times),
											new DataPointOffsetCorrelationsDisplay(BackEndAPI.Y_AXIS_OR_V_VELOCITY, legendEntries, vCorrelations, times),
											new DataPointOffsetCorrelationsDisplay(BackEndAPI.Z_AXIS_OR_W_VELOCITY, legendEntries, wCorrelations, times));
											//new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.W_VELOCITIES_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY, includeMinima)),
											//new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, vVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY, includeMinima)),
											//new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL) + DAStrings.getString(DAStrings.V_VELOCITIES_LABEL), new DataPointConditionalTimeSeriesDisplay(mDataSetId, DAStrings.getString(DAStrings.QUADRANT_HOLE_GRAPH_TITLE), yCoords, zCoords, uVelocitiesSets, uVelocitiesSets, wVelocitiesSets, DataPointConditionalTimeSeriesDisplay.MULTIPLY_WITH_QH_Q2, includeMinima)));
				} catch (Exception e) {
					e.printStackTrace();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}

				mLoadTaskFrame = offsetCorrelationsDisplay.showInFrame(DAStrings.getString(DAStrings.OFFSET_CORRELATIONS_GRAPH_FRAME_TITLE));
			} else if (command.equals(DAStrings.getString(DAStrings.SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL))) {
				int numberOfDataPoints = yCoords.size();
				
				if (numberOfDataPoints > 2) {
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}
				
//				Vector<Vector<Double>> uVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
//				Vector<Vector<Double>> vVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
//				Vector<Vector<Double>> wVelocitiesSets = new Vector<Vector<Double>>(numberOfDataPoints);
//
//				for (int i = 0; i < numberOfDataPoints; ++i) {
//					uVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
//					vVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
//					wVelocitiesSets.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
//				}
				
				Vector<Double> uVelocities1 = dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
				Vector<Double> uVelocities2 = numberOfDataPoints == 1 ? uVelocities1 : dataPointDetails.elementAt(1).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
				Vector<Double> vVelocities1 = dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
				Vector<Double> vVelocities2 = numberOfDataPoints == 1 ? vVelocities1 : dataPointDetails.elementAt(1).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
				Vector<Double> wVelocities1 = dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
				Vector<Double> wVelocities2 = numberOfDataPoints == 1 ? wVelocities1 : dataPointDetails.elementAt(1).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
				
				int numberOfVelocities = Math.min(uVelocities1.size(), uVelocities2.size());
				final int numberOfSamples = 250;
				final int sampleLengthInSeconds = 1;
				int sampleLength;
				try {
					sampleLength = (int) (sampleLengthInSeconds * DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_SAMPLING_RATE).intValue());
				} catch (BackEndAPIException e) {
					e.printStackTrace();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					return;
				}

				Vector<Double> uCorrelations = new Vector<Double>(numberOfSamples);
				Vector<Double> wCorrelations = new Vector<Double>(numberOfSamples);
				Vector<Double> vCorrelations = new Vector<Double>(numberOfSamples);
				Hashtable<Integer, Integer> samplesLookup = new Hashtable<Integer, Integer>(numberOfSamples);
				
				for (int sampleIndex = 0; sampleIndex < numberOfSamples; ++sampleIndex) {
					int startOfSample = (int) (((double) numberOfVelocities) * Math.random());
					
					if (startOfSample > (numberOfVelocities - sampleLength)) {
						startOfSample = numberOfVelocities - sampleLength;
					}
					
					while (samplesLookup.containsKey(startOfSample)) {
						startOfSample = (int) (((double) numberOfVelocities) * Math.random());
					}
					
					List<Double> uSample1 = uVelocities1.subList(startOfSample, startOfSample + sampleLength);
					List<Double> uSample2 = uVelocities2.subList(startOfSample, startOfSample + sampleLength);
					uCorrelations.add(MAJFCMaths.covariance(uSample1, uSample2));

					List<Double> vSample1 = vVelocities1.subList(startOfSample, startOfSample + sampleLength);
					List<Double> vSample2 = vVelocities2.subList(startOfSample, startOfSample + sampleLength);
					vCorrelations.add(MAJFCMaths.covariance(vSample1, vSample2));

					List<Double> wSample1 = wVelocities1.subList(startOfSample, startOfSample + sampleLength);
					List<Double> wSample2 = wVelocities2.subList(startOfSample, startOfSample + sampleLength);
					wCorrelations.add(MAJFCMaths.covariance(wSample1, wSample2));
				}

				Vector<String> legendEntries = new Vector<String>(1);
				legendEntries.add(DAStrings.getString(DAStrings.Y_COORD_LABEL));
				legendEntries.add(DAStrings.getString(DAStrings.Z_COORD_LABEL));
				
				try {
					mLoadTaskFrame = new DataPointCorrelationsDistributionDisplay(legendEntries, uCorrelations, vCorrelations, wCorrelations).showInFrame(DAStrings.getString(DAStrings.CORRELATIONS_DISTRIBUTION_GRAPH_FRAME_TITLE));
;				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL))
						|| command.equals(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL))) {
				writeDataPointDetailsToASCIIFile(yCoords.firstElement().intValue(), zCoords.firstElement().intValue(), dataPointSummaries, dataPointDetails, command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL)));
			} else if (command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL))
						|| command.equals(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL))) {
				writeDataPointDetailsToMatlabFile(yCoords.firstElement().intValue(), zCoords.firstElement().intValue(), dataPointSummaries, dataPointDetails, command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL)));
			} else if (command.equals(DAStrings.getString(DAStrings.SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL))
						|| command.equals(DAStrings.getString(DAStrings.SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL))) {
				sendDataPointDetailsToMatlab(yCoords.firstElement().intValue(), zCoords.firstElement().intValue(), dataPointSummaries, dataPointDetails, command.equals(DAStrings.getString(DAStrings.SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL)));
			}

			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		
		@Override
		public void onDataPointDetailsWithLinkedDPsLoaded(int requestId, Vector<AbstractDataSetUniqueId> dataSetIds, Vector<DataSetType> dataSetTypes, int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries,	Vector<DataPointDetail> dataPointDetails) {
			String command = mDataPointLoadRequestLookup.get(requestId);

			if (command.equals(DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_LABEL))) {
				mDataPointDetailDisplay.addDataPoint(yCoord, zCoord, dataPointSummaries, dataPointDetails);
			} else if (command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL))
						|| command.equals(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL))) {
				writeDataPointDetailsToASCIIFile(yCoord, zCoord, dataPointSummaries, dataPointDetails, command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL)));
			} else if (command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL))
						|| command.equals(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL))) {
				writeDataPointDetailsToMatlabFile(yCoord, zCoord, dataPointSummaries, dataPointDetails, command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL)));
			} else if (command.equals(DAStrings.getString(DAStrings.SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL))
						|| command.equals(DAStrings.getString(DAStrings.SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL))) {
				sendDataPointDetailsToMatlab(yCoord, zCoord, dataPointSummaries, dataPointDetails, command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL)));
			}
			
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		/**
		 * BackEndAPICallbackInterface implementation
		 */
		public void onDataPointsRemoved(AbstractDataSetUniqueId dataSetId, Vector<Integer> yCoords, Vector<Integer> zCoords) {
			DAFrame.getFrame().getBackEndAPICallBackAdapter().onDataPointsRemoved(dataSetId, yCoords, zCoords);
		}
	}

	/**
	 * Sets data point details to be displayed
	 * @param yCoord The y-coordinate of this data point
	 * @param zCoord The z-coordinate of this data point
	 * @param dataPointSummaries The summary data for this point
	 * @param excludeLevel The exclude level used for this point 
	 * @param dataPointDetails The data for this data point
	 * @param ftrcOnly Only include ftrc velocities (if false, all velocity stages are included, along with correlations and SNRs)
	 */
	public void writeDataPointDetailsToASCIIFile(int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails, boolean ftrcOnly) {
		boolean isMultiRun = mDataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE) || mDataSetType.equals(BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE);
		Vector<DataPointDetailDataModel> dataModels = getDataModels(yCoord, zCoord, dataPointSummaries, dataPointDetails, ftrcOnly);
		FileWriter fileWriter = null;
		int numberOfDataModels = dataModels.size();
		
		try {
			String outputFileDirName = DAFrame.getFrame().getCurrentDataSetConfig().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY) + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + DAFrame.getFrame().getCurrentDataSetId().getShortDisplayString() + DAStrings.getString(DAStrings.SAVE_DETAILS_DIRECTORY_SUFFIX);
			File outputFileDir = new File(outputFileDirName);
			if (outputFileDir.exists() == false) {
				outputFileDir.mkdirs();
			}
			
			File outputFile = new File(outputFileDirName + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + yCoord + '-' + zCoord + DADefinitions.FILE_EXTENSION_ASCII_DETAILS_OUTPUT);
			fileWriter = new FileWriter(outputFile);
			
			// Write the column headings
			fileWriter.write(DAStrings.getString(DAStrings.INDEX_TEXT));
			fileWriter.write('\t');

			for (int dmIndex = 0; dmIndex < numberOfDataModels; dmIndex++) {
				DataPointDetailDataModel theModel = dataModels.elementAt(dmIndex);
				
				int numberOfTimeSeries = theModel.getColumnCount();
				for (int seriesIndex = 0; seriesIndex < numberOfTimeSeries; ++seriesIndex) {
					String mrLabel = isMultiRun ?  (seriesIndex == 0 ? DAStrings.getString(DAStrings.RUN_MEAN) + ' ' : theModel.getColumnName(seriesIndex)) + ' ' : "";
					fileWriter.write(mrLabel + theModel.getColumnName(0));
					fileWriter.write('\t');
				}
			}

			fileWriter.write('\n');
			
			int numberOfValues = dataModels.firstElement().getRowCount();
			for (int valueIndex = 0; valueIndex < numberOfValues; ++valueIndex) {
				fileWriter.write(Integer.toString(valueIndex));
				fileWriter.write('\t');
		
				for (int dmIndex = 0; dmIndex < numberOfDataModels; dmIndex++) {
					DataPointDetailDataModel theModel = dataModels.elementAt(dmIndex);
					
					int numberOfTimeSeries = theModel.getColumnCount();
					for (int seriesIndex = 0; seriesIndex < numberOfTimeSeries; ++seriesIndex) {
						fileWriter.write(parseDMValue(theModel.getValueAt(valueIndex, seriesIndex)));
						fileWriter.write('\t');
					}
				}
				
				fileWriter.write('\n');
			}
			
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
	     	
			JOptionPane.showMessageDialog(DAFrame.getFrame(), e.getMessage(), DAStrings.getString(DAStrings.ERROR_SAVING_FILE_DETAILS_TITLE), JOptionPane.ERROR_MESSAGE);
	        			
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e1) {
				}
			}
		}
	}
	
	private Vector<DataPointDetailDataModel> getDataModels(int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails, boolean ftrcOnly) {
		int numberOfTimeSeries = dataPointDetails.size();
		Vector<Integer> synchIndices = new Vector<Integer>(numberOfTimeSeries);
		Vector<Vector<Double>> timeSeriesList = new Vector<Vector<Double>>(numberOfTimeSeries);
		Vector<DataPointDetailDataModel> dataModels = new Vector<DataPointDetailDataModel>(4);
		
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			synchIndices.add(dataPointSummaries.elementAt(i).get(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX).intValue());
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES));
		}

		dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.U_FILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList));
		
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES));
		}
		
		dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.V_FILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList));
				
		timeSeriesList.clear();
		for (int i = 0; i < numberOfTimeSeries; ++i) {
			timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES));
		}
		
		dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.W_FILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList));
		
		if (ftrcOnly == false) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_RAW_X_VELOCITIES));
			}

			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.PROBE_X_VELOCITIES_LABEL), synchIndices, timeSeriesList));
			
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.PROBE_Y_VELOCITIES_LABEL), synchIndices, timeSeriesList));
					
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.PROBE_Z_VELOCITIES_LABEL), synchIndices, timeSeriesList));

			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES));
			}

			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.U_UNFILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList));
			
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.V_UNFILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList));
					
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.W_UNFILTERED_VELOCITIES_LABEL), synchIndices, timeSeriesList));
			
//			timeSeriesList.clear();
//			for (int i = 0; i < numberOfTimeSeries; ++i) {
//				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES));
//			}
//
//			dataModels.add(new DataPointDetailDataModel("blah u", synchIndices, timeSeriesList));
//			
//			timeSeriesList.clear();
//			for (int i = 0; i < numberOfTimeSeries; ++i) {
//				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES));
//			}
//			
//			dataModels.add(new DataPointDetailDataModel("blah v", synchIndices, timeSeriesList));
//					
//			timeSeriesList.clear();
//			for (int i = 0; i < numberOfTimeSeries; ++i) {
//				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES));
//			}
//			
//			dataModels.add(new DataPointDetailDataModel("blah w", synchIndices, timeSeriesList));

			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS));
			}

			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.X_CORRELATIONS_LABEL), synchIndices, timeSeriesList));
			
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.X_SNRS_LABEL), synchIndices, timeSeriesList));

			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS));
			}

			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.Y_CORRELATIONS_LABEL), synchIndices, timeSeriesList));
			
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.Y_SNRS_LABEL), synchIndices, timeSeriesList));

			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS));
			}

			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.Z_CORRELATIONS_LABEL), synchIndices, timeSeriesList));
			
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.Z_SNRS_LABEL), synchIndices, timeSeriesList));
		}
		
		// Pressure (if there is one)
		if (dataPointDetails.firstElement().get(BackEndAPI.DPD_KEY_PRESSURE).size() > 0) {
			timeSeriesList.clear();
			for (int i = 0; i < numberOfTimeSeries; ++i) {
				timeSeriesList.add(dataPointDetails.elementAt(i).get(BackEndAPI.DPD_KEY_PRESSURE));
			}
			
			dataModels.add(new DataPointDetailDataModel(DAStrings.getString(DAStrings.PRESSURES_LABEL), synchIndices, timeSeriesList));
		}
		
		return dataModels;
	}
	
	/**
	 * Sets data point details to be displayed
	 * @param yCoord The y-coordinate of this data point
	 * @param zCoord The z-coordinate of this data point
	 * @param dataPointSummaries The summary data for this point
	 * @param excludeLevel The exclude level used for this point 
	 * @param dataPointDetails The data for this data point
	 * @param ftrcOnly If true, only ftrc velocities are exported
	 */
	public void writeDataPointDetailsToMatlabFile(int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails, boolean ftrcOnly) {
		int numberOfTimeSeries = dataPointDetails.size();
		Vector<DataPointDetailDataModel> dataModels = getDataModels(yCoord, zCoord, dataPointSummaries, dataPointDetails, ftrcOnly);
		int numberOfDataModels = dataModels.size();
		
		try {
			DataSetConfig config = BackEndAPI.getBackEndAPI().getConfigData(mDataSetId);
//			MLStructure configArray = new MLStructure(DADefinitions.MATLAB_OUTPUT_CONFIG_ARRAY, new int[] {2, 1});
			MLDouble configArray = new MLDouble(DADefinitions.MATLAB_OUTPUT_CONFIG_ARRAY, new int[] {1, 1});
			configArray.set(config.get(BackEndAPI.DSC_KEY_SAMPLING_RATE), 0);
//			String coords = "_" + mDataSetId.getShortDisplayString() + '_' + (yCoord < 0 ? "minus" + -yCoord : yCoord) + '_' + (zCoord < 0 ? "minus" + -zCoord : zCoord);
					
			Collection<MLArray> all = new Vector<MLArray>(2);
			for (int dmIndex = 0; dmIndex < numberOfDataModels; ++dmIndex) {
				DataPointDetailDataModel dataModel = dataModels.elementAt(dmIndex);
				int numberOfRows = dataModel.getRowCount();
				MLDouble matlabArray = new MLDouble(prepareStringForMatlab(dataModel.getColumnName(0)), new int[] {numberOfRows, numberOfTimeSeries});
			
				for (int valueIndex = 0; valueIndex < numberOfRows; ++valueIndex) {
					for (int seriesIndex = 0; seriesIndex < numberOfTimeSeries; ++seriesIndex) {
						matlabArray.set(parseDMValueForMatlab(dataModel.getValueAt(valueIndex, seriesIndex)), valueIndex, seriesIndex);
					}
				}

				all.add(matlabArray);
			}

			String outputFileDirName = DAFrame.getFrame().getCurrentDataSetConfig().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY) + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + DAFrame.getFrame().getCurrentDataSetId().getShortDisplayString() + DAStrings.getString(DAStrings.SAVE_DETAILS_DIRECTORY_SUFFIX);
			File outputFileDir = new File(outputFileDirName);
			if (outputFileDir.exists() == false) {
				outputFileDir.mkdirs();
			}
			
			File outputFile = new File(outputFileDirName + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + yCoord + '-' + zCoord + DADefinitions.FILE_EXTENSION_MATLAB_DETAILS_OUTPUT);
			new MatFileWriter(outputFile, all);
		} catch (Exception e) {
			e.printStackTrace();
	     	
			JOptionPane.showMessageDialog(DAFrame.getFrame(), e.getMessage(), DAStrings.getString(DAStrings.ERROR_SAVING_FILE_DETAILS_TITLE), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Sets data point details to be displayed
	 * @param yCoord The y-coordinate of this data point
	 * @param zCoord The z-coordinate of this data point
	 * @param dataPointSummaries The summary data for this point
	 * @param excludeLevel The exclude level used for this point 
	 * @param dataPointDetails The data for this data point
	 * @param ftrcOnly If true, only ftrc velocities are exported
	 */
	public void sendDataPointDetailsToMatlab(int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails, boolean ftrcOnly) {
		int numberOfTimeSeries = dataPointDetails.size();
		Vector<DataPointDetailDataModel> dataModels = getDataModels(yCoord, zCoord, dataPointSummaries, dataPointDetails, ftrcOnly);
		int numberOfDataModels = dataModels.size();
		String coords = "_" + mDataSetId.getShortDisplayString() + '_' + (yCoord < 0 ? "minus" + -yCoord : yCoord) + '_' + (zCoord < 0 ? "minus" + -zCoord : zCoord);
		
		try {
			for (int dmIndex = 0; dmIndex < numberOfDataModels; ++dmIndex) {
				DataPointDetailDataModel dataModel = dataModels.elementAt(dmIndex);
				int numberOfRows = dataModel.getRowCount();
				double[][] values = new double[numberOfTimeSeries][numberOfRows];
				
				for (int valueIndex = 0; valueIndex < numberOfRows; ++valueIndex) {
					for (int seriesIndex = 0; seriesIndex < numberOfTimeSeries; ++seriesIndex) {
						values[seriesIndex][valueIndex] = parseDMValueForMatlab(dataModel.getValueAt(valueIndex, seriesIndex));
					}
				}
				
				DataAnalyser.matlabConnection().setMatlabVariable(prepareStringForMatlab(dataModel.getColumnName(0) + coords), values);
			}
		} catch (Exception e) {
			e.printStackTrace();
	     	
			JOptionPane.showMessageDialog(DAFrame.getFrame(), e.getMessage(), DAStrings.getString(DAStrings.ERROR_SAVING_FILE_DETAILS_TITLE), JOptionPane.ERROR_MESSAGE);
		}
	}

	private String prepareStringForMatlab(String string) {
		char[] stringChars = string.toCharArray();
		
		for (int i = 0; i < stringChars.length; ++i) {
			if (Character.isLetterOrDigit(stringChars[i]) == false) {
				stringChars[i] = '_';
			}
		}
		
		return new String(stringChars);   
	}

	private String parseDMValue(Object value) {
		if (value == null) {
			return "N/A";
		}
		
		return (String) value;
	}
	
	private Double parseDMValueForMatlab(Object value) {
		if (value == null) {
			return 0d;
		}
		
		return MAJFCTools.parseDouble((String) value);
	}
	
	@Override
	/**
	 * ActionListener implementation
	 * @param theEvent The ActionEvent
	 */
	public void actionPerformed(ActionEvent theEvent) {
		tableAction(theEvent.getActionCommand());
	}
	
	private void tableAction(String command) {
		if (command.equals(DAStrings.getString(DAStrings.SELECTION_TOOL_BUTTON_LABEL))) {
			new DataPointSelectionTool(mDataSetId, mVelocitiesDetailsDataModel, mDataPointsOverviewDataTable);
			return;
		}
		
		new TableActionProgressTask(DAStrings.getString(DAStrings.TABLE_ACTION_PROGRESS_DIALOG_TITLE), command);
	}

	/**
	 * Class for the worker thread to do all the stuff required for saving the data set
	 */
	private class TableActionProgressTask extends DAProgressDialog {
		private final String mCommand;
		
		/**
		 * 
		 * @param parent
		 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
		 * @param title
		 */
		public TableActionProgressTask(String title, String command) {
			super(DAFrame.getFrame().getCurrentDataSetId(), DAFrame.getFrame(), title);
			
			mCommand = command;

			setVisible();
		}

		@Override
		/**
		 * The stuff done by this task.
		 */
		protected Void doInBackground() throws Exception {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			
			int[] selectedRowIndices = mDataPointsOverviewDataTable.getSelectedRows();
			Vector<Integer> yCoords = new Vector<Integer>(selectedRowIndices.length);
			Vector<Integer> zCoords = new Vector<Integer>(selectedRowIndices.length);

			for (int i = 0; i < selectedRowIndices.length; ++i) {
				int row = mDataPointsOverviewDataTable.convertRowIndexToModel(selectedRowIndices[i]);
				yCoords.add((Integer) mVelocitiesDetailsDataModel.getNumberValueAt(row, 0));
				zCoords.add((Integer) mVelocitiesDetailsDataModel.getNumberValueAt(row, 1));
			}

			setProgress(25);
			
			if (mCommand.equals(DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_LABEL))) {
				loadAndOpenDataPoints(yCoords, zCoords);
			} else if (mCommand.equals(DAStrings.getString(DAStrings.CALCULATE_CORRELATION_BUTTON_LABEL))) {
				calculateCorrelations(yCoords, zCoords);
			} else if (mCommand.equals(DAStrings.getString(DAStrings.CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL))) {
				calculatePseudoCorrelations(yCoords, zCoords);
			} else if (mCommand.equals(DAStrings.getString(DAStrings.SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_WAVELET_ANALYSIS_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_W_DIFF_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_PDF_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL))
					|| mCommand.equals(DAStrings.getString(DAStrings.SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL))
					|| isWriteFileCommand(mCommand)) {
				try {
					// For "Write to File" commands, each one needs to be handled separately
					if (isWriteFileCommand(mCommand)) {
						int numberOfCoords = yCoords.size(); 
						for (int i = 0; i < numberOfCoords; ++i) {
							mDataPointLoadRequestLookup.put(new Integer(++mDataPointLoadRequestId), mCommand);
							
							if(DataSet.isMultiRunMean(mDataSetType)){
								BackEndAPI.getBackEndAPI().loadDataPointDetailsWithLinkedDPs(mDataPointLoadRequestId, mDataSetId, yCoords.elementAt(i), zCoords.elementAt(i), mBackEndAPICallbackInterface);
							} else {
								BackEndAPI.getBackEndAPI().loadDataPointDetails(mDataPointLoadRequestId, mDataSetId, yCoords.elementAt(i), zCoords.elementAt(i), mBackEndAPICallbackInterface);
							}
						}
					} else {
						mDataPointLoadRequestLookup.put(new Integer(++mDataPointLoadRequestId), mCommand);
						BackEndAPI.getBackEndAPI().loadDataPointDetails(mDataPointLoadRequestId, mDataSetId, yCoords, zCoords, mBackEndAPICallbackInterface);
					}
				} catch (BackEndAPIException theException) {
					theException.printStackTrace();
				}
			} else if (mCommand.equals(DAStrings.getString(DAStrings.REMOVE_DATA_POINTS_BUTTON_LABEL))) {
				removeDataPoints(yCoords, zCoords);
			} else if (mCommand.equals(DAStrings.getString(DAStrings.CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL))) {
				DAFrame.getFrame().createRotationCorrectionBatch(mDataSetId, yCoords, zCoords);
			} else if (mCommand.equals(DAStrings.getString(DAStrings.CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL))) {
				DAFrame.getFrame().createRotationCorrectionBatchesFromFile(mDataSetId);
			}
			
			return null;
		}
		
		@Override
		public void whenDone() {
			if (mLoadTaskFrame != null) {
				mLoadTaskFrame.toFront();
			}
		}
	}
	
	private boolean isWriteFileCommand(String command) {
		return command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL))
				|| command.equals(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL))
				|| command.equals(DAStrings.getString(DAStrings.SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL))
				|| command.equals(DAStrings.getString(DAStrings.SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL))
				|| command.equals(DAStrings.getString(DAStrings.SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL))
				|| command.equals(DAStrings.getString(DAStrings.SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL));
	}

	private class MyJPopupMenu extends JPopupMenu {
		@Override
		public void show(Component parent, int x, int y) {
			setGUIStates();
			super.show(parent, x, y);
		}
		
		private void setGUIStates() {
			boolean dataSetUnlocked;
			boolean rowSelected = mDataPointsOverviewDataTable.getSelectedRowCount() > 0;
			DataSetConfig configData= null;
			
			try {
				dataSetUnlocked = DAFrame.getBackEndAPI().dataSetIsLocked(mDataSetId) == false;
				configData = BackEndAPI.getBackEndAPI().getConfigData(mDataSetId);
			} catch (BackEndAPIException theException) {
				theException.printStackTrace();
				dataSetUnlocked = false;
			}

			mViewDataPointDetailsMenuItem.setEnabled(rowSelected);
			mRemoveDataPointsMenuItem.setEnabled(dataSetUnlocked && rowSelected);
			mSelectionToolMenuItem.setEnabled(true);
			mCreateRotationCorrectionBatchMenuItem.setEnabled(dataSetUnlocked && rowSelected);
			mCreateRotationCorrectionBatchesFromFileMenuItem.setEnabled(dataSetUnlocked);
			mCalculateCorrelationsMenuItem.setEnabled(mDataPointsOverviewDataTable.getSelectedRowCount() > 1);
			mCalculatePseudoCorrelationsMenuItem.setEnabled(mDataPointsOverviewDataTable.getSelectedRowCount() > 1);
			mShowUPrimeWPrimeQuadrantHoleAnalysisMenuItem.setEnabled(rowSelected);
			mShowNormalisedUPrimeWPrimeQuadrantHoleAnalysisMenuItem.setEnabled(rowSelected);
			mShowUPrimeVPrimeQuadrantHoleAnalysisMenuItem.setEnabled(rowSelected);
			mShowNormalisedUPrimeVPrimeQuadrantHoleAnalysisMenuItem.setEnabled(rowSelected);
			mShowPowerSpectrumMenuItem.setEnabled(rowSelected);
			mShowWaveletAnalysisMenuItem.setEnabled(rowSelected && (configData.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE) == BackEndAPI.WTT_FWT.getIntIndex() || mDataPointsOverviewDataTable.getSelectedRowCount() == 1));
			mShowSignalCorrelationAndSNRMenuItem.setEnabled(rowSelected);
			mShowWDiffMenuItem.setEnabled(rowSelected);
			mShowPDFMenuItem.setEnabled(rowSelected);
			mShowConditionalTimeSeriesMenuItem.setEnabled(rowSelected);
			mShowOffsetCorrelationsMenuItem.setEnabled(rowSelected);
			mShowCorrelationsDistributionMenuItem.setEnabled(rowSelected);
			mSaveToFileMenu.setEnabled(rowSelected);
			mSaveFTRCToASCIIFileMenuItem.setEnabled(mSaveToFileMenu.isEnabled());
			mSaveAllToASCIIFileMenuItem.setEnabled(mSaveToFileMenu.isEnabled());
			mSaveFTRCToMatlabFileMenuItem.setEnabled(mSaveToFileMenu.isEnabled());
			mSaveAllToMatlabFileMenuItem.setEnabled(mSaveToFileMenu.isEnabled());
			mSendToMatlabMenu.setEnabled(rowSelected && DataAnalyser.matlabConnectionAvailable());
			mSendAllToMatlabMenuItem.setEnabled(mSendToMatlabMenu.isEnabled());
			mSendFTRCToMatlabMenuItem.setEnabled(mSendToMatlabMenu.isEnabled());
		}
	}
}