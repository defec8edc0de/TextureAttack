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
package de.tud.textureAttack.model.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import javax.imageio.ImageIO;

import de.tud.textureAttack.model.algorithms.Pair;
import de.tud.textureAttack.model.algorithms.selection.colorselection.ColorRegions;

public class ImageProcessingToolKit {

	public static final int DEST_WIDTH = 70;
	public static final int DEST_HEIGHT = 70;
	public static final double ASPECT_RATIO = (double) DEST_WIDTH / DEST_HEIGHT;

	public static BufferedImage resizeImage(BufferedImage originalImage,
			int type, int newWidth, int newHeight) throws OutOfMemoryError {
		BufferedImage resizedImage = new BufferedImage(newWidth, newHeight,
				type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
		g.dispose();
		return resizedImage;
	}

	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	/**
	 * Computes the cumulative differences between the color parts and return
	 * true if this difference is smaller as a given threshold
	 * 
	 * @param threshhold
	 * @param c1
	 * @param c2
	 * @return boolean
	 */
	public static boolean areRBGColorsSimilar(int threshhold, Color c1, Color c2) {
		int diffG = Math.abs(c1.getGreen() - c2.getGreen());
		int diffB = Math.abs(c1.getBlue() - c2.getBlue());
		int diffR = Math.abs(c1.getRed() - c2.getRed());
		int diffAlpha = Math.abs(c1.getAlpha() - c2.getAlpha());
		int sum = diffR + diffG + diffB + diffAlpha;
		return sum <= threshhold;
	}
	
	
	
	/**
	 * Computes the cumulative differences between the color parts and return
	 * true if this difference is smaller as a given threshold
	 * 
	 * @param threshhold
	 * @param c1
	 * @param c2
	 * @return boolean
	 */
	public static boolean areRBGColorsSimilar(int threshhold, int c1, int c2) {
		int diffG = Math.abs(green(c1) - green(c2));
		int diffB = Math.abs(blue(c1) - blue(c2));
		int diffR = Math.abs(red(c1) - red(c2));
		int diffAlpha = Math.abs(alpha(c1) - alpha(c2));
		int sum = diffR + diffG + diffB + diffAlpha;
		return sum <= threshhold;
	}

	private static int green(int c) {
		return (c >> 8) & 0xff;
	}

	public static BufferedImage createScaledImage(BufferedImage original) {
		double origAspectRatio = (double) original.getWidth()
				/ original.getHeight();
		double scale = origAspectRatio > ASPECT_RATIO ? (double) DEST_WIDTH
				/ original.getWidth() : (double) DEST_HEIGHT
				/ original.getHeight();
		int newW = (int) (original.getWidth() * scale);
		int newH = (int) (original.getHeight() * scale);
		BufferedImage img = new BufferedImage(DEST_WIDTH, DEST_HEIGHT,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(original, 0, 0, newW, newH, null);
		g2.dispose();
		return img;
	}
	
	public static BufferedImage readImageFromRelativePath(String relPath){
		try{ 
			BufferedImage img = ImageIO.read(ImageProcessingToolKit.class
				.getClassLoader().getResource(relPath));
			return img;
		}
		catch (IOException e){
			e.printStackTrace();
			return null;
		}
	}

	public static BufferedImage addIconToImage(BufferedImage img,
			String iconPath) {

		BufferedImage imageWithIcon = deepCopy(img);
		try {
			BufferedImage icon = ImageIO.read(ImageProcessingToolKit.class
					.getClassLoader().getResource(iconPath));
			Graphics2D gp = imageWithIcon.createGraphics();
			gp.drawImage(icon, 0, 0, null);
			gp.dispose();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return imageWithIcon;
	}

	public static boolean isCompleteTransparent(BufferedImage image, int x,
			int y) {
		int argb = image.getRGB(x, y);
		int alpha = (argb >> 24) & 0xff;
		return alpha == 0;// (image.getRGB(x, y) & 0xFF000000) == 0xFF000000;
	}

	/**
	 * Checks, if the given BufferedImage has a Alpha Pixel in it, which would
	 * be the background
	 * 
	 * @param img
	 * @return boolean
	 */
	public static boolean hasAlphaPixel(BufferedImage img) {
		for (int x = 0; x < img.getWidth(); x++)
			for (int y = 0; y < img.getHeight(); y++)
				if (isCompleteTransparent(img, x, y))
					return true;

		return false;
	}

	/**
	 * Checks, if the given BufferedImage has no alpha-supporting image type
	 * 
	 * @param img
	 * @return boolean
	 */
	public static boolean hasNoAlphaChannel(BufferedImage img) {
		switch (img.getType()) {
		case BufferedImage.TYPE_3BYTE_BGR:
			return true;
		case BufferedImage.TYPE_BYTE_BINARY:
			return true;
		case BufferedImage.TYPE_BYTE_GRAY:
			return true;
		case BufferedImage.TYPE_BYTE_INDEXED:
			return true;
		case BufferedImage.TYPE_INT_BGR:
			return true;
		case BufferedImage.TYPE_INT_RGB:
			return true;
		case BufferedImage.TYPE_USHORT_555_RGB:
			return true;
		case BufferedImage.TYPE_USHORT_565_RGB:
			return true;
		case BufferedImage.TYPE_USHORT_GRAY:
			return true;
		default:
			return false;
		}
	}

	public static boolean hasAlpha(BufferedImage img) {
		return img.getColorModel().hasAlpha();
	}

	public static int getAlpha(BufferedImage image, int x, int y) {
		int argb = image.getRGB(x, y);
		int alpha = (argb >> 24) & 0xff;
		return alpha;
	}
	
	private static int alpha(int c){
		return (c >> 24) & 0xff;
	}

	private static int red(int c) {
		return (c >> 16) & 0xff;
	}
	
	public static int getRed(BufferedImage image, int x, int y) {
		int argb = image.getRGB(x, y);
		int red = (argb >> 16) & 0xff;
		return red;
	}

	public static int getGreen(BufferedImage image, int x, int y) {
		int argb = image.getRGB(x, y);
		int green = (argb >> 8) & 0xff;
		return green;
	}

	private static int blue(int c) {
		return (c) & 0xff;
	}
	
	public static int getBlue(BufferedImage image, int x, int y) {
		int argb = image.getRGB(x, y);
		int blue = (argb) & 0xff;
		return blue;
	}

	public static BufferedImage readImage(String absoluteFilePath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(absoluteFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

	/**
	 * If the given Image has Alpha Pixels, it returns the BackgroundRaster,
	 * else an Raster with no background marks
	 * 
	 * @param img
	 * @return boolean[][] or null
	 */
	public static boolean[][] getAlphaBackground(BufferedImage image) {
		boolean[][] background = new boolean[image.getHeight()][image
				.getWidth()];
		for (int x = 0; x < image.getWidth(); x++)
			for (int y = 0; y < image.getHeight(); y++)
				background[y][x] = false;

		if (ImageProcessingToolKit.hasAlpha(image)) {
			for (int x = 0; x < image.getWidth(); x++)
				for (int y = 0; y < image.getHeight(); y++)
					background[y][x] = ImageProcessingToolKit
							.isCompleteTransparent(image, x, y);

		}
		return background;
	}

	public static BufferedImage generateBinaryImage(boolean[][] visited) {
		BufferedImage result = new BufferedImage(visited[0].length, visited.length, BufferedImage.TYPE_BYTE_BINARY);
		for (int x = 0; x < visited[0].length;x++){
			for (int y = 0; y < visited.length;y++){
				if (visited[y][x]){
					result.setRGB(x, y, 16777215);
				}
				else{
					result.setRGB(x, y, 0);
				}
			}
		}
		return result;
	}

	public static BufferedImage debugColorRegions(ColorRegions colorRegions, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		
		int color = colorRegions.getColor().getRGB();
		PriorityQueue<Pair<Integer, ArrayList<Point>>> regions = colorRegions.getRegions();
		for ( Iterator<Pair<Integer, ArrayList<Point>>> i = regions.iterator(); i.hasNext(); ) 
		{ 
			Pair<Integer, ArrayList<Point>> s = i.next();
			for (int x = 0; x < s.getValue().size();x++){
				img.setRGB(s.getValue().get(x).x, s.getValue().get(x).y, color);
			}
		 
		}
		
		return img;
	}

	public static BufferedImage debugColorRegionsList(
			ArrayList<ColorRegions> colorRegionsList, int width, int height, int type) {
		BufferedImage img = new BufferedImage(width, height, type);
		
		for (int k = 0; k < colorRegionsList.size();k++){
		ColorRegions colorRegions = colorRegionsList.get(k);
		int currentColor = colorRegions.getColor().getRGB();
		PriorityQueue<Pair<Integer, ArrayList<Point>>> regions = colorRegions.getRegions();
		for ( Iterator<Pair<Integer, ArrayList<Point>>> i = regions.iterator(); i.hasNext(); ) 
		{ 
			Pair<Integer, ArrayList<Point>> s = i.next();
			for (int x = 0; x < s.getValue().size();x++){
				img.setRGB(s.getValue().get(x).x, s.getValue().get(x).y, currentColor);
			}
		 
		}
		}
		
		return img;
	}

}
