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

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithmFactory;

/**
 * Stellt im UML-Klassendiagramm "ConcreteCreator" dar, die die konkreten
 * Klassen (ConcreteProduct) instanziiert.
 */
public class AttackAlgorithms extends AbstractAlgorithmFactory {

	public AttackAlgorithms(ActionController actionController) {
		super(actionController);
		createAlgorithm(actionController);
	}

	/**
	 * Implementiert die abstrakte Methode aus der Oberklasse und erzeugt
	 * konkrete Algoobjekt
	 */
	@Override
	protected void createAlgorithm(ActionController actionController) {
		algorithms.add(new RandomAttack(actionController));
	}

}
