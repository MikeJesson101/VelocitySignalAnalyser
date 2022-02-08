package com.mikejesson.vsa.backEndExposed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;
import java.util.Map.Entry;

import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCNumberExtractorTokenizer;
import com.mikejesson.majfc.helpers.MAJFCSafeArrayWithKeys;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.majfc.helpers.MAJFCTools.MAJFCToolsException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetConfigIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataSetConfigStringItemIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.DataAnalyser;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.MyAbstractDocHandler;

/**
 * Inner class to hold the data set configuration details
 * 
 * @author MAJ727
 * 
 */
public class DataSetConfig extends MAJFCSafeArrayWithKeys<Double, DataSetConfigIndex> {
	private MAJFCSafeArrayWithKeys<String, DataSetConfigStringItemIndex> mStringConfigItems;

	private LinkedList<DataPointCoordinates> mCoords;
	private LinkedHashMap<Integer, Integer> mBoundaryCoords;
	private LinkedHashMap<Integer, Integer> mCalculatedBoundaryCoords;

	private Vector<Integer> mSynchParentIds;
	private Hashtable<String, DataSetConfigIndex> sConfigXMLToIndexLookup;
	private Hashtable<String, DataSetConfigIndex> sOldConfigXMLToIndexLookup;
	private Hashtable<String, DataSetConfigStringItemIndex> sConfigXMLToStringItemIndexLookup;
	
