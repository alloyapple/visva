package com.japanappstudio.IDxPassword.info;

public class MasterPassword {

	private int masterPWId;
	private String masterPW;
	private String lastModified;
	
	public MasterPassword(int masterPWId, String masterPW, String lastModified) {
		super();
		this.masterPWId = masterPWId;
		this.masterPW = masterPW;
		this.lastModified = lastModified;
	}

	public int getMasterPWId() {
		return masterPWId;
	}

	public void setMasterPWId(int masterPWId) {
		this.masterPWId = masterPWId;
	}

	public String getMasterPW() {
		return masterPW;
	}

	public void setMasterPW(String masterPW) {
		this.masterPW = masterPW;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	
}
