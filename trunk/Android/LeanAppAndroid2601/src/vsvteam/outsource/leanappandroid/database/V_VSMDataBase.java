package vsvteam.outsource.leanappandroid.database;

public class V_VSMDataBase {
	// v_vsn variable
	private String projectName;
	private String VSMName;
	private int versionId;
	private String valueAddingTime;
	private String nonValueAddingTime;
	private String totalTime;
	private int ProjectID;

	// Empty constructor
	public V_VSMDataBase() {

	}

	// constructor
	public V_VSMDataBase(int id, String projectName, String VSMName, String valueAddingTime,
			String nonValueAddingTime, String totalTime, int versionId) {
		this.ProjectID = id;
		this.projectName = projectName;
		this.VSMName = VSMName;
		this.valueAddingTime = valueAddingTime;
		this.nonValueAddingTime = nonValueAddingTime;
		this.totalTime = totalTime;
		this.versionId = versionId;
	}

	// constructor
	public V_VSMDataBase(String projectName, String VSMName, String valueAddingTime,
			String nonValueAddingTime, String totalTime, int versionId) {
		this.projectName = projectName;
		this.VSMName = VSMName;
		this.valueAddingTime = valueAddingTime;
		this.nonValueAddingTime = nonValueAddingTime;
		this.totalTime = totalTime;
		this.versionId = versionId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getVSMName() {
		return VSMName;
	}

	public void setVSMName(String vSMName) {
		VSMName = vSMName;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public String getValueAddingTime() {
		return valueAddingTime;
	}

	public void setValueAddingTime(String valueAddingTime) {
		this.valueAddingTime = valueAddingTime;
	}

	public String getNonValueAddingTime() {
		return nonValueAddingTime;
	}

	public void setNonValueAddingTime(String nonValueAddingTime) {
		this.nonValueAddingTime = nonValueAddingTime;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public int getProjectID() {
		return ProjectID;
	}

	public void setProjectID(int id) {
		this.ProjectID = id;
	}
}
