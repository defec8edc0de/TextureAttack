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
			setDialogTitle("Choose Textures...");
			setMultiSelectionEnabled(true);
		} else if (type == SAVE_DIALOG) {
			setDialogType(SAVE_DIALOG);
			setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			setDialogTitle("Choose place to save the textures...");
		}

	}

}
