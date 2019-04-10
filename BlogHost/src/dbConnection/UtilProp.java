package dbConnection;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class used to load configuration data.
 */
public class UtilProp
{
	/**
	 * The environmental variable 'BLOGHOST_CONFIG_FILE' value.
	 */
	private static String CONFIG_FILE = "/Users/pav/Desktop/config.properties";//System.getenv("BLOGHOST_CONFIG_FILE");
	
	/**
	 * The properties object to pull properties from.
	 */
	private static Properties prop = new Properties();

	/**
	 * Attempts to load configuration information from the configuration file.
	 * @throws FileNotFoundException if configuration file cannot be found
	 * @throws IOException if there was an error reading the configuration file
	 */
	public static void loadProperty() throws FileNotFoundException, IOException
	{
		prop.load(new FileInputStream(CONFIG_FILE));
	}

	/**
	 * Returns a property value from the configuration file.
	 * @param key name of the property
	 * @return property value
	 */
	public static String getProp(String key)
	{
		return prop.getProperty(key).trim();
	}
}