	public DataSetConfig() {
		super(new Double[BackEndAPI.getNumberOfDataSetConfigKeys()]);
		
		for (int i = 0; i < mObjects.length; ++i) {
			mObjects[i] = 0.0;
		}
		
		set(BackEndAPI.DSC_KEY_EXCLUDE_LEVEL, 1.0);
		set(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1, 1.8);
		set(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2, 1.35);		
		set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD, (double) BackEndAPI.PRM_12PP_INTERPOLATION.getIntIndex());
		set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER, 3d);
		set(BackEndAPI.DSC_KEY_CS_TYPE, 0.0);
		set(BackEndAPI.DSC_KEY_SAMPLING_RATE, 200.0);
		set(BackEndAPI.DSC_KEY_LIMITING_CORRELATION, 70.0);
		set(BackEndAPI.DSC_KEY_LIMITING_SNR, 20.0);
		set(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER, DADefinitions.USING_ABSOLUTE_VALUE_FOR_CORR_AND_SNR_FILTER);
		set(BackEndAPI.DSC_KEY_LIMITING_W_DIFF, 0.01);
		set(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE, 0.0);
		set(BackEndAPI.DSC_KEY_FLUID_DENSITY, 1000.0);
		set(BackEndAPI.DSC_KEY_FLUID_KINEMATIC_VISCOSITY, 1.0);
		set(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION, 0.0);
		set(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION, 500.0);
		set(BackEndAPI.DSC_KEY_WATER_DEPTH, 300.0);
		set(BackEndAPI.DSC_KEY_CROSS_SECTIONAL_AREA, 1500.0);
		set(BackEndAPI.DSC_KEY_WETTED_PERIMETER, 1600.0);
		set(BackEndAPI.DSC_KEY_MEASURED_DISCHARGE, 1.0);
		set(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR, 1000.0);
		set(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR, 1.0);
		set(BackEndAPI.DSC_KEY_X_AXIS_INVERTED, 0.0);
		set(BackEndAPI.DSC_KEY_Y_AXIS_INVERTED, 0.0);
		set(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED, 0.0);
		set(BackEndAPI.DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET, 1.0);
		set(BackEndAPI.DSC_KEY_DATA_SET_LOCKED, 0.0);
		set(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE, 0.0);
		set(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE, 0.0);
		set(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD, 1.0);
		set(BackEndAPI.DSC_KEY_DEFAULT_CELL_WIDTH, 10.0);
		set(BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT, 10.0);
		set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE, -1d);
		set(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION, 0d);
		set(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS, 0d);
		set(BackEndAPI.DSC_KEY_AT_TRIM_START_BY, Double.NaN);
		set(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH, Double.NaN);
		set(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH, Double.NaN);
		set(BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT, Double.NaN);
		set(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT, 0d);
		set(BackEndAPI.DSC_KEY_PSD_TYPE, 0.0);
		set(BackEndAPI.DSC_KEY_PSD_WINDOW, 0.0);
		set(BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS, 10.0);
		set(BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP, 50.0);
		set(BackEndAPI.DSC_KEY_WAVELET_TYPE, 0.0);
		set(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE, 0.0);
		set(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER, 1.0);

		// String configuration items
		mStringConfigItems = new MAJFCSafeArrayWithKeys<String, DataSetConfigStringItemIndex>(new String[BackEndAPI.getNumberOfDataSetConfigStringItemKeys()]) {
			
			@Override
			protected DataSetConfigStringItemIndex makeKey(int index) {
				return new DataSetConfigStringItemIndex(index);
			}
		};

		mStringConfigItems.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY, MAJFCTools.SYSTEM_USER_HOME_DIR);
		mStringConfigItems.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY, MAJFCTools.SYSTEM_USER_HOME_DIR);
		mStringConfigItems.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY, MAJFCTools.SYSTEM_USER_HOME_DIR);
		mStringConfigItems.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME, null);
		mStringConfigItems.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT, null);
		mStringConfigItems.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER, ",");
		mStringConfigItems.set(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR, DataAnalyser.getDefaultCSVFileDecimalSeparator());
		
		mCoords = new LinkedList<DataPointCoordinates>();
		mCoords.add(new DataPointCoordinates(10, 10));
		mSynchParentIds = new Vector<Integer>(1);
		mSynchParentIds.add(-1);
	}
	
	/**
	 * @see MAJFCSafeArrayWithKeys#makeKey
	 */
	@Override
	protected DataSetConfigIndex makeKey(int index) {
		return new DataSetConfigIndex(index);
	}

	/**
	 * Copies all elements into the given MAJFCSafeArray
	 * @param receiver The array to copy into
	 * @return The array copied into
	 * @throws MAJFCToolsException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void copyInto(MAJFCSafeArrayWithKeys<Double, DataSetConfigIndex> receiver) {
		((DataSetConfig) receiver).mCoords = getCoords();
		((DataSetConfig) receiver).mBoundaryCoords = mBoundaryCoords == null ? null : (LinkedHashMap<Integer, Integer>) mBoundaryCoords.clone();
		((DataSetConfig) receiver).mCalculatedBoundaryCoords = mCalculatedBoundaryCoords == null ? null : (LinkedHashMap<Integer, Integer>) mCalculatedBoundaryCoords.clone();
		
		mStringConfigItems.copyInto(((DataSetConfig) receiver).mStringConfigItems);

		super.copyInto(receiver);
	}

	@SuppressWarnings("unchecked")
	public LinkedList<DataPointCoordinates> getCoords() {
		if (mCoords == null) {
			mCoords = DAFrame.getFrame().getDefaultDataSetConfiguration().getCoords();
		}
		
		return (LinkedList<DataPointCoordinates>) mCoords.clone();
	}
	
	@SuppressWarnings("unchecked")
	public void setCoords(LinkedList<DataPointCoordinates> coords) {
		if (coords == null) {
			mCoords = null;
			return;
		}
		
		mCoords = (LinkedList<DataPointCoordinates>) coords.clone();
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<Integer, Integer> getBoundaryCoords() {
		if (mBoundaryCoords == null || mBoundaryCoords.size() < 2) {
			LinkedHashMap<Integer, Integer> boundaryCoords = new LinkedHashMap<Integer, Integer>();
			boundaryCoords.put(get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue(), 0);
			boundaryCoords.put(get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue(), 0);

			return boundaryCoords;
		}
		
		return (LinkedHashMap<Integer, Integer>) mBoundaryCoords.clone();
	}
	
	public void loadBoundaryCoords() {
		BufferedReader bufferedFileReader = null;
		try {
			FileReader fileReader = new FileReader(new File(mStringConfigItems.get(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME)));
			bufferedFileReader = new BufferedReader(fileReader);
			mBoundaryCoords = new LinkedHashMap<Integer, Integer>();
			mCalculatedBoundaryCoords = new LinkedHashMap<Integer, Integer>();
			String line;
			Vector<DataPointCoordinates> boundaryCoords = new Vector<DataPointCoordinates>();
			
			while ((line = bufferedFileReader.readLine()) != null) {
				MAJFCNumberExtractorTokenizer tokenizerForCoords = new MAJFCNumberExtractorTokenizer(line, ",");
				
				if (tokenizerForCoords.countTokens() != 2) {
					continue;
				}
	
				int yCoord = (int) Math.rint(Double.parseDouble(tokenizerForCoords.nextToken()));
				int zCoord = (int) Math.rint(Double.parseDouble(tokenizerForCoords.nextToken()));
				boundaryCoords.add(new DataPointCoordinates(yCoord, zCoord));
			}
			
			Collections.sort(boundaryCoords);
			
			if (boundaryCoords.size() < 2) {
				throw new Exception();
			} else {
				set(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION, (double) boundaryCoords.firstElement().getY());
				set(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION, (double) boundaryCoords.lastElement().getY());
				
				int numberOfCoords = boundaryCoords.size();
				
				for (int i = 0; i < numberOfCoords; ++i) {
					DataPointCoordinates coords = boundaryCoords.elementAt(i);
					mBoundaryCoords.put(coords.getY(), coords.getZ());
				}
			}
		} catch (Exception e) {
//			mBoundaryCoords = new LinkedHashMap<Integer, Integer>();
//			mCalculatedBoundaryCoords = new LinkedHashMap<Integer, Integer>();
//			mBoundaryCoords.put(get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION).intValue(), 0);
//			mBoundaryCoords.put(get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION).intValue(), 0);
		} finally {
			try {
				bufferedFileReader.close();
			} catch (Exception e) {
			}
			
			calculateCrossSectionDimensions();
		}
	}
	
	/**
	 * Calculates the cross-sectional area based on the boundary definition or left- and right- bank positions
	 */
	public void calculateCrossSectionDimensions() {
		double waterDepth = get(BackEndAPI.DSC_KEY_WATER_DEPTH);
		Entry<Integer, Integer> lastCoords = null;
		double area = 0;
		double wettedPerimeter = 0;
		
		for (Entry<Integer, Integer> coords : getBoundaryCoords().entrySet()) {
			if (lastCoords != null) {
				double meanBedHeight = (lastCoords.getValue() + coords.getValue())/2;
				double trapezoidWidth = Math.abs(lastCoords.getKey() - coords.getKey());
			
				area += (waterDepth - meanBedHeight) * trapezoidWidth;
				
				double bedHeightChange = lastCoords.getValue() - coords.getValue();
				
				wettedPerimeter += Math.sqrt(Math.pow(trapezoidWidth, 2) + Math.pow(bedHeightChange, 2));
			} else {
				// Add left-bank side height
				wettedPerimeter += getSideHeight(waterDepth, coords.getValue());
			}
			
			lastCoords = coords;
		}	
		
		// Add right-bank side height
		wettedPerimeter += getSideHeight(waterDepth, lastCoords.getValue());
		
		double lengthScale = get(BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR);
		set(BackEndAPI.DSC_KEY_CROSS_SECTIONAL_AREA, area/(lengthScale * lengthScale));
		set(BackEndAPI.DSC_KEY_WETTED_PERIMETER, wettedPerimeter/lengthScale);
	}
	
	private double getSideHeight(double waterDepth, double bedHeight) {
		return waterDepth > bedHeight ? waterDepth - bedHeight : 0d;
	}
	
	public int getBoundaryZAt(int yCoord) {
		LinkedHashMap<Integer, Integer> boundaryCoords = getBoundaryCoords();
		
		Integer zCoord = boundaryCoords.get(yCoord);
		
		if (zCoord == null) {
			if (mCalculatedBoundaryCoords == null) {
				mCalculatedBoundaryCoords = new LinkedHashMap<Integer, Integer>();
			} else {
				zCoord = mCalculatedBoundaryCoords.get(yCoord);
			}
		}
		
		if (zCoord != null) {
			return zCoord;
		}
		
		Entry<Integer, Integer> lastCoords = null;
		for (Entry<Integer, Integer> coords : boundaryCoords.entrySet()) {
			if (lastCoords != null) {
				if (lastCoords.getKey() < yCoord && coords.getKey() > yCoord) {
					zCoord = MAJFCMaths.interpolate(yCoord, lastCoords.getKey(), coords.getKey(), lastCoords.getValue(), coords.getValue());
					mCalculatedBoundaryCoords.put(yCoord, zCoord);
					
					return zCoord;
				}
			}
			
			lastCoords = coords;
		}
		
		return 0;
	}
	
	public void set(String xmlTag, String value) {
		DataSetConfigIndex index = getDSCIndexForXML(xmlTag);
		
		if (index == null) {
			DataSetConfigStringItemIndex siIndex = getDSCStringItemIndexForXML(xmlTag);
			
			if (siIndex == null) {
				MAJFCLogger.log("Unrecognised config xml: " + xmlTag);
			} else {
				if (siIndex.equals(BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER) && value.equals(DADefinitions.XML_TAB_ALIAS)) {
					value = "\t";
				}
				
				mStringConfigItems.set(siIndex, value);
			}
		} else {
			set(index, Double.parseDouble(value));
		}
	}
	
	private DataSetConfigIndex getDSCIndexForXML(String xmlTag) {
		if (sConfigXMLToIndexLookup == null) {
			prepareConfigXMLToIndexLookup();
		}
		
		DataSetConfigIndex index = sConfigXMLToIndexLookup.get(xmlTag);
		
		if (index == null) {
			if (sOldConfigXMLToIndexLookup == null) {
				prepareOldConfigXMLToIndexLookup();
			}
			
			index = sOldConfigXMLToIndexLookup.get(xmlTag);
		}
		
		return index;
	}
	
	private DataSetConfigStringItemIndex getDSCStringItemIndexForXML(String xmlTag) {
		if (sConfigXMLToStringItemIndexLookup == null) {
			prepareConfigXMLToStringItemIndexLookup();
		}
		
		return sConfigXMLToStringItemIndexLookup.get(xmlTag);
	}
	
	public void set(DataSetConfigStringItemIndex index, String value) {
		mStringConfigItems.set(index, value);
	}
	
	/**
	 * Gets a list of the keys for the string items in this summary (note, this will only return keys for values which
	 * have been set using set(...) 
	 */
	public Vector<DataSetConfigStringItemIndex> getStringItemKeys() {
		return mStringConfigItems.getKeys();
	}
	
	/**
	 * Gets the data set config string item array element specified
	 * @param index The element index
	 * @return The object at the specified index
	 */
	public String get(DataSetConfigStringItemIndex index) {
		return mStringConfigItems.get(index);
	}	
	
	private void prepareConfigXMLToIndexLookup() {
		sConfigXMLToIndexLookup = new Hashtable<String, BackEndAPI.DataSetConfigIndex>(100);
		
		sConfigXMLToIndexLookup.put(DADefinitions.XML_PRE_FILTER_TYPE, BackEndAPI.DSC_KEY_PRE_FILTER_TYPE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_DESPIKING_FILTER_TYPE, BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_EXCLUDE_LEVEL, BackEndAPI.DSC_KEY_EXCLUDE_LEVEL);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_MODIFIED_PST_AUTO_SAFE_LEVEL_C1, BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2, BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_PST_REPLACEMENT_METHOD, BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_PST_REPLACEMENT_POLYNOMIAL_ORDER, BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_CS_TYPE, BackEndAPI.DSC_KEY_CS_TYPE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_SAMPLING_RATE, BackEndAPI.DSC_KEY_SAMPLING_RATE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_LIMITING_CORRELATION, BackEndAPI.DSC_KEY_LIMITING_CORRELATION);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_LIMITING_SNR, BackEndAPI.DSC_KEY_LIMITING_SNR);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER, BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_LIMITING_W_DIFF, BackEndAPI.DSC_KEY_LIMITING_W_DIFF);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_MOVING_AVERAGE_WINDOW_SIZE, BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_DATA_SET_LENGTH_SCALE_FACTOR, BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_DATA_FILE_VELOCITY_SCALE_FACTOR, BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_FLUID_DENSITY, BackEndAPI.DSC_KEY_FLUID_DENSITY);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_FLUID_KINEMATIC_VISCOSITY, BackEndAPI.DSC_KEY_FLUID_KINEMATIC_VISCOSITY);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_LEFT_BANK_POSITION, BackEndAPI.DSC_KEY_LEFT_BANK_POSITION);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_RIGHT_BANK_POSITION, BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_WATER_DEPTH, BackEndAPI.DSC_KEY_WATER_DEPTH);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_MEASURED_DISCHARGE, BackEndAPI.DSC_KEY_MEASURED_DISCHARGE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_BED_SLOPE, BackEndAPI.DSC_KEY_BED_SLOPE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_STANDARD_CELL_WIDTH, BackEndAPI.DSC_KEY_DEFAULT_CELL_WIDTH);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_STANDARD_CELL_HEIGHT, BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_SYNCH_LIMITING_VALUE, BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_SYNCH_LIMITING_VALUE_DIRECTION, BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_SYNCH_IGNORE_FIRST_X_SECONDS, BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_AT_TRIM_START_BY, BackEndAPI.DSC_KEY_AT_TRIM_START_BY);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_AT_PRIOR_LENGTH, BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_AT_SAMPLE_LENGTH, BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_LARGE_FILE_MEAS_PER_SPLIT, BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_MAIN_PROBE_INDEX, BackEndAPI.DSC_KEY_MAIN_PROBE_INDEX);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_FIXED_PROBE_INDEX, BackEndAPI.DSC_KEY_FIXED_PROBE_INDEX);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_SYNCH_PROBE_INDEX, BackEndAPI.DSC_KEY_SYNCH_PROBE_INDEX);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_USE_BINARY_FILE_FORMAT, BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_PSD_TYPE, BackEndAPI.DSC_KEY_PSD_TYPE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_PSD_WINDOW, BackEndAPI.DSC_KEY_PSD_WINDOW);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_NUMBER_OF_BARTLETT_WINDOWS, BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_PSD_WELCH_WINDOW_OVERLAP, BackEndAPI.DSC_KEY_PSD_WELCH_WINDOW_OVERLAP);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_WAVELET_TYPE, BackEndAPI.DSC_KEY_WAVELET_TYPE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_WAVELET_TRANSFORM_TYPE, BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_WAVELET_TRANSFORM_SCALE_BY_INST_POWER, BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_NUMBER_OF_PROBES, BackEndAPI.DSC_KEY_NUMBER_OF_PROBES_IN_DATA_SET);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_INVERT_X_AXIS, BackEndAPI.DSC_KEY_X_AXIS_INVERTED);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_INVERT_Y_AXIS, BackEndAPI.DSC_KEY_Y_AXIS_INVERTED);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_INVERT_Z_AXIS, BackEndAPI.DSC_KEY_Z_AXIS_INVERTED);
		sConfigXMLToIndexLookup.put(DADefinitions.XML_DATA_SET_LOCKED, BackEndAPI.DSC_KEY_DATA_SET_LOCKED);
	}

	private void prepareConfigXMLToStringItemIndexLookup() {
		sConfigXMLToStringItemIndexLookup = new Hashtable<String, BackEndAPI.DataSetConfigStringItemIndex>(100);

		// String items
		sConfigXMLToStringItemIndexLookup.put(DADefinitions.XML_DEFAULT_DATA_FILE_PATH, BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_VSA_DATA_FILE_DIRECTORY);
		sConfigXMLToStringItemIndexLookup.put(DADefinitions.XML_DEFAULT_CSV_FILE_PATH, BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_RAW_DATA_FILE_DIRECTORY);
		sConfigXMLToStringItemIndexLookup.put(DADefinitions.XML_DEFAULT_MATLAB_EXPORT_FILE_PATH, BackEndAPI.DSC_KEY_FOR_STRING_ITEM_DEFAULT_FILE_EXPORT_DIRECTORY);
		sConfigXMLToStringItemIndexLookup.put(DADefinitions.XML_BOUNDARY_DEFINITION_FILENAME, BackEndAPI.DSC_KEY_FOR_STRING_ITEM_BOUNDARY_DEFINITION_FILENAME);
		sConfigXMLToStringItemIndexLookup.put(DADefinitions.XML_CSV_FILE_FORMAT, BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_FORMAT);
		sConfigXMLToStringItemIndexLookup.put(DADefinitions.XML_DATA_FILE_DELIMITER, BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DELIMITER);
		sConfigXMLToStringItemIndexLookup.put(DADefinitions.XML_CSV_FILE_DECIMAL_SEPARATOR, BackEndAPI.DSC_KEY_FOR_STRING_ITEM_CSV_FILE_DECIMAL_SEPARATOR);
	}

	private void prepareOldConfigXMLToIndexLookup() {
		sOldConfigXMLToIndexLookup = new Hashtable<String, BackEndAPI.DataSetConfigIndex>(100);
		
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_PRE_FILTER_TYPE_XML_TAG, BackEndAPI.DSC_KEY_PRE_FILTER_TYPE);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_DESPIKING_FILTER_TYPE_XML_TAG, BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_EXCLUDE_LEVEL_XML_TAG, BackEndAPI.DSC_KEY_EXCLUDE_LEVEL);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_MODIFIED_PST_AUTO_SAFE_LEVEL_C1_XML_TAG, BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2_XML_TAG, BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_PST_REPLACEMENT_METHOD_XML_TAG, BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_CS_TYPE_XML_TAG, BackEndAPI.DSC_KEY_CS_TYPE);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_SAMPLING_RATE_XML_TAG, BackEndAPI.DSC_KEY_SAMPLING_RATE);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_LIMITING_CORRELATION_XML_TAG, BackEndAPI.DSC_KEY_LIMITING_CORRELATION);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_LIMITING_SNR_XML_TAG, BackEndAPI.DSC_KEY_LIMITING_SNR);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER_XML_TAG, BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_LIMITING_W_DIFF_XML_TAG, BackEndAPI.DSC_KEY_LIMITING_W_DIFF);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_DATA_SET_LENGTH_UNIT_SCALE_FACTOR_XML_TAG, BackEndAPI.DSC_KEY_LENGTH_UNIT_SCALE_FACTOR);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_DATA_FILE_VELOCITY_UNIT_SCALE_FACTOR_XML_TAG, BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_LEFT_BANK_POSITION_XML_TAG, BackEndAPI.DSC_KEY_LEFT_BANK_POSITION);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_RIGHT_BANK_POSITION_XML_TAG, BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_WATER_DEPTH_XML_TAG, BackEndAPI.DSC_KEY_WATER_DEPTH);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_MEASURED_DISCHARGE_XML_TAG, BackEndAPI.DSC_KEY_MEASURED_DISCHARGE);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_BED_SLOPE_XML_TAG, BackEndAPI.DSC_KEY_BED_SLOPE);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_DEFAULT_CELL_WIDTH_XML_TAG, BackEndAPI.DSC_KEY_DEFAULT_CELL_WIDTH);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_DEFAULT_CELL_HEIGHT_XML_TAG, BackEndAPI.DSC_KEY_DEFAULT_CELL_HEIGHT);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_MRS_LIMITING_VALUE_XML_TAG, BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_MRS_LIMITING_VALUE_DIRECTION_XML_TAG, BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_LARGE_FILE_MEAS_PER_SPLIT_XML_TAG, BackEndAPI.DSC_KEY_LARGE_FILE_MEAS_PER_SPLIT);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_FIXED_PROBE_INDEX_XML_TAG, BackEndAPI.DSC_KEY_FIXED_PROBE_INDEX);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_USE_BINARY_FILE_FORMAT, BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT);
		sOldConfigXMLToIndexLookup.put(DADefinitions.CONFIG_NUMBER_OF_BARTLETT_WINDOWS, BackEndAPI.DSC_KEY_NUMBER_OF_BARTLETT_WINDOWS);
	}

	/**
	 * Handles the events from the XML parser
	 * 
	 * @author MAJ727
	 */
	public static class DataSetConfigDocHandler extends MyAbstractDocHandler {
		private LinkedList<DataPointCoordinates> mCoordsList;
		private final DataSetConfig mConfigData;
		
		public DataSetConfigDocHandler(DataSetConfig configData) {
			mConfigData = configData;
		}
		
		@Override
		public void startElement(String elem, Hashtable<String, String> h) {
			if (elem.equals(DADefinitions.CONFIG_PROBE_SETUP_XML_TAG)) {
				mCoordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
			} else if (elem.equals(DADefinitions.CONFIG_PROBE_SETUP_COORDS_XML_TAG)) {
				DataPointCoordinates coords = new DataPointCoordinates(Integer.parseInt(h.get(DADefinitions.XML_Y_COORD)), Integer.parseInt(h.get(DADefinitions.XML_Z_COORD)));
				mCoordsList.add(coords);
			}
			
			super.startElement(elem, h);
		}
		
		@Override
		public void endElement(String elem) throws BackEndAPIException {
			if (elem.equals(DADefinitions.CONFIG_PROBE_SETUP_XML_TAG)) {
				mConfigData.setCoords(mCoordsList);
			}

			super.endElement(elem);
		}
		
		/**
		 * Element value found
		 * @param value The value for the element currently held in mStartElement
		 */
		@Override
		public void elementValue(String value) {
			// Values read from file are in "English" ('.' decimal separator) irrespective of locale
			if (mStartElement == null) {
				return;
			}

			mConfigData.set(mStartElement, value);
	  	}
		
		@Override
		public void endDocument() {
			// Do this here so that bank positions have been set if required (if there is no boundary definition file)
			mConfigData.loadBoundaryCoords();
			
			super.endDocument();
		}
	}
}

