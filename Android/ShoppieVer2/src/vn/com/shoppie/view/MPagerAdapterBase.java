package vn.com.shoppie.view;

import android.view.View;

public abstract class MPagerAdapterBase {
	public abstract View getView(int position);
	public abstract int getCount();
	public abstract Object getItem(int position);
	public abstract View getNextView(int currPosition);
	public abstract View getBackView(int currPosition);
	public abstract int getViewWidth();
	public abstract int getViewHeight();
	public abstract int getNextItemId(int currPosition);
	public abstract int getBackItemId(int currPosition);
	public abstract int getTitlePadding();
	public abstract int getTitleHeight();
	public abstract boolean isCircle();
	public abstract boolean canbeNext(int curId);
}
