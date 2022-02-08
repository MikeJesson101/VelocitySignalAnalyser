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

package com.mikejesson.vsa.backEndExposed;

import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCSafeArray;
import com.mikejesson.majfc.helpers.MAJFCSafeArrayWithKeys;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backend.CobraDataPointImporter;
import com.mikejesson.vsa.backend.DataSet;
import com.mikejesson.vsa.backend.GenericDataPointImporter;
import com.mikejesson.vsa.backend.LinkedDataSet;
import com.mikejesson.vsa.backend.MultiRunMeanValueDataSet;
import com.mikejesson.vsa.backend.NortekDataPointImporter;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.backend.GenericDataPointImporter.ImportCommand;
import com.mikejesson.vsa.backend.SolutionsForResearchDataPointImporter;
import com.mikejesson.vsa.backend.SontekDataPointImporter;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;


/**
 * @author MAJ727
 *
 */
public class BackEndAPI {
	private static int NUMBER_OF_DATA_SET_TYPES = 0;
	public static final DataSetType DST_SINGLE_PROBE = new DataSetType(NUMBER_OF_DATA_SET_TYPES++);
	public static final DataSetType DST_MULTI_PROBE = new DataSetType(NUMBER_OF_DATA_SET_TYPES++);
	public static final DataSetType DST_FIXED_PROBE = new DataSetType(NUMBER_OF_DATA_SET_TYPES++);
	public static final DataSetType DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE = new DataSetType(NUMBER_OF_DATA_SET_TYPES++);
	public static final DataSetType DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE = new DataSetType(NUMBER_OF_DATA_SET_TYPES++);
	public static final DataSetType DST_MULTI_RUN_RUN = new DataSetType(NUMBER_OF_DATA_SET_TYPES++);
	public static final DataSetType DST_MULTI_RUN_RUN_FIXED_PROBE = new DataSetType(NUMBER_OF_DATA_SET_TYPES++);

	public static DataSetType getDataSetType(String dataSetTypeString) {
		int dataSetTypeInt = Integer.parseInt(dataSetTypeString);
		
		if (dataSetTypeInt == DST_MULTI_PROBE.getIntIndex()) {
			return DST_MULTI_PROBE;
		} else if (dataSetTypeInt == DST_FIXED_PROBE.getIntIndex()) {
			return DST_FIXED_PROBE;
		} else if (dataSetTypeInt == BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE.getIntIndex()) {
			return BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE;
		} else if (dataSetTypeInt == BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE.getIntIndex()) {
			return BackEndAPI.DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE;
		} else if (dataSetTypeInt == BackEndAPI.DST_MULTI_RUN_RUN.getIntIndex()) {
			return BackEndAPI.DST_MULTI_RUN_RUN;
		} else if (dataSetTypeInt == BackEndAPI.DST_MULTI_RUN_RUN_FIXED_PROBE.getIntIndex()) {
			return BackEndAPI.DST_MULTI_RUN_RUN_FIXED_PROBE;
		}
		
		return DST_SINGLE_PROBE;
	}
	
	private static int NUMBER_OF_COMPONENT_IDENTIFIERS = 0;
	public static final CoordinateAxisIdentifer X_AXIS_OR_U_VELOCITY = new CoordinateAxisIdentifer(NUMBER_OF_COMPONENT_IDENTIFIERS++);
	public static final CoordinateAxisIdentifer Y_AXIS_OR_V_VELOCITY = new CoordinateAxisIdentifer(NUMBER_OF_COMPONENT_IDENTIFIERS++);
	public static final CoordinateAxisIdentifer Z_AXIS_OR_W_VELOCITY = new CoordinateAxisIdentifer(NUMBER_OF_COMPONENT_IDENTIFIERS++);
	
	private static int NUMBER_OF_DATA_POINT_DETAIL_KEYS = 0;
	public static final DataPointDetailIndex DPD_KEY_RAW_X_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_RAW_Y_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_RAW_Z_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_TRANSLATED_X_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_TRANSLATED_Y_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_TRANSLATED_Z_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_X_SIGNAL_CORRELATIONS = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_Y_SIGNAL_CORRELATIONS = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_Z_SIGNAL_CORRELATIONS = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_X_SIGNAL_SNRS = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_Y_SIGNAL_SNRS = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_Z_SIGNAL_SNRS = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_PRESSURE = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	public static final DataPointDetailIndex DPD_KEY_W_DIFF = new DataPointDetailIndex(NUMBER_OF_DATA_POINT_DETAIL_KEYS++);
	
	private static int NUMBER_OF_PROBE_DETAIL_KEYS = 0;
	public static final ProbeDetailIndex PD_KEY_PROBE_TYPE = new ProbeDetailIndex(NUMBER_OF_PROBE_DETAIL_KEYS++);
	public static final ProbeDetailIndex PD_KEY_PROBE_ID = new ProbeDetailIndex(NUMBER_OF_PROBE_DETAIL_KEYS++);
	public static final ProbeDetailIndex PD_KEY_SAMPLING_RATE = new ProbeDetailIndex(NUMBER_OF_PROBE_DETAIL_KEYS++);

