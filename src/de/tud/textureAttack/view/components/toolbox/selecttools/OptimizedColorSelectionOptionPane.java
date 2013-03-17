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
package de.tud.textureAttack.view.components.toolbox.selecttools;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.view.components.toolbox.NumberInputVerifier;
import de.tud.textureAttack.view.components.toolbox.ToolBoxOptionPane;

public class OptimizedColorSelectionOptionPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField colorThresholdTextField;
	private JTextField minRegionThresholdTextField;
	private ActionController actionController;

	public OptimizedColorSelectionOptionPane(ActionController actionController) {
		// super(new FlowLayout(FlowLayout.LEFT));
		// super(new SpringLayout());
		// super(new BorderLayout());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		// super(new GridLayout(3, 1, 5, 2));
		// super(new GridBagLayout());
		this.actionController = actionController;

		colorThresholdTextField = new JTextField();
		String colorThreshold = String
				.valueOf((Integer) actionController
						.getOption(Options.OptionIdentifierEnum.OptimizedColorSelection_ColorThreshold));
		colorThresholdTextField.setText(colorThreshold);

		colorThresholdTextField.setMinimumSize(new Dimension(30, 20));
		colorThresholdTextField.setMaximumSize(new Dimension(30, 20));
		colorThresholdTextField.setSize(new Dimension(30, 20));
		colorThresholdTextField.setPreferredSize(new Dimension(30, 20));
		colorThresholdTextField
				.setToolTipText("Sets how similiar (0 = equal) coherent colors of the background can be. Valid Value-Range: 0-9999");

		colorThresholdTextField.setInputVerifier(new NumberInputVerifier(
				NumberInputVerifier.InputType.integer, 0, 9999));

		colorThresholdTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						if (colorThresholdTextField.getInputVerifier()
								.shouldYieldFocus(colorThresholdTextField)) {
							int newValue = Integer
									.valueOf(colorThresholdTextField.getText());
							OptimizedColorSelectionOptionPane.this.actionController
									.setOption(
											Options.OptionIdentifierEnum.OptimizedColorSelection_ColorThreshold,
											newValue);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
					}
				});

		minRegionThresholdTextField = new JTextField();
		minRegionThresholdTextField
				.setText(String.valueOf((Integer) actionController
						.getOption(Options.OptionIdentifierEnum.OptimizedColorSelection_MinRegionThreshold)));
		minRegionThresholdTextField.setPreferredSize(new Dimension(30, 20));
		minRegionThresholdTextField
				.setToolTipText("Sets how big a color region have to be (min. image.width/X) and also sets image.wifth/2X as border if the background was found correctly. Valid Value-Range: 1-99999");
		minRegionThresholdTextField.setInputVerifier(new NumberInputVerifier(
				NumberInputVerifier.InputType.integer, 1, 99999));
		minRegionThresholdTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						if (minRegionThresholdTextField.getInputVerifier()
								.shouldYieldFocus(minRegionThresholdTextField)) {
							int newValue = Integer
									.valueOf(minRegionThresholdTextField
											.getText());
							OptimizedColorSelectionOptionPane.this.actionController
									.setOption(
											Options.OptionIdentifierEnum.OptimizedColorSelection_MinRegionThreshold,
											newValue);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
					}
				});

		ToolBoxOptionPane colorThresholdOption = new ToolBoxOptionPane(
				"Color Threshold", colorThresholdTextField);
		ToolBoxOptionPane minRegionThresholdOption = new ToolBoxOptionPane(
				"Minimal Region Threshold", minRegionThresholdTextField);
		ToolBoxOptionPane descriptionOption = new ToolBoxOptionPane(
				"Description",
				new JLabel(
						"<html>Optimized Version<br>of the ColorSelection<br>Algorithm (faster)</html>"));
		add(colorThresholdOption);
		add(minRegionThresholdOption);
		add(descriptionOption);
	}

}
