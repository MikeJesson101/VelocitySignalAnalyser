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


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.mikejesson.majfc.helpers.MAJFCFitPoly;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.CharacteristicScalerType;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.PSTReplacementMethod;
import com.mikejesson.vsa.widgits.DADefinitions;



//import Jama.Matrix;

/**
 * @author mikefedora
 *
 */
public class DespikingFilters {
	private static DespikingFilters sThis;
	
	/**
	 * Single instance instantiator
	 * @return The only instance of DespikingFilters
	 */
	public static DespikingFilters getDespikingFilters() {
		if (sThis == null) {
			sThis = new DespikingFilters();
		}
		
		return sThis;
	}
	
	/**
	 * Hidden constructor
	 */
	private DespikingFilters() {
	}
	
	/**
	 * Filter the given velocities using the Velocity Correlation Filter
	 * (Cea, Puertas and Pena, 2007, Velocity Measurements on Highly Turbulent Free Surface Flow Using ADV, Exp Fluids, 42, 333-348))
	 * 
	 * @param velocities The velocities to filter
	 * @param samplingRate The rate at which the velocity measurements were made
	 * @return The number of velocities removed 
	 */
	@SuppressWarnings("unchecked")
	public ComponentDataHolder<Integer> velocityCorrelation(ComponentDataHolder<Vector<Double>> velocities, BackEndAPI.PSTReplacementMethod pstReplacementMethod, double samplingRate) {
		ComponentDataHolder<Vector<Double>> highPassFilteredVelocities = new ComponentDataHolder<Vector<Double>>(MAJFCMaths.highPassFilter(velocities.getXComponent(), true, 1d, 1d/samplingRate), MAJFCMaths.highPassFilter(velocities.getYComponent(), true, 1d, 1d/samplingRate), MAJFCMaths.highPassFilter(velocities.getZComponent(), true, 1d, 1d/samplingRate));
		Vector<VelocityDatumInfo> removedVelocitiesList = new Vector<VelocityDatumInfo>(1000);
		Hashtable<VelocityDatumInfo, VelocityDatumInfo> removedVelocitiesLookup = new Hashtable<VelocityDatumInfo, VelocityDatumInfo>(1000);
		int totalNumberOfRemovedVelocities = 0;
		
		do {
			double[] meanVelocities = {	MAJFCMaths.mean(highPassFilteredVelocities.getXComponent()),
										MAJFCMaths.mean(highPassFilteredVelocities.getYComponent()),
										MAJFCMaths.mean(highPassFilteredVelocities.getZComponent())};
			
			ComponentDataHolder<Vector<Double>> fluctuations = new ComponentDataHolder<Vector<Double>>(new Vector<Double>(highPassFilteredVelocities.getXComponent().size()), new Vector<Double>(highPassFilteredVelocities.getYComponent().size()), new Vector<Double>(highPassFilteredVelocities.getZComponent().size()));

			// Calculate fluctuations
			int numberOfVelocities = highPassFilteredVelocities.getXComponent().size();
			for (int i = 0; i < numberOfVelocities; ++i) {
				Double xVelocity = highPassFilteredVelocities.getXComponent().elementAt(i);
				Double yVelocity = highPassFilteredVelocities.getYComponent().elementAt(i);
				Double zVelocity = highPassFilteredVelocities.getZComponent().elementAt(i);
				
				if (xVelocity == DADefinitions.REMOVED_VALUE) {
					fluctuations.getXComponent().add(DADefinitions.REMOVED_VALUE);
				} else {
					fluctuations.getXComponent().add(xVelocity - meanVelocities[0]);
				}

				if (yVelocity == DADefinitions.REMOVED_VALUE) {
					fluctuations.getYComponent().add(DADefinitions.REMOVED_VALUE);
				} else {
					fluctuations.getYComponent().add(yVelocity - meanVelocities[1]);
				}

				if (zVelocity == DADefinitions.REMOVED_VALUE) {
					fluctuations.getZComponent().add(DADefinitions.REMOVED_VALUE);
				} else {
					fluctuations.getZComponent().add(zVelocity - meanVelocities[2]);
				}
			}
			
			double[] velocityStDevs = {	MAJFCMaths.standardDeviation(highPassFilteredVelocities.getXComponent()),
										MAJFCMaths.standardDeviation(highPassFilteredVelocities.getYComponent()),
										MAJFCMaths.standardDeviation(highPassFilteredVelocities.getZComponent())};
			
			removedVelocitiesList.removeAllElements();
			removedVelocitiesLookup.clear();
			
			filterWithEllipse(fluctuations.getXComponent(), fluctuations.getYComponent(), velocityStDevs[0], velocityStDevs[1], removedVelocitiesLookup, removedVelocitiesList);
			filterWithEllipse(fluctuations.getXComponent(), fluctuations.getZComponent(), velocityStDevs[0], velocityStDevs[2], removedVelocitiesLookup, removedVelocitiesList);
			filterWithEllipse(fluctuations.getYComponent(), fluctuations.getZComponent(), velocityStDevs[1], velocityStDevs[2], removedVelocitiesLookup, removedVelocitiesList);
			
			// Remove the appropriate velocities.
			int numberOfRemovedVelocities = removedVelocitiesList.size();
			for (int i = 0; i < numberOfRemovedVelocities; ++i) {
				// Set these to REMOVED_VALUE so they are removed from the list of velocities which will be returned at the end
				velocities.getXComponent().set(removedVelocitiesList.elementAt(i).mIndex, DADefinitions.REMOVED_VALUE);
				velocities.getYComponent().set(removedVelocitiesList.elementAt(i).mIndex, DADefinitions.REMOVED_VALUE);
				velocities.getZComponent().set(removedVelocitiesList.elementAt(i).mIndex, DADefinitions.REMOVED_VALUE);

				// Set these to REMOVED_VALUE for the next iteration
				highPassFilteredVelocities.getXComponent().set(removedVelocitiesList.elementAt(i).mIndex, DADefinitions.REMOVED_VALUE);
				highPassFilteredVelocities.getYComponent().set(removedVelocitiesList.elementAt(i).mIndex, DADefinitions.REMOVED_VALUE);
				highPassFilteredVelocities.getZComponent().set(removedVelocitiesList.elementAt(i).mIndex, DADefinitions.REMOVED_VALUE);
			}
			
			totalNumberOfRemovedVelocities += removedVelocitiesList.size();
		} while (removedVelocitiesList.size() > 0);
		
		// Replace any removed values at the start of the data with the mean value
		replaceRemovedValuesBeforeFirstGoodValueWithMeanValue(velocities.getXComponent(), velocities.getYComponent(), velocities.getZComponent());
		
		// Replace the removed values using simple interpolation between the previous and next values
		replaceRemovedValues(velocities.getXComponent(), pstReplacementMethod);
		replaceRemovedValues(velocities.getYComponent(), pstReplacementMethod);
		replaceRemovedValues(velocities.getZComponent(), pstReplacementMethod);
		
		return new ComponentDataHolder<Integer>(totalNumberOfRemovedVelocities, totalNumberOfRemovedVelocities, totalNumberOfRemovedVelocities);
	}
	
