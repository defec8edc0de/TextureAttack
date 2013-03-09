package de.tud.textureAttack.model.algorithms.selection.colorselection;

import java.util.Comparator;


public class ColorRegionComparator implements
		Comparator<ColorRegions> {

	@Override
	public int compare(ColorRegions x,
			ColorRegions y) {
        if (x.getTopPixelCount() < y.getTopPixelCount())
        {
            return 1;
        }
        if (x.getTopPixelCount() > y.getTopPixelCount())
        {
            return -1;
        }
        return 0;
	}

}
