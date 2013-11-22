package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class GiftItem {
	@SerializedName("giftId")
	private String giftId;
	@SerializedName("merchId")
	private String merchId;
	@SerializedName("giftName")
	private String giftName;
	@SerializedName("description")
	private String description;
	@SerializedName("pieQty")
	private String pieQty;
	@SerializedName("giftImage")
	private String giftImage;
	@SerializedName("redeemQty")
	private String redeemQty;
	@SerializedName("giftPrice")
	private String giftPrice;
	@SerializedName("viewtop")
	private String viewtop;

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getMerchId() {
		return merchId;
	}

	public void setMerchId(String merchId) {
		this.merchId = merchId;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPieQty() {
		return pieQty;
	}

	public void setPieQty(String pieQty) {
		this.pieQty = pieQty;
	}

	public String getGiftImage() {
		return giftImage;
	}

	public void setGiftImage(String giftImage) {
		this.giftImage = giftImage;
	}

	public String getRedeemQty() {
		return redeemQty;
	}

	public void setRedeemQty(String redeemQty) {
		this.redeemQty = redeemQty;
	}

	public String getGiftPrice() {
		return giftPrice;
	}

	public void setGiftPrice(String giftPrice) {
		this.giftPrice = giftPrice;
	}

	public String getViewtop() {
		return viewtop;
	}

	public void setViewtop(String viewtop) {
		this.viewtop = viewtop;
	}

	public GiftItem() {

	}
	
}
