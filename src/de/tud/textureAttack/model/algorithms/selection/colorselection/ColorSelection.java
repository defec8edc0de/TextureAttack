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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

import javax.imageio.ImageIO;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.Pair;
import de.tud.textureAttack.model.algorithms.selection.AbstractSelectionAlgorithm;
import de.tud.textureAttack.model.io.IOUtils;
import de.tud.textureAttack.model.utils.ImageProcessingToolKit;

/**
 * Stellt im UML-Klassendiagramm "ConcreteProduct" dar, die von der Factory
 * Methode instanziiert wird.
 */
public class ColorSelection extends AbstractSelectionAlgorithm {

	private int colorThreshold;
	private int minRegionThreshold;
	private BufferedImage image;
	private ActionController actionController;

	private int minRegion;
	private int minRegion2;

	private boolean[][] visited;
	private ArrayList<ColorRegions> colorRegionsList;

	public ColorSelection(ActionController actionController) {
		super(Options.SelectionToolEnum.ColorSelection,
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
				.getOption(Options.OptionIdentifierEnum.ColorSelection_ColorThreshold);
		this.minRegionThreshold = (int) options
				.getOption(Options.OptionIdentifierEnum.ColorSelection_MinRegionThreshold);
		this.image = img;
		initialized = true;
		visited = new boolean[img.getHeight()][img.getWidth()];
		for (int x = 0; x < img.getWidth(); x++)
			for (int y = 0; y < img.getHeight(); y++)
				visited[y][x] = false;
		this.imageName = imageName;
		result = new boolean[img.getHeight()][img.getWidth()];
		// FrequencyComparator frequencyComparator = new FrequencyComparator();
		colorRegionsList = new ArrayList<ColorRegions>();

		minRegion = (int) (image.getWidth() / minRegionThreshold);
		minRegion2 = (int) (image.getWidth() / (minRegionThreshold << 2));
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
			System.out.println("ColorSelection-Algorithm not initialized!");
			return null;
		}
	}

	private boolean[][] getBackground() {
		return findColorAreasOfMostFrequentColor();
	}

	/**
	 * Computes an iterative floodfill from the given pixel and with the given
	 * color (colorThreshold for color similarity) and returns a Pair with
	 * Key=PixelCount of the flooded region and value=List of Points in this
	 * region
	 * 
	 * @param x
	 * @param y
	 * @param searchColor
	 * @param tolerance
	 * @return Pair<Integer, ArrayList<Point>>
	 */
	private Pair<Integer, ArrayList<Point>> findColorRegion(int x, int y,
			int searchColor) {

		int regionPixelCount = 0;
		Pair<Integer, ArrayList<Point>> result = null;
		ArrayList<Point> areaPixel = new ArrayList<Point>();
		Stack<Point> stack = new Stack<Point>();
		stack.push(new Point(x, y));

		while (!stack.isEmpty()) {
			Point p = stack.pop();
			int currentPixelColor = image.getRGB(p.x, p.y);

			if (!visited[p.y][p.x]
					&& ImageProcessingToolKit.areRBGColorsSimilar(
							colorThreshold, currentPixelColor, searchColor)) {
				visited[p.y][p.x] = true;
				regionPixelCount++;
				areaPixel.add(p);

				if (hasPixel(p.x, p.y + 1))
					stack.push(new Point(p.x, p.y + 1));
				if (hasPixel(p.x, p.y - 1))
					stack.push(new Point(p.x, p.y - 1));
				if (hasPixel(p.x + 1, p.y))
					stack.push(new Point(p.x + 1, p.y));
				if (hasPixel(p.x - 1, p.y))
					stack.push(new Point(p.x - 1, p.y));
			}
		}

		result = new Pair<Integer, ArrayList<Point>>(regionPixelCount,
				areaPixel);
		return result;

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

		// Checks the top Region, if the count is bigger then
		// image.Width/minRegionThreshold, if false
		// the Image should be manipulated with a better algorithm, because
		// background is maybe not found
		// get the color:List<Regions> Pairs from the sorted colorRegionList
		// with similiar color
		// and iterate over their regions priorityQueue with
		// adding every pixel in the regions to the background array, if the
		// region is big enough (specified by minRegion)

		ArrayList<ColorRegions> topRegions = getTopColorRegionsWithSimiliarColor();

		if (!topRegions.isEmpty() && regionValid(topRegions.get(0).getTop())) {
			for (ColorRegions currentColorRegions : topRegions) {
				PriorityQueue<Pair<Integer, ArrayList<Point>>> currentRegions = currentColorRegions
						.getRegions();

				while (!currentRegions.isEmpty()) {
					Pair<Integer, ArrayList<Point>> region = currentRegions
							.poll();
					if (region.getKey() >= minRegion) {
						for (int z = 0; z < region.getValue().size(); z++) {
							background[region.getValue().get(z).y][region
									.getValue().get(z).x] = true;
						}
					}
				}
			}

		} else
			return null;

		return background;

	}

