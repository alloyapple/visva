package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Slider extends ShoppieObject {
	public static final String CLASS_UNIQUE="Slider";
	public static final int NUM_FIELDS=4;
	
	public String slideId="";
	public String slideDesc="";
	public String slideLink="";
	public String slideImage="";
	
	public Slider(int index, String[] values) {
		super(index, values);
		
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE+" length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		
		this.slideId=values[0];
		this.slideDesc=values[1];
		this.slideLink=values[2];
		this.slideImage=values[3];
	}
	
	
	public interface SlideColumn extends ShoppieObjectColumn{
		public static final String TABLE_NAME="Slider";
		public static final String SLIDE_ID="slideId";
		public static final String SLIDE_DESC="slideDesc";
		public static final String SLIDE_LINK="slideLink";
		public static final String SLIDE_IMAGE="slideImage";
	}
}