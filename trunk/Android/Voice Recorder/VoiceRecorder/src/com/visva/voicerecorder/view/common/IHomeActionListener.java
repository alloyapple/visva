package com.visva.voicerecorder.view.common;

import android.view.View;

public interface IHomeActionListener {
    public void onClickItemListener(View view, int position, int fragment, int listViewType);

    public void onLongClickItemListener(View view, int position, int fragment, int listViewType);
}
