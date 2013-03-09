package de.tud.textureAttack.view.components;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import de.tud.textureAttack.model.AdvancedTextureImage;
import de.tud.textureAttack.model.utils.ImageProcessingToolKit;

public class ImagePreviewScrollPane extends JScrollPane {

	private static final long serialVersionUID = 1L;
	private AdvancedTextureImage image;
	private BufferedImage imageSelection;

	private PreviewImagePanel previewImagePanel;

	private Point clickPoint;
	private int colorThreshhold = -1;

	public ImagePreviewScrollPane() {
		super();
		previewImagePanel = new PreviewImagePanel();
		image = null;
		previewImagePanel.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {

				if (image != null) {
					String input = JOptionPane.showInputDialog(null,
							"Geben Sie einen Farbauswahl-Schwellwert an:", 10);
					try {
						colorThreshhold = Integer.valueOf(input);
						clickPoint = new Point(e.getX(), e.getY());
						if (colorThreshhold >= 0) {
							maskBackGroundWithSelectedColor(colorThreshhold);
							previewImagePanel.setImage(imageSelection);
							setViewportView(previewImagePanel);
						}
					} catch (NumberFormatException ex) {
					}

				}

			}
		});
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

	private void maskBackGroundWithSelectedColor(int threshhold) {
		imageSelection = ImageProcessingToolKit.deepCopy(image
				.getEditedBufferedImage());
		Color selectColor = getSelectedPixelColor();
		ArrayList<Point> selection = new ArrayList<Point>();
		for (int col = 0; col < imageSelection.getWidth(); col++) {
			for (int row = 0; row < imageSelection.getHeight(); row++) {
				if (ImageProcessingToolKit
						.areRBGColorsSimilar(threshhold, selectColor,
								new Color(imageSelection.getRGB(col, row)))) {
					selection.add(new Point(col, row));
					imageSelection.setRGB(col, row, Color.PINK.getRGB());
				}
			}
		}

	}

	private Color getSelectedPixelColor() {
		return new Color(image.getEditedBufferedImage().getRGB(clickPoint.x,
				clickPoint.y));
	}

	public AdvancedTextureImage getImage() {
		return image;
	}

}
