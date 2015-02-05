package com.visva.android.visvasdklibrary.location;

import android.location.Address;

public interface IAddressListener {
    public void onResponse(Address address);

    public void onFailedResponse(int errorType);
}
