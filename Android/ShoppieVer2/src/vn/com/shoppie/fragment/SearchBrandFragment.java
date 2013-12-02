package vn.com.shoppie.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.activity.SearchActivity;
import vn.com.shoppie.adapter.StoreAdapter;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SearchBrandFragment extends FragmentBasic {
	// =============================Constant Define=====================
	// ============================Control Define =====================
	// ============================Class Define =======================
	
	// ============================Variable Define =====================

	private ListView listView;
	private Vector<String> nameList = new Vector<String>();
	private Map<String, MerchantStoreItem> manageByName = new HashMap<String, MerchantStoreItem>();
	private StoreAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_search_brand_fragment, null);

		listView = (ListView) root.findViewById(R.id.list_brand);
		listView.setDivider(null);
		listView.setDividerHeight(0);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				SearchActivity act = (SearchActivity) getActivity();
				act.onClickViewStoreDetail(adapter.getItem(position));
			}
		});

		return root;
	}

	public void setAdapter(Vector<MerchantStoreItem> data) {
		adapter = new StoreAdapter(getActivity() , data , ((SearchActivity) getActivity()).getMyLocation());
		listView.setAdapter(adapter);
		
		nameList.clear();
		manageByName.clear();
		for (int i = 0; i < data.size(); i++) {
			String name = new String(data.get(i).getStoreName());
			nameList.add(data.get(i).getStoreName());
			if(manageByName.get(name) == null)
				manageByName.put(name, data.get(i));
		}
//		SearchActivity activity = (SearchActivity) getActivity();
//		activity.setPieMap(data);
	}

	public void filter(String content) {
		Vector<MerchantStoreItem> data = new Vector<MerchantStoreItem>();
		for (String name : nameList) {
			if (name.toLowerCase().contains(content.toLowerCase())) {
				data.add(manageByName.get(name));
			}
		}
		StoreAdapter adapter = new StoreAdapter(getActivity(), data , ((SearchActivity) getActivity()).getMyLocation());
		listView.setAdapter(adapter);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
