package de.tud.textureAttack.view.components.toolbox;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.view.components.toolbox.attacktools.AttacksToolboxPane;
import de.tud.textureAttack.view.components.toolbox.selecttools.SelectionToolboxPane;

public class ToolBoxTabbedWrapper extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private SelectionToolboxPane selectionToolboxPane;
	private AttacksToolboxPane attacksToolboxPane;

	public ToolBoxTabbedWrapper(ActionController actionController) {
		super(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);

		selectionToolboxPane = new SelectionToolboxPane(actionController);
		attacksToolboxPane = new AttacksToolboxPane(actionController);
		addTab("Attack Tools",
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tud/textureAttack/resources/images/load.png")),
				attacksToolboxPane,
				"Tools to attack the selected background of an Texture");
		addTab("Selection Tools",
				new ImageIcon(getClass().getClassLoader().getResource(
						"de/tud/textureAttack/resources/images/load.png")),
				selectionToolboxPane,
				"Tools to select the background of an Texture");

	}

}
