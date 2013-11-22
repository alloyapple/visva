package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

public class ShoppieFriendList {
	private ArrayList<ShoppieFriendItem> Result = new ArrayList<ShoppieFriendItem>();

	public ShoppieFriendList() {

	}

	public ArrayList<ShoppieFriendItem> getResult() {
		return Result;
	}

	public void setResult(ArrayList<ShoppieFriendItem> result) {
		Result = result;
	}

}
