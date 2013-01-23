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
		SurfaceHolder.Callback, ITouch {
	private ArrayList<MapObject> mMapObjects = new ArrayList<MapObject>();
	private ArrayList<Line> mLines = new ArrayList<Line>();
	private Context mContext;
	private MySurfaceThread thread;
	private MapObject mSelectedObject;
	private Line mLineObject;

	private boolean mIsDraw;

	public StreamMapSurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		getHolder().addCallback(this);

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
				return true;
			}
		});
		mContext = context;
	}

	public void initMap() {
		// add 
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

	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint g = new Paint();
		g.setColor(Color.WHITE);
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
	public boolean onTouchDown(MotionEvent e) {
		// TODO Auto-generated method stub
		mSelectedObject = getObjectSelected(e);
		return false;
	}

	@Override
	public boolean onTouchMove(MotionEvent e) {
		// TODO Auto-generated method stub
		if (mSelectedObject != null)
			mSelectedObject.move(e.getX(), e.getY());
		return true;
	}

	@Override
	public boolean onTouchUp(MotionEvent e) {
		// TODO Auto-generated method stub
		mSelectedObject = null;
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

interface ITouch {
	public boolean onTouchDown(MotionEvent e);

	public boolean onTouchMove(MotionEvent e);

	public boolean onTouchUp(MotionEvent e);
}