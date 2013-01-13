package vsvteam.outsource.leanappandroid.mapobjects;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

public class StreamMapSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, IGesture, ITouch {
	public IControl mControl;
	private ArrayList<MapObject> mMapObjects = new ArrayList<MapObject>();
	private ArrayList<Line> mLines = new ArrayList<Line>();
	private Context mContext;
	private MySurfaceThread thread;
	private MapObject mSelectedObject;
	private Line mLineObject;
	private GestureDetector mGestureDetector;
	private boolean mIsDraw;

	public StreamMapSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		getHolder().addCallback(this);
		mGestureDetector = new GestureDetector(context, new StreamMapDetector(
				this));
		setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					onTouchDown(event);
					break;
				case MotionEvent.ACTION_UP:
					onTouchUp(event);
					break;
				case MotionEvent.ACTION_MOVE:
					onTouchMove(event);
					break;
				default:
					break;
				}
				// return mGestureDetector.onTouchEvent(event);
				return true;
			}
		});
		mContext = context;
	}

	public void addMapObject(MapObject pObject) {
		mMapObjects.add(pObject);
	}

	public void removeMapObject(MapObject pObject) {
		mMapObjects.remove(pObject);
	}

	public void removeMapObject(int pIndex) {
		mMapObjects.remove(pIndex);
	}

	public int getCountObjetcs() {
		return mMapObjects.size();
	}

	public GestureDetector getGestureDetector() {
		return mGestureDetector;
	}

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint g= new Paint();
		g.setColor(Color.BLACK);
		canvas.drawRect(0, 0, getWidth(), getHeight(), g);
		drawLine(canvas);
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		thread = new MySurfaceThread(getHolder(), this);
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		thread.setRunning(false);
		// caculateThread.setRunning(false);
		while (retry) {
			try {
				thread.join();
				// caculateThread.join();
				retry = false;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		switch (mControl.getCurrentControl()) {
		case DfObjects.ARROW:
			break;
		case DfObjects.LINE:
			
			break;
		case DfObjects.CLASSBOX:
			addMapObject(new ClassBox(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		case DfObjects.RECTANGLE:
			addMapObject(new Rectangle(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		case DfObjects.FILEBOX:
			addMapObject(new FileBox(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		case DfObjects.SQUARE:
			addMapObject(new Square(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		default:
			// mSelectedObject=getObjectSelected(e);
			break;
		}

		return false;
	}

	public void drawLine(Canvas cv) {
		if (mLineObject != null)
			mLineObject.drawObjects(cv);
		for (int i = 0; i < mLines.size(); i++) {
			mLines.get(i).drawObjects(cv);
		}
	}

	public MapObject getObjectSelected(MotionEvent e) {
		for (int i = getCountObjetcs() - 1; i >= 0; i--) {
			if (mMapObjects.get(i).isScope(e)) {
				return mMapObjects.get(i);
			}
		}
		return null;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		// try {
		// if (mSelectedObject != null) {
		// mSelectedObject.move(e2.getX(), e2.getY());
		//
		// }
		// } catch (Exception e) {
		// Log.e("mk", "exception cmnr:(");
		// // nothing
		// }
		addMapObject(new Rectangle(mContext, e2.getX(), e2.getY()));
		((RelativeLayout) getParent()).addView(mMapObjects.get(
				getCountObjetcs() - 1).getLayout());
		// addMapObject(new Rectangle(mContext, 100, 40));

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchDown(MotionEvent e) {
		// TODO Auto-generated method stub
		switch (mControl.getCurrentControl()) {
		case DfObjects.ARROW:
			break;
		case DfObjects.LINE:
			mSelectedObject = getObjectSelected(e);
			if (mSelectedObject != null) {
				mIsDraw = true;
				mLineObject = new Line(mContext, e.getX(), e.getY());
				mLineObject.setFirstObject(mSelectedObject);
			}
			break;
		case DfObjects.CLASSBOX:
			addMapObject(new ClassBox(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		case DfObjects.RECTANGLE:
			addMapObject(new Rectangle(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		case DfObjects.FILEBOX:
			addMapObject(new FileBox(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		case DfObjects.SQUARE:
			addMapObject(new Square(mContext, e.getX(), e.getY()));
			((RelativeLayout) getParent()).addView(mMapObjects.get(
					getCountObjetcs() - 1).getLayout());
			break;
		case DfObjects.NONE:
			mSelectedObject = getObjectSelected(e);
			break;
		default:

			break;
		}

		return false;
	}

	@Override
	public boolean onTouchMove(MotionEvent e) {
		// TODO Auto-generated method stub
		if (mIsDraw) {
			mLineObject.setEndPoint(e);
		} else if (mSelectedObject != null
				&& mControl.getCurrentControl() == DfObjects.NONE) {
			mSelectedObject.move(e.getX(), e.getY());

		}
		// addMapObject(new Square(mContext, e.getX(), e.getY()));
		// ((RelativeLayout) getParent()).addView(mMapObjects.get(
		// getCountObjetcs() - 1).getLayout());
		return true;
	}

	@Override
	public boolean onTouchUp(MotionEvent e) {
		// TODO Auto-generated method stub
		if (mIsDraw) {
			mSelectedObject = getObjectSelected(e);
			if(mSelectedObject!=null)
			{
				mLineObject.setEndPoint(e);
				mLines.add(mLineObject);
				mLineObject.setEndObject(mSelectedObject);
				
			}
			mIsDraw=false;
			mLineObject=null;
		} else {

		}
		return false;
	}

}

class MySurfaceThread extends Thread {
	private SurfaceHolder myThreadSurfaceHolder;
	private StreamMapSurfaceView myThreadSurfaceView;
	private boolean myThreadRun = false;

	public MySurfaceThread(SurfaceHolder surfaceHolder,
			StreamMapSurfaceView surfaceView) {
		myThreadSurfaceHolder = surfaceHolder;
		myThreadSurfaceView = surfaceView;
	}

	public void setRunning(boolean b) {
		myThreadRun = b;
	}

	@Override
	public void run() {
		while (myThreadRun) {
			Canvas c = null;
			try {
				c = myThreadSurfaceHolder.lockCanvas(null);
				synchronized (myThreadSurfaceHolder) {
					myThreadSurfaceView.draw(c);
				}
			} finally {
				if (c != null) {
					myThreadSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}

class StreamMapDetector extends SimpleOnGestureListener {
	private IGesture mCaller;

	public StreamMapDetector(IGesture pIGesture) {
		mCaller = pIGesture;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		mCaller.onDoubleTap(e);
		return super.onDoubleTap(e);
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		mCaller.onDoubleTapEvent(e);
		return super.onDoubleTapEvent(e);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		mCaller.onDown(e);
		return super.onDown(e);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		mCaller.onFling(e1, e2, velocityX, velocityY);
		return super.onFling(e1, e2, velocityX, velocityY);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		mCaller.onLongPress(e);
		super.onLongPress(e);
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		mCaller.onScroll(e1, e2, distanceX, distanceY);
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		mCaller.onShowPress(e);
		super.onShowPress(e);
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		mCaller.onSingleTapConfirmed(e);
		return super.onSingleTapConfirmed(e);
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		mCaller.onSingleTapUp(e);
		return super.onSingleTapUp(e);
	}

}

interface IGesture {
	public boolean onDoubleTap(MotionEvent e);

	public boolean onDoubleTapEvent(MotionEvent e);

	public boolean onDown(MotionEvent e);

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY);

	public void onLongPress(MotionEvent e);

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY);

	public void onShowPress(MotionEvent e);

	public boolean onSingleTapConfirmed(MotionEvent e);

	public boolean onSingleTapUp(MotionEvent e);

}

interface ITouch {
	public boolean onTouchDown(MotionEvent e);

	public boolean onTouchMove(MotionEvent e);

	public boolean onTouchUp(MotionEvent e);
}