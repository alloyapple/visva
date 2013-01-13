package vsvteam.outsource.leanappandroid.database;

public class TStepsDataBase {
	// private variables
	private int stepID;
	private int processId;
	private int projectID;
	private int stepNo;
	private String stepDescription;
	private int versionID;
	private int previousVesID;
	private String videoFileName;

	// empty constructor
	public TStepsDataBase() {

	}

	// constructor
	public TStepsDataBase(int stepID, int processId, int projectID, int stepNo,
			String stepDescription, int versionID, int previousVesID, String videoFileName) {
		this.stepID = stepID;
		this.processId = processId;
		this.processId = projectID;
		this.stepNo = stepNo;
		this.stepDescription = stepDescription;
		this.versionID = versionID;
		this.previousVesID = previousVesID;
		this.videoFileName = videoFileName;
	}

	// constructor
	public TStepsDataBase(int processId, int projectId, int stepNo, String stepDescription,
			int versionID, int previousVesID, String videoFileName) {
		this.processId = processId;
		this.projectID = projectId;
		this.stepNo = stepNo;
		this.stepDescription = stepDescription;
		this.versionID = versionID;
		this.previousVesID = previousVesID;
		this.videoFileName = videoFileName;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public int getStepID() {
		return stepID;
	}

	public void setStepID(int stepID) {
		this.stepID = stepID;
	}

	public int getStepNo() {
		return stepNo;
	}

	public void setStepNo(int stepNo) {
		this.stepNo = stepNo;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public int getVersionID() {
		return versionID;
	}

	public void setVersionID(int versionID) {
		this.versionID = versionID;
	}

	public int getPreviousVesID() {
		return previousVesID;
	}

	public void setPreviousVesID(int previousVesID) {
		this.previousVesID = previousVesID;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}
}
