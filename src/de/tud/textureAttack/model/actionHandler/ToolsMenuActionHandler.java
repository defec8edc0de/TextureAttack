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

import de.tud.textureAttack.controller.ActionController;

public class ToolsMenuActionHandler implements ActionListener {

	public static final String FILTER_TODO = "Filter Todos";

	private ActionController actionController;

	public ToolsMenuActionHandler(ActionController actionController) {
		this.actionController = actionController;
	}

	@Override
	public void actionPerformed(ActionEvent menuItem) {
		if (menuItem.getActionCommand().equals(FILTER_TODO)) {
			actionController.filterPreviewList(FILTER_TODO);
		}

	}

}
