package vn.com.shoppie.database.sobject;

import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class CustBalance extends ShoppieObject {
	public static final String CLASS_UNIQUE = "CustBalance";
	public static final int NUM_FIELDS = 3;

	public String creditBal = "";
	public String debitBal = "";
	public String currentBal = "";
	
	public String _result="";

	public CustBalance(int id, String... values) {
		super(id, values);

		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE + " length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}

		this.creditBal = values[0];
		this.debitBal = values[1];
		this.currentBal = values[2];

	}
	
	public int getPie(){
		try{
			return Integer.valueOf(currentBal);
		}catch(NumberFormatException e){
			return -1;
		}
	}
	@Override
	public String[] getValues() {
		String[] _values={creditBal,debitBal,currentBal};
		values=_values;
		return values;
	}
}
