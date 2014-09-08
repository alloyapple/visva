package com.sharebravo.bravo.sdk.util.network;

import android.app.Activity;

import com.sharebravo.bravo.sdk.util.VisvaDialog;

public class AsyncUI {
    public int          numLoading = 0;
    private Activity    context;
    private VisvaDialog progressDialog;

    public AsyncUI(Activity context) {
        // TODO Auto-generated constructor stub
        this.context = context;
    }

    public void before() {
        // Show waiting dialog during connection
     //   if (numLoading == 0) {
            try {
                progressDialog = new VisvaDialog(context);
                progressDialog.show();
                progressDialog.setCancelable(false);
            } catch (Exception e) {

            }
      //  }
        numLoading++;
    }

    public void after() {
        // Process server response
        numLoading--;
        if (numLoading == 0) {
            if (progressDialog != null)

            {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

    }
}
