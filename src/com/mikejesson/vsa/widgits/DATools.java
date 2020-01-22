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

import java.awt.Component;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.mikejesson.majfc.guiComponents.MAJFCAbstractCallbackButton;
import com.mikejesson.vsa.frontEnd.DAFrame;

/**
 * @author MAJ727
 *
 */
public class DATools {
	private static Hashtable <String, String> mAllowedCSVFileExtensionsLookup;
	private static Hashtable <String, String> mAllowedVectrinoVNOFileExtensionsLookup;
	private static Hashtable <String, String> mAllowedPolySyncVNOFileExtensionsLookup;
	private static Hashtable <String, String> mAllowedVectorVECFileExtensionsLookup;
	private static Hashtable <String, String> mAllowedVectrinoIIMatFileExtensionsLookup;
	private static Hashtable <String, String> mAllowedVectrinoIIRawFileExtensionsLookup;
	private static Hashtable <String, String> mAllowedSontekADVFileExtensionsLookup;
	private static Hashtable <String, String> mAllowedUoBDPMSFileExtensionsLookup;
	private static Vector<String> mAllowedConvertedPolySyncVelocityFileExtensionsLookup;
	private static Vector<String> mAllowedCobraTHxFileExtensions;
		
	/**
	 * Is this a CSV file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isCSVFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_CSV, mAllowedCSVFileExtensionsLookup);
	}
	
	private static boolean isAllowedFile(File file, String fileExtensionsDefintion, Hashtable<String, String> allowedFileExtensionsLookup) {
		if (allowedFileExtensionsLookup == null) {
			allowedFileExtensionsLookup = new Hashtable<String, String>();
			
			StringTokenizer tokenizer = new StringTokenizer(fileExtensionsDefintion, ":");
			
			while (tokenizer.hasMoreTokens()) {
				String extension = tokenizer.nextToken();
				allowedFileExtensionsLookup.put(extension, extension);
			}
			
		}
		
		String filename = file.getName();
		int lastDotPosition = filename.lastIndexOf('.');

		if ((lastDotPosition < 0) || (lastDotPosition == filename.length())) {
			return false;
		}
		
		return allowedFileExtensionsLookup.get(filename.substring(lastDotPosition + 1).toLowerCase()) != null;
	}
	
	/**
	 * Is this a Vectrino VNO file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isVectrinoVNOFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_VECTRINO_VNO, mAllowedVectrinoVNOFileExtensionsLookup);
	}
	
	/**
	 * Is this a PolySync VNO file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isPolySyncVNOFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_POLYSYNC_VNO, mAllowedPolySyncVNOFileExtensionsLookup);
	}
	
	/**
	 * Is this a Vector VECO file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isVectorVECFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_VECTOR_VEC, mAllowedVectorVECFileExtensionsLookup);
	}
	
	/**
	 * Is this a Sontek ADV file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isSontekADVFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_SONTEK_ADV, mAllowedSontekADVFileExtensionsLookup);
	}
	
	/**
	 * Is this a Cobra thX file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isCobraTHxFile(File file) {
		if (mAllowedCobraTHxFileExtensions == null) {
			mAllowedCobraTHxFileExtensions = new Vector<String>();
			
			StringTokenizer tokenizer = new StringTokenizer(DADefinitions.FILE_EXTENSION_COBRA_THx, ":");
			
			while (tokenizer.hasMoreTokens()) {
				String extension = tokenizer.nextToken();
				mAllowedCobraTHxFileExtensions.add(extension);
			}
			
		}
		
		// Cobra filenames have a "th" extension with a third letter indicating the probe (e.g. <filename>.thA), so can't
		// use same method as for CSV files
		String filename = file.getName();
		Enumeration<String> allowedExtensions = mAllowedCobraTHxFileExtensions.elements();

		while (allowedExtensions.hasMoreElements()) {
			if (filename.contains('.' + allowedExtensions.nextElement())) {
				return true;
			}
		}
		
		return false;	
	}
	
	/**
	 * Is this a Vectrino II Matlab file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isVectrinoIIMatFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_VECTRINO_II_MAT, mAllowedVectrinoIIMatFileExtensionsLookup);
	}

	/**
	 * Is this a Vectrino II Matlab file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isVectrinoIIRawFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_VECTRINO_II_RAW, mAllowedVectrinoIIRawFileExtensionsLookup);
	}

	/**
	 * Is this a PolySync velocity file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isConvertedPolySyncVelocityFile(File file) {
		if (mAllowedConvertedPolySyncVelocityFileExtensionsLookup == null) {
			mAllowedConvertedPolySyncVelocityFileExtensionsLookup = new Vector<String>();
			
			StringTokenizer tokenizer = new StringTokenizer(DADefinitions.FILE_EXTENSION_CONVERTED_POLYSYNC_VELOCITY, ":");
			
			while (tokenizer.hasMoreTokens()) {
				String extension = tokenizer.nextToken();
				mAllowedConvertedPolySyncVelocityFileExtensionsLookup.add(extension);
			}
			
		}
		
		// PolySync filenames have a double extension (e.g. <filename>.vel.txt), so can't
		// use same method as for CSV files
		String filename = file.getName();
		Enumeration<String> allowedExtensions = mAllowedConvertedPolySyncVelocityFileExtensionsLookup.elements();

		while (allowedExtensions.hasMoreElements()) {
			if (filename.endsWith(allowedExtensions.nextElement())) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Is this a UoB DPMS file?
	 * @param file File to check
	 * @return True if it is
	 */
	public static boolean isUoBDPMSFile(File file) {
		return isAllowedFile(file, DADefinitions.FILE_EXTENSION_UOB_DPMS, mAllowedUoBDPMSFileExtensionsLookup);
	}

