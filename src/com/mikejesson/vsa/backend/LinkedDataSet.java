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
import java.util.Hashtable;

import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndCallbackInterface;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetType;


public abstract class LinkedDataSet extends DataSet {
	protected LinkedDataSet(DataSetType dataSetType) {
		super(dataSetType);
	}

	protected LinkedDataSet(File dataSetFile, DataSetType dataSetType) throws BackEndAPIException {
		super(dataSetFile, dataSetType);
	}

	protected abstract String makeLinkedDataSetFilename(File parentFile);
	
	@Override
	/**
	 * Data set saved successfully
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	protected void onSaved(BackEndCallbackInterface frontEndInterface) {
	}

	/**
	 * Sets the parent data set filename - needed when saving the parent as a new data set
	 * @param parentDataSetFile
	 */
	public void setParentDataSetFile(File parentDataSetFile) {
		mDataSetFile = new File(makeLinkedDataSetFilename(parentDataSetFile));
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Sets the lookup of keys of missing data point summary fields - used when fields are calculated via the parent, and
	 * so the fields must be set as not missing in the children so they are calculated.
	 * @para, The lookup of keys to be set 
	 */
	public void setMissingDataPointSummaryFieldKeys(Hashtable<DataPointSummaryIndex, DataPointSummaryIndex> missingDataPointSummaryIndicesLookup) {
		synchronized (mDataLock) {
			mMissingDataPointSummaryIndicesLookup = (Hashtable<DataPointSummaryIndex, DataPointSummaryIndex>) missingDataPointSummaryIndicesLookup.clone();
		}
	}
}
