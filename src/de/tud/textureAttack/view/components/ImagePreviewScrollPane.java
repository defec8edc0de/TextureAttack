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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;

import de.tud.textureAttack.model.AdvancedTextureImage;
import de.tud.textureAttack.model.AdvancedTextureImage.EditState;

public class ImagePreviewScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private AdvancedTextureImage image;

	private PreviewImagePanel previewImagePanel;

	public ImagePreviewScrollPane() {
		super();
		previewImagePanel = new PreviewImagePanel();
		
		image = null;
		add(previewImagePanel);
		setViewportView(previewImagePanel);
		getVerticalScrollBar().setUnitIncrement(32);
	}

	public boolean isImagePreviewed() {
		return (image != null);
	}

	public void setPreviewImage(BufferedImage img, int width, int height) {
		previewImagePanel.setImage(img, width, height);
		setViewportView(previewImagePanel);
	}

	public void setImage(AdvancedTextureImage img) {
		if (image != null) {
			image.resetImage();
			image = img;
			if (image.getState() != EditState.unsupported){
			if (image.getEditedBufferedImage() == null) {
				image.loadImage();
				if (image.getEditedBufferedImage() != null){
					previewImagePanel.setImage(image.getEditedBufferedImage(), image.getEditedBufferedImage().getWidth(), image.getEditedBufferedImage().getHeight());
					setViewportView(previewImagePanel);
				}
			}
			}
		}
		image = img;
		if (image.getState() != EditState.unsupported){
		if (image.getEditedBufferedImage() == null) {
			image.loadImage();
			if (image.getEditedBufferedImage() != null){
				previewImagePanel.setImage(image.getEditedBufferedImage(), image.getEditedBufferedImage().getWidth(), image.getEditedBufferedImage().getHeight());
				setViewportView(previewImagePanel);
			}
		}
		}
	}

	public AdvancedTextureImage getImage() {
		return image;
	}

	public BufferedImage getPreviewImage() {
		return previewImagePanel.getImage();
	}

}
