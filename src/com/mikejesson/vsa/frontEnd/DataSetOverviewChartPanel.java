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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.DomainInfo;
import org.jfree.data.Range;
import org.jfree.data.RangeInfo;
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYDataset;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabsFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel.MAJFCTabContents;
import com.mikejesson.majfc.helpers.MAJFCLinkedGUIComponentsAction;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CrossSectionDataIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.AnisotropicStressTensorGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.AnisotropicStressTensorInvariantIIGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.AnisotropicStressTensorInvariantIIIGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.CombinedReynoldsStressMagnitudeGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.CorrelationGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.DepthAveragedGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.DepthAveragedReynoldsStressGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.DifferenceOfLateralFluctuationMeanSquaresGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.EstimatedVerticalReynoldsStressAtBedGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.LateralTKEFluxGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.LateralVelocityGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.MaximumVelocitiesGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.MeanVelocityKineticEnergyGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.PercentageGoodGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.QuadrantHoleEventsRatioGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.QuadrantHoleSplitShearStressGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.ResultantShearStressGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.ReynoldsStressGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.ScaledDepthAveragedUVGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.StreamwiseVorticityGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.StreamwiseVorticityKEGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.TKEFluxGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.TKEGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.ThirdOrderCorrelationsGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.TransferOfStreamwiseMomentumGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.TurbulenceGenerationOrDissipationGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.TurbulenceIntensityGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.VelocityStandardDeviationGraph;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.VorticityEquationTermsGraphsPanel;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.ComponentTabsFrame;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ColourCodedChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYZDataSet;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class DataSetOverviewChartPanel extends MAJFCStackedPanelWithFrame implements DomainInfo, RangeInfo {
	private Number mClickEntityYCoord;
	private Number mClickEntityZCoord;
	private JMenuItem mViewPercentageGoodGraphButton;
//	private JMenuItem mViewDataPointDetailsButton;
	private JMenuItem mCreateEOFRMatrixButton;
	private JMenu mCrossSectionGraphs;
	private JMenuItem mViewStreamwiseVorticityGraphButton;
	private JMenuItem mViewMeanVelocityKEGraphButton;
	private JMenuItem mViewTKEGraphButton;
	private JMenuItem mViewStreamwiseVorticityKEGraphButton;
	private JMenuItem mVelocityGraphs;
	private JMenuItem mViewLateralVelocityGraphButton;
	private JMenuItem mViewDepthAveragedUVGraphButton;
	private JMenuItem mViewVelocityStandardDeviationGraphsButton;
	private JMenuItem mViewMaximumVelocitiesGraphButton;
	private JMenu mMomentumGraphs;
	private JMenuItem mViewVerticalTransferOfStreamwiseMomentumGraphButton;
	private JMenuItem mViewHorizontalTransferOfStreamwiseMomentumGraphButton;
	private JMenu mReynoldsStressGraphs;
	private JMenuItem mViewDepthAveragedReynoldsStressGraphButton;
	private JMenuItem mViewHorizontalReynoldsStressGraphButton;
	private JMenuItem mViewVerticalReynoldsStressGraphButton;
	private JMenuItem mViewTurbulenceGenerationAndDissipationGraphButton;
	private JMenuItem mViewUPrimeVPrimeCorrelationGraphButton;
	private JMenuItem mViewUPrimeWPrimeCorrelationGraphButton;
	private JMenuItem mViewCombinedReynoldsStressMagnitudeGraphButton;
	private JMenuItem mViewResultantReynoldsStressGraphButton;
	private JMenuItem mViewHorizontalReynoldsStressAtBedGraphButton;
	private JMenuItem mViewAnisotropicStressTensorGraphButton;
	private JMenuItem mViewAnisotropicStressTensorInvariantIIGraphButton;
	private JMenuItem mViewAnisotropicStressTensorInvariantIIIGraphButton;
	private JMenu mTIAndTKEAndVorticityGraphs;
	private JMenuItem mViewTIGraphsButton;
	private JMenuItem mViewLateralFluxOfStreamwiseTKEGraphButton;
	private JMenuItem mViewStreamwiseTKEFluxGraphButton;
	private JMenuItem mViewDifferenceOfSquaredLateralFluctuationRMSesGraphButton;
	private JMenuItem mViewVorticityEquationGraphs;
	private JMenuItem mViewVPrimeWPrimeGraphButton;
	private JMenuItem mViewVPrimeWPrimeCorrelationGraphButton;
	private JMenu mQHGraphs;
	
	private JMenuItem mViewQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton;
	private JMenuItem mViewScaledQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton;
	
	private JMenuItem mViewQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton;
	private JMenuItem mViewScaledQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton;

	private JMenuItem mViewQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton;
	private JMenuItem mViewScaledQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton;

	private JMenuItem mViewQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton;
	private JMenuItem mViewScaledQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton;

	private JMenuItem mViewQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton;
	private JMenuItem mViewScaledQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton;
	
	private JMenuItem mViewQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton;
	private JMenuItem mViewScaledQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton;

	private JMenuItem mViewUWQHSplitShearStressGraphButton;
	private JMenuItem mViewUVQHSplitShearStressGraphButton;

	private JMenu mThirdOrderCorrelationGraphs;
	private JMenuItem mViewThirdOrderCorrelation300GraphButton;
	private JMenuItem mViewThirdOrderCorrelation210GraphButton;
	private JMenuItem mViewThirdOrderCorrelation120GraphButton;
	private JMenuItem mViewThirdOrderCorrelation030GraphButton;
	private SectionGraph mVerticalSectionVelocityGraph;
	private SectionGraph mHorizontalSectionVelocityGraph;
	private DepthAveragedGraph mDepthAveragedGraph;
	private MyColourCodedChartPanel mChartPanel;
	private XYPlot mThePlot;
	private JFreeChart mTheChart;

	private AbstractDataSetUniqueId mDataSetId;
	private String mRawChartTitle;
	private final CrossSectionDataIndex mMeanVelocityCSDKey;
	private final CrossSectionDataIndex mRMSVelocityCSDKey;
	private final DataSetType mDataSetType;
	
	// Used by the graph plotter for determining axis ranges
	private Range mXRange;
	private Range mYRange;

	/**
	 * @param chart
	 */
	public DataSetOverviewChartPanel(AbstractDataSetUniqueId dataSetId, ScaleableXYZDataSet scaleableXYZDataset, String chartTitle, CrossSectionDataIndex meanVelocityCSDKey, CrossSectionDataIndex rmsVelocityCSDKey, String legendText, DataSetType dataSetType, boolean dataIsVectorInY) {
		super(new GridBagLayout());
		
		mDataSetId = dataSetId;
		mRawChartTitle = chartTitle;
		mMeanVelocityCSDKey = meanVelocityCSDKey;
		mRMSVelocityCSDKey = rmsVelocityCSDKey;
		mDataSetType = dataSetType;
		
		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.CROSS_SECTION_GRID_X_AXIS_TITLE));
