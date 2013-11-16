package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class UserInfoList {
	@SerializedName("Result")
	private ArrayList<UserDataInfo> Result;

	public ArrayList<UserDataInfo> getResult() {
		return Result;
	}

	public void setResult(ArrayList<UserDataInfo> result) {
		Result = result;
	}

	public UserInfoList() {
		// TODO Auto-generated constructor stub
	}
}
