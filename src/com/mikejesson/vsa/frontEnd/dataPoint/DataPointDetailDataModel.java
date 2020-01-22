/**
 * 
 */
package com.mikejesson.vsa.frontEnd.dataPoint;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.widgits.DAStrings;

/**
 * @author Mike
 *
 */
/**
 * Inner class providing a data model for the table
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointDetailDataModel extends AbstractTableModel {
	private final String[][] mData;
//	private final Color[] mColours;
	private final String[] mColTitles;
	private int[] mNumberOfDataInSeries;
	private int mNumberOfRows;
	private int mNumberOfColumns = 0;
	private Vector<Integer> mSynchOffsets;
	
	/**
	 * 
	 * @param title
	 * @param synchIndices Must be same length as fieldData
	 * @param fieldData
	 */
	public DataPointDetailDataModel(String title, Vector<Integer> synchIndices, Vector<Vector<Double>> fieldData) {
		mNumberOfColumns = fieldData.size();
		mColTitles = new String[mNumberOfColumns];
		mNumberOfDataInSeries = new int[mNumberOfColumns];
		mNumberOfDataInSeries[0] = fieldData.elementAt(0).size();
		mNumberOfRows = mNumberOfDataInSeries[0];
		
		mColTitles[0] = title;
		
		if (mNumberOfColumns == 1) {
			mSynchOffsets = new Vector<Integer>(0);
			mSynchOffsets.add(0);
			mData = new String[mNumberOfColumns][mNumberOfDataInSeries[0]];
//			mColours = new Color[mNumberOfDataInSeries[0]];
			
			// Load the time-series. 
			for (int rowIndex = 0; rowIndex < mNumberOfDataInSeries[0]; ++rowIndex) {
				double value = fieldData.elementAt(0).elementAt(rowIndex);
				mData[0][rowIndex] = MAJFCTools.formatNumber(value, 4, true);
			}
		} else {
			// Work out how long the time-series are, taking synch offsets into account
			// First series is the mean-value series so we can ignore it here
			int minSynchIndex = Integer.MAX_VALUE;
			int maxSynchIndex = Integer.MIN_VALUE;
			for (int i = 1; i < mNumberOfColumns; i++) {
				minSynchIndex = Math.min(minSynchIndex, synchIndices.elementAt(i));
				maxSynchIndex = Math.max(synchIndices.elementAt(i), maxSynchIndex);
			}

			// Work out the offsets for each time-series. Start each at its offset from the maxSynchronisingIndex,
			// except for the mean-value series which is started at the offset between the max and minSynchronisingIndex
			// Also calculate the length of the time-series once offsets have been taken into account.
			mSynchOffsets = new Vector<Integer>(mNumberOfColumns);
			mSynchOffsets.add(maxSynchIndex - minSynchIndex);
			for (int i = 1; i < mNumberOfColumns; i++) {
				mColTitles[i] = DAStrings.getString(DAStrings.MULTI_RUN_RUN_LABEL) + ' ' + i;
				
				mSynchOffsets.add(maxSynchIndex - synchIndices.elementAt(i));
				
				mNumberOfDataInSeries[i] = fieldData.elementAt(i).size();
				mNumberOfRows = Math.max(mNumberOfRows, mSynchOffsets.lastElement() + mNumberOfDataInSeries[i]);
			}

			mData = new String[mNumberOfColumns][mNumberOfRows];
//			mColours = new Color[mNumberOfDataInSeries[0]];
			
			// Load the time-series. 
			for (int colIndex = 0; colIndex < mNumberOfColumns; ++colIndex) {
				int numberOfData = mNumberOfDataInSeries[colIndex];
				int synchOffset = mSynchOffsets.elementAt(colIndex);
				
				for (int dataIndex = 0; dataIndex < numberOfData; ++dataIndex) {
					double value = fieldData.elementAt(colIndex).elementAt(dataIndex);
					mData[colIndex][synchOffset + dataIndex] = MAJFCTools.formatNumber(value, 4, true);
				}
			}
		}
	}

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
		return mNumberOfColumns;
	}

	/**
	 * Gets the number of rows in the model/grid
	 * @return the number of rows in the model/grid
	 */
	@Override
	public int getRowCount() {
		return mNumberOfRows;
	}

	/**
	 * Gets the value to display in the grid at the given cell
	 * @return the value to display in the specified cell
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return mData[columnIndex][rowIndex];
	}

//	public Color getColour(int series) {
//		return mColours[series];
//	}

	public int getNumberOfDataInSeries(int series) {
		return mNumberOfDataInSeries[series];
	}

	public int getSynchOffset(int series) {
		return mSynchOffsets.elementAt(series);
	}
}


