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
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class ScaleableChartPanel extends ExportableChartPanel {
	@SuppressWarnings("unused")
	private final AbstractDataSetUniqueId mDataSetId;
	private MAJFCPanel mTheGUI;
	private JCheckBox mDimensionlessAxes;
	private boolean mAxesDimensionless;
	private JCheckBox mHideTitle;
	private boolean mTitleHidden;
	private JCheckBox mMirrorAboutVertical;
	private boolean mMirroredAboutVertical;
	private boolean mDataIsVectorInY;
	private JCheckBox mXAxisLogarithmic;
	private boolean mXAxisIsLogarithmic;
	private JCheckBox mYAxisLogarithmic;
	private boolean mYAxisIsLogarithmic;
	private String mChartTitle;
	private final String mRawChartTitle;
	private boolean mTitleFixed;
	private final String mDimensionedXAxisTitle, mDimensionedYAxisTitle;
	private final ScaleableXYDataSet mScaleableDataSet;
	private JCheckBox mRelativeToMean;
	private boolean mIsRelativeToMean;
	private JCheckBox mShowDimensionlessValues;
	private boolean mIsShowingDimensionlessValues;
	
	/**
	 * Constructor
	 * @param theChart The chart to show in this panel
	 * @param dataSetId The id of the data set for which this panel displays data
	 * @param buildGUI If true, then the GUI is built from this constructor. If a child class is to build the GUI, set this to false and call
	 * buildGUI from the child version
	 * @param dimensionedXAxisTitle Title for the x-axis when it has dimensions
	 * @param dimensionedYAxisTitle Title for the y-axis when it has dimensions
	 * @param showDimensionlessAxesCheckBox Show (or don't) the dimensionless axes checkbox
	 * @param showMirrorAboutVerticalCheckbox Show (or don't) the mirror about vertical checkbox
	 * @param dataIsVectorAboutY If true then sign of the data is reversed when the data is mirrored about the vertical
	 * @param showHideTitleCheckBox Show (or don't) the hide title checkbox
	 * @param showLogartihmicAxesCheckBoxes Show (or don't) the logarithmic axes checkboxes
	 */
	public ScaleableChartPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYDataSet scaleableDataSet, boolean buildGUI, String dimensionedXAxisTitle, String dimensionedYAxisTitle, boolean showDimensionlessAxesCheckBox, boolean showHideTitleCheckBox, boolean showMirrorAboutVerticalCheckbox, boolean dataIsVectorInY, boolean showLogartihmicAxesCheckBoxes) {
		this(dataSetId, holderFrame, theChart, scaleableDataSet, buildGUI, dimensionedXAxisTitle, dimensionedYAxisTitle, dataIsVectorInY);

		mDimensionlessAxes.setVisible(showDimensionlessAxesCheckBox);
		mMirrorAboutVertical.setVisible(showMirrorAboutVerticalCheckbox);
		mHideTitle.setVisible(showHideTitleCheckBox);
		mXAxisLogarithmic.setVisible(showLogartihmicAxesCheckBoxes);
	}
	
	/**
	 * Constructor
	 * @param theChart The chart to show in this panel
	 * @param dataSetId The id of the data set for which this panel displays data
	 * @param buildGUI If true, then the GUI is built from this constructor. If a child class is to build the GUI, set this to false and call
	 * buildGUI from the child version
	 * @param dimensionedXAxisTitle Title for the x-axis when it has dimensions
	 * @param dimensionedYAxisTitle Title for the y-axis when it has dimensions
	 * @param dataIsVectorAboutY If true then sign of the data is reversed when the data is mirrored about the vertical
	 */
	public ScaleableChartPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame holderFrame, JFreeChart theChart, ScaleableXYDataSet scaleableDataSet, boolean buildGUI, String dimensionedXAxisTitle, String dimensionedYAxisTitle, boolean dataIsVectorInY) {
		super(holderFrame, theChart);
		
		mChartTitle = theChart.getTitle().getText();
		mRawChartTitle = mChartTitle;
		mTitleFixed = false;
		
		theChart.setBackgroundPaint(Color.white);
		
		mDataSetId = dataSetId;
		mScaleableDataSet = scaleableDataSet;
		mDimensionedXAxisTitle = dimensionedXAxisTitle;
		mDimensionedYAxisTitle = dimensionedYAxisTitle;
		mDataIsVectorInY = dataIsVectorInY;
		
		if (buildGUI) {
			buildGUI();
		}
	}

	/**
	 * Builds the GUI
	 * @return The next free y-position in the GridBagLayout
	 */
	protected int buildGUI() {
		mTheGUI = new MAJFCPanel(new GridBagLayout());
		mTheGUI.setBorder(BorderFactory.createLineBorder(Color.black));
		
		mDimensionlessAxes = new JCheckBox(DAStrings.getString(DAStrings.DIMENSIONLESS_AXES_LABEL));
		mAxesDimensionless = mDimensionlessAxes.isSelected();

		mDimensionlessAxes.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mAxesDimensionless = mDimensionlessAxes.isSelected();
				mScaleableDataSet.setAxesDimensionless(mAxesDimensionless);
				setAxisRanges(mScaleableDataSet.getAxisRanges());
				setAxisLabels();
				updateDisplay();	
			}
		});
		
		mHideTitle = new JCheckBox(DAStrings.getString(DAStrings.HIDE_TITLE_LABEL));
		mTitleHidden = mHideTitle.isSelected();

		mHideTitle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mTitleHidden = mHideTitle.isSelected();
				setAxisRanges(mScaleableDataSet.getAxisRanges());
				
				if (mTitleHidden) {
					getChart().setTitle("");
				} else {
					getChart().setTitle(mChartTitle);
				}
				
				updateDisplay();	
			}
		});
		
		mMirrorAboutVertical = new JCheckBox(DAStrings.getString(DAStrings.MIRROR_ABOUT_VERTICAL_LABEL));
		mMirroredAboutVertical = mMirrorAboutVertical.isSelected();

		mMirrorAboutVertical.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mMirroredAboutVertical = mMirrorAboutVertical.isSelected();
				mScaleableDataSet.setMirroredAboutVertical(mMirroredAboutVertical, mDataIsVectorInY);
				setAxisRanges(mScaleableDataSet.getAxisRanges());

				updateDisplay();	
			}
		});
		
		mXAxisLogarithmic = new JCheckBox(DAStrings.getString(DAStrings.X_AXIS_LOGARITHMIC_LABEL));
		mXAxisIsLogarithmic = mXAxisLogarithmic.isSelected();

		mXAxisLogarithmic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mXAxisIsLogarithmic = mXAxisLogarithmic.isSelected();
				setAxisRanges(mScaleableDataSet.getAxisRanges());

				updateDisplay();	
			}
		});
		
		mYAxisLogarithmic = new JCheckBox(DAStrings.getString(DAStrings.Y_AXIS_LOGARITHMIC_LABEL));
		mYAxisIsLogarithmic = mYAxisLogarithmic.isSelected();

		mYAxisLogarithmic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mYAxisIsLogarithmic = mYAxisLogarithmic.isSelected();
				setAxisRanges(mScaleableDataSet.getAxisRanges());

				updateDisplay();	
			}
		});
		
		mRelativeToMean = new JCheckBox(DAStrings.getString(DAStrings.RELATIVE_TO_MEAN_CHECKBOX_LABEL));
		mRelativeToMean.addActionListener(this);
			
		mRelativeToMean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mIsRelativeToMean = mRelativeToMean.isSelected();
				mScaleableDataSet.setRelativeToMean(mIsRelativeToMean);

				updateDisplay();	
			}
		});
		
		mShowDimensionlessValues = new JCheckBox(DAStrings.getString(DAStrings.DIMENSIONLESS_CHECKBOX_LABEL));
		mShowDimensionlessValues.addActionListener(this);
		mShowDimensionlessValues.setEnabled(mScaleableDataSet.mScalerForDimensionlessValues != 1.0);
			
		mShowDimensionlessValues.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mIsShowingDimensionlessValues = mShowDimensionlessValues.isSelected();
				mScaleableDataSet.setShowingDimensionlessValues(mIsShowingDimensionlessValues);

				updateDisplay();	
			}
		});
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));		
		
		int x = 0, y = 0;
		MAJFCPanel controlsPanel = new MAJFCPanel(new GridBagLayout());
		controlsPanel.add(mDimensionlessAxes, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		controlsPanel.add(mMirrorAboutVertical, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		controlsPanel.add(mHideTitle, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		controlsPanel.add(mXAxisLogarithmic, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		controlsPanel.add(mYAxisLogarithmic, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		controlsPanel.add(mRelativeToMean, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		controlsPanel.add(mShowDimensionlessValues, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

		Vector<JComponent> additionalComponents = getAdditionalComponents();
		
		if (additionalComponents != null) {
			int numberOfAdditionalComponents = additionalComponents.size();

			for (int i = 0; i < numberOfAdditionalComponents; ++i) {
				JComponent additionalComponent = additionalComponents.elementAt(i);
				
				if (additionalComponent == null) {
					x = 0;
					y++;
				} else {
					controlsPanel.add(additionalComponent, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
				}
			}
		}
		
		x = 0;
		mTheGUI.add(this, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 5, 5, 5, 5, 0, 0));
		mTheGUI.add(controlsPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		
		setAxisRanges(mScaleableDataSet.getAxisRanges());
		setAxisLabels();

		return y;
	}

	/**
	 * Gets additional components for the chart. Override this to add extras
	 * @return A list of the components to add
	 */
	protected Vector<JComponent> getAdditionalComponents() {
		return new Vector<JComponent>(10);
	}

	/**
	 * Are the axes dimensionless?
	 * @return True if they are
	 */
	protected boolean areAxesDimensionless() {
		return mAxesDimensionless;
	}
	
	/**
	 * Should the mean value be inverted (multiplied by -1)?
	 * @return True if it should
	 */
	public boolean invertMean() {
		return mMirroredAboutVertical & mDataIsVectorInY;
	}
	
	/**
	 * Sets the labels for the graph axes
	 */
	private void setAxisLabels() {
		XYPlot thePlot = getChart().getXYPlot();
		thePlot.getDomainAxis().setLabel(areAxesDimensionless() ? DAStrings.getString(DAStrings.DIMENSIONLESS_X_AXIS_LABEL) : mDimensionedXAxisTitle);
		thePlot.getRangeAxis().setLabel(areAxesDimensionless() ? DAStrings.getString(DAStrings.DIMENSIONLESS_Y_AXIS_LABEL) : mDimensionedYAxisTitle);
	}
	
	/**
	 * Gets the GUI used to display this panel
	 * @return The GUI to display the chart
	 */
	public MAJFCPanel getGUI() {
		return mTheGUI;
	}

	/**
	 * Sets the ranges for the two axes
	 * @param domainAxisXMin
	 * @param domainAxisXMax
	 * @param rangeAxisYMin
	 * @param rangeAxisYMax
	 */
	public void setNonDimensionlessAxisRange(int domainAxisXMin, int domainAxisXMax, int rangeAxisYMin, int rangeAxisYMax) {
		mScaleableDataSet.setNonDimensionlessAxisRange(domainAxisXMin, domainAxisXMax, rangeAxisYMin, rangeAxisYMax);
	}

	/**
	 * Sets the ranges for the two axes. Sets the viewed range, not the range used for scaling
	 * @param axisLimits The axis mins and maxes, in order x-min, x-max, y-min, y-max
	 */
	private void setAxisRanges(double[] axisLimits) {
		setAxisRanges(axisLimits[0], axisLimits[1], axisLimits[2], axisLimits[3]);
	}
	
	/**
	 * Sets the ranges for the two axes
	 * @param domainAxisXMin
	 * @param domainAxisXMax
	 * @param rangeAxisYMin
	 * @param rangeAxisYMax
	 */
	protected void setAxisRanges(double domainAxisXMin, double domainAxisXMax, double rangeAxisYMin, double rangeAxisYMax) {
		XYPlot thePlot = getChart().getXYPlot();
		ValueAxis xAxis = thePlot.getDomainAxis();
		ValueAxis yAxis = thePlot.getRangeAxis();

		if (mXAxisIsLogarithmic && xAxis.getClass().equals(LogarithmicAxis.class) == false) {
			xAxis = new LogarithmicAxis(xAxis.getLabel());
		} else if (mXAxisIsLogarithmic == false && xAxis.getClass().equals(NumberAxis.class) == false) {
			xAxis = new NumberAxis(xAxis.getLabel());
		}
		
		xAxis.setRange(domainAxisXMin, domainAxisXMax);
		thePlot.setDomainAxis(xAxis);

		if (mYAxisIsLogarithmic && yAxis.getClass().equals(LogarithmicAxis.class) == false) {
			yAxis = new LogarithmicAxis(yAxis.getLabel());
		} else if (mYAxisIsLogarithmic == false && yAxis.getClass().equals(NumberAxis.class) == false) {
			yAxis = new NumberAxis(yAxis.getLabel());
		}
		
		yAxis.setRange(rangeAxisYMin, rangeAxisYMax);
		thePlot.setRangeAxis(yAxis);

		xAxis.configure();
		yAxis.configure();
		
		repaint();
	}
	
	/**
	 * Updates the display
	 */
	public void updateDisplay() {
		setAxisRanges(mScaleableDataSet.getAxisRanges());

		if (mTitleFixed == false && mScaleableDataSet instanceof ScaleableXYZDataSet) {
			double meanToInsert = invertMean() ? -1 * ((ScaleableXYZDataSet) mScaleableDataSet).getMean() : ((ScaleableXYZDataSet) mScaleableDataSet).getMean();
			// TODO Bit of a hack to get around setting before the title has been set to fixed
			try {
				getChart().setTitle(MAJFCTools.substituteIntoString(getRawChartTitle(), MAJFCTools.formatNumber(meanToInsert, 4, true, true)));
			} catch (ArrayIndexOutOfBoundsException e) {
				getChart().setTitle("Failed to set title ScaleableChartPanel:updateDisplay()");
			}
		}

		mTheGUI.validate();
	}
	
	/**
	 * Gets the raw chart title
	 * @return The raw chart title
	 */
	protected String getRawChartTitle() {
		return mRawChartTitle;
	}
	
	public void setFixedTitle(String title) {
		getChart().setTitle(title);
		mTitleFixed = true;
	}
	
	public static abstract class ScaleableXYDataSet implements XYDataset {
		private double mUnscaledXMin;
		private double mUnscaledXMax;
		private double mUnscaledYMin;
		private double mUnscaledYMax;
		private boolean mAxesDimensionless;
		protected boolean mIsRelativeToMean;
		protected boolean mIsShowingDimensionlessValues;
		protected boolean mMirroredAboutVertical;
		protected boolean mDataIsVectorInY;
		protected double mScalerForDimensionlessValues;

		public ScaleableXYDataSet(double[] axisRanges) {
			this(axisRanges[0], axisRanges[1], axisRanges[2], axisRanges[3]);
		}
		
		public ScaleableXYDataSet(double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			this(1.0, unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}
		
		public ScaleableXYDataSet(double scalerForDimensionlessValues, double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			mScalerForDimensionlessValues = scalerForDimensionlessValues;
			
			mUnscaledXMin = unscaledXMin;
			mUnscaledXMax = unscaledXMax;
			mUnscaledYMin = unscaledYMin;
			mUnscaledYMax = unscaledYMax;
		}
		
		/**
		 * Sets the ranges for the two axes
		 * @param domainAxisXMin
		 * @param domainAxisXMax
		 * @param rangeAxisYMin
		 * @param rangeAxisYMax
		 */
		protected void setNonDimensionlessAxisRange(int domainAxisXMin, int domainAxisXMax, int rangeAxisYMin, int rangeAxisYMax) {
			mUnscaledXMin = domainAxisXMin;
			mUnscaledXMax = domainAxisXMax;
			mUnscaledYMin = rangeAxisYMin;
			mUnscaledYMax = rangeAxisYMax;
		}

		/**
		 * Gets the dimensioned (non-dimensionless) axis ranges
		 * @return The non-dimensionless axis ranges in order x-min, x-max, y-min, y-max
		 */
		public double[] getDimensionedAxisRanges() {
			return new double[] {mUnscaledXMin, mUnscaledXMax, mUnscaledYMin, mUnscaledYMax};
		}
		
		/**
		 * Gets the dimensionless axis ranges
		 * @return The dimensionless axis ranges in order x-min, x-max, y-min, y-max
		 */
		public double[] getAxisRanges() {
			if (mAxesDimensionless) {
				return new double[] {0, 1, 0, 1};
			}
			
			if (mUnscaledXMin == mUnscaledXMax) {
				mUnscaledXMin = mUnscaledXMin < 0 ? mUnscaledXMin * 1.1 : mUnscaledXMin * 0.9;
				mUnscaledXMax = mUnscaledXMax < 0 ? mUnscaledXMax * 0.9 : mUnscaledXMax * 1.1;
			}
			
			if (mUnscaledYMin == mUnscaledYMax) {
				mUnscaledYMin = mUnscaledYMin < 0 ? mUnscaledYMin * 1.1 : mUnscaledYMin * 0.9;
				mUnscaledYMax = mUnscaledYMax < 0 ? mUnscaledYMax * 0.9 : mUnscaledYMax * 1.1;
			}
			
			return new double[] {mUnscaledXMin, mUnscaledXMax, mUnscaledYMin, mUnscaledYMax};
		}
		
		/**
		 * Sets flag indicating whether the axes are dimensionless
		 * @param axesDimensionless
		 */
		public void setAxesDimensionless(boolean axesDimensionless) {
			mAxesDimensionless = axesDimensionless;
		}
		
		/**
		 * Sets flag indicating whether the values are relative to the mean value
		 * @param isRelativeToMean
		 */
		public void setRelativeToMean(boolean isRelativeToMean) {
			mIsRelativeToMean = isRelativeToMean;
		}
		
		/**
		 * Sets flag indicating whether the values are relative to the mean value
		 * @param isRelativeToMean
		 */
		public void setShowingDimensionlessValues(boolean isShowingDimensionlessValues) {
			mIsShowingDimensionlessValues = isShowingDimensionlessValues;
		}
		
		/**
		 * Sets flag indicating whether the graph is mirrored about the vertical
		 * @param mirroredAboutVertical
		 * @param dataIsVectorInY If true then the sign of the data is reversed when data are mirrored about vertical
		 */
		public void setMirroredAboutVertical(boolean mirroredAboutVertical, boolean dataIsVectorInY) {
			mMirroredAboutVertical = mirroredAboutVertical;
			mDataIsVectorInY = dataIsVectorInY;
		}

		public abstract double getTheXValue(int series, int item);
		public abstract double getTheYValue(int series, int item);

		@Override
		/**
		 * XYDataset implementation
		 */
		public abstract int getItemCount(int series);

		/**
		 * Scale a value according the max and min unscaled X values allowed
		 * @param unscaledValue The unscaled value
		 * @return The scaled value (or the unscaledValue if not set to scale axed)
		 */
		public double scaleXValue(double unscaledValue) {
			if (mAxesDimensionless) {
				double scaledValue = (unscaledValue - mUnscaledXMin)/(mUnscaledXMax - mUnscaledXMin);
				
				return mMirroredAboutVertical ? 1.0 - scaledValue : scaledValue;
			}

			return mMirroredAboutVertical ? mUnscaledXMax - unscaledValue : unscaledValue;
		}
		
		/**
		 * Scale a value according the max and min unscaled Y values allowed
		 * @param unscaledValue The unscaled value
		 * @return The scaled value (or the unscaledValue if not set to scale axed)
		 */
		public double scaleYValue(double unscaledValue) {
			if (mAxesDimensionless == false) {
				return unscaledValue;
			}
			
			return (unscaledValue - mUnscaledYMin)/(mUnscaledYMax - mUnscaledYMin);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public DomainOrder getDomainOrder() {
			return null;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public Number getX(int series, int item) {
			return (Double) getXValue(series, item);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getXValue(int series, int item) {
			double value = getTheXValue(series, item);

			return scaleXValue(value);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public Number getY(int series, int item) {
			return (Double) getYValue(series, item);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public double getYValue(int series, int item) {
			double value = getTheYValue(series, item);

			return scaleYValue(value);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return 1;
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return 0;
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public int indexOf(Comparable seriesKey) {
			return 0;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public void addChangeListener(DatasetChangeListener listener) {
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public DatasetGroup getGroup() {
			return null;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public void removeChangeListener(DatasetChangeListener listener) {
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public void setGroup(DatasetGroup group) {
		}

	}
	
	public static abstract class ScaleableXYZDataSet extends ScaleableXYDataSet implements XYZDataset {
		private double mUnmirroredMean;

		public ScaleableXYZDataSet(double mean, double scalerForDimensionlessValues, double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			super(scalerForDimensionlessValues, unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
			mUnmirroredMean = mean;
		}
	
		public abstract double getTheZValue(int series, int item);
		
		/**
		 * Gets the mean value of the data held in this dataset (unless overloaded, this is as set in the constructor)
		 * @return The mean value
		 */
		protected double getMean() {
			return mUnmirroredMean;
		}

		@Override
		/**
		 * XYZDataset implementation
		 */
		public Number getZ(int series, int item) {
			return (Double) getZValue(series, item);
		}
		
		//@Override
		/**
		 * XYZDataset implementation
		 */
		public double getZValue(int series, int item) {
			double zValue = (Double) getTheZValue(series, item);
			
			if (mIsRelativeToMean) {
				zValue /= getMean();
			}
			
			if (mIsShowingDimensionlessValues) {
				zValue /= mScalerForDimensionlessValues;
			}
			
			// If it's relative to the mean *and* mirrored then we don't need to invert direction
			// as getMean() above will have returned the unmirrored mean and so both the zValue and
			// the getMean() value will have the wrong sign, thus cancelling.
			if (mMirroredAboutVertical & mDataIsVectorInY & (mIsRelativeToMean == false)) {
				zValue *= -1;
			}
			
			return zValue;
		}
	}
}