	/**
	 * Makes an identifier for the data point display
	 * @param yCoord The y-coordinate of the point
	 * @param zCoord The z-coordinate of the point
	 */
	public static String makeDataPointIdentifier(Number yCoord, Number zCoord) {
		StringBuffer identifier = new StringBuffer();
		
		identifier.append(yCoord);
		identifier.append('-');
		identifier.append(zCoord);
		
		return identifier.toString();
	}
	
	/**
	 * Gets coordinates from a data-point identifier
	 * @param identifier The identifier for the data-point
	 * @return coords The coordinates of the point
	 * @param zCoord The z-coordinate of the point
	 */
	public static Vector<Double> getCoordsFromDataPointIdentifier(String identifier) {
		StringTokenizer st = new StringTokenizer(identifier, "-");
		double yCoord = Double.valueOf(st.nextToken());
		double zCoord = Double.valueOf(st.nextToken());
		
		int minusPos = identifier.indexOf("--");
		if (minusPos == 0) {
			yCoord *= -1;
			
			minusPos = identifier.indexOf("--", minusPos + 1);
		}
		
		if (minusPos > 0) {
			zCoord *= -1;
		}
		
		Vector<Double> coords = new Vector<Double>(2);
		coords.add(yCoord);
		coords.add(zCoord);
		
		return coords;
	}
	
	/**
	 * 
	 */
	private DATools() {
	}

	/**
	 * Brings up a dialog to allow selection of a file
	 * @param title The title for the dialog (can be null to use default title)
	 * @param defaultDirectory The default directory shown
	 * @param callbackButton The button which called this (can be null if the button text is not to be changed)
	 */
	public static void chooseFile(String title, String defaultDirectory, MAJFCAbstractCallbackButton<File> callbackButton, FileFilter fileFilter) {
		chooseFileOrDirectory(title, defaultDirectory, callbackButton, JFileChooser.FILES_AND_DIRECTORIES, fileFilter);
	}

	/**
	 * Brings up a dialog to allow selection of a file
	 * @param title The title for the dialog (can be null to use default title)
	 * @param defaultDirectory The default directory shown
	 * @param callbackButton The button which called this (can be null if the button text is not to be changed)
	 */
	private static void chooseFileOrDirectory(String title, String defaultDirectory, MAJFCAbstractCallbackButton<File> callbackButton, int selectionMode, FileFilter fileFilter) {
		JFileChooser fileChooser = new JFileChooser(defaultDirectory);
		fileChooser.setFileSelectionMode(selectionMode);
		fileChooser.setAcceptAllFileFilterUsed(false);
		
		if (fileFilter != null) {
			fileChooser.setFileFilter(fileFilter);
		}
		
		if (title != null) {
			fileChooser.setDialogTitle(title);
		}
		
		fileChooser.setApproveButtonText(DAStrings.getString(DAStrings.SELECT));
			
		int returnVal = fileChooser.showOpenDialog(DAFrame.getFrame());

        if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
          	
			callbackButton.callback(file);
        }
	}

	/**
	 * Brings up a dialog to allow selection of a directory
	 * @param title The title for the dialog (can be null to use default title)
	 * @param defaultDirectory The default directory shown
	 * @param callbackButton The button which called this (can be null if the button text is not to be changed)
	 */
	public static void chooseDirectory(String title, String defaultDirectory, MAJFCAbstractCallbackButton<File> callbackButton ) {
		chooseFileOrDirectory(title, defaultDirectory, callbackButton, JFileChooser.DIRECTORIES_ONLY, null);
	}
	
	/**
	 * Resizes table columns based on their contents.
	 * @param the table to resize
	 */
	public static int resizeColumnWidth(JTable table) {
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int totalWidth = 0;
		
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int columnIndex = 0; columnIndex < table.getColumnCount(); columnIndex++) {
	    	int width = 0;
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, columnIndex);
	            Component comp = table.prepareRenderer(renderer, row, columnIndex);
	            width = Math.max(width, comp.getPreferredSize().width + 1);
	        }
	        
	        TableColumn column = columnModel.getColumn(columnIndex);
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = table.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = column.getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, 0, columnIndex);
            width = Math.max(width, headerComp.getPreferredSize().width + 5);
            
	        column.setPreferredWidth(width);
	        
	        totalWidth += width;
	    }
	    
	    return totalWidth;
	}
}
