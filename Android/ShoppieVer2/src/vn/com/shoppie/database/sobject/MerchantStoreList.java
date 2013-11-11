package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MerchantStoreList {
	@SerializedName("Result")
	private ArrayList<MerchantStoreItem> Result;

	public ArrayList<MerchantStoreItem> getResult() {
		return Result;
	}

	public void setResult(ArrayList<MerchantStoreItem> result) {
		Result = result;
	}

	public MerchantStoreList() {

	}
}
