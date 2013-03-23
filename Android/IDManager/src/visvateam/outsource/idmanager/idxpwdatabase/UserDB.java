package visvateam.outsource.idmanager.idxpwdatabase;

public class UserDB {
	// all variables
	private int userId;
	private String password;
	private String sEmail;

	public UserDB(int userId, String password,String email) {
		super();
		this.userId = userId;
		this.password = password;
		this.sEmail = email;
	}

	// empty constructor
	public UserDB() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getsEmail() {
		return sEmail;
	}

	public void setsEmail(String sEmail) {
		this.sEmail = sEmail;
	}

}
