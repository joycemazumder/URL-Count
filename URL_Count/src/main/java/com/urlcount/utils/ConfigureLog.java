package com.urlcount.utils;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
 
public class ConfigureLog {
 
    public static void initLogging(String logpath) {
        try {
			// creates pattern layout
			PatternLayout layout = new PatternLayout();
			String conversionPattern = "[%-5p] %d %M - %m%n";
			layout.setConversionPattern(conversionPattern);
 
			// creates console appender
			ConsoleAppender consoleAppender = new ConsoleAppender();
			consoleAppender.setLayout(layout);
			consoleAppender.activateOptions();
			System.out.println(logpath+"urlcount.log");
			
 
			// creates file appender
			DailyRollingFileAppender rollingFileAppender = new DailyRollingFileAppender();
			
			rollingFileAppender.setFile(logpath+File.separator+"urlcount.log");
			rollingFileAppender.setDatePattern("'.'yyyy-MM-dd");
			rollingFileAppender.setLayout(layout);
			 rollingFileAppender.activateOptions();
			 
 
			// configures the root logger
			Logger rootLogger = Logger.getRootLogger();
			rootLogger.setLevel(Level.INFO);
			rootLogger.addAppender(consoleAppender);
			rootLogger.addAppender(rollingFileAppender);
		} catch (Exception e) {
			File logfile=new File(logpath+File.separator+"urlcount.log");
			e.printStackTrace();
		}
       
    }
    
    public static void main(String args[])
    {
    	/*initLogging();
    	  // creates a custom logger and log messages
        Logger logger = Logger.getLogger(ProgrammaticLog4jExample.class);
        logger.debug("1this is a debug log message");
        logger.info("1this is a information log message");
        logger.warn("1this is a warning log message");
        logger.error("1this is a error log message");*/
    }
}
