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
package de.tud.textureAttack.model.actionHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;

import de.tud.textureAttack.controller.ActionController;

public class ToolsMenuActionHandler implements ActionListener {

	public static final String FILTER_FINISHED = "Filter Finsihed Textures";

	public static final String FILTER_SUPPORTED = "Filter supported Textures";
	
	public static final String CREATE_LIST_UNSUPPORTED_TEXTURES = "Create List with unsupported Texture Paths";

	public static final String RESET_FILTER = "Reset filters";

	private ActionController actionController;

	public ToolsMenuActionHandler(ActionController actionController) {
		this.actionController = actionController;
	}

	@Override
	public void actionPerformed(ActionEvent menuItem) {
		if (menuItem.getActionCommand().equals(FILTER_FINISHED)) {
			actionController.filterPreviewList(FILTER_FINISHED, true);
		}
		if (menuItem.getActionCommand().equals(FILTER_SUPPORTED)) {
			actionController.filterPreviewList(FILTER_SUPPORTED, false);
		}
		if (menuItem.getActionCommand().equals(RESET_FILTER)) {
			actionController.filterPreviewList(RESET_FILTER, true);
		}
		if (menuItem.getActionCommand().equals(CREATE_LIST_UNSUPPORTED_TEXTURES)) {
			actionController.createListOfUnsupportedTextures();
		}
	}

}
