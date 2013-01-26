package vsvteam.outsource.leanappandroid.database;

public class TVersionDataBase {
	// private variables
	private int projectId;
	private int versionId;
	private int versionNo;
	private String versionDateTime;
	private String versionNote;

	// empty constructor
	public TVersionDataBase() {

	}

	// constructor
	public TVersionDataBase(int projectId, int versionId, int versionNo, String versionDateTime,
			String versionNote) {
		this.projectId = projectId;
		this.versionId = versionId;
		this.versionNo = versionNo;
		this.versionDateTime = versionDateTime;
		this.versionNote = versionNote;
	}

	// constructor
	public TVersionDataBase(int versionId, int versionNo, String versionDateTime, String versionNote) {
		this.versionId = versionId;
		this.versionNo = versionNo;
		this.versionDateTime = versionDateTime;
		this.versionNote = versionNote;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public int getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(int versionNo) {
		this.versionNo = versionNo;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getVersionDateTime() {
		return versionDateTime;
	}

	public void setVersionDateTime(String versionDateTime) {
		this.versionDateTime = versionDateTime;
	}

	public String getVersionNote() {
		return versionNote;
	}

	public void setVersionNote(String versionNote) {
		this.versionNote = versionNote;
	}
}
