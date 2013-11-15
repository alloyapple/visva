package vn.com.shoppie.object;

public class FacebookUser {

	private Picture picture;

	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String id;
	private String name;

	public class Picture {
		private Data data;

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}

		public Picture() {

		}

		public class Data {
			private String url;

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public Data() {

			}

		}
	}
}
