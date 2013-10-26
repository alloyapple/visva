package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class MerchantCategory extends ShoppieObject{
	public static final String CLASS_UNIQUE="MerchantCategory";
	public static final int NUM_FIELDS=2;
	
	public String merchCatId="";
	public String merchCatName="";
	
	
	public MerchantCategory(int index, String[] values) {
		super(index, values);
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE+" length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		this.merchCatId=values[0];
		this.merchCatName=values[1];
	}
	public String[] getValues(){
		values[0]=this.merchCatId;
		values[1]=this.merchCatName;
		return values;
	}
}
