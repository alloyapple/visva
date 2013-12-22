package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class CheckedFBFriend {
	@SerializedName("Result")
	private ArrayList<FriendID> Result;

	public ArrayList<FriendID> getResult() {
		return Result;
	}

	public void setResult(ArrayList<FriendID> result) {
		Result = result;
	}

	public CheckedFBFriend() {

	}

	public class FriendID {
		@SerializedName("friendId")
		private String friendId;

		public String getFriendId() {
			return friendId;
		}

		public void setFriendId(String friendId) {
			this.friendId = friendId;
		}

		public FriendID() {

		}

	}
}
