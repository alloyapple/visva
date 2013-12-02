package vn.com.shoppie.object;

public class Collection {
	private int _id;

	public Collection(int _id, int merchId, int collectionId, boolean isViewed) {
		super();
		this._id = _id;
		this.merchId = merchId;
		this.collectionId = collectionId;
		this.isViewed = isViewed;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	private int merchId;
	private int collectionId;
	private boolean isViewed;

	public Collection(int merchId, int collectionId, boolean isViewed) {
		super();
		this.merchId = merchId;
		this.collectionId = collectionId;
		this.isViewed = isViewed;
	}

	public Collection() {
		// TODO Auto-generated constructor stub
	}

	public int getMerchId() {
		return merchId;
	}

	public void setMerchId(int merchId) {
		this.merchId = merchId;
	}

	public int getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(int collectionId) {
		this.collectionId = collectionId;
	}

	public boolean isViewed() {
		return isViewed;
	}

	public void setViewed(boolean isViewed) {
		this.isViewed = isViewed;
	}

}
