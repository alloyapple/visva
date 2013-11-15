package vn.com.shoppie.fragment;

import vn.com.shoppie.R;
import vn.com.shoppie.adapter.CatelogyAdapter;
import vn.com.shoppie.database.sobject.MerchantStoreItem;
import vn.com.shoppie.util.CoverLoader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchBrandDetailFragment extends FragmentBasic{
	// =============================Constant Define=====================
	// ============================Control Define =====================
	private TextView name;
	private TextView desc;
	private TextView count;
	private View image;
	// ============================Class Define =======================
	private IOnClickShowStoreDetail mListener;
	// ============================Variable Define =====================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.page_search_brand_detail_fragment, null);
		name = (TextView) root.findViewById(R.id.name);
		desc = (TextView) root.findViewById(R.id.desc);
		count = (TextView) root.findViewById(R.id.count);
		image = root.findViewById(R.id.image);
		return root;
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
		public void onClickViewStoreDetail(MerchantStoreItem store);
	}

	public void setListener(IOnClickShowStoreDetail iOnClickShowStoreDetail) {
		this.mListener = iOnClickShowStoreDetail;
	}

	public void updateUI(MerchantStoreItem store) {
		// TODO Auto-generated method stub
		name.setText(store.getStoreName());
		desc.setText(store.getMerchDesc());
		count.setText("+" + store.getPieQty());
		CoverLoader.getInstance(getActivity()).DisplayImage(CatelogyAdapter.URL_HEADER + store.getMerchBanner(), image);
	}
}
