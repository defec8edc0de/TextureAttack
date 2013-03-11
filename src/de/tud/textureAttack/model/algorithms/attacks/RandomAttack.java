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
package de.tud.textureAttack.model.algorithms.attacks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.utils.ImageProcessingToolKit;

/**
 * Stellt im UML-Klassendiagramm "ConcreteProduct" dar, die von der Factory
 * Methode instanziiert wird.
 */
public class RandomAttack extends AbstractAttackAlgorithm {

	private int randomness;
	private BufferedImage image;

	public RandomAttack(ActionController actionController) {
		super(Options.AttackToolEnum.Random,
				AbstractAlgorithm.AlgoTypes.ATTACKS, actionController);
		randomness = 10;
		image = null;
		result = null;
	}

	public int getRandomness() {
		return randomness;
	}

	public void setRandomness(int randomness) {
		this.randomness = randomness;
	}

	@Override
	public BufferedImage executeAttack() {
		if (initialized) {
			initialized = false;
			actionController.setProgressCount(image.getWidth());
			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					if (((boolean[][]) result)[y][x]) {
						image.setRGB(
								x,
								y,
								generateRandomRGB(randomness,
										ImageProcessingToolKit.getRed(image, x,
												y), ImageProcessingToolKit
												.getGreen(image, x, y),
										ImageProcessingToolKit.getBlue(image,
												x, y), ImageProcessingToolKit
												.getAlpha(image, x, y)));
					}
				}
				if (isCancelled()) {
					actionController.setProgress(image.getWidth());
					return null;
				}
				if ((x % 5) == 0)
					actionController.setProgress(x);
			}
			return image;
		} else {
			System.out.println("Random-Attack-Algorithm not initialized!");
			return null;
		}
	}

	/**
	 * Generates random integer between min and max (with min and max in the
	 * result set)
	 * 
	 * @param min
	 * @param max
	 * @return int
	 */
	private int getRandomNumberFrom(int min, int max) {
		Random foo = new Random();
		int randomNumber = foo.nextInt((max + 1) - min) + min;

		return randomNumber;

	}

	/**
	 * Generates random RGB Value with same alpha value given and the given
	 * randomness
	 * 
	 * @param randomness
	 * @param red
	 * @param green
	 * @param blue
	 * @param alpha
	 * @return int RGB
	 */
	private int generateRandomRGB(int randomness, int red, int green, int blue,
			int alpha) {
		int newRed = Math.abs(getRandomNumberFrom(red - randomness, red
				+ randomness));
		int newGreen = Math.abs(getRandomNumberFrom(green - randomness, green
				+ randomness));
		int newBlue = Math.abs(getRandomNumberFrom(blue - randomness, blue
				+ randomness));

		Color c = new Color(newRed > 255 ? 255 : newRed, newGreen > 255 ? 255
				: newGreen, newBlue > 255 ? 255 : newBlue, alpha);
		return c.getRGB();
	}

	@Override
	public void init(BufferedImage img, boolean[][] backgroundRaster,
			Options options) {
		this.randomness = (int) options
				.getOption(Options.OptionIdentifierEnum.RandomAttack_Randomness);
		this.image = img;
		this.result = backgroundRaster;
		initialized = true;
	}

	@Override
	protected BufferedImage doInBackground() {
		return executeAttack();
	}
}
