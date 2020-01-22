package com.mikejesson.vsa.frontEnd;

import java.awt.Cursor;
import java.awt.Frame;

import javax.swing.JOptionPane;

import com.mikejesson.majfc.guiComponents.MAJFCProgressDialog;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.AbstractDataSetUniqueId;
import com.mikejesson.vsa.backEndExposed.BackEndAPI.DAProgressInterface;


/**
 * Helper class
 */
public abstract class DAProgressDialog extends MAJFCProgressDialog implements DAProgressInterface {
	private final AbstractDataSetUniqueId mDataSetId;
	private final Frame mParent;
	
	/**
	 * Constructor
	 * @param parent The parent frame of the dialog
	 * @param title The title for the dialog
	 */
	public DAProgressDialog(AbstractDataSetUniqueId dataSetId, Frame parent, String title) {
		super(parent, title);
		mDataSetId = dataSetId;
		mParent = parent;
	}

	/**
	 * Gets the data set Id
	 * @return The data set Id
	 */
	protected AbstractDataSetUniqueId getDataSetId() {
		return mDataSetId;
	}
	
	/**
	 * DAProgressInterface implementation
	 */
	public void setProgress(int progress, String message) {
		setProgress(progress);
	}
	
	@Override
	public void whenDone() {
		try {
			get();
		} catch (Exception theException) {
			theException.printStackTrace();
			
			if (theException.getClass() == BackEndAPIException.class) {
				JOptionPane.showMessageDialog(DAFrame.getFrame(), ((BackEndAPIException) theException).getMessage(), ((BackEndAPIException) theException).getTitle(), JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(DAFrame.getFrame(), theException.getMessage(), theException.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
		} finally {
			mParent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
};
