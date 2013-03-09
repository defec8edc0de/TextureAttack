package de.tud.textureAttack.model.algorithms;

import javax.swing.SwingWorker;

import de.tud.textureAttack.controller.ActionController;



/**
* Stellt im UML-Klassendiagramm "Product" dar, von der konkrete
* Klassen abgeleitet werden, die auch instanziiert werden können.
*/
public abstract class AbstractAlgorithm extends SwingWorker<Void, Void>{
	
	public static enum AlgoTypes{
		ATTACKS, SELECTIONS
	}
	
	
    private Enum name;
    private ActionController actionController;
    private AlgoTypes type;
 
    public Enum getName() {
        return name;
    }

    
    protected abstract Void doInBackground();
    
    public boolean chancel(boolean mayInterruptIfRunning){
		System.out.println(name.toString()+" chanceld!");
		actionController.setStatus(name.toString()+" chanceld!");
		return this.cancel(mayInterruptIfRunning);		
    }
    
    public void done(){
    	actionController.getStatusBar().done(name);
    }

    public AbstractAlgorithm(Enum name, AlgoTypes type, ActionController actionController) {
    	super();
    	this.actionController = actionController;
    	this.type = type;
    	this.name = name;
   
    }
    
    



	public Object getType() {
		return type;
	}
	
	
	
}
