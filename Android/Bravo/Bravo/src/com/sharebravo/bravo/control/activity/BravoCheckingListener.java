package com.sharebravo.bravo.control.activity;

import com.sharebravo.bravo.model.response.Spot;

public interface BravoCheckingListener {
    public void goToFragment(int fragmentID, boolean isBackStatus);

    public void goToMapView(Spot spot, int locationType);

    public void goToMapView(String foreignID, int locationType);

    public void goToBack();

    public void goToReturnSpotFragment(Spot mSpot);

    public void goToSpotDetail(Spot mSpot);
}
