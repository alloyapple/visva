package vn.com.shoppie.fragment;

import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchMapFragment extends SupportMapFragment{
	// =============================Constant Define=====================
	// ============================Control Define =====================
	// ============================Class Define =======================
	// ============================Variable Define =====================

	private GoogleMap map;
	
	public void changeLocation(double latitude , double longitute) {
		if(map == null)
			map = getMap();
		
		LatLng latLng = new LatLng(latitude, longitute); 
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	    map.animateCamera(CameraUpdateFactory.zoomTo(17));
	}
	
	public void addMaker(double latitude , double longitute , String value , String name) {
		// create marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitute)).title(name);
		 
		// Changing marker icon
		marker.icon(BitmapDescriptorFactory.fromBitmap(createMakerIcon(0, value)));
		 
		// adding marker
		getMap().addMarker(marker);
	}
	
	private Bitmap createMakerIcon(int type , String value) {
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);
//		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setColor(0xffffffff);
		textPaint.setTextSize(30);
		int width = (int) (textPaint.measureText(value) * 4f / 2f);
		int height = (int) (width * 5f / 4f);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setColor(0xffff0000);
		canvas.drawCircle(width / 2, width / 2, width / 2, paint);
		canvas.drawLine(width / 2, width, width / 2, height , paint);
		canvas.drawText(value, width / 2, width / 2 + textPaint.getTextSize() / 3, textPaint);
		
		return bitmap;
	}
	
	private Bitmap createMakerIcon1(int type , String value) {
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);
//		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setColor(0xffffffff);
		textPaint.setTextSize(30);
		int width = (int) (textPaint.measureText(value) * 4f / 2f);
		int height = (int) (width * 5f / 4f);
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setColor(0xffff0000);
		canvas.drawCircle(width / 2, width / 2, width / 2, paint);
		canvas.drawLine(width / 2, width, width / 2, height , paint);
		canvas.drawText(value, width / 2, width / 2 + textPaint.getTextSize() / 3, textPaint);
		
		return bitmap;
	}
	
	public void updatePie(Vector<MerchantStoreItem> data) {
		Log.d("Pie", "Start");
//		getMap().clear();
		
		int count = 0;
		for (MerchantStoreItem merchantStoreItem : data) {
			if(count == 0)
				changeLocation(Double.parseDouble(merchantStoreItem.getLatitude()), Double.parseDouble(merchantStoreItem.getLongtitude()));
			Log.d("Pie", "" + merchantStoreItem.getLatitude() + " : " + merchantStoreItem.getLongtitude());
			addMaker(Double.parseDouble(merchantStoreItem.getLatitude()), Double.parseDouble(merchantStoreItem.getLongtitude()), 
					"+" + merchantStoreItem.getPieQty(), merchantStoreItem.getStoreName());
			count++;
		}
	}
}
