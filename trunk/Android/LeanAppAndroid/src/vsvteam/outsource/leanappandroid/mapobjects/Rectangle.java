package vsvteam.outsource.leanappandroid.mapobjects;

import vsvteam.outsource.leanappandroid.R;
import android.content.Context;
import android.graphics.Canvas;

public class Rectangle extends MapObject{

	public Rectangle(Context pContext, float pX, float pY) {
		super(pContext, pX, pY);
		// TODO Auto-generated constructor stub
		mLayout.setBackgroundResource(R.drawable.box2);
	}

	@Override
	public void drawObjects(Canvas cv) {
		// TODO Auto-generated method stub
		
	}
	
}
