package com.urlcount.main;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.LogFactory;
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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.urlcount.object.CSSSelector;
import com.urlcount.utils.CSVReader;
import com.urlcount.utils.ExcelReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class GetUrlCountHTMLUnitCsv {

	private static final Logger logger = LogManager.getLogger(GetUrlCountHTMLUnitCsv.class);
	private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);
	public static void execute( String path) {

		logger.info("=============URL Count Process start with NO Browser=========");

		ExcelReader eReader = null;

		logger.info("Input folder path:" + path);

		String inputFileStr = path + File.separator + "input" + File.separator + "InputFilecsv.csv";

		String outputFileStr = path + File.separator + "output" + File.separator;

		LinkedHashMap<String, String> urlmap = new LinkedHashMap<String, String>();
		ArrayList<CSSSelector> cssList = new ArrayList<CSSSelector>();
		ArrayList<CSSSelector> urlList = new ArrayList<CSSSelector>();

		//eReader = PageFactory.initElements(driver, ExcelReader.class);
		//driver.manage().window().maximize();
		// MM_dd_yy_HH_mm_ss YYYYMMDDHHmm
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYYMMdd2HHmm");
		LocalDateTime now = LocalDateTime.now();
		String tm = dtf.format(now);

		String fname = outputFileStr + "Output" + tm + ".xlsx";
		ExcelReader.createExcel(fname);

		//eReader = PageFactory.initElements(driver, ExcelReader.class);
		try {
			urlList = CSVReader.readCSVFiles(inputFileStr);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int totalLength = urlmap.size();
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		//webClient.getOptions().setTimeout(2000);
		webClient.getOptions().setUseInsecureSSL(true);
		// overcome problems in JavaScript
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());

		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		 Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		 Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		 Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.StrictErrorReporter")
				.setLevel(Level.OFF);
		 Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject")
				.setLevel(Level.OFF);
		 Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument")
				.setLevel(Level.OFF);
		 Logger.getLogger("com.gargoylesoftware.htmlunit.html.HtmlScript").setLevel(Level.OFF);
		  Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.WindowProxy")
				.setLevel(Level.OFF);
		 Logger.getLogger("org.apache").setLevel(Level.OFF);
	
		urlList.forEach((CSSSelector css) -> {
	//		cssList.add(new CSSSelector(css.getUrl(), css.getCssSelector(), css.getCssSelectorNot(), fname));
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
		 

			} catch (Exception e) {
				logger.log(Level.ERROR, e.getMessage(), e);
			}
			 

				logger.info("++++++++++++++++1++++++10 sec++++++++");

				System.out.println(cssSel.getCssSelector().trim());
				if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelector() != "0")) {
					

					logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector());
					
					resultFound=checkNodeExists(cssSel.getUrl().trim(),cssSel.getCssSelector());

				}
				if (resultFound.equalsIgnoreCase("true")) {

					
					logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector() + ">>" + tagoutput);
					found = "True";
				}
			 

			 
			 

				logger.info("++++++++++++++2++++++++++++++++");
				if (!found.equalsIgnoreCase("True")) {
					if ((cssSel.getUrl().trim().length() > 4) && (cssSel.getCssSelectorNot() != "0")) {
						//By by = By.cssSelector(cssSel.getCssSelectorNot());
						//result2 = driver.findElement(by);
						resultFound=checkNodeExists(cssSel.getUrl().trim(),cssSel.getCssSelectorNot());
						System.out.println("resultFound:"+resultFound);
					}
					if (resultFound.equalsIgnoreCase("true")) {
						
						logger.info(
								"url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelectorNot() + ">>" + tagoutput);
						found2 = "True";
					}
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
	//	driver.manage().deleteAllCookies();
	//	driver.close();

		logger.info("=============URL Count Process  with NO Browser ends=============");
	}
	public static String checkNodeExists(String url ,String cssSelector)
	{
		String nodeExists="";
		
		try {
			List<HtmlAnchor> searchResults = new ArrayList<>();
			HtmlPage page = webClient
					.getPage(url);
			Thread.sleep(5000);
			DomNode domNode=page.querySelector(cssSelector);
			System.out.println("url:["+url+"] + Selector :["+cssSelector+  "]Result  domNode==null:"+(domNode==null));
			nodeExists=(domNode==null)?"false":"true";
			

		} catch (FailingHttpStatusCodeException | IOException | InterruptedException   e) {
			
			e.printStackTrace();
			nodeExists="Error";
		}
		catch (Exception   e) {
			
			e.printStackTrace();
			nodeExists="Error";
		}
		return nodeExists;
	}

}
