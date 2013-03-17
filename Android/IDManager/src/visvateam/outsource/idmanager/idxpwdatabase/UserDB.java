package visvateam.outsource.idmanager.idxpwdatabase;

public class UserDB {
	// all variables
	private int userId;
	private String password;

	public UserDB(int userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
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

}
