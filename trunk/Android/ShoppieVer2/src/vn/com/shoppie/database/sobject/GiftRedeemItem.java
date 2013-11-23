package vn.com.shoppie.database.sobject;

public class GiftRedeemItem {
	private int txnId;
	private String merchName;
	private String storeAddress;
	private String giftName;
	private int pieQty;

	public GiftRedeemItem(int txnId, String merchName, String storeAddress,
			String giftName, int pieQty) {
		super();
		this.txnId = txnId;
		this.merchName = merchName;
		this.storeAddress = storeAddress;
		this.giftName = giftName;
		this.pieQty = pieQty;
	}

	public int getTxnId() {
		return txnId;
	}

	public void setTxnId(int txnId) {
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
