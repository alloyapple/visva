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

	private int distanceNextStep;// distance next step
	private String distanceUnit;// distance unit
	private int operatorSpeed;// operator speed
	private String timeToNext;// time to next
	private int versionId;// version id

	// empty constructor
	public TSpaghettiDataBase() {

	}

	// constructors
	public TSpaghettiDataBase(int spaghettiId, int processId, int projectId,
			String processName, String projectName, int stepId,
			String stepDescription, int prevStepId, int nextStepId,
			int distanceNextSep, String distanceUnit, int operatorSpeed,
			String timeToNext, int versionId) {

		this.spaghettiID = spaghettiId;
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.stepId = stepId;
		this.stepDescription = stepDescription;
		this.prevStepId = prevStepId;
		this.nextStepId = nextStepId;
		this.distanceNextStep = distanceNextSep;
		this.distanceUnit = distanceUnit;
		this.operatorSpeed = operatorSpeed;
		this.timeToNext = timeToNext;
		this.versionId = versionId;
	}

	// constructors
	public TSpaghettiDataBase(int processId, int projectId, String processName,
			String projectName, int stepId, String stepDescription,
			int prevStepId, int nextStepId, int distanceNextSep,
			String distanceUnit, int operatorSpeed, String timeToNext,
			int versionId) {

		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.stepId = stepId;
		this.stepDescription = stepDescription;
		this.prevStepId = prevStepId;
		this.nextStepId = nextStepId;
		this.distanceNextStep = distanceNextSep;
		this.distanceUnit = distanceUnit;
		this.operatorSpeed = operatorSpeed;
		this.timeToNext = timeToNext;
		this.versionId = versionId;
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

	public int getOperatorSpeed() {
		return operatorSpeed;
	}

	public void setOperatorSpeed(int operatorSpeed) {
		this.operatorSpeed = operatorSpeed;
	}
}
