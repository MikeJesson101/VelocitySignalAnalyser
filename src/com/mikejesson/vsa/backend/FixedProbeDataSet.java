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

package com.mikejesson.vsa.backend;


import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;



/**
 * @author MAJ727
 *
 */
public class FixedProbeDataSet extends LinkedDataSet {
	private final DataPointCoordinates mCoordinates;
	
	/**
	 * Constructor - creates a new fixed probe data set for a fixed probe at the specified coordinates
	 * @param parentDataSetFile The parent data set's file
	 * @param coords The coordinates of the fixed probe
	 * @throws BackEndAPIException
	 */
	protected FixedProbeDataSet(DataSetType dataSetType, File parentDataSetFile, DataPointCoordinates coords, DataSetConfig configData) throws BackEndAPIException {
		super(dataSetType);

		mCoordinates = coords;
		mDataSetFile = new File(makeLinkedDataSetFilename(parentDataSetFile));
		mUniqueId = new DataSetUniqueId();
		
		setConfigData(configData);
		
		saveToFile(DAFrame.getFrame().getBackEndAPICallBackAdapter(), null);
	}
	
	/**
	 * Constructor - creates a new fixed probe data set for a fixed probe at the specified coordinates
	 * @param parentDataSetFile The parent data set's file
	 * @param coords The coordinates of the fixed probe
	 * @throws BackEndAPIException
	 */
	public FixedProbeDataSet(File parentDataSetFile, DataPointCoordinates coords, DataSetConfig configData) throws BackEndAPIException {
		this(BackEndAPI.DST_FIXED_PROBE, parentDataSetFile, coords, configData);
	}
	
	/**
	 * Constructor - creates a new fixed probe data set from the specified data file
	 * @param dataSetFile The data set's file
	 * @throws BackEndAPIException
	 */
	protected FixedProbeDataSet(DataSetType dataSetType, File dataSetFile) throws Exception {
		super(dataSetFile, dataSetType);

		StringTokenizer st1 = new StringTokenizer(stripLastExtension(dataSetFile.getName()), DADefinitions.COORDINATE_SEPARATOR);
		
		if (st1.countTokens() < 2) {
			throw new Exception();
		}
		
		String[] tokens = new String[st1.countTokens()];
		for (int i = 0; i < tokens.length; ++i) {
			tokens[i] = st1.nextToken();
		}
		
		// Coordinates should be last two tokens
		int yCoord = MAJFCTools.parseInt(tokens[tokens.length - 2]);
		int zCoord = MAJFCTools.parseInt(tokens[tokens.length - 1]);
		mCoordinates = new DataPointCoordinates(yCoord, zCoord);
	}

	/**
	 * Makes the fixed probe data set filename from the parent file
	 * @param parentDataSetFile
	 * @return
	 */
	protected String makeLinkedDataSetFilename(File parentDataSetFile) {
		StringBuffer dataSetFilename = new StringBuffer();
		dataSetFilename.append(makeDirectoryName(parentDataSetFile.getAbsolutePath()));
		dataSetFilename.append(MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR);
		dataSetFilename.append(DADefinitions.FIXED_PROBE_FILENAME_INSERT);
		dataSetFilename.append(mCoordinates.toString());
		dataSetFilename.append(DADefinitions.FILE_EXTENSION_DATA_SET);
		
		return dataSetFilename.toString();
	}

	/**
	 * Loads and creates the FixedProbeDataSets for the specified parent data set
	 * @param parentDataSetFile The parent data set's file
	 * @return A list of the FixedProbeDataSets for the parent DataSet
	 */
	public static Vector<LinkedDataSet> loadFixedProbeDataSets(File parentDataSetFile, DataSetType dataSetType) throws BackEndAPIException {
		Vector<LinkedDataSet> fixedProbeDataSets = new Vector<LinkedDataSet>();
		
		File parentDir = new File(makeDirectoryName(parentDataSetFile.getAbsolutePath()));
		File[] fileList = parentDir.listFiles();
		
		if (fileList == null) {
			return fixedProbeDataSets;
		}
		
		for (int i = 0; i < fileList.length; ++i) {
			if (fileList[i].isDirectory() == true) {
				continue;
			}
			
			if (fileList[i].getName().indexOf(DADefinitions.FIXED_PROBE_FILENAME_INSERT) < 0) {
				continue;
			}
			
			try {
				fixedProbeDataSets.add(dataSetType.equals(BackEndAPI.DST_MULTI_RUN_RUN) ? new MultiRunFixedProbeDataSet(fileList[i]) : new FixedProbeDataSet(BackEndAPI.DST_FIXED_PROBE, fileList[i]));
			} catch (Exception theException) {
				continue;
			}
		}
		
		return fixedProbeDataSets;
	}

	/**
	 * Gets the coordinates of the fixed probe
	 * @return The coordinates of the fixed probe
	 */
	public DataPointCoordinates getCoordinates() {
		return mCoordinates;
	}
}
