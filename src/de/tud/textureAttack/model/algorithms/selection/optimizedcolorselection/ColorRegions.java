/*******************************************************************************
 * Copyright (c) 2013 Sebastian Funke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Sebastian Funke - initial API and implementation
 ******************************************************************************/
package de.tud.textureAttack.model.algorithms.selection.optimizedcolorselection;

import java.awt.Color;
import java.util.PriorityQueue;

public class ColorRegions {

	private int rgba;

	private PriorityQueue<Region> regions;

	public int getRGB() {
		return rgba;
	}

	public void setColor(Color color) {
		this.rgba = color.getRGB();
	}

	public int getColor() {
		return rgba;
	}

	public void setColor(int color) {
		this.rgba = color;
	}

	public PriorityQueue<Region> getRegions() {
		return regions;
	}

	public void setRegions(PriorityQueue<Region> regions) {
		this.regions = regions;
	}

	public ColorRegions(int rgb) {
		this.rgba = rgb;
		regions = new PriorityQueue<Region>(11, new RegionComparator());
	}

	public ColorRegions(int color, PriorityQueue<Region> regions) {
		this.rgba = color;
		this.regions = regions;
	}

	public boolean addRegion(Region region) {
		return regions.offer(region);
	}

	public Region getTop() {
		return regions.peek();
	}

	public int getTopPixelCount() {
		return getTop().getPixelCount();
	}

	public boolean equals(Object obj) {
		if (obj instanceof ColorRegions) {
			ColorRegions cr = (ColorRegions) obj;
			return rgba == cr.getColor();
		}
		return false;
	}

	public int hashCode() {
		return rgba;
	}

	public String toString() {
		StringBuffer regionsString = new StringBuffer();
		for (Region entry = regions.element(); regions.iterator().hasNext();) {
			regionsString.append(entry.getPixelCount()).append(" ")
					.append(entry.getRegion().toString());
			regionsString.append("\n");
		}

		return rgba + " - " + regionsString.toString();
	}

}
