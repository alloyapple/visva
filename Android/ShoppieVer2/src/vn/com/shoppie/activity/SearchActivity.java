package vn.com.shoppie.activity;

import vn.com.shoppie.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class SearchActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_search_activity);
	}
	
	public void onClickBtnBack(View v){
		finish();
	}
}
