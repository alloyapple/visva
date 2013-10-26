package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Merchant extends ShoppieObject{
	public static final String CLASS_UNIQUE="Merchant";
	public static final int NUM_FIELDS=6;
	
	public String merchId="";
	public String merchName="";
	public String merchCatId="";
	public String merchImage="";
	public String merchBanner="";
	public String merchDescription="";
	
	public Merchant(int id,String... values){
		super(id, values);
		
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE+" length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		this.merchId=values[0];
		this.merchName=values[1];
		this.merchCatId=values[2];
		this.merchImage=values[3];
		this.merchBanner=values[4];
		this.merchDescription=values[5];
	}
	public String[] getValues(){
		values[0]=this.merchId;
		values[1]=this.merchName;
		values[2]=this.merchCatId;
		values[3]=this.merchImage;
		values[4]=this.merchBanner;
		values[5]=this.merchDescription;
		return values;
	}
}