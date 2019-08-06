package com.urlcount.main;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.urlcount.utils.ConfigureLog;
import com.urlcount.utils.GetUrlCountUtil;

public class GetUrlCount {

	 

	public static void main(String[] args) {
		String logpath=args[0]+File.separator+"Log\\";
		
		ConfigureLog.initLogging(logpath);
		Logger logger = LogManager.getLogger(GetUrlCount.class);
	//	PropertyConfigurator.configure(GetUrlCount.class.getClassLoader().getResourceAsStream("log4j.properties"));
		Properties prop=GetUrlCountUtil.loadProperties();
		
	 
		WebDriver driver=null;
		try {
			String path=GetUrlCountUtil.getFilePath(args[0]);
			logger.info("=============Process Starts=========");
			String driverpath = prop.getProperty("driverfile");
			getConsoleInput(path);
		} catch (Exception e1) {
			logger.error(e1.getMessage());
		}
		
		
		logger.info("=============Process Ends=============");
	}
	
	public static void getConsoleInput(String folderpath) throws Exception {
	Logger logger = LogManager.getLogger(GetUrlCount.class);
		WebDriver driver = null;
		ChromeDriver chromedriver=null;

			try {
			//System.out.println("Select following options....");
			System.out.println("Executing with browser");
			//System.out.println("2. Input value 2 for executing without browser");
			//System.out.println("Input value :");
			//Scanner in = new Scanner(System.in);
			//String s = in.nextLine();
			String s ="1";
			
			if (s.trim().equalsIgnoreCase("1")) {
				 
				
				System.setProperty("webdriver.chrome.driver", folderpath+File.separator+"input"+File.separator+"chromedriver.exe");
				
				ChromeOptions options = new ChromeOptions();
				
				
				
				
				//options.setHeadless(true);
				options.addArguments("--window-size=-1,-1");
				//options.addArguments("--disable-gpu");
				options.addArguments("--disable-extensions");
				
				options.setExperimentalOption("useAutomationExtension", false);
				//options.addArguments("--start-maximized");
				//options.addArguments("--headless");;
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
				capabilities.setCapability("chrome.switches", Arrays.asList("--incognito"));
				options.merge(capabilities);
				
				chromedriver = new ChromeDriver(options);
				Point p = new Point (1600,1500);

				chromedriver.manage().window().setPosition(p);
/*				System.out.println(folderpath+File.separator+"input"+File.separator+"geckodriver.exe");
				FirefoxBinary firefoxBinary = new FirefoxBinary();
				   firefoxBinary.addCommandLineOptions("--headless");
				   System.setProperty("webdriver.gecko.driver",folderpath+File.separator+"input"+File.separator+"geckodriver.exe");   
				   FirefoxOptions firefoxOptions = new FirefoxOptions();
				   firefoxOptions.setBinary(firefoxBinary);
				FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxOptions);
*/
				
				GetUrlCountWithBrowser.execute(chromedriver, folderpath); 

			} else if (s.trim().equalsIgnoreCase("2")) {
				System.out.println("Running Without browser");
				/*
				
									Proxy proxy = new Proxy(); 
					proxy.setHttpProxy("yoururl:portno"); 
					proxy.setSslProxy("yoururl:portno"); 
					
					DesiredCapabilities capabilities = DesiredCapabilities.chrome(); 
					capabilities.setCapability("proxy", proxy); 
					
					ChromeOptions options = new ChromeOptions(); 
					options.addArguments("start-maximized"); 
					
					capabilities.setCapability(ChromeOptions.CAPABILITY, options); 
					
					driver = new ChromeDriver(capabilities);

				
				
				 
				System.setProperty("webdriver.chrome.driver", folderpath+File.separator+"input"+File.separator+"chromedriver_win.exe");
				
			 	
			 	Proxy proxy = new Proxy(); 
				proxy.setHttpProxy("http://joyce.mazumder:kafka#@na9-s500:8080"); 
				proxy.setSslProxy("http://joyce.mazumder:kafka#@na9-s500:8080"); 

				DesiredCapabilities capabilities = DesiredCapabilities.chrome(); 
				//capabilities.setCapability("proxy", proxy); 
				capabilities.setJavascriptEnabled(true);
				ChromeOptions options = new ChromeOptions(); 
				//options.addArguments("start-maximized"); 

				 
			 
				//driver = new HtmlUnitDriver();
				
				 
				options.setHeadless(true);
				//options.addArguments("--window-size=-1,-1");
				//options.addArguments("--disable-gpu");
				options.addArguments("--disable-extensions");
				options.setExperimentalOption("useAutomationExtension", false);
				//options.addArguments("--start-maximized");
				options.addArguments("--headless");;
			 
				capabilities.setCapability(ChromeOptions.CAPABILITY, options);
				options.merge(capabilities);
				chromedriver = new ChromeDriver(options);
				
				 
				HtmlUnitDriver htmlUnitDriver = new HtmlUnitDriver(BrowserVersion.CHROME);
				*/
				 
				//GetUrlCountHTMLUnitCsv.execute( folderpath);
				 
				//GetUrlCountNoBrowser.execute(htmlUnitDriver, folderpath);
				GetUrlCountHTMLUnit.execute(folderpath);

			} else {
				System.out.println("Invalid choice...");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Occured .. terminating:"+e);
		}
		 

	}

}
