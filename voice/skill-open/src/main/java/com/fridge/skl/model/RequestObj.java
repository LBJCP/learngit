package com.fridge.skl.model;

import lombok.Data;

//注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
@Data
public class RequestObj implements java.io.Serializable {


	private static final long serialVersionUID = 1L;

	public static String VERSION = "1.0";

	private String version = "1.0.1";

	private Session session;

	private Context context;

	private Request request;


}
