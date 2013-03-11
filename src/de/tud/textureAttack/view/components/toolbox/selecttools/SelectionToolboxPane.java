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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.Options.SelectionToolEnum;
import de.tud.textureAttack.view.components.toolbox.ToolBoxOptionPane;

public class SelectionToolboxPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox<SelectionToolEnum> selectionToolComboBox;
	private ToolBoxOptionPane selectionToolOptionsPane;
	private GrabCutOptionsPane grabCutOptionsPane;
	private ColorSelectionOptionPane colorSelectionOptionPane;
	private ActionController actionController;

	public SelectionToolboxPane(ActionController actionController) {
		// super(new GridLayout(2, 1, 10, 20));
		// super(new FlowLayout(FlowLayout.LEFT, 10, 20));
		super(new BorderLayout());

		this.actionController = actionController;

		grabCutOptionsPane = new GrabCutOptionsPane(actionController);
		colorSelectionOptionPane = new ColorSelectionOptionPane(
				actionController);

		selectionToolComboBox = new JComboBox<SelectionToolEnum>();
		selectionToolComboBox.addItem(SelectionToolEnum.ColorSelection);
		selectionToolComboBox.addItem(SelectionToolEnum.GrabCut);

		selectionToolComboBox.setMinimumSize(new Dimension(80, 20));
		selectionToolComboBox.setMaximumSize(new Dimension(80, 20));
		selectionToolComboBox.setPreferredSize(new Dimension(80, 20));

		selectionToolComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				switch ((SelectionToolEnum) selectionToolComboBox
						.getSelectedItem()) {
				case GrabCut:
					SelectionToolboxPane.this.actionController.setOption(
							Options.OptionIdentifierEnum.Selection_Algorithm,
							Options.SelectionToolEnum.GrabCut);
					removeAndAdd(grabCutOptionsPane);
					break;
				case ColorSelection:
					SelectionToolboxPane.this.actionController.setOption(
							Options.OptionIdentifierEnum.Selection_Algorithm,
							Options.SelectionToolEnum.ColorSelection);
					removeAndAdd(colorSelectionOptionPane);
					break;
				default:
					break;
				}

			}

			/**
			 * Replaces the options panel for the given selection tool with the
			 * old
			 * 
			 * @param component
			 */
			private void removeAndAdd(JComponent component) {
				try {
					if (component == null) {
						SelectionToolboxPane.this.remove(1);
						SelectionToolboxPane.this.repaint();
						SelectionToolboxPane.this.revalidate();

					} else {
						SelectionToolboxPane.this.remove(1);
						add(component);
						SelectionToolboxPane.this.repaint();
						SelectionToolboxPane.this.revalidate();

					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if (component == null) {
						SelectionToolboxPane.this.repaint();
						SelectionToolboxPane.this.revalidate();

					} else {
						add(component);
						SelectionToolboxPane.this.repaint();
						SelectionToolboxPane.this.revalidate();
					}
				}

			}
		});
		selectionToolOptionsPane = new ToolBoxOptionPane(
				"Background-Selection-Tools", selectionToolComboBox);

		add(selectionToolOptionsPane, BorderLayout.NORTH);
		add(colorSelectionOptionPane, BorderLayout.CENTER);
	}

}
