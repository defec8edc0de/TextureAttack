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
package de.tud.textureAttack.model.algorithms;

import java.util.ArrayList;
import java.util.List;

import de.tud.textureAttack.controller.ActionController;

/**
 * Stellt im UML-Klassendiagramm "Creator" dar, von der konkrete Klassen
 * abgeleitet werden, die auch instanziiert werden können.
 */
public abstract class AbstractAlgorithmFactory {

	protected List<AbstractAlgorithm> algorithms = new ArrayList<AbstractAlgorithm>();

	/**
	 * Delegiert die Instanziierung der konkreten Algos an implementierende
	 * Unterklassen.
	 * 
	 */
	public AbstractAlgorithmFactory(ActionController actionController) {
		this.createAlgorithm(actionController);
	}

	public List<AbstractAlgorithm> getAttackAlgorithms() {
		ArrayList<AbstractAlgorithm> result = new ArrayList<AbstractAlgorithm>();
		for (AbstractAlgorithm algo : algorithms) {
			if (algo.getType().equals(AbstractAlgorithm.AlgoTypes.ATTACKS)) {
				result.add(algo);
			}
		}
		return result;
	}

	public AbstractAlgorithm getAlgorithm(Enum name) {
		for (AbstractAlgorithm algo : algorithms) {
			if (algo.getName().equals(name)) {
				return algo;
			}
		}
		return null;
	}

	public boolean removeAlgorithm(AbstractAlgorithm algorithm) {
		if (algorithms.contains(algorithm)) {
			algorithms.remove(algorithm);
			return true;
		} else
			return false;
	}

	/**
	 * Muss von einer Methode überschrieben werden, die konkrete Algos
	 * instanziiert. Dies ist das Herzstück des Factory Method Pattern
	 * 
	 */
	protected abstract void createAlgorithm(ActionController actionController);

	public boolean contains(Enum name) {
		boolean result = false;
		for (int i = 0; i < algorithms.size(); i++) {
			if (algorithms.get(i).getName().equals(name))
				return true;
		}
		return result;
	}
}
