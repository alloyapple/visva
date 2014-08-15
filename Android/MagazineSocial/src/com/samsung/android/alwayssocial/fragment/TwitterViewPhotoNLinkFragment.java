package com.samsung.android.alwayssocial.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.samsung.android.alwayssocial.R;
import com.samsung.android.alwayssocial.activity.TwitterSecondPageActivity;
import com.samsung.android.alwayssocial.constant.TwitterConstant;
import com.samsung.android.alwayssocial.service.StoryItemUnit;
import com.samsung.android.alwayssocial.util.VolleySingleton;

public class TwitterViewPhotoNLinkFragment extends Fragment {
    private static final String TAG = "TwitterViewPhotoNLinkFragment";
    private boolean mIsFavourited;
    private ImageButton mBtnBack;
    private TextView mTxtPhotoDescription;
    private ImageButton mBtnFavourite;
    private ImageButton mBtnComment;
    private ImageButton mBtnRetweet;
    private TwitterSecondPageActivity mActivity;

    private RelativeLayout mLayoutPhotoView;
    private NetworkImageView mPhotoDisplay;
    private ImageLoader mImageLoader;

    private WebView mWebView;
    private ProgressDialog webProgressBar;
    private StoryItemUnit mStoryItem;
    
    private float mScreenWidth;
    //private float mScreenHeight;

    public TwitterViewPhotoNLinkFragment() {
//        mImageLoader = new ImageLoader(getActivity());
//        mActivity = (TwitterSecondPageActivity) getActivity();
    }

//    public TwitterViewPhotoNLinkFragment(Activity activity) {
//        mActivity = (TwitterSecondPageActivity) activity;
//        mImageLoader = new ImageLoader(activity);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.tw_content_tweet_photo_n_link, null);
        mBtnBack = (ImageButton) root.findViewById(R.id.more_list_back_icon);
        mBtnFavourite = (ImageButton) root.findViewById(R.id.img_header_star);
        mBtnComment = (ImageButton) root.findViewById(R.id.img_header_comment_twitter);
        mBtnRetweet = (ImageButton) root.findViewById(R.id.img_header_retweet);
        mLayoutPhotoView = (RelativeLayout) root.findViewById(R.id.layout_photo_view);
        mTxtPhotoDescription = (TextView) root.findViewById(R.id.txt_description_photo_bottom);
        mPhotoDisplay = (NetworkImageView) root.findViewById(R.id.img_photo_content);
        mWebView = (WebView) root.findViewById(R.id.web_link_content);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics(); 
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.heightPixels;
        //mScreenHeight = displayMetrics.widthPixels;
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "mActivity "+mActivity+"  mStoryItem "+mStoryItem);
        //mImageLoader = new ImageLoader(getActivity());
        mImageLoader = VolleySingleton.getInstance(getActivity()).getImageLoader();
        mActivity = (TwitterSecondPageActivity) getActivity();
        int type = mActivity.get2ndPageDisplayType(mStoryItem);
        switch (type) {
        case TwitterConstant.TW_2ND_DISPLAY_TYPE_PICTURE:
            Log.d(TAG, "display image, body = " + mStoryItem.getBody() + ",  bodyUrl = " + mStoryItem.getBody_url());
            mTxtPhotoDescription.setText(mStoryItem.getBody());
            mTxtPhotoDescription.setEllipsize(TruncateAt.END);
            //mImageLoader.displayImage(mStoryItem.getBody_url(), mPhotoDisplay, true);
           
            mLayoutPhotoView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            
            /*resize image to fetch the screen*/
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            float delta = (float) mScreenWidth/mStoryItem.getImage_width();
            params.width = (int) mScreenWidth;
            params.height = (int) (delta*mStoryItem.getImage_height());
            mPhotoDisplay.setLayoutParams(params);
            mPhotoDisplay.setImageUrl(mStoryItem.getBody_url(), mImageLoader);
            
            break;
        case TwitterConstant.TW_2ND_DISPLAY_TYPE_LINK:
            mLayoutPhotoView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            //final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();

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
//                    alertDialog.setTitle("Error");
//                    alertDialog.setMessage(description);
//                    alertDialog.show();
                }
            });
            Log.d(TAG, "link url = " + mStoryItem.getLink_url());
            mWebView.getSettings().setSupportZoom(true);
            mWebView.loadUrl(mStoryItem.getLink_url());
            break;
        }
        mBtnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        
        mBtnComment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mActivity.onCommentButtonClick();
            }
        });
        mIsFavourited = (mStoryItem.isLikedFeed() == 1) ? true  : false;
        mBtnFavourite.setSelected(mIsFavourited);
        mBtnFavourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mBtnFavourite.isSelected()) {
                    mBtnFavourite.setSelected(false);
                    mIsFavourited = false;
                    mActivity.onFavouriteButtonClick(TwitterConstant.TW_POST_DESTROY_FAVOURITE, mStoryItem.getSocialFeedId(), mStoryItem);
                } else {
                    mBtnFavourite.setSelected(true);
                    mIsFavourited = true;
                    mActivity.onFavouriteButtonClick(TwitterConstant.TW_POST_CREATE_FAVOURITE, mStoryItem.getSocialFeedId(), mStoryItem);
                }

            }
        });

        mBtnRetweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //TODO Auto-generated method stub
                mActivity.onRetweetButtonClick(mStoryItem.getSocialFeedId());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setStoryItem(StoryItemUnit item) {
        this.mStoryItem = item;
    }

}
