package de.tud.textureAttack.view.components.toolbox.attacktools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.view.components.toolbox.ToolBoxOptionPane;

public class RandomAttackOptionsPane extends JPanel {

	private static final long serialVersionUID = 1L;
	

	private ToolBoxOptionPane randomnessChooserOptionPane;
	private JButton startButton;
	private JTextField randomnessTextField;
	private ActionController actionController;
	
	public RandomAttackOptionsPane(ActionController actionController){
		super(new GridLayout(3, 1, 0, 10));
		//super(new FlowLayout(10, 3, 20));
		//super(new BorderLayout());
		this.actionController = actionController;
		
		startButton = new JButton("Start");
		startButton.setToolTipText("Starts the selected Backgroundselektion on the marked Texture and sets random pixel in the background");
		startButton.setPreferredSize(new Dimension(70,20));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RandomAttackOptionsPane.this.actionController.manipulateSelectedImage();
			}
		});
		
		randomnessTextField = new JTextField(String.valueOf((Integer)actionController.getOption(Options.OptionIdentifierEnum.RandomAttack_Randomness)));
		randomnessTextField.setPreferredSize(new Dimension(30,20));
		randomnessTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						try {
							int newValue = Integer.valueOf(randomnessTextField
									.getText());
							RandomAttackOptionsPane.this.actionController
									.setOption(
											Options.OptionIdentifierEnum.RandomAttack_Randomness,
											newValue);
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null, "The Value "
									+ randomnessTextField.getText()
									+ " is no correct Value (between 0 and N)");
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
					}
				});
		JPanel randomOptionPanel = new JPanel();
		randomOptionPanel.add(randomnessTextField,BorderLayout.NORTH);
		randomOptionPanel.add(startButton,BorderLayout.CENTER);
		randomnessChooserOptionPane = new ToolBoxOptionPane("Randomness", randomOptionPanel);
		
		add(randomnessChooserOptionPane);
	}
	
	
		
	
}