	/**
	 * Replaces all REMOVED_VALUEs before the first good value with the mean value
	 * Assumes all velocity vectors are the same size and have values removed at the same indices as the velocities[0] vector
	 * @param velocities The vectors of velocity measurements to replace values in
	 */
	private void replaceRemovedValuesBeforeFirstGoodValueWithMeanValue(Vector<Double> ... velocities) {
		double[] meanVelocities = new double[velocities.length];
		
		for (int velocityVectorIndex = 0; velocityVectorIndex < velocities.length; ++velocityVectorIndex) {
			meanVelocities[velocityVectorIndex] = MAJFCMaths.mean(velocities[velocityVectorIndex]);
		}
		
		int numberOfVelocities = velocities[0].size();
		
		for (int elementIndex = 0; elementIndex < numberOfVelocities; ++elementIndex) {
			if (velocities[0].elementAt(elementIndex) == DADefinitions.REMOVED_VALUE) {
				for (int velocityVectorIndex = 0; velocityVectorIndex < velocities.length; ++velocityVectorIndex) {
					velocities[velocityVectorIndex].setElementAt(meanVelocities[velocityVectorIndex], elementIndex);
				}
			} else {
				break;
			}
		}
	}

	/**
	 * 
	 * @param field1Values
	 * @param field2Values
	 * @param stDev1
	 * @param stDev2
	 * @param removedValuesLookup
	 * @param removedValuesList
	 */
	private void filterWithEllipse(Vector<Double> field1Values, Vector<Double> field2Values, double stDev1, double stDev2, Hashtable<VelocityDatumInfo, VelocityDatumInfo> removedValuesLookup, Vector<VelocityDatumInfo> removedValuesList) {
		filterWithEllipse(field1Values, field2Values, stDev1, stDev2, removedValuesLookup, removedValuesList, field1Values, null, null);
	}
	
	/**
	 * 
	 * @param field1Values
	 * @param field2Values
	 * @param characteristicScalar1
	 * @param characteristicScalar2
	 * @param removedValuesLookup
	 * @param removedValuesList
	 * @param overrideTheta
	 * in field1Values
	 */
	private void filterWithEllipse(Vector<Double> field1Values, Vector<Double> field2Values, double characteristicScalar1, double characteristicScalar2, Hashtable<VelocityDatumInfo, VelocityDatumInfo> removedValuesLookup,  Vector<VelocityDatumInfo> removedValuesList, Vector<Double> limitingValues, Double autoSafeLimit, Double autoExcludeLimit) {
		if (characteristicScalar1 == 0) {
			return;
		}
		
		Vector<Double> field1Squares = new Vector<Double> (), field2Squares = new Vector<Double> ();
		Vector<Double> fieldCrosses = new Vector<Double> ();
		
		int numberOfFieldValues = field1Values.size();
		
		// Calculate the things we need to find the ellipse parameters
		for (int i = 0; i < numberOfFieldValues; ++i) {
			Double field1Value = field1Values.elementAt(i);
			Double field2Value = field2Values.elementAt(i);
			
			if (field1Value == DADefinitions.REMOVED_VALUE || field2Value == DADefinitions.REMOVED_VALUE) {
				field1Squares.add(DADefinitions.REMOVED_VALUE);
				field2Squares.add(DADefinitions.REMOVED_VALUE);
				fieldCrosses.add(DADefinitions.REMOVED_VALUE);
			} else {
				field1Squares.add(Math.pow(field1Value, 2));
				field2Squares.add(Math.pow(field2Value, 2));
				fieldCrosses.add(field1Value * field2Value);
			}
		}
		
		double universalThreshold = Math.sqrt(2 * Math.log(numberOfFieldValues));
		double field1Mean = MAJFCMaths.mean(field1Values);
		double field2Mean = MAJFCMaths.mean(field2Values);
		double limitingValuesMean = MAJFCMaths.mean(limitingValues);
		
		// Ellipse rotation from "field space" axes (clockwise angles +ve)
		double ellipseAxisAngleTheta = -Math.atan(MAJFCMaths.mean(fieldCrosses)/MAJFCMaths.mean(field1Squares));
		double cosTheta = Math.cos(ellipseAxisAngleTheta);
		double sinTheta = Math.sin(ellipseAxisAngleTheta);
		double cosSquaredThetaMinusSinSquaredTheta = Math.pow(cosTheta, 2) - Math.pow(sinTheta, 2);

		// Calculate the lengths of the ellipse's primary axes
		double axis1Length = Math.sqrt((Math.pow(universalThreshold * characteristicScalar1 * cosTheta, 2) - Math.pow(universalThreshold * characteristicScalar2 * sinTheta, 2))/cosSquaredThetaMinusSinSquaredTheta);
		double axis2Length = Math.sqrt((Math.pow(universalThreshold * characteristicScalar2 * cosTheta, 2) - Math.pow(universalThreshold * characteristicScalar1 * sinTheta, 2))/cosSquaredThetaMinusSinSquaredTheta);

		// Do the filtering
		for (int i = 0; i < numberOfFieldValues; ++i) {
			Double field1Value = field1Values.elementAt(i);
			Double field2Value = field2Values.elementAt(i);
			Double limitingValue = limitingValues.elementAt(i);
			
			if (field1Value == DADefinitions.REMOVED_VALUE || field2Value == DADefinitions.REMOVED_VALUE) {
				continue;
			}

			// Check for automatic inclusion
			if (autoSafeLimit != null && Math.abs(limitingValue - limitingValuesMean) < autoSafeLimit) {
				continue;
			}
			
			// Check for automatic exclusion
			boolean exclude = false;
			if (autoExcludeLimit != null && autoExcludeLimit > 0 && Math.abs(limitingValue - limitingValuesMean) > autoExcludeLimit) {
				exclude = true;
			}
			
			if (exclude == false) {
				// The field values are the co-ordinates a "field point" in "field space"
				double[] fieldPoint = { field1Value,
										field2Value,
										0};
							
				// First, translate the origin (the ellipse should be centred on the mean point in "field space")
				fieldPoint[0] -= field1Mean;
				fieldPoint[1] -= field2Mean;
				
				// Translate the field point co-ordinates into the ellipse primary axis co-ordinate system
				double[] fieldPointInEllipseAxes = MAJFCMaths.translateToRotatedAxes(fieldPoint, 0, 0, ellipseAxisAngleTheta);
				
				// Check the point is within the primary axis 1 range
				boolean insideEllipse = Math.abs(fieldPointInEllipseAxes[0]) <= axis1Length;
				
				// If the point is inside the primary axis 1 range, check it's within the primary axis 2 range at this point.
				// The relationship between the two co-ordinates of an ellipse is used to calculate the primary axis 2 range.
				if (insideEllipse) {
					double primaryAxis2Range = axis2Length * Math.sqrt(1 - Math.pow(fieldPointInEllipseAxes[0]/axis1Length, 2));
					
					insideEllipse = Math.abs(fieldPointInEllipseAxes[1]) <= primaryAxis2Range;
					
//					if (Math.abs(fieldPointInEllipseAxes[1]) <= primaryAxis2Range == false) {
//						System.out.println(i);
//					}
				}
				
				if (insideEllipse == false) {
					exclude = true;
				}
			}
			
			if (exclude == true) {
				VelocityDatumInfo velocityDatumInfo = new VelocityDatumInfo(i, field1Value, field2Value);
				if (removedValuesLookup.put(velocityDatumInfo, velocityDatumInfo) == null) {
					removedValuesList.add(velocityDatumInfo);
				}
			}
		}
	}
	
