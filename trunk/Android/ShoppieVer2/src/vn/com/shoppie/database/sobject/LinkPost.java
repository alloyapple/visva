package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class LinkPost extends ShoppieObject{
	public static final String CLASS_UNIQUE="Link Post";
	public static final int NUM_FIELDS=3;
	
	public String link="";
	public String xml="";
	public long time=0;
	
	public LinkPost(int index, String... values) {
		super(index, values);
		
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE+" length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		this.link=values[0];
		this.xml=values[1];
		time=Long.parseLong(values[2]);
	}
	@Override
	public String[] getValues() {
		values[0]=link;
		values[1]=xml;
		values[2]=time+"";
		return values;
	}
}
