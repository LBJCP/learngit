package com.fridge.skl.model;

public class Request implements java.io.Serializable {



	private static final long serialVersionUID = 1L;

	private String type;

	private String requestId;

	private long timestamp;

	private String reason;

	private GuessIntent[] intents;

	private Query query;

	public Request() {
	}

	public Request(String type, String requestId, long timestamp, String reason, GuessIntent[] intents, Query query) {
		super();
		this.type = type;
		this.requestId = requestId;
		this.timestamp = timestamp;
		this.reason = reason;
		this.intents = intents;
		this.query = query;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public GuessIntent[] getIntents() {
		return intents;
	}

	public void setIntents(GuessIntent[] intents) {
		this.intents = intents;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

}
