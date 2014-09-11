//TO DO:
//
// - improve timer performance (especially on Eee Pad)
// - improve child rearranging

package com.fgsecure.ujoolt.app.widget;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fgsecure.ujoolt.app.screen.MainScreenActivity;

public class DraggableView extends LinearLayout {
	// layout vars
	public static float childRatio = .9f;
	public int colCount, childSize, padding, dpi, scroll = 0;

	public static final int VERTICAL = 0;
	public static final int HORIZONAL = 1;

	protected float lastDelta = 0;
	public static final int ACCELERATION = 5;
	public Handler handler;
	// dragging vars
	protected int dragged = -1, lastX = -1, lastY = -1, lastTarget = -1;
	public boolean enabled = true, touching = false;
	// anim vars
	public static int animT = 150;
	protected ArrayList<Integer> newPositions = new ArrayList<Integer>();
	// listeners
	protected OnRearrangeListener onRearrangeListener;
	protected OnClickListener secondaryOnClickListener;
	public boolean isActive, isStarted;
	public float a = 4f;
	public float t = 0f;
	public boolean isMoveUp, isMoveDown, isMove, isMoveLeft, isMoveRight;
	public MainScreenActivity mainScreenActivity;
	public LinearLayout layout_jolt_screen;
	public int top_max = -1000;
	public int bottom_max = -1000;
	public int left_max = -1000;
	public int right_max = -1000;
	public int defaultPosition;
	public int disappearPoint;
	public int hiddenPoint;
	public int typeAction;
	public int type;
	public int orientation;

	public static int JOLT = 0;
	public static int SETTING = 1;
	public static int INFORMATION = 2;
	public static int LOGIN = 3;
	public static int TAKE_PHOTO = 4;
	public static int OPTIONS = 5;
	public static int SEARCH = 6;
	public static int REGISTER = 7;
	public static int SEARCH_ITEM = 8;

	public static int width, height;

	public boolean isRegister, isJolt;
	public boolean isQuitRegister, isStartJolt;

