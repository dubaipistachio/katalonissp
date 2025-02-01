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


//invalid dengan get constraint validation HTML5
class LoginTestWithDataFile {
	def executeLogin(String email, String password, boolean expectedResult) {
		try {
			WebUI.openBrowser('')
			WebUI.navigateToUrl('https://fe.pssi-dev.kecilin.id/login')
			WebUI.maximizeWindow()
			
			WebUI.setText(findTestObject('Object Repository/Page_/input_Email'), email)
			WebUI.setText(findTestObject('Object Repository/Page_/input_Password'), password)
			WebUI.click(findTestObject('Object Repository/Page_/button_Login'))
			
			WebUI.delay(2)
			
			
			
			
			
			if (expectedResult) {
				WebUI.verifyElementPresent(findTestObject('Object Repository/Page_/lbl_Dashboard'), 10)
				WebUI.verifyTextPresent('DEV_FR_Access_Frontend', false)
			} else {
				// Verifikasi error message pertama
				boolean firstError = WebUI.verifyElementPresent(findTestObject('Object Repository/Page_/lbl_ErrorMessage'),
					5, FailureHandling.OPTIONAL)
				
				// Verifikasi error message kedua jika yang pertama tidak ditemukan
//				if (!firstError) {
//					WebUI.verifyElementPresent(findTestObject('null'), 5)
//				}
			}
			
			WebUI.takeScreenshot("login-result-${email}.png")
			
		} catch (Exception e) {
			WebUI.warning("Error during login execution: " + e.getMessage())
//			WebUI.takeScreenshot()
			WebUI.takeScreenshot("error-${email}.png")
			throw e
		} finally {
			WebUI.closeBrowser()
		}
//	}
		}}

//Fungsi untuk contraint validation. Constraint Validation adalah pesan error dari HTML5
def verifyHTML5Validation(String elementId, String expectedValidationMessage = null) {
	try {
		def validity = WebUI.executeJavaScript("return document.getElementById('${elementId}').validity;", null)
		def isValid = WebUI.executeJavaScript("return document.getElementById('${elementId}').checkValidity();", null)
		def validationMessage = WebUI.executeJavaScript("return document.getElementById('${elementId}').validationMessage;", null)

		WebUI.comment("Input Valid? " + isValid)
		WebUI.comment("Pesan Validasi: " + validationMessage)

		if (expectedValidationMessage != null) {
			WebUI.verifyEqual(validationMessage, expectedValidationMessage, "Pesan validasi tidak sesuai")
		}

		return isValid
	} catch (Exception e) {
		WebUI.warning("Error verifying HTML5 validation: " + e.getMessage())
		return null // Return null to indicate an error
	}
}


// Main script
def loginTest = new LoginTestWithDataFile()
def loginData = findTestData('Data Files/LoginTestDataV4')

for (int row = 1; row <= loginData.getRowNumbers(); row++) {
	loginTest.executeLogin(
		loginData.getValue('email', row),
		loginData.getValue('password', row),
		loginData.getValue('expectedResult', row).toBoolean()
	)
}