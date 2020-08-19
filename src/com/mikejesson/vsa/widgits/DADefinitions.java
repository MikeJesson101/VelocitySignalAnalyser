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

/**
 * @author mikefedora
 *
 */
public class DADefinitions {
	public static final String VERSION = "1.5.64";
	
	public static final int FILE_FORMAT_2_FIRST_VERSION_NUMBER = 2;
	
	public static final double INVALID_SNR_OR_CORRELATION = Double.NaN;
	public static final int LARGE_FILES_MAX_MEAS_PER_SPLIT = 200000;
	
	public static final double USING_BINARY_FILE_FORMAT = 1d;
	public static final double USING_XML_FILE_FORMAT = 0d;
	
	public static final double USING_PERCENTAGE_FOR_CORR_AND_SNR_FILTER = 1d;
	public static final double USING_ABSOLUTE_VALUE_FOR_CORR_AND_SNR_FILTER = 0d;
	
	public static final String PROBE_TYPE_VECTRINO = "VNO";
	public static final String PROBE_TYPE_VECTOR = "VEC";
	public static final String PROBE_TYPE_VECTRINO_II = "VEC_II";
	
	public static final String SFR_ZERO_READINGS = "Zero Reading";
	public static final String SFR_READINGS = "Reading";
	
	public static final String STRINGS_FILE_NAME = "majdaStrings.txt";
	public static final String CONFIG_FILE_NAME = "majda.conf";
	public static final String CONFIG_FILE_NAME_EXTENSION = ".conf";
	public static final String CONFIG_FILE_NAME_EXTENSION_FOR_LAST_USED = ".lastconf";
	public static final String CONFIG_DIR_NAME = "majda";
	
	// Don't use these anymore, use the XML_... ones below
	public static final String CONFIG_DEFAULT_DATA_FILE_PATH_XML_TAG = "default_data_file_path";
	public static final String CONFIG_DEFAULT_CSV_FILE_PATH_XML_TAG = "default_csv_file_path";
	public static final String CONFIG_DEFAULT_MATLAB_EXPORT_FILE_PATH_XML_TAG = "default_file_path";
	public static final String CONFIG_PRE_FILTER_TYPE_XML_TAG = "default_pre_filter_type";
	public static final String CONFIG_DESPIKING_FILTER_TYPE_XML_TAG = "default_despiking_filter_type";
	public static final String CONFIG_EXCLUDE_LEVEL_XML_TAG = "default_exclude_level";
	public static final String CONFIG_MODIFIED_PST_AUTO_SAFE_LEVEL_C1_XML_TAG = "modified_pst_auto_safe_level_c1";
	public static final String CONFIG_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_XML_TAG = "modified_pst_auto_exclude_level_c2";
	public static final String CONFIG_PST_REPLACEMENT_METHOD_XML_TAG = "pst_replacement_method";
	public static final String CONFIG_CS_TYPE_XML_TAG = "cs_type";
	public static final String CONFIG_SAMPLING_RATE_XML_TAG = "sampling_rate";
	public static final String CONFIG_LIMITING_CORRELATION_XML_TAG = "limiting_corr";
	public static final String CONFIG_LIMITING_SNR_XML_TAG = "limiting_SNR";
	public static final String CONFIG_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_XML_TAG = "use_percentage_for_corr_and_snr_filter";
	public static final String CONFIG_LIMITING_W_DIFF_XML_TAG = "limiting_w_diff";
	public static final String CONFIG_MAIN_XML_TAG = "configuration";
	public static final String CONFIG_DATA_FILE_DELIMITER_XML_TAG = "data_file_delimiter";
	public static final String CONFIG_DATA_SET_LENGTH_UNIT_SCALE_FACTOR_XML_TAG = "length_unit_scale_factor";
	public static final String CONFIG_DATA_FILE_VELOCITY_UNIT_SCALE_FACTOR_XML_TAG = "default_data_file_scale_factor";
	public static final String CONFIG_LEFT_BANK_POSITION_XML_TAG = "default_left_bank_position";
	public static final String CONFIG_RIGHT_BANK_POSITION_XML_TAG = "default_right_bank_position";
	public static final String CONFIG_WATER_DEPTH_XML_TAG = "default_water_depth";
	public static final String CONFIG_MEASURED_DISCHARGE_XML_TAG = "default_measured_discharge";
	public static final String CONFIG_BED_SLOPE_XML_TAG = "default_bed_slope";
	public static final String CONFIG_DEFAULT_CELL_WIDTH_XML_TAG = "default_standard_cell_width";
	public static final String CONFIG_DEFAULT_CELL_HEIGHT_XML_TAG = "default_standard_cell_height";
	public static final String CONFIG_MRS_LIMITING_VALUE_XML_TAG = "default_mrs_limiting_value";
	public static final String CONFIG_MRS_LIMITING_VALUE_DIRECTION_XML_TAG = "default_mrs_limiting_value_direction";
	public static final String CONFIG_LARGE_FILE_MEAS_PER_SPLIT_XML_TAG = "large_file_meas_per_split";
	public static final String CONFIG_FIXED_PROBE_INDEX_XML_TAG = "fixed_probe_index";
	public static final String CONFIG_USE_BINARY_FILE_FORMAT = "use_binary_file_format";
	public static final String CONFIG_PROBE_SETUP_XML_TAG = "probe_setup";
	public static final String CONFIG_PROBE_SETUP_COORDS_XML_TAG = "coords";
	public static final String CONFIG_PROBE_SETUP_SYNCH_INDICES_XML_TAG = "synch_indices";
	public static final String CONFIG_NUMBER_OF_BARTLETT_WINDOWS = "no_of_bartlett_windows";
	
