package RW;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceRW {
	static StringBuilder msg = new StringBuilder();
	public static WebDriver driver;
	// static JavascriptExecutor mzexecutor;
	public static GenerateData genData;
	public static String RdyTime;
	public static String RecMsg;
	public String baseUrl = "https://Staging.fedexsameday.com/specialsupport.aspx";

	@BeforeSuite
	public void startup() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		// options.addArguments("headless");
		options.addArguments("headless");
		options.addArguments("--incognito");
		options.addArguments("--test-type");
		options.addArguments("--no-proxy-server");
		options.addArguments("--proxy-bypass-list=*");
		options.addArguments("--disable-extensions");
		options.addArguments("--no-sandbox");
		options.addArguments("--headless");
		options.addArguments("window-size=1366x788");
		capabilities.setPlatform(Platform.ANY);
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		driver = new ChromeDriver(options);
		// Default size
		Dimension currentDimension = driver.manage().window().getSize();
		int height = currentDimension.getHeight();
		int width = currentDimension.getWidth();
		System.out.println("Current height: " + height);
		System.out.println("Current width: " + width);
		System.out.println("window size==" + driver.manage().window().getSize());

		// Set new size
		Dimension newDimension = new Dimension(1366, 788);
		driver.manage().window().setSize(newDimension);

		// Getting
		Dimension newSetDimension = driver.manage().window().getSize();
		int newHeight = newSetDimension.getHeight();
		int newWidth = newSetDimension.getWidth();
		System.out.println("Current height: " + newHeight);
		System.out.println("Current width: " + newWidth);
	}

	public void login() {
		WebDriverWait wait = new WebDriverWait(driver, 50);
		// DEV
		// String baseUrl = "https://test.fedexsameday.com/specialsupport.aspx";
		// Staging

		// String baseUrl = "http://10.20.205.70:9050/specialsupport.aspx";
		// TEMP Prod
		// String baseUrl = "https://wwwda2.fedexsameday.com/specialsupport.aspx";
		driver.get(baseUrl);
		// Enter User_name and Password and click on Login
		driver.findElement(By.id("txtUserId")).clear();
		driver.findElement(By.id("txtUserId")).sendKeys("FedExSupport");
		driver.findElement(By.id("txtPassword")).clear();
		// Staging
		driver.findElement(By.id("txtPassword")).sendKeys("Fedex123");
		// Production
		// driver.findElement(By.id("txtPassword")).sendKeys("password");

		driver.findElement(By.id("rbRouteWork")).click();

		driver.findElement(By.id("cmdLogin")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("content")));
	}

	@Test
	public void serviceRW() throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions act = new Actions(driver);
		// options.addArguments("--test-type");
		WebDriverWait wait = new WebDriverWait(driver, 50);
		genData = new GenerateData();

		// --Login
		login();

		// Open Menu > Submenu > Submenu -- "RW Form page"

		driver.findElement(By.linkText("Admin")).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Route Work")));

		WebElement menu = driver.findElement(By.linkText("Route Work"));
		Actions builder = new Actions(driver);
		builder.moveToElement(menu).perform();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Route Work Form")));

		driver.findElement(By.linkText("Route Work Form")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("content1")));

		// Route WorkId
		WebElement RWId = driver.findElement(By.id("txtRouteWorkId"));

		if (RWId.isEnabled()) {
			throw new Error("\nRWId is enable. It is an issue");
		}

		// Route Work Name

		driver.findElement(By.id("txtrouteworkdescription")).sendKeys("Route Name Automation ");
		driver.findElement(By.id("txtrouteworkdescription")).sendKeys(genData.generateRandomNumber(7));
		String RWName = driver.findElement(By.id("txtrouteworkdescription")).getAttribute("value");
		System.out.println(RWName);

		// Customer
		// Staging
		driver.findElement(By.id("txtCustCode")).sendKeys("117117117");
		// TEMP Prod
		// driver.findElement(By.id("txtCustCode")).sendKeys("777777777");

		// Declare Value
		driver.findElement(By.id("declared_value")).clear();
		driver.findElement(By.id("declared_value")).sendKeys("444");

		// Route Work Reference#2 and #4
		driver.findElement(By.id("txtRouteWorkRef2")).sendKeys("reference2");
		driver.findElement(By.id("txtRouteWorkRef4")).sendKeys("reference4");

		// Executed By
		driver.findElement(By.id("rdbNGL")).click();

		// Generate Route(No. of hours)
		driver.findElement(By.id("txtGenerateRoute")).clear();
		driver.findElement(By.id("txtGenerateRoute")).sendKeys("1");

		// Preferred Courier Route
		driver.findElement(By.id("txtPrefCourierRoute")).sendKeys("PCRoute001");

		// Start-End Date
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy ");
		Date stdate = new Date();
		String stdate1 = dateFormat.format(stdate);
		// System.out.println(stdate1);

		Date enddate = new Date();
		Date addedDate1 = addDays(enddate, 5);
		String enddate1 = dateFormat.format(addedDate1);
		// System.out.println(enddate1);

		driver.findElement(By.id("txtStartDate")).sendKeys(stdate1);
		driver.findElement(By.id("txtEndDate")).sendKeys(enddate1);

		// driver.findElement(By.xpath(".//*[@id='row1']/img")).click();
		// driver.findElement(By.xpath("//a[contains(.,'Today')]")).click();
		// sleep(500);
		// driver.findElement(By.xpath(".//*[@id='row3']/img")).click();
		// driver.findElement(By.xpath("//a[contains(.,'Today')]")).click();

		// Ready-Due Time
		driver.findElement(By.id("ReadyHourDropDownList")).sendKeys("8");
		driver.findElement(By.id("ReadyMinuteDropDownList")).sendKeys("30");
		driver.findElement(By.id("DueHourDropDownList")).sendKeys("2");
		driver.findElement(By.id("DueMinuteDropDownList")).sendKeys("45");
		driver.findElement(By.id("DueFormatDropDownList")).sendKeys("PM");

		String RdyHH1 = driver.findElement(By.id("ReadyHourDropDownList")).getAttribute("value");
		int RdyHH2 = Integer.parseInt(RdyHH1);
		// System.out.println("Convert into Int:" + RdyHH2);

		if (RdyHH2 < 10) {
			String RdyHH = "0" + RdyHH2;
			// System.out.println("Ready HR:" + RdyHH);
			String RdyMM = driver.findElement(By.id("ReadyMinuteDropDownList")).getAttribute("value");
			// System.out.println(RdyMM);
			String RdyAP = driver.findElement(By.id("ReadyFormatDropDownList")).getAttribute("value");
			// System.out.println(RdyAP);

			RdyTime = RdyHH + ":" + RdyMM + " " + RdyAP;
			System.out.println("Ready Time: " + RdyTime);
		} else {
			int RdyHH = RdyHH2;

			String RdyMM = driver.findElement(By.id("ReadyMinuteDropDownList")).getAttribute("value");
			// System.out.println(RdyMM);
			String RdyAP = driver.findElement(By.id("ReadyFormatDropDownList")).getAttribute("value");
			// System.out.println(RdyAP);

			RdyTime = RdyHH + ":" + RdyMM + " " + RdyAP;
			System.out.println("Ready Time: " + RdyTime);
		}

		// System.out.println(RdyTime);

		// Recurrence
		driver.findElement(By.id("rdbtrecurrence_0")).click();
		driver.findElement(By.id("txtDaysDaily")).sendKeys("2");
		String RecType = driver.findElement(By.id("rdbtrecurrence_0")).getAttribute("value");
		// System.out.println("Rec Type: " + RecType);
		String RecValue = driver.findElement(By.id("txtDaysDaily")).getAttribute("value");
		// System.out.println("Rec Value: " + RecValue);

		// Exempt Date
		Date exmdate = new Date();
		Date addedDate2 = addDays(exmdate, 4);
		String exmdate1 = dateFormat.format(addedDate2);
		// System.out.println(exmdate1);

		driver.findElement(By.id("txtExemptDateDaily")).clear();
		driver.findElement(By.id("txtExemptDateDaily")).sendKeys("Holidays");
		driver.findElement(By.id("txtExemptDateDaily")).sendKeys(";");
		driver.findElement(By.id("txtExemptDateDaily")).sendKeys(exmdate1);

		String ExemptDate = driver.findElement(By.id("txtExemptDateDaily")).getAttribute("value");

		// AlterGenerateDate
		Date altdate = new Date();
		Date addedDate3 = addDays(altdate, 3);
		String altdate1 = dateFormat.format(addedDate3);
		// System.out.println(altdate1);

		driver.findElement(By.id("txtAlternateGenerationDateDaily")).sendKeys(altdate1);
		String AlertGenerationDate = driver.findElement(By.id("txtAlternateGenerationDateDaily")).getAttribute("value");

		// First Generation
		Date fgdate = new Date();
		String fgdate1 = dateFormat.format(fgdate);
		// System.out.println(fgdate1);

		driver.findElement(By.id("txtDate")).sendKeys(fgdate1);
		String FirstGenerationDate = driver.findElement(By.id("txtDate")).getAttribute("value");

		driver.findElement(By.id("ddlHoursTime")).sendKeys("12");
		driver.findElement(By.id("ddlMinuteTime")).sendKeys("10");
		driver.findElement(By.id("ddlTimeMinute")).sendKeys("AM");

		String fghh = driver.findElement(By.id("ddlHoursTime")).getAttribute("value");
		String fgmm = driver.findElement(By.id("ddlMinuteTime")).getAttribute("value");
		String fgampm = driver.findElement(By.id("ddlTimeMinute")).getAttribute("value");

		String FirstGenerationTime = fghh + ":" + fgmm + " " + fgampm;

		// Manifest
		driver.findElement(By.id("txtRouteWorkEmail")).sendKeys("pdoshi1@samyak.com");

		// Flat Rate
		driver.findElement(By.id("txtFlatRate")).sendKeys("234.56");

		// Return Un-deliverable Shipments To
		driver.findElement(By.id("txtUDCompany")).sendKeys("JOHN COMP");
		driver.findElement(By.id("txtUDContact")).sendKeys("JOHN STARVIS");
		driver.findElement(By.id("txtUDAddr1")).sendKeys("9011 CAPITAL KING, 7TH STRT");
		driver.findElement(By.id("txtUDAddr2")).sendKeys("No#771");
		driver.findElement(By.id("txtUDzip")).sendKeys("90003");
		driver.findElement(By.id("txtUDPhone")).sendKeys("4115116110");
		driver.findElement(By.id("txtUDDeliveryInst")).sendKeys("Please call before 4h of delivery process");

		// Shipment Details - 1
		// From
		driver.findElement(By.id("txtFromStopSeq")).sendKeys("1");
		driver.findElement(By.id("txtFromCompany")).sendKeys("CREATIVE ARTIST AGENCY");
		driver.findElement(By.id("txtFromContact")).sendKeys("Client Trust");
		driver.findElement(By.id("txtFromAddr1")).sendKeys("2000 AVENUE OF THE STARS");
		driver.findElement(By.id("txtFromAddr2")).sendKeys("");
		driver.findElement(By.id("txtFromZip")).sendKeys("90067");
		driver.findElement(By.id("txtFromPhone")).sendKeys("(424) 288-2125");
		driver.findElement(By.id("txtPUInst")).sendKeys("Art Work");
		driver.findElement(By.id("txtShipperEmail")).sendKeys("pdoshi1@samyak.com");
		driver.findElement(By.id("chkShpOrderRcvd")).click();
		driver.findElement(By.id("chkShpPickup")).click();
		driver.findElement(By.id("chkShpDelivery")).click();

		// TO
		driver.findElement(By.id("txtToStopSeq")).sendKeys("2");
		driver.findElement(By.id("txtToCompany")).sendKeys("SHOWTIME");
		driver.findElement(By.id("txtToContact")).sendKeys("GARY LEVINE");
		driver.findElement(By.id("txtToAddr1")).sendKeys("10880 WHILSHIRE BLVD");
		driver.findElement(By.id("txtToAddr2")).sendKeys("#1600");
		driver.findElement(By.id("txtToZip")).sendKeys("90024");
		driver.findElement(By.id("txtToPhone")).sendKeys("(424) 288-2125");
		driver.findElement(By.id("txtDelInst")).sendKeys("Art Work");
		driver.findElement(By.id("txtRecipientEmail")).sendKeys("pdoshi2@samyak.com");
		driver.findElement(By.id("chkRecpOrderRcvd")).click();
		driver.findElement(By.id("chkRecpQDTChange")).click();
		driver.findElement(By.id("chkRecpException")).click();

		// Package for Shipment-1

		driver.findElement(By.id("pieces")).clear();
		driver.findElement(By.id("pieces")).sendKeys("1");
		driver.findElement(By.id("txtContents")).clear();
		driver.findElement(By.id("txtContents")).sendKeys("FLOWERS with Chocolate");
		Thread.sleep(500);
		// driver.findElement(By.id("ddProfile")).sendKeys("Enter dimensions");
		driver.findElement(By.id("txtDimLen0")).clear();
		driver.findElement(By.id("txtDimLen0")).sendKeys("15");
		driver.findElement(By.id("txtDimWid0")).clear();
		driver.findElement(By.id("txtDimWid0")).sendKeys("10");
		driver.findElement(By.id("txtDimHt0")).clear();
		driver.findElement(By.id("txtDimHt0")).sendKeys("2");
		driver.findElement(By.id("txtActWt0")).clear();
		driver.findElement(By.id("txtActWt0")).sendKeys("2");

		driver.findElement(By.id("txtRouteWorkRef1")).sendKeys("Ref1 Ship1");
		driver.findElement(By.id("txtRouteWorkRef3")).sendKeys("Ref3 Ship1");

		driver.findElement(By.id("btnaddshipment")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("gvShipmentDetails")));
		// Shipment Details - 2
		// From
		driver.findElement(By.id("txtFromStopSeq")).clear();
		driver.findElement(By.id("txtFromStopSeq")).sendKeys("1");
		// TO
		driver.findElement(By.id("txtToStopSeq")).clear();
		driver.findElement(By.id("txtToStopSeq")).sendKeys("3");
		driver.findElement(By.id("txtToCompany")).sendKeys("PROTOCOL ENTERTAINMENT");
		driver.findElement(By.id("txtToContact")).sendKeys("JARRY CROSS");
		driver.findElement(By.id("txtToAddr1")).sendKeys("16128 SHERMAN WAY");
		driver.findElement(By.id("txtToAddr2")).clear();
		driver.findElement(By.id("txtToZip")).sendKeys("91406");
		driver.findElement(By.id("txtToPhone")).sendKeys("(424) 288-2125");
		driver.findElement(By.id("txtDelInst")).sendKeys("Art Work");
		driver.findElement(By.id("txtRecipientEmail")).sendKeys("pdoshi@samyak.com");
		driver.findElement(By.id("chkRecpOrderRcvd")).click();
		// Package for Shipment-2
		driver.findElement(By.id("pieces")).clear();
		driver.findElement(By.id("pieces")).sendKeys("1");
		driver.findElement(By.id("txtContents")).clear();
		driver.findElement(By.id("txtContents")).sendKeys("FLOWERS with Chocolate");
		Thread.sleep(500);
		// driver.findElement(By.id("ddProfile")).sendKeys("Enter dimensions");
		driver.findElement(By.id("txtDimLen0")).clear();
		driver.findElement(By.id("txtDimLen0")).sendKeys("9");
		driver.findElement(By.id("txtDimWid0")).clear();
		driver.findElement(By.id("txtDimWid0")).sendKeys("9");
		driver.findElement(By.id("txtDimHt0")).clear();
		driver.findElement(By.id("txtDimHt0")).sendKeys("9");
		driver.findElement(By.id("txtActWt0")).clear();
		driver.findElement(By.id("txtActWt0")).sendKeys("19");

		driver.findElement(By.id("txtRouteWorkRef1")).sendKeys("Ref1 Ship2");
		driver.findElement(By.id("txtRouteWorkRef3")).sendKeys("Ref3 Ship2");

		driver.findElement(By.id("btnaddshipment")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("gvShipmentDetails")));

		// Shipment Details - 3
		// From
		driver.findElement(By.id("txtFromStopSeq")).clear();
		driver.findElement(By.id("txtFromStopSeq")).sendKeys("1");
		// TO
		driver.findElement(By.id("txtToStopSeq")).clear();
		driver.findElement(By.id("txtToStopSeq")).sendKeys("4");
		driver.findElement(By.id("txtToCompany")).sendKeys("HBO");
		driver.findElement(By.id("txtToContact")).sendKeys("MR. MICHAEL LOMBARDO");
		driver.findElement(By.id("txtToAddr1")).sendKeys("2500 BROADWAY");
		driver.findElement(By.id("txtToAddr2")).sendKeys("#400");
		driver.findElement(By.id("txtToZip")).sendKeys("91404");
		driver.findElement(By.id("txtToPhone")).sendKeys("(424) 288-2125");
		driver.findElement(By.id("txtDelInst")).sendKeys("Art Work");
		driver.findElement(By.id("txtRecipientEmail")).sendKeys("pdoshi@samyak.com");
		driver.findElement(By.id("chkRecpOrderRcvd")).click();
		driver.findElement(By.id("chkRecpQDTChange")).click();
		driver.findElement(By.id("chkRecpException")).click();
		// Package for Shipment-3
		driver.findElement(By.id("pieces")).clear();
		driver.findElement(By.id("pieces")).sendKeys("1");
		driver.findElement(By.id("txtContents")).clear();
		driver.findElement(By.id("txtContents")).sendKeys("Only Chocolate");
		Thread.sleep(500);
		// driver.findElement(By.id("ddProfile")).sendKeys("Enter dimensions");
		driver.findElement(By.id("txtDimLen0")).clear();
		driver.findElement(By.id("txtDimLen0")).sendKeys("4");
		driver.findElement(By.id("txtDimWid0")).clear();
		driver.findElement(By.id("txtDimWid0")).sendKeys("2");
		driver.findElement(By.id("txtDimHt0")).clear();
		driver.findElement(By.id("txtDimHt0")).sendKeys("3");
		driver.findElement(By.id("txtActWt0")).clear();
		driver.findElement(By.id("txtActWt0")).sendKeys("13");

		driver.findElement(By.id("txtRouteWorkRef1")).sendKeys("Ref1 Ship3");
		driver.findElement(By.id("txtRouteWorkRef3")).sendKeys("Ref3 Ship3");

		driver.findElement(By.id("btnaddshipment")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("gvShipmentDetails")));

		// Shipment Details - 4
		// From
		driver.findElement(By.id("txtFromStopSeq")).clear();
		driver.findElement(By.id("txtFromStopSeq")).sendKeys("1");
		// TO
		driver.findElement(By.id("txtToStopSeq")).clear();
		driver.findElement(By.id("txtToStopSeq")).sendKeys("6");
		driver.findElement(By.id("txtToCompany")).sendKeys("ORLY ADELSON PRODUCTIONS");
		driver.findElement(By.id("txtToContact")).sendKeys("SQUINTAR FILMS INC");
		driver.findElement(By.id("txtToAddr1")).sendKeys("2900 OLYMPIC BLVD");
		driver.findElement(By.id("txtToAddr2")).clear();
		driver.findElement(By.id("txtToZip")).sendKeys("91404");
		driver.findElement(By.id("txtToPhone")).sendKeys("(424) 288-2125");
		driver.findElement(By.id("txtDelInst")).sendKeys("Art Work");
		driver.findElement(By.id("txtRecipientEmail")).sendKeys("pdoshi@samyak.com");
		driver.findElement(By.id("chkRecpOrderRcvd")).click();
		// Package for Shipment-4
		driver.findElement(By.id("pieces")).clear();
		driver.findElement(By.id("pieces")).sendKeys("2");
		WebElement webElement = driver.findElement(By.id("pieces"));
		webElement.sendKeys(Keys.TAB);
		Thread.sleep(500);
		driver.findElement(By.xpath(".//*[@id='rdbyes']")).click();

		driver.findElement(By.id("txtContents")).clear();
		driver.findElement(By.id("txtContents")).sendKeys("FLOWERS bookeh");
		Thread.sleep(500);
		// driver.findElement(By.id("ddProfile")).sendKeys("Enter dimensions");
		driver.findElement(By.id("txtDimLen0")).clear();
		driver.findElement(By.id("txtDimLen0")).sendKeys("14");
		driver.findElement(By.id("txtDimWid0")).clear();
		driver.findElement(By.id("txtDimWid0")).sendKeys("7");
		driver.findElement(By.id("txtDimHt0")).clear();
		driver.findElement(By.id("txtDimHt0")).sendKeys("22");
		driver.findElement(By.id("txtActWt0")).clear();
		driver.findElement(By.id("txtActWt0")).sendKeys("18");

		driver.findElement(By.id("txtRouteWorkRef1")).sendKeys("Ref1 Ship4");
		driver.findElement(By.id("txtRouteWorkRef3")).sendKeys("Ref3 Ship4");

		driver.findElement(By.id("btnaddshipment")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("gvShipmentDetails")));

		// Click on Done for validation
		WebElement BtnDone = driver.findElement(By.id("btndone"));
		js.executeScript("arguments[0].scrollIntoView(true);", BtnDone);
		Thread.sleep(2000);
		act.moveToElement(BtnDone).click().perform();
		// BtnDone.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lblShipmentCountErr")));
		String DoneValidation = driver.findElement(By.id("lblShipmentCountErr")).getText();
		System.out.println("ActualMsg==" + DoneValidation);

		String Val1 = "Shipment Stop Sequence is missing. Please include all stops in sequence to generate route.";

		if (DoneValidation.equals(Val1)) {
			System.out.println("Display this validation when seq not proper: " + DoneValidation);
		} else {
			throw new Error("\nStop sequence validation not proper");
		}

		// Click SaveforLater for Draft
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnsaveforlater")));
		driver.findElement(By.id("btnsaveforlater")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("currentForm")));

		// Get Generated RWId
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lmsg")));
		String RWIdtext = driver.findElement(By.id("lmsg")).getText();

		String[] RWSplit = RWIdtext.split(" ");
		String RWid = RWSplit[5];

		String RWid1 = RWid.substring(0, 10);

		System.out.println("RouteWorkId: " + RWid1);

		// Search with generated RWId
		driver.findElement(By.id("ddlStatus")).sendKeys("All");
		Thread.sleep(2000);
		driver.findElement(By.id("txtRouteWorkId")).sendKeys(RWid1);
		Thread.sleep(2000);
		driver.findElement(By.id("btnSearch")).click();
		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("currentForm")));

		// Edit RW
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='dgRWList_lbEdit_0']/img")));
		driver.findElement(By.xpath(".//*[@id='dgRWList_lbEdit_0']/img")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("content1")));

		// js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
		js.executeScript("window.scrollBy(0,250)");
		Thread.sleep(1000);

		// Change Sequence of shipment-4
		// driver.findElement(By.xpath(".//*[@id='gvShipmentDetails']/tbody/tr[0]/td[5]/a")).click();

		WebElement element4 = driver.findElement(By.xpath(".//*[@id='gvShipmentDetails_ctl06_lbEdit']"));
		Actions actions = new Actions(driver);
		actions.moveToElement(element4).click().build().perform();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("content1")));

		wait.until(ExpectedConditions.elementToBeClickable(By.id("txtToStopSeq")));
		driver.findElement(By.id("txtToStopSeq")).clear();
		driver.findElement(By.id("txtToStopSeq")).sendKeys("5");

		driver.findElement(By.id("btnaddshipment")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("gvShipmentDetails")));

		// Shipment Details - 5
		// From
		driver.findElement(By.id("txtFromStopSeq")).clear();
		driver.findElement(By.id("txtFromStopSeq")).sendKeys("1");
		// TO
		driver.findElement(By.id("txtToStopSeq")).clear();
		driver.findElement(By.id("txtToStopSeq")).sendKeys("6");
		driver.findElement(By.id("txtToCompany")).sendKeys("JAVE PRODUCTIONS, INC");
		driver.findElement(By.id("txtToCompany")).sendKeys(Keys.TAB);
		/*
		 * try { driver.switchTo().alert(); driver.switchTo().alert().accept();
		 * Thread.sleep(2000); } catch (Exception e) {
		 * System.out.println("Alert is not present"); }
		 */
		driver.findElement(By.id("txtToContact")).sendKeys("BARRY SIEGAL");
		driver.findElement(By.id("txtToAddr1")).sendKeys("2850 OCEAN PARK BLVD");
		driver.findElement(By.id("txtToAddr2")).sendKeys("#300");
		driver.findElement(By.id("txtToZip")).sendKeys("90405");
		driver.findElement(By.id("txtToPhone")).sendKeys("(424) 288-2125");
		driver.findElement(By.id("txtDelInst")).sendKeys("Art Work");
		driver.findElement(By.id("txtRecipientEmail")).sendKeys("pdoshi@samyak.com");
		driver.findElement(By.id("chkRecpOrderRcvd")).click();
		driver.findElement(By.id("chkRecpPickup")).click();
		driver.findElement(By.id("chkRecpDelivery")).click();
		// Package for Shipment-5
		// driver.findElement(By.id("pieces")).clear();
		driver.findElement(By.id("pieces")).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.id("pieces")).sendKeys(Keys.BACK_SPACE);
		driver.findElement(By.id("pieces")).sendKeys("2");
		driver.findElement(By.id("pieces")).sendKeys(Keys.TAB);
		Thread.sleep(1000);
		driver.findElement(By.xpath(".//*[@id='rdbNo']")).click();
		driver.findElement(By.id("txtContents")).clear();
		driver.findElement(By.id("txtContents")).sendKeys("FLOWERS with Chocolate");
		Thread.sleep(500);
		driver.findElement(By.id("txtQty0")).clear();
		driver.findElement(By.id("txtQty0")).sendKeys("1");
		// driver.findElement(By.id("drpdim0")).sendKeys("Enter dimensions");
		driver.findElement(By.id("txtDimLenN0")).clear();
		driver.findElement(By.id("txtDimLenN0")).sendKeys("15");
		driver.findElement(By.id("txtDimWidN0")).clear();
		driver.findElement(By.id("txtDimWidN0")).sendKeys("10");
		driver.findElement(By.id("txtDimHtN0")).clear();
		driver.findElement(By.id("txtDimHtN0")).sendKeys("2");
		driver.findElement(By.id("txtActWtNew0")).clear();
		driver.findElement(By.id("txtActWtNew0")).sendKeys("2");

		driver.findElement(By.id("txtQty1")).clear();
		driver.findElement(By.id("txtQty1")).sendKeys("1");
		// driver.findElement(By.id("drpdim1")).sendKeys("Enter dimensions");
		driver.findElement(By.id("txtDimLenN1")).clear();
		driver.findElement(By.id("txtDimLenN1")).sendKeys("15");
		driver.findElement(By.id("txtDimWidN1")).clear();
		driver.findElement(By.id("txtDimWidN1")).sendKeys("10");
		driver.findElement(By.id("txtDimHtN1")).clear();
		driver.findElement(By.id("txtDimHtN1")).sendKeys("2");
		driver.findElement(By.id("txtActWtNew1")).clear();
		driver.findElement(By.id("txtActWtNew1")).sendKeys("2");

		driver.findElement(By.id("txtRouteWorkRef1")).sendKeys("Ref1 Ship5");
		driver.findElement(By.id("txtRouteWorkRef3")).sendKeys("Ref3 Ship5");

		driver.findElement(By.id("btnaddshipment")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("gvShipmentDetails")));

		BtnDone = driver.findElement(By.id("btndone"));
		js.executeScript("arguments[0].scrollIntoView(true);", BtnDone);
		Thread.sleep(2000);
		act.moveToElement(BtnDone).click().perform();

		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("newcontent")));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ddlStatus")));
		driver.findElement(By.id("ddlStatus")).sendKeys("All");
		driver.findElement(By.id("txtRouteWorkId")).sendKeys(RWid1);
		driver.findElement(By.id("btnSearch")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("currentForm")));

		// Active RW
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//*[@id='dgRWList_lbActivate_0']/img")));
		driver.findElement(By.xpath(".//*[@id='dgRWList_lbActivate_0']/img")).click();
		Thread.sleep(2000);

		driver.switchTo().alert();
		driver.switchTo().alert().accept();
		Thread.sleep(2000);

		// Get message after activation of RW
		String NextGen = driver.findElement(By.id("lmsg")).getText();
		System.out.println(NextGen);

		// Reset RW list
		driver.findElement(By.id("btnSearch")).click();
		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("currentForm")));

		// Search again with RW
		// driver.findElement(By.id("ddlStatus")).sendKeys("All");
		// driver.findElement(By.id("txtRouteWorkId")).sendKeys(RWid1);
		// driver.findElement(By.id("btnSearch")).click();
		// Thread.sleep(10000);

		// Verify Recurrence schedule

		// Store the current window handle
		String winHandleBefore = driver.getWindowHandle();
		// Perform the click operation that opens new window
		driver.findElement(By.xpath(".//*[@id='dgRWList_lbRWOccurance_0']/img")).click();
		// Switch to new window opened
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}

		// Perform the actions on new window
		// String ReadyTime = "08:30 AM";

		wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("dgRWOccurance")));
		String ActGen1 = driver.findElement(By.xpath("//*[@id=\"dgRWOccurance\"]//tr[2]/td[6]")).getText();
		// System.out.println(ActGen1);
		String ActGen2 = driver.findElement(By.xpath("//*[@id=\"dgRWOccurance\"]//tr[3]/td[6]")).getText();
		// System.out.println(ActGen2);
		String ActGen3 = driver.findElement(By.xpath("//*[@id=\"dgRWOccurance\"]//tr[4]/td[6]")).getText();
		// System.out.println(ActGen3);

		Date SchGen1 = new Date();
		String Expdate1 = dateFormat.format(SchGen1);
		// System.out.println(Expdate1);

		Date SchGen2 = new Date();
		Date addedSchDate1 = addDays(SchGen2, 2);
		String Expdate2 = dateFormat.format(addedSchDate1);
		// System.out.println(Expdate2);

		Date SchGen3 = new Date();
		Date addedSchDate2 = addDays(SchGen3, 3);
		String Expdate3 = dateFormat.format(addedSchDate2);
		// System.out.println(Expdate3);

		String Expdate1final = Expdate1 + RdyTime;
		// System.out.println(Expdate1final);
		String Expdate2final = Expdate2 + RdyTime;
		// System.out.println(Expdate2final);
		String Expdate3final = Expdate3 + RdyTime;
		// System.out.println(Expdate3final);

		if (ActGen1.contains(Expdate1final)) {
			if (ActGen2.contains(Expdate2final)) {
				if (ActGen3.contains(Expdate3final)) {
					RecMsg = "All Schedule will generate proper as per recurrence set";
					System.out.println(RecMsg);
				}

			}

		}

		// Close the new window, if that window no more required
		driver.close();
		Thread.sleep(1000);
		// Switch back to original browser (first window)
		driver.switchTo().window(winHandleBefore);
		Thread.sleep(1000);
		// Continue with original browser (first window)

		// Send Route Work Details email
		msg.append("Route Work Id : " + RWid1 + "\n");
		msg.append("Route Work Name : " + RWName + "\n\n");

		msg.append("Message on Activation of RW : " + NextGen + "\n\n");

		msg.append("Start Date : " + stdate1 + "\n");
		msg.append("End Date : " + enddate1 + "\n");
		msg.append("Ready Time : " + RdyTime + "\n\n");

		msg.append("Recurrence Detail : " + RecType + " - " + "Every" + " " + RecValue + " " + "Day(s)" + "\n");
		msg.append("Exempt Date : " + ExemptDate + "\n");
		msg.append("Alternate Generation Date : " + AlertGenerationDate + "\n\n");

		msg.append("First Generation Dttm : " + FirstGenerationDate + " " + FirstGenerationTime + "\n\n");

		msg.append("Schedule in Order Queue : " + "\n");
		msg.append(ActGen1 + "\n");
		msg.append(ActGen2 + "\n");
		msg.append(ActGen3 + "\n\n");

		msg.append("Recurrence Verification : " + RecMsg + "\n\n");

		msg.append("*** This is automated generated email and send through automation script" + "\n");
		msg.append("Process URL : " + baseUrl);

		String subject = "Selenium Automation Script: Staging Route Work Smoke";
		try {
			Email.sendMail("ravina.prajapati@samyak.com,asharma@samyak.com,parth.doshi@samyak.com", subject,
					msg.toString(), "");

		} catch (Exception ex) {
			Logger.getLogger(ServiceRW.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static Date addDays(Date d, int days) {
		d.setTime(d.getTime() + days * 1000 * 60 * 60 * 24);
		return d;
	}

	@AfterSuite
	public void end() {
		driver.close();
		driver.quit();
	}

}