package exp.mtparet.dragdrop.view;

import visvateam.outsource.idmanager.database.IdManagerPreference;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class DndListViewFolder extends ListView {
	private Context mContext;
	private ImageView mDragView;
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowParams;
	private int mDragPos;
	private int mFirstDragPos;
	private int mDragPoint;
	private int mCoordOffset;
	private DragListener mDragListener;
	private DropListener mDropListener;
	private int mUpperBound;
	private int mLowerBound;
	private int mHeight;
	private Rect mTempRect = new Rect();
	private Bitmap mDragBitmap;
	private final int mTouchSlop;
	private int mItemHeightNormal;
	private int mItemHeightExpanded;
	private int dndViewId;
	private int dragImageX = 0;
	private int totalchilds = 0;

	// on move and receiver item
	private OnTouchListener mOnItemOutUpListener;
	private OnTouchListener mOnItemMoveListener;
	private OnItemClickListener mOnItemReceiver;
	private boolean isMove = false;
	private View childSelected;
	private float xInit;
	private float yInit;

	private IdManagerPreference mIdManagerPreference;

	public DndListViewFolder(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mTouchSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();// etScaledTouchSlop();
		totalchilds = getChildCount();
		mIdManagerPreference = IdManagerPreference.getInstance(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (getChildCount() > 0)
			totalchilds = getChildCount();

		if (mDragListener != null || mDropListener != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				int x = (int) ev.getX();
				int y = (int) ev.getY();

				if (x > this.getWidth() - 70) {
					Rect r = mTempRect;
					if (mDragView != null)
						mDragView.getDrawingRect(r);
					stopDragging();
					if (mDropListener != null && mDragPos >= 0
							&& mDragPos < getCount()) {
						mDropListener.drop(mFirstDragPos, mDragPos);
						if (mDragPos < (totalchilds - 1))
							setSelectionFromTop(0, 0);
					}
					unExpandViews(false);
					return false;
				}

				int itemnum = pointToPosition(x, y);
				System.out.println("Item num :" + itemnum);
				if (itemnum == AdapterView.INVALID_POSITION) {
					break;
				}
				View item = (View) getChildAt(itemnum
						- getFirstVisiblePosition());
				// item.setBackgroundColor(Color.RED);
				mItemHeightNormal = item.getHeight();
				mItemHeightExpanded = mItemHeightNormal * 2;
				mDragPoint = y - item.getTop();
				mCoordOffset = ((int) ev.getRawY()) - y;
				View dragger = item.findViewById(dndViewId);
				if (dragger == null)
					dragger = item;
				Rect r = mTempRect;
				dragger.getDrawingRect(r);
				if (x < 70) {
					item.setDrawingCacheEnabled(true);
					Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
					if (!mIdManagerPreference.isEditMode())
						startDragging(bitmap, y);
					mDragPos = itemnum;
					mFirstDragPos = mDragPos;
					mHeight = getHeight();
					int touchSlop = mTouchSlop;
					mUpperBound = Math.min(y - touchSlop, mHeight / 3);
					mLowerBound = Math.max(y + touchSlop, mHeight * 2 / 3);
					return false;
				} else {
					if (mDragView != null)
						mDragView.getDrawingRect(r);
					stopDragging();
					if (mDropListener != null && mDragPos >= 0
							&& mDragPos < getCount()) {
						mDropListener.drop(mFirstDragPos, mDragPos);
						if (mDragPos < (totalchilds - 1))
							setSelectionFromTop(0, 0);
					}
					unExpandViews(false);
				}
				mDragView = null;
				break;
			case MotionEvent.ACTION_UP:
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	public void redraw(int pos) {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (i == pos)
				v.setBackgroundColor(Color.parseColor("#E0E0E0"));
			else
				v.setBackgroundColor(Color.WHITE);

		}
	}

	public void refresh() {
		long time = Long.parseLong(("" + System.nanoTime()).substring(0, 7));
		MotionEvent me = MotionEvent.obtain(time, time,
				MotionEvent.ACTION_DOWN, getWidth() - 10, 300, 0);
		onInterceptTouchEvent(me);
		stopDragging();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean handled = false;
		if (getChildCount() > 0)
			totalchilds = getChildCount();

		if ((mDragListener != null || mDropListener != null)
				&& mDragView != null) {
			int action = ev.getAction();
			Rect r;
			switch (action) {
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				r = mTempRect;
				mDragView.getDrawingRect(r);
				stopDragging();
				int y = (int) ev.getY();
				int speed = 0;
				if (mDropListener != null && mDragPos >= 0
						&& mDragPos < getCount()) {
					mDropListener.drop(mFirstDragPos, mDragPos);

					if (mDragPos < (totalchilds - 1))
						setSelectionFromTop(0, 0);
				}
				unExpandViews(false);
				break;

			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				int x = (int) ev.getX();
				y = (int) ev.getY();

				if (x > 70) {
					r = mTempRect;
					if (mDragView != null)
						mDragView.getDrawingRect(r);
					stopDragging();
					if (mDropListener != null && mDragPos >= 0
							&& mDragPos < getCount()) {
						mDropListener.drop(mFirstDragPos, mDragPos);
						if (mDragPos < (totalchilds - 1))
							setSelectionFromTop(0, 0);
					}
					unExpandViews(false);
					return false;
				}

				dragView(x, y);
				int itemnum = getItemForPosition(y);
				if (itemnum >= 0) {
					if (action == MotionEvent.ACTION_DOWN
							|| itemnum != mDragPos) {
						if (mDragListener != null) {
							mDragListener.drag(mDragPos, itemnum);
						}
						mDragPos = itemnum;
						doExpansion();
					}
					speed = 0;
					adjustScrollBounds(y);
					if (y > mLowerBound) {

						speed = y > (mHeight + mLowerBound) / 2 ? 16 : 4;
					} else if (y < mUpperBound) {

						speed = y < mUpperBound / 2 ? -16 : -4;
					}
					if (speed != 0) {
						int ref = pointToPosition(0, mHeight / 2);
						if (ref == AdapterView.INVALID_POSITION) {
							// we hit a divider or an invisible view, check
							// somewhere else
							ref = pointToPosition(0, mHeight / 2
									+ getDividerHeight() + 64);
						}
						View v = getChildAt(ref - getFirstVisiblePosition());
						if (v != null) {
							int pos = v.getTop();
							setSelectionFromTop(ref, pos - speed);
						}
					}
				}
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	private int getItemForPosition(int y) {
		int adjustedy = y - mDragPoint - 32;
		int pos = myPointToPosition(0, adjustedy);
		if (pos >= 0) {
			if (pos <= mFirstDragPos) {
				pos += 1;
			}
		} else if (adjustedy < 0) {
			pos = 0;
		}
		return pos;
	}

	private int myPointToPosition(int x, int y) {
		Rect frame = mTempRect;
		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			final View child = getChildAt(i);
			child.getHitRect(frame);
			if (frame.contains(x, y)) {
				return getFirstVisiblePosition() + i;
			}
		}
		return INVALID_POSITION;
	}

	private void adjustScrollBounds(int y) {
		if (y >= mHeight / 3) {
			mUpperBound = mHeight / 3;
		}
		if (y <= mHeight * 2 / 3) {
			mLowerBound = mHeight * 2 / 3;
		}
	}

	private void doExpansion() {
		int childnum = mDragPos - getFirstVisiblePosition();
		if (mDragPos > mFirstDragPos) {
			childnum++;
		}

		View first = getChildAt(mFirstDragPos - getFirstVisiblePosition());
		for (int i = 0;; i++) {
			View vv = getChildAt(i);
			if (vv == null) {
				break;
			}
			int height = mItemHeightNormal;
			int visibility = View.VISIBLE;
			if (vv.equals(first)) {

				if (mDragPos == mFirstDragPos) {
					visibility = View.INVISIBLE;
				} else {
					height = 1;
				}
			} else if (i == childnum) {
				System.out.print("I :" + i);
				if (mDragPos < getCount() - 1) {
					height = mItemHeightExpanded;
				}
			}
			ViewGroup.LayoutParams params = vv.getLayoutParams();
			params.height = height;
			vv.setLayoutParams(params);
			vv.setVisibility(visibility);
		}
	}

	private void unExpandViews(boolean deletion) {
		for (int i = 0;; i++) {
			View v = getChildAt(i);
			if (v == null) {
				if (deletion) {
					int position = getFirstVisiblePosition();
					int y = getChildAt(0).getTop();
					setAdapter(getAdapter());
					setSelectionFromTop(position, y);
				}
				layoutChildren();
				v = getChildAt(i);
				if (v == null) {
					break;
				}
			}
			ViewGroup.LayoutParams params = v.getLayoutParams();
			params.height = mItemHeightNormal;
			v.setLayoutParams(params);
			v.setVisibility(View.VISIBLE);
		}
	}

	public void checkfordrop(int dragPos) {
		if (dragPos < totalchilds - 1)
			setSelectionFromTop(0, 0);
	}

	private void startDragging(Bitmap bm, int y) {
		stopDragging();

		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP;
		mWindowParams.x = -160;
		mWindowParams.y = y - mDragPoint + mCoordOffset;

		mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
		mWindowParams.format = PixelFormat.TRANSPARENT;
		mWindowParams.windowAnimations = 0;

		ImageView v = new ImageView(mContext);
		int backGroundColor = Color.parseColor("#e0103010");
		v.setBackgroundColor(backGroundColor);
		v.setImageBitmap(bm);
		mDragBitmap = bm;

		mWindowManager = (WindowManager) mContext.getSystemService("window");
		mWindowManager.addView(v, mWindowParams);
		mDragView = v;
	}

	private void dragView(int x, int y) {
		mWindowParams.y = y - mDragPoint + mCoordOffset;
		mWindowManager.updateViewLayout(mDragView, mWindowParams);
	}

	private void stopDragging() {
		if (mDragView != null) {
			WindowManager wm = (WindowManager) mContext
					.getSystemService("window");
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
	}

	public void setDragListener(DragListener l) {
		mDragListener = l;
	}

	public void setDropListener(DropListener l) {
		mDropListener = l;
	}

	public void setDndView(int id) {
		dndViewId = id;
	}

	public void setDragImageX(int x) {
		dragImageX = x;
	}

	public interface DragListener {
		void drag(int from, int to);
	}

	public interface DropListener {
		void drop(int from, int to);
	}

	/**
	 * When an item is moved horizontally out of this position, this listener is
	 * called. Before this OnItemSelectedListener is called.
	 * 
	 * @param listener
	 */
	public void setOnItemMoveListener(AdapterView.OnTouchListener listener) {
		mOnItemMoveListener = listener;
	}

	/**
	 * When a gesture on a item is terminated out of the the listView
	 * 
	 * @param listener
	 */
	public void setOnItemUpOutListener(AdapterView.OnTouchListener listener) {
		this.mOnItemOutUpListener = listener;
	}

	/**
	 * When an outsider item is moved and up on this listview. Return position
	 * where to add this item.
	 * 
	 * @param listener
	 */
	public void setOnItemReceiverListener(
			AdapterView.OnItemClickListener listener) {
		this.mOnItemReceiver = listener;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean onUpReceive(MotionEvent e) {

		if (e.getAction() == MotionEvent.ACTION_UP) {

			int x = (int) e.getX();
			int y = (int) e.getY();

			for (int i = 0; i < getChildCount(); i++) {
				Rect viewRect = new Rect();
				View child = getChildAt(i);
				int left = child.getLeft() + this.getLeft() - 150;
				int right = child.getRight() + this.getLeft() - 150;
				int top = child.getTop() + this.getTop();
				int bottom = child.getTop() + child.getHeight() / 2
						+ this.getTop();
				viewRect.set(left, top, right, bottom);

				if (viewRect.contains(x, y)) {
					if (getOnItemSelectedListener() != null) {
						getOnItemSelectedListener().onItemSelected(
								DndListViewFolder.this, child, i,
								getItemIdAtPosition(i));
					}
					if (mOnItemReceiver != null) {
						mOnItemReceiver.onItemClick(DndListViewFolder.this,
								child, i, getItemIdAtPosition(i));
					}
					return true;
				}

				Rect viewRect2 = new Rect();
				left = child.getLeft() + this.getLeft();
				right = child.getRight() + this.getLeft();
				top = child.getTop() + child.getHeight() / 2 + this.getTop();
				bottom = child.getBottom() + this.getTop();
				viewRect2.set(left, top, right, bottom);

				if (viewRect2.contains(x, y)) {
					if (getOnItemSelectedListener() != null) {
						getOnItemSelectedListener().onItemSelected(
								DndListViewFolder.this, child, i,
								getItemIdAtPosition(i));
					}
					if (mOnItemReceiver != null) {
						mOnItemReceiver.onItemClick(DndListViewFolder.this,
								child, i + 1, getItemIdAtPosition(i));
					}
					return true;
				}

			}

			int left = this.getLeft();
			int right = this.getRight();
			int top = this.getTop();
			int bottom = this.getBottom();
			Rect rect = new Rect(left, top, right, bottom);

			if (rect.contains(x, y)) {
				if (this.getChildCount() > 0) {
					int minY = this.getChildAt(this.getChildCount() - 1)
							.getBottom();
					int maxY = this.getChildAt(0).getTop();

					if (y < minY) {
						mOnItemReceiver.onItemClick(DndListViewFolder.this,
								null, 0, 0);
					} else {
						if (y > maxY) {
							mOnItemReceiver.onItemClick(DndListViewFolder.this,
									null, this.getChildCount(), 0);
						}

					}

					return true;

				} else {

					if (mOnItemReceiver != null) {
						mOnItemReceiver.onItemClick(DndListViewFolder.this,
								null, 0, 0);
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean onMove(MotionEvent event) {

		float xNow = event.getX();
		float yNow = event.getY();

		Rect viewRect = new Rect();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float xInit1 = event.getX();
			float yInit1 = event.getY();

			for (int i = 0; i < getChildCount(); i++) {
				View child = getChildAt(i);
				int left = child.getLeft();
				int right = child.getRight();
				int top = child.getTop();
				int bottom = child.getBottom();
				viewRect.set(left, top, right, bottom);
				if (viewRect.contains((int) xInit1, (int) yInit1)) {

					if (getOnItemSelectedListener() != null) {
						getOnItemSelectedListener().onItemSelected(
								DndListViewFolder.this, child, i,
								getItemIdAtPosition(i));
					}

					this.xInit = xInit1;
					this.yInit = yInit1;

					childSelected = child;

				}
			}
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			if (!isMove) {

				if (Math.abs(xNow - xInit) > 0 && Math.abs(yNow - yInit) < 4) {

					if (mOnItemMoveListener != null) {
						mOnItemMoveListener.onTouch(DndListViewFolder.this,
								event);
					}

					isMove = true;
					return true;

				}

			} else {
				mOnItemMoveListener.onTouch(DndListViewFolder.this, event);

				return true;
			}
		}

		if (event.getAction() == MotionEvent.ACTION_UP && isMove) {
			int left = this.getLeft();
			int right = this.getRight();
			int top = this.getTop();
			int bottom = this.getBottom();
			Rect rect = new Rect(left, top, right, bottom);

			if (mOnItemMoveListener != null) {
				mOnItemMoveListener.onTouch(DndListViewFolder.this, event);
			}

			if (!rect.contains((int) xNow, (int) yNow)) {

				if (mOnItemOutUpListener != null) {
					mOnItemOutUpListener.onTouch(this.childSelected, event);
				}

			}

			isMove = false;
			return false;
		}

		return false;
	}

}
