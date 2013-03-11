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
package de.tud.textureAttack.model.io;

public class InvalidTextureException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidTextureException() {
	}

	public InvalidTextureException(String s) {
		super(s);
	}

}
