package vn.com.shoppie.view;

import vn.com.shoppie.R;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

public class MPager extends RelativeLayout{
	public static final short SLIDE_UPDOWN = 0;
	public static final short SLIDE_LEFTRIGHT = 1;
	public static final short SLIDE_IN = 0;
	public static final short SLIDE_OUT = 1;

	private short slideMode = 0;
	private short inoutMode = 0;

	private int currentItem = 0;
	private MPagerAdapterBase mAdapter;
	
	private float lastX;
	private float lastY;

	private float downX;
	private float downY;

	private boolean isSlide = false;
	private boolean isOpenSlide = true;
	private boolean isOpenMoveSlide = true;

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
	private Scroller mDragScroller;
	private Scroller mExtendScroller;
	private VelocityTracker mVelocityTracker;

	private GestureDetector mGestureDetector;

	public MPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		if(mGestureDetector == null){
			mGestureDetector = new GestureDetector(getContext() , new GestureListener());
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

		initLayout();
	}

	private ScrollView scrollView;
	private LinearLayout container1;
	private RelativeLayout container;
	private LinearLayout layout1;
	
	private void initLayout() {
		layout1 = new LinearLayout(getContext());
		layout1.setGravity(Gravity.CENTER);
		addView(layout1 , -1 , -1);
		
		scrollView = new ScrollView(getContext());
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
		mExtendScroller = new Scroller(getContext(), sInterpolator);
		mDragScroller = new Scroller(getContext(), new AccelerateInterpolator());
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
	public boolean onTouchEvent(MotionEvent event) {
		if(container.getParent() != container1)
			return super.onTouchEvent(event);
		if(mAdapter == null)
			return super.onTouchEvent(event);
		if(mAdapter.getCount() <= 1)
			return super.onTouchEvent(event);
		
		mVelocityTracker.addMovement(event);
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			autoSlide();
			break;
		case MotionEvent.ACTION_CANCEL:
			autoSlide();
			break;

		default:
			break;
		}
		return true;
	}

	private void autoSlide() {
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
						if(slideMode == SLIDE_LEFTRIGHT)
							updateSlideLefRight();
						//						else
						//							updateSlideUpDown(value);
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

		if(slideMode == SLIDE_LEFTRIGHT)
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

		if(slideMode == SLIDE_LEFTRIGHT)
			distance = (getWidth() - currentSlide.getWidth()) / 2 + currentSlide.getWidth() * 5 / 4;
		else
			distance = (getHeight() - currentSlide.getHeight()) / 2 + currentSlide.getHeight() * 5 / 4;
		distanceX = distance;
		distanceY = (int) fx(distanceX);
		currentX = 0;

		//ObjectAnimator.clearAllAnimations();
		
		AnimatorSet set1 = new AnimatorSet();
		set1.playTogether(
				ObjectAnimator.ofFloat(currentSlide, "rotation", angle),
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
		Log.d("CurrentItem", "" + currentItem);
	}

	public int getSlideType(){
		return slideMode;
	}

	public void updateSlideLefRight() {
		float t = (distanceX - currentX) / distanceX;
		currentAngle = angle * t;
		//		currentX = -distance * t;
		currentY = fx(currentX);
		AnimatorSet set = new AnimatorSet();
		set.playTogether(
				ObjectAnimator.ofFloat(currentSlide, "rotation", currentAngle),
				ObjectAnimator.ofFloat(currentSlide, "translationX", currentX - distanceX),
				ObjectAnimator.ofFloat(currentSlide, "translationY", currentY - distanceY)
				);
		set.setDuration(0).start();
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

	public void extendView(){
		if(container.getParent() == container1 && container.getChildCount() > 2){
			int lastPos = getHeight() / 2 - container.getChildAt(0).getHeight() / 2;
			
			isOpenSlide = false;
			container1.removeView(container);
			scrollView.addView(container , -1 , -2);
			scrollView.scrollTo(0, 0);
			container.removeAllViews();
			removeViewAt(0);
			
			
			for(int i = 0 ; i < mAdapter.getCount() ; i++){
				container.addView(mAdapter.getView(i) , mAdapter.getViewWidth() , mAdapter.getViewHeight());
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
			
			for(int i = 0 ; i < container.getChildCount() ; i++){
				View v = container.getChildAt(i);
				params = (MarginLayoutParams) v.getLayoutParams();
				ObjectAnimator.ofFloat(v, "translationY", -(topScroll + params.topMargin - lastPos), 0).setDuration(500).start();
			}
		}
		else
			return;
		if(pos > 0)
			return;
	}

	public void collapseView(){
		if(container.getParent() == scrollView){
			isOpenSlide = false;
			
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
			isOpenSlide = true;
			
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

			return true;
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
				return true;
			if(!isSlide){
				if(true){
					slideMode = SLIDE_LEFTRIGHT;
					if(event.getX() > downX){
						if(container.getChildCount() <= 1)
							return false;
						startSlideIn();
					}
					else{
						if(container.getChildCount() <= 2)
							return false;
						startSlideOut();
					}
				}
				else{
					slideMode = SLIDE_UPDOWN;
					if(event.getY() < downY){
						if(container.getChildCount() <= 1)
							return false;
						startSlideIn();
					}
					else{
						if(container.getChildCount() <= 2)
							return false;
						startSlideOut();
					}
				}
				isSlide = true;
			}
			else{
				switch (slideMode) {
				case SLIDE_UPDOWN:
					break;
				case SLIDE_LEFTRIGHT:
					updateSlideLeftRight1(-distanceX);
					break;

				default:
					break;
				}
			}

			lastX = event.getX();
			lastY = event.getY();
			return true;
		}

		@Override
		public boolean onDown(MotionEvent event) {
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
				mScroller.abortAnimation();
				isSlide = true;
			}
			lastX = event.getX();
			lastY = event.getY();
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if(velocityX > 100){
				if(!isSlide){
					if(true){
						slideMode = SLIDE_LEFTRIGHT;
						if(e2.getX() > downX){
							if(container.getChildCount() <= 1)
								return false;
							startSlideIn();
						}
						else{
							if(container.getChildCount() <= 2)
								return false;
							startSlideOut();
						}
					}
					isSlide = true;
					//					value = 0;
				}
				mScroller.startScroll((int) currentX, 0, distanceX, 100 , 750);
				invalidate();
			}
			else if(velocityX < -100){

				if(!isSlide){
					if(true){
						slideMode = SLIDE_LEFTRIGHT;
						if(e2.getX() > downX){
							if(container.getChildCount() <= 1)
								return false;
							startSlideIn();
						}
						else{
							if(container.getChildCount() <= 2)
								return false;
							startSlideOut();
						}
					}
					isSlide = true;
					//					value = 0;
				}
				mScroller.startScroll((int) currentX, 0, -distanceX, 100 , 750);
				invalidate();
			}
			return true;
		}
	}

	private float fx(float x){
		return x / 3;
	}
	
	private static final Interpolator sInterpolator = new Interpolator() {
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t * t * t + 1.0f;
		}
	};
}