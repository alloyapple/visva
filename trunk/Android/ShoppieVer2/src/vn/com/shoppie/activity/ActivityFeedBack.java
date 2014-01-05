package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityFeedBack extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_act);
		
		RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
		
		View cover = new View(getApplicationContext());
		cover.setBackgroundResource(R.drawable.bg_center);
		LayoutParams params = new LayoutParams((int)(getDimention(R.dimen.collectiondetail_item_width) * 1.06f), 
				(int)((getDimention(R.dimen.collectiondetail_item_height) * 1.05f)));
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		container.addView(cover, 0 , params);
	}
	
	private int getDimention(int id) {
		return (int) getResources().getDimension(id);
	}
}
