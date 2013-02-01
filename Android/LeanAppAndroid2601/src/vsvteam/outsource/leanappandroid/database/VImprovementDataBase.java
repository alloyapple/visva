package vsvteam.outsource.leanappandroid.database;

public class VImprovementDataBase {
	// private all variables
	private int vImprovementId;
	private int stepId;
	private int processId;
	private int projectId;
	private String stepName;
	private String processName;
	private String projectName;

	private int versionId;
	private int preVerionId;
	private String sumOfPreVersDifferenceLowestTime;
	private String sumOfPreVersDifferenceAdjustedTime;
	private String sumOfPreVersDifferenceAdjustment;
	private String sumOfLowesTime;
	private String sumOfAdjustment;
	private String sumOfAdjustedTime;
	private int throughPut;
	private int PreVersDifferenceThroughPut;
	private int volXTime;
	private int savedTravelDistance;

	private String savedTravelTime;
	private int tUnitid;

	// empty constructor
	public VImprovementDataBase() {

	}

	// constructor
	public VImprovementDataBase(int vImprovementId, int stepId, int processId,
			int projectId, String stepName, String processName,
			String projectName, int versionId, int preVersionId,
			String sumOfPreVerDifferenceLowestTime,
			String sumOfPreVersDifferenceAdjustedTime,
			String sumOfPreVersDifferenceAdjustment, String sumOfLowestTime,
			String sumOfAdjustment, String sumOfAdjustedTime, int throughPut,
			int PreVersDifferenceThroughPut, int volXTime,
			int savedTravelDistance, String savedTravelTime, int tUnitid) {

		this.vImprovementId = vImprovementId;
		this.stepId = stepId;
		this.processId = processId;
		this.projectId = projectId;
		this.stepName = stepName;
		this.processName = processName;
		this.projectName = projectName;
		this.versionId = versionId;
		this.sumOfPreVersDifferenceLowestTime = sumOfPreVerDifferenceLowestTime;
		this.sumOfPreVersDifferenceAdjustedTime = sumOfAdjustedTime;
		this.sumOfPreVersDifferenceAdjustment = sumOfPreVersDifferenceAdjustment;
		this.sumOfLowesTime = sumOfLowestTime;
		this.sumOfAdjustment = sumOfAdjustment;
		this.sumOfAdjustedTime = sumOfAdjustedTime;
		this.throughPut = throughPut;
		this.PreVersDifferenceThroughPut = PreVersDifferenceThroughPut;
		this.volXTime = volXTime;
		this.savedTravelDistance = savedTravelDistance;
		this.savedTravelTime = savedTravelTime;
		this.tUnitid = tUnitid;
	}

	// constructor
	public VImprovementDataBase(int stepId, int processId, int projectId,
			String stepName, String processName, String projectName,
			int versionId, int preVersionId,
			String sumOfPreVerDifferenceLowestTime,
			String sumOfPreVersDifferenceAdjustedTime,
			String sumOfPreVersDifferenceAdjustment, String sumOfLowestTime,
			String sumOfAdjustment, String sumOfAdjustedTime, int throughPut,
			int PreVersDifferenceThroughPut, int volXTime,
			int savedTravelDistance, String savedTravelTime, int tUnitid) {

		this.stepId = stepId;
		this.processId = processId;
		this.projectId = projectId;
		this.stepName = stepName;
		this.processName = processName;
		this.projectName = projectName;
		this.versionId = versionId;
		this.sumOfPreVersDifferenceLowestTime = sumOfPreVerDifferenceLowestTime;
		this.sumOfPreVersDifferenceAdjustedTime = sumOfAdjustedTime;
		this.sumOfPreVersDifferenceAdjustment = sumOfPreVersDifferenceAdjustment;
		this.sumOfLowesTime = sumOfLowestTime;
		this.sumOfAdjustment = sumOfAdjustment;
		this.sumOfAdjustedTime = sumOfAdjustedTime;
		this.throughPut = throughPut;
		this.PreVersDifferenceThroughPut = PreVersDifferenceThroughPut;
		this.volXTime = volXTime;
		this.savedTravelDistance = savedTravelDistance;
		this.savedTravelTime = savedTravelTime;
		this.tUnitid = tUnitid;
	}

	public int getvImprovementId() {
		return vImprovementId;
	}

	public void setvImprovementId(int vImprovementId) {
		this.vImprovementId = vImprovementId;
	}

	public int getStepId() {
		return stepId;
	}

	public void setStepId(int stepId) {
		this.stepId = stepId;
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

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
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

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public int getPreVerionId() {
		return preVerionId;
	}

	public void setPreVerionId(int preVerionId) {
		this.preVerionId = preVerionId;
	}

	public String getSumOfPreVersDifferenceLowestTime() {
		return sumOfPreVersDifferenceLowestTime;
	}

	public void setSumOfPreVersDifferenceLowestTime(
			String sumOfPreVersDifferenceLowestTime) {
		this.sumOfPreVersDifferenceLowestTime = sumOfPreVersDifferenceLowestTime;
	}

	public String getSumOfPreVersDifferenceAdjustedTime() {
		return sumOfPreVersDifferenceAdjustedTime;
	}

	public void setSumOfPreVersDifferenceAdjustedTime(
			String sumOfPreVersDifferenceAdjustedTime) {
		this.sumOfPreVersDifferenceAdjustedTime = sumOfPreVersDifferenceAdjustedTime;
	}

	public String getSumOfPreVersDifferenceAdjustment() {
		return sumOfPreVersDifferenceAdjustment;
	}

	public void setSumOfPreVersDifferenceAdjustment(
			String sumOfPreVersDifferenceAdjustment) {
		this.sumOfPreVersDifferenceAdjustment = sumOfPreVersDifferenceAdjustment;
	}

	public String getSumOfLowesTime() {
		return sumOfLowesTime;
	}

	public void setSumOfLowesTime(String sumOfLowesTime) {
		this.sumOfLowesTime = sumOfLowesTime;
	}

	public String getSumOfAdjustment() {
		return sumOfAdjustment;
	}

	public void setSumOfAdjustment(String sumOfAdjustment) {
		this.sumOfAdjustment = sumOfAdjustment;
	}

	public String getSumOfAdjustedTime() {
		return sumOfAdjustedTime;
	}

	public void setSumOfAdjustedTime(String sumOfAdjustedTime) {
		this.sumOfAdjustedTime = sumOfAdjustedTime;
	}

	public int getThroughPut() {
		return throughPut;
	}

	public void setThroughPut(int throughPut) {
		this.throughPut = throughPut;
	}

	public int getPreVersDifferenceThroughPut() {
		return PreVersDifferenceThroughPut;
	}

	public void setPreVersDifferenceThroughPut(int preVersDifferenceThroughPut) {
		PreVersDifferenceThroughPut = preVersDifferenceThroughPut;
	}

	public int getVolXTime() {
		return volXTime;
	}

	public void setVolXTime(int volXTime) {
		this.volXTime = volXTime;
	}

	public int getSavedTravelDistance() {
		return savedTravelDistance;
	}

	public void setSavedTravelDistance(int savedTravelDistance) {
		this.savedTravelDistance = savedTravelDistance;
	}

	public String getSavedTravelTime() {
		return savedTravelTime;
	}

	public void setSavedTravelTime(String savedTravelTime) {
		this.savedTravelTime = savedTravelTime;
	}

	public int gettUnitid() {
		return tUnitid;
	}

	public void settUnitid(int tUnitid) {
		this.tUnitid = tUnitid;
	}

}
