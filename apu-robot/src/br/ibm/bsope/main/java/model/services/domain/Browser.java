package br.ibm.bsope.main.java.model.services.domain;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;



public class Browser {

	private static Browser browser;
	private WebDriver driver;
	private long defaultTimeout;
	
	public enum BrowserType { IE, FIREFOX }
	
	/**
	 * Avoid constructing without calling getInstance
	 */
	private Browser(){
	}
	
	/**
	 * Private constructor used by public getInstance methods
	 * @param type
	 * @param timeout
	 */
	private Browser(BrowserType type, long timeout){
		switch(type){
			case IE: 
				driver = new InternetExplorerDriver();
				break;
			case FIREFOX:
				driver = new FirefoxDriver();
				break;
		}
		defaultTimeout = timeout;
		driver.manage().window().maximize();
	}

	/**
	 * Default singleton assigning BrowserType Firefox and default timeout 60 seconds
	 * @return
	 */
	public static Browser getInstance(){
		return getInstance((BrowserType) null, 0);
	}
	
	/**
	 * Singleton passing default timeout as argument and assuming BrowserType Firefox as default
	 * @param defaultTimeout
	 * @return
	 */
	public static Browser getInstance(long defaultTimeout){
		return getInstance((BrowserType) null, defaultTimeout);
	}
	
	/**
	 * Singleton passing BrowserType and assuming 60 seconds as default timeout
	 * @param type
	 * @return
	 */
	public static Browser getInstance(BrowserType type){
		return getInstance(type, 0);
	}
	
	/**
	 * Singleton fully customized.
	 * @param type
	 * @param defaultTimeout
	 * @return
	 */
	public static Browser getInstance(BrowserType type, long defaultTimeout){
		if(type == null) type = BrowserType.FIREFOX;
		if(defaultTimeout == 0) defaultTimeout = 60;
		if(browser == null) browser = new Browser(type, defaultTimeout);
		return browser;
	}
	
	public void close(){
		this.driver.close();
		browser = null;
	}
	
	public void get(String url){
		this.driver.get(url);
	}
	
	public WebElement findElement(By locator){
		waitUntilElementIsPresent(locator);
		return this.driver.findElement(locator);
	}
	
	public Select findSelect(By locator){
		waitUntilElementIsPresent(locator);
		return new Select(this.driver.findElement(locator));
	}
	
	public boolean isElementPresent (By locator) {
	    return this.driver.findElements(locator).size() > 0;
	}
	
	public boolean isElementClickable(By locator){
		return ExpectedConditions.elementToBeClickable(locator) != null;
	}
	
	public void waitUntilElementIsPresent(By locator){
		final By elementLocator = locator;
		new WebDriverWait(this.driver,this.defaultTimeout).until(
				new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.findElements(elementLocator).size() > 0; 
					}
				}
			);
	}
	
	public void waitUntilElementIsClickable(By locator){
		final By elementLocator = locator;
		new WebDriverWait(this.driver, this.defaultTimeout).until(
				ExpectedConditions.elementToBeClickable(elementLocator)
		);
	}
	
	public void waitUntilPageIsLoaded(){
		new WebDriverWait(this.driver,this.defaultTimeout).until(
			new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"); 
				}
			}
		);
	}
	
	public void switchToWindow(Set<String> previousHandles){
		final Set<String> handles = previousHandles;
		String newHandle = new WebDriverWait(this.driver, this.defaultTimeout).until(
			new ExpectedCondition<String>() {
				public String apply(WebDriver d){
					Set<String> currentHandles = d.getWindowHandles();
					currentHandles.removeAll(handles);
					if(currentHandles.size()>0)
						return currentHandles.iterator().next();
					return null;
				}
			}
		);
		this.driver.switchTo().window(newHandle);
	}
	
	public void switchToWindow(String windowHandleOrName){
		final String title = windowHandleOrName;
		new WebDriverWait(this.driver, this.defaultTimeout).until(
			new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d){
					try{
						d.switchTo().window(title);
						return true;
					} catch(NoSuchWindowException e){
						return false;
					}
				}
			}
		);
		this.driver.switchTo().window(title);
	}
	
	public void waitInSeconds(long seconds){
		new WebDriverWait(this.driver,seconds);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

}
