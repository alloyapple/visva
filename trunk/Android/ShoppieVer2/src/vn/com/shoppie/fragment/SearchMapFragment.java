package vn.com.shoppie.fragment;

import java.security.acl.LastOwnerException;
import java.util.HashMap;
import java.util.List;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.SearchActivity;
import vn.com.shoppie.database.sobject.MerchantCategoryItem;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

public class SearchMapFragment extends SupportMapFragment{

	private GoogleMap map;
	private Marker curMarker = null;
	
	private HashMap<Marker, MerchantStoreItem> manageStorebyMarker = new HashMap<Marker, MerchantStoreItem>();
	
	private View mOriginalContentView;
	private TouchableWrapper mTouchView; 
	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
	    mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);    
	    mTouchView = new TouchableWrapper(getActivity());
	    mTouchView.addView(mOriginalContentView);
	    return mTouchView;
	  }
	
	public void changeLocation(double latitude , double longitute) {
		if(map == null)
			map = getMap();
		
		LatLng latLng = new LatLng(latitude, longitute); 
		map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
	    map.animateCamera(CameraUpdateFactory.zoomTo(14));
	    map.setMyLocationEnabled(true);
	    map.getUiSettings().setZoomControlsEnabled(true);
	    map.getUiSettings().setMyLocationButtonEnabled(true);
	    map.getUiSettings().setCompassEnabled(true);
	    map.getUiSettings().setRotateGesturesEnabled(true);
	    map.getUiSettings().setZoomGesturesEnabled(true);
	    map.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				if(curMarker != null) {
					MerchantStoreItem store = manageStorebyMarker.get(curMarker);
					
					int color = getColorByStore(store);
					
					curMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createMakerIcon(0, "+" + store.getPieQty() , color)));
					curMarker = null;
					getMap().animateCamera(CameraUpdateFactory.zoomTo(14));
				}
			}
		});
	    map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				
				if(curMarker != null) {
					MerchantStoreItem store = manageStorebyMarker.get(curMarker);
					
					Projection projection = getMap().getProjection();
					LatLng markerLocation = marker.getPosition();
					Point screenPosition = projection.toScreenLocation(markerLocation);
					
					if(marker.equals(curMarker)) {
						if(screenPosition.x <= mTouchView.getXLastTouchOnScreen() && marker.equals(curMarker)) {
							((SearchActivity) getActivity()).onClickViewStoreDetail(store);
						}
						else {
							int color = getColorByStore(store);
							
							curMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createMakerIcon(0, "+" + store.getPieQty() , color)));
							curMarker = null;
							getMap().animateCamera(CameraUpdateFactory.zoomTo(14));
						}
					}
					else {
						
						int color = getColorByStore(store);
						
						curMarker.setIcon(BitmapDescriptorFactory.fromBitmap(createMakerIcon(0, "+" + store.getPieQty() , color)));
						
						curMarker = marker;
						MerchantStoreItem store1 = manageStorebyMarker.get(marker);
						
						color = getColorByStore(store1);
						
						marker.setIcon(BitmapDescriptorFactory.fromBitmap(createMakerIconDetail(0, "+" + store1.getPieQty(), store1.getStoreName(), store1.getStoreAddress() , color)));
						getMap().moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
						getMap().animateCamera(CameraUpdateFactory.zoomTo(17));
					}
				}
				else {
					curMarker = marker;
					MerchantStoreItem store = manageStorebyMarker.get(marker);
					
					int color = getColorByStore(store);
					
					marker.setIcon(BitmapDescriptorFactory.fromBitmap(createMakerIconDetail(0, "+" + store.getPieQty(), store.getStoreName(), store.getStoreAddress() , color)));
					getMap().moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
					getMap().animateCamera(CameraUpdateFactory.zoomTo(17));
				}
				return true;
			}
		});
	}
	
	private void refreshMarker(Marker marker) {
		
	}

	
	private Marker addMaker(double latitude , double longitute , String value , String name , int color) {
		// create marker
		MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitute)).title(name);
		 
		// Changing marker icon
		Bitmap icon = createMakerIcon(0, value , color);
		marker.icon(BitmapDescriptorFactory.fromBitmap(icon));
		
		// adding marker
		Marker markerObject = getMap().addMarker(marker);
		return markerObject;
	}
	
	private void addMarker(MerchantStoreItem store) {
		int color = getColorByStore(store);
		Marker maker = addMaker(Double.parseDouble(store.getLatitude()), Double.parseDouble(store.getLongtitude()), 
				"+" + store.getPieQty(), store.getStoreName() , color);
		manageStorebyMarker.put(maker, store);

	}
	
	private int getColorByStore(MerchantStoreItem store) {
		try {
			MerchantCategoryItem category = ((SearchActivity) getActivity()).getCategoryByStore(store);
			String color = category.getLineColor();
			String temp[] = color.split(",");
			int colorValue = Color.rgb(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
			return colorValue;			
		} catch (Exception e) {
			// TODO: handle exception
			return 0xffff0000;
		}
	}
	
	private Bitmap createMakerIcon(int type , String value , int color) {
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
		textPaint.setTextSize(30);
		int width = (int) (textPaint.measureText(value) * 4f / 2f);
		int height = (int) (width + getPixelByDp(5));
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setColor(color);
		canvas.drawCircle(width / 2, width / 2, width / 2, paint);
		canvas.drawLine(width / 2, width, width / 2, height , paint);
		canvas.drawText(value, width / 2, width / 2 + textPaint.getTextSize() / 3, textPaint);
		
		return bitmap;
	}
	
	private Bitmap createMakerIconDetail(int type , String value , String name , String ad , int color) {
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(0xffffffff);
		textPaint.setTextSize(getResources().getDimension(R.dimen.marker_textsize));
		
		int width = (int) (textPaint.measureText(value) * 4f / 2f);
		int nameWidth = Math.min((int) (textPaint.measureText(name)), width * 3);
		int height = (int) (width + getPixelByDp(5));
		Bitmap bitmap = Bitmap.createBitmap(width * 2 + nameWidth * 2, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setStrokeWidth(3);
		paint.setAntiAlias(true);
		paint.setColor(color);
		
		canvas.drawCircle(canvas.getWidth() / 2, width / 2, width / 2, paint);
		canvas.drawCircle(canvas.getWidth() - width / 2, width / 2, width / 2, paint);
		canvas.drawRect(new Rect(canvas.getWidth() / 2, 0, canvas.getWidth() - width / 2, width), paint);
		canvas.drawLine(canvas.getWidth() / 2, width, canvas.getWidth() / 2, height , paint);
		canvas.drawText(value, canvas.getWidth() / 2, width / 2 + textPaint.getTextSize() / 3, textPaint);
		
		String nameToDraw = "";
		if(textPaint.measureText(name) <= nameWidth)
			nameToDraw = name;
		else {
			for (int i = name.length() - 1; i >= 0; i--) {
				if(textPaint.measureText(name.substring(0, i) + "...") <= nameWidth) {
					nameToDraw = name.substring(0, i) + "...";
					break;
				}
			}
		}
		textPaint.setTextAlign(Align.LEFT);
		canvas.drawText(nameToDraw, canvas.getWidth() / 2 + width / 2, width * 2 / 7 + textPaint.getTextSize() / 3, textPaint);
		
		String adToDraw = "";
		textPaint.setTextSize(20);
		if(textPaint.measureText(ad) <= nameWidth)
			adToDraw = ad;
		else {
			for (int i = ad.length() - 1; i >= 0; i--) {
				if(textPaint.measureText(ad.substring(0, i) + "...") <= nameWidth) {
					adToDraw = ad.substring(0, i) + "...";
					break;
				}
			}
		}
		textPaint.setTextAlign(Align.LEFT);
		canvas.drawText(adToDraw, canvas.getWidth() / 2 + width / 2, width * 3 / 4 + textPaint.getTextSize() / 3, textPaint);
		
		return bitmap;
	}
	
	public void updatePie(List<MerchantStoreItem> data) {
		manageStorebyMarker.clear();
		curMarker = null;
		getMap().clear();
		
		int count = 0;
		for (MerchantStoreItem merchantStoreItem : data) {
			if(count == 0) {
				Location location = ((SearchActivity) getActivity()).getMyLocation();
				changeLocation(location.getLatitude(), location.getLongitude());
//				changeLocation(Double.parseDouble(merchantStoreItem.getLatitude()), Double.parseDouble(merchantStoreItem.getLongtitude()));
				
			}
			addMarker(merchantStoreItem);
			
			count++;
		}
		
		this.data = data;
	}
	
	public int getPixelByDp(int dp) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		return (int) px;
	}
	
	public boolean isVisibleArea(Marker marker) {
		   final LatLngBounds.Builder bld = new LatLngBounds.Builder();
		   final VisibleRegion visibleRegion = getMap().getProjection().getVisibleRegion();
		   bld.include(visibleRegion.farLeft)
		      .include(visibleRegion.farRight)
		      .include(visibleRegion.nearLeft)
		      .include(visibleRegion.nearRight);
		   return bld.build().contains(marker.getPosition());
	}
	
	public Location getMyLocation() {
		return getMap().getMyLocation();
	}
 	
	public class TouchableWrapper extends FrameLayout {
		private int lastX;
		
		public TouchableWrapper(Context context) {
			super(context);
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				lastX = (int) event.getX();
				break;
			}
			return super.dispatchTouchEvent(event);
		}
		
		public int getXLastTouchOnScreen() {
			int location[] = new int[2];
			getLocationOnScreen(location);
			return lastX + location[0];
		}
	}
	
	private List<MerchantStoreItem> data;
}
