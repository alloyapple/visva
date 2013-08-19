package com.qoppa.samples.viewer;

import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.util.Log;

import com.qoppa.android.pdf.annotations.Annotation;
import com.qoppa.android.pdfProcess.IPicture;

public class PictureFactory
{
	static ThreadPoolExecutor service;
	static Vector<PDFPageView> currentlyOnQueue;
	
	static
	{
		service = new ThreadPoolExecutor(1, 1, 250, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		currentlyOnQueue = new Vector<PDFPageView>();
	}
	
	public static void requestPicture(PDFPageView view)
	{
		synchronized (currentlyOnQueue)
		{
			if(currentlyOnQueue.contains(view) == false)
			{
				currentlyOnQueue.add(view);
				service.submit(new PictureGenTask(view));
			}
		}
	}
	
	private static class PictureGenTask implements Runnable
	{
		PDFPageView m_View;
		
		public PictureGenTask(PDFPageView view)
		{
			m_View = view;
		}
		
		public void run()
		{
			Rect viewBounds = new Rect();
			m_View.getHitRect(viewBounds);
			
			Rect scrollBounds = new Rect();
			m_View.getPDFViewer().getScrollView().getDrawingRect(scrollBounds);
			
			// Only get the page picture if the view is visible.  It is very possible that the request page 
			//has been scrolled off the screen and we don't need to generate the picture just now.
			if(Rect.intersects(scrollBounds, viewBounds))
			{
				try
				{
					// Generate page picture
					IPicture background = m_View.getPage().getIPicture();
					Vector<Annotation> annots = m_View.getPage().getAnnotations();
					
					// Set the page contents to the page picture
					m_View.setPageContents(new PageContentsIPicture(background, annots, false));
				}
				catch (Throwable t)
				{
					//error occurred while rendering this page.  for this page, create 
					//a picture which draws an error message.  
					
					Log.e("error", Log.getStackTraceString(t));
					
					int pageWidth = (int) m_View.getPage().getDisplayWidth();
					int pageHeight = (int) m_View.getPage().getDisplayHeight();

					// Create picture and canvas to draw on
					Picture pic = new Picture();
					Canvas cv = pic.beginRecording(pageWidth, pageHeight);

					Paint p = new Paint();
					p.setColor(Color.WHITE);
					p.setStyle(Paint.Style.FILL);
					cv.drawRect(0, 0, pageWidth, pageHeight, p);

					p.setColor(Color.RED);
					p.setTextSize(22);
					p.setAntiAlias(true);

					String firstLine = "Error Loading:";
					String secondLine = t.getMessage();
					if(secondLine == null)
					{
						secondLine = t.toString();
					}

					Rect firstLineBounds = new Rect();
					Rect secondLineBounds = new Rect();

					p.getTextBounds(firstLine, 0, firstLine.length(), firstLineBounds);
					p.getTextBounds(secondLine, 0, secondLine.length(), secondLineBounds);

					int firstX = pageWidth / 2 - firstLineBounds.width() / 2;
					int secondX = pageWidth / 2 - secondLineBounds.width() / 2;

					int firstY = (pageHeight / 2 - firstLineBounds.height() / 2 - secondLineBounds.height() / 2);
					int secondY = firstY + firstLineBounds.height();

					cv.drawText(firstLine, firstX, firstY, p);
					cv.drawText(secondLine, secondX, secondY, p);

					// End recording
					pic.endRecording();

					// Set the page contents to the error picture
					m_View.setPageContents(new PageContentsPicture(pic, null, false));
				}
				
				m_View.getPDFViewer().redrawVisible();
			}
			
			synchronized (currentlyOnQueue)
			{
				currentlyOnQueue.remove(m_View);
			}
		}
	}
}
