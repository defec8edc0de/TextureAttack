package de.tud.textureAttack.view.components.toolbox.attacktools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.Options.AttackToolEnum;
import de.tud.textureAttack.view.components.toolbox.ToolBoxOptionPane;

public class AttacksToolboxPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JComboBox<AttackToolEnum> attackToolComboBox;
	private ToolBoxOptionPane attackToolOptionsPane;
	private RandomAttackOptionsPane randomAttackOptionsPane;
	private JButton automateButton;
	private ActionController actionController;

	public AttacksToolboxPane(ActionController actionController) {
		// super(new GridLayout(3, 1, 10, 20));
		// super(new FlowLayout(10, 10, 20));
		super(new BorderLayout());

		this.actionController = actionController;

		randomAttackOptionsPane = new RandomAttackOptionsPane(actionController);

		attackToolComboBox = new JComboBox<AttackToolEnum>();
		attackToolComboBox.setPreferredSize(new Dimension(80, 20));
		attackToolComboBox.addItem(AttackToolEnum.Random);

		attackToolComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				switch (String.valueOf(attackToolComboBox.getSelectedItem())) {
				case "random":
					AttacksToolboxPane.this.actionController.setOption(
							Options.OptionIdentifierEnum.Attack_Algorithm,
							Options.AttackToolEnum.Random);
					removeAndAdd(randomAttackOptionsPane);
					break;
				default:
					break;
				}

			}

			private void removeAndAdd(JComponent component) {
				try {
					if (component == null) {
						AttacksToolboxPane.this.remove(1);
						AttacksToolboxPane.this.repaint();
						AttacksToolboxPane.this.revalidate();

					} else {
						AttacksToolboxPane.this.remove(1);
						add(component);
						AttacksToolboxPane.this.repaint();
						AttacksToolboxPane.this.revalidate();

					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if (component == null) {
						AttacksToolboxPane.this.repaint();
						AttacksToolboxPane.this.revalidate();

					} else {
						add(component);
						AttacksToolboxPane.this.repaint();
						AttacksToolboxPane.this.revalidate();
					}
				}

			}
		});

		automateButton = new JButton("Auto-Attack");
		automateButton
				.setToolTipText("Try's to attack all loaded Textures with the chosen Attack and chosen background selection");
		automateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AttacksToolboxPane.this.actionController.manipulateAllImages();

			}
		});

		attackToolOptionsPane = new ToolBoxOptionPane("Choose Attack",
				attackToolComboBox);

		add(attackToolOptionsPane, BorderLayout.NORTH);
		add(randomAttackOptionsPane, BorderLayout.CENTER);
		add(automateButton, BorderLayout.SOUTH);

	}

}
