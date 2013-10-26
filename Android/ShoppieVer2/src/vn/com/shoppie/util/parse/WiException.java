package vn.com.shoppie.util.parse;

@SuppressWarnings("serial")
public class WiException extends Exception {
	public static final int TYPE_DEFAULT = 0x00;
	public static final int TYPE_CHANGEABLE = 0x01;
	public static final int TYPE_MODEL_EX = 0x10;
	public static final int TYPE_INFO = 0x100;
	public static final int TYPE_NULL = 0x1000;
	public static final int TYPE_INTERRUPTED = 0x10000;

	private int _exceptionType;
	private String _exceptionMessage;

	public WiException(int type, String message) {
		this._exceptionType = type;
		this._exceptionMessage = message;
		System.out.println(message);
	}

	public int getExcepType() {
		return this._exceptionType;
	}

	public String getExcepMessage() {
		return this._exceptionMessage;
	}
}
