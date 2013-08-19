package com.qoppa.samples.viewer;


import android.graphics.PointF;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class TouchHandlerView implements OnGestureListener, OnDoubleTapListener, OnTouchListener
{
	private PDFViewer m_PDFViewer;
	private GestureDetector m_GestureScanner;
	private View m_LastTouchedView = null;

	/**
	 * Variables for pinch zooming
	 */
	// Flag indicating zoom
	protected static final int NORMAL = 0;
	protected static final int ZOOM = 1;
	protected static final int IGNORE = 2;
	
	private int m_Mode = NORMAL;
	private PointF m_StartP = new PointF();
	private float m_StartDistance;
	private PointF m_MidP = new PointF();
	private PointF m_ScreenP = new PointF();
	private View m_ZoomView = null;

	public TouchHandlerView(PDFViewer viewer)
	{
		m_PDFViewer = viewer;

		m_GestureScanner = new GestureDetector(this);
		m_GestureScanner.setOnDoubleTapListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch (event.getAction() & MotionEvent.ACTION_MASK) 
		{
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP: 
			{
				actionUp(event);
				break;
			}
			case MotionEvent.ACTION_DOWN: {
				actionDown(v, event);
				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN
			: {
				actionPointerDown(v, event);
				break;
			}
			case MotionEvent.ACTION_MOVE: 
			{
				actionMove(v, event);
				break;
			}
		}

		return m_GestureScanner.onTouchEvent(event);
	}

	protected void actionUp(MotionEvent event)
	{
		if (m_Mode == ZOOM)
		{
			finishZoom();
		}
	}

	protected void actionDown(View v, MotionEvent event)
	{
		if (v instanceof FrameLayout || v instanceof LinearLayout)
		{
			m_LastTouchedView = v;
		}
		else
		{
			m_Mode = NORMAL;

			m_StartP.set(event.getX(), event.getY());

			m_LastTouchedView = v;
		}
	}

	protected void actionMove(View v, MotionEvent event)
	{
		if (m_Mode == ZOOM)
		{
			moveZoom(event);
		}
	}

	protected void finishZoom()
	{
		m_Mode = IGNORE;
		m_PDFViewer.getScrollView().stopZooming();

		float zoomScale = m_PDFViewer.getScrollView().getZoomingScale();
		float newScale = m_PDFViewer.getCurrentScale() * zoomScale;

		// Make sure the newScale is within the proper range
		if (newScale < m_PDFViewer.getMinimumScale())
		{
			newScale = m_PDFViewer.getMinimumScale();
		}
		if (newScale > m_PDFViewer.getMaximumScale())
		{
			newScale = m_PDFViewer.getMaximumScale();

			// Recalculate the zoom scale to scroll to the correct location
			zoomScale = newScale / m_PDFViewer.getCurrentScale();
		}

		// Scale and validate
		m_PDFViewer.setScale(newScale, null);
		m_PDFViewer.getScrollView().validate();

		// Scroll to the view and adjust to the midpoint and screen location
		int x = Math.round(-m_PDFViewer.getScrollView().getLeft() + m_ZoomView.getLeft() + (m_MidP.x * zoomScale) - m_ScreenP.x);
		int y = Math.round(-m_PDFViewer.getScrollView().getTop() + m_ZoomView.getTop() + (m_MidP.y * zoomScale) - m_ScreenP.y);
		m_PDFViewer.getScrollView().scrollTo(x, y);

		m_ZoomView = null;
	}

	protected void actionPointerDown(View v, final MotionEvent event)
	{
		if (v instanceof PDFPageView || v instanceof LinearLayout)
		{
			WrapMotionEvent wrapEvent = wrap(event);

			m_StartDistance = distance(wrapEvent);

			// There seem to be problems if the distance is
			// too small - 10f was used in the sample
			if (m_StartDistance > 10f)
			{
				// The event is in the view coordinates
				m_Mode = ZOOM;

				m_MidP.x = (wrapEvent.getX(0) + wrapEvent.getX(1)) / 2;
				m_MidP.y = (wrapEvent.getY(0) + wrapEvent.getY(1)) / 2;

				// Calculate the starting distance
				m_StartDistance = distance(wrapEvent);

				// Remember the view
				m_ZoomView = v;

				// Remember the screen location of the midpoint
				m_ScreenP.x = m_MidP.x + v.getLeft() - m_PDFViewer.getScrollView().getScrollX();
				m_ScreenP.y = m_MidP.y + v.getTop() - m_PDFViewer.getScrollView().getScrollY();

				// The scroll view uses screen coordinates
				m_PDFViewer.getScrollView().startZooming(m_ScreenP.x, m_ScreenP.y);

				// Zooming scale starts off at 1 and is a ratio of
				// the calculated distance over the starting distance
				m_PDFViewer.getScrollView().setZoomingScale(1.0f);
			}
		}
	}

	protected void moveZoom(final MotionEvent event)
	{
		float distance = distance(wrap(event));

		if (distance > 10f)
		{
			// Zooming scale starts off at 1 and is a ratio of
			// the calculated distance over the starting distance
			m_PDFViewer.getScrollView().setZoomingScale(distance / m_StartDistance);
		}
	}

	protected float distance(WrapMotionEvent wrapEvent)
	{
		float x = wrapEvent.getX(0) - wrapEvent.getX(1);
		float y = wrapEvent.getY(0) - wrapEvent.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	public boolean onTouchEvent(MotionEvent ev)
	{
		return m_GestureScanner.onTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
	{
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e)
	{
		if (m_LastTouchedView instanceof LinearLayout || m_LastTouchedView instanceof PDFPageView)
		{
			// don't zoom if touch was on the Framview

			QScrollView scrollView = m_PDFViewer.getScrollView();

			// Translate the event to screen coordinates.
			PointF point = new PointF(e.getX() + m_LastTouchedView.getLeft() - scrollView.getScrollX(), e.getY() + m_LastTouchedView.getTop() - scrollView.getScrollY());
			m_PDFViewer.zoom(point);
		}

		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e)
	{
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e)
	{
		return true;
	}

	public boolean dontIntercept(MotionEvent event, View view)
	{
		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (m_Mode == ZOOM || m_Mode == IGNORE)
			{
				return true;
			}
		}
		return false;
	}

	WrapMotionEvent wrap(MotionEvent event)
	{
		try
		{
			return new EclairMotionEvent(event);
		}
		catch (VerifyError e)
		{
			return new WrapMotionEvent(event);
		}
	}

	class WrapMotionEvent
	{
		protected MotionEvent event;

		protected WrapMotionEvent(MotionEvent event)
		{
			this.event = event;
		}

		public int getAction()
		{
			return event.getAction();
		}

		public float getX()
		{
			return event.getX();
		}

		public float getX(int pointerIndex)
		{
			verifyPointerIndex(pointerIndex);
			return getX();
		}

		public float getY()
		{
			return event.getY();
		}

		public float getY(int pointerIndex)
		{
			verifyPointerIndex(pointerIndex);
			return getY();
		}

		public int getPointerCount()
		{
			return 1;
		}

		public int getPointerId(int pointerIndex)
		{
			verifyPointerIndex(pointerIndex);
			return 0;
		}

		private void verifyPointerIndex(int pointerIndex)
		{
			if (pointerIndex > 0)
			{
				throw new IllegalArgumentException("Invalid pointer index for Donut/Cupcake");
			}
		}
	}

	class EclairMotionEvent extends WrapMotionEvent
	{

		protected EclairMotionEvent(MotionEvent event)
		{
			super(event);
		}

		public float getX(int pointerIndex)
		{
			return event.getX(pointerIndex);
		}

		public float getY(int pointerIndex)
		{
			return event.getY(pointerIndex);
		}

		public int getPointerCount()
		{
			return event.getPointerCount();
		}

		public int getPointerId(int pointerIndex)
		{
			return event.getPointerId(pointerIndex);
		}
	}
}
