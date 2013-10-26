package vn.com.shoppie.database.sobject;


@SuppressWarnings("serial")
public class Image extends ShoppieObject{
	public static final String CLASS_UNIQUE="Gift";
	public static final int NUM_FIELDS=1;
	
	public String pathNetwork;
	public String pathLocal;
	public Image(int index, String[] values) {
		super(index, values);
		
		this.pathNetwork=values[0];
		this.pathLocal=values[1];
	}
	
	
}
