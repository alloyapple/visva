package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class ParamMobileItem {
	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("value")
	private String value;
	@SerializedName("status")
	private String status;

	public ParamMobileItem() {

	}

	public ParamMobileItem(int id, String name, String value, String status) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
