package vsvteam.outsource.leanappandroid.mapobjects;

import java.util.ArrayList;

import vsvteam.outsource.leanappandroid.activity.valuestreammap.DrawMapActivity;
import vsvteam.outsource.leanappandroid.database.TProcessDataBase;
import vsvteam.outsource.leanappandroid.database.TProcessDataBaseHandler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

public class StreamMapSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback, ITouch {
	private ArrayList<MapObject> mMapObjects = new ArrayList<MapObject>();
	private ArrayList<ObjArrow> mLines = new ArrayList<ObjArrow>();
	private DrawMapActivity mContext;
	private RelativeLayout mParent;
	private MySurfaceThread thread;
	private MapObject mSelectedObject;
	private PointF first = new PointF();
	public TProcessDataBaseHandler mProcessDataBaseHandler;

	public StreamMapSurfaceView(DrawMapActivity context, RelativeLayout parent) {
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
		setActivityParent(context);
		mParent = parent;
		mProcessDataBaseHandler = new TProcessDataBaseHandler(mContext);
		// initMap();
	}

	public void initMap() {
		// add process box: add operators && add eye grass
		TProcessDataBase mTProcessDataBase = mContext.mTProcessDataBase;
		if (mTProcessDataBase != null) {
			addMapObject(new ObjProcessBox(mContext,
					mContext.getWidth() / 2 - 60, 20, mTProcessDataBase));
			addMapObject(new ObjCustomer(mContext, 40, 20,
					mTProcessDataBase.getCustomerEndPoint()));
			addMapObject(new ObjSupplier(mContext, mContext.getWidth() - 240,
					20, mTProcessDataBase.getSupplierStartPoint()));
			// addMapObject(new ObjBurst(mContext, 400, 300));
			// addMapObject(new ObjInventory(mContext, 500, 330));
			ObjSingleArrow mArrowCustomer = new ObjSingleArrow(mContext,
					mMapObjects.get(0), mMapObjects.get(1),
					mTProcessDataBase.getCommunication());
			mLines.add(mArrowCustomer);
			mParent.addView(mArrowCustomer.mLayout);
			ObjSingleArrow mArrowSupplier = new ObjSingleArrow(mContext,
					mMapObjects.get(0), mMapObjects.get(2),
					mTProcessDataBase.getCommunication());
			mLines.add(mArrowSupplier);
			mParent.addView(mArrowSupplier.mLayout);
			int mFirstProcessId = -1;
			try {
				mFirstProcessId = Integer.parseInt(mTProcessDataBase
						.getPreviousProcess());
			} catch (Exception e) {
				// TODO: handle exception
				mFirstProcessId = -1;
			}
			if (mFirstProcessId > 0) {
				TProcessDataBase mTProcessDataBaseFirst = mProcessDataBaseHandler
						.getProcess(mFirstProcessId);
				addMapObject(new ObjProcessBox(mContext,
						mContext.getWidth() / 2 - 200,
						mContext.getHeight() - 160, mTProcessDataBaseFirst));

				mLines.add(new ObjBigArrow(mContext, mMapObjects.get(1),
						mMapObjects.get(3)));
				mLines.add(new ObjSingleArrow(mContext, mMapObjects.get(0),
						mMapObjects.get(3), "Previous"));
			}
			int mNextProcessId = -1;
			try {
				mNextProcessId = Integer.parseInt(mTProcessDataBase
						.getNextProcess());
			} catch (Exception e) {
				// TODO: handle exception
				mNextProcessId = -1;
			}
			if (mNextProcessId > 0) {
				TProcessDataBase mTProcessDataBaseNext = mProcessDataBaseHandler
						.getProcess(mFirstProcessId);
				addMapObject(new ObjProcessBox(mContext,
						mContext.getWidth() / 2 + 20,
						mContext.getHeight() - 160, mTProcessDataBaseNext));

				mLines.add(new ObjBigArrow(mContext, mMapObjects.get(4),
						mMapObjects.get(2)));
				mLines.add(new ObjSingleArrow(mContext, mMapObjects.get(0),
						mMapObjects.get(4), "Next"));
				addMapObject(new ObjBurst(mContext, mContext.getWidth() - 160, 180));
				addMapObject(new ObjInventory(mContext, 500, 300));
			}
			mParent.addView(new ObjTaktTimeBox(mContext,
					mContext.getWidth() - 160, 40).getLayout());

		}
		// add customer
		// add supplier
		// add big arrow
		// add arrow

	}

	public void addMapObject(MapObject pObject) {
		mMapObjects.add(pObject);
		mParent.addView(pObject.getLayout());
	}

	public void removeMapObject(MapObject pObject) {
		mMapObjects.remove(pObject);
		mParent.removeView(pObject.getLayout());
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
		// if (mLineObject != null)
		// mLineObject.drawObjects(cv);
		for (int i = 0; i < mLines.size(); i++) {
			mLines.get(i).renderArrow(cv);
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
		first.x = e.getX();
		first.y = e.getY();
		return false;
	}

	@Override
	public boolean onTouchMove(MotionEvent e) {
		// TODO Auto-generated method stub
		if (mSelectedObject != null) {
			mSelectedObject.move(e.getX() - first.x, e.getY() - first.y);
			first.x = e.getX();
			first.y = e.getY();
		}
		return true;
	}

	@Override
	public boolean onTouchUp(MotionEvent e) {
		// TODO Auto-generated method stub
		mSelectedObject = null;
		return false;
	}

	public void setActivityParent(DrawMapActivity context) {
		this.mContext = context;
	}

	public Context getContextParent() {
		return mContext;
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