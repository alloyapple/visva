package vn.com.shoppie.database.sobject;

import java.text.NumberFormat;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Product extends ShoppieObject {
	public static final String CLASS_UNIQUE = "Product";
	public static final int NUM_FIELDS = 9;

	public String productId = "";
	public String merchId = "";
	public String productName = "";
	public String longDesc = "";
	public String likedNumber = "";
	public String price = "";
	public String oldPrice = "";
	public String productImage = "";
	public String pieQty = "";

	public Product(int index, String[] values) {
		super(index, values);
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.productId = values[0];
		this.merchId = values[1];
		this.productName = values[2];
		this.longDesc = values[3];
		this.likedNumber = values[4];
		this.price = values[5];
		this.oldPrice = values[6];
		this.productImage = values[7];
		this.pieQty = values[8];
	}

	public String getPrice() {
		String _price = "";
		try {
			double dPrice = Double.parseDouble(price);
			if(dPrice==0) return null;
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			_price=nf.format(dPrice);
			if(_price.length()>3)
				_price=_price.substring(1, _price.length()-3);
			else{
//				_price="0";
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return _price;
	}
}
