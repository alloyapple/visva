package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.database.sobject.GiftItem;
import vn.com.shoppie.util.ImageLoader;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class GiftDetailActivity extends Activity{
	private GiftItem item;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_detail_activity);
		
		init();
	}

	private void init() {
		item = ActivityGiftTransaction.currItem;
		
		((TextView) findViewById(R.id.desc)).setText(item.getDescription());
		ImageLoader.getInstance(this).DisplayImage(CatelogyAdapter.URL_HEADER + item.getGiftImage(), findViewById(R.id.image), true, true, false, false, true, false);
		
		String price[] = item.getPricesNotArr();
		int pie[] = item.getPiesNotArr();
		String text[] = new String[price.length];
		
		for(int i = 0 ; i < pie.length ; i++) {
			text[i] = item.getGiftName() + " " + price[i] + "VNĐ = " + pie[i] + " Pie"; 
		}
		
//		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_spinner_item, text);
		((Spinner) findViewById(R.id.spinner)).setAdapter(new MySpinnerAdapter(text));
	}
	
	public void onClickBack(View v) {
		finish();
	}
	
	class MySpinnerAdapter extends BaseAdapter {

		String data[];
		
		public MySpinnerAdapter(String data[]) {
			this.data = data;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.spinner_item, null);
			((TextView) convertView.findViewById(R.id.text)).setText(data[position]);
			return convertView;
		}
	}
}
