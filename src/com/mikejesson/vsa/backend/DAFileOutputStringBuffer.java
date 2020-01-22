/**
 * 
 */
package com.mikejesson.vsa.backend;

import com.mikejesson.majfc.helpers.MAJFCTools;

/**
 * @author Mike
 *
 */
public class DAFileOutputStringBuffer {
	private final StringBuffer mStringBuffer = new StringBuffer();
	
	public DAFileOutputStringBuffer() {
	}

	public void clear() {
		mStringBuffer.setLength(0);
	}
	
	public void append(String value) {
		mStringBuffer.append(value);
	}

	public void append(char value) {
		mStringBuffer.append(value);	
	}
	
	public void append(Double value) {
		mStringBuffer.append(String.valueOf(value));	
	}

	@Override
	public String toString() {
		return mStringBuffer.toString();
	}

	/**
	 * Makes an XML internal attribute from a tag and value, converting the number to "English" with a '.'
	 * decimal separator
	 * @param tag The attribute tag
	 * @param value The attribute value
	 * @return The XML internal attribute string
	 */
	public static String makeXMLInternalAttribute(String tag, Number value) {
		return MAJFCTools.makeXMLInternalAttribute(tag, String.valueOf(value));
	}

	/**
	 * Makes an XML node from the node name and value, converting the number to "English" with a '.'
	 * @param name The node name
	 * @param value The value
	 * @return The XML node string
	 */
	public static String makeXMLNode(String name, Number value) {
		return MAJFCTools.makeXMLNode(name, String.valueOf(value));
	}
}
