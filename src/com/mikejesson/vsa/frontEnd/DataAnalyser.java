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

import java.awt.Image;
import java.text.DecimalFormat;
import java.util.Locale;


import javax.swing.ImageIcon;
import javax.swing.UIManager;

import com.mikejesson.majfc.guiComponents.MAJFCStackedPanelWithFrame;
import com.mikejesson.majfc.helpers.MAJFCLogger;
import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.majfc.helpers.MAJFCMatlabConnection;
import com.mikejesson.vsa.widgits.DAStrings;


// TODO 2 volume averaged u'v'
public class DataAnalyser {
	private static DataAnalyser sThis;
	private static DAFrame sTheDAFrame;
	public static boolean sIsApplet = false;
	public static boolean sDevHack = false;
	public static boolean sShowAllMRRuns = false;
	private static boolean sLicenceAccepted = false;
	private static boolean sConnectToMatlab = true;
	private static MAJFCMatlabConnection sMatlabConnection;
	private static String sDefaultCSVFileDecimalSeparator;
	
	/**
	 * -l XX - sets logger severity to XX (int), e.g. java -jar "Data Analyser 1.1.1.jar" "-l" 20
	 * -f - sets the default locale to ENGLISH, e.g. java -jar "Data Analyser 1.1.1.jar" "-f"
	 * -t - sets the logger to log timing messages, e.g. java -jar "Data Analyser 1.1.1.jar" "-t"
	 * -d - development mode (activates/deactivates code using the sDevHack flag)
	 * @param args
	 */
	public static void main(String[] args) {
	    int loggerSeverity = 100;
		
		for (int i = 0; i < args.length; ++i) {
			String cla = args[i];
			
			if (cla.length() != 2 || cla.charAt(0) != '-') {
				MAJFCLogger.log("Unrecognised argument " + cla);
			}
			
			char claChar = args[i].charAt(1);
			
			switch (claChar) {
				case 'd':
					sDevHack = true;
					break;
				case 'l':
					try {
						loggerSeverity = MAJFCTools.parseInt(args[++i]);
					} catch (Exception e) {
						loggerSeverity = 100;
						MAJFCLogger.log("Incompatible arguments -" + claChar + ' ' + args[i]);
						return;
					}
					break;
				case 'f':
					Locale.setDefault(Locale.ENGLISH);
					break;
				case 't':
					MAJFCLogger.setLogTiming(true);
					break;
				case 'a':
					sShowAllMRRuns = true;
					break;
				case 'm':
					sConnectToMatlab = false;
					break;
				default:
					MAJFCLogger.log("Unrecognised arguments -" + claChar);
			}
		}

		MAJFCLogger.setSeverityLimit(loggerSeverity);
		
		Locale currentLocale = Locale.getDefault();
		 
		MAJFCLogger.log("currentLocale.getDisplayLanguage():\t" + currentLocale.getDisplayLanguage());
		MAJFCLogger.log("currentLocale.getDisplayCountry():\t" + currentLocale.getDisplayCountry());
		MAJFCLogger.log("currentLocale.getLanguage():\t\t" + currentLocale.getLanguage());
		MAJFCLogger.log("currentLocale.getCountry():\t\t" + currentLocale.getCountry());
		MAJFCLogger.log("System.getProperty(\"user.country\"):\t" + System.getProperty("user.country"));
		MAJFCLogger.log("System.getProperty(\"user.language\"):\t" + System.getProperty("user.language"));
		
		DecimalFormat numFormat = (DecimalFormat) DecimalFormat.getInstance();
		sDefaultCSVFileDecimalSeparator = new Character(numFormat.getDecimalFormatSymbols().getDecimalSeparator()).toString();
		MAJFCLogger.log("Default decimal separator:\t\t" + sDefaultCSVFileDecimalSeparator);
		
		
		sThis = new DataAnalyser();
		sThis.initialise();

		MAJFCStackedPanelWithFrame.setDefaultIconImage(DataAnalyser.getImage("frame_icon_16.png"));
	}
	
	private void initialise() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception theException) {
		}
		
		sTheDAFrame = DAFrame.getFrame();
		sMatlabConnection = sConnectToMatlab == false ? null : new MAJFCMatlabConnection(sTheDAFrame, DAStrings.getString(DAStrings.MATLAB_CONNECTION_TEST_DIALOG_TITLE), DAStrings.getString(DAStrings.MATLAB_PROGRESS_DIALOG_TITLE));
	}

	/**
	 * Gets the ImageIcon for the given name
	 * @param imageName
	 * @return the corresponding ImageIcon object
	 */
	public static ImageIcon getImageIcon(String imageName) {
		java.net.URL imageURL =  sThis.getClass().getResource("images/" + imageName);
		
		if (imageURL != null) {
			return new ImageIcon(imageURL);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the Image for the given name
	 * @param imageName
	 * @return the corresponding Image object
	 */
	public static Image getImage(String imageName) {
		java.net.URL imageURL =  sThis.getClass().getResource("images/" + imageName);
		
		if (imageURL != null) {
			return new ImageIcon(imageURL).getImage();
		} else {
			MAJFCLogger.log("Image " + imageName + " not found.");
			return null;
		}
	}
	
	public static boolean hasLicenceBeenAccepted() {
		return sLicenceAccepted;
	}
	
	public static void licenceAccepted() {
		sLicenceAccepted = true;
	}
	
	public static void licenceDeclined() {
		killVSA();
	}
	
	public static void killVSA() {
		if (sMatlabConnection != null) {
			sMatlabConnection.disconnect();
		}
		
		System.exit(0);
	}
	
	public static boolean matlabConnectionAvailable() {
		return sConnectToMatlab && sMatlabConnection.connectionAvailable();
	}
	
	public static MAJFCMatlabConnection matlabConnection() {
		return sMatlabConnection;
	}
	
	public static String getDefaultCSVFileDecimalSeparator() {
		return sDefaultCSVFileDecimalSeparator;
	}
}