	public static final String XML_DATA_SET = "data_set";
	public static final String XML_DATA_SET_TYPE = "data_set_type";
	public static final String XML_DATA_SET_LENGTH_SCALE_FACTOR = "length_unit_scale_factor";
	public static final String XML_DATA_FILE_VELOCITY_SCALE_FACTOR = "data_file_scale_factor";
	public static final String XML_EXCLUDE_LEVEL = "exclude_level";
	public static final String XML_MODIFIED_PST_AUTO_SAFE_LEVEL_C1 = "modified_pst_auto_safe_level_c1";
	public static final String XML_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2 = "modified_pst_auto_exclude_level_c2";
	public static final String XML_PST_REPLACEMENT_METHOD = "pst_replacement_method";
	public static final String XML_PST_REPLACEMENT_POLYNOMIAL_ORDER = "pst_replacement_polynomial_order";
	public static final String XML_CS_TYPE = "cs_type";
	public static final String XML_SAMPLING_RATE = "sampling_rate";
	public static final String XML_LIMITING_CORRELATION = "limiting_corr";
	public static final String XML_LIMITING_SNR = "limiting_SNR";
	public static final String XML_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER = "use_percentage_for_corr_and_snr_filter";
	public static final String XML_LIMITING_W_DIFF = "limiting_w_diff";
	public static final String XML_MOVING_AVERAGE_WINDOW_SIZE = "moving_average_window_size";
	public static final String XML_LEFT_BANK_POSITION = "left_bank_position";
	public static final String XML_RIGHT_BANK_POSITION = "right_bank_position";
	public static final String XML_WATER_DEPTH = "water_depth";
	public static final String XML_MEASURED_DISCHARGE = "measured_discharge";
	public static final String XML_INVERT_X_AXIS = "invert_x_axis";
	public static final String XML_INVERT_Y_AXIS = "invert_y_axis";
	public static final String XML_INVERT_Z_AXIS = "invert_z_axis";
	public static final String XML_DATA_POINT = "data_point";
	public static final String XML_Y_COORD = "ycoord";
	public static final String XML_Z_COORD = "zcoord";
	public static final String XML_MEAN = "mean";
	public static final String XML_ST_DEV = "st_dev";
	public static final String XML_ROTATION_CORRECTED_MEAN = "rotation_corrected_mean";
	public static final String XML_ROTATION_CORRECTED_ST_DEV = "rotation_corrected_st_dev";
	public static final String XML_FT_MEAN = "ft_mean";
	public static final String XML_FT_ST_DEV = "ft_st_dev";
	public static final String XML_FTRC_MEAN = "ftrc_mean";
	public static final String XML_FTRC_ST_DEV = "ftrc_st_dev";
	public static final String XML_DATA_POINT_X_COMPONENT = "x_component";
	public static final String XML_DATA_POINT_Y_COMPONENT = "y_component";
	public static final String XML_DATA_POINT_Z_COMPONENT = "z_component";
	public static final String XML_DATA_POINT_PRESSURE = "pressure";
	public static final String XML_DATA_POINT_VERSION = "version";
	public static final String XML_DATA_POINT_VALUE = "val";
	public static final String XML_DATA_POINT_RAW_VELOCITY = "vel";
	public static final String XML_DATA_POINT_CORRELATION = "corr";
	public static final String XML_DATA_POINT_SNR = "snr";
	public static final String XML_DATA_POINT_W_DIFF = "w_diff";
	public static final String XML_DATA_POINT_TRANSLATED_VELOCITY = "t_vel";
	public static final String XML_DATA_POINT_FILTERED_AND_TRANSLATED_VELOCITY = "ft_vel";
	public static final String XML_DATA_POINT_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY = "ftrc_vel";

