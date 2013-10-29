package vn.com.shoppie.object;

import android.content.Context;

public class FBUser {
	private String userName;
	private String userAvatarLink;
	private Context context;

	public FBUser(Context context, String userName, String userLink) {
		this.context = context;
		this.userName = userName;
		this.userAvatarLink = userLink;
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
}
