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

package com.mikejesson.vsa.widgits;


import java.util.Hashtable;

import com.mikejesson.majfc.helpers.MAJFCTools;
import com.mikejesson.vsa.backEndExposed.BackEndAPIException;


import qdxml.DocHandler;

/**
 * Handles the events from the XML parser.
 * Adds basic functionality (holds the name of the current element in mStartElement).
 * If any of the non-abstract methods are overridden then the parent version should
 * probably be called by the overriding method. Up to you.
 * 
 * @author MAJ727
 */
public abstract class MyAbstractDocHandler implements DocHandler {
	protected String mStartElement;
	
	/**
	 * Start of the document reached
	 */
	public void startDocument() {
		mStartElement = null;
	}
	  
	/**
	 * End of the document reached
	 */
	public void endDocument() {
		mStartElement = null;
	}
  
	/**
	 * Start element found
	 * @param elem The element name
	 * @param h Hashtable of child nodes
	 */
	public void startElement(String elem, Hashtable<String, String> h) {
		mStartElement = elem;
	}
  
	/**
	 * End element found
	 * @param elem The element name
	 * @throws BackEndAPIException 
	 */
	public void endElement(String elem) throws BackEndAPIException {
		if (elem.equals(mStartElement)){
			mStartElement = null;
		}
	}
  
	/**
	 * Text found - the contents of the current element
	 * Strips newline and tab characters from the start and end of the element
	 * @param text The text
	 */
  	public void text(String text) {
 		if (text.startsWith(MAJFCTools.SYSTEM_NEW_LINE_STRING)) {
  			text = text.substring(MAJFCTools.SYSTEM_NEW_LINE_STRING.length());
  		}
 		
		int length = text.length();
		while (length > 0 && text.charAt(0) == MAJFCTools.SYSTEM_TAB_CHAR) {
  			text = text.substring(1);
  			--length;
  		}
		
		while (length > 0 && text.charAt(length - 1) == MAJFCTools.SYSTEM_TAB_CHAR) {
  			text = text.substring(0, --length);
  		}
  		
  		if (text.endsWith(MAJFCTools.SYSTEM_NEW_LINE_STRING)) {
  			text = text.substring(0, length - MAJFCTools.SYSTEM_NEW_LINE_STRING.length());
  		}
  		
  		if (text.length() == 0) {
  			return;
  		}
  		
  		elementValue(text);
  	}
  	
  	/**
  	 * Element value found
  	 * @param value The value
  	 */
  	public abstract void elementValue(String value);
}