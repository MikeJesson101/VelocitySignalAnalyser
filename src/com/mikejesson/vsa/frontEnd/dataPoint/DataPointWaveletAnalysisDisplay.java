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

package com.mikejesson.vsa.frontEnd.dataPoint;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.data.RangeType;
import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCLinkedGUIComponentsAction;
import com.mikejesson.majfc.helpers.MAJFCMaths;
import com.mikejesson.majfc.helpers.MAJFCMaths.FourierTransformResults;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.majfc.helpers.wavelets.BussowCWT;
import com.mikejesson.majfc.helpers.wavelets.MAJFCCoifletWavelet;
import com.mikejesson.majfc.helpers.wavelets.MAJFCDaubechiesWavelet;
import com.mikejesson.majfc.helpers.wavelets.MAJFCLegendreWavelet;
import com.mikejesson.majfc.helpers.wavelets.MAJFCWavelet;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.frontEnd.crossSectionGraphs.DepthAveragedGraph;
import com.mikejesson.vsa.frontEnd.verticalAndHorizontalSectionGraphs.SectionGraph;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ColourCodedChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ExportableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYDataSet;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.ScaleableChartPanel.ScaleableXYZDataSet;
import com.mikejesson.vsa.widgits.basicAndAbstractGraphs.XYDataSetAdapter;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class DataPointWaveletAnalysisDisplay extends MAJFCStackedPanelWithFrame {
	private final boolean mUsingCWT;
	private final boolean mScaleByInstPower;

	private JTabbedPane mTabbedPane;
	private ScaleableChartPanel mScaleogramChartPanel;
	private JFreeChart mTheScaleogramChart;
	private JMenuItem mExportLevelsDataButton;
	private Double[] mScaleogramData;
	private ScaleogramDataSet mScaleogramChartDataSet;
	private ScaleableChartPanel mReconstructionChartPanel;
	private JFreeChart mTheReconstructionChart;
	private double[][][] mLevelsData;
	private LevelsDataSet mLevelsDataSet;
	private ReconstructionDataSet mReconstructionChartDataSet;
	private Vector<JComponent> mLevelSelectors;
	private Vector<Double> mPseudoFrequencies;
	private ExportableChartPanel mByFrequencyChartPanel;
	private JFreeChart mTheByFrequencyChart;
	private ByFrequencyDataSet mByFrequencyChartDataSet;
	private Vector<Double> mPowerByFrequency;
	private ExportableChartPanel mByTimeChartPanel;
	private JFreeChart mTheByTimeChart;
	private ByTimeDataSet mByTimeChartDataSet;
	private Vector<Double> mPowerByTime;
	private Vector<Double> mTimes;
	private Vector<String> mLegendEntries;
	private ContourXYZDataSet mContourXYZDataSet;
	private ColourCodedChartPanel mContourChartPanel;
	private JFreeChart mContourChart;

	private final AbstractDataSetUniqueId mDataSetId;
	
	private SectionGraph mVerticalSectionVelocityGraph;
	private SectionGraph mHorizontalSectionVelocityGraph;
	private DepthAveragedGraph mDepthAveragedGraph;
	
	private final int RECON_TIME_INDEX = 0;
	private final int RECON_VELOCITY_INDEX = 1;
	private final int DWT_PSD_INDEX = 2;
	
//	private final double POWER_LIMIT_PERCENTAGE = 0.01;

	/**
	 * @param title The title (column header) for this display
	 * @param legendEntries The legend entries (should match the order of velocitiesSets)
	 * @param velocitiesSets The velocity measurements for the first component of the data points to display power spectra for
	 * @param velocitiesSets2 The velocity measurements for the second component of the data points to display power spectra for
	 * @throws Exception 
	 * 
	 */
	public DataPointWaveletAnalysisDisplay(AbstractDataSetUniqueId dataSetId, Vector<String> legendEntries, Vector<Double> velocitiesSet) throws Exception {
		super(new GridBagLayout());
		
		mLegendEntries = legendEntries;
		mDataSetId = dataSetId;
	
		DataSetConfig configData = BackEndAPI.getBackEndAPI().getConfigData(dataSetId);
		mUsingCWT = configData.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_TYPE).intValue() == BackEndAPI.WTT_CWT.getIntIndex();
		mScaleByInstPower = configData.get(BackEndAPI.DSC_KEY_WAVELET_TRANSFORM_SCALE_BY_INST_POWER) == 1d;
		
		if (mUsingCWT) {
			buildForCWT(velocitiesSet);
		} else {
			buildForFWT(velocitiesSet);
		}
	}
	
	/**
	 * Sets everything for Fast Wavelet Transform
	 * @param velocitiesSets
	 * @throws BackEndAPIException
	 */
	private void buildForFWT(Vector<Double> velocitiesSet) throws BackEndAPIException {
		DataSetConfig configData = BackEndAPI.getBackEndAPI().getConfigData(mDataSetId);
		double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
		
		double yScaleogramMax = Double.MIN_NORMAL, yScaleogramMin = Double.MAX_VALUE;
		double yLevelsMax = Double.MIN_NORMAL, yLevelsMin = Double.MAX_VALUE;
		
		int numberOfUnpaddedVelocities = velocitiesSet.size();
		
		int powerOfTwo = MAJFCMaths.nearestPowerOfTwo(numberOfUnpaddedVelocities, true);
		int numberOfPaddedVelocities = (int) Math.pow(2d, powerOfTwo);
		
		// Pad the set. Use zeroes to prevent changing the energy.
		double padValue = 0;//MAJFCMaths.mean(velocitiesSet);
		Vector<Double> paddedVels = new Vector<Double>(numberOfPaddedVelocities);
		int startOfTrueData = (numberOfPaddedVelocities - numberOfUnpaddedVelocities)/2;
		for (int i = 0; i < numberOfPaddedVelocities; ++i) {
			if (i < startOfTrueData) {
				paddedVels.add(padValue);
			} else if ((i - startOfTrueData) < numberOfUnpaddedVelocities) {
				paddedVels.add(velocitiesSet.get(i - startOfTrueData));
			} else {
				paddedVels.add(padValue);
			}
		}
		
		int numberOfLevels = powerOfTwo + 1;
		mLevelsData = new double[numberOfLevels][][];
		mPseudoFrequencies = new Vector<Double>(numberOfLevels);
		
		MAJFCWavelet fwt = createFWTWavelet(configData.get(BackEndAPI.DSC_KEY_WAVELET_TYPE));

		// Number of samples N = 2^n
		// Number of levels L = n + 1
		Vector<Double> wavelets = fwt.transform(paddedVels);
		Vector<Double> yLevelsSum = new Vector<Double>(numberOfUnpaddedVelocities);
			
		for (int levelIndex = -1; levelIndex < numberOfLevels - 1; ++levelIndex) {
			int startCoeffIndex = (int) Math.pow(2, levelIndex);
			int endCoeffIndex = startCoeffIndex + (levelIndex == -1 ? 1 : startCoeffIndex);
			
			Vector<Double> pfReconstructionCoeffs = new Vector<Double>(numberOfPaddedVelocities);
			for (int i = 0; i < numberOfPaddedVelocities; ++i) {
				pfReconstructionCoeffs.add((i >= startCoeffIndex && i < endCoeffIndex) ? wavelets.get(i) : 0d);
			}
			
			Vector<Double> reconstruction = fwt.inverseTransform(pfReconstructionCoeffs);

//			mPseudoFrequencies.add(Math.pow(2, levelIndex) * samplingRate/((double) numberOfVelocitiesWhenPadded));
			estimatePseudoFrequency(reconstruction, samplingRate, mPseudoFrequencies);
			
//			mPseudoFrequencies.add(levelIndex == -1 ? 0 : estimatePseudoFrequencyFFT(reconstruction, samplingRate));

			mLevelsData[levelIndex + 1] = new double[numberOfUnpaddedVelocities][3];

			for (int i = 0; i < numberOfUnpaddedVelocities; ++i) {
				mLevelsData[levelIndex + 1][i][RECON_TIME_INDEX] = ((double) i)/samplingRate;
				mLevelsData[levelIndex + 1][i][RECON_VELOCITY_INDEX] = reconstruction.elementAt(i + startOfTrueData);
				mLevelsData[levelIndex + 1][i][DWT_PSD_INDEX] = Math.pow(mLevelsData[levelIndex + 1][i][RECON_VELOCITY_INDEX], 2); 
			}
						
			for (int i = 0; i < numberOfUnpaddedVelocities; ++i) {
				double reconstructedVelocity = mLevelsData[levelIndex + 1][i][RECON_VELOCITY_INDEX];
				if (levelIndex == -1) {
					yLevelsSum.add(reconstructedVelocity);
				} else {
					yLevelsSum.set(i, yLevelsSum.get(i) + reconstructedVelocity);
				}
				
				yLevelsMin = Math.min(yLevelsMin, Math.min(reconstructedVelocity, yLevelsSum.get(i)));
				yLevelsMax = Math.max(yLevelsMax, Math.max(reconstructedVelocity, yLevelsSum.get(i)));
			}
		}

		mScaleogramData = wavelets.subList(0, numberOfUnpaddedVelocities).toArray(new Double[numberOfUnpaddedVelocities]);

		for (int i = 0; i < numberOfUnpaddedVelocities; ++i) {
			yScaleogramMin = Math.min(yScaleogramMin, wavelets.elementAt(i));
			yScaleogramMax = Math.max(yScaleogramMax, wavelets.elementAt(i));
		}

		mScaleogramChartDataSet = new ScaleogramDataSet(0, numberOfUnpaddedVelocities, yScaleogramMin, yScaleogramMax);
		mLevelsDataSet = new LevelsDataSet(0, ((double) numberOfUnpaddedVelocities)/samplingRate, yLevelsMin, yLevelsMax);
		mReconstructionChartDataSet = new ReconstructionDataSet(0, ((double) numberOfUnpaddedVelocities)/samplingRate, yLevelsMin, yLevelsMax);
		
		mContourXYZDataSet = new ContourXYZDataSet(1, 1, 0, Math.ceil(mLevelsData[0][numberOfUnpaddedVelocities - 1][RECON_TIME_INDEX]), mPseudoFrequencies.firstElement(), mPseudoFrequencies.lastElement());
		
		scaleFWT(velocitiesSet, samplingRate);

		buildFWTGUI();
	}
	
	/**
	 * Scales the FWT coefficients.
	 * If mScaleByInstPower == true then the integral over frequency at each time is scaled
	 * to the instantaneous power from the time-domain signal (u(t)^2) before scaling so that the total energy is 1 (i.e.
	 * normalised by the total energy of the time-domain signal).
	 * If mScaleByInstPower == false, just the total energy scaling is done.
	 * @param velocitiesSet The time-domain signal the FWT coefficients are from.
	 * @param samplingRate The sampling rate of the time-domain signal.
	 */
	private void scaleFWT(Vector<Double> velocitiesSet, double samplingRate) {
		int numberOfFrequencies = mLevelsData.length;
		int numberOfTimes = mLevelsData[0].length;
		mPowerByTime = new Vector<Double>(numberOfTimes);
		ArrayList<Double> instPowerScalars = new ArrayList<Double>(numberOfTimes);
		double totalEnergy = 0;
		double deltaT = 1/samplingRate;
		
		for (int timeIndex = 0; timeIndex < numberOfTimes; ++timeIndex) {
			double waveletInstPower = 0;
			double instPowerFromVelocity = Math.pow(velocitiesSet.get(timeIndex), 2);
			
			// Integrate over frequency to get the instantaneous power
			for (int frequencyIndex = 0; frequencyIndex < numberOfFrequencies; ++frequencyIndex) {
				double deltaFreq = 0;
				
				if (frequencyIndex == 0) {
					deltaFreq = 0.5 * (mPseudoFrequencies.get(frequencyIndex + 1) - mPseudoFrequencies.get(frequencyIndex));
				} else if (frequencyIndex == numberOfFrequencies - 1) {
					deltaFreq = 0.5 * (mPseudoFrequencies.get(frequencyIndex) - mPseudoFrequencies.get(frequencyIndex - 1));
				} else {
					deltaFreq = 0.5 * (mPseudoFrequencies.get(frequencyIndex + 1) - mPseudoFrequencies.get(frequencyIndex - 1));
				}
					
				waveletInstPower += mLevelsData[frequencyIndex][timeIndex][DWT_PSD_INDEX] * deltaFreq;
			}
			
			mPowerByTime.add(waveletInstPower);
			
			instPowerScalars.add(mScaleByInstPower ? instPowerFromVelocity/waveletInstPower : 1d);
			
			// If scaling by the instantaneous power, the instPowerScalars will ensure that the integral over time equals energy
			// from the time-domain signal. If not scaling by instantaneous power, instPowerScalars are all 1, and we need to scale
			// by the energy in the wavelet domain.
			totalEnergy += (mScaleByInstPower ? instPowerFromVelocity : waveletInstPower) * deltaT;
		}
		
		// Now rescale the DWT output, ensuring that the integral over frequency gives the instantaneous power, and normalising by the total energy
		for (int timeIndex = 0; timeIndex < numberOfTimes; ++timeIndex) {
			double instPowerScalar = instPowerScalars.get(timeIndex);
			
			for (int frequencyIndex = 0; frequencyIndex < numberOfFrequencies; ++frequencyIndex) {
				mLevelsData[frequencyIndex][timeIndex][DWT_PSD_INDEX] *= instPowerScalar/totalEnergy;
			}
		}		
	}
	
	/**
	 * Estimates the frequency using the number of crossings of zero
	 * @param levelData
	 * @param samplingRate
	 * @return the estimate of the frequency
	 */
	private double estimatePseudoFrequencyFromCrossings(Vector<Double> levelData, double samplingRate) {
		int numberOfFallingCrossings = 0;
		int numberOfValues = levelData.size();
		
		for (int i = 1; i < numberOfValues; ++i) {
			if (levelData.get(i) < 0 && levelData.get(i-1) > 0) {
				++numberOfFallingCrossings;			
			}
		}
		
		return ((double) numberOfFallingCrossings)/(((double) numberOfValues)/samplingRate);
	}
	
	/**
	 * Estimates the frequency using the wavenumber with the maximum power density in the FFT
	 * @param levelData
	 * @param samplingRate
	 * @return the estimate of the frequency
	 */
	private void estimatePseudoFrequency(Vector<Double> levelData, double samplingRate, Vector<Double> pseudoFrequencies) {
		int numberOfPFsCalculated = pseudoFrequencies.size();
		
		if (numberOfPFsCalculated == 0) {
			pseudoFrequencies.add(0d);
			return;
		}

		FourierTransformResults ftr = MAJFCMaths.fastFourierTransform(levelData);
		
		double max = Double.MIN_VALUE;
		int maxWavenumber = 0;
		int numberOfValues = levelData.size();
		int halfNumberOfValues = numberOfValues/2;
		
		for (int i = 0; i < halfNumberOfValues; ++i) {
			double ftrValue = ftr.get(i).magnitude();
			
			if (ftrValue > max) {
				max = ftrValue;
				maxWavenumber = i;
			}
		}
		
		double pseudoFreq = maxWavenumber/(((double) numberOfValues)/samplingRate);
		double lastFreq = pseudoFrequencies.get(numberOfPFsCalculated - 1);
		if (pseudoFreq == lastFreq) {
			pseudoFreq = estimatePseudoFrequencyFromCrossings(levelData, samplingRate);

			if (pseudoFreq == lastFreq) {
				pseudoFreq = 2 * lastFreq;
			}
		}
		
		pseudoFrequencies.add(pseudoFreq);
	}

	/**
	 * Sets everything for Continuous Wavelet Transform (uses Bussow's CWT algorithm)
	 * @param velocitiesSet
	 * @throws BackEndAPIException
	 */
	private void buildForCWT(Vector<Double> velocitiesSet) throws BackEndAPIException {
		DataSetConfig configData = BackEndAPI.getBackEndAPI().getConfigData(mDataSetId);
		double samplingRate = configData.get(BackEndAPI.DSC_KEY_SAMPLING_RATE);
		int numberOfUnpaddedVelocities = velocitiesSet.size();
		
		int powerOfTwo = MAJFCMaths.nearestPowerOfTwo(numberOfUnpaddedVelocities, true) + 1;
		int numberOfPaddedVelocities = (int) Math.pow(2d, powerOfTwo);
//		double maxPower = Double.NEGATIVE_INFINITY;
//		double minPower = Double.POSITIVE_INFINITY;
		
		double[] velocitiesArr = new double[numberOfPaddedVelocities];

		// Pad the set. Use zeroes to prevent changing the energy.
		double padValue = 0;//MAJFCMaths.mean(velocitiesSet);
//		double meanOfFirstTenVels = MAJFCMaths.mean(velocitiesSet.subList(0, 10));
//		double meanOfLastTenVels = MAJFCMaths.mean(velocitiesSet.subList(velsInSet - 11, velsInSet - 1));
		int startOfTrueData = (numberOfPaddedVelocities - numberOfUnpaddedVelocities)/2;
		for (int i = 0; i < numberOfPaddedVelocities; ++i) {
			if (i < startOfTrueData) {
				velocitiesArr[i] = padValue;
//				velocitiesArr[i] = meanOfFirstTenVels;
			} else if ((i - startOfTrueData) < numberOfUnpaddedVelocities) {
				velocitiesArr[i] = velocitiesSet.get(i - startOfTrueData);
			} else {
				velocitiesArr[i] = padValue;
//				velocitiesArr[i] = meanOfLastTenVels;
			}
		}
		
		BussowCWT cwt = new BussowCWT(velocitiesArr, (int) samplingRate, samplingRate/2d, 100, "log", 8, 1);
		// power[frequency][time]
		// Bussow's energy density is the PSD (I think!)
		double[][] power = cwt.ed();//cwt.pd();
		double energyFromVelocity = Math.pow(MAJFCMaths.standardDeviation(velocitiesSet), 2) * numberOfUnpaddedVelocities * 1d/samplingRate;
		double scalar = 2/cwt.e();
		double[] frequencies = cwt.f;
		double[] times = cwt.t;
		double[] powerByTime = cwt.et(); // Bussow calls this energy, but I think it's power as if you integrate w.r.t. time you will get total energy
		double[] powerByFrequency = cwt.ef(); // Similarly to above, power as if integrate w.r.t. frequency you get the total energy
		mLevelsData = new double[frequencies.length][][];
		
		mPseudoFrequencies = new Vector<Double>(frequencies.length);
		mTimes = new Vector<Double>(numberOfUnpaddedVelocities);
		mPowerByTime = new Vector<Double>(numberOfUnpaddedVelocities);
		mPowerByFrequency = new Vector<Double>(frequencies.length);
		
		for (int frequencyIndex = 0; frequencyIndex < frequencies.length; ++frequencyIndex) {
			mLevelsData[frequencyIndex] = new double[numberOfUnpaddedVelocities][3];
			
			for (int timeIndex = 0; timeIndex < numberOfUnpaddedVelocities; ++timeIndex) {
				int timeIndexInPaddedSeries = timeIndex + startOfTrueData;
				// Normalise the PSD.
				if (mScaleByInstPower) {
					// Make sure that the power at each time step matches the power at that time in the original velocity time-series.
					// Then normalise by the mean power (energy is used for power from the wavelet transform as it differs from power by a factor, and
					// appears in numerator and denominator, and hence the factor cancels).
					double powerByTimeFromVelocity = Math.pow(velocitiesArr[timeIndexInPaddedSeries], 2);
					scalar = (powerByTimeFromVelocity/powerByTime[timeIndexInPaddedSeries])/energyFromVelocity;
				}
				
				double thePower = power[frequencyIndex][timeIndex + startOfTrueData] * scalar;
				double time = times[timeIndex + startOfTrueData] - times[startOfTrueData];
				mLevelsData[frequencyIndex][timeIndex][RECON_TIME_INDEX] = time;
				mLevelsData[frequencyIndex][timeIndex][DWT_PSD_INDEX] = thePower;// == 0 ? Double.NaN : thePower;
				
				if (frequencyIndex == 0) {
					mTimes.add(time);
					mPowerByTime.add(powerByTime[timeIndexInPaddedSeries]);
				}
			}
			
			mPseudoFrequencies.add(frequencies[frequencyIndex]);
			mPowerByFrequency.add(powerByFrequency[frequencyIndex]);
		}

		mByFrequencyChartDataSet = new ByFrequencyDataSet();
		mByTimeChartDataSet = new ByTimeDataSet();

		mContourXYZDataSet = new ContourXYZDataSet(1, 1, 0, Math.ceil(mLevelsData[0][mLevelsData[0].length - 1][RECON_TIME_INDEX]), mPseudoFrequencies.firstElement(), mPseudoFrequencies.lastElement());
		
		buildCWTGUI();
	}

	private MAJFCWavelet createFWTWavelet(double waveletType) {
		MAJFCWavelet wavelet = null;
	    
//	    if (waveletType == BackEndAPI.WT_HAAR02_ORTHO.getIntIndex()) {
//	    	wavelet = new Haar02Orthogonal();
//	    } else 
	    if (waveletType == BackEndAPI.WT_DAUB02.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(2);
	    } else if (waveletType == BackEndAPI.WT_DAUB04.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(4);
	    } else if (waveletType == BackEndAPI.WT_DAUB06.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(6);
	    } else if (waveletType == BackEndAPI.WT_DAUB08.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(8);
	    } else if (waveletType == BackEndAPI.WT_DAUB10.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(10);
	    } else if (waveletType == BackEndAPI.WT_DAUB12.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(12);
	    } else if (waveletType == BackEndAPI.WT_DAUB14.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(14);
	    } else if (waveletType == BackEndAPI.WT_DAUB16.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(16);
	    } else if (waveletType == BackEndAPI.WT_DAUB18.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(18);
	    } else if (waveletType == BackEndAPI.WT_DAUB20.getIntIndex()) {
	    	wavelet = new MAJFCDaubechiesWavelet(20);
	    } else if (waveletType == BackEndAPI.WT_LEGE02.getIntIndex()) {
	    	wavelet = new MAJFCLegendreWavelet(2);
	    } else if (waveletType == BackEndAPI.WT_LEGE04.getIntIndex()) {
	    	wavelet = new MAJFCLegendreWavelet(4);
	    } else if (waveletType == BackEndAPI.WT_LEGE06.getIntIndex()) {
	    	wavelet = new MAJFCLegendreWavelet(6);
	    } else if (waveletType == BackEndAPI.WT_COIF06.getIntIndex()) {
	    	wavelet = new MAJFCCoifletWavelet(6);
	    }
	    	
	    return wavelet;
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildFWTGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		String yAxisLabel = DAStrings.getString(DAStrings.WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_Y_AXIS_LABEL);
		NumberAxis xScaleogramAxis = new NumberAxis(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL));
