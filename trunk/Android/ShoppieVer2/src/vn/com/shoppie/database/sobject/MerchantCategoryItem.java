package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class MerchantCategoryItem {
	
	@SerializedName("merchCatId")
	public int merchCatId;
	@SerializedName("merchCatName")
	public String merchCatName;
	@SerializedName("merchCatDesc")
	public String merchCatDesc;
	@SerializedName("icon")
	public String icon;
	@SerializedName("image")
	public String image;

	@SerializedName("lineColor")
	public String lineColor;
	@SerializedName("campaignNumber")
	public int campaignNumber;

//	public MerchantCategory() {
//
//	}
//
//	public MerchantCategory(int merchCatId, String merchCatName,
//			String merchCatDesc, String icon, String image, String lineColor,
//			int campaignNumber) {
//		super();
//		this.merchCatId = merchCatId;
//		this.merchCatName = merchCatName;
//		this.merchCatDesc = merchCatDesc;
//		this.icon = icon;
//		this.image = image;
//		this.lineColor = lineColor;
//		this.campaignNumber = campaignNumber;
//	}

	public int getMerchCatId() {
		return merchCatId;
	}

	public void setMerchCatId(int merchCatId) {
		this.merchCatId = merchCatId;
	}

	public String getMerchCatName() {
		return merchCatName;
	}

	public void setMerchCatName(String merchCatName) {
		this.merchCatName = merchCatName;
	}

	public String getMerchCatDesc() {
		return merchCatDesc;
	}

	public void setMerchCatDesc(String merchCatDesc) {
		this.merchCatDesc = merchCatDesc;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLineColor() {
		return lineColor;
	}

	public void setLineColor(String lineColor) {
		this.lineColor = lineColor;
	}

	public int getCampaignNumber() {
		return campaignNumber;
	}

	public void setCampaignNumber(int campaignNumber) {
		this.campaignNumber = campaignNumber;
	}

}
