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
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class ImportMultiProbeBinaryDataFilesDialog extends AbstractImportMultiProbeDataFilesDialog {
	/**
	 * @param parent the parent frame
	 * @param modal is this modal?
	 */
	public ImportMultiProbeBinaryDataFilesDialog(AbstractDataSetUniqueId dataSetId, Frame parent, boolean modal) throws BackEndAPIException {
		super(dataSetId, parent, DAStrings.getString(DAStrings.IMPORT_MULTI_PROBE_BINARY_FILES_DIALOG_TITLE), modal);
	}
	
	/**
	 * Is this an allowed file type for this import?
	 * @return true if it is
	 */
	protected boolean isAllowedFile(File file) {
		return DATools.isPolySyncVNOFile(file) || DATools.isCobraTHxFile(file) || DATools.isUoBDPMSFile(file);
	}
	
	/**
	 * Imports point data from a file
	 * 
	 * @param the file to import from
	 * @param the y-coordinate of the data point
	 * @param the z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 */
	@Override
	public void importDataPointDataFromFile(File file, int yCoord, int zCoord, double theta, double phi, double alpha, char delimiter, char decimalSeparator) throws BackEndAPIException {
		if (DATools.isPolySyncVNOFile(file)) {
			DAFrame.getBackEndAPI().importDataPointDataFromPolySyncVNOFile(mDataSetId, file, mProbeSetup.getProbeCoords(yCoord, zCoord), theta, phi, alpha, mProbeSetup.getMainProbeIndex(), mProbeSetup.getFixedProbeIndex(), mProbeSetup.getSynchProbeIndex(), DAFrame.getFrame().getBackEndAPICallBackAdapter());
		} else if (DATools.isCobraTHxFile(file)) {
			String fileName = file.getName();
			char probeId = fileName.charAt(fileName.length() - 1);
			int probeIndex = Character.getNumericValue(probeId) - Character.getNumericValue('A');
			DataPointCoordinates coords = mProbeSetup.getProbeCoords(yCoord, zCoord).get(probeIndex);
			
			DAFrame.getBackEndAPI().importDataPointDataFromCobraTHxFile(mDataSetId, file, coords.getY(), coords.getZ(), theta, phi, alpha, DAFrame.getFrame().getBackEndAPICallBackAdapter());
		} else if (DATools.isUoBDPMSFile(file)) {
			DAFrame.getBackEndAPI().importDataPointDataFromUoBDPMSFile(mDataSetId, file, mProbeSetup.getProbeCoords(yCoord, zCoord), theta, phi, alpha, mProbeSetup.getMainProbeIndex(), mProbeSetup.getFixedProbeIndex(), mProbeSetup.getSynchProbeIndex(), DAFrame.getFrame().getBackEndAPICallBackAdapter());
		}
	}
}
