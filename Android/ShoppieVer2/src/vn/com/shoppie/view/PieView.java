package vn.com.shoppie.view;

import vn.com.shoppie.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class PieView extends View{
	
	private boolean isRunning = false;
	private long lastTime = 0;
	private int index = 0;
	
	public PieView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public PieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PieView(Context context) {
		super(context);ImageView i;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		
		if(isRunning){
			if(index >= imageId.length){
				isRunning = false;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setVisibility(GONE);
				return;
			}
			
			if(System.currentTimeMillis() - lastTime > 14){
				setBackgroundResource(imageId[index]);
				index++;
				lastTime = System.currentTimeMillis();
			}
			invalidate();
		}
	}
	
	public void start(){
		index = 0;
		lastTime = System.currentTimeMillis();
		isRunning = true;
		setBackgroundResource(imageId[index]);
		index++;
		invalidate();
	}
	
	private Integer imageId[] = {
			R.drawable.pie_23 , R.drawable.pie_22 ,
			R.drawable.pie_21 , R.drawable.pie_20 ,
			R.drawable.pie_19 , R.drawable.pie_18 ,
			R.drawable.pie_17 , R.drawable.pie_16 ,
			R.drawable.pie_15 , R.drawable.pie_14 ,
			R.drawable.pie_13 , R.drawable.pie_12 ,
			R.drawable.pie_11 , R.drawable.pie_10 ,
			R.drawable.pie_09 , R.drawable.pie_08 ,
			R.drawable.pie_07 , R.drawable.pie_06 ,
			R.drawable.pie_05 , R.drawable.pie_04 ,
			R.drawable.pie_03 , R.drawable.pie_02 ,
			R.drawable.pie_01
			
	};
}
