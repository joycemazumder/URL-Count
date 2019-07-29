package com.urlcount.utils;

import org.apache.commons.lang3.StringUtils;

public class CssSelectorApp {

	
	public static String getSelectorName(String selector)
	{
		 
		if(selector.contains("#"))
		{
		 
			return StringUtils.split(selector, "#")[1];
		}
		else if(selector.contains("."))
		{
		 
			return StringUtils.split(selector, ".")[1];
		}
		else
			return "";
	}
	public static void main(String[] args) {
		String selector="aaa._Cbbbb";
		String delim="";
		if(selector.contains("#"))
		{
			delim="#";
		}
		else if(selector.contains("."))
		{
			delim=".";
		}
	//	StringUtils.split(selector, delim);
		System.out.println(StringUtils.split(selector, delim)[1]);
	}

}
