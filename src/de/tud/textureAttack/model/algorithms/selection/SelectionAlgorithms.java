package de.tud.textureAttack.model.algorithms.selection;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithmFactory;
import de.tud.textureAttack.model.algorithms.selection.colorselection.ColorSelection;
import de.tud.textureAttack.model.algorithms.selection.grabcut.GrabCutSelection;

/**
* Stellt im UML-Klassendiagramm "ConcreteCreator" dar, die die
* konkreten Klassen (ConcreteProduct) instanziiert.
*/
public class SelectionAlgorithms extends AbstractAlgorithmFactory {

    public SelectionAlgorithms(ActionController actionController) {
		super(actionController);
	}

	/**
     * Implementiert die abstrakte Methode aus der Oberklasse
     * und erzeugt konkrete Algoobjekt
     */
	@Override
	protected void createAlgorithm(ActionController actionController) {
		algorithms.add(new ColorSelection(actionController));
		algorithms.add(new GrabCutSelection(actionController));
	}

}
