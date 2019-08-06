package com.urlcount.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.urlcount.main.GetUrlCount;

public class GetUrlCountUtil {

	static Properties prop = new Properties();
	private static final Logger logger = LogManager.getLogger(GetUrlCountUtil.class);
	private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);


	public static boolean checkValidURL(String link, ChromeDriver driver, String selector) {
		boolean isHome = false;
		WebElement webElement=null;
		String pageSource="";
		boolean isRedirect=false;
		try {
		/*	URL url = new URL(link);
			String homeurl = url.getProtocol() + "://" + url.getHost();*/
			
			isHome=CheckURL.validURL(link);
			if(!isHome)
			{
			isRedirect= isRedirecting(driver,link);
			 return isRedirect;
			}
			// driver.navigate().to(link);
			 			//isHome = true;
			//webElement = getElement(driver, url, selector);
			/*webElement =getElement(driver, homeurl, selector) ;
			if (webElement != null) {
				isHome = true;
			}*/

			/*
			 * HttpURLConnection con = (HttpURLConnection) new
			 * URL(homeurl).openConnection(); con.setRequestMethod("HEAD");
			 * System.out.println(con.getResponseCode());
			 */
			 
		}
		 catch (Exception e) {

			e.printStackTrace();

		}

		return isHome;

	}

	public static DomNode getNode(String url, String cssSelector, WebClient webClient) {
		DomNode domNode = null;
		int nodeExists = 0;

		try {
			List<HtmlAnchor> searchResults = new ArrayList<>();
			HtmlPage page = webClient.getPage(url);

			Thread.sleep(5000);
			domNode = page.querySelector(cssSelector);
			System.out.println("url:[" + page.getBaseURI() + "] + Selector :[" + cssSelector + "]Result  domNode==null:"
					+ (domNode == null));

		} catch (FailingHttpStatusCodeException | IOException | InterruptedException e) {

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return domNode;
	}

	public static Properties loadProperties() {

		try (InputStream input = GetUrlCount.class.getClassLoader().getResourceAsStream("config.properties")) {

			if (input == null) {
				logger.info("Sorry, unable to find config.properties");

			}
			prop.load(input);

		} catch (IOException ex) {
			logger.info(ex.getMessage());

		}
		return prop;

	}

	public static String getFilePath(String path) {
		boolean valid = false;
		File inputfile = new File(path + File.separator + "input");
		File outputfile = new File(path + File.separator + "output");

		if (inputfile.exists() && inputfile.isDirectory()) {
			System.out.println("exist:" + path + File.separator + "input");
			valid = true;
		} else {
			System.out.println("Input Directory path is not valid");

			System.exit(0);

		}

		if (outputfile.exists() && outputfile.isDirectory()) {
			System.out.println("exist:" + path + File.separator + "outputfile");
			valid = true;
		} else {
			System.out.println("Output Directory path is not valid");
			valid = false;
			System.exit(0);

		}

		return path;

	}
	public static boolean isRedirecting(ChromeDriver driver,String url)
	{
		boolean redirecting=false;
		
		try {

			driver.manage().deleteAllCookies();

			driver.get(url);
			 
			String pageLoadStatus = null;
			JavascriptExecutor js = null;
			do {

				js = (JavascriptExecutor) driver;

				pageLoadStatus = (String) js.executeScript("return document.readyState");

			} while (!pageLoadStatus.equals("complete"));

			System.out.println("Page Loaded.");

			Thread.sleep(5000);
			String currenturl=driver.getCurrentUrl().trim();
			logger.info(" Redirecting url the driver using:" + driver.getCurrentUrl());
			redirecting=!url.trim().equalsIgnoreCase(currenturl);

		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage(), e);
		}

		
		
		
		return redirecting;
	}
	public static String getPageSource(ChromeDriver driver,String url)
	{
		String pageSource="";
		
		try {

			driver.manage().deleteAllCookies();

			driver.get(url);
			 
			String pageLoadStatus = null;
			JavascriptExecutor js = null;
			do {

				js = (JavascriptExecutor) driver;

				pageLoadStatus = (String) js.executeScript("return document.readyState");

			} while (!pageLoadStatus.equals("complete"));

			System.out.println("Page Loaded.");

			

			logger.info("url driver using:" + driver.getCurrentUrl());

			  pageSource = driver.getPageSource();
			  Thread.sleep(5000);
			// if(pageSource.contains(cssSel.getCssSelector()))
			/*
			 * { logger.info("#####with browser###"); logger.info(pageSource);
			 * logger.info("########"); }
			 */

		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage(), e);
		}

		
		
		
		return pageSource;
	}
	public static WebElement getElement(ChromeDriver driver,  String url,String selector) {
		WebElement webElement = null;
		 
		try { 
			// selector="div#resultsFoundMessage";
		//	 	 logger.info(pageSource);
			 // List<WebElement> elements = driver.findElements(By.cssSelector(selector));
			 
			 
			webElement = driver.findElement(By.cssSelector(selector));
			 
			logger.info("tag and value :" + selector + " : " + webElement.getAttribute("id") + ":"
					+ webElement.getTagName() + ":" + webElement.getAttribute("innerText"));
		} catch (org.openqa.selenium.NoSuchElementException e) {
			logger.log(Level.ERROR, e.getMessage(), e);
			logger.info("\n\n 1 No Such Element - Result not found for url :[" + url + "]>>> CSS Selector:" + selector);

		} catch (Exception e) {

			logger.log(Level.ERROR, e.getMessage(), e);
			logger.info(
					"\n\n1 Error in searching - Result not found for url :[" + url + "]>>> CSS Selector:" + selector);

		}
		return webElement;
	}
	public static String getSelectorName(String selector)
	{
		 
		if(selector.contains("#"))
		{
			String s="";
			s=StringUtils.replace(StringUtils.split(selector, "#")[1],".", " ");
			return s;
			
			
		}
		else if(selector.contains("."))
		{
			StringBuilder r=new StringBuilder("");
		 String s[]=StringUtils.split(selector, ".");
			 for(int i=1;i<s.length;i++)
			 {
				 r.append(s[i]+" ");
			 }
			return r.toString().trim();
		}
		else
			return "";
	}
	public static void main(String[] args) {
		try {
			String selector="a.b.c.d";
			
			
			System.out.println(getSelectorName(selector));
			
			//String link = "https://www.macys.com/shop/featured/aZZZnne-kZZZlein-jewelry";
			// System.out.println(getFilePath(args[0]));
			// IsHomePage(link);
			// getConsoleInput();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