	/**
	 * Helper for the Velocity Correlation Filter
	 * Replaces the removed values (indicated by a value of DADefinitions.REMOVED_VALUE) by interpolating between the last good value before
	 * the removed value(s) and the first good point after the removed value(s)
	 * Any removed values before the first good value are not replaced, but retain the value DADefinitions.REMOVED_VALUE
	 * @param velocities The velocities to filter
	 * @param pstReplacementMethod The replacement method to use
	 */
	private void replaceRemovedValues(List<Double> velocities, BackEndAPI.PSTReplacementMethod pstReplacementMethod) {
		if (pstReplacementMethod.equals(BackEndAPI.PRM_NONE)) {
			return;
		}
		
		int numberOfVelocities = velocities.size();
		Double lastGoodValue = DADefinitions.REMOVED_VALUE;
		int lastGoodValueIndex = -1;
		int failedFitPolyCount = 0;

		for (int i = 0; i < numberOfVelocities; ++i) {
			Double thisValue = velocities.get(i);
			
			if (thisValue.equals(DADefinitions.REMOVED_VALUE) == false) {
				// See if we've removed any values between the last good value and this good value				
				if (i - lastGoodValueIndex > 1) {
					// Check to see if this is the first non-removed value, in which case we must fill all
					// preceding removed values with this value
					if (lastGoodValueIndex == -1) {
						lastGoodValue = thisValue;
					}

					// Fill the removed values
					int numberOfNonGoodValuesSinceLastGoodValue = i - (lastGoodValueIndex + 1);
					double replacementValueStep = ((thisValue - lastGoodValue)/(i - lastGoodValueIndex));

					if (pstReplacementMethod.equals(BackEndAPI.PRM_LINEAR_INTERPOLATION)) {
						for (int j = 0, valueIndex = lastGoodValueIndex + 1; j < numberOfNonGoodValuesSinceLastGoodValue; ++j, ++valueIndex) {
							velocities.set(valueIndex, lastGoodValue + (j + 1)*replacementValueStep);
						}
					} else if (pstReplacementMethod.equals(BackEndAPI.PRM_LAST_GOOD_VALUE)) {
						for (int j = 0, valueIndex = lastGoodValueIndex + 1; j < numberOfNonGoodValuesSinceLastGoodValue; ++j, ++valueIndex) {
							velocities.set(valueIndex, lastGoodValue);
						}
					} else if (pstReplacementMethod.equals(BackEndAPI.PRM_12PP_INTERPOLATION)) {
						//fit (double [] parameters, double [] x, double [] y, double [] sigma_x, double [] sigma_y, int num_points){
						final int NUMBER_OF_POINTS_EACH_SIDE = 12, TOTAL_NUMBER_OF_POINTS = NUMBER_OF_POINTS_EACH_SIDE * 2, POLYNOMIAL_ORDER = pstReplacementMethod.getPolynomialOrder();
						double[] parameters = new double[(POLYNOMIAL_ORDER + 1) * 2];
						double[] x = new double[TOTAL_NUMBER_OF_POINTS];
						double[] y = new double[TOTAL_NUMBER_OF_POINTS];
						Vector<Double> yVector = new Vector<Double>(TOTAL_NUMBER_OF_POINTS);
						double[] sigma_x = null;//new double[TOTAL_NUMBER_OF_POINTS];
						double[] sigma_y = null;//new double[TOTAL_NUMBER_OF_POINTS];
						
						// Find the 12 good points preceding the last good value (may be fewer if we're near the start)
						// Find the index of the 12th good point (remembering that we're searching backwards and need to fill
						// the fit arrays forwards) and start filling the fit arrays from there
						int numberOfGoodPoints = 0, searchIndex = lastGoodValueIndex;
						while (searchIndex > 0) {
							Double velocity = velocities.get(searchIndex);
							if (velocity.equals(DADefinitions.REMOVED_VALUE) == false) {
								++numberOfGoodPoints;
							}
							
							if (numberOfGoodPoints == NUMBER_OF_POINTS_EACH_SIDE) {
								break;
							}
							
							--searchIndex;
						}
						
						int fillIndex = 0;
						for (int velocitiesIndex = searchIndex; fillIndex < numberOfGoodPoints; ++velocitiesIndex) {
							Double velocity = velocities.get(velocitiesIndex);
						
							if (velocity.equals(DADefinitions.REMOVED_VALUE) == false) {
								x[fillIndex] = velocitiesIndex;
								y[fillIndex] = velocity;
								yVector.add(velocity);
								++fillIndex;
							}
						}

						// Find the 12 good points following the spike (may be fewer if we're near the end)
						for (int velocitiesIndex = i; fillIndex < TOTAL_NUMBER_OF_POINTS && velocitiesIndex < numberOfVelocities; ++velocitiesIndex) {
							Double velocity = velocities.get(velocitiesIndex);
							
							if (velocity.equals(DADefinitions.REMOVED_VALUE) == false) {
								x[fillIndex] = velocitiesIndex;
								y[fillIndex] = velocity;
								yVector.add(velocity);
								++fillIndex;
								++numberOfGoodPoints;
							}
						}
						
						
						// Calculate the replacement value. Use LGV as default.
						double replacementValue = lastGoodValueIndex >= 0 ? velocities.get(lastGoodValueIndex) : Double.MAX_VALUE;
						boolean fitSuccessful = false;
						
						// First check for a region of constant value (unlikely, but possible if there's dummy data)
						if (MAJFCMaths.standardDeviation(yVector) == 0) {
							replacementValue = y[0];
							
//							StringBuffer sb = new StringBuffer("Zero st dev in fit poly: ");
//							for (int k = 0; k < TOTAL_NUMBER_OF_POINTS; ++k ) {
//								sb.append(y[k]);
//								sb.append(' ');
//							}
//							
//							System.out.println(sb.toString());
						} else {
							// We can't do the FitPoly if there are less good points than the order of the equation
							// Also check that the y-values are constant
							if (numberOfGoodPoints > POLYNOMIAL_ORDER) { 
								try {
//									x=new double[] { 0,1,2,3,4,5,6,7,8,9,10,11,13,14,15,16,17,18,19,20,21,22,23,24};
//									y=new double[] {5.2466,5.2823,
//									5.3345,
//									5.3861,
//									5.4234,
//									5.4378,
//									5.4339,
//									5.4233,
//									5.4116,
//									5.4035,
//									5.3981,
//									5.3974,
//									
//									21.924,
//									21.9447,
//									5.5286,
//									5.5903,
//									5.662,
//									5.7374,
//									5.815,
//									5.8802,
//									5.9271,
//									5.9506,
//									5.9578,
//									5.9572};
//									numberOfGoodPoints = x.length;

									MAJFCFitPoly.fit(parameters, x, y, sigma_x, sigma_y, numberOfGoodPoints);
									fitSuccessful = true;
								} catch (Exception e) {
									++failedFitPolyCount;
//									System.out.println(e);
									MAJFCLogger.log("Failed Fit Polys: " + failedFitPolyCount, 100);
								}
							}
						}
							
						for (int j = 0, valueIndex = lastGoodValueIndex + 1; j < numberOfNonGoodValuesSinceLastGoodValue; ++j, ++valueIndex) {
							int xValueAtSpike = valueIndex;
							double replacementValueAtSpike = fitSuccessful ? 0 : replacementValue;
							
							if (fitSuccessful) {
								for (int k = 0; k < POLYNOMIAL_ORDER + 1; ++k) {
									replacementValueAtSpike += parameters[k] * Math.pow(xValueAtSpike, k);
								}
							}
                                   
							velocities.set(valueIndex, replacementValueAtSpike);
						}
					}
				}
			
				lastGoodValue = thisValue;
				lastGoodValueIndex = i;
			} else {
				// If the last value is a removed value, fill it (and any unfilled previous values) with the last good value
				if (i == (numberOfVelocities - 1)) {
					// Fill the removed values
					for (int j = lastGoodValueIndex + 1; j < (i + 1); ++j) {
						velocities.set(j, lastGoodValue);
					}
				}
			}
		}
	}

