package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Notification extends ShoppieObject {
	public static final String CLASS_UNIQUE = "Notification";
	public static final int NUM_FIELDS = 6;

	public String notificationId = "";
	public String merchId = "";
	public String storeId = "";
	public String content = "";
	public String status = "";
	public String regdate = "";

	public Notification(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.notificationId = values[0];
		this.merchId = values[1];
		this.storeId = values[2];
		this.content = values[3];
		this.status = values[4];
		this.regdate = values[5];

	}
}
