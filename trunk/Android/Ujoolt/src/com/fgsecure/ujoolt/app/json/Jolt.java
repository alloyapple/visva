package com.fgsecure.ujoolt.app.json;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.fgsecure.ujoolt.app.define.LoginType;
import com.fgsecure.ujoolt.app.utillity.ConfigUtility;
import com.fgsecure.ujoolt.app.utillity.JoltHolder;

public class Jolt {
	private int latitudeE6;
	private int longitudeE6;
	private int numberRejolt;
	private int numberFacebookRejolt;
	private int positionArr;

	private String nick;
	private String photoURL;
	private String videoURL;
	private String text;
	private String id;
	private String loginUserid;
	private String instagramId;
	private String facebookId;
	private String deviceId;

	private long date;

	private double distance;
	private double radius;
	private double lifeTime;

	private boolean newCreate;
	private boolean like;
	private boolean top;
	private boolean publicFacebook;

	private SoftReference<Bitmap> photoBitmap;
	private LoginType loginType;
	private JoltHolder joltHolder;

	public int groupID;
	public boolean isGrouped;
	public boolean isRejolted;
	public ArrayList<String> arrayRejoltId = new ArrayList<String>();

	public Jolt(JoltHolder joltHolder) {
		this.joltHolder = joltHolder;
		nick = "";
		photoURL = "";
		videoURL = "";
		text = "";
		id = "";
		loginUserid = "";
		instagramId = "";
		facebookId = "";
		deviceId = "";
	}

	public Jolt(Jolt jolt) {
		setLatitudeE6(jolt.getLatitudeE6());
		setLongitudeE6(jolt.getLongitudeE6());
		setNick(jolt.getNick());
		setText(jolt.getText());
		setDeviceID(jolt.getDeviceId());
		setId(jolt.getId());
		setDate(jolt.getDate());
		setNumberRejolt(jolt.getNumberRejolt());
		setPhotoURL(jolt.getPhotoURL());
		setVideoURL(jolt.getVideoURL());
		setLoginType(jolt.getLoginType());
		setLoginUserid(jolt.getLoginUserid());
		setInstagramId(jolt.getInstagramId());
		setFacebookId(jolt.getFacebookId());
	}

