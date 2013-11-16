package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class UserDataInfo {

	@SerializedName("dataValue")
	private int dataValue;
	@SerializedName("custCode")
	private String custCode;
	@SerializedName("deviceId")
	private String deviceId;
	@SerializedName("custName")
	private String custName;
	@SerializedName("custPhone")
	private String custPhone;
	@SerializedName("custEmail")
	private String custEmail;
	@SerializedName("gender")
	private String gender;
	@SerializedName("birthday")
	private String birthday;
	@SerializedName("custAddress")
	private String custAddress;

	public UserDataInfo() {

	}

	public int getDataValue() {
		return dataValue;
	}

	public void setDataValue(int dataValue) {
		this.dataValue = dataValue;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCustAddress() {
		return custAddress;
	}

	public void setCustAddress(String custAddress) {
		this.custAddress = custAddress;
	}

}
