package com.antonyt.infiniteviewpager;

import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.util.ImageLoader;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Example Fragment class that shows an identifier inside a TextView.
 * 
 */
public class StoreImageFragment extends Fragment {

	private int identifier;
	private int colour;
	private String link0;
	private String link1;
	private String link2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		link0 = args.getString("link0");
		link1 = args.getString("link1");
		link2 = args.getString("link2");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		TextView v = new TextView(getActivity());
		String link = link0;
		ImageLoader.getInstance(getActivity()).DisplayImage(
				link,
				v, true, true, false, false, true, false);
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("dummy", true);
	}
}
