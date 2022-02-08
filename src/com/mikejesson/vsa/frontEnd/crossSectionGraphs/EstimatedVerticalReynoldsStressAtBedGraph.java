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

package com.mikejesson.vsa.frontEnd.crossSectionGraphs;


import java.awt.HeadlessException;

import java.util.LinkedList;
import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCFitPoly;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class EstimatedVerticalReynoldsStressAtBedGraph extends AbstractCrossSectionGraph {
	private final String[] SERIES_KEYS;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 */
	public EstimatedVerticalReynoldsStressAtBedGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent) throws HeadlessException {
		super(parent, new Object[] {dataSetId}, DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_TITLE), DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_X_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_AT_BED_GRAPH_Y_AXIS_TITLE), null);
		
		SERIES_KEYS = new String[] { 	DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_FULL_SERIES_LINEAR_EXTRAPOLATION_SERIES_LABEL),
										DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_BOTTOM_POINTS_LINEAR_EXTRAPOLATION_SERIES_LABEL),
										DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_MEAN_OF_BOTTOM_THREE_POINTS_SERIES_LABEL),
										DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_SMOOTH_BED_LOG_LAW_SERIES_LABEL),
										DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_KS_FROM_ROUGH_BED_LOG_LAW_SERIES_LABEL),
										DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_10_SERIES_LABEL),
										DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_Y_EQUALS_20_SERIES_LABEL),
										DAStrings.getString(DAStrings.VERTICAL_REYNOLDS_STRESS_ESTIMATE_FROM_POINTS_ABOVE_0_POINT_2LINEAR_EXTRAPOLATION_SERIES_LABEL)};
	}

	@Override
	/**
	 * @see AbstractCrossSectionGraph#prepareDataArray
	 */
	protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets) {
		mData = new double[sortedYCoords.size()][9];

		double linearExtrapolationEstimateAllPoints = Double.NaN, linearExtrapolationEstimatePointsAbove0Point2 = Double.NaN, linearExtrapolationEstimateBottomTwoPoints = Double.NaN;
		double meanOfBottomThreePoints = Double.NaN;
		double zEquals10Value = Double.NaN, zEquals20Value = Double.NaN;
		double waterDepth = Double.NaN;
		DataSetConfig dataSetConfig = null;
		
		try {
			dataSetConfig = DAFrame.getBackEndAPI().getConfigData(dataSetId);
			waterDepth = dataSetConfig.get(BackEndAPI.DSC_KEY_WATER_DEPTH);
		} catch (Exception theException) {
			theException.printStackTrace();
			waterDepth = 1d;
		}

		for (int yCoordIndex = 0; yCoordIndex < mData.length; ++yCoordIndex) {
			int yCoord = sortedYCoords.elementAt(yCoordIndex);
			Vector<Integer> sortedZCoords = sortedZCoordsSets.elementAt(yCoordIndex);
			int numberOfZCoords = sortedZCoords.size();

			double[] zCoordsForFit = new double[numberOfZCoords];
			double[] uPrimeWPrimesForFit = new double[numberOfZCoords];
			LinkedList<Double> uPrimeWPrimesForFitList = new LinkedList<Double>();
			LinkedList<Double> shearVelocitiesFromLogLaw = new LinkedList<Double>();
			LinkedList<Double> roughSideKsFromLogLaw = new LinkedList<Double>();
			
			LinkedList<Double> zCoordsAbove0Point2ForFitList = new LinkedList<Double>();
			LinkedList<Double> uPrimeWPrimesPointsAbove0Point2ForFitList = new LinkedList<Double>();

			double fluidDensity = 0;
			try {
				fluidDensity = DAFrame.getBackEndAPI().getConfigData(dataSetId).get(BackEndAPI.DSC_KEY_FLUID_DENSITY);
			} catch (BackEndAPIException theException) {
				
			}
			
			for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
				int zCoord = sortedZCoords.elementAt(zCoordIndex);
				zCoordsForFit[zCoordIndex] = zCoord;
				
				try {
					uPrimeWPrimesForFit[zCoordIndex] = -fluidDensity * DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_PRIME_W_PRIME_MEAN);
					uPrimeWPrimesForFitList.addLast(uPrimeWPrimesForFit[zCoordIndex]);

					double uMean = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					Double shearVelocity = calculateSmoothSideShearVelocityFromLogLaw(uMean, zCoord);
					shearVelocitiesFromLogLaw.addLast(shearVelocity);
					if (shearVelocity.equals(Double.NaN) == false) {
						roughSideKsFromLogLaw.addLast(calculateRoughSideKs(uMean, shearVelocity, zCoord));
					}
					
					if (zCoord == 10) {
						zEquals10Value = uPrimeWPrimesForFit[zCoordIndex];
					}
					
					if (zCoord == 20) {
						zEquals20Value = uPrimeWPrimesForFit[zCoordIndex];
					}
					
					if (zCoord/waterDepth > 0.2) {
						zCoordsAbove0Point2ForFitList.addLast((double) zCoord);
						uPrimeWPrimesPointsAbove0Point2ForFitList.addLast(uPrimeWPrimesForFitList.getLast());
					}
				} catch (BackEndAPIException theException) {
				}
			}
			
			double[] zCoordsAbove0Point2ForFit = new double[zCoordsAbove0Point2ForFitList.size()];
			double[] uPrimeWPrimesForPointsAbove0Point2Fit = new double[zCoordsAbove0Point2ForFit.length];
			
			for (int i = 0; i < zCoordsAbove0Point2ForFit.length; ++i) {
				zCoordsAbove0Point2ForFit[i] = zCoordsAbove0Point2ForFitList.get(i);
				uPrimeWPrimesForPointsAbove0Point2Fit[i] = uPrimeWPrimesPointsAbove0Point2ForFitList.get(i);
			}
		
			double fitParametersAllPoints[] = new double[4];
			double fitParametersPointsAbove0Point2Points[] = new double[4];
			double fitParametersBottomTwoPoints[] = new double[4];
			
			try {
				double bedHeight = dataSetConfig.getBoundaryZAt(yCoord);
				MAJFCFitPoly.fit(fitParametersAllPoints, zCoordsForFit, uPrimeWPrimesForFit, null, null, numberOfZCoords);
				linearExtrapolationEstimateAllPoints = calculateFromPolynomial(bedHeight, fitParametersAllPoints, 2);
				
				MAJFCFitPoly.fit(fitParametersPointsAbove0Point2Points, zCoordsAbove0Point2ForFit, uPrimeWPrimesForPointsAbove0Point2Fit, null, null, zCoordsAbove0Point2ForFit.length);
				linearExtrapolationEstimatePointsAbove0Point2 = calculateFromPolynomial(bedHeight, fitParametersPointsAbove0Point2Points, 2);
				
				MAJFCFitPoly.fit(fitParametersBottomTwoPoints, zCoordsForFit, uPrimeWPrimesForFit, null, null, 2);
				linearExtrapolationEstimateBottomTwoPoints = calculateFromPolynomial(bedHeight, fitParametersBottomTwoPoints, 2);
				
				meanOfBottomThreePoints = MAJFCMaths.mean(uPrimeWPrimesForFitList, 3);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mData[yCoordIndex][Y_COORD_INDEX] = yCoord;
			mData[yCoordIndex][DATUM_INDEX] = linearExtrapolationEstimateAllPoints;
			mData[yCoordIndex][DATUM_INDEX + 1] = linearExtrapolationEstimateBottomTwoPoints;
			mData[yCoordIndex][DATUM_INDEX + 2] = meanOfBottomThreePoints;
			mData[yCoordIndex][DATUM_INDEX + 3] = Math.pow(MAJFCMaths.mean(shearVelocitiesFromLogLaw), 2) * fluidDensity;
			mData[yCoordIndex][DATUM_INDEX + 4] = MAJFCMaths.mean(roughSideKsFromLogLaw);
			mData[yCoordIndex][DATUM_INDEX + 5] = zEquals10Value;
			mData[yCoordIndex][DATUM_INDEX + 6] = zEquals20Value;
			mData[yCoordIndex][DATUM_INDEX + 7] = linearExtrapolationEstimatePointsAbove0Point2;
		}
	}
	
	/**
	 * Calculates y from y = A(n)x^n + A(n-1)x^(n-1)....+ A(1)*x + A(0)
	 * @param x
	 * @param polynomialCoefficients The A coeffiecients.
	 * @return y as calculated
	 */
	private double calculateFromPolynomial(double x, double[] polynomialCoefficients, int polynomialOrder) {
		double result = 0;
		polynomialOrder = Math.min(polynomialOrder, polynomialCoefficients.length);
		
		for (int i = 0; i < polynomialOrder; ++i) {
			result += polynomialCoefficients[i]*Math.pow(x, i);
		}
		
		return result;
	}

	private double calculateSmoothSideShearVelocityFromLogLaw(double uMean, int zCoord) {
		double shearVelocity = 0.2;
		double oldShearVelocity = Double.MAX_VALUE;
		double zPlus = Double.MAX_VALUE;
		final double precision = 1e-6;
		double kinematicViscosity = 1.0;
		try {
			kinematicViscosity = DAFrame.getBackEndAPI().getConfigData(getDataSetId()).get(BackEndAPI.DSC_KEY_FLUID_KINEMATIC_VISCOSITY);
			kinematicViscosity *= 1E-6;
		} catch (BackEndAPIException theException) {
		}

		while (Math.abs(oldShearVelocity - shearVelocity) > precision) {
			oldShearVelocity = shearVelocity;
			zPlus = (((double) zCoord)/1000) * oldShearVelocity / kinematicViscosity;
			shearVelocity = uMean/((1/DADefinitions.VON_KARMANS_CONSTANT) * Math.log(zPlus) + 5);
		}
		
		double waterDepth = -1;
		try {
			waterDepth = DAFrame.getBackEndAPI().getConfigData(getDataSetId()).get(BackEndAPI.DSC_KEY_WATER_DEPTH);
		} catch (Exception e) {
		}
		
		double rStar = shearVelocity * (waterDepth/1000)/kinematicViscosity;
		if (zPlus <= DADefinitions.LOG_LAW_DAMPING_FACTOR_B || zPlus > 0.2 * rStar) {
			return Double.NaN;
		}
		
		return shearVelocity;
	}

	private double calculateRoughSideKs(double uMean, double shearVelocity, int zCoord) {
		double exponent = -DADefinitions.VON_KARMANS_CONSTANT * (uMean/shearVelocity - 8.5);
		
		return (((double) zCoord)/1000) * Math.exp(exponent);
	}
	
	@Override
	/**
	 * XYDataset implementation
	 */
	public double getYValue(int series, int item) {
		return mData[item][DATUM_INDEX + series];
	}

	@SuppressWarnings("rawtypes")
	@Override
	/**
	 * XYDataset implementation
	 */
	public Comparable getSeriesKey(int series) {
		return SERIES_KEYS[series];
	}
}
