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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.text.ParseException;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.jmatio.io.MatFileReaderMAJ;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetailIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CharacteristicScalerType;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetail;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointDetailIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummary;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.PSTReplacementMethod;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.ProbeDetail;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.backend.GenericDataPointImporter.GenericImportDetails;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.DataAnalyser;
import com.mikejesson.vsa.widgits.CoordinateIndexedObject;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.MyAbstractDocHandler;

import qdxml.DocHandler;
import qdxml.QDParser;

/**
 * @author MAJ727
 *
 */
public class DataPoint implements CoordinateIndexedObject {
	private final int mYCoord;
	private final int mZCoord;
	private int mFileFormatVersion = 1;
	// x' = x * cos(theta) * cos(alpha) - y * sin(alpha) * cos(theta) + z * sin(theta) * cos(alpha)
	// y' = x * sin(alpha) * cos(phi) + y * cos(alpha) * cos(phi) - z * sin(phi) * cos(alpha)
	// z' = -x * cos(theta) * cos(phi) + y * cos(theta) * sin(phi) + z * cos(theta) * cos(phi)
		
	private Hashtable<String, DataPointDetailIndex> mComponentIndexLookup = new Hashtable<String, DataPointDetailIndex>();

	protected final DataSet mDataSet;
	protected Object mDataLock = new Object();
	
	protected DataPointSummary mSummary = new DataPointSummary();
	protected DataPointDetail mDetail;
	private ProbeDetail mProbeDetail = new ProbeDetail();
	
	private final static int MRS_LIMITING_VALUE_DIRECTION_RISING = 0;
//	private final static int MRS_LIMITING_VALUE_DIRECTION_FALLING = 1;
	private boolean mSynchParentIsFixedProbe = false;
	private DataPointCoordinates mSynchParentCoords = null;
	private int mPreTrimSynchIndex = -1;

	private boolean mIsSaved;
	
	private static final String[] DP_ESSENTIAL_FIELDS = {	
		DADefinitions.XML_Y_COORD,
		DADefinitions.XML_Z_COORD,
		DADefinitions.XML_THETA,
		DADefinitions.XML_PHI,
		DADefinitions.XML_ALPHA};
	
	private static final String[] DP_COMPONENT_FIELDS = {
		DADefinitions.XML_MEAN,
		DADefinitions.XML_ST_DEV,
		DADefinitions.XML_FTRC_MEAN,
		DADefinitions.XML_FTRC_ST_DEV,
		DADefinitions.XML_PERCENTAGE_OF_VELOCITIES_GOOD};

	/**
	 * Constructor used when the data point is for an import from an external file
	 * 
	 * @param yCoord The y-coordinate of this data point
	 * @param zCoord The z-coordinate of this data point
	 * @param theta The probe rotation in the xz plane for this measurement
	 * @param phi The probe rotation in the yz plane for this measurement
	 * @param alpha The probe rotation in the xy plane for this measurement
	 * @param dataSet The data set to which this point belongs
	 */
	public DataPoint(int yCoord, int zCoord, GenericImportDetails importDetails, int probeIndex, DataSet dataSet){
		mIsSaved = false;
				
		mYCoord = yCoord;
		mZCoord = zCoord;
		
		mSummary.set(BackEndAPI.DPS_KEY_XZ_PLANE_ROTATION_THETA, importDetails == null ? 0 : importDetails.mTheta);
		mSummary.set(BackEndAPI.DPS_KEY_YZ_PLANE_ROTATION_PHI, importDetails == null ? 0 : importDetails.mPhi);
		mSummary.set(BackEndAPI.DPS_KEY_XY_PLANE_ROTATION_ALPHA, importDetails == null ? 0 : importDetails.mAlpha);
		
		if (importDetails != null && importDetails.mSynchParentProbeIndex != -1 && importDetails.mSynchParentProbeIndex != probeIndex) {
			// If the synch/trim parent is a fixed probe, then it will be labelled with the main probe co-ordinates
			mSynchParentIsFixedProbe = importDetails.mSynchParentProbeIndex == importDetails.mFixedProbeIndex;
			mSynchParentCoords = mSynchParentIsFixedProbe ? importDetails.mCoordsList.get(importDetails.mMainProbeIndex) : importDetails.mCoordsList.get(importDetails.mSynchParentProbeIndex);
		}
		
		mDataSet = dataSet;

		setAllSeparatelyCalculableSummaryFieldsMissing();

		initialise();
		initialiseSummary();
	}
	
	/**
	 * Initialise the summary fields that must be set for a new data point
	 */
	private void initialiseSummary() {
		setDataPointSummaryField(BackEndAPI.DPS_KEY_BATCH_NUMBER, "-1");
		setDataPointSummaryField(BackEndAPI.DPS_KEY_BATCH_THETA_ROTATION_CORRECTION, "0");
		setDataPointSummaryField(BackEndAPI.DPS_KEY_BATCH_ALPHA_ROTATION_CORRECTION, "0");
		setDataPointSummaryField(BackEndAPI.DPS_KEY_BATCH_PHI_ROTATION_CORRECTION, "0");
	}