//		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL));
		NumberAxis yScaleogramAxis = new NumberAxis(yAxisLabel);

		DefaultXYItemRenderer theScaleogramRenderer = new DefaultXYItemRenderer();
		theScaleogramRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mScaleogramChartDataSet.getSeriesCount(); ++i) {
			theScaleogramRenderer.setSeriesShapesVisible(i, false);
		}
		
		XYPlot theScaleogramPlot = new XYPlot(mScaleogramChartDataSet, xScaleogramAxis, yScaleogramAxis, theScaleogramRenderer);
		theScaleogramPlot.setRangeZeroBaselineVisible(true);
		theScaleogramPlot.setDomainZeroBaselineVisible(true);
		
		mTheScaleogramChart = new JFreeChart(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, theScaleogramPlot, mLegendEntries != null && mLegendEntries.size() > 0);
		mScaleogramChartPanel = new ScaleableChartPanel(mDataSetId, this, mTheScaleogramChart, mScaleogramChartDataSet, true, DAStrings.getString(DAStrings.WAVELET_ANALYSIS_SCALEOGRAM_GRAPH_X_AXIS_LABEL), yAxisLabel, false);

		yAxisLabel = DAStrings.getString(DAStrings.WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL);
		NumberAxis xLevelsAxis = new NumberAxis(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL));
