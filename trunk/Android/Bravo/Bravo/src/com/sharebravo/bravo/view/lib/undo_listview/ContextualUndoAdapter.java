/*
 * Copyright 2013 Frankie Sardo
 * Copyright 2013 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sharebravo.bravo.view.lib.undo_listview;

import static com.nineoldandroids.view.ViewHelper.setAlpha;
import static com.nineoldandroids.view.ViewHelper.setTranslationX;
import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.FloatMath;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;

/**
 * Warning: a stable id for each item in the adapter is required. The decorated
 * adapter should not try to cast convertView to a particular view. The
 * undoLayout should have the same height as the content row.
 * <p>
 * Usage: <br>
 * * Create a new instance of this class providing the BaseAdapter to wrap, the undo layout, and the undo button id. <br>
 * * Call setDeleteItemCallback to be notified of when items should be removed from your collection. <br>
 * * Set your ListView to this ContextualUndoAdapter, and set this ContextualUndoAdapter to your ListView. <br>
 */
public class ContextualUndoAdapter extends BaseAdapterDecorator implements ContextualUndoListViewTouchListener.Callback {

    private final int           mUndoLayoutId;
    private final int           mUndoActionId;
    private final int           mAnimationTime            = 150;
    private ContextualUndoView  mCurrentRemovedView;
    private long                mCurrentRemovedId;
    private Map<View, Animator> mActiveAnimators          = new ConcurrentHashMap<View, Animator>();
    private DeleteItemCallback  mDeleteItemCallback;
    private ArrayList<ObBravo>  mObGetAllBravoRecentPosts = new ArrayList<ObBravo>();
    private ImageLoader         mImageLoader              = null;
    private double              mLat, mLong;
    private boolean             isSortByDate;

    /**
     * Create a new ContextualUndoAdapter based on given parameters.
     * 
     * @param baseAdapter
     *            The BaseAdapter to wrap
     * @param undoLayoutId
     *            The layout resource id to show as undo
     * @param undoActionId
     *            The id of the component which undoes the dismissal
     */
    public ContextualUndoAdapter(Context context, BaseAdapter baseAdapter, ArrayList<ObBravo> obGetAllBravoRecentPosts, int undoLayoutId,
            int undoActionId, boolean isSortByDate) {
        super(baseAdapter);
        mImageLoader = new ImageLoader(context);
        mObGetAllBravoRecentPosts = obGetAllBravoRecentPosts;
        mUndoLayoutId = undoLayoutId;
        mUndoActionId = undoActionId;
        mCurrentRemovedId = -1;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        ContextualUndoView contextualUndoView = (ContextualUndoView) convertView;
        if (contextualUndoView == null) {
            contextualUndoView = new ContextualUndoView(parent.getContext(), mUndoLayoutId);
            AIOLog.d("mUndoActionId:" + contextualUndoView.findViewById(mUndoActionId));

            initializeView(contextualUndoView, position);
            contextualUndoView.findViewById(mUndoActionId).setOnClickListener(new UndoListener(contextualUndoView));
        }

        View contentView = super.getView(position, contextualUndoView.getContentView(), parent);
        contextualUndoView.updateContentView(contentView);

        long itemId = getItemId(position);

        if (itemId == mCurrentRemovedId) {
            contextualUndoView.displayUndo();
            mCurrentRemovedView = contextualUndoView;
        } else {
            contextualUndoView.displayContentView();
        }

        contextualUndoView.setItemId(itemId);
        return contextualUndoView;
    }

