package vsvteam.outsource.leanappandroid.database;

public class TSpaghettiDataBase {
	// private all variables
	private int spaghettiID;// spaghetti id
	private int processId;// process id
	private int projectId;// project id
	private String processName;// process name
	private String projectName;// project name

	private int stepId;// step id
	private String stepDescription;// step description
	private int prevStepId;// previous step id
	private int nextStepId;// next step id
	private int distancePrevStep;// distance previous step
	private int distanceNextStep;// distance next step
	private String distanceUnit;// distance unit
	private int travelSpeed;// travel speed
	private String travelUnit;// travel unit
	private String timeToNext;// time to next
	private String timeFromPrevious; // time to previous
	private int versionId;// version id
	private String status;// status
	private String preVerSavedTime;// previous version saved time
	private String preVerSavedDistance;// previous version saved distance

	// empty constructor
	public TSpaghettiDataBase() {

	}

	// constructors
	public TSpaghettiDataBase(int spaghettiId, int processId, int projectId, String processName,
			String projectName, int stepId, String stepDescription, int prevStepId, int nextStepId,
			int distancePrevStep, int distanceNextSep, String distanceUnit, int travelSpeed,
			String travelUnit, String timeToNext,String timeFromPrevious, int versionId, String status,
			String preVerSavedTime, String preVerSavedDistance) {

		this.spaghettiID = spaghettiId;
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.stepId = stepId;
		this.stepDescription = stepDescription;
		this.prevStepId = prevStepId;
		this.nextStepId = nextStepId;
		this.distancePrevStep = distancePrevStep;
		this.distanceNextStep = distanceNextSep;
		this.distanceUnit = distanceUnit;
		this.travelSpeed = travelSpeed;
		this.travelUnit = travelUnit;
		this.timeToNext = timeToNext;
		this.timeFromPrevious = timeFromPrevious;
		this.versionId = versionId;
		this.status = status;
		this.preVerSavedTime = preVerSavedTime;
		this.preVerSavedDistance = preVerSavedDistance;
	}

	// constructors
	public TSpaghettiDataBase(int processId, int projectId, String processName, String projectName,
			int stepId, String stepDescription, int prevStepId, int nextStepId,
			int distancePrevStep, int distanceNextSep, String distanceUnit, int travelSpeed,
			String travelUnit, String timeToNext,String timeFromPrevious, int versionId, String status,
			String preVerSavedTime, String preVerSavedDistance) {

		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.stepId = stepId;
		this.stepDescription = stepDescription;
		this.prevStepId = prevStepId;
		this.nextStepId = nextStepId;
		this.distancePrevStep = distancePrevStep;
		this.distanceNextStep = distanceNextSep;
		this.distanceUnit = distanceUnit;
		this.travelSpeed = travelSpeed;
		this.travelUnit = travelUnit;
		this.timeToNext = timeToNext;
		this.timeFromPrevious = timeFromPrevious;
		this.versionId = versionId;
		this.status = status;
		this.preVerSavedTime = preVerSavedTime;
		this.preVerSavedDistance = preVerSavedDistance;
	}

	public int getSpaghettiID() {
		return spaghettiID;
	}

	public void setSpaghettiID(int spaghettiID) {
		this.spaghettiID = spaghettiID;
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

	public int getPrevStepId() {
		return prevStepId;
	}

	public void setPrevStepId(int prevStepId) {
		this.prevStepId = prevStepId;
	}

	public int getNextStepId() {
		return nextStepId;
	}

	public void setNextStepId(int nextStepId) {
		this.nextStepId = nextStepId;
	}

	public int getDistancePrevStep() {
		return distancePrevStep;
	}

	public void setDistancePrevStep(int distancePrevStep) {
		this.distancePrevStep = distancePrevStep;
	}

	public int getDistanceNextStep() {
		return distanceNextStep;
	}

	public void setDistanceNextStep(int distanceNextStep) {
		this.distanceNextStep = distanceNextStep;
	}

	public String getDistanceUnit() {
		return distanceUnit;
	}

	public void setDistanceUnit(String distanceUnit) {
		this.distanceUnit = distanceUnit;
	}

	public int getTravelSpeed() {
		return travelSpeed;
	}

	public void setTravelSpeed(int travelSpeed) {
		this.travelSpeed = travelSpeed;
	}

	public String getTravelUnit() {
		return travelUnit;
	}

	public void setTravelUnit(String travelUnit) {
		this.travelUnit = travelUnit;
	}

	public String getTimeToNext() {
		return timeToNext;
	}

	public void setTimeToNext(String timeToNext) {
		this.timeToNext = timeToNext;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPreVerSavedTime() {
		return preVerSavedTime;
	}

	public void setPreVerSavedTime(String preVerSavedTime) {
		this.preVerSavedTime = preVerSavedTime;
	}

	public String getPreVerSavedDistance() {
		return preVerSavedDistance;
	}

	public void setPreVerSavedDistance(String preVerSavedDistance) {
		this.preVerSavedDistance = preVerSavedDistance;
	}

	public String getTimeFromPrevious() {
		return timeFromPrevious;
	}

	public void setTimeFromPrevious(String timeFromPrevious) {
		this.timeFromPrevious = timeFromPrevious;
	}
}
