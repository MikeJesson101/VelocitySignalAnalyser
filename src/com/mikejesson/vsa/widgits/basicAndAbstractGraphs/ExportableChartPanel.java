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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTextAreaDialog;
import com.mikejesson.majfc.helpers.MAJFCIndex;
import com.mikejesson.majfc.helpers.MAJFCLinkedGUIComponentsAction;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.DataAnalyser;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.AbstractLateralVectorGraph;
import com.mikejesson.vsa.widgits.DAStrings;


/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class ExportableChartPanel extends ChartPanel {
	private JMenuItem mExportAsTableButton;
	private JMenuItem mExportForMatlabButton;
	private JMenuItem mExportAsMatlabScriptButton;

	private final InterpolationType mInterpolationType;
	private final MAJFCStackedPanelWithFrame mHolderFrame;

	private final static ExportType TABLE = new ExportType(0);
	private final static ExportType MATLAB = new ExportType(1);
	private final static ExportType MATLAB_SCRIPT = new ExportType(2);
	
	public final static InterpolationType NONE = new InterpolationType(0);
	public final static InterpolationType INTERPOLATION_INCLUDING_EDGES = new InterpolationType(1);
	public final static InterpolationType INTERPOLATION_EXCLUDING_EDGES = new InterpolationType(2);
	
	/**
	 * Constructor
	 * @param chart The chart to show in this panel
	 */
	public ExportableChartPanel(MAJFCStackedPanelWithFrame holderFrame, JFreeChart chart) {
		this(holderFrame, chart, INTERPOLATION_EXCLUDING_EDGES);
	}
	
	/**
	 * Constructor
	 * @param chart The chart to show in this panel
	 * @param interpolationType If true then missing data points are filled by interpolation using the nearest points, if false then NaN is inserted
	 */
	public ExportableChartPanel(MAJFCStackedPanelWithFrame holderFrame, JFreeChart chart, InterpolationType interpolationType) {
		super(chart);
		
		mInterpolationType = interpolationType;
		mHolderFrame = holderFrame;
		
		getChartRenderingInfo().setEntityCollection(null);
	}

	/**
	 * Gets the frame which holds this chart
	 * @return
	 */
	public MAJFCStackedPanelWithFrame getHolderFrame() {
		return mHolderFrame;
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
		mExportAsTableButton = new JMenuItem();
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL), null, DAStrings.getString(DAStrings.EXPORT_GRAPH_BUTTON_DESC), mExportAsTableButton, this);

		mExportForMatlabButton = new JMenuItem();
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL), null, DAStrings.getString(DAStrings.EXPORT_GRAPH_FOR_MATLAB_BUTTON_DESC), mExportForMatlabButton, this);

		mExportAsMatlabScriptButton = new JMenuItem();
		new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL), null, DAStrings.getString(DAStrings.EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_DESC), mExportAsMatlabScriptButton, this);

		JPopupMenu theMenu = super.createPopupMenu(true, copy, save, print, false);
		theMenu.addSeparator();
		theMenu.add(mExportAsTableButton);
		theMenu.add(mExportForMatlabButton);
		theMenu.add(mExportAsMatlabScriptButton);
		
		mExportForMatlabButton.setEnabled(DataAnalyser.matlabConnectionAvailable());

		return theMenu;
	}
	
	@Override
	/**
	 * ActionListener implementation
	 */
	public void actionPerformed(ActionEvent theEvent) {
		String command = theEvent.getActionCommand();
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		if (command.equals(DAStrings.getString(DAStrings.EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL))) {
			exportData(TABLE);
		} else if (command.equals(DAStrings.getString(DAStrings.EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL))) {
			exportData(MATLAB);
		} else if (command.equals(DAStrings.getString(DAStrings.EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL))) {
			exportData(MATLAB_SCRIPT);
		} else {
			super.actionPerformed(theEvent);
		}
		
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Export the data
	 * @param type The type of export to perform
	 */
	private void exportData(ExportType type) {
		XYPlot thePlot = getChart().getXYPlot();
		int numberOfDataSets = thePlot.getDatasetCount();
		
		for (int dataSetIndex = 0; dataSetIndex < numberOfDataSets; ++dataSetIndex) {
			XYDataset dataSet = thePlot.getDataset(dataSetIndex);
			
			try {
				AbstractLateralVectorGraph.MyXYDataSet alvgDataSet = (AbstractLateralVectorGraph.MyXYDataSet) dataSet;
				
				if (type.equals(TABLE)) {
					exportDataAsTable(alvgDataSet, dataSetIndex);
				} else if (type.equals(MATLAB)) {
					exportDataForMatlab(alvgDataSet, dataSetIndex, false);
				} else if (type.equals(MATLAB_SCRIPT)) {
					exportDataForMatlab(alvgDataSet, dataSetIndex, true);
				}
				
				return;
			} catch (ClassCastException theException) {
			}
			
			try {
				XYZDataset xyzDataSet = (XYZDataset) dataSet;

				if (type.equals(TABLE)) {
					exportDataAsTable(xyzDataSet, dataSetIndex);
				} else if (type.equals(MATLAB)) {
					exportDataForMatlab(xyzDataSet, dataSetIndex, false);
				} else if (type.equals(MATLAB_SCRIPT)) {
					exportDataForMatlab(xyzDataSet, dataSetIndex, true);
				}

				return;
			} catch (ClassCastException theException) {
			}
			
			try {
				XYDataset xyDataSet = (XYDataset) dataSet;

				if (type.equals(TABLE)) {
					exportDataAsTable(xyDataSet, dataSetIndex);
				} else if (type.equals(MATLAB)) {
					exportDataForMatlab(xyDataSet, dataSetIndex, false);
				} else if (type.equals(MATLAB_SCRIPT)) {
					exportDataForMatlab(xyDataSet, dataSetIndex, true);
				}

				return;
			} catch (ClassCastException theException) {
			}
			
			if (type.equals(TABLE)) {
				exportDataAsTable(dataSet, dataSetIndex);
			} else if (type.equals(MATLAB)) {
				exportDataForMatlab(dataSet, dataSetIndex, false);
			} else if (type.equals(MATLAB_SCRIPT)) {
				exportDataForMatlab(dataSet, dataSetIndex, true);
			}
		}
	}
	
	protected void exportDataAsTable(XYDataset dataSet, int dataSetIndex) {
		int numberOfSeries = dataSet.getSeriesCount();
		Vector<Vector<Vector<Double>>> seriesList = new Vector<Vector<Vector<Double>>>(numberOfSeries);
		Vector<String> columnTitles = new Vector<String>(10);
		
		for (int seriesIndex = 0; seriesIndex < numberOfSeries; ++seriesIndex) {
			int numberOfItems = dataSet.getItemCount(seriesIndex);
			Vector<Vector<Double>> dataPointsList = new Vector<Vector<Double>>(numberOfItems);
			String seriesKeyString = dataSet.getSeriesKey(seriesIndex).toString();
			String yColumnTitle = DAStrings.getString(DAStrings.Y_COLUMN_TITLE);
			String zColumnTitle = DAStrings.getString(DAStrings.Z_COLUMN_TITLE);
			
			if (seriesKeyString.length() > 0) {
				try {
					int indexOfEqualSign = seriesKeyString.indexOf('=');
					
					if (indexOfEqualSign >= 0) {
						seriesKeyString = seriesKeyString.substring(indexOfEqualSign + 1);
					}
					
					Double seriesKey = Double.valueOf(seriesKeyString);
					Vector<Double> seriesKeyDummyPoint = new Vector<Double>(2);
					seriesKeyDummyPoint.add(seriesKey);
					seriesKeyDummyPoint.add(seriesKey);
					
					dataPointsList.add(seriesKeyDummyPoint);
				} catch (Exception e) {
					yColumnTitle += " (" + dataSet.getSeriesKey(seriesIndex) + ')';
					zColumnTitle += " (" + dataSet.getSeriesKey(seriesIndex) + ')';
				}
			}
			
			columnTitles.add(yColumnTitle);
			columnTitles.add(zColumnTitle);
			
			for (int itemIndex = 0; itemIndex < numberOfItems; ++itemIndex) {
				Vector<Double> dataPoint = new Vector<Double>(3);
				
				dataPoint.add(dataSet.getXValue(seriesIndex, itemIndex));
				dataPoint.add(dataSet.getYValue(seriesIndex, itemIndex));

				dataPointsList.add(dataPoint);
			}
			
			seriesList.add(dataPointsList);
		}

		displayAsTable(getChart().getTitle().getText() + ' ' + (dataSetIndex + 1), seriesList, columnTitles);
	}
	
	private void exportDataForMatlab(XYDataset xyDataSet, int dataSetIndex, boolean asScript) {
		int numberOfSeries = xyDataSet.getSeriesCount();
		StringBuffer outputText = new StringBuffer();
		
		for (int seriesIndex = 0; seriesIndex < numberOfSeries; ++seriesIndex) {
			int numberOfItems = xyDataSet.getItemCount(seriesIndex);
			
			StringBuffer xValuesString = new StringBuffer(), yValuesString = new StringBuffer();

			for (int itemIndex = 0; itemIndex < numberOfItems; ++itemIndex) {
				Double xValue = xyDataSet.getXValue(0, itemIndex);
				Double yValue = xyDataSet.getYValue(0, itemIndex);

				xValuesString.append(xValue);
				xValuesString.append('\n');

				yValuesString.append(yValue);
				yValuesString.append('\n');
			}
			
			ValueAxis xAxis = getChart().getXYPlot().getDomainAxis();
			Range xAxisRange = xAxis.getRange();
			
			ValueAxis yAxis = getChart().getXYPlot().getRangeAxis();
			Range yAxisRange = yAxis.getRange();

			// Replace single quotes in labels with two single quotes (single quote is Matlab control character)
			outputText.append(DAStrings.getString(DAStrings.MATLAB_XY_OUTPUT_TEXT, xValuesString.toString(), 
																				yValuesString.toString(),
																				getChart().getTitle().getText().replace("\'", "\'\'"),
																				xAxis.getLabel().replace("\'", "\'\'"),
																				yAxis.getLabel().replace("\'", "\'\'"),
																				xAxisRange.getLowerBound(),
																				xAxisRange.getUpperBound(),
																				yAxisRange.getLowerBound(),
																				yAxisRange.getUpperBound(),
																				xAxis instanceof LogarithmicAxis ? "'log'" : "'lin'",
																				yAxis instanceof LogarithmicAxis ? "'log'" : "'lin'"));
		}

		if (asScript) {
			new ExportedDataForMatlabGUI(mHolderFrame, outputText.toString() + MAJFCTools.SYSTEM_NEW_LINE_STRING);
		} else {
			try {
				DataAnalyser.matlabConnection().sendToMatlab(outputText.toString(), mHolderFrame.getFrame());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void exportDataAsTable(XYZDataset xyzDataSet, int dataSetIndex) {
		// Only one series allowed for an XYZDataset
		int numberOfSeries = 1;//dataSet.getSeriesCount();
		Vector<Vector<Vector<Double>>> seriesList = new Vector<Vector<Vector<Double>>>(numberOfSeries);
		
		for (int seriesIndex = 0; seriesIndex < numberOfSeries; ++seriesIndex) {
			int numberOfItems = xyzDataSet.getItemCount(seriesIndex);
			Vector<Double> yCoordsList = new Vector<Double>(), zCoordsList = new Vector<Double>();
			Hashtable<Double, Double> yCoordsLookup = new Hashtable<Double, Double>(), zCoordsLookup = new Hashtable<Double, Double>();
			Hashtable<Double, Hashtable<Double, Double>> dataMatrix = new Hashtable<Double, Hashtable<Double, Double>>();
			
			// Add a dummy at the start so that when we have a column for the z-coordinate in the output
			yCoordsList.add(Double.NEGATIVE_INFINITY);

			// Put all the "z-values" into a matrix indexed by the y and z-coordinates
			for (int itemIndex = 0; itemIndex < numberOfItems; ++itemIndex) {
				Double yCoord = xyzDataSet.getXValue(seriesIndex, itemIndex);
				Double zCoord = xyzDataSet.getYValue(seriesIndex, itemIndex);

				if (yCoordsLookup.get(yCoord) == null) {
					yCoordsLookup.put(yCoord, yCoord);
					yCoordsList.add(yCoord);
				}

				if (zCoordsLookup.get(zCoord) == null) {
					zCoordsLookup.put(zCoord, zCoord);
					zCoordsList.add(zCoord);
				}
				
				Hashtable<Double, Double> dataRow = dataMatrix.get(zCoord);

				if (dataRow == null) {
					dataRow = new Hashtable<Double, Double>(20);
					dataMatrix.put(zCoord, dataRow);
				}
				
				Double dataEntry = dataRow.get(yCoord);
				
				if (dataEntry == null) {
					dataRow.put(yCoord, xyzDataSet.getZValue(seriesIndex, itemIndex));
				}
				
			}
			
			Collections.sort(yCoordsList);
			Collections.sort(zCoordsList);
			int numberOfYCoords = yCoordsList.size();
			int numberOfZCoords = zCoordsList.size();
			Vector<Vector<Double>> dataRowsList = new Vector<Vector<Double>>();
			dataRowsList.add(yCoordsList);
			
			for (int i = 0; i < numberOfZCoords; ++i) {
				Double zCoord = zCoordsList.elementAt(i);
				Vector<Double> dataRow = new Vector<Double>(numberOfYCoords);
				dataRow.add(zCoord);
				
				// Start at 1 to ignore the "-1" dummy entry in the y-coordinates list
				for (int j = 1; j < numberOfYCoords; ++j) {
					Double yCoord = yCoordsList.elementAt(j);
					dataRow.add(dataMatrix.get(zCoord).get(yCoord));
				}
				
				dataRowsList.add(dataRow);
			}
			
			seriesList.add(dataRowsList);
		}

		displayAsTable(getChart().getTitle().getText() + ' ' + (dataSetIndex + 1), seriesList);
	}

	private void exportDataForMatlab(XYZDataset xyzDataSet, int dataSetIndex, boolean asScript) {
		// Only one series allowed for an XYZDataset
		int numberOfSeries = 1;//dataSet.getSeriesCount();
		StringBuffer outputText = new StringBuffer();
		
		for (int seriesIndex = 0; seriesIndex < numberOfSeries; ++seriesIndex) {
			int numberOfItems = xyzDataSet.getItemCount(seriesIndex);
			Hashtable<Double, Hashtable<Double, Double>> yThenZIndexedDataLookup = new Hashtable<Double, Hashtable<Double,Double>>(100);
			Hashtable<Double, Double> zCoordLookup = new Hashtable<Double, Double>(20);
			Vector<Double> yCoordsList = new Vector<Double>(numberOfItems);
			Vector<Double> zCoordsList = new Vector<Double>(numberOfItems);
			
			for (int itemIndex = 0; itemIndex < numberOfItems; ++itemIndex) {
				Double yCoord = xyzDataSet.getXValue(0, itemIndex);
				Double zCoord = xyzDataSet.getYValue(0, itemIndex);
				Hashtable<Double, Double> zIndexedDataLookup = yThenZIndexedDataLookup.get(yCoord);
				
				if (zIndexedDataLookup == null) {
					zIndexedDataLookup = new Hashtable<Double, Double>(20);
					yThenZIndexedDataLookup.put(yCoord, zIndexedDataLookup);
					yCoordsList.add(yCoord);
				}
				
				Double datum = xyzDataSet.getZValue(0, itemIndex);
				zIndexedDataLookup.put(zCoord, datum);
				
				if (zCoordLookup.get(zCoord) == null) {
					zCoordsList.add(zCoord);
					zCoordLookup.put(zCoord, zCoord);
				}
			}
			
			Collections.sort(yCoordsList);
			Collections.sort(zCoordsList);

			StringBuffer yCoordsString = new StringBuffer(), zCoordsString = new StringBuffer(), dataString = new StringBuffer();
			int numberOfYCoords = yCoordsList.size(), numberOfZCoords = zCoordsList.size();

			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Double yCoord = yCoordsList.elementAt(yCoordIndex);
				Hashtable<Double, Double> zIndexedDataLookup = yThenZIndexedDataLookup.get(yCoord);
				yCoordsString.append(yCoord);
				yCoordsString.append('\n');

				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Double zCoord = zCoordsList.elementAt(zCoordIndex);
					
					if (yCoordIndex == 0) {
						zCoordsString.append(zCoord);
						zCoordsString.append('\n');
					}
					
					Double datum = zIndexedDataLookup.get(zCoord);
					
					if (datum != null) {
						dataString.append(datum);
					} else {
						if (mInterpolationType.equals(NONE)) {
							// If no data at this point, add Matlab "NaN" value entry to ensure matrix sizes match in Matlab
							dataString.append("NaN");
						} else {
							// Find previous and next valid data points along y-axis
							Double previousDatum = null, nextDatum = null;
							int previousYCoordIndex = yCoordIndex - 1, nextYCoordIndex = yCoordIndex + 1;
							
							while (previousDatum == null && previousYCoordIndex >= 0) {
								Hashtable<Double, Double> previousZIndexedDataLookup = yThenZIndexedDataLookup.get(yCoordsList.elementAt(previousYCoordIndex));
								previousDatum = previousZIndexedDataLookup.get(zCoord);
								--previousYCoordIndex;
							}
							
							while (nextDatum == null && nextYCoordIndex < numberOfYCoords) {
								Hashtable<Double, Double> nextZIndexedDataLookup = yThenZIndexedDataLookup.get(yCoordsList.elementAt(nextYCoordIndex));
								nextDatum = nextZIndexedDataLookup.get(zCoord);
								++nextYCoordIndex;
							}
							
							if (mInterpolationType.equals(INTERPOLATION_EXCLUDING_EDGES) && (previousDatum == null || nextDatum == null)) {
								dataString.append("NaN");
							} else if (previousDatum == null) {
								dataString.append(nextDatum);
							} else if (nextDatum == null) {
								dataString.append(previousDatum);
							} else {
								dataString.append((previousDatum + nextDatum)/2d);
							}
						}
					}

					dataString.append(' ');
				}
				
				dataString.append('\n');
			}
			
			ValueAxis xAxis = getChart().getXYPlot().getDomainAxis();
			Range xAxisRange = xAxis.getRange();
			
			ValueAxis yAxis = getChart().getXYPlot().getRangeAxis();
			Range yAxisRange = yAxis.getRange();

			// Replace single quotes in labels with two single quotes (single quote is Matlab control character)
			outputText.append(DAStrings.getString(DAStrings.MATLAB_XYZ_OUTPUT_TEXT, yCoordsString.toString(), 
																				zCoordsString.toString(),
																				dataString.toString(),
																				getChart().getTitle().getText().replace("\'", "\'\'"),
																				xAxis.getLabel().replace("\'", "\'\'"),
																				yAxis.getLabel().replace("\'", "\'\'"),
																				xAxisRange.getLowerBound(),
																				xAxisRange.getUpperBound(),
																				yAxisRange.getLowerBound(),
																				yAxisRange.getUpperBound(),
																				xAxis instanceof LogarithmicAxis ? "'log'" : "'lin'",
																				yAxis instanceof LogarithmicAxis ? "'log'" : "'lin'"));
		}

		if (asScript) {
			new ExportedDataForMatlabGUI(mHolderFrame, outputText.toString() + MAJFCTools.SYSTEM_NEW_LINE_STRING);
		} else {
			try {
				DataAnalyser.matlabConnection().sendToMatlab(outputText.toString(), mHolderFrame.getFrame());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	protected void exportDataAsTable(AbstractLateralVectorGraph.MyXYDataSet dataSet, int dataSetIndex) {
		// Always two series for an AbstractLateralVectorGraph dataset
		int numberOfSeries = 2;//dataSet.getSeriesCount();
		Vector<Vector<Vector<Double>>> seriesList = new Vector<Vector<Vector<Double>>>(numberOfSeries);
		
		for (int seriesIndex = 0; seriesIndex < numberOfSeries; ++seriesIndex) {
			int numberOfItems = dataSet.getItemCount(seriesIndex);
			Vector<Double> yCoordsList = new Vector<Double>(), zCoordsList = new Vector<Double>();
			Hashtable<Double, Double> yCoordsLookup = new Hashtable<Double, Double>(), zCoordsLookup = new Hashtable<Double, Double>();
			Hashtable<Double, Hashtable<Double, Double>> dataMatrix = new Hashtable<Double, Hashtable<Double, Double>>();
			
			// Add a dummy at the start so that when we have a column for the z-coordinate in the output
			yCoordsList.add(-1D);

			// Put all the "z-values" into a matrix indexed by the y and z-coordinates
			for (int itemIndex = 0; itemIndex < numberOfItems; ++itemIndex) {
				Double yCoord = dataSet.getXValue(seriesIndex, itemIndex);
				Double zCoord = dataSet.getYValue(seriesIndex, itemIndex);

				if (yCoordsLookup.get(yCoord) == null) {
					yCoordsLookup.put(yCoord, yCoord);
					yCoordsList.add(yCoord);
				}

				if (zCoordsLookup.get(zCoord) == null) {
					zCoordsLookup.put(zCoord, zCoord);
					zCoordsList.add(zCoord);
				}
				
				Hashtable<Double, Double> dataRow = dataMatrix.get(zCoord);

				if (dataRow == null) {
					dataRow = new Hashtable<Double, Double>(20);
					dataMatrix.put(zCoord, dataRow);
				}
				
				Double dataEntry = dataRow.get(yCoord);
				
				if (dataEntry == null) {
					dataRow.put(yCoord, seriesIndex == 0 ? dataSet.getYComponent(seriesIndex, itemIndex) : dataSet.getZComponent(seriesIndex, itemIndex));
				}
			}
			
			Collections.sort(yCoordsList);
			Collections.sort(zCoordsList);
			int numberOfYCoords = yCoordsList.size();
			int numberOfZCoords = zCoordsList.size();
			Vector<Vector<Double>> dataRowsList = new Vector<Vector<Double>>();
			dataRowsList.add(yCoordsList);
			
			for (int i = 0; i < numberOfZCoords; ++i) {
				Double zCoord = zCoordsList.elementAt(i);
				Vector<Double> dataRow = new Vector<Double>(numberOfYCoords);
				dataRow.add(zCoord);
				
				// Start at 1 to ignore the "-1" dummy entry in the y-coordinates list
				for (int j = 1; j < numberOfYCoords; ++j) {
					Double yCoord = yCoordsList.elementAt(j);
					dataRow.add(dataMatrix.get(zCoord).get(yCoord));
				}
				
				dataRowsList.add(dataRow);
			}
			
			seriesList.add(dataRowsList);
		}
		
		displayAsTable(getChart().getTitle().getText() + ' ' + (dataSetIndex + 1), seriesList);
	}
	
	protected void displayAsTable(String title, Vector<Vector<Vector<Double>>> seriesList) {
		new ExportedDataAsTableGUI(mHolderFrame, title, seriesList);
	}
	
	protected void displayAsTable(String title, Vector<Vector<Vector<Double>>> seriesList, Vector<String> columnTitles) {
		new ExportedDataAsTableGUI(mHolderFrame, title, seriesList, columnTitles);
	}
	
	private void exportDataForMatlab(AbstractLateralVectorGraph.MyXYDataSet alvgDataSet, int dataSetIndex, boolean asScript) {
		// TODO Auto-generated method stub
		
	}

	private class ExportedDataAsTableGUI extends AbstractTableModel {
		private Vector<Vector<Double>> mData;
		private int mNumberOfRows;
		private int mNumberOfColumns;
		private Vector<String> mColumnTitles;
		private MAJFCStackedPanelWithFrame mStackedPanel;
		private JScrollPane mTableScroll;
		private JTable mTable;
		
		private ExportedDataAsTableGUI(MAJFCStackedPanelWithFrame parent, String title, Vector<Vector<Vector<Double>>> data) {
			this(parent, title, data, null);
		}
		
		private ExportedDataAsTableGUI(MAJFCStackedPanelWithFrame parent, String title, Vector<Vector<Vector<Double>>> data, Vector<String> columnTitles) {
			prepareData(data);
			
			mColumnTitles = columnTitles;
			
			mTable = new JTable(this);
			mTable.setRowSelectionAllowed(true);
			mTable.setColumnSelectionAllowed(true);
			mTableScroll = new JScrollPane(mTable);
			mStackedPanel = new MAJFCStackedPanelWithFrame(parent, new GridBagLayout()) {
				/**
				 * Gets the preferred size
				 * @return The preferred size
				 */
				@Override
				public Dimension getPreferredSize() {
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					int width = (int) (screenSize.width * 0.5);
					int height = (int) (screenSize.height * 0.8);

					return new Dimension(width, height);
				}
			};
			mStackedPanel.add(mTableScroll, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
			mStackedPanel.showInFrame(title, null, DAFrame.getFrame());
		}

		private void prepareData(Vector<Vector<Vector<Double>>> data) {
			int numberOfSeries = data.size();
			mNumberOfRows = 0;
			mNumberOfColumns = 0;
			
			for (int i = 0; i < numberOfSeries; ++i) {
				Vector<Vector<Double>> dataPoints = data.elementAt(i);
				int numberOfDataPoints = dataPoints.size();
				mNumberOfRows = Math.max(mNumberOfRows, numberOfDataPoints);
				int numberOfColumnsForThisSeries = dataPoints.firstElement().size(); 
				mNumberOfColumns += numberOfColumnsForThisSeries;
			}

			mData = new Vector<Vector<Double>>(mNumberOfRows);
			for (int i = 0; i < mNumberOfRows; ++i) {
				mData.add(new Vector<Double>(mNumberOfColumns));
			}

			for (int i = 0; i < numberOfSeries; ++i) {
				Vector<Vector<Double>> dataPoints = data.elementAt(i);
				int numberOfDataPoints = dataPoints.size();
				int numberOfComponents = dataPoints.firstElement().size();
				
				for (int j = 0; j < mNumberOfRows; ++j) {
					if (j < numberOfDataPoints) {
						Vector<Double> dataPoint = dataPoints.elementAt(j);
						
						for (int k = 0; k < numberOfComponents; ++k) {
							mData.elementAt(j).add(dataPoint.elementAt(k));
						}
					} else {
						for (int k = 0; k < numberOfComponents; ++k) {
							mData.elementAt(j).add(Double.NaN);
						}
					}
				}
			}
		}
		
		@Override
		/**
		 * Gets the column title
		 * @return the column title
		 */
		public String getColumnName(int i) {
			if (mColumnTitles == null) {
				return super.getColumnName(i);
			}
			
			return mColumnTitles.elementAt(i);
		}
		
		@Override
		public int getColumnCount() {
			return mNumberOfColumns;
		}

		@Override
		public int getRowCount() {
			return mNumberOfRows;
		}

		@Override
		public Object getValueAt(int row, int column) {
			return mData.elementAt(row).elementAt(column);
		}
	}

	private class ExportedDataForMatlabGUI extends MAJFCTextAreaDialog {
		public ExportedDataForMatlabGUI(MAJFCStackedPanelWithFrame parent, String textAreaText) {
			super(parent, DAStrings.getString(DAStrings.SAVE), DAStrings.getString(DAStrings.CLOSE), textAreaText);
		}

		/**
		 * Writes the Matlab export text to a file
		 * @return 
		 */
		protected String getDefaultSaveDirectory() {
			return DAFrame.getFrame().getCurrentDataSetConfig().get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY);
		}
	}
	
	/**
	 * Helper class
	 * @author Mike
	 *
	 */
	private static class ExportType extends MAJFCIndex {
		public ExportType(int index) {
			super(index);
		}
	}
	
	/**
	 * Helper class
	 * @author Mike
	 *
	 */
	private static class InterpolationType extends MAJFCIndex {
		public InterpolationType(int index) {
			super(index);
		}
	}
}
