package com.fgsecure.ujoolt.app.adapter;

public class Rejolter {
	private String nick;
	private boolean ujoolt;

	public Rejolter(String nick, boolean ujoolt) {
		this.nick = nick;
		this.ujoolt = ujoolt;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public boolean isUjoolt() {
		return ujoolt;
	}

	public void setUjoolt(boolean ujoolt) {
		this.ujoolt = ujoolt;
	}
}