	/**
	 * Filter the given velocities using the Phase-Space Thresholding Filter
	 * (Goring and Nikora, 2002, Despiking Acoustic Doppler Velocimeter Data, Journal of Hydraulic Engineering, 128 (1))
	 * 
	 * @param velocities The velocities to filter. Also used to return the filtered velocities
	 * @param csType The type of scaling value to use (st. deviation, median, etc.)
	 * @param pstReplacementMethod The replacement method to be used when filtering
	 * @return The number of velocities removed 
	 */
	public ComponentDataHolder<Integer> phaseSpaceThresholding(ComponentDataHolder<Vector<Double>> velocities, CharacteristicScalerType csType, PSTReplacementMethod pstReplacementMethod, double samplingRate) {
		Vector<Double> xVelocities = velocities.getXComponent();
		Vector<Double> yVelocities = velocities.getYComponent();
		Vector<Double> zVelocities = velocities.getZComponent();

		int totalNumberOfRemovedVelocities = 0;		
		Hashtable<Integer, VelocityDatumInfo> totalRemovedVelocitiesLookup = new Hashtable<Integer, VelocityDatumInfo>(1000);
		
		// All velocity components are replaced if one is invalid, hence the totalNumberOfRemovedVelocities will change on each pstHelperFilterComponent.
		// The final value is the value after the last call to pstHelperFilterComponent (and should be the size of totalRemovedVelocitiesLookup).
		if (MAJFCMaths.standardDeviation(xVelocities) > 0) {
			totalNumberOfRemovedVelocities = pstHelperFilterComponent(xVelocities, yVelocities, zVelocities, csType, pstReplacementMethod, null, null, samplingRate, totalRemovedVelocitiesLookup);
		}
		
		if (MAJFCMaths.standardDeviation(yVelocities) > 0) {
			totalNumberOfRemovedVelocities = pstHelperFilterComponent(yVelocities, xVelocities, zVelocities, csType, pstReplacementMethod, null, null, samplingRate, totalRemovedVelocitiesLookup);
		}
		
		if (MAJFCMaths.standardDeviation(zVelocities) > 0) {
			totalNumberOfRemovedVelocities = pstHelperFilterComponent(zVelocities, xVelocities, yVelocities, csType, pstReplacementMethod, null, null, samplingRate, totalRemovedVelocitiesLookup);
		}
		
		return new ComponentDataHolder<Integer>(totalNumberOfRemovedVelocities, totalNumberOfRemovedVelocities, totalNumberOfRemovedVelocities);
	}
	
