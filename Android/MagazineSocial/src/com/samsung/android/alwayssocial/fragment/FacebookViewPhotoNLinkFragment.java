package com.samsung.android.alwayssocial.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.activity.FacebookSecondPageActivity;
import com.samsung.android.alwayssocial.app.AlwaysSocialAppImpl;
import com.samsung.android.alwayssocial.constant.FacebookConstant;
import com.samsung.android.alwayssocial.constant.GlobalConstant;
import com.samsung.android.alwayssocial.database.DBManager;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.TypeTimeline;
import com.samsung.android.alwayssocial.service.CommentParcelableObject;
import com.samsung.android.alwayssocial.service.IAlwaysService;
import com.samsung.android.alwayssocial.service.IUpdateUiCallback;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.touchImage.ImageViewTouch;
import com.samsung.android.alwayssocial.util.VolleySingleton;

public class FacebookViewPhotoNLinkFragment extends Fragment {

    private static final String TAG = "FacebookViewPhotoNLinkFragment";
    private boolean mIsLiked, mIsShouldUpdateUserLike;
    private ImageButton mBtnBack;
    private TextView mAlbumTitle;
    private ImageButton mBtnLike;
    private ImageButton mBtnComment;
    private StoryItemUnit mStoryItem;
    private FacebookSecondPageActivity mActivity;

    private TextView mTxtPhotoDescription;

    private RelativeLayout mLayoutPhotoView;
    private ImageViewTouch mPhotoDisplay;
    private ImageLoader mImageLoader;

    private WebView mWebView;
    private ProgressDialog webProgressBar;

    private IAlwaysService mService = null;

    private int mNumOfLikes;
    private int mNumOfComments;

    public FacebookViewPhotoNLinkFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.fb_content_feed_photo_n_link, null);
        mBtnBack = (ImageButton) root.findViewById(R.id.more_list_back_icon);
        mBtnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mActivity.finish();
            }
        });
        mBtnLike = (ImageButton) root.findViewById(R.id.img_header_like);
        mBtnLike.setSelected(mIsLiked);
        mBtnComment = (ImageButton) root.findViewById(R.id.img_header_comment);
        mBtnComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onCommentButtonClick();
            }
        });
        /*  mBtnShare = (ImageButton) root.findViewById(R.id.img_header_share);
          mBtnShare.setOnClickListener(new );*/

        mLayoutPhotoView = (RelativeLayout) root.findViewById(R.id.layout_photo_view);
        mAlbumTitle = (TextView) root.findViewById(R.id.title);
        mPhotoDisplay = (ImageViewTouch) root.findViewById(R.id.img_photo_content);
        mTxtPhotoDescription = (TextView) root.findViewById(R.id.txt_description_photo_bottom);

        mWebView = (WebView) root.findViewById(R.id.web_link_content);

        HashMap<String, Object> getLikeCountParams = new HashMap<String, Object>();
        getLikeCountParams.put("id", mStoryItem.getSocialFeedId());
        int firstPageFragmentType = ((FacebookSecondPageActivity) getActivity()).getFirstPageFragmentType();
        getLikeCountParams.put(FacebookConstant.FB_FRAGMENT_TYPE, firstPageFragmentType);
        mService = AlwaysSocialAppImpl.getInstance().getAlwaysBindService();
        try {
            mService.registerUiListener(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1, mServiceListener);
            mService.requestSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, FacebookConstant.FB_DATA_TYPE_LIKEINFO, getLikeCountParams);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        ((FacebookSecondPageActivity) getActivity()).setNeedToUpdate(false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //mImageLoader = new ImageLoader(getActivity());
        mImageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
        mActivity = (FacebookSecondPageActivity) getActivity();
        FacebookFeedWrapper.TypeTimeline timelineType = TypeTimeline.valueOf(mStoryItem.getFeeds_type_detail());
        if (timelineType == TypeTimeline.PHOTO) {
            FacebookFeedWrapper.TypeTimeline timeLine = TypeTimeline.valueOf(mStoryItem.getFeeds_type_detail());
            String title;
            String display = mStoryItem.getTitle();
            String[] stringArray = display.split(GlobalConstant.SUB_STRING_CODE);

            if (stringArray.length > 1) {
                if ((timeLine == TypeTimeline.LINK || timeLine == TypeTimeline.STATUS || timeLine == TypeTimeline.VIDEO)) {
                    title = stringArray[0];
                } else {
                    title = stringArray[1];
                }
            } else if (stringArray.length > 0) {
                title = stringArray[0];  
            } else {
                title = "";
            }
            mAlbumTitle.setText(title);

            String[] itemBody = mStoryItem.getBody().split(GlobalConstant.SUB_STRING_CODE);
            if (itemBody.length >= 3 && itemBody[2] != null && !itemBody[2].equals("")) {
                mTxtPhotoDescription.setText(itemBody[2]);
            }
            else {  
                mTxtPhotoDescription.setText("");
            }
            mPhotoDisplay.setImageUrl(mStoryItem.getBody_url(), mImageLoader);
            mLayoutPhotoView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        } else if (timelineType == TypeTimeline.LINK) {
            mWebView.setVisibility(View.VISIBLE);
            mLayoutPhotoView.setVisibility(View.GONE);
            mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();

            webProgressBar = ProgressDialog.show(mActivity, null, "Loading...", true, true);

            mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    if (webProgressBar.isShowing()) {
                        webProgressBar.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(mActivity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(description);
                    try {
                        alertDialog.show();
                    } catch (Exception e) {

                    }
                }
            });
            Log.d(TAG, "link url = " + mStoryItem.getLink_url());
            mWebView.loadUrl(mStoryItem.getLink_url());
        }
        if (mStoryItem.isLikedFeed() == 2) {
            mIsLiked = true;
        } else {
            mIsLiked = false;
        }
        mIsShouldUpdateUserLike = false;
        mBtnLike.setActivated(mIsLiked);
        mBtnLike.setSelected(mIsLiked);
        mBtnLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected()) {
                    mIsLiked = true;
                    mIsShouldUpdateUserLike = true;
                    try {
                        //int isLiked = AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().getIntValue(FacebookConstant.FB_PREF_KEY_IS_LIKED);
                        mService.postSNS(GlobalConstant.SOCIAL_TYPE_FACEBOOK, mStoryItem.getSocialFeedId(), null, FacebookConstant.FB_POST_TYPE_LIKE);
                        DBManager.getInstance(getActivity()).updateLikeCommentInfo(mStoryItem, mStoryItem.getNumber_of_like() + 1, mStoryItem.getNumber_of_comment(), 2);
                        AlwaysSocialAppImpl.getInstance().getAlwaysSharePrefs().remove(FacebookConstant.FB_PREF_KEY_LIKECOMMEND + "_likes");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                v.setSelected(mIsLiked);
            }
        });
    }

    public void setFeed(StoryItemUnit feed) {
        mStoryItem = feed;
    }

    public void onBackPressed() {
        Log.d(TAG, "onBackPress");
        if (webProgressBar != null && webProgressBar.isShowing()) {
            webProgressBar.dismiss();
        }
        mWebView.stopLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mService.unregisterUiNotify(GlobalConstant.SOCIAL_TYPE_FACEBOOK, 1);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IUpdateUiCallback mServiceListener = new IUpdateUiCallback.Stub() {

        @Override
        public void receiveStoryItems(String socialType, int feedType, List<StoryItemUnit> Items, boolean isResponseByRefresh, boolean isFirstTimeUpdate) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpdateUiByStoryItem(String socialType, int feedType, boolean isResponseByRefresh) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpdateLikeCommentCount(String socialType, int requestType, int count, int isUserLike) throws RemoteException {
            Log.d(TAG, "onUpdateLikeCommentCount request type = " + requestType + " count = " + count + " isUserLike = " + isUserLike);
            ((FacebookSecondPageActivity) getActivity()).setNeedToUpdate(true);
            if (GlobalConstant.SOCIAL_TYPE_FACEBOOK.compareTo(socialType) == 0) {
                if (requestType == FacebookConstant.FB_DATA_TYPE_LIKEINFO) {
                    mNumOfLikes = count;
                    mIsLiked = (isUserLike == 2) ? true : false;
                    if (isUserLike != 0) {
                        mBtnLike.setSelected(isUserLike == 1 ? false : true);
                        mBtnLike.setActivated(isUserLike == 1 ? false : true);
                    }
                } else if (requestType == FacebookConstant.FB_DATA_TYPE_COMMENTINFO) {
                    mNumOfComments = count;
                }
            }

        }

        @Override
        public void onUpdateErrorUi(String socialType, int errorCode, boolean isResponseByRefresh) throws RemoteException {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUpdateCommnetInforToUi(String socialType, int requestType, List<CommentParcelableObject> comments, String nextPage) throws RemoteException {
            // TODO Auto-generated method stub

        }
    };

    public int getLikesCount() {
        return mIsShouldUpdateUserLike ? (mNumOfLikes + 1) : mNumOfLikes;
    }

    public int getCommentsCount() {
        return mNumOfComments;
    }

    public int isUserLiked() {
        return mIsLiked ? 2 : 1;
    }
}
