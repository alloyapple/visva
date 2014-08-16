package com.sharebravo.bravo.sdk.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.sharebravo.bravo.R;

public class VisvaDialog extends Dialog {

    public VisvaDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_black_transparent);
        setContentView(R.layout.layout_progress_dialog);
    }
}
