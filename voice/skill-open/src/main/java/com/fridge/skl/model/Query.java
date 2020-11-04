package com.fridge.skl.model;

public class Query {

	public static final String TYPE_TEXT = "text";

	private String type;
	private String content;

	public Query(String type, String content) {
		super();
		this.type = type;
		this.content = content;
	}

	public Query() {
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
