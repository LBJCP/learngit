package com.fridge.skl.model.response;

import com.fridge.skl.model.SpeechTypeEnum;

public class Speech implements java.io.Serializable {


	private static final long serialVersionUID = 1L;

	private SpeechTypeEnum type;

	private String content;



	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SpeechTypeEnum getType() {
		return type;
	}

	public void setType(SpeechTypeEnum type) {
		this.type = type;
	}

}
