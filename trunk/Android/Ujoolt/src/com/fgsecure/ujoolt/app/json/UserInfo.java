package com.fgsecure.ujoolt.app.json;

import com.fgsecure.ujoolt.app.define.LoginType;

public class UserInfo {
	private String userId;
	private String userName;
	private LoginType loginType;

	public UserInfo() {
		userId = "";
		userName = "";
	}

	public UserInfo(String userName, String userId, LoginType loginType) {
		this.userId = userId;
		this.userName = userName;
		this.loginType = loginType;
	}

	public UserInfo(String userName, String userId, String loginType) {
		this.userId = userId;
		this.userName = userName;
		this.loginType = LoginType.getLoginType(loginType);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

	public String getStringLoginType() {
		return LoginType.getString(loginType);
	}

	public void setLoginType(String loginType) {
		this.loginType = LoginType.getLoginType(loginType);
	}
}
