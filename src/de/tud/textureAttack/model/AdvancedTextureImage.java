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

import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.activation.UnsupportedDataTypeException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import jogl.DDSImage;
import model.DDSFile;
import model.TEXFile;
import model.TextureImage;
import ddsutil.DDSUtil;
import ddsutil.MipMapsUtil;
import de.tud.textureAttack.model.algorithms.Options;
import de.tud.textureAttack.model.algorithms.attacks.AbstractAttackAlgorithm;
import de.tud.textureAttack.model.algorithms.selection.AbstractSelectionAlgorithm;
import de.tud.textureAttack.model.io.IOUtils;
import de.tud.textureAttack.model.io.InvalidTextureException;
import de.tud.textureAttack.model.utils.DXTBufferDecompressor;
import de.tud.textureAttack.model.utils.ImageProcessingToolKit;
import de.tud.textureAttack.view.components.StatusBar;

public class AdvancedTextureImage {

	public static enum EditState {
		loaded, todo, finished, noMipMap, unsupported
	}

	// path to the tmp folder for saving manipulated images
	public static final String TMP_PATH = System.getProperty("file.separator")
			+ "src" + System.getProperty("file.separator") + "de"
			+ System.getProperty("file.separator") + "tud"
			+ System.getProperty("file.separator") + "textureAttack"
			+ System.getProperty("file.separator") + "resources"
			+ System.getProperty("file.separator") + "tmp"
			+ System.getProperty("file.separator");

	private int textureType = -1;
	private String absoluteFilePath = null;
	private String tmpSavePath = null;
	private String baseDir = null;
	private TextureImage texture = null;
	private EditState state = null;
	private BufferedImage originalBufferedImage = null;
	private BufferedImage editedBufferedImage = null;

	/**
	 * Loads the Texture file if it is in a supported format
	 * 
	 * @param absoluteFilePath
	 * @param baseDir
	 * @throws InvalidTextureException
	 * @throws IOException
	 */
	public AdvancedTextureImage(String absoluteFilePath, String baseDir)
			throws InvalidTextureException, IOException {
		File file = new File(absoluteFilePath);
		this.baseDir = baseDir;
		if (DDSUtil.isReadSupported(file)) {
			if (DDSFile.isValidDDSImage(file)) {
				texture = new DDSFile(file);
				if (texture.getTextureType() == DDSFile.TextureType.CUBEMAP
						|| texture.getTextureType() == DDSFile.TextureType.VOLUME) {
					System.out
							.println("ERROR:			Textur "
									+ absoluteFilePath
									+ ": Cubemaps oder Volume-Texturen werden nicht unterstützt...");
					throw new InvalidTextureException(
							"Textur wird nicht unterstützt");
				} else {
					this.absoluteFilePath = absoluteFilePath;
					state = EditState.loaded;
					textureType = 0;
				}
			} else if (TEXFile.isValidTEXImage(file)) {
				texture = new TEXFile(absoluteFilePath);
				this.absoluteFilePath = absoluteFilePath;
				state = EditState.loaded;
				textureType = 1;
			}
		} else
			throw new InvalidTextureException(
					"Textur konnte nicht eingelesen werden!");

	}

