package de.tud.textureAttack.model.algorithms.attacks;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithmFactory;

/**
* Stellt im UML-Klassendiagramm "ConcreteCreator" dar, die die
* konkreten Klassen (ConcreteProduct) instanziiert.
*/
public class AttackAlgorithms extends AbstractAlgorithmFactory {

    public AttackAlgorithms(ActionController actionController) {
		super(actionController);
    	createAlgorithm(actionController);
	}

	/**
     * Implementiert die abstrakte Methode aus der Oberklasse
     * und erzeugt konkrete Algoobjekt
     */
	@Override
	protected void createAlgorithm(ActionController actionController) {
		algorithms.add(new RandomAttack(actionController));
	}

}
