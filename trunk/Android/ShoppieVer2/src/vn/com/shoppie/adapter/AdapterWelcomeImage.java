package vn.com.shoppie.adapter;

import java.util.ArrayList;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.ActivityWelcome;
import vn.com.shoppie.util.SUtilBitmap;
import vn.com.shoppie.util.SUtilText;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AdapterWelcomeImage extends PagerAdapter {
	Context context;
	ArrayList<Integer> data;
	
	public AdapterWelcomeImage(Context context, ArrayList<Integer> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}
	static Drawable drb;
	@Override
	public Object instantiateItem(View container, int position) {
		LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v;
		if (data.get(position) == ActivityWelcome.PAGE_REGISTER) {
			try{
				v=inflater.inflate(R.layout.activity_register, null);
				
			}catch(OutOfMemoryError e){
				Log.e("out of memory", "wellcome Image Adapter");
				return null;
			}
			final EditText name=(EditText)v.findViewById(R.id.activity_register_edt_name);
			
			name.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) { }
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
				
				@Override
				public void afterTextChanged(Editable s) {
					name.removeTextChangedListener(this);
					String txt=SUtilText.removeAccent(s.toString());
					s.clear();
					s.append(txt);
					name.setText(s);
					name.addTextChangedListener(this);
					name.setSelection(name.getText().length());
				}
			});
			final Button btnRegister=(Button)v.findViewById(R.id.activity_register_btn_register);
			
				btnRegister.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(_listener!=null){
							_listener.btnRegisterClick(btnRegister, name);
						}
					}
				});
		}else{
			v = inflater.inflate(R.layout.item_welcome, null);
			ImageView imv = (ImageView) v.findViewById(R.id.iv_welcome);
			try{
				int width=context.getResources().getInteger(R.integer.display_width);
				int height=context.getResources().getInteger(R.integer.display_height);
				imv.setImageBitmap(SUtilBitmap.decodeSampledBitmapFromResource(context.getResources(), data.get(position),width,height));
				
			}catch(OutOfMemoryError e){
				System.gc();
				imv.setImageDrawable(drb);
			}
		}
		((ViewPager) container).addView(v, 0);
		return v;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}
	
	OnWelcomeRegisterListener _listener;
	public void setOnRegisterListener(OnWelcomeRegisterListener listener){
		this._listener=listener;
	}
	public interface OnWelcomeRegisterListener{
		public void btnRegisterClick(View v,EditText name);
	}
}
