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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.guiComponents.MAJFCTabbedPanel;
import com.mikejesson.majfc.helpers.MAJFCIndex;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.majfc.helpers.MAJFCTools.MAJFCToolsException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;





/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class VorticityEquationTermsGraphsPanel extends MAJFCStackedPanelWithFrame {
	private final MAJFCTabbedPanel mTabbedPanel;
	private final VorticityEquationTermIdentifier TERM_1 = new VorticityEquationTermIdentifier(1); // Viscous diffusion
	private final VorticityEquationTermIdentifier TERM_2 = new VorticityEquationTermIdentifier(2); // Perkins' P3
	private final VorticityEquationTermIdentifier TERM_3 = new VorticityEquationTermIdentifier(3); // Perkins' P4
	private final VorticityEquationTermIdentifier TERM_4 = new VorticityEquationTermIdentifier(4); // Sum of Viscous diffusion, P3 and P4
	private final VorticityEquationTermIdentifier TERM_5 = new VorticityEquationTermIdentifier(5); // Unused
	
	Vector<Double[]> mViscousDiffusionData;
	Vector<Double[]> mPerkinsP3Data;
	Vector<Double[]> mPerkinsP4Data;
 	Vector<Double[]> mTerm4Data;
 	Vector<Double[]> mLHSData;
	
	/**
	 * Constructor
	 * 
	 * @param dataSetId The id of the data set to show this for
	 */
	public VorticityEquationTermsGraphsPanel(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent) {
		super(parent, new GridBagLayout());
		
		mTabbedPanel = new MAJFCTabbedPanel();
		mTabbedPanel.addTab(DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_1_LABEL), new VorticityEquationTermGraph(dataSetId, this, TERM_1, DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_1_GRAPH_TITLE), DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_1_LEGEND_TEXT)));
		mTabbedPanel.addTab(DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_2_LABEL), new VorticityEquationTermGraph(dataSetId, this, TERM_2, DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_2_GRAPH_TITLE), DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_2_LEGEND_TEXT)));
		mTabbedPanel.addTab(DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_3_LABEL), new VorticityEquationTermGraph(dataSetId, this, TERM_3, DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_3_GRAPH_TITLE), DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_3_LEGEND_TEXT)));
		mTabbedPanel.addTab(DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_4_LABEL), new VorticityEquationTermGraph(dataSetId, this, TERM_4, DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_4_GRAPH_TITLE), DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_4_LEGEND_TEXT)));
		mTabbedPanel.addTab(DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_5_LABEL), new VorticityEquationTermGraph(dataSetId, this, TERM_5, DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_5_GRAPH_TITLE), DAStrings.getString(DAStrings.VORTICITY_EQUATION_TERM_5_LEGEND_TEXT)));
		
		add(mTabbedPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));

		validate();
	}
	
	/**
	 * Helper class
	 * @author Mike
	 *
	 */
	private class VorticityEquationTermIdentifier extends MAJFCIndex {

		public VorticityEquationTermIdentifier(int termId) {
			super(termId);
		}
	}
	
	private class VorticityEquationTermGraph extends AbstractCrossSectionColourCodedGraph {
		private VorticityEquationTermIdentifier mTermId;
		
		/**
		 * Constructor
		 * @param yCoord The yCoord of the section to display
		 * @param title The title for the graph
		 * @param windowListener The window listener for this display
		 * @throws HeadlessException
		 */
		VorticityEquationTermGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, VorticityEquationTermIdentifier termId, String chartTitle, String legendText) throws HeadlessException {
			super(dataSetId, parent, new Object[] { dataSetId, termId }, 50, null, chartTitle, legendText, true);
		}
		
		@Override
		/**
		 * {@link AbstractCrossSectionColourCodedGraph#initialise(Object[]) }
		 */
		protected void initialise(Object[] initialisationObjects) {
			super.initialise(initialisationObjects);
			
			mTermId = (VorticityEquationTermIdentifier) initialisationObjects[1];
		}

		/**
		 * {@link AbstractCrossSectionColourCodedGraph#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, int, int, int)}
		 */
		protected void prepareDataArray(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex DPSIndex, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) {
			try {
				if (mTermId.equals(TERM_1)) {
					preparePerkinsViscousDiffusion(dataSetId, sortedYCoords, sortedZCoordsSets, data, yCoordIndexInDataArray, zCoordIndexInDataArray, dataIndexInDataArray);
					mViscousDiffusionData = data;
				} else if (mTermId.equals(TERM_2)) {
					preparePerkinsP3(dataSetId, sortedYCoords, sortedZCoordsSets, data, yCoordIndexInDataArray, zCoordIndexInDataArray, dataIndexInDataArray);
					mPerkinsP3Data = data;
				} else if (mTermId.equals(TERM_3)) {
					preparePerkinsP4(dataSetId, sortedYCoords, sortedZCoordsSets, data, yCoordIndexInDataArray, zCoordIndexInDataArray, dataIndexInDataArray);
					mPerkinsP4Data = data;
				} else if (mTermId.equals(TERM_4)) {
					// This relies on this method having already been called for TERM_1, TERM_2 and TERM_3.
					preparePerkinsTerm4(dataSetId, sortedYCoords, sortedZCoordsSets, data, yCoordIndexInDataArray, zCoordIndexInDataArray, dataIndexInDataArray);
					mTerm4Data = data;
				}  else if (mTermId.equals(TERM_5)) {
					prepareLHSOfVorticityEquation(dataSetId, sortedYCoords, sortedZCoordsSets, data, yCoordIndexInDataArray, zCoordIndexInDataArray, dataIndexInDataArray);
					mLHSData = new Vector<Double[]>(mViscousDiffusionData.size());
				}
			} catch (BackEndAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MAJFCToolsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void addToSum(Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedSumLookup, Integer yCoord, Integer zCoord, Double datum) {
			Hashtable<Integer, Double> zIndexedSumLookup = yThenZIndexedSumLookup.get(yCoord);
			
			if (zIndexedSumLookup == null) {
				zIndexedSumLookup = new Hashtable<Integer, Double>(20);
				yThenZIndexedSumLookup.put(yCoord, zIndexedSumLookup);
			}
			
			Double sumForTheseCoords = zIndexedSumLookup.get(zCoord);
			
			if (sumForTheseCoords == null) {
				sumForTheseCoords = new Double(0);
			}
			
			sumForTheseCoords += datum;
			
			zIndexedSumLookup.put(zCoord, sumForTheseCoords);
		}
		
		/**
		 * Prepares the data array as Perkins' viscous diffusion vorticity equation component
		 * {@link this#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, int, int, int)}
		 */
		protected void preparePerkinsViscousDiffusion(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) throws BackEndAPIException, MAJFCToolsException {
			Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedDWByDyMinusDVByDz;
			
			try {
				yThenZIndexedDWByDyMinusDVByDz = StreamwiseVorticityGraph.calculatePerkinsEpsilon(dataSetId, sortedYCoords, sortedZCoordsSets);
			} catch (BackEndAPIException e) {
				e.printStackTrace();
				return;
			} catch (MAJFCToolsException e) {
				e.printStackTrace();
				return;
			}
			
			// Now we have y-indexed then z-indexed lookup of E = (dW/dy - dV/dz)

			// Calculate d2E/dz2
			Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedD2EByDz2 = calculateDByDz(yThenZIndexedDWByDyMinusDVByDz, sortedYCoords, sortedZCoordsSets, 2);

			// Calculate d2E/dy2
			Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedD2EByDy2 = calculateDByDy(yThenZIndexedDWByDyMinusDVByDz, sortedYCoords, sortedZCoordsSets, 2);

			// Now we have two y- then z-coordinate indexed lookups, one of d2E/dy2 and one of d2E/dz2
			int numberOfYCoords = sortedYCoords.size();
			Vector<Double> dataValues = new Vector<Double>(1000);
			double kinematicViscosity = 1.0;
			try {
				kinematicViscosity = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_FLUID_KINEMATIC_VISCOSITY);
				kinematicViscosity *= 1E-6;
			} catch (BackEndAPIException theException) {
			}
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
				Vector<Integer> zCoordsForThisY = sortedZCoordsSets.elementAt(yCoordIndex);
				int numberOfZCoords = zCoordsForThisY.size();
							
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Integer zCoord = zCoordsForThisY.elementAt(zCoordIndex);
					Double[] datum = new Double[3];
					
					datum[yCoordIndexInDataArray] = yCoord.doubleValue();
					datum[zCoordIndexInDataArray] = zCoord.doubleValue();
					
					datum[dataIndexInDataArray] = kinematicViscosity * (yThenZIndexedD2EByDy2.get(yCoord).get(zCoord) + yThenZIndexedD2EByDz2.get(yCoord).get(zCoord));
					dataValues.add(datum[dataIndexInDataArray]);
					
					data.add(datum);
				}
			}
			
			mMean = MAJFCMaths.mean(dataValues);
		}		

		private Hashtable<Integer, Hashtable<Integer, Double>> calculateDByDz(Hashtable<Integer, Hashtable<Integer,Double>> yThenZIndexedDataLookup , Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, int orderOfDerivative) throws MAJFCToolsException {
			Hashtable<Integer, Hashtable<Integer, Double>> dByDz = new Hashtable<Integer, Hashtable<Integer,Double>>(100);
			int numberOfYCoords = sortedYCoords.size();
			
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
				Vector<Integer> sortedZCoords = sortedZCoordsSets.elementAt(yCoordIndex);
				int numberOfZCoords = sortedZCoords.size();
				Vector<Double> dataForThisY = new Vector<Double>(numberOfZCoords);
				Hashtable<Integer , Double> zIndexedDataForThisY = yThenZIndexedDataLookup.get(yCoord);
				
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
					dataForThisY.add(zIndexedDataForThisY.get(zCoord));
				}
				
				Vector<Double> dByDzs = MAJFCMaths.derivativesIntegerPositions(dataForThisY, sortedZCoords);
				
				for (int i = 1; i < orderOfDerivative; ++i) {
					dByDzs = MAJFCMaths.derivativesIntegerPositions(dByDzs, sortedZCoords);
				}
				
				// Put these d/dzs into a lookup for future use
				Hashtable<Integer , Double> zIndexedDByDz = new Hashtable<Integer, Double>(20);
				dByDz.put(yCoord, zIndexedDByDz);

				// sortedZCoords and dByDz should correspond, so just us zCoordIndex
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
					zIndexedDByDz.put(zCoord, dByDzs.elementAt(zCoordIndex));
				}
			}

			return dByDz;
		}
		
		private Hashtable<Integer, Hashtable<Integer, Double>> calculateDByDy(Hashtable<Integer, Hashtable<Integer,Double>> yThenZIndexedDataLookup , Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, int orderOfDerivative) throws MAJFCToolsException, BackEndAPIException {
			Hashtable<Integer, Hashtable<Integer, Double>> dByDy = new Hashtable<Integer, Hashtable<Integer,Double>>(100);
			Hashtable<Integer, Vector<Integer>> zCoordIndexedLookup = DAFrame.getBackEndAPI().getZCoordIndexedSortedDataPointCoordinates(mDataSetId);
			Enumeration<Integer> zCoordsEnum = zCoordIndexedLookup.keys();
						
			// Go through all the z-coordinates
			while (zCoordsEnum.hasMoreElements()) {
				Integer zCoord = zCoordsEnum.nextElement();
				Vector<Integer> yCoordsForThisZ = zCoordIndexedLookup.get(zCoord);
				int numberOfYCoords = yCoordsForThisZ.size();
				Vector<Double> dataForThisZ = new Vector<Double>(numberOfYCoords);
				
				for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
					Integer yCoord = yCoordsForThisZ.elementAt(yCoordIndex);
					Hashtable<Integer , Double> zIndexedDataLookup = yThenZIndexedDataLookup.get(yCoord);
					dataForThisZ.add(zIndexedDataLookup.get(zCoord));
				}
				
				Vector<Double> dByDys = MAJFCMaths.derivativesIntegerPositions(dataForThisZ, yCoordsForThisZ);
				
				for (int i = 1; i < orderOfDerivative; ++i) {
					dByDys = MAJFCMaths.derivativesIntegerPositions(dByDys, yCoordsForThisZ);
				}

				// Put these d/dys into a lookup for future use
				for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
					Integer yCoord = yCoordsForThisZ.elementAt(yCoordIndex);
					Hashtable<Integer , Double> zIndexedDByDys = dByDy.get(yCoord);
					
					if (zIndexedDByDys == null) {
						zIndexedDByDys = new Hashtable<Integer, Double>(100);
					}
					
					zIndexedDByDys.put(zCoord, dByDys.elementAt(yCoordIndex));
					dByDy.put(yCoord, zIndexedDByDys);
				}
			}
			
			return dByDy;
		}
		
		/**
		 * Prepares the data array as Perkins' P3 vorticity equation component
		 * {@link this#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, int, int, int)}
		 */
		protected void preparePerkinsP3(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) throws BackEndAPIException, MAJFCToolsException {
			int numberOfYCoords = sortedYCoords.size();
			Vector<Double> dataValues = new Vector<Double>(1000);

			// Calculate d/dz
			Hashtable<Integer, Vector<Double>> yIndexedDByDzSets = new Hashtable<Integer, Vector<Double>>(numberOfYCoords);
			
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
				Vector<Integer> sortedZCoords = sortedZCoordsSets.elementAt(yCoordIndex);
				int numberOfZCoords = sortedZCoords.size();
				Vector<Double> vPrimeSquaredMinusWPrimeSquared = new Vector<Double>(numberOfZCoords);
				
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
					double meanVPrimeSquared = Math.pow(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_RMS_V_PRIME), 2);
					double meanWPrimeSquared = Math.pow(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_RMS_W_PRIME), 2);
					vPrimeSquaredMinusWPrimeSquared.add(meanVPrimeSquared - meanWPrimeSquared);
				}
				
				yIndexedDByDzSets.put(yCoord, MAJFCMaths.derivativesIntegerPositions(vPrimeSquaredMinusWPrimeSquared, sortedZCoords));
			}
			
			// Calculate d/dy of the d/dz to give d2/dzdy...
			Hashtable<Integer, Vector<Integer>> yCoordIndexedLookup = DAFrame.getBackEndAPI().getYCoordIndexedSortedDataPointCoordinates(mDataSetId);
			Hashtable<Integer, Vector<Integer>> zCoordIndexedLookup = DAFrame.getBackEndAPI().getZCoordIndexedSortedDataPointCoordinates(mDataSetId);
			
			// ...collect all the d/dz values for each z-coordinate then do the d/dy.
			Enumeration<Integer> zCoordsEnum = zCoordIndexedLookup.keys();
			Vector<Integer> sortedZCoords = new Vector<Integer>(20);

			while (zCoordsEnum.hasMoreElements()) {
				sortedZCoords.add(zCoordsEnum.nextElement());
			}

			Collections.sort(sortedZCoords);
			int mainIndexMax = sortedZCoords.size();
			
			// Go through all the z-coordinates
			for (int mainIndex = 0; mainIndex < mainIndexMax; ++mainIndex) {
				Integer zCoord = sortedZCoords.elementAt(mainIndex);
				Vector<Integer> yCoords = zCoordIndexedLookup.get(zCoord);
				Vector<Double> dByDzsForThisZ = new Vector<Double>(1000);
				numberOfYCoords = yCoords.size();
				
				// Go through the y-coordinates for which data points exist for this z-coordinate
				for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
					int yCoord = yCoords.elementAt(yCoordIndex);
					Vector<Integer> zCoords = yCoordIndexedLookup.get(yCoord);
					
					// Go through the list of z-coordinates for this y-coordinate until we find this z-coordinate.
					// The index of this should correspond to the index of the d/dz value for this point in the list held
					// in yIndexedDByDzSets for the corresponding y-coordinate 
					int numberOfZCoords = zCoords.size();
					
					for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
						int thisZCoord = zCoords.elementAt(zCoordIndex);
						
						if (thisZCoord == zCoord) {
							Vector<Double> dByDzsForThisY = yIndexedDByDzSets.get(yCoord);
							dByDzsForThisZ.add(dByDzsForThisY.elementAt(zCoordIndex));
							break;
						}
					}
				}
				
				// At this point we should have a full list of d/dz values at this z-coordinate, corresponding
				// to the y-coordinates in yCoords
				Vector<Double> d2yDyDzs = MAJFCMaths.derivativesIntegerPositions(dByDzsForThisZ, yCoords);
				int numberOfData = d2yDyDzs.size();
				
				for (int i = 0; i < numberOfData; ++i) {
					Double[] datum = new Double[3];
					
					datum[yCoordIndexInDataArray] = yCoords.elementAt(i).doubleValue();
					datum[zCoordIndexInDataArray] = zCoord.doubleValue();
	
					datum[dataIndexInDataArray] = d2yDyDzs.elementAt(i);
					dataValues.add(datum[dataIndexInDataArray]);
					
					data.add(datum);
				}
			}
			
			mMean = MAJFCMaths.mean(dataValues);
		}		
		
		/**
		 * Prepares the data array as Perkins' P4 vorticity equation component
		 * {@link this#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, int, int, int)}
		 */
		protected void preparePerkinsP4(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) throws BackEndAPIException, MAJFCToolsException {
			int numberOfYCoords = sortedYCoords.size();
			Vector<Double> dataValues = new Vector<Double>(1000);

			// Calculate d2/dz2
			Hashtable<Integer, Vector<Double>> yIndexedD2ByDz2Sets = new Hashtable<Integer, Vector<Double>>(numberOfYCoords);
			
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
				Vector<Integer> sortedZCoords = sortedZCoordsSets.elementAt(yCoordIndex);
				int numberOfZCoords = sortedZCoords.size();
				Vector<Double> vPrimeWPrimeBarsForThisY = new Vector<Double>(numberOfZCoords);
				
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
					vPrimeWPrimeBarsForThisY.add(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN));
				}
				
				yIndexedD2ByDz2Sets.put(yCoord, MAJFCMaths.derivativesIntegerPositions(MAJFCMaths.derivativesIntegerPositions(vPrimeWPrimeBarsForThisY, sortedZCoords), sortedZCoords));
			}

			// Calculate d2/dz2
			Hashtable<Integer, Vector<Integer>> zCoordIndexedLookup = DAFrame.getBackEndAPI().getZCoordIndexedSortedDataPointCoordinates(mDataSetId);
			Enumeration<Integer> zCoordsEnum = zCoordIndexedLookup.keys();
			Hashtable<Integer, Vector<Double>> zIndexedD2ByDy2Sets = new Hashtable<Integer, Vector<Double>>(zCoordIndexedLookup.size());
			
			// Go through all the z-coordinates
			while (zCoordsEnum.hasMoreElements()) {
				Integer zCoord = zCoordsEnum.nextElement();
				Vector<Integer> sortedYCoordsForThisZCoord = zCoordIndexedLookup.get(zCoord);
				int numberOfYCoordsForThisZCoord = sortedYCoordsForThisZCoord.size();
				Vector<Double> vPrimeWPrimeBarsForThisZ = new Vector<Double>(numberOfYCoordsForThisZCoord);
				
				for (int yCoordIndex = 0; yCoordIndex < numberOfYCoordsForThisZCoord; ++yCoordIndex) {
					Integer yCoord = sortedYCoordsForThisZCoord.elementAt(yCoordIndex);
					vPrimeWPrimeBarsForThisZ.add(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_PRIME_W_PRIME_MEAN));
				}
