package com.fridge.skl.model;

public class ResponseObj implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
    public int length;

    private String version;

	private Session session;

	private Context context;

	private Response response;


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}
}
