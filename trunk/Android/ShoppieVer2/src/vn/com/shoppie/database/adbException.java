package vn.com.shoppie.database;


@SuppressWarnings("serial")
public class adbException extends Throwable{
	String message;
	public adbException(String message) {
		this.message=message;
	}
}
