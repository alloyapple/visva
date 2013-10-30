package vn.com.shoppie.object;

import android.content.Context;

public class FBUser {
	private String userName;
	private String userAvatarLink;
	private Context context;
	private boolean isJoinSP = false;
	private int numberPie;
	private String userId;

	public FBUser(Context context, String userName, String userLink,
			boolean isJoinSP, int numberPie, String userId) {
		this.context = context;
		this.userName = userName;
		this.userAvatarLink = userLink;
		this.isJoinSP = isJoinSP;
		this.numberPie = numberPie;
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserAvatarLink() {
		return userAvatarLink;
	}

	public void setUserAvatarLink(String userAvatarLink) {
		this.userAvatarLink = userAvatarLink;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public boolean isJoinSP() {
		return isJoinSP;
	}

	public void setJoinSP(boolean isJoinSP) {
		this.isJoinSP = isJoinSP;
	}

	public int getNumberPie() {
		return numberPie;
	}

	public void setNumberPie(int numberPie) {
		this.numberPie = numberPie;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
