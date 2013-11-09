package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MerchantCategoryList {
	@SerializedName("Result")
	private ArrayList<MerchantCategoryItem> Result;

	public MerchantCategoryList() {

	}

	public ArrayList<MerchantCategoryItem> getResult() {
		return Result;
	}

	public void setResult(ArrayList<MerchantCategoryItem> result) {
		Result = result;
	}
}
