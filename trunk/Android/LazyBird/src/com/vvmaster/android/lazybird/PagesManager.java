package com.vvmaster.android.lazybird;

import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by VV-MasteR team.
 * Copyright 2012
 */
public class PagesManager {
    final public static String FLURRY_KEY = "PTFBQ48H95DKXJ4JF6VJ";
    public static int PAGE_COUNT = 29;
    public static final int HIGHLIGHTED_TEXT_COLOR = Color.BLUE;

    private ArrayList<PageData> mPages;

    public PagesManager(Context context) {
        mPages = new ArrayList<PageData>(PAGE_COUNT);

        for (int i = 1; i <= PagesManager.PAGE_COUNT; ++i) {
            PageData page = new PageData(context, i);
            mPages.add(page);
        }
        PAGE_COUNT = mPages.size();
    }

    public PageData getPage(int index) {
        return (index >= 0 && index < mPages.size()) ? mPages.get(index) : null;
    }
}
