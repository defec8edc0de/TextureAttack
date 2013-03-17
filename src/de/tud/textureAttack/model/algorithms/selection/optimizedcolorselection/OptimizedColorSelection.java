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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.selection.AbstractSelectionAlgorithm;
import de.tud.textureAttack.model.utils.ImageProcessingToolKit;

/**
 * Stellt im UML-Klassendiagramm "ConcreteProduct" dar, die von der Factory
 * Methode instanziiert wird.
 */
public class OptimizedColorSelection extends AbstractSelectionAlgorithm {

	private int colorThreshold;
	private int minRegionThreshold;
	private BufferedImage image;
	private ActionController actionController;

	private boolean[] visited; // two-dimensional in one

	private ArrayList<ColorRegions> colorRegionsList;
	private int width;
	private int height;
	private int[] pixels;
	private int minRegion;
	private int minRegion2;

	public OptimizedColorSelection(ActionController actionController) {
		super(Options.SelectionToolEnum.OptimizedColorSelection,
				AbstractAlgorithm.AlgoTypes.SELECTIONS, actionController);
		colorThreshold = 0;
		image = null;
		visited = null;
		colorRegionsList = null;
		result = null;
		this.actionController = actionController;
	}

	/**
	 * Initializes the color selection algorithm, especially the relevant
	 * options were set from the given option object
	 */
	@Override
	public void init(BufferedImage img, Options options, String imageName) {
		this.colorThreshold = (int) options
				.getOption(Options.OptionIdentifierEnum.OptimizedColorSelection_ColorThreshold);
		this.minRegionThreshold = (int) options
				.getOption(Options.OptionIdentifierEnum.OptimizedColorSelection_MinRegionThreshold);
		this.image = img;
		initialized = true;
		this.imageName = imageName;
		result = new boolean[img.getHeight()][img.getWidth()];
		// FrequencyComparator frequencyComparator = new FrequencyComparator();
		colorRegionsList = new ArrayList<ColorRegions>();

		width = img.getWidth();
		height = img.getHeight();
		visited = new boolean[width * height];
		
	    BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    bi.getGraphics().drawImage(img, 0, 0, null);
		DataBufferInt data = (DataBufferInt) (bi.getRaster().getDataBuffer());
		pixels = data.getData();
		minRegion = (width / minRegionThreshold);
		minRegion2 = (width / (minRegionThreshold << 1)); // multiply with 2

	}

	/**
	 * Executes the background selection algorithm and returns a boolean[][] in
	 * dimension of the image, where true means this pixel is background
	 */
	@Override
	public boolean[][] executeSelection() {
		if (initialized) {
			initialized = false;
			return getBackground();
		} else {
			System.out.println("OptimizedColorSelection-Algorithm not initialized!");
			return null;
		}
	}

	private boolean[][] getBackground() {
		return findColorAreasOfMostFrequentColor();
	}

	/**
	 * Computes an iterative floodfill from the given pixel and with the given
	 * color (colorThreshold for color similarity) and returns a Region.
	 * Optimized: With more primitive structures and and more intelligent
	 * neighbor scanning
	 * 
	 * @param x
	 * @param y
	 * @param searchColor
	 * @param tolerance
	 * @return Pair<Integer, ArrayList<Point>>
	 */
	private Region findColorRegion(int x, int y, int color) {

		int[] point = new int[] { x, y };
		LinkedList<int[]> points = new LinkedList<int[]>();
		Region area = new Region();
		area.add(point);
		points.addFirst(point);

		while (!points.isEmpty()) {
			point = points.remove();

			x = point[0];
			y = point[1];
			int xr = x;

			int yp = y * width;
			int ypp = yp + width;
			int ypm = yp - width;

			do {
				area.add(new int[] { xr, y });
				visited[xr + yp] = true;
				xr++;
			} while ((xr < width)
					&& !visited[xr + y * width]
					&& ImageProcessingToolKit.areRBGColorsSimilar(
							colorThreshold, pixels[xr + y * width], color));

			int xl = x;
			do {
				area.add(new int[] { xl, y });
				visited[xl + yp] = true;
				xl--;
			} while ((xl >= 0)
					&& !visited[xl + y * width]
					&& ImageProcessingToolKit.areRBGColorsSimilar(
							colorThreshold, pixels[xl + y * width], color));

			xr--;
			xl++;

			boolean upLine = false;
			boolean downLine = false;

			for (int xi = xl; xi <= xr; xi++) {
				if ((y > 0)
						&& !visited[xi + ypm]
						&& ImageProcessingToolKit.areRBGColorsSimilar(
								colorThreshold, pixels[xi + ypm], color)
						&& !upLine) {
					points.addFirst(new int[] { xi, y - 1 });
					upLine = true;
				} else {
					upLine = false;
				}
				if ((y < height - 1)
						&& !visited[xi + ypp]
						&& ImageProcessingToolKit.areRBGColorsSimilar(
								colorThreshold, pixels[xi + ypp], color)
						&& !downLine) {
					points.addFirst(new int[] { xi, y + 1 });
					downLine = true;
				} else {
					downLine = false;
				}
			}
		}
		return area;
	}

