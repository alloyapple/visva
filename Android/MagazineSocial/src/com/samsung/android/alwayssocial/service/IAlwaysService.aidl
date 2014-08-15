package com.samsung.android.alwayssocial.service;

import android.os.Bundle;

import com.samsung.android.alwayssocial.service.IUpdateUiCallback;

interface IAlwaysService {
	// Listener APIs
	void registerUiListener(in String socialType, in int ownerId, IUpdateUiCallback listener);
	void unregisterUiNotify(in String socialType, in int ownerId);

	// SNS Request
	void requestSNS(in String socialType, in int requestDataType,  in Map param);
    void postSNS(in String socialType, in String id, in String data, in int postType);
    
    // SNS Logout
    void logoutSocial();
}