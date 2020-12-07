package cores;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class ReportManager {
	private static ExtentReports report;
	private static ExtentTest test;
	private static ExtentHtmlReporter htmlReporter;
	private static String filePath = System.getProperty("user.dir") + "/UITestReport.html";

	public static ExtentReports getInstance() {
		if (report == null) {
			GetExtent();
		}
		return report;
	}

	public static ExtentReports GetExtent() {
		report = new ExtentReports();

		htmlReporter = new ExtentHtmlReporter(filePath);
		htmlReporter.config().setDocumentTitle("UI Test Report");
		report.attachReporter(htmlReporter);
		return report;
	}

	public static ExtentTest createTest(String name, String description) {
		test = report.createTest(name, description);
		return test;
	}

	public static ExtentTest createTest(String name) {
		test = report.createTest(name, "");
		return test;
	}
}