	/**
	 * Checks if the given Pixel is out of the image
	 * 
	 * @param x
	 * @param y
	 * @return true if the pixel is inside the image, false otherwise
	 */
	private boolean hasPixel(int x, int y) {
		return ((x >= 0 && x < image.getWidth()) && (y >= 0 && y < image
				.getHeight()));
	}

	private boolean[][] findColorAreasOfMostFrequentColor() {

		findColorRegions();

		return getBackgroundFromColorRegionList();
	}

	private boolean[][] getBackgroundFromColorRegionList() {
		boolean[][] background = ImageProcessingToolKit
				.getAlphaBackground(image);

		// sort colorRegions for colorItem with biggest coherent region
		java.util.Collections.sort(colorRegionsList,
				new ColorRegionComparator());
		PriorityQueue<Region> regions = colorRegionsList.get(0).getRegions();
		// Checks the top Region, if the count is bigger then
		// image.Width/2*minRegionThreshold, if false
		// the Image should be manipulated with a better algorithm, because
		// background is maybe not found
		// get the color:List<Regions> from the sorted colorRegionList with the
		// biggest region and iterate over this regions priorityQueue with
		// adding every pixel in the regions to the background array, if the
		// region is big enough (specified by minRegionThreshold)
		Region topRegion = regions.peek();
		if (topRegion != null) {
			if (topRegion.getPixelCount() >= minRegion) {
				while (!regions.isEmpty()) {
					Region region = regions.poll();
					for (int z = 0; z < region.getRegion().size(); z++) {
						background[region.getCoordOfIndex(false, z)][region
								.getCoordOfIndex(true, z)] = true;

					}
				}
			} else {
				return null;
			}
		} else
			return null;

		return background;
	}

	/**
	 * returns true if the given pixelCount (of an area) is bigger than
	 * Image.width/minRegionThreshold
	 * 
	 * @param pixelCount
	 * @return
	 */
	private boolean areaBigEnough(int pixelCount) {
		return pixelCount > minRegion2;
	}

	/**
	 * Iterates over every pixel in the image, trys to get from the unvisited
	 * pixels flooded regions of the pixels color
	 */
	private void findColorRegions() {
		int progressLoop = 0;
		int step = (int) (0.01 * width);
		actionController.setProgressCount(width);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (!visited[x + (width * y)]) {
					int currentColor = pixels[x + (width * y)];
					ColorRegions colorRegion = new ColorRegions(currentColor);
					Region region = findColorRegion(x, y, currentColor);
					// checks if the color is already in the colorRegionsList,
					// if so, add the region to the list of the color in the
					// colorRegionList,
					// else add the region with color as new
					// item in colorRegionList
					if (areaBigEnough(region.getPixelCount())) {
						int index = colorRegionsList.indexOf(colorRegion);
						if (index == -1) {
							colorRegion.addRegion(region);
							colorRegionsList.add(colorRegion);
						} else
							colorRegionsList.get(index).addRegion(region);
					}
				}
			}
			if (isCancelled()) {
				actionController.setProgress(image.getWidth());
				return;
			}
			progressLoop++;
			if (progressLoop == step)
				actionController.setProgress(x);
		}

	}

	@Override
	protected boolean[][] doInBackground() {
		return executeSelection();
	}

}
