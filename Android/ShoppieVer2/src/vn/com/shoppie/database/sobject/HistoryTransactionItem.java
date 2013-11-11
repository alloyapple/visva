package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class HistoryTransactionItem {
	@SerializedName("currentBal")
	private int currentBal;
	@SerializedName("checkinPie")
	private int checkinPie;
	@SerializedName("purchasePie")
	private int purchasePie;
	@SerializedName("redeemPie")
	private int redeemPie;
	
	public HistoryTransactionItem(){
		
	}

	public int getCurrentBal() {
		return currentBal;
	}

	public void setCurrentBal(int currentBal) {
		this.currentBal = currentBal;
	}

	public int getCheckinPie() {
		return checkinPie;
	}

	public void setCheckinPie(int checkinPie) {
		this.checkinPie = checkinPie;
	}

	public int getPurchasePie() {
		return purchasePie;
	}

	public void setPurchasePie(int purchasePie) {
		this.purchasePie = purchasePie;
	}

	public int getRedeemPie() {
		return redeemPie;
	}

	public void setRedeemPie(int redeemPie) {
		this.redeemPie = redeemPie;
	}

}
