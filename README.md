# UITestRepo
Description:
- Test has been setup to run 3 test cases which can be found in 'TestCases.json' file under project folder
- Browser support chrome and firefox (and be able to run on local or remote machine)
- Tests are in-built action and don't include PageObject model
To execute test. Please check the instruction below
NOTE:
1. Check the 'Configuration.properties' file under the project folder
- 'chromeDriverFileName' & 'firefoxDriverFileName' variable are used to passed DriverFileName into executable code
 	+ If you're using Window OS, you can use 'chromedriver.exe'
 	+ If you're using Mac OS, you can use 'chromedriver'
- 'waitElementTimeOut' variable is used to set the time that we wait while browser is loading the elements
In case, there is slow internet connection. Please kindly use a bigger value.
- 'testBrowser' variable is used to run the test on the browser of your interest. (firefox or chrome)
- 'testCaseFileName' is used in case we want to change test case file name or used a new test suite for our test 
but in this case, it's already setup so you don't have to make change on this
- 'testEnvironment' variable to support setting the test to run on local machine or remote machine
(the tests have been tested successfully on chrome browser)
2. After test has been completed running. Please find the generated report named 'UITestReport.html' under the project folder
3. Assumpt that your local machine has been setup Eclipse, Maven, TestNG that can support running this test and has downloaded the project from Git 
4. Instruction: 
	4.1 - Download web driver: The version should compatible to your local web browser
	
		Chrome: https://chromedriver.chromium.org/downloads
		
		Firefox: https://github.com/mozilla/geckodriver/releases 
		
	4.2 - Download selenium server standalone if you want to run the test on remote environment (choose version that is able to support your local machine)
	
		Visit: https://www.selenium.dev/downloads/
		
To run test on local environment: 

	Step 1: Under the project folder, find file path '/src/test/java/tests/TestExecution.java'
	
	Step 2: Right click on 'TestExecution.java' file > Select 'Run As' > TestNG Test
	
	3 test cases will be executed in a row
	
To run test on remote environment: (This setup will run tests on local machine as remote machine)

	Step 1: Open Command Line on your machine
	
	+ Mac OS: Run this script 'java -jar "/path/to/project-folder/UITest/selenium-server-standalone-x.xxx.xx.jar' -role hub -port 4445
	
	+ Window OS: Run this script 'start java -jar "/path/to/project-folder/UITest/selenium-server-standalone-x.xxx.xx.jar" -role hub -port 4445
	
	Step 2: After the first command completed running successfully, copy the url after the text 'Clients should connect to' + {url} 
	
	Step 3: Open 'Configuration.properties' file and paste it into 'nodeURL' value > Save the file
	
	Step 4: Back to the command line at step #2, copy the IP Address has been registered 
	
	Step 5: Open a new Command line and run this 
	
	+ Mac OS: Run this script
	
	java -Dwebdriver.chrome.driver="/path/to/project-folder/UITest/chromedriver" -jar "/path/to/project-foldere/UITest/selenium-server-standalone-x.xxx.xx.jar" -role webdriver -browser "browserName=chrome,version=64.0,maxInstances=3" -hub http://{registeredIP}:4445/grid/register/ -port 5580
	
	+ Window OS: Run this script 
	
	start java -Dwebdriver.chrome.driver="/path/to/project-folder/UITest/chromedriver" -jar "/path/to/project-foldere/UITest/selenium-server-standalone-x.xxx.xx.jar" -role webdriver -browser "browserName=chrome,version=64.0,maxInstances=3" -hub http://{registeredIP}:4445/grid/register/ -port 5580
	
	Step 6: Back to the project, Right click on 'TestExecution.java' file > Select 'Run As' > TestNG Test
	
