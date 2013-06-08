package visvateam.outsource.idmanager.idxpwdatabase;

public class ElementID {

	// all variables
	private int eId;
	private int eGroupId;
	private String eTitle;
	private String eIcon;
	private long eTimeStamp;
	private int eFavourite;
	private int eFlag;
	private String eUrl;
	private String eNote;
	private String eImage;
	private int eOrder;
	private byte[] eIconData;
	private byte[] eMemoData;

	// constructor
	public ElementID(int eId, int eGroupId, String eTitle, String eIcon, long eTimeStamp,
			int eFavourite, int eFlag, String eUrl, String eNote, String eImage, int eOrder) {
		super();
		this.eId = eId;
		this.eGroupId = eGroupId;
		this.eTitle = eTitle;
		this.eIcon = eIcon;
		this.eTimeStamp = eTimeStamp;
		this.eFavourite = eFavourite;
		this.eFlag = eFlag;
		this.eUrl = eUrl;
		this.eNote = eNote;
		this.eImage = eImage;
		this.eOrder = eOrder;
	}
	public ElementID(int eId, int eGroupId, String eTitle, byte[] eIcon, long eTimeStamp,
			int eFavourite, int eFlag, String eUrl, String eNote, byte[] eMemo, int eOrder) {
		super();
		this.eId = eId;
		this.eGroupId = eGroupId;
		this.eTitle = eTitle;
		this.seteIconData(eIcon);
		this.eTimeStamp = eTimeStamp;
		this.eFavourite = eFavourite;
		this.eFlag = eFlag;
		this.eUrl = eUrl;
		this.eNote = eNote;
		this.seteMemoData(eMemo);
		this.eOrder = eOrder;
	}
	// empty constructor
	public ElementID() {

	}

	public int geteId() {
		return eId;
	}

	public void seteId(int eId) {
		this.eId = eId;
	}

	public int geteGroupId() {
		return eGroupId;
	}

	public void seteGroupId(int eGroupId) {
		this.eGroupId = eGroupId;
	}

	public String geteTitle() {
		return eTitle;
	}

	public void seteTitle(String eTitle) {
		this.eTitle = eTitle;
	}

	public String geteIcon() {
		return eIcon;
	}

	public void seteIcon(String eIcon) {
		this.eIcon = eIcon;
	}

	public long geteTimeStamp() {
		return eTimeStamp;
	}

	public void seteTimeStamp(long eTimeStamp) {
		this.eTimeStamp = eTimeStamp;
	}

	public int geteFavourite() {
		return eFavourite;
	}

	public void seteFavourite(int eFavourite) {
		this.eFavourite = eFavourite;
	}

	public int geteFlag() {
		return eFlag;
	}

	public void seteFlag(int eFlag) {
		this.eFlag = eFlag;
	}

	public String geteUrl() {
		return eUrl;
	}

	public void seteUrl(String eUrl) {
		this.eUrl = eUrl;
	}

	public String geteNote() {
		return eNote;
	}

	public void seteNote(String eNote) {
		this.eNote = eNote;
	}

	public String geteImage() {
		return eImage;
	}

	public void seteImage(String eImage) {
		this.eImage = eImage;
	}

	public int geteOrder() {
		return eOrder;
	}

	public void seteOrder(int eOrder) {
		this.eOrder = eOrder;
	}
	public void seteIconData(byte[] eIconData) {
		this.eIconData = eIconData;
	}
	public byte[] geteIconData() {
		return eIconData;
	}
	public void seteMemoData(byte[] eMemoData) {
		this.eMemoData = eMemoData;
	}
	public byte[] geteMemoData() {
		return eMemoData;
	}
}
