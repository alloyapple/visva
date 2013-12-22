package vn.com.shoppie.database.sobject;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class CustomerLikeBrandList {
	@SerializedName("Result")
	private ArrayList<MerchId> Result;

	public ArrayList<MerchId> getResult() {
		return Result;
	}

	public void setResult(ArrayList<MerchId> result) {
		Result = result;
	}

	public CustomerLikeBrandList() {

	}

	public class MerchId {
		@SerializedName("merchId")
		private String merchId;

		public String getMerchId() {
			return merchId;
		}

		public void setMerchId(String merchId) {
			this.merchId = merchId;
		}

		public MerchId() {

		}

	}
}
