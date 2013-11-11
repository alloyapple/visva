package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class FriendItem {
	@SerializedName("custId")
	private int custId;
	@SerializedName("custName")
	private String custName;
	@SerializedName("custPhone")
	private String custPhone;
	@SerializedName("facebookid")
	private String facebookid;
	@SerializedName("currentBal")
	private int currentBal;

	public FriendItem() {

	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCustPhone() {
		return custPhone;
	}

	public void setCustPhone(String custPhone) {
		this.custPhone = custPhone;
	}

	public String getFacebookid() {
		return facebookid;
	}

	public void setFacebookid(String facebookid) {
		this.facebookid = facebookid;
	}

	public int getCurrentBal() {
		return currentBal;
	}

	public void setCurrentBal(int currentBal) {
		this.currentBal = currentBal;
	}
	
	
}
