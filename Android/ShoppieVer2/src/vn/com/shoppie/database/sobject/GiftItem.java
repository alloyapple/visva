package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class GiftItem {
	@SerializedName("giftId")
	private String giftId;
	@SerializedName("giftCatId")
	private int giftCatId;
	public int getGiftCatId() {
		return giftCatId;
	}

	public void setGiftCatId(int giftCatId) {
		this.giftCatId = giftCatId;
	}

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
	
	public int getMinPie() {
		try {
			String pieString = getPieQty();
			String temp[] = pieString.split(",");
			if(temp != null) {
				int pie[] = new int[temp.length];
				for(int i = 0 ; i < temp.length ; i++) {
					pie[i] = Integer.parseInt(temp[i]);
				}
				for(int i = 0 ; i < temp.length ; i++) {
					for(int j = i + 1 ; j < temp.length ; j++) {
						if(pie[i] > pie[j]) {
							int pieT = pie[i];
							pie[i] = pie[j];
							pie[j] = pieT;
						}
					}
				}
				return pie[0];
			}
			return Integer.parseInt(pieString);
		} catch (Exception e) {
			return Integer.MAX_VALUE;
		}
	}
	
	public int[] getPies() {
		try {
			String pieString = getPieQty();
			String temp[] = pieString.split(",");
			if(temp != null) {
				int pie[] = new int[temp.length];
				for(int i = 0 ; i < temp.length ; i++) {
					pie[i] = Integer.parseInt(temp[i]);
				}
				for(int i = 0 ; i < temp.length ; i++) {
					for(int j = i + 1 ; j < temp.length ; j++) {
						if(pie[i] > pie[j]) {
							int pieT = pie[i];
							pie[i] = pie[j];
							pie[j] = pieT;
						}
					}
				}
				return pie;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public int[] getPiesNotArr() {
		try {
			String pieString = getPieQty();
			String temp[] = pieString.split(",");
			if(temp != null) {
				int pie[] = new int[temp.length];
				for(int i = 0 ; i < temp.length ; i++) {
					pie[i] = Integer.parseInt(temp[i]);
				}
				return pie;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String[] getPricesNotArr() {
		try {
			String priceString = getGiftPrice();
			String temp[] = priceString.split(",");
			if(temp != null) {
				return temp;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getPieStr() {
		int pies[] = getPies();
		if(pies == null) {
			return getMinPie() + "";
		}
		else {
			if(pies.length == 1)
				return getMinPie() + "";
			return getMinPie() + "+";
		}
	}
}
