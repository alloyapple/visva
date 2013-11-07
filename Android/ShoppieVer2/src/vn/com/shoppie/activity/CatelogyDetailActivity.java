package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CollectionDetailAdapter;
import vn.com.shoppie.view.MPager;
import vn.com.shoppie.view.OnItemClick;
import vn.com.shoppie.view.PieView;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class CatelogyDetailActivity extends VisvaAbstractActivity {

	private MPager mPager;
	
	@Override
	public int contentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_catelogy_detail;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onCreate() {
		if (Build.VERSION.SDK_INT >= 11)
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		
		ImageButton icon = (ImageButton) findViewById(R.id.actionbar_icon);
		icon.setBackgroundResource(R.drawable.ic_back);
		icon.setImageBitmap(null);
		
		mPager = (MPager) findViewById(R.id.pager);
		final CollectionDetailAdapter adapter = new CollectionDetailAdapter(this , mPager);
		mPager.setAdapter(adapter);
		mPager.setCanbeExtended(false);
		
		adapter.setOnItemClick(new OnItemClick() {
			
			@Override
			public void onClick(int pos) {
				// TODO Auto-generated method stub
				if(pos == adapter.getCount() - 1){
					PieView pie = (PieView) findViewById(R.id.pie);
					pie.setVisibility(View.VISIBLE);
					pie.start();
				}
			}
		});
	}
}
