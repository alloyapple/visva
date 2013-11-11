package vn.com.shoppie.database.sobject;


import com.google.gson.annotations.SerializedName;

public class StatusUpdatePie {
	@SerializedName("Result")
	private Value Result;
	public StatusUpdatePie() {
		
	}

	public Value getResult() {
		return Result;
	}


	public class Value {
		@SerializedName("value")
		private String value;

		public Value() {

		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}
