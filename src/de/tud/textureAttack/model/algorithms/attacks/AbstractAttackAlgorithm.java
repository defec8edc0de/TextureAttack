package de.tud.textureAttack.model.algorithms.attacks;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;



/**
* Stellt im UML-Klassendiagramm "Product" dar, von der konkrete
* Klassen abgeleitet werden, die auch instanziiert werden können.
*/
public abstract class AbstractAttackAlgorithm extends AbstractAlgorithm{

	protected boolean initialized;
	
	public AbstractAttackAlgorithm(Enum name, AlgoTypes type, ActionController actionController) {
		super(name, type, actionController);
		initialized = false;
	}
	
	public abstract void init(BufferedImage img,boolean[][] backgroundRaster, Options options);
	
	public abstract BufferedImage executeAttack();
	

	
	
}
