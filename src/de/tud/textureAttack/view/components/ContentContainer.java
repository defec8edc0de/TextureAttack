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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.utils.ImageProcessingToolKit;
import de.tud.textureAttack.view.components.toolbox.ToolBoxTabbedWrapper;

public class ContentContainer extends JSplitPane {

	private static final long serialVersionUID = 1L;
	private ActionController actionController;
	private ImageScrollPane imageScrollPane;
	private ImagePreviewScrollPane imagePreviewScrollPane;
	private ToolBoxTabbedWrapper toolBoxTabbedWrapper;
	private JScrollPane toolBoxScrollPane;

	private JSplitPane leftPanes;
	private JSplitPane rightPanes;

	public ContentContainer(ActionController actionController) {
		super(JSplitPane.HORIZONTAL_SPLIT, true);
		setOneTouchExpandable(true);
		setDividerLocation(540);
		this.actionController = actionController;

		initializeComponents();
	}

	/**
	 * Sets the left(preview scroll list) and right(toolbox) content of the
	 * splitpane
	 */
	private void initializeComponents() {

		// if you resize manualy the application, this will fix..a bit..the
		// right tools splitpane
		setResizeWeight(0.9999999999);

		leftPanes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		leftPanes.setOneTouchExpandable(true);
		leftPanes.setDividerLocation(150);

		rightPanes = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		rightPanes.setOneTouchExpandable(false);

		rightPanes.setDividerLocation(100);

		Dimension minimumSize = new Dimension(100, 50);
		imageScrollPane = new ImageScrollPane(actionController, new JList(
				new DefaultListModel()));
		imageScrollPane.setMinimumSize(minimumSize);

		imagePreviewScrollPane = new ImagePreviewScrollPane();
		imagePreviewScrollPane.setMinimumSize(minimumSize);

		toolBoxTabbedWrapper = new ToolBoxTabbedWrapper(actionController);
		// toolBoxTabbedWrapper.setMinimumSize(new Dimension(220, 50));
		// toolBoxTabbedWrapper.setMaximumSize(new Dimension(220, 50));

		leftPanes.add(imageScrollPane);
		leftPanes.add(imagePreviewScrollPane);

		toolBoxScrollPane = new JScrollPane(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// toolBoxScrollPane.setMinimumSize(new Dimension(220, 50));
		// toolBoxScrollPane.setMaximumSize(new Dimension(220, 50));
		toolBoxScrollPane.setViewportView(toolBoxTabbedWrapper);

		rightPanes.add(toolBoxScrollPane);

		add(leftPanes);
		add(rightPanes);

		setupKeyEvents();
	}

	/**
	 * sets up KeyEvents for the content container
	 */
	private void setupKeyEvents() {
		Action zoomOutAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (imagePreviewScrollPane.isImagePreviewed()) {
					imagePreviewScrollPane
							.setPreviewImage(ImageProcessingToolKit
									.resizeImage(imagePreviewScrollPane
											.getImage()
											.getEditedBufferedImage(),
											imagePreviewScrollPane.getImage()
													.getEditedBufferedImage()
													.getType(),
											imagePreviewScrollPane.getImage()
													.getEditedBufferedImage()
													.getWidth() * 2,
											imagePreviewScrollPane.getImage()
													.getEditedBufferedImage()
													.getHeight() * 2));
				}
			}
		};
		Action zoomInAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (imagePreviewScrollPane.isImagePreviewed()) {
					imagePreviewScrollPane
							.setPreviewImage(ImageProcessingToolKit
									.resizeImage(imagePreviewScrollPane
											.getImage()
											.getEditedBufferedImage(),
											imagePreviewScrollPane.getImage()
													.getEditedBufferedImage()
													.getType(),
											imagePreviewScrollPane.getImage()
													.getEditedBufferedImage()
													.getWidth() / 2,
											imagePreviewScrollPane.getImage()
													.getEditedBufferedImage()
													.getHeight() / 2));
				}
			}
		};

		KeyStroke zoomOutKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
				InputEvent.CTRL_DOWN_MASK);
		KeyStroke zoomInKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
				InputEvent.CTRL_DOWN_MASK);

		getActionMap().put("ZoomOut", zoomOutAction);
		getActionMap().put("ZoomIn", zoomOutAction);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(zoomOutKeyStroke,
				"ZoomOut");
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(zoomInKeyStroke,
				"ZoomIn");

	}

	public ImageScrollPane getImageScrollPane() {
		return imageScrollPane;
	}

	public ImagePreviewScrollPane getImagePreviewScrollPane() {
		return imagePreviewScrollPane;
	}

}
