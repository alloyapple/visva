package vn.com.shoppie.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import vn.com.shoppie.activity.LoginActivity;
import vn.com.shoppie.activity.SettingPreference;
import vn.com.shoppie.database.smng.LinkMng;
import vn.com.shoppie.database.smng.UserMng;
import vn.com.shoppie.database.sobject.LinkPost;
import vn.com.shoppie.database.sobject.ShoppieObject;
import vn.com.shoppie.database.sobject.User;
import vn.com.shoppie.util.parse.WiModelManager;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

public class SUtilXml {
	private static SUtilXml _instance = new SUtilXml();

	public static SUtilXml getInstance() {
		return _instance;
	}

	public static interface OnLoadXmlListener {
		/**called in UI-Thread*/
		public void loadSuccess(String link, String xml);
		/**called in UI-Thread*/
		public void loadFailed(String link);
	}

	// 24*60*60*1000
	long thresoldGET = 86400000;
	long thresoldPOST = 86400000;

	/**
	 * new mechanism to load xml from network 1. load from db 2. return xml from
	 * db 3. load from network 4. if new data: update to db & callback
	 * OnLoadXmlListener
	 * */
	public String getXmlFromLink(Context context, String fullLink, ArrayList<NameValuePair> params, OnLoadXmlListener listener) throws ClientProtocolException, IOException {
		String link = encodeLink(fullLink, params);
		String xml = "";
		LinkMng linkMng = new LinkMng(context);

		ArrayList<LinkPost> array = linkMng.select(LinkMng.link, link);
		if (array != null && array.size() > 0) {
			LinkPost linkPost = array.get(0);
			xml = linkPost.xml;
		}
//		log.e("new mechanism", xml.substring(0, 100));
		updateToDb(context, fullLink, params, listener);
		return xml;
	}
	
	public String getXmlREST(Context context,String fullUrl,List<NameValuePair> params) {
		String url=encodeLink(fullUrl, params);
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(url);
		String text = "error";
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);

			HttpEntity entity = response.getEntity();