	public static final String XML_DATA_POINT_MULTI_RUN_SYNCH_INDEX = "synch_index";
	public static final String XML_THETA = "xz_plane_probe_rotation";
	public static final String XML_PHI = "yz_plane_probe_rotation";
	public static final String XML_ALPHA = "xy_plane_probe_rotation";
	public static final String XML_BED_SLOPE = "bed_slope";
	public static final String XML_U_PRIME_V_PRIME_MEAN = "u_prime_v_prime_mean";
	public static final String XML_U_PRIME_W_PRIME_MEAN = "u_prime_w_prime_mean";
	public static final String XML_V_PRIME_W_PRIME_MEAN = "v_prime_w_prime_mean";
	public static final String XML_NUMBER_OF_PROBES = "number_of_probes";
	public static final String XML_TAB_ALIAS = "***TAB***";
	public static final String XML_CORRECT_ROTATION = "correct_rotation";
	public static final String XML_DATA_SET_LOCKED = "data_set_locked";
	public static final String XML_STANDARD_CELL_WIDTH = "standard_cell_width";
	public static final String XML_STANDARD_CELL_HEIGHT = "standard_cell_height";
	public static final String XML_SYNCH_IGNORE_FIRST_X_SECONDS = "ignore_first_x_seconds";
	public static final String XML_SYNCH_LIMITING_VALUE = "mrs_limiting_value";
	public static final String XML_SYNCH_LIMITING_VALUE_DIRECTION = "mrs_limiting_value_direction";
	public static final String XML_AT_TRIM_START_BY = "at_trim_start_by";
	public static final String XML_AT_PRIOR_LENGTH = "at_prior_length";
	public static final String XML_AT_SAMPLE_LENGTH = "at_sample_length";
	public static final String XML_LARGE_FILE_MEAS_PER_SPLIT = "large_file_meas_per_split";
	public static final String XML_USE_BINARY_FILE_FORMAT = "use_binary_file_format";
	public static final String XML_PSD_TYPE = "psd_type";
	public static final String XML_PSD_WINDOW = "psd_window";
	public static final String XML_NUMBER_OF_BARTLETT_WINDOWS = "no_of_bartlett_windows";
	public static final String XML_PSD_WELCH_WINDOW_OVERLAP = "psd_welch_window_overlap";
	public static final String XML_WAVELET_TYPE = "wavelet_type";
	public static final String XML_WAVELET_TRANSFORM_TYPE = "wavelet_transform_type";
	public static final String XML_WAVELET_TRANSFORM_SCALE_BY_INST_POWER = "wavelet_transform_scale_by_inst_power";
	public static final String XML_MAIN_PROBE_INDEX = "main_probe_index";
	public static final String XML_FIXED_PROBE_INDEX = "fixed_probe_index";
	public static final String XML_SYNCH_PROBE_INDEX = "synch_probe_index";
	public static final String XML_PROBE_SETUP = "probe_setup";
	public static final String XML_PROBE_SETUP_COORDS_XML_TAG = "coords";
	public static final String XML_PROBE_SETUP_SYNCH_INDICES_XML_TAG = "synch_indices";
	public static final String XML_PERCENTAGE_OF_VELOCITIES_GOOD = "percentage_good";
	public static final String XML_PRE_FILTER_TYPE = "pre_filter_type";
	public static final String XML_DESPIKING_FILTER_TYPE = "despiking_filter_type";
	public static final String XML_QUADRANT = "quadrant";
	public static final String XML_QUADRANT_HOLE_PERCENTAGE_ENTRIES_IN_QUADRANT = "percentage_entries_in_quadrant";
	public static final String XML_UW_QUADRANT_1_SHEAR_STRESS = "uw_quadrant_1_shear_stress";
	public static final String XML_UW_QUADRANT_2_SHEAR_STRESS = "uw_quadrant_2_shear_stress";
	public static final String XML_UW_QUADRANT_3_SHEAR_STRESS = "uw_quadrant_3_shear_stress";
	public static final String XML_UW_QUADRANT_4_SHEAR_STRESS = "uw_quadrant_4_shear_stress";
	public static final String XML_QH_UW_Q1_TO_Q3_EVENTS_RATIO = "uw_q1_to_q3_events_ratio";
	public static final String XML_QH_UW_Q2_TO_Q4_EVENTS_RATIO = "uw_q2_to_q4_events_ratio";
	public static final String XML_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO = "uw_q2_and_q4_to_q1_and_q3_events_ratio";
	public static final String XML_UV_QUADRANT_1_SHEAR_STRESS = "uv_quadrant_1_shear_stress";
	public static final String XML_UV_QUADRANT_2_SHEAR_STRESS = "uv_quadrant_2_shear_stress";
	public static final String XML_UV_QUADRANT_3_SHEAR_STRESS = "uv_quadrant_3_shear_stress";
	public static final String XML_UV_QUADRANT_4_SHEAR_STRESS = "uv_quadrant_4_shear_stress";
	public static final String XML_QH_UV_Q1_TO_Q3_EVENTS_RATIO = "uv_q1_to_q3_events_ratio";
	public static final String XML_QH_UV_Q2_TO_Q4_EVENTS_RATIO = "uv_q2_to_q4_events_ratio";
	public static final String XML_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO = "uv_q2_and_q4_to_q1_and_q3_events_ratio";
	public static final String XML_U_TURBULENCE_INTENSITY = "u_turbulence_intensity";
	public static final String XML_V_TURBULENCE_INTENSITY = "v_turbulence_intensity";
	public static final String XML_W_TURBULENCE_INTENSITY = "w_turbulence_intensity";
	public static final String XML_THIRD_ORDER_CORRELATION_0_3 = "third_order_correlation_0_3";
	public static final String XML_THIRD_ORDER_CORRELATION_1_2 = "third_order_correlation_1_2";
	public static final String XML_THIRD_ORDER_CORRELATION_2_1 = "third_order_correlation_2_1";
	public static final String XML_THIRD_ORDER_CORRELATION_3_0 = "third_order_correlation_3_0";
	public static final String[][][] XML_THIRD_ORDER_CORRELATION = 
		{ 	{ 	{"third_order_correlation_0_0_0", "third_order_correlation_0_0_1", "third_order_correlation_0_0_2", "third_order_correlation_0_0_3"},
				{"third_order_correlation_0_1_0", "third_order_correlation_0_1_1", "third_order_correlation_0_1_2", "third_order_correlation_0_1_3"},
				{"third_order_correlation_0_2_0", "third_order_correlation_0_2_1", "third_order_correlation_0_2_2", "third_order_correlation_0_2_3"},
				{"third_order_correlation_0_3_0", "third_order_correlation_0_3_1", "third_order_correlation_0_3_2", "third_order_correlation_0_3_3"}},
			{ 	{"third_order_correlation_1_0_0", "third_order_correlation_1_0_1", "third_order_correlation_1_0_2", "third_order_correlation_1_0_3"},
				{"third_order_correlation_1_1_0", "third_order_correlation_1_1_1", "third_order_correlation_1_1_2", "third_order_correlation_1_1_3"},
				{"third_order_correlation_1_2_0", "third_order_correlation_1_2_1", "third_order_correlation_1_2_2", "third_order_correlation_1_2_3"},
				{"third_order_correlation_1_3_0", "third_order_correlation_1_3_1", "third_order_correlation_1_3_2", "third_order_correlation_1_3_3"}},
			{ 	{"third_order_correlation_2_0_0", "third_order_correlation_2_0_1", "third_order_correlation_2_0_2", "third_order_correlation_2_0_3"},
				{"third_order_correlation_2_1_0", "third_order_correlation_2_1_1", "third_order_correlation_2_1_2", "third_order_correlation_2_1_3"},
				{"third_order_correlation_2_2_0", "third_order_correlation_2_2_1", "third_order_correlation_2_2_2", "third_order_correlation_2_2_3"},
				{"third_order_correlation_2_3_0", "third_order_correlation_2_3_1", "third_order_correlation_2_3_2", "third_order_correlation_2_3_3"}},
			{ 	{"third_order_correlation_3_0_0", "third_order_correlation_3_0_1", "third_order_correlation_3_0_2", "third_order_correlation_3_0_3"},
				{"third_order_correlation_3_1_0", "third_order_correlation_3_1_1", "third_order_correlation_3_1_2", "third_order_correlation_3_1_3"},
				{"third_order_correlation_3_2_0", "third_order_correlation_3_2_1", "third_order_correlation_3_2_2", "third_order_correlation_3_2_3"},
				{"third_order_correlation_3_3_0", "third_order_correlation_3_3_1", "third_order_correlation_3_3_2", "third_order_correlation_3_3_3"}}
		};
	public static final String[][] XML_ANISOTROPIC_STRESS_TENSOR = 
		{ 	{"anisotropic_stress_tensor_0_0", "anisotropic_stress_tensor_0_1", "anisotropic_stress_tensor_0_2"},
			{"anisotropic_stress_tensor_1_0", "anisotropic_stress_tensor_1_1", "anisotropic_stress_tensor_1_2"},
			{"anisotropic_stress_tensor_2_0", "anisotropic_stress_tensor_2_1", "anisotropic_stress_tensor_2_2"},
		};
	public static final String XML_TKE = "tke";
	public static final String XML_U_TKE_FLUX = "u_tke_flux";
	public static final String XML_V_TKE_FLUX = "v_tke_flux";
	public static final String XML_W_TKE_FLUX = "w_tke_flux";
	public static final String XML_FIXED_PROBE_U_CORRELATION = "fixed_probe_u_correlation";
	public static final String XML_FIXED_PROBE_V_CORRELATION = "fixed_probe_v_correlation";
	public static final String XML_FIXED_PROBE_W_CORRELATION = "fixed_probe_w_correlation";
	public static final String XML_BATCH_NUMBER = "batch_number";
	public static final String XML_BATCH_THETA_ROTATION_CORRECTION = "batch_theta_rotation_correction";
	public static final String XML_BATCH_ALPHA_ROTATION_CORRECTION = "batch_alpha_rotation_correction";
	public static final String XML_BATCH_PHI_ROTATION_CORRECTION = "batch_phi_rotation_correction";
	public static final String XML_MAX_U = "max_u";
	public static final String XML_MAX_V = "max_v";
	public static final String XML_MAX_W = "max_w";
	public static final String XML_X_MEAN_CORRELATION = "x_mean_corr";
	public static final String XML_X_MEAN_SNR = "x_corr_st_dev";
	public static final String XML_Y_MEAN_CORRELATION = "y_mean_corr";
	public static final String XML_Y_MEAN_SNR = "y_corr_st_dev";
	public static final String XML_Z_MEAN_CORRELATION = "z_mean_corr";
	public static final String XML_Z_MEAN_SNR = "z_corr_st_dev";

