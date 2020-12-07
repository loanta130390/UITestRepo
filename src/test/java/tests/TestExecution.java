package tests;

import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

import cores.BrowserFactory;
import cores.ReportManager;
import cores.StepExecution;
import managers.FileReaderManager;
import models.TestCase;
import utils.JsonFileHandle;
import org.testng.annotations.DataProvider;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

public class TestExecution {
	WebDriver driver;
	static String testCaseFilePath = System.getProperty("user.dir") + "/" + FileReaderManager.getInstance().getConfigReader().getTestCaseFileName();
	
	Logger logger = Logger.getLogger(TestExecution.class);
	BrowserFactory browserFactory = new BrowserFactory();
	String URL = FileReaderManager.getInstance().getConfigReader().getTestURL();

	static ExtentTest extentTest;
	static ExtentReports report;
	StepExecution stepExecution;

	@BeforeClass
	public static void setup() {
		report = ReportManager.getInstance();
	}

	@DataProvider()
	public Iterator<TestCase> dp() {
		JsonFileHandle jsonHandler = new JsonFileHandle();
		List<TestCase> listTestCases = new ArrayList<>();
		try {
			listTestCases = jsonHandler.parseJSONToListObject(testCaseFilePath, TestCase[].class);
		} catch (IOException e) {
			logger.info("Cannot find TestCases.json file");
			e.printStackTrace();
		}

		return listTestCases.iterator();
	}


	@Test(dataProvider = "dp")
	public void run(TestCase test) throws MalformedURLException {
		String browserName = FileReaderManager.getInstance().getConfigReader().getTestBrowser();
		driver = browserFactory.initDriver(browserName);
		stepExecution = new StepExecution(driver);
		
		// Tracking test report for current test case
		extentTest = report.createTest("Test case #" + test.getTestCaseId(), "<h3>" + test.getTestCaseDesc() + "<h3/>");

		// Execute steps
		test.getSteps().stream().forEachOrdered(step -> stepExecution.executeStep(step, extentTest, driver));
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown(ITestResult result) throws IOException {
		if (driver == null) {
			extentTest.fail(result.getThrowable().getMessage());
		}
		else if (result.getStatus() == ITestResult.FAILURE) {
			
			String temp = stepExecution.getScreenshot(driver);
			extentTest.fail(result.getThrowable().getMessage(),
					MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
		}

		report.flush();
		driver.quit();
	}


}
