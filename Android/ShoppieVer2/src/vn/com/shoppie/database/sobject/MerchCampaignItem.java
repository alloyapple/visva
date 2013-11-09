package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class MerchCampaignItem {
	@SerializedName("campaignId")
	private int campaignId;
	@SerializedName("merchId")
	private int merchId;
	@SerializedName("campaignName")
	private String campaignName;
	@SerializedName("campaignDesc")
	private String campaignDesc;
	@SerializedName("campaignImage")
	private String campaignImage;
	@SerializedName("campaignType")
	private int campaignType;
	@SerializedName("likedNumber")
	private int likedNumber;
	@SerializedName("newStatus")
	private String newStatus;
	@SerializedName("viewed")
	private int viewed;
	@SerializedName("luckyPie")
	private int luckyPie;

	public int getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public int getMerchId() {
		return merchId;
	}

	public void setMerchId(int merchId) {
		this.merchId = merchId;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCampaignDesc() {
		return campaignDesc;
	}

	public void setCampaignDesc(String campaignDesc) {
		this.campaignDesc = campaignDesc;
	}

	public String getCampaignImage() {
		return campaignImage;
	}

	public void setCampaignImage(String campaignImage) {
		this.campaignImage = campaignImage;
	}

	public int getCampaignType() {
		return campaignType;
	}

	public void setCampaignType(int campaignType) {
		this.campaignType = campaignType;
	}

	public int getLikedNumber() {
		return likedNumber;
	}

	public void setLikedNumber(int likedNumber) {
		this.likedNumber = likedNumber;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	public int getViewed() {
		return viewed;
	}

	public void setViewed(int viewed) {
		this.viewed = viewed;
	}

	public int getLuckyPie() {
		return luckyPie;
	}

	public void setLuckyPie(int luckyPie) {
		this.luckyPie = luckyPie;
	}
}
