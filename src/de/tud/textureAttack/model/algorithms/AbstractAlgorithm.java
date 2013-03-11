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

import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.tud.textureAttack.controller.ActionController;

/**
 * Stellt im UML-Klassendiagramm "Product" dar, von der konkrete Klassen
 * abgeleitet werden, die auch instanziiert werden können.
 */
public abstract class AbstractAlgorithm extends SwingWorker<Object, Void> {

	public static enum AlgoTypes {
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

	protected void done() {
		try {
			if (isCancelled()) {
				result = null;
			} else
				result = get();
			actionController.getStatusBar().done();

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	public AbstractAlgorithm(Enum name, AlgoTypes type,
			ActionController actionController) {
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
