/*
 *Copyright 2011 Matthieu Paret
 *
 *This file is part of DragAndDrop.
 *
 *DragAndDrop is free software: you can redistribute it and/or modify
 *it under the terms of the GNU Lesser General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *DragAndDrop is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU Lesser General Public License
 *along with DragAndDrop.  If not, see <http://www.gnu.org/licenses/>.
 */

package exp.mtparet.dragdrop.view;

import exp.mtparet.dragdrop.view.DndListViewFolder.DragListener;
import exp.mtparet.dragdrop.view.DndListViewFolder.DropListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Extends ListView, implement some additionnal listener
 * 
 * @author Matthieu Paret
 * 
 */
public class ListViewDragDrop extends ListView {
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

	private OnTouchListener mOnItemOutUpListener;
	private OnTouchListener mOnItemMoveListener; // It is an hacked touchLister,
													// in fact it is
													// OnMoveListener
	private OnItemClickListener mOnItemReceiver;
	private boolean isMove = false;
	private View childSelected;
	private float xInit;
	private float yInit;

	public ListViewDragDrop(Context context) {
		super(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();// etScaledTouchSlop();
		totalchilds = getChildCount();
	}

	public ListViewDragDrop(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTouchSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();// etScaledTouchSlop();
		totalchilds = getChildCount();
	}

	public ListViewDragDrop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mTouchSlop = ViewConfiguration.get(context).getScaledWindowTouchSlop();// etScaledTouchSlop();
		totalchilds = getChildCount();
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

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean handled = false;

		if (mOnItemMoveListener != null && !handled)
			handled = onMove(ev);

		if (!handled)
			return super.onTouchEvent(ev);

		return handled;
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
				int left = child.getLeft() + this.getLeft() - 100;/* edit here */
				int right = child.getRight() + this.getRight();
				int top = child.getTop() + this.getTop();
				int bottom = child.getTop() + child.getHeight() + this.getTop();
				viewRect.set(left, top, right, bottom);
				Log.e("left " + left + " top " + top, "bottom " + bottom
						+ "  right " + right + "x	" + x + "y	" + y);
				if (viewRect.contains(x, y)) {
					if (getOnItemSelectedListener() != null) {
						getOnItemSelectedListener().onItemSelected(
								ListViewDragDrop.this, child, i,
								getItemIdAtPosition(i));
					}
					if (mOnItemReceiver != null) {
						mOnItemReceiver.onItemClick(ListViewDragDrop.this,
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
								ListViewDragDrop.this, child, i,
								getItemIdAtPosition(i));
					}
					if (mOnItemReceiver != null) {
						mOnItemReceiver.onItemClick(ListViewDragDrop.this,
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
						mOnItemReceiver.onItemClick(ListViewDragDrop.this,
								null, 0, 0);
					} else {
						if (y > maxY) {
							mOnItemReceiver.onItemClick(ListViewDragDrop.this,
									null, this.getChildCount(), 0);
						}
					}

					return true;

				} else {

					if (mOnItemReceiver != null) {
						mOnItemReceiver.onItemClick(ListViewDragDrop.this,
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
								ListViewDragDrop.this, child, i,
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
						mOnItemMoveListener.onTouch(ListViewDragDrop.this,
								event);
					}

					isMove = true;
					return true;
				}

			} else {
				mOnItemMoveListener.onTouch(ListViewDragDrop.this, event);

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
				mOnItemMoveListener.onTouch(ListViewDragDrop.this, event);
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

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		Log.e("first", "run here");
		if (getChildCount() > 0)
			totalchilds = getChildCount();

		if (mDragListener != null || mDropListener != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				int x = (int) ev.getX();
				int y = (int) ev.getY();
				Log.e("this.getWidth " + this.getWidth(), "afd");

				if (x > this.getWidth() - 75) {
					return false;
				}

				int itemnum = pointToPosition(x, y);
				System.out.println("Item num :" + itemnum);
				if (itemnum == AdapterView.INVALID_POSITION) {
					break;
				}
				View item = (View) getChildAt(itemnum
						- getFirstVisiblePosition());
				mItemHeightNormal = item.getHeight();
				mItemHeightExpanded = mItemHeightNormal * 2;
				mDragPoint = y - item.getTop();
				mCoordOffset = ((int) ev.getRawY()) - y;
				View dragger = item.findViewById(dndViewId);
				if (dragger == null)
					dragger = item;
				Rect r = mTempRect;
				Log.e("r.right", "r.right " + r.right);
				dragger.getDrawingRect(r);
				if (x < r.right * 2) {
					item.setDrawingCacheEnabled(true);
					Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
					startDragging(bitmap, y);
					mDragPos = itemnum;
					mFirstDragPos = mDragPos;
					mHeight = getHeight();
					int touchSlop = mTouchSlop;
					mUpperBound = Math.min(y - touchSlop, mHeight / 3);
					mLowerBound = Math.max(y + touchSlop, mHeight * 2 / 3);
					return false;
				}
				mDragView = null;
				break;
			}
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void startDragging(Bitmap bm, int y) {
		stopDragging();

		mWindowParams = new WindowManager.LayoutParams();
		mWindowParams.gravity = Gravity.TOP;
		mWindowParams.x = -150;
		Log.e("dragx", "dragx " + dragImageX);
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
		Log.e("stop draging", "stop draging");
		if (mDragView != null) {
			WindowManager wm = (WindowManager) mContext.getSystemService("window");
			wm.removeView(mDragView);
			mDragView.setImageDrawable(null);
			mDragView = null;
		}
		if (mDragBitmap != null) {
			mDragBitmap.recycle();
			mDragBitmap = null;
		}
	}
}