	public static final String XML_RCB_FILE_BATCHES = "batches";
	public static final String XML_RCB_FILE_BATCH = "batch";
	public static final String XML_RCB_FILE_BATCH_NUMBER = "batch_number";
	public static final String XML_RCB_FILE_BATCH_LIMITS = "batch_limits";
	public static final String XML_RCB_FILE_Y_MIN = "y_min";
	public static final String XML_RCB_FILE_Y_MAX = "y_max";
	public static final String XML_RCB_FILE_Z_MIN = "z_min";
	public static final String XML_RCB_FILE_Z_MAX = "z_max";
	public static final String XML_MULTI_RUN_NUMBER_OF_RUNS = "number_of_runs";
	public static final String XML_SYNCH_PARENT_ID = "synch_index";
	
	public static final String XML_PROBE_DETAILS = "probe_details";
	public static final String XML_PD_PROBE_TYPE = "probe_type";
	public static final String XML_PD_PROBE_ID = "probe_id";
	public static final String XML_PD_SAMPLING_RATE = "sampling_rate";
	
	public static final String XML_DEFAULT_DATA_FILE_PATH = "default_data_file_path";
	public static final String XML_DEFAULT_CSV_FILE_PATH = "default_csv_file_path";
	public static final String XML_DEFAULT_MATLAB_EXPORT_FILE_PATH = "default_file_path";
	public static final String XML_CSV_FILE_FORMAT = "csv_file_format";
	public static final String XML_DATA_FILE_DELIMITER = "data_file_delimiter";
	public static final String XML_CSV_FILE_DECIMAL_SEPARATOR = "csv_file_decimal_separator";
	public static final String XML_BOUNDARY_DEFINITION_FILENAME = "boundary_definition_filename";
	
