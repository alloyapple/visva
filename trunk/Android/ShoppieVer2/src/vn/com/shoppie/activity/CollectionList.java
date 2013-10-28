package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.ListCollectionAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionList extends Activity{
	
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listcollection_activity);
		
		init();
	}
	
	private void init() {
		listView = (ListView) findViewById(R.id.list);
		TextView text = new TextView(this);
		text.setBackgroundColor(0xffffffff);
		text.setText("abcd");
		
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView = inflater.inflate(R.layout.catelogy_title, null, false);
		listView.addHeaderView(headerView);
		listView.setAdapter(new ListCollectionAdapter(this));
	}
}
