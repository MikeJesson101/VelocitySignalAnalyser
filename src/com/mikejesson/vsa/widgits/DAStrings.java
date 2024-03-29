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

import java.io.BufferedReader;     
import java.io.File;     
import java.io.FileReader;     
import java.io.IOException;     
import java.util.Hashtable;     

import com.mikejesson.majfc.helpers.MAJFCSafeArray;     
import com.mikejesson.majfc.helpers.MAJFCTools;     


public class DAStrings extends MAJFCSafeArray<String, DAStrings.DAStringIndex> {
	// In the future these may be customised through a configuration file
	private static int NUMBER_OF_STRING_INDICES = 0; 
	public static final DAStringIndex NEW_SINGLE_PROBE_DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_MULTIPLE_PROBE_DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_MULTIPLE_PROBE_DATA_SET_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_MULTI_RUN_MULTI_PROBE_DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_MULTI_RUN_MULTI_PROBE_DATA_SET_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_DATA_SET_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex OPEN_DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex OPEN_DATA_SET_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_CSV_FILES = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);			
	public static final DAStringIndex IMPORT_CSV_FILES_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_CSV_DATA_FILES_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_MULTIPLE_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_SINGLE_U_MEASUREMENTS_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CHOOSE_SINGLE_U_MEASUREMENTS_FILE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex OK = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CANCEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CLOSE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_COORD_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_COORD_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORTED_FILE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET_OPEN_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET_OPEN_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILE_EXISTS_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILE_EXISTS_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILE_NAME_WRONG_EXTENSION_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILE_NAME_WRONG_EXTENSION_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_FILE_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_FILE_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_FROM_FILE_DATA_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_FROM_FILE_DATA_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_VELOCITY_COMPONENT_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_VELOCITY_COMPONENT_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_VELOCITY_COMPONENT_XML_TAG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_VELOCITY_COMPONENT_XML_TAG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_CHARACTER_IN_CSV_FILE_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_CHARACTER_IN_CSV_FILE_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CSV_FILE_FILTER_NAME = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINTS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_POINT_DATA_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_POINT_DATA_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILTERED_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILTERED_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex POINT_DATA = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONFIGURATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONFIGURATION_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONFIGURATION_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEFAULT_DATA_FILE_PATH_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECT_DATA_FILE_PATH_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NEW_DATA_SET_FILENAME_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NO_SUCH_DATA_POINT_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NO_SUCH_DATA_POINT_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_DETAILS_READ_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_DETAILS_READ_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_FILTERED_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_FILTERED_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_FILTERED_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_FILTERED_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_FILTERED_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_FILTERED_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_UNFILTERED_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_UNFILTERED_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_UNFILTERED_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_FILTERED_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_FILTERED_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_FILTERED_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXCLUDE_LEVEL_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
 	public static final DAStringIndex DATA_FILE_DELIMITER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_FILE_DELIMITER_OPTIONS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
 	public static final DAStringIndex DATA_FILE_CSV_DECIMAL_SEPARATOR_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_FILE_CSV_DECIMAL_SEPARATOR_OPTIONS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEFAULT_CSV_FILE_PATH_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECT_CSV_FILE_PATH_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CHOOSE_CSV_FILE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ADD_ANOTHER_CSV_FILE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CROSS_SECTION_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CROSS_SECTION_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_VELOCITY_CROSS_SECTION_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_VELOCITY_CROSS_SECTION_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_VELOCITY_CROSS_SECTION_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CHOOSE_DIRECTORY = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILENAME_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_MOVING_TEMP_FILE_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_MOVING_TEMP_FILE_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex APPLY = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UPDATING_SUMMARY_DATA = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FLUID_DENSITY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FLUID_KINEMATIC_VISCOSITY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LEFT_BANK_POSITION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RIGHT_BANK_POSITION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WATER_DEPTH_POSITION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_CROSS_SECTION_DATA_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_CROSS_SECTION_DATA_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVERT_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVERT_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVERT_Z_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LOCK_DATA_SET_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RUN_INDEX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex XZ_PLANE_ROTATION_THETA_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex YZ_PLANE_ROTATION_PHI_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex XY_PLANE_ROTATION_ALPHA_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PROBE_X_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PROBE_Y_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PROBE_Z_VELOCITIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEASURED_DISCHARGE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVING_DATA_SET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORTING_SINGLE_U_MEASUREMENTS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RECALCULATING_DATA_PROGRESS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATING_DATA_POINT_SUMMARY_FIELDS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CREATING_RC_BATCH_PROGRESS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_RC_BATCH_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_RC_BATCH_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CREATING_RC_BATCHES_PROGRESS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_FILES_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_FILE_VELOCITY_UNIT_OPTIONS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET_CREATION_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET_CREATION_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_FILE_VELOCITY_UNITS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VIEW_DATA_POINT_DETAILS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VIEW_DATA_POINT_DETAILS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CROSS_SECTION_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_VELOCITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_VELOCITY_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DERIVATIVE_GRID_SECONDARY_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LATERAL_VELOCITY_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LATERAL_VELOCITY_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LATERAL_VELOCITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_DETAILS_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_DETAILS_TABLE_TAB = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex BED_SLOPE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATED_MEAN_SHEAR_STRESS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONSERVATION_OF_MOMENTUM_MEAN_SHEAR_STRESS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_FLUCTUATIONS_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_FLUCTUATIONS_MEAN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VECTRINO_HEADER_FILE_MISSING_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VECTRINO_HEADER_FILE_MISSING_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_LINE_IN_VECTRINO_DATA_FILE_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_LINE_IN_VECTRINO_DATA_FILE_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_BINARY_DATA_FILES_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_CONVERTED_VNO_FILES = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_MULTIPLE_CONVERTED_VECTRINO_FILES_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_REYNOLDS_STRESS_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_FULL_SERIES_LINEAR_EXTRAPOLATION_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_BOTTOM_POINTS_LINEAR_EXTRAPOLATION_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_MEAN_OF_BOTTOM_THREE_POINTS_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_SMOOTH_BED_LOG_LAW_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_KS_FROM_ROUGH_BED_LOG_LAW_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_10_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_20_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_POINTS_ABOVE_0_POINT_2LINEAR_EXTRAPOLATION_SERIES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex POLYSYNC_COLUMN_HEADINGS_MISSING_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex POLYSYNC_COLUMN_HEADINGS_MISSING_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex POLYSYNC_CORRUPT_LINE_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex POLYSYNC_CORRUPT_LINE_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CORRECT_ROTATION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PERCENTAGE_OF_VELOCITIES_GOOD_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_NONE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_REMOVE_ZEROES_LEVEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_EXCLUDE_LEVEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_VELOCITY_CORRELATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_CORRELATION_AND_SNR = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_TYPE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_NONE_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_EXCLUDE_LEVEL_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_VELOCITY_CORRELATION_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_CORRELATION_AND_SNR_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_DATA_TABLE_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_GRAPHS_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_ALL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_SELECTED = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_MULTI_PROBE_BINARY_FILES_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex IMPORT_MULTI_PROBE_BINARY_FILES_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAIN_PROBE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FIXED_PROBE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NUMBER_OF_PROBES = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_FIXED_PROBE_INDEX_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_FIXED_PROBE_INDEX_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FIXED_PROBE_INDEX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_OFFSET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_OFFSET = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_OPENING_DATA_SET_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_OPENING_DATA_SET_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_SAVING_DATA_SET_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_SAVING_DATA_SET_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_IMPORTING_DATA_POINT_DIALOG_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_NEW_DATA_SET_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CREATING_NEW_DATA_SET_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_LOADING_DATA_POINT_DETAILS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_LOADING_DATA_POINT_DETAILS_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CLEARING_DATA_POINT_DETAILS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CLEARING_DATA_POINT_DETAILS_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_CONFIG_DATA_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_CONFIG_DATA_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_SETTING_CONFIG_DATA_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_SETTING_CONFIG_DATA_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_RECALCULATING_SUMMARY_DATA_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_RECALCULATING_SUMMARY_DATA_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_DATA_SET_INFO_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_DATA_SET_INFO_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_DATA_SET_ALREADY_OPEN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_DATA_SET_ALREADY_OPEN_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CLOSING_DATA_SET_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CLOSING_DATA_SET_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAIN_PROBE_INDEX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_MAIN_PROBE_INDEX_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_MAIN_PROBE_INDEX_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAIN_DATA_POINTS_OVERVIEW_DISPLAY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONFIGURATION_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_V_PRIME_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_W_PRIME_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_W_PRIME_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_W_PRIME_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_U_PRIME_W_PRIME_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_GRAPH_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_SHORTHAND_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_CMSD_DURATION_GRAPH_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_CMSD_DURATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_CMSD_DURATION_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_CMSD_DURATION_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_1_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_2_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_3_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_4_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_CORRELATION_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_CORRELATION_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_CORRELATION_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_CORRELATION_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_NORMALISED_QUADRANT_HOLE_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LATERAL_TKE_FLUX_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_INTENSITY_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_INTENSITY_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex X_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex X_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_INTENSITY_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_REYNOLDS_STRESS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_PRIME_W_PRIME_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_PRIME_W_PRIME_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_PRIME_W_PRIME_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_PRIME_W_PRIME_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_CORRELATION_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_CORRELATION_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UW_CORRELATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UW_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UW_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UW_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UW_CORRELATION_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UW_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UW_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UW_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_CORRELATION_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_CORRELATION_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex UV_CORRELATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UV_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UV_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UV_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_UV_CORRELATION_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_UV_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VW_CORRELATION_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VW_CORRELATION_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VW_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VW_CORRELATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VW_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VW_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VW_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VW_CORRELATION_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VW_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VW_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VW_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex REYNOLDS_STRESS_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TI_AND_TKE_AND_VORTICITY_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRID_300_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRID_210_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRID_120_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRID_030_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXPORT_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXPORT_GRAPH_FOR_MATLAB_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SPECTRAL_DISTRIBUTION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TKE_FLUX_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TKE_FLUX_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TKE_FLUX_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex X_DIRECTION_TKE_FLUX_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_DIRECTION_TKE_FLUX_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_DIRECTION_TKE_FLUX_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TKE_FLUX_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_SPECTRAL_DISTRIBUTION_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SPECTRAL_DISTRIBUTION_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SPECTRAL_DISTRIBUTION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SPECTRAL_DISTRIBUTION_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex BACKWARD_COMPATIBILITY_MISSING_DATA_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex BACKWARD_COMPATIBILITY_MISSING_DATA_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex REMOVE_DATA_POINTS_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex REMOVE_DATA_POINTS_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex REMOVE_DATA_POINTS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex REMOVE_DATA_POINTS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_EOF_R_MATRIX_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_EOF_R_MATRIX_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_EOF_R_MATRIX_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EOF_R_MATRIX_DIALOG_Y1_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EOF_R_MATRIX_DIALOG_Y2_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EOF_R_MATRIX_DIALOG_Z1_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EOF_R_MATRIX_DIALOG_Z2_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EOF_R_MATRIX_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EOF_R_MATRIX_INCOMPLETE_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EOF_R_MATRIX_INCOMPLETE_GRID_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DATA_POINT_SUMMARY_DATA = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DATA_POINT_SUMMARY_DATA_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_REYNOLDS_STRESSES = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_REYNOLDS_STRESSES_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_QH_DATA = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_QH_DATA_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_TKE_DATA = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_TKE_DATA_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_FIXED_PROBE_CORRELATIONS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CREATE_ROTATION_CORRECTION_BATCH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECTION_TOOL_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECTION_TOOL_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_COORD_MIN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_COORD_MAX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_COORD_MIN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_COORD_MAX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex BATCH_THETA_ROTATION_CORRECTION_COLUMN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex BATCH_ALPHA_ROTATION_CORRECTION_COLUMN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex BATCH_PHI_ROTATION_CORRECTION_COLUMN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex BATCH_ROTATION_CORRECTION_BATCH_NUMBER_COLUMN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ROTATION_CORRECTION_BATCHES_FILE_MISSING_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ROTATION_CORRECTION_BATCHES_FILE_MISSING_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_MSG = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_U_ST_DEV_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_U_ST_DEV_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_U_ST_DEV_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_U_ST_DEV_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_V_ST_DEV_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_V_ST_DEV_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_V_ST_DEV_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_V_ST_DEV_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_W_ST_DEV_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_W_ST_DEV_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_W_ST_DEV_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VELOCITY_W_ST_DEV_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_PDF_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_PDF_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_PDF_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PDF_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_PDF_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_PDF_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_PDF_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_PDF_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_PDF_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_PDF_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_PDF_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_PDF_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex GAUSSIAN = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIMENSIONLESS_AXES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIMENSIONLESS_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIMENSIONLESS_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_ALL_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_DPS_ALL_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_SECTION_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_SECTION_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_SECTION_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_GRID_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RESULTANT_SHEAR_STRESS_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXTRAPOLATED_DEPTH_AVERAGED_GRAPH_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex POPULATED_CELL_DEPTH_AVERAGED_GRAPH_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_INTENSITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY__GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RELATIVE_TO_MEAN_CHECKBOX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DIMENSIONLESS_CHECKBOX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_1_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_2_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_3_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_4_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_5_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERMS_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_1_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_1_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_2_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_2_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_3_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_3_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_4_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_4_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_5_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_TERM_5_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_GRAPHS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VORTICITY_EQUATION_GRAPHS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LAST_MODIFIED_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex COMMENTS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MATLAB_XY_OUTPUT_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MATLAB_XYZ_OUTPUT_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WATER_TEMPERATURE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEFAULT_EXPORT_FILE_PATH_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SELECT_EXPORT_FILE_PATH_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex USE_DATA_FILE_PATH_FOR_ALL_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_U_TKE_FLUX_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_U_TKE_FLUX_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_U_TKE_FLUX_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_V_TKE_FLUX_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_V_TKE_FLUX_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_V_TKE_FLUX_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_W_TKE_FLUX_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_W_TKE_FLUX_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_W_TKE_FLUX_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VIEW_PERCENTAGE_GOOD_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VIEW_PERCENTAGE_GOOD_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_KE_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_KE_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STREAMWISE_VORTICITY_KE_GRID_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PERCENTAGE_GOOD_GRID_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PERCENTAGE_GOOD_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PERCENTAGE_GOOD_GRID_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENT_KINETIC_ENERGY_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENT_KINETIC_ENERGY_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENT_KINETIC_ENERGY_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENT_KINETIC_ENERGY_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENT_KINETIC_ENERGY_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_TKE_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_TKE_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_TKE_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INVERT_CHECKBOX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_CONDITIONAL_TIME_SERIES_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONDITIONAL_TIME_SERIES_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONDITIONAL_TIME_SERIES_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONDITIONAL_TIME_SERIES_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CONDITIONAL_TIME_SERIES_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_OFFSET_CORRELATIONS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex OFFSET_CORRELATIONS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_OFFSET_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_OFFSET_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_OFFSET_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CORRELATIONS_DISTRIBUTION_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CORRELATIONS_DISTRIBUTION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CORRELATIONS_DISTRIBUTION_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CORRELATIONS_DISTRIBUTION_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HIDE_TITLE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MIRROR_ABOUT_VERTICAL_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex X_AXIS_LOGARITHMIC_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_AXIS_LOGARITHMIC_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FILTERING_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CS_TYPE_STANDARD_DEVIATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CS_TYPE_MEAN_ABSOLUTE_DEVIATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PST_REPLACEMENT_METHOD_NONE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PST_REPLACEMENT_METHOD_LINEAR_INTERPOLATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PST_REPLACEMENT_METHOD_LAST_GOOD_VALUE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PST_REPLACEMENT_METHOD_12PP_INTERPOLATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MODIFIED_PST_AUTO_SAFE_LEVEL_C1_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PST_REPLACEMENT_METHOD_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CS_TYPE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAMPLING_RATE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LIMITING_CORRELATION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ABOUT_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ABOUT_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ABOUT_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERSION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex GPL_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex GPL_LINK = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex GPL_WARRANTY_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WARRANTY = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex I_ACCEPT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ADDITIONAL_SOFTWARE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MOMENTUM_GRAPHS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VTSM_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VTSM_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VTSM_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_VTSM_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VTSM_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VTSM_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VTSM_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_HTSM_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_HTSM_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_HTSM_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_HTSM_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_HTSM_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_HTSM_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_HTSM_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STANDARD_CELL_WIDTH_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex STANDARD_CELL_HEIGHT_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MULTI_RUN_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MULTI_RUN_SYNCH_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SYNCH_NONE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SYNCH_MAX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SYNCH_LIMITING_VALUE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SYNCH_LIMITING_VALUE_SETTER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SYNCH_LIMITING_VALUE_DIRECTION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SYNCH_LIMITING_VALUE_DIRECTION_OPTIONS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex FULL_CORRELATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ENSEMBLE_CORRELATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RANDOM_ENSEMBLE_CORRELATION = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_TRUE_ENSEMBLE_CORRELATION_SET_1 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_TRUE_ENSEMBLE_CORRELATION_SET_2 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_1 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_2 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex NOT_YET_CALCULATED = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PROCESSING_FILE_MESSAGE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TURBULENCE_GENERATION_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TURBULENCE_GENERATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TURBULENCE_GENERATION_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TURBULENCE_GENERATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TURBULENCE_DISSIPATION_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TURBULENCE_DISSIPATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TURBULENCE_DISSIPATION_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex LARGE_FILES_AND_MISC_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SPLIT_LARGE_FILES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex LARGE_FILES_NUMBER_OF_MEASUREMENTS_PER_SPLIT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex PRESSURES_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex DESPIKING_FILTER_MOVING_AVERAGE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_MOVING_AVERAGE_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex MULTI_RUN_RUN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RUN_MEAN = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex RUN_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex INDEX_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex ERROR_SAVING_FILE_DETAILS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_DETAILS_DIRECTORY_SUFFIX = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex PROBE_SETUP_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SYNCH_INDEX = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PROBE_NONE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex TIME_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex TRIM_WARNING = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TRIM_START_POINT_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex TRIM_END_POINT_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex TRIM_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MAXIMUM_VELOCITIES_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex DATA_POINT_OVERVIEW_TABLE_TAB = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_OVERVIEW_TABLE_PROBE_TYPE_COL_HEADER = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_OVERVIEW_TABLE_PROBE_ID_COL_HEADER = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_POINT_OVERVIEW_TABLE_SAMPLING_RATE_COL_HEADER = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex VARIOUS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SAVE_DETAILS_TO_FILE_MENU_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex BOUNDARY_DEFINITION_FILE_FILTER_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex DATA_SET_LENGTH_UNIT_OPTIONS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DATA_SET_LENGTH_UNITS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex LIMITING_SNR_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SIGNAL_CORRELATION_AND_SNR_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SIGNAL_CORRELATION_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SNR_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex DESPIKING_FILTER_W_DIFF = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DESPIKING_FILTER_W_DIFF_REFERENCE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex LIMITING_W_DIFF_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SHOW_W_DIFF_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_W_DIFF_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_DIFF_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_DIFF_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_DIFF_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_DIFF_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_DIFF_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex I_DECLINE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex USE_BINARY_FILE_FORMAT_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex PRE_FILTER_TYPE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex NUMBER_OF_BARTLETT_WINDOWS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex X_CORRELATIONS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex X_SNRS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_CORRELATIONS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Y_SNRS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_CORRELATIONS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_SNRS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex MEAN_CORRELATION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CORRELATION_STDEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MEAN_SNR_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SNR_ST_DEV_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex NEWER_VERSIONS = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex NEWER_VERSIONS_AVAILABLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex MATLAB_CONNECTION_TEST_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex MATLAB_PROGRESS_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SEND_DETAILS_TO_MATLAB_MENU_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SEND_ALL_DETAILS_TO_MATLAB_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex WAVELET_TYPE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TRANSFORM_TYPE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_HAAR02_ORTOHOGONAL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB02 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB04 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB06 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB08 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB10 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB12 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB14 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB16 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB18 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_DAUB20 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_LEGE02 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_LEGE04 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_LEGE06 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TYPE_COIF06 = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TRANSFORM_TYPE_DFT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TRANSFORM_TYPE_FWT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_TRANSFORM_TYPE_CWT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SPECTRAL_ANALYSIS_CONFIG_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SHOW_WAVELET_ANALYSIS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_WAVELET_ANALYSIS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex WAVELET_ANALYSIS_LEVELS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_SCALEOGRAM_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_RECONSTRUCTION_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex WAVELET_ANALYSIS_LEVELS_SELECTOR_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex AT_TRIM_START_BY_SETTER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex AT_PRIOR_LENGTH_SETTER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex AT_SAMPLE_LENGTH_SETTER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex AT_PRIOR_LENGTH_CHECKBOX_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex TABLE_ACTION_PROGRESS_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex WAVELET_ANALYSIS_CONTOUR_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex WAVELET_ANALYSIS_CONTOUR_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex VELOCITY_CORRELATIONS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_VELOCITY_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_VELOCITY_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_VELOCITY_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_CORRELATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CALCULATE_PSEUDO_CORRELATION_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_PSEUDO_CORRELATIONS_GRAPH_FRAME_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex U_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex V_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex W_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SYNCH_IGNORE_FIRST_X_SECONDS_SETTER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex MOVING_AVERAGE_WINDOW_SIZE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex EXPORT_DWT_LEVELS_DATA_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXPORT_DWT_LEVELS_DATA_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex CHOOSE_CONFIG_FILE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex COPY_CONFIG_FILE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex SELECT_NEW_CONFIG_FILE_DIALOG_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex CONFIGURATION_FILE_FILTER_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex CSV_FILE_FORMAT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex PST_REPLACEMENT_POLYNOMIAL_ORDER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex FIVE_THIRDS_LINE_CENTRE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex SHOW_FIVE_THIRDS_LINE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex FIVE_THIRDS_LINE_LEGEND_ENTRY = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex PSD_TYPE_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PSD_WINDOW_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PSD_WELCH_WINDOW_OVERLAP_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PSD_TYPE_BARTLETT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PSD_TYPE_WELCH = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PSD_WINDOW_NONE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PSD_WINDOW_BARTLETT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex PSD_WINDOW_HAMMING = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex EXPORT_PSD_S_NOUGHTS_BUTTON_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex EXPORT_PSD_S_NOUGHTS_BUTTON_DESC = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex Y_COLUMN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex Z_COLUMN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex S_NOUGHT_COLUMN_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex TI_SCALE_BY_Q_OVER_A_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex INVERT_AXES_TEXT = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex CWT_BY_FREQUENCY_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CWT_BY_FREQUENCY_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CWT_BY_TIME_GRAPH_TITLE = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CWT_BY_TIME_TAB_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex CWT_BY_FREQUENCY_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CWT_BY_FREQUENCY_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CWT_BY_TIME_GRAPH_X_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);
	public static final DAStringIndex CWT_BY_TIME_GRAPH_Y_AXIS_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex WAVELET_TRANSFORM_SCALE_BY_INST_POWER_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

	public static final DAStringIndex FILE_EXTENSION_LABEL = new DAStrings.DAStringIndex(NUMBER_OF_STRING_INDICES++);

//INSERT_DECLARATION      

	public static final String NEW_SINGLE_PROBE_DATA_SET_STR = "NEW_SINGLE_PROBE_DATA_SET";
	public static final String NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC_STR = "NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC";
	public static final String NEW_MULTIPLE_PROBE_DATA_SET_STR = "NEW_MULTIPLE_PROBE_DATA_SET";
	public static final String NEW_MULTIPLE_PROBE_DATA_SET_BUTTON_DESC_STR = "NEW_MULTIPLE_PROBE_DATA_SET_BUTTON_DESC";
	public static final String NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_STR = "NEW_MULTI_RUN_DATA_SET";
	public static final String NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_BUTTON_DESC_STR = "NEW_MULTI_RUN_DATA_SET_BUTTON_DESC";
	public static final String NEW_MULTI_RUN_MULTI_PROBE_DATA_SET_STR = "NEW_MULTI_RUN_MULTI_PROBE_DATA_SET";
	public static final String NEW_MULTI_RUN_MULTI_PROBE_DATA_SET_BUTTON_DESC_STR = "NEW_MULTI_RUN_MULTI_PROBE_DATA_SET_BUTTON_DESC";
	public static final String SAVE_STR = "SAVE";
	public static final String SAVE_DATA_SET_STR = "SAVE_DATA_SET";
	public static final String SAVE_DATA_SET_BUTTON_DESC_STR = "SAVE_DATA_SET_BUTTON_DESC";
	public static final String OPEN_DATA_SET_STR = "OPEN_DATA_SET";
	public static final String OPEN_DATA_SET_BUTTON_DESC_STR = "OPEN_DATA_SET_BUTTON_DESC";
	public static final String IMPORT_CSV_FILES_STR = "IMPORT_CSV_FILES";
	public static final String IMPORT_CSV_FILES_BUTTON_DESC_STR = "IMPORT_CSV_FILES_BUTTON_DESC";
	public static final String IMPORT_CSV_DATA_FILES_DIALOG_TITLE_STR = "IMPORT_CSV_DATA_FILES_DIALOG_TITLE";
	public static final String IMPORT_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_LABEL_STR = "IMPORT_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_LABEL";
	public static final String IMPORT_MULTIPLE_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_DESC_STR = "IMPORT_MULTIPLE_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_DESC";
	public static final String IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL_STR = "IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL";
	public static final String IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_DESC_STR = "IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_DESC";
	public static final String IMPORT_SINGLE_U_MEASUREMENTS_DIALOG_TITLE_STR = "IMPORT_SINGLE_U_MEASUREMENTS_DIALOG_TITLE";
	public static final String CHOOSE_SINGLE_U_MEASUREMENTS_FILE_STR = "CHOOSE_SINGLE_U_MEASUREMENTS_FILE";
	public static final String OK_STR = "OK";
	public static final String CANCEL_STR = "CANCEL";
	public static final String CLOSE_STR = "CLOSE";
	public static final String Y_COORD_LABEL_STR = "Y_COORD_LABEL";
	public static final String Z_COORD_LABEL_STR = "Z_COORD_LABEL";
	public static final String DATA_SET_STR = "DATA_SET";
	public static final String IMPORTED_FILE_STR = "IMPORTED_FILE";
	public static final String DATA_SET_OPEN_ERROR_TITLE_STR = "DATA_SET_OPEN_ERROR_TITLE";
	public static final String DATA_SET_OPEN_ERROR_MSG_STR = "DATA_SET_OPEN_ERROR_MSG";
	public static final String FILE_EXISTS_DIALOG_TITLE_STR = "FILE_EXISTS_DIALOG_TITLE";
	public static final String FILE_EXISTS_DIALOG_MSG_STR = "FILE_EXISTS_DIALOG_MSG";
	public static final String FILE_NAME_WRONG_EXTENSION_DIALOG_TITLE_STR = "FILE_NAME_WRONG_EXTENSION_DIALOG_TITLE";
	public static final String FILE_NAME_WRONG_EXTENSION_DIALOG_MSG_STR = "FILE_NAME_WRONG_EXTENSION_DIALOG_MSG";
	public static final String ERROR_CREATING_FILE_DIALOG_TITLE_STR = "ERROR_CREATING_FILE_DIALOG_TITLE";
	public static final String ERROR_CREATING_FILE_DIALOG_MSG_STR = "ERROR_CREATING_FILE_DIALOG_MSG";
	public static final String DATA_POINT_FROM_FILE_DATA_ERROR_TITLE_STR = "DATA_POINT_FROM_FILE_DATA_ERROR_TITLE";
	public static final String DATA_POINT_FROM_FILE_DATA_ERROR_MSG_STR = "DATA_POINT_FROM_FILE_DATA_ERROR_MSG";
	public static final String INVALID_VELOCITY_COMPONENT_TITLE_STR = "INVALID_VELOCITY_COMPONENT_TITLE";
	public static final String INVALID_VELOCITY_COMPONENT_MSG_STR = "INVALID_VELOCITY_COMPONENT_MSG";
	public static final String INVALID_VELOCITY_COMPONENT_XML_TAG_TITLE_STR = "INVALID_VELOCITY_COMPONENT_XML_TAG_TITLE";
	public static final String INVALID_VELOCITY_COMPONENT_XML_TAG_MSG_STR = "INVALID_VELOCITY_COMPONENT_XML_TAG_MSG";
	public static final String INVALID_CHARACTER_IN_CSV_FILE_TITLE_STR = "INVALID_CHARACTER_IN_CSV_FILE_TITLE";
	public static final String INVALID_CHARACTER_IN_CSV_FILE_MSG_STR = "INVALID_CHARACTER_IN_CSV_FILE_MSG";
	public static final String CSV_FILE_FILTER_NAME_STR = "CSV_FILE_FILTER_NAME";
	public static final String DATA_POINTS_STR = "DATA_POINTS";
	public static final String ERROR_GETTING_POINT_DATA_TITLE_STR = "ERROR_GETTING_POINT_DATA_TITLE";
	public static final String ERROR_GETTING_POINT_DATA_MSG_STR = "ERROR_GETTING_POINT_DATA_MSG";
	public static final String MEAN_LABEL_STR = "MEAN_LABEL";
	public static final String STDEV_LABEL_STR = "STDEV_LABEL";
	public static final String FILTERED_MEAN_LABEL_STR = "FILTERED_MEAN_LABEL";
	public static final String FILTERED_STDEV_LABEL_STR = "FILTERED_STDEV_LABEL";
	public static final String POINT_DATA_STR = "POINT_DATA";
	public static final String CONFIGURATION_STR = "CONFIGURATION";
	public static final String CONFIGURATION_BUTTON_DESC_STR = "CONFIGURATION_BUTTON_DESC";
	public static final String CONFIGURATION_DIALOG_TITLE_STR = "CONFIGURATION_DIALOG_TITLE";
	public static final String DEFAULT_DATA_FILE_PATH_LABEL_STR = "DEFAULT_DATA_FILE_PATH_LABEL";
	public static final String SELECT_STR = "SELECT";
	public static final String SELECT_DATA_FILE_PATH_DIALOG_TITLE_STR = "SELECT_DATA_FILE_PATH_DIALOG_TITLE";
	public static final String NEW_DATA_SET_FILENAME_DIALOG_TITLE_STR = "NEW_DATA_SET_FILENAME_DIALOG_TITLE";
	public static final String NO_SUCH_DATA_POINT_TITLE_STR = "NO_SUCH_DATA_POINT_TITLE";
	public static final String NO_SUCH_DATA_POINT_MSG_STR = "NO_SUCH_DATA_POINT_MSG";
	public static final String DATA_POINT_DETAILS_READ_ERROR_TITLE_STR = "DATA_POINT_DETAILS_READ_ERROR_TITLE";
	public static final String DATA_POINT_DETAILS_READ_ERROR_MSG_STR = "DATA_POINT_DETAILS_READ_ERROR_MSG";
	public static final String U_MEAN_LABEL_STR = "U_MEAN_LABEL";
	public static final String U_STDEV_LABEL_STR = "U_STDEV_LABEL";
	public static final String U_FILTERED_MEAN_LABEL_STR = "U_FILTERED_MEAN_LABEL";
	public static final String U_FILTERED_STDEV_LABEL_STR = "U_FILTERED_STDEV_LABEL";
	public static final String V_MEAN_LABEL_STR = "V_MEAN_LABEL";
	public static final String V_STDEV_LABEL_STR = "V_STDEV_LABEL";
	public static final String V_FILTERED_MEAN_LABEL_STR = "V_FILTERED_MEAN_LABEL";
	public static final String V_FILTERED_STDEV_LABEL_STR = "V_FILTERED_STDEV_LABEL";
	public static final String W_MEAN_LABEL_STR = "W_MEAN_LABEL";
	public static final String W_STDEV_LABEL_STR = "W_STDEV_LABEL";
	public static final String W_FILTERED_MEAN_LABEL_STR = "W_FILTERED_MEAN_LABEL";
	public static final String W_FILTERED_STDEV_LABEL_STR = "W_FILTERED_STDEV_LABEL";
	public static final String U_VELOCITIES_LABEL_STR = "U_VELOCITIES_LABEL";
	public static final String V_VELOCITIES_LABEL_STR = "V_VELOCITIES_LABEL";
	public static final String W_VELOCITIES_LABEL_STR = "W_VELOCITIES_LABEL";
	public static final String U_UNFILTERED_VELOCITIES_LABEL_STR = "U_UNFILTERED_VELOCITIES_LABEL";
	public static final String V_UNFILTERED_VELOCITIES_LABEL_STR = "V_UNFILTERED_VELOCITIES_LABEL";
	public static final String W_UNFILTERED_VELOCITIES_LABEL_STR = "W_UNFILTERED_VELOCITIES_LABEL";
	public static final String U_FILTERED_VELOCITIES_LABEL_STR = "U_FILTERED_VELOCITIES_LABEL";
	public static final String V_FILTERED_VELOCITIES_LABEL_STR = "V_FILTERED_VELOCITIES_LABEL";
	public static final String W_FILTERED_VELOCITIES_LABEL_STR = "W_FILTERED_VELOCITIES_LABEL";
	public static final String EXCLUDE_LEVEL_LABEL_STR = "EXCLUDE_LEVEL_LABEL";
	public static final String DATA_FILE_DELIMITER_LABEL_STR = "DATA_FILE_DELIMITER_LABEL";
	public static final String DATA_FILE_DELIMITER_OPTIONS_STR = "DATA_FILE_DELIMITER_OPTIONS";
	public static final String DATA_FILE_CSV_DECIMAL_SEPARATOR_LABEL_STR = "DATA_FILE_CSV_DECIMAL_SEPARATOR_LABEL";
	public static final String DATA_FILE_CSV_DECIMAL_SEPARATOR_OPTIONS_STR = "DATA_FILE_CSV_DECIMAL_SEPARATOR_OPTIONS";	
	public static final String DEFAULT_CSV_FILE_PATH_LABEL_STR = "DEFAULT_CSV_FILE_PATH_LABEL";
	public static final String SELECT_CSV_FILE_PATH_DIALOG_TITLE_STR = "SELECT_CSV_FILE_PATH_DIALOG_TITLE";
	public static final String CHOOSE_CSV_FILE_STR = "CHOOSE_CSV_FILE";
	public static final String ADD_ANOTHER_CSV_FILE_STR = "ADD_ANOTHER_CSV_FILE";
	public static final String DATA_SET_GRAPHS_STR = "DATA_SET_GRAPHS";
	public static final String CROSS_SECTION_GRID_X_AXIS_TITLE_STR = "CROSS_SECTION_GRID_X_AXIS_TITLE";
	public static final String CROSS_SECTION_GRID_Y_AXIS_TITLE_STR = "CROSS_SECTION_GRID_Y_AXIS_TITLE";
	public static final String U_VELOCITY_CROSS_SECTION_GRID_TITLE_STR = "U_VELOCITY_CROSS_SECTION_GRID_TITLE";
	public static final String U_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT_STR = "U_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT";
	public static final String V_VELOCITY_CROSS_SECTION_GRID_TITLE_STR = "V_VELOCITY_CROSS_SECTION_GRID_TITLE";
	public static final String V_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT_STR = "V_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT";
	public static final String W_VELOCITY_CROSS_SECTION_GRID_TITLE_STR = "W_VELOCITY_CROSS_SECTION_GRID_TITLE";
	public static final String W_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT_STR = "W_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT";
	public static final String CHOOSE_DIRECTORY_STR = "CHOOSE_DIRECTORY";
	public static final String FILENAME_LABEL_STR = "FILENAME_LABEL";
	public static final String ERROR_MOVING_TEMP_FILE_TITLE_STR = "ERROR_MOVING_TEMP_FILE_TITLE";
	public static final String ERROR_MOVING_TEMP_FILE_MSG_STR = "ERROR_MOVING_TEMP_FILE_MSG";
	public static final String APPLY_STR = "APPLY";
	public static final String UPDATING_SUMMARY_DATA_STR = "UPDATING_SUMMARY_DATA";
	public static final String FLUID_DENSITY_LABEL_STR = "FLUID_KINEMATIC_VISCOSITY_LABEL";
	public static final String FLUID_KINEMATIC_VISCOSITY_LABEL_STR = "FLUID_KINEMATIC_VISCOSITY_LABEL";
	public static final String LEFT_BANK_POSITION_LABEL_STR = "LEFT_BANK_POSITION_LABEL";
	public static final String RIGHT_BANK_POSITION_LABEL_STR = "RIGHT_BANK_POSITION_LABEL";
	public static final String WATER_DEPTH_POSITION_LABEL_STR = "WATER_DEPTH_POSITION_LABEL";
	public static final String ERROR_GETTING_CROSS_SECTION_DATA_TITLE_STR = "ERROR_GETTING_CROSS_SECTION_DATA_TITLE";
	public static final String ERROR_GETTING_CROSS_SECTION_DATA_MSG_STR = "ERROR_GETTING_CROSS_SECTION_DATA_MSG";
	public static final String INVERT_X_AXIS_LABEL_STR = "INVERT_X_AXIS_LABEL";
	public static final String INVERT_Y_AXIS_LABEL_STR = "INVERT_Y_AXIS_LABEL";
	public static final String INVERT_Z_AXIS_LABEL_STR = "INVERT_Z_AXIS_LABEL";
	public static final String LOCK_DATA_SET_LABEL_STR = "LOCK_DATA_SET_LABEL";
	public static final String RUN_INDEX_LABEL_STR = "RUN_INDEX_LABEL";
	public static final String XZ_PLANE_ROTATION_THETA_LABEL_STR = "XZ_PLANE_ROTATION_THETA_LABEL";
	public static final String YZ_PLANE_ROTATION_PHI_LABEL_STR = "YZ_PLANE_ROTATION_PHI_LABEL";
	public static final String XY_PLANE_ROTATION_ALPHA_LABEL_STR = "XY_PLANE_ROTATION_ALPHA_LABEL";
	public static final String PROBE_X_VELOCITIES_LABEL_STR = "PROBE_X_VELOCITIES_LABEL";
	public static final String PROBE_Y_VELOCITIES_LABEL_STR = "PROBE_Y_VELOCITIES_LABEL";
	public static final String PROBE_Z_VELOCITIES_LABEL_STR = "PROBE_Z_VELOCITIES_LABEL";
	public static final String MEASURED_DISCHARGE_LABEL_STR = "MEASURED_DISCHARGE_LABEL";
	public static final String SAVING_DATA_SET_STR = "SAVING_DATA_SET";
	public static final String IMPORTING_SINGLE_U_MEASUREMENTS_STR = "IMPORTING_SINGLE_U_MEASUREMENTS";
	public static final String RECALCULATING_DATA_PROGRESS_TITLE_STR = "RECALCULATING_DATA_PROGRESS_TITLE";
	public static final String CALCULATING_DATA_POINT_SUMMARY_FIELDS_TITLE_STR = "CALCULATING_DATA_POINT_SUMMARY_FIELDS_TITLE";
	public static final String CREATING_RC_BATCH_PROGRESS_TITLE_STR = "CREATING_RC_BATCH_PROGRESS_TITLE";
	public static final String ERROR_CREATING_RC_BATCH_DIALOG_TITLE_STR = "ERROR_CREATING_RC_BATCH_DIALOG_TITLE";
	public static final String ERROR_CREATING_RC_BATCH_DIALOG_MSG_STR = "ERROR_CREATING_RC_BATCH_DIALOG_MSG";
	public static final String CREATING_RC_BATCHES_PROGRESS_TITLE_STR = "CREATING_RC_BATCHES_PROGRESS_TITLE";
	public static final String ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_TITLE_STR = "ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_TITLE";
	public static final String ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_MSG_STR = "ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_MSG";
	public static final String DATA_FILES_CONFIG_TAB_LABEL_STR = "GENERAL_CONFIG_TAB_LABEL";
	public static final String DATA_SET_CONFIG_TAB_LABEL_STR = "DATA_SET_CONFIG_TAB_LABEL";
	public static final String DATA_POINT_CONFIG_TAB_LABEL_STR = "DATA_POINT_CONFIG_TAB_LABEL";
	public static final String DATA_FILE_VELOCITY_UNIT_OPTIONS_STR = "DATA_FILE_VELOCITY_UNIT_OPTIONS";
	public static final String DATA_SET_CREATION_ERROR_TITLE_STR = "DATA_SET_CREATION_ERROR_TITLE";
	public static final String DATA_SET_CREATION_ERROR_MSG_STR = "DATA_SET_CREATION_ERROR_MSG";
	public static final String DATA_FILE_VELOCITY_UNITS_LABEL_STR = "DATA_FILE_VELOCITY_UNITS_LABEL";
	public static final String VIEW_DATA_POINT_DETAILS_BUTTON_LABEL_STR = "VIEW_DATA_POINT_DETAILS_BUTTON_LABEL";
	public static final String VIEW_DATA_POINT_DETAILS_BUTTON_DESC_STR = "VIEW_DATA_POINT_DETAILS_BUTTON_DESC";
	public static final String CROSS_SECTION_GRAPHS_STR = "CROSS_SECTION_GRAPHS";
	public static final String VERTICAL_VELOCITY_GRID_TITLE_STR = "VERTICAL_VELOCITY_GRID_TITLE";
	public static final String VERTICAL_VELOCITY_GRID_X_AXIS_TITLE_STR = "VERTICAL_VELOCITY_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE_STR = "VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_VELOCITY_GRID_TITLE_STR = "HORIZONTAL_VELOCITY_GRID_TITLE";
	public static final String HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE_STR = "HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE";
	public static final String HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE_STR = "HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE";
	public static final String HORIZONTAL_VELOCITY_GRID_LEGEND_KEY_LABEL_STR = "HORIZONTAL_VELOCITY_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL_STR = "VERTICAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL";
	public static final String HORIZONTAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL_STR = "HORIZONTAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL";
	public static final String DEPTH_AVERAGED_VELOCITY_GRID_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_GRID_TITLE";
	public static final String DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE";
	public static final String DERIVATIVE_GRID_SECONDARY_Y_AXIS_TITLE_STR = "DERIVATIVE_GRID_SECONDARY_Y_AXIS_TITLE";
	public static final String LATERAL_VELOCITY_GRAPH_BUTTON_LABEL_STR = "LATERAL_VELOCITY_GRAPH_BUTTON_LABEL";
	public static final String LATERAL_VELOCITY_GRAPH_BUTTON_DESC_STR = "LATERAL_VELOCITY_GRAPH_BUTTON_DESC";
	public static final String LATERAL_VELOCITY_GRID_TITLE_STR = "LATERAL_VELOCITY_GRID_TITLE";
	public static final String DEPTH_AVERAGED_UV_GRAPH_BUTTON_LABEL_STR = "DEPTH_AVERAGED_UV_GRAPH_BUTTON_LABEL";
	public static final String DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC_STR = "DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC";
	public static final String DEPTH_AVERAGED_UV_GRAPH_TITLE_STR = "DEPTH_AVERAGED_UV_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_UV_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_UV_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_UV_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_UV_GRAPH_Y_AXIS_TITLE";
	public static final String DATA_POINT_DETAILS_FRAME_TITLE_STR = "DATA_POINT_DETAILS_FRAME_TITLE";
	public static final String VELOCITY_DETAILS_TABLE_TAB_STR = "VELOCITY_DETAILS_TABLE_TAB";
	public static final String BED_SLOPE_LABEL_STR = "BED_SLOPE_LABEL";
	public static final String CALCULATED_MEAN_SHEAR_STRESS_LABEL_STR = "CALCULATED_MEAN_SHEAR_STRESS_LABEL";
	public static final String CONSERVATION_OF_MOMENTUM_MEAN_SHEAR_STRESS_LABEL_STR = "CONSERVATION_OF_MOMENTUM_MEAN_SHEAR_STRESS_LABEL";
	public static final String UV_FLUCTUATIONS_MEAN_LABEL_STR = "UV_FLUCTUATIONS_MEAN_LABEL";
	public static final String UW_FLUCTUATIONS_MEAN_LABEL_STR = "UW_FLUCTUATIONS_MEAN_LABEL";
	public static final String DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR = "DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL";
	public static final String DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR = "DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC";
	public static final String DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_TITLE_STR = "DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_TITLE";
	public static final String DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE";
	public static final String VECTRINO_HEADER_FILE_MISSING_TITLE_STR = "VECTRINO_HEADER_FILE_MISSING_TITLE";
	public static final String VECTRINO_HEADER_FILE_MISSING_MSG_STR = "VECTRINO_HEADER_FILE_MISSING_MSG";
	public static final String INVALID_LINE_IN_VECTRINO_DATA_FILE_TITLE_STR = "INVALID_LINE_IN_VECTRINO_DATA_FILE_TITLE";
	public static final String INVALID_LINE_IN_VECTRINO_DATA_FILE_MSG_STR = "INVALID_LINE_IN_VECTRINO_DATA_FILE_MSG";
	public static final String IMPORT_BINARY_DATA_FILES_BUTTON_LABEL_STR = "IMPORT_BINARY_DATA_FILES_BUTTON_LABEL";
	public static final String IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC_STR = "IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC";
	public static final String IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_DIALOG_TITLE_STR = "IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_DIALOG_TITLE";
	public static final String IMPORT_CONVERTED_VNO_FILES_STR = "IMPORT_CONVERTED_VNO_FILES";
	public static final String IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC_STR = "IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC";
	public static final String IMPORT_MULTIPLE_CONVERTED_VECTRINO_FILES_DIALOG_TITLE_STR = "IMPORT_MULTIPLE_CONVERTED_VECTRINO_FILES_DIALOG_TITLE";
	public static final String VERTICAL_REYNOLDS_STRESS_LEGEND_TEXT_STR = "VERTICAL_REYNOLDS_STRESS_LEGEND_TEXT";
	public static final String HORIZONTAL_REYNOLDS_STRESS_LEGEND_TEXT_STR = "HORIZONTAL_REYNOLDS_STRESS_LEGEND_TEXT";
	public static final String VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC_STR = "VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC";
	public static final String VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_TITLE_STR = "VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_TITLE";
	public static final String VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_FULL_SERIES_LINEAR_EXTRAPOLATION_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_FULL_SERIES_LINEAR_EXTRAPOLATION_SERIES_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_BOTTOM_POINTS_LINEAR_EXTRAPOLATION_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_BOTTOM_POINTS_LINEAR_EXTRAPOLATION_SERIES_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_MEAN_OF_BOTTOM_THREE_POINTS_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_MEAN_OF_BOTTOM_THREE_POINTS_SERIES_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_SMOOTH_BED_LOG_LAW_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_SMOOTH_BED_LOG_LAW_SERIES_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_KS_FROM_ROUGH_BED_LOG_LAW_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_KS_FROM_ROUGH_BED_LOG_LAW_SERIES_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_10_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_10_SERIES_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_20_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_20_SERIES_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_POINTS_ABOVE_0_POINT_2LINEAR_EXTRAPOLATION_SERIES_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_POINTS_ABOVE_0_POINT_2LINEAR_EXTRAPOLATION_SERIES_LABEL";
	public static final String IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_BUTTON_DESC_STR = "IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_BUTTON_DESC";
	public static final String IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_DIALOG_TITLE_STR = "IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_DIALOG_TITLE";
	public static final String POLYSYNC_COLUMN_HEADINGS_MISSING_TITLE_STR = "POLYSYNC_COLUMN_HEADINGS_MISSING_TITLE";
	public static final String POLYSYNC_COLUMN_HEADINGS_MISSING_MSG_STR = "POLYSYNC_COLUMN_HEADINGS_MISSING_MSG";
	public static final String POLYSYNC_CORRUPT_LINE_TITLE_STR = "POLYSYNC_CORRUPT_LINE_TITLE";
	public static final String POLYSYNC_CORRUPT_LINE_MSG_STR = "POLYSYNC_CORRUPT_LINE_MSG";
	public static final String CORRECT_ROTATION_LABEL_STR = "CORRECT_ROTATION_LABEL";
	public static final String PERCENTAGE_OF_VELOCITIES_GOOD_LABEL_STR = "PERCENTAGE_OF_VELOCITIES_GOOD_LABEL";
	public static final String DESPIKING_FILTER_NONE_STR = "DESPIKING_FILTER_NONE";
	public static final String DESPIKING_FILTER_REMOVE_ZEROES_LEVEL_STR = "DESPIKING_FILTER_REMOVE_ZEROES_LEVEL";
	public static final String DESPIKING_FILTER_EXCLUDE_LEVEL_STR = "DESPIKING_FILTER_EXCLUDE_LEVEL";
	public static final String DESPIKING_FILTER_VELOCITY_CORRELATION_STR = "DESPIKING_FILTER_VELOCITY_CORRELATION";
	public static final String DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_STR = "DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING";
	public static final String DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_STR = "DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING";
	public static final String DESPIKING_FILTER_CORRELATION_AND_SNR_STR = "DESPIKING_FILTER_CORRELATION_AND_SNR";
	public static final String DESPIKING_FILTER_TYPE_LABEL_STR = "DESPIKING_FILTER_TYPE_LABEL";
	public static final String DESPIKING_FILTER_NONE_REFERENCE_STR = "DESPIKING_FILTER_NONE_REFERENCE";
	public static final String DESPIKING_FILTER_EXCLUDE_LEVEL_REFERENCE_STR = "DESPIKING_FILTER_EXCLUDE_LEVEL_REFERENCE";
	public static final String DESPIKING_FILTER_VELOCITY_CORRELATION_REFERENCE_STR = "DESPIKING_FILTER_VELOCITY_CORRELATION_REFERENCE";
	public static final String DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_REFERENCE_STR = "DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_REFERENCE";
	public static final String DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_REFERENCE_STR = "DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_REFERENCE";
	public static final String DESPIKING_FILTER_CORRELATION_AND_SNR_REFERENCE_STR = "DESPIKING_FILTER_CORRELATION_AND_SNR_REFERENCE";
	public static final String DATA_POINT_DATA_TABLE_TAB_LABEL_STR = "DATA_POINT_DATA_TABLE_TAB_LABEL";
	public static final String DATA_POINT_GRAPHS_TAB_LABEL_STR = "DATA_POINT_GRAPHS_TAB_LABEL";
	public static final String VELOCITY_LABEL_STR = "VELOCITY_LABEL";
	public static final String IMPORT_ALL_STR = "IMPORT_ALL";
	public static final String IMPORT_SELECTED_STR = "IMPORT_SELECTED";
	public static final String IMPORT_MULTI_PROBE_BINARY_FILES_BUTTON_DESC_STR = "IMPORT_MULTI_PROBE_BINARY_FILES_BUTTON_DESC";
	public static final String IMPORT_MULTI_PROBE_BINARY_FILES_DIALOG_TITLE_STR = "IMPORT_MULTI_PROBE_BINARY_FILES_DIALOG_TITLE";
	public static final String MAIN_PROBE_STR = "MAIN_PROBE";
	public static final String FIXED_PROBE_STR = "FIXED_PROBE";
	public static final String NUMBER_OF_PROBES_STR = "NUMBER_OF_PROBES";
	public static final String INVALID_FIXED_PROBE_INDEX_ERROR_TITLE_STR = "INVALID_FIXED_PROBE_INDEX_ERROR_TITLE";
	public static final String INVALID_FIXED_PROBE_INDEX_ERROR_MSG_STR = "INVALID_FIXED_PROBE_INDEX_ERROR_MSG";
	public static final String INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE_STR = "INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE";
	public static final String INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG_STR = "INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG";
	public static final String FIXED_PROBE_INDEX_LABEL_STR = "FIXED_PROBE_INDEX_LABEL";
	public static final String Y_OFFSET_STR = "Y_OFFSET";
	public static final String Z_OFFSET_STR = "Z_OFFSET";
	public static final String ERROR_OPENING_DATA_SET_DIALOG_TITLE_STR = "ERROR_OPENING_DATA_SET_DIALOG_TITLE";
	public static final String ERROR_OPENING_DATA_SET_DIALOG_MSG_STR = "ERROR_OPENING_DATA_SET_DIALOG_MSG";
	public static final String ERROR_SAVING_DATA_SET_DIALOG_TITLE_STR = "ERROR_SAVING_DATA_SET_DIALOG_TITLE";
	public static final String ERROR_SAVING_DATA_SET_DIALOG_MSG_STR = "ERROR_SAVING_DATA_SET_DIALOG_MSG";
	public static final String ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE_STR = "ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE";
	public static final String ERROR_IMPORTING_DATA_POINT_DIALOG_MSG_STR = "ERROR_IMPORTING_DATA_POINT_DIALOG_MSG";
	public static final String ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_TITLE_STR = "ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_TITLE";
	public static final String ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_MSG_STR = "ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_MSG";
	public static final String ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE_STR = "ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE";
	public static final String ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG_STR = "ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG";
	public static final String ERROR_CREATING_NEW_DATA_SET_TITLE_STR = "ERROR_CREATING_NEW_DATA_SET_TITLE";
	public static final String ERROR_CREATING_NEW_DATA_SET_MSG_STR = "ERROR_CREATING_NEW_DATA_SET_MSG";
	public static final String ERROR_LOADING_DATA_POINT_DETAILS_TITLE_STR = "ERROR_LOADING_DATA_POINT_DETAILS_TITLE";
	public static final String ERROR_LOADING_DATA_POINT_DETAILS_MSG_STR = "ERROR_LOADING_DATA_POINT_DETAILS_MSG";
	public static final String ERROR_CLEARING_DATA_POINT_DETAILS_TITLE_STR = "ERROR_CLEARING_DATA_POINT_DETAILS_TITLE";
	public static final String ERROR_CLEARING_DATA_POINT_DETAILS_MSG_STR = "ERROR_CLEARING_DATA_POINT_DETAILS_MSG";
	public static final String ERROR_GETTING_CONFIG_DATA_TITLE_STR = "ERROR_GETTING_CONFIG_DATA_TITLE";
	public static final String ERROR_GETTING_CONFIG_DATA_MSG_STR = "ERROR_GETTING_CONFIG_DATA_MSG";
	public static final String ERROR_SETTING_CONFIG_DATA_TITLE_STR = "ERROR_SETTING_CONFIG_DATA_TITLE";
	public static final String ERROR_SETTING_CONFIG_DATA_MSG_STR = "ERROR_SETTING_CONFIG_DATA_MSG";
	public static final String ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_TITLE_STR = "ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_TITLE";
	public static final String ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_MSG_STR = "ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_MSG";
	public static final String ERROR_RECALCULATING_SUMMARY_DATA_TITLE_STR = "ERROR_RECALCULATING_SUMMARY_DATA_TITLE";
	public static final String ERROR_RECALCULATING_SUMMARY_DATA_MSG_STR = "ERROR_RECALCULATING_SUMMARY_DATA_MSG";
	public static final String ERROR_GETTING_DATA_SET_INFO_TITLE_STR = "ERROR_GETTING_DATA_SET_INFO_TITLE";
	public static final String ERROR_GETTING_DATA_SET_INFO_MSG_STR = "ERROR_GETTING_DATA_SET_INFO_MSG";
	public static final String ERROR_DATA_SET_ALREADY_OPEN_TITLE_STR = "ERROR_DATA_SET_ALREADY_OPEN_TITLE";
	public static final String ERROR_DATA_SET_ALREADY_OPEN_MSG_STR = "ERROR_DATA_SET_ALREADY_OPEN_MSG";
	public static final String ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_TITLE_STR = "ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_TITLE";
	public static final String ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_MSG_STR = "ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_MSG";
	public static final String ERROR_CLOSING_DATA_SET_TITLE_STR = "ERROR_CLOSING_DATA_SET_TITLE";
	public static final String ERROR_CLOSING_DATA_SET_MSG_STR = "ERROR_CLOSING_DATA_SET_MSG";
	public static final String MAIN_PROBE_INDEX_LABEL_STR = "MAIN_PROBE_INDEX_LABEL";
	public static final String INVALID_MAIN_PROBE_INDEX_ERROR_TITLE_STR = "INVALID_MAIN_PROBE_INDEX_ERROR_TITLE";
	public static final String INVALID_MAIN_PROBE_INDEX_ERROR_MSG_STR = "INVALID_MAIN_PROBE_INDEX_ERROR_MSG";
	public static final String INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_TITLE_STR = "INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_TITLE";
	public static final String INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_MSG_STR = "INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_MSG";
	public static final String MAIN_DATA_POINTS_OVERVIEW_DISPLAY_LABEL_STR = "MAIN_DATA_POINTS_OVERVIEW_DISPLAY_LABEL";
	public static final String CONFIGURATION_TAB_LABEL_STR = "CONFIGURATION_TAB_LABEL";
	public static final String VERTICAL_U_PRIME_V_PRIME_GRID_TITLE_STR = "VERTICAL_U_PRIME_V_PRIME_GRID_TITLE";
	public static final String VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE_STR = "VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE_STR = "VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_U_PRIME_W_PRIME_GRID_TITLE_STR = "VERTICAL_U_PRIME_W_PRIME_GRID_TITLE";
	public static final String VERTICAL_U_PRIME_W_PRIME_GRID_X_AXIS_TITLE_STR = "VERTICAL_U_PRIME_W_PRIME_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_U_PRIME_W_PRIME_GRID_Y_AXIS_TITLE_STR = "VERTICAL_U_PRIME_W_PRIME_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_U_PRIME_W_PRIME_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_U_PRIME_W_PRIME_GRID_LEGEND_KEY_LABEL";
	public static final String QUADRANT_HOLE_GRAPH_TAB_LABEL_STR = "QUADRANT_HOLE_GRAPH_TAB_LABEL";
	public static final String QUADRANT_HOLE_GRAPH_TITLE_STR = "QUADRANT_HOLE_GRAPH_TITLE";
	public static final String QUADRANT_HOLE_GRAPH_X_AXIS_LABEL_STR = "QUADRANT_HOLE_GRAPH_X_AXIS_LABEL";
	public static final String QUADRANT_HOLE_GRAPH_Y_AXIS_LABEL_STR = "QUADRANT_HOLE_GRAPH_Y_AXIS_LABEL";
	public static final String QUADRANT_HOLE_SHORTHAND_LABEL_STR = "QUADRANT_HOLE_SHORTHAND_LABEL";
	public static final String QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TAB_LABEL_STR = "QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TAB_LABEL";
	public static final String QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TITLE_STR = "QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TITLE";
	public static final String QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL_STR = "QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL";
	public static final String QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL_STR = "QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL";
	public static final String QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TAB_LABEL_STR = "QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TAB_LABEL";
	public static final String QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TITLE_STR = "QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TITLE";
	public static final String QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL_STR = "QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL";
	public static final String QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL_STR = "QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL";
	public static final String QUADRANT_HOLE_CMSD_DURATION_GRAPH_TAB_LABEL_STR = "QUADRANT_HOLE_CMSD_DURATION_GRAPH_TAB_LABEL";
	public static final String QUADRANT_HOLE_CMSD_DURATION_GRAPH_TITLE_STR = "QUADRANT_HOLE_CMSD_DURATION_GRAPH_TITLE";
	public static final String QUADRANT_HOLE_CMSD_DURATION_GRAPH_X_AXIS_LABEL_STR = "QUADRANT_HOLE_CMSD_DURATION_GRAPH_X_AXIS_LABEL";
	public static final String QUADRANT_HOLE_CMSD_DURATION_GRAPH_Y_AXIS_LABEL_STR = "QUADRANT_HOLE_CMSD_DURATION_GRAPH_Y_AXIS_LABEL";
	public static final String QUADRANT_1_LABEL_STR = "QUADRANT_1_LABEL";
	public static final String QUADRANT_2_LABEL_STR = "QUADRANT_2_LABEL";
	public static final String QUADRANT_3_LABEL_STR = "QUADRANT_3_LABEL";
	public static final String QUADRANT_4_LABEL_STR = "QUADRANT_4_LABEL";
	public static final String QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_TITLE_STR = "QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_TITLE";
	public static final String QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_Y_AXIS_LABEL_STR = "QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_Y_AXIS_LABEL";
	public static final String CALCULATE_CORRELATION_BUTTON_LABEL_STR = "CALCULATE_CORRELATION_BUTTON_LABEL";
	public static final String CALCULATE_CORRELATION_BUTTON_DESC_STR = "CALCULATE_CORRELATION_BUTTON_DESC";
	public static final String CALCULATE_CORRELATION_ERROR_TITLE_STR = "CALCULATE_CORRELATION_ERROR_TITLE";
	public static final String CALCULATE_CORRELATION_ERROR_MSG_STR = "CALCULATE_CORRELATION_ERROR_MSG";
	public static final String SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR = "SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL";
	public static final String SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR = "SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC";
	public static final String UW_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR = "UW_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE";
	public static final String UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR = "UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE";
	public static final String UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE_STR = "UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE";
	public static final String UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE_STR = "UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE";
	public static final String UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE_STR = "UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE";
	public static final String UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL_STR = "UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL";
	public static final String UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC_STR = "UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC";
	public static final String UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL_STR = "UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL";
	public static final String SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR = "SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL";
	public static final String SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR = "SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC";
	public static final String UV_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR = "UV_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE";
	public static final String UV_NORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR = "UV_NORMALISED_QUADRANT_HOLE_FRAME_TITLE";
	public static final String UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE_STR = "UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE";
	public static final String UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE_STR = "UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE";
	public static final String UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE_STR = "UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE";
	public static final String UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL_STR = "UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL";
	public static final String UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC_STR = "UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC";
	public static final String UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL_STR = "UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL";
	public static final String LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_LABEL_STR = "LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_LABEL";
	public static final String LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC_STR = "LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC";
	public static final String LATERAL_TKE_FLUX_GRID_TITLE_STR = "LATERAL_TKE_FLUX_GRID_TITLE";
	public static final String TURBULENCE_INTENSITY_GRAPH_BUTTON_LABEL_STR = "TURBULENCE_INTENSITY_GRAPH_BUTTON_LABEL";
	public static final String TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC_STR = "TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC";
	public static final String TURBULENCE_INTENSITY_GRAPH_FRAME_TITLE_STR = "TURBULENCE_INTENSITY_GRAPH_FRAME_TITLE";
	public static final String X_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE_STR = "X_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE";
	public static final String Y_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE_STR = "Y_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE";
	public static final String Z_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE_STR = "Z_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE";
	public static final String X_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE_STR = "X_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE";
	public static final String Y_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE_STR = "Y_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE";
	public static final String Z_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE_STR = "Z_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE";
	public static final String TURBULENCE_INTENSITY_GRAPH_LEGEND_TEXT_STR = "TURBULENCE_INTENSITY_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR = "VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL";
	public static final String VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR = "VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC";
	public static final String VERTICAL_REYNOLDS_STRESS_GRAPH_TITLE_STR = "VERTICAL_REYNOLDS_STRESS_GRAPH_TITLE";
	public static final String HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR = "HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL";
	public static final String HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR = "HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC";
	public static final String HORIZONTAL_REYNOLDS_STRESS_GRAPH_TITLE_STR = "HORIZONTAL_REYNOLDS_STRESS_GRAPH_TITLE";
	public static final String V_PRIME_W_PRIME_GRAPH_BUTTON_LABEL_STR = "V_PRIME_W_PRIME_GRAPH_BUTTON_LABEL";
	public static final String V_PRIME_W_PRIME_GRAPH_BUTTON_DESC_STR = "V_PRIME_W_PRIME_GRAPH_BUTTON_DESC";
	public static final String V_PRIME_W_PRIME_GRAPH_TITLE_STR = "V_PRIME_W_PRIME_GRAPH_TITLE";
	public static final String V_PRIME_W_PRIME_GRAPH_LEGEND_TEXT_STR = "V_PRIME_W_PRIME_GRAPH_LEGEND_TEXT";
	public static final String UW_CORRELATION_GRAPH_BUTTON_LABEL_STR = "UW_CORRELATION_GRAPH_BUTTON_LABEL";
	public static final String UW_CORRELATION_GRAPH_BUTTON_DESC_STR = "UW_CORRELATION_GRAPH_BUTTON_DESC";
	public static final String UW_CORRELATION_GRAPH_TITLE_STR = "UW_CORRELATION_GRAPH_TITLE";
	public static final String UW_CORRELATION_GRAPH_LEGEND_TEXT_STR = "UW_CORRELATION_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_UW_CORRELATION_GRAPH_TITLE_STR = "VERTICAL_UW_CORRELATION_GRAPH_TITLE";
	public static final String VERTICAL_UW_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_UW_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_UW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_UW_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_UW_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_UW_CORRELATION_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_UW_CORRELATION_GRAPH_TITLE_STR = "DEPTH_AVERAGED_UW_CORRELATION_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_UW_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_UW_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_UW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_UW_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String UV_CORRELATION_GRAPH_BUTTON_LABEL_STR = "UV_CORRELATION_GRAPH_BUTTON_LABEL";
	public static final String UV_CORRELATION_GRAPH_BUTTON_DESC_STR = "UV_CORRELATION_GRAPH_BUTTON_DESC";
	public static final String UV_CORRELATION_GRAPH_TITLE_STR = "UV_CORRELATION_GRAPH_TITLE";
	public static final String UV_CORRELATION_GRAPH_LEGEND_TEXT_STR = "UV_CORRELATION_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_UV_CORRELATION_GRAPH_TITLE_STR = "VERTICAL_UV_CORRELATION_GRAPH_TITLE";
	public static final String VERTICAL_UV_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_UV_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_UV_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_UV_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_UV_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_UV_CORRELATION_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_UV_CORRELATION_GRAPH_TITLE_STR = "DEPTH_AVERAGED_UV_CORRELATION_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_UV_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_UV_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_UV_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_UV_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String VW_CORRELATION_GRAPH_BUTTON_LABEL_STR = "VW_CORRELATION_GRAPH_BUTTON_LABEL";
	public static final String VW_CORRELATION_GRAPH_BUTTON_DESC_STR = "VW_CORRELATION_GRAPH_BUTTON_DESC";
	public static final String VW_CORRELATION_GRAPH_TITLE_STR = "VW_CORRELATION_GRAPH_TITLE";
	public static final String VW_CORRELATION_GRAPH_LEGEND_TEXT_STR = "VW_CORRELATION_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_VW_CORRELATION_GRAPH_TITLE_STR = "VERTICAL_VW_CORRELATION_GRAPH_TITLE";
	public static final String VERTICAL_VW_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_VW_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_VW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_VW_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_VW_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_VW_CORRELATION_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_VW_CORRELATION_GRAPH_TITLE_STR = "DEPTH_AVERAGED_VW_CORRELATION_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_VW_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VW_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VW_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String REYNOLDS_STRESS_GRAPHS_STR = "REYNOLDS_STRESS_GRAPHS";
	public static final String TI_AND_TKE_AND_VORTICITY_GRAPHS_STR = "TI_AND_TKE_AND_VORTICITY_GRAPHS";
	public static final String THIRD_ORDER_CORRELATION_GRAPHS_STR = "THIRD_ORDER_CORRELATION_GRAPHS";
	public static final String THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_LABEL_STR = "THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_LABEL";
	public static final String THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC_STR = "THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC";
	public static final String THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_LABEL_STR = "THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_LABEL";
	public static final String THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC_STR = "THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC";
	public static final String THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_LABEL_STR = "THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_LABEL";
	public static final String THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC_STR = "THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC";
	public static final String THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_LABEL_STR = "THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_LABEL";
	public static final String THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC_STR = "THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC";
	public static final String THIRD_ORDER_CORRELATION_GRID_300_TITLE_STR = "THIRD_ORDER_CORRELATION_GRID_300_TITLE";
	public static final String THIRD_ORDER_CORRELATION_GRID_210_TITLE_STR = "THIRD_ORDER_CORRELATION_GRID_210_TITLE";
	public static final String THIRD_ORDER_CORRELATION_GRID_120_TITLE_STR = "THIRD_ORDER_CORRELATION_GRID_120_TITLE";
	public static final String THIRD_ORDER_CORRELATION_GRID_030_TITLE_STR = "THIRD_ORDER_CORRELATION_GRID_030_TITLE";
	public static final String THIRD_ORDER_CORRELATION_GRID_X_AXIS_TITLE_STR = "THIRD_ORDER_CORRELATION_GRID_X_AXIS_TITLE";
	public static final String THIRD_ORDER_CORRELATION_GRID_Y_AXIS_TITLE_STR = "THIRD_ORDER_CORRELATION_GRID_Y_AXIS_TITLE";
	public static final String THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT_STR = "THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT";
	public static final String EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL_STR = "EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL";
	public static final String EXPORT_GRAPH_BUTTON_DESC_STR = "EXPORT_GRAPH_BUTTON_DESC";
	public static final String EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL_STR = "EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL";
	public static final String EXPORT_GRAPH_FOR_MATLAB_BUTTON_DESC_STR = "EXPORT_GRAPH_FOR_MATLAB_BUTTON_DESC";
	public static final String SPECTRAL_DISTRIBUTION_LABEL_STR = "SPECTRAL_DISTRIBUTION_LABEL";
	public static final String QH_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "QH_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR = "QH_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE";
	public static final String QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "QH_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR = "QH_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE";
	public static final String QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT_STR = "QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT";
	public static final String QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT_STR = "QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT";
	public static final String QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR = "QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE";
	public static final String QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR = "QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE";
	public static final String QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT_STR = "QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT";
	public static final String QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC";
	public static final String TKE_FLUX_GRAPH_BUTTON_LABEL_STR = "TKE_FLUX_GRAPH_BUTTON_LABEL";
	public static final String TKE_FLUX_GRAPH_BUTTON_DESC_STR = "TKE_FLUX_GRAPH_BUTTON_DESC";
	public static final String TKE_FLUX_GRAPH_FRAME_TITLE_STR = "TKE_FLUX_GRAPH_FRAME_TITLE";
	public static final String X_DIRECTION_TKE_FLUX_GRAPH_TITLE_STR = "X_DIRECTION_TKE_FLUX_GRAPH_TITLE";
	public static final String Y_DIRECTION_TKE_FLUX_GRAPH_TITLE_STR = "Y_DIRECTION_TKE_FLUX_GRAPH_TITLE";
	public static final String Z_DIRECTION_TKE_FLUX_GRAPH_TITLE_STR = "Z_DIRECTION_TKE_FLUX_GRAPH_TITLE";
	public static final String TKE_FLUX_GRAPH_LEGEND_TEXT_STR = "TKE_FLUX_GRAPH_LEGEND_TEXT";
	public static final String SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL_STR = "SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL";
	public static final String SHOW_SPECTRAL_DISTRIBUTION_BUTTON_DESC_STR = "SHOW_SPECTRAL_DISTRIBUTION_BUTTON_DESC";
	public static final String SPECTRAL_DISTRIBUTION_GRAPH_FRAME_TITLE_STR = "SPECTRAL_DISTRIBUTION_GRAPH_FRAME_TITLE";
	public static final String SPECTRAL_DISTRIBUTION_GRAPH_TITLE_STR = "SPECTRAL_DISTRIBUTION_GRAPH_TITLE";
	public static final String SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL_STR = "SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL";
	public static final String SPECTRAL_DISTRIBUTION_GRAPH_Y_AXIS_LABEL_STR = "SPECTRAL_DISTRIBUTION_GRAPH_Y_AXIS_LABEL";
	public static final String BACKWARD_COMPATIBILITY_MISSING_DATA_TITLE_STR = "BACKWARD_COMPATIBILITY_MISSING_DATA_TITLE";
	public static final String BACKWARD_COMPATIBILITY_MISSING_DATA_MSG_STR = "BACKWARD_COMPATIBILITY_MISSING_DATA_MSG";
	public static final String REMOVE_DATA_POINTS_ERROR_TITLE_STR = "REMOVE_DATA_POINTS_ERROR_TITLE";
	public static final String REMOVE_DATA_POINTS_ERROR_MSG_STR = "REMOVE_DATA_POINTS_ERROR_MSG";
	public static final String REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_TITLE_STR = "REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_TITLE";
	public static final String REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_MSG_STR = "REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_MSG";
	public static final String REMOVE_DATA_POINTS_BUTTON_LABEL_STR = "REMOVE_DATA_POINTS_BUTTON_LABEL";
	public static final String REMOVE_DATA_POINTS_BUTTON_DESC_STR = "REMOVE_DATA_POINTS_BUTTON_DESC";
	public static final String CALCULATE_EOF_R_MATRIX_BUTTON_LABEL_STR = "CALCULATE_EOF_R_MATRIX_BUTTON_LABEL";
	public static final String CALCULATE_EOF_R_MATRIX_BUTTON_DESC_STR = "CALCULATE_EOF_R_MATRIX_BUTTON_DESC";
	public static final String CALCULATE_EOF_R_MATRIX_DIALOG_TITLE_STR = "CALCULATE_EOF_R_MATRIX_DIALOG_TITLE";
	public static final String EOF_R_MATRIX_DIALOG_Y1_LABEL_STR = "EOF_R_MATRIX_DIALOG_Y1_LABEL";
	public static final String EOF_R_MATRIX_DIALOG_Y2_LABEL_STR = "EOF_R_MATRIX_DIALOG_Y2_LABEL";
	public static final String EOF_R_MATRIX_DIALOG_Z1_LABEL_STR = "EOF_R_MATRIX_DIALOG_Z1_LABEL";
	public static final String EOF_R_MATRIX_DIALOG_Z2_LABEL_STR = "EOF_R_MATRIX_DIALOG_Z2_LABEL";
	public static final String EOF_R_MATRIX_DIALOG_TITLE_STR = "EOF_R_MATRIX_DIALOG_TITLE";
	public static final String EOF_R_MATRIX_INCOMPLETE_GRID_TITLE_STR = "EOF_R_MATRIX_INCOMPLETE_GRID_TITLE";
	public static final String EOF_R_MATRIX_INCOMPLETE_GRID_MSG_STR = "EOF_R_MATRIX_INCOMPLETE_GRID_MSG";
	public static final String CALCULATE_DATA_POINT_SUMMARY_DATA_STR = "CALCULATE_DATA_POINT_SUMMARY_DATA";
	public static final String CALCULATE_DATA_POINT_SUMMARY_DATA_DESC_STR = "CALCULATE_DATA_POINT_SUMMARY_DATA_DESC";
	public static final String CALCULATE_DPS_REYNOLDS_STRESSES_STR = "CALCULATE_DPS_REYNOLDS_STRESSES";
	public static final String CALCULATE_DPS_REYNOLDS_STRESSES_DESC_STR = "CALCULATE_DPS_REYNOLDS_STRESSES_DESC";
	public static final String CALCULATE_DPS_QH_DATA_STR = "CALCULATE_DPS_QH_DATA";
	public static final String CALCULATE_DPS_QH_DATA_DESC_STR = "CALCULATE_DPS_QH_DATA_DESC";
	public static final String CALCULATE_DPS_TKE_DATA_STR = "CALCULATE_DPS_TKE_DATA";
	public static final String CALCULATE_DPS_TKE_DATA_DESC_STR = "CALCULATE_DPS_TKE_DATA_DESC";
	public static final String CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_STR = "CALCULATE_DPS_FIXED_PROBE_CORRELATIONS";
	public static final String CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_DESC_STR = "CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_DESC";
	public static final String CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL_STR = "CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL";
	public static final String CREATE_ROTATION_CORRECTION_BATCH_BUTTON_DESC_STR = "CREATE_ROTATION_CORRECTION_BATCH_BUTTON_DESC";
	public static final String ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_TITLE_STR = "ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_TITLE";
	public static final String ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_MSG_STR = "ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_MSG";
	public static final String SELECTION_TOOL_BUTTON_LABEL_STR = "SELECTION_TOOL_BUTTON_LABEL";
	public static final String SELECTION_TOOL_BUTTON_DESC_STR = "SELECTION_TOOL_BUTTON_DESC";
	public static final String Y_COORD_MIN_LABEL_STR = "Y_COORD_MIN_LABEL";
	public static final String Y_COORD_MAX_LABEL_STR = "Y_COORD_MAX_LABEL";
	public static final String Z_COORD_MIN_LABEL_STR = "Z_COORD_MIN_LABEL";
	public static final String Z_COORD_MAX_LABEL_STR = "Z_COORD_MAX_LABEL";
	public static final String SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL_STR = "SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL";
	public static final String SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL_STR = "SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL";
	public static final String BATCH_THETA_ROTATION_CORRECTION_COLUMN_TITLE_STR = "BATCH_THETA_ROTATION_CORRECTION_COLUMN_TITLE";
	public static final String BATCH_ALPHA_ROTATION_CORRECTION_COLUMN_TITLE_STR = "BATCH_ALPHA_ROTATION_CORRECTION_COLUMN_TITLE";
	public static final String BATCH_PHI_ROTATION_CORRECTION_COLUMN_TITLE_STR = "BATCH_PHI_ROTATION_CORRECTION_COLUMN_TITLE";
	public static final String BATCH_ROTATION_CORRECTION_BATCH_NUMBER_COLUMN_TITLE_STR = "BATCH_ROTATION_CORRECTION_BATCH_NUMBER_COLUMN_TITLE";
	public static final String CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL_STR = "CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL";
	public static final String CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_DESC_STR = "CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_DESC";
	public static final String ROTATION_CORRECTION_BATCHES_FILE_MISSING_TITLE_STR = "ROTATION_CORRECTION_BATCHES_FILE_MISSING_TITLE";
	public static final String ROTATION_CORRECTION_BATCHES_FILE_MISSING_MSG_STR = "ROTATION_CORRECTION_BATCHES_FILE_MISSING_MSG";
	public static final String ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_TITLE_STR = "ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_TITLE";
	public static final String ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_MSG_STR = "ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_MSG";
	public static final String VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_LABEL_STR = "VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_LABEL";
	public static final String VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_DESC_STR = "VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_DESC";
	public static final String VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_LABEL_STR = "VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_LABEL";
	public static final String VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_DESC_STR = "VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_DESC";
	public static final String VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_LABEL_STR = "VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_LABEL";
	public static final String VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_DESC_STR = "VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_DESC";
	public static final String VERTICAL_VELOCITY_U_ST_DEV_GRID_TITLE_STR = "VERTICAL_VELOCITY_U_ST_DEV_GRID_TITLE";
	public static final String VERTICAL_VELOCITY_U_ST_DEV_GRID_X_AXIS_TITLE_STR = "VERTICAL_VELOCITY_U_ST_DEV_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_U_ST_DEV_GRID_Y_AXIS_TITLE_STR = "VERTICAL_VELOCITY_U_ST_DEV_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_U_ST_DEV_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_VELOCITY_U_ST_DEV_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_VELOCITY_V_ST_DEV_GRID_TITLE_STR = "VERTICAL_VELOCITY_V_ST_DEV_GRID_TITLE";
	public static final String VERTICAL_VELOCITY_V_ST_DEV_GRID_X_AXIS_TITLE_STR = "VERTICAL_VELOCITY_V_ST_DEV_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_V_ST_DEV_GRID_Y_AXIS_TITLE_STR = "VERTICAL_VELOCITY_V_ST_DEV_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_V_ST_DEV_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_VELOCITY_V_ST_DEV_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_VELOCITY_W_ST_DEV_GRID_TITLE_STR = "VERTICAL_VELOCITY_W_ST_DEV_GRID_TITLE";
	public static final String VERTICAL_VELOCITY_W_ST_DEV_GRID_X_AXIS_TITLE_STR = "VERTICAL_VELOCITY_W_ST_DEV_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_W_ST_DEV_GRID_Y_AXIS_TITLE_STR = "VERTICAL_VELOCITY_W_ST_DEV_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_VELOCITY_W_ST_DEV_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_VELOCITY_W_ST_DEV_GRID_LEGEND_KEY_LABEL";
	public static final String SHOW_PDF_BUTTON_LABEL_STR = "SHOW_PDF_BUTTON_LABEL";
	public static final String SHOW_PDF_BUTTON_DESC_STR = "SHOW_PDF_BUTTON_DESC";
	public static final String U_PDF_GRAPH_TITLE_STR = "U_PDF_GRAPH_TITLE";
	public static final String PDF_GRAPH_FRAME_TITLE_STR = "PDF_GRAPH_FRAME_TITLE";
	public static final String U_PDF_GRAPH_X_AXIS_LABEL_STR = "U_PDF_GRAPH_X_AXIS_LABEL";
	public static final String U_PDF_GRAPH_Y_AXIS_LABEL_STR = "U_PDF_GRAPH_Y_AXIS_LABEL";
	public static final String V_PDF_GRAPH_TITLE_STR = "V_PDF_GRAPH_TITLE";
	public static final String V_PDF_GRAPH_X_AXIS_LABEL_STR = "V_PDF_GRAPH_X_AXIS_LABEL";
	public static final String V_PDF_GRAPH_Y_AXIS_LABEL_STR = "V_PDF_GRAPH_Y_AXIS_LABEL";
	public static final String W_PDF_GRAPH_TITLE_STR = "W_PDF_GRAPH_TITLE";
	public static final String W_PDF_GRAPH_X_AXIS_LABEL_STR = "W_PDF_GRAPH_X_AXIS_LABEL";
	public static final String W_PDF_GRAPH_Y_AXIS_LABEL_STR = "W_PDF_GRAPH_Y_AXIS_LABEL";
	public static final String GAUSSIAN_STR = "GAUSSIAN";
	public static final String QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "QH_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR = "QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE";
	public static final String QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "QH_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR = "QH_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE";
	public static final String QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR = "QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE";
	public static final String QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR = "QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE";
	public static final String DIMENSIONLESS_AXES_LABEL_STR = "DIMENSIONLESS_AXES_LABEL";
	public static final String DIMENSIONLESS_X_AXIS_LABEL_STR = "DIMENSIONLESS_X_AXIS_LABEL";
	public static final String DIMENSIONLESS_Y_AXIS_LABEL_STR = "DIMENSIONLESS_Y_AXIS_LABEL";
	public static final String DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_LABEL_STR = "DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_LABEL";
	public static final String DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC_STR = "DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC";
	public static final String DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_FRAME_TITLE_STR = "DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_FRAME_TITLE";
	public static final String DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_TITLE_STR = "DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_TITLE";
	public static final String CALCULATE_DPS_ALL_LABEL_STR = "CALCULATE_DPS_ALL_LABEL";
	public static final String CALCULATE_DPS_ALL_DESC_STR = "CALCULATE_DPS_ALL_DESC";
	public static final String COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_LABEL_STR = "COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_LABEL";
	public static final String COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC_STR = "COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC";
	public static final String COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE_STR = "COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE";
	public static final String HORIZONTAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT_STR = "HORIZONTAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT_STR = "VERTICAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT";
	public static final String COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_LEGEND_TEXT_STR = "COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_LEGEND_TEXT";
	public static final String QH_GRAPHS_STR = "QH_GRAPHS";
	public static final String VELOCITY_GRAPHS_STR = "VELOCITY_GRAPHS";
	public static final String STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_LABEL_STR = "STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_LABEL";
	public static final String STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC_STR = "STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC";
	public static final String STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE_STR = "STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE";
	public static final String U_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE_STR = "U_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE";
	public static final String U_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT_STR = "U_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT";
	public static final String V_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE_STR = "V_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE";
	public static final String V_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT_STR = "V_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT";
	public static final String W_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE_STR = "W_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE";
	public static final String W_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT_STR = "W_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT";
	public static final String VERTICAL_SECTION_GRAPH_BUTTON_LABEL_STR = "VERTICAL_SECTION_GRAPH_BUTTON_LABEL";
	public static final String VERTICAL_SECTION_GRAPH_BUTTON_DESC_STR = "VERTICAL_SECTION_GRAPH_BUTTON_DESC";
	public static final String HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL_STR = "HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL";
	public static final String HORIZONTAL_SECTION_GRAPH_BUTTON_DESC_STR = "HORIZONTAL_SECTION_GRAPH_BUTTON_DESC";
	public static final String STREAMWISE_VORTICITY_GRAPH_BUTTON_LABEL_STR = "STREAMWISE_VORTICITY_GRAPH_BUTTON_LABEL";
	public static final String STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC_STR = "STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC";
	public static final String STREAMWISE_VORTICITY_GRID_FRAME_TITLE_STR = "STREAMWISE_VORTICITY_GRID_FRAME_TITLE";
	public static final String STREAMWISE_VORTICITY_GRID_TITLE_STR = "STREAMWISE_VORTICITY_GRID_TITLE";
	public static final String STREAMWISE_VORTICITY_GRID_LEGEND_TEXT_STR = "STREAMWISE_VORTICITY_GRID_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_LEGEND_KEY_LABEL";
	public static final String RESULTANT_SHEAR_STRESS_GRID_TITLE_STR = "RESULTANT_SHEAR_STRESS_GRID_TITLE";
	public static final String RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR = "RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL";
	public static final String RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR = "RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC";
	public static final String DEPTH_AVERAGED_GRAPH_BUTTON_LABEL_STR = "DEPTH_AVERAGED_GRAPH_BUTTON_LABEL";
	public static final String DEPTH_AVERAGED_GRAPH_BUTTON_DESC_STR = "DEPTH_AVERAGED_GRAPH_BUTTON_DESC";
	public static final String EXTRAPOLATED_DEPTH_AVERAGED_GRAPH_LABEL_STR = "EXTRAPOLATED_DEPTH_AVERAGED_GRAPH_LABEL";
	public static final String POPULATED_CELL_DEPTH_AVERAGED_GRAPH_LABEL_STR = "POPULATED_CELL_DEPTH_AVERAGED_GRAPH_LABEL";
	public static final String DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_TITLE_STR = "DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_TITLE";
	public static final String DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE_STR = "DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE";
	public static final String DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_Y_AXIS_TITLE";
	public static final String TURBULENCE_INTENSITY_GRID_TITLE_STR = "TURBULENCE_INTENSITY_GRID_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY__GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY__GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_TITLE_STR = "DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_TITLE";
	public static final String DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_TITLE_STR = "DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_TITLE";
	public static final String DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_TITLE_STR = "DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_TITLE";
	public static final String DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE";
	public static final String RELATIVE_TO_MEAN_CHECKBOX_LABEL_STR = "RELATIVE_TO_MEAN_CHECKBOX_LABEL";
	public static final String DIMENSIONLESS_CHECKBOX_LABEL_STR = "DIMENSIONLESS_CHECKBOX_LABEL";
	public static final String VORTICITY_EQUATION_TERM_1_LABEL_STR = "VORTICITY_EQUATION_TERM_1_LABEL";
	public static final String VORTICITY_EQUATION_TERM_2_LABEL_STR = "VORTICITY_EQUATION_TERM_2_LABEL";
	public static final String VORTICITY_EQUATION_TERM_3_LABEL_STR = "VORTICITY_EQUATION_TERM_3_LABEL";
	public static final String VORTICITY_EQUATION_TERM_4_LABEL_STR = "VORTICITY_EQUATION_TERM_4_LABEL";
	public static final String VORTICITY_EQUATION_TERM_5_LABEL_STR = "VORTICITY_EQUATION_TERM_5_LABEL";
	public static final String VORTICITY_EQUATION_TERMS_FRAME_TITLE_STR = "VORTICITY_EQUATION_TERMS_FRAME_TITLE";
	public static final String VORTICITY_EQUATION_TERM_1_GRAPH_TITLE_STR = "VORTICITY_EQUATION_TERM_1_GRAPH_TITLE";
	public static final String VORTICITY_EQUATION_TERM_1_LEGEND_TEXT_STR = "VORTICITY_EQUATION_TERM_1_LEGEND_TEXT";
	public static final String VORTICITY_EQUATION_TERM_2_GRAPH_TITLE_STR = "VORTICITY_EQUATION_TERM_2_GRAPH_TITLE";
	public static final String VORTICITY_EQUATION_TERM_2_LEGEND_TEXT_STR = "VORTICITY_EQUATION_TERM_2_LEGEND_TEXT";
	public static final String VORTICITY_EQUATION_TERM_3_GRAPH_TITLE_STR = "VORTICITY_EQUATION_TERM_3_GRAPH_TITLE";
	public static final String VORTICITY_EQUATION_TERM_3_LEGEND_TEXT_STR = "VORTICITY_EQUATION_TERM_3_LEGEND_TEXT";
	public static final String VORTICITY_EQUATION_TERM_4_GRAPH_TITLE_STR = "VORTICITY_EQUATION_TERM_4_GRAPH_TITLE";
	public static final String VORTICITY_EQUATION_TERM_4_LEGEND_TEXT_STR = "VORTICITY_EQUATION_TERM_4_LEGEND_TEXT";
	public static final String VORTICITY_EQUATION_TERM_5_GRAPH_TITLE_STR = "VORTICITY_EQUATION_TERM_5_GRAPH_TITLE";
	public static final String VORTICITY_EQUATION_TERM_5_LEGEND_TEXT_STR = "VORTICITY_EQUATION_TERM_5_LEGEND_TEXT";
	public static final String VORTICITY_EQUATION_GRAPHS_BUTTON_LABEL_STR = "VORTICITY_EQUATION_GRAPHS_BUTTON_LABEL";
	public static final String VORTICITY_EQUATION_GRAPHS_BUTTON_DESC_STR = "VORTICITY_EQUATION_GRAPHS_BUTTON_DESC";
	public static final String LAST_MODIFIED_LABEL_STR = "LAST_MODIFIED_LABEL";
	public static final String COMMENTS_LABEL_STR = "COMMENTS_LABEL";
	public static final String MATLAB_XY_OUTPUT_TEXT_STR = "MATLAB_XY_OUTPUT_TEXT";
	public static final String MATLAB_XYZ_OUTPUT_TEXT_STR = "MATLAB_XYZ_OUTPUT_TEXT";
	public static final String WATER_TEMPERATURE_LABEL_STR = "WATER_TEMPERATURE_LABEL";
	public static final String DEFAULT_EXPORT_FILE_PATH_LABEL_STR = "DEFAULT_EXPORT_FILE_PATH_LABEL";
	public static final String SELECT_EXPORT_FILE_PATH_DIALOG_TITLE_STR = "SELECT_EXPORT_FILE_PATH_DIALOG_TITLE";
	public static final String USE_DATA_FILE_PATH_FOR_ALL_LABEL_STR = "USE_DATA_FILE_PATH_FOR_ALL_LABEL";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL";
	public static final String VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_U_TKE_FLUX_GRID_TITLE_STR = "DEPTH_AVERAGED_U_TKE_FLUX_GRID_TITLE";
	public static final String DEPTH_AVERAGED_U_TKE_FLUX_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_U_TKE_FLUX_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_U_TKE_FLUX_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_U_TKE_FLUX_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_V_TKE_FLUX_GRID_TITLE_STR = "DEPTH_AVERAGED_V_TKE_FLUX_GRID_TITLE";
	public static final String DEPTH_AVERAGED_V_TKE_FLUX_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_V_TKE_FLUX_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_V_TKE_FLUX_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_V_TKE_FLUX_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_W_TKE_FLUX_GRID_TITLE_STR = "DEPTH_AVERAGED_W_TKE_FLUX_GRID_TITLE";
	public static final String DEPTH_AVERAGED_W_TKE_FLUX_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_W_TKE_FLUX_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_W_TKE_FLUX_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_W_TKE_FLUX_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE";
	public static final String VIEW_PERCENTAGE_GOOD_BUTTON_LABEL_STR = "VIEW_PERCENTAGE_GOOD_BUTTON_LABEL";
	public static final String VIEW_PERCENTAGE_GOOD_BUTTON_DESC_STR = "VIEW_PERCENTAGE_GOOD_BUTTON_DESC";
	public static final String STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_LABEL_STR = "STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_LABEL";
	public static final String STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC_STR = "STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC";
	public static final String STREAMWISE_VORTICITY_KE_GRID_FRAME_TITLE_STR = "STREAMWISE_VORTICITY_KE_GRID_FRAME_TITLE";
	public static final String STREAMWISE_VORTICITY_KE_GRID_TITLE_STR = "STREAMWISE_VORTICITY_KE_GRID_TITLE";
	public static final String STREAMWISE_VORTICITY_KE_GRID_LEGEND_TEXT_STR = "STREAMWISE_VORTICITY_KE_GRID_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_TITLE_STR = "DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_TITLE";
	public static final String DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE";
	public static final String PERCENTAGE_GOOD_GRID_FRAME_TITLE_STR = "PERCENTAGE_GOOD_GRID_FRAME_TITLE";
	public static final String PERCENTAGE_GOOD_GRID_TITLE_STR = "PERCENTAGE_GOOD_GRID_TITLE";
	public static final String PERCENTAGE_GOOD_GRID_LEGEND_TEXT_STR = "PERCENTAGE_GOOD_GRID_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_TITLE_STR = "DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_TITLE";
	public static final String DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE";
	public static final String MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_LABEL_STR = "MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_LABEL";
	public static final String MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC_STR = "MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC";
	public static final String U_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE_STR = "U_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE";
	public static final String U_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT_STR = "U_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT";
	public static final String V_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE_STR = "V_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE";
	public static final String V_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT_STR = "V_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT";
	public static final String W_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE_STR = "W_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE";
	public static final String W_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT_STR = "W_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT";
	public static final String TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_LABEL_STR = "TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_LABEL";
	public static final String TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC_STR = "TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC";
	public static final String TURBULENT_KINETIC_ENERGY_GRAPH_FRAME_TITLE_STR = "TURBULENT_KINETIC_ENERGY_GRAPH_FRAME_TITLE";
	public static final String TURBULENT_KINETIC_ENERGY_GRAPH_TITLE_STR = "TURBULENT_KINETIC_ENERGY_GRAPH_TITLE";
	public static final String TURBULENT_KINETIC_ENERGY_GRAPH_X_AXIS_TITLE_STR = "TURBULENT_KINETIC_ENERGY_GRAPH_X_AXIS_TITLE";
	public static final String TURBULENT_KINETIC_ENERGY_GRAPH_Y_AXIS_TITLE_STR = "TURBULENT_KINETIC_ENERGY_GRAPH_Y_AXIS_TITLE";
	public static final String TURBULENT_KINETIC_ENERGY_GRAPH_LEGEND_TEXT_STR = "TURBULENT_KINETIC_ENERGY_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_TKE_GRAPH_TITLE_STR = "DEPTH_AVERAGED_TKE_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_TKE_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_TKE_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_TKE_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_TKE_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR = "QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR = "QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE";
	public static final String QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT_STR = "QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR = "QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR = "QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE";
	public static final String QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT_STR = "QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR = "QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR = "QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE";
	public static final String QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT_STR = "QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR = "QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR = "QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE";
	public static final String QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT_STR = "QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE";
	public static final String INVERT_CHECKBOX_LABEL_STR = "INVERT_CHECKBOX_LABEL";
	public static final String SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL_STR = "SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL";
	public static final String SHOW_CONDITIONAL_TIME_SERIES_BUTTON_DESC_STR = "SHOW_CONDITIONAL_TIME_SERIES_BUTTON_DESC";
	public static final String CONDITIONAL_TIME_SERIES_GRAPH_FRAME_TITLE_STR = "CONDITIONAL_TIME_SERIES_GRAPH_FRAME_TITLE";
	public static final String CONDITIONAL_TIME_SERIES_GRAPH_TITLE_STR = "CONDITIONAL_TIME_SERIES_GRAPH_TITLE";
	public static final String CONDITIONAL_TIME_SERIES_GRAPH_X_AXIS_LABEL_STR = "CONDITIONAL_TIME_SERIES_GRAPH_X_AXIS_LABEL";
	public static final String CONDITIONAL_TIME_SERIES_GRAPH_Y_AXIS_LABEL_STR = "CONDITIONAL_TIME_SERIES_GRAPH_Y_AXIS_LABEL";
	public static final String SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR = "SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL";
	public static final String SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR = "SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC";
	public static final String SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR = "SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL";
	public static final String SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR = "SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC";
	public static final String SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL_STR = "SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL";
	public static final String SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_DESC_STR = "SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_DESC";
	public static final String SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL_STR = "SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL";
	public static final String SHOW_OFFSET_CORRELATIONS_BUTTON_DESC_STR = "SHOW_OFFSET_CORRELATIONS_BUTTON_DESC";
	public static final String OFFSET_CORRELATIONS_GRAPH_FRAME_TITLE_STR = "OFFSET_CORRELATIONS_GRAPH_FRAME_TITLE";
	public static final String U_OFFSET_CORRELATIONS_GRAPH_TITLE_STR = "U_OFFSET_CORRELATIONS_GRAPH_TITLE";
	public static final String U_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL_STR = "U_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL";
	public static final String U_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL_STR = "U_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL";
	public static final String V_OFFSET_CORRELATIONS_GRAPH_TITLE_STR = "V_OFFSET_CORRELATIONS_GRAPH_TITLE";
	public static final String V_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL_STR = "V_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL";
	public static final String V_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL_STR = "V_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL";
	public static final String W_OFFSET_CORRELATIONS_GRAPH_TITLE_STR = "W_OFFSET_CORRELATIONS_GRAPH_TITLE";
	public static final String W_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL_STR = "W_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL";
	public static final String W_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL_STR = "W_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL";
	public static final String SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL_STR = "SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL";
	public static final String SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_DESC_STR = "SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_DESC";
	public static final String CORRELATIONS_DISTRIBUTION_GRAPH_FRAME_TITLE_STR = "CORRELATIONS_DISTRIBUTION_GRAPH_FRAME_TITLE";
	public static final String CORRELATIONS_DISTRIBUTION_GRAPH_TITLE_STR = "CORRELATIONS_DISTRIBUTION_GRAPH_TITLE";
	public static final String CORRELATIONS_DISTRIBUTION_GRAPH_X_AXIS_LABEL_STR = "CORRELATIONS_DISTRIBUTION_GRAPH_X_AXIS_LABEL";
	public static final String CORRELATIONS_DISTRIBUTION_GRAPH_Y_AXIS_LABEL_STR = "CORRELATIONS_DISTRIBUTION_GRAPH_Y_AXIS_LABEL";
	public static final String HIDE_TITLE_LABEL_STR = "HIDE_TITLE_LABEL";
	public static final String MIRROR_ABOUT_VERTICAL_LABEL_STR = "MIRROR_ABOUT_VERTICAL_LABEL";
	public static final String X_AXIS_LOGARITHMIC_LABEL_STR = "X_AXIS_LOGARITHMIC_LABEL";
	public static final String Y_AXIS_LOGARITHMIC_LABEL_STR = "Y_AXIS_LOGARITHMIC_LABEL";
	public static final String QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR = "QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE";
	public static final String QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR = "QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE";
	public static final String QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR = "QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL";
	public static final String QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR = "QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC";
	public static final String QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR = "QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE";
	public static final String QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR = "QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE";
	public static final String FILTERING_CONFIG_TAB_LABEL_STR = "FILTERING_CONFIG_TAB_LABEL";
	public static final String CS_TYPE_STANDARD_DEVIATION_STR = "CS_TYPE_STANDARD_DEVIATION";
	public static final String CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION_STR = "CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION";
	public static final String CS_TYPE_MEAN_ABSOLUTE_DEVIATION_STR = "CS_TYPE_MEAN_ABSOLUTE_DEVIATION";
	public static final String PST_REPLACEMENT_METHOD_NONE_STR = "PST_REPLACEMENT_METHOD_NONE";
	public static final String PST_REPLACEMENT_METHOD_LINEAR_INTERPOLATION_STR = "PST_REPLACEMENT_METHOD_LINEAR_INTERPOLATION";
	public static final String PST_REPLACEMENT_METHOD_LAST_GOOD_VALUE_STR = "PST_REPLACEMENT_METHOD_LAST_GOOD_VALUE";
	public static final String PST_REPLACEMENT_METHOD_12PP_INTERPOLATION_STR = "PST_REPLACEMENT_METHOD_12PP_INTERPOLATION";
	public static final String MODIFIED_PST_AUTO_SAFE_LEVEL_C1_LABEL_STR = "MODIFIED_PST_AUTO_SAFE_LEVEL_C1_LABEL";
	public static final String MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_LABEL_STR = "MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_LABEL";
	public static final String PST_REPLACEMENT_METHOD_LABEL_STR = "PST_REPLACEMENT_METHOD_LABEL";
	public static final String CS_TYPE_LABEL_STR = "CS_TYPE_LABEL";
	public static final String SAMPLING_RATE_LABEL_STR = "SAMPLING_RATE_LABEL";
	public static final String LIMITING_CORRELATION_LABEL_STR = "LIMITING_CORRELATION_LABEL";
	public static final String ABOUT_BUTTON_LABEL_STR = "ABOUT_BUTTON_LABEL";
	public static final String ABOUT_BUTTON_DESC_STR = "ABOUT_BUTTON_DESC";
	public static final String ABOUT_DIALOG_TITLE_STR = "ABOUT_DIALOG_TITLE";
	public static final String VERSION_STR = "VERSION";
	public static final String GPL_TEXT_STR = "GPL_TEXT";
	public static final String GPL_LINK_STR = "GPL_LINK";
	public static final String GPL_WARRANTY_TEXT_STR = "GPL_WARRANTY_TEXT";
	public static final String WARRANTY_STR = "WARRANTY";
	public static final String I_ACCEPT_STR = "I_ACCEPT";
	public static final String ADDITIONAL_SOFTWARE_STR = "ADDITIONAL_SOFTWARE";
	public static final String MOMENTUM_GRAPHS_STR = "MOMENTUM_GRAPHS";
	public static final String SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL_STR = "SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL";
	public static final String SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC_STR = "SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC";
	public static final String VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE_STR = "VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE";
	public static final String VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE_STR = "VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE";
	public static final String VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL_STR = "VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL";
	public static final String VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL_STR = "VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL";
	public static final String VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT_STR = "VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_VTSM_GRAPH_TITLE_STR = "VERTICAL_VTSM_GRAPH_TITLE";
	public static final String VERTICAL_VTSM_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_VTSM_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_VTSM_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_VTSM_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_VTSM_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_VTSM_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_VTSM_GRAPH_TITLE_STR = "DEPTH_AVERAGED_VTSM_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_VTSM_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VTSM_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VTSM_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VTSM_GRAPH_Y_AXIS_TITLE";
	public static final String SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL_STR = "SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL";
	public static final String SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC_STR = "SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC";
	public static final String HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE_STR = "HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE";
	public static final String HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE_STR = "HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE";
	public static final String HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL_STR = "HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL";
	public static final String HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL_STR = "HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL";
	public static final String HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT_STR = "HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_HTSM_GRAPH_TITLE_STR = "VERTICAL_HTSM_GRAPH_TITLE";
	public static final String VERTICAL_HTSM_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_HTSM_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_HTSM_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_HTSM_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_HTSM_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_HTSM_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_HTSM_GRAPH_TITLE_STR = "DEPTH_AVERAGED_HTSM_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_HTSM_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_HTSM_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_HTSM_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_HTSM_GRAPH_Y_AXIS_TITLE";
	public static final String STANDARD_CELL_WIDTH_LABEL_STR = "STANDARD_CELL_WIDTH_LABEL";
	public static final String STANDARD_CELL_HEIGHT_LABEL_STR = "STANDARD_CELL_HEIGHT_LABEL";
	public static final String MULTI_RUN_CONFIG_TAB_LABEL_STR = "MULTI_RUN_CONFIG_TAB_LABEL";
	public static final String MULTI_RUN_SYNCH_LABEL_STR = "MULTI_RUN_SYNCH_LABEL";
	public static final String SYNCH_NONE_LABEL_STR = "SYNCH_NONE_LABEL";
	public static final String SYNCH_MAX_LABEL_STR = "SYNCH_MAX_LABEL";
	public static final String SYNCH_LIMITING_VALUE_LABEL_STR = "SYNCH_LIMITING_VALUE_LABEL";
	public static final String SYNCH_LIMITING_VALUE_SETTER_LABEL_STR = "SYNCH_LIMITING_VALUE_SETTER_LABEL";
	public static final String SYNCH_LIMITING_VALUE_DIRECTION_LABEL_STR = "SYNCH_LIMITING_VALUE_DIRECTION_LABEL";
	public static final String SYNCH_LIMITING_VALUE_DIRECTION_OPTIONS_STR = "SYNCH_LIMITING_VALUE_DIRECTION_OPTIONS";
	public static final String FULL_CORRELATION_STR = "FULL_CORRELATION";
	public static final String ENSEMBLE_CORRELATION_STR = "ENSEMBLE_CORRELATION";
	public static final String RANDOM_ENSEMBLE_CORRELATION_STR = "RANDOM_ENSEMBLE_CORRELATION";
	public static final String MEAN_TRUE_ENSEMBLE_CORRELATION_SET_1_STR = "MEAN_TRUE_ENSEMBLE_CORRELATION_SET_1";
	public static final String MEAN_TRUE_ENSEMBLE_CORRELATION_SET_2_STR = "MEAN_TRUE_ENSEMBLE_CORRELATION_SET_2";
	public static final String MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_1_STR = "MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_1";
	public static final String MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_2_STR = "MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_2";
	public static final String NOT_YET_CALCULATED_STR = "NOT_YET_CALCULATED";
	public static final String PROCESSING_FILE_MESSAGE_STR = "PROCESSING_FILE_MESSAGE";
	public static final String TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_LABEL_STR = "TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_LABEL";
	public static final String TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC_STR = "TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC";
	public static final String TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_FRAME_TITLE_STR = "TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_FRAME_TITLE";
	public static final String VERTICAL_TURBULENCE_GENERATION_TAB_LABEL_STR = "VERTICAL_TURBULENCE_GENERATION_TAB_LABEL";
	public static final String VERTICAL_TURBULENCE_GENERATION_GRAPH_TITLE_STR = "VERTICAL_TURBULENCE_GENERATION_GRAPH_TITLE";
	public static final String VERTICAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT_STR = "VERTICAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT";
	public static final String HORIZONTAL_TURBULENCE_GENERATION_TAB_LABEL_STR = "HORIZONTAL_TURBULENCE_GENERATION_TAB_LABEL";
	public static final String HORIZONTAL_TURBULENCE_GENERATION_GRAPH_TITLE_STR = "HORIZONTAL_TURBULENCE_GENERATION_GRAPH_TITLE";
	public static final String HORIZONTAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT_STR = "HORIZONTAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_TURBULENCE_DISSIPATION_TAB_LABEL_STR = "VERTICAL_TURBULENCE_DISSIPATION_TAB_LABEL";
	public static final String VERTICAL_TURBULENCE_DISSIPATION_GRAPH_TITLE_STR = "VERTICAL_TURBULENCE_DISSIPATION_GRAPH_TITLE";
	public static final String VERTICAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT_STR = "VERTICAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT";
	public static final String HORIZONTAL_TURBULENCE_DISSIPATION_TAB_LABEL_STR = "HORIZONTAL_TURBULENCE_DISSIPATION_TAB_LABEL";
	public static final String HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_TITLE_STR = "HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_TITLE";
	public static final String HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT_STR = "HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT";
	public static final String ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL_STR = "ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL";
	public static final String ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_DESC_STR = "ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL";
	public static final String ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE_STR = "ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE";
	public static final String ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_LEGEND_TEXT_STR = "ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_LABEL_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_LABEL";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_TITLE_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_TITLE";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_LEGEND_TEXT_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_LABEL_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_LABEL";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_TITLE_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_TITLE";
	public static final String ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_LEGEND_TEXT_STR = "ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_LEGEND_TEXT";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL";

	public static final String LARGE_FILES_CONFIG_TAB_LABEL_STR = "LARGE_FILES_CONFIG_TAB_LABEL";
	public static final String SPLIT_LARGE_FILES_LABEL_STR = "SPLIT_LARGE_FILES_LABEL";
	public static final String LARGE_FILES_NUMBER_OF_MEASUREMENTS_PER_SPLIT_STR = "LARGE_FILES_NUMBER_OF_MEASUREMENTS_PER_SPLIT";

	public static final String PRESSURES_LABEL_STR = "PRESSURES_LABEL";

	public static final String DESPIKING_FILTER_MOVING_AVERAGE_STR = "DESPIKING_FILTER_MOVING_AVERAGE";
	public static final String DESPIKING_FILTER_MOVING_AVERAGE_REFERENCE_STR = "DESPIKING_FILTER_MOVING_AVERAGE_REFERENCE";

	public static final String MULTI_RUN_RUN_LABEL_STR = "MULTI_RUN_RUN_LABEL";
	public static final String RUN_MEAN_STR = "RUN_MEAN";
	public static final String RUN_LABEL_STR = "RUN_LABEL";
	public static final String INDEX_TEXT_STR = "INDEX_TEXT";

	public static final String ERROR_SAVING_FILE_DETAILS_TITLE_STR = "ERROR_SAVING_FILE_DETAILS_TITLE";
	public static final String SAVE_DETAILS_DIRECTORY_SUFFIX_STR = "SAVE_DETAILS_DIRECTORY_SUFFIX";

	public static final String PROBE_SETUP_CONFIG_TAB_LABEL_STR = "PROBE_SETUP_CONFIG_TAB_LABEL";

	public static final String SYNCH_INDEX_STR = "SYNCH_INDEX";
	public static final String PROBE_NONE_STR = "PROBE_NONE";

	public static final String TIME_LABEL_STR = "TIME_LABEL";

	public static final String TRIM_WARNING_STR = "TRIM_WARNING";
	public static final String TRIM_START_POINT_LABEL_STR = "TRIM_START_POINT_LABEL";
	public static final String TRIM_END_POINT_LABEL_STR = "TRIM_END_POINT_LABEL";
	public static final String TRIM_LABEL_STR = "TRIM_LABEL";

	public static final String MAXIMUM_VELOCITIES_GRAPH_BUTTON_LABEL_STR = "MAXIMUM_VELOCITIES_GRAPH_BUTTON_LABEL";
	public static final String MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC_STR = "MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC";
	public static final String MAXIMUM_VELOCITIES_GRAPH_TITLE_STR = "MAXIMUM_VELOCITIES_GRAPH_TITLE";
	public static final String MAXIMUM_VELOCITIES_GRAPH_FRAME_TITLE_STR = "MAXIMUM_VELOCITIES_GRAPH_FRAME_TITLE";
	public static final String MAXIMUM_VELOCITIES_GRAPH_LEGEND_TEXT_STR = "MAXIMUM_VELOCITIES_GRAPH_LEGEND_TEXT";
	public static final String MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE_STR = "MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE";
	public static final String MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE_STR = "MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE";
	public static final String MAXIMUM_VELOCITIES_GRAPH_LEGEND_KEY_LABEL_STR = "MAXIMUM_VELOCITIES_GRAPH_LEGEND_KEY_LABEL";

	public static final String DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_TITLE_STR = "DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE";

	public static final String DATA_POINT_OVERVIEW_TABLE_TAB_STR = "DATA_POINT_OVERVIEW_TABLE_TAB";
	public static final String DATA_POINT_OVERVIEW_TABLE_PROBE_TYPE_COL_HEADER_STR = "DATA_POINT_OVERVIEW_TABLE_PROBE_TYPE_COL_HEADER";
	public static final String DATA_POINT_OVERVIEW_TABLE_PROBE_ID_COL_HEADER_STR = "DATA_POINT_OVERVIEW_TABLE_PROBE_ID_COL_HEADER";
	public static final String DATA_POINT_OVERVIEW_TABLE_SAMPLING_RATE_COL_HEADER_STR = "DATA_POINT_OVERVIEW_TABLE_SAMPLING_RATE_COL_HEADER";

	public static final String VARIOUS_STR = "VARIOUS";

	public static final String SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL_STR = "SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL";
	public static final String SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_DESC_STR = "SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_DESC";

	public static final String SAVE_DETAILS_TO_FILE_MENU_LABEL_STR = "SAVE_DETAILS_TO_FILE_MENU_LABEL";

	public static final String SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL_STR = "SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL";

	public static final String BOUNDARY_DEFINITION_FILE_FILTER_TEXT_STR = "BOUNDARY_DEFINITION_FILE_FILTER_TEXT";

	public static final String DATA_SET_LENGTH_UNIT_OPTIONS_STR = "DATA_SET_LENGTH_UNIT_OPTIONS";
	public static final String DATA_SET_LENGTH_UNITS_LABEL_STR = "DATA_SET_LENGTH_UNITS_LABEL";

	public static final String LIMITING_SNR_LABEL_STR = "LIMITING_SNR_LABEL";

	public static final String SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL_STR = "SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL";
	public static final String SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_DESC_STR = "SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_DESC";
	public static final String SIGNAL_CORRELATION_AND_SNR_GRAPH_FRAME_TITLE_STR = "SIGNAL_CORRELATION_AND_SNR_GRAPH_FRAME_TITLE";
	public static final String SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE_STR = "SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE";
	public static final String SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL_STR = "SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL";
	public static final String SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL_STR = "SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL";
	public static final String SIGNAL_CORRELATION_LEGEND_TEXT_STR = "SIGNAL_CORRELATION_LEGEND_TEXT";
	public static final String SNR_LEGEND_TEXT_STR = "SNR_LEGEND_TEXT";

	public static final String DESPIKING_FILTER_W_DIFF_STR = "DESPIKING_FILTER_W_DIFF";
	public static final String DESPIKING_FILTER_W_DIFF_REFERENCE_STR = "DESPIKING_FILTER_W_DIFF_REFERENCE";

	public static final String LIMITING_W_DIFF_LABEL_STR = "LIMITING_W_DIFF_LABEL";

	public static final String SHOW_W_DIFF_BUTTON_LABEL_STR = "SHOW_W_DIFF_BUTTON_LABEL";
	public static final String SHOW_W_DIFF_BUTTON_DESC_STR = "SHOW_W_DIFF_BUTTON_DESC";
	public static final String W_DIFF_GRAPH_FRAME_TITLE_STR = "W_DIFF_GRAPH_FRAME_TITLE";
	public static final String W_DIFF_GRAPH_TITLE_STR = "W_DIFF_GRAPH_TITLE";
	public static final String W_DIFF_GRAPH_X_AXIS_LABEL_STR = "W_DIFF_GRAPH_X_AXIS_LABEL";
	public static final String W_DIFF_GRAPH_Y_AXIS_LABEL_STR = "W_DIFF_GRAPH_Y_AXIS_LABEL";
	public static final String W_DIFF_LEGEND_TEXT_STR = "W_DIFF_LEGEND_TEXT";

	public static final String I_DECLINE_STR = "I_DECLINE";

	public static final String USE_BINARY_FILE_FORMAT_LABEL_STR = "USE_BINARY_FILE_FORMAT_LABEL";

	public static final String PRE_FILTER_TYPE_LABEL_STR = "PRE_FILTER_TYPE_LABEL";

	public static final String NUMBER_OF_BARTLETT_WINDOWS_LABEL_STR = "NUMBER_OF_BARTLETT_WINDOWS_LABEL";

	public static final String X_CORRELATIONS_LABEL_STR = "X_CORRELATIONS_LABEL";
	public static final String X_SNRS_LABEL_STR = "X_SNRS_LABEL";
	public static final String Y_CORRELATIONS_LABEL_STR = "Y_CORRELATIONS_LABEL";
	public static final String Y_SNRS_LABEL_STR = "Y_SNRS_LABEL";
	public static final String Z_CORRELATIONS_LABEL_STR = "Z_CORRELATIONS_LABEL";
	public static final String Z_SNRS_LABEL_STR = "Z_SNRS_LABEL";

	public static final String MEAN_CORRELATION_LABEL_STR = "MEAN_CORRELATION_LABEL";
	public static final String CORRELATION_STDEV_LABEL_STR = "CORRELATION_STDEV_LABEL";
	public static final String MEAN_SNR_LABEL_STR = "MEAN_SNR_LABEL";
	public static final String SNR_ST_DEV_LABEL_STR = "SNR_ST_DEV_LABEL";

	public static final String USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_LABEL_STR = "USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_LABEL";

	public static final String SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL_STR = "SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL";
	public static final String SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_DESC_STR = "SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_DESC";

	public static final String EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL_STR = "EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL";
	public static final String EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_DESC_STR = "EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_DESC";

	public static final String NEWER_VERSIONS_STR = "NEWER_VERSIONS";

	public static final String NEWER_VERSIONS_AVAILABLE_STR = "NEWER_VERSIONS_AVAILABLE";

	public static final String MATLAB_CONNECTION_TEST_DIALOG_TITLE_STR = "MATLAB_CONNECTION_TEST_DIALOG_TITLE";
	public static final String MATLAB_PROGRESS_DIALOG_TITLE_STR = "MATLAB_PROGRESS_DIALOG_TITLE";

	public static final String SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL_STR = "SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL";
	public static final String SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_DESC_STR = "SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_DESC";

	public static final String SEND_DETAILS_TO_MATLAB_MENU_LABEL_STR = "SEND_DETAILS_TO_MATLAB_MENU_LABEL";
	public static final String SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL_STR = "SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL";
	public static final String SEND_ALL_DETAILS_TO_MATLAB_BUTTON_DESC_STR = "SEND_ALL_DETAILS_TO_MATLAB_BUTTON_DESC";

	public static final String SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL_STR = "SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL";
	public static final String SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_DESC_STR = "SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_DESC";

	public static final String WAVELET_TYPE_LABEL_STR = "WAVELET_TYPE_LABEL";
	public static final String WAVELET_TRANSFORM_TYPE_LABEL_STR = "WAVELET_TRANSFORM_TYPE_LABEL";
	public static final String WAVELET_TYPE_HAAR02_ORTOHOGONAL_STR = "WAVELET_TYPE_HAAR02_ORTOHOGONAL";
	public static final String WAVELET_TYPE_DAUB02_STR = "WAVELET_TYPE_DAUB02";
	public static final String WAVELET_TYPE_DAUB04_STR = "WAVELET_TYPE_DAUB04";
	public static final String WAVELET_TYPE_DAUB06_STR = "WAVELET_TYPE_DAUB06";
	public static final String WAVELET_TYPE_DAUB08_STR = "WAVELET_TYPE_DAUB08";
	public static final String WAVELET_TYPE_DAUB10_STR = "WAVELET_TYPE_DAUB10";
	public static final String WAVELET_TYPE_DAUB12_STR = "WAVELET_TYPE_DAUB12";
	public static final String WAVELET_TYPE_DAUB14_STR = "WAVELET_TYPE_DAUB14";
	public static final String WAVELET_TYPE_DAUB16_STR = "WAVELET_TYPE_DAUB16";
	public static final String WAVELET_TYPE_DAUB18_STR = "WAVELET_TYPE_DAUB18";
	public static final String WAVELET_TYPE_DAUB20_STR = "WAVELET_TYPE_DAUB20";
	public static final String WAVELET_TYPE_LEGE02_STR = "WAVELET_TYPE_LEGE02";
	public static final String WAVELET_TYPE_LEGE04_STR = "WAVELET_TYPE_LEGE04";
	public static final String WAVELET_TYPE_LEGE06_STR = "WAVELET_TYPE_LEGE06";
	public static final String WAVELET_TYPE_COIF06_STR = "WAVELET_TYPE_COIF06";
	public static final String WAVELET_TRANSFORM_TYPE_DFT_STR = "WAVELET_TRANSFORM_TYPE_DFT";
	public static final String WAVELET_TRANSFORM_TYPE_FWT_STR = "WAVELET_TRANSFORM_TYPE_FWT";
	public static final String WAVELET_TRANSFORM_TYPE_CWT_STR = "WAVELET_TRANSFORM_TYPE_CWT";

	public static final String SPECTRAL_ANALYSIS_CONFIG_TAB_LABEL_STR = "SPECTRAL_ANALYSIS_CONFIG_TAB_LABEL";

	public static final String SHOW_WAVELET_ANALYSIS_BUTTON_LABEL_STR = "SHOW_WAVELET_ANALYSIS_BUTTON_LABEL";
	public static final String SHOW_WAVELET_ANALYSIS_BUTTON_DESC_STR = "SHOW_WAVELET_ANALYSIS_BUTTON_DESC";

	public static final String WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_TITLE_STR = "WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_TITLE";
	public static final String WAVELET_ANALYSIS_GRAPH_FRAME_TITLE_STR = "WAVELET_ANALYSIS_GRAPH_FRAME_TITLE";
	public static final String WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_Y_AXIS_LABEL_STR = "WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_Y_AXIS_LABEL";
	public static final String WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL_STR = "WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL";

	public static final String WAVELET_ANALYSIS_LEVELS_GRAPH_TITLE_STR = "WAVELET_ANALYSIS_LEVELS_GRAPH_TITLE";
	public static final String WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL_STR = "WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL";
	public static final String WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL_STR = "WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL";
	public static final String WAVELET_ANALYSIS_SCALEOGRAM_TAB_LABEL_STR = "WAVELET_ANALYSIS_SCALEOGRAM_TAB_LABEL";
	public static final String WAVELET_ANALYSIS_RECONSTRUCTION_TAB_LABEL_STR = "WAVELET_ANALYSIS_RECONSTRUCTION_TAB_LABEL";

	public static final String WAVELET_ANALYSIS_LEVELS_SELECTOR_LABEL_STR = "WAVELET_ANALYSIS_LEVELS_SELECTOR_LABEL";

	public static final String AT_TRIM_START_BY_SETTER_LABEL_STR = "AT_TRIM_START_BY_SETTER_LABEL";
	public static final String AT_PRIOR_LENGTH_SETTER_LABEL_STR = "AT_PRIOR_LENGTH_SETTER_LABEL";
	public static final String AT_SAMPLE_LENGTH_SETTER_LABEL_STR = "AT_SAMPLE_LENGTH_SETTER_LABEL";

	public static final String AT_PRIOR_LENGTH_CHECKBOX_LABEL_STR = "AT_PRIOR_LENGTH_CHECKBOX_LABEL";

	public static final String TABLE_ACTION_PROGRESS_DIALOG_TITLE_STR = "TABLE_ACTION_PROGRESS_DIALOG_TITLE";

	public static final String WAVELET_ANALYSIS_CONTOUR_TAB_LABEL_STR = "WAVELET_ANALYSIS_CONTOUR_TAB_LABEL";
	public static final String WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE_STR = "WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE";
	public static final String WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE_STR = "WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE";
	public static final String WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE_STR = "WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE";
	public static final String WAVELET_ANALYSIS_CONTOUR_LEGEND_TEXT_STR = "WAVELET_ANALYSIS_CONTOUR_LEGEND_TEXT";

	public static final String VELOCITY_CORRELATIONS_GRAPH_FRAME_TITLE_STR = "VELOCITY_CORRELATIONS_GRAPH_FRAME_TITLE";
	public static final String U_VELOCITY_CORRELATIONS_GRAPH_TITLE_STR = "U_VELOCITY_CORRELATIONS_GRAPH_TITLE";
	public static final String V_VELOCITY_CORRELATIONS_GRAPH_TITLE_STR = "V_VELOCITY_CORRELATIONS_GRAPH_TITLE";
	public static final String W_VELOCITY_CORRELATIONS_GRAPH_TITLE_STR = "W_VELOCITY_CORRELATIONS_GRAPH_TITLE";
	public static final String VELOCITY_CORRELATION_GRAPH_LEGEND_TEXT_STR = "VELOCITY_CORRELATION_GRAPH_LEGEND_TEXT";
	public static final String VELOCITY_CORRELATION_GRAPH_TITLE_STR = "VELOCITY_CORRELATION_GRAPH_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE";

	public static final String CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL_STR = "CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL";
	public static final String CALCULATE_PSEUDO_CORRELATION_BUTTON_DESC_STR = "CALCULATE_PSEUDO_CORRELATION_BUTTON_DESC";
	public static final String VELOCITY_PSEUDO_CORRELATIONS_GRAPH_FRAME_TITLE_STR = "VELOCITY_PSEUDO_CORRELATIONS_GRAPH_FRAME_TITLE";
	public static final String U_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE_STR = "U_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE";
	public static final String V_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE_STR = "V_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE";
	public static final String W_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE_STR = "W_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE";
	public static final String VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_TEXT_STR = "VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_TEXT";
	public static final String VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE_STR = "VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR = "VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL";
	public static final String HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE";
	public static final String HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR = "HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL";
	public static final String DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE";
	public static final String DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE";
	public static final String DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE_STR = "DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE";

	public static final String SYNCH_IGNORE_FIRST_X_SECONDS_SETTER_LABEL_STR = "SYNCH_IGNORE_FIRST_X_SECONDS_SETTER_LABEL";

	public static final String MOVING_AVERAGE_WINDOW_SIZE_LABEL_STR = "MOVING_AVERAGE_WINDOW_SIZE_LABEL";

	public static final String EXPORT_DWT_LEVELS_DATA_BUTTON_LABEL_STR = "EXPORT_DWT_LEVELS_DATA_BUTTON_LABEL";
	public static final String EXPORT_DWT_LEVELS_DATA_BUTTON_DESC_STR = "EXPORT_DWT_LEVELS_DATA_BUTTON_DESC";

	public static final String CHOOSE_CONFIG_FILE_STR = "CHOOSE_CONFIG_FILE";

	public static final String COPY_CONFIG_FILE_STR = "COPY_CONFIG_FILE";

	public static final String SELECT_NEW_CONFIG_FILE_DIALOG_TITLE_STR = "SELECT_NEW_CONFIG_FILE_DIALOG_TITLE";

	public static final String CONFIGURATION_FILE_FILTER_TEXT_STR = "CONFIGURATION_FILE_FILTER_TEXT";

	public static final String CSV_FILE_FORMAT_STR = "CSV_FILE_FORMAT";

	public static final String ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL_STR = "ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL";

	public static final String PST_REPLACEMENT_POLYNOMIAL_ORDER_LABEL_STR = "PST_REPLACEMENT_POLYNOMIAL_ORDER_LABEL";

	public static final String FIVE_THIRDS_LINE_CENTRE_STR = "FIVE_THIRDS_LINE_CENTRE";
	public static final String SHOW_FIVE_THIRDS_LINE_STR = "SHOW_FIVE_THIRDS_LINE";

	public static final String FIVE_THIRDS_LINE_LEGEND_ENTRY_STR = "FIVE_THIRDS_LINE_LEGEND_ENTRY";

	public static final String PSD_TYPE_LABEL_STR = "PSD_TYPE_LABEL";
	public static final String PSD_WINDOW_LABEL_STR = "PSD_WINDOW_LABEL";
	public static final String PSD_WELCH_WINDOW_OVERLAP_LABEL_STR = "PSD_WELCH_WINDOW_OVERLAP_LABEL";
	public static final String PSD_TYPE_BARTLETT_STR = "PSD_TYPE_BARTLETT";
	public static final String PSD_TYPE_WELCH_STR = "PSD_TYPE_WELCH";
	public static final String PSD_WINDOW_NONE_STR = "PSD_WINDOW_NONE";
	public static final String PSD_WINDOW_BARTLETT_STR = "PSD_WINDOW_BARTLETT";
	public static final String PSD_WINDOW_HAMMING_STR = "PSD_WINDOW_HAMMING";

	public static final String EXPORT_PSD_S_NOUGHTS_BUTTON_LABEL_STR = "EXPORT_PSD_S_NOUGHTS_BUTTON_LABEL";
	public static final String EXPORT_PSD_S_NOUGHTS_BUTTON_DESC_STR = "EXPORT_PSD_S_NOUGHTS_BUTTON_DESC";

	public static final String Y_COLUMN_TITLE_STR = "Y_COLUMN_TITLE";
	public static final String Z_COLUMN_TITLE_STR = "Z_COLUMN_TITLE";
	public static final String S_NOUGHT_COLUMN_TITLE_STR = "S_NOUGHT_COLUMN_TITLE";

	public static final String TI_SCALE_BY_Q_OVER_A_LABEL_STR = "TI_SCALE_BY_Q_OVER_A_LABEL";

	public static final String INVERT_AXES_TEXT_STR = "INVERT_AXES_TEXT";

	public static final String CWT_BY_FREQUENCY_GRAPH_TITLE_STR = "CWT_BY_FREQUENCY_GRAPH_TITLE";
	public static final String CWT_BY_FREQUENCY_TAB_LABEL_STR = "CWT_BY_FREQUENCY_TAB_LABEL";
	public static final String CWT_BY_TIME_GRAPH_TITLE_STR = "CWT_BY_TIME_GRAPH_TITLE";
	public static final String CWT_BY_TIME_TAB_LABEL_STR = "CWT_BY_TIME_TAB_LABEL";

	public static final String CWT_BY_FREQUENCY_GRAPH_X_AXIS_LABEL_STR = "CWT_BY_FREQUENCY_GRAPH_X_AXIS_LABEL";
	public static final String CWT_BY_FREQUENCY_GRAPH_Y_AXIS_LABEL_STR = "CWT_BY_FREQUENCY_GRAPH_Y_AXIS_LABEL";
	public static final String CWT_BY_TIME_GRAPH_X_AXIS_LABEL_STR = "CWT_BY_TIME_GRAPH_X_AXIS_LABEL";
	public static final String CWT_BY_TIME_GRAPH_Y_AXIS_LABEL_STR = "CWT_BY_TIME_GRAPH_Y_AXIS_LABEL";

	public static final String WAVELET_TRANSFORM_SCALE_BY_INST_POWER_LABEL_STR = "WAVELET_TRANSFORM_SCALE_BY_INST_POWER_LABEL";

	public static final String FILE_EXTENSION_LABEL_STR = "FILE_EXTENSION_LABEL";

//INSERT_STR_DEFINITION      

	private static Hashtable<String, DAStringIndex> sTheStringsLookup = new Hashtable<String, DAStringIndex>(NUMBER_OF_STRING_INDICES);

	private static String[] sTheStrings = { 
		"New Single Probe Data Set", // NEW_SINGLE_PROBE_DATA_SET 
		"Create a new single probe data set", // NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC 
		"New Multiple Probe Data Set", // NEW_MULTIPLE_PROBE_DATA_SET 
		"Create a new multiple probe data set", // NEW_MULTIPLE_PROBE_DATA_SET_BUTTON_DESC 
		"New Multi-Run, Single Probe Data Set", // NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET 
		"Create a new multiple run, single probe data set", // NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_BUTTON_DESC 
		"New Multi-Run, Multiple Probe Data Set", // NEW_MULTI_RUN_MULTI_PROBE_DATA_SET 
		"Create a new multiple run, multiple probe data set", // NEW_MULTI_RUN_MULTI_PROBE_DATA_SET_BUTTON_DESC 
		"Save", // SAVE    
		"Save Data Set", // SAVE_DATA_SET  
		"Save Data Set", // SAVE_DATA_SET_BUTTON_DESC  
		"Open Data Set", // OPEN_DATA_SET  
		"Open an existing data set", // OPEN_DATA_SET_BUTTON_DESC 
		"Import Multiple Data Files", // IMPORT_CSV_FILES 
		"Import CSV data files from a directory", // IMPORT_CSV_FILES_BUTTON_DESC 
		"Import Multiple CSV Data Files", // IMPORT_CSV_DATA_FILES_DIALOG_TITLE 
		"Import Multiple Multi Run Files", // IMPORT_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_LABEL 
		"Import multiple single probe, multi-run files", // IMPORT_MULTIPLE_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_DESC 
		"Import Single U Measurements", // IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL 
		"Import from a file of single U measurements", // IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_DESC 
		"Import Single U Measurements", // IMPORT_SINGLE_U_MEASUREMENTS_DIALOG_TITLE 
		"Select File", // CHOOSE_SINGLE_U_MEASUREMENTS_FILE   
		"Ok", // OK    
		"Cancel", // CANCEL    
		"Close", // CLOSE    
		"y-Coordinate", // Y_COORD_LABEL    
		"z-Coordinate", // Z_COORD_LABEL    
		"Data Set", // DATA_SET   
		"Imported File", // IMPORTED_FILE   
		"Error Opening Data Set", // DATA_SET_OPEN_ERROR_TITLE 
		"Failed to open the required data set. Sorry about that.,", // DATA_SET_OPEN_ERROR_MSG 
		"File Exists", // FILE_EXISTS_DIALOG_TITLE   
		"That data set already exists. Overwrite?", // FILE_EXISTS_DIALOG_MSG 
		"Incorrect Data Set Filename Extension", // FILE_NAME_WRONG_EXTENSION_DIALOG_TITLE 
		"Extension \".majds\" will automatically be appended.\nContinue?", // FILE_NAME_WRONG_EXTENSION_DIALOG_MSG 
		"Error Creating File", // ERROR_CREATING_FILE_DIALOG_TITLE  
		"Could not create file.\nNot sure why.\nMaybe you should try again with a different filename?,", // ERROR_CREATING_FILE_DIALOG_MSG 
		"Import Error", // DATA_POINT_FROM_FILE_DATA_ERROR_TITLE   
		"Error while importing data for the data point from the csv file,", // DATA_POINT_FROM_FILE_DATA_ERROR_MSG 
		"Invalid Velocity Component", // INVALID_VELOCITY_COMPONENT_TITLE  
		"The velocity component specified is invalid.", // INVALID_VELOCITY_COMPONENT_MSG 
		"Invalid XML", // INVALID_VELOCITY_COMPONENT_XML_TAG_TITLE   
		"The velocity component tag in the XML is invalid.,", // INVALID_VELOCITY_COMPONENT_XML_TAG_MSG 
		"Invalid Character", // INVALID_CHARACTER_IN_CSV_FILE_TITLE   
		"Invalid character found in the CSV file for this point.", // INVALID_CHARACTER_IN_CSV_FILE_MSG 
		"CSV Data Files", // CSV_FILE_FILTER_NAME  
		"Data Points", // DATA_POINTS   
		"Error Retrieving Point Data", // ERROR_GETTING_POINT_DATA_TITLE 
		"Unable to retrieve the data for point ,", // ERROR_GETTING_POINT_DATA_MSG 
		"Mean", // MEAN_LABEL    
		"St. Deviation", // STDEV_LABEL   
		"Filtered Mean", // FILTERED_MEAN_LABEL   
		"Filtered St. Deviation", // FILTERED_STDEV_LABEL  
		"Point Data", // POINT_DATA   
		"Configuration", // CONFIGURATION    
		"Set configuration options", // CONFIGURATION_BUTTON_DESC  
		"Configuration", // CONFIGURATION_DIALOG_TITLE    
		"Default output data file directory: ", // DEFAULT_DATA_FILE_PATH_LABEL 
		"Select", // SELECT    
		"Select Output Data File Path", // SELECT_DATA_FILE_PATH_DIALOG_TITLE 
		"Name New Data Set", // NEW_DATA_SET_FILENAME_DIALOG_TITLE 
		"No Such Data Point", // NO_SUCH_DATA_POINT_TITLE 
		"The data point doesn't exist", // NO_SUCH_DATA_POINT_MSG 
		"Error Loading Data Point Details", // DATA_POINT_DETAILS_READ_ERROR_TITLE 
		"Problem while trying to load details for the data point,", // DATA_POINT_DETAILS_READ_ERROR_MSG 
		"U Mean", // U_MEAN_LABEL   
		"U St. Dev.", // U_STDEV_LABEL  
		"U Filtered Mean", // U_FILTERED_MEAN_LABEL  
		"U Filtered St. Dev.", // U_FILTERED_STDEV_LABEL 
		"V Mean", // V_MEAN_LABEL   
		"V St. Dev.", // V_STDEV_LABEL  
		"V Filtered Mean", // V_FILTERED_MEAN_LABEL  
		"V Filtered St. Dev.n", // V_FILTERED_STDEV_LABEL 
		"W Mean", // W_MEAN_LABEL   
		"W St. Dev.", // W_STDEV_LABEL  
		"W Filtered Mean", // W_FILTERED_MEAN_LABEL  
		"W Filtered St. Dev.", // W_FILTERED_STDEV_LABEL 
		"U", // U_VELOCITIES_LABEL    
		"V", // V_VELOCITIES_LABEL    
		"W", // W_VELOCITIES_LABEL    
		"Unfiltered U", // U_UNFILTERED_VELOCITIES_LABEL   
		"Unfiltered V", // V_UNFILTERED_VELOCITIES_LABEL   
		"Unfiltered W", // W_UNFILTERED_VELOCITIES_LABEL   
		"Filtered U", // U_FILTERED_VELOCITIES_LABEL   
		"Filtered V", // V_FILTERED_VELOCITIES_LABEL   
		"Filtered W", // W_FILTERED_VELOCITIES_LABEL   
		"Exclude Level: ", // EXCLUDE_LEVEL_LABEL  
		"Data point data file delimiter: ", // DATA_FILE_DELIMITER_LABEL 
		"Space: ;Comma:,;Tab:\t", // DATA_FILE_DELIMITER_OPTIONS   
		"Data point data file decimal separator: ", // DATA_FILE_CSV_DECIMAL_SEPARATOR_LABEL 
		"Point:.;Comma:,", // DATA_FILE_CSV_DECIMAL_SEPARATOR_OPTIONS   
		"Default input data file directory:", // DEFAULT_CSV_FILE_PATH_LABEL 
		"Select Input Data File Path", // SELECT_CSV_FILE_PATH_DIALOG_TITLE 
		"Choose File", // CHOOSE_CSV_FILE   
		"Another File", // ADD_ANOTHER_CSV_FILE   
		"Data Set Graphs", // DATA_SET_GRAPHS  
		"Y Position (mm)", // CROSS_SECTION_GRID_X_AXIS_TITLE  
		"Z Position (mm)", // CROSS_SECTION_GRID_Y_AXIS_TITLE  
		"u Velocity Distribution (u/(Q/A); Q/A = %0%m/s, U" + '\u0305' + " = %1%m/s)", // U_VELOCITY_CROSS_SECTION_GRID_TITLE 
		"u/U", // U_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT    
		"v Velocity Distribution (v/(Q/A); Q/A = %0%m/s, V" + '\u0305' + " = %1%m/s, RMS(V) = %2%m/s)", // V_VELOCITY_CROSS_SECTION_GRID_TITLE 
		"v/U", // V_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT    
		"w Velocity Distribution (w/(Q/A); Q/A = %0%m/s, W" + '\u0305' + " = %1%m/s, RMS(W) = %2%m/s)", // W_VELOCITY_CROSS_SECTION_GRID_TITLE 
		"w/U", // W_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT    
		"Choose Directory", // CHOOSE_DIRECTORY   
		"Filename", // FILENAME_LABEL    
		"Error Moving Temporary Data Point File", // ERROR_MOVING_TEMP_FILE_TITLE 
		"Error while trying to move the temporary file to ,", // ERROR_MOVING_TEMP_FILE_MSG 
		"Apply", // APPLY    
		"Updating Summary Data", // UPDATING_SUMMARY_DATA  
		"Fluid Density (kg/m3): ", // FLUID_DENSITY_LABEL 
		"Fluid Kinematic Viscosity (m2/s; x10^-6): ", // FLUID_KINEMATIC_VISCOSITY_LABEL 
		"Left Bank Coordinate: ", // LEFT_BANK_POSITION_LABEL 
		"Right Bank Coordinate: ", // RIGHT_BANK_POSITION_LABEL 
		"Water Depth: ", // WATER_DEPTH_POSITION_LABEL  
		"Error Getting Cross-Section Data", // ERROR_GETTING_CROSS_SECTION_DATA_TITLE 
		"An error occured while trying to get cross-section data.", // ERROR_GETTING_CROSS_SECTION_DATA_MSG 
		"Invert x-axis (probe u +ve upstream)", // INVERT_X_AXIS_LABEL 
		"Invert y-axis (probe v +ve streamright to streamleft)", // INVERT_Y_AXIS_LABEL 
		"Invert z-axis (probe w +ve downwards)", // INVERT_Z_AXIS_LABEL 
		"Lock Data Set", // LOCK_DATA_SET_LABEL  
		"Run Index", // RUN_INDEX_LABEL   
		"Theta", // XZ_PLANE_ROTATION_THETA_LABEL    
		"Phi", // YZ_PLANE_ROTATION_PHI_LABEL    
		"Alpha", // XY_PLANE_ROTATION_ALPHA_LABEL    
		"Probe U", // PROBE_X_VELOCITIES_LABEL   
		"Probe V", // PROBE_Y_VELOCITIES_LABEL   
		"Probe W", // PROBE_Z_VELOCITIES_LABEL   
		"Measured Discharge (m3): ", // MEASURED_DISCHARGE_LABEL 
		"Saving Data Set", // SAVING_DATA_SET  
		"Importing Single Us", // IMPORTING_SINGLE_U_MEASUREMENTS  
		"Recalculating Data", // RECALCULATING_DATA_PROGRESS_TITLE   
		"Calculating Data Point Data", // CALCULATING_DATA_POINT_SUMMARY_FIELDS_TITLE 
		"Creating RC Batch", // CREATING_RC_BATCH_PROGRESS_TITLE  
		"Error Creating RC Batch", // ERROR_CREATING_RC_BATCH_DIALOG_TITLE 
		"Error creating RC batch", // ERROR_CREATING_RC_BATCH_DIALOG_MSG 
		"Creating RC Batches", // CREATING_RC_BATCHES_PROGRESS_TITLE  
		"Error Creating RC Batch From File", // ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_TITLE 
		"Error creating RC batch from file", // ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_MSG 
		"Data Files", // DATA_FILES_CONFIG_TAB_LABEL    
		"Data Set", // DATA_SET_CONFIG_TAB_LABEL   
		"Data Point", // DATA_POINT_CONFIG_TAB_LABEL   
		"mm/s:1000;cm/s:100;m/s:1", // DATA_FILE_VELOCITY_UNIT_OPTIONS    
		"Error Creating Data Set", // DATA_SET_CREATION_ERROR_TITLE 
		"An error occured while creating the data set.\nPlease check the console log.", // DATA_SET_CREATION_ERROR_MSG 
		"Data File Velocity Units: ", // DATA_FILE_VELOCITY_UNITS_LABEL 
		"View Data Point Details", // VIEW_DATA_POINT_DETAILS_BUTTON_LABEL 
		"Open data point detail view", // VIEW_DATA_POINT_DETAILS_BUTTON_DESC 
		"Cross-Section Graphs", // CROSS_SECTION_GRAPHS   
		"Vertical Velocity Distribution", // VERTICAL_VELOCITY_GRID_TITLE  
		"Relative Velocity U/(Q/A)", // VERTICAL_VELOCITY_GRID_X_AXIS_TITLE  
		"Depth (mm)", // VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE   
		"y = ", // VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL  
		"Horizontal Velocity Distribution", // HORIZONTAL_VELOCITY_GRID_TITLE  
		"y Position (mm)", // HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE  
		"Relative Velocity U/(Q/A)", // HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE  
		"z = ", // HORIZONTAL_VELOCITY_GRID_LEGEND_KEY_LABEL  
		"Add series for section at y-coordinate: ", // VERTICAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL 
		"Add series for section at z-coordinate: ", // HORIZONTAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL 
		"Depth Averaged Velocity Profile", // DEPTH_AVERAGED_VELOCITY_GRID_TITLE 
		"Y Position (mm)", // DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE  
		"Relative Depth-Averaged Velocity Ud/(Q/A)", // DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE 
		"Derivative", // DERIVATIVE_GRID_SECONDARY_Y_AXIS_TITLE    
		"Lateral Velocity Distribution", // LATERAL_VELOCITY_GRAPH_BUTTON_LABEL  
		"Show graph of lateral velocity distribution across the cross-section,", // LATERAL_VELOCITY_GRAPH_BUTTON_DESC 
		"Lateral Velocities", // LATERAL_VELOCITY_GRID_TITLE   
		"Depth Averaged UV Distribution", // DEPTH_AVERAGED_UV_GRAPH_BUTTON_LABEL 
		"Show graph of depth-averaged UV", // DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC 
		"Depth-Averaged " + '\u03C1' + "*H*UV Distribution", // DEPTH_AVERAGED_UV_GRAPH_TITLE 
		"Y Position (mm)", // DEPTH_AVERAGED_UV_GRAPH_X_AXIS_TITLE  
		"Depth-Average UV", // DEPTH_AVERAGED_UV_GRAPH_Y_AXIS_TITLE   
		"Data Point Details", // DATA_POINT_DETAILS_FRAME_TITLE  
		"Velocity Data", // VELOCITY_DETAILS_TABLE_TAB   
		"Bed Slope: ", // BED_SLOPE_LABEL  
		"Mean Shear Stress = ", // CALCULATED_MEAN_SHEAR_STRESS_LABEL 
		"'\u03C1'" + "*g*R*S0 = ", // CONSERVATION_OF_MOMENTUM_MEAN_SHEAR_STRESS_LABEL 
		"u'v'-bar: ", // UV_FLUCTUATIONS_MEAN_LABEL   
		"u'v'-bar: ", // UW_FLUCTUATIONS_MEAN_LABEL   
		"Depth-Averaged Reynolds Stress Distribution", // DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL 
		"Depth-Averaged Reynolds Stress Distribution", // DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC 
		"Depth-Averaged Reynolds Stress", // DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_TITLE  
		"Y Position (mm)", // DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_X_AXIS_TITLE  
		"Reynolds' Stress/U^2", // DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE   
		"Missing Header File", // VECTRINO_HEADER_FILE_MISSING_TITLE  
		"No header file was found for data file ,", // VECTRINO_HEADER_FILE_MISSING_MSG 
		"Invalid Vectrino Data File", // INVALID_LINE_IN_VECTRINO_DATA_FILE_TITLE 
		"Invalid line found in data file ,", // INVALID_LINE_IN_VECTRINO_DATA_FILE_MSG 
		"Import Single Probe Binary Data Files (*.vno, *.vec, etc.)", // IMPORT_BINARY_DATA_FILES_BUTTON_LABEL 
		"Import single probe binary data files (*.vno, *.vec, etc.) from a specfied directory", // IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC 
		"Import Single Probe Binary Data Files", // IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_DIALOG_TITLE 
		"Import Multiple Converted Vectrino Data Files", // IMPORT_CONVERTED_VNO_FILES 
		"Import converted Vectrino data files (*.dat) from a specfied directory", // IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC 
		"Import Converted Vectrino Data Files", // IMPORT_MULTIPLE_CONVERTED_VECTRINO_FILES_DIALOG_TITLE 
		"Depth-average of (-" + '\u03C1' + "u'v')", // VERTICAL_REYNOLDS_STRESS_LEGEND_TEXT 
		"Depth-average of (-" + '\u03C1' + "u'w')", // HORIZONTAL_REYNOLDS_STRESS_LEGEND_TEXT 
		"Vertical (-" + '\u03C1' + "u'w') Reynolds' Stress Distribution At Bed", // VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_LABEL 
		"Show graph of estimated vertical Reynolds' stress (-" + '\u03C1' + "u'w') distribution along the channel bed", // VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC 
		"Estimated Vertical Reynolds' Stress (-" + '\u03C1' + "u'w') Distribution At the Channel Bed", // VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_TITLE 
		"Y Position (mm)", // VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_X_AXIS_TITLE  
		"-" + '\u03C1' + "u'w'", // VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_Y_AXIS_TITLE 
		"Full Series L.E.", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_FULL_SERIES_LINEAR_EXTRAPOLATION_SERIES_LABEL  
		"Bottom Two Points L.E.", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_BOTTOM_POINTS_LINEAR_EXTRAPOLATION_SERIES_LABEL 
		"Mean of Bottom Three Points", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_MEAN_OF_BOTTOM_THREE_POINTS_SERIES_LABEL 
		"Smooth-side Log-Law", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_SMOOTH_BED_LOG_LAW_SERIES_LABEL   
		"Rough-side Log-Law Ks", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_KS_FROM_ROUGH_BED_LOG_LAW_SERIES_LABEL  
		"z=10mm value", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_10_SERIES_LABEL   
		"z=20mm value", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_20_SERIES_LABEL   
		"Points above z/H = 0.2", // VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_POINTS_ABOVE_0_POINT_2LINEAR_EXTRAPOLATION_SERIES_LABEL  
		"Import converted PolySync files (*vel.txt) from a specified directory", // IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_BUTTON_DESC 
		"Import Converted PolySync Files", // IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_DIALOG_TITLE 
		"No Column Headings", // POLYSYNC_COLUMN_HEADINGS_MISSING_TITLE  
		"PolySync file missing column headings", // POLYSYNC_COLUMN_HEADINGS_MISSING_MSG 
		"Corrupt Line", // POLYSYNC_CORRUPT_LINE_TITLE   
		"Data is missing in the PolySync file at line number ,", // POLYSYNC_CORRUPT_LINE_MSG 
		"Correct Rotation", // CORRECT_ROTATION_LABEL   
		"Percentage Good", // PERCENTAGE_OF_VELOCITIES_GOOD_LABEL   
		"None", // DESPIKING_FILTER_NONE    
		"Remove Zeroes", // DESPIKING_FILTER_REMOVE_ZEROES_LEVEL   
		"Exclude Level", // DESPIKING_FILTER_EXCLUDE_LEVEL   
		"Velocity Correlation", // DESPIKING_FILTER_VELOCITY_CORRELATION   
		"Phase-Space Thresholding", // DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING   
		"Modified Phase-Space Thresholding", // DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING  
		"Correlation and SNR", // DESPIKING_FILTER_CORRELATION_AND_SNR  
		"Despiking Filter: ", // DESPIKING_FILTER_TYPE_LABEL  
		" ", // DESPIKING_FILTER_NONE_REFERENCE   
		" ", // DESPIKING_FILTER_EXCLUDE_LEVEL_REFERENCE   
		"(Cea, Puertas and Pena, 2007, Velocity Measurements on Highly Turbulent Free Surface Flow Using ADV, Exp Fluids, 42, 333-348))", // DESPIKING_FILTER_VELOCITY_CORRELATION_REFERENCE 
		"(Goring and Nikora, 2002, Despiking Acoustic Doppler Velocimeter Data, Journal of Hydraulic Engineering, 128 (1))", // DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_REFERENCE 
		"(Parsheh, Sotiropoulos and Porte-Agel, 2010, Estimation of Power Spectra of Acoustic-Doppler Velocimetry Data Contaminated with Intermittent Spikes, JHE, 136 (6))", // DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_REFERENCE 
		" ", // DESPIKING_FILTER_CORRELATION_AND_SNR_REFERENCE   
		"Table", // DATA_POINT_DATA_TABLE_TAB_LABEL    
		"Time-Series", // DATA_POINT_GRAPHS_TAB_LABEL    
		"Velocity (m/s)", // VELOCITY_LABEL   
		"Import All", // IMPORT_ALL   
		"Import Selected", // IMPORT_SELECTED   
		"Import unconverted mulitple probe binary files (*.vno, *.thX) from a specified directory", // IMPORT_MULTI_PROBE_BINARY_FILES_BUTTON_DESC 
		"Import Multiple Probe Binary Files", // IMPORT_MULTI_PROBE_BINARY_FILES_DIALOG_TITLE 
		"Main Probe: ", // MAIN_PROBE  
		"Fixed Probe: ", // FIXED_PROBE  
		"Number of Probes: ", // NUMBER_OF_PROBES 
		"Incorrect Probe Count", // INVALID_FIXED_PROBE_INDEX_ERROR_TITLE  
		"The specified fixed probe index is greater than the number of probes in the dataset.", // INVALID_FIXED_PROBE_INDEX_ERROR_MSG 
		"Incorrect Probe Count", // INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE  
		"The file for this data point does not have the correct number of probes: ", // INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG 
		"Fixed Probe Index: ", // FIXED_PROBE_INDEX_LABEL 
		"y Offset: ", // Y_OFFSET  
		"z Offset: ", // Z_OFFSET  
		"Error Opening File", // ERROR_OPENING_DATA_SET_DIALOG_TITLE  
		"An error occured while trying to open file: ", // ERROR_OPENING_DATA_SET_DIALOG_MSG 
		"Error Saving Data Set", // ERROR_SAVING_DATA_SET_DIALOG_TITLE 
		"An error occured while trying to save the data set to file.", // ERROR_SAVING_DATA_SET_DIALOG_MSG 
		"Error Importing Data Point", // ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE 
		"An error occured while trying to import the data point from file.", // ERROR_IMPORTING_DATA_POINT_DIALOG_MSG 
		"Error Getting Unsorted Data Point Coordinates", // ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_TITLE 
		"Error while getting unsorted data point coordinates,", // ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_MSG 
		"Error Getting Sorted Data Point Coordinates", // ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE 
		"Error while getting sorted data point coordinates", // ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG 
		"Error Creating New Data Set", // ERROR_CREATING_NEW_DATA_SET_TITLE 
		"An error occured while trying to create a new data set,", // ERROR_CREATING_NEW_DATA_SET_MSG 
		"Error Loading Data Point Details", // ERROR_LOADING_DATA_POINT_DETAILS_TITLE 
		"An error occured while trying to load data point details for point", // ERROR_LOADING_DATA_POINT_DETAILS_MSG 
		"Error Clearing Data Point Details", // ERROR_CLEARING_DATA_POINT_DETAILS_TITLE 
		"An error occured while trying to clear data point details for point ,", // ERROR_CLEARING_DATA_POINT_DETAILS_MSG 
		"Error Getting Configuration Data", // ERROR_GETTING_CONFIG_DATA_TITLE 
		"An error occured while trying to get the configuration data.", // ERROR_GETTING_CONFIG_DATA_MSG 
		"Error Setting Configuration Data", // ERROR_SETTING_CONFIG_DATA_TITLE 
		"An error occured while trying to set the configuration data.", // ERROR_SETTING_CONFIG_DATA_MSG 
		"Error Doing Import Complete Tasks", // ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_TITLE 
		"An error occured while trying to perform the tasks required at the end of a data import session.", // ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_MSG 
		"Error Recalculating Summary Data", // ERROR_RECALCULATING_SUMMARY_DATA_TITLE 
		"An error occured while trying to recalculate the summary data.", // ERROR_RECALCULATING_SUMMARY_DATA_MSG 
		"Error Getting Data Set Info", // ERROR_GETTING_DATA_SET_INFO_TITLE 
		"An error occured while trying to retrieve data set information.", // ERROR_GETTING_DATA_SET_INFO_MSG 
		"Data Set Already Open", // ERROR_DATA_SET_ALREADY_OPEN_TITLE 
		"That data set is already open", // ERROR_DATA_SET_ALREADY_OPEN_MSG 
		"Error Retrieving Fixed Probe Data Set Ids,", // ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_TITLE 
		"An error occured while trying to retrieve the ids of the fixed probe data set(s) for data set ,", // ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_MSG 
		"Error Closing Data Set", // ERROR_CLOSING_DATA_SET_TITLE 
		"An error occured while trying to close the data set ,", // ERROR_CLOSING_DATA_SET_MSG 
		"Main Probe Index: ", // MAIN_PROBE_INDEX_LABEL 
		"Invalid Main Probe Index", // INVALID_MAIN_PROBE_INDEX_ERROR_TITLE 
		"The main probe index is invalid.", // INVALID_MAIN_PROBE_INDEX_ERROR_MSG 
		"Invalid Main-Fixed Probe Indices", // INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_TITLE 
		"The main probe index and fixed probe index are equal - this in not valid.,", // INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_MSG 
		"Main", // MAIN_DATA_POINTS_OVERVIEW_DISPLAY_LABEL    
		"Configuration", // CONFIGURATION_TAB_LABEL    
		"-" + '\u03c1' + "u'v'-bar" + " Distribution", // VERTICAL_U_PRIME_V_PRIME_GRID_TITLE 
		"-" + '\u03c1' + "u'v'-bar", // VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE 
		"Depth (mm)", // VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE   
		"y = ", // VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL  
		"-" + '\u03c1' + "u'w'-bar" + " Distribution", // VERTICAL_U_PRIME_W_PRIME_GRID_TITLE 
		"-" + '\u03c1' + "u'w'-bar", // VERTICAL_U_PRIME_W_PRIME_GRID_X_AXIS_TITLE 
		"Depth (mm)", // VERTICAL_U_PRIME_W_PRIME_GRID_Y_AXIS_TITLE   
		"y = ", // VERTICAL_U_PRIME_W_PRIME_GRID_LEGEND_KEY_LABEL  
		"Q-H", // QUADRANT_HOLE_GRAPH_TAB_LABEL    
		"Quadrant Hole", // QUADRANT_HOLE_GRAPH_TITLE   
		"Hole Size, J", // QUADRANT_HOLE_GRAPH_X_AXIS_LABEL  
		"Shear Stress, S(i, J)/S-max", // QUADRANT_HOLE_GRAPH_Y_AXIS_LABEL 
		"Quad. Hole", // QUADRANT_HOLE_SHORTHAND_LABEL   
		"Proportion", // QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TAB_LABEL    
		"Q-H Proportion", // QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TITLE   
		"Hole Size, J", // QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL  
		"Proportion (%)", // QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL   
		"Duration", // QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TAB_LABEL    
		"Q-H Duration", // QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TITLE   
		"Hole Size, J", // QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL  
		"Duration (%)", // QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL   
		"CMSD Duration", // QUADRANT_HOLE_CMSD_DURATION_GRAPH_TAB_LABEL   
		"Q-H Channel-Mean St. Dev. Scaled Duration", // QUADRANT_HOLE_CMSD_DURATION_GRAPH_TITLE 
		"Hole Size, J", // QUADRANT_HOLE_CMSD_DURATION_GRAPH_X_AXIS_LABEL  
		"Duration (%)", // QUADRANT_HOLE_CMSD_DURATION_GRAPH_Y_AXIS_LABEL   
		"Quadrant 1", // QUADRANT_1_LABEL   
		"Quadrant 2", // QUADRANT_2_LABEL   
		"Quadrant 3", // QUADRANT_3_LABEL   
		"Quadrant 4", // QUADRANT_4_LABEL   
		"blah", // QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_TITLE    
		"u'v'/(u'v'-bar)", // QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_Y_AXIS_LABEL    
		"Calculate Velocity Correlation", // CALCULATE_CORRELATION_BUTTON_LABEL  
		"Calculate the correlation between the measured velocities for each component of the selected data points,", // CALCULATE_CORRELATION_BUTTON_DESC 
		"Error Calculating Correlation", // CALCULATE_CORRELATION_ERROR_TITLE  
		"An error occured while trying to calculate the correlation between the selected data points.,", // CALCULATE_CORRELATION_ERROR_MSG 
		"Unnormalised u'w' QH", // SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL  
		"Show unnormalised u'w' Quadrant Hole Analysis graph for the selected data points", // SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC 
		"Unnormalised u'w' Quadrant Hole Analysis Display", // UW_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE 
		"Normalised u'w' Quadrant Hole Analysis Display", // UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE 
		"Unnormalised u'w' Quadrant Hole Shear Stresses", // UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE 
		"Shear Stress", // UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE   
		"Depth (mm)", // UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE   
		"u'w' QH Shear Stress by Quadrant", // UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL 
		"Show graph of u'w' Quadrant Hole shear stress (hole size 0) by quadrant", // UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC 
		"Quadrant ", // UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL   
		"Unnormalised u'v' QH", // SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL  
		"Show u'v' Quadrant Hole Analysis graph for the selected data points", // SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC 
		"Unnormalised u'v' Quadrant Hole Analysis Display", // UV_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE 
		"Normalised u'v' Quadrant Hole Analysis Display", // UV_NORMALISED_QUADRANT_HOLE_FRAME_TITLE 
		"Unnormalised u'v' Quadrant Hole Shear Stresses", // UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE 
		"Shear Stress", // UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE   
		"Depth (mm)", // UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE   
		"u'v' QH Shear Stress by Quadrant", // UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL 
		"Show graph of u'v' Quadrant Hole shear stress (hole size 0)", // UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC 
		"Quadrant ", // UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL   
		"Lateral TKE Flux", // LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_LABEL  
		"Show graph of lateral flux of turbulent kinetic energy", // LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC 
		"Lateral TKE Flux (0.5v'(u'^2 + v'^2 + w'^2))", // LATERAL_TKE_FLUX_GRID_TITLE 
		"Turbulence Intensity", // TURBULENCE_INTENSITY_GRAPH_BUTTON_LABEL   
		"Show Turbulence Intensity graphs", // TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC 
		"Turbulence Intensity", // TURBULENCE_INTENSITY_GRAPH_FRAME_TITLE   
		"x-Direction Turbulence Intensity (" + '\u03c3' + "[u]/U; mean TI = %0%)", // X_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE 
		"y-Direction Turbulence Intensity (" + '\u03c3' + "[v]/V; mean TI = %0%)", // Y_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE 
		"z-Direction Turbulence Intensity (" + '\u03c3' + "[w]/W; mean TI = %0%)", // Z_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE 
		"x-Direction Turbulence Intensity (" + '\u03c3' + "[u]/U" + '\u0305' + "; mean TI = %0%)", // X_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE 
		"y-Direction Turbulence Intensity (" + '\u03c3' + "[v]/V" + '\u0305' + "; mean TI = %0%)", // Y_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE 
		"z-Direction Turbulence Intensity (" + '\u03c3' + "[w]/W" + '\u0305' + "; mean TI = %0%)", // Z_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE 
		"TI", // TURBULENCE_INTENSITY_GRAPH_LEGEND_TEXT    
		"Vertical (u'w') Reynolds' Stress", // VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL 
		"Show graph of vertical Reynolds' Stress", // VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC 
		"Vertical (-" + '\u03C1' + "u'w') Reynolds' Stress (mean = %0%)", // VERTICAL_REYNOLDS_STRESS_GRAPH_TITLE 
		"Horizontal (u'v') Reynolds' Stress", // HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL 
		"Show graph of horizontal Reynolds' Stress", // HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC 
		"Horizontal (-" + '\u03C1' + "u'v') Reynolds' Stress (mean = %0%)", // HORIZONTAL_REYNOLDS_STRESS_GRAPH_TITLE 
		"v'w'-bar", // V_PRIME_W_PRIME_GRAPH_BUTTON_LABEL    
		"Show graph of v'w'-bar", // V_PRIME_W_PRIME_GRAPH_BUTTON_DESC 
		"v'w'-bar (mean = %0%)", // V_PRIME_W_PRIME_GRAPH_TITLE 
		"v'w'", // V_PRIME_W_PRIME_GRAPH_LEGEND_TEXT    
		"u'w' Correlation", // UW_CORRELATION_GRAPH_BUTTON_LABEL   
		"Show graph of u'w' correlation", // UW_CORRELATION_GRAPH_BUTTON_DESC 
		"u'w' Correlation (u'w'/(" + '\u03c3' + "[u]" + '\u03c3' + "[w]) Reynolds' Stress (mean = %0%)", // UW_CORRELATION_GRAPH_TITLE  
		"corr.", // UW_CORRELATION_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of u'w' Correlation", // VERTICAL_UW_CORRELATION_GRAPH_TITLE 
		"u'w' Correlation", // VERTICAL_UW_CORRELATION_GRAPH_X_AXIS_TITLE   
		"z Position (mm)", // VERTICAL_UW_CORRELATION_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_UW_CORRELATION_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged u'w' Correlation", // DEPTH_AVERAGED_UW_CORRELATION_GRAPH_TITLE  
		"y Position (mm)", // DEPTH_AVERAGED_UW_CORRELATION_GRAPH_X_AXIS_TITLE  
		"Depth-Averaged u'w' Correlation", // DEPTH_AVERAGED_UW_CORRELATION_GRAPH_Y_AXIS_TITLE  
		"u'v' Correlation", // UV_CORRELATION_GRAPH_BUTTON_LABEL   
		"Show graph of u'v' correlation", // UV_CORRELATION_GRAPH_BUTTON_DESC 
		"u'v' Correlation (u'v'/(" + '\u03c3' + "[u]" + '\u03c3' + "[v]) Reynolds' Stress (mean = %0%)", // UV_CORRELATION_GRAPH_TITLE  
		"corr.", // UV_CORRELATION_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of u'v' Correlation", // VERTICAL_UV_CORRELATION_GRAPH_TITLE 
		"u'v' Correlation", // VERTICAL_UV_CORRELATION_GRAPH_X_AXIS_TITLE   
		"z Position (mm)", // VERTICAL_UV_CORRELATION_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_UV_CORRELATION_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged u'v' Correlation", // DEPTH_AVERAGED_UV_CORRELATION_GRAPH_TITLE  
		"y Position (mm)", // DEPTH_AVERAGED_UV_CORRELATION_GRAPH_X_AXIS_TITLE  
		"Depth-Averaged u'v' Correlation", // DEPTH_AVERAGED_UV_CORRELATION_GRAPH_Y_AXIS_TITLE  
		"v'w' Correlation", // VW_CORRELATION_GRAPH_BUTTON_LABEL   
		"Show graph of v'w' correlation", // VW_CORRELATION_GRAPH_BUTTON_DESC 
		"v'w' Correlation (v'w'/(" + '\u03c3' + "[v]" + '\u03c3' + "[w]) Reynolds' Stress (mean = %0%)", // VW_CORRELATION_GRAPH_TITLE  
		"corr.", // VW_CORRELATION_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of v'w' Correlation", // VERTICAL_VW_CORRELATION_GRAPH_TITLE 
		"v'w' Correlation", // VERTICAL_VW_CORRELATION_GRAPH_X_AXIS_TITLE   
		"z Position (mm)", // VERTICAL_VW_CORRELATION_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_VW_CORRELATION_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged v'w' Correlation", // DEPTH_AVERAGED_VW_CORRELATION_GRAPH_TITLE  
		"y Position (mm)", // DEPTH_AVERAGED_VW_CORRELATION_GRAPH_X_AXIS_TITLE  
		"Depth-Averaged v'w' Correlation", // DEPTH_AVERAGED_VW_CORRELATION_GRAPH_Y_AXIS_TITLE  
		"Reynolds' Stress Graphs", // REYNOLDS_STRESS_GRAPHS  
		"TKE, TI and Vorticity Graphs", // TI_AND_TKE_AND_VORTICITY_GRAPHS 
		"Third Order Correlations Graphs", // THIRD_ORDER_CORRELATION_GRAPHS 
		"3-0-0", // THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_LABEL    
		"Show 3-0-0 third order correlation graph", // THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC 
		"2-1-0", // THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_LABEL    
		"Show 2-1-0 third order correlation graph", // THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC 
		"1-2-0", // THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_LABEL    
		"Show 1-2-0 third order correlation graph", // THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC 
		"0-3-0", // THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_LABEL    
		"Show 0-3-0 third order correlation graph", // THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC 
		"3-0-0 Third Order Correlation", // THIRD_ORDER_CORRELATION_GRID_300_TITLE 
		"2-1-0 Third Order Correlation", // THIRD_ORDER_CORRELATION_GRID_210_TITLE 
		"1-2-0 Third Order Correlation", // THIRD_ORDER_CORRELATION_GRID_120_TITLE 
		"0-3-0 Third Order Correlation", // THIRD_ORDER_CORRELATION_GRID_030_TITLE 
		"Third Order Correlation", // THIRD_ORDER_CORRELATION_GRID_X_AXIS_TITLE  
		"Z Position (mm)", // THIRD_ORDER_CORRELATION_GRID_Y_AXIS_TITLE  
		"Corr.", // THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT    
		"Export as Table", // EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL  
		"Export graph data as CSV file", // EXPORT_GRAPH_BUTTON_DESC 
		"Export for Matlab", // EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL  
		"Export graph as file for Matlab", // EXPORT_GRAPH_FOR_MATLAB_BUTTON_DESC 
		"Power Spectrum", // SPECTRAL_DISTRIBUTION_LABEL   
		"u'w' Q1 To Q3 Ratio", // QH_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"u'w' Q1 To Q3 Ratio (mean = %0%)", // QH_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE 
		"u'w' Q1-Q3 Ratio", // QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL  
		"Show u'w' quadrant-hole analysis Q1-Q3 ratio graph", // QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC 
		"u'w' Q2 To Q4 Ratio", // QH_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"u'w' Q2 To Q4 Ratio (mean = %0%)", // QH_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE 
		"Q1/Q3", // QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT    
		"Q2/Q4", // QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT    
		"u'w' Q2-Q4 Ratio", // QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL  
		"Show u'w' quadrant-hole analysis Q2-Q4 ratio graph", // QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC 
		"u'w' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE 
		"u'w' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE 
		"(Q2 + Q4)/(Q1 + Q3)", // QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT  
		"u'w' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL 
		"Show u'w' quadrant-hole analysis Q2 and Q4 to Q1 and Q3 events ratio graph", // QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC 
		"TKE Flux Graph", // TKE_FLUX_GRAPH_BUTTON_LABEL  
		"Show graph of TKE flux", // TKE_FLUX_GRAPH_BUTTON_DESC 
		"TKE Flux", // TKE_FLUX_GRAPH_FRAME_TITLE   
		"Streamwise TKE Flux (0.5u'(u'^2 + v'^2 + w'^2); mean=%0%),", // X_DIRECTION_TKE_FLUX_GRAPH_TITLE 
		"Transverse TKE Flux (0.5v'(u'^2 + v'^2 + w'^2); mean=%0%),", // Y_DIRECTION_TKE_FLUX_GRAPH_TITLE 
		"Vertical TKE Flux (0.5w'(u'^2 + v'^2 + w'^2); mean=%0%),", // Z_DIRECTION_TKE_FLUX_GRAPH_TITLE 
		"TKE Flux", // TKE_FLUX_GRAPH_LEGEND_TEXT   
		"Show Power Spectral Density", // SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL 
		"Show graph of the PSD for the selected data points", // SHOW_SPECTRAL_DISTRIBUTION_BUTTON_DESC 
		"Normalised Power Spectral Density", // SPECTRAL_DISTRIBUTION_GRAPH_FRAME_TITLE 
		"Normalised Power Spectral Density", // SPECTRAL_DISTRIBUTION_GRAPH_TITLE 
		"Frequency, f", // SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL   
		"Sxx/(" + '\u03c3' + "^2) (m2/s2)/Hz (%0% Windows)", // SPECTRAL_DISTRIBUTION_GRAPH_Y_AXIS_LABEL   
		"Missing Summary Data", // BACKWARD_COMPATIBILITY_MISSING_DATA_TITLE  
		"Some summary data items are missing from the XML.\nThis may be due to the data being from an old version.\nIt is suggested that all data is recalculated.,", // BACKWARD_COMPATIBILITY_MISSING_DATA_MSG 
		"Error While Removing Data Point", // REMOVE_DATA_POINTS_ERROR_TITLE 
		"An error occured while trying to remove the data points.", // REMOVE_DATA_POINTS_ERROR_MSG 
		"Error While Removing Data Point", // REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_TITLE 
		"The data point (%0%-%1%) does not exist.,", // REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_MSG 
		"Remove Data Points", // REMOVE_DATA_POINTS_BUTTON_LABEL  
		"Remove the selected data points", // REMOVE_DATA_POINTS_BUTTON_DESC 
		"EOF \"R\" Matrix", // CALCULATE_EOF_R_MATRIX_BUTTON_LABEL  
		"Calculate the \"R\" matrix for Empirical Orthogonal Function analysis", // CALCULATE_EOF_R_MATRIX_BUTTON_DESC 
		"Calculate EOF \"R\" Matrix", // CALCULATE_EOF_R_MATRIX_DIALOG_TITLE 
		"y1: ", // EOF_R_MATRIX_DIALOG_Y1_LABEL   
		"y2: ", // EOF_R_MATRIX_DIALOG_Y2_LABEL   
		"z1: ", // EOF_R_MATRIX_DIALOG_Z1_LABEL   
		"z2: ", // EOF_R_MATRIX_DIALOG_Z2_LABEL   
		"EOF \"R\" Matrix Bounds", // EOF_R_MATRIX_DIALOG_TITLE 
		"Incomplete Data Grid", // EOF_R_MATRIX_INCOMPLETE_GRID_TITLE  
		"The selected data point range includes y-coordinates for which the set of z-coordinates is incomplete.,", // EOF_R_MATRIX_INCOMPLETE_GRID_MSG 
		"Calculate DPS Data", // CALCULATE_DATA_POINT_SUMMARY_DATA  
		"Calculate uncalculated data point summary data fields,", // CALCULATE_DATA_POINT_SUMMARY_DATA_DESC 
		"Reynolds' Stresses", // CALCULATE_DPS_REYNOLDS_STRESSES   
		"Calculate Reynolds' stresses for all data points", // CALCULATE_DPS_REYNOLDS_STRESSES_DESC 
		"Quadrant Hole", // CALCULATE_DPS_QH_DATA   
		"Calculate Quadrant Hole analysis data for all data points", // CALCULATE_DPS_QH_DATA_DESC 
		"Turbulence Intensity & TKE", // CALCULATE_DPS_TKE_DATA 
		"Calculate TI and TKE data for all data points", // CALCULATE_DPS_TKE_DATA_DESC 
		"Fixed Probe Correlations", // CALCULATE_DPS_FIXED_PROBE_CORRELATIONS  
		"Calculate fixed probe correlation data for all data points", // CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_DESC 
		"Create R.C. Batch", // CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL  
		"Create a rotation correction batch from the selected data points", // CREATE_ROTATION_CORRECTION_BATCH_BUTTON_DESC 
		"Error Calculating Batch Rotation Correction", // ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_TITLE 
		"An error occurred while calculating the batch rotation correction for data point %0%-%1%.", // ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_MSG 
		"Selection Tool", // SELECTION_TOOL_BUTTON_LABEL   
		"Start the data point selection tool", // SELECTION_TOOL_BUTTON_DESC 
		"y Min", // Y_COORD_MIN_LABEL   
		"y Max", // Y_COORD_MAX_LABEL   
		"z Min", // Z_COORD_MIN_LABEL   
		"z Max", // Z_COORD_MAX_LABEL   
		"New Selection", // SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL   
		"Extend Selection", // SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL   
		"Theta R.C.", // BATCH_THETA_ROTATION_CORRECTION_COLUMN_TITLE   
		"Alpha R.C.", // BATCH_ALPHA_ROTATION_CORRECTION_COLUMN_TITLE   
		"Phi R.C.", // BATCH_PHI_ROTATION_CORRECTION_COLUMN_TITLE   
		"R.C. Batch #", // BATCH_ROTATION_CORRECTION_BATCH_NUMBER_COLUMN_TITLE  
		"Create R.C. Batches from File", // CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL 
		"Create rotation correction batches from the batch.majrcb file.", // CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_DESC 
		"Missing Rotation Correction Batches File", // ROTATION_CORRECTION_BATCHES_FILE_MISSING_TITLE 
		"No R.C. batches file (batches.majrcb) found in the data set directory (%0%).", // ROTATION_CORRECTION_BATCHES_FILE_MISSING_MSG 
		"Error Reading Batches File", // ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_TITLE 
		"An error occurred while reading the batches file (%0%; %1%).\nPlease check the format.", // ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_MSG 
		"u Standard Deviation", // VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_LABEL  
		"Show Standard Deviation of u over a vertical section", // VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_DESC 
		"v Standard Deviation", // VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_LABEL  
		"Show Standard Deviation of v over a vertical section", // VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_DESC 
		"w Standard Deviation", // VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_LABEL  
		"Show Standard Deviation of w over a vertical section", // VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_DESC 
		"Vertical Distribution of u Standard Deviation", // VERTICAL_VELOCITY_U_ST_DEV_GRID_TITLE 
		"u St Dev/U(z)", // VERTICAL_VELOCITY_U_ST_DEV_GRID_X_AXIS_TITLE  
		"z/h", // VERTICAL_VELOCITY_U_ST_DEV_GRID_Y_AXIS_TITLE    
		"y =", // VERTICAL_VELOCITY_U_ST_DEV_GRID_LEGEND_KEY_LABEL   
		"Vertical Distribution of v Standard Deviation", // VERTICAL_VELOCITY_V_ST_DEV_GRID_TITLE 
		"v St Dev/U(z)", // VERTICAL_VELOCITY_V_ST_DEV_GRID_X_AXIS_TITLE  
		"z/h", // VERTICAL_VELOCITY_V_ST_DEV_GRID_Y_AXIS_TITLE    
		"y =", // VERTICAL_VELOCITY_V_ST_DEV_GRID_LEGEND_KEY_LABEL   
		"Vertical Distribution of w Standard Deviation", // VERTICAL_VELOCITY_W_ST_DEV_GRID_TITLE 
		"w St Dev/U(z)", // VERTICAL_VELOCITY_W_ST_DEV_GRID_X_AXIS_TITLE  
		"z/h", // VERTICAL_VELOCITY_W_ST_DEV_GRID_Y_AXIS_TITLE    
		"y =", // VERTICAL_VELOCITY_W_ST_DEV_GRID_LEGEND_KEY_LABEL   
		"Show PDF", // SHOW_PDF_BUTTON_LABEL   
		"Show Probability Density Functions", // SHOW_PDF_BUTTON_DESC 
		"u PDF", // U_PDF_GRAPH_TITLE   
		"Probability Density Functions", // PDF_GRAPH_FRAME_TITLE  
		"u", // U_PDF_GRAPH_X_AXIS_LABEL    
		"PDF", // U_PDF_GRAPH_Y_AXIS_LABEL    
		"v PDF", // V_PDF_GRAPH_TITLE   
		"v", // V_PDF_GRAPH_X_AXIS_LABEL    
		"PDF", // V_PDF_GRAPH_Y_AXIS_LABEL    
		"w PDF", // W_PDF_GRAPH_TITLE   
		"w", // W_PDF_GRAPH_X_AXIS_LABEL    
		"PDF", // W_PDF_GRAPH_Y_AXIS_LABEL    
		"%0% Gaussian", // GAUSSIAN   
		"u'v' Q1-Q3 Ratio", // QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL  
		"Show u'v' quadrant-hole analysis Q1-Q3 ratio graph", // QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC 
		"u'v' Q1 to Q3 Ratio", // QH_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"u'v' Q1 to Q3 Ratio", // QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE 
		"u'v' Q2-Q4 Ratio", // QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL  
		"Show u'v' quadrant-hole analysis Q2-Q4 ratio graph", // QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC 
		"u'v' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL 
		"Show u'v' quadrant-hole analysis Q2 and Q4 to Q1 and Q3 events ratio graph", // QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC 
		"u'v' Q2 to Q4 Ratio", // QH_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"u'v' Q2 to Q4 Ratio", // QH_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE 
		"u'v' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE 
		"u'v' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE 
		"Dimensionless Axes", // DIMENSIONLESS_AXES_LABEL   
		"y/B", // DIMENSIONLESS_X_AXIS_LABEL    
		"z/h", // DIMENSIONLESS_Y_AXIS_LABEL    
		"(v'^2-bar - w'2-bar)", // DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_LABEL  
		"Show (v'^2-bar - w'2-bar) Graph", // DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC 
		"(v'^2-bar - w'2-bar) Distribution", // DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_FRAME_TITLE 
		"(v'^2-bar - w'2-bar) Distribution (mean = %0%)", // DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_TITLE 
		"All", // CALCULATE_DPS_ALL_LABEL    
		"Calculate all Data Point Summary fields", // CALCULATE_DPS_ALL_DESC 
		"Magnitude of Combined R.S.", // COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_LABEL 
		"Show distribution of magnitude of combined horizontal and vertical Reynolds' stress", // COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC 
		"Magnitude of Combined Reynolds' Stress (MCRS = SQRT((\u03C1u'v')^2 + (\u03C1u'w')^2); mean = %0%)", // COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE 
		"-\u03C1u'v'", // HORIZONTAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT    
		"-\u03C1u'w'", // VERTICAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT    
		"MCRS", // COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_LEGEND_TEXT    
		"Quadrant-Hole Graphs", // QH_GRAPHS   
		"Velocity Graphs", // VELOCITY_GRAPHS   
		"Standard Deviation", // STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_LABEL   
		"Show distribution of the standard deviation of the velocity components", // STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC 
		"Distribution of Standard Deviation", // STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE 
		"Distribution of Standard Deviation of u", // U_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE 
		"u St. Dev.", // U_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT  
		"Distribution of Standard Deviation of v", // V_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE 
		"v St. Dev.", // V_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT  
		"Distribution of Standard Deviation of w", // W_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE 
		"w St. Dev.", // W_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT  
		"Vertical Section", // VERTICAL_SECTION_GRAPH_BUTTON_LABEL   
		"View vertical section graph.", // VERTICAL_SECTION_GRAPH_BUTTON_DESC 
		"Horizontal Section", // HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL   
		"View horizontal section graph.", // HORIZONTAL_SECTION_GRAPH_BUTTON_DESC 
		"Streamwise Vorticity", // STREAMWISE_VORTICITY_GRAPH_BUTTON_LABEL   
		"Show streamwise vorticity distribution.", // STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC 
		"Streamwise Vorticity", // STREAMWISE_VORTICITY_GRID_FRAME_TITLE   
		"Streamwise Vorticity (Mean = %0%)", // STREAMWISE_VORTICITY_GRID_TITLE 
		"Vorticity", // STREAMWISE_VORTICITY_GRID_LEGEND_TEXT    
		"Relative Streamwise Vorticity (Vort", // VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE 
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE  
		"Vorticity", // VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_LEGEND_KEY_LABEL    
		"Resultant Shear Stress", // RESULTANT_SHEAR_STRESS_GRID_TITLE  
		"Resultant Shear Stress", // RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL  
		"Show resultant shear stress distribution.", // RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC 
		"Depth-Averaged", // DEPTH_AVERAGED_GRAPH_BUTTON_LABEL    
		"Show depth-averaged graphs", // DEPTH_AVERAGED_GRAPH_BUTTON_DESC  
		"Extrapolated", // EXTRAPOLATED_DEPTH_AVERAGED_GRAPH_LABEL    
		"Populated Cell", // POPULATED_CELL_DEPTH_AVERAGED_GRAPH_LABEL   
		"Depth-Averaged Streamwise Vorticity", // DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_TITLE  
		"Depth-Averaged Streamwise Vorticity", // DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE  
		"z Position (mm)", // DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE  
		"Depth-Averaged Combined Reynolds' Stress Magnitude", // DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_X_AXIS_TITLE  
		"Depth-Averaged Combined Reynolds' Stress Magnitude", // DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_Y_AXIS_TITLE 
		"Turbulence Intensity", // TURBULENCE_INTENSITY_GRID_TITLE   
		"TI", // VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE    
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY__GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_LEGEND_KEY_LABEL    
		"Depth-Averaged Tubulence Intensity", // DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_TITLE  
		"y Position (mm)", // DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE  
		"Turbulence Intensity", // DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_Y_AXIS_TITLE   
		"Depth-Averaged Horizontal Reynolds' Stress (-" + '\u03C1' + "u'v')", // DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE  
		"Depth-Averaged Horizontal Reynolds' Stress (-" + '\u03C1' + "u'v')", // DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE 
		"Depth-Averaged Vertical Reynolds' Stress (-" + '\u03C1' + "u'w')", // DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE  
		"Depth-Averaged Vertical Reynolds' Stress (-" + '\u03C1' + "u'w')", // DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE 
		"Relative to Mean", // RELATIVE_TO_MEAN_CHECKBOX_LABEL  
		"Dimensionless", // DIMENSIONLESS_CHECKBOX_LABEL    
		"Viscous Diffusion", // VORTICITY_EQUATION_TERM_1_LABEL   
		"Perkins' P3", // VORTICITY_EQUATION_TERM_2_LABEL   
		"Perkins' P4", // VORTICITY_EQUATION_TERM_3_LABEL   
		"Sum", // VORTICITY_EQUATION_TERM_4_LABEL    
		"LHS", // VORTICITY_EQUATION_TERM_5_LABEL    
		"Vorticity Equation Terms Distribution", // VORTICITY_EQUATION_TERMS_FRAME_TITLE 
		"Viscous Diffusion (mean = %0%)", // VORTICITY_EQUATION_TERM_1_GRAPH_TITLE 
		"Viscous Diffusion", // VORTICITY_EQUATION_TERM_1_LEGEND_TEXT   
		"Perkins' P3 (mean = %0%)", // VORTICITY_EQUATION_TERM_2_GRAPH_TITLE 
		"P3", // VORTICITY_EQUATION_TERM_2_LEGEND_TEXT    
		"Perkins' P4 (mean = %0%)", // VORTICITY_EQUATION_TERM_3_GRAPH_TITLE 
		"P4", // VORTICITY_EQUATION_TERM_3_LEGEND_TEXT    
		"Sum of Viscous Diffusion, P3 and P4 (mean = %0%)", // VORTICITY_EQUATION_TERM_4_GRAPH_TITLE 
		"Sum", // VORTICITY_EQUATION_TERM_4_LEGEND_TEXT    
		"LHS of Vorticity Equation (mean=%0%)", // VORTICITY_EQUATION_TERM_5_GRAPH_TITLE 
		"LHS", // VORTICITY_EQUATION_TERM_5_LEGEND_TEXT    
		"Vorticity Equation Graphs", // VORTICITY_EQUATION_GRAPHS_BUTTON_LABEL  
		"Show distribution of the vorticity equation terms.", // VORTICITY_EQUATION_GRAPHS_BUTTON_DESC 
		"Last Modified", // LAST_MODIFIED_LABEL   
		"Comments", // COMMENTS_LABEL    
		"figure;\n" +     
		"x=[\n%0%];\n" +     
		"y=[\n%1%];\n" +     
		"plot(x,y);\n" +     
		MAJFCTools.STRING_SUBSTITUTION_PERCENT_SYMBOL + "title('%2%');\n" +   
		"xlabel('%3%');\n" +     
		"ylabel('%4%');\n" +     
		"axis([%5% %6% %7% %8%]);\n" +  
		"set(gca,'FontSize',26);\n" +     
		"set(gca,'FontSize',26);\n" +     
		"set(get(gca,'XLabel'),'FontSize',26);\n" +     
		"set(get(gca,'YLabel'),'FontSize',26);\n" +     
		"set(gca,'XScale',%9%);\n" +     
		"set(gca,'YScale',%10%);", // MATLAB_XY_OUTPUT_TEXT    
		"figure;" +     
		"x=[\n%0%];\n" +     
		"y=[\n%1%];\n" +     
		"z=[\n%2%];\n" +     
		"contourf(x,y,z');\n" +     
		MAJFCTools.STRING_SUBSTITUTION_PERCENT_SYMBOL + "title('%3%');\n" +   
		"xlabel('%4%');\n" +     
		"ylabel('%5%');\n" +     
		"axis([%6% %7% %8% %9%]);\n" +  
		"set(gca,'FontSize',26);\n" +     
		"set(gca,'FontSize',26);\n" +     
		"set(get(gca,'XLabel'),'FontSize',26);\n" +     
		"set(get(gca,'YLabel'),'FontSize',26);\n" +     
		"set(gca,'XScale',%10%);\n" +     
		"set(gca,'YScale',%11%);\n" +     
		"cb=colorbar;\n" +     
		"set(cb,'FontSize',24);", // MATLAB_XYZ_OUTPUT_TEXT    
		"Water Temperature: ", // WATER_TEMPERATURE_LABEL  
		"Default Export File Path: ", // DEFAULT_EXPORT_FILE_PATH_LABEL 
		"Select Default Export File Path", // SELECT_EXPORT_FILE_PATH_DIALOG_TITLE 
		"Use Data File Path For All", // USE_DATA_FILE_PATH_FOR_ALL_LABEL 
		"Viscous Diffusion", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE   
		"Viscous Diffusion", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE   
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL    
		"Perkins' P3", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE   
		"Perkins' P3", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE   
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL    
		"Perkins' P4", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE   
		"Perkins' P4", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE   
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL    
		"Sum", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE    
		"Sum of Viscous Diffusion, Perkins' P3 and Perkins' P4", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE 
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL    
		"Term 5", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE   
		"Term 5", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE   
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL    
		"Streamwise TKE Flux", // VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE  
		"Streamwise TKE Flux", // VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE  
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL    
		"Depth-Averaged Streamwise TKE Flux", // DEPTH_AVERAGED_U_TKE_FLUX_GRID_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_U_TKE_FLUX_GRID_X_AXIS_TITLE  
		"Depth-Averaged Streamwise TKE Flux", // DEPTH_AVERAGED_U_TKE_FLUX_GRID_Y_AXIS_TITLE 
		"Transverse TKE Flux", // VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE  
		"Transverse TKE Flux", // VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE  
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL    
		"Depth-Averaged Transverse TKE Flux", // DEPTH_AVERAGED_V_TKE_FLUX_GRID_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_V_TKE_FLUX_GRID_X_AXIS_TITLE  
		"Depth-Averaged Transverse TKE Flux", // DEPTH_AVERAGED_V_TKE_FLUX_GRID_Y_AXIS_TITLE 
		"Vertical TKE Flux", // VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE  
		"Vertical TKE Flux", // VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE  
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE  
		"", // VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL    
		"Depth-Averaged Vertical TKE Flux", // DEPTH_AVERAGED_W_TKE_FLUX_GRID_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_W_TKE_FLUX_GRID_X_AXIS_TITLE  
		"Depth-Averaged Vertical TKE Flux", // DEPTH_AVERAGED_W_TKE_FLUX_GRID_Y_AXIS_TITLE 
		"Depth-Averaged Viscous Diffusion", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_TITLE  
		"Depth-Averaged Viscous Diffusion", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE  
		"z Position (mm)", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE  
		"Depth-Averaged Perkins' P3", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_TITLE  
		"Depth-Averaged Perkins' P3", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE  
		"z Position (mm)", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE  
		"Depth-Averaged Perkins' P4", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_TITLE  
		"Depth-Averaged Perkins' P4", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE  
		"z Position (mm)", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE  
		"Depth-Averaged Sum of Terms", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_TITLE 
		"Depth-Averaged Sum of Terms", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE 
		"z Position (mm)", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE  
		"", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_TITLE    
		"", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE    
		"z Position (mm)", // DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE  
		"Percentage Good", // VIEW_PERCENTAGE_GOOD_BUTTON_LABEL   
		"View graph of percentage of good readings", // VIEW_PERCENTAGE_GOOD_BUTTON_DESC 
		"Streawise Vorticity KE", // STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_LABEL  
		"View graph of streamwise vorticity kinetic energy", // STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC 
		"Streamwise Vorticity Kinetic Energy", // STREAMWISE_VORTICITY_KE_GRID_FRAME_TITLE 
		"Streamwise Vorticity Kinetic Energy (" + '\u03C1' + "w^2 * PI * r^4; mean = %0%)", // STREAMWISE_VORTICITY_KE_GRID_TITLE 
		"KE", // STREAMWISE_VORTICITY_KE_GRID_LEGEND_TEXT    
		"y Position (mm)", // VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE  
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE  
		"y = ", // VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_LEGEND_KEY_LABEL  
		"Depth-Averaged Streamwise Vorticity Kinetic Energy", // DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE  
		"Depth-Averaged Streamwise Vorticity Kinetic Energy", // DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE 
		"Percentage Good", // PERCENTAGE_GOOD_GRID_FRAME_TITLE   
		"Percentage Good (mean = %0%)", // PERCENTAGE_GOOD_GRID_TITLE 
		"% Good", // PERCENTAGE_GOOD_GRID_LEGEND_TEXT   
		"y Position (mm)", // VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE  
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE  
		"y = ", // VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_LEGEND_KEY_LABEL  
		"Depth-Averaged Percentage Good", // DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_TITLE  
		"y Position (mm)", // DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE  
		"Depth-Averaged Percentage Good", // DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE  
		"Mean Velocity KE", // MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_LABEL  
		"Show distribution of mean velocity kinetic energy", // MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC 
		"Mean Streamwise (U) Kinetic Energy (1/2" + '\u03c1' + "U^2)", // U_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE 
		"U-KE", // U_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT    
		"Mean Streamwise (V) Kinetic Energy (1/2" + '\u03c1' + "V^2)", // V_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE 
		"V-KE", // V_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT    
		"Mean Streamwise (W) Kinetic Energy (1/2" + '\u03c1' + "W^2)", // W_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE 
		"W-KE", // W_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT    
		"TKE", // TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_LABEL    
		"Show distribution of TKE", // TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC 
		"Turbulent Kinetic Energy", // TURBULENT_KINETIC_ENERGY_GRAPH_FRAME_TITLE  
		"Turbulent Kinetic Energy (0.5 * (u'^2 + v'^2 + w'^2); mean = %0%)", // TURBULENT_KINETIC_ENERGY_GRAPH_TITLE 
		"z Position (mm)", // TURBULENT_KINETIC_ENERGY_GRAPH_X_AXIS_TITLE  
		"y Position (mm)", // TURBULENT_KINETIC_ENERGY_GRAPH_Y_AXIS_TITLE  
		"TKE", // TURBULENT_KINETIC_ENERGY_GRAPH_LEGEND_TEXT    
		"Vertical Distribtion of TKE", // VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE 
		"TKE (0.5 * (u'^2 + v'^2 + w'^2))", // VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE 
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL  
		"Horizontal Distribtion of TKE", // HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE 
		"y Position (mm)", // HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE  
		"TKE (0.5 * (u'^2 + v'^2 + w'^2))", // HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE 
		"z = ", // HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged TKE", // DEPTH_AVERAGED_TKE_GRAPH_TITLE   
		"y Position (mm)", // DEPTH_AVERAGED_TKE_GRAPH_X_AXIS_TITLE  
		"TKE (0.5 * (u'^2 + v'^2 + w'^2))", // DEPTH_AVERAGED_TKE_GRAPH_Y_AXIS_TITLE 
		"Scaled u'w' Q1 to Q3", // QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL 
		"Show distribution of the scaled (by point " + '\u03c3' + "[u]" + '\u03c3' + "[w]) shear-stress) ratio of u'w' Q1 events to Q3 events", // QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC 
		"Scaled u'w' Q1 to Q3", // QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"Scaled u'w' Q1 to Q3 (mean = %0%)", // QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE 
		"y Position (mm)", // QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE  
		"z Position (mm)", // QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE  
		"Ratio", // QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of Scaled Q1 to Q3 Events Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE    
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Horizontal Distribution of Scaled Q1 to Q3 Events Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"y Position (mm)", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE  
		"Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE    
		"z = ", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged of Scaled Q1", // DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE  
		"Depth-Average Ratio", // DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE   
		"Scaled u'w' Q2 to Q4", // QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL 
		"Show distribution of the scaled (by point " + '\u03c3' + "[u]" + '\u03c3' + "[w]) ratio of u'w' Q2 events to Q4 events", // QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC 
		"Scaled u'w' Q2 to Q4", // QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"Scaled u'w' Q2 to Q4 (mean = %0%)", // QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE 
		"y Position (mm)", // QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE  
		"z Position (mm)", // QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE  
		"Ratio", // QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of Scaled Q2 to Q4 Events Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE    
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Horizontal Distribution of Scaled Q2 to Q4 Events Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"y Position (mm)", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE  
		"Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE    
		"z = ", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged of Scaled Q2/Q4 Events Ratio", // DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE  
		"Depth-Average Ratio", // DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE   
		"Scaled u'v' Q1 to Q3", // QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL 
		"Show distribution of the scaled (by point " + '\u03c3' + "[u]" + '\u03c3' + "[v]) ratio of u'v' Q1 events to Q3 events", // QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC 
		"Scaled u'v' Q1 to Q3", // QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"Scaled u'v' Q1 to Q3 (mean = %0%)", // QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE 
		"y Position (mm)", // QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE  
		"z Position (mm)", // QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE  
		"Ratio", // QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of Scaled Q1 to Q3 Events Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE    
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Horizontal Distribution of Scaled Q1 to Q3 Events Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE 
		"y Position (mm)", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE  
		"Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE    
		"z = ", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged of Scaled Q1/Q3 Events Ratio", // DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE  
		"Depth-Average Ratio", // DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE   
		"Scaled u'v' Q2 to Q4", // QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL 
		"Show distribution of the scaled (by point " + '\u03c3' + "[u]" + '\u03c3' + "[v]) ratio of u'v' Q2 events to Q4 events", // QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC 
		"Scaled u'v' Q2 to Q4", // QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"Scaled u'v' Q2 to Q4 (mean = %0%)", // QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE 
		"y Position (mm)", // QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE  
		"z Position (mm)", // QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE  
		"Ratio", // QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of Scaled Q2 to Q4 Events Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"Ratio", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE    
		"z Position (mm)", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE  
		"y = ", // VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Horizontal Distribution of Scaled Q2 to Q4 Events Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE 
		"y Position (mm)", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE  
		"Ratio", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE    
		"z = ", // HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged of Scaled Q2/Q4 Events Ratio", // DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE  
		"Depth-Average Ratio", // DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE   
		"Invert Ratio", // INVERT_CHECKBOX_LABEL   
		"Show Conditional Time Series", // SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL 
		"Show Conditional Time Series", // SHOW_CONDITIONAL_TIME_SERIES_BUTTON_DESC 
		"Conditional Time Series", // CONDITIONAL_TIME_SERIES_GRAPH_FRAME_TITLE  
		"Conditional Time Series", // CONDITIONAL_TIME_SERIES_GRAPH_TITLE  
		"Time", // CONDITIONAL_TIME_SERIES_GRAPH_X_AXIS_LABEL    
		"Velocity", // CONDITIONAL_TIME_SERIES_GRAPH_Y_AXIS_LABEL    
		"Normalised u'w' QH", // SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL  
		"Show normalised (by point " + '\u03c3' + "[u]" + '\u03c3' + "[w]) u'w' Quadrant Hole Analysis graph for the selected data points", // SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC 
		"Normalised u'v' QH", // SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL  
		"Show normalised (by point " + '\u03c3' + "[u]" + '\u03c3' + "[v]) u'v' Quadrant Hole Analysis graph for the selected data points", // SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC 
		"ASCII (Filtered)", // SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL   
		"Save filtered velocities to ASCII (text) file", // SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_DESC 
		"Show Offset Correlations", // SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL  
		"Show offset correlations", // SHOW_OFFSET_CORRELATIONS_BUTTON_DESC  
		"Offset Correlations", // OFFSET_CORRELATIONS_GRAPH_FRAME_TITLE   
		"u Offset Correlations", // U_OFFSET_CORRELATIONS_GRAPH_TITLE  
		"Time Shift", // U_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL   
		"u Offset Correlations", // U_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL  
		"v Offset Correlations", // V_OFFSET_CORRELATIONS_GRAPH_TITLE  
		"Time Shift", // V_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL   
		"v Offset Correlations", // V_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL  
		"w Offset Correlations", // W_OFFSET_CORRELATIONS_GRAPH_TITLE  
		"Time Shift", // W_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL   
		"w Offset Correlations", // W_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL  
		"Show Correlations Distribution", // SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL  
		"Show correlations distribution", // SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_DESC  
		"Correlations Distribution", // CORRELATIONS_DISTRIBUTION_GRAPH_FRAME_TITLE   
		"Correlations Distribtion", // CORRELATIONS_DISTRIBUTION_GRAPH_TITLE   
		"u Correlation", // CORRELATIONS_DISTRIBUTION_GRAPH_X_AXIS_LABEL   
		"v or w Correlation", // CORRELATIONS_DISTRIBUTION_GRAPH_Y_AXIS_LABEL 
		"Hide Title", // HIDE_TITLE_LABEL   
		"Mirror About Vertical", // MIRROR_ABOUT_VERTICAL_LABEL  
		"Log. X Axis", // X_AXIS_LOGARITHMIC_LABEL  
		"Log. Y Axis", // Y_AXIS_LOGARITHMIC_LABEL  
		"Scaled u'w' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL 
		"Show scaled (by point " + '\u03c3' + "[u]" + '\u03c3' + "[w]) u'w' quadrant-hole analysis Q2 and Q4 to Q1 and Q3 events ratio", // QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC 
		"Scaled u'w' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE 
		"Scaled u'w' Q2 and Q4 to Q1 and Q3 Events Ratio (mean = %0%)", // QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE 
		"Scaled u'v' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL 
		"Show scaled (by point " + '\u03c3' + "[u]" + '\u03c3' + "[v]) u'v' quadrant-hole analysis Q2 and Q4 to Q1 and Q3 events ratio", // QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC 
		"Scaled u'v' Q2 and Q4 to Q1 and Q3 Events Ratio", // QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE 
		"Scaled u'v' Q2 and Q4 to Q1 and Q3 Events Ratio (mean = %0%)", // QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE 
		"Filtering", // FILTERING_CONFIG_TAB_LABEL    
		"Standard Deviation", // CS_TYPE_STANDARD_DEVIATION   
		"Median Abs. Deviation", // CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION  
		"Mean Abs. Deviation", // CS_TYPE_MEAN_ABSOLUTE_DEVIATION  
		"None", // PST_REPLACEMENT_METHOD_LABELLACEMENT_METHOD_NONE    
		"Linear Interpolation", // PST_REPLACEMENT_METHOD_LINEAR_INTERPOLATION   
		"Last Good Value", // PST_REPLACEMENT_METHOD_LAST_GOOD_VALUE  
		"12 Point Polynomial Interp.", // PST_REPLACEMENT_METHOD_12PP_INTERPOLATION 
		"mPST Auto-safe Level C1: ", // MODIFIED_PST_AUTO_SAFE_LEVEL_C1_LABEL 
		"mPST Auto-exclude Level C2: ", // MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_LABEL 
		"Spike Replacement Method: ", // PST_REPLACEMENT_METHOD 
		"CS Type: ", // CS_TYPE_LABEL  
		"Sampling Rate (Hz): ", // SAMPLING_RATE_LABEL 
		"Acceptable Correlation Limit: ", // LIMITING_CORRELATION_LABEL 
		"About", // ABOUT_BUTTON_LABEL    
		"About this thing", // ABOUT_BUTTON_DESC  
		"About", // ABOUT_DIALOG_TITLE    
		"Version", // VERSION    
		"MAJ's Velocity Signal Analyser Copyright (C) 2009 - 2016 Michael Jesson\n\n" +
		"This program comes with ABSOLUTELY NO WARRANTY; for details see the licence excerpt below.\n" +
		"This is free software, and you are welcome to redistribute it under certain conditions.\n" +
		"It is distributed under the terms of the GNU General Public Licence version 3; see\n" +
		"http://www.gnu.org/licenses/gpl-3.0.html for details.", // GPL_TEXT  
		"http://www.gnu.org/licenses/gpl-3.0.html", // GPL_LINK    
		"From Gnu GPL v3:\n\n " + 
		"\"15. Disclaimer of Warranty.\n" +  
		"THERE IS NO WARRANTY FOR THE PROGRAM, TO THE EXTENT PERMITTED BY APPLICABLE LAW. EXCEPT WHEN OTHERWISE\n" +
		"STATED IN WRITING THE COPYRIGHT HOLDERS AND/OR OTHER PARTIES PROVIDE THE PROGRAM \"AS IS\" WITHOUT WARRANTY\n" +
		"OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF\n" +
		"MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE\n" +
		"OF THE PROGRAM IS WITH YOU. SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF ALL NECESSARY\n" +
		"SERVICING, REPAIR OR CORRECTION.\n\n" +  
		"16. Limitation of Liability.\n" +  
		"IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING WILL ANY COPYRIGHT HOLDER, OR ANY\n" +
		"OTHER PARTY WHO MODIFIES AND/OR CONVEYS THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES,\n" +
		"INCLUDING ANY GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR INABILITY\n" +
		"TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF DATA OR DATA BEING RENDERED INACCURATE OR\n" +
		"LOSSES SUSTAINED BY YOU OR THIRD PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS),\n" +
		"EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.\"\n\n" +
		"The full licence may be seen at http://www.gnu.org/licenses/gpl-3.0.html.", // GPL_WARRANTY_TEXT 
		"Warranty", // WARRANTY    
		"I Accept", // I_ACCEPT   
		"This program uses:\n" +   
		"\tJama-1.0.3.jar (available from http://math.nist.gov/javanumerics/jama/)\n" +  
		"\tjfreechart-1.0.14.jar & jcommon-1.0.17.jar (available from http://www.jfree.org/jfreechart/)\n" +
		"\tjmatio.jar (available from http://sourceforge.net/projects/jmatio/) with some modifications by\n" +
		"\t\tRobert Craig at Nortek and Mike Jesson\n" +
		"\tmatlabcontrol-4.1.0.jar (available from http://code.google.com/p/matlabcontrol/)\n" +  
		"\ticons from http://www.fatcow.com/free-icons/\n" +   
		"Where applicable, licencing information may be found in the user guide at www.mikejesson.com.\n", // ADDITIONAL_SOFTWARE 
		"Momentum Transfer", // MOMENTUM_GRAPHS   
		"Vertical Momentum Transfer", // SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL  
		"Show Vertical Transfer of Streamwise Momentum", // SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC 
		"Vertical Transfer of Streamwise Momentum (\u03C1UW; mean=%0)", // VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE 
		"Vertical Transfer of Streamwise Momentum", // VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE 
		"y Position (mm)", // VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL  
		"z Position (mm)", // VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL  
		"MT", // VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of Vertical Transfer of Streamwise Momentum", // VERTICAL_VTSM_GRAPH_TITLE 
		"Vertical Transfer of Streamwise Momentum", // VERTICAL_VTSM_GRAPH_X_AXIS_TITLE 
		"z Position (mm)", // VERTICAL_VTSM_GRAPH_Y_AXIS_TITLE  
		"VToSM", // VERTICAL_VTSM_GRAPH_LEGEND_KEY_LABEL    
		"Depth-Averaged Vertical Transfer of Streamwise Momentum", // DEPTH_AVERAGED_VTSM_GRAPH_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_VTSM_GRAPH_X_AXIS_TITLE  
		"Depth-Averaged Vertical Transfer of Streamwise Momentum", // DEPTH_AVERAGED_VTSM_GRAPH_Y_AXIS_TITLE 
		"Horizontal Momentum Transfer", // SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL  
		"Show Horizontal Transfer of Streamwise Momentum", // SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC 
		"Horizontal Transfer of Streamwise Momentum (\u03C1UV; mean=%0)", // HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE 
		"Horizontal Transfer of Streamwise Momentum", // HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE 
		"y Position (mm)", // HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL  
		"z Position (mm)", // HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL  
		"MT", // HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT    
		"Vertical Distribution of Horizontal Transfer of Streamwise Momentum", // VERTICAL_HTSM_GRAPH_TITLE 
		"Horizontal Transfer of Streamwise Momentum", // VERTICAL_HTSM_GRAPH_X_AXIS_TITLE 
		"z Position (mm)", // VERTICAL_HTSM_GRAPH_Y_AXIS_TITLE  
		"HTSoM", // VERTICAL_HTSM_GRAPH_LEGEND_KEY_LABEL    
		"Depth-Averaged Horizontal Transfer of Streamwise Momentum", // DEPTH_AVERAGED_HTSM_GRAPH_TITLE 
		"y Position (mm)", // DEPTH_AVERAGED_HTSM_GRAPH_X_AXIS_TITLE  
		"Depth-Averaged Horizontal Transfer of Streamwise Momentum", // DEPTH_AVERAGED_HTSM_GRAPH_Y_AXIS_TITLE 
		"Standard Cell Width: ", // STANDARD_CELL_WIDTH_LABEL 
		"Standard Cell Height: ", // STANDARD_CELL_HEIGHT_LABEL 
		"Multi-Run/Auto-Trim", // MULTI_RUN_CONFIG_TAB_LABEL    
		"Synchronise/Auto-Trim on:", // MULTI_RUN_SYNCH_LABEL   
		"No Synchronisation", // SYNCH_NONE_LABEL   
		"Maximum Value", // SYNCH_MAX_LABEL   
		"Limiting Value:", // SYNCH_LIMITING_VALUE_LABEL   
		"", // SYNCH_LIMITING_VALUE_SETTER_LABEL    
		"Direction: ", // SYNCH_LIMITING_VALUE_DIRECTION_LABEL   
		"Rising:0;Falling:1", // SYNCH_LIMITING_VALUE_DIRECTION_OPTIONS    
		"Full Series Correlation: ", // FULL_CORRELATION 
		"Ensemble Correlation: ", // ENSEMBLE_CORRELATION  
		"Random Ensemble Correlation: ", // RANDOM_ENSEMBLE_CORRELATION 
		"Mean True Ensemble Corr. 1: ", // MEAN_TRUE_ENSEMBLE_CORRELATION_SET_1 
		"Mean True Ensemble Corr. 1: ", // MEAN_TRUE_ENSEMBLE_CORRELATION_SET_2 
		"Mean Random Ensemble Corr. 1: ", // MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_1 
		"Mean Random Ensemble Corr. 1: ", // MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_2 
		"NYC", // NOT_YET_CALCULATED    
		"Processing file: %0% %1%", // PROCESSING_FILE_MESSAGE 
		"Turbulence Gen./Diss", // TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_LABEL   
		"Turbulence Generation and Dissipation", // TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC 
		"Tubulence Generation and Dissipation %0%", // TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_FRAME_TITLE 
		"Vertical Generation", // VERTICAL_TURBULENCE_GENERATION_TAB_LABEL   
		"Vertical Turbulence Generation (mean = %0%)", // VERTICAL_TURBULENCE_GENERATION_GRAPH_TITLE 
		"G", // VERTICAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT    
		"Horizontal Generation", // HORIZONTAL_TURBULENCE_GENERATION_TAB_LABEL   
		"Horizontal Turbulence Generation (mean = %0%)", // HORIZONTAL_TURBULENCE_GENERATION_GRAPH_TITLE 
		"G", // HORIZONTAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT    
		"Vertical Dissipation", // VERTICAL_TURBULENCE_DISSIPATION_TAB_LABEL   
		"Vertical Turbulence Dissipation (mean = %0%)", // VERTICAL_TURBULENCE_DISSIPATION_GRAPH_TITLE 
		"E", // VERTICAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT    
		"Horizontal Dissipation", // HORIZONTAL_TURBULENCE_DISSIPATION_TAB_LABEL   
		"Horizontal Turbulence Dissipation (mean = %0%)", // HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_TITLE 
		"E", // HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT    

		"Anisotropic Stress Tensor", // ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL  
		"Show anisotropic stress tensor graph", // ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_DESC 
		"Anisotropic Stress Tensor Component", // ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE 
		"b", // ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_LEGEND_TEXT    

		"Anisotropic Stress Tensor Component b", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE 
		"b", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE    
		"z Position", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE   
		"y = ", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL  

		"Anisotropic Stress Tensor Component b", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE 
		"y Position", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE   
		"b", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE    
		"z = ", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL  

		"Invariant II", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_LABEL   
		"Show graph of anisotropic stress tensor invariant II", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC 
		"Invariant II", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_TITLE   
		"Inv. II", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_LEGEND_TEXT   
		"Invariant II", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE   
		"Invariant II", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE   
		"z Position", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE   
		"y =", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL   
		"Invariant II", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE   
		"y Position", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE   
		"Invariant II", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE   
		"z =", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL   
		"Invariant III", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_LABEL   
		"Show graph of anisotropic stress tensor invariant III", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC 
		"Invariant III", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_TITLE   
		"Inv. III", // ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_LEGEND_TEXT   
		"Invariant III", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE   
		"Invariant III", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE   
		"z Position", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE   
		"y =", // VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL   
		"Invariant III", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE   
		"y Position", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE   
		"Invariant III", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE   
		"z =", // HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL   
		"Large Files", // LARGE_FILES_AND_MISC_CONFIG_TAB_LABEL   
		"Split large files", // SPLIT_LARGE_FILES_LABEL  
		"Measurements per split: ", // LARGE_FILES_NUMBER_OF_MEASUREMENTS_PER_SPLIT 

		"Pressure", // PRESSURES_LABEL    

		"Moving Average", // DESPIKING_FILTER_MOVING_AVERAGE   
		" ", // DESPIKING_FILTER_MOVING_AVERAGE_REFERENCE   

		"Run", // MULTI_RUN_RUN_LABEL    
		"Run Mean", // RUN_MEAN   
		"Run", // RUN_LABEL    
		"Meas. No.", // INDEX_TEXT   

		"Error Saving Data Point Details to File", // ERROR_SAVING_FILE_DETAILS_TITLE 
		"DataPointDetailsFiles", // SAVE_DETAILS_DIRECTORY_SUFFIX    

		"Probe Setup", // PROBE_SETUP_CONFIG_TAB_LABEL   

		"Synch. Parent: ", // SYNCH_INDEX  
		"None", // PROBE_NONE    

		"Time(s)", // TIME_LABEL    

		"PLEASE READ before using trim functionality:\nThe original, raw data files (e.g. .csv, .vno) are NOT affected by trimming the time-series." +
		"However, the internal temporary files used to store data between saves are overwritten. When the data set is saved the changes become permanent." +
		" It is recommended that you:\n" +
		"\n\tSave the data set before trimming;" +
		"\n\tSave the data set with a different file name (making a backup with the untrimmed data);" +
		"\n\tIf you make any mistakes, close the data set without saving and then re-open it to restore the last saved data;", // TRIM_WARNING 
		"Start: ", // TRIM_START_POINT_LABEL   
		"End: ", // TRIM_END_POINT_LABEL   
		"Trim", // TRIM_LABEL    

		"Maximum Velocities", // MAXIMUM_VELOCITIES_GRAPH_BUTTON_LABEL   
		"Show graph of maximum velocity by data point", // MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC 
		"Maximum Velocity", // MAXIMUM_VELOCITIES_GRAPH_TITLE   
		"Maximum Velocities", // MAXIMUM_VELOCITIES_GRAPH_FRAME_TITLE   
		"Max. Vel.", // MAXIMUM_VELOCITIES_GRAPH_LEGEND_TEXT   
		"Maximum Velocity", // MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE   
		"z Position", // MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE   
		"y =", // MAXIMUM_VELOCITIES_GRAPH_LEGEND_KEY_LABEL   

		"Depth-Averaged Maximum Velocity", // DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_TITLE  
		"y Position", // DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE   
		"Depth-Averaged Max. Velocity", // DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE  

		"Data Point Overview", // DATA_POINT_OVERVIEW_TABLE_TAB  
		"Probe Type", // DATA_POINT_OVERVIEW_TABLE_PROBE_TYPE_COL_HEADER   
		"Probe Id", // DATA_POINT_OVERVIEW_TABLE_PROBE_ID_COL_HEADER   
		"Sampling Rate (Hz)", // DATA_POINT_OVERVIEW_TABLE_SAMPLING_RATE_COL_HEADER  

		"Various", // VARIOUS    

		"Matlab (Filtered)", // SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL   
		"Save filtered velocities to Matlab (.mat) file", // SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_DESC 

		"Save Details to File", // SAVE_DETAILS_TO_FILE_MENU_LABEL 

		"Select channel boundary def. file.", // SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL 

		"Boundary Definition File (*.majbd)", // BOUNDARY_DEFINITION_FILE_FILTER_TEXT 

		"mm:1000;cm:100;m:1", // DATA_SET_LENGTH_UNIT_OPTIONS    
		"Length Units: ", // DATA_SET_LENGTH_UNITS_LABEL  

		"Acceptable SNR Limit: ", // LIMITING_SNR_LABEL 

		"Show Signal Correlation and SNR ", // SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL 
		"Show Signal Correlation and SNR ", // SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_DESC 
		"Signal Correlation and SNR", // SIGNAL_CORRELATION_AND_SNR_GRAPH_FRAME_TITLE 
		"Signal Correlation and SNR", // SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE 
		"Time(s)", // SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL    
		"Signal Correlation (%) or SNR (dB)", // SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL 
		" (Signal Correlation)", // SIGNAL_CORRELATION_LEGEND_TEXT  
		" (SNR)", // SNR_LEGEND_TEXT   

		"w1 and w2 Difference", // DESPIKING_FILTER_W_DIFF 
		" ", // DESPIKING_FILTER_W_DIFF_REFERENCE   

		"Limiting w Diff: ", // LIMITING_W_DIFF_LABEL 

		"Show |w1-w2|", // SHOW_W_DIFF_BUTTON_LABEL   
		"Show the difference between w calculated with each beam.", // SHOW_W_DIFF_BUTTON_DESC 
		"Difference between w1 and w2", // W_DIFF_GRAPH_FRAME_TITLE 
		"Difference between w1 and w2", // W_DIFF_GRAPH_TITLE 
		"Time(s)", // W_DIFF_GRAPH_X_AXIS_LABEL    
		"|w1 - w2|", // W_DIFF_GRAPH_Y_AXIS_LABEL  
		"|w1 - w2|", // W_DIFF_LEGEND_TEXT  

		"I Decline", // I_DECLINE   

		"Use Binary File Format", // USE_BINARY_FILE_FORMAT_LABEL 

		"Pre-Filter: ", // PRE_FILTER_TYPE_LABEL   

		"No. of Averaging Windows: ", // NUMBER_OF_BARTLETT_WINDOWS_LABEL 

		"x Corr.", // X_CORRELATIONS_LABEL   
		"x SNR", // X_SNRS_LABEL   
		"y Corr.", // Y_CORRELATIONS_LABEL   
		"y SNR", // Y_SNRS_LABEL   
		"z Corr.", // Z_CORRELATIONS_LABEL   
		"z SNR", // Z_SNRS_LABEL   

		"Mean Corr.", // MEAN_CORRELATION_LABEL   
		"Corr. St. Dev.", // CORRELATION_STDEV_LABEL  
		"Mean SNR", // MEAN_SNR_LABEL   
		"SNR St. Dev.", // SNR_ST_DEV_LABEL  

		"Percentage of Mean Corr./SNR", // USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_LABEL 

		"ASCII (All)", // SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL   
		"Save probe, translated and filtered velocities to ASCII (text) file.", // SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_DESC 

		"Export as Matlab Script", // EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL 
		"Exports the graph data as a Matlab script.", // EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_DESC 

		"Newer Versions", // NEWER_VERSIONS   

		"Newer versions are available. To update, please download the latest version from www.mikejesson.com.\n\n", // NEWER_VERSIONS_AVAILABLE 

		"Testing for Matlab Connection...", // MATLAB_CONNECTION_TEST_DIALOG_TITLE 
		"Exporting to Matlab...", // MATLAB_PROGRESS_DIALOG_TITLE  

		"Filtered", //SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL     
		"Send filtered velocities to Matlab.", //SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_DESC 

		"Send Details to Matlab", //SEND_DETAILS_TO_MATLAB_MENU_LABEL  
		"All", //SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL     
		"Send all details to Matlab.", //SEND_ALL_DETAILS_TO_MATLAB_BUTTON_DESC 

		"Matlab (All)", //SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL    
		"Save all details to Matlab file.", //SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_DESC

		"Wavelet Type: ", //WAVELET_TYPE_LABEL   
		"Wavelet Transform Type: ", //WAVELET_TRANSFORM_TYPE_LABEL  
		"Haar 02 Orthog.", //WAVELET_TYPE_HAAR02_ORTOHOGONAL   
		"Haar (Daub.-2)", //WAVELET_TYPE_DAUB02    
		"Daubechies-4", //WAVELET_TYPE_DAUB04     
		"Daubechies-6", //WAVELET_TYPE_DAUB06     
		"Daubechies-8", //WAVELET_TYPE_DAUB08     
		"Daubechies-10", //WAVELET_TYPE_DAUB10     
		"Daubechies-12", //WAVELET_TYPE_DAUB12     
		"Daubechies-14", //WAVELET_TYPE_DAUB14     
		"Daubechies-16", //WAVELET_TYPE_DAUB16     
		"Daubechies-18", //WAVELET_TYPE_DAUB18     
		"Daubechies-20", //WAVELET_TYPE_DAUB20     
		"Lege 02", //WAVELET_TYPE_LEGE02    
		"Lege 04", //WAVELET_TYPE_LEGE04    
		"Lege 06", //WAVELET_TYPE_LEGE06    
		"Coif 06", //WAVELET_TYPE_COIF06    
		"DFT", //WAVELET_TRANSFORM_TYPE_DFT     
		"FWT", //WAVELET_TRANSFORM_TYPE_FWT     
		"CWT (Morlet)", //WAVELET_TRANSFORM_TYPE_CWT    

		"Spectral Analysis", //SPECTRAL_ANALYSIS_CONFIG_TAB_LABEL    

		"Wavelet Analysis", //SHOW_WAVELET_ANALYSIS_BUTTON_LABEL    
		"Show wavelet analysis graph.", //SHOW_WAVELET_ANALYSIS_BUTTON_DESC  

		"Scaleogram", //WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_TITLE     
		"Wavelet Analysis", //WAVELET_ANALYSIS_GRAPH_FRAME_TITLE    
		"a(r)", //WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_Y_AXIS_LABEL     
		"Scale, r", //WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL    

		"Levels Reconstruction", //WAVELET_ANALYSIS_LEVELS_GRAPH_TITLE    
		"Time (s)", //WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL    
		"Velocity (m/s)", //WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL    
		"Scaleogram", //WAVELET_ANALYSIS_SCALEOGRAM_TAB_LABEL     
		"Reconstruction", //WAVELET_ANALYSIS_RECONSTRUCTION_TAB_LABEL     

		"Level %0% (f = %1%Hz)", //WAVELET_ANALYSIS_LEVELS_SELECTOR_LABEL 

		"Trim Start By: ", //AT_TRIM_START_BY_SETTER_LABEL	  
		"Seconds Before Synch Point: ", //AT_PRIOR_LENGTH_SETTER_LABEL 
		"Sample Length: ", //AT_SAMPLE_LENGTH_SETTER_LABEL   

		"Auto-trim ", //AT_PRIOR_LENGTH_CHECKBOX_LABEL    

		"Working... ", //TABLE_ACTION_PROGRESS_DIALOG_TITLE    

		"Contour", //WAVELET_ANALYSIS_CONTOUR_TAB_LABEL     
		"Contour", //WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE     
		"Time(s)", //WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE     
		"Frequency (Hz)", //WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE    
		"Power Density (log)", //WAVELET_ANALYSIS_CONTOUR_LEGEND_TEXT   

		"Velocity Correlations ", //VELOCITY_CORRELATIONS_GRAPH_FRAME_TITLE   
		"u Correlations", //U_VELOCITY_CORRELATIONS_GRAPH_TITLE    
		"v Correlations", //V_VELOCITY_CORRELATIONS_GRAPH_TITLE    
		"w Correlations", //W_VELOCITY_CORRELATIONS_GRAPH_TITLE    
		"Corr.", //VELOCITY_CORRELATION_GRAPH_LEGEND_TEXT     
		"Correlation", //VELOCITY_CORRELATION_GRAPH_TITLE     
		"Correlation", //VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE     
		"z Position", //VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE    
		"Show for y =", //VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL  
		"y Position", //HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE    
		"Correlation", //HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE     
		"Show for z =", //HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged Correlation", //DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_TITLE    
		"y Position", //DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE    
		"Correlation", //DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE     

		"Pseudo-Correlations", //CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL     
		"Show graph of pseudo-correlations.", //CALCULATE_PSEUDO_CORRELATION_BUTTON_DESC  
		"Velocity Correlations", //VELOCITY_PSEUDO_CORRELATIONS_GRAPH_FRAME_TITLE    
		"u Correlations", //U_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE    
		"v Correlations", //V_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE    
		"w Correlations", //W_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE    
		"Corr.", //VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_TEXT     
		"Correlation", //VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE     
		"Correlation", //VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE     
		"z Position", //VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE    
		"Show for y =", //VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL  
		"y Position", //HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE    
		"Correlation", //HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE     
		"Show for z =", //HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL  
		"Depth-Averaged Correlation", //DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE    
		"y Position", //DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE    
		"Correlation", //DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE     

		"Ignore First X Seconds: ", //SYNCH_IGNORE_FIRST_X_SECONDS_SETTER_LABEL 

		"Moving Average Window Size: ", //MOVING_AVERAGE_WINDOW_SIZE_LABEL 

		"Export Levels", //EXPORT_DWT_LEVELS_DATA_BUTTON_LABEL    
		"Export Levels Data ", //EXPORT_DWT_LEVELS_DATA_BUTTON_DESC  

		"Select Configuration File: ", //CHOOSE_CONFIG_FILE  

		"Copy config. file \"%0%\" ", //COPY_CONFIG_FILE 

		"Name New Config. File", //SELECT_NEW_CONFIG_FILE_DIALOG_TITLE  

		"VSA Configuration File (*.conf)", //CONFIGURATION_FILE_FILTER_TEXT  

		"CSV File Format: ", //CSV_FILE_FORMAT  

		"Enter custom CSV File Format", //ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL 

		"Polynomial Order", //PST_REPLACEMENT_POLYNOMIAL_ORDER_LABEL    

		"Centre -5/3 line on f = ", //FIVE_THIRDS_LINE_CENTRE
		"Show -5/3 line", //SHOW_FIVE_THIRDS_LINE   

		"-5/3", //FIVE_THIRDS_LINE_LEGEND_ENTRY     

		"PSD Method: ", //PSD_TYPE_LABEL   
		"PSD Window: ", //PSD_WINDOW_LABEL   
		"Welch Overlap: ", //PSD_WELCH_WINDOW_OVERLAP_LABEL   
		"Bartlett", //PSD_TYPE_BARTLETT     
		"Welch", //PSD_TYPE_WELCH     
		"None", //PSD_WINDOW_NONE     
		"Bartlett", //PSD_WINDOW_BARTLETT     
		"Hamming", //PSD_WINDOW_HAMMING     

		"Export S(0)s", //EXPORT_PSD_S_NOUGHTS_BUTTON_LABEL    
		"Export S(0)s", //EXPORT_PSD_S_NOUGHTS_BUTTON_DESC    

		"y", //Y_COLUMN_TITLE     
		"z", //Z_COLUMN_TITLE     
		"S(0)", //S_NOUGHT_COLUMN_TITLE     

		"Scale by U" + '\u0305' + " = Q/A", //TI_SCALE_BY_Q_OVER_A_LABEL

		"These inversion selectors should be configured such that the data are in left-handed axes.", //INVERT_AXES_TEXT

		"Power by Frequency", //CWT_BY_FREQUENCY_GRAPH_TITLE   
		"By Frequency", //CWT_BY_FREQUENCY_TAB_LABEL    
		"Power by Time", //CWT_BY_TIME_GRAPH_TITLE   
		"By Time", //CWT_BY_TIME_TAB_LABEL    

		"Power", //CWT_BY_FREQUENCY_GRAPH_X_AXIS_LABEL     
		"Frequency (Hz)", //CWT_BY_FREQUENCY_GRAPH_Y_AXIS_LABEL    
		"Time (s)", //CWT_BY_TIME_GRAPH_X_AXIS_LABEL    
		"Power", //CWT_BY_TIME_GRAPH_Y_AXIS_LABEL     

		"Scale WT by inst. power", //WAVELET_TRANSFORM_SCALE_BY_INST_POWER_LABEL 

		"Extension", //FILE_EXTENSION_LABEL

//INSERT_DEFINITION      
	};      
	private static DAStrings sMe = new DAStrings(sTheStrings);

	private DAStrings(String[] objects) {   
		super(objects);      
	}      
	public static String getString(DAStringIndex index) { 
		return sMe.get(index);     
	}      
	public static String getString(DAStringIndex index, String ... inserts) {
		if (inserts == null || inserts.length == 0) {
			return getString(index);     
		}      
		return MAJFCTools.substituteIntoString(sMe.get(index), inserts);    
	}      
	public static String getString(DAStringIndex index, Object ... inserts) {
		if (inserts == null || inserts.length == 0) {
			return getString(index);     
		}      
		String[] insertStrings = new String[inserts.length];  
		for (int i = 0; i < inserts.length; ++i) {
			insertStrings[i] = MAJFCTools.stringValueOf(inserts[i]);    
		}      
		return getString(index, insertStrings);    
	}      
	/**      
	 * Override default strings with values from a file. The strings file should be of form:
	 *     
	 * index key   
	 * value    
	 * index key   
	 * value    
	 *     
	 * e.g.    
	 * 	NEW_SINGLE_PROBE_DATA_SET    
	 * Create new single probe data set
	 * NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC    
	 * Create a new data set for data gathered with a single probe
	 *     
	 * @param stringsFile   
	 * @throws IOException   
	 */     
	public static void updateStringsFromFile(File stringsFile) throws IOException {
		if (sTheStringsLookup.size() == 0) {  
			populateStringsLookup();      
		}      
		BufferedReader fileReader = new BufferedReader(new FileReader(stringsFile)); 
		String indexKey = fileReader.readLine();   
		while (indexKey != null) {  
			DAStringIndex stringIndex = null;   
			if ((stringIndex = sTheStringsLookup.get(indexKey)) != null) {
				sTheStrings[stringIndex.getIntIndex()] = fileReader.readLine();    
			}      
			indexKey = fileReader.readLine();    
		}      

		fileReader.close();      
	}      

	/**      
	* Inner class    
	*      
	* @author MAJ727    
	*      
	*/      
	static class DAStringIndex extends MAJFCSafeArray.MAJFCSafeArrayIndex { 
		private DAStringIndex(int index) {   
			super(index);      
		}      
	}      

	private static void populateStringsLookup() {  
		sTheStringsLookup.put(NEW_SINGLE_PROBE_DATA_SET_STR, NEW_SINGLE_PROBE_DATA_SET);     
		sTheStringsLookup.put(NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC_STR, NEW_SINGLE_PROBE_DATA_SET_BUTTON_DESC);     
		sTheStringsLookup.put(NEW_MULTIPLE_PROBE_DATA_SET_STR, NEW_MULTIPLE_PROBE_DATA_SET);     
		sTheStringsLookup.put(NEW_MULTIPLE_PROBE_DATA_SET_BUTTON_DESC_STR, NEW_MULTIPLE_PROBE_DATA_SET_BUTTON_DESC);     
		sTheStringsLookup.put(NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_STR, NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET);     
		sTheStringsLookup.put(NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_BUTTON_DESC_STR, NEW_MULTI_RUN_SINGLE_PROBE_DATA_SET_BUTTON_DESC);     
		sTheStringsLookup.put(SAVE_STR, SAVE);     
		sTheStringsLookup.put(SAVE_DATA_SET_STR, SAVE_DATA_SET);     
		sTheStringsLookup.put(SAVE_DATA_SET_BUTTON_DESC_STR, SAVE_DATA_SET_BUTTON_DESC);     
		sTheStringsLookup.put(OPEN_DATA_SET_STR, OPEN_DATA_SET);     
		sTheStringsLookup.put(OPEN_DATA_SET_BUTTON_DESC_STR, OPEN_DATA_SET_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_CSV_FILES_STR, IMPORT_CSV_FILES);     
		sTheStringsLookup.put(IMPORT_CSV_FILES_BUTTON_DESC_STR, IMPORT_CSV_FILES_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_CSV_DATA_FILES_DIALOG_TITLE_STR, IMPORT_CSV_DATA_FILES_DIALOG_TITLE);     
		sTheStringsLookup.put(IMPORT_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_LABEL_STR, IMPORT_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_LABEL);     
		sTheStringsLookup.put(IMPORT_MULTIPLE_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_DESC_STR, IMPORT_MULTIPLE_SINGLE_PROBE_MULTI_RUN_FILES_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL_STR, IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_LABEL);     
		sTheStringsLookup.put(IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_DESC_STR, IMPORT_SINGLE_U_MEASUREMENTS_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_SINGLE_U_MEASUREMENTS_DIALOG_TITLE_STR, IMPORT_SINGLE_U_MEASUREMENTS_DIALOG_TITLE);     
		sTheStringsLookup.put(CHOOSE_SINGLE_U_MEASUREMENTS_FILE_STR, CHOOSE_SINGLE_U_MEASUREMENTS_FILE);     
		sTheStringsLookup.put(OK_STR, OK);     
		sTheStringsLookup.put(CANCEL_STR, CANCEL);     
		sTheStringsLookup.put(CLOSE_STR, CLOSE);     
		sTheStringsLookup.put(Y_COORD_LABEL_STR, Y_COORD_LABEL);     
		sTheStringsLookup.put(Z_COORD_LABEL_STR, Z_COORD_LABEL);     
		sTheStringsLookup.put(DATA_SET_STR, DATA_SET);     
		sTheStringsLookup.put(IMPORTED_FILE_STR, IMPORTED_FILE);     
		sTheStringsLookup.put(DATA_SET_OPEN_ERROR_TITLE_STR, DATA_SET_OPEN_ERROR_TITLE);     
		sTheStringsLookup.put(DATA_SET_OPEN_ERROR_MSG_STR, DATA_SET_OPEN_ERROR_MSG);     
		sTheStringsLookup.put(FILE_EXISTS_DIALOG_TITLE_STR, FILE_EXISTS_DIALOG_TITLE);     
		sTheStringsLookup.put(FILE_EXISTS_DIALOG_MSG_STR, FILE_EXISTS_DIALOG_MSG);     
		sTheStringsLookup.put(FILE_NAME_WRONG_EXTENSION_DIALOG_TITLE_STR, FILE_NAME_WRONG_EXTENSION_DIALOG_TITLE);     
		sTheStringsLookup.put(FILE_NAME_WRONG_EXTENSION_DIALOG_MSG_STR, FILE_NAME_WRONG_EXTENSION_DIALOG_MSG);     
		sTheStringsLookup.put(ERROR_CREATING_FILE_DIALOG_TITLE_STR, ERROR_CREATING_FILE_DIALOG_TITLE);     
		sTheStringsLookup.put(ERROR_CREATING_FILE_DIALOG_MSG_STR, ERROR_CREATING_FILE_DIALOG_MSG);     
		sTheStringsLookup.put(DATA_POINT_FROM_FILE_DATA_ERROR_TITLE_STR, DATA_POINT_FROM_FILE_DATA_ERROR_TITLE);     
		sTheStringsLookup.put(DATA_POINT_FROM_FILE_DATA_ERROR_MSG_STR, DATA_POINT_FROM_FILE_DATA_ERROR_MSG);     
		sTheStringsLookup.put(INVALID_VELOCITY_COMPONENT_TITLE_STR, INVALID_VELOCITY_COMPONENT_TITLE);     
		sTheStringsLookup.put(INVALID_VELOCITY_COMPONENT_MSG_STR, INVALID_VELOCITY_COMPONENT_MSG);     
		sTheStringsLookup.put(INVALID_VELOCITY_COMPONENT_XML_TAG_TITLE_STR, INVALID_VELOCITY_COMPONENT_XML_TAG_TITLE);     
		sTheStringsLookup.put(INVALID_VELOCITY_COMPONENT_XML_TAG_MSG_STR, INVALID_VELOCITY_COMPONENT_XML_TAG_MSG);     
		sTheStringsLookup.put(INVALID_CHARACTER_IN_CSV_FILE_TITLE_STR, INVALID_CHARACTER_IN_CSV_FILE_TITLE);     
		sTheStringsLookup.put(INVALID_CHARACTER_IN_CSV_FILE_MSG_STR, INVALID_CHARACTER_IN_CSV_FILE_MSG);     
		sTheStringsLookup.put(CSV_FILE_FILTER_NAME_STR, CSV_FILE_FILTER_NAME);     
		sTheStringsLookup.put(DATA_POINTS_STR, DATA_POINTS);     
		sTheStringsLookup.put(ERROR_GETTING_POINT_DATA_TITLE_STR, ERROR_GETTING_POINT_DATA_TITLE);     
		sTheStringsLookup.put(ERROR_GETTING_POINT_DATA_MSG_STR, ERROR_GETTING_POINT_DATA_MSG);     
		sTheStringsLookup.put(MEAN_LABEL_STR, MEAN_LABEL);     
		sTheStringsLookup.put(STDEV_LABEL_STR, STDEV_LABEL);     
		sTheStringsLookup.put(FILTERED_MEAN_LABEL_STR, FILTERED_MEAN_LABEL);     
		sTheStringsLookup.put(FILTERED_STDEV_LABEL_STR, FILTERED_STDEV_LABEL);     
		sTheStringsLookup.put(POINT_DATA_STR, POINT_DATA);     
		sTheStringsLookup.put(CONFIGURATION_STR, CONFIGURATION);     
		sTheStringsLookup.put(CONFIGURATION_BUTTON_DESC_STR, CONFIGURATION_BUTTON_DESC);     
		sTheStringsLookup.put(CONFIGURATION_DIALOG_TITLE_STR, CONFIGURATION_DIALOG_TITLE);     
		sTheStringsLookup.put(DEFAULT_DATA_FILE_PATH_LABEL_STR, DEFAULT_DATA_FILE_PATH_LABEL);     
		sTheStringsLookup.put(SELECT_STR, SELECT);     
		sTheStringsLookup.put(SELECT_DATA_FILE_PATH_DIALOG_TITLE_STR, SELECT_DATA_FILE_PATH_DIALOG_TITLE);     
		sTheStringsLookup.put(NEW_DATA_SET_FILENAME_DIALOG_TITLE_STR, NEW_DATA_SET_FILENAME_DIALOG_TITLE);     
		sTheStringsLookup.put(NO_SUCH_DATA_POINT_TITLE_STR, NO_SUCH_DATA_POINT_TITLE);     
		sTheStringsLookup.put(NO_SUCH_DATA_POINT_MSG_STR, NO_SUCH_DATA_POINT_MSG);     
		sTheStringsLookup.put(DATA_POINT_DETAILS_READ_ERROR_TITLE_STR, DATA_POINT_DETAILS_READ_ERROR_TITLE);     
		sTheStringsLookup.put(DATA_POINT_DETAILS_READ_ERROR_MSG_STR, DATA_POINT_DETAILS_READ_ERROR_MSG);     
		sTheStringsLookup.put(U_MEAN_LABEL_STR, U_MEAN_LABEL);     
		sTheStringsLookup.put(U_STDEV_LABEL_STR, U_STDEV_LABEL);     
		sTheStringsLookup.put(U_FILTERED_MEAN_LABEL_STR, U_FILTERED_MEAN_LABEL);     
		sTheStringsLookup.put(U_FILTERED_STDEV_LABEL_STR, U_FILTERED_STDEV_LABEL);     
		sTheStringsLookup.put(V_MEAN_LABEL_STR, V_MEAN_LABEL);     
		sTheStringsLookup.put(V_STDEV_LABEL_STR, V_STDEV_LABEL);     
		sTheStringsLookup.put(V_FILTERED_MEAN_LABEL_STR, V_FILTERED_MEAN_LABEL);     
		sTheStringsLookup.put(V_FILTERED_STDEV_LABEL_STR, V_FILTERED_STDEV_LABEL);     
		sTheStringsLookup.put(W_MEAN_LABEL_STR, W_MEAN_LABEL);     
		sTheStringsLookup.put(W_STDEV_LABEL_STR, W_STDEV_LABEL);     
		sTheStringsLookup.put(W_FILTERED_MEAN_LABEL_STR, W_FILTERED_MEAN_LABEL);     
		sTheStringsLookup.put(W_FILTERED_STDEV_LABEL_STR, W_FILTERED_STDEV_LABEL);     
		sTheStringsLookup.put(U_VELOCITIES_LABEL_STR, U_VELOCITIES_LABEL);     
		sTheStringsLookup.put(V_VELOCITIES_LABEL_STR, V_VELOCITIES_LABEL);     
		sTheStringsLookup.put(W_VELOCITIES_LABEL_STR, W_VELOCITIES_LABEL);     
		sTheStringsLookup.put(U_UNFILTERED_VELOCITIES_LABEL_STR, U_UNFILTERED_VELOCITIES_LABEL);     
		sTheStringsLookup.put(V_UNFILTERED_VELOCITIES_LABEL_STR, V_UNFILTERED_VELOCITIES_LABEL);     
		sTheStringsLookup.put(W_UNFILTERED_VELOCITIES_LABEL_STR, W_UNFILTERED_VELOCITIES_LABEL);     
		sTheStringsLookup.put(U_FILTERED_VELOCITIES_LABEL_STR, U_FILTERED_VELOCITIES_LABEL);     
		sTheStringsLookup.put(V_FILTERED_VELOCITIES_LABEL_STR, V_FILTERED_VELOCITIES_LABEL);     
		sTheStringsLookup.put(W_FILTERED_VELOCITIES_LABEL_STR, W_FILTERED_VELOCITIES_LABEL);     
		sTheStringsLookup.put(EXCLUDE_LEVEL_LABEL_STR, EXCLUDE_LEVEL_LABEL);     
		sTheStringsLookup.put(DATA_FILE_DELIMITER_LABEL_STR, DATA_FILE_DELIMITER_LABEL);     
		sTheStringsLookup.put(DATA_FILE_DELIMITER_OPTIONS_STR, DATA_FILE_DELIMITER_OPTIONS);     
		sTheStringsLookup.put(DATA_FILE_CSV_DECIMAL_SEPARATOR_LABEL_STR, DATA_FILE_CSV_DECIMAL_SEPARATOR_LABEL);     
		sTheStringsLookup.put(DATA_FILE_CSV_DECIMAL_SEPARATOR_OPTIONS_STR, DATA_FILE_CSV_DECIMAL_SEPARATOR_OPTIONS);     
		sTheStringsLookup.put(DEFAULT_CSV_FILE_PATH_LABEL_STR, DEFAULT_CSV_FILE_PATH_LABEL);     
		sTheStringsLookup.put(SELECT_CSV_FILE_PATH_DIALOG_TITLE_STR, SELECT_CSV_FILE_PATH_DIALOG_TITLE);     
		sTheStringsLookup.put(CHOOSE_CSV_FILE_STR, CHOOSE_CSV_FILE);     
		sTheStringsLookup.put(ADD_ANOTHER_CSV_FILE_STR, ADD_ANOTHER_CSV_FILE);     
		sTheStringsLookup.put(DATA_SET_GRAPHS_STR, DATA_SET_GRAPHS);     
		sTheStringsLookup.put(CROSS_SECTION_GRID_X_AXIS_TITLE_STR, CROSS_SECTION_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(CROSS_SECTION_GRID_Y_AXIS_TITLE_STR, CROSS_SECTION_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(U_VELOCITY_CROSS_SECTION_GRID_TITLE_STR, U_VELOCITY_CROSS_SECTION_GRID_TITLE);     
		sTheStringsLookup.put(U_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT_STR, U_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT);     
		sTheStringsLookup.put(V_VELOCITY_CROSS_SECTION_GRID_TITLE_STR, V_VELOCITY_CROSS_SECTION_GRID_TITLE);     
		sTheStringsLookup.put(V_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT_STR, V_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT);     
		sTheStringsLookup.put(W_VELOCITY_CROSS_SECTION_GRID_TITLE_STR, W_VELOCITY_CROSS_SECTION_GRID_TITLE);     
		sTheStringsLookup.put(W_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT_STR, W_VELOCITY_CROSS_SECTION_GRID_LEGEND_TEXT);     
		sTheStringsLookup.put(CHOOSE_DIRECTORY_STR, CHOOSE_DIRECTORY);     
		sTheStringsLookup.put(FILENAME_LABEL_STR, FILENAME_LABEL);     
		sTheStringsLookup.put(ERROR_MOVING_TEMP_FILE_TITLE_STR, ERROR_MOVING_TEMP_FILE_TITLE);     
		sTheStringsLookup.put(ERROR_MOVING_TEMP_FILE_MSG_STR, ERROR_MOVING_TEMP_FILE_MSG);     
		sTheStringsLookup.put(APPLY_STR, APPLY);     
		sTheStringsLookup.put(UPDATING_SUMMARY_DATA_STR, UPDATING_SUMMARY_DATA);     
		sTheStringsLookup.put(FLUID_DENSITY_LABEL_STR, FLUID_DENSITY_LABEL);
		sTheStringsLookup.put(FLUID_KINEMATIC_VISCOSITY_LABEL_STR, FLUID_KINEMATIC_VISCOSITY_LABEL);
		sTheStringsLookup.put(LEFT_BANK_POSITION_LABEL_STR, LEFT_BANK_POSITION_LABEL);     
		sTheStringsLookup.put(RIGHT_BANK_POSITION_LABEL_STR, RIGHT_BANK_POSITION_LABEL);     
		sTheStringsLookup.put(WATER_DEPTH_POSITION_LABEL_STR, WATER_DEPTH_POSITION_LABEL);     
		sTheStringsLookup.put(ERROR_GETTING_CROSS_SECTION_DATA_TITLE_STR, ERROR_GETTING_CROSS_SECTION_DATA_TITLE);     
		sTheStringsLookup.put(ERROR_GETTING_CROSS_SECTION_DATA_MSG_STR, ERROR_GETTING_CROSS_SECTION_DATA_MSG);     
		sTheStringsLookup.put(INVERT_X_AXIS_LABEL_STR, INVERT_X_AXIS_LABEL);     
		sTheStringsLookup.put(INVERT_Y_AXIS_LABEL_STR, INVERT_Y_AXIS_LABEL);     
		sTheStringsLookup.put(INVERT_Z_AXIS_LABEL_STR, INVERT_Z_AXIS_LABEL);     
		sTheStringsLookup.put(LOCK_DATA_SET_LABEL_STR, LOCK_DATA_SET_LABEL);     
		sTheStringsLookup.put(RUN_INDEX_LABEL_STR, RUN_INDEX_LABEL);     
		sTheStringsLookup.put(XZ_PLANE_ROTATION_THETA_LABEL_STR, XZ_PLANE_ROTATION_THETA_LABEL);     
		sTheStringsLookup.put(YZ_PLANE_ROTATION_PHI_LABEL_STR, YZ_PLANE_ROTATION_PHI_LABEL);     
		sTheStringsLookup.put(XY_PLANE_ROTATION_ALPHA_LABEL_STR, XY_PLANE_ROTATION_ALPHA_LABEL);     
		sTheStringsLookup.put(PROBE_X_VELOCITIES_LABEL_STR, PROBE_X_VELOCITIES_LABEL);     
		sTheStringsLookup.put(PROBE_Y_VELOCITIES_LABEL_STR, PROBE_Y_VELOCITIES_LABEL);     
		sTheStringsLookup.put(PROBE_Z_VELOCITIES_LABEL_STR, PROBE_Z_VELOCITIES_LABEL);     
		sTheStringsLookup.put(MEASURED_DISCHARGE_LABEL_STR, MEASURED_DISCHARGE_LABEL);     
		sTheStringsLookup.put(SAVING_DATA_SET_STR, SAVING_DATA_SET);     
		sTheStringsLookup.put(IMPORTING_SINGLE_U_MEASUREMENTS_STR, IMPORTING_SINGLE_U_MEASUREMENTS);     
		sTheStringsLookup.put(RECALCULATING_DATA_PROGRESS_TITLE_STR, RECALCULATING_DATA_PROGRESS_TITLE);     
		sTheStringsLookup.put(CALCULATING_DATA_POINT_SUMMARY_FIELDS_TITLE_STR, CALCULATING_DATA_POINT_SUMMARY_FIELDS_TITLE);     
		sTheStringsLookup.put(CREATING_RC_BATCH_PROGRESS_TITLE_STR, CREATING_RC_BATCH_PROGRESS_TITLE);     
		sTheStringsLookup.put(ERROR_CREATING_RC_BATCH_DIALOG_TITLE_STR, ERROR_CREATING_RC_BATCH_DIALOG_TITLE);     
		sTheStringsLookup.put(ERROR_CREATING_RC_BATCH_DIALOG_MSG_STR, ERROR_CREATING_RC_BATCH_DIALOG_MSG);     
		sTheStringsLookup.put(CREATING_RC_BATCHES_PROGRESS_TITLE_STR, CREATING_RC_BATCHES_PROGRESS_TITLE);     
		sTheStringsLookup.put(ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_TITLE_STR, ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_TITLE);     
		sTheStringsLookup.put(ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_MSG_STR, ERROR_CREATING_RC_BATCH_FROM_FILE_DIALOG_MSG);     
		sTheStringsLookup.put(DATA_FILES_CONFIG_TAB_LABEL_STR, DATA_FILES_CONFIG_TAB_LABEL);     
		sTheStringsLookup.put(DATA_SET_CONFIG_TAB_LABEL_STR, DATA_SET_CONFIG_TAB_LABEL);     
		sTheStringsLookup.put(DATA_POINT_CONFIG_TAB_LABEL_STR, DATA_POINT_CONFIG_TAB_LABEL);     
		sTheStringsLookup.put(DATA_FILE_VELOCITY_UNIT_OPTIONS_STR, DATA_FILE_VELOCITY_UNIT_OPTIONS);     
		sTheStringsLookup.put(DATA_SET_CREATION_ERROR_TITLE_STR, DATA_SET_CREATION_ERROR_TITLE);     
		sTheStringsLookup.put(DATA_SET_CREATION_ERROR_MSG_STR, DATA_SET_CREATION_ERROR_MSG);     
		sTheStringsLookup.put(DATA_FILE_VELOCITY_UNITS_LABEL_STR, DATA_FILE_VELOCITY_UNITS_LABEL);     
		sTheStringsLookup.put(VIEW_DATA_POINT_DETAILS_BUTTON_LABEL_STR, VIEW_DATA_POINT_DETAILS_BUTTON_LABEL);     
		sTheStringsLookup.put(VIEW_DATA_POINT_DETAILS_BUTTON_DESC_STR, VIEW_DATA_POINT_DETAILS_BUTTON_DESC);     
		sTheStringsLookup.put(CROSS_SECTION_GRAPHS_STR, CROSS_SECTION_GRAPHS);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_GRID_TITLE_STR, VERTICAL_VELOCITY_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_GRID_X_AXIS_TITLE_STR, VERTICAL_VELOCITY_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE_STR, VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_VELOCITY_GRID_TITLE_STR, HORIZONTAL_VELOCITY_GRID_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE_STR, HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE_STR, HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_VELOCITY_GRID_LEGEND_KEY_LABEL_STR, HORIZONTAL_VELOCITY_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL_STR, VERTICAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL_STR, HORIZONTAL_SECTION_GRAPH_SERIES_CHOOSER_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_GRID_TITLE_STR, DEPTH_AVERAGED_VELOCITY_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DERIVATIVE_GRID_SECONDARY_Y_AXIS_TITLE_STR, DERIVATIVE_GRID_SECONDARY_Y_AXIS_TITLE);     
		sTheStringsLookup.put(LATERAL_VELOCITY_GRAPH_BUTTON_LABEL_STR, LATERAL_VELOCITY_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(LATERAL_VELOCITY_GRAPH_BUTTON_DESC_STR, LATERAL_VELOCITY_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(LATERAL_VELOCITY_GRID_TITLE_STR, LATERAL_VELOCITY_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_GRAPH_BUTTON_LABEL_STR, DEPTH_AVERAGED_UV_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC_STR, DEPTH_AVERAGED_UV_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_GRAPH_TITLE_STR, DEPTH_AVERAGED_UV_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_UV_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_UV_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DATA_POINT_DETAILS_FRAME_TITLE_STR, DATA_POINT_DETAILS_FRAME_TITLE);     
		sTheStringsLookup.put(VELOCITY_DETAILS_TABLE_TAB_STR, VELOCITY_DETAILS_TABLE_TAB);     
		sTheStringsLookup.put(BED_SLOPE_LABEL_STR, BED_SLOPE_LABEL);     
		sTheStringsLookup.put(CALCULATED_MEAN_SHEAR_STRESS_LABEL_STR, CALCULATED_MEAN_SHEAR_STRESS_LABEL);     
		sTheStringsLookup.put(CONSERVATION_OF_MOMENTUM_MEAN_SHEAR_STRESS_LABEL_STR, CONSERVATION_OF_MOMENTUM_MEAN_SHEAR_STRESS_LABEL);     
		sTheStringsLookup.put(UV_FLUCTUATIONS_MEAN_LABEL_STR, UV_FLUCTUATIONS_MEAN_LABEL);     
		sTheStringsLookup.put(UW_FLUCTUATIONS_MEAN_LABEL_STR, UW_FLUCTUATIONS_MEAN_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR, DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR, DEPTH_AVERAGED_REYNOLDS_STRESS_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_TITLE_STR, DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VECTRINO_HEADER_FILE_MISSING_TITLE_STR, VECTRINO_HEADER_FILE_MISSING_TITLE);     
		sTheStringsLookup.put(VECTRINO_HEADER_FILE_MISSING_MSG_STR, VECTRINO_HEADER_FILE_MISSING_MSG);     
		sTheStringsLookup.put(INVALID_LINE_IN_VECTRINO_DATA_FILE_TITLE_STR, INVALID_LINE_IN_VECTRINO_DATA_FILE_TITLE);     
		sTheStringsLookup.put(INVALID_LINE_IN_VECTRINO_DATA_FILE_MSG_STR, INVALID_LINE_IN_VECTRINO_DATA_FILE_MSG);     
		sTheStringsLookup.put(IMPORT_BINARY_DATA_FILES_BUTTON_LABEL_STR, IMPORT_BINARY_DATA_FILES_BUTTON_LABEL);     
		sTheStringsLookup.put(IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC_STR, IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_DIALOG_TITLE_STR, IMPORT_SINGLE_PROBE_BINARY_DATA_FILES_DIALOG_TITLE);     
		sTheStringsLookup.put(IMPORT_CONVERTED_VNO_FILES_STR, IMPORT_CONVERTED_VNO_FILES);     
		sTheStringsLookup.put(IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC_STR, IMPORT_CONVERTED_VECTRINO_FILES_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_MULTIPLE_CONVERTED_VECTRINO_FILES_DIALOG_TITLE_STR, IMPORT_MULTIPLE_CONVERTED_VECTRINO_FILES_DIALOG_TITLE);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_LEGEND_TEXT_STR, VERTICAL_REYNOLDS_STRESS_LEGEND_TEXT);     
		sTheStringsLookup.put(HORIZONTAL_REYNOLDS_STRESS_LEGEND_TEXT_STR, HORIZONTAL_REYNOLDS_STRESS_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_LABEL_STR, VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC_STR, VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_TITLE_STR, VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_X_AXIS_TITLE_STR, VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_FULL_SERIES_LINEAR_EXTRAPOLATION_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_FULL_SERIES_LINEAR_EXTRAPOLATION_SERIES_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_BOTTOM_POINTS_LINEAR_EXTRAPOLATION_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_BOTTOM_POINTS_LINEAR_EXTRAPOLATION_SERIES_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_MEAN_OF_BOTTOM_THREE_POINTS_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_MEAN_OF_BOTTOM_THREE_POINTS_SERIES_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_SMOOTH_BED_LOG_LAW_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_SMOOTH_BED_LOG_LAW_SERIES_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_KS_FROM_ROUGH_BED_LOG_LAW_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_KS_FROM_ROUGH_BED_LOG_LAW_SERIES_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_10_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_10_SERIES_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_20_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_20_SERIES_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_POINTS_ABOVE_0_POINT_2LINEAR_EXTRAPOLATION_SERIES_LABEL_STR, VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_POINTS_ABOVE_0_POINT_2LINEAR_EXTRAPOLATION_SERIES_LABEL);     
		sTheStringsLookup.put(IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_BUTTON_DESC_STR, IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_DIALOG_TITLE_STR, IMPORT_MULTIPLE_CONVERTED_POLYSYNC_FILES_DIALOG_TITLE);     
		sTheStringsLookup.put(POLYSYNC_COLUMN_HEADINGS_MISSING_TITLE_STR, POLYSYNC_COLUMN_HEADINGS_MISSING_TITLE);     
		sTheStringsLookup.put(POLYSYNC_COLUMN_HEADINGS_MISSING_MSG_STR, POLYSYNC_COLUMN_HEADINGS_MISSING_MSG);     
		sTheStringsLookup.put(POLYSYNC_CORRUPT_LINE_TITLE_STR, POLYSYNC_CORRUPT_LINE_TITLE);     
		sTheStringsLookup.put(POLYSYNC_CORRUPT_LINE_MSG_STR, POLYSYNC_CORRUPT_LINE_MSG);     
		sTheStringsLookup.put(CORRECT_ROTATION_LABEL_STR, CORRECT_ROTATION_LABEL);     
		sTheStringsLookup.put(PERCENTAGE_OF_VELOCITIES_GOOD_LABEL_STR, PERCENTAGE_OF_VELOCITIES_GOOD_LABEL);     
		sTheStringsLookup.put(DESPIKING_FILTER_NONE_STR, DESPIKING_FILTER_NONE);     
		sTheStringsLookup.put(DESPIKING_FILTER_REMOVE_ZEROES_LEVEL_STR, DESPIKING_FILTER_REMOVE_ZEROES_LEVEL);     
		sTheStringsLookup.put(DESPIKING_FILTER_EXCLUDE_LEVEL_STR, DESPIKING_FILTER_EXCLUDE_LEVEL);     
		sTheStringsLookup.put(DESPIKING_FILTER_VELOCITY_CORRELATION_STR, DESPIKING_FILTER_VELOCITY_CORRELATION);     
		sTheStringsLookup.put(DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_STR, DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING);     
		sTheStringsLookup.put(DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_STR, DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING);     
		sTheStringsLookup.put(DESPIKING_FILTER_CORRELATION_AND_SNR_STR, DESPIKING_FILTER_CORRELATION_AND_SNR);     
		sTheStringsLookup.put(DESPIKING_FILTER_TYPE_LABEL_STR, DESPIKING_FILTER_TYPE_LABEL);     
		sTheStringsLookup.put(DESPIKING_FILTER_NONE_REFERENCE_STR, DESPIKING_FILTER_NONE_REFERENCE);     
		sTheStringsLookup.put(DESPIKING_FILTER_EXCLUDE_LEVEL_REFERENCE_STR, DESPIKING_FILTER_EXCLUDE_LEVEL_REFERENCE);     
		sTheStringsLookup.put(DESPIKING_FILTER_VELOCITY_CORRELATION_REFERENCE_STR, DESPIKING_FILTER_VELOCITY_CORRELATION_REFERENCE);     
		sTheStringsLookup.put(DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_REFERENCE_STR, DESPIKING_FILTER_PHASE_SPACE_THRESHOLDING_REFERENCE);     
		sTheStringsLookup.put(DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_REFERENCE_STR, DESPIKING_FILTER_MODIFIED_PHASE_SPACE_THRESHOLDING_REFERENCE);     
		sTheStringsLookup.put(DESPIKING_FILTER_CORRELATION_AND_SNR_REFERENCE_STR, DESPIKING_FILTER_CORRELATION_AND_SNR_REFERENCE);     
		sTheStringsLookup.put(DATA_POINT_DATA_TABLE_TAB_LABEL_STR, DATA_POINT_DATA_TABLE_TAB_LABEL);     
		sTheStringsLookup.put(DATA_POINT_GRAPHS_TAB_LABEL_STR, DATA_POINT_GRAPHS_TAB_LABEL);     
		sTheStringsLookup.put(VELOCITY_LABEL_STR, VELOCITY_LABEL);     
		sTheStringsLookup.put(IMPORT_ALL_STR, IMPORT_ALL);     
		sTheStringsLookup.put(IMPORT_SELECTED_STR, IMPORT_SELECTED);     
		sTheStringsLookup.put(IMPORT_MULTI_PROBE_BINARY_FILES_BUTTON_DESC_STR, IMPORT_MULTI_PROBE_BINARY_FILES_BUTTON_DESC);     
		sTheStringsLookup.put(IMPORT_MULTI_PROBE_BINARY_FILES_DIALOG_TITLE_STR, IMPORT_MULTI_PROBE_BINARY_FILES_DIALOG_TITLE);     
		sTheStringsLookup.put(MAIN_PROBE_STR, MAIN_PROBE);     
		sTheStringsLookup.put(FIXED_PROBE_STR, FIXED_PROBE);     
		sTheStringsLookup.put(NUMBER_OF_PROBES_STR, NUMBER_OF_PROBES);     
		sTheStringsLookup.put(INVALID_FIXED_PROBE_INDEX_ERROR_TITLE_STR, INVALID_FIXED_PROBE_INDEX_ERROR_TITLE);     
		sTheStringsLookup.put(INVALID_FIXED_PROBE_INDEX_ERROR_MSG_STR, INVALID_FIXED_PROBE_INDEX_ERROR_MSG);     
		sTheStringsLookup.put(INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE_STR, INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_TITLE);     
		sTheStringsLookup.put(INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG_STR, INCORRECT_NUMBER_OF_PROBES_IN_DATA_POINT_ERROR_MSG);     
		sTheStringsLookup.put(FIXED_PROBE_INDEX_LABEL_STR, FIXED_PROBE_INDEX_LABEL);     
		sTheStringsLookup.put(Y_OFFSET_STR, Y_OFFSET);     
		sTheStringsLookup.put(Z_OFFSET_STR, Z_OFFSET);     
		sTheStringsLookup.put(ERROR_OPENING_DATA_SET_DIALOG_TITLE_STR, ERROR_OPENING_DATA_SET_DIALOG_TITLE);     
		sTheStringsLookup.put(ERROR_OPENING_DATA_SET_DIALOG_MSG_STR, ERROR_OPENING_DATA_SET_DIALOG_MSG);     
		sTheStringsLookup.put(ERROR_SAVING_DATA_SET_DIALOG_TITLE_STR, ERROR_SAVING_DATA_SET_DIALOG_TITLE);     
		sTheStringsLookup.put(ERROR_SAVING_DATA_SET_DIALOG_MSG_STR, ERROR_SAVING_DATA_SET_DIALOG_MSG);     
		sTheStringsLookup.put(ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE_STR, ERROR_IMPORTING_DATA_POINT_DIALOG_TITLE);     
		sTheStringsLookup.put(ERROR_IMPORTING_DATA_POINT_DIALOG_MSG_STR, ERROR_IMPORTING_DATA_POINT_DIALOG_MSG);     
		sTheStringsLookup.put(ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_TITLE_STR, ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_TITLE);     
		sTheStringsLookup.put(ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_MSG_STR, ERROR_GETTING_UNSORTED_DATA_POINT_COORDINATES_MSG);     
		sTheStringsLookup.put(ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE_STR, ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_TITLE);     
		sTheStringsLookup.put(ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG_STR, ERROR_GETTING_SORTED_DATA_POINT_COORDINATES_MSG);     
		sTheStringsLookup.put(ERROR_CREATING_NEW_DATA_SET_TITLE_STR, ERROR_CREATING_NEW_DATA_SET_TITLE);     
		sTheStringsLookup.put(ERROR_CREATING_NEW_DATA_SET_MSG_STR, ERROR_CREATING_NEW_DATA_SET_MSG);     
		sTheStringsLookup.put(ERROR_LOADING_DATA_POINT_DETAILS_TITLE_STR, ERROR_LOADING_DATA_POINT_DETAILS_TITLE);     
		sTheStringsLookup.put(ERROR_LOADING_DATA_POINT_DETAILS_MSG_STR, ERROR_LOADING_DATA_POINT_DETAILS_MSG);     
		sTheStringsLookup.put(ERROR_CLEARING_DATA_POINT_DETAILS_TITLE_STR, ERROR_CLEARING_DATA_POINT_DETAILS_TITLE);     
		sTheStringsLookup.put(ERROR_CLEARING_DATA_POINT_DETAILS_MSG_STR, ERROR_CLEARING_DATA_POINT_DETAILS_MSG);     
		sTheStringsLookup.put(ERROR_GETTING_CONFIG_DATA_TITLE_STR, ERROR_GETTING_CONFIG_DATA_TITLE);     
		sTheStringsLookup.put(ERROR_GETTING_CONFIG_DATA_MSG_STR, ERROR_GETTING_CONFIG_DATA_MSG);     
		sTheStringsLookup.put(ERROR_SETTING_CONFIG_DATA_TITLE_STR, ERROR_SETTING_CONFIG_DATA_TITLE);     
		sTheStringsLookup.put(ERROR_SETTING_CONFIG_DATA_MSG_STR, ERROR_SETTING_CONFIG_DATA_MSG);     
		sTheStringsLookup.put(ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_TITLE_STR, ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_TITLE);     
		sTheStringsLookup.put(ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_MSG_STR, ERROR_DOING_IMPORT_COMPLETE_TASKS_DATA_MSG);     
		sTheStringsLookup.put(ERROR_RECALCULATING_SUMMARY_DATA_TITLE_STR, ERROR_RECALCULATING_SUMMARY_DATA_TITLE);     
		sTheStringsLookup.put(ERROR_RECALCULATING_SUMMARY_DATA_MSG_STR, ERROR_RECALCULATING_SUMMARY_DATA_MSG);     
		sTheStringsLookup.put(ERROR_GETTING_DATA_SET_INFO_TITLE_STR, ERROR_GETTING_DATA_SET_INFO_TITLE);     
		sTheStringsLookup.put(ERROR_GETTING_DATA_SET_INFO_MSG_STR, ERROR_GETTING_DATA_SET_INFO_MSG);     
		sTheStringsLookup.put(ERROR_DATA_SET_ALREADY_OPEN_TITLE_STR, ERROR_DATA_SET_ALREADY_OPEN_TITLE);     
		sTheStringsLookup.put(ERROR_DATA_SET_ALREADY_OPEN_MSG_STR, ERROR_DATA_SET_ALREADY_OPEN_MSG);     
		sTheStringsLookup.put(ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_TITLE_STR, ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_TITLE);     
		sTheStringsLookup.put(ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_MSG_STR, ERROR_GETTING_FIXED_PROBE_DATA_SET_IDS_MSG);     
		sTheStringsLookup.put(ERROR_CLOSING_DATA_SET_TITLE_STR, ERROR_CLOSING_DATA_SET_TITLE);     
		sTheStringsLookup.put(ERROR_CLOSING_DATA_SET_MSG_STR, ERROR_CLOSING_DATA_SET_MSG);     
		sTheStringsLookup.put(MAIN_PROBE_INDEX_LABEL_STR, MAIN_PROBE_INDEX_LABEL);     
		sTheStringsLookup.put(INVALID_MAIN_PROBE_INDEX_ERROR_TITLE_STR, INVALID_MAIN_PROBE_INDEX_ERROR_TITLE);     
		sTheStringsLookup.put(INVALID_MAIN_PROBE_INDEX_ERROR_MSG_STR, INVALID_MAIN_PROBE_INDEX_ERROR_MSG);     
		sTheStringsLookup.put(INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_TITLE_STR, INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_TITLE);     
		sTheStringsLookup.put(INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_MSG_STR, INVALID_PROBE_INDICES_FIXED_EQUALS_MAIN_ERROR_MSG);     
		sTheStringsLookup.put(MAIN_DATA_POINTS_OVERVIEW_DISPLAY_LABEL_STR, MAIN_DATA_POINTS_OVERVIEW_DISPLAY_LABEL);     
		sTheStringsLookup.put(CONFIGURATION_TAB_LABEL_STR, CONFIGURATION_TAB_LABEL);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_V_PRIME_GRID_TITLE_STR, VERTICAL_U_PRIME_V_PRIME_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE_STR, VERTICAL_U_PRIME_V_PRIME_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE_STR, VERTICAL_U_PRIME_V_PRIME_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_U_PRIME_V_PRIME_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_W_PRIME_GRID_TITLE_STR, VERTICAL_U_PRIME_W_PRIME_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_W_PRIME_GRID_X_AXIS_TITLE_STR, VERTICAL_U_PRIME_W_PRIME_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_W_PRIME_GRID_Y_AXIS_TITLE_STR, VERTICAL_U_PRIME_W_PRIME_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_U_PRIME_W_PRIME_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_U_PRIME_W_PRIME_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_GRAPH_TAB_LABEL_STR, QUADRANT_HOLE_GRAPH_TAB_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_GRAPH_TITLE_STR, QUADRANT_HOLE_GRAPH_TITLE);     
		sTheStringsLookup.put(QUADRANT_HOLE_GRAPH_X_AXIS_LABEL_STR, QUADRANT_HOLE_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_GRAPH_Y_AXIS_LABEL_STR, QUADRANT_HOLE_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_SHORTHAND_LABEL_STR, QUADRANT_HOLE_SHORTHAND_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TAB_LABEL_STR, QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TAB_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TITLE_STR, QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_TITLE);     
		sTheStringsLookup.put(QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL_STR, QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL_STR, QUADRANT_HOLE_PROPORTION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TAB_LABEL_STR, QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TAB_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TITLE_STR, QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_TITLE);     
		sTheStringsLookup.put(QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL_STR, QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL_STR, QUADRANT_HOLE_DURATION_AT_HOLE_SIZE_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_CMSD_DURATION_GRAPH_TAB_LABEL_STR, QUADRANT_HOLE_CMSD_DURATION_GRAPH_TAB_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_CMSD_DURATION_GRAPH_TITLE_STR, QUADRANT_HOLE_CMSD_DURATION_GRAPH_TITLE);     
		sTheStringsLookup.put(QUADRANT_HOLE_CMSD_DURATION_GRAPH_X_AXIS_LABEL_STR, QUADRANT_HOLE_CMSD_DURATION_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_CMSD_DURATION_GRAPH_Y_AXIS_LABEL_STR, QUADRANT_HOLE_CMSD_DURATION_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(QUADRANT_1_LABEL_STR, QUADRANT_1_LABEL);     
		sTheStringsLookup.put(QUADRANT_2_LABEL_STR, QUADRANT_2_LABEL);     
		sTheStringsLookup.put(QUADRANT_3_LABEL_STR, QUADRANT_3_LABEL);     
		sTheStringsLookup.put(QUADRANT_4_LABEL_STR, QUADRANT_4_LABEL);     
		sTheStringsLookup.put(QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_TITLE_STR, QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_TITLE);     
		sTheStringsLookup.put(QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_Y_AXIS_LABEL_STR, QUADRANT_HOLE_U_PRIME_V_PRIME_PRODUCT_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(CALCULATE_CORRELATION_BUTTON_LABEL_STR, CALCULATE_CORRELATION_BUTTON_LABEL);     
		sTheStringsLookup.put(CALCULATE_CORRELATION_BUTTON_DESC_STR, CALCULATE_CORRELATION_BUTTON_DESC);     
		sTheStringsLookup.put(CALCULATE_CORRELATION_ERROR_TITLE_STR, CALCULATE_CORRELATION_ERROR_TITLE);     
		sTheStringsLookup.put(CALCULATE_CORRELATION_ERROR_MSG_STR, CALCULATE_CORRELATION_ERROR_MSG);     
		sTheStringsLookup.put(SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR, SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR, SHOW_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC);     
		sTheStringsLookup.put(UW_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR, UW_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE);     
		sTheStringsLookup.put(UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR, UW_NORMALISED_QUADRANT_HOLE_FRAME_TITLE);     
		sTheStringsLookup.put(UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE_STR, UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE);     
		sTheStringsLookup.put(UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE_STR, UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE_STR, UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL_STR, UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC_STR, UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL_STR, UW_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR, SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR, SHOW_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC);     
		sTheStringsLookup.put(UV_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR, UV_UNNORMALISED_QUADRANT_HOLE_FRAME_TITLE);     
		sTheStringsLookup.put(UV_NORMALISED_QUADRANT_HOLE_FRAME_TITLE_STR, UV_NORMALISED_QUADRANT_HOLE_FRAME_TITLE);     
		sTheStringsLookup.put(UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE_STR, UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_TITLE);     
		sTheStringsLookup.put(UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE_STR, UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE_STR, UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL_STR, UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC_STR, UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESS_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL_STR, UV_QUADRANT_HOLE_SPLIT_SHEAR_STRESSES_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_LABEL_STR, LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC_STR, LATERAL_FLUX_OF_STREAMWISE_TKE_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(LATERAL_TKE_FLUX_GRID_TITLE_STR, LATERAL_TKE_FLUX_GRID_TITLE);     
		sTheStringsLookup.put(TURBULENCE_INTENSITY_GRAPH_BUTTON_LABEL_STR, TURBULENCE_INTENSITY_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC_STR, TURBULENCE_INTENSITY_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(TURBULENCE_INTENSITY_GRAPH_FRAME_TITLE_STR, TURBULENCE_INTENSITY_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(X_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE_STR, X_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE);     
		sTheStringsLookup.put(Y_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE_STR, Y_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE);     
		sTheStringsLookup.put(Z_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE_STR, Z_DIRECTION_TURBULENCE_INTENSITY_GRID_TITLE);     
		sTheStringsLookup.put(X_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE_STR, X_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE);     
		sTheStringsLookup.put(Y_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE_STR, Y_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE);     
		sTheStringsLookup.put(Z_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE_STR, Z_DIRECTION_TURBULENCE_INTENSITY_SCALED_BY_Q_OVER_A_GRID_TITLE);     
		sTheStringsLookup.put(TURBULENCE_INTENSITY_GRAPH_LEGEND_TEXT_STR, TURBULENCE_INTENSITY_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR, VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR, VERTICAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_GRAPH_TITLE_STR, VERTICAL_REYNOLDS_STRESS_GRAPH_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR, HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR, HORIZONTAL_REYNOLDS_STRESS_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(HORIZONTAL_REYNOLDS_STRESS_GRAPH_TITLE_STR, HORIZONTAL_REYNOLDS_STRESS_GRAPH_TITLE);     
		sTheStringsLookup.put(V_PRIME_W_PRIME_GRAPH_BUTTON_LABEL_STR, V_PRIME_W_PRIME_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(V_PRIME_W_PRIME_GRAPH_BUTTON_DESC_STR, V_PRIME_W_PRIME_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(V_PRIME_W_PRIME_GRAPH_TITLE_STR, V_PRIME_W_PRIME_GRAPH_TITLE);     
		sTheStringsLookup.put(V_PRIME_W_PRIME_GRAPH_LEGEND_TEXT_STR, V_PRIME_W_PRIME_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(UW_CORRELATION_GRAPH_BUTTON_LABEL_STR, UW_CORRELATION_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(UW_CORRELATION_GRAPH_BUTTON_DESC_STR, UW_CORRELATION_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(UW_CORRELATION_GRAPH_TITLE_STR, UW_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(UW_CORRELATION_GRAPH_LEGEND_TEXT_STR, UW_CORRELATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_UW_CORRELATION_GRAPH_TITLE_STR, VERTICAL_UW_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_UW_CORRELATION_GRAPH_X_AXIS_TITLE_STR, VERTICAL_UW_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_UW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_UW_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_UW_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_UW_CORRELATION_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UW_CORRELATION_GRAPH_TITLE_STR, DEPTH_AVERAGED_UW_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UW_CORRELATION_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_UW_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_UW_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(UV_CORRELATION_GRAPH_BUTTON_LABEL_STR, UV_CORRELATION_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(UV_CORRELATION_GRAPH_BUTTON_DESC_STR, UV_CORRELATION_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(UV_CORRELATION_GRAPH_TITLE_STR, UV_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(UV_CORRELATION_GRAPH_LEGEND_TEXT_STR, UV_CORRELATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_UV_CORRELATION_GRAPH_TITLE_STR, VERTICAL_UV_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_UV_CORRELATION_GRAPH_X_AXIS_TITLE_STR, VERTICAL_UV_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_UV_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_UV_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_UV_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_UV_CORRELATION_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_CORRELATION_GRAPH_TITLE_STR, DEPTH_AVERAGED_UV_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_CORRELATION_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_UV_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_UV_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_UV_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VW_CORRELATION_GRAPH_BUTTON_LABEL_STR, VW_CORRELATION_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(VW_CORRELATION_GRAPH_BUTTON_DESC_STR, VW_CORRELATION_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(VW_CORRELATION_GRAPH_TITLE_STR, VW_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VW_CORRELATION_GRAPH_LEGEND_TEXT_STR, VW_CORRELATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_VW_CORRELATION_GRAPH_TITLE_STR, VERTICAL_VW_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_VW_CORRELATION_GRAPH_X_AXIS_TITLE_STR, VERTICAL_VW_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_VW_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VW_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_VW_CORRELATION_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VW_CORRELATION_GRAPH_TITLE_STR, DEPTH_AVERAGED_VW_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VW_CORRELATION_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VW_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VW_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VW_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(REYNOLDS_STRESS_GRAPHS_STR, REYNOLDS_STRESS_GRAPHS);     
		sTheStringsLookup.put(TI_AND_TKE_AND_VORTICITY_GRAPHS_STR, TI_AND_TKE_AND_VORTICITY_GRAPHS);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRAPHS_STR, THIRD_ORDER_CORRELATION_GRAPHS);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_LABEL_STR, THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC_STR, THIRD_ORDER_CORRELATIONS_300_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_LABEL_STR, THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC_STR, THIRD_ORDER_CORRELATIONS_210_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_LABEL_STR, THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC_STR, THIRD_ORDER_CORRELATIONS_120_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_LABEL_STR, THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC_STR, THIRD_ORDER_CORRELATIONS_030_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRID_300_TITLE_STR, THIRD_ORDER_CORRELATION_GRID_300_TITLE);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRID_210_TITLE_STR, THIRD_ORDER_CORRELATION_GRID_210_TITLE);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRID_120_TITLE_STR, THIRD_ORDER_CORRELATION_GRID_120_TITLE);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRID_030_TITLE_STR, THIRD_ORDER_CORRELATION_GRID_030_TITLE);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRID_X_AXIS_TITLE_STR, THIRD_ORDER_CORRELATION_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRID_Y_AXIS_TITLE_STR, THIRD_ORDER_CORRELATION_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT_STR, THIRD_ORDER_CORRELATION_GRID_LEGEND_TEXT);     
		sTheStringsLookup.put(EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL_STR, EXPORT_GRAPH_AS_TABLE_BUTTON_LABEL);     
		sTheStringsLookup.put(EXPORT_GRAPH_BUTTON_DESC_STR, EXPORT_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL_STR, EXPORT_GRAPH_FOR_MATLAB_BUTTON_LABEL);     
		sTheStringsLookup.put(EXPORT_GRAPH_FOR_MATLAB_BUTTON_DESC_STR, EXPORT_GRAPH_FOR_MATLAB_BUTTON_DESC);     
		sTheStringsLookup.put(SPECTRAL_DISTRIBUTION_LABEL_STR, SPECTRAL_DISTRIBUTION_LABEL);     
		sTheStringsLookup.put(QH_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, QH_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR, QH_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR, QH_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, QH_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR, QH_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT_STR, QH_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT_STR, QH_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR, QH_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR, QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR, QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT_STR, QH_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR, QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(TKE_FLUX_GRAPH_BUTTON_LABEL_STR, TKE_FLUX_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(TKE_FLUX_GRAPH_BUTTON_DESC_STR, TKE_FLUX_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(TKE_FLUX_GRAPH_FRAME_TITLE_STR, TKE_FLUX_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(X_DIRECTION_TKE_FLUX_GRAPH_TITLE_STR, X_DIRECTION_TKE_FLUX_GRAPH_TITLE);     
		sTheStringsLookup.put(Y_DIRECTION_TKE_FLUX_GRAPH_TITLE_STR, Y_DIRECTION_TKE_FLUX_GRAPH_TITLE);     
		sTheStringsLookup.put(Z_DIRECTION_TKE_FLUX_GRAPH_TITLE_STR, Z_DIRECTION_TKE_FLUX_GRAPH_TITLE);     
		sTheStringsLookup.put(TKE_FLUX_GRAPH_LEGEND_TEXT_STR, TKE_FLUX_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL_STR, SHOW_SPECTRAL_DISTRIBUTION_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_SPECTRAL_DISTRIBUTION_BUTTON_DESC_STR, SHOW_SPECTRAL_DISTRIBUTION_BUTTON_DESC);     
		sTheStringsLookup.put(SPECTRAL_DISTRIBUTION_GRAPH_FRAME_TITLE_STR, SPECTRAL_DISTRIBUTION_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(SPECTRAL_DISTRIBUTION_GRAPH_TITLE_STR, SPECTRAL_DISTRIBUTION_GRAPH_TITLE);     
		sTheStringsLookup.put(SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL_STR, SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(SPECTRAL_DISTRIBUTION_GRAPH_Y_AXIS_LABEL_STR, SPECTRAL_DISTRIBUTION_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(BACKWARD_COMPATIBILITY_MISSING_DATA_TITLE_STR, BACKWARD_COMPATIBILITY_MISSING_DATA_TITLE);     
		sTheStringsLookup.put(BACKWARD_COMPATIBILITY_MISSING_DATA_MSG_STR, BACKWARD_COMPATIBILITY_MISSING_DATA_MSG);     
		sTheStringsLookup.put(REMOVE_DATA_POINTS_ERROR_TITLE_STR, REMOVE_DATA_POINTS_ERROR_TITLE);     
		sTheStringsLookup.put(REMOVE_DATA_POINTS_ERROR_MSG_STR, REMOVE_DATA_POINTS_ERROR_MSG);     
		sTheStringsLookup.put(REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_TITLE_STR, REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_TITLE);     
		sTheStringsLookup.put(REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_MSG_STR, REMOVE_DATA_POINT_NO_SUCH_DATA_POINT_ERROR_MSG);     
		sTheStringsLookup.put(REMOVE_DATA_POINTS_BUTTON_LABEL_STR, REMOVE_DATA_POINTS_BUTTON_LABEL);     
		sTheStringsLookup.put(REMOVE_DATA_POINTS_BUTTON_DESC_STR, REMOVE_DATA_POINTS_BUTTON_DESC);     
		sTheStringsLookup.put(CALCULATE_EOF_R_MATRIX_BUTTON_LABEL_STR, CALCULATE_EOF_R_MATRIX_BUTTON_LABEL);     
		sTheStringsLookup.put(CALCULATE_EOF_R_MATRIX_BUTTON_DESC_STR, CALCULATE_EOF_R_MATRIX_BUTTON_DESC);     
		sTheStringsLookup.put(CALCULATE_EOF_R_MATRIX_DIALOG_TITLE_STR, CALCULATE_EOF_R_MATRIX_DIALOG_TITLE);     
		sTheStringsLookup.put(EOF_R_MATRIX_DIALOG_Y1_LABEL_STR, EOF_R_MATRIX_DIALOG_Y1_LABEL);     
		sTheStringsLookup.put(EOF_R_MATRIX_DIALOG_Y2_LABEL_STR, EOF_R_MATRIX_DIALOG_Y2_LABEL);     
		sTheStringsLookup.put(EOF_R_MATRIX_DIALOG_Z1_LABEL_STR, EOF_R_MATRIX_DIALOG_Z1_LABEL);     
		sTheStringsLookup.put(EOF_R_MATRIX_DIALOG_Z2_LABEL_STR, EOF_R_MATRIX_DIALOG_Z2_LABEL);     
		sTheStringsLookup.put(EOF_R_MATRIX_DIALOG_TITLE_STR, EOF_R_MATRIX_DIALOG_TITLE);     
		sTheStringsLookup.put(EOF_R_MATRIX_INCOMPLETE_GRID_TITLE_STR, EOF_R_MATRIX_INCOMPLETE_GRID_TITLE);     
		sTheStringsLookup.put(EOF_R_MATRIX_INCOMPLETE_GRID_MSG_STR, EOF_R_MATRIX_INCOMPLETE_GRID_MSG);     
		sTheStringsLookup.put(CALCULATE_DATA_POINT_SUMMARY_DATA_STR, CALCULATE_DATA_POINT_SUMMARY_DATA);     
		sTheStringsLookup.put(CALCULATE_DATA_POINT_SUMMARY_DATA_DESC_STR, CALCULATE_DATA_POINT_SUMMARY_DATA_DESC);     
		sTheStringsLookup.put(CALCULATE_DPS_REYNOLDS_STRESSES_STR, CALCULATE_DPS_REYNOLDS_STRESSES);     
		sTheStringsLookup.put(CALCULATE_DPS_REYNOLDS_STRESSES_DESC_STR, CALCULATE_DPS_REYNOLDS_STRESSES_DESC);     
		sTheStringsLookup.put(CALCULATE_DPS_QH_DATA_STR, CALCULATE_DPS_QH_DATA);     
		sTheStringsLookup.put(CALCULATE_DPS_QH_DATA_DESC_STR, CALCULATE_DPS_QH_DATA_DESC);     
		sTheStringsLookup.put(CALCULATE_DPS_TKE_DATA_STR, CALCULATE_DPS_TKE_DATA);     
		sTheStringsLookup.put(CALCULATE_DPS_TKE_DATA_DESC_STR, CALCULATE_DPS_TKE_DATA_DESC);     
		sTheStringsLookup.put(CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_STR, CALCULATE_DPS_FIXED_PROBE_CORRELATIONS);     
		sTheStringsLookup.put(CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_DESC_STR, CALCULATE_DPS_FIXED_PROBE_CORRELATIONS_DESC);     
		sTheStringsLookup.put(CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL_STR, CREATE_ROTATION_CORRECTION_BATCH_BUTTON_LABEL);     
		sTheStringsLookup.put(CREATE_ROTATION_CORRECTION_BATCH_BUTTON_DESC_STR, CREATE_ROTATION_CORRECTION_BATCH_BUTTON_DESC);     
		sTheStringsLookup.put(ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_TITLE_STR, ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_TITLE);     
		sTheStringsLookup.put(ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_MSG_STR, ERROR_CALCULATING_BATCH_ROTATION_CORRECTION_MSG);     
		sTheStringsLookup.put(SELECTION_TOOL_BUTTON_LABEL_STR, SELECTION_TOOL_BUTTON_LABEL);     
		sTheStringsLookup.put(SELECTION_TOOL_BUTTON_DESC_STR, SELECTION_TOOL_BUTTON_DESC);     
		sTheStringsLookup.put(Y_COORD_MIN_LABEL_STR, Y_COORD_MIN_LABEL);     
		sTheStringsLookup.put(Y_COORD_MAX_LABEL_STR, Y_COORD_MAX_LABEL);     
		sTheStringsLookup.put(Z_COORD_MIN_LABEL_STR, Z_COORD_MIN_LABEL);     
		sTheStringsLookup.put(Z_COORD_MAX_LABEL_STR, Z_COORD_MAX_LABEL);     
		sTheStringsLookup.put(SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL_STR, SELECTION_TOOL_NEW_SELECTION_BUTTON_LABEL);     
		sTheStringsLookup.put(SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL_STR, SELECTION_TOOL_EXTEND_SELECTION_BUTTON_LABEL);     
		sTheStringsLookup.put(BATCH_THETA_ROTATION_CORRECTION_COLUMN_TITLE_STR, BATCH_THETA_ROTATION_CORRECTION_COLUMN_TITLE);     
		sTheStringsLookup.put(BATCH_ALPHA_ROTATION_CORRECTION_COLUMN_TITLE_STR, BATCH_ALPHA_ROTATION_CORRECTION_COLUMN_TITLE);     
		sTheStringsLookup.put(BATCH_PHI_ROTATION_CORRECTION_COLUMN_TITLE_STR, BATCH_PHI_ROTATION_CORRECTION_COLUMN_TITLE);     
		sTheStringsLookup.put(BATCH_ROTATION_CORRECTION_BATCH_NUMBER_COLUMN_TITLE_STR, BATCH_ROTATION_CORRECTION_BATCH_NUMBER_COLUMN_TITLE);     
		sTheStringsLookup.put(CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL_STR, CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_LABEL);     
		sTheStringsLookup.put(CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_DESC_STR, CREATE_ROTATION_CORRECTION_BATCHES_FROM_FILE_BUTTON_DESC);     
		sTheStringsLookup.put(ROTATION_CORRECTION_BATCHES_FILE_MISSING_TITLE_STR, ROTATION_CORRECTION_BATCHES_FILE_MISSING_TITLE);     
		sTheStringsLookup.put(ROTATION_CORRECTION_BATCHES_FILE_MISSING_MSG_STR, ROTATION_CORRECTION_BATCHES_FILE_MISSING_MSG);     
		sTheStringsLookup.put(ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_TITLE_STR, ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_TITLE);     
		sTheStringsLookup.put(ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_MSG_STR, ROTATION_CORRECTION_BATCHES_ERROR_PARSING_FILE_MSG);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_LABEL_STR, VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_DESC_STR, VERTICAL_VELOCITY_U_ST_DEV_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_LABEL_STR, VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_DESC_STR, VERTICAL_VELOCITY_V_ST_DEV_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_LABEL_STR, VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_DESC_STR, VERTICAL_VELOCITY_W_ST_DEV_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_U_ST_DEV_GRID_TITLE_STR, VERTICAL_VELOCITY_U_ST_DEV_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_U_ST_DEV_GRID_X_AXIS_TITLE_STR, VERTICAL_VELOCITY_U_ST_DEV_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_U_ST_DEV_GRID_Y_AXIS_TITLE_STR, VERTICAL_VELOCITY_U_ST_DEV_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_U_ST_DEV_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_VELOCITY_U_ST_DEV_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_V_ST_DEV_GRID_TITLE_STR, VERTICAL_VELOCITY_V_ST_DEV_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_V_ST_DEV_GRID_X_AXIS_TITLE_STR, VERTICAL_VELOCITY_V_ST_DEV_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_V_ST_DEV_GRID_Y_AXIS_TITLE_STR, VERTICAL_VELOCITY_V_ST_DEV_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_V_ST_DEV_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_VELOCITY_V_ST_DEV_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_W_ST_DEV_GRID_TITLE_STR, VERTICAL_VELOCITY_W_ST_DEV_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_W_ST_DEV_GRID_X_AXIS_TITLE_STR, VERTICAL_VELOCITY_W_ST_DEV_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_W_ST_DEV_GRID_Y_AXIS_TITLE_STR, VERTICAL_VELOCITY_W_ST_DEV_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VELOCITY_W_ST_DEV_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_VELOCITY_W_ST_DEV_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(SHOW_PDF_BUTTON_LABEL_STR, SHOW_PDF_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_PDF_BUTTON_DESC_STR, SHOW_PDF_BUTTON_DESC);     
		sTheStringsLookup.put(U_PDF_GRAPH_TITLE_STR, U_PDF_GRAPH_TITLE);     
		sTheStringsLookup.put(PDF_GRAPH_FRAME_TITLE_STR, PDF_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(U_PDF_GRAPH_X_AXIS_LABEL_STR, U_PDF_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(U_PDF_GRAPH_Y_AXIS_LABEL_STR, U_PDF_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(V_PDF_GRAPH_TITLE_STR, V_PDF_GRAPH_TITLE);     
		sTheStringsLookup.put(V_PDF_GRAPH_X_AXIS_LABEL_STR, V_PDF_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(V_PDF_GRAPH_Y_AXIS_LABEL_STR, V_PDF_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(W_PDF_GRAPH_TITLE_STR, W_PDF_GRAPH_TITLE);     
		sTheStringsLookup.put(W_PDF_GRAPH_X_AXIS_LABEL_STR, W_PDF_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(W_PDF_GRAPH_Y_AXIS_LABEL_STR, W_PDF_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(GAUSSIAN_STR, GAUSSIAN);     
		sTheStringsLookup.put(QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR, QH_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, QH_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR, QH_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR, QH_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR, QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, QH_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR, QH_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR, QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR, QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE);     
		sTheStringsLookup.put(DIMENSIONLESS_AXES_LABEL_STR, DIMENSIONLESS_AXES_LABEL);     
		sTheStringsLookup.put(DIMENSIONLESS_X_AXIS_LABEL_STR, DIMENSIONLESS_X_AXIS_LABEL);     
		sTheStringsLookup.put(DIMENSIONLESS_Y_AXIS_LABEL_STR, DIMENSIONLESS_Y_AXIS_LABEL);     
		sTheStringsLookup.put(DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_LABEL_STR, DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC_STR, DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_FRAME_TITLE_STR, DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_TITLE_STR, DIFFERENCE_OF_SQUARED_LATERAL_FLUCTUTATION_RMSES_GRID_TITLE);     
		sTheStringsLookup.put(CALCULATE_DPS_ALL_LABEL_STR, CALCULATE_DPS_ALL_LABEL);     
		sTheStringsLookup.put(CALCULATE_DPS_ALL_DESC_STR, CALCULATE_DPS_ALL_DESC);     
		sTheStringsLookup.put(COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_LABEL_STR, COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC_STR, COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE_STR, COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT_STR, HORIZONTAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT_STR, VERTICAL_REYNOLDS_STRESS_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_LEGEND_TEXT_STR, COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(QH_GRAPHS_STR, QH_GRAPHS);     
		sTheStringsLookup.put(VELOCITY_GRAPHS_STR, VELOCITY_GRAPHS);     
		sTheStringsLookup.put(STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_LABEL_STR, STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC_STR, STANDARD_DEVIATION_CONTOUR_PLOT_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE_STR, STANDARD_DEVIATION_CONTOUR_PLOT_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(U_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE_STR, U_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE);     
		sTheStringsLookup.put(U_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT_STR, U_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT);     
		sTheStringsLookup.put(V_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE_STR, V_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE);     
		sTheStringsLookup.put(V_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT_STR, V_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT);     
		sTheStringsLookup.put(W_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE_STR, W_STANDARD_DEVIATION_CONTOUR_PLOT_GRID_TITLE);     
		sTheStringsLookup.put(W_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT_STR, W_STANDARD_DEVIATION_CONTOUR_PLOT_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_SECTION_GRAPH_BUTTON_LABEL_STR, VERTICAL_SECTION_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(VERTICAL_SECTION_GRAPH_BUTTON_DESC_STR, VERTICAL_SECTION_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL_STR, HORIZONTAL_SECTION_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_SECTION_GRAPH_BUTTON_DESC_STR, HORIZONTAL_SECTION_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_GRAPH_BUTTON_LABEL_STR, STREAMWISE_VORTICITY_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC_STR, STREAMWISE_VORTICITY_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_GRID_FRAME_TITLE_STR, STREAMWISE_VORTICITY_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_GRID_TITLE_STR, STREAMWISE_VORTICITY_GRID_TITLE);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_GRID_LEGEND_TEXT_STR, STREAMWISE_VORTICITY_GRID_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(RESULTANT_SHEAR_STRESS_GRID_TITLE_STR, RESULTANT_SHEAR_STRESS_GRID_TITLE);     
		sTheStringsLookup.put(RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL_STR, RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC_STR, RESULTANT_REYNOLDS_STRESS_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(DEPTH_AVERAGED_GRAPH_BUTTON_LABEL_STR, DEPTH_AVERAGED_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_GRAPH_BUTTON_DESC_STR, DEPTH_AVERAGED_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(EXTRAPOLATED_DEPTH_AVERAGED_GRAPH_LABEL_STR, EXTRAPOLATED_DEPTH_AVERAGED_GRAPH_LABEL);     
		sTheStringsLookup.put(POPULATED_CELL_DEPTH_AVERAGED_GRAPH_LABEL_STR, POPULATED_CELL_DEPTH_AVERAGED_GRAPH_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_TITLE_STR, DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_STREAMWISE_VORTICITY_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE_STR, DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_COMBINED_REYNOLDS_STRESS_MAGNITUDE_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(TURBULENCE_INTENSITY_GRID_TITLE_STR, TURBULENCE_INTENSITY_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY__GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY__GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_TURBULENCE_INTENSITY_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_TITLE_STR, DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_TURBULENCE_INTENSITY_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_TITLE_STR, DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_HORIZONTAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_TITLE_STR, DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VERTICAL_REYNOLDS_STRESS_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(RELATIVE_TO_MEAN_CHECKBOX_LABEL_STR, RELATIVE_TO_MEAN_CHECKBOX_LABEL);     
		sTheStringsLookup.put(DIMENSIONLESS_CHECKBOX_LABEL_STR, DIMENSIONLESS_CHECKBOX_LABEL);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_1_LABEL_STR, VORTICITY_EQUATION_TERM_1_LABEL);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_2_LABEL_STR, VORTICITY_EQUATION_TERM_2_LABEL);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_3_LABEL_STR, VORTICITY_EQUATION_TERM_3_LABEL);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_4_LABEL_STR, VORTICITY_EQUATION_TERM_4_LABEL);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_5_LABEL_STR, VORTICITY_EQUATION_TERM_5_LABEL);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERMS_FRAME_TITLE_STR, VORTICITY_EQUATION_TERMS_FRAME_TITLE);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_1_GRAPH_TITLE_STR, VORTICITY_EQUATION_TERM_1_GRAPH_TITLE);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_1_LEGEND_TEXT_STR, VORTICITY_EQUATION_TERM_1_LEGEND_TEXT);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_2_GRAPH_TITLE_STR, VORTICITY_EQUATION_TERM_2_GRAPH_TITLE);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_2_LEGEND_TEXT_STR, VORTICITY_EQUATION_TERM_2_LEGEND_TEXT);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_3_GRAPH_TITLE_STR, VORTICITY_EQUATION_TERM_3_GRAPH_TITLE);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_3_LEGEND_TEXT_STR, VORTICITY_EQUATION_TERM_3_LEGEND_TEXT);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_4_GRAPH_TITLE_STR, VORTICITY_EQUATION_TERM_4_GRAPH_TITLE);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_4_LEGEND_TEXT_STR, VORTICITY_EQUATION_TERM_4_LEGEND_TEXT);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_5_GRAPH_TITLE_STR, VORTICITY_EQUATION_TERM_5_GRAPH_TITLE);     
		sTheStringsLookup.put(VORTICITY_EQUATION_TERM_5_LEGEND_TEXT_STR, VORTICITY_EQUATION_TERM_5_LEGEND_TEXT);     
		sTheStringsLookup.put(VORTICITY_EQUATION_GRAPHS_BUTTON_LABEL_STR, VORTICITY_EQUATION_GRAPHS_BUTTON_LABEL);     
		sTheStringsLookup.put(VORTICITY_EQUATION_GRAPHS_BUTTON_DESC_STR, VORTICITY_EQUATION_GRAPHS_BUTTON_DESC);     
		sTheStringsLookup.put(LAST_MODIFIED_LABEL_STR, LAST_MODIFIED_LABEL);     
		sTheStringsLookup.put(COMMENTS_LABEL_STR, COMMENTS_LABEL);     
		sTheStringsLookup.put(MATLAB_XY_OUTPUT_TEXT_STR, MATLAB_XY_OUTPUT_TEXT);     
		sTheStringsLookup.put(MATLAB_XYZ_OUTPUT_TEXT_STR, MATLAB_XYZ_OUTPUT_TEXT);     
		sTheStringsLookup.put(WATER_TEMPERATURE_LABEL_STR, WATER_TEMPERATURE_LABEL);     
		sTheStringsLookup.put(SELECT_EXPORT_FILE_PATH_DIALOG_TITLE_STR, SELECT_EXPORT_FILE_PATH_DIALOG_TITLE);     
		sTheStringsLookup.put(USE_DATA_FILE_PATH_FOR_ALL_LABEL_STR, USE_DATA_FILE_PATH_FOR_ALL_LABEL);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_U_TKE_FLUX_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_U_TKE_FLUX_GRID_TITLE_STR, DEPTH_AVERAGED_U_TKE_FLUX_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_U_TKE_FLUX_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_U_TKE_FLUX_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_U_TKE_FLUX_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_U_TKE_FLUX_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_V_TKE_FLUX_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_V_TKE_FLUX_GRID_TITLE_STR, DEPTH_AVERAGED_V_TKE_FLUX_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_V_TKE_FLUX_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_V_TKE_FLUX_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_V_TKE_FLUX_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_V_TKE_FLUX_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_W_TKE_FLUX_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_W_TKE_FLUX_GRID_TITLE_STR, DEPTH_AVERAGED_W_TKE_FLUX_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_W_TKE_FLUX_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_W_TKE_FLUX_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_W_TKE_FLUX_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_W_TKE_FLUX_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VIEW_PERCENTAGE_GOOD_BUTTON_LABEL_STR, VIEW_PERCENTAGE_GOOD_BUTTON_LABEL);     
		sTheStringsLookup.put(VIEW_PERCENTAGE_GOOD_BUTTON_DESC_STR, VIEW_PERCENTAGE_GOOD_BUTTON_DESC);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_LABEL_STR, STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC_STR, STREAMWISE_VORTICITY_KE_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_KE_GRID_FRAME_TITLE_STR, STREAMWISE_VORTICITY_KE_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_KE_GRID_TITLE_STR, STREAMWISE_VORTICITY_KE_GRID_TITLE);     
		sTheStringsLookup.put(STREAMWISE_VORTICITY_KE_GRID_LEGEND_TEXT_STR, STREAMWISE_VORTICITY_KE_GRID_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_STREAMWISE_VORTICITY_KE_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_TITLE_STR, DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_STREAMWISE_VORTICITY_KE_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(PERCENTAGE_GOOD_GRID_FRAME_TITLE_STR, PERCENTAGE_GOOD_GRID_FRAME_TITLE);     
		sTheStringsLookup.put(PERCENTAGE_GOOD_GRID_TITLE_STR, PERCENTAGE_GOOD_GRID_TITLE);     
		sTheStringsLookup.put(PERCENTAGE_GOOD_GRID_LEGEND_TEXT_STR, PERCENTAGE_GOOD_GRID_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_PERCENTAGE_GOOD_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_TITLE_STR, DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE_STR, DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_PERCENTAGE_GOOD_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_LABEL_STR, MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC_STR, MEAN_VELOCITY_KINETIC_ENERGY_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(U_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE_STR, U_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE);     
		sTheStringsLookup.put(U_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT_STR, U_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT);     
		sTheStringsLookup.put(V_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE_STR, V_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE);     
		sTheStringsLookup.put(V_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT_STR, V_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT);     
		sTheStringsLookup.put(W_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE_STR, W_MEAN_VELOCITY_KINETIC_ENERGY_GRID_TITLE);     
		sTheStringsLookup.put(W_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT_STR, W_MEAN_VELOCITY_KINETIC_ENERGY_LEGEND_TEXT);     
		sTheStringsLookup.put(TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_LABEL_STR, TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC_STR, TURBULENT_KINETIC_ENERGY_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(TURBULENT_KINETIC_ENERGY_GRAPH_FRAME_TITLE_STR, TURBULENT_KINETIC_ENERGY_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(TURBULENT_KINETIC_ENERGY_GRAPH_TITLE_STR, TURBULENT_KINETIC_ENERGY_GRAPH_TITLE);     
		sTheStringsLookup.put(TURBULENT_KINETIC_ENERGY_GRAPH_X_AXIS_TITLE_STR, TURBULENT_KINETIC_ENERGY_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(TURBULENT_KINETIC_ENERGY_GRAPH_Y_AXIS_TITLE_STR, TURBULENT_KINETIC_ENERGY_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(TURBULENT_KINETIC_ENERGY_GRAPH_LEGEND_TEXT_STR, TURBULENT_KINETIC_ENERGY_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_TKE_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_TKE_GRAPH_TITLE_STR, DEPTH_AVERAGED_TKE_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_TKE_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_TKE_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_TKE_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_TKE_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR, QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR, QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR, QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT_STR, QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UW_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR, QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR, QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR, QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT_STR, QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UW_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL_STR, QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC_STR, QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR, QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT_STR, QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UV_Q1_TO_Q3_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL_STR, QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC_STR, QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR, QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT_STR, QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_QH_SCALED_UV_Q2_TO_Q4_RATIO_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(INVERT_CHECKBOX_LABEL_STR, INVERT_CHECKBOX_LABEL);     
		sTheStringsLookup.put(SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL_STR, SHOW_CONDITIONAL_TIME_SERIES_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_CONDITIONAL_TIME_SERIES_BUTTON_DESC_STR, SHOW_CONDITIONAL_TIME_SERIES_BUTTON_DESC);     
		sTheStringsLookup.put(CONDITIONAL_TIME_SERIES_GRAPH_FRAME_TITLE_STR, CONDITIONAL_TIME_SERIES_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(CONDITIONAL_TIME_SERIES_GRAPH_TITLE_STR, CONDITIONAL_TIME_SERIES_GRAPH_TITLE);     
		sTheStringsLookup.put(CONDITIONAL_TIME_SERIES_GRAPH_X_AXIS_LABEL_STR, CONDITIONAL_TIME_SERIES_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(CONDITIONAL_TIME_SERIES_GRAPH_Y_AXIS_LABEL_STR, CONDITIONAL_TIME_SERIES_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR, SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR, SHOW_NORMALISED_UW_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC);     
		sTheStringsLookup.put(SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL_STR, SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC_STR, SHOW_NORMALISED_UV_QUADRANT_HOLE_ANALYSIS_BUTTON_DESC);     
		sTheStringsLookup.put(SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL_STR, SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_LABEL);     
		sTheStringsLookup.put(SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_DESC_STR, SAVE_FTRC_DETAILS_TO_ASCII_FILE_BUTTON_DESC);     
		sTheStringsLookup.put(SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL_STR, SHOW_OFFSET_CORRELATIONS_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_OFFSET_CORRELATIONS_BUTTON_DESC_STR, SHOW_OFFSET_CORRELATIONS_BUTTON_DESC);     
		sTheStringsLookup.put(OFFSET_CORRELATIONS_GRAPH_FRAME_TITLE_STR, OFFSET_CORRELATIONS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(U_OFFSET_CORRELATIONS_GRAPH_TITLE_STR, U_OFFSET_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(U_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL_STR, U_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(U_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL_STR, U_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(V_OFFSET_CORRELATIONS_GRAPH_TITLE_STR, V_OFFSET_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(V_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL_STR, V_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(V_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL_STR, V_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(W_OFFSET_CORRELATIONS_GRAPH_TITLE_STR, W_OFFSET_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(W_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL_STR, W_OFFSET_CORRELATIONS_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(W_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL_STR, W_OFFSET_CORRELATIONS_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL_STR, SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_DESC_STR, SHOW_CORRELATIONS_DISTRIBUTION_BUTTON_DESC);     
		sTheStringsLookup.put(CORRELATIONS_DISTRIBUTION_GRAPH_FRAME_TITLE_STR, CORRELATIONS_DISTRIBUTION_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(CORRELATIONS_DISTRIBUTION_GRAPH_TITLE_STR, CORRELATIONS_DISTRIBUTION_GRAPH_TITLE);     
		sTheStringsLookup.put(CORRELATIONS_DISTRIBUTION_GRAPH_X_AXIS_LABEL_STR, CORRELATIONS_DISTRIBUTION_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(CORRELATIONS_DISTRIBUTION_GRAPH_Y_AXIS_LABEL_STR, CORRELATIONS_DISTRIBUTION_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(HIDE_TITLE_LABEL_STR, HIDE_TITLE_LABEL);     
		sTheStringsLookup.put(MIRROR_ABOUT_VERTICAL_LABEL_STR, MIRROR_ABOUT_VERTICAL_LABEL);     
		sTheStringsLookup.put(X_AXIS_LOGARITHMIC_LABEL_STR, X_AXIS_LOGARITHMIC_LABEL);     
		sTheStringsLookup.put(Y_AXIS_LOGARITHMIC_LABEL_STR, Y_AXIS_LOGARITHMIC_LABEL);     
		sTheStringsLookup.put(QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR, QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR, QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR, QH_UW_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE);     
		sTheStringsLookup.put(QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL_STR, QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC_STR, QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE_STR, QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE_STR, QH_UV_SCALED_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_GRAPH_TITLE);     
		sTheStringsLookup.put(FILTERING_CONFIG_TAB_LABEL_STR, FILTERING_CONFIG_TAB_LABEL);     
		sTheStringsLookup.put(CS_TYPE_STANDARD_DEVIATION_STR, CS_TYPE_STANDARD_DEVIATION);     
		sTheStringsLookup.put(CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION_STR, CS_TYPE_MEDIAN_ABSOLUTE_DEVIATION);     
		sTheStringsLookup.put(CS_TYPE_MEAN_ABSOLUTE_DEVIATION_STR, CS_TYPE_MEAN_ABSOLUTE_DEVIATION);     
		sTheStringsLookup.put(PST_REPLACEMENT_METHOD_NONE_STR, PST_REPLACEMENT_METHOD_NONE);     
		sTheStringsLookup.put(PST_REPLACEMENT_METHOD_LINEAR_INTERPOLATION_STR, PST_REPLACEMENT_METHOD_LINEAR_INTERPOLATION);     
		sTheStringsLookup.put(PST_REPLACEMENT_METHOD_LAST_GOOD_VALUE_STR, PST_REPLACEMENT_METHOD_LAST_GOOD_VALUE);     
		sTheStringsLookup.put(PST_REPLACEMENT_METHOD_12PP_INTERPOLATION_STR, PST_REPLACEMENT_METHOD_12PP_INTERPOLATION);     
		sTheStringsLookup.put(MODIFIED_PST_AUTO_SAFE_LEVEL_C1_LABEL_STR, MODIFIED_PST_AUTO_SAFE_LEVEL_C1_LABEL);     
		sTheStringsLookup.put(MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_LABEL_STR, MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_LABEL);     
		sTheStringsLookup.put(PST_REPLACEMENT_METHOD_LABEL_STR, PST_REPLACEMENT_METHOD_LABEL);     
		sTheStringsLookup.put(CS_TYPE_LABEL_STR, CS_TYPE_LABEL);     
		sTheStringsLookup.put(SAMPLING_RATE_LABEL_STR, SAMPLING_RATE_LABEL);     
		sTheStringsLookup.put(LIMITING_CORRELATION_LABEL_STR, LIMITING_CORRELATION_LABEL);     
		sTheStringsLookup.put(ABOUT_BUTTON_LABEL_STR, ABOUT_BUTTON_LABEL);     
		sTheStringsLookup.put(ABOUT_BUTTON_DESC_STR, ABOUT_BUTTON_DESC);     
		sTheStringsLookup.put(ABOUT_DIALOG_TITLE_STR, ABOUT_DIALOG_TITLE);     
		sTheStringsLookup.put(VERSION_STR, VERSION);     
		sTheStringsLookup.put(GPL_TEXT_STR, GPL_TEXT);     
		sTheStringsLookup.put(GPL_LINK_STR, GPL_LINK);     
		sTheStringsLookup.put(GPL_WARRANTY_TEXT_STR, GPL_WARRANTY_TEXT);     
		sTheStringsLookup.put(WARRANTY_STR, WARRANTY);     
		sTheStringsLookup.put(I_ACCEPT_STR, I_ACCEPT);     
		sTheStringsLookup.put(ADDITIONAL_SOFTWARE_STR, ADDITIONAL_SOFTWARE);     
		sTheStringsLookup.put(MOMENTUM_GRAPHS_STR, MOMENTUM_GRAPHS);     
		sTheStringsLookup.put(SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL_STR, SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC_STR, SHOW_VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC);     
		sTheStringsLookup.put(VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE_STR, VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE_STR, VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL_STR, VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL_STR, VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT_STR, VERTICAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_VTSM_GRAPH_TITLE_STR, VERTICAL_VTSM_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_VTSM_GRAPH_X_AXIS_TITLE_STR, VERTICAL_VTSM_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VTSM_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_VTSM_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_VTSM_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_VTSM_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VTSM_GRAPH_TITLE_STR, DEPTH_AVERAGED_VTSM_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VTSM_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VTSM_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VTSM_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VTSM_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL_STR, SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC_STR, SHOW_HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_BUTTON_DESC);     
		sTheStringsLookup.put(HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE_STR, HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE_STR, HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL_STR, HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL_STR, HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT_STR, HORIZONTAL_TRANSFER_OF_STREAMWISE_MOMENTUM_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_HTSM_GRAPH_TITLE_STR, VERTICAL_HTSM_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_HTSM_GRAPH_X_AXIS_TITLE_STR, VERTICAL_HTSM_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_HTSM_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_HTSM_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_HTSM_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_HTSM_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_HTSM_GRAPH_TITLE_STR, DEPTH_AVERAGED_HTSM_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_HTSM_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_HTSM_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_HTSM_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_HTSM_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(STANDARD_CELL_WIDTH_LABEL_STR, STANDARD_CELL_WIDTH_LABEL);     
		sTheStringsLookup.put(STANDARD_CELL_HEIGHT_LABEL_STR, STANDARD_CELL_HEIGHT_LABEL);     
		sTheStringsLookup.put(MULTI_RUN_CONFIG_TAB_LABEL_STR, MULTI_RUN_CONFIG_TAB_LABEL);     
		sTheStringsLookup.put(MULTI_RUN_SYNCH_LABEL_STR, MULTI_RUN_SYNCH_LABEL);     
		sTheStringsLookup.put(SYNCH_NONE_LABEL_STR, SYNCH_NONE_LABEL);     
		sTheStringsLookup.put(SYNCH_MAX_LABEL_STR, SYNCH_MAX_LABEL);     
		sTheStringsLookup.put(SYNCH_LIMITING_VALUE_LABEL_STR, SYNCH_LIMITING_VALUE_LABEL);     
		sTheStringsLookup.put(SYNCH_LIMITING_VALUE_SETTER_LABEL_STR, SYNCH_LIMITING_VALUE_SETTER_LABEL);     
		sTheStringsLookup.put(SYNCH_LIMITING_VALUE_DIRECTION_LABEL_STR, SYNCH_LIMITING_VALUE_DIRECTION_LABEL);     
		sTheStringsLookup.put(SYNCH_LIMITING_VALUE_DIRECTION_OPTIONS_STR, SYNCH_LIMITING_VALUE_DIRECTION_OPTIONS);     
		sTheStringsLookup.put(FULL_CORRELATION_STR, FULL_CORRELATION);     
		sTheStringsLookup.put(ENSEMBLE_CORRELATION_STR, ENSEMBLE_CORRELATION);     
		sTheStringsLookup.put(RANDOM_ENSEMBLE_CORRELATION_STR, RANDOM_ENSEMBLE_CORRELATION);     
		sTheStringsLookup.put(MEAN_TRUE_ENSEMBLE_CORRELATION_SET_1_STR, MEAN_TRUE_ENSEMBLE_CORRELATION_SET_1);     
		sTheStringsLookup.put(MEAN_TRUE_ENSEMBLE_CORRELATION_SET_2_STR, MEAN_TRUE_ENSEMBLE_CORRELATION_SET_2);     
		sTheStringsLookup.put(MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_1_STR, MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_1);     
		sTheStringsLookup.put(MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_2_STR, MEAN_RANDOM_ENSEMBLE_CORRELATION_SET_2);     
		sTheStringsLookup.put(NOT_YET_CALCULATED_STR, NOT_YET_CALCULATED);     
		sTheStringsLookup.put(PROCESSING_FILE_MESSAGE_STR, PROCESSING_FILE_MESSAGE);     
		sTheStringsLookup.put(TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_LABEL_STR, TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC_STR, TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_FRAME_TITLE_STR, TURBULENCE_GENERATION_AND_DISSIPATION_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(VERTICAL_TURBULENCE_GENERATION_TAB_LABEL_STR, VERTICAL_TURBULENCE_GENERATION_TAB_LABEL);     
		sTheStringsLookup.put(VERTICAL_TURBULENCE_GENERATION_GRAPH_TITLE_STR, VERTICAL_TURBULENCE_GENERATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT_STR, VERTICAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(HORIZONTAL_TURBULENCE_GENERATION_TAB_LABEL_STR, HORIZONTAL_TURBULENCE_GENERATION_TAB_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_TURBULENCE_GENERATION_GRAPH_TITLE_STR, HORIZONTAL_TURBULENCE_GENERATION_GRAPH_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT_STR, HORIZONTAL_TURBULENCE_GENERATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_TURBULENCE_DISSIPATION_TAB_LABEL_STR, VERTICAL_TURBULENCE_DISSIPATION_TAB_LABEL);     
		sTheStringsLookup.put(VERTICAL_TURBULENCE_DISSIPATION_GRAPH_TITLE_STR, VERTICAL_TURBULENCE_DISSIPATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT_STR, VERTICAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(HORIZONTAL_TURBULENCE_DISSIPATION_TAB_LABEL_STR, HORIZONTAL_TURBULENCE_DISSIPATION_TAB_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_TITLE_STR, HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT_STR, HORIZONTAL_TURBULENCE_DISSIPATION_GRAPH_LEGEND_TEXT);     

		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL_STR, ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_DESC_STR, ANISOTROPIC_STRESS_TENSOR_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE_STR, ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_TITLE);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_LEGEND_TEXT_STR, ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRAPH_LEGEND_TEXT);     

		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL);     

		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_COMPONENT_GRID_LEGEND_KEY_LABEL);     

		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_LABEL_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_TITLE_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_TITLE);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_LEGEND_TEXT_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_II_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_LABEL_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_TITLE_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_TITLE);     
		sTheStringsLookup.put(ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_LEGEND_TEXT_STR, ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_ANISOTROPIC_STRESS_TENSOR_INVARIANT_III_GRID_LEGEND_KEY_LABEL);     

		sTheStringsLookup.put(LARGE_FILES_CONFIG_TAB_LABEL_STR, LARGE_FILES_AND_MISC_CONFIG_TAB_LABEL);     
		sTheStringsLookup.put(SPLIT_LARGE_FILES_LABEL_STR, SPLIT_LARGE_FILES_LABEL);     
		sTheStringsLookup.put(LARGE_FILES_NUMBER_OF_MEASUREMENTS_PER_SPLIT_STR, LARGE_FILES_NUMBER_OF_MEASUREMENTS_PER_SPLIT);     

		sTheStringsLookup.put(PRESSURES_LABEL_STR, PRESSURES_LABEL);     

		sTheStringsLookup.put(DESPIKING_FILTER_MOVING_AVERAGE_STR, DESPIKING_FILTER_MOVING_AVERAGE);     
		sTheStringsLookup.put(DESPIKING_FILTER_MOVING_AVERAGE_REFERENCE_STR, DESPIKING_FILTER_MOVING_AVERAGE_REFERENCE);     

		sTheStringsLookup.put(MULTI_RUN_RUN_LABEL_STR, MULTI_RUN_RUN_LABEL);     
		sTheStringsLookup.put(RUN_MEAN_STR, RUN_MEAN);     
		sTheStringsLookup.put(RUN_LABEL_STR, RUN_LABEL);     
		sTheStringsLookup.put(INDEX_TEXT_STR, INDEX_TEXT);     

		sTheStringsLookup.put(ERROR_SAVING_FILE_DETAILS_TITLE_STR, ERROR_SAVING_FILE_DETAILS_TITLE);     
		sTheStringsLookup.put(SAVE_DETAILS_DIRECTORY_SUFFIX_STR, SAVE_DETAILS_DIRECTORY_SUFFIX);     

		sTheStringsLookup.put(PROBE_SETUP_CONFIG_TAB_LABEL_STR, PROBE_SETUP_CONFIG_TAB_LABEL);     

		sTheStringsLookup.put(SYNCH_INDEX_STR, SYNCH_INDEX);     
		sTheStringsLookup.put(PROBE_NONE_STR, PROBE_NONE);     

		sTheStringsLookup.put(TIME_LABEL_STR, TIME_LABEL);     

		sTheStringsLookup.put(TRIM_WARNING_STR, TRIM_WARNING);     
		sTheStringsLookup.put(TRIM_START_POINT_LABEL_STR, TRIM_START_POINT_LABEL);     
		sTheStringsLookup.put(TRIM_END_POINT_LABEL_STR, TRIM_END_POINT_LABEL);     
		sTheStringsLookup.put(TRIM_LABEL_STR, TRIM_LABEL);     

		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_BUTTON_LABEL_STR, MAXIMUM_VELOCITIES_GRAPH_BUTTON_LABEL);     
		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC_STR, MAXIMUM_VELOCITIES_GRAPH_BUTTON_DESC);     
		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_TITLE_STR, MAXIMUM_VELOCITIES_GRAPH_TITLE);     
		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_FRAME_TITLE_STR, MAXIMUM_VELOCITIES_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_LEGEND_TEXT_STR, MAXIMUM_VELOCITIES_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE_STR, MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE_STR, MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(MAXIMUM_VELOCITIES_GRAPH_LEGEND_KEY_LABEL_STR, MAXIMUM_VELOCITIES_GRAPH_LEGEND_KEY_LABEL);     

		sTheStringsLookup.put(DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_TITLE_STR, DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_MAXIMUM_VELOCITIES_GRAPH_Y_AXIS_TITLE);     

		sTheStringsLookup.put(DATA_POINT_OVERVIEW_TABLE_TAB_STR, DATA_POINT_OVERVIEW_TABLE_TAB);     
		sTheStringsLookup.put(DATA_POINT_OVERVIEW_TABLE_PROBE_TYPE_COL_HEADER_STR, DATA_POINT_OVERVIEW_TABLE_PROBE_TYPE_COL_HEADER);     
		sTheStringsLookup.put(DATA_POINT_OVERVIEW_TABLE_PROBE_ID_COL_HEADER_STR, DATA_POINT_OVERVIEW_TABLE_PROBE_ID_COL_HEADER);     
		sTheStringsLookup.put(DATA_POINT_OVERVIEW_TABLE_SAMPLING_RATE_COL_HEADER_STR, DATA_POINT_OVERVIEW_TABLE_SAMPLING_RATE_COL_HEADER);     

		sTheStringsLookup.put(VARIOUS_STR, VARIOUS);     

		sTheStringsLookup.put(SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL_STR, SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL);     
		sTheStringsLookup.put(SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_DESC_STR, SAVE_FTRC_DETAILS_TO_MATLAB_FILE_BUTTON_DESC);     

		sTheStringsLookup.put(SAVE_DETAILS_TO_FILE_MENU_LABEL_STR, SAVE_DETAILS_TO_FILE_MENU_LABEL);     

		sTheStringsLookup.put(SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL_STR, SELECT_CHANNEL_BED_DEFINITION_FILE_LABEL);     

		sTheStringsLookup.put(BOUNDARY_DEFINITION_FILE_FILTER_TEXT_STR, BOUNDARY_DEFINITION_FILE_FILTER_TEXT);     

		sTheStringsLookup.put(DATA_SET_LENGTH_UNIT_OPTIONS_STR, DATA_SET_LENGTH_UNIT_OPTIONS);     
		sTheStringsLookup.put(DATA_SET_LENGTH_UNITS_LABEL_STR, DATA_SET_LENGTH_UNITS_LABEL);     

		sTheStringsLookup.put(LIMITING_SNR_LABEL_STR, LIMITING_SNR_LABEL);     

		sTheStringsLookup.put(SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL_STR, SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_DESC_STR, SHOW_SIGNAL_CORRELATION_AND_SNR_BUTTON_DESC);     
		sTheStringsLookup.put(SIGNAL_CORRELATION_AND_SNR_GRAPH_FRAME_TITLE_STR, SIGNAL_CORRELATION_AND_SNR_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE_STR, SIGNAL_CORRELATION_AND_SNR_GRAPH_TITLE);     
		sTheStringsLookup.put(SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL_STR, SIGNAL_CORRELATION_AND_SNR_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL_STR, SIGNAL_CORRELATION_AND_SNR_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(SIGNAL_CORRELATION_LEGEND_TEXT_STR, SIGNAL_CORRELATION_LEGEND_TEXT);     
		sTheStringsLookup.put(SNR_LEGEND_TEXT_STR, SNR_LEGEND_TEXT);     

		sTheStringsLookup.put(DESPIKING_FILTER_W_DIFF_STR, DESPIKING_FILTER_W_DIFF);     
		sTheStringsLookup.put(DESPIKING_FILTER_W_DIFF_REFERENCE_STR, DESPIKING_FILTER_W_DIFF_REFERENCE);     

		sTheStringsLookup.put(LIMITING_W_DIFF_LABEL_STR, LIMITING_W_DIFF_LABEL);     

		sTheStringsLookup.put(SHOW_W_DIFF_BUTTON_LABEL_STR, SHOW_W_DIFF_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_W_DIFF_BUTTON_DESC_STR, SHOW_W_DIFF_BUTTON_DESC);     
		sTheStringsLookup.put(W_DIFF_GRAPH_FRAME_TITLE_STR, W_DIFF_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(W_DIFF_GRAPH_TITLE_STR, W_DIFF_GRAPH_TITLE);     
		sTheStringsLookup.put(W_DIFF_GRAPH_X_AXIS_LABEL_STR, W_DIFF_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(W_DIFF_GRAPH_Y_AXIS_LABEL_STR, W_DIFF_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(W_DIFF_LEGEND_TEXT_STR, W_DIFF_LEGEND_TEXT);     

		sTheStringsLookup.put(I_DECLINE_STR, I_DECLINE);     

		sTheStringsLookup.put(USE_BINARY_FILE_FORMAT_LABEL_STR, USE_BINARY_FILE_FORMAT_LABEL);     

		sTheStringsLookup.put(PRE_FILTER_TYPE_LABEL_STR, PRE_FILTER_TYPE_LABEL);     

		sTheStringsLookup.put(NUMBER_OF_BARTLETT_WINDOWS_LABEL_STR, NUMBER_OF_BARTLETT_WINDOWS_LABEL);     

		sTheStringsLookup.put(X_CORRELATIONS_LABEL_STR, X_CORRELATIONS_LABEL);     
		sTheStringsLookup.put(X_SNRS_LABEL_STR, X_SNRS_LABEL);     
		sTheStringsLookup.put(Y_CORRELATIONS_LABEL_STR, Y_CORRELATIONS_LABEL);     
		sTheStringsLookup.put(Y_SNRS_LABEL_STR, Y_SNRS_LABEL);     
		sTheStringsLookup.put(Z_CORRELATIONS_LABEL_STR, Z_CORRELATIONS_LABEL);     
		sTheStringsLookup.put(Z_SNRS_LABEL_STR, Z_SNRS_LABEL);     

		sTheStringsLookup.put(MEAN_CORRELATION_LABEL_STR, MEAN_CORRELATION_LABEL);     
		sTheStringsLookup.put(CORRELATION_STDEV_LABEL_STR, CORRELATION_STDEV_LABEL);     
		sTheStringsLookup.put(MEAN_SNR_LABEL_STR, MEAN_SNR_LABEL);     
		sTheStringsLookup.put(SNR_ST_DEV_LABEL_STR, SNR_ST_DEV_LABEL);     

		sTheStringsLookup.put(USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_LABEL_STR, USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_LABEL);     

		sTheStringsLookup.put(SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL_STR, SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_LABEL);     
		sTheStringsLookup.put(SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_DESC_STR, SAVE_ALL_DETAILS_TO_ASCII_FILE_BUTTON_DESC);     

		sTheStringsLookup.put(EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL_STR, EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_LABEL);     
		sTheStringsLookup.put(EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_DESC_STR, EXPORT_GRAPH_AS_MATLAB_SCRIPT_BUTTON_DESC);     

		sTheStringsLookup.put(NEWER_VERSIONS_STR, NEWER_VERSIONS);     

		sTheStringsLookup.put(NEWER_VERSIONS_AVAILABLE_STR, NEWER_VERSIONS_AVAILABLE);     

		sTheStringsLookup.put(MATLAB_CONNECTION_TEST_DIALOG_TITLE_STR, MATLAB_CONNECTION_TEST_DIALOG_TITLE);     
		sTheStringsLookup.put(MATLAB_PROGRESS_DIALOG_TITLE_STR, MATLAB_PROGRESS_DIALOG_TITLE);     

		sTheStringsLookup.put(SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL_STR, SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_LABEL);     
		sTheStringsLookup.put(SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_DESC_STR, SEND_FTRC_DETAILS_TO_MATLAB_BUTTON_DESC);     

		sTheStringsLookup.put(SEND_DETAILS_TO_MATLAB_MENU_LABEL_STR, SEND_DETAILS_TO_MATLAB_MENU_LABEL);     
		sTheStringsLookup.put(SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL_STR, SEND_ALL_DETAILS_TO_MATLAB_BUTTON_LABEL);     
		sTheStringsLookup.put(SEND_ALL_DETAILS_TO_MATLAB_BUTTON_DESC_STR, SEND_ALL_DETAILS_TO_MATLAB_BUTTON_DESC);     

		sTheStringsLookup.put(SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL_STR, SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_LABEL);     
		sTheStringsLookup.put(SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_DESC_STR, SAVE_ALL_DETAILS_TO_MATLAB_FILE_BUTTON_DESC);     

		sTheStringsLookup.put(WAVELET_TYPE_LABEL_STR, WAVELET_TYPE_LABEL);     
		sTheStringsLookup.put(WAVELET_TRANSFORM_TYPE_LABEL_STR, WAVELET_TRANSFORM_TYPE_LABEL);     
		sTheStringsLookup.put(WAVELET_TYPE_HAAR02_ORTOHOGONAL_STR, WAVELET_TYPE_HAAR02_ORTOHOGONAL);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB02_STR, WAVELET_TYPE_DAUB02);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB04_STR, WAVELET_TYPE_DAUB04);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB06_STR, WAVELET_TYPE_DAUB06);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB08_STR, WAVELET_TYPE_DAUB08);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB10_STR, WAVELET_TYPE_DAUB10);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB12_STR, WAVELET_TYPE_DAUB12);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB14_STR, WAVELET_TYPE_DAUB14);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB16_STR, WAVELET_TYPE_DAUB16);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB18_STR, WAVELET_TYPE_DAUB18);     
		sTheStringsLookup.put(WAVELET_TYPE_DAUB20_STR, WAVELET_TYPE_DAUB20);     
		sTheStringsLookup.put(WAVELET_TYPE_LEGE02_STR, WAVELET_TYPE_LEGE02);     
		sTheStringsLookup.put(WAVELET_TYPE_LEGE04_STR, WAVELET_TYPE_LEGE04);     
		sTheStringsLookup.put(WAVELET_TYPE_LEGE06_STR, WAVELET_TYPE_LEGE06);     
		sTheStringsLookup.put(WAVELET_TYPE_COIF06_STR, WAVELET_TYPE_COIF06);     
		sTheStringsLookup.put(WAVELET_TRANSFORM_TYPE_DFT_STR, WAVELET_TRANSFORM_TYPE_DFT);     
		sTheStringsLookup.put(WAVELET_TRANSFORM_TYPE_FWT_STR, WAVELET_TRANSFORM_TYPE_FWT);     
		sTheStringsLookup.put(WAVELET_TRANSFORM_TYPE_CWT_STR, WAVELET_TRANSFORM_TYPE_CWT);     

		sTheStringsLookup.put(SPECTRAL_ANALYSIS_CONFIG_TAB_LABEL_STR, SPECTRAL_ANALYSIS_CONFIG_TAB_LABEL);     

		sTheStringsLookup.put(SHOW_WAVELET_ANALYSIS_BUTTON_LABEL_STR, SHOW_WAVELET_ANALYSIS_BUTTON_LABEL);     
		sTheStringsLookup.put(SHOW_WAVELET_ANALYSIS_BUTTON_DESC_STR, SHOW_WAVELET_ANALYSIS_BUTTON_DESC);     

		sTheStringsLookup.put(WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_TITLE_STR, WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_TITLE);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_GRAPH_FRAME_TITLE_STR, WAVELET_ANALYSIS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_Y_AXIS_LABEL_STR, WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL_STR, WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL);     

		sTheStringsLookup.put(WAVELET_ANALYSIS_LEVELS_GRAPH_TITLE_STR, WAVELET_ANALYSIS_LEVELS_GRAPH_TITLE);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL_STR, WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL_STR, WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_SCALEOGRAM_TAB_LABEL_STR, WAVELET_ANALYSIS_SCALEOGRAM_TAB_LABEL);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_RECONSTRUCTION_TAB_LABEL_STR, WAVELET_ANALYSIS_RECONSTRUCTION_TAB_LABEL);     

		sTheStringsLookup.put(WAVELET_ANALYSIS_LEVELS_SELECTOR_LABEL_STR, WAVELET_ANALYSIS_LEVELS_SELECTOR_LABEL);     

		sTheStringsLookup.put(AT_TRIM_START_BY_SETTER_LABEL_STR, AT_TRIM_START_BY_SETTER_LABEL);     
		sTheStringsLookup.put(AT_PRIOR_LENGTH_SETTER_LABEL_STR, AT_PRIOR_LENGTH_SETTER_LABEL);     
		sTheStringsLookup.put(AT_SAMPLE_LENGTH_SETTER_LABEL_STR, AT_SAMPLE_LENGTH_SETTER_LABEL);     

		sTheStringsLookup.put(AT_PRIOR_LENGTH_CHECKBOX_LABEL_STR, AT_PRIOR_LENGTH_CHECKBOX_LABEL);     

		sTheStringsLookup.put(TABLE_ACTION_PROGRESS_DIALOG_TITLE_STR, TABLE_ACTION_PROGRESS_DIALOG_TITLE);     

		sTheStringsLookup.put(WAVELET_ANALYSIS_CONTOUR_TAB_LABEL_STR, WAVELET_ANALYSIS_CONTOUR_TAB_LABEL);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE_STR, WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE_STR, WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE_STR, WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE);     
		sTheStringsLookup.put(WAVELET_ANALYSIS_CONTOUR_LEGEND_TEXT_STR, WAVELET_ANALYSIS_CONTOUR_LEGEND_TEXT);     

		sTheStringsLookup.put(VELOCITY_CORRELATIONS_GRAPH_FRAME_TITLE_STR, VELOCITY_CORRELATIONS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(U_VELOCITY_CORRELATIONS_GRAPH_TITLE_STR, U_VELOCITY_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(V_VELOCITY_CORRELATIONS_GRAPH_TITLE_STR, V_VELOCITY_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(W_VELOCITY_CORRELATIONS_GRAPH_TITLE_STR, W_VELOCITY_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(VELOCITY_CORRELATION_GRAPH_LEGEND_TEXT_STR, VELOCITY_CORRELATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VELOCITY_CORRELATION_GRAPH_TITLE_STR, VELOCITY_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_VELOCITY_CORRELATION_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_TITLE_STR, DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VELOCITY_CORRELATION_GRAPH_Y_AXIS_TITLE);     

		sTheStringsLookup.put(CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL_STR, CALCULATE_PSEUDO_CORRELATION_BUTTON_LABEL);     
		sTheStringsLookup.put(CALCULATE_PSEUDO_CORRELATION_BUTTON_DESC_STR, CALCULATE_PSEUDO_CORRELATION_BUTTON_DESC);     
		sTheStringsLookup.put(VELOCITY_PSEUDO_CORRELATIONS_GRAPH_FRAME_TITLE_STR, VELOCITY_PSEUDO_CORRELATIONS_GRAPH_FRAME_TITLE);     
		sTheStringsLookup.put(U_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE_STR, U_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(V_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE_STR, V_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(W_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE_STR, W_VELOCITY_PSEUDO_CORRELATIONS_GRAPH_TITLE);     
		sTheStringsLookup.put(VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_TEXT_STR, VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_TEXT);     
		sTheStringsLookup.put(VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE_STR, VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR, VERTICAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE);     
		sTheStringsLookup.put(HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL_STR, HORIZONTAL_DISTRIBUTION_OF_VELOCITY_PSEUDO_CORRELATION_GRAPH_LEGEND_KEY_LABEL);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE_STR, DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE_STR, DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_X_AXIS_TITLE);     
		sTheStringsLookup.put(DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE_STR, DEPTH_AVERAGED_VELOCITY_PSEUDO_CORRELATION_GRAPH_Y_AXIS_TITLE);     

		sTheStringsLookup.put(SYNCH_IGNORE_FIRST_X_SECONDS_SETTER_LABEL_STR, SYNCH_IGNORE_FIRST_X_SECONDS_SETTER_LABEL);     

		sTheStringsLookup.put(MOVING_AVERAGE_WINDOW_SIZE_LABEL_STR, MOVING_AVERAGE_WINDOW_SIZE_LABEL);     

		sTheStringsLookup.put(EXPORT_DWT_LEVELS_DATA_BUTTON_LABEL_STR, EXPORT_DWT_LEVELS_DATA_BUTTON_LABEL);     
		sTheStringsLookup.put(EXPORT_DWT_LEVELS_DATA_BUTTON_DESC_STR, EXPORT_DWT_LEVELS_DATA_BUTTON_DESC);     

		sTheStringsLookup.put(CHOOSE_CONFIG_FILE_STR, CHOOSE_CONFIG_FILE);     

		sTheStringsLookup.put(COPY_CONFIG_FILE_STR, COPY_CONFIG_FILE);     

		sTheStringsLookup.put(SELECT_NEW_CONFIG_FILE_DIALOG_TITLE_STR, SELECT_NEW_CONFIG_FILE_DIALOG_TITLE);     

		sTheStringsLookup.put(CONFIGURATION_FILE_FILTER_TEXT_STR, CONFIGURATION_FILE_FILTER_TEXT);     

		sTheStringsLookup.put(CSV_FILE_FORMAT_STR, CSV_FILE_FORMAT);     

		sTheStringsLookup.put(ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL_STR, ENTER_CUSTOM_CSV_FILE_FORMAT_LABEL);     

		sTheStringsLookup.put(PST_REPLACEMENT_POLYNOMIAL_ORDER_LABEL_STR, PST_REPLACEMENT_POLYNOMIAL_ORDER_LABEL);     

		sTheStringsLookup.put(FIVE_THIRDS_LINE_CENTRE_STR, FIVE_THIRDS_LINE_CENTRE);     
		sTheStringsLookup.put(SHOW_FIVE_THIRDS_LINE_STR, SHOW_FIVE_THIRDS_LINE);     

		sTheStringsLookup.put(FIVE_THIRDS_LINE_LEGEND_ENTRY_STR, FIVE_THIRDS_LINE_LEGEND_ENTRY);     

		sTheStringsLookup.put(PSD_TYPE_LABEL_STR, PSD_TYPE_LABEL);     
		sTheStringsLookup.put(PSD_WINDOW_LABEL_STR, PSD_WINDOW_LABEL);     
		sTheStringsLookup.put(PSD_WELCH_WINDOW_OVERLAP_LABEL_STR, PSD_WELCH_WINDOW_OVERLAP_LABEL);     
		sTheStringsLookup.put(PSD_TYPE_BARTLETT_STR, PSD_TYPE_BARTLETT);     
		sTheStringsLookup.put(PSD_TYPE_WELCH_STR, PSD_TYPE_WELCH);     
		sTheStringsLookup.put(PSD_WINDOW_NONE_STR, PSD_WINDOW_NONE);     
		sTheStringsLookup.put(PSD_WINDOW_BARTLETT_STR, PSD_WINDOW_BARTLETT);     
		sTheStringsLookup.put(PSD_WINDOW_HAMMING_STR, PSD_WINDOW_HAMMING);     

		sTheStringsLookup.put(EXPORT_PSD_S_NOUGHTS_BUTTON_LABEL_STR, EXPORT_PSD_S_NOUGHTS_BUTTON_LABEL);     
		sTheStringsLookup.put(EXPORT_PSD_S_NOUGHTS_BUTTON_DESC_STR, EXPORT_PSD_S_NOUGHTS_BUTTON_DESC);     

		sTheStringsLookup.put(Y_COLUMN_TITLE_STR, Y_COLUMN_TITLE);     
		sTheStringsLookup.put(Z_COLUMN_TITLE_STR, Z_COLUMN_TITLE);     
		sTheStringsLookup.put(S_NOUGHT_COLUMN_TITLE_STR, S_NOUGHT_COLUMN_TITLE);     

		sTheStringsLookup.put(TI_SCALE_BY_Q_OVER_A_LABEL_STR, TI_SCALE_BY_Q_OVER_A_LABEL);     

		sTheStringsLookup.put(INVERT_AXES_TEXT_STR, INVERT_AXES_TEXT);     

		sTheStringsLookup.put(CWT_BY_FREQUENCY_GRAPH_TITLE_STR, CWT_BY_FREQUENCY_GRAPH_TITLE);     
		sTheStringsLookup.put(CWT_BY_FREQUENCY_TAB_LABEL_STR, CWT_BY_FREQUENCY_TAB_LABEL);     
		sTheStringsLookup.put(CWT_BY_TIME_GRAPH_TITLE_STR, CWT_BY_TIME_GRAPH_TITLE);     
		sTheStringsLookup.put(CWT_BY_TIME_TAB_LABEL_STR, CWT_BY_TIME_TAB_LABEL);     

		sTheStringsLookup.put(CWT_BY_FREQUENCY_GRAPH_X_AXIS_LABEL_STR, CWT_BY_FREQUENCY_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(CWT_BY_FREQUENCY_GRAPH_Y_AXIS_LABEL_STR, CWT_BY_FREQUENCY_GRAPH_Y_AXIS_LABEL);     
		sTheStringsLookup.put(CWT_BY_TIME_GRAPH_X_AXIS_LABEL_STR, CWT_BY_TIME_GRAPH_X_AXIS_LABEL);     
		sTheStringsLookup.put(CWT_BY_TIME_GRAPH_Y_AXIS_LABEL_STR, CWT_BY_TIME_GRAPH_Y_AXIS_LABEL);     

		sTheStringsLookup.put(WAVELET_TRANSFORM_SCALE_BY_INST_POWER_LABEL_STR, WAVELET_TRANSFORM_SCALE_BY_INST_POWER_LABEL);     

		sTheStringsLookup.put(FILE_EXTENSION_LABEL_STR, FILE_EXTENSION_LABEL);

//INSERT_ADD_TO_LOOKUP      
	}      
}      
