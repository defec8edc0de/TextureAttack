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
package de.tud.textureAttack.model.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {

	public static final String PROPERTIES_LOCATION = "src/de/tud/textureAttack/resources/properties.properties";
	public static Properties properties;

	public static void loadProperties() throws PropertiesException {
		properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream(
					PROPERTIES_LOCATION));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			properties = null;
			throw new PropertiesException("Property Datei in "
					+ PROPERTIES_LOCATION + " nicht gefunden!");
		}
		try {
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			properties = null;
			throw new PropertiesException(
					"IOException beim Laden der Properties aufgetreten...");
		}
	}

}
