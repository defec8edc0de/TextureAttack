package de.tud.textureAttack.model.algorithms;

import java.util.ArrayList;
import java.util.List;

import de.tud.textureAttack.controller.ActionController;
 
/**
* Stellt im UML-Klassendiagramm "Creator" dar, von der konkrete
* Klassen abgeleitet werden, die auch instanziiert werden können.
*/
public abstract class AbstractAlgorithmFactory {
	
    protected List<AbstractAlgorithm> algorithms =
        new ArrayList<AbstractAlgorithm>();
        
   
    /**
     * Delegiert die Instanziierung der konkreten Algos an
     * implementierende Unterklassen.
     *
     */
    public AbstractAlgorithmFactory(ActionController actionController) {
        this.createAlgorithm(actionController);
    }
 
    public List<AbstractAlgorithm> getAttackAlgorithms() {
    	ArrayList<AbstractAlgorithm> result = new ArrayList<AbstractAlgorithm>();
    	for (AbstractAlgorithm algo : algorithms){
    		if (algo.getType().equals(AbstractAlgorithm.AlgoTypes.ATTACKS)){
    			result.add(algo);
    		}
    	}
       return result;
    }
    
    public AbstractAlgorithm getAlgorithm(Enum name) {
    	for (AbstractAlgorithm algo : algorithms){
    		if (algo.getName().equals(name)){
    			return algo;
    		}
    	}
       return null;
    }

 
    /**
     * Muss von einer Methode überschrieben werden, die konkrete
     * Algos instanziiert. Dies ist das Herzstück des Factory
     * Method Pattern
     *
     */
    protected abstract void createAlgorithm(ActionController actionController);
}