	private static int NUMBER_OF_DATA_POINT_SUMMARY_KEYS = 0;
	public static final DataPointSummaryIndex DPS_KEY_U_MEAN_TRANSLATED_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_TRANSLATED_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_FILTERED_AND_TRANSLATED_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_PERCENTAGE_OF_U_VELOCITIES_GOOD = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_MEAN_TRANSLATED_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_TRANSLATED_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_FILTERED_AND_TRANSLATED_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_PERCENTAGE_OF_V_VELOCITIES_GOOD = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_W_MEAN_TRANSLATED_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_W_TRANSLATED_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_W_FILTERED_AND_TRANSLATED_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_PERCENTAGE_OF_W_VELOCITIES_GOOD = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_XZ_PLANE_ROTATION_THETA = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_YZ_PLANE_ROTATION_PHI = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_XY_PLANE_ROTATION_ALPHA = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_PRIME_V_PRIME_MEAN = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_PRIME_W_PRIME_MEAN = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_PRIME_W_PRIME_MEAN = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_RMS_U_PRIME = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_RMS_V_PRIME = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_RMS_W_PRIME = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex[][][] DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY = new DataPointSummaryIndex[][][]
	                           {	{ 	{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)} },
	                        	   	{ 	{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)} },
	                        	   	{ 	{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)} },
	                        	   	{ 	{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
	                        	   		{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)} },
	                           };
	public static final DataPointSummaryIndex[][] DPS_KEY_ANISOTROPIC_STRESS_TENSOR_ARRAY = new DataPointSummaryIndex[][]
            { 	{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
				{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)},
				{new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++), new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++)}
            };
	public static final DataPointSummaryIndex DPS_KEY_MAXIMUM_U = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);;
	public static final DataPointSummaryIndex DPS_KEY_MAXIMUM_V = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);;
	public static final DataPointSummaryIndex DPS_KEY_MAXIMUM_W = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);;
	public static final DataPointSummaryIndex DPS_KEY_TKE = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_U_TKE_FLUX = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_V_TKE_FLUX = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_W_TKE_FLUX = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_FIXED_PROBE_U_CORRELATION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_FIXED_PROBE_V_CORRELATION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_FIXED_PROBE_W_CORRELATION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_BATCH_NUMBER = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_BATCH_THETA_ROTATION_CORRECTION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_BATCH_ALPHA_ROTATION_CORRECTION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_BATCH_PHI_ROTATION_CORRECTION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_TRANSVERSE_TRANSFER_OF_STREAMWISE_MOMENTUM = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_MRS_SYNCHRONISATION_INDEX = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_X_MEAN_CORRELATION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_X_MEAN_SNR = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_Y_MEAN_CORRELATION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_Y_MEAN_SNR = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_Z_MEAN_CORRELATION = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	public static final DataPointSummaryIndex DPS_KEY_Z_MEAN_SNR = new DataPointSummaryIndex(NUMBER_OF_DATA_POINT_SUMMARY_KEYS++);
	
	public static final int NUMBER_OF_DIMENSIONS = 3;
	public static final int THIRD_ORDER_CORRELATION_DIMENSION = 4;

	private static int NUMBER_OF_DATA_SET_CONFIG_KEYS = 0;
	public static final DataSetConfigIndex DSC_KEY_CROSS_SECTIONAL_AREA = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_WETTED_PERIMETER = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_LENGTH_UNIT_SCALE_FACTOR = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_EXCLUDE_LEVEL = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_FLUID_DENSITY = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_FLUID_KINEMATIC_VISCOSITY = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_LEFT_BANK_POSITION = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_RIGHT_BANK_POSITION = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_WATER_DEPTH = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_MEASURED_DISCHARGE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_X_AXIS_INVERTED = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_Y_AXIS_INVERTED = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_Z_AXIS_INVERTED = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_BED_SLOPE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_DATA_SET_LOCKED = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_PRE_FILTER_TYPE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_DESPIKING_FILTER_TYPE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1 = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2 = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_PST_REPLACEMENT_METHOD = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_CS_TYPE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_SAMPLING_RATE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_LIMITING_CORRELATION = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_LIMITING_SNR = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_LIMITING_W_DIFF = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_DEFAULT_CELL_WIDTH = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_DEFAULT_CELL_HEIGHT = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_SYNCH_LIMITING_VALUE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_AT_TRIM_START_BY = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_AT_PRIOR_LENGTH = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_AT_SAMPLE_LENGTH = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_PSD_TYPE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_PSD_WINDOW = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_PSD_WELCH_WINDOW_OVERLAP = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_WAVELET_TYPE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_WAVELET_TRANSFORM_TYPE = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_USE_BINARY_FILE_FORMAT = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_MAIN_PROBE_INDEX = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_FIXED_PROBE_INDEX = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	public static final DataSetConfigIndex DSC_KEY_SYNCH_PROBE_INDEX = new DataSetConfigIndex(NUMBER_OF_DATA_SET_CONFIG_KEYS++);
	
	private static int NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS = 0;
	public static final DataSetConfigStringItemIndex DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY = new DataSetConfigStringItemIndex(NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS++);
	public static final DataSetConfigStringItemIndex DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY = new DataSetConfigStringItemIndex(NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS++);
	public static final DataSetConfigStringItemIndex DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY = new DataSetConfigStringItemIndex(NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS++);
	public static final DataSetConfigStringItemIndex DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME = new DataSetConfigStringItemIndex(NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS++);
	public static final DataSetConfigStringItemIndex DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT = new DataSetConfigStringItemIndex(NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS++);
	public static final DataSetConfigStringItemIndex DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER = new DataSetConfigStringItemIndex(NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS++);
	public static final DataSetConfigStringItemIndex DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR = new DataSetConfigStringItemIndex(NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS++);

	private static int NUMBER_OF_DESPIKING_FILTER_TYPES = 0;
	public static final DespikingFilterType DFT_NONE = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_EXCLUDE_LEVEL = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_VELOCITY_CORRELATION = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_PHASE_SPACE_THRESHOLDING = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_MODIFIED_PHASE_SPACE_THRESHOLDING = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_CORRELATION_AND_SNR = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_MOVING_AVERAGE = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_W_DIFF = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	public static final DespikingFilterType DFT_REMOVE_ZEROES = new DespikingFilterType(NUMBER_OF_DESPIKING_FILTER_TYPES++);
	
	private static int NUMBER_OF_WAVELET_TYPES = 0;
	public static final WaveletType WT_DAUB02 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB04 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB06 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB08 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB10 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB12 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB14 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB16 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB18 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_DAUB20 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_LEGE02 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_LEGE04 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_LEGE06 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_HAAR02_ORTHO = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	public static final WaveletType WT_COIF06 = new WaveletType(NUMBER_OF_WAVELET_TYPES++);
	
	private static int NUMBER_OF_WAVELET_TRANSFORM_TYPES = 0;
	public static final WaveletTransformType WTT_FWT = new WaveletTransformType(NUMBER_OF_WAVELET_TRANSFORM_TYPES++);
	public static final WaveletTransformType WTT_CWT = new WaveletTransformType(NUMBER_OF_WAVELET_TRANSFORM_TYPES++);
	public static final WaveletTransformType WTT_DFT = new WaveletTransformType(NUMBER_OF_WAVELET_TRANSFORM_TYPES++);

	private static int NUMBER_OF_PSD_TYPES = 0;
	public static final PSDType PSD_BARTLETT = new PSDType(NUMBER_OF_PSD_TYPES++);
	public static final PSDType PSD_WELCH = new PSDType(NUMBER_OF_PSD_TYPES++);

	private static int NUMBER_OF_EXCLUDE_LEVEL_SCALERS = 0;
	public static final CharacteristicScalerType CS_STANDARD_DEVIATION = new CharacteristicScalerType(NUMBER_OF_EXCLUDE_LEVEL_SCALERS++);
	public static final CharacteristicScalerType CS_MEAN_ABSOLUTE_DEVIATION = new CharacteristicScalerType(NUMBER_OF_EXCLUDE_LEVEL_SCALERS++);
	public static final CharacteristicScalerType CS_MEDIAN_ABSOLUTE_DEVIATION = new CharacteristicScalerType(NUMBER_OF_EXCLUDE_LEVEL_SCALERS++);

	private static int NUMBER_OF_CS_TYPES = 0;
	public static final PSTReplacementMethod CS_TYPE_STANDARD_DEVIATION = new PSTReplacementMethod(NUMBER_OF_CS_TYPES++);
	public static final PSTReplacementMethod CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION = new PSTReplacementMethod(NUMBER_OF_CS_TYPES++);
	public static final PSTReplacementMethod CS_TYPE_MEAN_ABSOLUTE_DEVIATION = new PSTReplacementMethod(NUMBER_OF_CS_TYPES++);

	private static int NUMBER_OF_PST_REPLACEMENT_METHODS = 0;
	public static final PSTReplacementMethod PRM_NONE = new PSTReplacementMethod(NUMBER_OF_PST_REPLACEMENT_METHODS++);
	public static final PSTReplacementMethod PRM_LINEAR_INTERPOLATION = new PSTReplacementMethod(NUMBER_OF_PST_REPLACEMENT_METHODS++);
	public static final PSTReplacementMethod PRM_LAST_GOOD_VALUE = new PSTReplacementMethod(NUMBER_OF_PST_REPLACEMENT_METHODS++);
	public static final PSTReplacementMethod PRM_12PP_INTERPOLATION = new PSTReplacementMethod(NUMBER_OF_PST_REPLACEMENT_METHODS++);
