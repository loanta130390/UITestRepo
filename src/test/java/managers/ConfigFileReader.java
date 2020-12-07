package managers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import com.google.common.primitives.Ints;

public class ConfigFileReader {

	public Properties properties = new Properties();
	private final String propertyFilePath = "./Configuration.properties";

	public ConfigFileReader() {
		try {
			InputStream is = new FileInputStream(propertyFilePath);
			if (is != null)
				properties.load(is);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("File properties results null value");
		}
	}

	public String getTestURL() {
		String testURL = properties.getProperty("testURL");
		if (testURL != null)
			return testURL;
		else
			throw new RuntimeException("testURL not specified in the Configuration.properties file.");
	}

	public String getTestCasesFileDirectory() {
		String testCasesJSONFileDirectory = properties.getProperty("testCasesJSONFileDirectory");
		if (testCasesJSONFileDirectory != null)
			return testCasesJSONFileDirectory;
		else
			throw new RuntimeException(
					"testCasesJSONFileDirectory not specified in the Configuration.properties file.");
	}

	public String getChromeDriverFileName() {
		String chromeDriverFileName = properties.getProperty("chromeDriverFileName");
		if (chromeDriverFileName != null)
			return chromeDriverFileName;
		else
			throw new RuntimeException("chromeDriverFileName not specified in the Configuration.properties file.");
	}

	public String getFirefoxDriverFileName() {
		String firefoxDriverFileName = properties.getProperty("firefoxDriverFileName");
		if (firefoxDriverFileName != null)
			return firefoxDriverFileName;
		else
			throw new RuntimeException("firefoxDriverFileName not specified in the Configuration.properties file.");
	}

	public String getNodeURL() {
		String nodeURL = properties.getProperty("nodeURL");
		if (nodeURL != null)
			return nodeURL;
		else
			throw new RuntimeException("hubURL not specified in the Configuration.properties file.");
	}
	
	public String getTestBrowser() {
		String testBrowser = properties.getProperty("testBrowser");
		if (testBrowser != null)
			return testBrowser;
		else
			throw new RuntimeException("testBrowser not specified in the Configuration.properties file.");
	}
	
	public String getTestCaseFileName() {
		String testCaseFileName = properties.getProperty("testCaseFileName");
		if (testCaseFileName != null)
			return testCaseFileName;
		else
			throw new RuntimeException("testCaseFileName not specified in the Configuration.properties file.");
	}

	public int getWaitElementTimeOut() {
		String temp = properties.getProperty("waitElementTimeOut");
		int waitElementTimeOut = Optional.ofNullable(temp).map(Ints::tryParse).orElse(30);

		if (waitElementTimeOut > 0)
			return waitElementTimeOut;
		else
			throw new RuntimeException("waitElementTimeOut not specified in the Configuration.properties file.");
	}
	public String getTestEnvironment() {
		String testEnvironment = properties.getProperty("testEnvironment");
		if (testEnvironment != null)
			return testEnvironment;
		else
			throw new RuntimeException("testEnvironment not specified in the Configuration.properties file.");
	}
}
