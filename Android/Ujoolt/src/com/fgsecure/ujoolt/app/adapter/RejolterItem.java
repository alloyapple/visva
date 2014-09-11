package com.fgsecure.ujoolt.app.adapter;

public class RejolterItem {
	private Rejolter rejolterL;
	private Rejolter rejolterR;

	public RejolterItem(Rejolter rejolterL, Rejolter rejolterR) {
		this.rejolterL = rejolterL;
		this.rejolterR = rejolterR;
	}

	public Rejolter getRejolterL() {
		return rejolterL;
	}

	public void setRejolterL(Rejolter rejolterL) {
		this.rejolterL = rejolterL;
	}

	public Rejolter getRejolterR() {
		return rejolterR;
	}

	public void setRejolterR(Rejolter rejolterR) {
		this.rejolterR = rejolterR;
	}
}
