package com.sharebravo.bravo.control.activity;

import com.sharebravo.bravo.model.response.ObGetBravo;

public interface HomeActionListener {
    public void goToRecentPostDetail(ObGetBravo obGetBravo);

    public void goToFragment(int fragmentID);

    public void goToBack();
}
