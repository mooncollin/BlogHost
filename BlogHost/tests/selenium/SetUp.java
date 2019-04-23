package selenium;

public class SetUp
{
	public static final String LINUX_CHROME_DRIVER = "WebContent/WEB-INF/seleniumDrivers/linux/chromedriver";
	public static final String MAC_CHROME_DRIVER = "WebContent/WEB-INF/seleniumDrivers/mac/chromedriver";
	public static final String WIN_CHROME_DRIVER = "WebContent/WEB-INF/seleniumDrivers/win/chromedriver.exe";
	public static final String WIN_FIREFOX_DRIVER = "WebContent/WEB-INF/seleniumDrivers/win/geckodriver.exe";
	
	public static final String CHROME_WEBDRIVER_PROPERTY = "webdriver.chrome.driver";
	public static final String FIREFOX_WEBDRIVER_PROPERTY = "webdriver.gecko.driver";
	
	public static final String BLOGHOST_URL = "http://localhost:8080/BlogHost";
	
	
	public static String getSeleniumDriverProperty()
	{
		String[] driverOptions = System.getenv("BLOGHOST_SELENIUM").split("/");
		if(driverOptions.length != 2)
		{
			return null;
		}
		String result = null;
		String os = driverOptions[0].toLowerCase();
		String browser = driverOptions[1].toLowerCase();
		if(os.contains("linux"))
		{
			if(browser.contains("chrome"))
			{
				result = LINUX_CHROME_DRIVER;
			}
		}
		else if(os.contains("mac"))
		{
			if(browser.contains("chrome"))
			{
				result = MAC_CHROME_DRIVER;
			}
		}
		else if(os.contains("win"))
		{
			if(browser.contains("chrome"))
			{
				result = WIN_CHROME_DRIVER;
			}
			else if(browser.contains("firefox"))
			{
				result = WIN_FIREFOX_DRIVER;
			}
		}
		
		return result;
	}
	
	public static String setUpSeleniumDriverProperty()
	{
		String property = getSeleniumDriverProperty();
		String result;
		if(property == null)
		{
			throw new NullPointerException("Please set the BLOGHOST_SELENIUM environment variable");
		}
		
		if(property.equals(LINUX_CHROME_DRIVER) ||
		   property.equals(MAC_CHROME_DRIVER)	||
		   property.equals(WIN_CHROME_DRIVER))
		{
			result = CHROME_WEBDRIVER_PROPERTY;
		}
		else if(property.equals(WIN_FIREFOX_DRIVER))
		{
			result = FIREFOX_WEBDRIVER_PROPERTY;
		}
		else
		{
			throw new IllegalStateException("Incorrect BLOGHOST_SELENIUM environment variable");
		}
		
		System.setProperty(result, property);
		return result;
	}
}
