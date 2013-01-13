package vsvteam.outsource.leanappandroid.database;

public class TProjectDataBase {

	// private variables
	private int projectID;
	private String projectName;
	private String companyName;
	private String projectDescription;
	private String companyAddress;
	private String notes;

	// Empty constructor
	public TProjectDataBase() {

	}

	// constructor
	public TProjectDataBase(int id, String projectName, String companyName,
			String projectDescription, String companyAddress, String notes) {
		this.projectID = id;
		this.projectName = projectName;
		this.companyName = companyName;
		this.projectDescription = projectDescription;
		this.companyAddress = companyAddress;
		this.notes = notes;
	}

	// constructor
	public TProjectDataBase(String projectName, String companyName, String projectDescription,
			String companyAddress, String notes) {
		this.projectName = projectName;
		this.companyName = companyName;
		this.projectDescription = projectDescription;
		this.companyAddress = companyAddress;
		this.notes = notes;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
