package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class MerchCampaignList {
	@SerializedName("Result")
	private ArrayList<MerchCampaignItem> Result;

	public ArrayList<MerchCampaignItem> getResult() {
		return Result;
	}

	public void setResult(ArrayList<MerchCampaignItem> result) {
		Result = result;
	}

	public MerchCampaignList() {

	}

}
