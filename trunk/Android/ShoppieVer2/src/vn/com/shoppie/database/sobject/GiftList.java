package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GiftList {
	@SerializedName("Gifts")
	private ArrayList<GiftItem> Gifts;

	public ArrayList<GiftItem> getGifts() {
		return Gifts;
	}

	public void setGifts(ArrayList<GiftItem> gifts) {
		Gifts = gifts;
	}

	public GiftList() {

	}
}
