package com.urlcount.main;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.urlcount.object.CSSSelector;
import com.urlcount.utils.ExcelReader;

public class GetUrlCountNoBrowser {

	private static final Logger logger = LogManager.getLogger(GetUrlCountNoBrowser.class);

	public static void execute(HtmlUnitDriver driver, String path) {

		logger.info("=============URL Count Process start with NO Browser=========");

		ExcelReader eReader = null;

		logger.info("Input folder path:" + path);

		String inputFileStr = path + File.separator + "input" + File.separator + "InputFile.xlsx";

		String outputFileStr = path + File.separator + "output" + File.separator;

		LinkedHashMap<String, String> urlmap = new LinkedHashMap<String, String>();
		ArrayList<CSSSelector> cssList = new ArrayList<CSSSelector>();
		ArrayList<CSSSelector> urlList = new ArrayList<CSSSelector>();

		eReader = PageFactory.initElements(driver, ExcelReader.class);
		driver.manage().window().maximize();
		// MM_dd_yy_HH_mm_ss YYYYMMDDHHmm
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMdd2HHmm");
		LocalDateTime now = LocalDateTime.now();
		String tm = dtf.format(now);

		String fname = outputFileStr + "Output" + tm + ".xlsx";
		ExcelReader.createExcel(fname);

		eReader = PageFactory.initElements(driver, ExcelReader.class);
		urlList = ExcelReader.readExcel(inputFileStr, "URL", 0);
		int totalLength = urlmap.size();

		urlList.forEach((CSSSelector css) -> {
		//	cssList.add(new CSSSelector(css.getUrl(), css.getCssSelector(), css.getCssSelectorNot(), fname));
		});

		/*
		 * urlmap.forEach((k, v) -> { cssList.add(new CSSSelector(k, v, fname)); });
		 */
		String url = "";
		File outputFile = null;
		for (CSSSelector cssSel : cssList) {
			String resultFound = "";
			String found = "";
			String found2 = "";
			String tagoutput = "";
			WebElement element=null;
			WebElement result1 = null;
			WebElement result2 = null;
			url = cssSel.getUrl().trim();
			try {

				outputFile = new File(cssSel.getOutputfile());
				driver.manage().deleteAllCookies();
				
				
				
				//WebDriverWait wait = new WebDriverWait(driver, 100);
				// driver.setJavascriptEnabled(true);

				// elementToBeClickable(By.id("usrUtils")));
				
				//  driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
				  driver.get(url);
				 
				Thread.sleep(10000);

				
				//element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSel.getCssSelector())));
				
				  String pageSoruce=driver.getPageSource();
				  
				  logger.info("#####without browser###"); 
				  logger.info(pageSoruce);
				  logger.info("########");
				 

			} catch (Exception e) {
				logger.log(Level.ERROR, e.getMessage(), e);
			}
			try {

				logger.info("++++++++++++++++1++++++10 sec++++++++");

				System.out.println(cssSel.getCssSelector().trim());
				if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelector() != "0")) {
					

					logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector());
					//WebDriverWait wait = new WebDriverWait(driver, 10);
					//result1 = wait.until(driver1 -> driver.findElement(By.cssSelector(cssSel.getCssSelector())));
					 result1 = driver.findElement(By.cssSelector(cssSel.getCssSelector()));

				}
				if (result1 != null) {

					tagoutput = result1.getText();
					logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector() + ">>" + tagoutput);
					found = "True";
				}
			}

			catch (org.openqa.selenium.NoSuchElementException e) {
				logger.log(Level.ERROR, e.getMessage(), e);
				logger.info("\n\n 1 No Such Element - Result not found for url :[" + url + "]>>> CSS Selector:"
						+ cssSel.getCssSelector());

				found = "False";
			} catch (Exception e) {

				found = "Error in searching ";
				logger.log(Level.ERROR, e.getMessage(), e);
				logger.info("\n\n1 Error in searching - Result not found for url :[" + url + "]>>> CSS Selector:"
						+ cssSel.getCssSelector());

			}
			try {

				logger.info("++++++++++++++2++++++++++++++++");
				if (!found.equalsIgnoreCase("True")) {
					if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelectorNot() != "0")) {
						By by = By.cssSelector(cssSel.getCssSelectorNot());
						result2 = driver.findElement(by);
					}
					if (result2 != null) {
						tagoutput = result2.getText();
						logger.info(
								"url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelectorNot() + ">>" + tagoutput);
						found2 = "True";
					}
				}
			}

			catch (org.openqa.selenium.NoSuchElementException e) {
				logger.log(Level.ERROR, e.getMessage(), e);
				logger.info("\n\n2 No Such Element - Result not found for url :[" + url + "]>>> CSS Selector:"
						+ cssSel.getCssSelectorNot());

				found2 = "False";
			} catch (Exception e) {

				found2 = "Error in Searching";

				logger.log(Level.ERROR, e.getMessage(), e);
				logger.info("\n\n2 Error in searching - Result not found for url :[" + url + "]>>> CSS Selector:"
						+ cssSel.getCssSelectorNot());

			}

			if (found.equalsIgnoreCase("True")) {
				resultFound = "True";
			} else if (found2.equalsIgnoreCase("True")) {
				resultFound = "false";
			} else {
				resultFound = "Error Occured";
				// resultFound = "false";
			}

			if (cssSel.getCssSelector().equalsIgnoreCase("0")) {
				resultFound = "No CSS selector available";
			}
			if (cssSel.getCssSelectorNot().equalsIgnoreCase("0")) {
				resultFound = "No CSS selector for Zero result ";
			}
			logger.info("#### url:" + url + " , css selector:" + cssSel.getCssSelector() + " Found:" + resultFound);
			int Rowno = 1;
			try {
				Rowno = ExcelReader.findCurrentRowNo(cssSel.getOutputfile());
			} catch (Exception e) {
				logger.log(Level.ERROR, e.getMessage(), e);
			}
			try {
				ExcelReader.writeToExcel(outputFile, 0, url, 0, Rowno);
				ExcelReader.writeToExcel(outputFile, 1, resultFound, 0, Rowno);

			} catch (IOException e) {
				logger.log(Level.ERROR, e.getMessage(), e);
			} catch (Exception e) {
				logger.log(Level.ERROR, e.getMessage(), e);
			}
		}
		driver.manage().deleteAllCookies();
		driver.close();

		logger.info("=============URL Count Process  with NO Browser ends=============");
	}

}
