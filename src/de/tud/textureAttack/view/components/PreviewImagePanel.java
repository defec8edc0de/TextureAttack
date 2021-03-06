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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PreviewImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int PREF_W = 256;
	private int PREF_H = 256;
	private BufferedImage img = null;


	
	
	public void setImage(BufferedImage img, int width, int height) {
		this.img = img;
		PREF_W = width < 256 ? 256 : width;
		PREF_H =  height < 256 ? 256 : height;
		repaint();
	}
	
	

	public BufferedImage getImage() {
		return img;
	}

	public boolean isPreviewed() {
		return (img != null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (img == null) {
			return;
		}
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.drawImage(img, 0, 0, PREF_W, PREF_H, null);

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PREF_W, PREF_H);
	}
}
