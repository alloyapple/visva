package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class HistoryTxn extends ShoppieObject {
	public static final String CLASS_UNIQUE = "HistoryTxn";
	public static final int NUM_FIELDS = 9;

	public String txnId = "";
	public String merchId = "";
	public String txnType = "";
	public String txnAmt = "";
	public String pieQty = "";
	public String billCode = "";
	public String txnDate = "";
	public String merchName = "";
	public String storeName = "";

	public HistoryTxn(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.txnId = values[0];
		this.merchId = values[1];
		this.txnType = values[2];
		this.txnAmt = values[3];
		this.pieQty = values[4];
		this.billCode = values[5];
		this.txnDate = values[6];
		this.merchName = values[7];
		this.storeName = values[8];
	}
	public String getTypeTnx(){
		if(txnType.equals("1")){
			return "Checkin";
		}else if(txnType.equals("3")){
			return "Mua hàng";
		}else{
			return "Giao dịch khác";
		}
	}
	public String getIdTnx(){
		return "Mã giao dịch: "+txnId;
	}
}
