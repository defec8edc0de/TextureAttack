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

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;

import de.tud.textureAttack.model.algorithms.Pair;

public class RegionComparator implements
		Comparator<Pair<Integer, ArrayList<Point>>> {

	@Override
	public int compare(Pair<Integer, ArrayList<Point>> x,
			Pair<Integer, ArrayList<Point>> y) {
		if (x.getKey() < y.getKey()) {
			return 1;
		}
		if (x.getKey() > y.getKey()) {
			return -1;
		}
		return 0;
	}

}
