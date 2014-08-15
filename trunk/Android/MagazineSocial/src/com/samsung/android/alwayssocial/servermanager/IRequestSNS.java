package com.samsung.android.alwayssocial.servermanager;

import com.samsung.android.alwayssocial.object.ResponseData;
import com.samsung.android.alwayssocial.service.IResponseFromSNSCallback;

public interface IRequestSNS {
    public void onResponse(String socialType, int requestType, ResponseData responseFacebook, IResponseFromSNSCallback callback);
    public void onErrorResponse(String socialType, int error, IResponseFromSNSCallback callback);
}