//		xAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
		xAxis.setRangeType(RangeType.POSITIVE);
		
		NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.CROSS_SECTION_GRID_Y_AXIS_TITLE));
//		yAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
		yAxis.setRangeType(RangeType.POSITIVE);
		
		mThePlot = new XYPlot(scaleableXYZDataset, xAxis, yAxis, null);
		mThePlot.setRangeCrosshairVisible(true);
		mThePlot.setDomainCrosshairVisible(true);
		mThePlot.setRangeCrosshairLockedOnData(true);
		mThePlot.setDomainCrosshairLockedOnData(true);
		mTheChart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, mThePlot, false);
		mChartPanel = new MyColourCodedChartPanel(this, mTheChart, scaleableXYZDataset, 50, legendText, dataIsVectorInY);
		mThePlot.setRenderer(mChartPanel.getRenderer());
		
		// We need a dummy XYDataset to allow the crosshairs to be placed over one of the XYZDataset points of the contour plot
		XYLineAndShapeRenderer dummyDataSetRenderer = new XYLineAndShapeRenderer();
		dummyDataSetRenderer.setSeriesLinesVisible(0, false);
		dummyDataSetRenderer.setSeriesShape(0, new Rectangle(0,0));
		mThePlot.setDataset(1, scaleableXYZDataset);
		mThePlot.setRenderer(1, dummyDataSetRenderer);
		
		add(mChartPanel.getGUI(), MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}
	
	private class MyColourCodedChartPanel extends ColourCodedChartPanel implements ChartMouseListener {
		public MyColourCodedChartPanel(MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYZDataSet scaleableXYZDataSet, int steps, String legendText, boolean dataIsVectorInY) {
			super(mDataSetId, holderFrame, theChart, scaleableXYZDataSet, 10, 10, steps, legendText, dataIsVectorInY);
			addChartMouseListener(this);
		}

		@Override
		/**
		 * Builds the legend for the cross-section grid
		 */
		protected MAJFCPanel getCustomLegendPanel() {
			int y = 0;
			MAJFCPanel customPanel = new MAJFCPanel(new GridBagLayout());
			
	//TODO remove bodge
			try {
	customPanel.add(new JLabel("U-bar " + MAJFCTools.formatNumber(BackEndAPI.getBackEndAPI().getCrossSectionDataField(mDataSetId, BackEndAPI.CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_U_VELOCITY), 3, true)), MAJFCTools.createGridBagConstraint(0, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 10, 0, 10, 0, 0));
	double scalingU = DAFrame.getFrame().getChannelMeanVelocityForScaling(mDataSetId, mDataSetType);
	customPanel.add(new JLabel("Q/A " + MAJFCTools.formatNumber(scalingU, 3, true)), MAJFCTools.createGridBagConstraint(1, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 10, 0, 10, 0, 0));
	customPanel.add(new JLabel("Q/A -/+ 3% " + MAJFCTools.formatNumber(scalingU * 0.97, 3, true) + '/' + MAJFCTools.formatNumber(scalingU * 1.03, 3, true)), MAJFCTools.createGridBagConstraint(2, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 10, 0, 10, 0, 0));
	
	}
	catch (Exception e) {
				
	}
			return customPanel;
		}
		
		@Override
		protected void showVerticalSectionGraph() {
			if (mVerticalSectionVelocityGraph == null) {
				mVerticalSectionVelocityGraph = new SectionGraph(getHolderFrame(), SectionGraph.VERTICAL_SECTION, mDataSetId, mChartPanel.getXYZDataSet(), mClickEntityYCoord, DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL),new WindowAdapter() {
					@Override
					/**
					 * WindowListener implementation
					 */
					public void windowClosing(WindowEvent theEvent) {
						mVerticalSectionVelocityGraph = null;
					}
				});
			} else {
				mVerticalSectionVelocityGraph.addOrRemoveSeries(mClickEntityYCoord.intValue());
			}
		}

		@Override
		protected void showHorizontalSectionGraph() {
			if (mHorizontalSectionVelocityGraph == null) {
				mHorizontalSectionVelocityGraph = new SectionGraph(getHolderFrame(), SectionGraph.HORIZONTAL_SECTION, mDataSetId, mChartPanel.getXYZDataSet(), mClickEntityZCoord, DAStrings.getString(DAStrings.HORIZONTAL_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL),new WindowAdapter() {
					@Override
					/**
					 * WindowListener implementation
					 */
					public void windowClosing(WindowEvent theEvent) {
						mHorizontalSectionVelocityGraph = null;
					}
				});
			} else {
				mHorizontalSectionVelocityGraph.addOrRemoveSeries(mClickEntityYCoord.intValue());
			}
		}

		@Override
		protected void showDepthAveragedGraph() {
			if (mDepthAveragedGraph == null) {
				mDepthAveragedGraph = new DepthAveragedGraph(mDataSetId, getHolderFrame(), mChartPanel.getXYZDataSet(), false, DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE), new WindowAdapter() {
					@Override
					/**
					 * WindowListener implementation
					 */
					public void windowClosing(WindowEvent theEvent) {
						mDepthAveragedGraph = null;
					}
				});
			}
		}

		@Override
		/**
		 * Create the right-click context menu
		 * @param properties Flag to show/hide properties menu item
		 * @param save Flag to show/hide save menu item
		 * @param print Flag to show/hide print menu item
		 * @param zoom Flag to show/hide zoom menu item
		 */
		protected JPopupMenu createPopupMenu(boolean properties, boolean save, boolean print, boolean zoom) {
			return createPopupMenu(properties, true, save, print, zoom);
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
			
			mThirdOrderCorrelationGraphs = new JMenu(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRAPHS));
			mCrossSectionGraphs = new JMenu(DAStrings.getString(DAStrings.CROSS_SECTION_GRAPHS));
			mVelocityGraphs = new JMenu(DAStrings.getString(DAStrings.VELOCITY_GRAPHS));
			mMomentumGraphs = new JMenu(DAStrings.getString(DAStrings.MOMENTUM_GRAPHS));
			mReynoldsStressGraphs = new JMenu(DAStrings.getString(DAStrings.REYNOLDS_STRESS_GRAPHS));
			mTIAndTKEAndVorticityGraphs = new JMenu(DAStrings.getString(DAStrings.TI_AND_TKE_AND_VORTICITY_GRAPHS));
			mQHGraphs = new JMenu(DAStrings.getString(DAStrings.QH_GRAPHS));
			
			mViewPercentageGoodGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VIEW_PERCENTAGE_GOOD_BUTTON_LABEL), null, DAStrings.getString(DAStrings.VIEW_PERCENTAGE_GOOD_BUTTON_DESC), mViewPercentageGoodGraphButton, this);
			
