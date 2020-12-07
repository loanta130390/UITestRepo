package cores;

import static org.testng.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.io.Files;
import enums.LocatorEnum;
import managers.FileReaderManager;
import models.Step;

public class BaseAction {

	public WebDriver driver;
	public WebDriverWait wait;
	int waitTimeOut = FileReaderManager.getInstance().getConfigReader().getWaitElementTimeOut();

	public BaseAction(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, waitTimeOut);
	}

	public WebDriver clickElement(WebElement element, WebDriver driver2) {
		try {
			new WebDriverWait(driver, waitTimeOut).until(ExpectedConditions.elementToBeClickable(element));
			element.click();
		} catch (NoSuchElementException e) {
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", element);
		}

		return driver;
	}

	public WebDriver sendKeys(WebElement element, String param, WebDriver driver) {
		if (param.equalsIgnoreCase("enter")) {
			Actions builder = new Actions(driver);
			builder.sendKeys(Keys.ENTER).perform();
		} else if (param != null) {
			element.sendKeys(param);
		}
		return driver;
	}

	public WebDriver dragAndDropElement(WebElement element, String param, WebDriver driver) {
		String[] dragRange = param.split(Pattern.quote(","));
		int startPoint = Integer.parseInt(dragRange[0].trim());
		int endPoint = Integer.parseInt(dragRange[1].trim());
		new Actions(driver).dragAndDropBy(element, startPoint, endPoint).build().perform();
		return driver;
	}

	public WebElement findWebElement(String locator, String selector, WebDriver driver) {
		LocatorEnum locatorEnum = LocatorEnum.valueOf(locator);
		WebElement element;
		By by = null;

		switch (locatorEnum) {
		case id:
			by = By.id(selector);
			break;
		case name:
			by = By.name(selector);
			break;
		case className:
			by = By.className(selector);
			break;
		case css:
			by = By.cssSelector(selector);
			break;
		case partialLinkText:
			by = By.partialLinkText(selector);
			break;
		case tagName:
			by = By.tagName(selector);
			break;
		case linkText:
			by = By.linkText(selector);
			break;
		case xpath:
			by = By.xpath(selector);
			break;
		default:

			break;
		}
		waitPresence(by);
		element = driver.findElement(by);
		return element;
	}

	public WebDriver assertRealTimeChange(Step step, WebDriver driver) {
		String selector = step.getSelector().trim();
		String locator = step.getLocator().trim();

		WebElement originalElement = findWebElement(locator, selector, driver);
		String originalValue = originalElement.getText().trim();

		WebElement secondElement = findWebElement(locator, selector, driver);
		try {
			new WebDriverWait(driver, 20).until(ExpectedConditions.stalenessOf(secondElement));
		} catch (Exception e) {

		}
		Wait<WebDriver> newwait = new FluentWait<>(driver).ignoring(NoSuchElementException.class);
		newwait.until(ExpectedConditions.visibilityOf(findWebElement(locator, selector, driver)));
		WebElement newElement = findWebElement(locator, selector, driver);
		String newValue = newElement.getText().trim();
		if (!step.getParam().isEmpty()) {
			boolean expected = (step.getParam().equalsIgnoreCase("yes")) ? false : true;
			assertEquals(originalValue.equalsIgnoreCase(newValue), expected);
		}

		return driver;
	}

	public WebDriver assertNotExistElement(Step step, WebDriver driver) {
		String locator = step.getLocator().trim();
		String selector = step.getSelector().trim();
		WebElement element = null;
		try {
			element = findWebElement(locator, selector, driver);
		} catch (Exception e) {
			assertEquals(element == null, true);
		}

		return driver;
	}

	public void waitPresence(By by) {
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
		} catch (Exception e) {
			new WebDriverWait(driver, waitTimeOut).until(ExpectedConditions.visibilityOfElementLocated(by));
		}
	}

	public String getScreenshot(WebDriver driver) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		
		String path = System.getProperty("user.dir") + "/Screenshot/" + System.currentTimeMillis() + ".png";
		File destination = new File(path);
		destination.getParentFile().mkdirs();
		
		try {
			Files.copy(src, destination);   
		} catch (IOException e) {
			System.out.println("Capture Failed " + e.getMessage());
		}

		return path;
	}
}
