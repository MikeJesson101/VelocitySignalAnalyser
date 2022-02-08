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

package com.mikejesson.vsa.frontEnd;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mikejesson.majfc.guiComponents.MAJFCAbstractFileOrDirChooserButton;
import com.mikejesson.majfc.guiComponents.MAJFCDialogButton;
import com.mikejesson.majfc.guiComponents.MAJFCPanel;
import com.mikejesson.majfc.guiComponents.MAJFCTranslatingDropDownPanel;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.widgits.DADefinitions;
import com.mikejesson.vsa.widgits.DAStrings;
import com.mikejesson.vsa.widgits.DATools;


/**
 * @author mikefedora
 *
 */
@SuppressWarnings("serial")
public class AboutDialog extends JDialog implements ActionListener {
	private MAJFCPanel mAboutPanel;
	private JScrollPane mWarrantyScroll;
	private JTextArea mWarrantyPanel;
	private JScrollPane mReleaseNotesScroll;
	private MAJFCDialogButton mAcceptButton;
	private MAJFCDialogButton mDeclineButton;
	private MAJFCTranslatingDropDownPanel<String> mConfigFileSelector;
	private MAJFCAbstractFileOrDirChooserButton mCopyConfigFileButton;
	private String mLastConfigFileName;
	
	/**
	 * The constructor
	 * @param parent the parent frame
	 * @param modal is this dialog modal?
	 */
	public AboutDialog(Frame parent) {
		super(parent, true);
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setTitle(DAStrings.getString(DAStrings.ABOUT_DIALOG_TITLE));
		setIconImage(DataAnalyser.getImage("about.png"));
		
		buildGUI();
		
		validate();
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	/**
	 * Build the GUI components for this dialog
	 */
	private void buildGUI() {
		setLayout(new GridBagLayout());
		
		StringBuffer about = new StringBuffer();
		about.append(DAStrings.getString(DAStrings.VERSION) + ' ' + DADefinitions.VERSION);
		about.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
		about.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
		about.append(DAStrings.getString(DAStrings.GPL_TEXT));
		about.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
		about.append(MAJFCTools.SYSTEM_NEW_LINE_STRING);
		about.append(DAStrings.getString(DAStrings.ADDITIONAL_SOFTWARE));
		
		JTextArea aboutText = new JTextArea(about.toString());
		aboutText.setEditable(false);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		aboutText.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		mWarrantyPanel = new JTextArea(DAStrings.getString(DAStrings.GPL_WARRANTY_TEXT));
		mWarrantyPanel.setEditable(false);
		
		mWarrantyScroll = new JScrollPane(mWarrantyPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		String releaseNotes = getReleaseNotesText();
		JTextPane releaseNotesPane = new JTextPane() {
			@Override
			public Dimension getPreferredSize() {
				Dimension warrantyDimension = mWarrantyPanel.getPreferredSize();
				return new Dimension(warrantyDimension.width, super.getPreferredSize().height);
			}
		};
		releaseNotesPane.setEditable(false);   
		releaseNotesPane.setText(releaseNotes);
		
		mReleaseNotesScroll = new JScrollPane(releaseNotesPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mReleaseNotesScroll.getViewport().setViewPosition(new Point(0,0));
		JTabbedPane tabs = new JTabbedPane();
		tabs.add(DAStrings.getString(DAStrings.WARRANTY), mWarrantyScroll);
		tabs.add(DAStrings.getString(DAStrings.NEWER_VERSIONS), mReleaseNotesScroll);
		int newerVersionsIndex = tabs.indexOfTab(DAStrings.getString(DAStrings.NEWER_VERSIONS));
		tabs.setEnabledAt(newerVersionsIndex, releaseNotes != null);
		if (releaseNotes != null) {
			tabs.setSelectedIndex(newerVersionsIndex);
		}
		
		mAcceptButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.I_ACCEPT), this);
		mAcceptButton.setPreferredSize(new Dimension(75, 25));

		mDeclineButton = new MAJFCDialogButton(DAStrings.getString(DAStrings.I_DECLINE), this);
		mDeclineButton.setPreferredSize(new Dimension(75, 25));
		mDeclineButton.setEnabled(DataAnalyser.hasLicenceBeenAccepted() == false);
		
		mConfigFileSelector = new MAJFCTranslatingDropDownPanel<String>(DAStrings.getString(DAStrings.CHOOSE_CONFIG_FILE), getConfigFileOptions(), DataAnalyser.hasLicenceBeenAccepted() ? DAFrame.getFrame().getConfigFileName() : DADefinitions.CONFIG_FILE_NAME);
		mConfigFileSelector.setEnabled(mDeclineButton.isEnabled());
		mConfigFileSelector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mCopyConfigFileButton.setFileOrDir(DAFrame.getFrame().getConfigFileDirectoryPath() + mConfigFileSelector.getSelectedItem());
			}
		});
		mCopyConfigFileButton = new MAJFCAbstractFileOrDirChooserButton(DAFrame.getFrame().getConfigFileDirectoryPath() + mConfigFileSelector.getSelectedItem()) {
			@Override
			public void callback(File file) {
	           	try {
	           		if (file.getAbsolutePath().endsWith(DADefinitions.CONFIG_FILE_NAME_EXTENSION) == false) {
	           			file = new File(file.getAbsoluteFile() + DADefinitions.CONFIG_FILE_NAME_EXTENSION);
	           		}
	           		
					MAJFCTools.copyFile(new File(DAFrame.getFrame().getConfigFileDirectoryPath() + mConfigFileSelector.getSelectedItem()), file);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	           	mConfigFileSelector.fillDropDownWithOptions(getConfigFileOptions(), file.getName());
			}
			
			@Override
			protected String makeText(String fileName) {
				return DAStrings.getString(DAStrings.COPY_CONFIG_FILE, fileName);
			}
		};
		mCopyConfigFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DATools.chooseFile(DAStrings.getString(DAStrings.SELECT_NEW_CONFIG_FILE_DIALOG_TITLE), DAFrame.getFrame().getConfigFileDirectoryPath(), mCopyConfigFileButton, new FileNameExtensionFilter(DAStrings.getString(DAStrings.CONFIGURATION_FILE_FILTER_TEXT), DADefinitions.CONFIG_FILE_NAME_EXTENSION.substring(1)));
			}
		});
		
		// Set the selected config file as the last one selected
		File configFileDirectory = new File(DAFrame.getFrame().getConfigFileDirectoryPath());
		
		String[] lastConfigFilesList = configFileDirectory.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.endsWith(DADefinitions.CONFIG_FILE_NAME_EXTENSION_FOR_LAST_USED);
			}
		});
		
		if (lastConfigFilesList.length > 0) {
			mLastConfigFileName = new StringTokenizer(lastConfigFilesList[0], ".").nextToken();
			mConfigFileSelector.setSelectedItem(mLastConfigFileName + DADefinitions.CONFIG_FILE_NAME_EXTENSION);
		} else {
			mLastConfigFileName = null;
		}
		//mCopyConfigFileButton.setPreferredSize(new Dimension(75, 25));

		JPanel configFileSelectorPanel = new JPanel(new GridBagLayout());
		
		int x = 0, y = 0;
		configFileSelectorPanel.add(mConfigFileSelector, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		configFileSelectorPanel.add(mCopyConfigFileButton, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		
		x = 0;
		y = 0;
		buttonPanel.add(mAcceptButton, MAJFCTools.createGridBagConstraint(x++, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		buttonPanel.add(mDeclineButton, MAJFCTools.createGridBagConstraint(x, y, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		
		x = 0;
		y = 0;
		mAboutPanel = new MAJFCPanel(new GridBagLayout());
		mAboutPanel.add(aboutText, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 5, 5, 3, 3));
		mAboutPanel.add(buttonPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 0, 5, 3, 3));
		mAboutPanel.add(tabs, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 5, 5, 5, 5, 3, 3));
		mAboutPanel.add(configFileSelectorPanel, MAJFCTools.createGridBagConstraint(x, y++, 1, 1, 0.0, 0.0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, 5, 5, 0, 5, 3, 3));
		
		add(mAboutPanel, MAJFCTools.createGridBagConstraint(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.LINE_START, GridBagConstraints.BOTH, 0, 0, 0, 0, 0, 0));
	}	
	
	/**
	 * Gets the list of options for the config file dropdown.
	 * 
	 * @return The list of options in the correct format for the dropdown constructor.
	 */
	private String getConfigFileOptions() {
		File configFileDirectory = new File(DAFrame.getFrame().getConfigFileDirectoryPath());
		
		String[] configFilesList = configFileDirectory.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String fileName) {
				return fileName.endsWith(DADefinitions.CONFIG_FILE_NAME_EXTENSION);
			}
		});
		
		if (configFilesList.length == 0) {
			return DADefinitions.CONFIG_FILE_NAME + ':' + DADefinitions.CONFIG_FILE_NAME;
		}
		
		StringBuffer options = new StringBuffer();
		
		for (int i = 0; i < configFilesList.length; ++i) {
			options.append(configFilesList[i].substring(0, configFilesList[i].lastIndexOf('.')));
			options.append(':');
			options.append(configFilesList[i]);
			options.append(';');
		}
		
		return options.substring(0, options.length() - 1);
	}

	/**
	 * Reads the release notes text from the server and returns the relevant section.
	 * @return The release notes for releases after the running version.
	 */
	private String getReleaseNotesText() {
		StringBuffer releaseNotes = new StringBuffer();
		Scanner scanner = null;
		
		try {
			URL url = new URL("https://raw.githubusercontent.com/MikeJesson101/VelocitySignalAnalyser/master/releasenotes.txt");
//			URL url = new URL("http://www.mikejesson.com/DataAnalyserWebsite/downloads/releasenotes.txt");
			scanner = new Scanner(url.openStream());
			scanner.useDelimiter("\n");
			
			while (scanner.hasNext()) {
				// Check version number
				String line = scanner.next();
				
				if (line.endsWith(DADefinitions.VERSION) || line.endsWith(DADefinitions.VERSION + MAJFCTools.SYSTEM_CARRIAGE_RETURN_CHAR)) {
					break;
				}
				
				releaseNotes.append(line);
				releaseNotes.append('\n');
			}
		} catch (Exception e) {
			return null;
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		
		return releaseNotes.length() > 0 ? DAStrings.getString(DAStrings.NEWER_VERSIONS_AVAILABLE) + releaseNotes.toString() : null;
	}
	
	/**
	 * Gets the preferred size
	 * @return The preferred size
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension preferredSize = super.getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = preferredSize.width;//(int) (screenSize.width * 0.9);
		int height = Math.min(preferredSize.height, (int) (screenSize.height * 0.9));

		return new Dimension(width, height);
	}

	/**
	 * ActionListener implementation
	 */
	@Override
	public void actionPerformed(ActionEvent theEvent) {
		Object source = theEvent.getSource();
		
		if (source.equals(mAcceptButton)) {
			if (mConfigFileSelector.isEnabled()) {
				String configFileDir = DAFrame.getFrame().getConfigFileDirectoryPath();
				String configFileName = mConfigFileSelector.getSelectedItem();
				String configFileShortName = new StringTokenizer(configFileName, ".").nextToken();
				
				boolean stillUsingLastConfig = mLastConfigFileName != null && mLastConfigFileName.equals(configFileShortName);
				if (stillUsingLastConfig == false) {
					if (mLastConfigFileName != null) {
						File lastConfigFile = new File(configFileDir + mLastConfigFileName + DADefinitions.CONFIG_FILE_NAME_EXTENSION_FOR_LAST_USED);
						lastConfigFile.delete();
					}
				
					String newLastConfigFileName = configFileShortName + DADefinitions.CONFIG_FILE_NAME_EXTENSION_FOR_LAST_USED;
					File newLastConfigFile = new File(configFileDir + newLastConfigFileName);
					try {
						newLastConfigFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				DAFrame.getFrame().onConfigFileSpecified(configFileName);
			}
			
			DataAnalyser.licenceAccepted();
			setVisible(false);
		} else if (source.equals(mDeclineButton)) {
			DataAnalyser.killVSA();
		}
	}
}