	private boolean regionValid(Pair<Integer, ArrayList<Point>> topRegion) {
		if (topRegion != null) {
			if (topRegion.getKey() >= minRegion)
				return true;
		}
		return false;

	}

	/**
	 * Sorts the colorRegionsList and returns the biggest ColorRegions if
	 * colorThreshold == 0 the biggest ColorRegion is the topMost after sort
	 * else it have to compare of similarity(with colorThreshold) the color of
	 * the topmost ColorRegion with the lower
	 * 
	 * @return ArrayList<ColorRegions>
	 */
	private ArrayList<ColorRegions> getTopColorRegionsWithSimiliarColor() {
		ArrayList<ColorRegions> colorRegions = new ArrayList<ColorRegions>();
		if (!colorRegionsList.isEmpty()) {
			// sort colorRegions for colorItem with biggest coherent region
			java.util.Collections.sort(colorRegionsList,
					new ColorRegionComparator());
			ColorRegions topMostColorRegion = colorRegionsList.get(0);
			Color topMostColor = topMostColorRegion.getColor();

			// If colorThreshold is 0, just return the topMost ColorRegion
			if (colorThreshold == 0) {
				colorRegions.add(topMostColorRegion);
			} else {
				for (ColorRegions currentColorRegions : colorRegionsList) {
					// colorThreshold*2 has the following explanation:
					// the colorRegions have different color differences to each
					// other
					// the most often one, can have a too big difference, so a
					// region
					// wont get marked as background, if the threshold is too
					// small
					// therefore you need to raise the colorThreshold a bit
					// (between 1-2)
					if (ImageProcessingToolKit.areRBGColorsSimilar(
							((int) (colorThreshold * 2)), topMostColor,
							currentColorRegions.getColor())) {
						colorRegions.add(currentColorRegions);
					}
				}
			}
		}
		return colorRegions;
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
		int step = (int) (0.01 * image.getWidth());
		actionController.setProgressCount(image.getWidth());
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (!visited[y][x]) {
					int currentColor = image.getRGB(x, y);
					ColorRegions colorRegion = new ColorRegions(new Color(
							currentColor));
					Pair<Integer, ArrayList<Point>> region = findColorRegion(x,
							y, currentColor);

					// checks if the color is already in the colorRegionsList,
					// if so, add the region to the list of the color in the
					// colorRegionList,
					// else add the region with color as new
					// item in colorRegionList
					if (areaBigEnough(region.getKey())) {
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
		actionController.setProgress(image.getWidth());

	}

	private void debugVisited(int i, boolean[][] v) {
		BufferedImage img = ImageProcessingToolKit.generateBinaryImage(v);
		File f = new File("c:\\debug\\debug" + i + ".jpg");
		try {
			ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void debugEveryColorRegion(int i) {
		for (ColorRegions colorRegion : colorRegionsList) {
			BufferedImage img = ImageProcessingToolKit.debugColorRegions(
					colorRegion, image.getWidth(), image.getHeight());
			File f = new File("c:\\debug\\debug" + i + ".jpg");
			try {
				ImageIO.write(img, "jpg", f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void debugColorRegionsList(int i, ArrayList<ColorRegions> list) {
		BufferedImage img = ImageProcessingToolKit.debugColorRegionsList(list,
				image.getWidth(), image.getHeight(), image.getType());
		File f = new File("c:\\debug\\debug_colorRegionsList" + i + ".jpg");
		try {
			ImageIO.write(img, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected boolean[][] doInBackground() {
		return executeSelection();
	}

}