	public static final String FILE_EXTENSION_DATA_SET = ".majds";
	public static final String FILE_EXTENSION_DATA_POINT = ".majdp";
	public static final String FILE_EXTENSION_DATA_POINT_BINARY = ".majdpb";
	public static final String FILE_EXTENSION_CSV = "dat:csv:txt";
	public static final String FILE_EXTENSION_CONVERTED_POLYSYNC_VELOCITY = "vel.txt";
	public static final String FILE_EXTENSION_VECTRINO_VNO = "vno";
	public static final String FILE_EXTENSION_POLYSYNC_VNO = "vno";
	public static final String FILE_EXTENSION_VECTOR_VEC = "vec";
	public static final String FILE_EXTENSION_SONTEK_ADV = "adv";
	public static final String FILE_EXTENSION_COBRA_THx = "th";
	public static final String FILE_EXTENSION_ASCII_DETAILS_OUTPUT = ".dat";
	public static final String FILE_EXTENSION_MATLAB_DETAILS_OUTPUT = ".mat";
	public static final String FILE_EXTENSION_VECTRINO_II_MAT = "mat";
	public static final String FILE_EXTENSION_VECTRINO_II_RAW = "raw";
	public static final String FILE_EXTENSION_BOUNDARY_DEFINITION = "majbd";
	public static final String FILE_EXTENSION_UOB_DPMS = "uobcsv";
	
