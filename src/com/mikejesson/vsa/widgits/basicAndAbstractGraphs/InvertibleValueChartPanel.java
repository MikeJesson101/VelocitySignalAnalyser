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

package com.mikejesson.vsa.widgits.basicAndAbstractGraphs;


import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import org.jfree.chart.JFreeChart;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.widgits.DAStrings;



@SuppressWarnings("serial")
public abstract class InvertibleValueChartPanel extends ColourCodedChartPanel {
	private JCheckBox mInvert;
	
	public InvertibleValueChartPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, JFreeChart theChart, ScaleableXYZDataSet scaleableDataSet, int nonDimensionlessBlockHeight, int nonDimensionlessBlockWidth, int steps, String legendText, boolean dataIsVectorInY) {
		super(dataSetId, parent, theChart, scaleableDataSet, nonDimensionlessBlockHeight, nonDimensionlessBlockWidth, steps, legendText, dataIsVectorInY);
	}
	
	/**
	 * {@link ScaleableChartPanel#getAdditionalComponents()}
	 */
	protected Vector<JComponent> getAdditionalComponents() {
		Vector<JComponent> additionalComponents = super.getAdditionalComponents();
		
		mInvert = new JCheckBox(DAStrings.getString(DAStrings.INVERT_CHECKBOX_LABEL));
		mInvert.addActionListener(this);
		
		additionalComponents.addElement(mInvert);
		
		return additionalComponents;
	}
	
	protected abstract void updateChartDataArray();
	
	/**
	 * Is this chart being displayed with the ratio inverted?
	 * @return True if is
	 */
	public boolean displayInverted() {
		return mInvert.isSelected();
	}
	
	@Override
	public void actionPerformed(ActionEvent theEvent) {
		String action = theEvent.getActionCommand();
		
		if (action.equals(DAStrings.getString(DAStrings.INVERT_CHECKBOX_LABEL))) {
			updateChartDataArray();
		} else {
			super.actionPerformed(theEvent);
		}
	}
}