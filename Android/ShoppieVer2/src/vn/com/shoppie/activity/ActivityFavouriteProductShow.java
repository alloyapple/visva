package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ActivityFavouriteProductShow extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.collectiondetail_1);
	}

}
