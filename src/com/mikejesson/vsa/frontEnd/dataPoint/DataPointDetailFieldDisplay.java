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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JTable;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ExportableChartPanel;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointDetailFieldDisplay extends MAJFCStackedPanelWithFrame implements XYDataset {
	private JTable mDataTable;
	private final DataPointDetailDataModel mDataModel;
	private ExportableChartPanel mChartPanel;
	private JFreeChart mTheChart;
	private final AbstractDataSetUniqueId mDataSetId;
	private final double mSamplingRate;
	
	/**
	 * @param title The title (column header) for this display
	 * @param fieldData The list of data for this field
	 * 
	 */
	public DataPointDetailFieldDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Vector<Double>> fieldData) {
		this(dataSetId, title, null, fieldData);
	}

	/**
	 * @param title The title (column header) for this display
	 * @param fieldData The list of data for this field
	 * 
	 */
	public DataPointDetailFieldDisplay(AbstractDataSetUniqueId dataSetId, String title, Vector<Integer> synchIndices, Vector<Vector<Double>> fieldData) {
		super(new GridBagLayout());

		mDataModel = new DataPointDetailDataModel(title, synchIndices, fieldData);
		mDataSetId = dataSetId;
		
		double samplingRate = 1d;
		try {
			samplingRate = BackEndAPI.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
		} catch (BackEndAPIException e) {
		}

		mSamplingRate = samplingRate;
		
		buildGUI();
	}
	
	/**
	 * Builds the GUI for this display
	 */
	private void buildGUI() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());
		
		mDataTable = new JTable(mDataModel);
//		mDataTable.setDefaultRenderer(Object.class, new NumberCellRenderer() {
//			
//			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//				
//				Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//				cell.setBackground(mDataModel.getColour(row));
//
//				return cell;
//			}
//		});
		mDataTable.setFillsViewportHeight(true);
	
		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.TIME_LABEL));//DAStrings.getString(DAStrings.));
		//xAxis.setTickUnit(new NumberTickUnit(0, NumberFormat.getIntegerInstance()));

		NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.VELOCITY_LABEL));
		yAxis.setTickUnit(new NumberTickUnit(0.1, NumberFormat.getNumberInstance()));
		//yAxis.setRangeType(RangeType.POSITIVE);

		DefaultXYItemRenderer theRenderer = new DefaultXYItemRenderer();
		theRenderer.setSeriesShapesVisible(0, false);
		
		XYPlot thePlot = new XYPlot(this, xAxis, yAxis, theRenderer);
		mTheChart = new JFreeChart(mDataModel.getColumnName(0), JFreeChart.DEFAULT_TITLE_FONT, thePlot, false);
		
		mChartPanel = new ExportableChartPanel(this, mTheChart);

/*		mDataTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent theEvent) {
				if (theEvent.getClickCount() != 2) {
					return;
				}
				int row = mDataTable.convertRowIndexToModel(mDataTable.getSelectedRow());
				int yCoord = ((Integer)(mDataModel.getValueAt(row, 0))).intValue();
				int zCoord = ((Integer)(mDataModel.getValueAt(row, 1))).intValue();
				
				try {
					Logger.log(MAJFCTools.stringValueOf(yCoord) + '-' + MAJFCTools.stringValueOf(zCoord));
					DAFrame.getBackEndAPI().loadDataPointDetails(yCoord, zCoord);
				} catch (Exception theException) {
					
				}
			}
		});
		
		List <RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
		TableRowSorter<MyDataModel> sorter = new TableRowSorter<MyDataModel>(mDataModel);
		sorter.setSortKeys(sortKeys);
		mDataTable.setRowSorter(sorter);
*/
		add(mDataTable.getTableHeader(), MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0, 0, 0, 0, 0, 0));
		add(mDataTable, MAJFCTools.createGridBagConstraint(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
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
	public int getItemCount(int series) {
		return mDataModel.getNumberOfDataInSeries(series);
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public Number getX(int series, int item) {
		return getXValue(series, item);
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public double getXValue(int series, int item) {
		return ((double) synchOffsetItem(series, item))/mSamplingRate;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public Number getY(int series, int item) {
		return getYValue(series, item);
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public double getYValue(int series, int item) {
		try {
			return MAJFCTools.parseDouble((String) mDataModel.getValueAt(synchOffsetItem(series, item), series));
		} catch (Exception theException) {
			return -999999999;
		}
	}

	private int synchOffsetItem(int series, int item) {
		return item + mDataModel.getSynchOffset(series);
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public int getSeriesCount() {
		return mDataModel.getColumnCount();
	}

	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public Comparable getSeriesKey(int arg0) {
		return "";
	}

	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public int indexOf(Comparable arg0) {
		return 0;
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public void addChangeListener(DatasetChangeListener arg0) {
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
	public void removeChangeListener(DatasetChangeListener arg0) {
	}

	@Override
	/**
	 * XYDataset implementation
	 */
	public void setGroup(DatasetGroup arg0) {
	}

	/**
	 * Gets the chart panel to display the data in this field
	 * @return the mChartPanel
	 */
	public ChartPanel getChartPanel() {
		return mChartPanel;
	}

}