    private void initializeView(ContextualUndoView contextualUndoView, int position) {
        ViewHolder holder = new ViewHolder();
        holder._recentPostImage = (ImageView) contextualUndoView.findViewById(R.id.img_post_recent);
        holder._recentPostTime = (TextView) contextualUndoView.findViewById(R.id.text_recent_post_time);
        holder._recentPostSpotName = (TextView) contextualUndoView.findViewById(R.id.text_recent_post_spot_name);
        // holder._userAvatar = (ImageView) contextualUndoView.findViewById(R.id.img_recent_post_user_avatar);
        holder._userName = (TextView) contextualUndoView.findViewById(R.id.text_recent_post_user_name);
        holder._totalComment = (TextView) contextualUndoView.findViewById(R.id.text_total_spot_comment);
        AIOLog.d("mObGetAllBravoRecentPosts.size():" + mObGetAllBravoRecentPosts.size() + ", position:" + position);
        if (mObGetAllBravoRecentPosts.size() > 0 && position < mObGetAllBravoRecentPosts.size()) {
            final ObBravo obGetBravo = mObGetAllBravoRecentPosts.get(position);

            if (StringUtility.isEmpty(obGetBravo.Full_Name)) {
                holder._userName.setText("Unknown");
            } else
                holder._userName.setText(obGetBravo.Full_Name);
            holder._recentPostSpotName.setText(obGetBravo.Spot_Name);

            String profile_img_url = obGetBravo.Profile_Img_URL;

            AIOLog.d("obGetBravo.Profile_Img_URL: " + obGetBravo.Profile_Img_URL);
            // if (StringUtility.isEmpty(profile_img_url)) {
            // holder._userAvatar.setImageResource(R.drawable.user_picture_default);
            // } else {
            // mImageLoader.DisplayImage(profile_img_url, R.drawable.user_picture_default, holder._userAvatar, true);
            // }
            // holder._userAvatar.setOnClickListener(new View.OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // // iClickUserAvatar.onClickUserAvatar(obGetBravo.User_ID);
            // }
            // });

            // set observer to view
            AIOLog.d("obGetBravo.Last_Pic: " + obGetBravo.Last_Pic);
            String imgSpotUrl = obGetBravo.Last_Pic;
            if (StringUtility.isEmpty(imgSpotUrl)) {
                holder._recentPostImage.setVisibility(View.GONE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.recent_post_none_img);
            } else {
                holder._recentPostImage.setVisibility(View.VISIBLE);
                holder._recentPostSpotName.setBackgroundResource(R.drawable.bg_home_cover);
                mImageLoader.DisplayImage(imgSpotUrl, R.drawable.user_picture_default, holder._recentPostImage, false);
            }
            if (isSortByDate) {
                long createdTime = 0;
                if (obGetBravo.Date_Created == null)
                    createdTime = 0;
                else
                    createdTime = obGetBravo.Date_Created.getSec();
                if (createdTime == 0) {
                    holder._recentPostTime.setText("Unknown");
                } else {
                    String createdTimeConvertStr = TimeUtility.convertToDateTime(createdTime);
                    holder._recentPostTime.setText(createdTimeConvertStr);
                    AIOLog.d("obGetBravo.Date_Created.sec: " + obGetBravo.Date_Created.getSec());
                    AIOLog.d("obGetBravo.Date_Created.Usec: " + createdTimeConvertStr);
                }
            } else {
                Double distance = gps2m((float) mLat, (float) mLong, (float) obGetBravo.Spot_Latitude, (float) obGetBravo.Spot_Longitude);
                String result = String.format("%.1f", distance);
                holder._recentPostTime.setText(result + "km");
            }
            AIOLog.d("obGetBravo.Total_Comments: " + obGetBravo.Total_Comments + "  holder._totalComment : " + holder._totalComment);
            if (obGetBravo.Total_Comments <= 0) {
                holder._totalComment.setVisibility(View.GONE);
            } else {
                holder._totalComment.setVisibility(View.VISIBLE);
                holder._totalComment.setText(obGetBravo.Total_Comments + "");
            }
        }
    }

    class ViewHolder {
        ImageView _userAvatar;
        TextView  _userName;
        ImageView _recentPostImage;
        TextView  _recentPostTime;
        TextView  _recentPostSpotName;
        TextView  _totalComment;
    }

    @Override
    public void setAbsListView(AbsListView listView) {
        super.setAbsListView(listView);
        ContextualUndoListViewTouchListener contextualUndoListViewTouchListener = new ContextualUndoListViewTouchListener(listView, this);
        listView.setOnTouchListener(contextualUndoListViewTouchListener);
        listView.setOnScrollListener(contextualUndoListViewTouchListener.makeScrollListener());
        listView.setRecyclerListener(new RecycleViewListener());
    }

    @Override
    public void onViewSwiped(View dismissView, int dismissPosition) {
        AIOLog.d("dismissView:" + dismissView);
        ContextualUndoView contextualUndoView = (ContextualUndoView) dismissView;
        if (contextualUndoView.isContentDisplayed()) {
            restoreViewPosition(contextualUndoView);
            contextualUndoView.displayUndo();
            removePreviousContextualUndoIfPresent();
            setCurrentRemovedView(contextualUndoView);
        } else {
            if (mCurrentRemovedView != null) {
                performRemoval();
            }
        }
    }

    private void restoreViewPosition(View view) {
        setAlpha(view, 1f);
        setTranslationX(view, 0);
    }

    private void removePreviousContextualUndoIfPresent() {
        if (mCurrentRemovedView != null) {
            performRemoval();
        }
    }

