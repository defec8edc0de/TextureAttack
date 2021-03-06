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

import java.awt.image.BufferedImage;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;

/**
 * Stellt im UML-Klassendiagramm "Product" dar, von der konkrete Klassen
 * abgeleitet werden, die auch instanziiert werden k�nnen.
 */
public abstract class AbstractAttackAlgorithm extends AbstractAlgorithm {

	protected boolean initialized;

	public AbstractAttackAlgorithm(Enum name, AlgoTypes type,
			ActionController actionController) {
		super(name, type, actionController);
		initialized = false;
	}

	public abstract void init(BufferedImage img, boolean[][] backgroundRaster,
			Options options);

	public abstract BufferedImage executeAttack();

}
