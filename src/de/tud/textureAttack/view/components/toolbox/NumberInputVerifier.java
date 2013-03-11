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

import java.awt.Color;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NumberInputVerifier extends InputVerifier {

	public enum InputType {
		integer, string
	};

	private InputType inputType;
	private boolean range;
	private int min;
	private int max;

	public NumberInputVerifier(InputType type) {
		super();
		this.inputType = type;
		this.range = false;
		this.min = 0;
		this.max = 0;
	}

	/**
	 * Constructs a NumberInputVerifier with min and max as valid number range
	 * (min and max are in the range)
	 * 
	 * @param type
	 * @param min
	 * @param max
	 */
	public NumberInputVerifier(InputType type, int min, int max) {
		super();
		this.inputType = type;
		this.range = false;
		this.min = min;
		this.max = max;
		this.range = true;
	}

	/**
	 * The edge values are in the range
	 * 
	 * @param min
	 * @param max
	 */
	public void setRange(int min, int max) {
		range = true;
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean verify(JComponent input) {
		JTextField jtf = (JTextField) input;
		boolean ans = false;
		String toWorkWith = jtf.getText();
		toWorkWith = toWorkWith.replaceAll(",", ".");
		switch (inputType) {
		case integer:
			ans = checkIfNumber(toWorkWith);
			break;

		default:
			break;
		}
		if (!ans) {
			JOptionPane.showMessageDialog(null, "The Value " + jtf.getText()
					+ " is no correct Value!");
		}

		return ans;
	}

	@Override
	public boolean shouldYieldFocus(JComponent input) {
		if (!verify(input)) {
			input.setForeground(Color.RED);
			return false;
		} else {
			input.setForeground(Color.BLACK);
			return true;
		}
	}

	private boolean checkIfNumber(String toWorkWith) {
		try {
			int i = Integer.parseInt(toWorkWith);
			if (range) {
				if ((i >= min) && (i <= max))
					return true;
				else
					return false;
			}
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
