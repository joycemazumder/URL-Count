package com.urlcount.main;

import java.io.File;
import java.io.IOException;
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

import com.urlcount.object.CSSSelector;
import com.urlcount.urlmail.SSLEmail;
import com.urlcount.utils.ExcelReader;

public class GetUrlCountWithBrowser {

	private static final Logger logger = LogManager.getLogger(GetUrlCountWithBrowser.class);

	public static void execute(ChromeDriver  driver,String path) throws Exception {

 

		logger.info("=============URL Count Process start  with Browser=========");
		 
		ExcelReader eReader = null;

		logger.info("Input folder path:" + path);

		String inputFileStr = path + File.separator + "input" + File.separator + "InputFile.xlsx";

		String outputFileStr = path + File.separator + "output" + File.separator;

		LinkedHashMap<String, String> urlmap = new LinkedHashMap<String, String>();
		ArrayList<CSSSelector> cssList = new ArrayList<CSSSelector>();
		ArrayList<CSSSelector> urlList = new ArrayList<CSSSelector>();

		eReader = PageFactory.initElements(driver, ExcelReader.class);
		 
	//	MM_dd_yy_HH_mm_ss		YYYYMMDDHHmm
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMdd2HHmm");
		LocalDateTime now = LocalDateTime.now();
		String tm = dtf.format(now);

		String fname = outputFileStr + "Output" + tm + ".xlsx";
		ExcelReader.createExcel(fname);

		eReader = PageFactory.initElements(driver, ExcelReader.class);
		urlList = ExcelReader.readExcel(inputFileStr, "URL", 0);
		int totalLength = urlmap.size();

		urlList.forEach((CSSSelector css) -> {
			cssList.add(new CSSSelector(css.getUrl(), css.getCssSelector(), css.getCssSelectorNot(), fname));
		});

 
		/*
		 * urlmap.forEach((k, v) -> { cssList.add(new CSSSelector(k, v, fname)); });
		 */
		String url = "";
		File outputFile=null;
		int rowCount=1;
		for (CSSSelector cssSel : cssList) {
			String resultFound="";
			String found = "";
			String found2 = "";
			int foundResult = 0;
			int notFoundResult = 0;
			String tagoutput="";
			WebElement result1 = null;
			WebElement result2 = null;
			url = cssSel.getUrl().trim();
			int tagValue=0;
			  outputFile=new File(cssSel.getOutputfile());
			try {

				driver.manage().deleteAllCookies();
				 
				driver.get(url);
			  String pageLoadStatus = null;

				JavascriptExecutor js= null;
				do {

					js = (JavascriptExecutor) driver;

					pageLoadStatus = (String)js.executeScript("return document.readyState");

					} while ( !pageLoadStatus.equals("complete") );

					System.out.println("Page Loaded."); 

			 
				Thread.sleep(5000);
			
				 logger.info("url driver using:"+driver.getCurrentUrl());
				 
				 String pageSource=driver.getPageSource();  
			// if(pageSource.contains(cssSel.getCssSelector()))
				/*		{
				logger.info("#####with browser###");
			 	logger.info(pageSource);
				logger.info("########");
						}
						*/
						
			} catch (Exception e) {
				logger.log(Level.ERROR,e.getMessage(),e);
			}
			

				logger.info("+++++++++row = "+(rowCount++)+"+++++++++++++");
				
				
				System.out.println(cssSel.getCssSelector().trim());
				if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelector() != "0")) {
					

					logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector());
					
					result1 =  getElement(driver,cssSel.getCssSelector(),cssSel.getUrl());
					if (result1!=null) {
						tagValue=extractNumber(result1.getAttribute("innerText"));
					 	foundResult=1;
						if(tagValue>0)
						{
						logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector() + ">>" + tagValue);
						resultFound = "True";
						}
						else if(tagValue==0)
						{
							resultFound = "False";
						}
					}
					else
					{
						foundResult=0;
					}

				}
				if(foundResult==0)
				{
					
					if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelectorNot() != "0")) {
						 
					 
						result2 = getElement(driver,cssSel.getCssSelectorNot(),cssSel.getUrl());
						if (result2!=null)
		                {
							resultFound = "false";
		                	
		                }
						else
						{
							resultFound = "Error";
							
						}
						System.out.println("resultFound:"+resultFound);
		 			
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
				logger.log(Level.ERROR,e.getMessage(),e);
			}
			try {
				
				ExcelReader.writeToExcel(outputFile, 0, url, 0, Rowno);
				ExcelReader.writeToExcel(outputFile, 1, resultFound, 0, Rowno);
				
				 
			} catch (IOException e) {
				logger.log(Level.ERROR,e.getMessage(),e);
			}
			 catch (Exception e) {
					logger.log(Level.ERROR,e.getMessage(),e);
				}
			
		}
		try
		{
		driver.manage().deleteAllCookies();
		driver.close();
		SSLEmail.SendMail(outputFile);
		}
		catch(Exception e)
		{
			logger.log(Level.ERROR,e.getMessage(),e);
		}
		
		
		logger.info("=============URL Count Process  with  Browser ends=============");
	}
		public static int   extractNumber(final String str) {                
			int number=0;
			String regex="[^0-9]";
			String numStr="";
			Pattern pattern = Pattern.compile(regex);
			try
			{
				numStr = pattern.matcher(str).replaceAll("");
				if(Character.isDigit(numStr.charAt(0)))
				{
				  number=Integer.parseInt(numStr);
				}
			}
			catch(Exception e)
			{
				logger.log(Level.ERROR,e.getMessage(),e);
				number=1;
			}
		   return  number; 
		}
		
	public static WebElement getElement(ChromeDriver driver,String selector,String url)
	{
		WebElement webElement=null;
		try
		{
			//selector="div#resultsFoundMessage";
			//List<WebElement> elements = driver.findElements(By.cssSelector(selector));
		 webElement = driver.findElement(By.cssSelector(selector));
		 logger.info("url driver using:"+driver.getCurrentUrl());
		logger.info("tag and value :"+selector+" : "+webElement.getAttribute("id")+":"+webElement.getTagName()+":"+webElement.getAttribute("innerText"));
		}
		catch (org.openqa.selenium.NoSuchElementException e) {
			logger.log(Level.ERROR,e.getMessage(),e);
			logger.info("\n\n 1 No Such Element - Result not found for url :[" + url + "]>>> CSS Selector:"
					+ selector);
			 
			
		} catch (Exception e) {
			 
			logger.log(Level.ERROR,e.getMessage(),e);
			logger.info("\n\n1 Error in searching - Result not found for url :[" + url + "]>>> CSS Selector:"
					+ selector);

		}
		return webElement;
	}
	
}
