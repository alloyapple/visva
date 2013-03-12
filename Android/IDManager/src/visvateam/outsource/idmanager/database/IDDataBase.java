package visvateam.outsource.idmanager.database;

public class IDDataBase {
	// all variables
	private int passWordId;
	private int folderId;
	private String titleRecord;
	private String icon;
	private String favouriteGroup;
	private String titleId1;
	private String dataId1;
	private String titleId2;
	private String dataId2;
	private String titleId3;
	private String dataId3;
	private String titleId4;
	private String dataId4;
	private String titleId5;
	private String dataId5;
	private String titleId6;
	private String dataId6;
	private String titleId7;
	private String dataId7;
	private String titleId8;
	private String dataId8;
	private String titleId9;
	private String dataId9;
	private String titleId10;
	private String dataId10;
	private String titleId11;
	private String dataId11;
	private String titleId12;
	private String dataId12;
	private String url;
	private String note;
	private String imageMemo;
	private String flag;
	private String timeStamp;
	private boolean isEncrypted;

	private int userId;
	private int like;

	// constructor
	public IDDataBase() {

	}

	// constructor
	public IDDataBase(int passWordId, int folderId, String titleRecord,
			String icon, String favouriteGroup, String titleId1,
			String dataId1, String titleId2, String dataId2, String titleId3,
			String dataId3, String titleId4, String dataId4, String titleId5,
			String dataId5, String titleId6, String dataId6, String titleId7,
			String dataId7, String titleId8, String dataId8, String titleId9,
			String dataId9, String titleId10, String dataId10,
			String titleId11, String dataId11, String titleId12,
			String dataId12, String url, String note, String imageMemo,
			String flag, String timeStamp, boolean isEncrypted, int userId,
			int like) {
		super();
		this.passWordId = passWordId;
		this.folderId = folderId;
		this.titleRecord = titleRecord;
		this.icon = icon;
		this.favouriteGroup = favouriteGroup;
		this.titleId1 = titleId1;
		this.dataId1 = dataId1;
		this.titleId2 = titleId2;
		this.dataId2 = dataId2;
		this.titleId3 = titleId3;
		this.dataId3 = dataId3;
		this.titleId4 = titleId4;
		this.dataId4 = dataId4;
		this.titleId5 = titleId5;
		this.dataId5 = dataId5;
		this.titleId6 = titleId6;
		this.dataId6 = dataId6;
		this.titleId7 = titleId7;
		this.dataId7 = dataId7;
		this.titleId8 = titleId8;
		this.dataId8 = dataId8;
		this.titleId9 = titleId9;
		this.dataId9 = dataId9;
		this.titleId10 = titleId10;
		this.dataId10 = dataId10;
		this.titleId11 = titleId11;
		this.dataId11 = dataId11;
		this.titleId12 = titleId12;
		this.dataId12 = dataId12;
		this.url = url;
		this.note = note;
		this.imageMemo = imageMemo;
		this.flag = flag;
		this.timeStamp = timeStamp;
		this.isEncrypted = isEncrypted;
		this.userId = userId;
		this.like = like;
	}

	public int getPassWordId() {
		return passWordId;
	}

	public void setPassWordId(int passWordId) {
		this.passWordId = passWordId;
	}

	public int getFolderId() {
		return folderId;
	}

	public void setFolderId(int folderId) {
		this.folderId = folderId;
	}

	public String getTitleRecord() {
		return titleRecord;
	}

