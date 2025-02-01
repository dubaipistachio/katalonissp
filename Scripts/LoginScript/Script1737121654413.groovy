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
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testdata.reader.CsvDataFileReader

// Definisi Test Data (Sesuaikan dengan path file Anda)
def testData = new CsvDataFileReader('Data Files/LoginTestData')

// Fungsi untuk login
@Keyword
def login(String email, String password) {
	WebUI.setText(findTestObject('Object Repository/Page_/input_Email'), email)
	WebUI.setEncryptedText(findTestObject('Object Repository/Page_/input_Password'), password)
	WebUI.click(findTestObject('Object Repository/Page_/btn_Login'))
}

// Fungsi untuk memverifikasi pesan error
@Keyword
def verifyErrorMessage(String expectedErrorMessage) {
	WebUI.waitForElementVisible(findTestObject('Object Repository/Page_/lbl_ErrorMessage'), 5)
	String actualErrorMessage = WebUI.getText(findTestObject('Object Repository/Page_/lbl_ErrorMessage'))
	CustomKeywords.'customKeywords.verifyEqual'(actualErrorMessage, expectedErrorMessage, 'Pesan error tidak sesuai dengan yang diharapkan')
}

// Script Test Case
WebUI.openBrowser('https://fe.pssi-dev.kecilin.id/login')

// Loop melalui Test Data
int rowCount = testData.findAll().size()

for (int i = 1; i <= rowCount; i++) {
	String email = testData.getValue(1, i)
	String password = testData.getValue(2, i)
	String valid = testData.getValue(3, i) // Mendapatkan nilai validitas dari data file
	String expectedErrorMessage = testData.getValue(4, i) // Mendapatkan pesan error yang diharapkan

	println("Pengujian dengan Email: " + email + ", Valid: " + valid)

	login(email, password)

	if (valid.equalsIgnoreCase("true")) {
		// Email valid, verifikasi login berhasil
		if (WebUI.verifyElementVisible(findTestObject('Object Repository/Page_/lbl_Dashboard'), FailureHandling.OPTIONAL)) {
			println("Login berhasil untuk email valid: " + email)
			WebUI.takeScreenshot()
		} else {
			println("Login *seharusnya* berhasil untuk email valid: " + email + ", tetapi gagal.")
			WebUI.takeScreenshot()
			// Tambahkan logika verifikasi pesan error jika diperlukan
		}
	} else {
		// Email tidak valid, verifikasi pesan error
		verifyErrorMessage(expectedErrorMessage)
	}

	WebUI.delay(2)
	WebUI.navigateToUrl('https://fe.pssi-dev.kecilin.id/login')
}

WebUI.closeBrowser()






//// Definisi Test Object (Sebaiknya disimpan di Object Repository)
//TestObject txt_Email = findTestObject('Object Repository/Page_/input_Email') // Ganti dengan path object Anda
//TestObject txt_Password = findTestObject('Object Repository/Page_/input_Password') // Ganti dengan path object Anda
//TestObject btn_Login = findTestObject('Object Repository/Page_/button_Login') // Ganti dengan path object Anda
//TestObject lbl_Dashboard = findTestObject('Object Repository/Page_/lbl_Dashboard') // Ganti dengan path object Anda (Untuk verifikasi)
//TestObject lbl_ErrorMessage = findTestObject('Object Repository/Page_/lbl_ErrorMessage') //Ganti dengan path object anda (Untuk verifikasi pesan error)
//
//// Definisi Test Data (Sebaiknya disimpan di Data Files - contoh: Excel, CSV)
//TestData testData = findTestData('Data Files/LoginTestData') // Ganti dengan path data file Anda
//
//
//@Keyword
//def login(String email, String password) {
//	WebUI.setText(txt_Email, email)
//	WebUI.setEncryptedText(txt_Password, password) // Gunakan setEncryptedText jika menyimpan password terenkripsi
//	WebUI.click(btn_Login)
//}
//
//// Script Test Case
//WebUI.openBrowser('https://fe.pssi-dev.kecilin.id/login')
//
//// Loop melalui Test Data
//
//int rowCount = testData.size()
//
//for (int i = 1; i <= rowCount; i++) {
//	String email = testData.getValue(1, i)
//	String password = testData.getValue(2, i)
//	String valid = testData.getValue(3, i) // Mendapatkan nilai validitas dari data file
//
//	println("Pengujian dengan Email: " + email + ", Valid: " + valid)
//
//	login(email, password)
//
//	// Verifikasi berdasarkan validitas email
//	if (valid.equalsIgnoreCase("true")) {
//		// Email valid, verifikasi login berhasil
//		if (WebUI.verifyElementVisible(lbl_Dashboard, FailureHandling.OPTIONAL)) {
//			println("Login berhasil untuk email valid: " + email)
//			WebUI.takeScreenshot()
//		} else {
//			println("Login *seharusnya* berhasil untuk email valid: " + email + ", tetapi gagal.")
//			WebUI.takeScreenshot()
//			if(WebUI.verifyElementVisible(lbl_ErrorMessage, FailureHandling.OPTIONAL))
//			{
//				println("Pesan Error: "+WebUI.getText(lbl_ErrorMessage))
//			}
//			else
//			{
//				println("Tidak ada pesan error spesifik yang ditampilkan")
//			}
//		}
//	} else {
//		// Email tidak valid, verifikasi pesan error atau perilaku yang sesuai
//		if (WebUI.verifyElementVisible(lbl_ErrorMessage, FailureHandling.OPTIONAL)) {
//			println("Login gagal seperti yang diharapkan untuk email tidak valid: " + email)
//			println("Pesan Error: "+WebUI.getText(lbl_ErrorMessage))
//			WebUI.takeScreenshot()
//
//		} else {
//			println("Login *seharusnya* gagal untuk email tidak valid: " + email + ", tetapi tidak ada pesan error yang ditampilkan.")
//			WebUI.takeScreenshot()
//		}
//	}
//
//	WebUI.delay(2)
//	WebUI.navigateToUrl('https://fe.pssi-dev.kecilin.id/login')
//}
//
//WebUI.closeBrowser()