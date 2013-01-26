package vsvteam.outsource.leanappandroid.database;

public class TPQPRDataBase {
	// private all variables
	private int tpqprId;
	private int processId;
	private int projectId;
	private String processName;
	private String projectName;
	private int partNo;
	private int demandQty;
	private int percentOnTotal;
	private int sequenceNo;
	private int usedInStepId;
	private int versionId;
	private boolean tFocus;

	// empty construtor
	public TPQPRDataBase() {

	}

	// construtor
	public TPQPRDataBase(int tpqprId, int processId, int projectId, String processName,
			String projectName, int partNo, int demandQty, int percentOnTotal, int sequenceNo,
			int usedInStepId, int versionId, boolean tFocus) {
		this.tpqprId = tpqprId;
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.partNo = partNo;
		this.demandQty = demandQty;
		this.percentOnTotal = percentOnTotal;
		this.sequenceNo = sequenceNo;
		this.usedInStepId = usedInStepId;
		this.versionId = versionId;
		this.tFocus = tFocus;
	}

	// constructor
	public TPQPRDataBase(int processId, int projectId, String processName, String projectName,
			int partNo, int demandQty, int percentOnTotal, int sequenceNo, int usedInStepId,
			int versionId, boolean tFocus) {
		this.processId = processId;
		this.projectId = projectId;
		this.processName = processName;
		this.projectName = projectName;
		this.partNo = partNo;
		this.demandQty = demandQty;
		this.percentOnTotal = percentOnTotal;
		this.sequenceNo = sequenceNo;
		this.usedInStepId = usedInStepId;
		this.versionId = versionId;
		this.tFocus = tFocus;
	}

	public int getTpqprId() {
		return tpqprId;
	}

	public void setTpqprId(int tpqprId) {
		this.tpqprId = tpqprId;
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

	public int getPartNo() {
		return partNo;
	}

	public void setPartNo(int partNo) {
		this.partNo = partNo;
	}

	public int getDemandQty() {
		return demandQty;
	}

	public void setDemandQty(int demandQty) {
		this.demandQty = demandQty;
	}

	public int getPercentOnTotal() {
		return percentOnTotal;
	}

	public void setPercentOnTotal(int percentOnTotal) {
		this.percentOnTotal = percentOnTotal;
	}

	public int getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public int getUsedInStepId() {
		return usedInStepId;
	}

	public void setUsedInStepId(int usedInStepId) {
		this.usedInStepId = usedInStepId;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public boolean isTfocus() {
		return tFocus;
	}

	public void setTfocus(boolean tFocus) {
		this.tFocus = tFocus;
	}
}
