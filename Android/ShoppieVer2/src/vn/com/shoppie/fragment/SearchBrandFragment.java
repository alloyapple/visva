package vn.com.shoppie.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.StoreAdapter;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SearchBrandFragment extends FragmentBasic {
	// =============================Constant Define=====================
	// ============================Control Define =====================
	// ============================Class Define =======================
	private IOnClickShowStoreDetail mListener;
	// ============================Variable Define =====================

	private ListView listView;
	private Vector<String> nameList = new Vector<String>();
	private Map<String, MerchantStoreItem> manageByName = new HashMap<String, MerchantStoreItem>();

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
				mListener.onClickViewStoreDetail(position);
			}
		});

		return root;
	}

	public void setAdapter(Vector<MerchantStoreItem> data) {
		StoreAdapter adapter = new StoreAdapter(getActivity(), data);
		listView.setAdapter(adapter);

		nameList.clear();
		manageByName.clear();
		for (int i = 0; i < data.size(); i++) {
			String name = new String(data.get(i).getStoreName());
			nameList.add(data.get(i).getStoreName());
			manageByName.put(name, data.get(i));
		}
	}

	public void filter(String content) {
		Vector<MerchantStoreItem> data = new Vector<MerchantStoreItem>();
		for (String name : nameList) {
			if (name.contains(content)) {
				data.add(manageByName.get(name));
			}
		}
		StoreAdapter adapter = new StoreAdapter(getActivity(), data);
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

	public interface IOnClickShowStoreDetail {
		public void onClickViewStoreDetail(int position);
	}

	public void setListener(IOnClickShowStoreDetail iOnClickShowStoreDetail) {
		this.mListener = iOnClickShowStoreDetail;
	}

	public void updateUI(int position) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "truyen cai " + position + " sang ",
				Toast.LENGTH_SHORT).show();
	}
}
