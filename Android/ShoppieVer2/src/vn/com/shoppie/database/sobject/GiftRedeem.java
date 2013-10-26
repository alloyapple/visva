package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class GiftRedeem extends ShoppieObject {
	public static final String CLASS_UNIQUE = "GiftRedeem";
	public static final int NUM_FIELDS = 13;

	public String txnId = "";
	public String merchName = "";
	public String storeAddress = "";
	public String giftName = "";
	public String pieQty = "";
	public String time="";
	public String giftImage="";
	public String merchId="";
	
	public String redeemQty="";
	public String giftId="";
	public String storeName="";
	public String storeId="";
	public String status="";

	public GiftRedeem(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.txnId = values[0];
		this.merchName = values[1];
		this.storeAddress = values[2];
		this.giftName = values[3];
		this.pieQty = values[4];
		this.time=values[5];
		this.giftImage=values[6];
		this.merchId=values[7];
		this.redeemQty=values[8];
		this.giftId=values[9];
		this.storeName=values[10];
		this.storeId=values[11];
		this.status=values[12];
	}
	public long getTime(){
		try{
			return Long.parseLong(time);
		}catch(NumberFormatException e){
			return -1;
		}
	}
	@Override
	public String[] getValues() {
		values[0]=this.txnId;
		values[1]=this.merchName;
		values[2]=this.storeAddress;
		values[3]=this.giftName;
		values[4]=this.pieQty;
		values[5]=this.time;
		values[6]=this.giftImage;
		values[7]=this.merchId;
		values[8]=this.redeemQty;
		values[9]=this.giftId;
		values[10]=this.storeName;
		values[11]=this.storeId;
		values[12]=this.status;
		return values;
	}
}
