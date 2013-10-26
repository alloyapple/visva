package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Shop extends ShoppieObject {
	public static final String CLASS_UNIQUE="Shop";
	public static final int NUM_FIELDS=5;
	
	public int idAvatarDef;
	public String title = "";
	public int score = 0;
	public String desc = "";
	public int discount = 0;

	public Shop(int id,String[] values) {
		super(id, values);
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE+" length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		
		this.idAvatarDef=Integer.parseInt(values[0]);
		this.title=values[1];
		this.score=Integer.parseInt(values[2]);
		this.desc=values[3];
		this.discount=Integer.parseInt(values[4]);
		
	}

	public interface ShopColumn extends ShoppieObjectColumn {
		public static final String TABLE_NAME = "Shop";
		public static final String TITLE = "title";
		public static final String SCORE = "score";
		public static final String DESC = "desc";
		public static final String DISCOUNT = "discount";
	}
}
