package vsvteam.outsource.leanappandroid.tabbar;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ViewAnimator;

/**
 * The purpose of this Activity is to manage the activities in a tab. Note:
 * Child Activities can handle Key Presses before they are seen here.
 * 
 * @author Eric Harlow
 */
@SuppressWarnings("deprecation")
public class TabGroupActivity extends ActivityGroup {

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private Stack<String> stack;
	private ViewAnimator mViewAnimator;

	public static TabGroupActivity instance;

	private String TAG = getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (stack == null)
			stack = new Stack<String>();

		instance = this;
		mViewAnimator = new ViewAnimator(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		System.gc();
		resetChildActivities();
	}

	@Override
	public void finishFromChild(Activity child) {
		try {
			pop();
		} catch (Exception e) {
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		try {
			pop();
		} catch (Exception e) {
			e.printStackTrace();
			finish();
		}
	}

	public void push(String id, Intent intent) {
		@SuppressWarnings("deprecation")
		Window window = getLocalActivityManager().startActivity(id,
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		if (window != null) {
			stack.push(id);
			// animation
			// View view = window.getDecorView();
			// mViewAnimator.setInAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.slide_in_left));
			// mViewAnimator.setOutAnimation(AnimationUtils.loadAnimation(this,
			// R.anim.slide_out_left));
			// mViewAnimator.addView(view);
			// mViewAnimator.showNext();
			// setContentView(mViewAnimator);
			// end animation
			setContentView(window.getDecorView());
		}
	}

	public void resetChildActivities() {
		try {
			LocalActivityManager manager = getLocalActivityManager();
			if (stack.size() != 1) {
				int index = stack.size() - 1;

				for (int i = index; i > 0; i--) {
					manager.destroyActivity(stack.get(i), true);
					stack.remove(i);
				}

				Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
				Window newWindow = manager.startActivity(stack.peek(), lastIntent);
				setContentView(newWindow.getDecorView());
				System.gc();
			}
		} catch (Exception e) {
		}
	}

	public void pop() {
		if (stack.size() == 1) {
			finish();
		}

		LocalActivityManager manager = getLocalActivityManager();
		manager.destroyActivity(stack.pop(), true);
		if (stack.size() > 0) {
			Intent lastIntent = manager.getActivity(stack.peek()).getIntent();
			Window newWindow = manager.startActivity(stack.peek(), lastIntent);

			setContentView(newWindow.getDecorView());
		}
	}

	public static TabGroupActivity getInstance() {
		return instance;
	}

	public int getSizeOfStack() {
		return stack.size();
	}
}
