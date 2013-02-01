package vsvteam.outsource.leanappandroid.database;

public class TUnitDataBase {
	// private all variables
	private int tUnitId;
	private int operatorSpeed;

	public int getOperatorSpeed() {
		return operatorSpeed;
	}

	public void setOperatorSpeed(int operatorSpeed) {
		this.operatorSpeed = operatorSpeed;
	}

	// empty constructor
	public TUnitDataBase() {

	}

	// constructor
	public TUnitDataBase(int tUnitId, int operatorSpeed) {
		this.tUnitId = tUnitId;
		this.operatorSpeed = operatorSpeed;
	}

	// constructor
	public TUnitDataBase(int operatorSpeed) {
		this.operatorSpeed = operatorSpeed;
	}

	public int gettUnitId() {
		return tUnitId;
	}

	public void settUnitId(int tUnitId) {
		this.tUnitId = tUnitId;
	}
}
