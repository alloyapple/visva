package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class FriendList {
	@SerializedName("Result")
	private ArrayList<FriendItem> Result;
	
	public FriendList(){
		
	}

	public ArrayList<FriendItem> getResult() {
		return Result;
	}

	public void setResult(ArrayList<FriendItem> result) {
		Result = result;
	}

}
