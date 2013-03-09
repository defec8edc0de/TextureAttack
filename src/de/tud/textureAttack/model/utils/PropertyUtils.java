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
			throw new PropertiesException("Property Datei in "+PROPERTIES_LOCATION+" nicht gefunden!");
		}
		try {
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
			properties = null;
			throw new PropertiesException("IOException beim Laden der Properties aufgetreten...");
		}
	}
	
}
