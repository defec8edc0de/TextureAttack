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
package de.tud.textureAttack.view.components.toolbox;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ToolBoxOptionPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel optionLabel;
	private JComponent optionComponent;

	public ToolBoxOptionPane(String optionName, JComponent optionComponent) {
		super(new BorderLayout());
		optionLabel = new JLabel("<html><b>" + optionName + "</b></html>");
		this.optionComponent = optionComponent;
		add(optionLabel, BorderLayout.NORTH);
		JPanel p = new JPanel(new BorderLayout());
		p.add(optionComponent, BorderLayout.NORTH);
		add(p, BorderLayout.CENTER);
	}

	public JLabel getOptionLabel() {
		return optionLabel;
	}

	public void setOptionLabel(JLabel optionLabel) {
		this.optionLabel = optionLabel;
	}

	public JComponent getOptionComponent() {
		return optionComponent;
	}

	public void setOptionComponent(JComponent optionComponent) {
		this.optionComponent = optionComponent;
	}

}
