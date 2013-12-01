package vn.com.shoppie.object;

public class FavouriteDataObject {
	private int _id;
	private String image_url;
	private String type;
	private String favourite_id;

	public String getFavourite_id() {
		return favourite_id;
	}

	public void setFavourite_id(String favourite_id) {
		this.favourite_id = favourite_id;
	}

	public FavouriteDataObject() {

	}

	public FavouriteDataObject(String image_url, String type,
			String favourite_id) {
		super();
		this.image_url = image_url;
		this.type = type;
		this.favourite_id = favourite_id;
	}

	public FavouriteDataObject(int _id, String image_url, String type,
			String favourite_id) {
		super();
		this._id = _id;
		this.image_url = image_url;
		this.type = type;
		this.favourite_id = favourite_id;
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
