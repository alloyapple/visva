package visvateam.outsource.idmanager.idxpwdatabase;

public class GroupFolder {
	// all variables
	private int gId;
	private String gName;
	private int gType;
	private int gUserId;
	private int gOrder;

	// constructor
	public GroupFolder(int gId, String gName, int gType, int gUserId, int gOrder) {
		super();
		this.gId = gId;
		this.gName = gName;
		this.gType = gType;
		this.gUserId = gUserId;
		this.gOrder = gOrder;
	}

	// empty constructor
	public GroupFolder() {

	}

	public int getgId() {
		return gId;
	}

	public void setgId(int gId) {
		this.gId = gId;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public int getgType() {
		return gType;
	}

	public void setgType(int gType) {
		this.gType = gType;
	}

	public int getgUserId() {
		return gUserId;
	}

	public void setgUserId(int gUserId) {
		this.gUserId = gUserId;
	}

	public int getgOrder() {
		return gOrder;
	}

	public void setgOrder(int gOrder) {
		this.gOrder = gOrder;
	}
}
