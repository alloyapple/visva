package com.sharebravo.bravo.control.activity;

import com.sharebravo.bravo.model.response.ObPostBravo;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.model.response.Spot;

public interface BravoCheckingListener {
    public void goToFragment(int fragmentID);

    public void goToMapView(Spot spot, int locationType);

    public void goToMapView(String foreignID, int locationType);

    public void goToBack();

    public void goToReturnSpotFragment(Spot mSpot);

    public void goToAddSpot();

    public void putSNS(SNS sns);
    
    public void deleteSNS(SNS sns);
    
    public void finishPostBravo();

    public void shareViaSNSByRecentPost(String twitter, ObPostBravo mObPostBravo, String sharedText);

    public void requestToLoginSNS(String snsType);
}
