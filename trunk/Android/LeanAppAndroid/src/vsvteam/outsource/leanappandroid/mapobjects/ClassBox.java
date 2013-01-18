package vsvteam.outsource.leanappandroid.mapobjects;

import vsvteam.outsource.leanappandroid.R;
import android.content.Context;
import android.graphics.Canvas;

public class ClassBox extends MapObject{

	public ClassBox(Context pContext, float pX, float pY) {
		super(pContext, pX, pY);
		// TODO Auto-generated constructor stub
		mLayout.setBackgroundResource(R.drawable.box1);
	}

	@Override
	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub
		
	}

}
