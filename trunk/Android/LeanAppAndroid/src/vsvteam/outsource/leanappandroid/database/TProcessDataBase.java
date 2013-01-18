package vsvteam.outsource.leanappandroid.database;

public class TProcessDataBase {
	// private variables
	private int processId;// process id
	private int projectId;//project id
	private String projectName;// project name
	private String processName;// process name
	private String processDescription;// process description
	private String processNotes;// process notes
	private int totalCycleTime;// total cycle time
	private int valueAddingTime;// value adding time
	private int nonValueAddingTime;// non value adding time
	private int defectPercent;// defect percent
	private int totOperators;// total operator
	private int shifts;// shift per day
	private int availability;// availability
	private int totDistanceTraveled;// total distance traveled
	private int upTime; // up time
	private int changeOverTime; // change over time
	private int taktTime; // takt time id
	private int versionId; // version id
	private String previousProcess; // previous process
	private String nextProcess; // next process
	private String vsmName;// vsvName

	// empty constructor
	public TProcessDataBase() {

	}

	// construtor
	public TProcessDataBase(int processId,int projectId, String projectName, String processName,
			String processDescription, String processNotes, int totalCycleTime,
			int valueAddingTime, int nonValueAddingTime, int defectPercent, int totOperators,
			int shifts, int availability, int totDistanceTraveled, int upTime, int changeOverTime,
			int taktTime, int versionId, String previousProcess, String nextProcess, String vsmName) {
		this.processId = processId;
		this.projectId = projectId;
		this.projectName = projectName;
		this.processName = processName;
		this.processDescription = processDescription;
		this.processNotes = processNotes;
		this.totalCycleTime = totalCycleTime;
		this.valueAddingTime = valueAddingTime;
		this.nonValueAddingTime = nonValueAddingTime;
		this.defectPercent = defectPercent;
		this.totOperators = totOperators;
		this.shifts = shifts;
		this.availability = availability;
		this.totDistanceTraveled = totDistanceTraveled;
		this.upTime = upTime;
		this.changeOverTime = changeOverTime;
		this.taktTime = taktTime;
		this.versionId = versionId;
		this.previousProcess = previousProcess;
		this.nextProcess = nextProcess;
		this.vsmName = vsmName;
	}

	// construtor
	public TProcessDataBase(int projectId,String projectName, String processName, String processDescription,
			String processNotes, int totalCycleTime, int valueAddingTime, int nonValueAddingTime,
			int defectPercent, int totOperators, int shifts, int availability,
			int totDistanceTraveled, int upTime, int changeOverTime, int taktTime, int versionId,
			String previousProcess, String nextProcess, String vsmName) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.processName = processName;
		this.processDescription = processDescription;
		this.processNotes = processNotes;
		this.totalCycleTime = totalCycleTime;
		this.valueAddingTime = valueAddingTime;
		this.nonValueAddingTime = nonValueAddingTime;
		this.defectPercent = defectPercent;
		this.totOperators = totOperators;
		this.shifts = shifts;
		this.availability = availability;
		this.totDistanceTraveled = totDistanceTraveled;
		this.upTime = upTime;
		this.changeOverTime = changeOverTime;
		this.taktTime = taktTime;
		this.versionId = versionId;
		this.previousProcess = previousProcess;
		this.nextProcess = nextProcess;
		this.vsmName = vsmName;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessDescription() {
		return processDescription;
	}

	public void setProcessDescription(String processDescription) {
		this.processDescription = processDescription;
	}

	public String getProcessNotes() {
		return processNotes;
	}

	public void setProcessNotes(String processNotes) {
		this.processNotes = processNotes;
	}

	public int getTotalCycleTime() {
		return totalCycleTime;
	}

	public void setTotalCycleTime(int totalCycleTime) {
		this.totalCycleTime = totalCycleTime;
	}

	public int getValueAddingTime() {
		return valueAddingTime;
	}

	public void setValueAddingTime(int valueAddingTime) {
		this.valueAddingTime = valueAddingTime;
	}

	public int getNonValueAddingTime() {
		return nonValueAddingTime;
	}

	public void setNonValueAddingTime(int nonValueAddingTime) {
		this.nonValueAddingTime = nonValueAddingTime;
	}

	public int getDefectPercent() {
		return defectPercent;
	}

	public void setDefectPercent(int defectPercent) {
		this.defectPercent = defectPercent;
	}

	public int getTotOperators() {
		return totOperators;
	}

	public void setTotOperators(int totOperators) {
		this.totOperators = totOperators;
	}

	public int getShifts() {
		return shifts;
	}

	public void setShifts(int shifts) {
		this.shifts = shifts;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public int getTotDistanceTraveled() {
		return totDistanceTraveled;
	}

	public void setTotDistanceTraveled(int totDistanceTraveled) {
		this.totDistanceTraveled = totDistanceTraveled;
	}

	public int getUpTime() {
		return upTime;
	}

	public void setUpTime(int upTime) {
		this.upTime = upTime;
	}

	public int getChangeOverTime() {
		return changeOverTime;
	}

	public void setChangeOverTime(int changeOverTime) {
		this.changeOverTime = changeOverTime;
	}

	public int getTaktTime() {
		return taktTime;
	}

	public void setTaktTime(int taktTime) {
		this.taktTime = taktTime;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public String getPreviousProcess() {
		return previousProcess;
	}

	public void setPreviousProcess(String previousProcess) {
		this.previousProcess = previousProcess;
	}

	public String getNextProcess() {
		return nextProcess;
	}

	public void setNextProcess(String nextProcess) {
		this.nextProcess = nextProcess;
	}

	public String getVsmName() {
		return vsmName;
	}

	public void setVsmName(String vsmName) {
		this.vsmName = vsmName;
	}

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}
}