	/**
	 * Filter the given velocities using the modified Phase-Space Thresholding Filter
	 * (Parsheh, Sotiropoulos and Porte-Agel, 2010, Estimation of Power Spectra of Acoustic-Doppler Velocimetry Data Contaminated with Intermittent Spikes, Journal of Hydraulic Engineering, 136 (6))
	 * 
	 * @param velocities The velocities to filter. Also used to return the filtered velocities
	 * @param csType The type of scaling value to use (st. deviation, median, etc.)
	 * @param pstReplacementMethod The replacement method to be used when filtering
	 * @param mPSTExcludeLevel The exclude level to use for the initial filtering
	 * @return The number of velocities removed 
	 */
	public ComponentDataHolder<Integer> modifiedPhaseSpaceThresholding(ComponentDataHolder<Vector<Double>> velocities, CharacteristicScalerType csType, BackEndAPI.PSTReplacementMethod pstReplacementMethod, Double autoSafeC1, Double autoExcludeC2, Double samplingRate) {
		Vector<Double> xVelocities = velocities.getXComponent();
		Vector<Double> yVelocities = velocities.getYComponent();
		Vector<Double> zVelocities = velocities.getZComponent();
		
		double medianAbsDev = MAJFCMaths.medianAbsoluteDeviation(xVelocities);
		double numberOfVelocities = xVelocities.size();
		double autoSafeLimit = autoSafeC1 * medianAbsDev;
		double autoExcludeScaler = autoExcludeC2 * Math.sqrt(2 * Math.log(numberOfVelocities));
		double autoExcludeLimit = autoExcludeScaler * medianAbsDev;
		
		// Remove any automatically excluded values
		int totalNumberOfAutoRemovedVelocities = autoExcludeC2 == 0 ? 0 : excludeLevel(velocities, autoExcludeScaler, BackEndAPI.CS_MEDIAN_ABSOLUTE_DEVIATION, pstReplacementMethod, samplingRate).getXComponent();
		int totalNumberOfRemovedVelocities = 0;
		Hashtable<Integer, VelocityDatumInfo> totalRemovedVelocitiesLookup = new Hashtable<Integer, VelocityDatumInfo>(1000);
		if (medianAbsDev > 0) {
			totalNumberOfRemovedVelocities = pstHelperFilterComponent(xVelocities, yVelocities, zVelocities, csType, pstReplacementMethod, autoSafeLimit, autoExcludeLimit, samplingRate, totalRemovedVelocitiesLookup);
		}
		
		medianAbsDev = MAJFCMaths.medianAbsoluteDeviation(yVelocities);
		numberOfVelocities = yVelocities.size();
		autoSafeLimit = autoSafeC1 * medianAbsDev;
		autoExcludeLimit = autoExcludeC2 * medianAbsDev * Math.sqrt(2 * Math.log(numberOfVelocities));

		if (medianAbsDev > 0) {
			totalNumberOfRemovedVelocities = pstHelperFilterComponent(yVelocities, xVelocities, zVelocities, csType, pstReplacementMethod, autoSafeLimit, autoExcludeLimit, samplingRate, totalRemovedVelocitiesLookup);
		}
		
		medianAbsDev = MAJFCMaths.medianAbsoluteDeviation(zVelocities);
		numberOfVelocities = zVelocities.size();
		autoSafeLimit = autoSafeC1 * medianAbsDev;
		autoExcludeLimit = autoExcludeC2 * medianAbsDev * Math.sqrt(2 * Math.log(numberOfVelocities));

		if (medianAbsDev > 0) {
			totalNumberOfRemovedVelocities = pstHelperFilterComponent(zVelocities, xVelocities, yVelocities, csType, pstReplacementMethod, autoSafeLimit, autoExcludeLimit, samplingRate, totalRemovedVelocitiesLookup);
		}
		
		totalNumberOfRemovedVelocities += totalNumberOfAutoRemovedVelocities;
		
		return new ComponentDataHolder<Integer>(totalNumberOfRemovedVelocities, totalNumberOfRemovedVelocities, totalNumberOfRemovedVelocities);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Helper function for Phase-Space Thresholding Filter. Filtering is done based on velocitiesToFilter and its derivatives,
	 * but if velocitiesToFilter has a measurement removed then the other velocity components have their corresponding
	 * measurements removed too. 
	 * @see DespikingFilters#phaseSpaceThresholding
	 * 
	 * @param velocitiesToFilter The velocities of the component to filter
	 * @param secondaryVelocities1 The velocities for the second component
	 * @param secondaryVelocities2 The velocities for the third component
	 * @param csType The type of scaling value to use (st. deviation, median, etc.)
	 * @param pstReplacementMethod The replacement method to be used when filtering
	 * @param autoSafeLimit Limit of range from mean within which values are automatically safe
	 * @param autoExcludeLimit Limit of range from mean outside which values are automatically excluded
	 * @param stDevVelocities The velocity set to use for the standard deviation calculations (need if velocitiesToFilter has been
	 * pre-filtered using the exclude level, as for the modified PST filtering method)
	 * @return The number of velocities removed 
	 */
	private int pstHelperFilterComponent(Vector<Double> velocitiesToFilter, Vector<Double> secondaryVelocities1, Vector<Double> secondaryVelocities2, CharacteristicScalerType csType, BackEndAPI.PSTReplacementMethod pstReplacementMethod, Double autoSafeLimit, Double autoExcludeLimit, Double samplingRate, Hashtable<Integer, VelocityDatumInfo> totalRemovedVelocitiesLookup) {
		Vector<Double> highPassFilteredVelocities = samplingRate == 0 || MAJFCMaths.standardDeviation(velocitiesToFilter) == 0 ? velocitiesToFilter : MAJFCMaths.highPassFilter(velocitiesToFilter, true, samplingRate/(1000*2d*Math.PI), 1d/samplingRate);
		Vector<Double> firstDerivatives;
		Vector<Double> secondDerivatives;
		Vector<VelocityDatumInfo> removedVelocitiesThisIterationList = new Vector<VelocityDatumInfo>(1000);
		Hashtable<VelocityDatumInfo, VelocityDatumInfo> removedVelocitiesThisIterationLookup = new Hashtable<VelocityDatumInfo, VelocityDatumInfo>(1000);
		Vector<Vector<VelocityDatumInfo>> lastRemovedVelocitiesListsList = new Vector<Vector<VelocityDatumInfo>>();
		boolean repeatingReplacements = false;
//		Hashtable<Integer, VelocityDatumInfo> totalRemovedVelocitiesLookup = new Hashtable<Integer, VelocityDatumInfo>(1000);
		int lastTotalRemovedVelocitiesCount = 0;
		int iterationCount = 0;
		
		do {
			++iterationCount;
			
			// Calculate fluctuations
			firstDerivatives = MAJFCMaths.derivatives(highPassFilteredVelocities, 1);
			secondDerivatives = MAJFCMaths.derivatives(firstDerivatives, 1);
			
			double[] characteristicScaler = { 0, 0, 0 };
			
			if (csType.equals(BackEndAPI.CS_MEDIAN_ABSOLUTE_DEVIATION)) {
				characteristicScaler[0] = MAJFCMaths.medianAbsoluteDeviation(highPassFilteredVelocities);
				characteristicScaler[1] = MAJFCMaths.medianAbsoluteDeviation(firstDerivatives);
				characteristicScaler[2] = MAJFCMaths.medianAbsoluteDeviation(secondDerivatives);
			} else if (csType.equals(BackEndAPI.CS_STANDARD_DEVIATION)) {
				characteristicScaler[0] = MAJFCMaths.standardDeviation(highPassFilteredVelocities);
				characteristicScaler[1] = MAJFCMaths.standardDeviation(firstDerivatives);
				characteristicScaler[2] = MAJFCMaths.standardDeviation(secondDerivatives);
			} else if (csType.equals(BackEndAPI.CS_MEAN_ABSOLUTE_DEVIATION)) {
				characteristicScaler[0] = MAJFCMaths.meanAbsoluteDeviation(highPassFilteredVelocities);
				characteristicScaler[1] = MAJFCMaths.meanAbsoluteDeviation(firstDerivatives);
				characteristicScaler[2] = MAJFCMaths.meanAbsoluteDeviation(secondDerivatives);
			} 
			
			lastRemovedVelocitiesListsList.add((Vector<VelocityDatumInfo>) removedVelocitiesThisIterationList.clone());
			Enumeration<VelocityDatumInfo> keys = removedVelocitiesThisIterationLookup.keys();
			Enumeration<VelocityDatumInfo> elements = removedVelocitiesThisIterationLookup.elements();
			
			lastTotalRemovedVelocitiesCount = totalRemovedVelocitiesLookup.size();

			while (keys.hasMoreElements()) {
				totalRemovedVelocitiesLookup.put(keys.nextElement().mIndex, elements.nextElement());
			}
			
			// If we're just replacing things we've already replaced, stop
			if (iterationCount > 1 && lastTotalRemovedVelocitiesCount == totalRemovedVelocitiesLookup.size()) {
				break;
			}
			
			removedVelocitiesThisIterationList.removeAllElements();
			removedVelocitiesThisIterationLookup.clear();

			filterWithEllipse(highPassFilteredVelocities, firstDerivatives, characteristicScaler[0], characteristicScaler[1], removedVelocitiesThisIterationLookup, removedVelocitiesThisIterationList, highPassFilteredVelocities, autoSafeLimit, autoExcludeLimit);
			filterWithEllipse(highPassFilteredVelocities, secondDerivatives, characteristicScaler[0], characteristicScaler[2], removedVelocitiesThisIterationLookup, removedVelocitiesThisIterationList, highPassFilteredVelocities, autoSafeLimit, autoExcludeLimit);
			filterWithEllipse(firstDerivatives, secondDerivatives, characteristicScaler[1], characteristicScaler[2], removedVelocitiesThisIterationLookup, removedVelocitiesThisIterationList, highPassFilteredVelocities, autoSafeLimit, autoExcludeLimit);
			
			int numberOfRemovedVelocities = removedVelocitiesThisIterationLookup.size();
			int numberOfLastRemovedVelocitiesListEntries = lastRemovedVelocitiesListsList.size();
			
			if (numberOfLastRemovedVelocitiesListEntries > 0) {
				int lastNumberOfRemovedVelocities = lastRemovedVelocitiesListsList.lastElement().size();

				// Check that we're not stuck in a loop, replacing values with their own values
				if (numberOfRemovedVelocities >= lastNumberOfRemovedVelocities) {
					// Check against the last set of removed velocities which was the same size as the current one
					for (int i = 0; i < numberOfLastRemovedVelocitiesListEntries; ++i) {
						if (lastRemovedVelocitiesListsList.elementAt(i).size() == numberOfRemovedVelocities) {
							repeatingReplacements = MAJFCMaths.isSubset(removedVelocitiesThisIterationLookup, lastRemovedVelocitiesListsList.elementAt(i));
							
							if (repeatingReplacements) {
								break;
							}
						}
					}
				}
				
				if (repeatingReplacements) {
					MAJFCLogger.log("Repeating replacements - Removal count: " + numberOfRemovedVelocities + " Last Removal Count: " + lastNumberOfRemovedVelocities, 10);
					break;
				}
			}
			
			// Remove the appropriate velocities
			for (int i = 0; i < numberOfRemovedVelocities; ++i) {
				int index = removedVelocitiesThisIterationList.elementAt(i).mIndex;
				// Set these to REMOVED_VALUE for the next iteration
				highPassFilteredVelocities.set(index, DADefinitions.REMOVED_VALUE);
				
				// Set these to REMOVED_VALUE so they are removed from the list of velocities which will be returned at the end
				velocitiesToFilter.set(index, DADefinitions.REMOVED_VALUE);
				secondaryVelocities1.set(index, DADefinitions.REMOVED_VALUE);
				secondaryVelocities2.set(index, DADefinitions.REMOVED_VALUE);
			}
			
			// Replace any removed values at the start of the data with the mean value
			replaceRemovedValuesBeforeFirstGoodValueWithMeanValue(highPassFilteredVelocities, velocitiesToFilter, secondaryVelocities1, secondaryVelocities2);
			
			// Replace the removed values
			replaceRemovedValues(highPassFilteredVelocities, pstReplacementMethod);
			replaceRemovedValues(velocitiesToFilter, pstReplacementMethod);
			replaceRemovedValues(secondaryVelocities1, pstReplacementMethod);
			replaceRemovedValues(secondaryVelocities2, pstReplacementMethod);
//			replaceRemovedValuesByInterpolationAcrossFirstXPointsEachSideOfSpike(highPassFilteredVelocities, 10);
//			replaceRemovedValuesByInterpolationAcrossFirstXPointsEachSideOfSpike(velocitiesToFilter, 10);
//			replaceRemovedValuesByInterpolationAcrossFirstXPointsEachSideOfSpike(secondaryVelocities1, 10);
//			replaceRemovedValuesByInterpolationAcrossFirstXPointsEachSideOfSpike(secondaryVelocities2, 10);

			MAJFCLogger.log("Iteration: " + iterationCount + "\t " + "Removed: " + numberOfRemovedVelocities + " new removals", 100);
		} while (removedVelocitiesThisIterationLookup.size() > 0 && repeatingReplacements == false);
		
		return totalRemovedVelocitiesLookup.size();
	}
	
	/**
	 * Filter the given velocities using an exclude level (factor of standard deviation), replacing the removed values
	 * using simple interpolation between the bounding "good" values
	 * @param velocities The velocities to filter
	 * @param excludeLevel The exclude level
	 */
	public ComponentDataHolder<Integer> excludeLevel(ComponentDataHolder<Vector<Double>> velocitiesHolder, double excludeLevel) {
		return excludeLevel(velocitiesHolder, excludeLevel, BackEndAPI.CS_STANDARD_DEVIATION, BackEndAPI.PRM_LINEAR_INTERPOLATION);
	}
	
	/**
	 * Filter the given velocities using an exclude level (factor of a scaling value)
	 * @param velocitiesHolder The velocities to filter
	 * @param excludeLevel The exclude level
	 * @param elsType The type of scaling value to use
	 * @param pstReplacementMethod The method to use when replacing removed values
	 * @param samplingRate The rate at which the velocities were sampled (used for high-pass filtering - set to 0 for no high-pass filter)
	 */
	public ComponentDataHolder<Integer> excludeLevel(ComponentDataHolder<Vector<Double>> velocities, double excludeLevel, CharacteristicScalerType elsType, PSTReplacementMethod pstReplacementMethod) {
		return excludeLevel(velocities, excludeLevel, elsType, pstReplacementMethod, 0d);
	}
	
	/**
	 * Filter the given velocities using an exclude level (factor of a scaling value)
	 * @param velocities The velocities to filter
	 * @param excludeLevel The exclude level
	 * @param elsType The type of scaling value to use
	 * @param pstReplacementMethod The method to use when replacing removed values
	 * @param samplingRate The rate at which the velocities were sampled (used for high-pass filtering - set to 0 for no high-pass filter)
	 * @return The number of values excluded
	 */
	public ComponentDataHolder<Integer> excludeLevel(ComponentDataHolder<Vector<Double>> velocities, double excludeLevel, CharacteristicScalerType elsType, PSTReplacementMethod pstReplacementMethod, double samplingRate) {
		int numberOfRemovedXValues = elHelperFilterComponent(velocities.getXComponent(), excludeLevel, elsType, pstReplacementMethod, samplingRate);
		int numberOfRemovedYValues = elHelperFilterComponent(velocities.getYComponent(), excludeLevel, elsType, pstReplacementMethod, samplingRate);
		int numberOfRemovedZValues = elHelperFilterComponent(velocities.getZComponent(), excludeLevel, elsType, pstReplacementMethod, samplingRate);
		
		return new ComponentDataHolder<Integer>(numberOfRemovedXValues, numberOfRemovedYValues, numberOfRemovedZValues);
	}
	
	private int elHelperFilterComponent(Vector<Double> velocities, double excludeLevel, CharacteristicScalerType elsType, PSTReplacementMethod pstReplacementMethod, double samplingRate) {
		@SuppressWarnings("unchecked")
		Vector<Double> highPassFilteredVelocities = samplingRate > 0 ? MAJFCMaths.highPassFilter(velocities, true, samplingRate/(1000*2d*Math.PI), 1d/samplingRate) : (Vector<Double>) velocities.clone();
		
		double mean = MAJFCMaths.mean(highPassFilteredVelocities);
		double scaler = 1.0;

		if (elsType.equals(BackEndAPI.CS_MEAN_ABSOLUTE_DEVIATION)) {
			scaler = MAJFCMaths.meanAbsoluteDeviation(highPassFilteredVelocities);
		} else if (elsType.equals(BackEndAPI.CS_MEDIAN_ABSOLUTE_DEVIATION)) {
			scaler = MAJFCMaths.medianAbsoluteDeviation(highPassFilteredVelocities);
		} else {
			scaler = MAJFCMaths.standardDeviation(highPassFilteredVelocities);			
		}
		
		double upperLimit = mean + excludeLevel * scaler;
		double lowerLimit = mean - excludeLevel * scaler;
		
		int numberOfVelocities = velocities.size();
		int numberOfRemovedValues = 0;
		for (int i = 0; i < numberOfVelocities; ++i) {
			double velocity = highPassFilteredVelocities.get(i);
			
			if (lowerLimit > velocity || velocity > upperLimit) {
				velocities.set(i, DADefinitions.REMOVED_VALUE);
				++numberOfRemovedValues;
			}
		}
		
		replaceRemovedValues(velocities, pstReplacementMethod);
		
		return numberOfRemovedValues;
	}

	/**
	 * Filter the given velocities by signal correlation
	 * @param velocities The velocities to filter
	 * @param signalCorrelations The signal correlations for the velocities
	 * @param limitingCorrelation The minimum acceptable correlation
	 * @param pstReplacementMethod The method to use when replacing removed values
	 * @return The number of values excluded
	 */
	public ComponentDataHolder<Integer> correlationAndSNR(ComponentDataHolder<Vector<Double>> velocities, ComponentDataHolder<Vector<Double>> signalCorrelations, double limitingCorrelation, ComponentDataHolder<Vector<Double>> signalSNRs, double limitingSNR, boolean usePercentageForCorrAndSNRFilter, PSTReplacementMethod pstReplacementMethod) {
		int numberOfRemovedXValues = correlationAndSNRHelperFilterComponent(velocities.getXComponent(), signalCorrelations.getXComponent(), limitingCorrelation, signalSNRs.getXComponent(), limitingSNR, usePercentageForCorrAndSNRFilter, pstReplacementMethod);
		int numberOfRemovedYValues = correlationAndSNRHelperFilterComponent(velocities.getYComponent(), signalCorrelations.getYComponent(), limitingCorrelation, signalSNRs.getYComponent(), limitingSNR, usePercentageForCorrAndSNRFilter, pstReplacementMethod);
		int numberOfRemovedZValues = correlationAndSNRHelperFilterComponent(velocities.getZComponent(), signalCorrelations.getZComponent(), limitingCorrelation, signalSNRs.getZComponent(), limitingSNR, usePercentageForCorrAndSNRFilter, pstReplacementMethod);
		
		return new ComponentDataHolder<Integer>(numberOfRemovedXValues, numberOfRemovedYValues, numberOfRemovedZValues);
	}
	
	private int correlationAndSNRHelperFilterComponent(Vector<Double> velocities, Vector<Double> signalCorrelations, double limitingCorrelation, Vector<Double> signalSNRs, double limitingSNR, boolean usePercentageForCorrAndSNRFilter, PSTReplacementMethod pstReplacementMethod) {
		int numberOfVelocities = velocities.size();
		int numberOfRemovedValues = 0;
		
		if (usePercentageForCorrAndSNRFilter) {
			limitingCorrelation *= MAJFCMaths.mean(signalCorrelations)/100d;
			limitingSNR *= MAJFCMaths.mean(signalSNRs)/100d;
		}
		
		for (int i = 0; i < numberOfVelocities; ++i) {
			double correlation = signalCorrelations.get(i);
			double SNR = signalSNRs.get(i);
			
			if ((correlation != DADefinitions.INVALID_SNR_OR_CORRELATION && correlation < limitingCorrelation)
					|| (SNR != DADefinitions.INVALID_SNR_OR_CORRELATION && SNR < limitingSNR)) {
				velocities.set(i, DADefinitions.REMOVED_VALUE);
				++numberOfRemovedValues;
			}
		}
		
		replaceRemovedValues(velocities, pstReplacementMethod);
		
		return numberOfRemovedValues;
	}
	
	/**
	 * Filter the given velocities by signal correlation
	 * @param velocities The velocities to filter
	 * @param signalCorrelations The signal correlations for the velocities
	 * @param limitingCorrelation The minimum acceptable correlation
	 * @param pstReplacementMethod The method to use when replacing removed values
	 * @return The number of values excluded
	 */
	public ComponentDataHolder<Integer> wDiff(ComponentDataHolder<Vector<Double>> velocities, Vector<Double> wDiffs, double limitingWDiff, PSTReplacementMethod pstReplacementMethod) {
		Vector<Double> xVelocities = velocities.getXComponent();
		Vector<Double> yVelocities = velocities.getYComponent();
		Vector<Double> zVelocities = velocities.getZComponent();
		double meanWPercentage = 100d/MAJFCMaths.rms(zVelocities);
		
		int numberOfVelocities = xVelocities.size();
		int numberOfRemovedValues = 0;
		
		for (int i = 0; i < numberOfVelocities; ++i) {
			// Convert to a percentage of the w velocity mean
			double wDiff = wDiffs.get(i) * meanWPercentage;
			
			if (wDiff != DADefinitions.INVALID_SNR_OR_CORRELATION && wDiff > limitingWDiff) {
				xVelocities.set(i, DADefinitions.REMOVED_VALUE);
				yVelocities.set(i, DADefinitions.REMOVED_VALUE);
				zVelocities.set(i, DADefinitions.REMOVED_VALUE);
				++numberOfRemovedValues;
			}
		}
		
		replaceRemovedValues(xVelocities, pstReplacementMethod);
		replaceRemovedValues(yVelocities, pstReplacementMethod);
		replaceRemovedValues(zVelocities, pstReplacementMethod);
		
		return new ComponentDataHolder<Integer>(numberOfRemovedValues, numberOfRemovedValues, numberOfRemovedValues);
	}
	
	/**
	 * Filter the given velocities by removing anything which equals exactly zero
	 * @param velocities The velocities to filter
	 * @param pstReplacementMethod The method to use when replacing removed values
	 * @return The number of values excluded
	 */
	public ComponentDataHolder<Integer> removeZeroes(ComponentDataHolder<Vector<Double>> velocities, PSTReplacementMethod pstReplacementMethod) {
		Vector<Double> xVelocities = velocities.getXComponent();
		Vector<Double> yVelocities = velocities.getYComponent();
		Vector<Double> zVelocities = velocities.getZComponent();
		
		int numberOfVelocities = xVelocities.size();
		int numberOfRemovedValues = 0;
		
		for (int i = 0; i < numberOfVelocities; ++i) {
			if (xVelocities.elementAt(i) == 0) {
				xVelocities.set(i, DADefinitions.REMOVED_VALUE);
				yVelocities.set(i, DADefinitions.REMOVED_VALUE);
				zVelocities.set(i, DADefinitions.REMOVED_VALUE);
				++numberOfRemovedValues;
			}
		}
		
		replaceRemovedValues(xVelocities, pstReplacementMethod);
		replaceRemovedValues(yVelocities, pstReplacementMethod);
		replaceRemovedValues(zVelocities, pstReplacementMethod);
		
		return new ComponentDataHolder<Integer>(numberOfRemovedValues, numberOfRemovedValues, numberOfRemovedValues);
	}

	/**
	 * Helper class to hold removed velocity data
	 */
	private class VelocityDatumInfo {
		private final int mIndex;
		private final double mField1Value;
		private final double mField2Value;
		
		/**
		 * Constructor
		 * @param index The index of the data point
		 * @param field1Value The value of field 1 at this index
		 * @param field2Value The value of field 2 at this index
		 */
		VelocityDatumInfo(int index, double field1Value, double field2Value) {
			mIndex = index;
			mField1Value = field1Value;
			mField2Value = field2Value;
		}
		
		@Override
		/**
		 * Equality comparator - checks whether index and field values are all equal
		 * @param theOtherOne The one to compare to
		 * @return True if this object and theOtherOne are equal
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
			
			VelocityDatumInfo otherOne = (VelocityDatumInfo) theOtherOne;
			return mIndex == otherOne.mIndex && mField1Value == otherOne.mField1Value && mField2Value == otherOne.mField2Value;
		}

		@Override
		public int hashCode() {
			int hash = 15 * mIndex;
	//		hash = (int) (15 * hash + mField1Value);
	//		hash = (int) (15 * hash + mField2Value);
			
			return hash;
		}
	}
	
	/**
	 * Class for returning filtered velocities
	 */
	public class ComponentDataHolder<ObjectType extends Object> {
		private final ObjectType mXComponent;
		private final ObjectType mYComponent;
		private final ObjectType mZComponent;
		
		/**
		 * Constructor
		 * @param xComponent The x-component to hold
		 * @param yComponent The y-component to hold
		 * @param zComponent The z-component to hold
		 */
		public ComponentDataHolder(ObjectType xComponent, ObjectType yComponent, ObjectType zComponent) {
			mXComponent = xComponent;
			mYComponent = yComponent;
			mZComponent = zComponent;
		}
		
		/**
		 * Get the x-component
		 * @return The x-component
		 */
		public ObjectType getXComponent() {
			return mXComponent;
		}
		
		/**
		 * Get the y-component
		 * @return The y-component
		 */
		public ObjectType getYComponent() {
			return mYComponent;
		}		
		
		/**
		 * Get the z-component
		 * @return The z-component
		 */
		public ObjectType getZComponent() {
			return mZComponent;
		}
		
		/**
		 * Clone
		 */
		public ComponentDataHolder<ObjectType> clone() {
			return null;
		}
	}

	/**
	 * A simple moving average filter
	 * @param filteredVelocities
	 * @param windowSize the size of window to average over
	 * @return
	 */
	public ComponentDataHolder<Integer> movingAverage(ComponentDataHolder<Vector<Double>> filteredVelocities, int windowSize) {
		final int WINDOW_HALF_WIDTH = windowSize/2;
		int numberOfVelocities = filteredVelocities.mXComponent.size();
		@SuppressWarnings("unchecked")
		Vector<Double> uVelocitiesToFilter = filteredVelocities.mXComponent == null ? null : (Vector<Double>) filteredVelocities.mXComponent.clone();
		@SuppressWarnings("unchecked")
		Vector<Double> vVelocitiesToFilter = filteredVelocities.mYComponent == null ? null : (Vector<Double>) filteredVelocities.mYComponent.clone();
		@SuppressWarnings("unchecked")
		Vector<Double> wVelocitiesToFilter = filteredVelocities.mZComponent == null ? null : (Vector<Double>) filteredVelocities.mZComponent.clone();
		
		// If we have less than windowSize points do nothing.
		if (numberOfVelocities > windowSize) {
			for (int i = 0; i < numberOfVelocities; ++i) {
				int firstIndex = i < WINDOW_HALF_WIDTH ? 0 : i - WINDOW_HALF_WIDTH;
				int lastIndex = firstIndex + windowSize;
				
				if (lastIndex >= numberOfVelocities) {
					lastIndex = numberOfVelocities - 1;
					firstIndex = lastIndex - windowSize;
				}

				filteredVelocities.mXComponent.set(i, MAJFCMaths.mean(uVelocitiesToFilter, firstIndex, lastIndex));
				
				if (filteredVelocities.mYComponent != null) {
					filteredVelocities.mYComponent.set(i, MAJFCMaths.mean(vVelocitiesToFilter, firstIndex, lastIndex));
				}
				
				if (filteredVelocities.mZComponent != null) {
					filteredVelocities.mZComponent.set(i, MAJFCMaths.mean(wVelocitiesToFilter, firstIndex, lastIndex));
				}
			}
		}
		
		return new ComponentDataHolder<Integer>(0, 0, 0);
	}
}
