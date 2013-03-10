package de.tud.textureAttack.model.algorithms.attacks;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.Random;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.AdvancedTextureImage;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.utils.ImageProcessingToolKit;

/**
* Stellt im UML-Klassendiagramm "ConcreteProduct" dar, die von
* der Factory Methode instanziiert wird.
*/
public class RandomAttack extends AbstractAttackAlgorithm {
   
	private int randomness;
	private BufferedImage image;
	
	public RandomAttack(ActionController actionController) {
        super(Options.AttackToolEnum.Random, AbstractAlgorithm.AlgoTypes.ATTACKS, actionController); 
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
		if(initialized){
			initialized = false;
			actionController.setProgressCount(image.getWidth());
			for (int x = 0; x < image.getWidth();x++){
				for (int y = 0; y < image.getHeight();y++){
					if (((boolean[][])result)[y][x]){
						image.setRGB(x, y, generateRandomRGB(randomness,ImageProcessingToolKit.getRed(image, x, y), ImageProcessingToolKit.getGreen(image, x, y), ImageProcessingToolKit.getBlue(image, x, y),ImageProcessingToolKit.getAlpha(image, x, y)));
					}
				}
				if  (isCancelled()) { actionController.setProgress(image.getWidth()); return null;}
				if ((x % 5) == 0) actionController.setProgress(x);
			}
			return image;
		}
		else {
			System.out.println("Random-Attack-Algorithm not initialized!");
			return null;
		}
	}



	private int generateRandomRGB(int randomness,int red, int green, int blue, int alpha) {
		Random r = new Random();

		Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), alpha);
		return c.getRGB();
	}




	@Override
	public void init(BufferedImage img, boolean[][] backgroundRaster, Options options) {
		this.randomness = (int) options.getOption(Options.OptionIdentifierEnum.RandomAttack_Randomness);
        this.image = img;
        this.result = backgroundRaster;
		initialized = true;
	}




	@Override
	protected BufferedImage doInBackground() {
		return executeAttack();
	}
}