package com.sharebravo.bravo.view.adapter;

public interface DetailPostListener {
    public void goToCallSpot();

    public void goToFragment(int framentID);

    public void goToShare();

    public void goToSave();

    public void goToSubmitComment(String commentText);

    public void goToReport();

    public void goToFollow(boolean isFollow);

}
