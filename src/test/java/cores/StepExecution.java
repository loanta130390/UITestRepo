package cores;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import enums.ActionEnum;
import models.Step;

public class StepExecution extends BaseAction{

	public StepExecution(WebDriver driver) {
		super(driver);
	}

	public void executeStep(Step step, ExtentTest logger, WebDriver driver) {
		logStepDesc(step, logger);
		doAction(step, logger, driver);
	}

	private void doAction(Step step, ExtentTest test, WebDriver driver) {
		String action = step.getAction().trim();
		String locator = step.getLocator().trim();
		String selector = step.getSelector().trim();
		String param = step.getParam().trim().trim();
		ActionEnum actionEnum = ActionEnum.valueOf(action);
		WebElement element = null;
		try {
			if (!locator.isEmpty() && !action.equalsIgnoreCase("assertNull")) {
				element = findWebElement(locator, selector, driver);
			}	
		} catch (Exception e) {
			test.fail("Unable to locate element at step #" + step.getStepNo() + ". " + step.getStepDesc() + "<br/>" + e.getMessage());
		}
		
		try {
			switch (actionEnum) {
			case click:
				driver = clickElement(element, driver);
				break;
			case assertTrue:
				String actualText = element.getText();
				assertTrue(actualText.contains(param));
				break;
			case switchToNewTab:
				new WebDriverWait(driver, 20);
				ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
				driver.switchTo().window(tabs2.get(1));
				break;
			case sendKey:
				driver = sendKeys(element, param, driver);
				break;
			case assertExist:
				assertTrue(element.isDisplayed(), "Not found element at step #"+ step.getStepNo());
				break; 
			case dragAndDropBy:
				driver = dragAndDropElement(element, param, driver);
				break;
			case assertRealTimeUpdate:
				driver = assertRealTimeChange(step, driver);
				break;
			case assertEquals: 
				assertEquals(element.isSelected(), Boolean.parseBoolean(param));
				break;
			case assertNull:
				driver = assertNotExistElement(step, driver);
				break;
			default:
				
				break;
			}
		} catch (Exception e) {
			test.fail("Undefined/failed action at step #" + step.getStepNo() + ". " + step.getStepDesc() + "<br/>" + e.getMessage());
		}
	}

	private void logStepDesc(Step step, ExtentTest logger) {
		String testInfo = step.getStepNo() + ". " + step.getStepDesc() + ". ";
		String testValue = step.getParam().trim();
		String[] keyboardArray = {"return", "enter","tab","keydown", "keyup"};
		
		boolean isKeyboard = Arrays.stream(keyboardArray).anyMatch(testValue::equalsIgnoreCase);
		if (!step.getParam().isEmpty()) {
			if (step.getAction().equalsIgnoreCase("sendKey") && !isKeyboard) {
				testInfo = testInfo + "(Test value: " + step.getParam() + ")";
			}
			else if(step.getAction().contains("assert")) {
				testInfo = testInfo + "(Assert value: " + step.getParam() + ")";
			}
		}
		
		logger.log(Status.INFO, testInfo);
	}
}
