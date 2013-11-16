package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
	@SerializedName("Result")
	private UserDataInfo Result;

	public UserDataInfo getResult() {
		return Result;
	}

	public void setResult(UserDataInfo result) {
		Result = result;
	}

	public UserInfo() {

	}
}
