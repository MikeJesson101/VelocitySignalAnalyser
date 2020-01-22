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
import java.util.LinkedList;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractImportMultiProbeDataFilesDialog extends AbstractImportDataFilesDialog {
	protected ProbeSetupPanel mProbeSetup;
	private boolean mAllowMultipleProbes;
	protected boolean mAllowFixedProbes;
	
	/**
	 * @param parent the parent frame
	 * @param modal is this modal?
	 * @throws BackEndAPIException 
	 */
	public AbstractImportMultiProbeDataFilesDialog(AbstractDataSetUniqueId dataSetId, Frame parent, String title, boolean modal) throws BackEndAPIException {
		super(dataSetId, parent, title, modal);
	}

	@Override
	protected void initialise() {
		mAllowMultipleProbes = mDataSetType.equals(BackEndAPI.DST_SINGLE_PROBE) == false;
		mAllowFixedProbes = mAllowMultipleProbes;
	}
	
	/**
	 * Is this an allowed file type for this import?
	 * @return true if it is
	 */
	protected abstract boolean isAllowedFile(File file);
	
	/**
	 * Allows child classes to add a custom panel above the buttons
	 * @return The custom panel or null for no custom panel
	 */
	protected MAJFCPanel buildCustomPanel() {
		mProbeSetup = new ProbeSetupPanel(mDataSetId, mAllowMultipleProbes, mAllowFixedProbes);
		
		return mProbeSetup;
	}
	
	protected LinkedList<DataPointCoordinates> getProbeCoords(int yCoord, int zCoord) {
		return mProbeSetup.getProbeCoords(yCoord, zCoord);
	}
	
	protected int getMainProbeIndex() {
		return mProbeSetup.getMainProbeIndex();
	}
	
	protected int getFixedProbeIndex() {
		return mProbeSetup.getFixedProbeIndex();
	}
	
	protected int getSynchParentProbeIndex() {
		return mProbeSetup.getSynchProbeIndex();
	}
}
