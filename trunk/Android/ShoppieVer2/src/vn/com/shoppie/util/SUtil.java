package vn.com.shoppie.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import vn.com.shoppie.activity.SettingPreference;
import vn.com.shoppie.network.NetworkUtility;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class SUtil {
	private static SUtil _instance = new SUtil();

	public static SUtil getInstance() {
		return _instance;
	}
	
	public String getTimeFormat(long time){
		if(time==-1) return "";
		Date date=new Date(time);
		DateFormat formatter=new SimpleDateFormat("HH:mm - dd/MM/yyyy");
		return formatter.format(date);
	}
	
	public void setFontDigital(Context ctx,TextView tv){
		tv.setTypeface(getDigital(ctx));
	}
	public void setFontRoboto(Context ctx,TextView tv){
		tv.setTypeface(getRoboto(ctx));
	}
	public Typeface getDigital(Context ctx){
		return getFont(ctx, "digifaw.ttf");
	}
	
	public Typeface getRoboto(Context ctx){
		return getFont(ctx, "roboto.ttf");
	}
	/**
	 * Get Font from Assets.
	 * TextView.setTypeface(digital);
	 * @param: pathFont: font file in ./assets/font/
	 * */
	public Typeface getFont(Context ctx, String pathFont) {
		Typeface myTypeface = Typeface.createFromAsset(ctx.getAssets(), "font/" + pathFont);
		return myTypeface;
	}

	public String getDeviceId(Context ctx) throws NullPointerException {
		TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		String id = telephonyManager.getDeviceId();
		if (id == null)
			return "";
		else
			return id;
	}

	public boolean configBluetooth(boolean turnOn) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null)
			return false;
