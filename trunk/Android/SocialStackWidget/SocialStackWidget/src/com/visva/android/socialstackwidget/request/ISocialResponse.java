package com.visva.android.socialstackwidget.request;

public interface ISocialResponse {
    public void onResponse(SocialResponseBase data);
    public void onErrorResponse(SocialErrorResponseBase error);
}