    private void setCurrentRemovedView(ContextualUndoView currentRemovedView) {
        mCurrentRemovedView = currentRemovedView;
        mCurrentRemovedId = currentRemovedView.getItemId();
    }

    private void clearCurrentRemovedView() {
        mCurrentRemovedView = null;
        mCurrentRemovedId = -1;
    }

    @Override
    public void onListScrolled() {
        if (mCurrentRemovedView != null) {
            performRemoval();
        }
    }

    private void performRemoval() {
        ValueAnimator animator = ValueAnimator.ofInt(mCurrentRemovedView.getHeight(), 1).setDuration(mAnimationTime);
        animator.addListener(new RemoveViewAnimatorListenerAdapter(mCurrentRemovedView));
        animator.addUpdateListener(new RemoveViewAnimatorUpdateListener(mCurrentRemovedView));
        animator.start();
        mActiveAnimators.put(mCurrentRemovedView, animator);
        clearCurrentRemovedView();
    }

    /**
     * Set the DeleteItemCallback for this ContextualUndoAdapter. This is called when an item should be deleted from your collection.
     */
    public void setDeleteItemCallback(DeleteItemCallback deleteItemCallback) {
        mDeleteItemCallback = deleteItemCallback;
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putLong("mCurrentRemovedId", mCurrentRemovedId);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        mCurrentRemovedId = bundle.getLong("m1CurrentRemovedId", -1);
    }

    /**
     * A callback interface which is used to notify when items should be removed from the collection.
     */
    public interface DeleteItemCallback {
        /**
         * Called when an item should be removed from the collection.
         * 
         * @param position
         *            the position of the item that should be removed.
         */
        public void deleteItem(int position);
    }

    private class RemoveViewAnimatorListenerAdapter extends AnimatorListenerAdapter {

        private final View mDismissView;
        private final int  mOriginalHeight;

        public RemoveViewAnimatorListenerAdapter(View dismissView) {
            mDismissView = dismissView;
            mOriginalHeight = dismissView.getHeight();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mActiveAnimators.remove(mDismissView);
            restoreViewPosition(mDismissView);
            restoreViewDimension(mDismissView);
            deleteCurrentItem();
        }

        private void restoreViewDimension(View view) {
            ViewGroup.LayoutParams lp;
            lp = view.getLayoutParams();
            lp.height = mOriginalHeight;
            view.setLayoutParams(lp);
        }

        private void deleteCurrentItem() {
            ContextualUndoView contextualUndoView = (ContextualUndoView) mDismissView;
            int position = getAbsListView().getPositionForView(contextualUndoView);
            mDeleteItemCallback.deleteItem(position);
        }
    }

    private class RemoveViewAnimatorUpdateListener implements ValueAnimator.AnimatorUpdateListener {

        private final View                   mDismissView;
        private final ViewGroup.LayoutParams mLayoutParams;

        public RemoveViewAnimatorUpdateListener(View dismissView) {
            mDismissView = dismissView;
            mLayoutParams = dismissView.getLayoutParams();
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mLayoutParams.height = (Integer) valueAnimator.getAnimatedValue();
            mDismissView.setLayoutParams(mLayoutParams);
        }
    }

    private class UndoListener implements View.OnClickListener {

        private final ContextualUndoView mContextualUndoView;

        public UndoListener(ContextualUndoView contextualUndoView) {
            mContextualUndoView = contextualUndoView;
        }

        @Override
        public void onClick(View v) {
            clearCurrentRemovedView();
            mContextualUndoView.displayContentView();
            moveViewOffScreen();
            animateViewComingBack();
        }

        private void moveViewOffScreen() {
            ViewHelper.setTranslationX(mContextualUndoView, mContextualUndoView.getWidth());
        }

        private void animateViewComingBack() {
            animate(mContextualUndoView).translationX(0).setDuration(mAnimationTime).setListener(null);
        }
    }

    private class RecycleViewListener implements AbsListView.RecyclerListener {
        @Override
        public void onMovedToScrapHeap(View view) {
            Animator animator = mActiveAnimators.get(view);
            if (animator != null) {
                animator.cancel();
            }
        }
    }

    private double gps2m(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180 / 3.14169);

        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        float t1 = FloatMath.cos(a1) * FloatMath.cos(a2) * FloatMath.cos(b1) * FloatMath.cos(b2);
        float t2 = FloatMath.cos(a1) * FloatMath.sin(a2) * FloatMath.cos(b1) * FloatMath.sin(b2);
        float t3 = FloatMath.sin(a1) * FloatMath.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt / 1000;
    }
}