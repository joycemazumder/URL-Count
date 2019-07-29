package com.urlcount.object;

public class CSSSelector {

	private String url;
	
	private String homeSelectorStr="";

	private String cssSelector;

	private String cssSelectorNot;

	private String outputfile;

	

	public CSSSelector(String url, String homeSelectorStr, String cssSelector, String cssSelectorNot,
			String outputfile) {
		super();
		this.url = url;
		this.homeSelectorStr = homeSelectorStr;
		this.cssSelector = cssSelector;
		this.cssSelectorNot = cssSelectorNot;
		this.outputfile = outputfile;
	}

	public String getHomeSelectorStr() {
		return homeSelectorStr;
	}

	public void setHomeSelectorStr(String homeSelectorStr) {
		this.homeSelectorStr = homeSelectorStr;
	}

	public String getCssSelectorNot() {
		return cssSelectorNot;
	}

	public void setCssSelectorNot(String cssSelectorNot) {
		this.cssSelectorNot = cssSelectorNot;
	}


	public String getCssSelector() {
		return cssSelector;
	}

	public void setCssSelector(String cssSelector) {
		this.cssSelector = cssSelector;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOutputfile() {
		return outputfile;
	}

	public void setOutputfile(String outputfile) {
		this.outputfile = outputfile;
	}

}
