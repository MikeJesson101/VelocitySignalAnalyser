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

package com.mikejesson.vsa.widgits;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel.MAJFCTabContents;
import com.mikejesson.majfc.helpers.MAJFCTools;


/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class ComponentTabsFrame extends MAJFCStackedPanelWithFrame { // Should probably be based on TabsFrame but no time to sort it out
	private final MAJFCTabbedPanel mTabbedPanel;

	/**
	 * Constructor
	 * 
	 * @param parent The parent panel
	 * @param xPanel The x panel
	 * @param yPanel The y panel
	 * @param zPanel The z panel
	 * @param additionalFrames Any additional panels to add
	 * @param additionalFrameLabels Labels for the additional panels
	 */
	public ComponentTabsFrame(MAJFCStackedPanelWithFrame parent, MAJFCPanel xPanel, MAJFCPanel yPanel, MAJFCPanel zPanel, MAJFCTabContents ... additionalTabContents) {
		super(parent, new GridBagLayout());
		
		MAJFCPanel[] panels = new MAJFCPanel[] { xPanel, yPanel, zPanel };
		
		for (int i = 0; i < panels.length; ++i) {
			MAJFCPanel panel = panels[i];
			if (panel instanceof MAJFCStackedPanelWithFrame) {
				((MAJFCStackedPanelWithFrame) panel).setParent(this);
			}
		}
		
		if (additionalTabContents != null) {
			for (int i = 0; i < additionalTabContents.length; ++i) {
				MAJFCPanel panel = additionalTabContents[i].getPanel();
				if (panel instanceof MAJFCStackedPanelWithFrame) {
					((MAJFCStackedPanelWithFrame) panel).setParent(this);
				}
			}
		}

		mTabbedPanel = new MAJFCTabbedPanel(0, new MAJFCTabContents(DAStrings.getString(DAStrings.U_VELOCITIES_LABEL), xPanel), new MAJFCTabContents(DAStrings.getString(DAStrings.V_VELOCITIES_LABEL), yPanel), new MAJFCTabContents(DAStrings.getString(DAStrings.W_VELOCITIES_LABEL), zPanel), additionalTabContents);
		
		add(mTabbedPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}
}
