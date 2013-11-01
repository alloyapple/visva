package vn.com.shoppie.view;

import vn.com.shoppie.R;
import vn.com.shoppie.view.MScrollView.OnReachBottom;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class MPager extends RelativeLayout{
	public static final short SLIDE_UP = 0;
	public static final short SLIDE_DOWN = 1;
	public static final short SLIDE_IN = 0;
	public static final short SLIDE_OUT = 1;

	private short slideMode = 0;
	private short inoutMode = 0;

	private int currentItem = 0;
	private MPagerAdapterBase mAdapter;
	
	private float downX;
	private float downY;

	private boolean isSlide = false;
	private boolean isAutoSlide = false;
	private boolean isOpenSlide = true;
	private boolean isOpenMoveSlide = true;
	private boolean isOpenCollapse = false;

	private View currentSlide;

	int distance = 600;
	int distanceX = 600;
	int distanceY = 600;
	float angle = 30;
	float angleY = 30;

	float currentX = 0;
	float currentY = 0;
	float currentAngle = 0;

	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;

	private GestureDetector mGestureDetector;
	private GestureDetector mCollapseGestureDetector;
	
	private OnStartExtend onStartExtend;
	public MPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		if(mGestureDetector == null){
			mGestureDetector = new GestureDetector(getContext() , new GestureListener());
		}
		
		if(mCollapseGestureDetector == null){
			mCollapseGestureDetector = new GestureDetector(getContext() , new CollapseGestureListenner());
		}
		
		initLayout();
	}

	public MPager(Context context) {
		super(context);
		if(mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		if(mGestureDetector == null){
			mGestureDetector = new GestureDetector(getContext() , new GestureListener());
		}
		
		if(mCollapseGestureDetector == null){
			mCollapseGestureDetector = new GestureDetector(getContext() , new CollapseGestureListenner());
		}
		
		initLayout();
	}

	private MScrollView scrollView;
	private LinearLayout container1;
	private RelativeLayout container;
	private LinearLayout layout1;
	
	private void initLayout() {
		layout1 = new LinearLayout(getContext());
		layout1.setGravity(Gravity.CENTER);
		addView(layout1 , -1 , -1);
		
		scrollView = new MScrollView(getContext());
		layout1.addView(scrollView, -1, -2);

		container1 = new LinearLayout(getContext());
		addView(container1, -1, -1);

		container = new RelativeLayout(getContext());
		container.setGravity(Gravity.CENTER);
		container1.addView(container, -1, -1);

		//add 3 views to circle
		View view1 = new View(getContext()); view1.setBackgroundColor(0xffff0000);
		View view2 = new View(getContext()); view2.setBackgroundColor(0xff00ff00);
		View view3 = new View(getContext()); view3.setBackgroundColor(0xff0000ff);
		container.addView(view1);
		container.addView(view2);
		container.addView(view3);
	}

	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		init();
	}

	public void init(){
		mScroller = new Scroller(getContext(), sInterpolator);
		distance = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 40;
		distanceX = distance;
		distanceX = distance / 2;

		currentSlide = container.getChildAt(container.getChildCount() - 1);
	}
	
	public void setAdapter(MPagerAdapterBase adapter){
		this.mAdapter = adapter;
		
		if(getChildAt(0) != container1 || getChildAt(0) != layout1){
			View v = new View(getContext());
			v.setBackgroundResource(R.drawable.bg_center);
			LayoutParams params = new LayoutParams((int)(mAdapter.getViewWidth() * 1.06f), 
					(int)(mAdapter.getViewHeight() * 1.05f));
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			addView(v, 0 , params);
		}
		
		container.removeAllViews();
		if(adapter.getCount() > 0){
			for(int i = 0 ; i < mAdapter.getCount() && i < 3 ; i++)
				container.addView(new View(getContext()));
			initByAdapter();
		}
	}
	
	private void initByAdapter(){
		currentItem = 0;
		addViewTo(mAdapter.getView(currentItem), container.getChildCount() - 1);
		if(container.getChildCount() > 1)
			addViewTo(mAdapter.getBackView(currentItem), container.getChildCount() - 2);
		addViewTo(mAdapter.getNextView(currentItem), 0);
	}
	
	private void cacheNextView(){
		addViewTo(mAdapter.getNextView(currentItem), 0);
	}
	
	private void cacheBackView(){
		addViewTo(mAdapter.getBackView(currentItem), container.getChildCount() - 2);
	}
	
	private void addViewTo(View view , int pos){
		LayoutParams params = new LayoutParams(mAdapter.getViewWidth(), mAdapter.getViewHeight());
		container.removeViewAt(pos);
		container.addView(view, pos , params);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		switch (ev.getAction()) {
//		case MotionEvent:
//			
//			break;
//
//		default:
//			break;
//		}
		if(isSlide)
			return true;
		else{
			onTouchEvent(ev);
			return false;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isAutoSlide)
			return true;
		
		if(event.getPointerCount() > 1)
			return false;
		mVelocityTracker.addMovement(event);
		if(container.getParent() != container1){
			isOpenCollapse = false;
			if(mCollapseGestureDetector.onTouchEvent(event))
				return true;
			return super.onTouchEvent(event);
		}
		if(mAdapter == null)
			return super.onTouchEvent(event);
		if(mAdapter.getCount() <= 1)
			return super.onTouchEvent(event);

		
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			Log.d("UP", "UP");
			autoSlide();
			return true;
		case MotionEvent.ACTION_CANCEL:
			Log.d("UP", "CANCEL");
			autoSlide();
			return true;

		default:
			break;
		}
		return true;
	}

	private void autoSlide() {
		Log.d("Auto", "Slide " + currentX + " " + distanceX);
		if(!isSlide)
			return;
		if(inoutMode == SLIDE_IN){
			if(currentX > 0.2f * distanceX)
				mScroller.startScroll((int) currentX, 0, distanceX, 100 , 750);
			else
				mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 750);
		}
		else{
			if(currentX > 0.7f * distanceX)
				mScroller.startScroll((int) currentX, 0, distanceX, 100 , 750);
			else
				mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 750);
		}
		invalidate();
		isAutoSlide = true;
		isOpenMoveSlide = false;
		isSlide = false;
	}

	/**
	 * 
	 * @param value 0 if collapse. other if extend
	 */
	private void extendView(int value){
		for (int i = container.getChildCount() - 1 ; i >= 0 ; i--) {
			View v = container.getChildAt(i);
			MarginLayoutParams params = (MarginLayoutParams) v.getLayoutParams();
			if(value == 0)
				params.topMargin = (i) * value;
			else{
				params.topMargin = (i) * mAdapter.getTitlePadding();
			}
		}
		
		container.requestLayout();
	}

	@Override
	public void computeScroll() {

		if(mScroller != null){
			if(!mScroller.isFinished()){
				if(mScroller.computeScrollOffset()){
					currentX = mScroller.getCurrX();
					if(currentX >= distanceX){
						currentX = distanceX;
						updateSlideLefRight();
						finishDrag();
						mScroller.abortAnimation();
					}
					else if(currentX < 0){
						currentX = 0;
						updateSlideLefRight();
						finishDrag();
						mScroller.abortAnimation();
					}
					else{
						updateSlideLefRight();
					}
					postInvalidate();
					//					invalidate();
				}
			}
		}
		super.computeScroll();
	}

	private void startSlideOut() {
		//ObjectAnimator.clearAllAnimations();
		inoutMode = SLIDE_OUT;
		currentSlide = container.getChildAt(container.getChildCount() - 1);

		if(slideMode == SLIDE_DOWN)
			distance = (getWidth() - currentSlide.getWidth()) / 2 + currentSlide.getWidth() * 5 / 4;
		else
			distance = (getHeight() - currentSlide.getHeight()) / 2 + currentSlide.getHeight() * 5 / 4;
		distanceX = distance;
		distanceY = (int) fx(distanceX);
		currentX = distanceX;

		currentX = distanceX;
	}

	private void startSlideIn() {
		inoutMode = SLIDE_IN;

		currentSlide = container.getChildAt(0);

		if(slideMode == SLIDE_DOWN)
			distance = (getWidth() - currentSlide.getWidth()) / 2 + currentSlide.getWidth() * 5 / 4;
		else
			distance = (getHeight() - currentSlide.getHeight()) / 2 + currentSlide.getHeight() * 5 / 4;
		distanceX = distance;
		distanceY = (int) fx(distanceX);
		currentX = 0;

		//ObjectAnimator.clearAllAnimations();
		
		AnimatorSet set1 = new AnimatorSet();
		set1.playTogether(
				ObjectAnimator.ofFloat(currentSlide, "rotation", angle()),
				ObjectAnimator.ofFloat(currentSlide, "translationX", -distance * 2),
				ObjectAnimator.ofFloat(currentSlide, "translationY", -distance * 2)
				);
		set1.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				// TODO Auto-generated method stub
				container.removeView(currentSlide);
				container.addView(currentSlide, container.getChildCount());
				currentX = 0;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});
		set1.setDuration(0).start();
	}

	//	private void updateSlideUpDown(MotionEvent event){
	//		value -= (event.getY() - lastY) / 5.5f;
	//		if(value > 100)
	//			value = 100;
	//		else if(value < 1)
	//			value = 1;
	//		
	//		updateSlideUpDown(value);
	//	}

	//	private void updateSlideLeftRight(MotionEvent event){
	//		value += (event.getX() - lastX) / 5.5f;
	//		if(value > 100)
	//			value = 100;
	//		else if(value < 1)
	//			value = 1;
	//
	//		updateSlideLefRight(value);
	//	}

	private void updateSlideLeftRight1(float distanceX){
		currentX += distanceX;

		if(currentX > this.distanceX)
			currentX = this.distanceX;
		else if(currentX < 1)
			currentX = 1;

		updateSlideLefRight();
	}
	
	private void clearAnimator(View v){
		AnimatorSet set = new AnimatorSet();
		set.playTogether(
				ObjectAnimator.ofFloat(v, "rotation", 0),
				ObjectAnimator.ofFloat(v, "translationX", 0),
				ObjectAnimator.ofFloat(v, "translationY", 0)
				);

		set.setDuration(0).start();
		
	}
	
	private void finishDrag(){
		Log.d("Finish", "Drag");
		if(currentX == 0){
			switch (inoutMode) {
			case SLIDE_IN://fail
				container.removeView(currentSlide);
				container.addView(currentSlide, 0);
//				addViewTo(currentSlide, 0);
				break;

			case SLIDE_OUT://success
				currentItem = mAdapter.getBackItemId(currentItem);
				container.removeView(currentSlide);
				container.addView(currentSlide, 0);
				cacheNextView();
				cacheBackView();
				break;
			default:
				break;
			}
		}
		else if(currentX == distanceX){
			switch (inoutMode) {
			case SLIDE_IN://success
				currentItem = mAdapter.getNextItemId(currentItem);
				cacheNextView();
				cacheBackView();
				break;

			case SLIDE_OUT://fail
				
				break;
			default:
				break;
			}
		}
		clearAnimator(currentSlide);
		
		isAutoSlide = false;
		Log.d("CurrentItem", "" + currentItem);
	}

	public int getSlideType(){
		return slideMode;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void updateSlideLefRight() {
		float t = (distanceX - currentX) / distanceX;
		currentAngle = angle() * t;
		//		currentX = -distance * t;
		currentY = fx(currentX);
		
		if(Build.VERSION.SDK_INT < 11){
			AnimatorSet set = new AnimatorSet();
			set.playTogether(
					ObjectAnimator.ofFloat(currentSlide, "rotation", currentAngle),
					ObjectAnimator.ofFloat(currentSlide, "translationX", currentX - distanceX),
					ObjectAnimator.ofFloat(currentSlide, "translationY", currentY - distanceY)
					);
			set.setDuration(0).start();
		}
		else{
			currentSlide.setRotation(currentAngle);
			currentSlide.setTranslationX(currentX - distanceX);
			currentSlide.setTranslationY(currentY - distanceY);
		}
		
	}

	public void updateSlideUpDown(int value) {
		Log.d("Update", "Value " + value);
		float t = (float) (100 - value) / 100;
		currentAngle = angleY * t;
		currentX = -distance / 3 * t;
		currentY = fx(currentX);
		AnimatorSet set = new AnimatorSet();
		set.playTogether(
				ObjectAnimator.ofFloat(currentSlide, "rotation", currentAngle),
				ObjectAnimator.ofFloat(currentSlide, "translationX", currentX),
				ObjectAnimator.ofFloat(currentSlide, "translationY", currentY)
				);
		set.setDuration(0).start();
	}


	int pos = 0;

	public void addViewBySet(int lastPos , boolean isFirst){
		int count = container.getChildCount() + 9;
		int realCount = 0;
		for(int i = container.getChildCount() ; i < count && i < mAdapter.getCount() ; i++){
			container.addView(mAdapter.getView(i) , mAdapter.getViewWidth() , mAdapter.getViewHeight());
			realCount++;
		}
		
		extendView(40);
		int scrollHeight = container.getChildAt(1).getHeight();
		MarginLayoutParams params = (MarginLayoutParams) container.getChildAt(container.getChildCount() - 1).getLayoutParams();
		scrollHeight += params.topMargin;
		
		int topScroll = 0;
		if(scrollHeight >= getHeight())
			topScroll = 0;
		else
			topScroll = (getHeight() - scrollHeight) / 2;
		
		if(isFirst){
			for(int i = 0 ; i < container.getChildCount() ; i++){
				View v = container.getChildAt(i);
				params = (MarginLayoutParams) v.getLayoutParams();
				ObjectAnimator.ofFloat(v, "translationY", -(topScroll + params.topMargin - lastPos), 0).setDuration(500).start();
			}
			scrollView.setOnReachBottom(new OnReachBottom() {
				
				@Override
				public void onReachBottom() {
					addViewBySet(-1, false);
				}
			});
			scrollView.setStopScroll(false);
		}
		else if(realCount > 0){
			scrollView.setStopScroll(true);
			for(int i = container.getChildCount() - realCount + 1 ; i < container.getChildCount() ; i++){
				ObjectAnimator.ofFloat(container.getChildAt(i), "translationY", mAdapter.getViewHeight() - mAdapter.getTitlePadding(), 0).setDuration(350).start();
			}
			ObjectAnimator a = ObjectAnimator.ofFloat(container.getChildAt(container.getChildCount() - realCount), "translationY", mAdapter.getViewHeight() - mAdapter.getTitlePadding(), 0).setDuration(350);
			a.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator arg0) {
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0) {
				}
				
				@Override
				public void onAnimationEnd(Animator arg0) {
					scrollView.setStopScroll(false);
				}
				
				@Override
				public void onAnimationCancel(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			a.start();
		}
	}
	
	public void extendView(){
		if(container.getParent() == container1 && container.getChildCount() > 2){
			if(onStartExtend != null){
				onStartExtend.onExtend(this);
			}
			
			int lastPos = getHeight() / 2 - container.getChildAt(0).getHeight() / 2;
			
			isOpenSlide = false;
			isOpenCollapse = false;
			container1.removeView(container);
			scrollView.addView(container , -1 , -2);
			scrollView.scrollTo(0, 0);
			container.removeAllViews();
			removeViewAt(0);
			
			addViewBySet(lastPos , true);
		}
		else
			return;
		if(pos > 0)
			return;
	}

	public void collapseView(){
		if(!isOpenCollapse)
			return;
		if(container.getParent() == scrollView){
			if(onStartExtend != null){
				onStartExtend.onCollapse(this);
			}
			scrollView.setStopScroll(true);
			scrollView.scrollTo(0, 0);
			for(int i = 5 ; i < container.getChildCount() ; i++){
				container.removeViewAt(i);
			}
			
			isOpenSlide = false;
			isOpenMoveSlide = false;
			
			int containerTop = scrollView.getTop();
			int newTop = (getHeight() - container.getChildAt(1).getHeight()) / 2;
			int lastPos[] = new int[container.getChildCount()];
			for(int i = 0 ; i < container.getChildCount() ; i++){
				MarginLayoutParams params = (MarginLayoutParams) container.getChildAt(i).getLayoutParams();
				lastPos[i] = -(newTop - (containerTop + params.topMargin));
			}
			
			extendView(0);
			scrollView.removeAllViews();
			container1.addView(container , -1 , -1);
			
			ObjectAnimator a = ObjectAnimator.ofFloat(container.getChildAt(0), "translationY", lastPos[0] , 0).setDuration(500);
			a.addListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator arg0) {
					setAdapter(mAdapter);
					isOpenSlide = true;
				}
				
				@Override
				public void onAnimationCancel(Animator arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			a.start();
			
			for(int i = 1 ; i < container.getChildCount() ; i++){
				ObjectAnimator.ofFloat(container.getChildAt(i), "translationY", lastPos[i] , 0).setDuration(500).start();
			}
			lastPos = null;
		}
		else
			return;

		if(pos < 40)
			return;
	}

	class GestureListener extends GestureDetector.SimpleOnGestureListener {
		
		@Override
		public boolean onSingleTapUp(MotionEvent ev) {

			return false;
		}

		@Override
		public void onShowPress(MotionEvent ev) {

		}

		@Override
		public void onLongPress(MotionEvent ev) {

		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent event, float distanceX, float distanceY) {
			if(!isOpenMoveSlide)
				return false;
			if(!isSlide){
				int minSlide = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 3/2;
				Log.d("minSlide", "" + minSlide);
				if(Math.abs(distanceX) < minSlide && Math.abs(event.getX() - downX) < minSlide){
					if(Math.abs(event.getY() - downY) > minSlide * 10){
						extendView();
					}
				}
				else{
					if(distanceY < 0){
						if(event.getX() > downX){
							slideMode = SLIDE_DOWN;
							if(container.getChildCount() <= 1)
								return false;
							startSlideIn();
						}
						else{
							slideMode = SLIDE_UP;
							if(container.getChildCount() <= 2)
								return false;
							startSlideOut();
						}
					}
					else{
						if(event.getX() > downX){
							slideMode = SLIDE_UP;
							if(container.getChildCount() <= 1)
								return false;
							startSlideIn();
						}
						else{
							slideMode = SLIDE_DOWN;
							if(container.getChildCount() <= 2)
								return false;
							startSlideOut();
						}
					}
					isSlide = true;
				}
			}
			else{
				if(currentX < MPager.this.distanceX / 5)
					distanceX *= 3;
				else if(currentX < MPager.this.distanceX / 4)
					distanceX *= 2.5f;
				else if(currentX < MPager.this.distanceX / 3)
					distanceX *= 2;
				else if(currentX < MPager.this.distanceX / 2)
					distanceX *= 1.5f;
				switch (slideMode) {
				case SLIDE_UP:
					updateSlideLeftRight1(-distanceX);
					break;
				case SLIDE_DOWN:
					updateSlideLeftRight1(-distanceX);
					break;

				default:
					break;
				}
			}

			return false;
		}

		@Override
		public boolean onDown(MotionEvent event) {
			Log.d("OnDown", "OnDown");
			if(isOpenSlide)
				isOpenMoveSlide = true;
			else{
				isOpenMoveSlide = false;
				return true;
			}
			if(mScroller.isFinished()){
				isSlide = false;
				downX 	= event.getX();
				downY 	= event.getY();
			}
			else{
				Log.d("Touch", "Continue");
				mScroller.abortAnimation();
				isSlide = true;
			}
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//			if(velocityX > 100){
//				if(!isSlide){
//					if(velocityY > 0){
//						slideMode = SLIDE_DOWN;
//					}
//					else{
//						slideMode = SLIDE_UP;
//					}
//					if(e2.getX() > downX){
//						if(container.getChildCount() <= 1)
//							return false;
//						startSlideIn();
//					}
//					else{
//						if(container.getChildCount() <= 2)
//							return false;
//						startSlideOut();
//					}
//					isSlide = true;
//					//					value = 0;
//				}
//				mScroller.startScroll((int) currentX, 0, distanceX, 100 , 750);
//				invalidate();
//			}
//			else if(velocityX < -100){
//
//				if(!isSlide){
//					if(true){
//						slideMode = SLIDE_DOWN;
//						if(e2.getX() > downX){
//							if(container.getChildCount() <= 1)
//								return false;
//							startSlideIn();
//						}
//						else{
//							if(container.getChildCount() <= 2)
//								return false;
//							startSlideOut();
//						}
//					}
//					isSlide = true;
//					//					value = 0;
//				}
//				mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 750);
//				invalidate();
//			}
			return false;
		}
	}
	
	class CollapseGestureListenner extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			Log.d("Fling", "" + velocityY);
			if(velocityY < -10000){
				scrollView.scrollTo(scrollView.getScrollX(), scrollView.getScrollY());
				isOpenCollapse = true;
				collapseView();
			}
			return true;
		}
	}
	
	private float angle(){
		if(slideMode == SLIDE_DOWN)
			return angle;
		else
			return -angle;
	}
	
	private float fx(float x){
		if(slideMode == SLIDE_DOWN)
			return x / 3;
		else
			return -x / 3;
	}
	
	public void setOnStartExtendListenner(OnStartExtend onStartExtend) {
		this.onStartExtend = onStartExtend;
	}

	private static final Interpolator sInterpolator = new Interpolator() {
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t * t * t + 1.0f;
		}
	};
	
	public interface OnStartExtend{
		public void onExtend(View v);
		public void onCollapse(View v);
	}
}
