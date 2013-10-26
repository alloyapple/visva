package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class MerchImage extends ShoppieObject {
	public static final String CLASS_UNIQUE = "MerchImage";
	public static final int NUM_FIELDS = 4;

	public String merchId = "";
	public String imageId = "";
	public String imageDesc = "";
	public String image = "";

	public MerchImage(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.merchId = values[0];
		this.imageId = values[1];
		this.imageDesc = values[2];
		this.image = values[3];

	}
}
