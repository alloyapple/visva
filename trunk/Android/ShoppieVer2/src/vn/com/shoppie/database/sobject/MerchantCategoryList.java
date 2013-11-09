package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MerchantCategoryList {
	@SerializedName("Result")
	private ArrayList<MerchantCategory> Result;

	public MerchantCategoryList() {

	}

	public ArrayList<MerchantCategory> getResult() {
		return Result;
	}

	public void setResult(ArrayList<MerchantCategory> result) {
		Result = result;
	}
}