	public void setTitleRecord(String titleRecord) {
		this.titleRecord = titleRecord;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getFavouriteGroup() {
		return favouriteGroup;
	}

	public void setFavouriteGroup(String favouriteGroup) {
		this.favouriteGroup = favouriteGroup;
	}

	public String getDataId(int i) {
		switch (i) {
		case 1:
			return getDataId1();
		case 2:
			return getDataId2();
		case 3:
			return getDataId3();
		case 4:
			return getDataId4();
		case 5:
			return getDataId5();
		case 6:
			return getDataId6();
		case 7:
			return getDataId7();
		case 8:
			return getDataId8();
		case 9:
			return getDataId9();
		case 10:
			return getDataId10();
		case 11:
			return getDataId11();
		case 12:
			return getDataId12();

		default:
			return "";
		}
	}

	public String getTitleId(int i) {
		switch (i) {
		case 1:
			return getTitleId1();
		case 2:
			return getTitleId2();
		case 3:
			return getTitleId3();
		case 4:
			return getTitleId4();
		case 5:
			return getTitleId5();
		case 6:
			return getTitleId6();
		case 7:
			return getTitleId7();
		case 8:
			return getTitleId8();
		case 9:
			return getTitleId9();
		case 10:
			return getTitleId10();
		case 11:
			return getTitleId11();
		case 12:
			return getTitleId12();

		default:
			return "";
		}
	}

	public String getTitleId1() {
		return titleId1;
	}

	public void setTitleId1(String titleId1) {
		this.titleId1 = titleId1;
	}

	public String getDataId1() {
		return dataId1;
	}

	public void setDataId1(String dataId1) {
		this.dataId1 = dataId1;
	}

	public String getTitleId2() {

		return titleId2;
	}

	public void setTitleId2(String titleId2) {
		this.titleId2 = titleId2;
	}

	public String getDataId2() {
		return dataId2;
	}

	public void setDataId2(String dataId2) {
		this.dataId2 = dataId2;
	}

	public String getTitleId3() {
		return titleId3;
	}

	public void setTitleId3(String titleId3) {
		this.titleId3 = titleId3;
	}

	public String getDataId3() {
		return dataId3;
	}

	public void setDataId3(String dataId3) {
		this.dataId3 = dataId3;
	}

	public String getTitleId4() {
		return titleId4;
	}

	public void setTitleId4(String titleId4) {
		this.titleId4 = titleId4;
	}

	public String getDataId4() {
		return dataId4;
	}

	public void setDataId4(String dataId4) {
		this.dataId4 = dataId4;
	}

	public String getTitleId5() {
		return titleId5;
	}

	public void setTitleId5(String titleId5) {
		this.titleId5 = titleId5;
	}

	public String getDataId5() {
		return dataId5;
	}

	public void setDataId5(String dataId5) {
		this.dataId5 = dataId5;
	}

	public String getTitleId6() {
		return titleId6;
	}

	public void setTitleId6(String titleId6) {
		this.titleId6 = titleId6;
	}

	public String getDataId6() {
		return dataId6;
	}

	public void setDataId6(String dataId6) {
		this.dataId6 = dataId6;
	}

	public String getTitleId7() {
		return titleId7;
	}

	public void setTitleId7(String titleId7) {
		this.titleId7 = titleId7;
	}

	public String getDataId7() {
		return dataId7;
	}

	public void setDataId7(String dataId7) {
		this.dataId7 = dataId7;
	}

	public String getTitleId8() {
		return titleId8;
	}

	public void setTitleId8(String titleId8) {
		this.titleId8 = titleId8;
	}

	public String getDataId8() {
		return dataId8;
	}

	public void setDataId8(String dataId8) {
		this.dataId8 = dataId8;
	}

	public String getTitleId9() {
		return titleId9;
	}

	public void setTitleId9(String titleId9) {
		this.titleId9 = titleId9;
	}

	public String getDataId9() {
		return dataId9;
	}

	public void setDataId9(String dataId9) {
		this.dataId9 = dataId9;
	}

	public String getTitleId10() {
		return titleId10;
	}

	public void setTitleId10(String titleId10) {
		this.titleId10 = titleId10;
	}

	public String getDataId10() {
		return dataId10;
	}

	public void setDataId10(String dataId10) {
		this.dataId10 = dataId10;
	}

	public String getTitleId11() {
		return titleId11;
	}

	public void setTitleId11(String titleId11) {
		this.titleId11 = titleId11;
	}

	public String getDataId11() {
		return dataId11;
	}

	public void setDataId11(String dataId11) {
		this.dataId11 = dataId11;
	}

	public String getTitleId12() {
		return titleId12;
	}

	public void setTitleId12(String titleId12) {
		this.titleId12 = titleId12;
	}

	public String getDataId12() {
		return dataId12;
	}

	public void setDataId12(String dataId12) {
		this.dataId12 = dataId12;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getImageMemo() {
		return imageMemo;
	}

	public void setImageMemo(String imageMemo) {
		this.imageMemo = imageMemo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public boolean isEncrypted() {
		return isEncrypted;
	}

	public void setEncrypted(boolean isEncrypted) {
		this.isEncrypted = isEncrypted;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

}
