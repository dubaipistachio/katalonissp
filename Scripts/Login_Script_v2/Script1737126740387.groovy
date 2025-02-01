import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

//Login tanpa wait atau delay



class LoginTestWithDataFile {
	// Main test method that accepts test data
	def executeLogin(String email, String password, boolean expectedResult) {
		// Open browser and navigate to login page
		WebUI.openBrowser('')
		WebUI.navigateToUrl('https://fe.pssi-dev.kecilin.id/login')
		WebUI.maximizeWindow()
		
		// Input credentials from data file
		WebUI.setText(findTestObject('Object Repository/Page_/input_Email'), email)
		WebUI.setText(findTestObject('Object Repository/Page_/input_Password'), password)
		
		// Click login button
		WebUI.click(findTestObject('Object Repository/Page_/button_Login'))
		
		// Verify based on expected result
		if (expectedResult) {
			// Verify successful login
			WebUI.verifyElementPresent(findTestObject('Object Repository/Page_/lbl_Dashboard'), 10)
			WebUI.verifyTextPresent('DEV_FR_Access_Frontend', false)
			WebUI.takeScreenshot("successful-login-${email}.png")
		} else {
			// Verify error message for failed login
			WebUI.verifyElementPresent(findTestObject('Object Repository/Page_/lbl_ErrorMessage'), 10)
			WebUI.verifyTextPresent('Invalid email or password.', false)
			WebUI.takeScreenshot("failed-login-${email}.png")
		}
		
		WebUI.closeBrowser()
	}
}

// Main test script
def loginTest = new LoginTestWithDataFile()

// Get data from Excel file
def loginData = findTestData('Data Files/LoginTestData')

// Loop through all rows in the data file
for (int row = 1; row <= loginData.getRowNumbers(); row++) {
	// Read data from Excel
	String email = loginData.getValue('email', row)
	String password = loginData.getValue('password', row)
	boolean expectedResult = loginData.getValue('expectedResult', row).toBoolean()
	
	// Execute test with current row data
	loginTest.executeLogin(email, password, expectedResult)
}