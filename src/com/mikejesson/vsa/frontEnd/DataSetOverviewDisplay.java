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



import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JTabbedPane;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.widgits.DAStrings;


/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataSetOverviewDisplay extends MAJFCPanel {
	private JTabbedPane mDataPointsOverviewsHolder = new JTabbedPane();
	private Hashtable<AbstractDataSetUniqueId, DataSetDataPointsOverviewDisplay> mDataPointsOverviewsLookup = new Hashtable<AbstractDataSetUniqueId, DataSetDataPointsOverviewDisplay>();
	private ConfigurationPanel mConfigInfoDisplay;
	private AbstractDataSetUniqueId mDataSetId;
	private DataSetType mDataSetType;
	private final String MAIN_TAB_LABEL = DAStrings.getString(DAStrings.MAIN_DATA_POINTS_OVERVIEW_DISPLAY_LABEL);

	private boolean mConfigChanged = false;
	private boolean mDataSetChanged = false;

	/**
	 * Constructs the display
	 */
	public DataSetOverviewDisplay(AbstractDataSetUniqueId dataSetId) {
		super(new GridBagLayout());
		
		mDataSetId = dataSetId;
		mDataSetType = BackEndAPI.DST_FIXED_PROBE;
		
		try {
			mDataSetType = BackEndAPI.getBackEndAPI().getDataSetType(dataSetId);
		} catch (BackEndAPIException e) {
			e.printStackTrace();
		}

		buildGUI();
	}
	
	/**
	 * Builds the GUI
	 */
	private void buildGUI() {
		mConfigInfoDisplay = new ConfigurationPanel(mDataSetId, mDataSetType, this);
		
		addDataPointsOverviewDisplay(MAIN_TAB_LABEL, mDataSetId, mDataSetType);
		mDataPointsOverviewsHolder.add(DAStrings.getString(DAStrings.CONFIGURATION_TAB_LABEL), mConfigInfoDisplay);
		
		int y = 0;
		add(mDataPointsOverviewsHolder, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 1.0, 0.5, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 10, 5, 0, 5, 0, 0));
	}

	public void configChanged() {
		mConfigChanged = true;

		DAFrame.getFrame().setGUIStates();
		updateDisplay();
	}
	
	/**
	 * Updates the display
	 */
	public void updateDisplay() {
		addLinkedDataSetViews();
		
		mConfigInfoDisplay.updateDisplay();
		
		Enumeration<DataSetDataPointsOverviewDisplay> dataPointOverviewDisplays = mDataPointsOverviewsLookup.elements();
		while (dataPointOverviewDisplays.hasMoreElements()) {
			dataPointOverviewDisplays.nextElement().updateDisplay();
		}

		repaint();
	}

	private void addLinkedDataSetViews() {
		Vector<AbstractDataSetUniqueId> linkedDataSetIds;
		
		try {
			linkedDataSetIds = DAFrame.getBackEndAPI().getLinkedDataSetIds(mDataSetId);
		} catch (BackEndAPIException theException) {
			theException.printStackTrace();
			return;
		}
		
		int numberOfLinkedProbeDataSets = linkedDataSetIds.size();
		
		for (int i = 0; i < numberOfLinkedProbeDataSets; ++i) {
			AbstractDataSetUniqueId linkedProbeDataSetId = linkedDataSetIds.elementAt(i);
			DataSetType dataSetType = BackEndAPI.DST_FIXED_PROBE;
			
			try {
				dataSetType = BackEndAPI.getBackEndAPI().getDataSetType(linkedProbeDataSetId);
			} catch (BackEndAPIException e) {
				e.printStackTrace();
			}
			
			if (mDataPointsOverviewsLookup.get(linkedProbeDataSetId) == null) {
				addDataPointsOverviewDisplay(linkedProbeDataSetId.getFullDisplayString(), linkedProbeDataSetId, dataSetType);
			}
		}
	}

	/**
	 * Adds a DataSetDataPointsOverviewDisplay for the specified data set
	 * @param dataSetId The id of the data set to add a view for
	 */
	private void addDataPointsOverviewDisplay(String label, AbstractDataSetUniqueId dataSetId, DataSetType dataSet) {
		DataSetDataPointsOverviewDisplay dataPointsOverviewDisplay = new DataSetDataPointsOverviewDisplay(dataSetId, dataSet); 
		mDataPointsOverviewsLookup.put(dataSetId, dataPointsOverviewDisplay);
		mDataPointsOverviewsHolder.add(label, dataPointsOverviewDisplay);
	}
	
	/**
	 * Sets the config data in the GUI.
	 * @param configData The configuration data
	 */
	public void setConfigData(DataSetConfig configData) {
		mConfigInfoDisplay.setConfigData(configData);
		mConfigInfoDisplay.resetDataFieldChangedFlags();

		mConfigChanged = false;
	}
	
	/**
	 * Has configuration data been changed?
	 * @return True if the configuration data has been changed by the user
	 */
	public boolean hasChanged() {
		return mDataSetChanged || mConfigChanged;
	}

	/**
	 * Sets the flag indicating whether the data set represented by this view has changed
	 * @param dataSetChanged The value to set the mDataSetChanged to
	 */
	public void setDataSetChanged(boolean dataSetChanged) {
		mDataSetChanged = dataSetChanged;
	}

	/**
	 * Sets the data set id
	 * @param newDataSetId The new data set id
	 */
	public void setDataSetId(AbstractDataSetUniqueId newDataSetId) {
		DataSetDataPointsOverviewDisplay overviewDisplay = mDataPointsOverviewsLookup.remove(mDataSetId);
		
		if (overviewDisplay != null) {
			overviewDisplay.setDataSetId(newDataSetId);
			mDataPointsOverviewsLookup.put(newDataSetId, overviewDisplay);
		}
		
		mConfigInfoDisplay.setDataSetId(newDataSetId);
		
		mDataSetId = newDataSetId;
	}
}