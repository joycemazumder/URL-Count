package com.urlcount.utils;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CheckURL {
	private static final Logger logger = LogManager.getLogger(CheckURL.class);
	public static void main(String[] args) {
		String url="https://www.kohlsxtrw233.com/";
		URL urlh=null;
		try {
			urlh = new URL(url);
			urlh.toURI();
			//String homeurl = urlh.getProtocol() + "://" + urlh.getHost() ;
			final URL urlt = new URL(url);
			 
			HttpURLConnection huc = (HttpURLConnection) urlt.openConnection();
			 huc.connect();
				
			/* int responseCode = huc.getResponseCode();
			System.out.println(responseCode);
			if (responseCode != 404) {
			System.out.println("GOOD");
			} else {
			System.out.println("BAD");
			}*/
			
			//validURL(homeurl);
		} catch (Exception e) {
			logger.log(Level.ERROR, e.getMessage(), e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			System.out.println("end...");
		}
		
	}
	public static boolean validURL(String url)
	{
		boolean foundURL=false;
		
		 try
	        {
			 
			 final URL urlt = new URL(url);
			 HttpURLConnection huc = (HttpURLConnection) urlt.openConnection();
			 huc.connect();
			 foundURL=true;
				
				
				/*
				if (responseCode != 404) {
					foundURL=true;
				} 
			 */
			 
			 
			/*// System.out.println( getStatusCode ( url));
	             InetAddress inetAddress = InetAddress.getByName(url);
	            // show the Internet Address as name/address
	            System.out.println(inetAddress.getHostName() + " " + inetAddress.getHostAddress());
			 foundURL=true;*/
	        }
	        catch (Exception e )
	        {
	        	logger.log(Level.ERROR,"ERROR: Cannot access "+ e.getMessage(), e);
	        }
		return foundURL;
	}
	 

}
