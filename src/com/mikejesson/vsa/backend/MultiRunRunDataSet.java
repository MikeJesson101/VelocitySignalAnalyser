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
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndCallbackInterface;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;



/**
 * @author MAJ727
 *
 */
public class MultiRunRunDataSet extends LinkedDataSet {
	private final int mRunIndex;
	
	/**
	 * Constructor - creates a new fixed probe data set for a fixed probe at the specified coordinates
	 * @param filename The filename for this data set
	 * @param configData The config data for this data set
	 * @throws BackEndAPIException
	 */
	public MultiRunRunDataSet(File parentDataSetFile, int runIndex, DataSetConfig configData) throws BackEndAPIException {
		super(BackEndAPI.DST_MULTI_RUN_RUN);
		
		mRunIndex = runIndex;
		mDataSetFile = new File(makeLinkedDataSetFilename(parentDataSetFile));
		mUniqueId = new DataSetUniqueId();
	
		if (mDataSetFile.exists() == false) {
			try {
				mDataSetFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new BackEndAPIException("blah", "blah blah");
			}
		}
		
		setConfigData(configData);
		
		saveToFile(DAFrame.getFrame().getBackEndAPICallBackAdapter(), null);
	}
	
	/**
	 * Constructor - creates a new fixed probe data set from the specified data file
	 * @param dataSetFile The data set's file
	 * @throws BackEndAPIException
	 */
	public MultiRunRunDataSet(File dataSetFile) throws Exception {
		super(dataSetFile, BackEndAPI.DST_MULTI_RUN_RUN);

		StringTokenizer st1 = new StringTokenizer(stripLastExtension(dataSetFile.getName()), DADefinitions.COORDINATE_SEPARATOR);
		
		if (st1.countTokens() < 2) {
			throw new Exception();
		}
		
		String[] tokens = new String[st1.countTokens()];
		for (int i = 0; i < tokens.length; ++i) {
			tokens[i] = st1.nextToken();
		}
		
		// Coordinates should be last two tokens
		mRunIndex = MAJFCTools.parseInt(tokens[tokens.length - 1]);
	}
	
	/**
	 * Creates a dataset (should be used rather than the constructor as it can be overridden)
	 * 
	 * @param parameters
	 * @return
	 * @throws BackEndAPIException
	 */
	@Override
	protected DataPoint createNewDataPoint(Hashtable<String, String> parameters) throws BackEndAPIException {
		return new DataPoint(parameters, this);
	}
	
	/**
	 * Creates a dataset (should be used rather than the constructor as it can be overridden)
	 * 
	 * @param yCoord
	 * @param zCoord
	 * @param theta
	 * @param phi
	 * @param alpha
	 * @param dataSet
	 * @return
	 */
	@Override
	public DataPoint createNewDataPoint(int yCoord, int zCoord, GenericImportDetails importDetails, int probeIndex, DataSet dataSet) {
		return new DataPoint(yCoord, zCoord, importDetails, probeIndex, (MultiRunRunDataSet) dataSet);
	}

	/**
	 * Creates a fixed probe dataset. This only exists to allow overloading by child classes - it should be called via 
	 * {@link DataSet#addFixedProbeDataSet(DataPointCoordinates)}
	 * @param coords
	 * @return
	 * @throws BackEndAPIException
	 */
	protected MultiRunFixedProbeDataSet makeFixedProbeDataSet(DataPointCoordinates coords) throws BackEndAPIException {
		return new MultiRunFixedProbeDataSet(mDataSetFile, coords, mConfigData);
	}

	/**
	 * Loads and creates the FixedProbeDataSets for the specified parent data set
	 * @param parentDataSetFile The parent data set's file
	 * @return A list of the FixedProbeDataSets for the parent DataSet
	 */
	public static Vector<MultiRunRunDataSet> loadFixedProbeDataSets(File parentDataSetFile) throws BackEndAPIException {
		Vector<MultiRunRunDataSet> fixedProbeDataSets = new Vector<MultiRunRunDataSet>();
		
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
				fixedProbeDataSets.add(new MultiRunRunDataSet(fileList[i]));
			} catch (Exception theException) {
				continue;
			}
		}
		
		return fixedProbeDataSets;
	}
	
	/*
	 * Gets the run index of this data set
	 * @return The run index
	 */
	public int getRunIndex() {
		return mRunIndex;
	}
	
	/**
	 * Makes the filename for a run dataset from the mean values dataset filename
	 * @param parentDataSetFile
	 * @param runIndex The run index
	 * @return
	 */
	protected String makeLinkedDataSetFilename(File meanValuesDataSetFile) {
		StringBuffer dataSetFilename = new StringBuffer();
		dataSetFilename.append(makeDirectoryName(meanValuesDataSetFile.getAbsolutePath()));
		dataSetFilename.append(MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR);
		dataSetFilename.append(DADefinitions.MULTI_RUN_FILENAME_INSERT);
		dataSetFilename.append(mRunIndex);
		dataSetFilename.append(DADefinitions.FILE_EXTENSION_DATA_SET);
		
		return dataSetFilename.toString();
	}

	@Override
	/**
	 * Data set saved successfully
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	protected void onSaved(BackEndCallbackInterface frontEndInterface) {
	}
}
