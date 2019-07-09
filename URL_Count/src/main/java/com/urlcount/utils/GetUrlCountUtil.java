package com.urlcount.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.urlcount.main.GetUrlCount;

public class GetUrlCountUtil {

	static Properties prop = new Properties();
	private static final Logger logger = LogManager.getLogger(GetUrlCountUtil.class);

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



	public static void main(String[] args) {
		try {
			System.out.println(getFilePath(args[0]));

			// getConsoleInput();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
