package com.urlcount.urlmail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestApp {
	public static void main(String[] args) {
		
		Properties prop = new Properties();
         
        InputStream in;
		try {
			
			in = new FileInputStream("C:\\Project\\input" + "\\mail.properties");
			prop.load(in);
	        in.close();
	         prop.forEach((k,v)->System.out.println(k+":"+v));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
	}
}
