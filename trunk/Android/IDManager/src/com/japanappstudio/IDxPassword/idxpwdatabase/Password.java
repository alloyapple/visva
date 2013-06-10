package com.japanappstudio.IDxPassword.idxpwdatabase;

public class Password {
	// all variables
	private int passwordId;
	private int eId;
	private String titleNameId;
	private String password;

	public Password(int passwordId, int eId, String titleNameId, String password) {
		super();
		this.passwordId = passwordId;
		this.eId = eId;
		this.titleNameId = titleNameId;
		this.password = password;
	}

	public Password() {

	}

	public int getPasswordId() {
		return passwordId;
	}

	public void setPasswordId(int passwordId) {
		this.passwordId = passwordId;
	}

	public int geteId() {
		return eId;
	}

	public void seteId(int eId) {
		this.eId = eId;
	}

	public String getTitleNameId() {
		return titleNameId;
	}

	public void setTitleNameId(String titleNameId) {
		this.titleNameId = titleNameId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
