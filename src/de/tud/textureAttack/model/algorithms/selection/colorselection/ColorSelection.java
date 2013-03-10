package de.tud.textureAttack.model.algorithms.selection.colorselection;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.Pair;
import de.tud.textureAttack.model.algorithms.selection.AbstractSelectionAlgorithm;
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

	private boolean[][] visited;
	private ArrayList<ColorRegions> colorRegionsList;

	public ColorSelection(ActionController actionController) {
		super(Options.SelectionToolEnum.ColorSelection,
				AbstractAlgorithm.AlgoTypes.SELECTIONS,actionController);
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
		this.imageName = imageName;
		result = new boolean[img.getHeight()][img.getWidth()];
		// FrequencyComparator frequencyComparator = new FrequencyComparator();
		colorRegionsList = new ArrayList<ColorRegions>();

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
			Color searchColor) {

		int regionPixelCount = 0;
		Pair<Integer, ArrayList<Point>> result = null;
		ArrayList<Point> areaPixel = new ArrayList<Point>();
		Stack<Point> stack = new Stack<Point>();
		stack.push(new Point(x, y));

		while (!stack.isEmpty()) {
			Point p = stack.pop();
			Color currentPixelColor = new Color(image.getRGB(p.x, p.y));

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

		// sort colorRegions for colorItem with biggest coherent region
		java.util.Collections.sort(colorRegionsList,
				new ColorRegionComparator());
		PriorityQueue<Pair<Integer, ArrayList<Point>>> regions = colorRegionsList
				.get(0).getRegions();
		// get the color:List<Regions> from the sorted colorRegionList with the
		// biggest region and iterate over this regions priorityQueue with
		// adding every pixel in the regions to the background array, if the
		// region is big enough (specified by minRegionThreshold)
		while (!regions.isEmpty()) {
			Pair<Integer, ArrayList<Point>> region = regions.poll();
			for (int z = 0; z < region.getValue().size(); z++) {
				if (areaBigEnough(region.getKey())) {
					background[region.getValue().get(z).y][region.getValue()
							.get(z).x] = true;
				}
			}
		}

		return background;
	}

	/**
	 * returns true if the given pixelCount (of an area) is bigger than
	 * Image.width/minRegionThreshold
	 * 
	 * @param pixelCount
	 * @return
	 */
	private boolean areaBigEnough(Integer pixelCount) {
		return pixelCount > (image.getWidth() / minRegionThreshold);
	}

	/**
	 * Iterates over every pixel in the image, trys to get from the unvisited
	 * pixels flooded regions of the pixels color
	 */
	private void findColorRegions() {
		actionController.setProgressCount(image.getWidth());
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (!visited[y][x]) {
					Color currentColor = new Color(image.getRGB(x, y));
					ColorRegions colorRegion = new ColorRegions(currentColor);
					Pair<Integer, ArrayList<Point>> region = findColorRegion(x,
							y, currentColor);
					// checks if the color is already in the colorRegionsList,
					// if so, add the region to the list of the color in the
					// colorRegionList,
					// else add the region with color as new
					// item in colorRegionList
					int index = colorRegionsList.indexOf(colorRegion);
					if (index == -1) {
						colorRegion.addRegion(region);
						colorRegionsList.add(colorRegion);
					} else
						colorRegionsList.get(index).addRegion(region);
				}
			}
			if  (isCancelled()) { actionController.setProgress(image.getWidth()); return;}
			if ((x % 5) == 0) actionController.setProgress(x);
		}


	}



	@Override
	protected boolean[][] doInBackground() {
		return executeSelection();
	}

}