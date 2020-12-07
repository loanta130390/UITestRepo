package cores;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import enums.TestBrowser;
import managers.FileReaderManager;

public class BrowserFactory {

	public BrowserFactory() {

	}

	public WebDriver driver;
	public static String currentDir = System.getProperty("user.dir") + "/";
	public static String chromeDriverFilePath = currentDir
			+ FileReaderManager.getInstance().getConfigReader().getChromeDriverFileName();
	public static String firefoxDriverFilePath = currentDir
			+ FileReaderManager.getInstance().getConfigReader().getFirefoxDriverFileName();

	public static final String testEnvironment = FileReaderManager.getInstance().getConfigReader().getTestEnvironment();
	public static final String nodeURL = FileReaderManager.getInstance().getConfigReader().getNodeURL();
	public static final String testURL = FileReaderManager.getInstance().getConfigReader().getTestURL();

	public WebDriver initDriver(String browserName) throws MalformedURLException {
		browserName = browserName.toLowerCase();
		TestBrowser testBrowser = TestBrowser.valueOf(browserName);

		switch (testBrowser) {
		case firefox:
			driver = getFirefoxDriver();
			break;

		default:
			driver = getChromeDriver();
			break;
		}
		driver.get(testURL);
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(45, TimeUnit.SECONDS);
		return driver;
	}

	public WebDriver getFirefoxDriver() {
		FirefoxProfile geoDisabled = new FirefoxProfile();
		geoDisabled.setPreference("geo.enabled", false);
		geoDisabled.setPreference("geo.provider.use_corelocation", false);
		geoDisabled.setPreference("geo.prompt.testing", false);
		geoDisabled.setPreference("geo.prompt.testing.allow", false);
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(geoDisabled);
		options.addPreference("dom.webnotifications.enabled", false);
		System.setProperty("webdriver.gecko.driver", firefoxDriverFilePath);
		if (testEnvironment.equalsIgnoreCase("remote")) {
			DesiredCapabilities capability = DesiredCapabilities.firefox();
			capability.merge(options);
			try {
				this.driver = new RemoteWebDriver(new URL(nodeURL), capability);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}else {
			this.driver = new FirefoxDriver(options);
		} 
		return driver;
	}

	public WebDriver getChromeDriver() throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		options.addArguments(Arrays.asList("--no-sandbox", "--ignore-certificate-errors", "--homepage=about:blank",
				"--no-first-run"));
		options.setExperimentalOption("useAutomationExtension", false);
		options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
		options.addArguments("start-maximized");
		options.addArguments("test-type");
		options.addArguments("enable-strict-powerful-feature-restrictions");
		options.addArguments("disable-geolocation");

		System.setProperty("webdriver.chrome.driver", chromeDriverFilePath);
		if (testEnvironment.equalsIgnoreCase("remote")) {
			DesiredCapabilities capability = DesiredCapabilities.chrome();
			capability.setCapability(ChromeOptions.CAPABILITY, options);
			try {
				driver = new RemoteWebDriver(new URL(nodeURL), capability);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			this.driver = new ChromeDriver(options);
		}

		return driver;
	}

}
