package com.qoppa.samples.viewer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.graphics.Picture;
import android.graphics.RectF;
import android.view.View;

import com.qoppa.android.pdf.annotations.Annotation;
import com.qoppa.android.pdfProcess.PDFPage;

public class PDFPageView extends View
{
	protected PDFPage m_PDFPage;
	protected PDFViewer m_PDFViewer;
	
	protected float m_Scale;
	
	public PDFPageView(Context context, PDFPage page, PDFViewer viewer, float scale)
	{
		super(context);
		
		m_PDFViewer = viewer;
		m_PDFPage = page;
		m_Scale = scale;
	}
	
	@Override
	protected void onDraw(final Canvas canvas) 
	{
		if (canvas.getClipBounds().width() > 0 && canvas.getClipBounds().height() > 0)
		{
			PageContents page = getPageContents();
			if(page != null)
			{
				int restore = canvas.save();
				
				canvas.scale(m_Scale, m_Scale);
				
				page.drawBackground(canvas);
				
				if (page.m_Annotations != null && page.m_Annotations.size() > 0)
				{
					// The canvas needs to be rotated like the page when it's painted
					RotationInfo rotInfo = getPageRotationInfo(m_PDFPage.getPageRotation(), m_PDFPage.getCropBox().width(), m_PDFPage.getCropBox().height());
					canvas.concat(rotInfo.m_XForm);
					canvas.translate(-m_PDFPage.getDisplayX(), -m_PDFPage.getDisplayY());
					
					for (Annotation annot : page.m_Annotations)
					{
						// Save canvas state
						int sCount = canvas.save();

						// Translate to the annotation's origin
						RectF annotRect = annot.getRectangle();
						canvas.translate(annotRect.left, annotRect.top);

						// Paint the annotation
						annot.paint(canvas, false);

						// Restore canvas state
						canvas.restoreToCount(sCount);
					}
				}
				
				canvas.restoreToCount(restore);

				//draw a dark grey border around the page
				Paint paint = new Paint();
				paint.setColor(Color.rgb(150, 150, 150));
				paint.setStrokeWidth(1f);
				paint.setStyle(Style.STROKE);
				canvas.drawRect(new RectF(.5f, .5f, getWidth() - .5f , getHeight() - .5f), paint);
			}
		}
	}
	
	
	@Override
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = (int)((m_PDFPage.getDisplayWidth()) * m_Scale);
		int height = (int)((m_PDFPage.getDisplayHeight())* m_Scale);
		
		setMeasuredDimension(width, height);
	}
	
    public void zoomChanged (float newScale)
    {
    	m_Scale = newScale;
    	requestLayout();
    }
    
	public synchronized void setPageContents(PageContents pc)
	{
		if(getParent() != null)
		{
			m_PDFViewer.cachePage(m_PDFPage.getPageIndex(), pc);
		}
	}
	
    protected synchronized PageContents getPageContents()
    {
    	// Try to get page contents from the soft reference
    	PageContents contents = m_PDFViewer.getCachcedPage(m_PDFPage.getPageIndex());
    	
    	//either wasn't cached or was grey
    	if (contents == null || contents.m_IsGray)
    	{
    		/*we need to request everytime that the reference is gray because
        	its very possible a prior request was made and removed (requests 
        	are removed if the requesting page is no longer visible (scrolled off screen))
        	by the time the factory got to fulfilling the request.  In which case, the request
        	for this gray page is no longer on the queue and needs to be re placed.  
        	The factory will ensure the request does not get placed on the queue twice.*/
    		PictureFactory.requestPicture(this);
        	
        	if (contents == null)
        	{
        		//set the contents of this page as a gray picture until the rendering completes

        		int pageWidth = (int) m_PDFPage.getDisplayWidth();
    			int pageHeight = (int) m_PDFPage.getDisplayHeight();
    	
    			// Create picture and canvas to draw on
    			Picture pic = new Picture();
    			Canvas cv = pic.beginRecording(pageWidth, pageHeight);
    	
    			// Paint dark gray rectangle
    			Paint p = new Paint();
    			p.setColor(Color.GRAY);
    			p.setStyle(Paint.Style.FILL);
    			cv.drawRect(0, 0, pageWidth, pageHeight, p);
    			pic.endRecording();
    				
    			//android seems to have issues if a pictures canvas
    			//isn't entirely popped.  Do this to be safe.  
    			cv.restoreToCount(1);
    	
    			// Save the dark gray page contents so that we don't regenerate it all the time
    			contents = new PageContentsPicture(pic, null, true);
    			setPageContents(contents);
        	}
    	}
    	else
    	{
    		//the page contents was cached.  do a put to bump it to the front of the cache, because
    		//we want the most recently viewed PageContents to be released last.
    		m_PDFViewer.cachePage(m_PDFPage.getPageIndex(), contents);
    	}
    	
    	return contents;
    }
	
	public PDFPage getPage()
	{
		return m_PDFPage;
	}
	
	public PDFViewer getPDFViewer()
	{
		return m_PDFViewer;
	}
	
	//theta is in degrees
    private RotationInfo getPageRotationInfo (float degrees, float pageWidth, float pageHeight)
    {	
    	Matrix m = new Matrix();
    	m.setRotate(degrees);
    		
    	RotationInfo rotInfo = new RotationInfo ();
        rotInfo.m_XForm = m;
        
        // transform width/height point to see where they end up
        float[] pt = new float[]{pageWidth, pageHeight};
        rotInfo.m_XForm.mapPoints(pt);
        rotInfo.m_WHPoint = new PointF (pt[0], pt[1]);

        if (rotInfo.m_WHPoint.x < 0)
        {
            rotInfo.m_XForm.preTranslate (0, -pageHeight);
        }
        if (rotInfo.m_WHPoint.y < 0)
        {
            rotInfo.m_XForm.preTranslate (-pageWidth, 0);
        }
        
        return rotInfo;
    }
    
    class RotationInfo
    {
        public Matrix m_XForm;
        public PointF m_WHPoint;
    }

}
