package selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class BlogHostTests
{
	private WebDriver driver;
	
	@Before
	public void setUp()
	{
		String driverType = SetUp.setUpSeleniumDriverProperty();
		if(driverType.equals(SetUp.CHROME_WEBDRIVER_PROPERTY))
		{
			driver = new ChromeDriver();
		}
		else if(driverType.equals(SetUp.FIREFOX_WEBDRIVER_PROPERTY))
		{
			driver = new FirefoxDriver();
		}
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public void login(String username, String password)
	{
		driver.get(SetUp.BLOGHOST_URL + "/HomePage");
		driver.findElement(By.id("navbarDropdown")).click();
		driver.findElement(By.cssSelector("button[data-target='#loginModal']")).click();
		driver.findElement(By.name("Username")).sendKeys(username);
		driver.findElement(By.name("Password")).sendKeys(password);
		driver.findElement(By.xpath("//button[contains(text(), 'LogIn')]")).click();
	}
	
	@Test
	public void loginTest()
	{
		login("User1", "user1");
		driver.findElement(By.linkText("User1"));
		
	}
	
	@Test
	public void logoutTest() 
	{
		login("User1", "user1");
		driver.findElement(By.linkText("User1")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.linkText("Login/Register"));
	}
	
	@Test
	public void sideBarLinksTest() throws Exception
	{
		driver.get(SetUp.BLOGHOST_URL + "/HomePage");
		driver.findElement(By.xpath("//span[contains(text(), 'Home')]")).click();
		assertEquals(SetUp.BLOGHOST_URL + "/HomePage", driver.getCurrentUrl());
		login("test", "test");
		driver.findElement(By.xpath("//span[contains(text(), 'My Site')]")).click();
		Thread.sleep(3000);
		assertTrue(driver.getCurrentUrl().contains(SetUp.BLOGHOST_URL + "/Site"));
		driver.findElement(By.xpath("//span[contains(text(), 'My Store')]")).click();
		Thread.sleep(2000);
		assertTrue(driver.getCurrentUrl().contains(SetUp.BLOGHOST_URL + "/Store"));
	}
	
	@Test
	public void addAndDeletePostTest()
	{
		login("test", "test");
		driver.findElement(By.xpath("//span[contains(text(), 'My Site')]")).click();
		driver.findElement(By.linkText("Add New Post")).click();
		driver.findElement(By.name("titleInput")).sendKeys("SELENIUM TITLE");
		driver.findElement(By.name("postInput")).sendKeys("SELENIUM POST");
		driver.findElement(By.xpath("//button[contains(text(), 'Submit')]")).click();
		
		driver.findElement(By.xpath("//h2[contains(text(), 'SELENIUM TITLE')]"));
		WebElement numberOfPosts = driver.findElement(By.xpath("//h5[contains(text(), 'posts:')]"));
		int posts = Integer.valueOf(numberOfPosts.getText().substring(14));
		driver.findElement(By.xpath("//li[@class='list-group-item']/form/button")).click();
		numberOfPosts = driver.findElement(By.xpath("//h5[contains(text(), 'posts:')]"));
		int afterPosts = Integer.valueOf(numberOfPosts.getText().substring(14));
		assertTrue(posts == afterPosts + 1);
	}
	
	@Test
	public void addAndDeleteStoreItemTest()
	{
		login("test", "test");
		driver.findElement(By.xpath("//span[contains(text(), 'My Store')]")).click();
		driver.findElement(By.linkText("Add Item")).click();
		driver.findElement(By.name("itemNameInput")).sendKeys("SELENIUM NAME");
		driver.findElement(By.name("itemDescriptionInput")).sendKeys("SELENIUM DESCRIPTION");
		driver.findElement(By.name("itemAmountInput")).sendKeys("1");
		driver.findElement(By.xpath("//button[contains(text(), 'Submit')]")).click();
		
		driver.findElement(By.xpath("//h5[contains(text(), 'SELENIUM NAME')]"));
		
		driver.findElement(By.xpath("//button[contains(text(), 'Delete')]")).click();
		
		driver.findElement(By.xpath("//h1[contains(text(), 'This Store is Empty!')]"));
	}
	
	@Test
	public void subscribeTest() throws Exception
	{
		login("test", "test");
		driver.get(SetUp.BLOGHOST_URL + "/Site?site=2");
		driver.findElement(By.id("subscribeButton")).click();
		Thread.sleep(2000);
		assertEquals("Subscribed", driver.findElement(By.id("subscribeButton")).getText());
		driver.findElement(By.id("subscribeButton")).click();
		Thread.sleep(2000);
		assertEquals("Subscribe", driver.findElement(By.id("subscribeButton")).getText());
	}
	
	@After
	public void tearDown()
	{
		driver.quit();
	}
}
