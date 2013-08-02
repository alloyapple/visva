package vn.zgome.game.streetknight.core;

public interface IAPListener {
	public void callSmsPurchase();
	public void callBankPurchase();
	public void callPaypalPurchase();
	public void callCardPurchase();
	public void callSmsAuto();
}
