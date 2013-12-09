package vn.com.shoppie.database.sobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MerchantStoreItem implements Parcelable{
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
	@SerializedName("merchBanner2")
	private String merchBanner2;
	@SerializedName("merchBanner3")
	private String merchBanner3;
	@SerializedName("likedNumber")
	private int likedNumber;
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
	@SerializedName("pieQtyPurchase")
	private int pieQtyPurchase;
	@SerializedName("pieQty")
	private int pieQty;
	@SerializedName("liked")
	private int liked;
	
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
	public int getPieQty() {
		return pieQty;
	}
	public void setPieQty(int pieQty) {
		this.pieQty = pieQty;
	}

	public MerchantStoreItem(Parcel in) {
		// TODO Auto-generated constructor stub
		this.merchId = in.readInt();
		this.merchCatId = in.readInt();
		this.merchName = in.readString();
		this.merchDesc = in.readString();
		this.merchLogo = in.readString();
		this.merchBanner = in.readString();
		this.merchBanner2 = in.readString();
		this.merchBanner3 = in.readString();
		this.likedNumber = in.readInt();
		this.storeId = in.readInt();
		this.storeName = in.readString();
		this.storeAddress = in.readString();
		this.latitude = in.readString();
		this.longtitude = in.readString();
		this.pieQtyPurchase = in.readInt();
		this.pieQty = in.readInt();
		this.liked = in.readInt();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeInt(merchId);
		out.writeInt(merchCatId);
		out.writeString(merchName);
		out.writeString(merchDesc);
		out.writeString(merchLogo);
		out.writeString(merchBanner);
		out.writeString(merchBanner2);
		out.writeString(merchBanner3);
		out.writeInt(likedNumber);
		out.writeInt(storeId);
		out.writeString(storeName);
		out.writeString(storeAddress);
		out.writeString(latitude);
		out.writeString(longtitude);
		out.writeInt(pieQtyPurchase);
		out.writeInt(pieQty);
		out.writeInt(liked);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<MerchantStoreItem> CREATOR = new Parcelable.Creator<MerchantStoreItem>() {
		public MerchantStoreItem createFromParcel(Parcel in) {
			return new MerchantStoreItem(in);
		}

		public MerchantStoreItem[] newArray(int size) {
			return new MerchantStoreItem[size];
		}
	};
}
