package visvateam.outsource.idmanager.idxpwdatabase;

public class User {
	// all variables
	private int userId;
	private String password;

	public User(int userId, String password) {
		super();
		this.userId = userId;
		this.password = password;
	}

	// empty constructor
	public User() {
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
