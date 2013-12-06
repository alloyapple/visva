package vn.com.shoppie.fragment;

import vn.com.shoppie.R;
import vn.com.shoppie.database.sobject.HistoryTransactionList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryTradeFragment extends FragmentBasic {
	private TextView mTxtCurrentPie;
	private TextView mTxtCheckkinPie;
	private TextView mTxtShoppingPie;
	private TextView mTxtGiftPie;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(R.layout.page_personal_history_trade,
				null);
		
		mTxtCurrentPie = (TextView)root.findViewById(R.id.txt_personal_history_trade_current);
		mTxtCheckkinPie = (TextView)root.findViewById(R.id.txt_personal_history_trade_chekin);
		mTxtShoppingPie = (TextView)root.findViewById(R.id.txt_personal_history_trade_shoping);
		mTxtGiftPie = (TextView)root.findViewById(R.id.txt_personal_history_trade_gift);
		mTxtCurrentPie.setText("0 pie");
		mTxtCheckkinPie.setText("0 pie");
		mTxtShoppingPie.setText("0 pie");
		mTxtGiftPie.setText("0 pie");
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

	public void updatePie(HistoryTransactionList historyTransactionList) {
		// TODO Auto-generated method stub
		if(historyTransactionList.getResult().size() > 0){
			mTxtCurrentPie.setText(historyTransactionList.getResult().get(0).getCurrentBal()+" pie");
			mTxtCheckkinPie.setText(historyTransactionList.getResult().get(0).getCheckinPie()+" pie");
			mTxtShoppingPie.setText(historyTransactionList.getResult().get(0).getPurchasePie()+" pie");
			mTxtGiftPie.setText(historyTransactionList.getResult().get(0).getRedeemPie()+" pie");
		}
	}
}