	public static final String NDV_HEADER_FILE_EXTENSION = ".hdr";
	public static final String NDV_DATA_FILE_EXTENSION = ".dat";
	public static final String NDV_X_VELOCITY_HEADER_IDENTIFIER = "Velocity (Beam1|X";
	public static final String NDV_Y_VELOCITY_HEADER_IDENTIFIER = "Velocity (Beam2|Y";
	public static final String NDV_Z1_VELOCITY_HEADER_IDENTIFIER = "Velocity (Beam3|Z";
	public static final String NDV_Z2_VELOCITY_HEADER_IDENTIFIER = "Velocity (Beam4|Z2";
	public static final String NDV_X_CORRELATION_HEADER_IDENTIFIER = "Correlation (Beam1";
	public static final String NDV_Y_CORRELATION_HEADER_IDENTIFIER = "Correlation (Beam2";
	public static final String NDV_Z1_CORRELATION_HEADER_IDENTIFIER = "Correlation (Beam3";
	public static final String NDV_Z2_CORRELATION_HEADER_IDENTIFIER = "Correlation (Beam4";
	public static final String NDV_X_SNR_HEADER_IDENTIFIER = "SNR (Beam1";
	public static final String NDV_Y_SNR_HEADER_IDENTIFIER = "SNR (Beam2";
	public static final String NDV_Z1_SNR_HEADER_IDENTIFIER = "SNR (Beam3";
	public static final String NDV_Z2_SNR_HEADER_IDENTIFIER = "SNR (Beam4";
	public static final String NDV_SAMPLING_RATE_HEADER_IDENTIFIER = "Sampling rate";
	public static final String NDV_PROBE_TYPE_IDENTIFIER = "Output sync";
	public static final String NDV_PROBE_ID_IDENTIFIER = "Serial number";
	
	public static final String KILOHERTZ = "kHz";
	
	public static final String TEMP_DIR_NAME = "temp";
	public static final String ROTATION_CORRECTION_BATCHES_FILENAME = "batches.majrcb";
	public static final String COMMAND_APPLY_CONFIG = "apply_config";
	public static final String FIXED_PROBE_FILENAME_INSERT = "fixed-probe-";
	public static final String COORDINATE_SEPARATOR = "-";
	public static final String MULTI_RUN_FILENAME_INSERT = "run" + COORDINATE_SEPARATOR;
	
	public static final double WATER_DENSITY_RHO = 1000;
	public static final double GRAVITATIONAL_ACCELERATION_G = 9.807;
	public static final double KINEMATIC_VISCOSITY_MU = 1E-6;
	public static final double VON_KARMANS_CONSTANT = 0.41;
	public static final double LOG_LAW_DAMPING_FACTOR_B = 26;
	
	public static final String VECTRINO_DATA_FILE_DELIMITER = " ";
	
	public static final String POLYSYNC_DATA_FILE_DELIMITER = " ";
	public static final String POLYSYNC_PROBE_NUMBER_COLUMN_HEADING = "Probe";
	public static final String POLYSYNC_X_COLUMN_HEADING = "X (1)";
	public static final String POLYSYNC_Y_COLUMN_HEADING = "Y (2)";
	public static final String POLYSYNC_Z1_COLUMN_HEADING = "Z (3)";
	public static final String POLYSYNC_Z2_COLUMN_HEADING = "W (4)";
	
