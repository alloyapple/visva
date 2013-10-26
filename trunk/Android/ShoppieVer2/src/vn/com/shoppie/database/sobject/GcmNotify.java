package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class GcmNotify extends ShoppieObject {
	public static final int NUM_FIELDS = 4;

	public String message = "";
	public String type = "";
	public String pieQty = "";
	public String time = "";

	public GcmNotify(int id, String... values) {
		super(id, values);
		CLASS_UNIQUE=this.getClass().getSimpleName();
		
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.message = values[0];
		this.type = values[1];
		this.pieQty = values[2];
		this.time = values[3];
	}
	@Override
	public String[] getValues() {
		values[0]=this.message;
		values[1]=this.type;
		values[2]=this.pieQty;
		values[3]=this.time;
		return values;
	}
}
