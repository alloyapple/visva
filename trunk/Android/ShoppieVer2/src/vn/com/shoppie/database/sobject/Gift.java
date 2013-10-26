package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;


@SuppressWarnings("serial")
public class Gift extends ShoppieObject {
	public static final String CLASS_UNIQUE="Gift";
	public static final int NUM_FIELDS=7;
	
	public String giftId="";
	public String merchId="";
	public String giftName="";
	public String description="";
	public String pieQty="";
	public String giftImage="";
	public String redeemQty="";
	
	public Gift(int index, String[] values) {
		super(index, values);
		
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE+" length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		
		this.giftId=values[0];
		this.merchId=values[1];
		this.giftName=values[2];
		this.description=values[3];
		this.pieQty=values[4];
		this.giftImage=values[5];
		this.redeemQty=values[6];
	}
	
	public int getPie(){
		try{
			return Integer.valueOf(pieQty);
		}catch(NumberFormatException e){
			return Integer.MAX_VALUE;
		}
	}
	
	
	public interface GiftColumn extends ShoppieObjectColumn{
		public static final String TABLE_NAME="Gift";
		public static final String GIFT_ID="giftId";
		public static final String MERCH_ID="merchId";
		public static final String GIFT_NAME="giftName";
		public static final String DESCRIPTION="description";
		public static final String PIE_QTY="pieQty";
		public static final String GIFT_IMAGE="giftImage";
		public static final String REDEEM_QTY="redeemQty";
	}


	@Override
	public String toString() {
		return "Gift [giftId=" + giftId + ", merchId=" + merchId + ", giftName=" + giftName + ", description=" + description + ", pieQty=" + pieQty + ", giftImage=" + giftImage + ", redeemQty=" + redeemQty + ", CLASS_UNIQUE=" + CLASS_UNIQUE + ", _id=" + _id + "]";
	}
	
	
}