//		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.SPECTRAL_DISTRIBUTION_GRAPH_X_AXIS_LABEL));
		NumberAxis yLevelsAxis = new NumberAxis(yAxisLabel);

		DefaultXYItemRenderer theLevelsRenderer = new DefaultXYItemRenderer();
		theLevelsRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mScaleogramChartDataSet.getSeriesCount(); ++i) {
			theLevelsRenderer.setSeriesShapesVisible(i, false);
		}
		
		XYPlot theReconstructionPlot = new XYPlot(mReconstructionChartDataSet, xLevelsAxis, yLevelsAxis, theLevelsRenderer);
		theReconstructionPlot.setRangeZeroBaselineVisible(true);
		theReconstructionPlot.setDomainZeroBaselineVisible(true);
		
		mTheReconstructionChart = new JFreeChart(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_LEVELS_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, theReconstructionPlot, mLegendEntries != null && mLegendEntries.size() > 0);
		mReconstructionChartPanel = new ReconstructionScaleableChartPanel(this);
		mTabbedPane = new JTabbedPane();
		mTabbedPane.add(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_SCALEOGRAM_TAB_LABEL), mScaleogramChartPanel.getGUI());
		mTabbedPane.add(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_RECONSTRUCTION_TAB_LABEL), mReconstructionChartPanel.getGUI());

		if (mContourXYZDataSet != null) {
			NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE));
	//		xAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
			xAxis.setRangeType(RangeType.POSITIVE);
	
			NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE));
	//		yAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getNumberInstance()));
			yAxis.setRangeType(RangeType.POSITIVE);
	
			mContourChart = new JFreeChart(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, new XYPlot(mContourXYZDataSet, xAxis, yAxis, null), false);
			mContourChartPanel = getColourCodedChartPanel();
			mContourChart.getXYPlot().setRenderer(mContourChartPanel.getRenderer());

			mTabbedPane.add(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_TAB_LABEL), mContourChartPanel.getGUI());
		}	

		add(mTabbedPane, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}

	/**
	 * Builds the GUI for this display
	 */
	private void buildCWTGUI() {
		setBorder(BorderFactory.createEtchedBorder());
		
		NumberAxis xByFrequencyAxis = new NumberAxis(DAStrings.getString(DAStrings.CWT_BY_FREQUENCY_GRAPH_X_AXIS_LABEL));
		NumberAxis yByFrequencyAxis = new NumberAxis(DAStrings.getString(DAStrings.CWT_BY_FREQUENCY_GRAPH_Y_AXIS_LABEL));

		DefaultXYItemRenderer theByFrequencyRenderer = new DefaultXYItemRenderer();
//		theByFrequencyRenderer.setSeriesPaint(3, Color.MAGENTA);
		
		for (int i = 0; i < mByFrequencyChartDataSet.getSeriesCount(); ++i) {
			theByFrequencyRenderer.setSeriesShapesVisible(i, false);
		}
		
		XYPlot theByFrequencyPlot = new XYPlot(mByFrequencyChartDataSet, xByFrequencyAxis, yByFrequencyAxis, theByFrequencyRenderer);
		theByFrequencyPlot.setRangeZeroBaselineVisible(true);
		theByFrequencyPlot.setDomainZeroBaselineVisible(true);
		
		mTheByFrequencyChart = new JFreeChart(DAStrings.getString(DAStrings.CWT_BY_FREQUENCY_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, theByFrequencyPlot, mLegendEntries != null && mLegendEntries.size() > 0);
		mByFrequencyChartPanel = new ExportableChartPanel(this, mTheByFrequencyChart);

		NumberAxis xByTimeAxis = new NumberAxis(DAStrings.getString(DAStrings.CWT_BY_TIME_GRAPH_X_AXIS_LABEL));
		NumberAxis yByTimeAxis = new NumberAxis(DAStrings.getString(DAStrings.CWT_BY_TIME_GRAPH_Y_AXIS_LABEL));

		DefaultXYItemRenderer theByTimeRenderer = new DefaultXYItemRenderer();

		for (int i = 0; i < mByTimeChartDataSet.getSeriesCount(); ++i) {
			theByTimeRenderer.setSeriesShapesVisible(i, false);
		}

		XYPlot theByTimePlot = new XYPlot(mByTimeChartDataSet, xByTimeAxis, yByTimeAxis, theByTimeRenderer);
		theByTimePlot.setRangeZeroBaselineVisible(true);
		theByTimePlot.setDomainZeroBaselineVisible(true);
		
		mTheByTimeChart = new JFreeChart(DAStrings.getString(DAStrings.CWT_BY_TIME_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, theByTimePlot, mLegendEntries != null && mLegendEntries.size() > 0);
		mByTimeChartPanel = new ExportableChartPanel(this, mTheByTimeChart);

		mTabbedPane = new JTabbedPane();
		mTabbedPane.add(DAStrings.getString(DAStrings.CWT_BY_FREQUENCY_TAB_LABEL), mByFrequencyChartPanel);
		mTabbedPane.add(DAStrings.getString(DAStrings.CWT_BY_TIME_TAB_LABEL), mByTimeChartPanel);

		NumberAxis xAxis = new NumberAxis(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE));
//		xAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getIntegerInstance()));
		xAxis.setRangeType(RangeType.POSITIVE);

		NumberAxis yAxis = new NumberAxis(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE));
//		yAxis.setTickUnit(new NumberTickUnit(20, NumberFormat.getNumberInstance()));
		yAxis.setRangeType(RangeType.POSITIVE);

		mContourChart = new JFreeChart(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_GRAPH_TITLE), JFreeChart.DEFAULT_TITLE_FONT, new XYPlot(mContourXYZDataSet, xAxis, yAxis, null), false);
		mContourChartPanel = getColourCodedChartPanel();
		XYBlockRenderer renderer = mContourChartPanel.getRenderer();
		renderer.setBaseToolTipGenerator(null);
		mContourChart.getXYPlot().setRenderer(renderer);
		mTabbedPane.add(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_TAB_LABEL), mContourChartPanel.getGUI());

		add(mTabbedPane, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}
	
	private ColourCodedChartPanel getColourCodedChartPanel() {
		double timeStep = mLevelsData[0][1][RECON_TIME_INDEX] - mLevelsData[0][0][RECON_TIME_INDEX];
		double frequencyStep = mPseudoFrequencies.get(1) - mPseudoFrequencies.get(0);
		return new ColourCodedChartPanel(mDataSetId, this, mContourChart, mContourXYZDataSet, frequencyStep == 0 ? 0.01 : frequencyStep, timeStep, 50, DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_X_AXIS_TITLE), DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_Y_AXIS_TITLE), DAStrings.getString(DAStrings.WAVELET_ANALYSIS_CONTOUR_LEGEND_TEXT), false) {
			@Override
			protected void showVerticalSectionGraph() {
				if (mVerticalSectionVelocityGraph == null) {
					mVerticalSectionVelocityGraph = new SectionGraph(getHolderFrame(), SectionGraph.VERTICAL_SECTION, mDataSetId, mContourChartPanel.getXYZDataSet(), 0, DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_Y_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL), new WindowAdapter() {
						@Override
						/**
						 * WindowListener implementation
						 */
						public void windowClosing(WindowEvent theEvent) {
							mVerticalSectionVelocityGraph = null;
						}
					});
				} else {
					mVerticalSectionVelocityGraph.requestFocus();
				}
			}

			@Override
			protected void showHorizontalSectionGraph() {
				if (mHorizontalSectionVelocityGraph == null) {
					mHorizontalSectionVelocityGraph = new SectionGraph(getHolderFrame(), SectionGraph.HORIZONTAL_SECTION, mDataSetId, mContourChartPanel.getXYZDataSet(), 0, DAStrings.getString(DAStrings.HORIZONTAL_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.HORIZONTAL_VELOCITY_GRID_Y_AXIS_TITLE), DAStrings.getString(DAStrings.VERTICAL_VELOCITY_GRID_LEGEND_KEY_LABEL), new WindowAdapter() {
						@Override
						/**
						 * WindowListener implementation
						 */
						public void windowClosing(WindowEvent theEvent) {
							mHorizontalSectionVelocityGraph = null;
						}
					});
				} else {
					mHorizontalSectionVelocityGraph.requestFocus();
				}
			}

			@Override
			protected void showDepthAveragedGraph() {
				if (mDepthAveragedGraph == null) {
					mDepthAveragedGraph = new DepthAveragedGraph(mDataSetId, getHolderFrame(), mContourChartPanel.getXYZDataSet(), false, DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_GRID_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_GRID_X_AXIS_TITLE), DAStrings.getString(DAStrings.DEPTH_AVERAGED_VELOCITY_GRID_Y_AXIS_TITLE), new WindowAdapter() {
						@Override
						/**
						 * WindowListener implementation
						 */
						public void windowClosing(WindowEvent theEvent) {
							mDepthAveragedGraph = null;
						}
					});
				}
			}
		};
	}

	private class ScaleogramDataSet extends ScaleableXYDataSet {
		public ScaleogramDataSet(double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			super(unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mScaleogramData.length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return 1;
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return mLegendEntries.elementAt(series);
		}

		@Override
		public double getTheXValue(int series, int item) {
			return item;//mScaleogramData[series][item][FREQUENCY_INDEX];
		}

		@Override
		public double getTheYValue(int series, int item) {
			return mScaleogramData[item];
		}
	}

	private class LevelsDataSet extends ScaleableXYDataSet {
		public LevelsDataSet(double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			super(unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getItemCount(int series) {
			return mLevelsData[0].length;
		}

		@Override
		/**
		 * XYDataset implementation
		 */
		public int getSeriesCount() {
			return mLevelsData.length;
		}

		@SuppressWarnings("rawtypes")
		@Override
		/**
		 * XYDataset implementation
		 */
		public Comparable getSeriesKey(int series) {
			return "";
		}

		@Override
		public double getTheXValue(int series, int item) {
			return mLevelsData[0][item][RECON_TIME_INDEX];
		}

		@Override
		public double getTheYValue(int level, int item) {
			return mLevelsData[level][item][RECON_VELOCITY_INDEX];
		}
	}
	
	private class ReconstructionDataSet extends LevelsDataSet {
		public ReconstructionDataSet(double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			super(unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}

		@Override
		public int getSeriesCount() {
			return 1;
		}

		public int getNumberOfLevels() {
			return mLevelsData.length;
		}

		@Override
		public double getTheYValue(int level, int item) {
			double sum = 0;
			for (int i = 0; i < mLevelsData.length; ++i) {
				if (mLevelSelectors != null && ((JCheckBox) mLevelSelectors.get(i)).isSelected()) {
					sum += mLevelsData[i][item][RECON_VELOCITY_INDEX];
				}
			}
			
			return sum;
		}

	}
	
	private class ByFrequencyDataSet extends XYDataSetAdapter {
		@Override
		public int getSeriesCount() {
			return 1;
		}

		@Override
		public Comparable getSeriesKey(int series) {
			return series;
		}

		@Override
		public int indexOf(Comparable arg0) {
			return (Integer) arg0;
		}

		@Override
		public int getItemCount(int series) {
			switch (series) {
				case 0:
					return mPowerByFrequency.size();
			}
			
			return 0;
		}

		@Override
		public double getXValue(int series, int item) {
			switch (series) {
				case 0:
					return mPowerByFrequency.get(item);
			}
		
			return 0;
		}

		@Override
		public double getYValue(int series, int item) {
			return mPseudoFrequencies.get(item);
		}
	}
	
	private class ByTimeDataSet extends XYDataSetAdapter {

		@Override
		public int getSeriesCount() {
			return 1;
		}

		@Override
		public Comparable getSeriesKey(int series) {
			return series;
		}

		@Override
		public int indexOf(Comparable arg0) {
			return (Integer) arg0;
		}

		@Override
		public int getItemCount(int series) {
			switch (series) {
				case 0:
					return mPowerByTime.size();
			}
			
			return 0;
		}

		@Override
		public double getXValue(int series, int item) {
			return mTimes.get(item);
		}

		@Override
		public double getYValue(int series, int item) {
			switch (series) {
				case 0:
					return mPowerByTime.get(item);
			}
	
			return 0;
		}
	}
//	private void calculateSkewness() {
//		int numberOfItems = mLevelsChartDataSet.getItemCount(0);
//		Vector<Double> reconstruction = new Vector<Double>(numberOfItems);
//		for (int item = 0; item < numberOfItems; ++item) {
//			reconstruction.add( mLevelsChartDataSet.getTheYValue(0, item));
//		}
//		
//		mLevelsChartPanel.getChart().setTitle("Skewness: " + MAJFCMaths.skewness(reconstruction));
//	}

	protected class ContourXYZDataSet extends ScaleableXYZDataSet {
		public ContourXYZDataSet(double mean, double scalerForDimensionlessValues, double unscaledXMin, double unscaledXMax, double unscaledYMin, double unscaledYMax) {
			super(mean, scalerForDimensionlessValues, unscaledXMin, unscaledXMax, unscaledYMin, unscaledYMax);
		}
	
		@Override
		/**
		 * ScaleableXYDataSet implementation
		 */
		public int getItemCount(int series) {
			if (mLevelsData == null) {
				return 0;
			}
			
			return mLevelsData.length * mLevelsData[0].length;
		}
	
		@Override
		/**
		 * ScaleableXYDataSet implementation
		 */
		public double getTheXValue(int series, int item) {
			int numberOfVelocities = mLevelsData[0].length;
			int levelIndex = (int) Math.floor(item/numberOfVelocities);
			int itemIndex = item % numberOfVelocities;

			return mLevelsData[levelIndex][itemIndex][RECON_TIME_INDEX];
		}
	
		@Override
		/**
		 * ScaleableXYDataSet implementation
		 */
		public double getTheYValue(int series, int item) {
			int numberOfVelocities = mLevelsData[0].length;
			int levelIndex = (int) Math.floor(item/numberOfVelocities);
//			int itemIndex = item % numberOfVelocities;
			
			return mPseudoFrequencies.get(levelIndex);
		}

		@Override
		/**
		 * XYZDataset implementation
		 */
		public double getTheZValue(int series, int item) {
			int numberOfVelocities = mLevelsData[0].length;
			int levelIndex = (int) Math.floor(item/numberOfVelocities);
			int itemIndex = item % numberOfVelocities;

			return mLevelsData[levelIndex][itemIndex][DWT_PSD_INDEX]; 
		}
	}

	@Override
	public void close() {
		// Try to free up some memory
		for (int i = 0; i < mLevelsData.length; ++i) {
			for (int j = 0; j < mLevelsData[i].length; ++j) {
				mLevelsData[i][j] = null;
			}
			
			mLevelsData[i] = null;
		}

		mLevelsData = null;

		super.close();
	}
	
	private class ReconstructionScaleableChartPanel extends ScaleableChartPanel {
		private ReconstructionScaleableChartPanel(MAJFCStackedPanelWithFrame holderFrame) {
			super(mDataSetId, holderFrame, mTheReconstructionChart, mReconstructionChartDataSet, true, DAStrings.getString(DAStrings.WAVELET_ANALYSIS_LEVELS_GRAPH_X_AXIS_LABEL), DAStrings.getString(DAStrings.WAVELET_ANALYSIS_LEVELS_GRAPH_Y_AXIS_LABEL), false);
		}
		
		/**
		 * Gets additional components for the chart. Override this to add extras
		 * @return A list of the components to add
		 */
		@Override
		protected Vector<JComponent> getAdditionalComponents() {
			int numberOfLevels = mReconstructionChartDataSet.getNumberOfLevels();
			mLevelSelectors = new Vector<JComponent>(numberOfLevels);
			Vector<JComponent> additionalComponents = new Vector<JComponent>(numberOfLevels + 5);
			
			for (int i = 0; i < numberOfLevels; ++i) {
				JCheckBox levelSelector = new JCheckBox(DAStrings.getString(DAStrings.WAVELET_ANALYSIS_LEVELS_SELECTOR_LABEL, MAJFCTools.formatNumber(i - 1, 0), MAJFCTools.formatNumber(mPseudoFrequencies.get(i), 2))); 
				levelSelector.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						updateDisplay();
//						calculateSkewness();
					}
				});
				
				if (i % 7 == 0) {
					additionalComponents.add(null);
				}
				
				additionalComponents.add(levelSelector);
				mLevelSelectors.add(levelSelector);
			}
			
			return additionalComponents;
		}
		
		@Override
		/**
		 * Create the right-click context menu
		 * @param properties Flag to show/hide properties menu item
		 * @param save Flag to show/hide save menu item
		 * @param print Flag to show/hide print menu item
		 * @param zoom Flag to show/hide zoom menu item
		 */
		protected JPopupMenu createPopupMenu(boolean properties, boolean copy, boolean save, boolean print, boolean zoom) {
			JPopupMenu theMenu = super.createPopupMenu(false, copy, save, print, false);
			theMenu.addSeparator();
			
			mExportLevelsDataButton = new JMenuItem();
			new MAJFCLinkedGUIComponentsAction(DAStrings.getString(DAStrings.EXPORT_DWT_LEVELS_DATA_BUTTON_LABEL), null, DAStrings.getString(DAStrings.EXPORT_DWT_LEVELS_DATA_BUTTON_DESC), mExportLevelsDataButton, this);
			
			theMenu.add(mExportLevelsDataButton);
			
			return theMenu;
		}
		
		@Override
		public void actionPerformed(ActionEvent theEvent) {
			Action action = ((AbstractButton) theEvent.getSource()).getAction();
			
			if (action == null) {
				super.actionPerformed(theEvent);
				return;
			}

			String actionId = (String) action.getValue(Action.SHORT_DESCRIPTION);
			
			if (actionId.equals(DAStrings.getString(DAStrings.EXPORT_DWT_LEVELS_DATA_BUTTON_DESC))) {
				exportDataAsTable(mLevelsDataSet, 1);
			} else {
				super.actionPerformed(theEvent);
			}
		}
	}
}
