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


import javax.swing.JDialog;
import javax.swing.JTable;

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
public class DataPointSelectionTool extends JDialog implements ActionListener {
	private final AbstractDataSetUniqueId mDataSetId;
	private final JTable mTable;
	private final DataSetDataPointsOverviewDisplay.VelocitiesDetailsDataModel mDataModel;
	private MAJFCDialogButton mNewSelectionButton, mExtendSelectionButton, mCancelButton;
	private MAJFCNumberTextAreaPanel mYCoordMin, mYCoordMax, mZCoordMin, mZCoordMax;
	private MAJFCPanel mCoordinatesPanel;
	
	DataPointSelectionTool(AbstractDataSetUniqueId dataSetId, DataSetDataPointsOverviewDisplay.VelocitiesDetailsDataModel dataModel, JTable table) {
		super(DAFrame.getFrame(), true);
		
		mDataSetId = dataSetId;
		mDataModel = dataModel;
		mTable = table;
		
		buildGUI();
		
		setLocationRelativeTo(DAFrame.getFrame());
		validate();
		pack();
		setVisible(true);
	}
	
	/**
	 * Builds the GUI for this dialog
	 */
	private void buildGUI() {
		setLayout(new GridBagLayout());
		
		mNewSelectionButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL), this);
		mExtendSelectionButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL), this);
		mCancelButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.CANCEL), this);

		double waterDepth = 0, leftBankPosition = 0, rightBankPosition = 0;

		try {
			DataSetConfig configData = BackEndAPI.getBackEndAPI().getConfigData(mDataSetId);

			waterDepth = configData.get(BackEndAPI.DSC_KEY_WATER_DEPTH);
			rightBankPosition = configData.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION);
			leftBankPosition = configData.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION);
		} catch (BackEndAPIException theException) {
			
		}
		
		mYCoordMin = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.Y_COORD_MIN_LABEL), leftBankPosition, rightBankPosition, leftBankPosition, 0);
		mYCoordMax = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.Y_COORD_MAX_LABEL), leftBankPosition, rightBankPosition, rightBankPosition, 0);
		mZCoordMin = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.Z_COORD_MIN_LABEL), 0, waterDepth, 0, 0);
		mZCoordMax = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.Z_COORD_MAX_LABEL), 0, waterDepth, waterDepth, 0);
		
		int x = 0, y = 0;
		mCoordinatesPanel = new MAJFCPanel(new GridBagLayout());
		mCoordinatesPanel.add(mYCoordMin, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		mCoordinatesPanel.add(mYCoordMax, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		x = 0;
		mCoordinatesPanel.add(mZCoordMin, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		mCoordinatesPanel.add(mZCoordMax, MAJFCTools.createGridBagConstraint(x++, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		MAJFCPanel buttonPanel = new MAJFCPanel(new GridBagLayout());
		buttonPanel.add(mNewSelectionButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		buttonPanel.add(mExtendSelectionButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));
		buttonPanel.add(mCancelButton, MAJFCTools.createGridBagConstraint(x++, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, 5, 5, 5, 5, 3, 3));

		y = 0;
		add(mCoordinatesPanel, MAJFCTools.createGridBagConstraint(0, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		add(buttonPanel, MAJFCTools.createGridBagConstraint(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
	}

	@Override
	/**
	 * ActionListener implementation
	 * @param arg0 The ActionEvent
	 */
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		
		if (command.equals(DAStrings.getString(DAStrings.SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL))
			|| command.equals(DAStrings.getString(DAStrings.SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL))) {
			if (mCoordinatesPanel.componentValuesValid() == false) {
				return;
			}
			
			selectRows(command.equals(DAStrings.getString(DAStrings.SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL)));
		} else if (command.equals(DAStrings.getString(DAStrings.CANCEL))) {
			setVisible(false);
		}
	}
	
	/**
	 * Select the rows in the data model
	 * @param newSelection Set true to clear existing selection, false to extend existing selection
	 */
	private void selectRows(boolean newSelection) {
		int yMin = mYCoordMin.getValue().intValue(), yMax = mYCoordMax.getValue().intValue();
		int zMin = mZCoordMin.getValue().intValue(), zMax = mZCoordMax.getValue().intValue();
		
		if (newSelection) {
			mTable.clearSelection();
		}
		
		int numberOfRows = mDataModel.getRowCount();
		
		for (int modelIndex = 0; modelIndex < numberOfRows; ++modelIndex) {
			int rowYCoord =	(Integer) mDataModel.getValueAt(modelIndex, mDataModel.Y_COORD_COLUMN_INDEX);
			
			if (rowYCoord < yMin || rowYCoord > yMax) {
				continue;
			}
			
			int rowZCoord =	(Integer) mDataModel.getValueAt(modelIndex, mDataModel.Z_COORD_COLUMN_INDEX);
	
			if (rowZCoord < zMin || rowZCoord > zMax) {
				continue;
			}
			
			// If we've got to here then we should be selecting this row
			int tableIndex = mTable.convertRowIndexToView(modelIndex);

			if (mTable.isCellSelected(tableIndex, 0) == false) {
				mTable.changeSelection(tableIndex, 0, true, false);
			}
		}
	}
}
