package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class HistoryTransactionList {
	@SerializedName("Result")
	private ArrayList<HistoryTransactionItem> Result;

	public HistoryTransactionList() {

	}

	public ArrayList<HistoryTransactionItem> getResult() {
		return Result;
	}

	public void setResult(ArrayList<HistoryTransactionItem> result) {
		Result = result;
	}
	
	
}
