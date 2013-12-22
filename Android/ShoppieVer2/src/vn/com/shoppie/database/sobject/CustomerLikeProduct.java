package vn.com.shoppie.database.sobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CustomerLikeProduct implements Parcelable{
	@SerializedName("productId")
	private int productId;
	@SerializedName("merchId")
	private int merchId;
	@SerializedName("productName")
	private String productName;
	@SerializedName("shortDesc")
	private String shortDesc;
	@SerializedName("longDesc")
	private String longDesc;
	@SerializedName("likedNumber")
	private int likedNumber;
	@SerializedName("price")
	private int price;
	@SerializedName("oldPrice")
	private int oldPrice;
	@SerializedName("productImage")
	private String productImage;
	@SerializedName("thumbNail")
	private String thumbNail;
	@SerializedName("newStatus")
	private String newStatus;
	@SerializedName("pieQty")
	private int pieQty;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getMerchId() {
		return merchId;
	}

	public void setMerchId(int merchId) {
		this.merchId = merchId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public int getLikedNumber() {
		return likedNumber;
	}

	public void setLikedNumber(int likedNumber) {
		this.likedNumber = likedNumber;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(int oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getThumbNail() {
		return thumbNail;
	}

	public void setThumbNail(String thumbNail) {
		this.thumbNail = thumbNail;
	}

	public String getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(String newStatus) {
		this.newStatus = newStatus;
	}

	public int getPieQty() {
		return pieQty;
	}

	public void setPieQty(int pieQty) {
		this.pieQty = pieQty;
	}


	public CustomerLikeProduct(Parcel in) {
		// TODO Auto-generated constructor stub
		this.productId = in.readInt();
		this.merchId = in.readInt();
		this.productName = in.readString();
		this.shortDesc = in.readString();
		this.longDesc = in.readString();
		this.likedNumber = in.readInt();
		this.price = in.readInt();
		this.oldPrice = in.readInt();
		this.productImage = in.readString();
		this.thumbNail = in.readString();
		this.newStatus = in.readString();
		this.pieQty = in.readInt();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeInt(productId);
		out.writeInt(merchId);
		out.writeString(productName);
		out.writeString(shortDesc);
		out.writeString(longDesc);
		out.writeInt(likedNumber);
		out.writeInt(price);
		out.writeInt(oldPrice);
		out.writeString(productImage);
		out.writeString(thumbNail);
		out.writeString(newStatus);
		out.writeInt(pieQty);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<CustomerLikeProduct> CREATOR = new Parcelable.Creator<CustomerLikeProduct>() {
		public CustomerLikeProduct createFromParcel(Parcel in) {
			return new CustomerLikeProduct(in);
		}

		public CustomerLikeProduct[] newArray(int size) {
			return new CustomerLikeProduct[size];
		}
	};
}
