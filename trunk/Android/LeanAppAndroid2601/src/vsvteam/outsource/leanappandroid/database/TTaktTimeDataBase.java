package vsvteam.outsource.leanappandroid.database;

public class TTaktTimeDataBase {
	// private variables
	private int taktTimeId;// takt time id
	private int processId;// process Id
	private int projectId;// project Id
	private String processName;// process Name
	private String projectName;// project Name
	private int shiftPerDay;// shift per day
	private int hourPerShift;// hour per shift
	private int breakTimePerShift;// break time per shift(minutes)
	private int operatorPerShift; // operator per shift
	private int customerDemandPerUnits; // customer demand per shift(minutes)
	private String calculatdTaktTime; // calculated takt time(minutes)
	private int versionId; // version id

	// empty constructor
	public TTaktTimeDataBase() {

	}

	// constructor
	public TTaktTimeDataBase(int taktTimeId, int processId, int projectId, String processName,
			String projectName, int shiftPerDay, int hourPerShift, int breakTimePerShift,
			int operatorPerShift, int customerDemandPerUnits, String calculatedTaktTime,
			int versionId) {
		this.taktTimeId = taktTimeId;
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.shiftPerDay = shiftPerDay;
		this.hourPerShift = hourPerShift;
		this.breakTimePerShift = breakTimePerShift;
		this.operatorPerShift = operatorPerShift;
		this.customerDemandPerUnits = customerDemandPerUnits;
		this.calculatdTaktTime = calculatedTaktTime;
		this.versionId = versionId;
	}

	// constructor
	public TTaktTimeDataBase(int processId, int projectId, String processName, String projectName,
			int shiftPerDay, int hourPerShift, int breakTimePerShift, int operatorPerShift,
			int customerDemandPerUnits, String calculatedTaktTime, int versionId) {
		this.processId = processId;
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.shiftPerDay = shiftPerDay;
		this.hourPerShift = hourPerShift;
		this.breakTimePerShift = breakTimePerShift;
		this.operatorPerShift = operatorPerShift;
		this.customerDemandPerUnits = customerDemandPerUnits;
		this.calculatdTaktTime = calculatedTaktTime;
		this.versionId = versionId;
	}

	public int getTaktTimeId() {
		return taktTimeId;
	}

	public void setTaktTimeId(int taktTimeId) {
		this.taktTimeId = taktTimeId;
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

	public int getShiftPerDay() {
		return shiftPerDay;
	}

	public void setShiftPerDay(int shiftPerDay) {
		this.shiftPerDay = shiftPerDay;
	}

	public int getHourPerShift() {
		return hourPerShift;
	}

	public void setHourPerShift(int hourPerShift) {
		this.hourPerShift = hourPerShift;
	}

	public int getBreakTimePerShift() {
		return breakTimePerShift;
	}

	public void setBreakTimePerShift(int breakTimePerShift) {
		this.breakTimePerShift = breakTimePerShift;
	}

	public int getCustomerDemandPerUnits() {
		return customerDemandPerUnits;
	}

	public void setCustomerDemandPerUnits(int customerDemandPerUnits) {
		this.customerDemandPerUnits = customerDemandPerUnits;
	}

	public String getCalculatdTaktTime() {
		return calculatdTaktTime;
	}

	public void setCalculatdTaktTime(String calculatdTaktTime) {
		this.calculatdTaktTime = calculatdTaktTime;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public int getOperatorPerShift() {
		return operatorPerShift;
	}

	public void setOperatorPerShift(int operatorPerShift) {
		this.operatorPerShift = operatorPerShift;
	}
}
