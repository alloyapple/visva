package vsvteam.outsource.leanappandroid.database;

public class TProcessDataBase {
	// private variables
	private int processId;// process id
	private int projectId;// project id
	private String projectName;// project name
	private String processName;// process name
	private String processDescription;// process description
	private String defectNotes; // defect notes
	private int totalCycleTime;// total cycle time
	private int valueAddingTime;// value adding time
	private int nonValueAddingTime;// non value adding time
	private int defectPercent;// defect percent

	private int shifts;// shift per day

	private int totDistanceTraveled;// total distance traveled
	private int upTime; // up time

	private int taktTime; // takt time id
	private int versionId; // version id
	private String previousProcess; // previous process
	private String nextProcess; // next process
	private String vsmName;// vsvName
	private boolean verify_details;//verify detail
	private int outputInventory;//out put inventory

	private String supplierStartPoint;//supplier start point
	private String customerEndPoint;//customer end point
	private String communication;//communication

	// empty constructor
	public TProcessDataBase() {

	}

	// construtor
	public TProcessDataBase(int processId, int projectId, String projectName, String processName,
			String processDescription, String defectNotes, int totalCycleTime, int valueAddingTime,
			int nonValueAddingTime, int defectPercent, int shifts, int totDistanceTraveled,
			int upTime, int taktTime, int versionId, String previousProcess, String nextProcess,
			String vsmName, boolean verify_details, int outputInventory, String supplierStartPoint,
			String customerEndPoint, String communication) {
		this.processId = processId;
		this.projectId = projectId;
		this.projectName = projectName;
		this.processName = processName;
		this.processDescription = processDescription;
		this.defectNotes = defectNotes;
		this.totalCycleTime = totalCycleTime;
		this.valueAddingTime = valueAddingTime;
		this.nonValueAddingTime = nonValueAddingTime;
		this.defectPercent = defectPercent;
		this.shifts = shifts;
		this.totDistanceTraveled = totDistanceTraveled;
		this.upTime = upTime;
		this.taktTime = taktTime;
		this.versionId = versionId;
		this.previousProcess = previousProcess;
		this.nextProcess = nextProcess;
		this.vsmName = vsmName;
		this.verify_details = verify_details;
		this.outputInventory = outputInventory;
		this.supplierStartPoint = supplierStartPoint;
		this.customerEndPoint = customerEndPoint;
		this.communication = communication;
	}

	// construtor
	public TProcessDataBase(int projectId, String projectName, String processName,
			String processDescription, String defectNotes, int totalCycleTime, int valueAddingTime,
			int nonValueAddingTime, int defectPercent, int shifts, int totDistanceTraveled,
			int upTime, int taktTime, int versionId, String previousProcess, String nextProcess,
			String vsmName, boolean verify_details, int outputInventory, String supplierStartPoint,
			String customerEndPoint, String communication) {
		this.projectId = projectId;
		this.projectName = projectName;
		this.processName = processName;
		this.processDescription = processDescription;
		this.defectNotes = defectNotes;
		this.totalCycleTime = totalCycleTime;
		this.valueAddingTime = valueAddingTime;
		this.nonValueAddingTime = nonValueAddingTime;
		this.defectPercent = defectPercent;
		this.shifts = shifts;
		this.totDistanceTraveled = totDistanceTraveled;
		this.upTime = upTime;
		this.taktTime = taktTime;
		this.versionId = versionId;
		this.previousProcess = previousProcess;
		this.nextProcess = nextProcess;
		this.vsmName = vsmName;
		this.verify_details = verify_details;
		this.outputInventory = outputInventory;
		this.supplierStartPoint = supplierStartPoint;
		this.customerEndPoint = customerEndPoint;
		this.communication = communication;
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

	
	public int getShifts() {
		return shifts;
	}

	public void setShifts(int shifts) {
		this.shifts = shifts;
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

	public String getDefectNotes() {
		return defectNotes;
	}

	public void setDefectNotes(String defectNotes) {
		this.defectNotes = defectNotes;
	}

	public boolean isVerify_details() {
		return verify_details;
	}

	public void setVerify_details(boolean verify_details) {
		this.verify_details = verify_details;
	}

	public int getOutputInventory() {
		return outputInventory;
	}

	public void setOutputInventory(int outputInventory) {
		this.outputInventory = outputInventory;
	}

	public String getSupplierStartPoint() {
		return supplierStartPoint;
	}

	public void setSupplierStartPoint(String supplierStartPoint) {
		this.supplierStartPoint = supplierStartPoint;
	}

	public String getCustomerEndPoint() {
		return customerEndPoint;
	}

	public void setCustomerEndPoint(String customerEndPoint) {
		this.customerEndPoint = customerEndPoint;
	}

	public String getCommunication() {
		return communication;
	}

	public void setCommunication(String communication) {
		this.communication = communication;
	}
}