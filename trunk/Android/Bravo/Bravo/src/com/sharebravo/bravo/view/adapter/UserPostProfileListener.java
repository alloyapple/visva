package com.sharebravo.bravo.view.adapter;

public interface UserPostProfileListener {
    public void requestUserImageType(int userImageType);

    public void goToFragment(int fragmentID);

    public void goToMapView();

    public void onClickUserAvatar(String userId);
}
