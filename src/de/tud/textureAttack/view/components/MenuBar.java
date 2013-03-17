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
package de.tud.textureAttack.view.components;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.actionHandler.FileMenuActionHandler;
import de.tud.textureAttack.model.actionHandler.ToolsMenuActionHandler;

public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	private JMenu fileMenu;
	private FileMenuActionHandler fileMenu_actionHandler;
	private JMenuItem fileMenu_exitItem;
	private JMenuItem fileMenu_loadTexturesItem;
	private JMenuItem fileMenu_saveTexturesItem;
	private JMenuItem fileMenu_saveAllTexturesItem;
	private JMenuItem fileMenu_preferencesItem;

	private JMenu toolsMenu;
	private ToolsMenuActionHandler toolsMenu_actionHandler;
	private JMenuItem toolsMenu_filterFinished;
	private JMenuItem toolMenu_filtersupported;
	private JMenuItem toolMenu_createUsupportedList;
	private JMenuItem toolMenu_resetFilter;


	private ActionController actionController;

	public MenuBar(ActionController actionController) {
		super();
		this.actionController = actionController;
		initializeMenu();
	}

	private void initializeMenu() {
		fileMenu = new JMenu("Datei");
		fileMenu_actionHandler = new FileMenuActionHandler(actionController);

		fileMenu_loadTexturesItem = new JMenuItem("Lade Texturen",
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tud/textureAttack/resources/images/load.png")));
		fileMenu_loadTexturesItem.addActionListener(fileMenu_actionHandler);
		fileMenu_saveTexturesItem = new JMenuItem("Speichere Textur",
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tud/textureAttack/resources/images/save.png")));
		fileMenu_saveTexturesItem.addActionListener(fileMenu_actionHandler);
		fileMenu_saveAllTexturesItem = new JMenuItem("Speichere alle Texturen",
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tud/textureAttack/resources/images/saveall.png")));
		fileMenu_saveAllTexturesItem.addActionListener(fileMenu_actionHandler);

		fileMenu_preferencesItem = new JMenuItem(
				"Einstellungen",
				new ImageIcon(
						getClass()
								.getClassLoader()
								.getResource(
										"de/tud/textureAttack/resources/images/preferences.png")));
		fileMenu_preferencesItem.addActionListener(fileMenu_actionHandler);
		fileMenu.addSeparator();
		fileMenu_exitItem = new JMenuItem("Beenden", new ImageIcon(getClass()
				.getClassLoader().getResource(
						"de/tud/textureAttack/resources/images/exit.png")));
		fileMenu_exitItem.addActionListener(fileMenu_actionHandler);

		fileMenu.add(fileMenu_loadTexturesItem);
		fileMenu.add(fileMenu_saveTexturesItem);
		fileMenu.add(fileMenu_saveAllTexturesItem);
		fileMenu.addSeparator();
		//fileMenu.add(fileMenu_preferencesItem);
		fileMenu.addSeparator();
		fileMenu.add(fileMenu_exitItem);

		toolsMenu = new JMenu("Tools");
		toolsMenu_actionHandler = new ToolsMenuActionHandler(actionController);
		
		toolsMenu_filterFinished = new JMenuItem(ToolsMenuActionHandler.FILTER_FINISHED);
		toolsMenu_filterFinished.addActionListener(toolsMenu_actionHandler);
		
		toolMenu_filtersupported = new JMenuItem(ToolsMenuActionHandler.FILTER_SUPPORTED);
		toolMenu_filtersupported.addActionListener(toolsMenu_actionHandler);
		
		toolMenu_createUsupportedList = new JMenuItem(ToolsMenuActionHandler.CREATE_LIST_UNSUPPORTED_TEXTURES);
		toolMenu_createUsupportedList.addActionListener(toolsMenu_actionHandler);
		
		toolMenu_resetFilter = new JMenuItem(ToolsMenuActionHandler.RESET_FILTER);
		toolMenu_resetFilter.addActionListener(toolsMenu_actionHandler);		
		
		toolsMenu.add(toolsMenu_filterFinished);
		toolsMenu.add(toolMenu_filtersupported);
		toolsMenu.add(toolMenu_createUsupportedList);
		toolsMenu.addSeparator();
		toolsMenu.add(toolMenu_resetFilter);

		add(fileMenu);
		add(toolsMenu);
	}

	public void fireAction(String menuItem, Object source, int id,
			String command) {
		if (menuItem.equals(ToolsMenuActionHandler.FILTER_FINISHED)) {
			toolsMenu_actionHandler.actionPerformed(new ActionEvent(source, id,
					command));
		}
		if (menuItem.equals(ToolsMenuActionHandler.FILTER_SUPPORTED)) {
			toolsMenu_actionHandler.actionPerformed(new ActionEvent(source, id,
					command));
		}
		if (menuItem.equals(ToolsMenuActionHandler.CREATE_LIST_UNSUPPORTED_TEXTURES)) {
			toolsMenu_actionHandler.actionPerformed(new ActionEvent(source, id,
					command));
		}		

	}

}
