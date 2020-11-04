package com.fridge.skl.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session implements java.io.Serializable {


	private static final long serialVersionUID = 1L;

	private boolean isNew;

	private String sessionId;

	private Map<String, String> attributes = new ConcurrentHashMap<String, String>();

	public Session(boolean isNew, String sessionId, Map<String, String> attributes) {
		super();
		this.isNew = isNew;
		this.sessionId = sessionId;
		this.attributes = attributes;
	}

	public Session() {
	}


	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
