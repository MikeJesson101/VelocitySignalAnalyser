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

package com.mikejesson.vsa.frontEnd.importDialogs;

import java.awt.Frame;

import java.io.File;

import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractImportMultiRunDataFilesDialog extends AbstractImportMultiProbeDataFilesDialog {
	/**
	 * @param parent the parent frame
	 * @param modal is this modal?
	 */
	public AbstractImportMultiRunDataFilesDialog(AbstractDataSetUniqueId dataSetId, Frame parent, String title, boolean modal) throws BackEndAPIException {
		super(dataSetId, parent, title, modal);
	}
	
	@Override
	protected void initialise() {
		super.initialise();
		
		mAllowedNumbersOfTokens[0] = 3;
		mAllowedNumbersOfTokens[1] = 6;
		
//		mAllowFixedProbes = false;
	}
	
	protected class MultiRunFileToImportDetails extends FileToImportDetails {
		private final int mRunIndex;
		
		MultiRunFileToImportDetails(String[] coords, double theta, double phi, double alpha, String comments, File file) {
			super(coords, theta, phi, alpha, comments, file, 1);
			
			mRunIndex = Integer.parseInt(coords[0]);
		}
	}
	
	/**
	 * @param coords The coordinates of this data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param comment Any comment attached to the filename
	 * @param file The file to import the details from
	 * @param coordsIndexOffset Offset (from zero) of the first coordinate in coords
	 */ 
	@Override
	protected FileToImportDetails createFileToImportDetails(String[] coords, double theta, double phi, double alpha, String comment, File file) {
		return new MultiRunFileToImportDetails(coords, theta, phi, alpha, comment, file);
	}

	@Override
	protected MyDataModel getDataModel() {
		return new MultiRunDataModel();
	}

//	
//	@Override
//	public void setGUIStates() {
//		super.setGUIStates();
//		
//		clearFixedProbeSelection();
//	}
//	
	protected class MultiRunDataModel extends MyDataModel {
		private final String[] mAdditionalColTitles = {	
				DAStrings.getString(DAStrings.RUN_INDEX_LABEL) };
		
		public MultiRunDataModel() {
			super();
		}
		
		/**
		* Gets the column title
		* @return the column title
		*/
		@Override
		public String getColumnName(int i){
			if (i < mAdditionalColTitles.length) {
				return mAdditionalColTitles[i];
			} else {
				return super.getColumnName(i - mAdditionalColTitles.length);
			}
		}

		@Override
		/**
		 * Gets the number of columns in the table
		 * @return The number of columns in the table
		 */
		public int getColumnCount() {
			return super.getColumnCount() + mAdditionalColTitles.length;
		}

		@Override
		/**
		 * Gets the data for the specified cell in the table
		 * @param row The row of the required data
		 * @param column The column of the required data
		 * @return The data for the cell at row-column
		 */
		public Object getValueAt(int row, int column) {
			MultiRunFileToImportDetails fileDetails = (MultiRunFileToImportDetails) getFilesInDirectory().elementAt(row);
			
			switch (column) {
				case 0:
					return fileDetails.mRunIndex;
				default:
					return super.getValueAt(row, column - mAdditionalColTitles.length);
			}
		}
	}
}
