package com.qoppa.samples.viewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class PageViewDivider extends View
{	
	private float m_Scale;
	private float m_PriorPageWidth;
	private static final int m_ScrollViewColor  = 0xFFEEEEEE;
	
	public PageViewDivider(Context context, float scale, float widthOfPageAboveThisDivider)
	{
		super(context);
		m_Scale = scale;
		m_PriorPageWidth = widthOfPageAboveThisDivider;
	}
	
	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = (int)(m_PriorPageWidth * m_Scale);
		int height = (int)(2 * m_Scale);
		
		setMeasuredDimension(width, height);
	}
	
    public void zoomChanged (float newScale)
    {
    	m_Scale = newScale;
    	requestLayout();
    }

    protected void onDraw(Canvas canvas)
    {
    	Paint p = new Paint();
    	p.setColor(m_ScrollViewColor);
    	
    	canvas.drawRect(0, 0, getWidth(), getHeight(), p);
    }
}
