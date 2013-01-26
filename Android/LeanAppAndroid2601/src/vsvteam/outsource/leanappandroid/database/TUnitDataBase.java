package vsvteam.outsource.leanappandroid.database;

public class TUnitDataBase {
	// private all variables
	private int tUnitId;
	private String tUnitName;
	private String code;
	private String reference;
	private int conversionRate;
	private String conversionReference;

	// empty constructor
	public TUnitDataBase() {

	}

	// constructor
	public TUnitDataBase(int tUnitId, String tUnitName, String code, String reference,
			int conversionRate, String conversionReference) {
		this.tUnitId = tUnitId;
		this.tUnitName = tUnitName;
		this.code = code;
		this.reference = reference;
		this.conversionRate = conversionRate;
		this.conversionReference = conversionReference;
	}

	// constructor
	public TUnitDataBase(String tUnitName, String code, String reference, int conversionRate,
			String conversionReference) {
		this.tUnitName = tUnitName;
		this.code = code;
		this.reference = reference;
		this.conversionRate = conversionRate;
		this.conversionReference = conversionReference;
	}

	public int gettUnitId() {
		return tUnitId;
	}

	public void settUnitId(int tUnitId) {
		this.tUnitId = tUnitId;
	}

	public String gettUnitName() {
		return tUnitName;
	}

	public void settUnitName(String tUnitName) {
		this.tUnitName = tUnitName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public int getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(int conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getConversionReference() {
		return conversionReference;
	}

	public void setConversionReference(String conversionReference) {
		this.conversionReference = conversionReference;
	}
}
