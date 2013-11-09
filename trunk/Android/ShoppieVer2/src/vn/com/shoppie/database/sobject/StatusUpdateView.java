package vn.com.shoppie.database.sobject;

import com.google.gson.annotations.SerializedName;

public class StatusUpdateView {
	@SerializedName("Result")
	private Value Result;
	public StatusUpdateView() {
		
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
