package de.tud.textureAttack.view.dialogs;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.tud.textureAttack.model.utils.PropertyUtils;

public class TexturesDialog extends JFileChooser {

	private static final long serialVersionUID = 1L;
	public final static int OPEN_DIALOG = 0;
	public final static int SAVE_DIALOG = 1;

	public TexturesDialog(int type) {
		super();
		setCurrentDirectory(new File(
				PropertyUtils.properties.getProperty("lastDir")));
		if (type == OPEN_DIALOG) {
			setDialogType(OPEN_DIALOG);
			setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			setFileFilter(new FileNameExtensionFilter(
					"DirectDrawSurface texture", "dds"));
			setDialogTitle("Texturen wählen...");
			setMultiSelectionEnabled(true);
		} else if (type == SAVE_DIALOG) {
			setDialogType(SAVE_DIALOG);
			setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			setDialogTitle("Ort zum speichern der Texturen wählen...");
		}

	}

}
