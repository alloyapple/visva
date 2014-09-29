package com.sharebravo.bravo.control.activity;

import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetUserInfo;
import com.sharebravo.bravo.model.response.SNS;
import com.sharebravo.bravo.model.response.Spot;

public interface HomeActionListener {
    public void goToRecentPostDetail(ObBravo obGetBravo);

    public void goToCoverImage(ObBravo obGetBravo);

    public void goToViewImage(ObGetUserInfo obGetUserInfo, int userImageType);

    public void goToFragment(int fragmentID);

    public void goToUserData(String userId);

    public void goToShare(ObBravo bravoObj, int shareType);

    public void goToMapView(String lat, String log, int locationType);

    public void goToMapView(String foreignID, int locationType, String fullName);

    public void goToUserTimeLine(ObGetUserInfo userInfo);

    public void goToUsergFollowing(String foreignID);

    public void goToUsergFollower(String foreignID);

    public void goToBack();

    public void goToLiked(String mSpotID);

    public void goToSaved(String mSpotID);

    public void goToSpotDetail(Spot mSpot);

    public void goToAddSpot();

    public void goToInputMySpot();

    public void goToLocateMySpot();

    // public void goToBravoSpot(double lat, double lon);


    public void shareViaSNS(String snsType, ObBravo mBravo, String sharedText);

    public void requestToLoginSNS(String snsType);

    public void goToReturnSpotFragment(Spot mSpot);

    public void goToMapView(Spot spot, int makerByLocationSpot);

    public void putSNS(SNS snn);
}
