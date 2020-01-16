package javau1;

public class Ukol1conf {
	private static final String LOG_PROPERTY_FILE = "C:\\dev\\git\\test\\Ukol1\\log.properties";
	private static final String PROPERTY_FILE = "C:\\dev\\git\\test\\Ukol1\\ukol1.properties";
	
	private Ukol1conf() {
	}
	
	private static final PropertyFileUtils propertyFileUtils = PropertyFileUtils.getInstance(PROPERTY_FILE);
	
	public static String getDriverClass() throws PropertyFileException{
		return propertyFileUtils.getProperty("DRIVER_CLASS");
	}
	public static String getUrl() throws PropertyFileException{
		return propertyFileUtils.getProperty("URL_PROTOCOL") + propertyFileUtils.getProperty("URL_SYSTEM");
	}
	public static String getUserName() throws PropertyFileException {
		return propertyFileUtils.getProperty("USER_NAME");
	}
	public static String getUserPassword() throws PropertyFileException {
		return propertyFileUtils.getProperty("USER_PASSWORD");
	}
	public static String getLogPropertyFile() {
		return LOG_PROPERTY_FILE;
	}
}