	/**
	 * Constructor used when the data point is for a point from a .majdp file
	 * 
	 * @param the parameters from this point in a hashtable
	 */
	public DataPoint(Hashtable<String, String> parameters, DataSet dataSet) throws BackEndAPIException{
		mIsSaved = true;
		
		for (int i = 0; i < DP_ESSENTIAL_FIELDS.length; ++i){
			if (!parameters.containsKey(DataPoint.DP_ESSENTIAL_FIELDS[i])){
				MAJFCLogger.log("Incomplete data point - ignoring.");
			    throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_FROM_FILE_DATA_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_FROM_FILE_DATA_ERROR_MSG));
			}
	 	}
	  
		mDataSet = dataSet;

		String yCoord = (String) parameters.get(DADefinitions.XML_Y_COORD);
		String zCoord = (String) parameters.get(DADefinitions.XML_Z_COORD);
		String theta = (String) parameters.get(DADefinitions.XML_THETA);
		String phi = (String) parameters.get(DADefinitions.XML_PHI);
		String alpha = (String) parameters.get(DADefinitions.XML_ALPHA);

		int theYCoord = -1, theZCoord = -1;
		
		try {
			theYCoord = MAJFCTools.parseInt(yCoord);
			theZCoord = MAJFCTools.parseInt(zCoord);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		mYCoord = theYCoord;
		mZCoord = theZCoord;
		
		mSummary.set(BackEndAPI.DPS_KEY_XZ_PLANE_ROTATION_THETA, MAJFCTools.parseDouble(theta));
		mSummary.set(BackEndAPI.DPS_KEY_YZ_PLANE_ROTATION_PHI, MAJFCTools.parseDouble(phi));
		mSummary.set(BackEndAPI.DPS_KEY_XY_PLANE_ROTATION_ALPHA, MAJFCTools.parseDouble(alpha));
	
		String uPrimevPrimeMean = (String) parameters.get(DADefinitions.XML_U_PRIME_V_PRIME_MEAN);
		String uPrimewPrimeMean = (String) parameters.get(DADefinitions.XML_U_PRIME_W_PRIME_MEAN);
		String vPrimewPrimeMean = (String) parameters.get(DADefinitions.XML_V_PRIME_W_PRIME_MEAN);
		String uwQ1ShearStress = (String) parameters.get(DADefinitions.XML_UW_QUADRANT_1_SHEAR_STRESS);
		String uwQ2ShearStress = (String) parameters.get(DADefinitions.XML_UW_QUADRANT_2_SHEAR_STRESS);
		String uwQ3ShearStress = (String) parameters.get(DADefinitions.XML_UW_QUADRANT_3_SHEAR_STRESS);
		String uwQ4ShearStress = (String) parameters.get(DADefinitions.XML_UW_QUADRANT_4_SHEAR_STRESS);
		String uwQ1ToQ3EventsRatio = (String) parameters.get(DADefinitions.XML_QH_UW_Q2_TO_Q4_EVENTS_RATIO);
		String uwQ2ToQ4EventsRatio = (String) parameters.get(DADefinitions.XML_QH_UW_Q2_TO_Q4_EVENTS_RATIO);
		String uwQ2AndQ4ToQ1AndQ3EventsRatio = (String) parameters.get(DADefinitions.XML_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO);
		String uvQ1ShearStress = (String) parameters.get(DADefinitions.XML_UV_QUADRANT_1_SHEAR_STRESS);
		String uvQ2ShearStress = (String) parameters.get(DADefinitions.XML_UV_QUADRANT_2_SHEAR_STRESS);
		String uvQ3ShearStress = (String) parameters.get(DADefinitions.XML_UV_QUADRANT_3_SHEAR_STRESS);
		String uvQ4ShearStress = (String) parameters.get(DADefinitions.XML_UV_QUADRANT_4_SHEAR_STRESS);
		String uvQ1ToQ3EventsRatio = (String) parameters.get(DADefinitions.XML_QH_UV_Q2_TO_Q4_EVENTS_RATIO);
		String uvQ2ToQ4EventsRatio = (String) parameters.get(DADefinitions.XML_QH_UV_Q2_TO_Q4_EVENTS_RATIO);
		String uvQ2AndQ4ToQ1AndQ3EventsRatio = (String) parameters.get(DADefinitions.XML_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO);
		String uTurbulenceIntensity = (String) parameters.get(DADefinitions.XML_U_TURBULENCE_INTENSITY);
		String vTurbulenceIntensity = (String) parameters.get(DADefinitions.XML_V_TURBULENCE_INTENSITY);
		String wTurbulenceIntensity = (String) parameters.get(DADefinitions.XML_W_TURBULENCE_INTENSITY);
		String[][][] thirdOrderCorrelations = new String[BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION][BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION][BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION];
		for (int uPrimePower = 0; uPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++uPrimePower) {
			for (int vPrimePower = 0; vPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++vPrimePower) {
				for (int wPrimePower = 0; wPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++wPrimePower) {
					thirdOrderCorrelations[uPrimePower][vPrimePower][wPrimePower] = (String) parameters.get(DADefinitions.XML_THIRD_ORDER_CORRELATION[uPrimePower][vPrimePower][wPrimePower]);
				}
			}
		}		
		String[][] anisotropicStressTensor = new String[3][3];
		for (int row = 0; row < anisotropicStressTensor.length; ++row) {
			for (int column = 0; column < anisotropicStressTensor[0].length; ++column) {
				anisotropicStressTensor[row][column] = (String) parameters.get(DADefinitions.XML_ANISOTROPIC_STRESS_TENSOR[row][column]);
			}
		}		
		String tke = (String) parameters.get(DADefinitions.XML_TKE);
		String uTKEFlux = (String) parameters.get(DADefinitions.XML_U_TKE_FLUX);
		String vTKEFlux = (String) parameters.get(DADefinitions.XML_V_TKE_FLUX);
		String wTKEFlux = (String) parameters.get(DADefinitions.XML_W_TKE_FLUX);
		String fixedProbeUCorrelation = (String) parameters.get(DADefinitions.XML_FIXED_PROBE_U_CORRELATION);
		String fixedProbeVCorrelation = (String) parameters.get(DADefinitions.XML_FIXED_PROBE_V_CORRELATION);
		String fixedProbeWCorrelation = (String) parameters.get(DADefinitions.XML_FIXED_PROBE_W_CORRELATION);
		String batchNumber = (String) parameters.get(DADefinitions.XML_BATCH_NUMBER);
		String batchThetaRotationCorrection = "0";
		String batchAlphaRotationCorrection = "0";
		String batchPhiRotationCorrection = "0";
		
		if (batchNumber == null) {
			batchNumber = "-1";
		} else {
			batchThetaRotationCorrection = (String) parameters.get(DADefinitions.XML_BATCH_THETA_ROTATION_CORRECTION);
			batchAlphaRotationCorrection = (String) parameters.get(DADefinitions.XML_BATCH_ALPHA_ROTATION_CORRECTION);
			batchPhiRotationCorrection = (String) parameters.get(DADefinitions.XML_BATCH_PHI_ROTATION_CORRECTION);
		}		
		
		String maximumU = (String) parameters.get(DADefinitions.XML_MAX_U);
		String maximumV = (String) parameters.get(DADefinitions.XML_MAX_V);
		String maximumW = (String) parameters.get(DADefinitions.XML_MAX_W);
		
		String xMeanCorrelation = (String) parameters.get(DADefinitions.XML_X_MEAN_CORRELATION);
		String xMeanSNR = (String) parameters.get(DADefinitions.XML_X_MEAN_SNR);
		String yMeanCorrelation = (String) parameters.get(DADefinitions.XML_Y_MEAN_CORRELATION);
		String yMeanSNR = (String) parameters.get(DADefinitions.XML_Y_MEAN_SNR);
		String zMeanCorrelation = (String) parameters.get(DADefinitions.XML_Z_MEAN_CORRELATION);
		String zMeanSNR = (String) parameters.get(DADefinitions.XML_Z_MEAN_SNR);
		
		// When reading from file, numbers are "English" ('.' decimal separator) irrespective of locale
		setSummaryDataField(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN, Double.parseDouble(uPrimevPrimeMean));
		setSummaryDataField(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN, Double.parseDouble(uPrimewPrimeMean));
		setSummaryDataField(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN, Double.parseDouble(vPrimewPrimeMean));
		setSummaryDataField(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS, Double.parseDouble(uwQ1ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS, Double.parseDouble(uwQ2ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS, Double.parseDouble(uwQ3ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS, Double.parseDouble(uwQ4ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO, Double.parseDouble(uwQ1ToQ3EventsRatio));
		setSummaryDataField(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO, Double.parseDouble(uwQ2ToQ4EventsRatio));
		setSummaryDataField(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, Double.parseDouble(uwQ2AndQ4ToQ1AndQ3EventsRatio));
		setSummaryDataField(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS, Double.parseDouble(uvQ1ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS, Double.parseDouble(uvQ2ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS, Double.parseDouble(uvQ3ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS, Double.parseDouble(uvQ4ShearStress));
		setSummaryDataField(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO, Double.parseDouble(uvQ1ToQ3EventsRatio));
		setSummaryDataField(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO, Double.parseDouble(uvQ2ToQ4EventsRatio));
		setSummaryDataField(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, Double.parseDouble(uvQ2AndQ4ToQ1AndQ3EventsRatio));
		setSummaryDataField(BackEndAPI.DPS_KEY_RMS_U_PRIME, Double.parseDouble(uTurbulenceIntensity));
		setSummaryDataField(BackEndAPI.DPS_KEY_RMS_V_PRIME, Double.parseDouble(vTurbulenceIntensity));
		setSummaryDataField(BackEndAPI.DPS_KEY_RMS_W_PRIME, Double.parseDouble(wTurbulenceIntensity));
		for (int uPrimePower = 0; uPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++uPrimePower) {
			for (int vPrimePower = 0; vPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++vPrimePower) {
				for (int wPrimePower = 0; wPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++wPrimePower) {
					setSummaryDataField(BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[uPrimePower][vPrimePower][wPrimePower], Double.parseDouble(thirdOrderCorrelations[uPrimePower][vPrimePower][wPrimePower]));
				}
			}
		}
		for (int row = 0; row < BackEndAPI.DPS_KEY_ANISOTROPIC_STRESS_TENSOR_ARRAY.length; ++row) {
			for (int column = 0; column < BackEndAPI.DPS_KEY_ANISOTROPIC_STRESS_TENSOR_ARRAY[0].length; ++column) {
				setSummaryDataField(BackEndAPI.DPS_KEY_ANISOTROPIC_STRESS_TENSOR_ARRAY[row][column], Double.parseDouble(anisotropicStressTensor[row][column]));
			}
		}
		setSummaryDataField(BackEndAPI.DPS_KEY_TKE, Double.parseDouble(tke));
		setSummaryDataField(BackEndAPI.DPS_KEY_U_TKE_FLUX, Double.parseDouble(uTKEFlux));
		setSummaryDataField(BackEndAPI.DPS_KEY_V_TKE_FLUX, Double.parseDouble(vTKEFlux));
		setSummaryDataField(BackEndAPI.DPS_KEY_W_TKE_FLUX, Double.parseDouble(wTKEFlux));
	
		setSummaryDataField(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION, Double.parseDouble(fixedProbeUCorrelation));
		setSummaryDataField(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION, Double.parseDouble(fixedProbeVCorrelation));
		setSummaryDataField(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION, Double.parseDouble(fixedProbeWCorrelation));
		
		setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_NUMBER, Double.parseDouble(batchNumber));
		setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_THETA_ROTATION_CORRECTION, Double.parseDouble(batchThetaRotationCorrection));
		setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_ALPHA_ROTATION_CORRECTION, Double.parseDouble(batchAlphaRotationCorrection));
		setSummaryDataField(BackEndAPI.DPS_KEY_BATCH_PHI_ROTATION_CORRECTION, Double.parseDouble(batchPhiRotationCorrection));

		setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_U, Double.parseDouble(maximumU));
		setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_V, Double.parseDouble(maximumV));
		setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_W, Double.parseDouble(maximumW));
	
		setSummaryDataField(BackEndAPI.DPS_KEY_X_MEAN_CORRELATION, Double.parseDouble(xMeanCorrelation));
		setSummaryDataField(BackEndAPI.DPS_KEY_X_MEAN_SNR, Double.parseDouble(xMeanSNR));
		setSummaryDataField(BackEndAPI.DPS_KEY_Y_MEAN_CORRELATION, Double.parseDouble(yMeanCorrelation));
		setSummaryDataField(BackEndAPI.DPS_KEY_Y_MEAN_SNR, Double.parseDouble(yMeanSNR));
		setSummaryDataField(BackEndAPI.DPS_KEY_Z_MEAN_CORRELATION, Double.parseDouble(zMeanCorrelation));
		setSummaryDataField(BackEndAPI.DPS_KEY_Z_MEAN_SNR, Double.parseDouble(zMeanSNR));

		initialise();
	}
	
	private void setAllSeparatelyCalculableSummaryFieldsMissing() {
		setDataPointSummaryField(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_RMS_U_PRIME, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_RMS_V_PRIME, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_RMS_W_PRIME, null);
		for (int uPrimePower = 0; uPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++uPrimePower) {
			for (int vPrimePower = 0; vPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++vPrimePower) {
				for (int wPrimePower = 0; wPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++wPrimePower) {
					setDataPointSummaryField(BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[uPrimePower][vPrimePower][wPrimePower], null);
				}
			}
		}
		for (int row = 0; row < DADefinitions.XML_ANISOTROPIC_STRESS_TENSOR.length; ++row) {
			for (int column = 0; column < DADefinitions.XML_ANISOTROPIC_STRESS_TENSOR[0].length; ++column) {
				setDataPointSummaryField(BackEndAPI.DPS_KEY_ANISOTROPIC_STRESS_TENSOR_ARRAY[row][column], null);
			}
		}
		setDataPointSummaryField(BackEndAPI.DPS_KEY_TKE, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_U_TKE_FLUX, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_V_TKE_FLUX, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_W_TKE_FLUX, null);
	
		setDataPointSummaryField(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION, null);
		setDataPointSummaryField(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION, null);

	}
	
	/**
	 * Initialisation stuff that is common between all constructors
	 */
	private void initialise() {
		mComponentIndexLookup.put(DADefinitions.XML_DATA_POINT_X_COMPONENT, BackEndAPI.DPD_KEY_RAW_X_VELOCITIES);
		mComponentIndexLookup.put(DADefinitions.XML_DATA_POINT_Y_COMPONENT, BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES);
		mComponentIndexLookup.put(DADefinitions.XML_DATA_POINT_Z_COMPONENT, BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES);
	}
	
	/**
	 * Sets a data point summary field the given value, and reports to the data set if the value is invalid (field has not been
	 * calculated)
	 * @param dpsIndex The index key for the field to be set
	 * @param value The value to be set
	 */
	private void setDataPointSummaryField(DataPointSummaryIndex dpsIndex, String value) {
		if (value == null || value.equals(MAJFCTools.stringValueOf(Double.NaN))) {
			mDataSet.setMissingDataPointSummaryFieldKey(dpsIndex);
			return;
		}
		
		mSummary.set(dpsIndex, MAJFCTools.parseDouble(value));
	}

	@Override
	/**
	 * Equality test - checks x and y coordinates
	 */
	public boolean equals(Object theOtherOne){
		if (this == theOtherOne) {
			return true;
		}
		
		if (theOtherOne == null) {
			return false;
		}
		
		if (this.getClass() != theOtherOne.getClass()) {
			return false;
		}
		
		DataPoint otherOne = (DataPoint) theOtherOne;
		if ((otherOne.getYCoord() == mYCoord) && (otherOne.getZCoord() == mZCoord)){
			return true;
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 15 * mYCoord;
		hash = 15 * hash + mZCoord;
		
		return hash;
	}
	
	/**
	 * Adds the velocity component with the given index and parameters
	 * @param velocityComponent
	 * @param parameters
	 */
	public void addVelocityComponentFromDataSetSummaryXML(String componentName, Hashtable<String, String> parameters) throws BackEndAPIException{
		for (int i = 0; i < DP_COMPONENT_FIELDS.length; ++i){
			if (!parameters.containsKey(DP_COMPONENT_FIELDS[i])){
				MAJFCLogger.log("Incomplete data point component - ignoring.");
				throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_FROM_FILE_DATA_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_FROM_FILE_DATA_ERROR_MSG));
			}
		 }
		  
		String mean = (String)parameters.get(DADefinitions.XML_MEAN);
		String stdev = (String)parameters.get(DADefinitions.XML_ST_DEV);
		String ftMean = (String)parameters.get(DADefinitions.XML_FT_MEAN);
		String ftStdev = (String)parameters.get(DADefinitions.XML_FT_ST_DEV);
		String ftRCMean = (String)parameters.get(DADefinitions.XML_FTRC_MEAN);
		String ftRCStdev = (String)parameters.get(DADefinitions.XML_FTRC_ST_DEV);
		String percentageGood = (String)parameters.get(DADefinitions.XML_PERCENTAGE_OF_VELOCITIES_GOOD);
		  
		// When reading from file, numbers are "English" ('.' decimal separator) irrespective of locale
		double meanVelocity = Double.parseDouble(mean);
		double standardDeviation = Double.parseDouble(stdev);
		double ftMeanVelocity = Double.parseDouble(ftMean);
		double ftStandardDeviation = Double.parseDouble(ftStdev);
		double ftRCMeanVelocity = Double.parseDouble(ftRCMean);
		double ftRCStandardDeviation = Double.parseDouble(ftRCStdev);
		double percentGood = Double.parseDouble(percentageGood);

		if (componentName.equals(DADefinitions.XML_DATA_POINT_X_COMPONENT)) {
			String synchIndex = (String)parameters.get(DADefinitions.XML_DATA_POINT_MULTI_RUN_SYNCH_INDEX);
			
			if (synchIndex == null) {
				setSummaryDataField(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX, 0d);
			} else {
				setSummaryDataField(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX, Double.parseDouble(synchIndex));
			}
			setSummaryDataField(BackEndAPI.DPS_KEY_U_MEAN_TRANSLATED_VELOCITY, meanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_U_TRANSLATED_ST_DEV, standardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_VELOCITY, ftMeanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_ST_DEV, ftStandardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, ftRCMeanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV, ftRCStandardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_PERCENTAGE_OF_U_VELOCITIES_GOOD, percentGood);
		} else if (componentName.equals(DADefinitions.XML_DATA_POINT_Y_COMPONENT)) {
			setSummaryDataField(BackEndAPI.DPS_KEY_V_MEAN_TRANSLATED_VELOCITY, meanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_V_TRANSLATED_ST_DEV, standardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_VELOCITY, ftMeanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_ST_DEV, ftStandardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, ftRCMeanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV, ftRCStandardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_PERCENTAGE_OF_V_VELOCITIES_GOOD, percentGood);
		} else if (componentName.equals(DADefinitions.XML_DATA_POINT_Z_COMPONENT)) {
			setSummaryDataField(BackEndAPI.DPS_KEY_W_MEAN_TRANSLATED_VELOCITY, meanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_W_TRANSLATED_ST_DEV, standardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_VELOCITY, ftMeanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_ST_DEV, ftStandardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, ftRCMeanVelocity);
			setSummaryDataField(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV, ftRCStandardDeviation);
			setSummaryDataField(BackEndAPI.DPS_KEY_PERCENTAGE_OF_W_VELOCITIES_GOOD, percentGood);
		}
	}		
		
	/**
	 * Gets a summary XML representation for use in the data set file
	 * @return Summary of the mean and standard deviation data in XML form
	 */
	public String getXMLSummaryRepresentation(){
		// <data_point ycoord="1" zcoord="2" xz_plane_probe_rotation="3" yz_plane_probe_rotation="4" xy_plane_probe_rotation="5"/>

		DAFileOutputStringBuffer theXML = new DAFileOutputStringBuffer();
		theXML.append(MAJFCTools.makeXMLStartTag(DADefinitions.XML_DATA_POINT, false));
		
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Y_COORD, mYCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Z_COORD, mZCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_THETA, mSummary.get(BackEndAPI.DPS_KEY_XZ_PLANE_ROTATION_THETA)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_PHI, mSummary.get(BackEndAPI.DPS_KEY_YZ_PLANE_ROTATION_PHI)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_ALPHA, mSummary.get(BackEndAPI.DPS_KEY_XY_PLANE_ROTATION_ALPHA)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_U_PRIME_V_PRIME_MEAN, mSummary.get(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_U_PRIME_W_PRIME_MEAN, mSummary.get(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_V_PRIME_W_PRIME_MEAN, mSummary.get(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UW_QUADRANT_1_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UW_QUADRANT_2_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UW_QUADRANT_3_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UW_QUADRANT_4_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_QH_UW_Q1_TO_Q3_EVENTS_RATIO, mSummary.get(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_QH_UW_Q2_TO_Q4_EVENTS_RATIO, mSummary.get(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, mSummary.get(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UV_QUADRANT_1_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UV_QUADRANT_2_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UV_QUADRANT_3_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_UV_QUADRANT_4_SHEAR_STRESS, mSummary.get(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_QH_UV_Q1_TO_Q3_EVENTS_RATIO, mSummary.get(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_QH_UV_Q2_TO_Q4_EVENTS_RATIO, mSummary.get(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, mSummary.get(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_U_TURBULENCE_INTENSITY, mSummary.get(BackEndAPI.DPS_KEY_RMS_U_PRIME)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_V_TURBULENCE_INTENSITY, mSummary.get(BackEndAPI.DPS_KEY_RMS_V_PRIME)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_W_TURBULENCE_INTENSITY, mSummary.get(BackEndAPI.DPS_KEY_RMS_W_PRIME)));
		
		for (int uPrimePower = 0; uPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++uPrimePower) {
			for (int vPrimePower = 0; vPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++vPrimePower) {
				for (int wPrimePower = 0; wPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++wPrimePower) {
					theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_THIRD_ORDER_CORRELATION[uPrimePower][vPrimePower][wPrimePower], mSummary.get(BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[uPrimePower][vPrimePower][wPrimePower])));
				}
			}
		}		

		for (int row = 0; row < DADefinitions.XML_ANISOTROPIC_STRESS_TENSOR.length; ++row) {
			for (int column = 0; column < DADefinitions.XML_ANISOTROPIC_STRESS_TENSOR[0].length; ++column) {
				theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_ANISOTROPIC_STRESS_TENSOR[row][column], mSummary.get(BackEndAPI.DPS_KEY_ANISOTROPIC_STRESS_TENSOR_ARRAY[row][column])));
			}
		}		

		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_TKE, mSummary.get(BackEndAPI.DPS_KEY_TKE)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_U_TKE_FLUX, mSummary.get(BackEndAPI.DPS_KEY_U_TKE_FLUX)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_V_TKE_FLUX, mSummary.get(BackEndAPI.DPS_KEY_V_TKE_FLUX)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_W_TKE_FLUX, mSummary.get(BackEndAPI.DPS_KEY_W_TKE_FLUX)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FIXED_PROBE_U_CORRELATION, mSummary.get(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FIXED_PROBE_V_CORRELATION, mSummary.get(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FIXED_PROBE_W_CORRELATION, mSummary.get(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_BATCH_NUMBER, mSummary.get(BackEndAPI.DPS_KEY_BATCH_NUMBER)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_BATCH_THETA_ROTATION_CORRECTION, mSummary.get(BackEndAPI.DPS_KEY_BATCH_THETA_ROTATION_CORRECTION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_BATCH_ALPHA_ROTATION_CORRECTION, mSummary.get(BackEndAPI.DPS_KEY_BATCH_ALPHA_ROTATION_CORRECTION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_BATCH_PHI_ROTATION_CORRECTION, mSummary.get(BackEndAPI.DPS_KEY_BATCH_PHI_ROTATION_CORRECTION)));

		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_MAX_U, mSummary.get(BackEndAPI.DPS_KEY_MAXIMUM_U)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_MAX_V, mSummary.get(BackEndAPI.DPS_KEY_MAXIMUM_V)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_MAX_W, mSummary.get(BackEndAPI.DPS_KEY_MAXIMUM_W)));
		
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_X_MEAN_CORRELATION, mSummary.get(BackEndAPI.DPS_KEY_X_MEAN_CORRELATION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_X_MEAN_SNR, mSummary.get(BackEndAPI.DPS_KEY_X_MEAN_SNR)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Y_MEAN_CORRELATION, mSummary.get(BackEndAPI.DPS_KEY_Y_MEAN_CORRELATION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Y_MEAN_SNR, mSummary.get(BackEndAPI.DPS_KEY_Y_MEAN_SNR)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Z_MEAN_CORRELATION, mSummary.get(BackEndAPI.DPS_KEY_Z_MEAN_CORRELATION)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Z_MEAN_SNR, mSummary.get(BackEndAPI.DPS_KEY_Z_MEAN_SNR)));

		theXML.append('>');
		
		theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
		
		synchronized (mDataLock) {
			theXML.append(getSummaryXMLRepresentation());
			theXML.append(getProbeDetailXML());
		}
		
		theXML.append(MAJFCTools.makeXMLEndTag(DADefinitions.XML_DATA_POINT, true));
		theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
		
		return theXML.toString();
	}
	
	/**
	 * Gets a summary XML representation for use in the data set file
	 * @return Summary of the mean and standard deviation data in XML form
	 */
	private String getSummaryXMLRepresentation(){
		DAFileOutputStringBuffer theXML = new DAFileOutputStringBuffer();
		
		theXML.append('<');
		theXML.append(DADefinitions.XML_DATA_POINT_X_COMPONENT);
		theXML.append(' ');
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Y_COORD, mYCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Z_COORD, mZCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_DATA_POINT_MULTI_RUN_SYNCH_INDEX, mSummary.get(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_MEAN, mSummary.get(BackEndAPI.DPS_KEY_U_MEAN_TRANSLATED_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_U_TRANSLATED_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FT_MEAN, mSummary.get(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FT_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FTRC_MEAN, mSummary.get(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FTRC_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_PERCENTAGE_OF_VELOCITIES_GOOD, mSummary.get(BackEndAPI.DPS_KEY_PERCENTAGE_OF_U_VELOCITIES_GOOD)));
		theXML.append("/>");
		theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
	
		theXML.append('<');
		theXML.append(DADefinitions.XML_DATA_POINT_Y_COMPONENT);
		theXML.append(' ');
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Y_COORD, mYCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Z_COORD, mZCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_MEAN, mSummary.get(BackEndAPI.DPS_KEY_V_MEAN_TRANSLATED_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_V_TRANSLATED_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FT_MEAN, mSummary.get(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FT_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FTRC_MEAN, mSummary.get(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FTRC_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_PERCENTAGE_OF_VELOCITIES_GOOD, mSummary.get(BackEndAPI.DPS_KEY_PERCENTAGE_OF_V_VELOCITIES_GOOD)));
		theXML.append("/>");
		theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
		
		theXML.append('<');
		theXML.append(DADefinitions.XML_DATA_POINT_Z_COMPONENT);
		theXML.append(' ');
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Y_COORD, mYCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_Z_COORD, mZCoord));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_MEAN, mSummary.get(BackEndAPI.DPS_KEY_W_MEAN_TRANSLATED_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_W_TRANSLATED_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FT_MEAN, mSummary.get(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FT_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FTRC_MEAN, mSummary.get(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_FTRC_ST_DEV, mSummary.get(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV)));
		theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_PERCENTAGE_OF_VELOCITIES_GOOD, mSummary.get(BackEndAPI.DPS_KEY_PERCENTAGE_OF_W_VELOCITIES_GOOD)));
		theXML.append("/>");
		theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);

		return theXML.toString();
	}	

	/**
	 * Saves the data point to a file
	 * 
	 * @param directory The directory to save the file in
	 * @throws BackEndAPIException 
	 */
	public void saveToFullFile(String directory, String tempDirectory) throws BackEndAPIException {
		String dataPointFilename = makeDataPointFilename(directory);
		File dataPointFile = new File(dataPointFilename);
		
		// If this file doesn't exist check for one in the temp directory and move it across
		if (mIsSaved == false) {
			File dataPointTempFile = new File(tempDirectory + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + getFilename());
			
			if (dataPointTempFile.exists() == false) {
				MAJFCLogger.log("No file or temp file for data point " + getFilename());
				return;
			}
			
			moveTempFile(dataPointTempFile, dataPointFile);
		}
	
		mIsSaved = true;
	}
	
	/**
	 * Moves a data point temporary file to the main data point data directory
	 * @param dataPointTempFile The temporary file to move
	 * @param dataPointFile The destination file
	 */
	private void moveTempFile(File dataPointTempFile, File dataPointFile) throws BackEndAPIException {
		try {
			MAJFCTools.moveFile(dataPointTempFile, dataPointFile);
		}
		catch (Exception theException) {
			throw new BackEndAPIException(DAStrings.getString(DAStrings.ERROR_MOVING_TEMP_FILE_TITLE), DAStrings.getString(DAStrings.ERROR_MOVING_TEMP_FILE_MSG) + dataPointTempFile.getAbsolutePath());
		}
	}

	/**
	 * Saves the data point to a file
	 * @param directory The directory to save the file in
	 */
	public void saveToTempFile(String directory) {
		File tempDataPointDir = new File(directory);
		
		if (tempDataPointDir.exists() == false) {
			tempDataPointDir.mkdirs();
		}

		String dataPointFilename = makeDataPointFilename(directory);
		
		if (useBinaryFileFormat()) {
			saveToBinaryFile(dataPointFilename);
		} else {
			saveToXMLFile(dataPointFilename);
		}
		
		mIsSaved = false;
	}
	
	private void saveToXMLFile(String dataPointFilename) {
		File dataPointFile = new File(dataPointFilename);
		
		try{
			FileWriter fileWriter = new FileWriter(dataPointFile);

			fileWriter.write(MAJFCTools.makeXMLStartTag(DADefinitions.XML_DATA_POINT, true));
	
			fileWriter.write(MAJFCTools.makeXMLNode(DADefinitions.XML_DATA_POINT_VERSION, DADefinitions.VERSION));
			
			writeXMLDetailRepresentation(fileWriter, BackEndAPI.DPD_KEY_RAW_X_VELOCITIES);
			writeXMLDetailRepresentation(fileWriter, BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES);
			writeXMLDetailRepresentation(fileWriter, BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES);
			
			writeXMLDetailRepresentation(fileWriter, BackEndAPI.DPD_KEY_PRESSURE);
		
			fileWriter.write(MAJFCTools.makeXMLEndTag(DADefinitions.XML_DATA_POINT, true));

			fileWriter.close();
		} catch (Exception theException) {
			theException.printStackTrace();
			MAJFCLogger.log("Problem writing temporary file " + dataPointFile.getAbsolutePath());
		}
	}
	
	public void saveToBinaryFile(String dataPointFilename) {
		try {
			Vector<Double> rawXVelocities = mDetail.get(BackEndAPI.DPD_KEY_RAW_X_VELOCITIES);
			Vector<Double> rawYVelocities = mDetail.get(BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES);
			Vector<Double> rawZVelocities = mDetail.get(BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES);
			Vector<Double> rawPressures = mDetail.get(BackEndAPI.DPD_KEY_PRESSURE);
			Vector<Double> translatedXVelocities = mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES);
			Vector<Double> translatedYVelocities = mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES);
			Vector<Double> translatedZVelocities = mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES);
			Vector<Double> ftXVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES);
			Vector<Double> ftYVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES);
			Vector<Double> ftZVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES);
			Vector<Double> ftrcXVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
			Vector<Double> ftrcYVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
			Vector<Double> ftrcZVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
			Vector<Double> xSNRs = mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS);
			Vector<Double> ySNRs = mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS);
			Vector<Double> zSNRs = mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS);
			Vector<Double> xCorrelations = mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS);
			Vector<Double> yCorrelations = mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS);
			Vector<Double> zCorrelations = mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS);
			Vector<Double> wDiffs = mDetail.get(BackEndAPI.DPD_KEY_W_DIFF);
			int numberOfRows = rawXVelocities.size();
			boolean hasPressures = rawPressures != null && rawPressures.size() > 0;
			
			MLDouble mlRawXVelocities = new MLDouble(DADefinitions.MATLAB_X_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlRawYVelocities = new MLDouble(DADefinitions.MATLAB_Y_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlRawZVelocities = new MLDouble(DADefinitions.MATLAB_Z_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlRawPressures = new MLDouble(DADefinitions.MATLAB_PRESSURES, new int[] {numberOfRows, 1});
			MLDouble mlTranslatedXVelocities = new MLDouble(DADefinitions.MATLAB_TRANSLATED_X_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlTranslatedYVelocities = new MLDouble(DADefinitions.MATLAB_TRANSLATED_Y_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlTranslatedZVelocities = new MLDouble(DADefinitions.MATLAB_TRANSLATED_Z_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlFTXVelocities = new MLDouble(DADefinitions.MATLAB_FT_X_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlFTYVelocities = new MLDouble(DADefinitions.MATLAB_FT_Y_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlFTZVelocities = new MLDouble(DADefinitions.MATLAB_FT_Z_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlFTRCXVelocities = new MLDouble(DADefinitions.MATLAB_FTRC_X_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlFTRCYVelocities = new MLDouble(DADefinitions.MATLAB_FTRC_Y_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlFTRCZVelocities = new MLDouble(DADefinitions.MATLAB_FTRC_Z_VELOCITIES, new int[] {numberOfRows, 1});
			MLDouble mlXSNRs = new MLDouble(DADefinitions.MATLAB_X_SNRS, new int[] {numberOfRows, 1});
			MLDouble mlYSNRs = new MLDouble(DADefinitions.MATLAB_Y_SNRS, new int[] {numberOfRows, 1});
			MLDouble mlZSNRs = new MLDouble(DADefinitions.MATLAB_Z_SNRS, new int[] {numberOfRows, 1});
			MLDouble mlXCorrelations = new MLDouble(DADefinitions.MATLAB_X_CORRELATIONS, new int[] {numberOfRows, 1});
			MLDouble mlYCorrelations = new MLDouble(DADefinitions.MATLAB_Y_CORRELATIONS, new int[] {numberOfRows, 1});
			MLDouble mlZCorrelations = new MLDouble(DADefinitions.MATLAB_Z_CORRELATIONS, new int[] {numberOfRows, 1});
			MLDouble mlWDiffs = new MLDouble(DADefinitions.MATLAB_W_DIFFS, new int[] {numberOfRows, 1});

			for (int valueIndex = 0; valueIndex < numberOfRows; ++valueIndex) {
				mlRawXVelocities.set(rawXVelocities.elementAt(valueIndex), valueIndex, 0);
				mlRawYVelocities.set(rawYVelocities.elementAt(valueIndex), valueIndex, 0);
				mlRawZVelocities.set(rawZVelocities.elementAt(valueIndex), valueIndex, 0);
					
				if (hasPressures) {
					mlRawPressures.set(rawPressures.elementAt(valueIndex), valueIndex, 0);
				}
		
				mlTranslatedXVelocities.set(translatedXVelocities.elementAt(valueIndex), valueIndex, 0);
				mlTranslatedYVelocities.set(translatedYVelocities.elementAt(valueIndex), valueIndex, 0);
				mlTranslatedZVelocities.set(translatedZVelocities.elementAt(valueIndex), valueIndex, 0);

				mlFTXVelocities.set(ftXVelocities.elementAt(valueIndex), valueIndex, 0);
				mlFTYVelocities.set(ftYVelocities.elementAt(valueIndex), valueIndex, 0);
				mlFTZVelocities.set(ftZVelocities.elementAt(valueIndex), valueIndex, 0);

				mlFTRCXVelocities.set(ftrcXVelocities.elementAt(valueIndex), valueIndex, 0);
				mlFTRCYVelocities.set(ftrcYVelocities.elementAt(valueIndex), valueIndex, 0);
				mlFTRCZVelocities.set(ftrcZVelocities.elementAt(valueIndex), valueIndex, 0);

				mlXSNRs.set(xSNRs.elementAt(valueIndex), valueIndex, 0);
				mlYSNRs.set(ySNRs.elementAt(valueIndex), valueIndex, 0);
				mlZSNRs.set(zSNRs.elementAt(valueIndex), valueIndex, 0);

				mlXCorrelations.set(xCorrelations.elementAt(valueIndex), valueIndex, 0);
				mlYCorrelations.set(yCorrelations.elementAt(valueIndex), valueIndex, 0);
				mlZCorrelations.set(zCorrelations.elementAt(valueIndex), valueIndex, 0);
				
				mlWDiffs.set(wDiffs.elementAt(valueIndex), valueIndex, 0);
			}

//			MLStructure dataArray = new MLStructure(DADefinitions.MATLAB_OUTPUT_DATA_ARRAY, new int[] {2,1});//{4 * numberOfTimeSeries, 1});
//			dataArray.setField(DADefinitions.MATLAB_OUTPUT_X_VELOCITIES, xVelocities);
//			dataArray.setField(DADefinitions.MATLAB_OUTPUT_Y_VELOCITIES, yVelocities);
//			dataArray.setField(DADefinitions.MATLAB_OUTPUT_Z_VELOCITIES, zVelocities);
//			dataArray.setField(DADefinitions.MATLAB_OUTPUT_PRESSURES, pressures);
			
			StringTokenizer st = new StringTokenizer(DADefinitions.VERSION, ".");
			int numberOfVersionLevels = st.countTokens();
			MLDouble mlFileFormatVersion = new MLDouble(DADefinitions.MATLAB_FILE_FORMAT_VERSION, new int[] {numberOfVersionLevels, 1});
			
			for (int i = 0; i < numberOfVersionLevels; ++i) {
				mlFileFormatVersion.set(Double.parseDouble(st.nextToken()), i, 0);
			}
			
			Collection<MLArray> all = new Vector<MLArray>(2);
//			all.add(configArray);
//			all.add(dataArray);
			all.add(mlFileFormatVersion);
			all.add(mlRawXVelocities);
			all.add(mlRawYVelocities);
			all.add(mlRawZVelocities);
			if (hasPressures) {
				all.add(mlRawPressures);
			}

			all.add(mlTranslatedXVelocities);
			all.add(mlTranslatedYVelocities);
			all.add(mlTranslatedZVelocities);

			all.add(mlFTXVelocities);
			all.add(mlFTYVelocities);
			all.add(mlFTZVelocities);

			all.add(mlFTRCXVelocities);
			all.add(mlFTRCYVelocities);
			all.add(mlFTRCZVelocities);

			all.add(mlXSNRs);
			all.add(mlYSNRs);
			all.add(mlZSNRs);
			
			all.add(mlXCorrelations);
			all.add(mlYCorrelations);
			all.add(mlZCorrelations);
			
			all.add(mlWDiffs);

			File outputFile = new File(dataPointFilename);
			new MatFileWriter(outputFile, all);
		} catch (Exception e) {
			e.printStackTrace();
	     	
			JOptionPane.showMessageDialog(DAFrame.getFrame(), e.getMessage(), DAStrings.getString(DAStrings.ERROR_SAVING_FILE_DETAILS_TITLE), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private String getProbeDetailXML() {
		DAFileOutputStringBuffer probeDetailXML = new DAFileOutputStringBuffer();
		
		probeDetailXML.append(MAJFCTools.makeXMLStartTag(DADefinitions.XML_PROBE_DETAILS, false));
		probeDetailXML.append(MAJFCTools.makeXMLInternalAttribute(DADefinitions.XML_PD_PROBE_TYPE, mProbeDetail.get(BackEndAPI.PD_KEY_PROBE_TYPE)));
		probeDetailXML.append(MAJFCTools.makeXMLInternalAttribute(DADefinitions.XML_PD_PROBE_ID, mProbeDetail.get(BackEndAPI.PD_KEY_PROBE_ID)));
		probeDetailXML.append(MAJFCTools.makeXMLInternalAttribute(DADefinitions.XML_PD_SAMPLING_RATE, mProbeDetail.get(BackEndAPI.PD_KEY_SAMPLING_RATE)));
		probeDetailXML.append(MAJFCTools.makeXMLEndTag(DADefinitions.XML_PROBE_DETAILS, false));
		
		return probeDetailXML.toString();
	}

	/**
	 * Allows child classes to add additional XML
	 * @return
	 */
	protected String getAdditionalSummaryXML() {
		return "";
	}

	/**
	 * Makes the filename of a data point file
	 * @param dataSetDirName
	 * @param dataPoint
	 * @return The filename for the data point file
	 */
	public String makeDataPointFilename(String dataSetDirName) {
		return dataSetDirName + MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + getFilename();
	}
	
	/**
	 * Adds a measured velocity to the list of velocities measured at this point for the specified component
	 * @param velocityComponent
	 * @param measuredVelocity
	 */
	public void addMeasuredVelocity(String componentName, double measuredVelocity) throws BackEndAPIException {
		addMeasuredVelocity(componentName, measuredVelocity, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION, DADefinitions.INVALID_SNR_OR_CORRELATION);
	}
	
	/**
	 * Adds a measured velocity to the list of velocities measured at this point for the specified component
	 * @param velocityComponent
	 * @param measuredVelocity
	 * @param correlation The correlation (set to DADefinitions.INVALID_SNR_OR_CORRELATION if not required)
	 * @param snr The SNR (set to DADefinitions.INVALID_SNR_OR_CORRELATION if not required)
	 * @param wDiff The difference between the two w-beam measurements (set to DADefinitions.INVALID_SNR_OR_CORRELATION if not required)
	*/
	public void addMeasuredVelocity(String componentName, double measuredVelocity, double correlation, double snr, double wDiff) throws BackEndAPIException{
		addMeasuredVelocity(componentName, measuredVelocity, correlation, snr, wDiff, Double.NaN, Double.NaN, Double.NaN);
	}
	
	/**
	 * Adds a measured velocity to the list of velocities measured at this point for the specified component
	 * This should only be used when restoring from a file
	 * @param velocityComponent The velocity component this measurement is for
	 * @param measuredVelocity The raw velocity measurement
	 * @param correlation The correlation (set to DADefinitions.INVALID_SNR_OR_CORRELATION if not required)
	 * @param snr The SNR (set to DADefinitions.INVALID_SNR_OR_CORRELATION if not required)
	 * @param wDiff The difference between the two w (z direction) velocity measurements (set to DADefinitions.INVALID_SNR_OR_CORRELATION if not required; only used if componentName is the z-component)
	 * @param tVelocity The translated velocity (set to Double.NaN if not specified)
	 * @param ftVelocity The filtered and translated velocity (set to Double.NaN if not specified)
	 * @param ftrcVelocity The filtered, translated and rotation corrected velocity (set to Double.NaN if not specified) 
	 */
	public void addMeasuredVelocity(String componentName, double measuredVelocity, double correlation, double snr, double wDiff, Double tVelocity, Double ftVelocity, Double ftrcVelocity) throws BackEndAPIException{
		synchronized (mDataLock){
			if (mDetail == null) {
				mDetail = new DataPointDetail();
			}
			
			DataPointDetailIndex componentIndex = mComponentIndexLookup.get(componentName);
			mDetail.get(componentIndex).add(measuredVelocity);
			
			DataPointDetailIndex correlationIndex = null;
			DataPointDetailIndex snrIndex = null;
			DataPointDetailIndex wDiffIndex = null;
			DataPointDetailIndex tVelocitiesIndex = null, ftVelocitiesIndex = null, ftrcVelocitiesIndex = null;
			if (componentIndex.equals(BackEndAPI.DPD_KEY_RAW_X_VELOCITIES)) {
				correlationIndex = BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS;
				snrIndex = BackEndAPI.DPD_KEY_X_SIGNAL_SNRS;
				tVelocitiesIndex = BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES;
				ftVelocitiesIndex = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES;
				ftrcVelocitiesIndex = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES;
			} else if (componentIndex.equals(BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES)) {
				correlationIndex = BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS;
				snrIndex = BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS;
				tVelocitiesIndex = BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES;
				ftVelocitiesIndex = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES;
				ftrcVelocitiesIndex = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES;
			} else if (componentIndex.equals(BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES)) {
				correlationIndex = BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS;
				snrIndex = BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS;
				wDiffIndex = BackEndAPI.DPD_KEY_W_DIFF;
				tVelocitiesIndex = BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES;
				ftVelocitiesIndex = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES;
				ftrcVelocitiesIndex = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES;
			}

			if (correlationIndex != null) {
				mDetail.get(correlationIndex).add(correlation);
			}

			if (snrIndex != null) {
				mDetail.get(snrIndex).add(snr);
			}
			
			if (wDiffIndex != null) {
				mDetail.get(wDiffIndex).add(wDiff);
			}
			
			if (tVelocitiesIndex != null && tVelocity.equals(Double.NaN) == false) {
				mDetail.get(tVelocitiesIndex).add(tVelocity);
			}
			
			if (ftVelocitiesIndex != null && ftVelocity.equals(Double.NaN) == false) {
				mDetail.get(ftVelocitiesIndex).add(ftVelocity);
			}
			
			if (ftrcVelocitiesIndex != null && ftrcVelocity.equals(Double.NaN) == false) {
				mDetail.get(ftrcVelocitiesIndex).add(ftrcVelocity);
			}
		}
	}
	
	public void addMeasuredPressure(double pressure) {
		synchronized (mDataLock) {
			mDetail.get(BackEndAPI.DPD_KEY_PRESSURE).add(pressure);
		}
	}
	
	/**
	 * @return the YCoord
	 */
	public int getYCoord() {
		return mYCoord;
	}

	/**
	 * @return the ZCoord
	 */
	public int getZCoord() {
		return mZCoord;
	}
	
	/**
	 * Gets the summary data field specified.
	 * NOTE: getYCoord() and getZCoord() should be used for the coordinates.
	 * @param field The field identifier (one of the BackEndAPI.DPS_KEY_... keys)
	 * @return The specified field value
	 */
	public double getSummaryDataField(DataPointSummaryIndex field) {
		synchronized (mDataLock) {
			return mSummary.get(field);
		}
	}

	/**
	 * Sets the summary data field specified
	 * @param field The field to set
	 * @param value The value to set the field to
	 */
	public void setSummaryDataField(DataPointSummaryIndex field, double value) {
		synchronized (mDataLock) {
			mSummary.set(field, value);
		}
	}
	
	/**
	 * Sets the probe details data field specified
	 * @param field The field to set
	 * @param value The value to set the field to
	 */
	public void setProbeDetails(ProbeDetail probeDetails) {
		synchronized (mDataLock) {
			probeDetails.copyInto(mProbeDetail);
		}
	}
	
	/**
	 * Gets the summary data field specified.
	 * NOTE: getYCoord() and getZCoord() should be used for the coordinates.
	 * @param field The field identifier (one of the BackEndAPI.DPS_KEY_... keys)
	 * @return The specified field value
	 */
	public String getProbeDetailsDataField(ProbeDetailIndex field) {
		synchronized (mDataLock) {
			return mProbeDetail.get(field);
		}
	}

	/**
	 * Gets the velocity measurements for the specified component direction
	 * @param index The index of the required velocities
	 * @return A copy of the velocity data for the specified component
	 */
	public Vector<Double> getVelocities(DataPointDetailIndex index) {
		synchronized (mDataLock) {
			return mDetail.get(index);
		}
	}

	/**
	 * Load the details for this data point and recalculate summary data
	 * @param dataSet The data set file which this point belongs to
	 */
	public void loadDetails(File dataSet) throws BackEndAPIException {
		loadDetailsAndRecalculateSummaryData(dataSet, false);
		
		// If this is file version 2 or earlier then we still need to recalculate the filtered velocities.
		// If this is file version 3 or later then the filtered velocities will have been read from the file
		if (mFileFormatVersion < 3) {
			calculate();
		}
	}
	
	/**
	 * Load the details for this data point and recalculate summary data
	 * @param dataSet The data set file which this point belongs to
	 */
	public void loadDetailsAndRecalculateSummaryData(File dataSet) throws BackEndAPIException{
		loadDetailsAndRecalculateSummaryData(dataSet, true);
	}
	
	/**
	 * Load the details for this data point and recalculate summary data
	 * @param dataSet The data set file which this point belongs to
	 */
	private void loadDetailsAndRecalculateSummaryData(File dataSet, boolean recalculate) throws BackEndAPIException{
		File file;
		
		// If not fully saved then use the temp file
		if (mIsSaved == false) {
			file = new File(getAbsoluteFileName(dataSet.getAbsolutePath(), MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR + DADefinitions.TEMP_DIR_NAME));
		} else {
			file = new File(getAbsoluteFileName(dataSet.getAbsolutePath(), ""));
		}
		                            
		try{
			if (mDetail == null) {
				if (useBinaryFileFormat()) {
					loadFromBinaryFile(file);
				} else {
					// If we are not clearing the cache, just pretend that we've read the file
						DocHandler docHandler = new LoadDetailsDocHandler();
						FileReader fileReader = new FileReader(file);
						QDParser.parse(docHandler, fileReader);
		
						fileReader.close();
				}
			}
			
			if (recalculate) {
				calculate(true, false);
			}
		}
		catch (Exception theException){
			throw new BackEndAPIException(DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_TITLE), DAStrings.getString(DAStrings.DATA_POINT_DETAILS_READ_ERROR_MSG) + "\n\"" + theException.getMessage() + "\"");
		}
	}

	/**
	 * Clears the detailed data.
	 * Used to save heap space.
	 */
	public void clearDetails() {
		mDetail = null;
		System.gc();
System.out.println("Clear details: " + mDataSet.mUniqueId.getShortDisplayString() + " " + mYCoord + "-" + mZCoord);		
		Vector<LinkedDataSet> linkedDataSets = mDataSet.getAllLinkedDataSets();
		LinkedDataSet linkedDataSet = null;
		int numberOfLinkedDataSets = linkedDataSets.size();
		
		for (int i = 0; i < numberOfLinkedDataSets; ++i) {
			linkedDataSet = linkedDataSets.elementAt(i);
			
			try {
				linkedDataSet.clearDataPointDetails(mYCoord, mZCoord);
			} catch (BackEndAPIException theException) {
				continue;
			}
		}
	}
	
	/**
	 * Loads point data from a binary file.
	 * 
	 * @param file The file to import from

	 * @throws BackEndAPIException 
	 */
	private void loadFromBinaryFile(File file) throws Exception {
		int NUMBER_OF_FIELDS = 0;
		final int X_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Y_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Z1_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
		final int Z2_VELOCITY_INDEX = NUMBER_OF_FIELDS++;
	
		MatFileReaderMAJ mfr = new MatFileReaderMAJ(file);

		MLDouble mlFileFormatVersion = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_FILE_FORMAT_VERSION);
		
		mFileFormatVersion = mlFileFormatVersion.get(1).intValue();
		
		MLDouble mlRawXVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_X_VELOCITIES);
		MLDouble mlRawYVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_Y_VELOCITIES);
		MLDouble mlRawZVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_Z_VELOCITIES);
		MLDouble mlRawPressures = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_PRESSURES);
		
		MLDouble mlTranslatedXVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_TRANSLATED_X_VELOCITIES);
		MLDouble mlTranslatedYVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_TRANSLATED_Y_VELOCITIES);
		MLDouble mlTranslatedZVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_TRANSLATED_Z_VELOCITIES);
		
		MLDouble mlFTXVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_FT_X_VELOCITIES);
		MLDouble mlFTYVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_FT_Y_VELOCITIES);
		MLDouble mlFTZVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_FT_Z_VELOCITIES);
		
		MLDouble mlFTRCXVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_FTRC_X_VELOCITIES);
		MLDouble mlFTRCYVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_FTRC_Y_VELOCITIES);
		MLDouble mlFTRCZVelocities = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_FTRC_Z_VELOCITIES);
		
		MLDouble mlXSNRs = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_X_SNRS);
		MLDouble mlYSNRs = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_Y_SNRS);
		MLDouble mlZSNRs = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_Z_SNRS);
	
		MLDouble mlXCorrelations = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_X_CORRELATIONS);
		MLDouble mlYCorrelations = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_Y_CORRELATIONS);
		MLDouble mlZCorrelations = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_Z_CORRELATIONS);
		
		MLDouble mlWDiffs = (MLDouble) mfr.getMLArray(DADefinitions.MATLAB_W_DIFFS);

		int numberOfMeasurements = mlRawXVelocities.getM();
		for (int measurementIndex = 0; measurementIndex < numberOfMeasurements; ++measurementIndex) {
			for (int componentIndex = 0; componentIndex < Z2_VELOCITY_INDEX; ++componentIndex) {
				if (componentIndex == X_VELOCITY_INDEX) {
					addMeasuredVelocity(DADefinitions.XML_DATA_POINT_X_COMPONENT, mlRawXVelocities.get(measurementIndex), mlXCorrelations.get(measurementIndex), mlXSNRs.get(measurementIndex), mlWDiffs.get(measurementIndex), mlTranslatedXVelocities.get(measurementIndex), mlFTXVelocities.get(measurementIndex), mlFTRCXVelocities.get(measurementIndex));
					
					if (mlRawPressures != null && mlRawPressures.getM() >= measurementIndex) {
						addMeasuredPressure(mlRawPressures.get(measurementIndex));
					}
				} else if (componentIndex == Y_VELOCITY_INDEX) {
					addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Y_COMPONENT, mlRawYVelocities.get(measurementIndex), mlYCorrelations.get(measurementIndex), mlYSNRs.get(measurementIndex), mlWDiffs.get(measurementIndex), mlTranslatedYVelocities.get(measurementIndex), mlFTYVelocities.get(measurementIndex), mlFTRCYVelocities.get(measurementIndex));
				} else if (componentIndex == Z1_VELOCITY_INDEX) {
					addMeasuredVelocity(DADefinitions.XML_DATA_POINT_Z_COMPONENT, mlRawZVelocities.get(measurementIndex), mlZCorrelations.get(measurementIndex), mlZSNRs.get(measurementIndex), mlWDiffs.get(measurementIndex), mlTranslatedZVelocities.get(measurementIndex), mlFTZVelocities.get(measurementIndex), mlFTRCZVelocities.get(measurementIndex));
				}
			}
		}
	}
	
	/**
	 * Handles the events from the XML parser
	 * 
	 * @author MAJ727
	 */
	protected class LoadDetailsDocHandler extends MyAbstractDocHandler {
		String mComponentTag = null;
		
		/**
		 * Start of document
		 */
		public void startDocument() {
			// Clear existing data
			mDetail = null;
			super.startDocument();
		}

		/**
		 * Start element found
		 * @param elem The element name
		 * @param h Hashtable of child nodes
		 */
		public void startElement(String elem, Hashtable<String, String> h) {
			mStartElement = elem;
			
			switch (mFileFormatVersion) {
				case 1:
					return;
				case 2:
					if (elem.equals(DADefinitions.XML_DATA_POINT_X_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_Y_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_Z_COMPONENT)) {
						mComponentTag = elem;
					} else if (elem.equals(DADefinitions.XML_DATA_POINT_VALUE)) {
						double velocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_RAW_VELOCITY));
						
						try {
							// Values read from file are in "English" ('.' decimal separator) irrespective of locale
							addMeasuredVelocity(mComponentTag, velocity);
						} catch (BackEndAPIException theException) {
							MAJFCLogger.log("Weird data while loading data point details: " + mStartElement + ' ' + elem);
						}
					}
					break;
				case 3:
					if (elem.equals(DADefinitions.XML_DATA_POINT_X_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_Y_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_Z_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_PRESSURE)) {
						mComponentTag = elem;
					} else if (elem.equals(DADefinitions.XML_DATA_POINT_VALUE)) {
						boolean isPressure = mComponentTag.equals(DADefinitions.XML_DATA_POINT_PRESSURE);
						
						if (isPressure) {
							double pressure = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_PRESSURE));
							addMeasuredPressure(pressure);
							
							break;
						}
						
						double velocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_RAW_VELOCITY));
						double correlation = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_CORRELATION));
						double snr = DADefinitions.INVALID_SNR_OR_CORRELATION;
						double wDiff = DADefinitions.INVALID_SNR_OR_CORRELATION;
						double tVelocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_TRANSLATED_VELOCITY));
						double ftVelocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_FILTERED_AND_TRANSLATED_VELOCITY));
						double ftrcVelocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY));
						
						try {
							// Values read from file are in "English" ('.' decimal separator) irrespective of locale
							addMeasuredVelocity(mComponentTag, velocity, correlation, snr, wDiff, tVelocity, ftVelocity, ftrcVelocity);
						} catch (BackEndAPIException theException) {
							MAJFCLogger.log("Weird data while loading data point details: " + mStartElement + ' ' + elem);
						}
					}
					break;
				case 4:
				case 5:
					if (elem.equals(DADefinitions.XML_DATA_POINT_X_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_Y_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_Z_COMPONENT) || elem.equals(DADefinitions.XML_DATA_POINT_PRESSURE)) {
						mComponentTag = elem;
					} else if (elem.equals(DADefinitions.XML_DATA_POINT_VALUE)) {
						boolean isPressure = mComponentTag.equals(DADefinitions.XML_DATA_POINT_PRESSURE);
						
						if (isPressure) {
							double pressure = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_PRESSURE));
							addMeasuredPressure(pressure);
							
							break;
						}
						
						double velocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_RAW_VELOCITY));
						double correlation = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_CORRELATION));
						double snr = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_SNR));
						String wDiffStr = h.get(DADefinitions.XML_DATA_POINT_W_DIFF);
						double wDiff = wDiffStr == null ? DADefinitions.INVALID_SNR_OR_CORRELATION : Double.valueOf(wDiffStr);
						double tVelocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_TRANSLATED_VELOCITY));
						double ftVelocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_FILTERED_AND_TRANSLATED_VELOCITY));
						double ftrcVelocity = Double.valueOf(h.get(DADefinitions.XML_DATA_POINT_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY));
						
						try {
							// Values read from file are in "English" ('.' decimal separator) irrespective of locale
							addMeasuredVelocity(mComponentTag, velocity, correlation, snr, wDiff, tVelocity, ftVelocity, ftrcVelocity);
						} catch (BackEndAPIException theException) {
							MAJFCLogger.log("Weird data while loading data point details: " + mStartElement + ' ' + elem);
						}
					}
					break;
				default:
					return;
			}
		}

		@Override
		public void elementValue(String value) {
			if (mStartElement.equals(DADefinitions.XML_DATA_POINT_VERSION)) {
				// Use intermediate version number to determine file format
				StringTokenizer st = new StringTokenizer(value, ".");
				st.nextToken();
				mFileFormatVersion = Integer.valueOf(st.nextToken());
				
				return;
			}
			
			// Old style velocity
			switch (mFileFormatVersion) {
				case 1:
					try {
						// Values read from file are in "English" ('.' decimal separator) irrespective of locale
						addMeasuredVelocity(mStartElement, Double.valueOf(value));
					} catch (BackEndAPIException theException) {
						MAJFCLogger.log("Weird data while loading data point details: " + mStartElement + ' ' + value);
					}
					break;
				case 2:
					break;
				default:
					return;
			}
		}
	}

	/**
	 * Gets the filename used when saving data for this file
	 * @return The filename used when saving data for this file
	 */
	public String getFilename() {
		StringBuffer filename = new StringBuffer();
		filename.append(getYCoord());
		filename.append(DADefinitions.COORDINATE_SEPARATOR);
		filename.append(getZCoord());
		filename.append(useBinaryFileFormat() ? DADefinitions.FILE_EXTENSION_DATA_POINT_BINARY : DADefinitions.FILE_EXTENSION_DATA_POINT);
		
		return filename.toString();
	}
	
	private boolean useBinaryFileFormat() {
		return mDataSet.mConfigData.get(BackEndAPI.DSC_KEY_USE_BINARY_FILE_FORMAT).equals(DADefinitions.USING_BINARY_FILE_FORMAT);
	}
	
	/**
	 * Gets the absolute filename used when saving data for this file
	 * @param dataSetFilePath The path to the data set file for the data set containing this data point
	 * @return The absolute filename used when saving data for this file
	 */
	public String getAbsoluteFileName(String dataSetFilePath, String extraPath) {
		StringBuffer filename = new StringBuffer();
		filename.append(dataSetFilePath.substring(0, dataSetFilePath.lastIndexOf('.')));
		filename.append(extraPath);
		filename.append(MAJFCTools.SYSTEM_FILE_PATH_SEPARATOR);
		filename.append(getFilename());
		
		return filename.toString();
	}

	/**
	 * Copies the summary data
	 * @param dataPointSummary The object to copy it into
	 */
	public void copySummary(DataPointSummary dataPointSummary) {
		synchronized (mDataLock) {
			mSummary.copyInto(dataPointSummary);
		}
	}
	
	/**
	 * Copies the detail data
	 * @param dataPointSummary The object to copy it into
	 */
	public void copyDetail(DataPointDetail dataPointDetail) {
		synchronized (mDataLock) {
			mDetail.copyInto(dataPointDetail);
		}
	}
	
	/**
	 * Calculate the mean and standard deviation for this point.
	 * @see DataPoint#filterVelocities()
	 */
	public void calculate() {
		calculate(true, true);
	}
	
	/**
	 * Calculate the mean and standard deviation for this point.
	 * @see DataPoint#filterVelocities()
	 */
