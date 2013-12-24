package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GiftRedeemList {
	@SerializedName("Result")
	private GiftRedeemItem Result;

	public GiftRedeemList() {

	}

	public GiftRedeemItem getResult() {
		return Result;
	}

	public void setResult(GiftRedeemItem result) {
		Result = result;
	}

}
