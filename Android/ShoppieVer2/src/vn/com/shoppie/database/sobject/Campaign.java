package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class Campaign extends ShoppieObject {
	public static final String CLASS_UNIQUE = "Campaign";
	public static final int NUM_FIELDS = 7;

	public String id = "";
	public String merchId = "";
	public String name = "";
	public String desc = "";
	public String linkImage = "";
	public String campaignType = "1";
	public String newStatus="";

	public Campaign(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.id = values[0];
		this.merchId = values[1];
		this.name = values[2];
		this.desc = values[3];
		this.linkImage = values[4];
		this.campaignType = values[5];
		this.newStatus=values[6];
	}
	
	@Override
	public String[] getValues() {
		values[0]=id;
		values[1]=merchId;
		values[2]=name;
		values[3]=desc;
		values[4]=linkImage;
		values[5]=campaignType;
		values[6]=newStatus;
		return values;
	}

	public int getType() {
		int type = -1;
		try {
			type = Integer.valueOf(campaignType);
			
		} catch (NumberFormatException e) {
		}
//		Log.e("type Campaign", type+"");
		return type;
	}

	@Override
	public String toString() {
		return "Campaign [id=" + id + ", merchId=" + merchId + ", name=" + name + ", desc=" + desc + ", linkImage=" + linkImage + ", campaignType=" + campaignType + "]";
	}

	public interface CampaignColumn extends ShoppieObjectColumn {
		public static final String TABLE_NAME = "campaign";

		public static final String MERCH_ID = "merchId";
		public static final String NAME = "name";
		public static final String DESC = "desc";
		public static final String LINK_IMAGE = "linkImage";
		public static final String CAMPAIGN_TYPE = "campaignType";
	}

}
