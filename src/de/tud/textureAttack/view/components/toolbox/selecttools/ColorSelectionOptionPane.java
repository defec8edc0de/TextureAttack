package de.tud.textureAttack.view.components.toolbox.selecttools;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.view.components.toolbox.ToolBoxOptionPane;

public class ColorSelectionOptionPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField colorThresholdTextField;
	private JTextField minRegionThresholdTextField;
	private ActionController actionController;

	public ColorSelectionOptionPane(ActionController actionController) {
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
						.getOption(Options.OptionIdentifierEnum.ColorSelection_ColorThreshold));
		colorThresholdTextField.setText(colorThreshold);

		colorThresholdTextField.setMinimumSize(new Dimension(30, 20));
		colorThresholdTextField.setMaximumSize(new Dimension(30, 20));
		colorThresholdTextField.setSize(new Dimension(30, 20));
		colorThresholdTextField.setPreferredSize(new Dimension(30, 20));

		colorThresholdTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						try {
							int newValue = Integer
									.valueOf(colorThresholdTextField.getText());
							ColorSelectionOptionPane.this.actionController
									.setOption(
											Options.OptionIdentifierEnum.ColorSelection_ColorThreshold,
											newValue);
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null, "The Value "
									+ colorThresholdTextField.getText()
									+ " is no correct Value (between 0 and N)");
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
					}
				});

		minRegionThresholdTextField = new JTextField();
		minRegionThresholdTextField
				.setText(String.valueOf((Integer) actionController
						.getOption(Options.OptionIdentifierEnum.ColorSelection_MinRegionThreshold)));
		minRegionThresholdTextField.setPreferredSize(new Dimension(30, 20));
		minRegionThresholdTextField.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						try {
							int newValue = Integer
									.valueOf(minRegionThresholdTextField
											.getText());
							ColorSelectionOptionPane.this.actionController
									.setOption(
											Options.OptionIdentifierEnum.ColorSelection_MinRegionThreshold,
											newValue);
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null, "The Value "
									+ minRegionThresholdTextField.getText()
									+ " is no correct Value (between 0 and N)");
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
						"<html>Searches in the picture for<br>coherent color regions, counts<br>the pixel in the regions<br>and marks the regions as<br>background, which have the<br>biggest pixelcount.<br>Min-Region-Threshold:<br>Controls how<br>small (pixelcount) an region<br> minimal have to be.<br>max. Pixelcount = <br>Image.width/Threshold.<br>Color-Threshold:<br>Controls the similarity of<br>colors in regions<br>which belongs together</html>"));
		add(colorThresholdOption);
		add(minRegionThresholdOption);
		add(descriptionOption);
	}

}
