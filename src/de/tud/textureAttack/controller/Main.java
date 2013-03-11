/*******************************************************************************
 * Copyright (c) 2012 Sebastian Funke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Sebastian Funke - initial API and implementation
 ******************************************************************************/
package de.tud.textureAttack.controller;

public class Main {

	private ActionController actionController;

	public Main() {
		actionController = new ActionController();
	}

	/**
	 * Main Method to start the application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Main program = new Main();

		// // ImageJ Test
		// int width = 400;
		// int height = 400;
		// ImageProcessor ip = new ByteProcessor(width, height);
		// String title = "My new image";
		// ImagePlus imp = new ImagePlus(title, ip);
		// imp.show();

	}

}
