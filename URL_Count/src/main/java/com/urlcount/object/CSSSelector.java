package com.urlcount.object;

public class CSSSelector {

	private String url;

	private String cssSelector;

	private String cssSelectorNot;

	private String outputfile;

	public CSSSelector(String url, String cssSelector, String cssSelectorNot, String outputfile) {
		super();
		this.url = url;
		this.cssSelector = cssSelector;
		this.cssSelectorNot = cssSelectorNot;
		this.outputfile = outputfile;
	}

	public String getCssSelectorNot() {
		return cssSelectorNot;
	}

	public void setCssSelectorNot(String cssSelectorNot) {
		this.cssSelectorNot = cssSelectorNot;
	}

	public CSSSelector() {
		// TODO Auto-generated constructor stub
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