//	static long time = 0;
	public void calculate(boolean calculateChildren) {
		calculate(calculateChildren, true);
	}
	
	/**
	 * Calculate the mean and standard deviation for this point.
	 * @see DataPoint#filterVelocities()
	 */
//	static long time = 0;
	private void calculate(boolean calculateChildren, boolean doSynchronisation) {
		// Do this first to prevent a lock with the DataSet.mDataLock
		// Only calculate the fields which have already been calculated for the rest of the data set
		Hashtable<DataPointSummaryIndex, DataPointSummaryIndex> missingDataPointSummaryFieldKeys = mDataSet.getMissingDataPointSummaryFieldKeys();

		synchronized (mDataLock) {
//			long lastTime = time;
//			time = System.currentTimeMillis();
//			System.out.println((time - lastTime)/1000d + " Calculate: " + mDataSet.getUniqueId().getShortDisplayString() + ' ' + mYCoord + "-" + mZCoord);
			calculateTranslatedVelocities(BackEndAPI.DPD_KEY_RAW_X_VELOCITIES, BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES, BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES, BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES, BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES, BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES, true, false, true);

			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES, BackEndAPI.DPS_KEY_U_MEAN_TRANSLATED_VELOCITY, BackEndAPI.DPS_KEY_U_TRANSLATED_ST_DEV);
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES, BackEndAPI.DPS_KEY_V_MEAN_TRANSLATED_VELOCITY, BackEndAPI.DPS_KEY_V_TRANSLATED_ST_DEV);
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES, BackEndAPI.DPS_KEY_W_MEAN_TRANSLATED_VELOCITY, BackEndAPI.DPS_KEY_W_TRANSLATED_ST_DEV);

			filterVelocities(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES, BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES, BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES);
			
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_VELOCITY, BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_ST_DEV);
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES, BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_VELOCITY, BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_ST_DEV);
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES, BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_VELOCITY, BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_ST_DEV);

			calculateTranslatedVelocities(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES, BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES, BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES, BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES, BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES, BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES, false, true, false);
			
			if (doSynchronisation){// && DataSet.isMultiRunMean(mDataSet.getDataSetType()) == false) {
				// Do any auto-trimming here, before the rotation correction - the r.c. should have a negligible effect, but could change the indices very slightly and screw things up
				boolean calculate = false;
				
				if (mDataSet.autoTrim()) {
					DataPointDetailIndex[] dpdIndices = new DataPointDetailIndex[] {	BackEndAPI.DPD_KEY_RAW_X_VELOCITIES,
																						BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES,
																						BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES,
																						BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS,
																						BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS,
																						BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS,
																						BackEndAPI.DPD_KEY_X_SIGNAL_SNRS,
																						BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS,
																						BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS,
																						BackEndAPI.DPD_KEY_PRESSURE,
																						BackEndAPI.DPD_KEY_W_DIFF };

					DataSetConfig configData = mDataSet.getConfigData();
					double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
					double trimStartBy = configData.get(BackEndAPI.DSC_KEY_AT_TRIM_START_BY);
					int startIndex = (int) (trimStartBy * samplingRate);

					// Trim the start before doing anything else
					if (startIndex > 0) {
						for (int i = 0; i < dpdIndices.length; ++i) {
							Vector<Double> values = mDetail.get(dpdIndices[i]);
							int endIndex = values.size();
	
							if (endIndex <= startIndex) {
								continue;
							}
							
							Vector<Double> trimmedValues = new Vector<Double>(values.subList(startIndex, endIndex));
							mDetail.set(dpdIndices[i], trimmedValues);
						}
						
						calculate = true;
					}

					startIndex = 0;
					int synchronisationIndex = findSynchronisationIndex();
					if (synchronisationIndex != -1) {
						double priorLength = configData.get(BackEndAPI.DSC_KEY_AT_PRIOR_LENGTH);
						startIndex = synchronisationIndex - ((int) (priorLength * samplingRate));
						if (startIndex < 0) {
							startIndex = 0;
						}
						
						// Adjust the synchronisationIndex now that we have stripped off some of the preceding values
						mPreTrimSynchIndex = synchronisationIndex;
						setSummaryDataField(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX, synchronisationIndex - startIndex);
						
						calculate = true;
					}
					
					// Trim the end if necessary
					int sampleLength = configData.get(BackEndAPI.DSC_KEY_AT_SAMPLE_LENGTH).intValue();
					
					for (int i = 0; i < dpdIndices.length; ++i) {
						Vector<Double> values = mDetail.get(dpdIndices[i]);
						int endIndex = sampleLength == 0 ? values.size() : Math.min(startIndex + sampleLength, values.size());
						
						if (endIndex == 0) {
							continue;
						}

						Vector<Double> trimmedValues = new Vector<Double>(values.subList(startIndex, endIndex));
						mDetail.set(dpdIndices[i], trimmedValues);
						
						
						calculate = true;
					}

				}
				
				if (calculate) {
					calculate(calculateChildren, false);
					return;
				}
			}
			
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES, BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);
			calculateMeansAndStDevs(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES, BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV);

			saveToTempFile(mDataSet.makeTemporaryDirectoryName());
			
			setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_U, Collections.max(mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES)));
			setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_V, Collections.max(mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES)));
			setSummaryDataField(BackEndAPI.DPS_KEY_MAXIMUM_W, Collections.max(mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES)));
			
			setSummaryDataField(BackEndAPI.DPS_KEY_X_MEAN_CORRELATION, MAJFCMaths.mean(mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS)));
			setSummaryDataField(BackEndAPI.DPS_KEY_X_MEAN_SNR, MAJFCMaths.mean(mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS)));
			setSummaryDataField(BackEndAPI.DPS_KEY_Y_MEAN_CORRELATION, MAJFCMaths.mean(mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS)));
			setSummaryDataField(BackEndAPI.DPS_KEY_Y_MEAN_SNR, MAJFCMaths.mean(mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS)));
			setSummaryDataField(BackEndAPI.DPS_KEY_Z_MEAN_CORRELATION, MAJFCMaths.mean(mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS)));
			setSummaryDataField(BackEndAPI.DPS_KEY_Z_MEAN_SNR, MAJFCMaths.mean(mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS)));
			
			boolean fixedProbeDataCalculated = false;
			
			if (missingDataPointSummaryFieldKeys != null) {
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN) == false
						|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN) == false
						|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN) == false) {
						calculateFluctuatingVelocityProductMeans();
				}
				
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO) == false) {
						calculateQuadrantHoleShearStresses();
				}
				
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_U_PRIME) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_V_PRIME) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_RMS_W_PRIME) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_TKE) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_U_TKE_FLUX) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_V_TKE_FLUX) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_W_TKE_FLUX) == false) {
						calculateTKEAndTKEFlux();
				}
				
				if (missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION) == false
							|| missingDataPointSummaryFieldKeys.contains(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION) == false) {
						calculateFixedProbeCorrelations();
						fixedProbeDataCalculated = true;
				}
			}
			
			// Depending on which of the above calculations have been done, the calculations for the associated fixed probe
			// may not have been done
			if (calculateChildren) {
				loadLinkedDataSetDataPointDetails(new DataPointSummary(), new DataPointDetail());
			} else if (fixedProbeDataCalculated == false && DataSet.isMultiRunMean(mDataSet.getDataSetType()) == false) {
				Vector<LinkedDataSet> linkedDataSets = mDataSet.getAllLinkedDataSets();
				int numberOfLinkedDataSets = linkedDataSets.size();
				
				for (int i = 0; i < numberOfLinkedDataSets; ++i) {
					LinkedDataSet linkedDataSet = linkedDataSets.elementAt(i);
					
					if (linkedDataSet.getClass().equals(FixedProbeDataSet.class)) {
						try {
							linkedDataSet.loadDataPointDetails(getYCoord(), getZCoord(), new DataPointSummary(), new DataPointDetail(), true);
						} catch (BackEndAPIException e) {
						}
					}
				}
			}
		}
	}
	
	private int findSynchronisationIndex() {
		int synchronisationIndex = -1;
		
		if (DataSet.isMultiRunMean(mDataSet.getDataSetType()) == false && (DataSet.isMultiRunRun(mDataSet.getDataSetType()) || mDataSet.getDataSetType().equals(BackEndAPI.DST_MULTI_RUN_RUN_FIXED_PROBE) || mDataSet.autoTrim())) {
			if (mSynchParentCoords != null) {
				DataPoint synchParent = null;
				
				if (mSynchParentIsFixedProbe) {
					Vector<LinkedDataSet> linkedDataSets = mDataSet.getAllLinkedDataSets();
					int numberOfLinkedDataSets = linkedDataSets.size();
					
					for (int i = 0; i < numberOfLinkedDataSets; ++i) {
						DataSet synchDataSet = linkedDataSets.elementAt(i);
						
						// If this is the multi-run mean value dataset, then linkedDataSets will included the multi-run run datasets as
						// well as the multi-run fixed probe dataset, and the mean value fixed probe data set. Don't check the latter two.
						if (synchDataSet.getClass().equals(MultiRunFixedProbeDataSet.class) == false
							&& (DataSet.isMultiRunMean(mDataSet.getDataSetType()) == false && synchDataSet.getClass().equals(FixedProbeDataSet.class) == false)) {
							continue;
						}
						 
						try {
							double synchIndex = synchDataSet.getSummaryDataField(mSynchParentCoords.getY(), mSynchParentCoords.getZ(), BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX);
							synchronisationIndex = Double.isNaN(synchIndex) ? -1 : (int) synchIndex;
							
							synchParent = synchDataSet.getDataPoint(mSynchParentCoords.getY(), mSynchParentCoords.getZ());

							// If we find this point in this fixed probe dataset then we've finished searching
							break;
						} catch (BackEndAPIException e) {
							// The datapoint for these coordinates was not in this fixed probe dataset - move on to the next one.
							continue;
						}
					}
				} else {
					try {
						synchronisationIndex = (int) mDataSet.getSummaryDataField(mSynchParentCoords.getY(), mSynchParentCoords.getZ(), BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX);
						synchParent = mDataSet.getDataPoint(mSynchParentCoords.getY(), mSynchParentCoords.getZ());
					} catch (BackEndAPIException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return -1;
					}
				}
				
				if (synchParent != null && synchParent.mPreTrimSynchIndex != -1) {
					synchronisationIndex = synchParent.mPreTrimSynchIndex;
				}
			} else {
				Vector<Double> uVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
				DataSetConfig configData = mDataSet.getConfigData();
//				DespikingFilters despikingFilter = DespikingFilters.getDespikingFilters();
//				despikingFilter.movingAverage(despikingFilter.new ComponentDataHolder<Vector<Double>>(uVelocities, null, null), configData.get(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE).intValue());
				Double limitingValue = configData.get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE);
				int numberOfVelocities = uVelocities.size();
				int startPosition = (int) (configData.get(BackEndAPI.DSC_KEY_SYNCH_IGNORE_FIRST_X_SECONDS) * configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE));
				boolean risingEdge = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_SYNCH_LIMITING_VALUE_DIRECTION) == MRS_LIMITING_VALUE_DIRECTION_RISING;

				if (limitingValue.doubleValue() == -1) {
					// No synchronisation
					synchronisationIndex = -1;
				} else if (limitingValue.equals(Double.NaN)) {
					// Find index of maximum or minimum velocity
					double maxOrMinVelocity = risingEdge ? Double.MIN_VALUE : Double.MAX_VALUE;
					
					for (int i = startPosition; i < numberOfVelocities; ++i) {
						if (risingEdge) {
							if (uVelocities.elementAt(i) > maxOrMinVelocity) {
								maxOrMinVelocity = uVelocities.elementAt(i);
								synchronisationIndex = i;
							}
						} else if (uVelocities.elementAt(i) < maxOrMinVelocity) {
							maxOrMinVelocity = uVelocities.elementAt(i);
							synchronisationIndex = i;
						}
					}
				} else {
					// Find first value over/under the limitingValue
	
					for (int i = startPosition; i < numberOfVelocities; ++i) {
						double thisValue = uVelocities.elementAt(i);
						
						if (risingEdge) {
							if (thisValue >= limitingValue) {
								synchronisationIndex = i;
								break;
							}
						} else if (thisValue <= limitingValue) {
							synchronisationIndex = i;
							break;
						}
					}
				}
			}		
		}
		
		setSummaryDataField(BackEndAPI.DPS_KEY_MRS_SYNCHRONISATION_INDEX, synchronisationIndex);
		System.out.println("Dataset: " + mDataSet.getUniqueId().getFullDisplayString() + "\t\t" + mYCoord + '-' + mZCoord + "\t\tsynch index: " + synchronisationIndex);
		if (DataAnalyser.sDevHack) {
			System.out.println(mDataSet.getUniqueId().getShortDisplayString() + "\t" + getYCoord() + '-' + getZCoord() + ":\tsynch index: " + synchronisationIndex);
		}
		
		return synchronisationIndex;
	}

	/**
	 * Filters the velocity data. This method uses the DataPointDetail BackEndAPI.DPS_KEY_TRANSLATED_*_VELOCITIES as the
	 * starting point and populates the specified DataPointDetail fields
	 * 
	 * @param filteredXVelocitiesIndex The index of the DataPointDetail store in which to store the filtered x-velocities
	 * @param filteredYVelocitiesIndex The index of the DataPointDetail store in which to store the filtered y-velocities
	 * @param filteredZVelocitiesIndex The index of the DataPointDetail store in which to store the filtered z-velocities

	 */
	private void filterVelocities(DataPointDetailIndex filteredXVelocitiesIndex, DataPointDetailIndex filteredYVelocitiesIndex, DataPointDetailIndex filteredZVelocitiesIndex) {
		DespikingFilters.ComponentDataHolder<Vector<Double>> velocities = DespikingFilters.getDespikingFilters().new ComponentDataHolder<Vector<Double>>(mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES), mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES), mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES));
		@SuppressWarnings("unchecked")
		DespikingFilters.ComponentDataHolder<Vector<Double>> filteredVelocities = DespikingFilters.getDespikingFilters().new ComponentDataHolder<Vector<Double>>((Vector<Double>)velocities.getXComponent().clone(), (Vector<Double>)velocities.getYComponent().clone(), (Vector<Double>)velocities.getZComponent().clone());
		
		if (velocities.getXComponent().size() > 1) {
			int[] filterTypes = new int[] { mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_PRE_FILTER_TYPE).intValue(),
											mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_DESPIKING_FILTER_TYPE).intValue() };
			double samplingRate = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
			double limitingCorrelation = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_LIMITING_CORRELATION);
			double limitingSNR = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_LIMITING_SNR);
			double limitingWDiff = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_LIMITING_W_DIFF);
			int movingAverageWindowSize = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_MOVING_AVERAGE_WINDOW_SIZE).intValue();
			boolean usePercentageForCorrAndSNRFilter = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_USE_PERCENTAGE_FOR_CORR_AND_SNR_FILTER).equals(DADefinitions.USING_PERCENTAGE_FOR_CORR_AND_SNR_FILTER);
			Integer[] totalNumberOfRemovedVelocities = null;
			
			for (int filterIndex = 0; filterIndex < filterTypes.length; ++filterIndex) {
				DespikingFilters.ComponentDataHolder<Integer> numberOfRemovedVelocities = null;
				
				if (filterTypes[filterIndex] == BackEndAPI.DFT_NONE.getIntIndex() ||  DataSet.isMultiRunMean(mDataSet.getDataSetType())) {
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().new ComponentDataHolder<Integer>(0, 0, 0);
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_EXCLUDE_LEVEL.getIntIndex()) {
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().excludeLevel(filteredVelocities, mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_EXCLUDE_LEVEL));
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_REMOVE_ZEROES.getIntIndex()) {
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().removeZeroes(filteredVelocities, getPSTReplacementMethod());
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_VELOCITY_CORRELATION.getIntIndex()) {
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().velocityCorrelation(filteredVelocities, getPSTReplacementMethod(), samplingRate);
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_PHASE_SPACE_THRESHOLDING.getIntIndex()) {
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().phaseSpaceThresholding(filteredVelocities, getCharacteristicScalerType(), getPSTReplacementMethod(), samplingRate);
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_MODIFIED_PHASE_SPACE_THRESHOLDING.getIntIndex()) {
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().modifiedPhaseSpaceThresholding(filteredVelocities, getCharacteristicScalerType(), getPSTReplacementMethod(), mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_SAFE_LEVEL_C1), mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_MODIFIED_PST_AUTO_EXCLUDE_LEVEL_C2), samplingRate);
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_CORRELATION_AND_SNR.getIntIndex()) {
					DespikingFilters.ComponentDataHolder<Vector<Double>> signalCorrelations = DespikingFilters.getDespikingFilters().new ComponentDataHolder<Vector<Double>>(mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS), mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS), mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS));
					DespikingFilters.ComponentDataHolder<Vector<Double>> signalSNRs = DespikingFilters.getDespikingFilters().new ComponentDataHolder<Vector<Double>>(mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS), mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS), mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS));
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().correlationAndSNR(filteredVelocities, signalCorrelations, limitingCorrelation, signalSNRs, limitingSNR, usePercentageForCorrAndSNRFilter, getPSTReplacementMethod());
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_W_DIFF.getIntIndex()) {
					Vector<Double> wDiffs = mDetail.get(BackEndAPI.DPD_KEY_W_DIFF);
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().wDiff(filteredVelocities, wDiffs, limitingWDiff, getPSTReplacementMethod());
				} else if (filterTypes[filterIndex] == BackEndAPI.DFT_MOVING_AVERAGE.getIntIndex()) {
					numberOfRemovedVelocities = DespikingFilters.getDespikingFilters().movingAverage(filteredVelocities, movingAverageWindowSize);
				}

				if (numberOfRemovedVelocities != null) {
					if (filterIndex == 0) {
						totalNumberOfRemovedVelocities = new Integer[] { numberOfRemovedVelocities.getXComponent(), numberOfRemovedVelocities.getYComponent(), numberOfRemovedVelocities.getZComponent() };
					} else if (totalNumberOfRemovedVelocities == null || numberOfRemovedVelocities == null ) {
						// If either is null (and this isn't the first pass) then we can't calculate the number of removed velocities
					} else {
						totalNumberOfRemovedVelocities[0] += numberOfRemovedVelocities.getXComponent();
						totalNumberOfRemovedVelocities[1] += numberOfRemovedVelocities.getYComponent();
						totalNumberOfRemovedVelocities[2] += numberOfRemovedVelocities.getZComponent();
					}
				}
			}
			
			if (totalNumberOfRemovedVelocities != null) {
				double percentageVelocitiesRemoved = 100.0 - 100.0 * ((double) totalNumberOfRemovedVelocities[0])/((double) velocities.getXComponent().size());
				mSummary.set(BackEndAPI.DPS_KEY_PERCENTAGE_OF_U_VELOCITIES_GOOD, percentageVelocitiesRemoved);
				percentageVelocitiesRemoved = 100.0 - 100.0 * ((double) totalNumberOfRemovedVelocities[1])/((double) velocities.getYComponent().size());
				mSummary.set(BackEndAPI.DPS_KEY_PERCENTAGE_OF_V_VELOCITIES_GOOD, percentageVelocitiesRemoved);
				percentageVelocitiesRemoved = 100.0 - 100.0 * ((double) totalNumberOfRemovedVelocities[2])/((double) velocities.getZComponent().size());
				mSummary.set(BackEndAPI.DPS_KEY_PERCENTAGE_OF_W_VELOCITIES_GOOD, percentageVelocitiesRemoved);
			}
		}
		
		mDetail.set(filteredXVelocitiesIndex, filteredVelocities.getXComponent());
		mDetail.set(filteredYVelocitiesIndex, filteredVelocities.getYComponent());
		mDetail.set(filteredZVelocitiesIndex, filteredVelocities.getZComponent());
	}
	
	private CharacteristicScalerType getCharacteristicScalerType() {
		int characteristicScalerIndex = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_CS_TYPE).intValue();
		CharacteristicScalerType csType = BackEndAPI.CS_STANDARD_DEVIATION;
		
		if (characteristicScalerIndex == BackEndAPI.CS_MEDIAN_ABSOLUTE_DEVIATION.getIntIndex()) { 
			csType = BackEndAPI.CS_MEDIAN_ABSOLUTE_DEVIATION;
		} else if (characteristicScalerIndex == BackEndAPI.CS_MEAN_ABSOLUTE_DEVIATION.getIntIndex()) { 
			csType = BackEndAPI.CS_MEAN_ABSOLUTE_DEVIATION;
		}
		
		return csType;
	}

	private PSTReplacementMethod getPSTReplacementMethod() {
		int pstReplacementMethodIndex = mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_METHOD).intValue();
		PSTReplacementMethod pstReplacementMethod = BackEndAPI.PRM_NONE;
		
		if (pstReplacementMethodIndex == BackEndAPI.PRM_LINEAR_INTERPOLATION.getIntIndex()) { 
			pstReplacementMethod = BackEndAPI.PRM_LINEAR_INTERPOLATION;
		} else if (pstReplacementMethodIndex == BackEndAPI.PRM_LAST_GOOD_VALUE.getIntIndex()) { 
			pstReplacementMethod = BackEndAPI.PRM_LAST_GOOD_VALUE;
		} else if (pstReplacementMethodIndex == BackEndAPI.PRM_12PP_INTERPOLATION.getIntIndex()) { 
			pstReplacementMethod = BackEndAPI.PRM_12PP_INTERPOLATION;
		}
		
		pstReplacementMethod.setPolynomialOrder(mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_PST_REPLACEMENT_POLYNOMIAL_ORDER).intValue());
		return pstReplacementMethod;
	}

	/**
	 * Calculates the mean and standard deviation of the specified velocity set, storing them in the DataPointSummary at the specified
	 * indices
	 * @param velocitiesIndex The DataPointDetail index to read the velocities from
	 * @param meanVelocityIndex The DataPointSummary index to store the mean velocity in
	 * @param meanStDevIndex The DataPointSummary index to store the standard deviation in
	 */
	private void calculateMeansAndStDevs(DataPointDetailIndex velocitiesIndex, DataPointSummaryIndex meanVelocityIndex, DataPointSummaryIndex meanStDevIndex) {
		Vector<Double> velocities = mDetail.get(velocitiesIndex);

		mSummary.set(meanVelocityIndex, MAJFCMaths.mean(velocities));
		mSummary.set(meanStDevIndex, MAJFCMaths.standardDeviation(velocities));
	}
	
	/**
	 * Calculates the u'v' and u'w' mean values
	 */
	private void calculateFluctuatingVelocityProductMeans() {
		Vector<Double> uVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
		Vector<Double> vVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
		Vector<Double> wVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
		
		double uBar = mSummary.get(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		double vBar = mSummary.get(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		double wBar = mSummary.get(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		
		int numberOfVelocities = uVelocities.size();
		double uPrimeVPrimeSum = 0, uPrimeWPrimeSum = 0, vPrimeWPrimeSum = 0;
		
		for (int i = 0; i < numberOfVelocities; ++i) {
			double uPrime = uVelocities.elementAt(i) - uBar;
			double vPrime = vVelocities.elementAt(i) - vBar;
			double wPrime = wVelocities.elementAt(i) - wBar;
			
			uPrimeVPrimeSum += uPrime * vPrime;
			uPrimeWPrimeSum += uPrime * wPrime;
			vPrimeWPrimeSum += vPrime * wPrime;
		}
		
		mSummary.set(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN, uPrimeVPrimeSum/numberOfVelocities);
		mSummary.set(BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN, uPrimeWPrimeSum/numberOfVelocities);
		mSummary.set(BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN, vPrimeWPrimeSum/numberOfVelocities);
	}
	
	/**
	 * Calculates the Quadrant Hole shear stresses
	 */
	private void calculateQuadrantHoleShearStresses() {
		Vector<Double> uVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
		Vector<Double> vVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
		Vector<Double> wVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
		
		double uBar = mSummary.get(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		double vBar = mSummary.get(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		double wBar = mSummary.get(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		double uwStDevProduct = mSummary.get(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV) * mSummary.get(BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV); 
		double uvStDevProduct = mSummary.get(BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV) * mSummary.get(BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV); 

		double uwShearStressSumQ1 = 0, uwShearStressSumQ2 = 0, uwShearStressSumQ3 = 0, uwShearStressSumQ4 = 0;
		double uvShearStressSumQ1 = 0, uvShearStressSumQ2 = 0, uvShearStressSumQ3 = 0, uvShearStressSumQ4 = 0;
		
		int numberOfVelocities = uVelocities.size();
		
		for (int j = 0; j < numberOfVelocities; ++j) {
			double holeLimit = 0 * uwStDevProduct;
			double uPrime = uVelocities.elementAt(j) - uBar;
			double vPrime = vVelocities.elementAt(j) - vBar;
			double wPrime = wVelocities.elementAt(j) - wBar;
		
			if (Math.abs(uPrime * wPrime) > holeLimit) {
				double shearStress = uPrime * wPrime / uwStDevProduct;
				if (uPrime > 0) {
					if (wPrime > 0) {
						uwShearStressSumQ1 += shearStress;
					} else {
						uwShearStressSumQ4 += shearStress;
					}
				} else {
					if (wPrime > 0) {
						uwShearStressSumQ2 += shearStress;
					} else {
						uwShearStressSumQ3 += shearStress;
					}
				}
			}

			if (Math.abs(uPrime * vPrime) > holeLimit) {
				double shearStress = uPrime * vPrime / uvStDevProduct;
				if (uPrime > 0) {
					if (vPrime > 0) {
						uvShearStressSumQ1 += shearStress;
					} else {
						uvShearStressSumQ4 += shearStress;
					}
				} else {
					if (vPrime > 0) {
						uvShearStressSumQ2 += shearStress;
					} else {
						uvShearStressSumQ3 += shearStress;
					}
				}
			}
		}
		
		double uwTotalShearStress = uwShearStressSumQ1 + uwShearStressSumQ2 + uwShearStressSumQ3 + uwShearStressSumQ4;
		
		double[] uwScaledShearStressSums = new double[] { uwShearStressSumQ1/uwTotalShearStress, uwShearStressSumQ2/uwTotalShearStress, uwShearStressSumQ3/uwTotalShearStress, uwShearStressSumQ4/uwTotalShearStress };
		
		final double SCALED_SHEAR_STRESS_LIMIT = 1.0;
		
		for (int i = 0; i < uwScaledShearStressSums.length; ++i) {
			int minus = uwScaledShearStressSums[i] < 0 ? -1 : 1;

			if (uwTotalShearStress > 0) {
				uwScaledShearStressSums[i] = -1 * minus * SCALED_SHEAR_STRESS_LIMIT;
			} else if (Math.abs(uwScaledShearStressSums[i]) > SCALED_SHEAR_STRESS_LIMIT) {
				uwScaledShearStressSums[i] = minus * SCALED_SHEAR_STRESS_LIMIT;
			}
		}
 
		mSummary.set(BackEndAPI.DPS_KEY_UW_QUADRANT_1_SHEAR_STRESS, uwScaledShearStressSums[0]);///maxShearStressSum);
		mSummary.set(BackEndAPI.DPS_KEY_UW_QUADRANT_2_SHEAR_STRESS, uwScaledShearStressSums[1]);///maxShearStressSum);
		mSummary.set(BackEndAPI.DPS_KEY_UW_QUADRANT_3_SHEAR_STRESS, uwScaledShearStressSums[2]);///maxShearStressSum);
		mSummary.set(BackEndAPI.DPS_KEY_UW_QUADRANT_4_SHEAR_STRESS, uwScaledShearStressSums[3]);///maxShearStressSum);
		
		double uwQ1ToQ3EventsRatio = uwShearStressSumQ1/uwShearStressSumQ3;
		double uwQ2ToQ4EventsRatio = uwShearStressSumQ2/uwShearStressSumQ4;
		double uwQ2AndQ4ToQ1AndQ3EventsRatio = -(uwShearStressSumQ2 + uwShearStressSumQ4)/(uwShearStressSumQ1 + uwShearStressSumQ3);

		mSummary.set(BackEndAPI.DPS_KEY_QH_UW_Q1_TO_Q3_EVENTS_RATIO, uwQ1ToQ3EventsRatio);
		mSummary.set(BackEndAPI.DPS_KEY_QH_UW_Q2_TO_Q4_EVENTS_RATIO, uwQ2ToQ4EventsRatio);
		mSummary.set(BackEndAPI.DPS_KEY_QH_UW_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, uwQ2AndQ4ToQ1AndQ3EventsRatio);

		double uvTotalShearStress = uvShearStressSumQ1 + uvShearStressSumQ2 + uvShearStressSumQ3 + uvShearStressSumQ4;

		double[] uvScaledShearStressSums = new double[] { uvShearStressSumQ1/uvTotalShearStress, uvShearStressSumQ2/uvTotalShearStress, uvShearStressSumQ3/uvTotalShearStress, uvShearStressSumQ4/uvTotalShearStress };
		
		for (int i = 0; i < uvScaledShearStressSums.length; ++i) {
			int minus = uvScaledShearStressSums[i] < 0 ? -1 : 1;
			if (Math.abs(uvScaledShearStressSums[i]) > SCALED_SHEAR_STRESS_LIMIT) {
				uvScaledShearStressSums[i] = minus * SCALED_SHEAR_STRESS_LIMIT;
			}
		}
		
		mSummary.set(BackEndAPI.DPS_KEY_UV_QUADRANT_1_SHEAR_STRESS, uvScaledShearStressSums[0]);///maxShearStressSum);
		mSummary.set(BackEndAPI.DPS_KEY_UV_QUADRANT_2_SHEAR_STRESS, uvScaledShearStressSums[1]);///maxShearStressSum);
		mSummary.set(BackEndAPI.DPS_KEY_UV_QUADRANT_3_SHEAR_STRESS, uvScaledShearStressSums[2]);///maxShearStressSum);
		mSummary.set(BackEndAPI.DPS_KEY_UV_QUADRANT_4_SHEAR_STRESS, uvScaledShearStressSums[3]);///maxShearStressSum);
		
		double uvQ1ToQ3EventsRatio = uvShearStressSumQ1/uvShearStressSumQ3;
		double uvQ2ToQ4EventsRatio = uvShearStressSumQ2/uvShearStressSumQ4;
		double uvQ2AndQ4ToQ1AndQ3EventsRatio = -(uvShearStressSumQ2 + uvShearStressSumQ4)/(uvShearStressSumQ1 + uvShearStressSumQ3);

		mSummary.set(BackEndAPI.DPS_KEY_QH_UV_Q1_TO_Q3_EVENTS_RATIO, uvQ1ToQ3EventsRatio);
		mSummary.set(BackEndAPI.DPS_KEY_QH_UV_Q2_TO_Q4_EVENTS_RATIO, uvQ2ToQ4EventsRatio);
		mSummary.set(BackEndAPI.DPS_KEY_QH_UV_Q2_AND_Q4_TO_Q1_AND_Q3_EVENTS_RATIO, uvQ2AndQ4ToQ1AndQ3EventsRatio);
	}	
	
	/**
	 * Calculates the turbulent kinetic energy and TKE flux
	 */
	private void calculateTKEAndTKEFlux() {
		Vector<Double> uVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
		Vector<Double> vVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
		Vector<Double> wVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
		
		double uBar = mSummary.get(BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		double vBar = mSummary.get(BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		double wBar = mSummary.get(BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
		
		double uPrimeSquaredSum = 0, vPrimeSquaredSum = 0, wPrimeSquaredSum = 0;
		double tkeSum = 0;
		double uTKEFluxSum = 0, vTKEFluxSum = 0, wTKEFluxSum = 0;
		double[][][] thirdOrderCorrelationNumeratorSums = new double[BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION][BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION][BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION];
		double[][] anisotropicStressTensorNumeratorSums = new double[BackEndAPI.NUMBER_OF_DIMENSIONS][BackEndAPI.NUMBER_OF_DIMENSIONS];
		int numberOfVelocities = uVelocities.size();
		
		for (int i = 0; i < numberOfVelocities; ++i) {
			double uPrime = uVelocities.elementAt(i) - uBar;
			double vPrime = vVelocities.elementAt(i) - vBar;
			double wPrime = wVelocities.elementAt(i) - wBar;
			
			double uPrimeSquared = Math.pow(uPrime, 2);
			double vPrimeSquared = Math.pow(vPrime, 2);
			double wPrimeSquared = Math.pow(wPrime, 2);
			
			uPrimeSquaredSum += uPrimeSquared;
			vPrimeSquaredSum += vPrimeSquared;
			wPrimeSquaredSum += wPrimeSquared;
			
			double tke = 0.5 * (uPrimeSquared + vPrimeSquared + wPrimeSquared);
			
			tkeSum += tke;
			
			uTKEFluxSum += uPrime * tke;
			vTKEFluxSum += vPrime * tke;
			wTKEFluxSum += wPrime * tke;
			
			for (int uPrimePower = 0; uPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++uPrimePower) {
				for (int vPrimePower = 0; vPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++vPrimePower) {
					for (int wPrimePower = 0; wPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++wPrimePower) {
						if (uPrimePower + vPrimePower + wPrimePower != 3) {
							thirdOrderCorrelationNumeratorSums[uPrimePower][vPrimePower][wPrimePower] = Double.NaN;
						} else {
							thirdOrderCorrelationNumeratorSums[uPrimePower][vPrimePower][wPrimePower] += Math.pow(uPrime, uPrimePower) * Math.pow(vPrime, vPrimePower) * Math.pow(wPrime, wPrimePower);
						}
					}
				}
			}
			
			double[] primes = {uPrime, vPrime, wPrime};
			for (int row = 0; row < anisotropicStressTensorNumeratorSums.length; ++row) {
				for (int col = 0; col < anisotropicStressTensorNumeratorSums[0].length; ++col) {
					anisotropicStressTensorNumeratorSums[row][col] += primes[row] * primes[col];
				}
			}
		}

		Double uPrimeRMS = Math.sqrt(uPrimeSquaredSum/numberOfVelocities);
		double vPrimeRMS = Math.sqrt(vPrimeSquaredSum/numberOfVelocities);
		double wPrimeRMS = Math.sqrt(wPrimeSquaredSum/numberOfVelocities);
		mSummary.set(BackEndAPI.DPS_KEY_RMS_U_PRIME, uPrimeRMS);
		mSummary.set(BackEndAPI.DPS_KEY_RMS_V_PRIME, vPrimeRMS);
		mSummary.set(BackEndAPI.DPS_KEY_RMS_W_PRIME, wPrimeRMS);
		mSummary.set(BackEndAPI.DPS_KEY_TKE, tkeSum/numberOfVelocities);
		mSummary.set(BackEndAPI.DPS_KEY_U_TKE_FLUX, uTKEFluxSum/numberOfVelocities);
		mSummary.set(BackEndAPI.DPS_KEY_V_TKE_FLUX, vTKEFluxSum/numberOfVelocities);
		mSummary.set(BackEndAPI.DPS_KEY_W_TKE_FLUX, wTKEFluxSum/numberOfVelocities);

		for (int uPrimePower = 0; uPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++uPrimePower) {
			for (int vPrimePower = 0; vPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++vPrimePower) {
				for (int wPrimePower = 0; wPrimePower < BackEndAPI.THIRD_ORDER_CORRELATION_DIMENSION; ++wPrimePower) {
					mSummary.set(BackEndAPI.DPS_KEY_THIRD_ORDER_CORRELATION_KEYS_ARRAY[uPrimePower][vPrimePower][wPrimePower], (thirdOrderCorrelationNumeratorSums[uPrimePower][vPrimePower][wPrimePower]/numberOfVelocities)/(Math.pow(uPrimeRMS, uPrimePower) * Math.pow(vPrimeRMS, vPrimePower) * Math.pow(wPrimeRMS, wPrimePower)));
				}
			}
		}
		
		for (int row = 0; row < anisotropicStressTensorNumeratorSums.length; ++row) {
			for (int col = 0; col < anisotropicStressTensorNumeratorSums[0].length; ++col) {
				double kronecker = row == col ? 1d : 0d;
				mSummary.set(BackEndAPI.DPS_KEY_ANISOTROPIC_STRESS_TENSOR_ARRAY[row][col], (anisotropicStressTensorNumeratorSums[row][col]/(2d*tkeSum)) - kronecker/3d);
			}
		}
	}

	/**
	 * Calculates the correlation between the fixed probe and mobile probe measurements
	 */
	private void calculateFixedProbeCorrelations() {
		calculateFixedProbeCorrelation(BackEndAPI.DPS_KEY_FIXED_PROBE_U_CORRELATION);
		calculateFixedProbeCorrelation(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION);
		calculateFixedProbeCorrelation(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION);
	}
	
	/**
	 * Calculates the correlation between the fixed probe and mobile probe measurements
	 * @param correlationKey The correlation to calculate (u, v or w)
	 */
	private void calculateFixedProbeCorrelation(DataPointSummaryIndex correlationKey) {
		DataPointDetailIndex velocitiesKey = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES;
		DataPointSummaryIndex meanVelocityKey = BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY;
		DataPointSummaryIndex stDevKey = BackEndAPI.DPS_KEY_U_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV;
		
		if (correlationKey.equals(BackEndAPI.DPS_KEY_FIXED_PROBE_V_CORRELATION)) {
			velocitiesKey = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES;
			meanVelocityKey = BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY;
			stDevKey = BackEndAPI.DPS_KEY_V_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV;
		} else if (correlationKey.equals(BackEndAPI.DPS_KEY_FIXED_PROBE_W_CORRELATION)) {
			velocitiesKey = BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES;
			meanVelocityKey = BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY;
			stDevKey = BackEndAPI.DPS_KEY_W_FILTERED_AND_TRANSLATED_AND_BATCH_RC_ST_DEV;
		}
		
		Vector<Double> velocities = mDetail.get(velocitiesKey);
		DataPointSummary fixedProbeSummary = new DataPointSummary();
		DataPointDetail fixedProbeDetail = new DataPointDetail();
		double fixedProbeCorrelation = Double.NaN;		

		if (loadLinkedDataSetDataPointDetails(fixedProbeSummary, fixedProbeDetail)) {
			Vector<Double> fixedProbeVelocities = fixedProbeDetail.get(velocitiesKey);
			double meanVelocity = mSummary.get(meanVelocityKey);
			double fixedProbeMeanVelocity = fixedProbeSummary.get(meanVelocityKey);
			double stDev = mSummary.get(stDevKey);
			double fixedProbeStDev = fixedProbeSummary.get(stDevKey);

			fixedProbeCorrelation = MAJFCMaths.correlation(velocities, meanVelocity, stDev, fixedProbeVelocities, fixedProbeMeanVelocity, fixedProbeStDev);
		}
		
		mSummary.set(correlationKey, fixedProbeCorrelation);
	}
	
	/**
	 * Loads the details and summary for the data points in any linked data sets
	 * @param linkedSummary The summary object to copy the fixed probe summary into
	 * @param linkedDetail The detail object to copy the fixed probe detail into
	 * @return true if the data is successfully loaded, false if the fixed probe data point was not found
	 */
	private boolean loadLinkedDataSetDataPointDetails(DataPointSummary linkedSummary, DataPointDetail linkedDetail) {
		Vector<LinkedDataSet> linkedDataSets = mDataSet.getAllLinkedDataSets();
		LinkedDataSet linkedDataSet = null;

		int numberOfLinkedDataSets = linkedDataSets.size();
		
		for (int i = 0; i < numberOfLinkedDataSets; ++i) {
			linkedDataSet = linkedDataSets.elementAt(i);
			
			try {
				linkedDataSet.loadDataPointDetails(mYCoord, mZCoord, linkedSummary, linkedDetail, true);
//				linkedDataSet.clearDataPointDetails(mYCoord, mZCoord);
			} catch (BackEndAPIException theException) {
				linkedDataSet = null;
				return false;
			}
			
			// If we found the point don't keep looking
//			return true;
		}
		
		return true;
	}

	/**
	 * Writes a full XML representation of all the measurements to a file
	 * @param fileWriter The file writer to write the XML to
	 * @param dpdIndex The index of the velocities to write
	 * @return All the measurements for this component in XML form or null if there are no measurements (as this point hasn't been loaded during this session)
	 * @throws IOException 
	 */
	public void writeXMLDetailRepresentation(FileWriter fileWriter, DataPointDetailIndex dpdIndex) throws IOException {
		Vector<Double> fieldMeasurements = mDetail.get(dpdIndex);
		Vector<Double> correlations = null;
		Vector<Double> snrs = null;
		Vector<Double> wDiffs = null;
		Vector<Double> tVelocities = null, ftVelocities = null, ftrcVelocities = null; 
		String name = null;
		boolean isVelocity = true;
		String firstAttributeName = DADefinitions.XML_DATA_POINT_RAW_VELOCITY;
		
		if (dpdIndex.equals(BackEndAPI.DPD_KEY_RAW_X_VELOCITIES)) {
			name = DADefinitions.XML_DATA_POINT_X_COMPONENT;
			correlations = mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_CORRELATIONS);
			snrs = mDetail.get(BackEndAPI.DPD_KEY_X_SIGNAL_SNRS);
			tVelocities = mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_X_VELOCITIES);
			ftVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_X_VELOCITIES);
			ftrcVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_X_VELOCITIES);
		} else if (dpdIndex.equals(BackEndAPI.DPD_KEY_RAW_Y_VELOCITIES)) {
			name = DADefinitions.XML_DATA_POINT_Y_COMPONENT;
			correlations = mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_CORRELATIONS);
			snrs = mDetail.get(BackEndAPI.DPD_KEY_Y_SIGNAL_SNRS);
			tVelocities = mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_Y_VELOCITIES);
			ftVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Y_VELOCITIES);
			ftrcVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Y_VELOCITIES);
		} else if (dpdIndex.equals(BackEndAPI.DPD_KEY_RAW_Z_VELOCITIES)) {
			name = DADefinitions.XML_DATA_POINT_Z_COMPONENT;
			correlations = mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_CORRELATIONS);
			snrs = mDetail.get(BackEndAPI.DPD_KEY_Z_SIGNAL_SNRS);
			wDiffs = mDetail.get(BackEndAPI.DPD_KEY_W_DIFF);
			tVelocities = mDetail.get(BackEndAPI.DPD_KEY_TRANSLATED_Z_VELOCITIES);
			ftVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_Z_VELOCITIES);
			ftrcVelocities = mDetail.get(BackEndAPI.DPD_KEY_FILTERED_AND_TRANSLATED_AND_BATCH_RC_Z_VELOCITIES);
		} else if (dpdIndex.equals(BackEndAPI.DPD_KEY_PRESSURE)) {
			name = DADefinitions.XML_DATA_POINT_PRESSURE;
			isVelocity = false;
			firstAttributeName = DADefinitions.XML_DATA_POINT_PRESSURE;
		}
		
		if (fieldMeasurements.size() == 0) {
			return;
		}
		
		fileWriter.write(MAJFCTools.makeXMLStartTag(name, true));
		int numberOfValues = fieldMeasurements.size();

		for (int i = 0; i < numberOfValues; ++i) {
			DAFileOutputStringBuffer theXML = new DAFileOutputStringBuffer();
			
			theXML.append(MAJFCTools.SYSTEM_TAB_CHAR);
			theXML.append(MAJFCTools.makeXMLStartTag(DADefinitions.XML_DATA_POINT_VALUE, false));
			theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(firstAttributeName, fieldMeasurements.elementAt(i)));
			
			if (isVelocity) {
				theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_DATA_POINT_CORRELATION, correlations.elementAt(i)));
				theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_DATA_POINT_SNR, snrs.elementAt(i)));
				if (wDiffs != null) {
					theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_DATA_POINT_W_DIFF, wDiffs.elementAt(i)));
				}
				theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_DATA_POINT_TRANSLATED_VELOCITY, tVelocities.elementAt(i)));
				theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_DATA_POINT_FILTERED_AND_TRANSLATED_VELOCITY, ftVelocities.elementAt(i)));
				theXML.append(DAFileOutputStringBuffer.makeXMLInternalAttribute(DADefinitions.XML_DATA_POINT_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY, ftrcVelocities.elementAt(i)));
			}
			
			theXML.append(MAJFCTools.makeXMLEndTag(DADefinitions.XML_DATA_POINT_VALUE, false));
			theXML.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
			
			fileWriter.write(theXML.toString());
		}

		fileWriter.write(MAJFCTools.makeXMLEndTag(name, true));
	}
	
	/**
	 * Calculates the translated velocities. If the untranslated velocities are the raw velocities (untranslatedXVelocitiesIndex == BackEndAPI.DPD_KEY_RAW_X_VELOCITIES)
	 * then unit conversion is also applied, based on the configuration "Data Scaling Factor".
	 * Requires all three measured velocity data sets to be complete before calling
	 * 
	 * @param untranslatedXVelocitiesIndex The index of the DataPointDetail store from which to read the untranslated x-velocities
	 * @param untranslatedYVelocitiesIndex The index of the DataPointDetail store from which to read the untranslated y-velocities
	 * @param untranslatedZVelocitiesIndex The index of the DataPointDetail store from which to read the untranslated z-velocities
	 * @param translatedXVelocitiesIndex The index of the DataPointDetail store in which to store the translated x-velocities
	 * @param translatedYVelocitiesIndex The index of the DataPointDetail store in which to store the translated y-velocities
	 * @param translatedZVelocitiesIndex The index of the DataPointDetail store in which to store the translated z-velocities
	 * @param includeProbeOrientationTranslation If true then the probe orientation angle is included in the translation
	 * @param includeBatchRotationCorrection If true then the batch rotation correction is included in the translation
	 * @param includeAxisInversion If true then the axis inversion settings are checked and included in the translation as necessary
	 */
	private void calculateTranslatedVelocities(DataPointDetailIndex untranslatedXVelocitiesIndex, DataPointDetailIndex untranslatedYVelocitiesIndex, DataPointDetailIndex untranslatedZVelocitiesIndex, DataPointDetailIndex translatedXVelocitiesIndex, DataPointDetailIndex translatedYVelocitiesIndex, DataPointDetailIndex translatedZVelocitiesIndex, boolean includeProbeOrientationTranslation, boolean includeBatchRotationCorrection, boolean includeAxisInversion) {
		// x' = x * cos(theta) * cos(alpha) - y * sin(alpha) * cos(theta) + z * sin(theta) * cos(alpha)
		// y' = x * sin(alpha) * cos(phi) + y * cos(alpha) * cos(phi) - z * sin(phi) * cos(alpha)
		// z' = -x * cos(theta) * cos(phi) + y * cos(theta) * sin(phi) + z * cos(theta) * cos(phi)
		try {
			mDetail.get(translatedXVelocitiesIndex).removeAllElements();
			mDetail.get(translatedYVelocitiesIndex).removeAllElements();
			mDetail.get(translatedZVelocitiesIndex).removeAllElements();
			
			Vector<Double> xVelocities = mDetail.get(untranslatedXVelocitiesIndex);
			Vector<Double> yVelocities = mDetail.get(untranslatedYVelocitiesIndex);
			Vector<Double> zVelocities = mDetail.get(untranslatedZVelocitiesIndex);
			
			int numberOfVelocities = xVelocities.size();
			
			// The rotation angles are the *clockwise* rotation of the probe to the original axes. Therefore we are translating to a
			// a co-ordinate system which is *anti-clockwise* rotated from the probe axes, i.e. through the negative angles
			double theta = 0, phi = 0, alpha = 0;
			
			if (includeProbeOrientationTranslation) {
				theta += Math.toRadians(mSummary.get(BackEndAPI.DPS_KEY_XZ_PLANE_ROTATION_THETA));
				phi += Math.toRadians(mSummary.get(BackEndAPI.DPS_KEY_YZ_PLANE_ROTATION_PHI));
				alpha += Math.toRadians(mSummary.get(BackEndAPI.DPS_KEY_XY_PLANE_ROTATION_ALPHA));
			}
			
			if (includeBatchRotationCorrection) {
				theta += Math.toRadians(mSummary.get(BackEndAPI.DPS_KEY_BATCH_THETA_ROTATION_CORRECTION));
				phi += Math.toRadians(mSummary.get(BackEndAPI.DPS_KEY_BATCH_PHI_ROTATION_CORRECTION));
				alpha += Math.toRadians(mSummary.get(BackEndAPI.DPS_KEY_BATCH_ALPHA_ROTATION_CORRECTION));
				
			}

			double unitScaleFactor = untranslatedXVelocitiesIndex.equals(BackEndAPI.DPD_KEY_RAW_X_VELOCITIES) ? mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_VELOCITY_UNIT_SCALE_FACTOR) : 1.0;

			for (int i = 0; i < numberOfVelocities; ++i) {
				double[] untranslatedVelocities = {	xVelocities.elementAt(i),
													yVelocities.elementAt(i),
													zVelocities.elementAt(i) };
			
				// Axis inversion - after this, the data should be in left-handed axes.
				if (includeAxisInversion) {
					if (mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_X_AXIS_INVERTED) > 0) {
						untranslatedVelocities[0] *= -1;
					}
	
					if (mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_Y_AXIS_INVERTED) > 0) {
						untranslatedVelocities[1] *= -1;
					}
	
					if (mDataSet.getConfigData().get(BackEndAPI.DSC_KEY_Z_AXIS_INVERTED) > 0) {
						untranslatedVelocities[2] *= -1;
					}
				}

				double[] translatedVelocities = MAJFCMaths.translateToRotatedAxes(untranslatedVelocities, phi, theta, alpha);

				// Need to adjust for the scale factor too
				mDetail.get(translatedXVelocitiesIndex).add(translatedVelocities[0]/unitScaleFactor);
				mDetail.get(translatedYVelocitiesIndex).add(translatedVelocities[1]/unitScaleFactor);
				mDetail.get(translatedZVelocitiesIndex).add(translatedVelocities[2]/unitScaleFactor);
			}
		} catch (Exception theException) {
			MAJFCLogger.log("Error when calculating translated velocities for " + mYCoord + '-' + mZCoord + ' ');
			mDetail.get(translatedXVelocitiesIndex).removeAllElements();
			mDetail.get(translatedYVelocitiesIndex).removeAllElements();
			mDetail.get(translatedZVelocitiesIndex).removeAllElements();
		}
	}

	/**
	 * Trims the data to the specified range
	 * @param dataSetFile
	 * @param startIndex
	 * @param endIndex
	 * @throws BackEndAPIException 
	 */
	public void trim(File dataSetFile, int startIndex, int endIndex, DataPointSummary dataPointSummary, DataPointDetail dataPointDetail) throws BackEndAPIException {
		synchronized (mDataLock) {
			loadDetails(dataSetFile);
			
			Vector<DataPointDetailIndex> keys = mDetail.getKeys();
			int numberOfKeys = keys.size();
			for (int i = 0; i < numberOfKeys; ++i) {
				DataPointDetailIndex key = keys.elementAt(i);
				Vector<Double> fieldValues = mDetail.get(key);
				
				if (fieldValues == null || fieldValues.size() <= endIndex) {
					continue;
				}
				
				List<Double> subList = fieldValues.subList(startIndex, endIndex);
				
				int subListLength = subList.size();
				Vector<Double> subVector = new Vector<Double>(subListLength);
				for (int j = 0; j < subListLength; ++j) {
					subVector.add(subList.get(j));
				}
				
				mDetail.set(key, subVector);
			}
			
			// Calculate will also save to a temporary file
			calculate(false, false);
			
			mSummary.copyInto(dataPointSummary);
			mDetail.copyInto(dataPointDetail);

			clearDetails();
		}
	}
}
