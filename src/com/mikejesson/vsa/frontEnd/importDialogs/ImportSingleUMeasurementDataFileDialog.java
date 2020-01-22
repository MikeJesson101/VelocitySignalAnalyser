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
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class ImportSingleUMeasurementDataFileDialog extends AbstractImportDataFilesDialog {
	/**
	 * @param parent the parent frame
	 * @param modal is this modal?
	 */
	public ImportSingleUMeasurementDataFileDialog(AbstractDataSetUniqueId dataSetId, Frame parent, boolean modal) throws BackEndAPIException {
		super(dataSetId, parent, DAStrings.getString(DAStrings.IMPORT_SINGLE_U_MEASUREMENTS_DIALOG_TITLE), DAStrings.getString(DAStrings.CHOOSE_SINGLE_U_MEASUREMENTS_FILE), modal);
	}

	@Override
	protected void initialise() {
		mAllowedNumbersOfTokens[0] = 1;
		mAllowedNumbersOfTokens[1] = 1;
	}

	/**
	 * Is this an allowed file type for this import?
	 * @return true if it is
	 */
	protected boolean isAllowedFile(File file) {
		return DATools.isCSVFile(file);
	}
	
	@Override
	public void importDataPointDataFromFile(File file, int yCoord, int zCoord, double theta, double phi, double alpha, char delimiter, char decimalSeparator) throws BackEndAPIException {
		DAFrame.getBackEndAPI().importSingleUMeasurementsFromCSV(mDataSetId, file, delimiter, decimalSeparator, DAFrame.getFrame().getBackEndAPICallBackAdapter());
	}
}