	/**
	 * Loads the internal Reference (absolute File Path) as
	 * Image/ImageCopy/Texture in the memory
	 * 
	 * @throws UnsupportedDataTypeException
	 */
	public void loadImage() {
		if (textureType == 0)
			texture = new DDSFile(new File(absoluteFilePath));
		else if (textureType == 1)
			texture = new TEXFile(new File(absoluteFilePath));
		// readTextureImageFromFile(new File(absoluteFilePath));
		try {
			if (texture != null) {
				DDSFile ddsimagefile;
				ddsimagefile = new DDSFile(texture.getFile());
				// ddsimagefile.loadImageData();

				// Workaround: old code to load dds without mipmaps
				DDSImage ddsimage;
				try {
					ddsimage = DDSImage.read(texture.getFile());
					CompressionType compressionType = ddsutil.PixelFormats
							.getSquishCompressionFormat(ddsimage
									.getPixelFormat());
					ddsimagefile.setTopMipMap(new DXTBufferDecompressor(
							ddsimage.getMipMap(0).getData(), ddsimage
									.getWidth(), ddsimage.getHeight(),
							compressionType).getImage());

					originalBufferedImage = ddsimagefile.getData();
					if (tmpSavePath == null)
						editedBufferedImage = ImageProcessingToolKit
								.deepCopy(originalBufferedImage);
					else
						editedBufferedImage = ImageProcessingToolKit
								.readImage(tmpSavePath);

					texture = ddsimagefile;

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EditState getState() {
		return state;
	}

	public void setState(EditState state) {
		this.state = state;
	}

	public BufferedImage getOriginalBufferedImage() {
		return originalBufferedImage;
	}

	public void setOriginalBufferedImage(BufferedImage originalBufferedImage) {
		this.originalBufferedImage = originalBufferedImage;
		state = EditState.loaded;
		texture.setData(originalBufferedImage);
	}

	public BufferedImage getEditedBufferedImage() {
		return editedBufferedImage;
	}

	public void setEditedBufferedImage(BufferedImage editedBufferedImage) {
		this.editedBufferedImage = editedBufferedImage;
	}

	/**
	 * Executes the given Background Select and Attack algorithm on the
	 * editedImage Version and saves a tmp copy of the manipulated image as
	 * uncompressed png in the tmp folder
	 * 
	 * @param attack
	 */
	public void processManipulation(AbstractAttackAlgorithm attackAlgo,
			AbstractSelectionAlgorithm selectAlgo, Options options,
			StatusBar statusBar) {

		// texture already loaded?
		if (editedBufferedImage == null)
			loadImage();
		// get Background Pixels of image with selectAlgo
		selectAlgo.init(editedBufferedImage, options,
				IOUtils.getFileNameFromPath(absoluteFilePath));
		Object backgroundRaster = null;

		backgroundRaster = statusBar.startTask(
				IOUtils.getFileNameFromPath(absoluteFilePath)
						+ " background selection", selectAlgo);

		// if selectalgo wasn't successful, mark image as todo
		if (backgroundRaster == null) {
			state = EditState.todo;
		} else {
			// attack the image with given attaclAlgo
			attackAlgo.init(editedBufferedImage,
					(boolean[][]) backgroundRaster, options);
			Object o = statusBar.startTask(
					IOUtils.getFileNameFromPath(absoluteFilePath)
							+ " background manipulation", attackAlgo);
			if (o instanceof BufferedImage) {
				editedBufferedImage = (BufferedImage) o;

				// save the resulting image tmp, before release memory with
				// resetImage() and set state to finished
				File absolutPath = new File("");
				String fileName = IOUtils.getFileNameFromPath(absoluteFilePath)
						.substring(
								0,
								IOUtils.getFileNameFromPath(absoluteFilePath)
										.length() - 4)
						+ ".png";
				saveImageFile(editedBufferedImage,
						absolutPath.getAbsolutePath() + TMP_PATH, fileName,
						"png");
				state = EditState.finished;
			}
		}

		resetImage();

	}

	/**
	 * Saves the given BufferedImage to the specified path with fileName and
	 * given imageTyp (imageTyp can be "png", "jpg", ...)
	 * 
	 * @param img
	 * @param path
	 * @param fileName
	 *            (has to include .fileType !!!)
	 * @param imageType
	 */
	private void saveImageFile(BufferedImage img, String path, String fileName,
			String imageType) {
		try {
			tmpSavePath = path + fileName;
			File outputfile = new File(path + fileName);
			ImageIO.write(img, imageType, outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a thumbnail (smaller) version of the editedImage (loads it
	 * before, if not loaded already) And places a state icon on the thumbnail
	 * for the state of the image (todo, finished)
	 * 
	 * @return BufferedImage
	 */
	public BufferedImage getThumbnail() {
		BufferedImage icon = null;
		loadImage();
		if (editedBufferedImage != null) {
			switch (state) {
			case todo:
				icon = ImageProcessingToolKit.addIconToImage(
						ImageProcessingToolKit
								.createScaledImage(editedBufferedImage),
						"de/tud/textureAttack/resources/images/todo.png");
				break;
			case finished:
				icon = ImageProcessingToolKit.addIconToImage(
						ImageProcessingToolKit
								.createScaledImage(editedBufferedImage),
						"de/tud/textureAttack/resources/images/finished.png");
				break;
			default:
				icon = ImageProcessingToolKit
						.createScaledImage(editedBufferedImage);
				break;
			}

		}
		resetImage();
		return icon;
	}

	/**
	 * Reads the Texture, originalImage and editedImage from the DDS-FilePath
	 * 
	 * @param file
	 * @throws OutOfMemoryError
	 */
	public void readTextureImageFromFile(final File file)
			throws OutOfMemoryError {

		long mem0;
		try {
			// actionController.setStatus("LOAD:			Lade Textur "+textureFile.getAbsolutePath());
			System.out.println("LOAD:			Lade Textur " + file.getAbsolutePath());
			if (texture != null)
				texture.loadImageData();
			else {
				// actionController.setStatus("ERROR:			Textur "+file.getAbsolutePath()+" konnte nicht eingelesen werden...");
				System.out.println("ERROR:			Textur " + file.getAbsolutePath()
						+ " konnte nicht eingelesen werden...");
				return;
			}

			mem0 = Runtime.getRuntime().totalMemory()
					- Runtime.getRuntime().freeMemory();

		} catch (UnsupportedDataTypeException e) {
			// actionController.setStatus("ERROR:			"+e.getMessage());
			state = EditState.unsupported;
			System.out.println("ERROR:			" + e.getMessage());
			return;
		} catch (final OutOfMemoryError ex) {
			mem0 = Runtime.getRuntime().totalMemory()
					- Runtime.getRuntime().freeMemory();
			throw new OutOfMemoryError(
					"Kein Speicher (mem0="
							+ mem0
							+ ") mehr frei, versuchen Sie das Programm mit mehr Speicher zu starten z.B. java -Xms4096k -jar textureAttack.jar\n"
							+ ex.getMessage());
		}

	}

	/**
	 * Saves the Texture (DDS) with the editedImage as ImageData at the given
	 * Path the ImageData must have powerOfTwo size, the texture File will be
	 * written to the specified Path with the old folder structure
	 * 
	 * @param path
	 */
	public void writeTextureToPath(final String path) {

		if (editedBufferedImage == null)
			loadImage();
		BufferedImage imageToConvert = editedBufferedImage;
		try {
			if (imageToConvert != null) {

				// check image dimensions

				if (!MipMapsUtil.isPowerOfTwo(imageToConvert.getWidth())
						&& !MipMapsUtil
								.isPowerOfTwo(imageToConvert.getHeight())
						&& texture.hasMipMaps()) {
					System.out
							.println("ERROR:			Bildgröße ist keine zweierPotenz");
					// actionController
					// .setStatus("ERROR:			Bildgröße ist keiner Zweierpotenz! Speichern fehlgeschlagen...");
					return;
				}

				// gets old folder structure and creates this structure in the
				// given path before saving
				String oldFilePath = texture.getAbsolutePath().replace(baseDir,
						"");// IOUtils.getFileNameFromPath(texture.getAbsolutePath());
				String path_structure = oldFilePath.substring(0, oldFilePath
						.lastIndexOf(System.getProperty("file.separator")));
				File path_struc = new File(path + path_structure);
				path_struc.mkdirs();

				File newFile = new File(path + oldFilePath);
				if (newFile.exists()) {
					if (JOptionPane
							.showConfirmDialog(
									null,
									"Die Datei "
											+ newFile.getAbsolutePath()
											+ " existiert bereits, wollen Sie sie überschreiben?") == JOptionPane.OK_OPTION) {
						System.out.println("OVERRIDE:			Überschreibe " + path
								+ "\\" + oldFilePath + "...");
						// actionController.setStatus("OVERRIDE:			Überschreibe "
						// + path + "\\" + fileName + "...");
						DDSUtil.write(newFile, imageToConvert,
								texture.getPixelformat(), texture.hasMipMaps());
						return;
					} else
						return;
				}

				System.out.println("SAVE:			Speichere " + oldFilePath
						+ " nach " + path + "...");
				// actionController.setStatus("SAVE:			Speichere " +
				// fileName
				// + " nach " + path + "...");
				DDSUtil.write(newFile, imageToConvert,
						texture.getPixelformat(), texture.hasMipMaps());

			}

		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final OutOfMemoryError e) {
			e.printStackTrace();
			final long mem0 = Runtime.getRuntime().totalMemory()
					- Runtime.getRuntime().freeMemory();
			// actionController
			// .setStatus("ERROR:			Kein Speicher ("+mem0+") mehr frei, versuchen Sie das Programm mit mehr Speicher zu starten z.B. java -Xms4096k -jar textureAttack.jar");
			System.out
					.println("ERROR:			Kein Speicher ("
							+ mem0
							+ ") mehr frei, versuchen Sie das Programm mit mehr Speicher zu starten z.B. java -Xms4096k -jar textureAttack.jar");
		}

		resetImage();

	}

	public String getAbsolutePath() {
		return absoluteFilePath;
	}

	/**
	 * Resets all memory consuming image data (Flushs BufferedImages and sets
	 * everything null)
	 */
	public void resetImage() {
		if (originalBufferedImage != null) {
			originalBufferedImage.flush();
			editedBufferedImage.flush();
		}
		originalBufferedImage = null;
		editedBufferedImage = null;
		texture = null;
	}

	/**
	 * Returns the path of the temp. saved (manipulated) editedImage
	 * 
	 * @return
	 */
	public String getTmpSavePath() {
		return tmpSavePath;
	}

}