	public boolean isVideoJolt() {
		if (getVideoURL() != null && !getVideoURL().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPhoJolt() {
		if (getPhotoURL() != null && !getPhotoURL().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isInstagram() {
		if (instagramId != null && !instagramId.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isFacebook() {
		if (facebookId != null && !facebookId.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setLifeTime(double lifeTime) {
		this.lifeTime = lifeTime;
	}

	public double getLifeTime() {
		return lifeTime;
	}

	public void setLongitudeE6(int longitude) {
		this.longitudeE6 = longitude;
	}

	public int getLongitudeE6() {
		return longitudeE6;
	}

	public void setLatitudeE6(int latitude) {
		this.latitudeE6 = latitude;
	}

	public int getLatitudeE6() {
		return latitudeE6;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public void setPhotoURL(String photoURL) {
		this.photoURL = photoURL;
	}

	public String getPhotoURL() {
		return photoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getDate() {
		return date;
	}

	public void setNumberRejolt(int numberRejolt) {
		this.numberRejolt = numberRejolt;
	}

	public int getNumberRejolt() {
		return numberRejolt;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getDistance() {
		return distance;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setDeviceID(String deviceID) {
		this.deviceId = deviceID;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setPhotoBitmapFromURL(String url) {
		if (url != null && !url.equalsIgnoreCase("")) {
			photoBitmap = new SoftReference<Bitmap>(joltHolder.getBitmap(url));
			joltHolder.bitmap = null;
		}
	}

	public void setPhotoBitmap(Bitmap photoBitmap) {
		this.photoBitmap = new SoftReference<Bitmap>(photoBitmap);
	}

	public Bitmap getPhotoBitmap() {
		if (photoBitmap != null) {
			return photoBitmap.get();
		} else {
			return null;
		}
	}

	public void setPositionArr(int positionArr) {
		this.positionArr = positionArr;
	}

	public int getPositionArr() {
		return positionArr;
	}

	public void setLoginType(LoginType loginType) {
		this.loginType = loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = LoginType.getLoginType(loginType);
	}

	public LoginType getLoginType() {
		return loginType;
	}

	public void setLoginUserid(String loginUserid) {
		this.loginUserid = loginUserid;
	}

	public String getLoginUserid() {
		return loginUserid;
	}

	public void setInstagramId(String instagramId) {
		this.instagramId = instagramId;
	}

	public String getInstagramId() {
		return instagramId;
	}

	public void setNumberFacebookRejolt(int numberFacebookRejolt) {
		this.numberFacebookRejolt = numberFacebookRejolt;
	}

	public void setNumberFacebookRejolt(ArrayList<String> friendsFacebook) {
		this.numberFacebookRejolt = getNumberFriend(friendsFacebook);
	}

	public int getNumberFacebookRejolt() {
		return numberFacebookRejolt;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setNewCreate(boolean newCreate) {
		this.newCreate = newCreate;
	}

	public boolean isNewCreate() {
		return newCreate;
	}

	public void addArrJoltFollow(String userId) {
		arrayRejoltId.add(userId);
	}

	public int getNumberFriend(ArrayList<String> friendsFacebook) {
		int count = 0;
		if (friendsFacebook == null) {
			return 0;
		} else {
			for (int i = 0; i < friendsFacebook.size(); i++) {
				for (int j = 0; j < arrayRejoltId.size(); j++) {
					if (friendsFacebook.get(i).equalsIgnoreCase(arrayRejoltId.get(j))) {
						count++;
						break;
					}
				}
			}
			return count;
		}
	}

	public long getDistanceToLocation(int lati, int longi) {

		// int dx = Math.abs(lati - latitude);
		// int dy = Math.abs(longi - longitude);
		//
		// return (long) Math.sqrt(dx * dx + dy * dy);
		Location locationA = new Location("point A");

		locationA.setLatitude(getLatitudeE6() / 1E6);
		locationA.setLongitude(getLongitudeE6() / 1E6);

		Location locationB = new Location("point B");

		locationB.setLatitude(lati / 1E6);
		locationB.setLongitude(longi / 1E6);

		float distance = locationA.distanceTo(locationB);

		return (long) (distance);
	}

	public boolean checkIsAlive() {
		long curTime = ConfigUtility.getCurTimeStamp();
		if (curTime - date <= (lifeTime * 3600)) {
			return true;
		}
		return false;
	}

	public boolean checkIsAliveEvent() {
		long curTime = ConfigUtility.getCurTimeStamp();
		if (curTime - date <= 86400) {
			return true;
		}
		return false;
	}

	public boolean isMyJolt() {
		if (isInstagram() || isFacebook()) {
			return false;
		} else {
			if ((UserSync.IdUjoolt.equals(getLoginUserid())
					|| UserSync.IdFacebook.equals(getLoginUserid()) || UserSync.IdTwitter
						.equals(getLoginUserid())) && getLoginUserid().length() > 2) {
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean isUjooltJolt() {
		if (isOnUjooltServer() && !isInstagram() && !isFacebook()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOnUjooltServer() {
		if (id != null && !id.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public void rejolt() {
		isRejolted = true;
		setNumberRejolt(getNumberRejolt() + 1);
		setRadius(getRadius() + 50);
		if (getLifeTime() <= 14.0) {
			setLifeTime(getLifeTime() + 1.0);
		}
	}

	public void showInfo() {
		Log.e("Jolt info:", "info lat: " + latitudeE6 + " long: " + longitudeE6);
		Log.e("Jolt info:", "info nick: " + nick + " text: " + text + "insta id" + instagramId
				+ ", id: " + id);
		Log.e("Jolt info:", "info login id:" + loginUserid);
		Log.e("Jolt info:", "info fb id:" + facebookId);
		Log.e("Jolt info:", "info media:" + videoURL + photoURL);
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public void setLike(ArrayList<String> arrListId) {
		if (arrListId == null || arrListId.size() == 0) {
			like = false;
		} else {
			for (int i = 0; i < arrListId.size(); i++) {
				if (id.equals(arrListId.get(i))) {
					like = true;
					return;
				}
			}
			like = false;
		}
	}

	public boolean isLike() {
		return like;
	}

	public boolean isTop() {
		return top;
	}

	public void setTop(boolean top) {
		this.top = top;
	}

	public boolean isPublicFacebook() {
		return publicFacebook;
	}

	public void setPublicFacebook(boolean publicFacebook) {
		this.publicFacebook = publicFacebook;
	}

	public boolean isFriendFacebook(ArrayList<String> arrListFriend) {
		if (arrListFriend.size() == 0 || loginUserid.equals("")) {
			return false;
		} else {
			for (int i = 0; i < arrListFriend.size(); i++) {
				if (loginUserid.equals(arrListFriend.get(i))) {
					return true;
				}
			}
			return false;
		}
	}

	public boolean isSocialJolt() {
		return (getNumberFacebookRejolt() > 0);
	}
}
