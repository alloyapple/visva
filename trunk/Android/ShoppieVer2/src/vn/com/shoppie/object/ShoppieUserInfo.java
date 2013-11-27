package vn.com.shoppie.object;

public class ShoppieUserInfo {

	private String name;
	private String phone;
	private String id;
	private String email;
	private String address;
	private int gender;
	private String avatar;
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	private String cover;

	public ShoppieUserInfo(String name, String phone, String id, String email,
			String address, int gender, String avatar, String cover) {
		super();
		this.name = name;
		this.phone = phone;
		this.id = id;
		this.email = email;
		this.address = address;
		this.gender = gender;
		this.avatar = avatar;
		this.cover = cover;
	}

	public ShoppieUserInfo(String name, String phone, String email,
			String address, int gender, String avatar, String cover) {
		super();
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.gender = gender;
		this.avatar = avatar;
		this.cover = cover;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

}
