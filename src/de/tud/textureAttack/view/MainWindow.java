/*******************************************************************************
O * Copyright (c) 2012 Sebastian Funke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Sebastian Funke - initial API and implementation
 ******************************************************************************/
package de.tud.textureAttack.view;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.AdvancedTextureImage;
import de.tud.textureAttack.view.components.ContentContainer;
import de.tud.textureAttack.view.components.MenuBar;
import de.tud.textureAttack.view.components.StatusBar;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private MenuBar menuBar;
	private StatusBar statusBar;
	private ContentContainer contentContainer;

	private ActionController actionController;

	public MainWindow(ActionController actionController) {
		super("Texture Attacker");
		this.actionController = actionController;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		initilizeComponents();

		setVisible(true);
	}

	private void initilizeComponents() {
		contentContainer = new ContentContainer(actionController);
		menuBar = new MenuBar(actionController);
		statusBar = new StatusBar("Program started", actionController, getWidth());
		add(menuBar, BorderLayout.NORTH);
		add(contentContainer, BorderLayout.CENTER);
		add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Loads the loaded texture Images in the left preview scroll list
	 * 
	 * @param images
	 */
	public void loadTextureImages(ArrayList<AdvancedTextureImage> images) {
		contentContainer.getImageScrollPane().setIcons(images);
		revalidate();
	}

	/**
	 * Sets the application status in the bottom status bar
	 * 
	 * @param newStatus
	 */
	public void setStatus(String newStatus) {
		statusBar.setStatus(newStatus);
	}

	public int getSelectedImagePosition() {
		return contentContainer.getImageScrollPane().getSelecion();
	}

	/**
	 * Sets the progress of a calculation in the progress bar in the right upper
	 * corner
	 * 
	 * @param i
	 */
	public void setProgress(int i) {
		statusBar.setProgress(i);
		repaint();
		revalidate();
	}

	public void setProgressCount(int i) {
		statusBar.setProgressCount(i);
	}

	public MenuBar getMenu() {
		return menuBar;
	}

	/**
	 * Returns the main content container (SplitPane) of the GUI
	 * 
	 * @return
	 */
	public ContentContainer getContentContainer() {
		return contentContainer;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}

}
