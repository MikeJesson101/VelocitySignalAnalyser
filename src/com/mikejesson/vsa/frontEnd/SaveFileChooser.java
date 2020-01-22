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

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;


@SuppressWarnings("serial")
/**
 * File chooser used for saving data sets
 */
public class SaveFileChooser extends JFileChooser{
	
	/**
	 * Constructor
	 * @param defaultDirectory
	 */
	public SaveFileChooser(String defaultDirectory){
		super(defaultDirectory);
		FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Data Sets", "majds");
		setFileFilter(fileFilter);
		setAcceptAllFileFilterUsed(false);
	}
	
	/**
	 * Overwrite the file chooser method
	 */
	public void approveSelection(){
		File file = getSelectedFile();
		
		// Should only get this for a new file name, not when overwriting an existing file
		if (!file.getName().endsWith(DADefinitions.FILE_EXTENSION_DATA_SET)){
//			if (JOptionPane.showConfirmDialog(this, DAStrings.getString(DAStrings.FILE_NAME_WRONG_EXTENSION_DIALOG_MSG), DAStrings.getString(DAStrings.FILE_NAME_WRONG_EXTENSION_DIALOG_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.CANCEL_OPTION){
//				return;
//			}
			
			file = new File(file.getAbsolutePath() + DADefinitions.FILE_EXTENSION_DATA_SET);
		}

		// If we are not using the current file, get confirmation for overwriting
		if (file.exists()) {
			if (file.equals(DAFrame.getFrame().getCurrentDataSetFile()) == false && JOptionPane.showConfirmDialog(this, DAStrings.getString(DAStrings.FILE_EXISTS_DIALOG_MSG), DAStrings.getString(DAStrings.FILE_EXISTS_DIALOG_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.CANCEL_OPTION) {
				return;
			}
		} else{
			boolean fileCreated = true;
			try{
				fileCreated = file.createNewFile();
				setSelectedFile(file);
			}
			catch (Exception theException){
				fileCreated = false;
			}
			
			if (!fileCreated){
				JOptionPane.showMessageDialog(this, DAStrings.getString(DAStrings.ERROR_CREATING_FILE_DIALOG_MSG), DAStrings.getString(DAStrings.ERROR_CREATING_FILE_DIALOG_TITLE), JOptionPane.OK_OPTION);
				return;
			}
		}
			
		super.approveSelection();
	}
}
