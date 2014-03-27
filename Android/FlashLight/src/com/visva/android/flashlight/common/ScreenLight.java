package com.visva.android.flashlight.common;

public class ScreenLight {

	private String name = "";
	private int id = 0;
	private int bitmapId = 0;
	private boolean isCheck = false;

	public ScreenLight() {
		super();
	}

	public ScreenLight(String name, int id, int bitmapId) {
		super();
		this.name = name;
		this.id = id;
		this.bitmapId = bitmapId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBitmapId() {
		return bitmapId;
	}

	public void setBitmapId(int bitmapId) {
		this.bitmapId = bitmapId;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public void toggedCheck(){
		this.isCheck = !isCheck;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bitmapId;
		result = prime * result + id;
		result = prime * result + (isCheck ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ScreenLight other = (ScreenLight) obj;
		if (bitmapId != other.bitmapId) return false;
		if (id != other.id) return false;
		if (isCheck != other.isCheck) return false;
		if (name == null) {
			if (other.name != null) return false;
		} else if (!name.equals(other.name)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScreenLight [name=" + name + ", id=" + id + ", bitmapId=" + bitmapId + ", isCheck=" + isCheck + "]";
	}

}
