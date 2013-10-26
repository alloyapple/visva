package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Promotion extends ShoppieObject {
	public static final String CLASS_UNIQUE = "Promotion";
	public static final int NUM_FIELDS = 4;

	public String merchId = "";
	public String txnType = "";
	public String pieQty = "";
	public String campaignName = "";

	public Promotion(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.merchId = values[0];
		this.txnType = values[1];
		this.pieQty = values[2];
		this.campaignName = values[3];

	}

}
