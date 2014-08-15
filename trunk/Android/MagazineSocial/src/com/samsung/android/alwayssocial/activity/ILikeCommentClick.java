package com.samsung.android.alwayssocial.activity;

import com.samsung.android.alwayssocial.service.StoryItemUnit;

public interface ILikeCommentClick {
    public void onLikeCommentClick(String type, StoryItemUnit storyItem, boolean like);
}