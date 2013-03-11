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
package de.tud.textureAttack.model.algorithms.selection.colorselection;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.PriorityQueue;

import de.tud.textureAttack.model.algorithms.Pair;

public class ColorRegions {

	private Color color;
	private PriorityQueue<Pair<Integer, ArrayList<Point>>> regions;

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public PriorityQueue<Pair<Integer, ArrayList<Point>>> getRegions() {
		return regions;
	}

	public void setRegions(
			PriorityQueue<Pair<Integer, ArrayList<Point>>> regions) {
		this.regions = regions;
	}

	public ColorRegions(Color c) {
		this.color = c;
		regions = new PriorityQueue<Pair<Integer, ArrayList<Point>>>(11,
				new RegionComparator());
	}

	public ColorRegions(Color color,
			PriorityQueue<Pair<Integer, ArrayList<Point>>> regions) {
		this.color = color;
		this.regions = regions;
	}

	public boolean addRegion(Pair<Integer, ArrayList<Point>> region) {
		return regions.offer(region);
	}

	public Pair<Integer, ArrayList<Point>> getTop() {
		return regions.peek();
	}

	public int getTopPixelCount() {
		return getTop().getKey();
	}

	public boolean equals(Object obj) {
		if (obj instanceof ColorRegions) {
			ColorRegions cr = (ColorRegions) obj;
			return color.equals(cr.getColor());
		}
		return false;
	}

	public int hashCode() {
		return color.getRGB();
	}

	public String toString() {
		StringBuffer regionsString = new StringBuffer();
		for (Pair<Integer, ArrayList<Point>> entry = regions.element(); regions
				.iterator().hasNext();) {
			regionsString.append(entry.getKey()).append(" ")
					.append(entry.getValue().toString());
			regionsString.append("\n");
		}

		return color + " - " + regionsString.toString();
	}

}