//				Vector<Double> test1 = MAJFCMaths.derivativesIntegerPositions(vPrimeWPrimeBarsForThisZ, sortedYCoordsForThisZCoord);
//				Vector<Double> test2 = MAJFCMaths.derivativesIntegerPositions(test1, sortedYCoordsForThisZCoord);
				
				zIndexedD2ByDy2Sets.put(zCoord, MAJFCMaths.derivativesIntegerPositions(MAJFCMaths.derivativesIntegerPositions(vPrimeWPrimeBarsForThisZ, sortedYCoordsForThisZCoord), sortedYCoordsForThisZCoord));
			}
			
			// Now put the d2/dz2 and d2/dy2 together
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
				Vector<Integer> sortedZCoords = sortedZCoordsSets.elementAt(yCoordIndex);
				int numberOfZCoords = sortedZCoords.size();
				
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords ; ++zCoordIndex) {
					Integer zCoord = sortedZCoords.elementAt(zCoordIndex);
					
					// Here we have a y- and z-coordinate from the original sorted coordinate lists.
					// Find the d2ByDz2 for those coordinates
					double d2ByDz2 = 0;
					// Block just to make scope clearer to avoid bugs!
					{
						Vector<Double> d2ByDz2s = yIndexedD2ByDz2Sets.get(yCoord);
						d2ByDz2 = d2ByDz2s.elementAt(zCoordIndex);
					}
					
					double d2ByDy2 = 0;
					// Block just to make scope clearer to avoid bugs!
					{
						Vector<Integer> yCoords = zCoordIndexedLookup.get(zCoord);
						Vector<Double> d2ByDy2s = zIndexedD2ByDy2Sets.get(zCoord);
						
						int numberOfD2ByDy2s = d2ByDy2s.size();
						
						for (int i = 0; i < numberOfD2ByDy2s; ++i) {
							if (yCoords.elementAt(i).equals(yCoord)) {
								d2ByDy2 = d2ByDy2s.elementAt(i);
								break;
							}
						}
					}
					
					Double[] datum = new Double[3];
						
					datum[yCoordIndexInDataArray] = yCoord.doubleValue();
					datum[zCoordIndexInDataArray] = zCoord.doubleValue();
		
					datum[dataIndexInDataArray] = d2ByDz2 - d2ByDy2;
					dataValues.add(datum[dataIndexInDataArray]);
						
					data.add(datum);
				}
				
			}
			
			mMean = MAJFCMaths.mean(dataValues);
		}		

		/**
		 * Prepares the data array as Term 4
		 * {@link this#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, int, int, int)}
		 */
		protected void preparePerkinsTerm4(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) throws BackEndAPIException, MAJFCToolsException {
			int numberOfData = mViscousDiffusionData.size();
			Vector<Double> dataValues = new Vector<Double>(numberOfData);
			Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedSumLookup = new Hashtable<Integer, Hashtable<Integer,Double>>(100);
			
			// We don't know that all three data arrays are in the same order
			for (int i = 0; i < numberOfData; ++i) {
				Integer yCoord = mViscousDiffusionData.elementAt(i)[yCoordIndexInDataArray].intValue();
				Integer zCoord = mViscousDiffusionData.elementAt(i)[zCoordIndexInDataArray].intValue();
				double datum = mViscousDiffusionData.elementAt(i)[dataIndexInDataArray];
				
				addToSum(yThenZIndexedSumLookup, yCoord, zCoord, datum);
				
				yCoord = mPerkinsP3Data.elementAt(i)[yCoordIndexInDataArray].intValue();
				zCoord = mPerkinsP3Data.elementAt(i)[zCoordIndexInDataArray].intValue();
				datum = mPerkinsP3Data.elementAt(i)[dataIndexInDataArray];
				
				addToSum(yThenZIndexedSumLookup, yCoord, zCoord, datum);
				
				yCoord = mPerkinsP4Data.elementAt(i)[yCoordIndexInDataArray].intValue();
				zCoord = mPerkinsP4Data.elementAt(i)[zCoordIndexInDataArray].intValue();
				datum = mPerkinsP4Data.elementAt(i)[dataIndexInDataArray];
				
				addToSum(yThenZIndexedSumLookup, yCoord, zCoord, datum);
			}
			
			int numberOfYCoords = sortedYCoords.size();
			int datumIndex = 0;
			
			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++ yCoordIndex) {
				Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
				Vector<Integer> zCoords = sortedZCoordsSets.elementAt(yCoordIndex);
				int numberOfZCoords = zCoords.size();
				
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Integer zCoord = zCoords.elementAt(zCoordIndex);
					Double sumDatum = yThenZIndexedSumLookup.get(yCoord).get(zCoord);
					data.add(datumIndex++, new Double[] { yCoord.doubleValue(), zCoord.doubleValue(), sumDatum });
					dataValues.add(sumDatum);
				}
			}
			
			mMean = MAJFCMaths.mean(dataValues);
		}

		/**
		 * Prepares the data array as LHS of vorticity equation
		 * {@link this#prepareDataArray(AbstractDataSetUniqueId, Vector, Vector, Vector, int, int, int)}
		 */
		protected void prepareLHSOfVorticityEquation(AbstractDataSetUniqueId dataSetId, Vector<Integer> sortedYCoords, Vector<Vector<Integer>> sortedZCoordsSets, Vector<Double[]> data, int yCoordIndexInDataArray, int zCoordIndexInDataArray, int dataIndexInDataArray) throws BackEndAPIException, MAJFCToolsException {
			Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedDWByDyMinusDVByDz;
			
			try {
				yThenZIndexedDWByDyMinusDVByDz = StreamwiseVorticityGraph.calculatePerkinsEpsilon(dataSetId, sortedYCoords, sortedZCoordsSets);
			} catch (BackEndAPIException e) {
				e.printStackTrace();
				return;
			} catch (MAJFCToolsException e) {
				e.printStackTrace();
				return;
			}
			
			// Now we have y-indexed then z-indexed lookup of E = (dW/dy - dV/dz)

			// Calculate dE/dy
			Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedDEByDy = calculateDByDy(yThenZIndexedDWByDyMinusDVByDz, sortedYCoords, sortedZCoordsSets, 1);

			// Calculate dE/dz
			Hashtable<Integer, Hashtable<Integer, Double>> yThenZIndexedDEByDz = calculateDByDz(yThenZIndexedDWByDyMinusDVByDz, sortedYCoords, sortedZCoordsSets, 1);

			// Now we have two y- then z-coordinate indexed lookups, one of dE/dy and one of dE/dz
			int numberOfYCoords = sortedYCoords.size();
			Vector<Double> dataValues = new Vector<Double>(1000);

			for (int yCoordIndex = 0; yCoordIndex < numberOfYCoords; ++yCoordIndex) {
				Integer yCoord = sortedYCoords.elementAt(yCoordIndex);
				Vector<Integer> zCoordsForThisY = sortedZCoordsSets.elementAt(yCoordIndex);
				int numberOfZCoords = zCoordsForThisY.size();
							
				for (int zCoordIndex = 0; zCoordIndex < numberOfZCoords; ++zCoordIndex) {
					Integer zCoord = zCoordsForThisY.elementAt(zCoordIndex);
					Double[] datum = new Double[3];
					
					datum[yCoordIndexInDataArray] = yCoord.doubleValue();
					datum[zCoordIndexInDataArray] = zCoord.doubleValue();

					double pointV = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_V_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					double pointW = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, BackEndAPI.DPS_KEY_W_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY);
					
					datum[dataIndexInDataArray] = pointV * yThenZIndexedDEByDy.get(yCoord).get(zCoord) + pointW * yThenZIndexedDEByDz.get(yCoord).get(zCoord);
					dataValues.add(datum[dataIndexInDataArray]);
					
					data.add(datum);
				}
			}
			
			mMean = MAJFCMaths.mean(dataValues);
		}		

		@Override
		protected void showTheVerticalSectionGraph() {
			if (mVerticalSectionGraph == null) {
				String gridTitle = "", xAxisTitle = "", yAxisTitle = "", legendText = "";

				if (mTermId.equals(TERM_1)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_2)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_3)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_4)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_5)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL);
				}
			
				mVerticalSectionGraph = new SectionGraph(this, SectionGraph.VERTICAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
					@Override
					/**	
					 * WindowListener implementation
					 */
					public void windowClosing(WindowEvent theEvent) {
						mVerticalSectionGraph = null;
					}
				});
			} else {
				mVerticalSectionGraph.addOrRemoveSeries(307);
			}
		}

		@Override
		protected void showTheHorizontalSectionGraph() {
			if (mHorizontalSectionGraph == null) {
				String gridTitle = "", xAxisTitle = "", yAxisTitle = "", legendText = "";

				if (mTermId.equals(TERM_1)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_1_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_2)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_2_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_3)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_3_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_4)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_4_GRID_LEGEND_KEY_LABEL);
				} else if (mTermId.equals(TERM_5)) {
					gridTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_FRAME_TITLE);
					xAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE);
					yAxisTitle = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE);
					legendText = DAStrings.getString(DAStrings.VERTICAL_DISTRIBUTION_OF_VORTICITY_EQUATION_TERM_5_GRID_LEGEND_KEY_LABEL);
				}
			
				mHorizontalSectionGraph = new SectionGraph(this, SectionGraph.HORIZONTAL_SECTION, mDataSetId, getXYZDataSet(), null, gridTitle, xAxisTitle, yAxisTitle, legendText, new WindowAdapter() {
					@Override
					/**	
					 * WindowListener implementation
					 */
					public void windowClosing(WindowEvent theEvent) {
						mHorizontalSectionGraph = null;
					}
				});
			} else {
				mHorizontalSectionGraph.addOrRemoveSeries(307);
			}
		}

		@Override
		protected Vector<String> getDepthAveragedGraphLabels() {
			String gridTitle = "", xAxisTitle = "", yAxisTitle = "";

			if (mTermId.equals(TERM_1)) {
				gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_1_GRID_Y_AXIS_TITLE);
			} else if (mTermId.equals(TERM_2)) {
				gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_2_GRID_Y_AXIS_TITLE);
			} else if (mTermId.equals(TERM_3)) {
				gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_3_GRID_Y_AXIS_TITLE);
			} else if (mTermId.equals(TERM_4)) {
				gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_4_GRID_Y_AXIS_TITLE);
			} else if (mTermId.equals(TERM_5)) {
				gridTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_TITLE);
				xAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_X_AXIS_TITLE);
				yAxisTitle = DAStrings.getString(DAStrings.DEPTH_AVERAGED_VORTICITY_EQUATION_TERM_5_GRID_Y_AXIS_TITLE);
			}

			Vector<String> graphLabels = new Vector<String>(3);
			graphLabels.add(gridTitle);
			graphLabels.add(xAxisTitle);
			graphLabels.add(yAxisTitle);
			
			return graphLabels;
		}
	}	
}
