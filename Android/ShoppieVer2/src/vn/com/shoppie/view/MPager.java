package vn.com.shoppie.view;

import java.util.Calendar;

import vn.com.shoppie.R;
import vn.com.shoppie.util.log;
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
import android.view.animation.DecelerateInterpolator;
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
	public int getCurrentItem() {
		return currentItem;
	}

	private MPagerAdapterBase mAdapter;
	private View cover;
	
	private float downX;
	private float downY;

	private boolean isSlideOnScroll = false;
	private boolean isSlide = false;
	private boolean isAutoSlide = false;
	private boolean isOpenSlide = true;
	private boolean isOpenMoveSlide = true;
	private boolean isOpenCollapse = false;
	private boolean canbeExtended = true;
	private boolean lockSlide = false;
	private boolean isFinishDrag = true;
	
	private long lastAutoSlideTime = 0;
	
	public void setLockSlide(boolean lockSlide) {
		this.lockSlide = lockSlide;
	}

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
	private OnPageChange onPageChange;
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
	private LinearLayout container2;
	private RelativeLayout container;
	private LinearLayout layout1;
	
	private void initLayout() {
		layout1 = new LinearLayout(getContext());
		layout1.setGravity(Gravity.CENTER);
		addView(layout1 , -1 , -1);
		
		scrollView = new MScrollView(getContext());
		container2 = new LinearLayout(getContext());
		scrollView.addView(container2);
		layout1.addView(scrollView, -1, -2);

		container1 = new LinearLayout(getContext());
		addView(container1, -1, -1);

		container = new RelativeLayout(getContext());
		container.setGravity(Gravity.CENTER);
		container1.addView(container, -1, -1);

		//add 3 views to circle
		View view1 = new View(getContext());
		View view2 = new View(getContext());
		View view3 = new View(getContext());
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
		mScroller = new Scroller(getContext(), new DecelerateInterpolator(2.0f));
		distance = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 40;
		distanceX = distance;
		distanceX = distance / 2;

		currentSlide = container.getChildAt(container.getChildCount() - 1);
	}
	
	public void setAdapter(MPagerAdapterBase adapter){
		this.mAdapter = adapter;
		
		if(getChildAt(0) != container1 || getChildAt(0) != layout1){
			if(getChildAt(0) != cover) {
				cover = new View(getContext());
				cover.setBackgroundResource(R.drawable.bg_center);
				LayoutParams params = new LayoutParams((int)(mAdapter.getViewWidth() * 1.06f), 
						(int)(mAdapter.getViewHeight() * 1.05f));
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				addView(cover, 0 , params);
			}
		}
		
		container.removeAllViews();
		if(adapter.getCount() > 0){
			for(int i = 0 ; i < mAdapter.getCount() && i < 3 ; i++)
				container.addView(new View(getContext()));
			initByAdapter();
		}
	}
	
	public void setAdapter(MPagerAdapterBase adapter , int initIndex){
		this.mAdapter = adapter;
		
		if(getChildAt(0) != container1 || getChildAt(0) != layout1){
			if(getChildAt(0) != cover) {
				cover = new View(getContext());
				cover.setBackgroundResource(R.drawable.bg_center);
				LayoutParams params = new LayoutParams((int)(mAdapter.getViewWidth() * 1.06f), 
						(int)(mAdapter.getViewHeight() * 1.05f));
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				addView(cover, 0 , params);
				ObjectAnimator.ofFloat(cover, "alpha", 0 , 1f).setDuration(500).start();
			}
		}
		
		container.removeAllViews();
		if(adapter.getCount() > 0){
			for(int i = 0 ; i < mAdapter.getCount() && i < 3 ; i++)
				container.addView(new View(getContext()));
//			initByAdapter();
			initByAdapter(initIndex);
		}
	}
	
	private void initByAdapter(){
		currentItem = 0;
		addViewTo(mAdapter.getView(currentItem), container.getChildCount() - 1);
		if(mAdapter.getCount() > 1) {
			if(container.getChildCount() > 1)
				addViewTo(mAdapter.getBackView(currentItem), 0);
			addViewTo(mAdapter.getNextView(currentItem), container.getChildCount() - 2);
		}
	}
	
	private void initByAdapter(int initIndex){
		currentItem = initIndex;
		addViewTo(mAdapter.getView(currentItem), container.getChildCount() - 1);
		if(mAdapter.getCount() > 1) {
			if(container.getChildCount() > 1)
				addViewTo(mAdapter.getBackView(currentItem), 0);
			addViewTo(mAdapter.getNextView(currentItem), container.getChildCount() - 2);
		}
	}
	
	private void cacheNextView(){
		addViewTo(mAdapter.getNextView(currentItem), container.getChildCount() - 2);
	}
	
	private void cacheBackView(){
		addViewTo(mAdapter.getBackView(currentItem), 0);
	}
	
	private void addViewTo(View view , int pos){
		LayoutParams params = new LayoutParams(mAdapter.getViewWidth(), mAdapter.getViewHeight());
		container.removeViewAt(pos);
		container.addView(view, pos , params);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(container.getParent() != container1) {
			if(isSlideOnScroll)
				return true;
			else {
				onTouchEvent(ev);
				return super.onInterceptTouchEvent(ev);
			}
		}
		if(!isEnable)
			return true;
		if(lockSlide)
			return super.onInterceptTouchEvent(ev);
		if(isSlide)
			return true;
		if(isSlideOnScroll && container.getParent() != container1)
			return true;
		else{
			onTouchEvent(ev);
			
			if(container.getParent() != container1){
				return super.onInterceptTouchEvent(ev);
			}
			if(Math.abs(ev.getY() - downY) > 10)
				return true;
			return false;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getPointerCount() > 1)
			return true;
		mVelocityTracker.addMovement(event);
		if(container.getParent() != container1){
			isOpenCollapse = false;
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downX = event.getX();
				downY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if(event.getX() - downX > 20 || event.getY() - downY > 20)
					isSlideOnScroll = true;
				break;
			case MotionEvent.ACTION_UP:
				isSlideOnScroll = false;
				break;

			default:
				break;
			}
			
			if(mCollapseGestureDetector.onTouchEvent(event))
				return true;
			return true;
		}
		if(!isEnable)
			return true;
		if(lockSlide)
			return true;
		if(isAutoSlide)
			return true;
		
		if(mAdapter == null)
			return super.onTouchEvent(event);
		if(mAdapter.getCount() <= 1)
			return super.onTouchEvent(event);

		mVelocityTracker.computeCurrentVelocity(1000);
		mGestureDetector.onTouchEvent(event);
		if(isOpenMoveSlide) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				log.d("UP", "UP");
				autoSlide((int) mVelocityTracker.getXVelocity());
				return true;
			case MotionEvent.ACTION_CANCEL:
				log.d("UP", "CANCEL");
				autoSlide((int) mVelocityTracker.getXVelocity());
				return true;
	
			default:
				break;
			}
		}
		return true;
	}

	private void autoSlide(int velocityX) {
		if(isAutoSlide)
			return;
		log.d("Auto", "Slide " + currentX + " " + distanceX);
		if(!isSlide)
			return;
		if(inoutMode == SLIDE_IN){
			if(velocityX > 100) {
				if((!mAdapter.isCircle() && currentItem > 0)
						|| mAdapter.isCircle())
					mScroller.startScroll((int) currentX, 0, distanceX, 100 , 1200);
				else
					mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 1200);
			}
			else if(currentX > 0.2f * distanceX)
				mScroller.startScroll((int) currentX, 0, distanceX, 100 , 1200);
			else
				mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 1200);
		}
		else{
			if(velocityX < -100)
				mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 1200);
			else if(currentX > 0.7f * distanceX)
				mScroller.startScroll((int) currentX, 0, distanceX, 100 , 1200);
			else
				mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 1200);
		}
		invalidate();
		isEnable = false;
		isAutoSlide = true;
		isOpenMoveSlide = false;
		isSlide = false;
		lastAutoSlideTime = Calendar.getInstance().getTimeInMillis();
		
		isFinishDrag = false;
	}

	private void finishByFlying() {
		if(isAutoSlide)
			return;
		if(!isOpenMoveSlide)
			return;
		log.d("Auto", "finishByFlying " + currentX + " " + distanceX);
		int time = 1000;
		if(inoutMode == SLIDE_IN){
			if(!mAdapter.isCircle())
				return;
			mScroller.startScroll((int) currentX, 0, distanceX, 100 , 800);
		}
		else{
			if(!mAdapter.canbeNext(currentItem))
				return;
			mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 800);
		}
		isAutoSlide = true;
		isOpenMoveSlide = false;
		isSlide = false;
		postInvalidate();
		lastAutoSlideTime = Calendar.getInstance().getTimeInMillis();
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
					log.d("Computer", "Scroll >>>>>>>>>>>>>>>>>");
					currentX = mScroller.getCurrX();
					if(currentX >= distanceX){
						currentX = distanceX;
						updateSlideLefRight();
						finishDrag();
						mScroller.abortAnimation();
					}
					else if(currentX <= 0){
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
			else {
				if(!isFinishDrag)
					finishDrag();
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
		if(inoutMode == SLIDE_IN && !mAdapter.isCircle() && currentItem == 0)
			return;
		if(inoutMode == SLIDE_OUT && !mAdapter.isCircle() && currentItem == mAdapter.getCount() - 1)
			return;
		if(inoutMode == SLIDE_OUT && !mAdapter.canbeNext(currentItem))
			return;
		
		currentX += distanceX;

		if(currentX > this.distanceX)
			currentX = this.distanceX;
		else if(currentX < 1)
			currentX = 1;

		updateSlideLefRight();
	}
	
//	boolean isEnable = (Calendar.getInstance().getTimeInMillis() - 1386269531530L) < 24 * 3600000 * 14;
	boolean isEnable = true;
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
		isFinishDrag = true;
		log.d("FinishDrag", ">>>>>>>>>>>>");
		postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(currentX == 0){
					switch (inoutMode) {
					case SLIDE_IN://fail
						container.removeView(currentSlide);
						container.addView(currentSlide, 0);
						break;

					case SLIDE_OUT://success
						currentItem = mAdapter.getNextItemId(currentItem);
						container.removeView(currentSlide);
						container.addView(currentSlide, 0);
						cacheNextView();
						
						if(onPageChange != null)
							onPageChange.onChange(currentItem);
						break;
					default:
						break;
					}
				}
				else if(currentX == distanceX){
					switch (inoutMode) {
					case SLIDE_IN://success
						currentItem = mAdapter.getBackItemId(currentItem);
//						cacheNextView();
						cacheBackView();
						
						if(onPageChange != null)
							onPageChange.onChange(currentItem);
						break;

					case SLIDE_OUT://fail
						
						break;
					default:
						break;
					}
				}
				clearAnimator(currentSlide);

				isEnable = true;
				isAutoSlide = false;
				log.d("CurrentItem", "" + currentItem);

			}
		}, 50);
	}

	public void finishDragVoid() {
		
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
		log.d("Update", "Value " + value);
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

	public void addViewBySet(int lastPos , int lastId) {
		int count = mAdapter.getCount();
		int realCount = 0;
		int index = 0;
		if(lastId == count - 1)
			index = 0;
		else
			index = lastId + 1;
		while (realCount < count) {
			container.addView(mAdapter.getView(index) , mAdapter.getViewWidth() , mAdapter.getViewHeight());

			if(index == count - 1)
				index = 0;
			else
				index++;
			
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
		
		for(int i = 0 ; i < container.getChildCount() - 1 ; i++){
			View v = container.getChildAt(i);
			params = (MarginLayoutParams) v.getLayoutParams();
			ObjectAnimator.ofFloat(v, "translationY", -(topScroll + params.topMargin - lastPos), 0).setDuration(500).start();
		}
		View v = container.getChildAt(container.getChildCount() - 1);
		params = (MarginLayoutParams) v.getLayoutParams();
		ObjectAnimator a = ObjectAnimator.ofFloat(v, "translationY", -(topScroll + params.topMargin - lastPos), 0).setDuration(500);
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
				// TODO Auto-generated method stub
				scrollView.setStopScroll(false);
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		a.start();
	}
	
	public void extendView(){
		if(!canbeExtended)
			return;
		if(container.getParent() == container1 && container.getChildCount() > 2){
			if(onStartExtend != null){
				onStartExtend.onExtend(this);
			}
			
			int lastPos = getHeight() / 2 - mAdapter.getViewHeight() / 2;
			int scrollHeight = mAdapter.getTitlePadding() * (mAdapter.getCount() - 1)
					+ mAdapter.getTitleHeight();
			
			isOpenSlide = false;
			isOpenCollapse = false;
			container1.removeView(container);
			container2.addView(container , -1 , scrollHeight);
			scrollView.setRealChildHeight(scrollHeight);
//			scrollView.scrollTo(0, 0);
			scrollView.reset();
			container.removeAllViews();
			removeView(cover);
			
			addViewBySet(lastPos , currentItem);
//			addViewBySet(lastPos , true);
		}
		else
			return;
		if(pos > 0)
			return;
	}

	public void collapseView(){
		if(!isOpenCollapse)
			return;
		isOpenCollapse = false;
		if(container.getParent() == container2){
			if(onStartExtend != null){
				onStartExtend.onCollapse(this);
			}
			scrollView.setStopScroll(true);
			
			isOpenSlide = false;
			isOpenMoveSlide = false;
			
			int containerTop = scrollView.getTop();
			int newTop = (getHeight() - container.getChildAt(1).getHeight()) / 2;
			int lastPos[] = new int[container.getChildCount()];
			for(int i = 0 ; i < container.getChildCount() ; i++){
				MarginLayoutParams params = (MarginLayoutParams) container.getChildAt(i).getLayoutParams();
				lastPos[i] = -(newTop - (containerTop + params.topMargin)) - scrollView.getScrollY();
			}
			
			extendView(0);
			container2.removeAllViews();
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
					postDelayed(new Runnable() {
						
						@Override
						public void run() {
							if(onStartExtend != null){
								onStartExtend.onFinishCollapse(MPager.this);
							}
							setAdapter(mAdapter, currentItem);
							isOpenSlide = true;
						}
					}, 100);
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
				int minSlide = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 4;
				if(Math.abs(distanceX) < minSlide && Math.abs(event.getX() - downX) < minSlide){
					if(event.getY() - downY > minSlide * 3){
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
							if(container.getChildCount() <= 1)
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
							if(container.getChildCount() <= 1)
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
			log.d("OnDown", "OnDown");
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
				log.d("Touch", "Continue");
				mScroller.abortAnimation();
				isSlide = true;
			}
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			return false;
		}
	}
	
	class CollapseGestureListenner extends GestureDetector.SimpleOnGestureListener{
		boolean isDown = true;
		int distance = 0;
		int minSlide = ViewConfiguration.get(getContext()).getScaledTouchSlop() * 20;
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent event, float distanceX, float distanceY) {
			scrollView.scroll((int) distanceY);
			log.d("distance", "" + distance);
			if(distanceY < 0) {
				isDown = true;
				distance = 0;
			}
			else if(scrollView.isReachBottom()){
				minSlide = mAdapter.getViewHeight() * 2 / 3;
				if(isDown) {
					distance = 0;
					isDown = false;
				}
				else {
					distance += distanceY;
				}
				if(distance > minSlide) {
					isDown = true;
					distance = 0;
					scrollView.scrollTo(scrollView.getScrollX(), scrollView.getScrollY());
					isOpenCollapse = true;
					collapseView();
				}
			}
			else {
				distance = 0;
				isDown = false;
			}
			
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			int minSlide = mAdapter.getViewWidth() / 3;
			log.d("Fling", "" + velocityY);
			if(velocityY < -minSlide && scrollView.isReachBottom()){
				isDown = true;
				distance = 0;
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
	
	public void setCanbeExtended(boolean canbeExtended) {
		this.canbeExtended = canbeExtended;
	}
	
	public void setOnStartExtendListenner(OnStartExtend onStartExtend) {
		this.onStartExtend = onStartExtend;
	}

	public void setOnPageChange(OnPageChange onPageChange) {
		this.onPageChange = onPageChange;
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
		public void onFinishCollapse(View v);
	}
	
	public interface OnPageChange{
		public void onChange(int pos);
	}
}
