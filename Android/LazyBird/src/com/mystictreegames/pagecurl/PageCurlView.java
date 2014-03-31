package com.mystictreegames.pagecurl;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import com.vvmaster.android.lazybird.PageData;
import com.vvmaster.android.lazybird.PagesManager;
import com.vvmaster.android.lazybird.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;

/**
 * 
 * @author Moritz 'Moss' Wundke (b.thax.dcg@gmail.com)
 * 
 */
public class PageCurlView extends View {
	/** Our Log tag */
	private final static String TAG = "*** PageCurlView";

	// Debug text paint stuff
	private Paint mTextPaint;
	private TextPaint mTextPaintShadow;

	/** Px / Draw call */
	private int mCurlSpeed;

	/** Fixed update time used to create a smooth curl animation */
	private int mUpdateRate;

	/** The initial offset for x and y axis movements */
	private int mInitialEdgeOffset;

	/** The mode we will use */
	private int mCurlMode;

	/** Simple curl mode. Curl target will move only in one axis. */
	public static final int CURLMODE_SIMPLE = 0;

	/** Dynamic curl mode. Curl target will move on both X and Y axis. */
	public static final int CURLMODE_DYNAMIC = 1;

	/** Enable/Disable debug mode */
	private boolean bEnableDebugMode = false;

	/** The context which owns us */
	private WeakReference<Context> mContext;

	/** Handler used to auto flip time based */
	private FlipAnimationHandler mAnimationHandler;

	/**
	 * Maximum radius a page can be flipped, by default it's the width of the
	 * view
	 */
	private float mFlipRadius;

	/** Point used to move */
	private Vector2D mMovement;

	/** The finger position */
	private Vector2D mFinger;

	/** Movement point form the last frame */
	private Vector2D mOldMovement;

	/** Page curl edge */
	private Paint mCurlEdgePaint;

	/** Our points used to define the current clipping paths in our draw call */
	private Vector2D mA, mB, mC, mD, mE, mF, mOldF, mOrigin;

	/** Left and top offset to be applied when drawing */
	private int mCurrentLeft, mCurrentTop;

	/** If false no draw call has been done */
	private boolean bViewDrawn;

	/** Defines the flip direction that is currently considered */
	private boolean bFlipRight;

	/** If TRUE we are currently auto-flipping */
	private boolean bFlipping;

	/** TRUE if the user moves the pages */
	private boolean bUserMoves;

	/** Used to control touch input blocking */
	private boolean bBlockTouchInput = false;

	/** Enable input after the next draw event */
	private boolean bEnableInputAfterDraw = false;

	/** LAGACY The current foreground */
	private Bitmap mForeground;

	/** LAGACY The current background */
	private Bitmap mBackground;

	private Bitmap mBoxText;
	private Bitmap mBoxTextLeft;
	private Bitmap mBoxTextRight;

	/** LAGACY List of pages, this is just temporal */
	private ArrayList<Bitmap> mPages;
	// private AnimationThread mAnimationThread;
	private MyThread myThread;
	private boolean isInitAnimation;
	/** LAGACY Current selected page */
	private int mIndex = 0;
	private int mIndexTouchText = 0;
	private boolean mIsZooming = false;
	private PagesManager mPagesManager;
	private static final int mTimeZoom = 1200;

	private int mHighlightedWordsCount;
	private boolean[] isTextAppear;
	private OnPageChangedListener mPageChangedListener;

	public static interface OnPageChangedListener {
		void onChange(int foreground, int background);

		void onEnd();

		void onNextZoom();

		void onTab(int idSound);
	}

	public void setOnPageChangedListener(OnPageChangedListener listener) {
		mPageChangedListener = listener;
	}

	public Bitmap getBmBackground() {
		return mForeground;
	}

	/**
	 * Inner class used to represent a 2D point.
	 */
	private class Vector2D {
		public float x, y;

		public Vector2D(float x, float y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "(" + this.x + "," + this.y + ")";
		}

		public float length() {
			return (float) Math.sqrt(x * x + y * y);
		}

		public float lengthSquared() {
			return (x * x) + (y * y);
		}

		public boolean equals(Object o) {
			if (o instanceof Vector2D) {
				Vector2D p = (Vector2D) o;
				return p.x == x && p.y == y;
			}
			return false;
		}

		public Vector2D reverse() {
			return new Vector2D(-x, -y);
		}

		public Vector2D sum(Vector2D b) {
			return new Vector2D(x + b.x, y + b.y);
		}

		public Vector2D sub(Vector2D b) {
			return new Vector2D(x - b.x, y - b.y);
		}

		public float dot(Vector2D vec) {
			return (x * vec.x) + (y * vec.y);
		}

		public float cross(Vector2D a, Vector2D b) {
			return a.cross(b);
		}

		public float cross(Vector2D vec) {
			return x * vec.y - y * vec.x;
		}

		public float distanceSquared(Vector2D other) {
			float dx = other.x - x;
			float dy = other.y - y;

			return (dx * dx) + (dy * dy);
		}

		public float distance(Vector2D other) {
			return (float) Math.sqrt(distanceSquared(other));
		}

		public float dotProduct(Vector2D other) {
			return other.x * x + other.y * y;
		}

		public Vector2D normalize() {
			float magnitude = (float) Math.sqrt(dotProduct(this));
			return new Vector2D(x / magnitude, y / magnitude);
		}

		public Vector2D mult(float scalar) {
			return new Vector2D(x * scalar, y * scalar);
		}
	}

	/**
	 * Inner class used to make a fixed timed animation of the curl effect.
	 */
	class FlipAnimationHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			PageCurlView.this.FlipAnimationStep();
		}

		public void sleep(long millis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), millis);
		}
	}

	/**
	 * Base
	 * 
	 * @param context
	 */
	public PageCurlView(Context context) {
		super(context);
		init(context);
		ResetClipEdge();
	}

	/**
	 * Construct the object from an XML file. Valid Attributes:
	 * 
	 * @see android.view.View#View(android.content.Context,
	 *      android.util.AttributeSet)
	 */
	public PageCurlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

		// Get the data from the XML AttributeSet
		{
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.PageCurlView);

			// Get data
			bEnableDebugMode = a.getBoolean(
					R.styleable.PageCurlView_enableDebugMode, bEnableDebugMode);
			mCurlSpeed = a.getInt(R.styleable.PageCurlView_curlSpeed,
					mCurlSpeed);
			mUpdateRate = a.getInt(R.styleable.PageCurlView_updateRate,
					mUpdateRate);
			mInitialEdgeOffset = a.getInt(
					R.styleable.PageCurlView_initialEdgeOffset,
					mInitialEdgeOffset);
			mCurlMode = a.getInt(R.styleable.PageCurlView_curlMode, mCurlMode);
			a.recycle();
		}

		ResetClipEdge();
	}

	/**
	 * Initialize the view
	 */
	private final void init(Context context) {
		// Foreground text paint
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(16);
		mTextPaint.setColor(0xFF000000);

		// The shadow
		mTextPaintShadow = new TextPaint();
		mTextPaintShadow.setAntiAlias(true);
		mTextPaintShadow.setTextSize(16);
		mTextPaintShadow.setColor(0x00000000);

		// Cache the context
		mContext = new WeakReference<Context>(context);

		// Base padding
		setPadding(3, 3, 3, 3);

		// The focus flags are needed
		setFocusable(true);
		setFocusableInTouchMode(true);

		mMovement = new Vector2D(0, 0);
		mFinger = new Vector2D(0, 0);
		mOldMovement = new Vector2D(0, 0);

		// Create our curl animation handler
		mAnimationHandler = new FlipAnimationHandler();

		// Create our edge paint
		mCurlEdgePaint = new Paint();
		mCurlEdgePaint.setColor(Color.WHITE);
		mCurlEdgePaint.setAntiAlias(true);
		mCurlEdgePaint.setStyle(Paint.Style.FILL);
		mCurlEdgePaint.setShadowLayer(10, -5, 5, 0x99000000);

		// Set the default props, those come from an XML :D
		mCurlSpeed = 230;
		mUpdateRate = 33;
		mInitialEdgeOffset = 20;
		mCurlMode = 1;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inScaled = false;
		mBoxText = BitmapFactory.decodeResource(getResources(),
				R.drawable.midle_box_text, opts);
		mBoxTextLeft = BitmapFactory.decodeResource(getResources(),
				R.drawable.left_box_text, opts);
		mBoxTextRight = BitmapFactory.decodeResource(getResources(),
				R.drawable.right_box_text, opts);
	}
