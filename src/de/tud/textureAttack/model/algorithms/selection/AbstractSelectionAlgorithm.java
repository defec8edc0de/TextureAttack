package de.tud.textureAttack.model.algorithms.selection;

import java.awt.image.BufferedImage;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;

/**
 * Stellt im UML-Klassendiagramm "Product" dar, von der konkrete Klassen
 * abgeleitet werden, die auch instanziiert werden können.
 */
public abstract class AbstractSelectionAlgorithm extends AbstractAlgorithm {

	protected boolean initialized;

	public AbstractSelectionAlgorithm(Enum name, AlgoTypes type, ActionController actionController) {
		super(name, type, actionController);
		initialized = false;
	}

	public abstract void init(BufferedImage img, Options options);

	public abstract boolean[][] executeSelection();

}
