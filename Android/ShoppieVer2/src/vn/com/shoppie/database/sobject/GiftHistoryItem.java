package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class GiftHistoryItem {
	@SerializedName("txnId")
	private String txnId;
	@SerializedName("giftName")
	private String giftName;
	@SerializedName("pieQty")
	private String pieQty;
	@SerializedName("txnDate")
	private String txnDate;
	@SerializedName("status")
	private String status;
	public String getTxnId() {
		return txnId;
	}
	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}
	public String getGiftName() {
		return giftName;
	}
	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}
	public String getPieQty() {
		return pieQty;
	}
	public void setPieQty(String pieQty) {
		this.pieQty = pieQty;
	}
	public String getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(String txnDate) {
		this.txnDate = txnDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