public int mStartPage;
	public void setPagesManager(PagesManager mngr, int startPage) {
		// LEGACY PAGE HANDLING!

		mPagesManager = mngr;

		// Create pages
		mPages = new ArrayList<Bitmap>(PagesManager.PAGE_COUNT);
		for (int i = 0; i < PagesManager.PAGE_COUNT; ++i) {
			mPages.add(null);
		}
		mBitmapBlinking = new ArrayList<Bitmap[]>(PagesManager.PAGE_COUNT);
		mRectFDst = new ArrayList<RectF[]>(PagesManager.PAGE_COUNT);
		mRectSrc = new ArrayList<Rect[]>(PagesManager.PAGE_COUNT);
		for (int i = 0; i < PagesManager.PAGE_COUNT; ++i) {
			mBitmapBlinking.add(null);
			mRectFDst.add(null);
			mRectSrc.add(null);
		}
		int nextPage = startPage < mPages.size() - 1 ? startPage + 1
				: startPage;
		doLoadBitmap(startPage);
		doLoadBitmap(nextPage);
//		mStartPage=startPage;
		mIndex = startPage;
mIndexAnimation=mIndexTouchText = mIndex;
		resetTextAppear(mIndexTouchText);
		setViews(startPage, nextPage, false);
	}

	public void setHighlightedWordsCount(int count) {
		mHighlightedWordsCount = count;
		invalidate();
	}

	public int getCurrentPageIndex() {
		return mIndex;
	}

	public int getCurrentPageDisplay() {
		return mIndexTouchText;
	}

	public PagesManager getPageManager() {
		return mPagesManager;
	}

	/**
	 * Reset points to it's initial clip edge state
	 */
	public void ResetClipEdge() {
		// Set our base movement
		mMovement.x = mInitialEdgeOffset;
		mMovement.y = mInitialEdgeOffset;
		mOldMovement.x = 0;
		mOldMovement.y = 0;

		// Now set the points
		// TODO: OK, those points MUST come from our measures and
		// the actual bounds of the view!
		mA = new Vector2D(mInitialEdgeOffset, 0);
		mB = new Vector2D(this.getWidth(), this.getHeight());
		mC = new Vector2D(this.getWidth(), 0);
		mD = new Vector2D(0, 0);
		mE = new Vector2D(0, 0);
		mF = new Vector2D(0, 0);
		mOldF = new Vector2D(0, 0);

		// The movement origin point
		mOrigin = new Vector2D(this.getWidth(), 0);
	}

	/**
	 * Return the context which created use. Can return null if the context has
	 * been erased.
	 */
	private Context GetContext() {
		return mContext.get();
	}

	/**
	 * See if the current curl mode is dynamic
	 * 
	 * @return TRUE if the mode is CURLMODE_DYNAMIC, FALSE otherwise
	 */
	public boolean IsCurlModeDynamic() {
		return mCurlMode == CURLMODE_DYNAMIC;
	}

	/**
	 * Set the curl speed.
	 * 
	 * @param curlSpeed
	 *            - New speed in px/frame
	 * @throws IllegalArgumentException
	 *             if curlspeed < 1
	 */
	public void SetCurlSpeed(int curlSpeed) {
		if (curlSpeed < 1)
			throw new IllegalArgumentException(
					"curlSpeed must be greated than 0");
		mCurlSpeed = curlSpeed;
	}

	/**
	 * Get the current curl speed
	 * 
	 * @return int - Curl speed in px/frame
	 */
	public int GetCurlSpeed() {
		return mCurlSpeed;
	}

	/**
	 * Set the update rate for the curl animation
	 * 
	 * @param updateRate
	 *            - Fixed animation update rate in fps
	 * @throws IllegalArgumentException
	 *             if updateRate < 1
	 */
	public void SetUpdateRate(int updateRate) {
		if (updateRate < 1)
			throw new IllegalArgumentException(
					"updateRate must be greated than 0");
		mUpdateRate = updateRate;
	}

	/**
	 * Get the current animation update rate
	 * 
	 * @return int - Fixed animation update rate in fps
	 */
	public int GetUpdateRate() {
		return mUpdateRate;
	}

	/**
	 * Set the initial pixel offset for the curl edge
	 * 
	 * @param initialEdgeOffset
	 *            - px offset for curl edge
	 * @throws IllegalArgumentException
	 *             if initialEdgeOffset < 0
	 */
	public void SetInitialEdgeOffset(int initialEdgeOffset) {
		if (initialEdgeOffset < 0)
			throw new IllegalArgumentException(
					"initialEdgeOffset can not negative");
		mInitialEdgeOffset = initialEdgeOffset;
	}

	/**
	 * Get the initial pixel offset for the curl edge
	 * 
	 * @return int - px
	 */
	public int GetInitialEdgeOffset() {
		return mInitialEdgeOffset;
	}

	/**
	 * Set the curl mode.
	 * <p>
	 * Can be one of the following values:
	 * </p>
	 * <table>
	 * <colgroup align="left" /> <colgroup align="left" />
	 * <tr>
	 * <th>Value</th>
	 * <th>Description</th>
	 * </tr>
	 * <tr>
	 * <td>
	 * <code>{@link #CURLMODE_SIMPLE com.dcg.pagecurl:CURLMODE_SIMPLE}</code></td>
	 * <td>Curl target will move only in one axis.</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * <code>{@link #CURLMODE_DYNAMIC com.dcg.pagecurl:CURLMODE_DYNAMIC}</code></td>
	 * <td>Curl target will move on both X and Y axis.</td>
	 * </tr>
	 * </table>
	 * 
	 * @see #CURLMODE_SIMPLE
	 * @see #CURLMODE_DYNAMIC
	 * @param curlMode
	 * @throws IllegalArgumentException
	 *             if curlMode is invalid
	 */
	public void SetCurlMode(int curlMode) {
		if (curlMode != CURLMODE_SIMPLE && curlMode != CURLMODE_DYNAMIC)
			throw new IllegalArgumentException("Invalid curlMode");
		mCurlMode = curlMode;
	}

	/**
	 * Return an integer that represents the current curl mode.
	 * <p>
	 * Can be one of the following values:
	 * </p>
	 * <table>
	 * <colgroup align="left" /> <colgroup align="left" />
	 * <tr>
	 * <th>Value</th>
	 * <th>Description</th>
	 * </tr>
	 * <tr>
	 * <td>
	 * <code>{@link #CURLMODE_SIMPLE com.dcg.pagecurl:CURLMODE_SIMPLE}</code></td>
	 * <td>Curl target will move only in one axis.</td>
	 * </tr>
	 * <tr>
	 * <td>
	 * <code>{@link #CURLMODE_DYNAMIC com.dcg.pagecurl:CURLMODE_DYNAMIC}</code></td>
	 * <td>Curl target will move on both X and Y axis.</td>
	 * </tr>
	 * </table>
	 * 
	 * @see #CURLMODE_SIMPLE
	 * @see #CURLMODE_DYNAMIC
	 * @return int - current curl mode
	 */
	public int GetCurlMode() {
		return mCurlMode;
	}

	/**
	 * Enable debug mode. This will draw a lot of data in the view so you can
	 * track what is happening
	 * 
	 * @param bFlag
	 *            - boolean flag
	 */
	public void SetEnableDebugMode(boolean bFlag) {
		bEnableDebugMode = bFlag;
	}

	/**
	 * Check if we are currently in debug mode.
	 * 
	 * @return boolean - If TRUE debug mode is on, FALSE otherwise.
	 */
	public boolean IsDebugModeEnabled() {
		return bEnableDebugMode;
	}

	/**
	 * @see android.view.View#measure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int finalWidth, finalHeight;
		finalWidth = measureWidth(widthMeasureSpec);
		finalHeight = measureHeight(heightMeasureSpec);
		setMeasuredDimension(finalWidth, finalHeight);

	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = specSize;
		}
		return result;

	}

	public void onAttachedToWindow() {
		super.onAttachedToWindow();

	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = specSize;
		}
		return result;
	}

	/**
	 * Render the text
	 * 
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	// @Override
	// protected void onDraw(Canvas canvas) {
	// super.onDraw(canvas);
	// canvas.drawText(mText, getPaddingLeft(), getPaddingTop() - mAscent,
	// mTextPaint);
	// }

	// ---------------------------------------------------------------
	// Curling. This handles touch events, the actual curling
	// implementations and so on.
	// ---------------------------------------------------------------
	private float mDownX;
	// private boolean isText;
	private boolean isReset;
	// private boolean isBlock;
	private static float mDelta = 60f;
	private int indexPageDown;

	// private int curlSpeedReal;
	// private int reset;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsZooming)
			return true;
		if (!bBlockTouchInput) {
			// Get our finger position
			mFinger.x = event.getX();
			mFinger.y = event.getY();
			int width = getWidth();

			// Depending on the action do what we need to
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				indexPageDown = mIndex;
				isTouchFlip = true;
				mDownX = mFinger.x;
				if (mFinger.x > width >> 1
						&& mIndex == PagesManager.PAGE_COUNT - 1) {
					break;
				}
				mOldMovement.x = mFinger.x;
				mOldMovement.y = mFinger.y;
				// onEventTexAppear();
				// If we moved over the half of the display flip to next
				if (mOldMovement.x > (width >> 1)) {
					mMovement.x = mInitialEdgeOffset;
					mMovement.y = mInitialEdgeOffset;

					// Set the right movement flag
					bFlipRight = true;
				} else {
					if (mIndex > 0) {
						// Set the left movement flag
						bFlipRight = false;

						// go to next previous page
						previousView();
						// Set new movement
						mMovement.x = IsCurlModeDynamic() ? width << 1 : width;
						mMovement.y = mInitialEdgeOffset;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				bUserMoves = false;
				bFlipping = true;
				if (mDownX < width >> 1 && indexPageDown > 0) {
					if (Math.abs(mDownX - mFinger.x) < mDelta) {
						onEventTexAppear();
						bFlipRight = true;
						isReset = false;
					} else if (mFinger.x - mDownX > mDelta) {
						isReset = true;
					} else {
						bFlipRight = true;
						isReset = false;
					}

				} else {
					if (Math.abs(mDownX - mFinger.x) < mDelta) {
						onEventTexAppear();
						bFlipRight = false;
						isReset = false;
					} else if (mFinger.x - mDownX > mDelta) {
						bFlipRight = false;
						isReset = false;
					} else {
						bFlipRight = true;
						isReset = true;
					}
				}
				if (mFinger.x > width >> 1
						&& mIndex == PagesManager.PAGE_COUNT - 1 && bFlipRight) {
					// go to end page.
					if (mPageChangedListener != null)
						mPageChangedListener.onEnd();
					break;
				}
				FlipAnimationStep();
				break;

			case MotionEvent.ACTION_MOVE:
				if (mFinger.x > width >> 1
						&& mIndex == PagesManager.PAGE_COUNT - 1) {
					break;
				}
				bUserMoves = true;

				// Get movement
				mMovement.x -= mFinger.x - mOldMovement.x;
				mMovement.y -= mFinger.y - mOldMovement.y;
				mMovement = CapMovement(mMovement, true);

				// Make sure the y value get's locked at a nice level
				if (mMovement.y <= 1)
					mMovement.y = 1;

				// Get movement direction
				if (mFinger.x < mOldMovement.x) {
					bFlipRight = true;
				} else {
					bFlipRight = false;
				}

				// Save old movement values
				mOldMovement.x = mFinger.x;
				mOldMovement.y = mFinger.y;

				// Force a new draw call
				DoPageCurl();
				this.invalidate();
				break;

			}

		}

		// TODO: Only consume event if we need to.
		return true;
	}

	/**
	 * Make sure we never move too much, and make sure that if we move too much
	 * to add a displacement so that the movement will be still in our radius.
	 */
	private Vector2D CapMovement(Vector2D point, boolean bMaintainMoveDir) {
		// Make sure we never ever move too much
		if (point.distance(mOrigin) > mFlipRadius) {
			if (bMaintainMoveDir) {
				// Maintain the direction
				point = mOrigin.sum(point.sub(mOrigin).normalize()
						.mult(mFlipRadius));
			} else {
				// Change direction
				if (point.x > (mOrigin.x + mFlipRadius))
					point.x = (mOrigin.x + mFlipRadius);
				else if (point.x < (mOrigin.x - mFlipRadius))
					point.x = (mOrigin.x - mFlipRadius);
				point.y = (float) (Math.sin(Math.acos(Math.abs(point.x
						- mOrigin.x)
						/ mFlipRadius)) * mFlipRadius);
			}
		}
		return point;
	}

	/**
	 * Execute a step of the flip animation
	 */
	public boolean isTouchFlip = false;

	public void FlipAnimationStep() {
		if (!bFlipping) {
			return;
		}
		int width = getWidth();

		// No input when flipping
		bBlockTouchInput = true;

		// Handle speed
		float curlSpeed = mCurlSpeed;
		if (!bFlipRight)
			curlSpeed *= -1;

		// Move us
		mMovement.x += curlSpeed;
		mMovement = CapMovement(mMovement, false);

		// Create values
		DoPageCurl();
		// Check for endings :D
		if (mA.x < 1 || mA.x > width - 1) {
			bFlipping = false;
			if (bFlipRight) {
				// SwapViews();
				nextView();

			} else if (isReset) {
				PageData pageDate = mPagesManager.getPage(mIndex);

				if (mPageChangedListener != null && !pageDate.mIsZoom)
					mPageChangedListener.onChange(mforeground, mbackground);
			}
			ResetClipEdge();

			// Create values
			DoPageCurl();

			// Enable touch input after the next draw event
			bEnableInputAfterDraw = true;
			// event reset text touch
			mIndexAnimation = mIndexTouchText = mIndex;
			if (isReset) {
				resetTextAppear(mIndexTouchText);
				float ratioX = 0;
				float ratioY = 0;
				PageData pageDate = mPagesManager.getPage(mIndex);
				if (pageDate.mIsZoom) {
					mIsZooming = true;
					ratioX = (float) getWidth() / mForeground.getWidth();
					ratioY = (float) getHeight() / mForeground.getHeight();
					ScaleAnimation scale1 = new ScaleAnimation(1, 2, 1, 2,
							pageDate.mPivotZoomX * ratioX, pageDate.mPivotZoomY
									* ratioY);
					scale1.setDuration(0);
					startAnimation(scale1);

					ScaleAnimation scale2 = new ScaleAnimation(2, 1, 2, 1,
							pageDate.mPivotZoomX * ratioX, pageDate.mPivotZoomY
									* ratioY);
					scale2.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
							mIsZooming = false;
							if (mPageChangedListener != null)
								mPageChangedListener.onChange(mforeground,
										mbackground);
						}
					});
					scale2.setDuration(mTimeZoom);
					startAnimation(scale2);
				}
			}
			resetBlinking();
			isTouchFlip = false;
			invalidate();
		} else {
			mAnimationHandler.sleep(mUpdateRate);
		}

		// Force a new draw call
		this.invalidate();
		isNextView = true;
	}

	/**
	 * Do the page curl depending on the methods we are using
	 */
	private void DoPageCurl() {
		if (bFlipping) {
			if (IsCurlModeDynamic())
				doDynamicCurl();
			else
				doSimpleCurl();

		} else {
			if (IsCurlModeDynamic())
				doDynamicCurl();
			else
				doSimpleCurl();
		}
	}

	/**
	 * Do a simple page curl effect
	 */
	private void doSimpleCurl() {
		int width = getWidth();
		int height = getHeight();

		// Calculate point A
		mA.x = width - mMovement.x;
		mA.y = height;

		// Calculate point D
		mD.x = 0;
		mD.y = 0;
		if (mA.x > width / 2) {
			mD.x = width;
			mD.y = height - (width - mA.x) * height / mA.x;
		} else {
			mD.x = 2 * mA.x;
			mD.y = 0;
		}

		// Now calculate E and F taking into account that the line
		// AD is perpendicular to FB and EC. B and C are fixed points.
		double angle = Math
				.atan((height - mD.y) / (mD.x + mMovement.x - width));
		double _cos = Math.cos(2 * angle);
		double _sin = Math.sin(2 * angle);

		// And get F
		mF.x = (float) (width - mMovement.x + _cos * mMovement.x);
		mF.y = (float) (height - _sin * mMovement.x);

		// If the x position of A is above half of the page we are still not
		// folding the upper-right edge and so E and D are equal.
		if (mA.x > width / 2) {
			mE.x = mD.x;
			mE.y = mD.y;
		} else {
			// So get E
			mE.x = (float) (mD.x + _cos * (width - mD.x));
			mE.y = (float) -(_sin * (width - mD.x));
		}
	}

	/**
	 * Calculate the dynamic effect, that one that follows the users finger
	 */
	private void doDynamicCurl() {
		int width = getWidth();
		int height = getHeight();

		// F will follow the finger, we add a small displacement
		// So that we can see the edge
		mF.x = width - mMovement.x + 0.1f;
		mF.y = height - mMovement.y + 0.1f;

		// Set min points
		if (mA.x == 0) {
			mF.x = Math.min(mF.x, mOldF.x);
			mF.y = Math.max(mF.y, mOldF.y);
		}

		// Get diffs
		float deltaX = width - mF.x;
		float deltaY = height - mF.y;

		float BH = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY) / 2);
		double tangAlpha = deltaY / deltaX;
		double alpha = Math.atan(deltaY / deltaX);
		double _cos = Math.cos(alpha);
		double _sin = Math.sin(alpha);

		mA.x = (float) (width - (BH / _cos));
		mA.y = height;

		mD.y = (float) (height - (BH / _sin));
		mD.x = width;

		mA.x = Math.max(0, mA.x);
		if (mA.x == 0) {
			mOldF.x = mF.x;
			mOldF.y = mF.y;
		}

		// Get W
		mE.x = mD.x;
		mE.y = mD.y;

		// Correct
		if (mD.y < 0) {
			mD.x = width + (float) (tangAlpha * mD.y);
			mE.y = 0;
			mE.x = width + (float) (Math.tan(2 * alpha) * mD.y);
		}
	}

	/**
	 * Swap between the fore and back-ground.
	 */
	@Deprecated
	private void SwapViews() {
		Bitmap temp = mForeground;
		mForeground = mBackground;
		mBackground = temp;
	}

	/**
	 * Swap to next view
	 */
	private void nextView() {
		if (mIndex == mPages.size() - 1)
			return;

		int foreIndex = mIndex + 1;
		if (foreIndex >= mPages.size()) {
			foreIndex = mPages.size() - 1;
		}
		int backIndex = foreIndex + 1;
		if (backIndex >= mPages.size()) {
			backIndex = mPages.size() - 1;
		}
		mIndex = foreIndex;
		if (isReset)
			setViews(foreIndex, backIndex, true);
		else
			setViews(foreIndex, backIndex, false);
	}

	/**
	 * Swap to previous view
	 */
	private void previousView() {
		if (mIndex == 0)
			return;

		int backIndex = mIndex;
		int foreIndex = backIndex - 1;
		if (foreIndex < 0) {
			foreIndex = 0;
		}
		mIndex = foreIndex;
		setViews(foreIndex, backIndex, false);
	}

	/**
	 * Set current fore and background
	 * 
	 * @param foreground
	 *            - Foreground view index
	 * @param background
	 *            - Background view index
	 */
	public int mforeground, mbackground;
	public boolean isNextView;

	private void setViews(int foreground, int background, boolean _isNextView) {
		mforeground = foreground;
		mbackground = background;
		isNextView = _isNextView;
		float ratioX = 0;
		float ratioY = 0;
		PageData pageDate = mPagesManager.getPage(mIndex);
		if (pageDate.mIsZoom && _isNextView) {
			mIsZooming = true;
			ratioX = (float) getWidth() / mForeground.getWidth();
			ratioY = (float) getHeight() / mForeground.getHeight();
			ScaleAnimation scale1 = new ScaleAnimation(1, 2, 1, 2,
					pageDate.mPivotZoomX * ratioX, pageDate.mPivotZoomY
							* ratioY);
			scale1.setDuration(0);
			startAnimation(scale1);
		}
		doLoadNearestBitmaps(foreground, background);
		mForeground = mPages.get(foreground);
		mBackground = mPages.get(background);
		mHighlightedWordsCount = 0;
		if (isNextView) {
			if (!pageDate.mIsZoom) {
				if (mPageChangedListener != null)
					mPageChangedListener.onChange(foreground, background);
			} else {
				if (mPageChangedListener != null)
					mPageChangedListener.onNextZoom();
			}
		}
		cleanupUnusedBitmaps(foreground, background);
		if (pageDate.mIsZoom && _isNextView) {
			ScaleAnimation scale2 = new ScaleAnimation(2, 1, 2, 1,
					pageDate.mPivotZoomX * ratioX, pageDate.mPivotZoomY
							* ratioY);
			scale2.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					mIsZooming = false;
					if (mPageChangedListener != null)
						mPageChangedListener.onChange(mforeground, mbackground);
				}
			});
			scale2.setDuration(mTimeZoom);
			startAnimation(scale2);
		}
	}

	private void drawText(Canvas canvas, float sx, float sy, int pageIndex,
			int highlightedWordsCount, boolean _mIsZoom) {
		PageData pageData = mPagesManager.getPage(pageIndex);
		if (pageData == null)
			return;

		// Log.d(TAG, "Drawing text for bmp: " + bmp.getWidth() + " x " +
		// bmp.getHeight());

		canvas.save();

		Paint paint = new Paint();
		paint.setTypeface(pageData.mTypeface);
		if (_mIsZoom)
			paint.setTextSize(2 * pageData.mFontSize);
		else
			paint.setTextSize(pageData.mFontSize);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setSubpixelText(true);
		paint.setFlags(paint.getFlags() & ~Paint.DEV_KERN_TEXT_FLAG);
		final float defStroke = paint.getStrokeWidth();
		final float highStroke = defStroke + 2.f;

		// final float density = getResources().getDisplayMetrics().density;
		// int scaledValue = (int) (pageData.mFrameXOffset * density + 0.5f);

		canvas.scale(sx, sy);
		if (_mIsZoom)
			canvas.translate(2 * pageData.mFrameXOffset - pageData.mPivotZoomX,
					2 * pageData.mFrameYOffset - paint.getFontMetrics().ascent
							- pageData.mPivotZoomY);
		else {
			canvas.translate(pageData.mFrameXOffset, pageData.mFrameYOffset
					- paint.getFontMetrics().ascent);
		}
		int passedWords = 0;
		for (int i = 0; i < pageData.mStrings.length; ++i) {
			int start = 0;
			int end = pageData.mStrings[i].length();
			final String[] sentence = pageData.mStrings[i].split(" ");
			if (passedWords + sentence.length <= highlightedWordsCount) {
				paint.setStrokeWidth(highStroke);
				paint.setColor(PagesManager.HIGHLIGHTED_TEXT_COLOR);
			} else if (passedWords >= highlightedWordsCount) {
				paint.setStrokeWidth(defStroke);
				paint.setColor(pageData.mFontColor);
			} else {
				int wordIndex = highlightedWordsCount - passedWords;
				String wordString = sentence[wordIndex];

				end = pageData.mStrings[i].indexOf(" " + wordString) + 1
						+ wordString.length();

				paint.setStrokeWidth(defStroke);
				paint.setColor(pageData.mFontColor);
				// canvas.drawText(pageData.mStrings[i], end,
				// pageData.mStrings[i].length(),
				// pageData.mStringXOffsets[i] +
				// Math.round(paint.measureText(pageData.mStrings[i], start,
				// end)), pageData.mStringYOffsets[i], paint);
				canvas.drawText(pageData.mStrings[i], start,
						pageData.mStrings[i].length(),
						pageData.mStringXOffsets[i],
						pageData.mStringYOffsets[i], paint);
				// Log.d(TAG, "drawing text for index: " + pageIndex +
				// " highlighted: " + highlightedWordsCount + " start: " + start
				// +
				// " end: " + end + " length: " +
				// pageData.mStrings[i].length());

				paint.setStrokeWidth(highStroke);
				paint.setColor(PagesManager.HIGHLIGHTED_TEXT_COLOR);
			}
			if (!_mIsZoom)
				canvas.drawText(pageData.mStrings[i], start, end,
						pageData.mStringXOffsets[i],
						pageData.mStringYOffsets[i], paint);
			else {
				canvas.drawText(pageData.mStrings[i], start, end,
						pageData.mStringXOffsets[i] * 2,
						pageData.mStringYOffsets[i] * 2, paint);
			}
			passedWords += sentence.length;
		}

		canvas.restore();
	}

	private void doLoadNearestBitmaps(int foreground, int background) {
		int prev = foreground - 1;
		int next = background + 1;
		if (prev >= 0 && mPages.get(prev) == null) {
			doLoadBitmap(prev);
		}
		if (next < mPages.size() && mPages.get(next) == null) {
			doLoadBitmap(next);
		}
	}

	private void doLoadBitmap(int index) {
		if (index >= 0 && index < mPages.size() && mPages.get(index) == null) {
			PageData page = mPagesManager.getPage(index);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inScaled = false;
			Bitmap readOnlyBmp = BitmapFactory.decodeResource(getResources(),
					page.mBitmapResId, opts);
			Bitmap writeableBmp = readOnlyBmp.copy(Bitmap.Config.RGB_565, true);
			mPages.set(index, writeableBmp);
			readOnlyBmp.recycle();
		}
	}

	private void cleanupUnusedBitmaps(int foreground, int background) {
		for (int i = 0; i < PagesManager.PAGE_COUNT; ++i) {
			if (i < (foreground - 1) || i > (background + 1)) {
				Bitmap picToDelete = mPages.get(i);
				if (picToDelete != null) {
					picToDelete.recycle();
					mPages.set(i, null);
				}
			}
		}
	}

	// ---------------------------------------------------------------
	// Drawing methods
	// ---------------------------------------------------------------

	@Override
	protected void onDraw(Canvas canvas) {
		if (!isInitAnimation) {
			initBlinking();
			myThread = new MyThread();
			isInitAnimation = true;
		}
		// Always refresh offsets
		mCurrentLeft = getLeft();
		mCurrentTop = getTop();

		// Translate the whole canvas
		// canvas.translate(mCurrentLeft, mCurrentTop);

		// We need to initialize all size data when we first draw the view
		if (!bViewDrawn) {
			bViewDrawn = true;
			onFirstDrawEvent(canvas);
		}

		canvas.drawColor(Color.WHITE);

		// Curl pages
		// DoPageCurl();

		// TODO: This just scales the views. to the current
		// width and height. We should add some logic for:
		// 1) Maintain aspect ratio
		// 2) Uniform scale
		// 3) ...
		Rect rect = new Rect();
		rect.left = 0;
		rect.top = 0;
		rect.bottom = getHeight();
		rect.right = getWidth();

		// First Page render
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);

		// Draw our elements
		drawForeground(canvas, rect, paint);
		if (isInitAnimation)
			drawBlinking(canvas, paint);
		// mAnimationThread.drawAnimation(canvas, paint);
		drawBackground(canvas, rect, paint);
		drawCurlEdge(canvas);
		// if (!isBlock)
		drawTextAppear(canvas, paint);

		// Draw any debug info once we are done
		if (bEnableDebugMode)
			drawDebug(canvas);

		// Check if we can re-enable input
		if (bEnableInputAfterDraw) {
			bBlockTouchInput = false;
			bEnableInputAfterDraw = false;
		}

		// Restore canvas
		// canvas.restore();
	}

	/**
	 * Called on the first draw event of the view
	 * 
	 * @param canvas
	 */
	protected void onFirstDrawEvent(Canvas canvas) {

		mFlipRadius = getWidth();

		ResetClipEdge();
		DoPageCurl();
	}

	/**
	 * Draw the foreground
	 * 
	 * @param canvas
	 * @param rect
	 * @param paint
	 */
	private void drawForeground(Canvas canvas, Rect rect, Paint paint) {
		canvas.drawBitmap(mForeground, null, rect, paint);

		drawText(canvas, (float) rect.width() / mForeground.getWidth(),
				(float) rect.height() / mForeground.getHeight(), mIndex,
				mHighlightedWordsCount, false);
	}

	/**
	 * Create a Path used as a mask to draw the background page
	 * 
	 * @return
	 */
	private Path createBackgroundPath() {
		Path path = new Path();
		path.moveTo(mA.x, mA.y);
		path.lineTo(mB.x, mB.y);
		path.lineTo(mC.x, mC.y);
		path.lineTo(mD.x, mD.y);
		path.lineTo(mA.x, mA.y);
		return path;
	}

	/**
	 * Draw the background image.
	 * 
	 * @param canvas
	 * @param rect
	 * @param paint
	 */
	boolean zoomText = false;

	private void drawBackground(Canvas canvas, Rect rect, Paint paint) {
		Path mask = createBackgroundPath();
		PageData pageData = mPagesManager.getPage(mIndex + 1);
		if (pageData == null)
			return;
		float ratioX = (float) getWidth() / mBackground.getWidth();
		float ratioY = (float) getHeight() / mBackground.getHeight();
		// Save current canvas so we do not mess it up
		canvas.save();
		canvas.clipPath(mask);
		RectF rectF;
		if (pageData.mIsZoom && (isNextView || !isTouchFlip)) {
			rectF = new RectF((rect.left - pageData.mPivotZoomX * ratioX),
					(rect.top - pageData.mPivotZoomY * ratioY), (rect.right
							+ getWidth() - pageData.mPivotZoomX * ratioX),
					(rect.bottom + getHeight() - pageData.mPivotZoomY * ratioY));
		} else {
			rectF = new RectF(rect.left, rect.top, rect.right, rect.bottom);
		}
		canvas.drawBitmap(mBackground, null, rectF, paint);

		if (pageData.mIsZoom && (isNextView || !isTouchFlip) && !mIsZooming) {
			zoomText = true;
		} else {
			zoomText = false;
		}
		drawText(canvas, (float) rect.width() / mBackground.getWidth(),
				(float) rect.height() / mBackground.getHeight(), mIndex + 1, 0,
				zoomText);

		canvas.restore();
	}

	/**
	 * Creates a path used to draw the curl edge in.
	 * 
	 * @return
	 */
	private Path createCurlEdgePath() {
		Path path = new Path();
		path.moveTo(mA.x, mA.y);
		path.lineTo(mD.x, mD.y);
		path.lineTo(mE.x, mE.y);
		path.lineTo(mF.x, mF.y);
		path.lineTo(mA.x, mA.y);
		return path;
	}

	/**
	 * Draw the curl page edge
	 * 
	 * @param canvas
	 */
	private void drawCurlEdge(Canvas canvas) {
		Path path = createCurlEdgePath();
		canvas.drawPath(path, mCurlEdgePaint);
	}

	/**
	 * Draw page num (let this be a bit more custom)
	 * 
	 * @param canvas
	 * @param pageNum
	 */
	private void drawPageNum(Canvas canvas, int pageNum) {
		mTextPaint.setColor(Color.WHITE);
		String pageNumText = "- " + pageNum + " -";
		drawCentered(canvas, pageNumText,
				canvas.getHeight() - mTextPaint.getTextSize() - 5, mTextPaint,
				mTextPaintShadow);
	}

	// ---------------------------------------------------------------
	// Debug draw methods
	// ---------------------------------------------------------------

	/**
	 * Draw a text with a nice shadow
	 */
	public static void drawTextShadowed(Canvas canvas, String text, float x,
			float y, Paint textPain, Paint shadowPaint) {
		canvas.drawText(text, x - 1, y, shadowPaint);
		canvas.drawText(text, x, y + 1, shadowPaint);
		canvas.drawText(text, x + 1, y, shadowPaint);
		canvas.drawText(text, x, y - 1, shadowPaint);
		canvas.drawText(text, x, y, textPain);
	}

	/**
	 * Draw a text with a nice shadow centered in the X axis
	 * 
	 * @param canvas
	 * @param text
	 * @param y
	 * @param textPain
	 * @param shadowPaint
	 */
	public static void drawCentered(Canvas canvas, String text, float y,
			Paint textPain, Paint shadowPaint) {
		float posx = (canvas.getWidth() - textPain.measureText(text)) / 2;
		drawTextShadowed(canvas, text, posx, y, textPain, shadowPaint);
	}

	/**
	 * Draw debug info
	 * 
	 * @param canvas
	 */
	private void drawDebug(Canvas canvas) {
		float posX = 10;
		float posY = 20;

		Paint paint = new Paint();
		paint.setStrokeWidth(5);
		paint.setStyle(Style.STROKE);

		paint.setColor(Color.BLACK);
		canvas.drawCircle(mOrigin.x, mOrigin.y, getWidth(), paint);

		paint.setStrokeWidth(3);
		paint.setColor(Color.RED);
		canvas.drawCircle(mOrigin.x, mOrigin.y, getWidth(), paint);

		paint.setStrokeWidth(5);
		paint.setColor(Color.BLACK);
		canvas.drawLine(mOrigin.x, mOrigin.y, mMovement.x, mMovement.y, paint);

		paint.setStrokeWidth(3);
		paint.setColor(Color.RED);
		canvas.drawLine(mOrigin.x, mOrigin.y, mMovement.x, mMovement.y, paint);

		posY = debugDrawPoint(canvas, "A", mA, Color.RED, posX, posY);
		posY = debugDrawPoint(canvas, "B", mB, Color.GREEN, posX, posY);
		posY = debugDrawPoint(canvas, "C", mC, Color.BLUE, posX, posY);
		posY = debugDrawPoint(canvas, "D", mD, Color.CYAN, posX, posY);
		posY = debugDrawPoint(canvas, "E", mE, Color.YELLOW, posX, posY);
		posY = debugDrawPoint(canvas, "F", mF, Color.LTGRAY, posX, posY);
		posY = debugDrawPoint(canvas, "Mov", mMovement, Color.DKGRAY, posX,
				posY);
		posY = debugDrawPoint(canvas, "Origin", mOrigin, Color.MAGENTA, posX,
				posY);
		posY = debugDrawPoint(canvas, "Finger", mFinger, Color.GREEN, posX,
				posY);

		// Draw some curl stuff (Just some test)
		/*
		 * canvas.save(); Vector2D center = new
		 * Vector2D(getWidth()/2,getHeight()/2);
		 * //canvas.rotate(315,center.x,center.y);
		 * 
		 * // Test each lines //float radius = mA.distance(mD)/2.f; //float
		 * radius = mA.distance(mE)/2.f; float radius = mA.distance(mF)/2.f;
		 * //float radius = 10; float reduction = 4.f; RectF oval = new RectF();
		 * oval.top = center.y-radius/reduction; oval.bottom =
		 * center.y+radius/reduction; oval.left = center.x-radius; oval.right =
		 * center.x+radius; canvas.drawArc(oval, 0, 360, false, paint);
		 * canvas.restore(); /*
		 */
	}

	private float debugDrawPoint(Canvas canvas, String name, Vector2D point,
			int color, float posX, float posY) {
		return debugDrawPoint(canvas, name + " " + point.toString(), point.x,
				point.y, color, posX, posY);
	}

	private float debugDrawPoint(Canvas canvas, String name, float X, float Y,
			int color, float posX, float posY) {
		mTextPaint.setColor(color);
		drawTextShadowed(canvas, name, posX, posY, mTextPaint, mTextPaintShadow);
		Paint paint = new Paint();
		paint.setStrokeWidth(5);
		paint.setColor(color);
		canvas.drawPoint(X, Y, paint);
		return posY + 15;
	}

	public boolean checkScopeTouch(RectF rectTouch, float xTouch, float yTouch) {
		if (xTouch >= rectTouch.left && xTouch <= rectTouch.right
				&& yTouch >= rectTouch.top && yTouch <= rectTouch.bottom) {
			return true;
		} else
			return false;
	}

	public void resetTextAppear(int _mIndexTouchText) {
		PageData pageData = mPagesManager.getPage(_mIndexTouchText);
		int numberText = pageData.mStringText.length;
		isTextAppear = new boolean[numberText];
		for (int i = 0; i < numberText; i++) {
			isTextAppear[i] = false;
		}
	}

	RectF rectDstBox = new RectF();
	RectF rectDstLeft = new RectF();
	RectF rectDstRight = new RectF();

	public void drawTextAppear(Canvas cv, Paint paint) {
		PageData pageData = mPagesManager.getPage(mIndexTouchText);
		float ratioX = (float) getWidth() / (float) mBackground.getWidth();
		float ratioY = (float) getHeight() / (float) mBackground.getHeight();
		if (pageData != null) {
			for (int i = 0; i < pageData.mRectBoxText.length; i++) {
				if (isTextAppear[i]) {
					rectDstLeft.left = pageData.mRectBoxText[i].left * ratioX;
					rectDstBox.top = rectDstRight.top = rectDstLeft.top = pageData.mRectBoxText[i].top
							* ratioY;
					rectDstLeft.right = rectDstLeft.left
							+ pageData.mRectBoxText[i].height() * ratioY / 2;
					rectDstBox.bottom = rectDstRight.bottom = rectDstLeft.bottom = pageData.mRectBoxText[i].bottom
							* ratioY;
					rectDstRight.left = pageData.mRectBoxText[i].right * ratioX
							- pageData.mRectBoxText[i].height() * ratioY / 2;
					rectDstRight.right = pageData.mRectBoxText[i].right
							* ratioX;
					rectDstBox.left = rectDstLeft.right - 0.3f;
					rectDstBox.right = rectDstRight.left + 0.3f;
					cv.drawBitmap(mBoxTextLeft,
							new Rect(0, 0, mBoxTextLeft.getWidth(),
									mBoxTextLeft.getHeight()), rectDstLeft,
							paint);
					cv.drawBitmap(mBoxText, new Rect(0, 0, mBoxText.getWidth(),
							mBoxText.getHeight()), rectDstBox, paint);
					cv.drawBitmap(mBoxTextRight,
							new Rect(0, 0, mBoxTextRight.getWidth(),
									mBoxTextRight.getHeight()), rectDstRight,
							paint);
					drawTextAppearAfterTouch(cv, mIndexTouchText, i);
				}
			}
		}
	}

	private void drawTextAppearAfterTouch(Canvas canvas, int pageIndex,
			int index) {
		float ratioX = (float) getWidth() / mBackground.getWidth();
		float ratioY = (float) getHeight() / mBackground.getHeight();
		PageData pageData = mPagesManager.getPage(pageIndex);
		if (pageData == null)
			return;
		canvas.save();
		Paint paint = new Paint();
		paint.setTypeface(pageData.mTypefaceTextTouch);
		paint.setTextSize(pageData.mFontSize - 1);
		paint.setColor(0xFFFFFFFF);
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setSubpixelText(true);
		paint.setFlags(paint.getFlags() & ~Paint.DEV_KERN_TEXT_FLAG);
		canvas.scale(ratioX, ratioY);
		canvas.translate(pageData.mRectBoxText[index].left,
				pageData.mRectBoxText[index].top
						- paint.getFontMetrics().ascent);
		canvas.drawText(pageData.mStringText[index], 10, 10, paint);
		canvas.restore();
	}

	public boolean onEventTexAppear() {
		float ratioX = (float) getWidth() / mBackground.getWidth();
		float ratioY = (float) getHeight() / mBackground.getHeight();
		RectF rectTouch = new RectF();
		PageData pageData = mPagesManager.getPage(mIndexTouchText);
		for (int i = 0; i < pageData.mRectTouch.size(); i++) {
			for (int j = 0; j < pageData.mRectTouch.get(i).length; j++) {
				Rect mRect = pageData.mRectTouch.get(i)[j];
				rectTouch.left = mRect.left * ratioX;
				rectTouch.top = mRect.top * ratioY;
				rectTouch.right = mRect.right * ratioX;
				rectTouch.bottom = mRect.bottom * ratioY;
				if (checkScopeTouch(rectTouch, mFinger.x, mFinger.y)) {
					// appear text to in screen.
					if (isTextAppear != null
							&& !pageData.mStringText[i].equals("NONE TEXT")) {
						isTextAppear[i] = true;
					}
					if (mPageChangedListener != null)
						mPageChangedListener.onTab(pageData.idSound[i]);
					return true;
				}
			}
		}
		return false;
	}

	public int mIndexAnimation;
	private ArrayList<Bitmap[]> mBitmapBlinking;
	private ArrayList<RectF[]> mRectFDst;
	private ArrayList<Rect[]> mRectSrc;
	private Bitmap[] mBitmapBlingItem;
	private RectF[] mRectFDstItem;
	private Rect[] mRectSrcItem;
	private BlinkingEyeHanler mBlinkingEyeHandler = new BlinkingEyeHanler();
	private int indexAnimation;
	private int idAnimation;

	class BlinkingEyeHanler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			blinkingEye();
			indexAnimation++;
			if (indexAnimation < mPagesManager.getPage(mIndexAnimation).mOrderAnimation
					.size())
				setIdAnimation(indexAnimation);
		}

		public void sleep(long millis) {
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), millis);
		}
	}

	public void setIdAnimation(int _indexAnimation) {
		idAnimation = mPagesManager.getPage(mIndexAnimation).mOrderAnimation
				.get(_indexAnimation);
	}

	public void blinkingEye() {
		if (indexAnimation >= mPagesManager.getPage(mIndexAnimation).mOrderAnimation
				.size()) {
		} else {
			mBlinkingEyeHandler
					.sleep(mPagesManager.getPage(mIndexAnimation).mTimeAnimation
							.get(indexAnimation));
		}

	}

	public void initBlinking() {
		indexAnimation = 0;
		idAnimation = mPagesManager.getPage(mIndexAnimation).mOrderAnimation
				.get(indexAnimation);
		doLoadBlinking(mIndexAnimation);
		doLoadBlinking(mIndexAnimation + 1);
		mBitmapBlingItem = mBitmapBlinking.get(mIndexAnimation);
		mRectFDstItem = mRectFDst.get(mIndexAnimation);
		mRectSrcItem = mRectSrc.get(mIndexAnimation);
		cleanupUnusedBitmapBlinking(mIndexAnimation);
	}

	public void resetBlinking() {
		indexAnimation = 0;
		idAnimation = mPagesManager.getPage(mIndexAnimation).mOrderAnimation
				.get(indexAnimation);
		int prev = mIndexAnimation - 1;
		int next = mIndexAnimation + 1;
		if (prev >= 0 && mBitmapBlinking.get(prev) == null) {
			doLoadBlinking(prev);
		}
		if (next < mBitmapBlinking.size() && mBitmapBlinking.get(next) == null) {
			doLoadBlinking(next);
		}
		mBitmapBlingItem = mBitmapBlinking.get(mIndexAnimation);
		mRectFDstItem = mRectFDst.get(mIndexAnimation);
		mRectSrcItem = mRectSrc.get(mIndexAnimation);
		cleanupUnusedBitmapBlinking(mIndexAnimation);
		myThread.resetPage();
	}

	private void cleanupUnusedBitmapBlinking(int index) {
		for (int i = 0; i < PagesManager.PAGE_COUNT; ++i) {
			if (i < (index - 1) || i > (index + 1)) {
				Bitmap[] mBitmapDelete = mBitmapBlinking.get(i);
				if (mBitmapDelete != null) {
					for (int j = 0; j < mBitmapDelete.length; j++) {
						Bitmap picToDelete = mBitmapDelete[j];
						if (picToDelete != null) {
							picToDelete.recycle();
							picToDelete = null;
							mBitmapDelete[j] = null;
						}
					}
					mBitmapBlinking.set(i, null);
				}
			}
		}
	}

	Bitmap[] mBitmapAnim;

	private void doLoadBlinking(int index) {
		RectF[] rectDst;
		Rect[] rectSrc;
		if (index >= 0 && index < mBitmapBlinking.size()
				&& mBitmapBlinking.get(index) == null) {
			PageData pageData = mPagesManager.getPage(index);
			float ratioX = (float) getWidth() / (float) mForeground.getWidth();
			float ratioY = (float) getHeight()
					/ (float) mForeground.getHeight();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inScaled = false;
			mBitmapAnim = new Bitmap[pageData.mBitmapAnimationId.length];
			rectDst = new RectF[pageData.mBitmapAnimationId.length];
			rectSrc = new Rect[pageData.mBitmapAnimationId.length];
			for (int i = 0; i < pageData.mBitmapAnimationId.length; i++) {
				Bitmap readOnlyBmp = BitmapFactory.decodeResource(
						getResources(), pageData.mBitmapAnimationId[i], opts);
				mBitmapAnim[i] = readOnlyBmp.copy(Bitmap.Config.RGB_565, true);
				readOnlyBmp.recycle();
				readOnlyBmp = null;
				rectSrc[i] = new Rect(0, 0, mBitmapAnim[i].getWidth(),
						mBitmapAnim[i].getHeight());
				rectDst[i] = new RectF();
				rectDst[i].left = pageData.mRectAnimation[i].left * ratioX;
				rectDst[i].top = pageData.mRectAnimation[i].top * ratioY;
				rectDst[i].right = pageData.mRectAnimation[i].right * ratioX;
				rectDst[i].bottom = pageData.mRectAnimation[i].bottom * ratioY;
			}
			mBitmapBlinking.set(index, mBitmapAnim);
			mRectFDst.set(index, rectDst);
			mRectSrc.set(index, rectSrc);
		}
	}

	public void drawBlinking(Canvas canvas, Paint paint) {
		if (idAnimation > 0)
			canvas.drawBitmap(mBitmapBlingItem[idAnimation - 1],
					mRectSrcItem[idAnimation - 1],
					mRectFDstItem[idAnimation - 1], paint);
	}

	public final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			PageCurlView.this.invalidate();
			super.handleMessage(msg);
		}

	};

	class MyThread implements Runnable {
		long beginTimepage;
		long currentTimePage;
		boolean running;
		boolean isStopage;

		public MyThread() {

			running = true;
			resetPage();
			new Thread(MyThread.this).start();
		}

		public void resetPage() {
			isStopage = true;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			beginTimepage = System.currentTimeMillis();
			while (running) {
				while (isStopage) {
					currentTimePage = System.currentTimeMillis();
					if (mIndexAnimation != 21
							&& (currentTimePage - beginTimepage >= mPagesManager
									.getPage(mIndexAnimation).mTimeAnimation
									.get(indexAnimation))) {
						beginTimepage = currentTimePage;
						indexAnimation++;
						if (indexAnimation < mPagesManager
								.getPage(mIndexAnimation).mOrderAnimation
								.size()) {
							setIdAnimation(indexAnimation);
							mHandler.sendEmptyMessage(0);

						} else {
							isStopage = false;
						}
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					// Log.e("MMMMMMMMMMMMM", "index "+mIndexAnimation);
//					Log.e("IIIII", "id " + indexAnimation);
					e.printStackTrace();
				}
			}
		}
	}

	public int getmIndexAnimation() {
		return mIndexAnimation;
	}

	public void setmIndexAnimation(int mIndexAnimation) {
		this.mIndexAnimation = mIndexAnimation;
	}

	public void recycle() {
		for (int i = 0; i < mPages.size(); i++) {
			if (mPages.get(i) != null)
				mPages.get(i).recycle();
			mPages.remove(i);
		}
		mBackground = null;
		mForeground = null;
		if (mBitmapBlinking != null) {
			for (int i = 0; i < mBitmapBlinking.size(); i++) {
				for (int j = 0; mBitmapBlinking.get(i) != null
						&& j < mBitmapBlinking.get(i).length; j++) {
					mBitmapBlinking.get(i)[j].recycle();
					mBitmapBlinking.get(i)[j] = null;
				}
				// mBitmapBlinking.remove(i);
			}
			mBitmapBlinking.clear();
		}
	}
}
