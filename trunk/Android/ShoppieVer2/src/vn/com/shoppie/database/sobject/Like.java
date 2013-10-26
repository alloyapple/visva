package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Like extends ShoppieObject {
	public static final String CLASS_UNIQUE = "Like";
	public static final int NUM_FIELDS = 7;

	public static final String TYPE_LIKE_BRAND="brand";
	public static final String TYPE_LIKE_PRODUCT="product";
	public static final String TYPE_LIKE_GIFT="gift";
	
	public String merchId="";
	public String type = "";
	public String id = "";
	public String qty = "";
	public String time = "";
	public String name="";
	public String link="";

	public Like(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		this.merchId=values[0];
		this.type = values[1];
		this.id = values[2];
		this.qty = values[3];
		this.time = values[4];
		this.name=values[5];
		this.link=values[6];
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
		values[0]=merchId;
		values[1]=type;
		values[2]=id;
		values[3]=qty;
		values[4]=time;
		values[5]=name;
		values[6]=link;
		return values;
	}
	
	public boolean equal(Like _like){
		if(_like.id==null || !this.id.equals(_like.id)) return false;
		if(_like.merchId==null || !this.merchId.equals(_like.merchId)) return false;
		if(_like.name==null || !this.name.equals(_like.name)) return false;
		if(_like.type==null || !this.type.equals(_like.type)) return false;
		if(_like.link==null || !this.link.equals(_like.link)) return false;
		return true;
	}
}
