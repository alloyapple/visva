package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GiftHistoryList {
	@SerializedName("Result")
	private ArrayList<GiftHistoryItem> Result;

	public ArrayList<GiftHistoryItem> getGifts() {
		return Result;
	}

	public void setGifts(ArrayList<GiftHistoryItem> gifts) {
		Result = gifts;
	}

	public GiftHistoryList() {

	}
}
