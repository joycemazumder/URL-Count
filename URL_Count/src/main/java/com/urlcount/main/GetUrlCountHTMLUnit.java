package com.urlcount.main;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.urlcount.object.CSSSelector;
import com.urlcount.utils.ExcelReader;

public class GetUrlCountHTMLUnit {

	private static final Logger logger = LogManager.getLogger(GetUrlCountHTMLUnit.class);
	private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);
	public static void execute( String path) {

		logger.info("=============URL Count Process start with NO Browser=========");

		ExcelReader eReader = null;

		logger.info("Input folder path:" + path);

		String inputFileStr = path + File.separator + "input" + File.separator + "InputFile.xlsx";

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
		urlList = ExcelReader.readExcel(inputFileStr, "URL", 0);
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
			cssList.add(new CSSSelector(css.getUrl(), css.getCssSelector(), css.getCssSelectorNot(), fname));
		});

		/*
		 * urlmap.forEach((k, v) -> { cssList.add(new CSSSelector(k, v, fname)); });
		 */
		String url = "";
		File outputFile = null;
		for (CSSSelector cssSel : cssList) {
			int foundResult = 0;
			int notFoundResult = 0;
			int tagValue=0;
			String found = "";
			String found2 = "";
			String tagoutput = "";
			WebElement element=null;
			WebElement result1 = null;
			WebElement result2 = null;
			String resultFound="";
			DomNode domNode=null;
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
					
					domNode=getNode(cssSel.getUrl().trim(),cssSel.getCssSelector());
					if (domNode!=null) {

						tagValue=getNodeValue(domNode);
						foundResult=1;
						if(tagValue>0)
						{
						logger.info("url :[" + url + "]>>> CSS Selector:" + cssSel.getCssSelector() + ">>" + tagValue);
						resultFound = "True";
						}
						else
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
						//By by = By.cssSelector(cssSel.getCssSelectorNot());
						//result2 = driver.findElement(by);
						domNode=getNode(cssSel.getUrl().trim(),cssSel.getCssSelectorNot());
						if (domNode!=null)
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
				logger.log(Level.ERROR, e.getMessage(), e);
			}
			try {
				ExcelReader.writeToExcel(outputFile, 0, url, 0, Rowno);
				ExcelReader.writeToExcel(outputFile, 1, resultFound, 0, Rowno);
				ExcelReader.writeToExcel(outputFile, 2, cssSel.getCssSelector(), 0, Rowno);
				ExcelReader.writeToExcel(outputFile, 3, cssSel.getCssSelectorNot(), 0, Rowno);
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
	public static DomNode getNode(String url ,String cssSelector)
	{
		DomNode domNode=null;
		int nodeExists=0;
		 
		try {
			List<HtmlAnchor> searchResults = new ArrayList<>();
			HtmlPage page = webClient
					.getPage(url);
			
			Thread.sleep(5000);
			 domNode=page.querySelector(cssSelector);
			System.out.println("url:["+page.getBaseURI()+"] + Selector :["+cssSelector+  "]Result  domNode==null:"+(domNode==null));
			
			
			

		} catch (FailingHttpStatusCodeException | IOException | InterruptedException   e) {
			
			e.printStackTrace();
			 
		}
		catch (Exception   e) {
			
			e.printStackTrace();
			 
		}
		return domNode;
	}
	 
	public static int getNodeValue(DomNode domNode)
	{
		String tagValue="";
		if(domNode!=null)
		{
			tagValue=domNode.asText();
			String regex = "(.)*(\\d)(.)*";      
			Pattern pattern = Pattern.compile(regex);
			 
			boolean containsNumber = pattern.matcher(tagValue).matches();
			System.out.println(containsNumber);
			if(containsNumber)
			{
				int number=extractNumber(tagValue);
				System.out.println(">>>>tag value:"+number);
				return number;
			}
			
		}
		return 0;
	}
	
	public static int   extractNumber(final String str) {                

		String regex="[^0-9]";
		String numStr="";
		Pattern pattern = Pattern.compile(regex);
		
		numStr = pattern.matcher(str).replaceAll("");
	 	System.out.println(numStr);
	   return  Integer.parseInt(numStr); 
	}

}
