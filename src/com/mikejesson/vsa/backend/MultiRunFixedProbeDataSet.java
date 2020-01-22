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
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;

/**
 * @author MAJ727
 *
 */
public class MultiRunFixedProbeDataSet extends FixedProbeDataSet {
	/**
	 * Constructor - creates a new fixed probe data set for a fixed probe at the specified coordinates
	 * @param parentDataSetFile The parent data set's file
	 * @param coords The coordinates of the fixed probe
	 * @throws BackEndAPIException
	 */
	public MultiRunFixedProbeDataSet(File parentDataSetFile, DataPointCoordinates coords, DataSetConfig configData) throws BackEndAPIException {
		super(BackEndAPI.DST_MULTI_RUN_RUN_FIXED_PROBE, parentDataSetFile, coords, configData);

	}
	
	/**
	 * Constructor - creates a new fixed probe data set from the specified data file
	 * @param dataSetFile The data set's file
	 * @throws BackEndAPIException
	 */
	public MultiRunFixedProbeDataSet(File dataSetFile) throws Exception {
		super(BackEndAPI.DST_MULTI_RUN_RUN_FIXED_PROBE, dataSetFile);
	}
}
