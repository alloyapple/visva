package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class CampaignImage extends ShoppieObject {
	public static final String CLASS_UNIQUE = "CampaignImage";
	public static final int NUM_FIELDS = 5;

	public String campaignId = "";
	public String imageName = "";
	public String imagePath = "";
	public String imageDesc = "";
	public String price = "";

	public CampaignImage(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.campaignId = values[0];
		this.imageName = values[1];
		this.imagePath = values[2];
		this.imageDesc = values[3];
		this.price = values[4];

	}

}