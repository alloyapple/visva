package vn.com.shoppie.activity;

import java.util.ArrayList;

import vn.com.shoppie.ActivityLogo;
import vn.com.shoppie.R;
import vn.com.shoppie.adapter.AdapterNotification;
import vn.com.shoppie.database.sobject.ShoppieObject;
import vn.com.shoppie.view.LayoutTop;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class ActivityNotification extends VisvaAbstractActivity implements OnClickListener {
	public static final String FLAG_NOTIFI = "notification";
	public static final String FLAG_NOTIFI_DATA="data";
	public boolean isFromNotifi = false;
	RelativeLayout mRlTop;
	LayoutTop mLayoutTop;
	ListView mLvNotifi;
	ProgressBar prgLoading;

	AdapterNotification mAdapter;
	ArrayList<ShoppieObject> data = new ArrayList<ShoppieObject>();

	String message ;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_notification);
		message = this.getString(R.string.hom_thu);
		findViewById();
		if (this.getIntent().hasExtra(FLAG_NOTIFI)) {
			message = this.getIntent().getStringExtra(FLAG_NOTIFI);
			isFromNotifi = true;
		} else {
			isFromNotifi = false;
		}
		//refreshUI();
	}

	public void findViewById() {
		mRlTop = (RelativeLayout) findViewById(R.id.layout_top);
		mLayoutTop = new LayoutTop(this, mRlTop);

		mLvNotifi = (ListView) findViewById(R.id.lv_notifi);
		mAdapter = new AdapterNotification(this, data);
		mLvNotifi.setAdapter(mAdapter);

		mLayoutTop.mBtnRight.setImageResource(R.drawable.ic_new_email);
		mLayoutTop.mBtnRight.setOnClickListener(this);
		mLayoutTop.mBtnLeft.setOnClickListener(this);
		mLayoutTop.mBtnRight.setVisibility(View.GONE);
		prgLoading=(ProgressBar)findViewById(R.id.prg_loading);
		

		if (!isFromNotifi) {
			mLayoutTop.setText(message);
		}
	}


	public void refreshData(ArrayList<ShoppieObject> newData) {
		if(newData!=null && !newData.isEmpty()){
			data.clear();
			for(ShoppieObject obj: newData){
				data.add(obj);
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.layout_top_btn_left:
			this.finish();
			break;
		case R.id.layout_top_btn_right:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		if (isFromNotifi) {
			startActivity(new Intent(this, ActivityLogo.class));
		}
		super.onDestroy();
	}

	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}
}
