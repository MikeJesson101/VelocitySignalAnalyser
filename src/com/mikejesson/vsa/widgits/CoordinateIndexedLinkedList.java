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


import java.util.LinkedList;

import com.mikejesson.majfc.helpers.MAJFCLogger;

/**
 * @author Mike
 *
 */
@SuppressWarnings("serial")
public class CoordinateIndexedLinkedList<ObjectType extends CoordinateIndexedObject> extends LinkedList<ObjectType > {
	/**
	 * Adds a CoordinateIndexedObject to the specified list of CoordinateIndexedObjects
	 * Maintains the list in order (by y-coordinate then z-coordinate)
	 * @param theObject The object to add
	 */
	public void addCoordinateIndexedObjectToList(ObjectType theObject) {
		int yCoord = theObject.getYCoord();
		int zCoord = theObject.getZCoord();
		int numberOfDataPoints = size();
		
		// Check if this is the first data point
		if (numberOfDataPoints == 0) {
			add(theObject);
			return;
		}
		
		// Check if this is to be the last object in the list
		CoordinateIndexedObject lastObjectInList = getLast();
		
		if ((lastObjectInList.getYCoord() < yCoord)
				|| ((lastObjectInList.getYCoord() == yCoord) && (lastObjectInList.getZCoord() < zCoord))) {
			add(theObject);
			return;
		}
		
		for (int i = 0; i < numberOfDataPoints; ++i) {
			CoordinateIndexedObject objectFromList = get(i);
			int vectorDataPointYCoord = objectFromList.getYCoord();
			int vectorDataPointZCoord = objectFromList.getZCoord();
			
			if (vectorDataPointYCoord < yCoord) {
				continue;
			}
			
			if (vectorDataPointYCoord == yCoord) {
				if (vectorDataPointZCoord < zCoord) {
					continue;
				}
				
				if (vectorDataPointZCoord == zCoord) {
					// Should never get here
					MAJFCLogger.log("Adding duplicate data point " + yCoord + ' ' + zCoord);
				}
				
				if (vectorDataPointZCoord > zCoord) {
					add(i, theObject);
					return;
				}
			}
			
			if (vectorDataPointYCoord > yCoord) {
				add(i, theObject);
				return;
			}
		}
	}
}
