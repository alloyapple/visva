package visvateam.outsource.idmanager.database;

public class UserDataBase {
	// all variables
	private int userId;
	private String userPassword;
	private String lastTimeSignIn;

	public UserDataBase() {
		super();
	}

	public UserDataBase(int userId, String userPassword, String lastTimeSignIn) {
		super();
		this.userId = userId;
		this.userPassword = userPassword;
		this.lastTimeSignIn = lastTimeSignIn;
	}

	public UserDataBase(String userPassword, String lastTimeSignIn) {
		super();
		this.userPassword = userPassword;
		this.lastTimeSignIn = lastTimeSignIn;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getLastTimeSignIn() {
		return lastTimeSignIn;
	}

	public void setLastTimeSignIn(String lastTimeSignIn) {
		this.lastTimeSignIn = lastTimeSignIn;
	}

}
