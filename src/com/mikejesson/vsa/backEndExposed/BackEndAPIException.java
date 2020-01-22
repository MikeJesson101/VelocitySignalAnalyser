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

package com.mikejesson.vsa.backEndExposed;

/**
 * @author MAJ727
 *
 */
@SuppressWarnings("serial")
public class BackEndAPIException extends Exception {
	String mTitle;
	
	/**
	 * Only available constructor
	 * 
	 * @param title
	 * @param message
	 */
	public BackEndAPIException(String title, String message){
		super(message);
		mTitle = title;
	}

	/**
	 * Only available constructor
	 * 
	 * @param title
	 * @param message
	 */
	public BackEndAPIException(String title, String message, Exception theException){
		this(title, message+ "\n\"" + theException.getMessage() + "\"");
		
		setStackTrace(theException.getStackTrace());
	}

	/**
	 * Gets the title to show in a dialog box for this exception
	 * 
	 * @return the title
	 */
	public String getTitle(){
		return mTitle;
	}
}