			text = getASCIIContentFromEntity(entity);
			return text;
		} catch (Exception e) {
			return "error: "+e.getMessage();
		}
	}

	@Deprecated
	public String getXmlFromLink(Context context, String link) throws ClientProtocolException, IOException {
		LinkMng linkMng = new LinkMng(context);
		ArrayList<LinkPost> array = linkMng.select(LinkMng.link, link);
		if (array != null && array.size() > 0) {
			LinkPost linkPost = array.get(0);
//			long crtTime = System.currentTimeMillis();
//			long linkTime = linkPost.time;
//			if ((crtTime - linkTime) <= thresoldGET) {
//				return linkPost.xml;
//			}
			linkMng.delete(linkPost._id);
		}
		String xml = getXmlFromUrl(link);
		if (xml != null && xml.length() > 0) {
			LinkPost newLinkPost = new LinkPost(-1, link, xml, System.currentTimeMillis() + "");
			linkMng.insertNewTo(newLinkPost.getValues());
		}
		return xml;
	}

	private void updateToDb(final Context context, final String fullLink, final ArrayList<NameValuePair> valuePair, final OnLoadXmlListener listener) {
		final String link = encodeLink(fullLink, valuePair);
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				try {
					String xml="";
					if(valuePair!=null){
						xml = sendPost(fullLink, valuePair);
					}else{
						xml=getXmlFromUrl(fullLink);
					}
						
					if (xml.equals(null))
						return null;
					if (xml.contains("error"))
						return null;
					LinkMng linkMng = new LinkMng(context);
					ArrayList<LinkPost> array = linkMng.select(LinkMng.link, link);
					long crtTime = System.currentTimeMillis();
					LinkPost newLink = new LinkPost(0, link, xml, crtTime + "");
					if (array != null && array.size() > 0) {
						LinkPost linkPost = array.get(0);
						if (!linkPost.xml.equals(newLink.xml))
							linkMng.edit(linkPost._id, newLink.getValues());
					} else {
						linkMng.insertNewTo(newLink.getValues());
					}
					return xml;
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				} catch (SQLException e) {

				}
				return null;
			}

			protected void onPostExecute(String result) {
				if (listener != null) {
					if (result == null)
						listener.loadFailed(link);
					else
						listener.loadSuccess(link, result);
				}
			}
		}.execute();
	}

	public boolean sendFeedBack(Context context, String message) {
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
		data.add(new BasicNameValuePair("custId", SettingPreference.getUserID(context) + ""));
		data.add(new BasicNameValuePair("message", message));
		try {
			String xml = sendPost(WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_FEEDBACK, data);
			if (xml.contains("result")) {
				return true;
			} else {
				return false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public String postXml(Context context, String link, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IllegalArgumentException, IOException {
		return postXml(context, link, nameValuePairs, thresoldPOST);
	}

	public String encodeLink(String link, List<NameValuePair> nameValuePairs) {
		String encodeLink = link + "?";
		if (nameValuePairs == null || nameValuePairs.isEmpty())
			return link;
		for (NameValuePair item : nameValuePairs) {
			encodeLink += item.getName() + "=" + item.getValue() + "&";
		}
		
		encodeLink=encodeLink.substring(0, encodeLink.length()-1);
		return encodeLink;
	}

	public String postXml(Context context, String link, List<NameValuePair> nameValuePairs, long threasoldPOST) throws ClientProtocolException, IllegalArgumentException, IOException {
		String encodeLink = encodeLink(link, nameValuePairs);
		LinkMng linkMng = new LinkMng(context);
		ArrayList<LinkPost> array = linkMng.select(LinkMng.link, encodeLink);
		if (array != null && array.size() > 0) {
			LinkPost linkPost = array.get(0);
			long crtTime = System.currentTimeMillis();
			long linkTime = linkPost.time;
			if ((crtTime - linkTime) <= threasoldPOST) {
				return linkPost.xml;
			}
			linkMng.delete(linkPost._id);
		}
		String xml = sendPost(link, nameValuePairs);
		if (xml != null && xml.length() > 0 && !(xml.indexOf("error")>=0)) {
			LinkPost newLinkPost = new LinkPost(-1, encodeLink, xml, System.currentTimeMillis() + "");
			linkMng.insertNewTo(newLinkPost.getValues());
		}
		return xml;
	}

	public String postXml(Context context, String link, List<NameValuePair> nameValuePairs, long threasoldPOST, boolean forcePost) throws ClientProtocolException, IllegalArgumentException, IOException {
		String encodeLink = encodeLink(link, nameValuePairs);
		LinkMng linkMng = new LinkMng(context);
		if (!forcePost) {
			ArrayList<LinkPost> array = linkMng.select(LinkMng.link, encodeLink);
			if (array != null && array.size() > 0) {
				LinkPost linkPost = array.get(0);
				long crtTime = System.currentTimeMillis();
				long linkTime = linkPost.time;
				if ((crtTime - linkTime) <= threasoldPOST) {
					return linkPost.xml;
				}
				linkMng.delete(linkPost._id);
			}
		}

		String xml = sendPost(link, nameValuePairs);
		if (xml != null && xml.length() > 0) {
			LinkPost newLinkPost = new LinkPost(-1, encodeLink, xml, System.currentTimeMillis() + "");
			linkMng.insertNewTo(newLinkPost.getValues());
		}
		return xml;
	}

	public String sendPost(String url, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException, IllegalArgumentException {
		HttpClient httpclient = new DefaultHttpClient();
		// Your URL String
		// url="http://ws.shoppie.com.vn/index.php/api/webservice/autoregister";
		HttpPost httppost = new HttpPost(url);

		// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("deviceToken",
		// util.getDeviceId(BluetoothExample.this)));
		// nameValuePairs.add(new BasicNameValuePair("deviceType", "Android"));
		// nameValuePairs.add(new BasicNameValuePair("custCode", address));

		if (nameValuePairs != null)
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response;
		try{
		response = httpclient.execute(httppost);
		}catch(OutOfMemoryError e){
			return null;
		}
		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			String xml;
			// xml= EntityUtils.toString(responseEntity);
			xml = EntityUtils.toString(responseEntity, "UTF-8");
//			 log.m(xml);
//			 log.e("response post", "xml: "+xml+"");
			return xml;
		}
		return null;
	}

	private String getXmlFromUrl(String url) throws ClientProtocolException, IOException {
		// String url =
		// "http://ws.shoppie.com.vn/index.php/api/webservice/merchcampaigns";
		// url merchant:
		// http://ws.shoppie.com.vn/index.php/api/webservice/merchants
		XMLParser pa = new XMLParser();
		try {
			String value = pa.getXmlFromUrl(url);
			if (!value.contains("error")) {
				// log.m("XMLParse: "+value);
				return value;
			}
		} catch (NullPointerException e) {
			//log.e("SUtilXML line 267", "null: " + url);
		}
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);

		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromHttpResponse(response);
		// log.m("XMLParse from HTML response:"+xml);
		return xml;
	}

	public int registerAccount(Context context) {
		SUtil util = SUtil.getInstance();
		util.configBluetooth(true);
		String address = "";
		try {
			address = BluetoothAdapter.getDefaultAdapter().getAddress();
		} catch (NullPointerException e) {
			return -1;
		}
		util.configBluetooth(false);
		if (address == null) {
			log.e("address", "null");
			return -1;
		}
		return registerAccount(context, util.getDeviceId(context), address);
	}
	public String getCharTag(String xml,String startTag,String endTag){
		int startIndex = xml.indexOf(startTag);
		int endIndex = xml.indexOf(endTag);
		if(startIndex<0 || endIndex<0) return "";
		String result = xml.substring(startIndex + startTag.length(), endIndex);
		return result;
	}
	
	/**
	 * @warning: must be run in non-UI thread.
	 * */
	public boolean updateInfo(Context context, String custId, String custName, String custEmail, String custPhone, String gender,String address,String birthday) {
		// send POST
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_UPDATE_INFO_USER;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		nameValuePairs.add(new BasicNameValuePair("custName", custName));
		nameValuePairs.add(new BasicNameValuePair("custEmail", custEmail));
		nameValuePairs.add(new BasicNameValuePair("custPhone", custPhone));
		nameValuePairs.add(new BasicNameValuePair("gender", gender));
		nameValuePairs.add(new BasicNameValuePair("address", address));
		nameValuePairs.add(new BasicNameValuePair("birthday", birthday));
		String xmlResult = "";
		try {
			String xml = SUtilXml.getInstance().postXml(context, url, nameValuePairs, 200);
			String tmp = "<dataValue>";
			int startIndex = xml.indexOf(tmp);
			int endIndex = xml.indexOf("</dataValue>");
			xmlResult = xml.substring(startIndex + tmp.length(), endIndex);
			int result = Integer.parseInt(xmlResult);
			if (result > 0) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			log.e("SUtilXML:line 187: parse result error", xmlResult);
			return false;
		} catch (SQLException e) {
			log.e("SUtilXML:line 190: SQL exception", xmlResult);
			return false;
		} catch (ClientProtocolException e) {
			log.e("SUtilXML:line 193: ClientProtocolException", xmlResult);
			return false;
		} catch (IllegalArgumentException e) {
			log.e("SUtilXML:line 196: IllegalArgumentException", xmlResult);
			return false;
		} catch (IOException e) {
			log.e("SUtilXML:line 199: IOException", xmlResult);
			return false;
		}
	}

	/**
	 * @warning: must be run in non-UI thread.
	 * */
	public String registerAccount(Context context, String deviceToken, String bluetoothId, String deviceId, String latitude, String longitude, String custName) {
		String userID = "-1";
		// send POST
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_REGISTER;
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("deviceToken", deviceToken));
		nameValuePairs.add(new BasicNameValuePair("deviceType", "Android"));
		nameValuePairs.add(new BasicNameValuePair("custCode", bluetoothId));
		nameValuePairs.add(new BasicNameValuePair("deviceId", deviceId));
		nameValuePairs.add(new BasicNameValuePair("latitude", latitude));
		nameValuePairs.add(new BasicNameValuePair("longtitude", longitude));
		nameValuePairs.add(new BasicNameValuePair("custName", custName));

		try {
			String xml;
			xml = SUtilXml.getInstance().postXml(context, url, nameValuePairs, 200);
			// xml = sendPost(url, nameValuePairs);
			String tmp = "<dataValue>";
			if (!xml.contains(tmp)) {
				return LoginActivity.ERR_SERVER;
			}

			int startIndex = xml.indexOf(tmp);
			int endIndex = xml.indexOf("</dataValue>");
			log.e("xml", xml);
			userID = xml.substring(startIndex + tmp.length(), endIndex);
			// UserMng userMng = new UserMng(context);
			// userMng.delete(0);
			// userMng.insertTo(0, userID + "");
			return userID;

		} catch (ClientProtocolException e) {
			log.m(e.toString());
			return LoginActivity.ERR_SERVER;
		} catch (IOException e) {
			log.m(e.toString());
			return LoginActivity.ERR_SERVER;
		} catch (SQLException e) {
			log.e("SUtilXML:line 191: SQL exception", userID);
			return LoginActivity.ERR_DATABASE_ERR;
		}
	}

	/**
	 * @warning: must be run in non-UI thread.
	 * */
	public int registerAccount(Context context, String deviceId, String bluetoothId) {
		int userID = -1;
		// send POST
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_REGISTER;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("deviceToken", /*
																 * util.getDeviceId
																 * (context)
																 */deviceId));
		nameValuePairs.add(new BasicNameValuePair("deviceType", "Android"));
		nameValuePairs.add(new BasicNameValuePair("custCode", bluetoothId));
		try {
			String xml = SUtilXml.getInstance().postXml(context, url, nameValuePairs, 200);
			WiModelManager mng = new WiModelManager();
			ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
			mng.parse(xml);
			data = mng.getResultShoppieObject();
			// log.m(data.size() + "");
			// for (ShoppieObject user : data) {
			// log.m(((User) user).id);
			// }

			try {
				User user = new User(-1, ((User) data.get(0)).custId, "");
				try {
					userID = Integer.valueOf(((User) data.get(0)).custId);
				} catch (NumberFormatException e) {
					return -1;
				}
				// SettingPreference.setUserID(context, Integer.valueOf(((User)
				// data.get(0)).custId));
//				log.m("User ID:" + ((User) data.get(0)).custId);
				new UserMng(context).insertNewTo(user.getValues());
				return userID;
			} catch (SQLException e) {
				log.m(e.toString());
				return userID;
			}
		} catch (ClientProtocolException e) {
			log.m(e.toString());
			return userID;
		} catch (IOException e) {
			log.m(e.toString());
			return userID;
		}
	}
	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();

		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);

			if (n > 0)
				out.append(new String(b, 0, n));
		}

		return out.toString();
	}
}
