package vn.com.shoppie.database.sobject;

import com.facebook.model.GraphUser;
import com.google.android.gms.maps.model.LatLng;
import vn.com.shoppie.database.adbException;

@SuppressWarnings("serial")
public class User extends ShoppieObject{
	public static final String CLASS_UNIQUE="Result";
	public static final int NUM_FIELDS=6;
	
	public String custId="";
	public String custName="";
	public String custEmail="";
	public String custPhone="";
	public String password="";
	public String linkImage="";
	public LatLng location=null;
	
	// Extra info
	public GraphUser user=null;
	
	private static User _instance=null;
	public static User getInstance(){
		if(_instance == null) _instance=new User(-1, new String[6]);
		return _instance;
	}
	public static void setInstance(int id,String...values){
		_instance = new User(id, values);
	}
	
	public User(int id,String... values){
		super(id, values);
		
		try {
			if (values.length != NUM_FIELDS)
				throw new adbException(CLASS_UNIQUE+" length values invalid!");
		} catch (adbException e) {
			e.printStackTrace();
		}
		
		this.custId=values[0];
		this.custName=values[1];
		this.custEmail=values[2];
		this.custPhone=values[3];
		this.password=values[4];
		this.linkImage=values[5];
	}
	
	public interface UserColumn extends ShoppieObjectColumn{
		public static final String TABLE_NAME="User";
		public static final String CUST_ID="id";
		public static final String CUST_NAME="name";
		public static final String CUST_EMAIL="email";
		public static final String CUST_PHONE="phone";
		public static final String CUST_PASS="password";
		public static final String LINK_IMAGE="linkImage";
		
	}
	@Override
	public String[] getValues() {
		values[0]=custId;
		values[1]=custName;
		values[2]=custEmail;
		values[3]=custPhone;
		values[4]=password;
		values[5]=linkImage;
		return values;
	}
}
