package com.urlcount.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.urlcount.object.CSSSelector;
import com.urlcount.urlmail.SSLEmail;
import com.urlcount.utils.ExcelReader;
import com.urlcount.utils.GetUrlCountUtil;

  class GetUrlCountWithBrowser {

	private static final Logger logger = LogManager.getLogger(GetUrlCountWithBrowser.class);
	private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);
	
	public static void execute(ChromeDriver driver, String path) throws Exception {
		  boolean isValidURL=false;
		logger.info("=============URL Count Process start  with Browser=========");

		ExcelReader eReader = null;

		logger.info("Input folder path:" + path);

		String inputFileStr = path + File.separator + "input" + File.separator + "InputFile.xlsx";

		String outputFileStr = path + File.separator + "output" + File.separator;

		LinkedHashMap<String, String> urlmap = new LinkedHashMap<String, String>();
		ArrayList<CSSSelector> cssList = new ArrayList<CSSSelector>();
		ArrayList<CSSSelector> urlList = new ArrayList<CSSSelector>();

		//eReader = PageFactory.initElements(driver, ExcelReader.class);

		// MM_dd_yy_HH_mm_ss YYYYMMDDHHmm
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMdd2HHmm");
		LocalDateTime now = LocalDateTime.now();
		String tm = dtf.format(now);

		String fname = outputFileStr + "Output" + tm + ".xlsx";
		ExcelReader.createExcel(fname);

		 
		urlList = ExcelReader.readExcel(inputFileStr, "URL", 0);
		int totalLength = urlmap.size();

		urlList.forEach((CSSSelector css) -> {
			cssList.add(new CSSSelector(css.getUrl(),css.getHomeSelectorStr() ,css.getCssSelector(), css.getCssSelectorNot(), fname));
		});

		/*
		 * urlmap.forEach((k, v) -> { cssList.add(new CSSSelector(k, v, fname)); });
		 */
		String url = "";
		File outputFile = null;
		int rowCount = 1;
		for (CSSSelector cssSel : cssList) {
			String resultFound = "";
		
			int foundResult = 0;
			WebElement result1 = null;
			
			url = cssSel.getUrl().trim();
			int tagValue = 0;
			String pageSource="";
			outputFile = new File(cssSel.getOutputfile());
			
			URL urlh = new URL(url);
			String homeurl = urlh.getProtocol() + "://" + urlh.getHost();
			
			
			logger.info("+++++++++row = " + (rowCount++) + "+++++++++++++");
			
			
			isValidURL=GetUrlCountUtil.checkValidURL(url, driver, cssSel.getCssSelector().trim());
			pageSource= GetUrlCountUtil.getPageSource(driver, url);
			if(!isValidURL)
			{
				resultFound="InValid URL";
				foundResult = 1;
			}
			else if(cssSel.getCssSelector().equalsIgnoreCase("HOME"))
			{
				resultFound="Home";
				foundResult = 1;
			}
			else if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelector() != "0")) {

				logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector());

				result1 = GetUrlCountUtil.getElement(driver, cssSel.getUrl(), cssSel.getCssSelector());
				if (result1 != null) {
					tagValue = extractNumber(result1.getAttribute("innerText"));
					foundResult = 1;
					if (tagValue > 0) {
						logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector() + ">>" + tagValue);
						resultFound = "True";
					} else if (tagValue == 0) {
						resultFound = "False";
					}
				} else {
					
				boolean sp=	isSelectorInPageSource(pageSource, cssSel.getCssSelector());
					if(sp)
					{
						resultFound = "True";
						foundResult = 1;
					}
					else
					{
					   foundResult = 0;
					}
				}

			}
			if (foundResult == 0) {
				 
				try {
					 
					 String selectorName=GetUrlCountUtil.getSelectorName(cssSel.getCssSelectorNot());
						
					// selector="div#resultsFoundMessage";
				//	 	 logger.info(pageSource);
					 logger.info("##############url driver using:" + driver.getCurrentUrl()+"::"+pageSource.contains(selectorName));
					// List<WebElement> elements = driver.findElements(By.cssSelector(selector));
					 if(!selectorName.equals(""))
					 {
						 if(pageSource.contains(selectorName))
						 {
							 resultFound="False";
						 }
						 else
						 {
							 resultFound = "Error";
						 }
					 }
					 else
					 {
						 resultFound=iffoundResultZero(cssSel, driver);
						if(resultFound.equals(""))
						{
							resultFound = "Error";
						}
					 }
			}
				catch(Exception e)
				{
					e.printStackTrace();
				}
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
		try {
			driver.manage().deleteAllCookies();
			driver.close();
		//	SSLEmail.SendMail(outputFile);
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage(), e);
		}

		logger.info("=============URL Count Process  with  Browser ends=============");
	}

	public static int extractNumber(final String str) {
		int number = 0;
		String regex = "[^0-9]";
		String numStr = "";
		Pattern pattern = Pattern.compile(regex);
		try {
			numStr = pattern.matcher(str).replaceAll("");
			if (Character.isDigit(numStr.charAt(0))) {
				number = Integer.parseInt(numStr);
			}
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage(), e);
			number = 1;
		}
		return number;
	}
	public static String iffoundResultZero( CSSSelector cssSel, ChromeDriver driver) {
		WebElement result2 = null;
		String rFound = "";
		
		if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelectorNot() != "0")) {

			try {
				result2 = GetUrlCountUtil.getElement(driver, cssSel.getCssSelectorNot(), cssSel.getUrl());
				if (result2 != null) {
					rFound = "false";

				} else {
					rFound = "Error";

				}
				System.out.println("resultFound:" + rFound);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return rFound;
	}
	
	public static boolean isSelectorInPageSource( String page, String selector) {
		 
		boolean selectorInPageSource=false;
		try {
		 	 String selectorName=GetUrlCountUtil.getSelectorName(selector);
				
			// selector="div#resultsFoundMessage";
		//	 	 logger.info(pageSource);
			 logger.info("Selector in page source::"+page.contains(selectorName));
			// List<WebElement> elements = driver.findElements(By.cssSelector(selector));
			 if(!selectorName.equals(""))
			 {
				 if(page.contains(selectorName))
				 {
					 selectorInPageSource=true;
				 }
			 }
			 else
			 {
				 selectorInPageSource=false;
			 }
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
			selectorInPageSource=false;
		}
		return selectorInPageSource;
	}
}
