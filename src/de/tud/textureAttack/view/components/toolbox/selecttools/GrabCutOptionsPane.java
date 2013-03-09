package de.tud.textureAttack.view.components.toolbox.selecttools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.view.components.toolbox.ToolBoxOptionPane;

public class GrabCutOptionsPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private ToolBoxOptionPane forColorChooserOptionPane;
	private JButton forColorChooseButton;
	private ToolBoxOptionPane backColorChooserOptionPane;
	private JButton backColorChooseButton;
	private ActionController actionController;

	public GrabCutOptionsPane(ActionController actionController) {
		super(new GridLayout(3, 1, 0, 10));
		// super(new FlowLayout(10, 3, 20));

		this.actionController = actionController;

		forColorChooseButton = new JButton();
		forColorChooseButton.setBackground((Color) actionController
				.getOption(Options.OptionIdentifierEnum.GrabCut_forColor));
		forColorChooseButton.setPreferredSize(new Dimension(45, 45));
		forColorChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(null,
						"Choose Foreground PenColor",
						forColorChooseButton.getBackground());
				forColorChooseButton.setBackground(newColor);
				GrabCutOptionsPane.this.actionController
						.setOption(
								Options.OptionIdentifierEnum.GrabCut_forColor,
								newColor);
			}
		});
		forColorChooserOptionPane = new ToolBoxOptionPane(
				"Foreground<br>Color", forColorChooseButton);

		backColorChooseButton = new JButton();
		backColorChooseButton.setBackground((Color) actionController
				.getOption(Options.OptionIdentifierEnum.GrabCut_backColor));
		backColorChooseButton.setPreferredSize(new Dimension(45, 45));
		backColorChooseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(null,
						"Choose Background PenColor",
						backColorChooseButton.getBackground());
				backColorChooseButton.setBackground(newColor);
				GrabCutOptionsPane.this.actionController.setOption(
						Options.OptionIdentifierEnum.GrabCut_backColor,
						newColor);
			}
		});
		backColorChooserOptionPane = new ToolBoxOptionPane(
				"Background<br>Color", backColorChooseButton);

		add(forColorChooserOptionPane);
		add(backColorChooserOptionPane);
		// getVerticalScrollBar().setUnitIncrement(32);
	}

}
