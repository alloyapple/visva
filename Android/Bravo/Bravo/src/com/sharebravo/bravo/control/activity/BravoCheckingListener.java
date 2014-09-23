package com.sharebravo.bravo.control.activity;

import com.sharebravo.bravo.model.response.ObGetSpot.Spot;

public interface BravoCheckingListener {
    public void goToFragment(int fragmentID);

    public void goToMapView(Spot spot, int locationType);

    public void goToMapView(String foreignID, int locationType);

    public void goToBack();

    public void goToReturnSpotFragment(Spot mSpot);
}
