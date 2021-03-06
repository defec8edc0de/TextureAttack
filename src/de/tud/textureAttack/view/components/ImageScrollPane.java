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
package de.tud.textureAttack.view.components;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.AdvancedTextureImage;
import de.tud.textureAttack.model.io.IOUtils;

public class ImageScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private ActionController actionController;

	private DefaultListModel iconListModel;
	private JList iconList;

	private int selectedIcon;

	public ImageScrollPane(ActionController actionController, JList list) {
		super(list);
		iconList = list;
		iconListModel = (DefaultListModel) list.getModel();
		this.actionController = actionController;
		this.setPreferredSize(new Dimension(230, 800));

		iconList.setLayoutOrientation(JList.VERTICAL_WRAP);
		iconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		iconList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				selectedIcon = iconList.getSelectedIndex();

			}
		});
		iconList.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				String absolutePath = ImageScrollPane.this
						.getSelectedImageByPath();
				if (absolutePath != null) {
					ImageScrollPane.this.actionController.setStatus(IOUtils
							.getFileNameFromPath(absolutePath));
				}

				if ((e.getButton() == 1) && (e.getClickCount() == 2)) {
					if (!iconListModel.isEmpty()){
					ImageScrollPane.this.actionController
							.setPreviewImage(ImageScrollPane.this.actionController
									.getAdvancedTextureImageFromAbsolutePath(ImageScrollPane.this
											.getSelectedImageByPath()));
					}
					}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});

		setDoubleBuffered(true);
	}

	public void setIcons(ArrayList<AdvancedTextureImage> images) {
		if (images != null) {
			iconListModel.clear();
			actionController.setStatus("Creating Previewthumbnails.......");
			actionController.setProgressCount(images.size());
			for (int i = 0; i < images.size(); i++) {
					iconListModel.addElement(new ImageIcon(images.get(i)
							.getThumbnail(), images.get(i).getAbsolutePath()));
					actionController.setProgress(i);
				
			}
		}
		actionController.setStatus(iconListModel.size()+" Textures loaded!");
	}

	public void setIcon(AdvancedTextureImage image) {
		if (image != null) {
			for (int i = 0; i < iconListModel.size(); i++) {
				ImageIcon icon = (ImageIcon) iconListModel.get(i);
				if (icon.getDescription().equals(image.getAbsolutePath())) {
					iconListModel.set(i, new ImageIcon(image.getThumbnail(),
							image.getAbsolutePath()));
				}
			}
		}
	}

	public boolean isSelected(int index) {
		return false;
	}

	public void select(int index) {
		iconList.setSelectedIndex(index);
	}

	public void unSelect() {
		iconList.clearSelection();
	}

	public int getSelecion() {
		return selectedIcon;
	}

	public String getSelectedImageByPath() {
		int index = iconList.getSelectedIndex();
		return index != -1 ? ((ImageIcon) iconListModel.getElementAt(index))
				.getDescription() : null;
	}

}
