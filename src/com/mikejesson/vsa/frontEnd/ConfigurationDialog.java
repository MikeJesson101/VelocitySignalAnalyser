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

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JDialog;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.widgits.DAStrings;

/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class ConfigurationDialog extends JDialog {
	/**
	 * The constructor
	 * @param parent the parent frame
	 * @param modal is this dialog modal?
	 */
	public ConfigurationDialog(Frame parent, boolean modal, boolean allowCancel) {
		super(parent, modal);

		setTitle(DAStrings.getString(DAStrings.CONFIGURATION_DIALOG_TITLE));
		setIconImage(DataAnalyser.getImage("wrench.png"));
		
		setLayout(new GridBagLayout());
		add(new ConfigurationPanel(this, allowCancel), MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
		
		validate();
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
