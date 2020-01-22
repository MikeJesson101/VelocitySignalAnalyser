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

package com.mikejesson.vsa.widgits.basicAndAbstractGraphs;




import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.xy.XYZDataset;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCLinkedGUIComponentsAction;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public abstract class ColourCodedChartPanel extends ScaleableChartPanel {
	private ScaleableXYZDataSet mDataSet; 
	private XYBlockRenderer mChartRenderer;
	private LookupPaintScale mChartPaintScale;
	private JLabel[] mCrossSectionGridLegendLabels;
	private JPanel mCrossSectionGraphLegend;
	
	private JMenuItem mVerticalSectionGraphButton;
	private JMenuItem mHorizontalSectionGraphButton;
	private JMenuItem mDepthAveragedGraphButton;
	
	private final int STEPS;
	private final int LEGEND_NUMBER_OF_LINES = 2;
	private final int LEGEND_MAX_LABELS_PER_LINE = 5;
	private final String mLegendText;
	private final double mNonDimensionlessBlockHeight, mNonDimensionlessBlockWidth;
	private final double mDimensionlessBlockHeight, mDimensionlessBlockWidth;
	
	/**
	 * @param chart
	 */
	public ColourCodedChartPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYZDataSet scaleableDataSet, double nonDimensionlessBlockHeight, double nonDimensionlessBlockWidth, int steps, String legendText, boolean dataIsVectorInY) {
		this(dataSetId, holderFrame, theChart, scaleableDataSet, nonDimensionlessBlockHeight, nonDimensionlessBlockWidth, steps, DAStrings.getString(DAStrings.CROSS_SECTION_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.CROSS_SECTION_GRID_Y_AXIS_TITLE), legendText, dataIsVectorInY);
	}
	
	/**
	 * @param chart
	 */
	public ColourCodedChartPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYZDataSet scaleableDataSet, double nonDimensionlessBlockHeight, double nonDimensionlessBlockWidth, int steps, String xAxisLabel, String yAxisLabel, String legendText, boolean dataIsVectorInY) {
		super(dataSetId, holderFrame, theChart, scaleableDataSet, false, xAxisLabel, yAxisLabel, dataIsVectorInY);
		
		STEPS = steps;
		mLegendText = legendText;
		mNonDimensionlessBlockHeight = nonDimensionlessBlockHeight;
		mNonDimensionlessBlockWidth = nonDimensionlessBlockWidth;
		
		double[] nonDimensionlessAxisRanges = scaleableDataSet.getDimensionedAxisRanges(); 
		mDimensionlessBlockHeight = (double) mNonDimensionlessBlockHeight/((double) (nonDimensionlessAxisRanges[3] - nonDimensionlessAxisRanges[2]));
		mDimensionlessBlockWidth = (double) mNonDimensionlessBlockWidth/((double) (nonDimensionlessAxisRanges[1] - nonDimensionlessAxisRanges[0]));
		
		mChartRenderer = new XYBlockRenderer();
		
		mDataSet = scaleableDataSet;
		
		buildGUI();
	}

	@Override
	/**
	 * Sets the ranges for the two axes
	 * @param domainAxisXMin
	 * @param domainAxisXMax
	 * @param rangeAxisYMin
	 * @param rangeAxisYMax
	 */
	protected void setAxisRanges(double domainAxisXMin, double domainAxisXMax, double rangeAxisYMin, double rangeAxisYMax) {
		if (areAxesDimensionless()) {
			mChartRenderer.setBlockHeight(mDimensionlessBlockHeight);
			mChartRenderer.setBlockWidth(mDimensionlessBlockWidth);
			
		} else {
			mChartRenderer.setBlockHeight(mNonDimensionlessBlockHeight);
			mChartRenderer.setBlockWidth(mNonDimensionlessBlockWidth);
		}
		
		super.setAxisRanges(domainAxisXMin, domainAxisXMax, rangeAxisYMin, rangeAxisYMax);
	}
	
	/**
	 * Builds the GUI
	 * @return The next free y-position in the GridBagLayout
	 */
	protected int buildGUI() {
		int y = super.buildGUI();
		mCrossSectionGraphLegend = new JPanel(new GridBagLayout());
		 
		getGUI().add(mCrossSectionGraphLegend, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
		
		updateDisplay();
		
		return y;
	}

	/**
	 * Builds the legend for the cross-section grid
	 */
	protected MAJFCPanel getCustomLegendPanel() {
		return null;
	}
	
	/**
	 * Gets the data set
	 * @return The data set
	 */
	protected XYZDataset getDataSet() {
		return mDataSet;
	}
	
	/**
	 * Gets the legend panel for the graph
	 * @return The legend panel
	 */
	protected JPanel getGraphLegend() {
		return mCrossSectionGraphLegend;
	}
	
	/**
	 * Gets the renderer for the graph
	 * @return The renderer
	 */
	public XYBlockRenderer getRenderer() {
		return mChartRenderer;
	}
	
	/**
	 * Builds the legend for the cross-section grid
	 * Called from setupPaintColours
	 * @see ColourCodedChartPanel#setupPaintColours()
	 */
	protected void buildCrossSectionGridLegend() {
		mCrossSectionGraphLegend.removeAll();
		int x = 0;
		int y = 0;
		int labelsPerLine = Math.round(mCrossSectionGridLegendLabels.length/LEGEND_NUMBER_OF_LINES);
		double labelsStep = 1;
		
		if (labelsPerLine > LEGEND_MAX_LABELS_PER_LINE) {
			labelsStep = mCrossSectionGridLegendLabels.length/(LEGEND_MAX_LABELS_PER_LINE * LEGEND_NUMBER_OF_LINES);
			labelsPerLine = LEGEND_MAX_LABELS_PER_LINE;
		}
		
		for (int i = 0; i < labelsPerLine * LEGEND_NUMBER_OF_LINES; ++i) {
			if (i == labelsPerLine) {
				x = 0;
				++y;
			}

			int labelIndex = (int) Math.round(labelsStep * i);
			mCrossSectionGraphLegend.add(mCrossSectionGridLegendLabels[labelIndex], MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 10, 10, 10, 20, 0));
		}

		++y;
		MAJFCPanel customLegendPanel = getCustomLegendPanel();
		
		if (customLegendPanel != null) {
			mCrossSectionGraphLegend.add(customLegendPanel, MAJFCTools.createGridBagConstraint(0, y + 2, labelsPerLine, 1, 1.0, 1.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));
		}
	}
	
	/**
	 * Updates the display
	 */
	public void updateDisplay() {
		if (mDataSet != null) {
			mCrossSectionGraphLegend.setVisible(mDataSet.getItemCount(0) != 0);
			setupPaintColours();
		}
		
		super.updateDisplay();
	}
	
	/**
	 * Sets up the paint colours for the chart points based on the U mean velocity at the point
	 */
	protected void setupPaintColours() {
		if (DAFrame.getFrame().fileImportInProgress()) {
			return;
		}
		
		int points = mDataSet.getItemCount(0);
		
		if (points == 0) {
			return;
		}
		
		double maxZ = mDataSet.getZValue(0, 0);
		
		if (Double.isInfinite(maxZ) || Double.isNaN(maxZ)) {
			return;
		}
		
		double minZ = maxZ;
		
		for (int i = 0; i < points; ++i) {
			double thisZ = mDataSet.getZValue(0, i);
			maxZ = thisZ > maxZ ? thisZ : maxZ;
			minZ = thisZ < minZ ? thisZ : minZ;
		}
		
		if (minZ == maxZ) {
			maxZ++;
		}
		
		mChartPaintScale = new LookupPaintScale(minZ, maxZ, Color.gray);

		double step = (maxZ - minZ)/(STEPS - 1);
		double[] values = new double[STEPS];
		mCrossSectionGridLegendLabels = new JLabel[STEPS];
		
		// Should give a set of values, rounded down to 0.1, with first the highest such value that is less than
		// minU and the last the lowest such value which is greater than maxU.
		for (int i = 0; i < STEPS; ++i) {
			// Rounds down to the nearest 0.1
			//values[i] = Math.floor((minZ + step * i)*10)/10;
			values[i] = minZ + step * i;
		}
		
		for (int i = 0; i < STEPS; ++i) {
			Color theColour;
			
			if (i < STEPS/3) {
				theColour = new Color(0 + (2 * i * (255 - 0)/STEPS), 255 - 2 * i * 255/STEPS, 0);
			} else if (i > 2*STEPS/3) {
				theColour = new Color(0 + (i * (255 - 0)/STEPS), 255 - i * 255/STEPS, 0);
			} else {
				theColour = new Color(255 - ((i - STEPS/3) * 255/STEPS), 255 - ((i - STEPS/3) * 255/STEPS), 0);
			}
			
			mChartPaintScale.add(values[i], theColour);
			
			String text;
			
			if (i == STEPS - 1) {
				text = MAJFCTools.formatNumber(values[i], 3, true) + " < " + mLegendText;
			} else {
				text = MAJFCTools.formatNumber(values[i], 3, true) + " < " + mLegendText + " <" + MAJFCTools.formatNumber(values[i + 1], 3, true);
			}
			
			mCrossSectionGridLegendLabels[i] = new JLabel(text);
			mCrossSectionGridLegendLabels[i].setOpaque(true);
			mCrossSectionGridLegendLabels[i].setBackground(theColour);
			mCrossSectionGridLegendLabels[i].setHorizontalAlignment(JLabel.CENTER);
			mCrossSectionGridLegendLabels[i].setBorder(BorderFactory.createLineBorder(Color.black));
		}
		
		buildCrossSectionGridLegend();
		
		mChartRenderer.setPaintScale(mChartPaintScale);
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
		return createPopupMenu(properties, false, save, print, zoom);
	}
	
	@Override
	/**
	 * Create the right-click context menu
	 * @param properties Flag to show/hide properties menu item
	 * @param copy Flag to show/hide copy menu item
	 * @param save Flag to show/hide save menu item
	 * @param print Flag to show/hide print menu item
	 * @param zoom Flag to show/hide zoom menu item
	 */
	protected JPopupMenu createPopupMenu(boolean properties, boolean copy, boolean save, boolean print, boolean zoom) {
		JPopupMenu theMenu = super.createPopupMenu(false, copy, save, print, false);
		theMenu.addSeparator();
		
		mVerticalSectionGraphButton = new JMenuItem();
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.VERTICAL_SECTION_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.VERTICAL_SECTION_GRAPH_BUTTON_DESC), mVerticalSectionGraphButton, this);

		mHorizontalSectionGraphButton = new JMenuItem();
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.HORIZONTAL_SECTION_GRAPH_BUTTON_DESC), mHorizontalSectionGraphButton, this);

		mDepthAveragedGraphButton = new JMenuItem();
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.DEPTH_AVERAGED_GRAPH_BUTTON_LABEL), null, DAStrings.getString(DAStrings.DEPTH_AVERAGED_GRAPH_BUTTON_DESC), mDepthAveragedGraphButton, this);
		
		theMenu.add(mVerticalSectionGraphButton);
		theMenu.add(mHorizontalSectionGraphButton);
		theMenu.add(mDepthAveragedGraphButton);
		
		return theMenu;
	}
	
	@Override
	public void actionPerformed(ActionEvent theEvent) {
		String action = theEvent.getActionCommand();
		
		if (action.equals(DAStrings.getString(DAStrings.VERTICAL_SECTION_GRAPH_BUTTON_LABEL))) {
			showVerticalSectionGraph();
		} else if (action.equals(DAStrings.getString(DAStrings.HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL))) {
			showHorizontalSectionGraph();
		} else if (action.equals(DAStrings.getString(DAStrings.DEPTH_AVERAGED_GRAPH_BUTTON_LABEL))) {
			showDepthAveragedGraph();
		} else if (action.equals(DAStrings.getString(DAStrings.RELATIVE_TO_MEAN_CHECKBOX_LABEL))) {
			updateDisplay();
		} else {
			super.actionPerformed(theEvent);
		}
	}

	public ScaleableXYZDataSet getXYZDataSet() {
		return mDataSet;
	}
	
	protected abstract void showVerticalSectionGraph();
	protected abstract void showHorizontalSectionGraph();
	protected abstract void showDepthAveragedGraph();
}
