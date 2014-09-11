package com.sharebravo.bravo.view.adapter;

public interface UserPostProfileListener {
    public void requestUserImageType(int userImageType);

    public void goToFragment(int fragmentID);

    public void goToMapView();

    public void onClickUserAvatar(String userId);

    public void goToFollow(boolean isFollow);

    public void goToBlock(boolean isBlock);

    public void goToUserTimeline();

    public void goToUserFollowing();

    public void goToUserFollower();

    public void goToFravouriteView(int fragmentId);
}
