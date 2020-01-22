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

package com.mikejesson.vsa.widgits.basicAndAbstractGraphs;

import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

/**
 * @author Mike
 *
 */
public abstract class XYDataSetAdapter implements XYDataset {

	/**
	 * @see org.jfree.data.xy.XYDataset#getItemCount(int)
	 */
	@Override
	public abstract int getItemCount(int series);

	/**
	 * @see org.jfree.data.xy.XYDataset#getXValue(int, int)
	 */
	@Override
	public abstract double getXValue(int series, int item);

	/**
	 * @see org.jfree.data.xy.XYDataset#getYValue(int, int)
	 */
	@Override
	public abstract double getYValue(int series, int item);

	/**
	 * @see org.jfree.data.general.SeriesDataset#getSeriesCount()
	 */
	@Override
	public abstract int getSeriesCount();
	
	/**
	 * @see org.jfree.data.xy.XYDataset#getDomainOrder()
	 */
	@Override
	public DomainOrder getDomainOrder() {
		return null;
	}
	
	/**
	 * @see org.jfree.data.xy.XYDataset#getX(int, int)
	 */
	@Override
	public Number getX(int series, int item) {
		return getXValue(series, item);
	}

	/**
	 * @see org.jfree.data.xy.XYDataset#getY(int, int)
	 */
	@Override
	public Number getY(int series, int item) {
		return getYValue(series, item);
	}
	/**
	 * @see org.jfree.data.general.SeriesDataset#getSeriesKey(int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Comparable getSeriesKey(int series) {
		return "";
	}

	/**
	 * @see org.jfree.data.general.SeriesDataset#indexOf(java.lang.Comparable)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int indexOf(Comparable comparable) {
		return 0;
	}

	/**
	 * @see org.jfree.data.general.Dataset#addChangeListener(org.jfree.data.general.DatasetChangeListener)
	 */
	@Override
	public void addChangeListener(DatasetChangeListener listener) {
	}

	/**
	 * @see org.jfree.data.general.Dataset#getGroup()
	 */
	@Override
	public DatasetGroup getGroup() {
		return null;
	}

	/**
	 * @see org.jfree.data.general.Dataset#removeChangeListener(org.jfree.data.general.DatasetChangeListener)
	 */
	@Override
	public void removeChangeListener(DatasetChangeListener listener) {
	}

	/**
	 * @see org.jfree.data.general.Dataset#setGroup(org.jfree.data.general.DatasetGroup)
	 */
	@Override
	public void setGroup(DatasetGroup dataSetGroup) {
	}

}
