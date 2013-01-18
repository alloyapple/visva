package vsvteam.outsource.leanappandroid.database;

public class TCycleTimeDataBase {
	// private variable
	private int cycleTimeId;
	private int processId;
	private int projectId;
	private String processName;
	private String projectName;
	private int cycleCounter;
	private String operator;
	private int shiftNo;
	private int stepId;
	private String stepDescription;
	private String lowestTime;
	private String adjustment;
	private String adjustTime;
	private String audioNote;
	private String videoFileName;
	private String startTimeStamp;
	private String endTimeStamp;
	private int versionId;
	private int previousVersionId;
	private String preVerDifferenceLowestTime;
	private String preVerDifferenceAdjustedTime;
	private String preVerDifferenceAdjustment;

	// empty constructor
	public TCycleTimeDataBase() {

	}

	// constructor
	public TCycleTimeDataBase(int cycleTimeId, int processId, int projectId, String processName,
			String projectName, int cycleCounter, String operator, int shiftNo, int stepId,
			String stepDescription, String lowestTime, String adjustment, String adjustTime,
			String audioNote, String videoFileName, String startTimeStamp, String endTimeStamp,
			int versionId, int previousVersionId, String preDifferenceLowestTime,
			String preVerDifferenceAdjustedTime, String preVerDifferenceAdjustment) {
		this.cycleTimeId = cycleTimeId;
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.cycleCounter = cycleCounter;
		this.operator = operator;
		this.shiftNo = shiftNo;
		this.stepId = stepId;
		this.stepDescription = stepDescription;
		this.lowestTime = lowestTime;
		this.adjustment = adjustment;
		this.adjustTime = adjustTime;
		this.audioNote = audioNote;
		this.videoFileName = videoFileName;
		this.startTimeStamp = startTimeStamp;
		this.endTimeStamp = endTimeStamp;
		this.versionId = versionId;
		this.previousVersionId = previousVersionId;
		this.preVerDifferenceLowestTime = preDifferenceLowestTime;
		this.preVerDifferenceAdjustedTime = preVerDifferenceAdjustedTime;
		this.preVerDifferenceAdjustment = preVerDifferenceAdjustment;
	}

	// constructor
	public TCycleTimeDataBase(int processId, int projectId, String processName, String projectName,
			int cycleCounter, String operator, int shiftNo, int stepId, String stepDescription,
			String lowestTime, String adjustment, String adjustTime, String audioNote,
			String videoFileName, String startTimeStamp, String endTimeStamp, int versionId,
			int previousVersionId, String preDifferenceLowestTime,
			String preVerDifferenceAdjustedTime, String preVerDifferenceAdjustment) {
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.cycleCounter = cycleCounter;
		this.operator = operator;
		this.shiftNo = shiftNo;
		this.stepId = stepId;
		this.stepDescription = stepDescription;
		this.lowestTime = lowestTime;
		this.adjustment = adjustment;
		this.adjustTime = adjustTime;
		this.audioNote = audioNote;
		this.videoFileName = videoFileName;
		this.startTimeStamp = startTimeStamp;
		this.endTimeStamp = endTimeStamp;
		this.versionId = versionId;
		this.previousVersionId = previousVersionId;
		this.preVerDifferenceLowestTime = preDifferenceLowestTime;
		this.preVerDifferenceAdjustedTime = preVerDifferenceAdjustedTime;
		this.preVerDifferenceAdjustment = preVerDifferenceAdjustment;
	}

	public int getCycleTimeId() {
		return cycleTimeId;
	}

	public void setCycleTimeId(int cycleTimeId) {
		this.cycleTimeId = cycleTimeId;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getCycleCounter() {
		return cycleCounter;
	}

	public void setCycleCounter(int cycleCounter) {
		this.cycleCounter = cycleCounter;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public int getShiftNo() {
		return shiftNo;
	}

	public void setShiftNo(int shiftNo) {
		this.shiftNo = shiftNo;
	}

	public int getStepId() {
		return stepId;
	}

	public void setStepId(int stepId) {
		this.stepId = stepId;
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public String getLowestTime() {
		return lowestTime;
	}

	public void setLowestTime(String lowestTime) {
		this.lowestTime = lowestTime;
	}

	public String getAdjustment() {
		return adjustment;
	}

	public void setAdjustment(String adjustment) {
		this.adjustment = adjustment;
	}

	public String getAdjustTime() {
		return adjustTime;
	}

	public void setAdjustTime(String adjustTime) {
		this.adjustTime = adjustTime;
	}

	public String getAudioNote() {
		return audioNote;
	}

	public void setAudioNote(String audioNote) {
		this.audioNote = audioNote;
	}

	public String getVideoFileName() {
		return videoFileName;
	}

	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}

	public String getStartTimeStamp() {
		return startTimeStamp;
	}

	public void setStartTimeStamp(String startTimeStamp) {
		this.startTimeStamp = startTimeStamp;
	}

	public String getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(String endTimeStamp) {
		this.endTimeStamp = endTimeStamp;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public int getPreviousVersionId() {
		return previousVersionId;
	}

	public void setPreviousVersionId(int previousVersionId) {
		this.previousVersionId = previousVersionId;
	}

	public String getPreVerDifferenceLowestTime() {
		return preVerDifferenceLowestTime;
	}

	public void setPreVerDifferenceLowestTime(String preVerDifferenceLowestTime) {
		this.preVerDifferenceLowestTime = preVerDifferenceLowestTime;
	}

	public String getPreVerDifferenceAdjustedTime() {
		return preVerDifferenceAdjustedTime;
	}

	public void setPreVerDifferenceAdjustedTime(String preVerDifferenceAdjustedTime) {
		this.preVerDifferenceAdjustedTime = preVerDifferenceAdjustedTime;
	}

	public String getPreVerDifferenceAdjustment() {
		return preVerDifferenceAdjustment;
	}

	public void setPreVerDifferenceAdjustment(String preVerDifferenceAdjustment) {
		this.preVerDifferenceAdjustment = preVerDifferenceAdjustment;
	}
}
