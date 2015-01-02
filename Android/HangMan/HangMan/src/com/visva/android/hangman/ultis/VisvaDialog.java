package com.visva.android.hangman.ultis;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.visva.android.hangman.R;

public class VisvaDialog extends Dialog {

    public VisvaDialog(Context context) {
        super(context);
    }

    public VisvaDialog(Context context, int style) {
        super(context, style);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_black_transparent);
        setContentView(R.layout.progress_hud);

        getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        getWindow().setAttributes(lp);
    }
}
