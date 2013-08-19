package com.qoppa.samples.viewer;

import java.util.Vector;

import android.graphics.Canvas;

import com.qoppa.android.pdf.annotations.Annotation;
import com.qoppa.android.pdfProcess.IPicture;

public class PageContentsIPicture extends PageContents
{
	private IPicture m_Background;
	
	public PageContentsIPicture(IPicture picture, Vector<Annotation> annotations, boolean isGray)
	{
		super(annotations, isGray);
		m_Background = picture;
	}
	
	public void drawBackground(Canvas canvas)
	{
		m_Background.draw(canvas);
	}
}