//			mViewDataPointDetailsButton = new JMenuItem();
//			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_LABEL), null, DAStrings.getString(DAStrings.VIEW_DATA_POINT_DETAILS_BUTTON_DESC), mViewDataPointDetailsButton, this);
			
			mViewUWQHSplitShearStressGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC), mViewUWQHSplitShearStressGraphButton, this);
				
			mViewUVQHSplitShearStressGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC), mViewUVQHSplitShearStressGraphButton, this);
				
			mCreateEOFRMatrixButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.CALCULATE_EOF_R_MATRIX_BUTTON_LABEL), null, DAStrings.getString(DAStrings.CALCULATE_EOF_R_MATRIX_BUTTON_DESC), mCreateEOFRMatrixButton, this);

			mViewAnisotropicStressTensorGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_DESC), mViewAnisotropicStressTensorGraphButton, this);
			
			mViewAnisotropicStressTensorInvariantIIGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC), mViewAnisotropicStressTensorInvariantIIGraphButton, this);

			mViewAnisotropicStressTensorInvariantIIIGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC), mViewAnisotropicStressTensorInvariantIIIGraphButton, this);
				
			mViewThirdOrderCorrelation300GraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC), mViewThirdOrderCorrelation300GraphButton, this);
	
			mViewThirdOrderCorrelation210GraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC), mViewThirdOrderCorrelation210GraphButton, this);
	
			mViewThirdOrderCorrelation120GraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC), mViewThirdOrderCorrelation120GraphButton, this);
	
			mViewThirdOrderCorrelation030GraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC), mViewThirdOrderCorrelation030GraphButton, this);
	
			mViewLateralVelocityGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.LATERAL_VELOCITY_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.LATERAL_VELOCITY_GRAPH_BUTTON_DESC), mViewLateralVelocityGraphButton, this);
			
			mViewMaximumVelocitiesGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.MAXIMUM_VELOCITIES_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC), mViewMaximumVelocitiesGraphButton, this);
			
			mViewLateralFluxOfStreamwiseTKEGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC), mViewLateralFluxOfStreamwiseTKEGraphButton, this);
			
			mViewDepthAveragedUVGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC), mViewDepthAveragedUVGraphButton, this);
	
			mViewDepthAveragedReynoldsStressGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC), mViewDepthAveragedReynoldsStressGraphButton, this);
			
			mViewHorizontalReynoldsStressGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC), mViewHorizontalReynoldsStressGraphButton, this);
						
			mViewVerticalReynoldsStressGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC), mViewVerticalReynoldsStressGraphButton, this);
			
			mViewTurbulenceGenerationAndDissipationGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC), mViewTurbulenceGenerationAndDissipationGraphButton, this);
						
			mViewVPrimeWPrimeGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.V_PRIME_W_PRIME_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.V_PRIME_W_PRIME_GRAPH_BUTTON_DESC), mViewVPrimeWPrimeGraphButton, this);
			
			mViewUPrimeVPrimeCorrelationGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.UV_CORRELATION_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.UV_CORRELATION_GRAPH_BUTTON_DESC), mViewUPrimeVPrimeCorrelationGraphButton, this);
						
			mViewUPrimeWPrimeCorrelationGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.UW_CORRELATION_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.UW_CORRELATION_GRAPH_BUTTON_DESC), mViewUPrimeWPrimeCorrelationGraphButton, this);
	
			mViewVPrimeWPrimeCorrelationGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VW_CORRELATION_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.VW_CORRELATION_GRAPH_BUTTON_DESC), mViewVPrimeWPrimeCorrelationGraphButton, this);
	
			mViewCombinedReynoldsStressMagnitudeGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC), mViewCombinedReynoldsStressMagnitudeGraphButton, this);
	
			mViewResultantReynoldsStressGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC), mViewResultantReynoldsStressGraphButton, this);

			mViewStreamwiseVorticityGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC), mViewStreamwiseVorticityGraphButton, this);
						
			mViewHorizontalReynoldsStressAtBedGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC), mViewHorizontalReynoldsStressAtBedGraphButton, this);
	
			mViewTIGraphsButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.TURBULENCE_INTENSITY_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC), mViewTIGraphsButton, this);
			
			mViewVelocityStandardDeviationGraphsButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC), mViewVelocityStandardDeviationGraphsButton, this);
			
			mViewQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC), mViewQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton, this);

			mViewScaledQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC), mViewScaledQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton, this);
			
			mViewQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC), mViewQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton, this);

			mViewScaledQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC), mViewScaledQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton, this);
			
			mViewQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC), mViewQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton, this);
			
			mViewScaledQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC), mViewScaledQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton, this);
			
			mViewQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC), mViewQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton, this);

			mViewScaledQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC), mViewScaledQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton, this);
			
			mViewQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC), mViewQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton, this);

			mViewScaledQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC), mViewScaledQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton, this);
			
			mViewQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC), mViewQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton, this);
			
			mViewScaledQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC), mViewScaledQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton, this);
			
			mViewStreamwiseTKEFluxGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.TKE_FLUX_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.TKE_FLUX_GRAPH_BUTTON_DESC), mViewStreamwiseTKEFluxGraphButton, this);
			
			mViewDifferenceOfSquaredLateralFluctuationRMSesGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC), mViewDifferenceOfSquaredLateralFluctuationRMSesGraphButton, this);

			mViewVorticityEquationGraphs = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VORTICITY_EQUATION_GRAPHS_BUTTON_LABEL), null, DAStrings.getString(DAStrings.VORTICITY_EQUATION_GRAPHS_BUTTON_DESC), mViewVorticityEquationGraphs, this);
			
			mViewStreamwiseVorticityKEGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC), mViewStreamwiseVorticityKEGraphButton, this);

			mViewMeanVelocityKEGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC), mViewMeanVelocityKEGraphButton, this);

			mViewTKEGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC), mViewTKEGraphButton, this);
						
			mViewVerticalTransferOfStreamwiseMomentumGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL), null, DAStrings.getString(DAStrings.SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC), mViewVerticalTransferOfStreamwiseMomentumGraphButton, this);
						
			mViewHorizontalTransferOfStreamwiseMomentumGraphButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL), null, DAStrings.getString(DAStrings.SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC), mViewHorizontalTransferOfStreamwiseMomentumGraphButton, this);
						
			theMenu.add(mViewPercentageGoodGraphButton);
