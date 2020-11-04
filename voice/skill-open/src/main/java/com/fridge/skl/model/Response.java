package com.fridge.skl.model;

import com.fridge.skl.model.response.Command;
import com.fridge.skl.model.response.Speech;

public class Response implements java.io.Serializable {


	private static final long serialVersionUID = 1L;

	private Speech speech;

	private Command command;

	private boolean shouldEndSession;

	private boolean valid;


	public Speech getSpeech() {
		return speech;
	}

	public void setSpeech(Speech speech) {
		this.speech = speech;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isShouldEndSession() {
		return shouldEndSession;
	}

	public void setShouldEndSession(boolean shouldEndSession) {
		this.shouldEndSession = shouldEndSession;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}
}
