package javau1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class PropertyFileUtils {
private static final Logger LOGGER = Logger.getLogger(PropertyFileUtils.class);
	
	private static PropertyFileUtils instance;
	
	private final Properties properties;
	
	private PropertyFileUtils(String propertyFile) {
		
		PropertyConfigurator.configure(Ukol1conf.getLogPropertyFile());
		LOGGER.debug("begin");
		
		properties = new Properties();
		
		try {
			FileInputStream fileInputStream = new FileInputStream(propertyFile);
			properties.load(fileInputStream);
			fileInputStream.close();
		}
		catch (IOException ex) {
			LOGGER.error(ex);
		}
	}
	
	public static PropertyFileUtils getInstance(String propertyFile) {
		if (instance == null) {
			instance = new PropertyFileUtils(propertyFile);
			LOGGER.debug("New instance");
		}
		else {
			LOGGER.debug("Old instance");
		}
		return instance;
	}
	
	public static void cancelInstance() {
		instance = null;
	}
	
	public String getProperty(String propertyName) throws PropertyFileException{
		LOGGER.debug("begin");
		
		String propertyValue = properties.getProperty(propertyName);
		
		LOGGER.debug("propertyName: " + propertyName);
		LOGGER.debug("propertyValue: " + propertyValue);
		
		if (propertyValue == null) {
			LOGGER.error("Property not found");
			PropertyFileException propertyFileException = new PropertyFileException("Property file not found");
			propertyFileException.setPropertyName(propertyName);
			propertyFileException.setPropertyValue(propertyValue);
			throw propertyFileException;
		}
		
		return propertyValue;
	}
}
