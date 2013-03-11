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
package de.tud.textureAttack.model.algorithms.selection.grabcut;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.AbstractAlgorithm;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.selection.AbstractSelectionAlgorithm;

public class GrabCutSelection extends AbstractSelectionAlgorithm {

	private Color foregroundColor;
	private Color backgroundColor;
	private BufferedImage image;

	public GrabCutSelection(ActionController actionController) {
		super(Options.SelectionToolEnum.GrabCut,
				AbstractAlgorithm.AlgoTypes.SELECTIONS, actionController);
		image = null;
	}

	/**
	 * Initializes the color selection algorithm, especially the relevant
	 * options were set from the given option object
	 */
	@Override
	public void init(BufferedImage img, Options options, String imageName) {
		this.foregroundColor = (Color) options
				.getOption(Options.OptionIdentifierEnum.GrabCut_forColor);
		this.backgroundColor = (Color) options
				.getOption(Options.OptionIdentifierEnum.GrabCut_backColor);
		this.image = img;
		initialized = true;
		this.imageName = imageName;
		JOptionPane.showMessageDialog(null, "GrabCut is not implemented yet");

	}

	/**
	 * Executes the background selection algorithm and returns a boolean[][] in
	 * dimension of the image, where true means this pixel is background
	 */
	@Override
	public boolean[][] executeSelection() {
		if (initialized) {
			// TODO
			return null;
		} else {
			System.out.println("ColorSelection-Algorithm not initialized!");
			return null;
		}
	}

	@Override
	protected boolean[][] doInBackground() {
		// TODO Auto-generated method stub
		return null;
	}

}
