/**
 * 
 */
package com.mikejesson.vsa.frontEnd.crossSectionGraphs;


import java.awt.HeadlessException;
import java.util.Hashtable;
import java.util.Vector;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCTools.MAJFCToolsException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointSummaryIndex;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DADefinitions;



/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class TurbulenceGenerationOrDissipationGraph extends ReynoldsStressGraph {
	Hashtable<Integer, Hashtable<Integer, Double>> mCoord1ThenCoord2SortedUDerivativesLookup;
	boolean mDissipation;
	
	/**
	 * Constructor
	 * @param yCoord The yCoord of the section to display
	 * @param title The title for the graph
	 * @param windowListener The window listener for this display
	 * @throws HeadlessException
	 * @throws BackEndAPIException 
	 */
	public TurbulenceGenerationOrDissipationGraph(AbstractDataSetUniqueId dataSetId, MAJFCStackedPanelWithFrame parent, DataPointSummaryIndex theReynoldsStressDataPointSummaryIndex, String title, String legendText, boolean dissipation) throws HeadlessException, BackEndAPIException {
		super(dataSetId, parent, dissipation, theReynoldsStressDataPointSummaryIndex, title, legendText, false);
	}
	
	/**
	 * Does anything that needs doing before the main body of the parent constructor is called
	 * This should be called at start of any override method in child classes
	 */
	protected void initialise(Object[] initialisationObjects) {
		super.initialise(initialisationObjects);
		
		mDissipation = (Boolean) initialisationObjects[0];
	}
	
	/**
	 * Gets the specified datum for the specified point from the specified data set
	 * {@link AbstractCrossSectionColourCodedGraph#getDatumAt(AbstractDataSetUniqueId, DataPointSummaryIndex, int, int)}
	 */
	protected double getDatumAt(AbstractDataSetUniqueId dataSetId, DataPointSummaryIndex dpsIndex, int yCoord, int zCoord) throws BackEndAPIException {
		double datum = DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, zCoord, mDPSIndex);
		
		if (mCoord1ThenCoord2SortedUDerivativesLookup == null) {
			mCoord1ThenCoord2SortedUDerivativesLookup = new Hashtable<Integer, Hashtable<Integer, Double>>(100);
		}
		
		int coord1, coord2;
		if (dpsIndex.equals(BackEndAPI.DPS_KEY_U_PRIME_V_PRIME_MEAN)) {
			coord1 = zCoord;
			coord2 = yCoord;
		} else {
			coord1 = yCoord;
			coord2 = zCoord;
		}
		
		Hashtable<Integer, Double> coord2SortedDerivativesLookup = mCoord1ThenCoord2SortedUDerivativesLookup.get(coord1);
		
		if (coord2SortedDerivativesLookup == null) {
			Vector<Integer> sortedCoord2s;
			
			if (coord1 == yCoord) {
				sortedCoord2s = DAFrame.getBackEndAPI().getYCoordIndexedSortedDataPointCoordinates(dataSetId).get(yCoord);
			} else {
				sortedCoord2s = DAFrame.getBackEndAPI().getZCoordIndexedSortedDataPointCoordinates(dataSetId).get(zCoord);
			}
			
			int numberOfCoord2s = sortedCoord2s.size();
			Vector<Double> coord2SortedVelocities = new Vector<Double>(numberOfCoord2s);
			Vector<Double> sortedCoord2sInSIUnits = new Vector<Double>(numberOfCoord2s);
			Vector<Double> coord2SortedUDerivatives  = new Vector<Double>(numberOfCoord2s);
			
			for (int coord2Index = 0; coord2Index < numberOfCoord2s; ++ coord2Index) {
				int thisCoord2 = sortedCoord2s.elementAt(coord2Index);
				
				if (coord1 == yCoord) {
					coord2SortedVelocities.add(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, yCoord, thisCoord2, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY));
				} else {
					coord2SortedVelocities.add(DAFrame.getBackEndAPI().getDataPointSummaryDataFieldAtPoint(dataSetId, thisCoord2, zCoord, BackEndAPI.DPS_KEY_U_MEAN_FILTERED_AND_TRANSLATED_AND_BATCH_RC_VELOCITY));
				}

				sortedCoord2sInSIUnits.add(((double) thisCoord2)/1000);
			}
			
			try {
				coord2SortedUDerivatives = MAJFCMaths.derivatives(coord2SortedVelocities, sortedCoord2sInSIUnits);
			} catch (MAJFCToolsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			coord2SortedDerivativesLookup = new Hashtable<Integer, Double>(numberOfCoord2s);
			for (int coord2Index = 0; coord2Index < numberOfCoord2s; ++ coord2Index) {
				coord2SortedDerivativesLookup.put(sortedCoord2s.elementAt(coord2Index), coord2SortedUDerivatives.elementAt(coord2Index));
			}
			
			mCoord1ThenCoord2SortedUDerivativesLookup.put(yCoord, coord2SortedDerivativesLookup);
		}
		
		//return datum/(DADefinitions.KINEMATIC_VISCOSITY_MU * zSortedDerivativesLookup.get(zCoord));
		if (mDissipation) {
			double kinematicViscosity = 1.0;
			try {
				kinematicViscosity = DAFrame.getBackEndAPI().getConfigData(mDataSetId).get(BackEndAPI.DSC_KEY_FLUID_KINEMATIC_VISCOSITY);
				kinematicViscosity *= 1E-6;
			} catch (BackEndAPIException theException) {
			}
			return kinematicViscosity * Math.pow(coord2SortedDerivativesLookup.get(coord2), 2);
		} else {
			return datum * coord2SortedDerivativesLookup.get(coord2);
		}
	}
}
