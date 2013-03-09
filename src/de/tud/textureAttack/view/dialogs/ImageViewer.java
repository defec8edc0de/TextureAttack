package de.tud.textureAttack.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import de.tud.textureAttack.model.utils.ImageProcessingToolKit;

public class ImageViewer {

	private static JScrollPane scrollPane;
	private static JFrame window;

	private static BufferedImage labelImage;
	private static BufferedImage labelImageSelection;
	private static JLabel label;

	private static Point clickPoint;
	private static int colorThreshhold = -1;

	public static void showImageViewer(Component parent, BufferedImage image) {
		window = new JFrame("Image Viewer");
		labelImage = image;
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		label = new JLabel(new ImageIcon(image));
		label.addMouseListener(new MouseListener() {
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
				String input = JOptionPane.showInputDialog(null,
						"Geben Sie einen Farbauswahl-Schwellwert an:", 10);
				try {
					colorThreshhold = Integer.valueOf(input);
					clickPoint = new Point(e.getX(), e.getY());
					if (colorThreshhold >= 0) {
						maskBackGroundWithSelectedColor(colorThreshhold);
						JLabel label = new JLabel(new ImageIcon(
								labelImageSelection));
						scrollPane.setViewportView(label);
					}
				} catch (NumberFormatException ex) {
				}

			}

		});
		scrollPane = new JScrollPane(label);
		scrollPane.setDoubleBuffered(true);
		window.add(scrollPane, BorderLayout.CENTER);
		window.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				try {
					if (e.getKeyChar() == '+') {
						labelImage = ImageProcessingToolKit.resizeImage(
								labelImage, labelImage.getType(),
								labelImage.getWidth() * 2,
								labelImage.getHeight() * 2);
						JLabel label = new JLabel(new ImageIcon(labelImage));
						scrollPane.setViewportView(label);

					} else if (e.getKeyChar() == '-') {
						labelImage = ImageProcessingToolKit.resizeImage(
								labelImage, labelImage.getType(),
								labelImage.getWidth() / 2,
								labelImage.getHeight() / 2);
						JLabel label = new JLabel(new ImageIcon(labelImage));
						scrollPane.setViewportView(label);

					} else if (e.getKeyChar() == 'z') {
						JLabel label = new JLabel(new ImageIcon(labelImage));
						scrollPane.setViewportView(label);
					}
				} catch (OutOfMemoryError e1) {
				}
			}
		});
		window.pack();
		window.setLocationRelativeTo(parent);
		window.setVisible(true);
	}

	private static void maskBackGroundWithSelectedColor(int threshhold) {
		labelImageSelection = ImageProcessingToolKit.deepCopy(labelImage);
		Color selectColor = getSelectedPixelColor();
		ArrayList<Point> selection = new ArrayList<Point>();
		for (int col = 0; col < labelImageSelection.getWidth(); col++) {
			for (int row = 0; row < labelImageSelection.getHeight(); row++) {
				if (ImageProcessingToolKit.areRBGColorsSimilar(threshhold,
						selectColor,
						new Color(labelImageSelection.getRGB(col, row)))) {
					selection.add(new Point(col, row));
					labelImageSelection.setRGB(col, row, Color.PINK.getRGB());
				}
			}
		}

	}

	private static Color getSelectedPixelColor() {
		return new Color(labelImage.getRGB(clickPoint.x, clickPoint.y));
	}

}