//			theMenu.add(mViewDataPointDetailsButton);
			theMenu.addSeparator();
			
			theMenu.add(mCreateEOFRMatrixButton);
			theMenu.addSeparator();
			
			mVelocityGraphs.add(mViewLateralVelocityGraphButton);
			mVelocityGraphs.add(mViewDepthAveragedUVGraphButton);
			mVelocityGraphs.add(mViewVelocityStandardDeviationGraphsButton);
			mVelocityGraphs.add(mViewMaximumVelocitiesGraphButton);
			mCrossSectionGraphs.add(mVelocityGraphs);
						
			mMomentumGraphs.add(mViewVerticalTransferOfStreamwiseMomentumGraphButton);
			mMomentumGraphs.add(mViewHorizontalTransferOfStreamwiseMomentumGraphButton);
			mCrossSectionGraphs.add(mMomentumGraphs);
			
			mCrossSectionGraphs.add(mViewStreamwiseVorticityGraphButton);
			mCrossSectionGraphs.add(mViewMeanVelocityKEGraphButton);
			mCrossSectionGraphs.add(mViewStreamwiseVorticityKEGraphButton);
						
			mReynoldsStressGraphs.add(mViewHorizontalReynoldsStressGraphButton);
			mReynoldsStressGraphs.add(mViewVerticalReynoldsStressGraphButton);
			mReynoldsStressGraphs.add(mViewUPrimeVPrimeCorrelationGraphButton);
			mReynoldsStressGraphs.add(mViewUPrimeWPrimeCorrelationGraphButton);
			mReynoldsStressGraphs.add(mViewCombinedReynoldsStressMagnitudeGraphButton);
			mReynoldsStressGraphs.add(mViewResultantReynoldsStressGraphButton);
			mReynoldsStressGraphs.add(mViewDepthAveragedReynoldsStressGraphButton);
			mReynoldsStressGraphs.add(mViewHorizontalReynoldsStressAtBedGraphButton);
			mReynoldsStressGraphs.add(mViewAnisotropicStressTensorGraphButton);
			mReynoldsStressGraphs.add(mViewAnisotropicStressTensorInvariantIIGraphButton);
			mReynoldsStressGraphs.add(mViewAnisotropicStressTensorInvariantIIIGraphButton);
			mCrossSectionGraphs.add(mReynoldsStressGraphs);
			
			mTIAndTKEAndVorticityGraphs.add(mViewTIGraphsButton);
			mTIAndTKEAndVorticityGraphs.add(mViewTKEGraphButton);
			mTIAndTKEAndVorticityGraphs.add(mViewStreamwiseTKEFluxGraphButton);
			mTIAndTKEAndVorticityGraphs.add(mViewLateralFluxOfStreamwiseTKEGraphButton);
			mTIAndTKEAndVorticityGraphs.add(mViewVorticityEquationGraphs);
			mTIAndTKEAndVorticityGraphs.add(mViewDifferenceOfSquaredLateralFluctuationRMSesGraphButton);
			mTIAndTKEAndVorticityGraphs.add(mViewVPrimeWPrimeGraphButton);
			mTIAndTKEAndVorticityGraphs.add(mViewVPrimeWPrimeCorrelationGraphButton);
			mTIAndTKEAndVorticityGraphs.add(mViewTurbulenceGenerationAndDissipationGraphButton);
			mCrossSectionGraphs.add(mTIAndTKEAndVorticityGraphs);
			
			mQHGraphs.add(mViewQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewScaledQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton);
			mQHGraphs.add(mViewScaledQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton);
			mQHGraphs.add(mViewQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewScaledQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewScaledQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton);
			mQHGraphs.add(mViewScaledQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton);
			mQHGraphs.add(mViewQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewScaledQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton);
			mQHGraphs.add(mViewUWQHSplitShearStressGraphButton);
			mQHGraphs.add(mViewUVQHSplitShearStressGraphButton);
			mCrossSectionGraphs.add(mQHGraphs);

			mThirdOrderCorrelationGraphs.add(mViewThirdOrderCorrelation300GraphButton);
			mThirdOrderCorrelationGraphs.add(mViewThirdOrderCorrelation210GraphButton);
			mThirdOrderCorrelationGraphs.add(mViewThirdOrderCorrelation120GraphButton);
			mThirdOrderCorrelationGraphs.add(mViewThirdOrderCorrelation030GraphButton);
			mCrossSectionGraphs.add(mThirdOrderCorrelationGraphs);
			
			theMenu.add(mCrossSectionGraphs);
			
			return theMenu;
		}
	
		
		/**
		 * ActionListener implementation
		 * @param theEvent The action event
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent theEvent) {
			Action action = ((AbstractButton) theEvent.getSource()).getAction();
			
			if (action == null) {
				super.actionPerformed(theEvent);
				return;
			}
			
			String actionId = (String) action.getValue(Action.SHORT_DESCRIPTION);
			
			if (actionId.equals(DAStrings.getString(DAStrings.VIEW_PERCENTAGE_GOOD_BUTTON_DESC))) {
				ComponentTabsFrame ctp = new ComponentTabsFrame(getHolderFrame(),
																new PercentageGoodGraph(mDataSetId, null, BackEndAPI.DPS_KEY_PERCENTAGE_OF_U_VELOCITIES_GOOD, DAStrings.getString(DAStrings.PERCENTAGE_GOOD_GRID_TITLE)),
																new PercentageGoodGraph(mDataSetId, null, BackEndAPI.DPS_KEY_PERCENTAGE_OF_V_VELOCITIES_GOOD, DAStrings.getString(DAStrings.PERCENTAGE_GOOD_GRID_TITLE)),
																new PercentageGoodGraph(mDataSetId, null, BackEndAPI.DPS_KEY_PERCENTAGE_OF_W_VELOCITIES_GOOD, DAStrings.getString(DAStrings.PERCENTAGE_GOOD_GRID_TITLE)));

				ctp.showInFrame(DAStrings.getString(DAStrings.PERCENTAGE_GOOD_GRID_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
			} else if (actionId.equals(DAStrings.getString(DAStrings.MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC))) {
				ComponentTabsFrame ctp = new ComponentTabsFrame(getHolderFrame(),
						new MaximumVelocitiesGraph(mDataSetId, null, BackEndAPI.DPS_KEY_MAXIMUM_U, DAStrings.getString(DAStrings.MAXIMUM_VELOCITIES_GRAPH_TITLE)),
						new MaximumVelocitiesGraph(mDataSetId, null, BackEndAPI.DPS_KEY_MAXIMUM_V, DAStrings.getString(DAStrings.MAXIMUM_VELOCITIES_GRAPH_TITLE)),
						new MaximumVelocitiesGraph(mDataSetId, null, BackEndAPI.DPS_KEY_MAXIMUM_W, DAStrings.getString(DAStrings.MAXIMUM_VELOCITIES_GRAPH_TITLE)));

				ctp.showInFrame(DAStrings.getString(DAStrings.MAXIMUM_VELOCITIES_GRAPH_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
			}  else if (actionId.equals(DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_DESC))) {
				ComponentTabsFrame ctp = new ComponentTabsFrame(getHolderFrame(),
						new AnisotropicStressTensorGraph(mDataSetId, null, 0, 0, DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE)),
						new AnisotropicStressTensorGraph(mDataSetId, null, 1, 1, DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE)),
						new AnisotropicStressTensorGraph(mDataSetId, null, 2, 2, DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE)));

				ctp.showInFrame(DAStrings.getString(DAStrings.TKE_FLUX_GRAPH_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
			}  else if (actionId.equals(DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC))) {
				new AnisotropicStressTensorInvariantIIGraph(mDataSetId, null);
			}  else if (actionId.equals(DAStrings.getString(DAStrings.ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC))) {
				new AnisotropicStressTensorInvariantIIIGraph(mDataSetId, null);
			}  else if (actionId.equals(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC))) {
				new ThirdOrderCorrelationsGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[3][0][0], DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_300_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_300_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT));
			}  else if (actionId.equals(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC))) {
				new ThirdOrderCorrelationsGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[2][1][0], DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_210_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_210_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT));
			}  else if (actionId.equals(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC))) {
				new ThirdOrderCorrelationsGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[1][2][0], DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_120_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_120_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT));
			}  else if (actionId.equals(DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC))) {
				new ThirdOrderCorrelationsGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[0][3][0], DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_030_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_030_TITLE), DAStrings.getString(DAStrings.THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT), true);
				
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT), true);
				
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT), true);
				
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT), true);

			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT), true);
			
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC))) {
				new QuadrantHoleEventsRatioGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, DAStrings.getString(DAStrings.QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE), DAStrings.getString(DAStrings.QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT), true);
				
			} else if (actionId.equals(DAStrings.getString(DAStrings.TKE_FLUX_GRAPH_BUTTON_DESC))) {
				ComponentTabsFrame ctp = new ComponentTabsFrame(getHolderFrame(),
																new TKEFluxGraph(mDataSetId, null, BackEndAPI.DPS_KEY_U_TKE_FLUX, DAStrings.getString(DAStrings.X_DIRECTION_TKE_FLUX_GRAPH_TITLE)),
																new TKEFluxGraph(mDataSetId, null, BackEndAPI.DPS_KEY_V_TKE_FLUX, DAStrings.getString(DAStrings.Y_DIRECTION_TKE_FLUX_GRAPH_TITLE)),
																new TKEFluxGraph(mDataSetId, null, BackEndAPI.DPS_KEY_W_TKE_FLUX, DAStrings.getString(DAStrings.Z_DIRECTION_TKE_FLUX_GRAPH_TITLE)));
				
				ctp.showInFrame(DAStrings.getString(DAStrings.TKE_FLUX_GRAPH_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
			} else if (actionId.equals(DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC))) {
				try {
					MAJFCTabsFrame tp = new MAJFCTabsFrame(getHolderFrame(),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_1_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS, DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_2_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS, DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_3_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS, DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_4_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS, DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))));
						
						tp.showInFrame(DAStrings.getString(DAStrings.UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BackEndAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (actionId.equals(DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC))) {
				try {
					MAJFCTabsFrame tp = new MAJFCTabsFrame(getHolderFrame(),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_1_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS, DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_2_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS, DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_3_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS, DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))),
						new MAJFCTabContents(DAStrings.getString(DAStrings.QUADRANT_4_LABEL), new QuadrantHoleSplitShearStressGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS, DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE), DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL))));
						
						tp.showInFrame(DAStrings.getString(DAStrings.UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BackEndAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (actionId.equals(DAStrings.getString(DAStrings.CALCULATE_EOF_R_MATRIX_BUTTON_DESC))) {
				new EmpiricalOrthogonalFunctionAnalysisRMatrixCalculationDialog(mDataSetId);
			} else if (actionId.equals(DAStrings.getString(DAStrings.LATERAL_VELOCITY_GRAPH_BUTTON_DESC))) {
				new LateralVelocityGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC))) {
				new LateralTKEFluxGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC))) {
				new ScaledDepthAveragedUVGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC))) {
				new DepthAveragedReynoldsStressGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC))) {
				try {
					new ReynoldsStressGraph(mDataSetId, getHolderFrame(), true, BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN, DAStrings.getString(DAStrings.HORIZONTAL_REYNOLDS_STRESS_GRAPH_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT));
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BackEndAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (actionId.equals(DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC))) {
				try {
					new ReynoldsStressGraph(mDataSetId, getHolderFrame(), true, BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN, DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_GRAPH_TITLE), DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT));
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BackEndAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (actionId.equals(DAStrings.getString(DAStrings.TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC))) {
				try {
					MAJFCTabsFrame tp = new MAJFCTabsFrame(getHolderFrame(),
						new MAJFCTabContents(DAStrings.getString(DAStrings.HORIZONTAL_TURBULENCE_GENERATION_TAB_LABEL), new TurbulenceGenerationOrDissipationGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN, DAStrings.getString(DAStrings.HORIZONTAL_TURBULENCE_GENERATION_GRAPH_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT), false)),
						new MAJFCTabContents(DAStrings.getString(DAStrings.VERTICAL_TURBULENCE_GENERATION_TAB_LABEL), new TurbulenceGenerationOrDissipationGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN, DAStrings.getString(DAStrings.VERTICAL_TURBULENCE_GENERATION_GRAPH_TITLE), DAStrings.getString(DAStrings.VERTICAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT), false)),
						new MAJFCTabContents(DAStrings.getString(DAStrings.HORIZONTAL_TURBULENCE_DISSIPATION_TAB_LABEL), new TurbulenceGenerationOrDissipationGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN, DAStrings.getString(DAStrings.HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT), true)),
						new MAJFCTabContents(DAStrings.getString(DAStrings.VERTICAL_TURBULENCE_DISSIPATION_TAB_LABEL), new TurbulenceGenerationOrDissipationGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN, DAStrings.getString(DAStrings.VERTICAL_TURBULENCE_DISSIPATION_GRAPH_TITLE), DAStrings.getString(DAStrings.VERTICAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT), true)));
						
						tp.showInFrame(DAStrings.getString(DAStrings.TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BackEndAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (actionId.equals(DAStrings.getString(DAStrings.UV_CORRELATION_GRAPH_BUTTON_DESC))) {
				new CorrelationGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN, DAStrings.getString(DAStrings.UV_CORRELATION_GRAPH_TITLE), DAStrings.getString(DAStrings.UV_CORRELATION_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.UW_CORRELATION_GRAPH_BUTTON_DESC))) {
				new CorrelationGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN, DAStrings.getString(DAStrings.UW_CORRELATION_GRAPH_TITLE), DAStrings.getString(DAStrings.UW_CORRELATION_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.VW_CORRELATION_GRAPH_BUTTON_DESC))) {
				new CorrelationGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN, DAStrings.getString(DAStrings.VW_CORRELATION_GRAPH_TITLE), DAStrings.getString(DAStrings.VW_CORRELATION_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC))) {
				try {
					new CombinedReynoldsStressMagnitudeGraph(mDataSetId, getHolderFrame(), null, DAStrings.getString(DAStrings.COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE), DAStrings.getString(DAStrings.COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_LEGEND_TEXT));
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BackEndAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (actionId.equals(DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC))) {
				new StreamwiseVorticityGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC))) {
				new EstimatedVerticalReynoldsStressAtBedGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC))) {
				ComponentTabsFrame ctp = new ComponentTabsFrame(getHolderFrame(),
																new TurbulenceIntensityGraph(mDataSetId, null, BackEndAPI.DPS_KEY_RMS_U_PRIME, DAStrings.getString(DAStrings.X_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE), DAStrings.getString(DAStrings.X_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE)),
																new TurbulenceIntensityGraph(mDataSetId, null, BackEndAPI.DPS_KEY_RMS_V_PRIME, DAStrings.getString(DAStrings.Y_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE), DAStrings.getString(DAStrings.Y_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE)),
																new TurbulenceIntensityGraph(mDataSetId, null, BackEndAPI.DPS_KEY_RMS_W_PRIME, DAStrings.getString(DAStrings.Z_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE), DAStrings.getString(DAStrings.Z_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE)));
																
				ctp.showInFrame(DAStrings.getString(DAStrings.TURBULENCE_INTENSITY_GRAPH_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
			} else if (actionId.equals(DAStrings.getString(DAStrings.DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC))) {
				new DifferenceOfLateralFluctuationMeanSquaresGraph(mDataSetId, getHolderFrame(), null, DAStrings.getString(DAStrings.DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_FRAME_TITLE), DAStrings.getString(DAStrings.DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_TITLE));
			} else if (actionId.equals(DAStrings.getString(DAStrings.STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC))) {
				ComponentTabsFrame ctp = new ComponentTabsFrame(getHolderFrame(),
																new VelocityStandardDeviationGraph(mDataSetId, null, BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV, DAStrings.getString(DAStrings.U_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE), DAStrings.getString(DAStrings.U_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT)),
																new VelocityStandardDeviationGraph(mDataSetId, null, BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV, DAStrings.getString(DAStrings.V_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE), DAStrings.getString(DAStrings.V_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT)),
																new VelocityStandardDeviationGraph(mDataSetId, null, BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV, DAStrings.getString(DAStrings.W_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE), DAStrings.getString(DAStrings.W_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT)));
				ctp.showInFrame(DAStrings.getString(DAStrings.STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
			} else if (actionId.equals(DAStrings.getString(DAStrings.RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC))) {
				new ResultantShearStressGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.VORTICITY_EQUATION_GRAPHS_BUTTON_DESC))) {
				VorticityEquationTermsGraphsPanel vetgp = new VorticityEquationTermsGraphsPanel(mDataSetId, getHolderFrame());
				
				vetgp.showInFrame(DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERMS_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');

			} else if (actionId.equals(DAStrings.getString(DAStrings.STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC))) {
				new StreamwiseVorticityKEGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.V_PRIME_W_PRIME_GRAPH_BUTTON_DESC))) {
				try {
					new ReynoldsStressGraph(mDataSetId, getHolderFrame(), false, BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN, DAStrings.getString(DAStrings.V_PRIME_W_PRIME_GRAPH_TITLE), DAStrings.getString(DAStrings.V_PRIME_W_PRIME_GRAPH_LEGEND_TEXT));
				} catch (HeadlessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BackEndAPIException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (actionId.equals(DAStrings.getString(DAStrings.MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC))) {
				ComponentTabsFrame ctp = new ComponentTabsFrame(getHolderFrame(),
																new MeanVelocityKineticEnergyGraph(mDataSetId, null, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, DAStrings.getString(DAStrings.U_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE), DAStrings.getString(DAStrings.U_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT)),
																new MeanVelocityKineticEnergyGraph(mDataSetId, null, BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, DAStrings.getString(DAStrings.V_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE), DAStrings.getString(DAStrings.V_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT)),
																new MeanVelocityKineticEnergyGraph(mDataSetId, null, BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, DAStrings.getString(DAStrings.W_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE), DAStrings.getString(DAStrings.W_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT)));

				ctp.showInFrame(DAStrings.getString(DAStrings.STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE) + '(' + mDataSetId.getFullDisplayString() + ')');
			} else if (actionId.equals(DAStrings.getString(DAStrings.TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC))) {
				new TKEGraph(mDataSetId, getHolderFrame());
			} else if (actionId.equals(DAStrings.getString(DAStrings.SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC))){
				new TransferOfStreamwiseMomentumGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, DAStrings.getString(DAStrings.VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE), DAStrings.getString(DAStrings.VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT));
			} else if (actionId.equals(DAStrings.getString(DAStrings.SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC))){
				new TransferOfStreamwiseMomentumGraph(mDataSetId, getHolderFrame(), BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, DAStrings.getString(DAStrings.HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT));
			} else {
				super.actionPerformed(theEvent);
			}
		}
		
		/**
		 * Called when the chart is right-clicked
	     *
	     * @param x Horizontal position of the context menu (and thus of the right-click, hopefully)
		 * @param y Vertical position of the context menu (and thus of the right-click, hopefully)
		 */
		protected void displayPopupMenu(int x, int y) {
			if (isEmpty()) {
				return;
			}
			
//			double yCoord = mThePlot.getDomainAxis().java2DToValue(x, getChartRenderingInfo().getPlotInfo().getDataArea(), mThePlot.getDomainAxisEdge());
//			double zCoord = mThePlot.getRangeAxis().java2DToValue(y, getChartRenderingInfo().getPlotInfo().getDataArea(), mThePlot.getRangeAxisEdge());
//		   
//			XYItemEntity closest = findClosestEntity(yCoord, zCoord);
//			mClickEntityYCoord = getDataSet().getX(0, closest.getItem());
//			mClickEntityZCoord = getDataSet().getY(0, closest.getItem());
//			mThePlot.handleClick(x, y, getChartRenderingInfo().getPlotInfo());
//			mThePlot.setDomainCrosshairValue(mClickEntityYCoord.doubleValue());
//			mThePlot.setRangeCrosshairValue(mClickEntityZCoord.doubleValue());
			
			super.displayPopupMenu(x, y);

			enableMenuItems();
		}
		
