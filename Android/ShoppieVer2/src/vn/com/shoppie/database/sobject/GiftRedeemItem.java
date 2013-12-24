package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class GiftRedeemItem {
	@SerializedName("txnId")
	private String txnId;
	@SerializedName("merchName")
	private String merchName;
	@SerializedName("storeAddress")
	private String storeAddress;
	@SerializedName("giftName")
	private String giftName;
	@SerializedName("pieQty")
	private int pieQty;

	public GiftRedeemItem(String txnId, String merchName, String storeAddress,
			String giftName, int pieQty) {
		super();
		this.txnId = txnId;
		this.merchName = merchName;
		this.storeAddress = storeAddress;
		this.giftName = giftName;
		this.pieQty = pieQty;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getMerchName() {
		return merchName;
	}

	public void setMerchName(String merchName) {
		this.merchName = merchName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public int getPieQty() {
		return pieQty;
	}

	public void setPieQty(int pieQty) {
		this.pieQty = pieQty;
	}

}
