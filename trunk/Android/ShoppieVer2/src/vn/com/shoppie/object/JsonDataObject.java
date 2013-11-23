package vn.com.shoppie.object;

public class JsonDataObject {
	private int _id;
	private String type;
	private String jsonData;

	public JsonDataObject() {

	}

	public JsonDataObject(String jsonData, String type) {
		super();
		this.type = type;
		this.jsonData = jsonData;
	}

	public JsonDataObject(int _id, String jsonData, String type) {
		super();
		this._id = _id;
		this.type = type;
		this.jsonData = jsonData;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

}
