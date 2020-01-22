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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;


import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.mikejesson.majfc.guiComponents.MAJFCDialogButton;
import com.mikejesson.majfc.guiComponents.MAJFCNumberTextAreaPanel;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class EmpiricalOrthogonalFunctionAnalysisRMatrixCalculationDialog extends JDialog implements ActionListener, PropertyChangeListener {
	private final AbstractDataSetUniqueId mDataSetId;
	private MAJFCDialogButton mOkButton, mCancelButton;
	private MAJFCNumberTextAreaPanel mY1GUI, mY2GUI;
	private MAJFCNumberTextAreaPanel mZ1GUI, mZ2GUI;
	private MAJFCPanel coordinatesPanel, mButtonPanel;

	/**
	 * Constructor
	 * 
	 * @param uniqueId Id of the data set represented
	 */
	public EmpiricalOrthogonalFunctionAnalysisRMatrixCalculationDialog(AbstractDataSetUniqueId uniqueId) {
		super(DAFrame.getFrame(), true);
		
		mDataSetId = uniqueId;
		
		setTitle(DAStrings.getString(DAStrings.EOF_R_MATRIX_DIALOG_TITLE));
		
		buildGUI();
		
		validate();
		pack();
		setLocationRelativeTo(DAFrame.getFrame());
		setVisible(true);
	}
	
	/**
	 * Builds the GUI
	 */
	private void buildGUI() {
		setLayout(new GridBagLayout());

		mOkButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.OK), this);
		mCancelButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.CANCEL), this);

		try {
			DataSetConfig config = DAFrame.getBackEndAPI().getConfigData(mDataSetId);
			double leftBankPosition = config.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION);
			double rightBankPosition = config.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION);
			double waterDepth = config.get(BackEndAPI.DSC_KEY_WATER_DEPTH);
			
			mY1GUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.EOF_R_MATRIX_DIALOG_Y1_LABEL), leftBankPosition, rightBankPosition, leftBankPosition, 0);
			mY2GUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.EOF_R_MATRIX_DIALOG_Y2_LABEL), leftBankPosition, rightBankPosition, rightBankPosition, 0);
			mZ1GUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.EOF_R_MATRIX_DIALOG_Z1_LABEL), 0, waterDepth, 0, 0);
			mZ2GUI = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.EOF_R_MATRIX_DIALOG_Z2_LABEL), 0, waterDepth, waterDepth, 0);
			
			mY1GUI.addPropertyChangeListenerToTextField(this);
			mY2GUI.addPropertyChangeListenerToTextField(this);
			mZ1GUI.addPropertyChangeListenerToTextField(this);
			mZ2GUI.addPropertyChangeListenerToTextField(this);
		} catch (Exception theException) {
			theException.printStackTrace();
			return;
		}

		int x = 0, y = 0;
		coordinatesPanel = new MAJFCPanel(new GridBagLayout());
		coordinatesPanel.add(mY1GUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		coordinatesPanel.add(mY2GUI, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		coordinatesPanel.add(mZ1GUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		coordinatesPanel.add(mZ2GUI, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		
		x = 0;
		mButtonPanel = new MAJFCPanel(new GridBagLayout());
		mButtonPanel.add(mOkButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		mButtonPanel.add(mCancelButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));

		y = 0;
		add(coordinatesPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		add(mButtonPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
	}

	@Override
	/**
	 * ActionListener implementation
	 * @param theEvent The ActionEvent
	 */
	public void actionPerformed(ActionEvent theEvent) {
		Object source = theEvent.getSource();
		
		if (source.equals(mOkButton)){
			if (coordinatesPanel.componentValuesValid() == false) {
				return;
			}
			
			Vector<Integer> allSortedYCoords = new Vector<Integer>();
			Vector<Vector<Integer>> allSortedZCoords = new Vector<Vector<Integer>>();
			
			try {
				DAFrame.getBackEndAPI().getSortedDataPointCoordinates(mDataSetId, allSortedYCoords, allSortedZCoords);
			}
			catch (BackEndAPIException theException) {
		       	JOptionPane.showMessageDialog(DAFrame.getFrame(), DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_MSG), DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_TITLE), JOptionPane.ERROR_MESSAGE);
		       	return;
			}
			
			Vector<Integer> sortedYCoordsToUse = new Vector<Integer>(allSortedYCoords.size());
			Vector<Vector<Integer>> sortedZCoordsToUse = new Vector<Vector<Integer>>(allSortedZCoords.size());
			int numberOfYCoords = allSortedYCoords.size();
			int y1 = mY1GUI.getValue().intValue();
			int y2 = mY2GUI.getValue().intValue();
			int z1 = mZ1GUI.getValue().intValue();
			int z2 = mZ2GUI.getValue().intValue();
			
			// Pick out the selected columns of data (indexed by y-coodinate)
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = allSortedYCoords.elementAt(yCoordIndex);
				
				if (yCoord >= y1 && yCoord <= y2) {
					sortedYCoordsToUse.add(yCoord);
					Vector<Integer> zCoords = allSortedZCoords.elementAt(yCoordIndex);
					int numberOfZCoords = zCoords.size();
					Vector<Integer> zCoordsToUse = new Vector<Integer>(numberOfZCoords);
										
					for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
						Integer zCoord = zCoords.elementAt(zCoordIndex);
						
						if (zCoord >= z1 && zCoord <= z2) {
							zCoordsToUse.add(zCoord);
						} else if (zCoord > z2) {
							break;
						}
					}
					
					sortedZCoordsToUse.add(zCoordsToUse);
				} else if (yCoord > y2) {
					break;
				}
			}

			numberOfYCoords = sortedYCoordsToUse.size();
			int numberOfZCoords = sortedZCoordsToUse.elementAt(0).size();
			
			// Check that the grid of data points is complete, with no missing points
			for (int i = 0; i < numberOfYCoords; ++i) {
				if (sortedZCoordsToUse.elementAt(i).size() != numberOfZCoords) {
					JOptionPane.showMessageDialog(DAFrame.getFrame(), DAStrings.getString(DAStrings.EOF_R_MATRIX_INCOMPLETE_GRID_MSG), DAStrings.getString(DAStrings.EOF_R_MATRIX_INCOMPLETE_GRID_TITLE), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			// And now build the
			
		} else if (source.equals(mCancelButton)) {
			setVisible(false);
		}
	}

	@Override
	/**
	 * PropertyChangeListener implementation
	 * @param theEvent The PropertyChangeEvent
	 */
	public void propertyChange(PropertyChangeEvent theEvent) {
		if (mY1GUI.isSource(theEvent)) {
			mY2GUI.setMinimumValue(mY1GUI.getValue());
		} else if (mY2GUI.isSource(theEvent)) {
			mY1GUI.setMaximumValue(mY2GUI.getValue());
		} else if (mZ1GUI.isSource(theEvent)) {
			mZ2GUI.setMinimumValue(mZ1GUI.getValue());
		} else if (mZ2GUI.isSource(theEvent)) {
			mZ1GUI.setMaximumValue(mZ2GUI.getValue());
		}
		
		setGUIStates();
	}
	
	/**
	 * Sets the states of the GUI components
	 */
	public void setGUIStates() {
		mOkButton.setEnabled(coordinatesPanel.componentValuesValid());
	}
}