	public static final String XML_POLYSYNC_CONFIGURATION_TAG = "CONFIGURATION";
	public static final String XML_POLYSYNC_PROBE_TYPE_TAG = "type";
	public static final String XML_POLYSYNC_SAMPLING_RATE_TAG = "SAMPLING_RATE";
	public static final String XML_POLYSYNC_SERIAL_NUMBER_TAG = "SERIAL_NUMBER";
	
	public static final String VECTRINO_II_HDR_FILE_DELIMITER = ":";
	public static final String VECTRINO_II_DAT_FILE_DELIMITER = ":";
	public static final String VECTRINO_II_SAMPLING_RATE_TAG = "sampleRate";
	public static final String VECTRINO_II_CELL_SIZE_TAG = "cellSize";
	public static final double VECTRINO_II_CELL_SIZE_SCALAR = 0.1;
	public static final String VECTRINO_II_NUMBER_OF_CELLS_TAG = "nCells";
	public static final String VECTRINO_II_X_VELOCITIES = "Profiles_Velocity_X";
	public static final String VECTRINO_II_Y_VELOCITIES = "Profiles_Velocity_Y";
	public static final String VECTRINO_II_Z1_VELOCITIES = "Profiles_Velocity_Z1";
	public static final String VECTRINO_II_Z2_VELOCITIES = "Profiles_Velocity_Z2";
	public static final String VECTRINO_II_BEAM_1_CORRELATIONS = "Profiles_Correlation_Beam_1";
	public static final String VECTRINO_II_BEAM_2_CORRELATIONS = "Profiles_Correlation_Beam_2";
	public static final String VECTRINO_II_BEAM_3_CORRELATIONS = "Profiles_Correlation_Beam_3";
	public static final String VECTRINO_II_BEAM_4_CORRELATIONS = "Profiles_Correlation_Beam_4";
	public static final String VECTRINO_II_BEAM_1_SNRS = "Profiles_SNR_Beam_1";
	public static final String VECTRINO_II_BEAM_2_SNRS = "Profiles_SNR_Beam_2";
	public static final String VECTRINO_II_BEAM_3_SNRS = "Profiles_SNR_Beam_3";
	public static final String VECTRINO_II_BEAM_4_SNRS = "Profiles_SNR_Beam_4";
	public static final String VECTRINO_II_INSTRUMENT_NAME_TAG = "Ins_name";
	public static final String VECTRINO_II_INSTRUMENT_NAME = "Vectrino-II";
	public static final String VECTRINO_PROFILER_INSTRUMENT_NAME = "Vectrino Profiler";
	
	public static final String VECTRINO_II_MATLAB_CONFIG_ARRAY = "Config";
	public static final String VECTRINO_II_MATLAB_SAMPLING_RATE = "sampleRate";
	public static final String VECTRINO_II_MATLAB_CELL_SIZE = "cellSize";
	public static final String VECTRINO_II_MATLAB_NUMBER_OF_CELLS = "nCells";
	public static final String VECTRINO_II_MATLAB_X_VELOCITIES = "Profiles_VelX";
	public static final String VECTRINO_II_MATLAB_Y_VELOCITIES = "Profiles_VelY";
	public static final String VECTRINO_II_MATLAB_Z1_VELOCITIES = "Profiles_VelZ1";
	public static final String VECTRINO_II_MATLAB_Z2_VELOCITIES = "Profiles_VelZ2";
	public static final String VECTRINO_II_MATLAB_BEAM_1_CORRELATIONS = "Profiles_CorBeam1";
	public static final String VECTRINO_II_MATLAB_BEAM_2_CORRELATIONS = "Profiles_CorBeam2";
	public static final String VECTRINO_II_MATLAB_BEAM_3_CORRELATIONS = "Profiles_CorBeam3";
	public static final String VECTRINO_II_MATLAB_BEAM_4_CORRELATIONS = "Profiles_CorBeam4";
	public static final String VECTRINO_II_MATLAB_BEAM_1_SNRS = "Profiles_SNRBeam1";
	public static final String VECTRINO_II_MATLAB_BEAM_2_SNRS = "Profiles_SNRBeam2";
	public static final String VECTRINO_II_MATLAB_BEAM_3_SNRS = "Profiles_SNRBeam3";
	public static final String VECTRINO_II_MATLAB_BEAM_4_SNRS = "Profiles_SNRBeam4";
	
