/**
 * 
 */
package com.mikejesson.vsa.frontEnd.importDialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import com.mikejesson.majfc.guiComponents.MAJFCNumberTextAreaPanel;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCTranslatingDropDownPanel;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPI;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DataPointCoordinates;
import com.mikejesson.vsa.backEndExposed.DataSetConfig;
import com.mikejesson.vsa.frontEnd.DAFrame;
import com.mikejesson.vsa.widgits.DAStrings;



/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class ProbeSetupPanel extends MAJFCPanel implements PropertyChangeListener, ActionListener {
	private final AbstractDataSetUniqueId mDataSetId;
	protected MAJFCTranslatingDropDownPanel<Integer> mNumberOfProbes;
	private MAJFCTranslatingDropDownPanel<Integer> mMainProbeId;
	private MAJFCTranslatingDropDownPanel<Integer> mFixedProbeId;
	private MAJFCTranslatingDropDownPanel<Integer> mSynchProbeId;
	private Vector<ProbeDetails> mProbeDetailsList;
	private JScrollPane mScrollPane;
	private MAJFCPanel mProbeDetailsPanel;
	private int mMainProbeIndex;
	private int mFixedProbeIndex;
	private int mSynchProbeIndex;
	private double mLeftBankPosition, mRightBankPosition, mWaterDepth;
	private final boolean mAllowMultipleProbes;
	private final boolean mAllowFixedProbes;
	private final boolean mAllowSynchPairing;
	private LinkedList<DataPointCoordinates> mDefaultCoordsList;
	private final boolean mIsForConfig;
	private MAJFCPanel mGUIParent;
	
	/**
	 * Creates a ProbeSetupPanel for setting up configuration
	 */
	public ProbeSetupPanel(MAJFCPanel parent) {
		this(null, true, true);
		
		mGUIParent = parent;
	}
	
	public ProbeSetupPanel(AbstractDataSetUniqueId dataSetId, boolean allowMultipleProbes, boolean allowFixedProbes) {
		super(new GridBagLayout());
		
		mDataSetId = dataSetId;
		
		mIsForConfig = mDataSetId == null;
		
		mAllowMultipleProbes = allowMultipleProbes;
		mAllowFixedProbes = allowFixedProbes;
		
//		boolean allowSynchPairing = false;
		
//		try {
//			allowSynchPairing = dataSetId == null || DataSet.isDataSet.isMultiRunMean(BackEndAPI.getBackEndAPI().getDataSetType(dataSetId));
//		} catch (BackEndAPIException e) {
//			allowSynchPairing = false;
//		}
//		
		mAllowSynchPairing = true;//allowSynchPairing;
		
		mGUIParent = null;
		
		buildGUI();
		
		initialiseProbeSettings();
	}
	
	private void initialiseProbeSettings() {
		int numberOfProbes = mAllowMultipleProbes ? mDefaultCoordsList.size() : 1;
		mNumberOfProbes.setSelectedItemViaIndex(numberOfProbes);
		updateNumberOfProbes();
		
		for (int i = 0; i < numberOfProbes; ++i) {
			DataPointCoordinates coords = mDefaultCoordsList.get(i);
			mProbeDetailsList.elementAt(i).mYFixedProbeCoordOrOffset.setValue(coords.getY());
			mProbeDetailsList.elementAt(i).mZFixedProbeCoordOrOffset.setValue(coords.getZ());
		}
		
		updateProbeDetailsText();
	}
	
	/**
	 * Gets the preferred size
	 * @return The preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension superSize = super.getPreferredSize();
		
		int width = (int) (superSize.width * 1.1);
		int height = Math.min(superSize.height, (int) (screenSize.height * 0.7)/4);

		return new Dimension(width, height);
	}

	@Override
	public void propertyChange(PropertyChangeEvent theEvent) {
        if (mNumberOfProbes.isSource(theEvent)) {
        	updateNumberOfProbes();
        } else {
        	if (mGUIParent != null) {
        		mGUIParent.childChanged();
        	}
        }
    }

	/**
	 * Allows child classes to add a custom panel above the buttons
	 * @return The custom panel or null for no custom panel
	 */
	protected void buildGUI() {
		StringBuffer options = new StringBuffer();
		for (int i = 1; i < 65; ++i) {
			options.append(i);
			options.append(':');
			options.append(i);
			options.append(';');
		}

		try {
			DataSetConfig config = mIsForConfig ? DAFrame.getFrame().getDefaultDataSetConfiguration() : DAFrame.getBackEndAPI().getConfigData(mDataSetId);
			mLeftBankPosition = config.get(BackEndAPI.DSC_KEY_LEFT_BANK_POSITION);
			mRightBankPosition = config.get(BackEndAPI.DSC_KEY_RIGHT_BANK_POSITION);
			mWaterDepth = config.get(BackEndAPI.DSC_KEY_WATER_DEPTH);
			mDefaultCoordsList = config.getCoords();
			mMainProbeIndex = config.get(BackEndAPI.DSC_KEY_MAIN_PROBE_INDEX).intValue();
			mFixedProbeIndex = config.get(BackEndAPI.DSC_KEY_FIXED_PROBE_INDEX).intValue();
			mSynchProbeIndex = config.get(BackEndAPI.DSC_KEY_SYNCH_PROBE_INDEX).intValue();
			
			// Check for old versions which do not have the main probe index stored in the xml.
			if (mFixedProbeIndex == 0 && mMainProbeIndex == mFixedProbeIndex) {
				mMainProbeIndex = 1;
			}
		} catch (Exception theException) {
			theException.printStackTrace();
			mLeftBankPosition = 0;
			mRightBankPosition = 1000;
			mWaterDepth = 200;
			mMainProbeIndex = 0;
			mFixedProbeIndex = -1;
		}

		// Check we have a coords list in case of old config files
		if (mDefaultCoordsList == null) {
			mDefaultCoordsList = new LinkedList<BackEndAPI.DataPointCoordinates>();
			mDefaultCoordsList.add(new DataPointCoordinates(0, 0));
		}

		mNumberOfProbes = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.NUMBER_OF_PROBES), options.toString(), mAllowMultipleProbes ? mDefaultCoordsList.size() : 1);
		mNumberOfProbes.setEnabled(mAllowMultipleProbes);
		mNumberOfProbes.addActionListener(this);
		
		mMainProbeId = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.MAIN_PROBE), getProbeIdOptions(false), -1);
		mMainProbeId.setEnabled(mAllowMultipleProbes);
		mMainProbeId.addActionListener(this);

		mFixedProbeId = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.FIXED_PROBE), getProbeIdOptions(true), -1);
		mFixedProbeId.setEnabled(mAllowFixedProbes);
		mFixedProbeId.addActionListener(this);

		mSynchProbeId = new MAJFCTranslatingDropDownPanel<Integer>(DAStrings.getString(DAStrings.SYNCH_INDEX), getProbeIdOptions(true), -1);
		mSynchProbeId.setEnabled(mAllowSynchPairing);
		mSynchProbeId.addActionListener(this);

		int x = 0, y = 0;
		MAJFCPanel idsPanel = new MAJFCPanel(new GridBagLayout());
		idsPanel.add(mNumberOfProbes, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 0, 10, 0, 0, 0, 0));
		idsPanel.add(mMainProbeId, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 0, 10, 0, 0, 0, 0));
		idsPanel.add(mFixedProbeId, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 0, 10, 0, 0, 0, 0));
		idsPanel.add(mSynchProbeId, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 1.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 0, 10, 0, 0, 0, 0));

		mProbeDetailsPanel = new MAJFCPanel(new GridBagLayout());
		mScrollPane = new JScrollPane(mProbeDetailsPanel);
		
		x = 0;
		y = 0;
		add(mScrollPane, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 5, 0, 0, 0, 0, 0));
		add(idsPanel, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 5, 0, 0, 0, 0, 0));

		updateNumberOfProbes();
	}

	/**
	 * Updates the custom panel, adding the required number of probe detail panels.
	 */
	private void updateNumberOfProbes() {
		mProbeDetailsPanel.removeAll();
		
		int numberOfProbes = mNumberOfProbes.getSelectedItem();
		
		if (numberOfProbes == 1) {
			mFixedProbeIndex = -1;
		} else if (mFixedProbeIndex >= numberOfProbes) {
			mFixedProbeIndex = -1;
		} else if (mSynchProbeIndex >= numberOfProbes) {
			mSynchProbeIndex = -1;
		}
		
		mMainProbeId.fillDropDownWithOptions(getProbeIdOptions(false), mMainProbeIndex);
		mFixedProbeId.fillDropDownWithOptions(getProbeIdOptions(true), mFixedProbeIndex);
		mSynchProbeId.fillDropDownWithOptions(getProbeIdOptions(true), mSynchProbeIndex);
		
		mProbeDetailsList = new Vector<ProbeDetails>(numberOfProbes);
		
		for (int i = 0; i < numberOfProbes; ++i) {
			ProbeDetails probeDetails = new ProbeDetails(i, this);
			mProbeDetailsList.add(probeDetails);
		}
		
		updateProbeCoords();
		
		validate();
		
		setGUIStates();
	}

	private void updateProbeCoords() {
		int numberOfProbes = mProbeDetailsList.size();
		int numberOfDefaultCoords = mDefaultCoordsList == null ? 0 : mDefaultCoordsList.size();
		
		for (int i = 0 ; i < numberOfProbes; ++i) {
			ProbeDetails probeDetails = mProbeDetailsList.elementAt(i);
			
			if (i < numberOfDefaultCoords) {
				probeDetails.mYFixedProbeCoordOrOffset.setValue(mDefaultCoordsList.get(i).getY());
				probeDetails.mZFixedProbeCoordOrOffset.setValue(mDefaultCoordsList.get(i).getZ());
			} else {
				probeDetails.mYFixedProbeCoordOrOffset.setValue(0);
				probeDetails.mZFixedProbeCoordOrOffset.setValue(0);
			}
		}
	}
	
	private void updateProbeDetailsText() {
		int numberOfProbes = mProbeDetailsList.size();
		
		for (int i = 0 ; i < numberOfProbes; ++i) {
			mProbeDetailsList.elementAt(i).updateText();
		}
	}
	
	@Override
	/**
	 * Sets enabling of the GUI components
	 */
	protected void setGUIStates() {
		super.setGUIStates();
		
		if (mProbeDetailsList.size() == 1) {
			mMainProbeId.setSelectedItem(0);
			mFixedProbeId.setSelectedItem(-1);
			mSynchProbeId.setSelectedItem(-1);
		}
		
		validate();
	}
	
	@Override
	public void actionPerformed(ActionEvent theEvent) {
		if (mNumberOfProbes.isSource(theEvent)) {
			updateNumberOfProbes();
		} else if (mMainProbeId.isSource(theEvent)) {
			if (mMainProbeId.getSelectedItem() != null) {
				mMainProbeIndex = mMainProbeId.getSelectedItem();
			}
		} else if (mFixedProbeId.isSource(theEvent)) {
			if (mFixedProbeId.getSelectedItem() != null) {
				mFixedProbeIndex = mFixedProbeId.getSelectedItem();
			}
			
			// Don't allow the fixed probe to be the main probe - set it to "None" if this is done.
			if (mFixedProbeIndex == mMainProbeIndex) {
				mFixedProbeId.setSelectedIndex(0);
			}
		} else if (mSynchProbeId.isSource(theEvent)) {
			if (mSynchProbeId.getSelectedItem() != null) {
				mSynchProbeIndex = mSynchProbeId.getSelectedItem();
			}
		}
		
		if (mProbeDetailsList != null) {
	 		updateProbeDetailsText();
			setGUIStates();
		}
	}

	public void fixNumberOfProbes(int numberOfProbes) {
		mNumberOfProbes.setSelectedItem(numberOfProbes);
		mNumberOfProbes.setEnabled(false);
	}
	
	/**
	 * Gets the coordinate list for the data points in this file
	 * @param mainProbeYCoord The y-coordinate of the "main" data point
	 * @param mainProbeZCoord The z-coordinate of the "main" data point
	 * @return
	 */
	public LinkedList<DataPointCoordinates> getProbeCoords(int mainProbeYCoord, int mainProbeZCoord) {
		LinkedList<DataPointCoordinates> probeCoords = new LinkedList<DataPointCoordinates>();
		int numberOfProbes = mNumberOfProbes.getSelectedItem();// mProbeDetailsList.size();
		
		for (int i = 0; i < numberOfProbes; ++i) {
			ProbeDetails probeDetails = mProbeDetailsList.elementAt(i);
			int yCoord = mainProbeYCoord, zCoord = mainProbeZCoord;
			
			if (probeDetails.isMainProbe()) {
			} else if (probeDetails.isFixed()) {
				yCoord = (int) probeDetails.getYCoordOrOffset();
				zCoord = (int) probeDetails.getZCoordOrOffset();
			} else {
				yCoord += (int) probeDetails.getYCoordOrOffset();
				zCoord += (int) probeDetails.getZCoordOrOffset();
			}
			
			probeCoords.add(new DataPointCoordinates(yCoord, zCoord));
		}

		return probeCoords;
	}
	
	private String getProbeIdOptions(boolean includeNone) {
		StringBuffer sb= new StringBuffer();
		
		if (includeNone) {
			sb.append(DAStrings.getString(DAStrings.PROBE_NONE));
			sb.append(':');
			sb.append("-1");
		}
		
		int numberOfProbes = mNumberOfProbes.getSelectedItem();
		
		for (int i = 0; i < numberOfProbes; ++i) {
			sb.append(';');
			sb.append(i + 1);
			sb.append(':');
			sb.append(i);
		}
		
		return sb.toString();
	}

	/**
	 * Gets the index of the main probe
	 * @return The main probe index
	 */
	public int getMainProbeIndex() {
		return mMainProbeIndex;
	}	
	
	/**
	 * Gets the index specified for the fixed probe (user input value - 1 => first probe is zero)
	 * @return The fixed probe index (defaults to -1 if there is no fixed probe)
	 */
	public int getFixedProbeIndex() {
		return mFixedProbeIndex;
	}
	
	/**
	 * Gets the index specified for the synch probe (user input value - 1 => first probe is zero)
	 * @return The synch probe index (defaults to -1 if there is no synch probe)
	 */
	public int getSynchProbeIndex() {
		return mSynchProbeIndex;
	}

	/**
	 * Inner helper class
	 * @author Mike
	 *
	 */
	private class ProbeDetails extends MAJFCPanel {
		private final ProbeSetupPanel mParent;
		private final int mIndex;
		private JLabel mProbeId;
		private MAJFCNumberTextAreaPanel mYFixedProbeCoordOrOffset;
		private MAJFCNumberTextAreaPanel mZFixedProbeCoordOrOffset;
		
		public ProbeDetails(int index, ProbeSetupPanel parent) {
			super();
			
			GridLayout gridLayout = new GridLayout();
			gridLayout.setHgap(10);
			setLayout(gridLayout);
			
			mParent = parent;
			mIndex = index;
		
			mProbeId = new JLabel(String.valueOf(index + 1));
			
			mYFixedProbeCoordOrOffset = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.Y_OFFSET), 0, 1000, 0, 0, mParent);
			mZFixedProbeCoordOrOffset = new MAJFCNumberTextAreaPanel(DAStrings.getString(DAStrings.Z_OFFSET), 0, 1000, 0, 0, mParent);
			
			int x = 0, y = mIndex;
			mProbeDetailsPanel.add(mProbeId, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 0, 0, 0, 0, 0, 0));
			mProbeDetailsPanel.add(mYFixedProbeCoordOrOffset, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 0, 0, 3, 0, 0, 0));
			mProbeDetailsPanel.add(mZFixedProbeCoordOrOffset, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 0, 10, 3, 0, 0, 0));
			
			validate();
		}

		/**
		 * Set this as the fixed probe
		 */
		private void updateText() {
			if (mFixedProbeIndex == mIndex) {
				mYFixedProbeCoordOrOffset.setText(DAStrings.getString(DAStrings.Y_COORD_LABEL));
				mYFixedProbeCoordOrOffset.setMinimumValue(mParent.mLeftBankPosition);
				mYFixedProbeCoordOrOffset.setMaximumValue(mParent.mRightBankPosition);

				mZFixedProbeCoordOrOffset.setText(DAStrings.getString(DAStrings.Z_COORD_LABEL));
				mZFixedProbeCoordOrOffset.setMinimumValue(0d);
				mZFixedProbeCoordOrOffset.setMaximumValue(mParent.mWaterDepth);
			} else {
				mYFixedProbeCoordOrOffset.setText(DAStrings.getString(DAStrings.Y_OFFSET));
				mYFixedProbeCoordOrOffset.setMinimumValue(-(mParent.mRightBankPosition - mParent.mLeftBankPosition));
				mYFixedProbeCoordOrOffset.setMaximumValue(mParent.mRightBankPosition - mParent.mLeftBankPosition);
				
				mZFixedProbeCoordOrOffset.setText(DAStrings.getString(DAStrings.Z_OFFSET));
				mZFixedProbeCoordOrOffset.setMinimumValue(-mParent.mWaterDepth);
				mZFixedProbeCoordOrOffset.setMaximumValue(mParent.mWaterDepth);
			}
			
			mYFixedProbeCoordOrOffset.setVisible(mMainProbeIndex != mIndex);
			mZFixedProbeCoordOrOffset.setVisible(mYFixedProbeCoordOrOffset.isVisible());

			mParent.updateProbeCoords();
		}

		/**
		 * Gets the y-coordinate (for a fixed probe) or y-offset (for an offset probe)
		 * @return
		 */
		private double getYCoordOrOffset() {
			return mYFixedProbeCoordOrOffset.getValue();
		}
		
		/**
		 * Gets the z-coordinate (for a fixed probe) or z-offset (for an offset probe)
		 * @return
		 */
		private double getZCoordOrOffset() {
			return mZFixedProbeCoordOrOffset.getValue();
		}

		/**
		 * Is this the fixed probe?
		 * @return true if it is
		 */
		private boolean isFixed() {
			return mFixedProbeIndex == mIndex;
		}

		/**
		 * Is this the fixed probe?
		 * @return true if it is
		 */
		private boolean isMainProbe() {
			return mMainProbeIndex == mIndex;
		}
	}

	public void setLeftBankPos(Double value) {
		mLeftBankPosition = value;
		updateProbeDetailsText();
	}

	public void setRightBankPos(Double value) {
		mRightBankPosition = value;
		updateProbeDetailsText();
	}

	public void setWaterDepth(Double value) {
		mWaterDepth = value;
		updateProbeDetailsText();
	}

}
