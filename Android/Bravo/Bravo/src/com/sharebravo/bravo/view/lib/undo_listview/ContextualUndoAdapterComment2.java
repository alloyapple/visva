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

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetComment;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.ImageLoader;
import com.sharebravo.bravo.utils.BravoUtils;
import com.sharebravo.bravo.utils.StringUtility;
import com.sharebravo.bravo.utils.TimeUtility;
import com.sharebravo.bravo.view.adapter.AdapterBravoDetail;

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
@SuppressLint("DefaultLocale")
public class ContextualUndoAdapterComment2 extends BaseAdapterDecorator implements ContextualUndoListViewTouchListener.Callback {

    private final int               mUndoLayoutId;
    private final int               mUndoActionId;
    private final int               mAnimationTime            = 150;
    private ContextualUndoView      mCurrentRemovedView;
    private long                    mCurrentRemovedId;
    private Map<View, Animator>     mActiveAnimators          = new ConcurrentHashMap<View, Animator>();
    private DeleteItemCallback      mDeleteItemCallback;
    private ArrayList<ObGetComment> mObGetAllBravoGetComments = new ArrayList<ObGetComment>();
    private ImageLoader             mImageLoader              = null;

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
    public ContextualUndoAdapterComment2(Context context, BaseAdapter baseAdapter, ArrayList<ObGetComment> obGetAllBravoGetComments,
            int undoLayoutId,
            int undoActionId) {
        super(baseAdapter);
        mImageLoader = new ImageLoader(context);
        mObGetAllBravoGetComments = obGetAllBravoGetComments;
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
            contextualUndoView.findViewById(mUndoActionId).setOnClickListener(new UndoListener(position));
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
        holder._textUserName = (TextView) contextualUndoView.findViewById(R.id.txtview_user_name_comment);
        if (mObGetAllBravoGetComments == null || mObGetAllBravoGetComments.size() <= 1)
            return;
        AIOLog.d("mObGetAllBravoRecentPosts.size():" + mObGetAllBravoGetComments.size() + ", position:" + position);
        if (mObGetAllBravoGetComments.size() > 0 && position < mObGetAllBravoGetComments.size()) {
            final ObGetComment obGetComment = mObGetAllBravoGetComments.get(position);

            if (StringUtility.isEmpty(obGetComment.fullName)) {
                holder._textUserName.setText("Unknown");
            } else
                holder._textUserName.setText(obGetComment.fullName);
        }
    }

    class ViewHolder {
        TextView  _textUserName;
        ImageView _userAvatar;
        TextView  _textComment;
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
        // ContextualUndoView contextualUndoView = (ContextualUndoView) dismissView;
        // if (contextualUndoView.isContentDisplayed()) {
        // restoreViewPosition(contextualUndoView);
        // contextualUndoView.displayUndo();
        // setCurrentRemovedView(contextualUndoView);
        // } else {
        // restoreViewPosition(mCurrentRemovedView);
        // mCurrentRemovedView.displayContentView();
        // setCurrentRemovedView(mCurrentRemovedView);
        // }
    }

    private void restoreViewPosition(View view) {
        setAlpha(view, 1f);
        setTranslationX(view, 0);
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
           // performRemoval();
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
            // deleteCurrentItem();
        }

        private void restoreViewDimension(View view) {
            ViewGroup.LayoutParams lp;
            lp = view.getLayoutParams();
            lp.height = mOriginalHeight;
            view.setLayoutParams(lp);
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

        private int _position;

        public UndoListener(int position) {
            _position = position;
        }

        @Override
        public void onClick(View v) {
            mDeleteItemCallback.deleteItem(_position);
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
}