	public static final String VECTRINO_II_MATLAB_DATA_ARRAY = "Data";
	public static final String VECTRINO_II_MATLAB_ARRAY_X_VELOCITIES = "Profiles_VelX";
	public static final String VECTRINO_II_MATLAB_ARRAY_Y_VELOCITIES = "Profiles_VelY";
	public static final String VECTRINO_II_MATLAB_ARRAY_Z1_VELOCITIES = "Profiles_VelZ1";
	public static final String VECTRINO_II_MATLAB_ARRAY_Z2_VELOCITIES = "Profiles_VelZ2";
	public static final String VECTRINO_II_MATLAB_ARRAY_BEAM_1_CORRELATIONS = "VelocityHeader_CorBeam1";
	public static final String VECTRINO_II_MATLAB_ARRAY_BEAM_2_CORRELATIONS = "VelocityHeader_CorBeam2";
	public static final String VECTRINO_II_MATLAB_ARRAY_BEAM_3_CORRELATIONS = "VelocityHeader_CorBeam3";
	public static final String VECTRINO_II_MATLAB_ARRAY_BEAM_4_CORRELATIONS = "VelocityHeader_CorBeam4";
	
	public static final String MATLAB_OUTPUT_CONFIG_ARRAY = VECTRINO_II_MATLAB_CONFIG_ARRAY;
	public static final String MATLAB_OUTPUT_SAMPLING_RATE = VECTRINO_II_MATLAB_SAMPLING_RATE;
	
	public static final String MATLAB_OUTPUT_DATA_ARRAY = VECTRINO_II_MATLAB_DATA_ARRAY;
	public static final String MATLAB_FILE_FORMAT_VERSION = "fileFormatVersion";
	public static final String MATLAB_X_VELOCITIES = "xVelocities";
	public static final String MATLAB_Y_VELOCITIES = "yVelocities";
	public static final String MATLAB_Z_VELOCITIES = "zVelocities";
	public static final String MATLAB_PRESSURES = "pressures";
	public static final String MATLAB_TRANSLATED_X_VELOCITIES = "translatedXVelocities";
	public static final String MATLAB_TRANSLATED_Y_VELOCITIES = "translatedYVelocities";
	public static final String MATLAB_TRANSLATED_Z_VELOCITIES = "translatedZVelocities";
	public static final String MATLAB_FT_X_VELOCITIES = "ftXVelocities";
	public static final String MATLAB_FT_Y_VELOCITIES = "ftYVelocities";
	public static final String MATLAB_FT_Z_VELOCITIES = "ftZVelocities";
	public static final String MATLAB_FTRC_X_VELOCITIES = "ftrcXVelocities";
	public static final String MATLAB_FTRC_Y_VELOCITIES = "ftrcYVelocities";
	public static final String MATLAB_FTRC_Z_VELOCITIES = "ftrcZVelocities";
	public static final String MATLAB_X_SNRS = "xSNRS";
	public static final String MATLAB_Y_SNRS = "ySNRS";
	public static final String MATLAB_Z_SNRS = "zSNRS";
	public static final String MATLAB_X_CORRELATIONS = "xCorrelations";
	public static final String MATLAB_Y_CORRELATIONS = "yCorrelations";
	public static final String MATLAB_Z_CORRELATIONS = "zCorrelations";
	public static final String MATLAB_W_DIFFS = "wDiffs";

	public static final String VNO_XML_CONFIGURATION = "CONFIGURATION";
	public static final String VNO_XML_COMMON = "COMMON";
	public static final String VNO_XML_PROBE = "PROBE";
	
	public static final int VNO_SCALING_FACTOR_BIT_MASK = 0x20;
	public static final int VEC_SCALING_FACTOR_BIT_MASK = 0x02;
	public static final int VNO_SCALING_BIT_NOT_SET_FACTOR = 1000;
	public static final int VNO_SCALING_BIT_SET_FACTOR = 10000;
	
	public static final Double REMOVED_VALUE = Double.NaN;

	public static final char FIELD_COMPONENT_SPECIFIER_IGNORE = 'i';
	public static final char FIELD_COMPONENT_SPECIFIER_U = 'x';
	public static final char FIELD_COMPONENT_SPECIFIER_V = 'y';
	public static final char FIELD_COMPONENT_SPECIFIER_W = 'z';
	public static final char FIELD_SPECIFIER_SNR = 's';
	public static final char FIELD_SPECIFIER_CORR = 'c';

	/**
	 * Constructor
	 */
	private DADefinitions() {
	}

}
