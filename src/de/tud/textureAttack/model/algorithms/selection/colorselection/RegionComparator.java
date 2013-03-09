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
        if (x.getKey() < y.getKey())
        {
            return 1;
        }
        if (x.getKey() > y.getKey())
        {
            return -1;
        }
        return 0;
	}

}
