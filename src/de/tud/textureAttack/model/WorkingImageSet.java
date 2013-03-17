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
package de.tud.textureAttack.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.tud.textureAttack.controller.ActionController;
import de.tud.textureAttack.model.AdvancedTextureImage.EditState;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.attacks.AbstractAttackAlgorithm;
import de.tud.textureAttack.model.algorithms.selection.AbstractSelectionAlgorithm;
import de.tud.textureAttack.model.io.InvalidTextureException;
import de.tud.textureAttack.view.components.StatusBar;

public class WorkingImageSet {

	private ArrayList<AdvancedTextureImage> imageList;
	private ActionController actionController;

	public WorkingImageSet(ActionController actionController) {
		imageList = new ArrayList<AdvancedTextureImage>();
		this.actionController = actionController;
	}

	public ArrayList<BufferedImage> getOriginialImages() {
		ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
		for (AdvancedTextureImage img : imageList) {
			list.add(img.getOriginalBufferedImage());
		}
		return list;
	}

	public void clearImages() {
		imageList.clear();
	}

	public void addAdvancedImageTexture(AdvancedTextureImage img) {
		imageList.add(img);
	}

	public void removeAdvancedTextureImage(int i) {
		imageList.remove(i);
	}

	/**
	 * Gets the absoluteFilePaths of loaded images, creates from them the
	 * AdvancedTextureImages and stores it in the working set
	 * 
	 * @param files
	 * @param baseDir
	 *            Base Directory of the files, for saving later with dir
	 *            structure
	 */
	public void setTextures(File[] files, String baseDir) {
		if (files != null) {
			imageList.clear();
			actionController.setProgressCount(files.length);
			actionController.setStatus("Loading Textures.......");
			for (int i = 0; i < files.length; i++) {
				try {
					actionController.setProgress(i);
					AdvancedTextureImage img = new AdvancedTextureImage(
							files[i].getAbsolutePath(), baseDir);
					imageList.add(img);
				} catch (InvalidTextureException | IOException e) {
					System.out.println("ERROR:        " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}

	public ArrayList<BufferedImage> getEditedImages() {
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		for (AdvancedTextureImage img : imageList)
			images.add(img.getEditedBufferedImage());
		return images;
	}

	public BufferedImage getEditedImage(int index) {
		return imageList.get(index).getEditedBufferedImage();
	}

	/**
	 * Returns the original Image of the specified index (loads the image,
	 * before) if it exists, else null
	 * 
	 * @param selectedImagePosition
	 * @return BufferedImage
	 */
	public BufferedImage getOriginialImage(int selectedImagePosition) {
		if (imageList.isEmpty() || (selectedImagePosition > imageList.size()))
			return null;
		else {
			if (imageList.get(selectedImagePosition).getEditedBufferedImage() == null)
				imageList.get(selectedImagePosition).loadImage();
			return imageList.get(selectedImagePosition)
					.getOriginalBufferedImage();
		}
	}

	/**
	 * Returns the AdvancedTextureImage not loaded with the given Index i if it
	 * is in the set, else null
	 * 
	 * @param i
	 * @return AdvancedTextureImage
	 */
	public AdvancedTextureImage getAdvancedTextureImage(int i) {
		if (imageList.isEmpty() || (i > imageList.size()))
			return null;
		else {
			return imageList.get(i);
		}
	}

	/**
	 * Returns the AdvancedTextureImage loaded (load image data before, if not
	 * loaded) with the given Index i if it is in the set, else null
	 * 
	 * @param i
	 * @return AdvancedTextureImage
	 */
	public AdvancedTextureImage getLoadedAdvancedTextureImage(int i) {
		if (imageList.isEmpty() || (i > imageList.size()))
			return null;
		else {
			if (imageList.get(i).getEditedBufferedImage() == null) {
				imageList.get(i).loadImage();
			}
			return imageList.get(i);
		}
	}

	public ArrayList<AdvancedTextureImage> getAdvancedTextureImages() {
		return imageList;
	}

	/**
	 * Saves all Textures of the set with their folder structure to the
	 * specified absolutePath and deletes their tmp image files (created during
	 * manipulation)
	 * 
	 * @param absolutePath
	 */
	public void saveTextures(String absolutePath) {
		actionController.setProgressCount(imageList.size());
		int i = 0;
		for (AdvancedTextureImage img : imageList) {
			if (img.getState() != EditState.unsupported){
			actionController.setProgress(i);
			i++;
			img.writeTextureToPath(absolutePath);
			if (img.getTmpSavePath() != null) {
				// tmpSavePath: delete tmp image
				File file = new File(img.getTmpSavePath());
				if (file.exists())
					file.delete();
				img.setTmpSavePath(null);
			}
		}
		}
	}

	/**
	 * Returns the advances texture image, which matches the given
	 * absoluteFilePath (the path from the source texture), or null if there is
	 * no such a advanced texture image
	 * 
	 * @param path
	 * @return AdvancedTextureImage
	 */
	public AdvancedTextureImage getAdvancedTextureImageFromAbsoluteFilePath(
			String path) {
		for (int i = 0; i < imageList.size(); i++) {
			if (imageList.get(i).getAbsolutePath().equals(path))
				return imageList.get(i);
		}
		return null;
	}

	/**
	 * Manipulated all images in the set, with the given background selection
	 * algorithm, pixel manipulation algorithm with the given options
	 * 
	 * @param attackAlgorithm
	 * @param selectAlgorithm
	 * @param options
	 */
	public void manipulateAllImages(AbstractAttackAlgorithm attackAlgorithm,
			AbstractSelectionAlgorithm selectAlgorithm, Options options,
			StatusBar statusBar) {
		int i = 0;
		actionController.getStatusBar().setImageProgressParameter(
				imageList.size());
		for (AdvancedTextureImage advImage : imageList) {
			if (advImage.getState() != EditState.unsupported){
			advImage.processManipulation(attackAlgorithm, selectAlgorithm,
					options, statusBar);
			actionController.getStatusBar().setImageProgress(i);
			i++;
			actionController.setTextureIcon(advImage);
			actionController.getStatusBar().resetAlgorithms();
			// reset the algorithms, because they are swingworker and need to be
			// reseted
			attackAlgorithm = (AbstractAttackAlgorithm) actionController
					.getAlgorithm(attackAlgorithm.getName());
			selectAlgorithm = (AbstractSelectionAlgorithm) actionController
					.getAlgorithm(selectAlgorithm.getName());
			if (actionController.getStatusBar().getCanceledAutoAttack())
				break;
			}
		}

		actionController.getStatusBar().doneAll();

	}

	public void saveTexture(String absolutePath, String selectedImageByPath) {
		AdvancedTextureImage img = getAdvancedTextureImageFromAbsoluteFilePath(selectedImageByPath);
		if (img.getState() != EditState.unsupported){
		img.writeTextureToPath(absolutePath);
		if (img.getTmpSavePath() != null) {
			// tmpSavePath: delete tmp image
			File file = new File(img.getTmpSavePath());
			if (file.exists())
				file.delete();
			img.setTmpSavePath(null);
		}
		}
	}

	public ArrayList<String> getUnsupportedTexturePaths() {
		ArrayList<String> result = new ArrayList<String>();
		for (AdvancedTextureImage img : imageList){
			if (img.getState() == EditState.unsupported)
				result.add(img.getAbsolutePath());
		}
		return result;
	}

	public int getTextureCount() {
		return imageList.size();
	}

}
