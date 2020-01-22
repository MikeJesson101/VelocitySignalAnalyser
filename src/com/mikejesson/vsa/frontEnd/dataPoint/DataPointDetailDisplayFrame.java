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


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.mikejesson.majfc.guiComponents.MAJFCAbstractButtonTab;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.DataAnalyser;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointDetailDisplayFrame extends MAJFCStackedPanelWithFrame {
	private JTabbedPane mDisplays;
	private Hashtable<String, int[]> mDisplayedPointsLookup = new Hashtable<String, int[]>();
	private final AbstractDataSetUniqueId mDataSetId;
	
	public DataPointDetailDisplayFrame(AbstractDataSetUniqueId dataSetId) {
		super(new GridBagLayout());
		mDataSetId = dataSetId;
		
		buildGUI();
		
		WindowListener windowListener = new WindowAdapter() {
			/**
			 * WindowAdapter implementation
			 */
			@Override
			public void windowClosing(WindowEvent theEvent) {
				removeAllDataPoints();
			}
		};
		
		showInFrame(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_FRAME_TITLE) + ": " + mDataSetId.getShortDisplayString(), windowListener, DAFrame.getFrame(), false);
	}

	/**
	 * Gets the preferred size
	 * @return The preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.width * 0.8);
		int height = (int) (screenSize.height * 0.9);

		return new Dimension(width, height);
	}
	
	/**
	 * Builds the GUI
	 */
	private void buildGUI() {
		mDisplays = new JTabbedPane();
		
		add(mDisplays, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 1, 1, 1, 1, 0, 0 ));
	}
	
	/**
	 * Add a data point to the displays
	 * @param yCoord
	 * @param zCoord
	 * @param dataPointSummary
	 * @param dataPointDetail
	 */
	public JFrame addDataPoint(int yCoord, int zCoord, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail) {
		Vector<DataPointSummary> dataPointSummaries = new Vector<DataPointSummary>(1);
		dataPointSummaries.add(dataPointSummary);
		
		Vector<DataPointDetail> dataPointDetails = new Vector<DataPointDetail>(1);
		dataPointDetails.add(dataPointDetail);
		
		return addDataPoint(yCoord, zCoord, dataPointSummaries, dataPointDetails);
	}
	
	/**
	 * Add a data point to the displays
	 * @param yCoord
	 * @param zCoord
	 * @param dataPointSummaries
	 * @param dataPointDetails
	 */
	public JFrame addDataPoint(int yCoord, int zCoord, Vector<DataPointSummary> dataPointSummaries, Vector<DataPointDetail> dataPointDetails) {
		String title = DATools.makeDataPointIdentifier(yCoord, zCoord);

		if (mDisplayedPointsLookup.get(title) != null) {
			mDisplays.setSelectedIndex(mDisplays.indexOfTab(title));
		} else {
			int tabsCount = mDisplays.getTabCount();
			int[] coords = {yCoord, zCoord};
			mDisplayedPointsLookup.put(title, coords);
			DataPointDetailDisplay dataPointDisplay = new DataPointDetailDisplay(mDataSetId, this, yCoord, zCoord, dataPointSummaries, dataPointDetails);
			mDisplays.add(title, dataPointDisplay);
			mDisplays.setTabComponentAt(tabsCount, new MyButtonTab(yCoord, zCoord, title));
			mDisplays.setSelectedIndex(tabsCount);
			validate();
			mFrame.validate();
			mFrame.pack();
		}
		
		if (mFrame.isVisible() == false) {
			mFrame.setLocationRelativeTo(DAFrame.getFrame());
			mFrame.setVisible(true);
		}
		
		return mFrame;
	}

	/**
	 * Removes a data point from the display
	 * @param yCoord
	 * @param zCoord
	 */
	public void removeDataPoint(int yCoord, int zCoord) {
		String title = DATools.makeDataPointIdentifier(yCoord, zCoord);

		if (mDisplayedPointsLookup.remove(title) == null) {
			return;
		}
		
		int tabIndex = mDisplays.indexOfTab(title);
		mDisplays.remove(tabIndex);

		if (mDisplays.getTabCount() == 0) {
			mFrame.setVisible(false);
		}

		try {
			DAFrame.getBackEndAPI().clearDataPointDetails(mDataSetId, yCoord, zCoord, DAFrame.getFrame().getBackEndAPICallBackAdapter());
		} catch (BackEndAPIException theException) {
			MAJFCLogger.log("Failed to clear data point details " + yCoord + ' ' + zCoord);
		}
	}
	
	/**
	 * Clears all the data points
	 */
	public void removeAllDataPoints() {
		Enumeration<int[]> coordsList = mDisplayedPointsLookup.elements();
		
		while (coordsList.hasMoreElements()) {
			int[] coords = coordsList.nextElement();
			removeDataPoint(coords[0], coords[1]);
		}
	}

	private class MyButtonTab extends MAJFCAbstractButtonTab {
		private final int mYCoord;
		private final int mZCoord;
		
		/**
		 * Constructor
		 * @param yCoord The y-coordinate of the point this button applies to
		 * @param zCoord The z-coordinate of the point this button applies to
		 */
		private MyButtonTab(int yCoord, int zCoord, String title) {
			super(title, DataAnalyser.getImageIcon("close.gif"));
			
			mYCoord = yCoord;
			mZCoord = zCoord;
		}

		/**
		 * Tab button pressed
		 */
		protected void onButtonPressed() {
			removeDataPoint(mYCoord, mZCoord);
		}
	}
}
