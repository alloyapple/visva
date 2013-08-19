package com.qoppa.samples.viewer;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Picture;

import com.qoppa.android.pdf.annotations.Annotation;

public class PageContentsPicture extends PageContents
{
	private Picture m_Picture;
	
	public PageContentsPicture(Picture picture, Vector<Annotation> annotations, boolean isGray)
	{
		super(annotations, isGray);
		m_Picture = picture;
	}

	@Override
	public void drawBackground(Canvas canvas)
	{
		canvas.drawPicture(m_Picture);
	}

}
