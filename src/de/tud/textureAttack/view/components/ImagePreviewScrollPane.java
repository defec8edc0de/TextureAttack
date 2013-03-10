package de.tud.textureAttack.view.components;

import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;

import de.tud.textureAttack.model.AdvancedTextureImage;

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

	public void setPreviewImage(BufferedImage img) {
		previewImagePanel.setImage(img);
		setViewportView(previewImagePanel);
	}

	public void setImage(AdvancedTextureImage img) {
		if (image != null) {
			image.resetImage();
			image = img;
			if (image.getEditedBufferedImage() == null) {
				image.loadImage();
				previewImagePanel.setImage(image.getEditedBufferedImage());
				setViewportView(previewImagePanel);
			}
		}
		image = img;
		if (image.getEditedBufferedImage() == null) {
			image.loadImage();
			previewImagePanel.setImage(image.getEditedBufferedImage());
			setViewportView(previewImagePanel);
		}
	}

	public AdvancedTextureImage getImage() {
		return image;
	}

}
