package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class MerchantStoreItem {
	@SerializedName("merchId")
	private int merchId;
	@SerializedName("merchCatId")
	private int merchCatId;
	@SerializedName("merchName")
	private String merchName;
	@SerializedName("merchDesc")
	private String merchDesc;
	@SerializedName("merchLogo")
	private String merchLogo;
	@SerializedName("merchBanner")
	private String merchBanner;
	@SerializedName("storeId")
	private int storeId;
	@SerializedName("storeName")
	private String storeName;
	@SerializedName("storeAddress")
	private String storeAddress;
	@SerializedName("latitude")
	private String latitude;
	@SerializedName("longtitude")
	private String longtitude;
	@SerializedName("txnType")
	private int txnType;
	@SerializedName("pieQty")
	private int pieQty;
	
	public MerchantStoreItem(){
		
	}
	
	public int getMerchId() {
		return merchId;
	}
	public void setMerchId(int merchId) {
		this.merchId = merchId;
	}
	public int getMerchCatId() {
		return merchCatId;
	}
	public void setMerchCatId(int merchCatId) {
		this.merchCatId = merchCatId;
	}
	public String getMerchName() {
		return merchName;
	}
	public void setMerchName(String merchName) {
		this.merchName = merchName;
	}
	public String getMerchDesc() {
		return merchDesc;
	}
	public void setMerchDesc(String merchDesc) {
		this.merchDesc = merchDesc;
	}
	public String getMerchLogo() {
		return merchLogo;
	}
	public void setMerchLogo(String merchLogo) {
		this.merchLogo = merchLogo;
	}
	public String getMerchBanner() {
		return merchBanner;
	}
	public void setMerchBanner(String merchBanner) {
		this.merchBanner = merchBanner;
	}
	public int getStoreId() {
		return storeId;
	}
	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}
	public int getTxnType() {
		return txnType;
	}
	public void setTxnType(int txnType) {
		this.txnType = txnType;
	}
	public int getPieQty() {
		return pieQty;
	}
	public void setPieQty(int pieQty) {
		this.pieQty = pieQty;
	}
}
