package de.tud.textureAttack.model.algorithms;

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.tud.textureAttack.controller.ActionController;



/**
* Stellt im UML-Klassendiagramm "Product" dar, von der konkrete
* Klassen abgeleitet werden, die auch instanziiert werden k�nnen.
*/
public abstract class AbstractAlgorithm extends SwingWorker<Object, Void>{
	
	public static enum AlgoTypes{
		ATTACKS, SELECTIONS
	}
	
	
    private Enum name;
    protected ActionController actionController;
    private AlgoTypes type;
 
    protected Object result;
    
    public Enum getName() {
        return name;
    }

    
    protected abstract Object doInBackground();
    
    protected void done(){
    	try {
    		if (isCancelled()) {
    			result = null;
    		}
    		else result = get();
			actionController.getStatusBar().done();
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
    }
    
    
    


    public AbstractAlgorithm(Enum name, AlgoTypes type, ActionController actionController) {
    	super();
    	this.actionController = actionController;
    	this.type = type;
    	this.name = name;
    	this.result = null;
   
    }


	public Object getType() {
		return type;
	}


	public Object getResult() {
		return result;
	}
	
	
	
}
