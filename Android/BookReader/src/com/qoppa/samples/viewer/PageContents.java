package com.qoppa.samples.viewer;

import java.util.Vector;

import android.graphics.Canvas;

import com.qoppa.android.pdf.annotations.Annotation;

public abstract class PageContents
{
	public Vector<Annotation> m_Annotations;
	public boolean m_IsGray;
	
	public PageContents(Vector<Annotation> annotations, boolean isGray)
	{
		m_Annotations = annotations;
		m_IsGray = isGray;
	}

	public abstract void drawBackground(Canvas canvas);
}
