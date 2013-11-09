package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class MerchProductItem {
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
	@SerializedName("liked")
	private int liked;

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

	public int getLiked() {
		return liked;
	}

	public void setLiked(int liked) {
		this.liked = liked;
	}
}
