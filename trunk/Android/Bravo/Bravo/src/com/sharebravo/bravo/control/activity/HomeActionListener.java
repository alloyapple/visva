package com.sharebravo.bravo.control.activity;

import com.sharebravo.bravo.model.response.ObBravo;

public interface HomeActionListener {
    public void goToRecentPostDetail(ObBravo obGetBravo);

    public void goToCoverImage(ObBravo obGetBravo);

    public void goToFragment(int fragmentID);

    public void goToUserData(String userId);

    public void goToShare(ObBravo bravoObj, int shareType);

    public void goToMapView(String lat, String log, int locationType);

    public void goToBack();
}