	// CONSTRUCTOR AND HELPERS
	public DraggableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// setListeners();
		// Log.e("asd", "asdas");
		// removeThread();
		// setChildrenDrawingOrderEnabled(true);
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		dpi = metrics.densityDpi;
	}

	public void removeThread() {
		handler = new Handler();
		handler.removeCallbacks(updateTask);
		handler.postAtTime(updateTask, SystemClock.uptimeMillis() + 500);
	}

	public static void setWH(int w, int h) {
		width = w;
		height = h;
	}

	public void setListeners() {
		// setOnTouchListener(this);
		// super.setOnClickListener(this);
		// setOnLongClickListener(this);
	}

	public void removeListener() {
		setOnTouchListener(null);
		super.setOnClickListener(null);
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setMainActivity(MainScreenActivity mainScreenActivity) {
		this.mainScreenActivity = mainScreenActivity;

	}

	// @Override
	// public void setOnClickListener(OnClickListener l) {
	// secondaryOnClickListener = l;
	// }

	public void setMaxTopBottom(int top, int bottom) {
		top_max = top;
		bottom_max = bottom;
	}

	public void setMaxLeftRight(int left, int right) {
		left_max = left;
		right_max = right;
	}

	public Runnable updateTask = new Runnable() {
		public void run() {

			// Log.e("isStart " + isStarted, "touching " + touching);
			if (!mainScreenActivity.isQuitRegister && !mainScreenActivity.isStartJolt) {
				if (!isStarted && !touching) {
					if (type == JOLT || type == LOGIN || type == TAKE_PHOTO || type == SEARCH
							|| type == REGISTER) {
						scroll = bottom_max;
					} else if (type == SETTING || type == OPTIONS) {
						scroll = top_max;
					} else if (type == INFORMATION) {
						scroll = hiddenPoint;
					} else if (type == SEARCH_ITEM) {
						scroll = right_max;
					}
					// // Log.e("chay", "not start");
					onLayout(true, getLeft(), getTop(), getRight(), getBottom());
				} else {

					if (!touching) {
						clampScroll();
						onLayout(true, getLeft(), getTop(), getRight(), getBottom());
					}

				}
			} else {
				// Log.e("scroll " + type, "" + scroll);
				// if (MainScreenActivity.isQuitRegister) {
				// if (type == JOLT) {
				// scroll = 900;
				// }
				// }
				// Log.e("scroll " + type, "" + scroll);
				if (mainScreenActivity.isQuitRegister && type == LOGIN) {
					clampScroll();
					onLayout(true, getLeft(), getTop(), getRight(), getBottom());
				} else if (mainScreenActivity.isStartJolt && type == JOLT) {
					clampScroll();
					onLayout(true, getLeft(), getTop(), getRight(), getBottom());
				}
			}

			handler.postDelayed(this, 25);
		}

	};

	@Override
	public void addView(View child) {
		super.addView(child);
		newPositions.add(-1);
	};

	@Override
	public void removeViewAt(int index) {
		super.removeViewAt(index);
		newPositions.remove(index);
	};

	public void stop() {
		isStarted = false;
		handler.removeCallbacks(updateTask);
		updateTask = null;
	}

	// LAYOUT
	// @Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		// compute width of view, in dp
		// float w = (r - l) / (dpi / 160f);

		// determine number of columns, at least 2
		// colCount = 2;
		// int sub = 240;
		// w -= 280;
		// while (w > 0) {
		// colCount++;
		// w -= sub;
		// sub += 40;
		// }
		// colCount = 1;
		// childSize = 360;
		// // padding = 0;
		// for (int i = 0; i < getChildCount(); i++)
		// if (i != dragged) {
		// Point xy = getCoorFromIndex(i);
		if (orientation == VERTICAL) {
			getChildAt(0).layout(0, -scroll, width, -scroll + height);
		} else {
			getChildAt(0).layout(scroll, 0, -scroll + width, height);
		}
		// }
	}

	// @Override
	// protected int getChildDrawingOrder(int childCount, int i) {
	// if (dragged == -1)
	// return i;
	// else if (i == childCount - 1)
	// return dragged;
	// else if (i >= dragged)
	// return i + 1;
	// return i;
	// }
	public int getIndexFromCoor(int x, int y) {
		int col = getColOrRowFromCoor(x), row = getColOrRowFromCoor(y + scroll);
		if (col == -1 || row == -1) // touch is between columns or rows
			return -1;
		int index = row * colCount + col;
		if (index >= getChildCount())
			return -1;
		return index;
	}

	protected int getColOrRowFromCoor(int coor) {
		coor -= padding;
		for (int i = 0; coor > 0; i++) {
			if (coor < childSize)
				return i;
			coor -= (childSize + padding);
		}
		return -1;
	}

	protected int getTargetFromCoor(int x, int y) {
		if (getColOrRowFromCoor(y + scroll) == -1) // touch is between rows
			return -1;
		// if (getIndexFromCoor(x, y) != -1) //touch on top of another visual
		// return -1;

		int leftPos = getIndexFromCoor(x - (childSize / 4), y);
		int rightPos = getIndexFromCoor(x + (childSize / 4), y);
		if (leftPos == -1 && rightPos == -1) // touch is in the middle of
												// nowhere
			return -1;
		if (leftPos == rightPos) // touch is in the middle of a visual
			return -1;

		int target = -1;
		if (rightPos > -1)
			target = rightPos;
		else if (leftPos > -1)
			target = leftPos + 1;
		if (dragged < target)
			return target - 1;

		// Toast.makeText(getContext(), "Target: " + target + ".",
		// Toast.LENGTH_SHORT).show();
		return target;
	}

	protected Point getCoorFromIndex(int index) {
		int col = index % colCount;
		int row = index / colCount;
		return new Point(padding + (childSize + padding) * col, padding + (childSize + padding)
				* row - scroll);
	}

	public int getIndexOf(View child) {
		for (int i = 0; i < getChildCount(); i++)
			if (getChildAt(i) == child)
				return i;
		return -1;
	}

	public boolean onTouch(View view, MotionEvent event) {
		int action = event.getAction();
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			enabled = true;
			lastX = (int) event.getX();
			lastY = (int) event.getY();
			touching = true;
			break;
		case MotionEvent.ACTION_MOVE:

			touching = true;
			isMove = true;
			if (!isStarted) {
				isStarted = true;
			}
			if (!isActive) {
				isActive = true;
			}
			int delta = lastY - (int) event.getY();
			if (dragged != -1) {
				// change draw location of dragged visual
				int x = (int) event.getX(), y = (int) event.getY();
				int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
				getChildAt(dragged).layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));

				// check for new target hover
				int target = getTargetFromCoor(x, y);
				if (lastTarget != target) {
					if (target != -1) {
						animateGap(target);
						lastTarget = target;
					}
				}
			} else {
				scroll += delta;
				// clampScroll();
				if (Math.abs(delta) > 2)
					enabled = false;
				onLayout(true, getLeft(), getTop(), getRight(), getBottom());
			}
			lastX = (int) event.getX();
			lastY = (int) event.getY();
			lastDelta = delta;

			break;
		case MotionEvent.ACTION_UP:

			if (dragged != -1) {
				View v = getChildAt(dragged);
				if (lastTarget != -1)
					reorderChildren();
				else {
					Point xy = getCoorFromIndex(dragged);
					v.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize);
				}
				v.clearAnimation();
				if (v instanceof ImageView)
					((ImageView) v).setAlpha(255);
				lastTarget = -1;
				dragged = -1;
			}
			touching = false;
			isMove = false;
			if (lastDelta > 0) {
				isMoveUp = true;
				isMoveDown = false;
			} else if (lastDelta < 0) {
				isMoveDown = true;
				isMoveUp = false;
			}
			break;
		}
		if (dragged != -1)
			return true;
		return false;
	}

	public void move(int move, int xTouch, int yTouch) {
		switch (move & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			enabled = true;
			lastX = xTouch;
			lastY = yTouch;
			touching = true;
			break;
		case MotionEvent.ACTION_MOVE:
			isMove = true;
			if (type == INFORMATION) {
				if (!isStarted) {
					isStarted = true;
				}
			}
			int delta = lastY - yTouch;
			if (dragged != -1) {
				// change draw location of dragged visual
				int x = xTouch, y = yTouch;
				int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
				getChildAt(dragged).layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));

				// check for new target hover
				int target = getTargetFromCoor(x, y);
				if (lastTarget != target) {
					if (target != -1) {
						animateGap(target);
						lastTarget = target;
					}
				}
			} else {
				scroll += delta;
				// clampScroll();
				if (scroll > top_max) {
					scroll = top_max;
				}
				if (scroll < bottom_max) {
					scroll = bottom_max;
				}
				if (Math.abs(delta) > 2)
					enabled = false;
				onLayout(true, getLeft(), getTop(), getRight(), getBottom());
			}
			lastX = xTouch;
			lastY = yTouch;
			lastDelta = delta;

			break;
		case MotionEvent.ACTION_UP:

			if (dragged != -1) {
				View v = getChildAt(dragged);
				if (lastTarget != -1)
					reorderChildren();
				else {
					Point xy = getCoorFromIndex(dragged);
					v.layout(xy.x, xy.y, xy.x + childSize, xy.y + childSize);
				}
				v.clearAnimation();
				if (v instanceof ImageView)
					((ImageView) v).setAlpha(255);
				lastTarget = -1;
				dragged = -1;
			}
			touching = false;
			isMove = false;
			if (lastDelta >= 0) {
				isMoveUp = true;
				isMoveDown = false;
			} else if (lastDelta < 0) {
				isMoveDown = true;
				isMoveUp = false;
			}
			break;
		}
	}

	// EVENT HELPERS
	protected void animateDragged() {

		View v = getChildAt(dragged);
		int x = getCoorFromIndex(dragged).x + childSize / 2, y = getCoorFromIndex(dragged).y
				+ childSize / 2;
		int l = x - (3 * childSize / 4), t = y - (3 * childSize / 4);
		v.layout(l, t, l + (childSize * 3 / 2), t + (childSize * 3 / 2));
		AnimationSet animSet = new AnimationSet(true);
		ScaleAnimation scale = new ScaleAnimation(.667f, 1, .667f, 1, childSize * 3 / 4,
				childSize * 3 / 4);
		scale.setDuration(animT);
		AlphaAnimation alpha = new AlphaAnimation(1, .5f);
		alpha.setDuration(animT);

		animSet.addAnimation(scale);
		animSet.addAnimation(alpha);
		animSet.setFillEnabled(true);
		animSet.setFillAfter(true);

		v.clearAnimation();
		v.startAnimation(animSet);
	}

	protected void animateGap(int target) {
		Log.e("vao trong day", ":9");
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (i == dragged)
				continue;
			int newPos = i;
			if (dragged < target && i >= dragged + 1 && i <= target)
				newPos--;
			else if (target < dragged && i >= target && i < dragged)
				newPos++;

			// animate
			int oldPos = i;
			if (newPositions.get(i) != -1)
				oldPos = newPositions.get(i);
			if (oldPos == newPos)
				continue;

			Point oldXY = getCoorFromIndex(oldPos);
			Point newXY = getCoorFromIndex(newPos);
			Point oldOffset = new Point(oldXY.x - v.getLeft(), oldXY.y - v.getTop());
			Point newOffset = new Point(newXY.x - v.getLeft(), newXY.y - v.getTop());

			TranslateAnimation translate = new TranslateAnimation(Animation.ABSOLUTE, oldOffset.x,
					Animation.ABSOLUTE, newOffset.x, Animation.ABSOLUTE, oldOffset.y,
					Animation.ABSOLUTE, newOffset.y);
			translate.setDuration(animT);
			translate.setFillEnabled(true);
			translate.setFillAfter(true);
			v.clearAnimation();
			v.startAnimation(translate);

			newPositions.set(i, newPos);
		}
	}

	protected void reorderChildren() {
		// FIGURE OUT HOW TO REORDER CHILDREN WITHOUT REMOVING THEM ALL AND
		// RECONSTRUCTING THE LIST!!!
		if (onRearrangeListener != null)
			onRearrangeListener.onRearrange(dragged, lastTarget);
		ArrayList<View> children = new ArrayList<View>();
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).clearAnimation();
			children.add(getChildAt(i));
		}
		removeAllViews();
		while (dragged != lastTarget)
			if (lastTarget == children.size()) // dragged and dropped to the
												// right of the last element
			{
				children.add(children.remove(dragged));
				dragged = lastTarget;
			} else if (dragged < lastTarget) // shift to the right
			{
				Collections.swap(children, dragged, dragged + 1);
				dragged++;
			} else if (dragged > lastTarget) // shift to the left
			{
				Collections.swap(children, dragged, dragged - 1);
				dragged--;
			}
		for (int i = 0; i < children.size(); i++) {
			newPositions.set(i, -1);
			addView(children.get(i));
		}
		onLayout(true, getLeft(), getTop(), getRight(), getBottom());
	}

	public void scrollToTop() {
		scroll = 0;
	}

	public void scrollToBottom() {
		scroll = Integer.MAX_VALUE;
		clampScroll();
	}

	// public void reset() {
	// isStarted = false;
	// isActive = false;
	// }

	protected void clampScroll() {

	}

	protected int getMaxScroll() {
		int rowCount = (int) Math.ceil((double) getChildCount() / colCount), max = rowCount
				* childSize + (rowCount + 1) * padding - getHeight();
		return max;
	}

	public int getLastIndex() {
		return getIndexFromCoor(lastX, lastY);
	}

	// OTHER METHODS
	public void setOnRearrangeListener(OnRearrangeListener l) {
		this.onRearrangeListener = l;
	}

	public void setOnItemClickListener(OnItemClickListener l) {
	}

	public int getHiddenPoint() {
		return hiddenPoint;
	}

	public void setHiddenPoint(int hiddenPoint) {
		this.hiddenPoint = hiddenPoint;
	}

	public void setTypeAction(int t) {
		typeAction = t;
	}

	public int getDisappearPoint() {
		return disappearPoint;
	}

	public void setDisappearPoint(int disappearPoint) {
		this.disappearPoint = disappearPoint;
	}

	public void moveAccelerate(int des, int way) {
		switch (way) {
		case 0:
			break;

		case 1:
			break;
		}
	}

}