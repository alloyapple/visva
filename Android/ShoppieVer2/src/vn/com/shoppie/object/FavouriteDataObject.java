package vn.com.shoppie.object;

public class FavouriteDataObject {
	private int _id;
	private String image_url;
	private String type;

	public FavouriteDataObject() {

	}

	public FavouriteDataObject(int _id, String image_url, String type) {
		super();
		this._id = _id;
		this.image_url = image_url;
		this.type = type;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
