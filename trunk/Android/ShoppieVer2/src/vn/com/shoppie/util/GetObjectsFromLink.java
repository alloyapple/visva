package vn.com.shoppie.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import vn.com.shoppie.activity.SettingPreference;
import vn.com.shoppie.database.smng.LikeMng;
import vn.com.shoppie.database.smng.MerchantMng;
import vn.com.shoppie.database.sobject.Gift;
import vn.com.shoppie.database.sobject.GiftRedeem;
import vn.com.shoppie.database.sobject.Like;
import vn.com.shoppie.database.sobject.Merchant;
import vn.com.shoppie.database.sobject.Notification;
import vn.com.shoppie.database.sobject.Product;
import vn.com.shoppie.database.sobject.ShoppieObject;
import vn.com.shoppie.util.parse.WiModelManager;
import vn.com.shoppie.webconfig.WebServiceConfig;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

public class GetObjectsFromLink {
	private static GetObjectsFromLink _instance = new GetObjectsFromLink();

	public static GetObjectsFromLink getInstance() {
		return _instance;
	}

	public void loadMerchant(final Context context) {
		loadMerchant(context, null);
	}

	public GiftRedeem giftRedeem(Context context, String merchId, String storeId, String custId, String giftId, String redeemQty,String giftImage) {
		String[] values = new String[GiftRedeem.NUM_FIELDS];
		GiftRedeem giftRedeem = new GiftRedeem(-1, values);

		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_GIFT_REDEEM;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("merchId", merchId));
		nameValuePairs.add(new BasicNameValuePair("storeId", storeId));
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		nameValuePairs.add(new BasicNameValuePair("giftId", giftId));
		nameValuePairs.add(new BasicNameValuePair("redeemQty", redeemQty));
		try {
			String xml = SUtilXml.getInstance().sendPost(url, nameValuePairs);
			if (xml == null || xml.equals("") || xml.contains("error"))
				return null;
			Log.e("GetObjectsFromLink giftReddeem line 54", "giftredeem: "+xml+"");
			giftRedeem.txnId = SUtilXml.getInstance().getCharTag(xml, "<txnId>", "</txnId>");
			giftRedeem.merchId=merchId;
			giftRedeem.merchName = SUtilXml.getInstance().getCharTag(xml, "<merchName>", "</merchName>");
			giftRedeem.storeAddress = SUtilXml.getInstance().getCharTag(xml, "<storeAddress>", "</storeAddress>");
			giftRedeem.giftName = SUtilXml.getInstance().getCharTag(xml, "<giftName>", "</giftName>");
			giftRedeem.pieQty = SUtilXml.getInstance().getCharTag(xml, "<pieQty>", "</pieQty>");
			giftRedeem.time=System.currentTimeMillis()+"";
			giftRedeem.giftImage=giftImage;
			
			return giftRedeem;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<ShoppieObject> getHistoryTnx(final Context context, String custId, final SUtilXml.OnLoadXmlListener listener) {
		// LOAD HISTORYTNX
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_GET_CUST_HISTORY_TXNS;
		WiModelManager mng = new WiModelManager();
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("custId", custId));
			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, params, listener);
			mng.parse(xml);
			data = mng.getResultShoppieObject();
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		}
		return data;
	}

	public ArrayList<ShoppieObject> getMerchImage(final Context context, String merchId, final SUtilXml.OnLoadXmlListener listener) {
		// LOAD PROMOTION
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_MERCH_IMAGES;
		WiModelManager mng = new WiModelManager();
		try {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("merchId", merchId));
			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, params, listener);
			mng.parse(xml);
			data = mng.getResultShoppieObject();
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		}
		return data;
	}

	public ArrayList<ShoppieObject> getPromotion(final Context context, final SUtilXml.OnLoadXmlListener listener) {
		// LOAD PROMOTION
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_PROMOTIONS;
		WiModelManager mng = new WiModelManager();
		try {
			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, null, listener);
			mng.parse(xml);
			data = mng.getResultShoppieObject();
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		}
		return data;
	}

	/**
	 * run on UI-Thread
	 * */
	public void loadMerchant(final Context context, final SUtilXml.OnLoadXmlListener listener) {
		// LOAD MERCHANTS
		new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... arg0) {

				String merchants = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_MERCHANTS;
				Log.e("Load merchants: " ,""+ merchants);
				try {
					String xml = SUtilXml.getInstance().getXmlFromLink(context, merchants, null, listener);
					WiModelManager mng = new WiModelManager();
					// System.out.println("do in background LoadMerchant response:"+xml);
					if (xml.contains("error")) {
						Log.e("Load merchant","Error getxml from merchant");
						return false;
					}
					mng.parse(xml);
					ArrayList<ShoppieObject> dataMerchants = mng.getResultShoppieObject();
					if (dataMerchants == null || dataMerchants.isEmpty())
						return false;

					MerchantMng mcMng = new MerchantMng(context);

					ArrayList<Merchant> mcData = mcMng.select();
					ArrayList<Integer> arrayId = new ArrayList<Integer>();

					for (ShoppieObject item : dataMerchants) {
						for (Merchant mcItem : mcData) {
							if (((Merchant) item).merchId.equals(mcItem.merchId)) {
								if(arrayId.indexOf(dataMerchants.indexOf(item))>=0) continue;
								arrayId.add(dataMerchants.indexOf(item));
								mcMng.edit(mcItem._id, item.getValues());
							}
						}
					}
					for (int i = arrayId.size() - 1; i >= 0; i--) {
						dataMerchants.remove(i);
					}
					int i = 0;
					for (ShoppieObject item : dataMerchants) {
						Merchant merchantItem = (Merchant) item;
						merchantItem.getValues();
						if (merchantItem.merchId == null)
							continue;
						item.getBitmap(context, ((Merchant) item).merchImage);

						mcMng.insertNewTo(merchantItem.getValues());
						i++;
					}
					Log.e("GetObjFrmLink load Merchant line: 202","insert Merchant:" + i);
				} catch (ClientProtocolException e) {
					if (SettingPreference.PRINT_STACK_TRACE)
						Log.e("Load Merchant Exception", e.toString());
				} catch (IOException e) {
					if (SettingPreference.PRINT_STACK_TRACE)
						Log.e("Load Merchant Exception", e.toString());
				} catch (NullPointerException e) {
					if (SettingPreference.PRINT_STACK_TRACE)
						Log.e("Load Merchant Exception", e.toString());
				}catch (IndexOutOfBoundsException e) {
					if (SettingPreference.PRINT_STACK_TRACE)
						Log.e("Load Merchant Exception", e.toString());
				}

				return false;
			}
		}.execute();
	}

	public ArrayList<ShoppieObject> getMerchantCategories(Context context) {
		return getMerchantCategories(context, null);
	}

	// Must be run on non-UIthread
	public ArrayList<ShoppieObject> getMerchantCategories(Context context, SUtilXml.OnLoadXmlListener listener) {
		// MERCHAMPAIGNS
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_MERCHANTS_CATEGORY;
		WiModelManager mng = new WiModelManager();
		try {
			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, null, listener);
			mng.parse(xml);
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		}
		data = mng.getResultShoppieObject();
		return data;
	}

	public ArrayList<ShoppieObject> getCampaigns(Context context) {
		return getCampaigns(context, null);
	}

	// Must be run on non-UIthread
	public ArrayList<ShoppieObject> getCampaigns(Context context, SUtilXml.OnLoadXmlListener listener) {

		// MERCHAMPAIGNS
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_MERCHCAMPAIGNS;
		WiModelManager mng = new WiModelManager();
		try {
			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, null, listener);
			mng.parse(xml);
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
			System.out.println(e.toString());
		}
		data = mng.getResultShoppieObject();
		return data;
	}

	public ArrayList<ShoppieObject> getGiftSlider(Context context) {
		return getGiftSlider(context, null);
	}

	// get SLIDER GIFT
	public ArrayList<ShoppieObject> getGiftSlider(Context context, SUtilXml.OnLoadXmlListener listener) {
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();

		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_GIFT_SLIDER;
		try {
			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, null, listener);
			WiModelManager mng = new WiModelManager();
			mng.parse(xml);
			data = mng.getResultShoppieObject();

		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}
		return data;
	}

	public ArrayList<ShoppieObject> getGifts(Context context) {
		return getGifts(context, null);
	}

	// get GIFT
	public ArrayList<ShoppieObject> getGifts(Context context, SUtilXml.OnLoadXmlListener listener) {
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_GIFTS;
		WiModelManager mng = new WiModelManager();
		try {

			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, null, listener);
			mng.parse(xml);
			data = mng.getResultShoppieObject();
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public ArrayList<ShoppieObject> getNotification(Context context, SUtilXml.OnLoadXmlListener listener) {
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_NOTIFICATION;
		WiModelManager mng = new WiModelManager();
		try {

			String xml = SUtilXml.getInstance().getXmlFromLink(context, url, null, listener);
			// Log.e("notifications", xml);

			mng.parse(xml);

			data = mng.getResultShoppieObject();
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				e.printStackTrace();
			}
		}
		return data;
	}

	public ArrayList<Notification> getNotification(Context context) {
		ArrayList<Notification> data = new ArrayList<Notification>();
		ArrayList<ShoppieObject> result = loadData(context, WebServiceConfig.URL_NOTIFICATION, null);
		for (ShoppieObject obj : result) {
			if (obj instanceof Notification) {
				data.add((Notification) obj);
			}
		}
		Log.e("add Notification", data.size() + " items");
		return data;
	}

	public String likeProductGift(Context context, String custId, ShoppieObject obj, final SUtilXml.OnLoadXmlListener listener) {
		int intResult = 0;
		String result = "";
		String type = "";
		// send POST
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_LIKE_PRODUCT;
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("custId", custId));
		if (obj instanceof Product) {
			Product product = (Product) obj;
			nameValuePairs.add(new BasicNameValuePair("productId", product.productId));
			type = "product";
		} else if (obj instanceof Gift) {
			Gift gift = (Gift) obj;
			nameValuePairs.add(new BasicNameValuePair("giftId", gift.giftId));
			type = "gift";
		}
		String xml = "";
		String xmlResult = "";
		try {
			xml = SUtilXml.getInstance().postXml(context, url, nameValuePairs);
			result = SUtilXml.getInstance().getCharTag(xml, "<dataValue>", "</dataValue>");
			intResult = Integer.parseInt(result);
			if (listener != null)
				listener.loadSuccess(url, xml);
			// add to database
			LikeMng likeMng = new LikeMng(context);
			ArrayList<Like> array = likeMng.select(LikeMng.type, type);
			if (!array.isEmpty()) {
				for (Like _like : array) {
					if (type.equals("product")) {
						if (_like.merchId.equals(((Product) obj).merchId) && _like.id.equals(((Product) obj).productId)) {
							_like.qty = intResult + "";
							_like.time = System.currentTimeMillis() + "";
							_like.type = type;
							_like.id = ((Product) obj).productId;
							_like.name=((Product) obj).productName;

							int numberUpdated = likeMng.edit(_like._id, _like.getValues());
							Log.e("update Like to db:", numberUpdated + " items");
							break;
						}
					} else if (type.equals("gift")) {
						if (_like.merchId.equals(((Gift) obj).merchId) && _like.id.equals(((Gift) obj).giftId)) {
							_like.qty = intResult + "";
							_like.time = System.currentTimeMillis() + "";
							_like.type = type;
							_like.id = ((Gift) obj).giftId;
							_like.name=((Gift) obj).giftName;

							int numberUpdated = likeMng.edit(_like._id, _like.getValues());
							Log.e("update Like to db:", numberUpdated + " items");
							break;
						}
					}
				}
			} else {
				Like like = new Like(-1, new String[Like.NUM_FIELDS]);
				if (type.equals("product")) {
					Product product = (Product) obj;
					like.merchId = product.merchId;
					like.type = type;
					like.id = product.productId;
					like.time = System.currentTimeMillis() + "";
					like.qty = intResult + "";
					like.name=product.productName;
				} else if (type.equals("gift")) {
					Gift gift = (Gift) obj;
					like.merchId = gift.merchId;
					like.type = type;
					like.id = gift.giftId;
					like.time = System.currentTimeMillis() + "";
					like.qty = intResult + "";
					like.name=gift.giftName;
				}
				long rowId = likeMng.insertNewTo(like.getValues());
				Log.e("insert Like to db:", rowId + " id");
			}
			return result;
		} catch (NumberFormatException e) {
			if (listener != null)
				listener.loadFailed(url);
			Log.e("SUtilXML:line 369: parse result error", xmlResult);
			return xml;
		} catch (SQLException e) {
			if (listener != null)
				listener.loadFailed(url);
			Log.e("SUtilXML:line 372: SQL exception", xmlResult);
			return xml;
		} catch (ClientProtocolException e) {
			if (listener != null)
				listener.loadFailed(url);
			Log.e("SUtilXML:line 375: ClientProtocolException", xmlResult);
			return xml;
		} catch (IllegalArgumentException e) {
			if (listener != null)
				listener.loadFailed(url);
			Log.e("SUtilXML:line 378: IllegalArgumentException", xmlResult);
			return xml;
		} catch (IOException e) {
			if (listener != null)
				listener.loadFailed(url);
			Log.e("SUtilXML:line 381: IOException", xmlResult);
			return xml;
		}
	}
	public String likeProductGift(Context context,Like like){
		LikeMng likeMng=new LikeMng(context);
		ArrayList<Like> arrayLike=likeMng.select(LikeMng.merchId, like.merchId);
		boolean liked=false;
		if(arrayLike!=null && !arrayLike.isEmpty())
		for(Like _like: arrayLike){
			if(_like.equal(like)){
				liked=true;
				break;
			}
		}
		if(!liked){
			Log.e("Like SQL", "insert new like product/gift");
			likeMng.insertNewTo(like.getValues());
		}
		// send POST
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_LIKE_PRODUCT;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("custId", SettingPreference.getUserID(context)+""));
		nameValuePairs.add(new BasicNameValuePair("giftId", like.id));
		nameValuePairs.add(new BasicNameValuePair("productId", like.id));
		String xmlResult = "";
		String xml = "";
		try {
			xml = SUtilXml.getInstance().postXml(context, url, nameValuePairs, 200,true);
			String tmp = "<dataValue>";
			int startIndex = xml.indexOf(tmp);
			int endIndex = xml.indexOf("</dataValue>");
			xmlResult = xml.substring(startIndex + tmp.length(), endIndex);
			String result = xmlResult;
			return result;
		} catch (NumberFormatException e) {
			Log.e("SUtilXML:line 183: parse result error", xmlResult);
			return xml;
		} catch (SQLException e) {
			Log.e("SUtilXML:line 186: SQL exception", xmlResult);
			return xml;
		} catch (ClientProtocolException e) {
			Log.e("SUtilXML:line 189: ClientProtocolException", xmlResult);
			return xml;
		} catch (IllegalArgumentException e) {
			Log.e("SUtilXML:line 192: IllegalArgumentException", xmlResult);
			return xml;
		} catch (IOException e) {
			Log.e("SUtilXML:line 195: IOException", xmlResult);
			return xml;
		} catch(IndexOutOfBoundsException e){
			Log.e("SUtilXML:line 496: IndexOutOfBoundsException", xmlResult);
			return xml;
		}
	}
	/**
	 * @warning: must be run in non-UI thread.
	 * */
	public String likeBrand(Context context, Like like,boolean hasLiked) {
		LikeMng likeMng=new LikeMng(context);
		ArrayList<Like> arrayLike=likeMng.select(LikeMng.merchId, like.merchId);
		boolean liked=false;
		if(arrayLike!=null && !arrayLike.isEmpty())
		for(Like _like: arrayLike){
			if(_like.equal(like)){
				liked=true;
				break;
			}
		}
		if(!liked && !hasLiked){
			Log.e("Like SQL", "insert new like brand");
			likeMng.insertNewTo(like.getValues());
		}
//		if(!like.type.equals(Like.TYPE_LIKE_BRAND)) return "";
		// send POST
		String url = WebServiceConfig.URL_SHOPPIE_HOME + WebServiceConfig.URL_LIKE_BRAND;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("custId", SettingPreference.getUserID(context)+""));
		nameValuePairs.add(new BasicNameValuePair("merchId", like.merchId));
		String xmlResult = "";
		String xml = "";
		try {
			xml = SUtilXml.getInstance().postXml(context, url, nameValuePairs, 200,true);
			String tmp = "<dataValue>";
			int startIndex = xml.indexOf(tmp);
			int endIndex = xml.indexOf("</dataValue>");
			xmlResult = xml.substring(startIndex + tmp.length(), endIndex);
			String result = xmlResult;
			return result;
		} catch (NumberFormatException e) {
			Log.e("SUtilXML:line 183: parse result error", xmlResult);
			return xml;
		} catch (SQLException e) {
			Log.e("SUtilXML:line 186: SQL exception", xmlResult);
			return xml;
		} catch (ClientProtocolException e) {
			Log.e("SUtilXML:line 189: ClientProtocolException", xmlResult);
			return xml;
		} catch (IllegalArgumentException e) {
			Log.e("SUtilXML:line 192: IllegalArgumentException", xmlResult);
			return xml;
		} catch (IOException e) {
			Log.e("SUtilXML:line 195: IOException", xmlResult);
			return xml;
		} catch(IndexOutOfBoundsException e){
			Log.e("SUtilXML:line 496: IndexOutOfBoundsException", xmlResult);
			return xml;
		}
	}

	/** new mechanism */
	public ArrayList<ShoppieObject> loadData(Context context, String subLink, ArrayList<NameValuePair> params, SUtilXml.OnLoadXmlListener listener) {
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		WiModelManager mng = new WiModelManager();
		try {

			String xml = SUtilXml.getInstance().getXmlFromLink(context, WebServiceConfig.URL_SHOPPIE_HOME + subLink, params, listener);
			mng.parse(xml);
			data = mng.getResultShoppieObject();
		} catch (ClientProtocolException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				Log.e("ERR: GetObjectFromLink line 584", e.getMessage());
			}
		} catch (IOException e) {
			if (SettingPreference.PRINT_STACK_TRACE) {
				Log.e("ERR: GetObjectFromLink line 588", e.getMessage());
			}
		}
		return data;
	}

	/**
	 * @param typeLoad
	 *            : "/postMethod"
	 * @param params
	 *            : BasicNameValuePair
	 * */
	public ArrayList<ShoppieObject> loadData(Context context, String typeLoad, ArrayList<NameValuePair> params) {
		ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();
		// load product
		try {
			// ArrayList<NameValuePair> dataToSend = new
			// ArrayList<NameValuePair>();
			// dataToSend.add(new BasicNameValuePair(MERCH_ID, merchId));
			String xml = SUtilXml.getInstance().postXml(context, WebServiceConfig.URL_SHOPPIE_HOME + typeLoad, params);
			// Log.e("xml",xml);
			if (xml.equals(null)) {
			}
			WiModelManager mng = new WiModelManager();
			mng.parse(xml);
			ArrayList<ShoppieObject> result = mng.getResultShoppieObject();
			Log.e("size " + typeLoad + ":", result.size() + "");
			for (ShoppieObject object : result) {
				data.add(object);
			}
			return data;
		} catch (ClientProtocolException e) {
			Log.e("ERR: GetObjectFromLink line 620", e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.e("ERR: GetObjectFromLink line 622", e.getMessage());
		} catch (IOException e) {
			Log.e("ERR: GetObjectFromLink line 624", e.getMessage());
		}
		return data;
	}
}