//	public static final PSTReplacementMethod DFT_PHASE_SPACE_THRESHOLDING = new PSTReplacementMethod(NUMBER_OF_PST_REPLACEMENT_METHODS++);
//	public static final PSTReplacementMethod DFT_MODIFIED_PHASE_SPACE_THRESHOLDING = new PSTReplacementMethod(NUMBER_OF_PST_REPLACEMENT_METHODS++);
	
	private static int NUMBER_OF_CROSS_SECTION_DATA_KEYS = 0;
	public static final CrossSectionDataIndex CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_U_VELOCITY = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_V_VELOCITY = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_W_VELOCITY = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_RMS_FTRC_U = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_RMS_FTRC_V = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_RMS_FTRC_W = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_MEAN_RMS_U_PRIME = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_MEAN_RMS_V_PRIME = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_MEAN_RMS_W_PRIME = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	public static final CrossSectionDataIndex CSD_KEY_MEAN_BOUNDARY_SHEAR = new CrossSectionDataIndex(NUMBER_OF_CROSS_SECTION_DATA_KEYS++);
	
	private static BackEndAPI sMe;
	private Hashtable<AbstractDataSetUniqueId, DataSet> mDataSetLookup = new Hashtable<AbstractDataSetUniqueId, DataSet>();
	private final GenericDataPointImporter mGenericDataPointImporter;
	private final NortekDataPointImporter mNortekDataPointImporter;
	private final SontekDataPointImporter mSontekDataPointImporter;
	private final CobraDataPointImporter mCobraDataPointImporter;
	private final SolutionsForResearchDataPointImporter mSolutionsForResearchDataPointImporter;

	/**
	 * Get the only instance of the API to the data storage
	 * 
	 * @return the instance or null if the API has not yet been created
	 */
	public static BackEndAPI getBackEndAPI(){
		if (sMe == null){
			sMe = new BackEndAPI();
		}
		
		return sMe;
	}
	
	/**
	 * Hidden constructor, accessed through getBackEndAPI
	 */
	private BackEndAPI() {
		mGenericDataPointImporter = new GenericDataPointImporter();
		mNortekDataPointImporter = new NortekDataPointImporter();
		mSontekDataPointImporter = new SontekDataPointImporter();
		mCobraDataPointImporter = new CobraDataPointImporter();
		mSolutionsForResearchDataPointImporter = new SolutionsForResearchDataPointImporter();
	}

	/**
	 * Opens the data set held in the specified file
	 * @param file the file containing the data set
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException
	 */
	public void openDataSet(File file, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException{
		try {
			DataSet openedDataSet = DataSet.openDataSetFromFile(file);
			
			if (mDataSetLookup.get(openedDataSet.getUniqueId()) != null) {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_DATA_SET_ALREADY_OPEN_TITLE), DAStrings.getString(DAStrings.ERROR_DATA_SET_ALREADY_OPEN_MSG));
			}
			
			mDataSetLookup.put(openedDataSet.getUniqueId(), openedDataSet);
			
			Vector<LinkedDataSet> linkedDataSets = openedDataSet.getAllLinkedDataSets();
			int numberOfLinkedDataSets = linkedDataSets.size();
			
			for (int i = 0 ; i < numberOfLinkedDataSets; ++i) {
				LinkedDataSet linkedDataSet = linkedDataSets.elementAt(i);
				mDataSetLookup.put(linkedDataSet.getUniqueId(), linkedDataSet);
			}
			
			frontEndInterface.onDataSetOpened(openedDataSet.getUniqueId(), file, openedDataSet.getConfigData());
		}
		catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_OPENING_DATA_SET_DIALOG_TITLE), DAStrings.getString(DAStrings.ERROR_OPENING_DATA_SET_DIALOG_MSG) + file.getAbsolutePath());
			}
		}
	}
	
	/**
	 * Saves the data set held to the specified file
	 * @param file The filename to save to
	 * @param frame The parent frame for any dialog thrown up
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException
	 */
	public void saveDataSet(final AbstractDataSetUniqueId uniqueId, final File file, final BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			if (dataSet != null) {
				dataSet.saveToFile(file, frontEndInterface, progressInterface);
			}
		}
		catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_SAVING_DATA_SET_DIALOG_TITLE), DAStrings.getString(DAStrings.ERROR_SAVING_DATA_SET_DIALOG_MSG) + file.getAbsolutePath());
			}
		}
	}

	/**
	 * Imports point data from a CSV file
	 * @param uniqueId The Id of the data set to import data point data for
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromCSVFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, char delimiter, char decimalSeparator, int mainProbeIndex, int fixedProbeIndex, int synchProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, delimiter, decimalSeparator, mainProbeIndex, fixedProbeIndex, synchProbeIndex), new ImportCommand() {
			
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mGenericDataPointImporter.importDataPointDataFromCSVFile(dataSet, importDetails);
			}
		});		
	}

	/**
	 * Imports single U measurements from a CSV file
	 * File format should be:
	 * x-coord[0],y-coord[0],U[0]
	 * x-coord[1],y-coord[1],U[1]
	 * 		.
	 * 		.
	 * 		.
	 * x-coord[n],y-coord[n],U[n]
	 * 
	 * @param uniqueId The Id of the data set to import data point data for
	 * @param file The file to import from
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
 	 * @param progressInterface The interface to report progress
	 */
	public void importSingleUMeasurementsFromCSV(AbstractDataSetUniqueId uniqueId, File file, char delimiter, char decimalSeparator, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			if (dataSet != null) {
				Vector<Integer> yCoords = new Vector<Integer>(100);
				Vector<Integer> zCoords = new Vector<Integer>(100);
				
				mGenericDataPointImporter.importSingleUMeasurementsFromCSV(file, yCoords, zCoords, dataSet, delimiter);
				int numberOfPointsAdded = yCoords.size();
				
				if (numberOfPointsAdded != zCoords.size()) {
					throw new Exception();
				}
				
				for (int i = 0; i < numberOfPointsAdded; ++i) {
					onDataPointAdded(dataSet, yCoords.elementAt(i), zCoords.elementAt(i), frontEndInterface);
				}
				
//	    		BackEndAPI.getBackEndAPI().recalculateSummaryData(uniqueId, null, DAFrame.getFrame(), progressInterface);
		    	DAFrame.getFrame().fileImportFinished(uniqueId);

			}
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE), DAStrings.getString(DAStrings.ERROR_IMPORTING_DATA_POINT_DIALOG_MSG) + file.getAbsolutePath());
			}
		}	
	}
	
	/**
	 * 
	 * @param dataSet The data set data has been added to
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	private void onDataPointAdded(DataSet dataSet, int yCoord, int zCoord, BackEndCallbackInterface frontEndInterface) {
		Vector<LinkedDataSet> linkedDataSets = dataSet.getAllLinkedDataSets();
		int numberOfLinkedDataSets = linkedDataSets.size();
		
		for (int i = 0 ; i < numberOfLinkedDataSets; ++i) {
			LinkedDataSet linkedDataSet = linkedDataSets.elementAt(i);
			mDataSetLookup.put(linkedDataSet.getUniqueId(), linkedDataSet);
		}
		
		if (dataSet.getDataSetType().equals(BackEndAPI.DST_MULTI_RUN_RUN) == false) {
			frontEndInterface.onDataPointAdded(dataSet.getUniqueId(), yCoord, zCoord);
		}
	}

	/**
	 * Imports point data from a Vector VEC file
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	private void importDataPointData(AbstractDataSetUniqueId uniqueId, BackEndCallbackInterface frontEndInterface, GenericImportDetails importDetails, ImportCommand importCommand) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			if (dataSet != null) {
				importCommand.execute(dataSet, importDetails);
				onDataPointAdded(dataSet, importDetails.getFirstYCoord(), importDetails.getFirstZCoord(), frontEndInterface);
			}
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE), DAStrings.getString(DAStrings.ERROR_IMPORTING_DATA_POINT_DIALOG_MSG) + importDetails.getFile().getAbsolutePath());
			}
		}	
	}	

	/**
	 * Imports point data from a Vectrino NDV file (.dat with accompanying .hdr)
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromNortekNDVFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mNortekDataPointImporter.importDataPointDataFromNDVFile(dataSet, importDetails);
			}
		});
	}	
	
	/**
	 * Imports point data from an unconverted Vectrino VNO file
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromNortekSingleProbeBinaryFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mNortekDataPointImporter.importDataPointDataFromNortekSingleProbeBinaryFile(dataSet, importDetails);
			}
		});	
	}	
	
	/**
	 * Imports point data from a Vectrino II MAT file
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromVectrinoIIMatFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mNortekDataPointImporter.importDataPointDataFromVectrinoIIMatFile(dataSet, importDetails);
			}
		});
	}

	/**
	 * Imports point data from a Vectrino II Raw file
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromVectrinoIIRawFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mNortekDataPointImporter.importDataPointDataFromVectrinoIIRawFile(dataSet, importDetails);
			}
		});
	}
	
	/**
	 * Imports point data from a Cobra thX file
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromCobraTHxFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mCobraDataPointImporter.importDataPointDataFromCobraTHxFile(0, dataSet, importDetails);
			}
		});
	}

	/**
	 * Imports point data from a converted PolySync file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
	 * @param fixedProbeIndex The index (starting at 0 for the first probe) of the fixed probe, or negative if there is no fixed probe (coords must be of length > 2 if this is non-negative)
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importDataPointDataFromConvertedPolySyncFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, int mainProbeIndex, int fixedProbeIndex, int synchProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', mainProbeIndex, fixedProbeIndex, synchProbeIndex), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mNortekDataPointImporter.importDataPointDataFromConvertedPolySyncFile(dataSet, importDetails);
				
				return null;
			}
		});
	}	

	/**
	 * Imports point data from a PolySync VNO file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
	 * @param mainProbeIndex The index (starting at 0 for the first probe) of the main probe. This is the probe whose coordinates are used to identify the fixed probe within its data set
	 * @param fixedProbeIndex The index (starting at 0 for the first probe) of the fixed probe, or negative if there is no fixed probe (coords must be of length > 2 if this is non-negative)
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importDataPointDataFromPolySyncVNOFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, int mainProbeIndex, int fixedProbeIndex, int synchProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', mainProbeIndex, fixedProbeIndex, synchProbeIndex), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mNortekDataPointImporter.importDataPointDataFromPolySyncVNOFile(dataSet, importDetails);
			}
		});
	}		

	/**
	 * Imports point data from an unconverted Sontek ADV file
	 * 
	 * @param file The file to import from
	 * @param yCoord The y-coordinate of the data point
	 * @param zCoord The z-coordinate of the data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromSontekSingleProbeBinaryFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mSontekDataPointImporter.importDataPointDataFromSontekSingleProbeBinaryFile(dataSet, importDetails);
			}
		});	
	}
	
	/**
	 * Imports point data from a UoB DPMS file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void importDataPointDataFromUoBDPMSFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, int mainProbeIndex, int fixedProbeIndex, int synchProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', mainProbeIndex, fixedProbeIndex, synchProbeIndex), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				return mSolutionsForResearchDataPointImporter.importDataPointDataFromUoBDPMSFile(dataSet, importDetails);
			}
		});
	}	
	
	/**
	 * Imports multi-run point data from a file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	private void importMultiRunDataPointData(AbstractDataSetUniqueId uniqueId, BackEndCallbackInterface frontEndInterface, GenericImportDetails importDetails, ImportCommand importCommand) throws BackEndAPIException {
		try {
			MultiRunMeanValueDataSet dataSet = (MultiRunMeanValueDataSet) mDataSetLookup.get(uniqueId);

			importCommand.execute(dataSet, importDetails);
			
			Vector<LinkedDataSet> runDataSets = dataSet.getRunDataSets();
			int numberOfRuns = dataSet.getNumberOfRuns();
			
			for (int i = 0; i < numberOfRuns; ++i) {
				onDataPointAdded(runDataSets.elementAt(i), importDetails.getFirstYCoord(), importDetails.getFirstYCoord(), frontEndInterface);
			}
			
			onDataPointAdded(dataSet, importDetails.getFirstYCoord(), importDetails.getFirstYCoord(), frontEndInterface);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE), DAStrings.getString(DAStrings.ERROR_IMPORTING_DATA_POINT_DIALOG_MSG) + importDetails.getFile().getAbsolutePath());
			}
		}	
	}	
	
	/**
	 * Imports multi-run point data from a PolySync VNO file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromCSVFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, char delimiter, int mainProbeIndex, int fixedProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, delimiter, mainProbeIndex, fixedProbeIndex), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mGenericDataPointImporter.importMultiRunDataPointDataFromCSVFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}	
	
	/**
	 * Imports multi-run point data from a PolySync VNO file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromNortekSingleProbeBinaryFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mNortekDataPointImporter.importMultiRunDataPointDataFromNortekSingleProbeBinaryFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}	
	
	/**
	 * Imports multi-run point data from a Cobra THx file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromCobraTHxFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, int mainProbeIndex, int fixedProbeIndex, int synchProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', mainProbeIndex, fixedProbeIndex, synchProbeIndex), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mCobraDataPointImporter.importMultiRunDataPointDataFromCobraTHxFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}
	
	/**
	 * Imports multi-run point data from a PolySync VNO file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromPolySyncVNOFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, int mainProbeIndex, int fixedProbeIndex, int synchProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', mainProbeIndex, fixedProbeIndex, synchProbeIndex), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mNortekDataPointImporter.importMultiRunDataPointDataFromPolySyncVNOFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}	
	
	/**
	 * Imports multi-run point data from a Vectrino II mat file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromVectrinoIIMatFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mNortekDataPointImporter.importMultiRunDataPointDataFromVectrinoIIMatFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}
	
	/**
	 * Imports multi-run point data from a Vectrino II raw file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromVectrinoIIRawFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mNortekDataPointImporter.importMultiRunDataPointDataFromVectrinoIIRawFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}
	
	/**
	 * Imports multi-run point data from a Sontek ADV file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromSontekSingleProbeBinaryFile(AbstractDataSetUniqueId uniqueId, File file, int yCoord, int zCoord, double theta, double phi, double alpha, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		LinkedList<DataPointCoordinates> coordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
		coordsList.add(new DataPointCoordinates(yCoord, zCoord));
		
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, ',', 0, -1), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mSontekDataPointImporter.importMultiRunDataPointDataFromSontekSingleProbeBinaryFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}	
	
	/**
	 * Imports multi-run point data from a UoB DPMS file
	 * 
	 * @param file The file to import from
	 * @param coordsList The (y-coordinate, z-coordinates) of the data points in this VNO file
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param delimiter The delimiter used in the file
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException if fixedProbeIndex >= coords.length or coords.length does not equal the number of probes in the data file or any other exception!
	 */
	public void importMultiRunDataPointDataFromUoBDPMSFile(AbstractDataSetUniqueId uniqueId, File file, LinkedList<DataPointCoordinates> coordsList, double theta, double phi, double alpha, char delimiter, int mainProbeIndex, int fixedProbeIndex, int synchProbeIndex, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		importMultiRunDataPointData(uniqueId, frontEndInterface, mGenericDataPointImporter.new GenericImportDetails(file, coordsList, theta, phi, alpha, delimiter, mainProbeIndex, fixedProbeIndex, synchProbeIndex), new ImportCommand() {
			@Override
			public Vector<DataPointCoordinates> execute(DataSet dataSet, GenericImportDetails importDetails) throws BackEndAPIException {
				mSolutionsForResearchDataPointImporter.importMultiRunDataPointDataFromUoBDPMSFile((MultiRunMeanValueDataSet) dataSet, importDetails);
				
				return null;
			}
		});
	}	
	
	/**
	 * Gets the specified field for the specified data point from the point's summary data
	 * @param yCoord The y-coordinate of the point
	 * @param zCoord The z-coordinate of the point
	 * @param field The field identifier (one of the BackEndAPI.DPS_KEY_... keys)
	 * @return The value for the specified field for the specified data point
	 * @throws BackEndAPIException
	 */
	public double getDataPointSummaryDataFieldAtPoint(AbstractDataSetUniqueId uniqueId, int yCoord, int zCoord, DataPointSummaryIndex field) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			return dataSet.getSummaryDataField(yCoord, zCoord, field);
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_POINT_DATA_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_POINT_DATA_MSG) + yCoord + '-' + zCoord);
		}
	}
	
	/**
	 * Gets the specified field for the specified data point from the point's probe details
	 * @param yCoord The y-coordinate of the point
	 * @param zCoord The z-coordinate of the point
	 * @param field The field identifier (one of the BackEndAPI.DPS_KEY_... keys)
	 * @return The value for the specified field for the specified data point
	 * @throws BackEndAPIException
	 */
	public String getDataPointProbeDetailsFieldAtPoint(AbstractDataSetUniqueId uniqueId, int yCoord, int zCoord, ProbeDetailIndex field) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			return dataSet.getProbeDetailsDataField(yCoord, zCoord, field);
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_POINT_DATA_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_POINT_DATA_MSG) + yCoord + '-' + zCoord);
		}
	}
	
	/**
	 * Gets the specified cross-section data field
	 * @param field The field identifier
	 * @return The value for the specified field
	 */
	public double getCrossSectionDataField(AbstractDataSetUniqueId uniqueId, CrossSectionDataIndex field) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);

			return dataSet.getCrossSectionDataField(field);
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_CROSS_SECTION_DATA_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_CROSS_SECTION_DATA_MSG));
		}
	}
	
	/**
	 * Gets the coordinates of all data points in this data set, ordered by y-coordinate then z-coordinate
	 * @return the coordinates as a nx2 array {{x1, y1}, {x2, y2}, ...}
	 */
	public Integer[][] getUnsortedDataPointCoordinates(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			return dataSet.getUnsortedDataPointCoordinates();
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_MSG) + uniqueId.getFullDisplayString());
		}
	}
	
	/**
	 * Gets the sorted coordinates of all data points in this data set. The z-coordinate vector contains vectors of z-coordinates
	 * for each point at the y-coordinate in the corresponding position of the y-coordinate vector.
	 * @param sortedYCoords The vector to copy the sorted X coordinates into
	 * @param sortedZCoords The vector of vectors to copy the sorted Y coordinates into
	 */
	public void getSortedDataPointCoordinates(AbstractDataSetUniqueId uniqueId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoords) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			dataSet.getSortedDataPointCoordinates(sortedYCoords, sortedZCoords);
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG));
		}
	}
	
	/**
	 * Gets the sorted coordinates of all data points in this data set in the form of a hashtable indexed by the y-coordinate,
	 * containing vectors of z-coordinates for which points exist at that y-coordinate
	 * return sortedCoords The sorted coordinates
	 */
	public Hashtable<Integer, Vector<Integer>> getYCoordIndexedSortedDataPointCoordinates(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			return dataSet.getYCoordIndexedSortedDataPointCoordinates();
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG));
		}
	}

	/**
	 * Gets the sorted coordinates of all data points in this data set in the form of a hashtable indexed by the z-coordinate,
	 * containing vectors of y-coordinates for which points exist at that z-coordinate
	 * return sortedCoords The sorted coordinates
	 */
	public Hashtable<Integer, Vector<Integer>> getZCoordIndexedSortedDataPointCoordinates(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			return dataSet.getZCoordIndexedSortedDataPointCoordinates();
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG));
		}
	}

	/**
	 * Gets a list of all the z-coordinates, sorted into order
	 * @param uniqueId The id of the data set to get the coordinates for
	 * @return The sorted coordinates
	 */
	public Vector<Integer> getSortedListOfAllZCoords(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			
			return dataSet.getSortedListOfAllZCoords();
		} catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG));
		}
	}

	/**
	 * Clears all point data to make way for a new data set (everything else is done by saveDataSet)
	 * @param configData The configuration data for this new data set 
	 * @param file The file in which to store the new data set
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @see com.mikejesson.vsa.backEndExposed.BackEndAPI#saveDataSet
	 * @throws BackEndAPIException
	 */
	public void createNewDataSet(DataSetType dataSetType, DataSetConfig configData, File file, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		try {
			// If this file already exists, we are overwriting it so delete everything
			if (file.exists()) {
				File dataSetDir = new File(DataSet.makeDirectoryName(file.getAbsolutePath()));
				MAJFCTools.deleteDirectoryTree(dataSetDir);
				
				// Replace this file with a blank one (is there better way to do this?)
				file.delete();
				file.createNewFile();
			}
			
			DataSet newDataSet;
			
			if (dataSetType.equals(DST_MULTI_RUN_MEAN_VALUE_SINGLE_PROBE) || dataSetType.equals(DST_MULTI_RUN_MEAN_VALUE_MULTI_PROBE)) {
				newDataSet = new MultiRunMeanValueDataSet(file, configData, dataSetType, 10);
			} else {
				newDataSet = new DataSet(file, configData, dataSetType);
			}
			
			mDataSetLookup.put(newDataSet.getUniqueId(), newDataSet);
			
			frontEndInterface.onNewDataSetCreated(newDataSet.getUniqueId(), file);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_CREATING_NEW_DATA_SET_TITLE), DAStrings.getString(DAStrings.ERROR_CREATING_NEW_DATA_SET_MSG) + file.getAbsolutePath());
			}
		}	
	}
	
	/**
	 * Loads the detailed data for the data points specified
	 * @param requestId The id of this load request
	 * @param uniqueId The unique id of the data set to load from
	 * @param yCoords Data point y-coordinates
	 * @param zCoords Data point z-coordinates
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void loadDataPointDetails(int requestId, AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		int numberOfDataPoints = yCoords.size();
		Vector<DataPointSummary> dataPointSummaries = new Vector<DataPointSummary>(numberOfDataPoints);
		Vector<DataPointDetail> dataPointDetails = new Vector<DataPointDetail>(numberOfDataPoints);

		for (int i = 0; i < numberOfDataPoints; ++i) {
			dataPointSummaries.add(new DataPointSummary());
			dataPointDetails.add(new DataPointDetail());
			loadDataPointDetails(uniqueId, yCoords.elementAt(i), zCoords.elementAt(i), dataPointSummaries.elementAt(i), dataPointDetails.elementAt(i));
		}
		
		frontEndInterface.onDataPointDetailsLoaded(requestId, uniqueId, yCoords, zCoords, dataPointSummaries, dataPointDetails);
	}
		
	
	/**
	 * Loads the detailed data for the data point specified
	 * @param requestId The id of this load request
	 * @param uniqueId The unique id of the data set to load from
	 * @param yCoord Data point y-coordinate
	 * @param zCoord Data point z-coordinate
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void loadDataPointDetails(int requestId, AbstractDataSetUniqueId uniqueId, int yCoord, int zCoord, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		Vector<Integer> yCoords = new Vector<Integer>(1);
		Vector<Integer> zCoords = new Vector<Integer>(1);
		Vector<DataPointSummary> dataPointSummaries = new Vector<DataPointSummary>(1);
		Vector<DataPointDetail> dataPointDetails = new Vector<DataPointDetail>(1);

		yCoords.add(yCoord);
		zCoords.add(zCoord);
		dataPointSummaries.add(new DataPointSummary());
		dataPointDetails.add(new DataPointDetail());
		
		loadDataPointDetails(uniqueId, yCoord, zCoord, dataPointSummaries.firstElement(), dataPointDetails.firstElement());
	
		frontEndInterface.onDataPointDetailsLoaded(requestId, uniqueId, yCoords, zCoords, dataPointSummaries, dataPointDetails);
	}
	
	/**
	 * Loads the data for the specified data point
	 * @param uniqueId The id of the data set to load data from
	 * @param yCoord Data point y-coordinate
	 * @param zCoord Data point x-coordinate
	 * @param dataPointSummary The DataPointSummary object to fill
	 * @param dataPointDetail The DataPointDetail object to fill
	 * @throws BackEndAPIException
	 */
	private void loadDataPointDetails(AbstractDataSetUniqueId uniqueId, int yCoord, int zCoord, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail) throws BackEndAPIException {
		try{
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			dataSet.loadDataPointDetails(yCoord, zCoord, dataPointSummary, dataPointDetail);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_LOADING_DATA_POINT_DETAILS_TITLE), DAStrings.getString(DAStrings.ERROR_LOADING_DATA_POINT_DETAILS_MSG) + yCoord + '-' + zCoord);
			}
		}	
	}

	/**
	 * Loads the detailed data for the data point specified and its linked datapoints
	 * @param requestId The id of this load request
	 * @param uniqueId The unique id of the data set to load from
	 * @param yCoord Data point y-coordinate
	 * @param zCoord Data point z-coordinate
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void loadDataPointDetailsWithLinkedDPs(int requestId, AbstractDataSetUniqueId uniqueId, int yCoord, int zCoord, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		Vector<AbstractDataSetUniqueId> dataSetIds = new Vector<AbstractDataSetUniqueId>(20);
		dataSetIds.add(uniqueId);
		dataSetIds.addAll(getLinkedDataSetIds(uniqueId));
		int numberOfLinkedDataSets = dataSetIds.size();				
		int numberOfDataPoints = numberOfLinkedDataSets + 1;
		Vector<DataPointSummary> dataPointSummaries = new Vector<DataPointSummary>(numberOfDataPoints);
		Vector<DataPointDetail> dataPointDetails = new Vector<DataPointDetail>(numberOfDataPoints);
		Vector<DataSetType> dataSetTypes = new Vector<DataSetType>(numberOfDataPoints);

		for (int i = 0; i < numberOfLinkedDataSets; ++i) {
			DataPointSummary dps = new DataPointSummary();
			DataPointDetail dpd = new DataPointDetail();
			
			try {
				loadDataPointDetails(dataSetIds.elementAt(i), yCoord, zCoord, dps, dpd);
				dataSetTypes.add(getDataSetType(dataSetIds.elementAt(i)));
			} catch (Exception e) {
				continue;
			}
			
			dataPointSummaries.add(dps);
			dataPointDetails.add(dpd);
		}
		
		frontEndInterface.onDataPointDetailsWithLinkedDPsLoaded(requestId, dataSetIds, dataSetTypes, yCoord, zCoord, dataPointSummaries, dataPointDetails);
	}
	
	/**
	 * Clears the detailed data for the data point specified.
	 * Used to control heap use
	 * @param yCoord Data point y-coordinate
	 * @param zCoord Data point z-coordinate
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void clearDataPointDetails(AbstractDataSetUniqueId uniqueId, int yCoord, int zCoord, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		try{
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			dataSet.clearDataPointDetails(yCoord, zCoord);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_CLEARING_DATA_POINT_DETAILS_TITLE), DAStrings.getString(DAStrings.ERROR_CLEARING_DATA_POINT_DETAILS_MSG) + yCoord + '-' + zCoord);
			}
		}	
		
		frontEndInterface.onDataPointDetailsCleared(uniqueId, yCoord, zCoord);
	}
	
	/**
	 * Gets the ids of the linked (fixed probe or run) data sets for the specified data set
	 * @param uniqueId The id of the data set to get the linked data sets for
	 * @return The ids of the linked data sets for the specified data set
	 * @throws BackEndAPIException
	 */
	public Vector<AbstractDataSetUniqueId> getLinkedDataSetIds(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try{
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			return dataSet.getAllLinkedDataSetIds();
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_MSG) + uniqueId.getFullDisplayString());
			}
		}
	}
	
	/**
	 * Closes the specified data set
	 * @param uniqueId The id of the data set to close
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 * @throws BackEndAPIException
	 */
	public void closeDataSet(AbstractDataSetUniqueId uniqueId, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		try{
			DataSet closedDataSet = mDataSetLookup.remove(uniqueId);
			
			Vector<AbstractDataSetUniqueId> linkedDataSetIds = closedDataSet.getAllLinkedDataSetIds();
			int numberOfLinkedDataSets = linkedDataSetIds.size();
			
			for (int i = 0; i < numberOfLinkedDataSets; ++i) {
				mDataSetLookup.remove(linkedDataSetIds.elementAt(i));
			}
			
			frontEndInterface.onDataSetClosed(uniqueId);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_CLOSING_DATA_SET_TITLE), DAStrings.getString(DAStrings.ERROR_CLOSING_DATA_SET_MSG) + uniqueId.getFullDisplayString());
			}
		}
	}

	/**
	 * Gets the number of DPD_KEY_...s
	 * @return The number of DPD_KEY_..s
	 */
	public static int getNumberOfDataPointDetailKeys() {
		return NUMBER_OF_DATA_POINT_DETAIL_KEYS;
	}
	
	/**
	 * @return The number of DPS_KEY_...s
	 */
	public static int getNumberOfDataPointSummaryKeys() {
		return NUMBER_OF_DATA_POINT_SUMMARY_KEYS;
	}
	
	/**
	 * @return The number of DSC_KEY_...s
	 */
	public static int getNumberOfDataSetConfigKeys() {
		return NUMBER_OF_DATA_SET_CONFIG_KEYS;
	}
	
	/**
	 * @return The number of DSC_KEY_FOR_STRING_ITEM_...s
	 */
	public static int getNumberOfDataSetConfigStringItemKeys() {
		return NUMBER_OF_DATA_SET_CONFIG_STRING_ITEM_KEYS;
	}
	
	/**
	 * @return The number of CSD_KEY_...s
	 */
	public static int getNumberOfCrossSectionDataKeys() {
		return NUMBER_OF_CROSS_SECTION_DATA_KEYS;
	}
	
	/**
	 * Get the configuration data
	 * @return The configuration data
	 */
	public DataSetConfig getConfigData(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			return dataSet.getConfigData();
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_CONFIG_DATA_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_CONFIG_DATA_MSG));
			}
		}	
	}
	
	/**
	 * Set the configuration data specified by the key.
	 * NOTE: This does not force recalculation of summary data - recalculateSummaryData should be called once
	 * all configuration data has been set
	 * @param configData The new configuration data
	 */
	public void setConfigData(AbstractDataSetUniqueId uniqueId, DataSetConfig configData) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			dataSet.setConfigData(configData);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_SETTING_CONFIG_DATA_TITLE), DAStrings.getString(DAStrings.ERROR_SETTING_CONFIG_DATA_MSG));
			}
		}		
	}
	
	/**
	 * Recalculates summary data (should be used when configuration data is changed
	 * Loads all data point data for the current data set in order to recalculate
	 * summary data
	 * @param uniqueId The id of the data set to process
	 * @param dataFieldsChangedFlages A set of flags (indexed by the BackEndAPI.DSC_KEY_... keys) indicating which
	 * configuration data has changed
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
 	 * @param progressInterface The interface to report progress
	 */
	public void recalculateSummaryData(AbstractDataSetUniqueId uniqueId, DataSetConfigChangedFlags dataFieldsChangedFlags, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			dataSet.recalculateSummaryData(dataFieldsChangedFlags, false, frontEndInterface, progressInterface);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_RECALCULATING_SUMMARY_DATA_TITLE), DAStrings.getString(DAStrings.ERROR_RECALCULATING_SUMMARY_DATA_MSG));
			}
		}		
	}

	/**
	 * All data importing from files is complete
	 */
	public void importFromFilesComplete(AbstractDataSetUniqueId uniqueId, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			dataSet.importFromFileComplete();
			dataSet.recalculateSummaryData(null, false, frontEndInterface, progressInterface);
		} catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_TITLE), DAStrings.getString(DAStrings.ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_MSG));
			}
		}		
	}
	
	/**
	 * Is the data set locked?
	 * @param uniqueId The id of the data set to process
	 * @return True if it is
	 */
	public boolean dataSetIsLocked(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			DataSetType dataSetType = dataSet.getDataSetType();
			return dataSet.getConfigData().get(DSC_KEY_DATA_SET_LOCKED) == 1.0 || dataSetType.equals(DST_FIXED_PROBE) || dataSetType.equals(DST_MULTI_RUN_RUN) || dataSetType.equals(BackEndAPI.DST_MULTI_RUN_RUN_FIXED_PROBE);
		}
		catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_MSG));
			}
		}		
	}
	
	/**
	 * Is the data set for a single probe?
	 * @param uniqueId The id of the data set to process
	 * @return True if it is
	 */
	public boolean dataSetIsForSingleProbe(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			return dataSet.getConfigData().get(DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET) == 1.0;
		}
		catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_MSG));
			}
		}		
	}
	
	/**
	 * Is the data set for multiple probes?
	 * @param uniqueId The id of the data set to process
	 * @return True if it is
	 */
	public boolean dataSetIsForMultipleProbes(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			return dataSet.getConfigData().get(DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET) > 1.0;
		}
		catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_MSG));
			}
		}
	}
	
	/**
	 * Gets the data set type (@see {@link BackEndAPI#DST_SINGLE_PROBE})
	 * @param uniqueId The id of the data set to process
	 * @return The date set type
	 */
	public DataSetType getDataSetType(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		try {
			DataSet dataSet = mDataSetLookup.get(uniqueId);
			return dataSet.getDataSetType();
		}
		catch (Exception theException){
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				throw (BackEndAPIException) theException;
			} else {
				throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_TITLE), DAStrings.getString(DAStrings.ERROR_GETTING_DATA_SET_INFO_MSG));
			}
		}
	}
	
	/**
	 * Inner class to hold the summary data
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataPointSummary extends MAJFCSafeArrayWithKeys<Double, DataPointSummaryIndex> {
		public DataPointSummary() {
			super(new Double[getNumberOfDataPointSummaryKeys()]);
			
			for (int i = 0; i < mObjects.length; ++i) {
				mObjects[i] = Double.NaN;
			}
		}
		
		/**
		 * @see MAJFCSafeArrayWithKeys#makeKey
		 */
		@Override
		protected DataPointSummaryIndex makeKey(int index) {
			return new DataPointSummaryIndex(index);
		}
	}
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataPointSummaryIndex extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private DataPointSummaryIndex(int index) {
			super(index);
		}
	}		
	
	/**
	 * Inner class to hold the detail data
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataPointDetail extends MAJFCSafeArrayWithKeys<Vector<Double>, DataPointDetailIndex> {
		@SuppressWarnings("unchecked")
		public DataPointDetail() {
			super(new Vector[getNumberOfDataPointDetailKeys()]);
			
			for (int i = 0; i < mObjects.length; ++i) {
				mObjects[i] = new Vector<Double>();
			}
		}
		
		/**
		 * @see MAJFCSafeArrayWithKeys#makeKey
		 */
		@Override
		protected DataPointDetailIndex makeKey(int index) {
			return new DataPointDetailIndex(index);
		}
	}
		
	/**
	 * Inner class to hold the probe detail data
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class ProbeDetail extends MAJFCSafeArrayWithKeys<String, ProbeDetailIndex> {
		public ProbeDetail() {
			super(new String[getNumberOfProbeDetailKeys()]);
			
			for (int i = 0; i < mObjects.length; ++i) {
				mObjects[i] = "";
			}
		}
		
		/**
		 * @see MAJFCSafeArrayWithKeys#makeKey
		 */
		@Override
		protected ProbeDetailIndex makeKey(int index) {
			return new ProbeDetailIndex(index);
		}
	}

	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class ProbeDetailIndex extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private ProbeDetailIndex(int index) {
			super(index);
		}
	}		
	
	/**
	 * Gets the number of PD_KEY_...s
	 * @return The number of PD_KEY_..s
	 */
	public static int getNumberOfProbeDetailKeys() {
		return NUMBER_OF_PROBE_DETAIL_KEYS;
	}
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataSetType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private DataSetType(int index) {
			super(index);
		}
	}		

	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class CoordinateAxisIdentifer extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private CoordinateAxisIdentifer(int index) {
			super(index);
		}
	}		

	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataPointDetailIndex extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private DataPointDetailIndex(int index) {
			super(index);
		}
	}		
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class CrossSectionDataIndex extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private CrossSectionDataIndex(int index) {
			super(index);
		}
	}		
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataSetConfigIndex extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		DataSetConfigIndex(int index) {
			super(index);
		}
	}		
	
	/**
	 * Inner class
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataSetConfigStringItemIndex extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		DataSetConfigStringItemIndex(int index) {
			super(index);
		}
	}		
	
	/**
	 * Inner class to hold the data set configuration details
	 * 
	 * @author MAJ727
	 * 
	 */
	public static class DataSetConfigChangedFlags extends MAJFCSafeArray<Boolean, DataSetConfigIndex> {
		private DataSetConfigChangedFlags mStringConfigItems;

		public DataSetConfigChangedFlags() {
			this(getNumberOfDataSetConfigKeys(), true);
		}
		
		private DataSetConfigChangedFlags(int numberOfKeys, boolean createStringItems) {
			super(new Boolean[getNumberOfDataSetConfigKeys()]);
			
			if (createStringItems) {
				mStringConfigItems = new DataSetConfigChangedFlags(getNumberOfDataSetConfigStringItemKeys(), false);
			} else {
				mStringConfigItems = null;
			}
			
			reset();
		}

		public void reset() {
			for (int i = 0; i < mObjects.length; ++i) {
				mObjects[i] = false;
			}
			
			if (mStringConfigItems != null) {
				mStringConfigItems.reset();
			}
		}

		public boolean changeHasOccured() {
			for (int i = 0; i < mObjects.length; ++i) {
				if (mObjects[i] == true) {
					return true;
				}
			}
			
			return mStringConfigItems != null && mStringConfigItems.changeHasOccured();
		}
		
		public void set(DataSetConfigStringItemIndex index, boolean changed) {
			mStringConfigItems.set(new DataSetConfigIndex(index.getIntIndex()), changed);
		}
	}
	
	/**
	 * Data set saved successfully. This should NOT be called by users of this package.
	 * @param uniqueId The unique id of the saved data set
	 * @param oldUniqueId The old (pre-save) id of the saved data set
	 * @param dataSet The saved data set
	 * @param dataSetFile The file of the data set
 	 * @param frontEndInterface an instance of the callback interface used by the BackEndAPI to communicate with the front end
	 */
	public void onDataSetSaved(AbstractDataSetUniqueId uniqueId, AbstractDataSetUniqueId oldUniqueId, DataSet dataSet, File dataSetFile, BackEndCallbackInterface frontEndInterface) {
		mDataSetLookup.remove(oldUniqueId);
		mDataSetLookup.put(uniqueId, dataSet);
		
		frontEndInterface.onDataSetSaved(uniqueId, oldUniqueId, dataSetFile);
	}	
	
	/**
	 * Inner class
	 * 
	 * @author mikefedora
	 *
	 */
	public static class DespikingFilterType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private DespikingFilterType(int index) {
			super(index);
		}
	}
	
	/**
	 * Inner class
	 * 
	 * @author mikefedora
	 *
	 */
	public static class WaveletType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private WaveletType(int index) {
			super(index);
		}
	}
	
	/**
	 * Inner class
	 * 
	 * @author mikefedora
	 *
	 */
	public static class WaveletTransformType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private WaveletTransformType(int index) {
			super(index);
		}
	}

	/**
	 * Inner class
	 * 
	 * @author mikefedora
	 *
	 */
	public static class PSDType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private PSDType(int index) {
			super(index);
		}
	}
	
	/**
	 * Inner class
	 * 
	 * @author mikefedora
	 *
	 */
	public static class PSTReplacementMethod extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private int mPolynomialOrder = 3;
		
		private PSTReplacementMethod(int index) {
			super(index);
		}
		
		public void setPolynomialOrder(int polynomialOrder) {
			mPolynomialOrder = polynomialOrder;
		}
		
		public int getPolynomialOrder() {
			return mPolynomialOrder;
		}
	}
	
	/**
	 * Inner class
	 * 
	 * @author mikefedora
	 *
	 */
	public static class CharacteristicScalerType extends MAJFCSafeArray.MAJFCSafeArrayIndex {
		private CharacteristicScalerType(int index) {
			super(index);
		}
	}
	
	/**
	 * Inner class to hold data point coordinates
	 * @author MAJ727
	 *
	 */
	public static class DataPointCoordinates implements Comparable<DataPointCoordinates> {
		private final int mY;
		private final int mZ;
		
		/**
		 * Constructor
		 * @param yCoord The y-coordinate
		 * @param zCoord The z-coordinate
		 */
		public DataPointCoordinates(int yCoord, int zCoord) {
			mY = yCoord;
			mZ = zCoord;
		}

		/**
		 * @return The y-coordinate
		 */
		public int getY() {
			return mY;
		}
		
		/**
		 * @return The z-coordinate
		 */
		public int getZ() {
			return mZ;
		}
		
		@Override
		public boolean equals(Object theOtherOne) {
			if (theOtherOne == null) {
				return false;
			}
			
			if (theOtherOne == this) {
				return true;
			}
			
			if (theOtherOne.getClass() != this.getClass()) {
				return false;
			}
			
			DataPointCoordinates otherOne = (DataPointCoordinates) theOtherOne;
			
			return mY == otherOne.mY && mZ == otherOne.mZ; 
		}
		
		@Override
		/**
		 * Gets the hashcode
		 */
		public int hashCode() {
			int hash = 7 * mY;
			hash = 7 * hash + mZ;
			
			return hash;
		}
		
		@Override
		/**
		 * Get string representation of this object
		 * @return The string representation (<y-Coordinate>-<z-Coordinate>)
		 */
		public String toString() {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append(mY);
			stringBuffer.append(DADefinitions.COORDINATE_SEPARATOR);
			stringBuffer.append(mZ);
			
			return stringBuffer.toString();
		}

		@Override
		public int compareTo(DataPointCoordinates theOtherOne) {
			int yDiff = mY - theOtherOne.mY;
			
			if (yDiff != 0) {
				return yDiff;
			}
			
			return mZ - theOtherOne.mZ;
		}
	}
	
	/**
	 * Inner class providing a unique identifier for data sets
	 * @author MAJ727
	 *
	 */
	public abstract static class AbstractDataSetUniqueId {
		private final String mId;
		private String mFullDisplayString;
		private String mShortDisplayString;
		
		/**
		 * Constructor
		 * @param dataSet The data set this is the identifier for
		 */
		protected AbstractDataSetUniqueId(String id) {
			mId = id;
			setDisplayString(mId.substring(mId.lastIndexOf(MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR) + 1));
		}

		@Override
		public boolean equals(Object theOtherOne) {
			if (theOtherOne == null) {
				return false;
			}
			
			if (theOtherOne == this) {
				return true;
			}
			
			if (theOtherOne.getClass() != this.getClass()) {
				return false;
			}
			
			AbstractDataSetUniqueId otherOne = (AbstractDataSetUniqueId) theOtherOne;
			
			return mId.equals(otherOne.mId); 
		}
		
		@Override
		/**
		 * Gets the hashcode
		 */
		public int hashCode() {
			return mId.hashCode();
		}

		/**
		 * Sets the display string for this id
		 * @param displayString The new display string to set
		 */
		public void setDisplayString(String displayString) {
			mFullDisplayString = displayString;
			mShortDisplayString = displayString.substring(0, displayString.indexOf(DADefinitions.FILE_EXTENSION_DATA_SET));
		}

		/**
		 * Gets the display string for this id (including file extension)
		 * @return The displayString
		 */
		public String getFullDisplayString() {
			return mFullDisplayString;
		}

		/**
		 * Gets the display string for this id (without file extension)
		 * @return The displayString
		 */
		public String getShortDisplayString() {
			return mShortDisplayString;
		}
	}

	/**
	 * Calculates the correlation between the specified velocity components of specified data points.
	 * Calculates the correlation between component1 of the first data point in the y/z-coordinate arrays and component2 of
	 * each of the subsequent points
	 * @param uniqueId The unique id of the data set the points are in
	 * @param yCoords The y-coordinates of the data points to calculate the correlations between
	 * @param zCoords The z-coordinates of the data points to calculate the correlations between
	 * @param component1 The first component to correlate (should be one of DPD_DPD_KEY_FILTERED_?_VELOCITIES)
	 * @param component2 The second component to correlate (should be one of DPD_DPD_KEY_FILTERED_?_VELOCITIES)
	 * @param frontEndInterface The callback interface to report the result to
	 */
	public void calculateCorrelationBetweenComponentsOfDataPoints(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		int numberOfDataPoints = yCoords.size();
		
		if (numberOfDataPoints != zCoords.size()) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.CALCULATE_CORRELATION_ERROR_TITLE), DAStrings.getString(DAStrings.CALCULATE_CORRELATION_ERROR_MSG));
		}

		Vector<Double> uCorrelations = new Vector<Double>(numberOfDataPoints);
		Vector<Double> vCorrelations = new Vector<Double>(numberOfDataPoints);
		Vector<Double> wCorrelations = new Vector<Double>(numberOfDataPoints);
		DataPointSummary dataPointSummary1 = new DataPointSummary();
		DataPointDetail dataPointDetail1 = new DataPointDetail();
		loadDataPointDetails(uniqueId, yCoords.elementAt(0), zCoords.elementAt(0), dataPointSummary1, dataPointDetail1);
		Vector<Double> uVelocities1 = dataPointDetail1.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
		Vector<Double> vVelocities1 = dataPointDetail1.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
		Vector<Double> wVelocities1 = dataPointDetail1.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
		
		for (int i = 1; i < numberOfDataPoints; ++i) {
			DataPointSummary dataPointSummary2 = new DataPointSummary();
			DataPointDetail dataPointDetail2 = new DataPointDetail();
			loadDataPointDetails(uniqueId, yCoords.elementAt(i), zCoords.elementAt(i), dataPointSummary2, dataPointDetail2);
			
			Vector<Double> uVelocities2 = dataPointDetail2.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
			Vector<Double> vVelocities2 = dataPointDetail2.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
			Vector<Double> wVelocities2 = dataPointDetail2.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
			
			uCorrelations.add(MAJFCMaths.correlation(uVelocities1, uVelocities2));
			vCorrelations.add(MAJFCMaths.correlation(vVelocities1, vVelocities2));
			wCorrelations.add(MAJFCMaths.correlation(wVelocities1, wVelocities2));
		}
		
		frontEndInterface.onCorrelationsCalculated(uniqueId, yCoords, zCoords, uCorrelations, vCorrelations, wCorrelations);
	}
	
	/**
	 * Calculates the correlation between the specified velocity components of specified data points.
	 * Calculates the correlation between component1 of the first data point in the y/z-coordinate arrays and component2 of
	 * each of the subsequent points
	 * @param uniqueId The unique id of the data set the points are in
	 * @param yCoords The y-coordinates of the data points to calculate the correlations between
	 * @param zCoords The z-coordinates of the data points to calculate the correlations between
	 * @param component1 The first component to correlate (should be one of DPD_DPD_KEY_FILTERED_?_VELOCITIES)
	 * @param component2 The second component to correlate (should be one of DPD_DPD_KEY_FILTERED_?_VELOCITIES)
	 * @param frontEndInterface The callback interface to report the result to
	 */
	public void calculatePseudoCorrelationBetweenComponentsOfDataPoints(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		int numberOfDataPoints = yCoords.size();
		
		if (numberOfDataPoints != zCoords.size()) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.CALCULATE_CORRELATION_ERROR_TITLE), DAStrings.getString(DAStrings.CALCULATE_CORRELATION_ERROR_MSG));
		}

		Vector<Double> uCorrelations = new Vector<Double>(numberOfDataPoints);
		Vector<Double> vCorrelations = new Vector<Double>(numberOfDataPoints);
		Vector<Double> wCorrelations = new Vector<Double>(numberOfDataPoints);
		DataPointSummary dataPointSummary1 = new DataPointSummary();
		DataPointDetail dataPointDetail1 = new DataPointDetail();
		loadDataPointDetails(uniqueId, yCoords.elementAt(0), zCoords.elementAt(0), dataPointSummary1, dataPointDetail1);
		Vector<Double> uVelocities1 = dataPointDetail1.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
		Vector<Double> vVelocities1 = dataPointDetail1.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
		Vector<Double> wVelocities1 = dataPointDetail1.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
		
		for (int i = 1; i < numberOfDataPoints; ++i) {
			DataPointSummary dataPointSummary2 = new DataPointSummary();
			DataPointDetail dataPointDetail2 = new DataPointDetail();
			loadDataPointDetails(uniqueId, yCoords.elementAt(i), zCoords.elementAt(i), dataPointSummary2, dataPointDetail2);
			
			Vector<Double> uVelocities2 = dataPointDetail2.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
			Vector<Double> vVelocities2 = dataPointDetail2.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
			Vector<Double> wVelocities2 = dataPointDetail2.get(DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
			
			uCorrelations.add(MAJFCMaths.pseudoCorrelation(uVelocities1, uVelocities2));
			vCorrelations.add(MAJFCMaths.pseudoCorrelation(vVelocities1, vVelocities2));
			wCorrelations.add(MAJFCMaths.pseudoCorrelation(wVelocities1, wVelocities2));
		}
		
		frontEndInterface.onCorrelationsCalculated(uniqueId, yCoords, zCoords, uCorrelations, vCorrelations, wCorrelations);
	}
	
	/**
	 * Removes data points from the specified data set
	 * @param uniqueId The id of the data set to remove points from
	 * @param yCoords The y-coordinates of the data points to remove
	 * @param zCoords The z-coordinates of the data points to remove (should match yCoords, in pairs)
	 * @param frontEndInterface The interface to report progress to the front end
	 * @throws BackEndAPIException
	 */
	public void removeDataPoints(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, BackEndCallbackInterface frontEndInterface) throws BackEndAPIException {
		int numberOfDataPoints = yCoords.size();
		
		if (numberOfDataPoints != zCoords.size()) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.REMOVE_DATA_POINTS_ERROR_TITLE), DAStrings.getString(DAStrings.REMOVE_DATA_POINTS_ERROR_MSG));
		}
		
		DataSet dataSet = mDataSetLookup.get(uniqueId);
		Vector<Integer> removedYCoords = new Vector<Integer>(numberOfDataPoints), removedZCoords = new Vector<Integer>(numberOfDataPoints);
		
		for (int i = 0; i < numberOfDataPoints; ++i) {
			try {
				dataSet.removeDataPoint(yCoords.elementAt(i), zCoords.elementAt(i));
				removedYCoords.add(yCoords.elementAt(i));
				removedZCoords.add(zCoords.elementAt(i));
			} catch (BackEndAPIException theException) {
				frontEndInterface.onDataPointsRemoved(uniqueId, removedYCoords, removedZCoords);
				throw theException;
			}
		}
		
		frontEndInterface.onDataPointsRemoved(uniqueId, removedYCoords, removedZCoords);
	}
	
	/**
	 * Gets the lookup of keys of missing data point summary fields 
	 * @param uniqueId The id of the data set to remove points from
	 */
	public Hashtable<DataPointSummaryIndex, DataPointSummaryIndex> getMissingDataPointSummaryFieldKeys(AbstractDataSetUniqueId uniqueId) throws BackEndAPIException {
		DataSet dataSet = mDataSetLookup.get(uniqueId);
		return dataSet.getMissingDataPointSummaryFieldKeys();
	}
	
	/**
	 * Calculates the specified data point summary field and any associated summary fields 
	 * @param uniqueId The id of the data set to remove points from
	 * @param dpsIndex The field to calculate (use BackEndAPI.DPS_KEY_U_TKE_FLUX for any third order correlation stuff)
	 * @param frontEndInterface The interface to report progress to the front end
	 * @param progressInterface The interface to report progress
	 * @throws BackEndAPIException
	 */
	public void calculateDataPointSummaryField(AbstractDataSetUniqueId uniqueId, DataPointSummaryIndex dpsIndex, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		DataSet dataSet = mDataSetLookup.get(uniqueId);
				
		frontEndInterface.onDataPointSummaryFieldCalculated(uniqueId, dataSet.calculateDataPointSummaryField(dpsIndex, DAFrame.getFrame(), frontEndInterface, progressInterface));
	}
	
	/**
	 * Creates a rotation correction batch from the specified data points and calculates their rotation corrections
	 * @param uniqueId The id of the data set to use
	 * @param yCoords The y-coordinates of the data points to assign to this batch
	 * @param zCoords The z-coordinates of the data points to assign to this batch
	 * @param frontEndInterface The interface to report progress to the front end
 	 * @param progressInterface The interface used to report progress
	 * @throws BackEndAPIException
	 */
	public void createRotationCorrectionBatch(AbstractDataSetUniqueId uniqueId, Vector<Integer> yCoords, Vector<Integer> zCoords, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		DataSet dataSet = mDataSetLookup.get(uniqueId);
		dataSet.createRotationCorrectionBatch((int) System.currentTimeMillis(), yCoords, zCoords, frontEndInterface, progressInterface);
		frontEndInterface.onRotationCorrectionBatchCreated(uniqueId);
	}

	/**
	 * Create rotation correction batches from the batch.majrcb file in the data set's main directory
	 * batches.majrcb must have format:
	 * <batches>
	 * 		<batch batch_number="xx">
	 * 			<y_min>int</y_min>
	 * 			<y_max>int</y_max>
	 * 			<z_min>int</z_min>
	 * 			<z_max>int</z_max>
	 * 		</batch>
	 * 			...
	 * 		<batch batch_number="yy">
	 * 			<y_min>int</y_min>
	 * 			<y_max>int</y_max>
	 * 			<z_min>int</z_min>
	 * 			<z_max>int</z_max>
	 * 		</batch>
	 * </batches>  
	 * @param uniqueId The data set to make the batches in
	 * @param frontEndInterface The interface to report to the front end
	 * @param progressInterface The interface used to report progress
	 * @throws BackEndAPIException
	 */
	public void createRotationCorrectionBatchesFromFile(AbstractDataSetUniqueId uniqueId, BackEndCallbackInterface frontEndInterface, DAProgressInterface progressInterface) throws BackEndAPIException {
		DataSet dataSet = mDataSetLookup.get(uniqueId);
		dataSet.createRotationCorrectionBatchesFromFile(frontEndInterface, progressInterface);
		frontEndInterface.onRotationCorrectionBatchesCreatedFromFile(uniqueId);
	}

	public interface DAProgressInterface {
		public void setProgress(int progress, String message);
	}

	/**
	 * Trims a data point to the specified portion of the time-series
	 * @param uniqueId
	 * @param yCoord
	 * @param zCoord
	 * @param startIndex
	 * @param endIndex
	 * @throws BackEndAPIException 
	 */
	public void trimDataPoint(AbstractDataSetUniqueId uniqueId, BackEndCallbackInterface frontEndInterface, int yCoord, int zCoord, int startIndex, int endIndex) throws BackEndAPIException {
		DataSet dataSet = mDataSetLookup.get(uniqueId);
		DataPointSummary dataPointSummary = new DataPointSummary();
		DataPointDetail dataPointDetail = new DataPointDetail();
		dataSet.trimDataPoint(yCoord, zCoord, startIndex, endIndex, dataPointSummary, dataPointDetail);
		
		frontEndInterface.onDataPointTrimmed(uniqueId, dataPointSummary, dataPointDetail);
	}
}