//		mBluetoothAdapter.setDiscoverableTimeout(100);
		if (turnOn) {
			boolean result = mBluetoothAdapter.enable();
			mBluetoothAdapter.startDiscovery();
			
			return result;

		} else {
			return mBluetoothAdapter.disable();
		}
	}
	public Set<BluetoothDevice> getListDevices() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> bondedSet = mBluetoothAdapter.getBondedDevices();
		return bondedSet;
	}

	public String getBluetoothAddress(Context context,boolean preferenceFirst) {
		String result="";
		result=SettingPreference.getUserDeviceToken(context);
		if(preferenceFirst && result!=null && result.length()>5) return result;
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter==null){
			return "";
		}
		result=mBluetoothAdapter.getAddress();
		if(result==null) return "";
		SettingPreference.setUserDeviceToken(context, result);
		return result;
	}

	public String getBluetoothName() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		return mBluetoothAdapter.getName();
	}

	// XML and BITMAP CACHE BACKUP
	@SuppressWarnings("unused")
	private String sendPost(String url, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException, IllegalArgumentException {
		HttpClient httpclient = new DefaultHttpClient();
		// Your URL String
		// url="http://ws.shoppie.com.vn/index.php/api/webservice/autoregister";
		HttpPost httppost = new HttpPost(url);

		// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("deviceToken",
		// util.getDeviceId(BluetoothExample.this)));
		// nameValuePairs.add(new BasicNameValuePair("deviceType", "Android"));
		// nameValuePairs.add(new BasicNameValuePair("custCode", address));

		// if(nameValuePairs.size()>0)
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response;
		response = httpclient.execute(httppost);
		HttpEntity responseEntity = response.getEntity();
		if (responseEntity != null) {
			String xml;
			// xml= EntityUtils.toString(responseEntity);
			xml = EntityUtils.toString(responseEntity, "UTF-8");
			// System.out.println(xml);
			return xml;
		}
		return null;
	}

	@SuppressWarnings("unused")
	private String getXmlFromUrl(String url) throws ClientProtocolException, IOException {
		// String url =
		// "http://ws.shoppie.com.vn/index.php/api/webservice/merchcampaigns";

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = httpClient.execute(httpGet);
		int resCode = response.getStatusLine().getStatusCode();

		// Toast.makeText(getApplicationContext(),response.getStatusLine().getStatusCode()+"",
		// Toast.LENGTH_LONG).show();

		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromHttpResponse(response);

		return xml;
	}

	public boolean isNetworkConnected(Context appContext) {
		// Check if Internet present
		if (!NetworkUtility.getInstance(appContext).isNetworkAvailable()) {
			// stop executing code by return
			return false;
		}
		return true;
	}

	private HashMap<String, Bitmap> dataBitmap = new HashMap<String, Bitmap>();

	@SuppressWarnings("unused")
	private Bitmap getImageSync(Context context, String link) {
		Bitmap bmp = null;
		if (dataBitmap.containsKey(link)) {
			bmp = dataBitmap.get(link);
			if (bmp != null)
				return dataBitmap.get(link);
			dataBitmap.remove(link);
		}
		bmp = downloadImage(context, link);
		if (bmp != null)
			dataBitmap.put(link, bmp);
		return bmp;
	}

	@SuppressWarnings("unused")
	private Bitmap getImageAsync(final Context context, final String link) {
		if (dataBitmap.containsKey(link)) {
			Bitmap bmp = dataBitmap.get(link);
			if (bmp != null)
				return dataBitmap.get(link);
			dataBitmap.remove(link);
		}
		new Thread() {
			public void run() {
				Bitmap bmp = downloadImage(context, link);
				if (bmp != null)
					dataBitmap.put(link, bmp);
			};
		}.start();
		return null;
	}

	private Bitmap scaleBitmap(File f) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(f);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2; // try to decrease decoded image
		options.inPurgeable = true; // if necessary purge pixels into
									// disk
		options.inScaled = true; // scale down image to actual device
									// density
		return BitmapFactory.decodeStream(fis, null, options);
	}

	@SuppressWarnings("unused")
	private Bitmap scaleBitmap(String path) throws FileNotFoundException {
		File f = new File(path);
		return scaleBitmap(f);
	}

	private Bitmap downloadImage(Context context, String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setUseCaches(true);
			conn.connect();
			InputStream input = conn.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (SocketTimeoutException e) {
			// e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			// e.printStackTrace();
			return null;
		} catch (SocketException e) {
			// e.printStackTrace();
			return null;
		} catch (IOException e) {
			// e.printStackTrace();
			return null;
		}
	}

	// decode from file
	@SuppressWarnings("unused")
	private Bitmap decodeFile(FileInputStream f) throws FileNotFoundException {
		// decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(f, null, o);

		// Find the correct scale value. It should be the power of 2.
		final int REQUIRED_SIZE = 70;
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeStream(f, null, o2);
	}

	/**
	 * decode from file.
	 * 
	 * @param: WIDTH/HIGHT: out put size
	 * */
	@SuppressWarnings("unused")
	private Bitmap decodeFile(File f, int WIDTH, int HIGHT) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// The new size we want to scale to
			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}

		return null;
	}

	@SuppressWarnings("unused")
	private String bitmapToFile(Bitmap bitmap, String link) throws IOException {
		String nameBitmap = encodeLink(link);
		String writeTo = new File(Environment.getExternalStorageDirectory() + File.separator + nameBitmap).toString();
		FileOutputStream output;
		output = new FileOutputStream(writeTo);
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
		output.close();
		return nameBitmap;
	}

	// http://web.shoppie.com.vn:8080/Upload/MERCHANTS/8/CAMPAIGNS/cp.jpg
	private String encodeLink(String link) {
		return Base64.encodeToString(link.getBytes(), Base64.URL_SAFE);
	}

	@SuppressWarnings("unused")
	private String decodeLink(String base64) {
		return Base64.decode(base64, Base64.URL_SAFE).toString();
	}
	
	public static void writeRegId(Context sContext,String regId, String name, String email) {
		try {
			File SDCardRoot = Environment.getExternalStorageDirectory().getAbsoluteFile();
			File myFile = new File(SDCardRoot,"shoppie_gcm_id.txt");
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("<bluemac>" + email + "</bluemac>");
			myOutWriter.append("<name>" + name + "</name>");
			myOutWriter.append("<key>" + regId + "</key>");
			myOutWriter.flush();
			myOutWriter.close();
			fOut.close();
//			if (sContext != null)
//				Toast.makeText(sContext, "Done writing SD '"+myFile.getName()+"'", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
//			if (sContext != null)
//				Toast.makeText(sContext, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
