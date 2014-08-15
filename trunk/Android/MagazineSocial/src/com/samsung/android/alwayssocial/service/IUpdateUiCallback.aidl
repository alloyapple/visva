package com.samsung.android.alwayssocial.service;

import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.StoryItemUnit;

interface IUpdateUiCallback {
	// callback from service to activity about feed
	void onUpdateUiByStoryItem(in String socialType, in int feedType, in boolean isResponseByRefresh);
	void onUpdateErrorUi(in String socialType, in int errorCode,boolean isResponseByRefresh);
	
	// callback from service to activity about comment information
	void onUpdateCommnetInforToUi(in String socialType, in int requestType, in List<CommentParcelableObject> comments, in String nextPage);
	void onUpdateLikeCommentCount(in String socialType, in int requestType, in int count, in int isUserLike);
	
	// callback from service to transfer story object to activity
	void receiveStoryItems(in String socialType, in int feedType, in List<StoryItemUnit> Items, in boolean isResponseByRefresh, in boolean isFirstTimeUpdate);
}
