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



import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;


/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class DataSetConfigInfoDisplay extends MAJFCPanel {
	private AbstractDataSetUniqueId mDataSetId;
	private final DataSetType mDataSetType;
	private final DataSetOverviewDisplay mParent;
	
	/**
	 * @param actionListener 
	 * 
	 */
	public DataSetConfigInfoDisplay(AbstractDataSetUniqueId dataSetId, DataSetType dataSetType, DataSetOverviewDisplay parent) {
		mDataSetId = dataSetId;
		mDataSetType = dataSetType;
		mParent = parent;
		
		buildGUI();
	}

	/**
	 * Builds the GUI
	 * @param actionListener The action listener for the controls on this panel
	 */
	private void buildGUI() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		add(new ConfigurationPanel(mDataSetId, mDataSetType, mParent), MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
	}
	
		/**
	 * Updates the values in the display
	 */
	public void updateDisplay() {
	}

	/**
	 * Sets the data set id
	 * @param newDataSetId The new data set id
	 */
	public void setDataSetId(AbstractDataSetUniqueId newDataSetId) {
		mDataSetId = newDataSetId;
	}
}