//		@SuppressWarnings("unchecked")
//		private XYItemEntity findClosestEntity(double yCoord, double zCoord) {
//			if (isEmpty()) {
//		    	return null;
//		    }
//			
//			Iterator<ChartEntity> iterator = getChartRenderingInfo().getEntityCollection().iterator();
//		    XYItemEntity theEntity = null;
//		    XYItemEntity closest = null;
//		    double minDistance = 99999999;
//		    
//		    while (iterator.hasNext()) {
//		    	try {
//		    		theEntity = (XYItemEntity) iterator.next();
//		    		
//		    		if (theEntity.getSeriesIndex() != 0) {
//		    			continue;
//		    		}
//		    	} catch (ClassCastException theException) {
//		    		theEntity = null;
//		    		continue;
//		    	}
//		    	
//		    	double entityYCoord = getDataSet().getX(0, theEntity.getItem()).doubleValue();
//		    	double entityZCoord = getDataSet().getY(0, theEntity.getItem()).doubleValue();
//		    	double distance = Math.sqrt(Math.pow(yCoord - entityYCoord, 2) + Math.pow(zCoord - entityZCoord, 2));
//	
//		    	if (distance <  minDistance) {
//		    		minDistance = distance;
//		    		closest = theEntity;
//		    	}
//		    }
//		    
//		    return closest;
//		}
			
		/**
		 * Checks whether this graph has any points
		 * @return True is there are no points
		 */
		private boolean isEmpty() {
		    return mChartPanel.getDataSet().getItemCount(0) == 0;

//			ChartRenderingInfo info = getChartRenderingInfo();
//	        
//		    if (info == null) {
//		    	return true;
//		    }
//		    
//		    
//		    EntityCollection theEntities = info.getEntityCollection();
//		    
//		    // theEntities always seems to hold a ChartEntity at the start, even if there are no points
//		    if (theEntities == null || theEntities.getEntityCount() < 2) {
//		    	return true;
//		    }
//		    
//		    return false;
		}
		
		@Override
		/**
		 * ChartMouseListener implementation
		 * @param theEvent The event
		 */
		public void chartMouseClicked(ChartMouseEvent theEvent) {
//			if (theEvent.getTrigger().getClickCount() != 2) {
//				return;
//			}
//			
//			if (theEvent.getEntity() instanceof XYItemEntity == false) {
//				return;
//			}
			
//			XYItemEntity theEntity = (XYItemEntity) theEvent.getEntity();
//			
//			if (theEntity == null) {
//				return;
//			}
//
//			int yCoord = getDataSet().getX(0, theEntity.getItem()).intValue();
//			int zCoord = getDataSet().getY(0, theEntity.getItem()).intValue();
			
//			try {
//				DAFrame.getBackEndAPI().loadDataPointDetails(0, mDataSetId, yCoord, zCoord, DAFrame.getFrame());
//			} catch (Exception theException) {
//			}
		}

		@Override
		/**
		 * ChartMouseListener implementation
		 * @param theEvent The event
		 */
		public void chartMouseMoved(ChartMouseEvent theEvent) {
		}

	}
	
	@Override
	/**
	 * Updates the display
	 */
	public void updateDisplay() {
		double meanVelocity = 1.0, rms = 1.0;
		@SuppressWarnings("unused")
		int leftBankPosition = 0, rightBankPosition = 0, bedLevel = 0, waterDepth = 0;
		
		try {
			meanVelocity = DAFrame.getBackEndAPI().getCrossSectionDataField(mDataSetId, mMeanVelocityCSDKey);
			rms = DAFrame.getBackEndAPI().getCrossSectionDataField(mDataSetId, mRMSVelocityCSDKey);
			
			DataSetConfig config = DAFrame.getBackEndAPI().getConfigData(mDataSetId);
			leftBankPosition = config.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue();
			rightBankPosition = config.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue();
			waterDepth = config.get(BackEndAPI.DSC_KEY_WATER_DEPTH).intValue();
		} catch (BackEndAPIException theException) {
			meanVelocity = 1.0;
			theException.printStackTrace();
		}
		
		String chartTitle = MAJFCTools.substituteIntoString(mRawChartTitle, new String[] { MAJFCTools.formatNumber(DAFrame.getFrame().getChannelMeanVelocityForScaling(mDataSetId, mDataSetType), 4, true), MAJFCTools.formatNumber(meanVelocity, 4, true, true), MAJFCTools.formatNumber(rms, 4, true, true)});
		mChartPanel.setFixedTitle(chartTitle);
		mChartPanel.setNonDimensionlessAxisRange(leftBankPosition, rightBankPosition, 0, waterDepth);
		mChartPanel.updateDisplay();
		
		super.updateDisplay();
	}
	
	@Override
	/**
	 * DomainInfo implementation
	 */
	public Range getDomainBounds(boolean includeInterval) {
		return mXRange;
	}

	@Override
	/**
	 * DomainInfo implementation
	 */
	public double getDomainLowerBound(boolean includeInterval) {
		return mXRange.getLowerBound();
	}

	@Override
	/**
	 * DomainInfo implementation
	 */
	public double getDomainUpperBound(boolean includeInterval) {
		return mXRange.getUpperBound();
	}

	@Override
	/**
	 * RangeInfo implementation
	 */
	public Range getRangeBounds(boolean includeInterval) {
		return mYRange;
	}

	@Override
	/**
	 * RangeInfo implementation
	 */
	public double getRangeLowerBound(boolean includeInterval) {
		return mYRange.getLowerBound();
	}

	@Override
	/**
	 * RangeInfo implementation
	 */
	public double getRangeUpperBound(boolean includeInterval) {
		return mYRange.getUpperBound();
	}

	/**
	 * Sets the axis ranges
	 * @param xRange
	 * @param yRange
	 */
	public void setRanges(Range xRange, Range yRange) {
		mXRange = xRange;
		mYRange = yRange;
	}

	/**
	 * Add another data set to the chart
	 * @param dataSet The new data set
	 * @param renderer The renderer to use for this dataset
	 */
	public void addDataSet(XYDataset dataSet, XYItemRenderer renderer) {
		int index = mThePlot.getDatasetCount();
		
		mThePlot.setDataset(index, dataSet);
		mThePlot.setRenderer(index, renderer);
	}

	/**
	 * Sets the id of the data set shown by this display
	 * @param dataSetId The data set id
	 */
	public void setDataSetId(AbstractDataSetUniqueId dataSetId) {
		mDataSetId = dataSetId;
		
		if (mVerticalSectionVelocityGraph != null) {
			mVerticalSectionVelocityGraph.setDataSetId(mDataSetId);
		}
	}
	
	/**
	 * Enables menu items depending on what has been calculated
	 */
	private void enableMenuItems() {
		try {
			Hashtable<DataPointSummaryIndex, DataPointSummaryIndex> missingDPSFieldKeys = DAFrame.getBackEndAPI().getMissingDataPointSummaryFieldKeys(mDataSetId);

			mReynoldsStressGraphs.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN) == false && missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN) == false && missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN) == false);

			mViewUWQHSplitShearStressGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS) == false);
			mViewUVQHSplitShearStressGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS) == false);
			mViewQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO) == false);
			mViewQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO) == false);
			mViewQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false);
			mViewQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO) == false);
			mViewQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO) == false);
			mViewQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false);

			mViewScaledQuadrantHoleUPrimeWPrimeQ1ToQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO) == false);
			mViewScaledQuadrantHoleUPrimeWPrimeQ2ToQ4EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO) == false);
			mViewScaledQuadrantHoleUPrimeWPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false);
			mViewScaledQuadrantHoleUPrimeVPrimeQ1ToQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO) == false);
			mViewScaledQuadrantHoleUPrimeVPrimeQ2ToQ4EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO) == false);
			mViewScaledQuadrantHoleUPrimeVPrimeQ2AndQ4ToQ1AndQ3EventsRatioGraphButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false);

			mTIAndTKEAndVorticityGraphs.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_U_PRIME) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_V_PRIME) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_W_PRIME) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_TKE) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_U_TKE_FLUX) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_V_TKE_FLUX) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_W_TKE_FLUX) == false);
			mThirdOrderCorrelationGraphs.setEnabled(mTIAndTKEAndVorticityGraphs.isEnabled());
			
			mQHGraphs.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO) == false
					&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false);

			mCreateEOFRMatrixButton.setEnabled(missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION) == false
							&& missingDPSFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION) == false);
				
		} catch (BackEndAPIException theException) {
			
		}
	}
}
