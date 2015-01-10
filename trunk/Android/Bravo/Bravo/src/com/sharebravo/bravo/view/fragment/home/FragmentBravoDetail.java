package com.sharebravo.bravo.view.fragment.home;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sharebravo.bravo.R;
import com.sharebravo.bravo.control.activity.HomeActivity;
import com.sharebravo.bravo.control.request.BravoRequestManager;
import com.sharebravo.bravo.control.request.IRequestListener;
import com.sharebravo.bravo.model.response.ObBravo;
import com.sharebravo.bravo.model.response.ObGetBravo;
import com.sharebravo.bravo.model.response.ObGetComments;
import com.sharebravo.bravo.model.response.ObGetFollowingCheck;
import com.sharebravo.bravo.model.response.ObGetLikeItem;
import com.sharebravo.bravo.model.response.ObGetMylistItem;
import com.sharebravo.bravo.model.response.ObGetSpot;
import com.sharebravo.bravo.model.response.Spot;
import com.sharebravo.bravo.sdk.log.AIOLog;
import com.sharebravo.bravo.sdk.util.network.NetworkUtility;
import com.sharebravo.bravo.utils.DialogUtility;
import com.sharebravo.bravo.utils.IDialogListener;
import com.sharebravo.bravo.view.adapter.AdapterBravoDetail;
import com.sharebravo.bravo.view.adapter.DetailBravoListener;
import com.sharebravo.bravo.view.fragment.FragmentBasic;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapCover;
import com.sharebravo.bravo.view.fragment.maps.FragmentMapView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView;
import com.sharebravo.bravo.view.lib.pullrefresh_loadmore.XListView.IXListViewListener;

public class FragmentBravoDetail extends FragmentBasic implements DetailBravoListener {

    private static final int    REQUEST_CODE_CAMERA      = 8001;
    private static final int    REQUEST_CODE_GALLERY     = 8002;
    private static final int    CROP_FROM_CAMERA         = 8003;

    private Uri                 mCapturedImageURI        = null;
    private XListView           listviewRecentPostDetail = null;
    private AdapterBravoDetail  adapterRecentPostDetail  = null;
    private Button              btnBack;
    private OnItemClickListener onItemClick              = new OnItemClickListener() {

                                                             @Override
                                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                             }
                                                         };

    private ObBravo             mBravoObj;
    private Spot                mSpot;
    private LinearLayout        mLayoutPoorConnection;
    private ObGetComments       mObGetComments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.page_recent_post_detail_tab, container);
        mLayoutPoorConnection = (LinearLayout) root.findViewById(R.id.layout_poor_connection);
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            mLayoutPoorConnection.setVisibility(View.GONE);
        } else {
            mLayoutPoorConnection.setVisibility(View.VISIBLE);
        }
        mHomeActionListener = (HomeActivity) getActivity();
        listviewRecentPostDetail = (XListView) root.findViewById(R.id.listview_recent_post_detail);
        adapterRecentPostDetail = new AdapterBravoDetail(getActivity(), this);
        adapterRecentPostDetail.setListener(this);
        listviewRecentPostDetail.setFooterDividersEnabled(false);
        listviewRecentPostDetail.setAdapter(adapterRecentPostDetail);
        listviewRecentPostDetail.setOnItemClickListener(onItemClick);
        listviewRecentPostDetail.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
                onStopPullAndLoadListView();
            }

            @Override
            public void onLoadMore() {
                onStopPullAndLoadListView();
            }
        });

        listviewRecentPostDetail.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                adapterRecentPostDetail.parallaxImage(adapterRecentPostDetail.getBackGroundParallax());
                adapterRecentPostDetail.parallaxImage(adapterRecentPostDetail.getMapParallax());
            }
        });
        btnBack = (Button) root.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mHomeActionListener.goToBack();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setBravoOb(ObBravo obj) {
        this.mBravoObj = obj;
        mSpot = new Spot();
        mSpot.Spot_ID = mBravoObj.Spot_ID;
        mSpot.Spot_FID = mBravoObj.Spot_FID;
        mSpot.Spot_Name = mBravoObj.Spot_Name;
        mSpot.Spot_Source = mBravoObj.Spot_Source;
        mSpot.Spot_Latitude = mBravoObj.Spot_Latitude;
        mSpot.Spot_Longitude = mBravoObj.Spot_Longitude;
        mSpot.Spot_Genre = mBravoObj.Spot_Genre;
        mSpot.Spot_Phone = mBravoObj.Spot_Phone;
        mSpot.Spot_Price = mBravoObj.Spot_Price;
        mSpot.Spot_Type = mBravoObj.Spot_Type;
        FragmentMapCover.mLat = mBravoObj.Spot_Latitude;
        FragmentMapCover.mLong = mBravoObj.Spot_Longitude;
        adapterRecentPostDetail.setBravoOb(mBravoObj);
        adapterRecentPostDetail.notifyDataSetChanged();
    }

    private void onStopPullAndLoadListView() {
        listviewRecentPostDetail.stopRefresh();
        listviewRecentPostDetail.stopLoadMore();
    }

    private void requestGetComments() {
        BravoRequestManager.getInstance(getActivity()).requestToGetCommentsForSpotDetail(getActivity(), mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestGetComments:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                mObGetComments = gson.fromJson(response.toString(), ObGetComments.class);
                AIOLog.d("mObGetComments:" + mObGetComments);
                if (mObGetComments == null)
                    return;
                else {
                    AIOLog.d("size of recent post list: " + mObGetComments.data.size());
                    adapterRecentPostDetail.updateAllCommentList(mObGetComments);
                    if (listviewRecentPostDetail.getVisibility() == View.GONE)
                        listviewRecentPostDetail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {

            }
        });
    }

    private void requestGetBravo() {
        if (mBravoObj == null)
            return;
        BravoRequestManager.getInstance(getActivity()).requestToGetBravoForSpotDetail(getActivity(), mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetBravo obGetBravo = gson.fromJson(response.toString(), ObGetBravo.class);
                AIOLog.d("obGetAllBravoRecentPosts:" + obGetBravo);
                if (obGetBravo == null)
                    return;
                else {
                    String Last_Pic = obGetBravo.data.Last_Pic;
                    ArrayList<String> image = obGetBravo.data.Bravo_Pics;
                    for (int i = 0; i < image.size(); i++) {
                        Log.d("KieuThang", "image:" + i + ":=>" + image.get(i));
                    }
                    Log.d("KieuThang", "Last_Pic:" + Last_Pic);
                    mBravoObj = obGetBravo.data;
                    mBravoObj.Last_Pic = Last_Pic;
                    mBravoObj.Bravo_Pics = obGetBravo.data.Bravo_Pics;
                    setBravoOb(mBravoObj);
                    requestGetLiked();
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail.this);
    }

    private void requestGetFollowingCheck() {
        BravoRequestManager.getInstance(getActivity()).requestGetFollowingCheck(mBravoObj.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetFollowingCheck obGetFollowCheck;
                obGetFollowCheck = gson.fromJson(response.toString(), ObGetFollowingCheck.class);
                if (obGetFollowCheck == null)
                    return;
                else {
                    adapterRecentPostDetail.updateFollowing(obGetFollowCheck.valid == 1 ? true : false);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        });
    }

    private void requestDeleteFollow(ObBravo obBravo) {
        if (mBravoObj == null)
            return;
        BravoRequestManager.getInstance(getActivity()).requestDeleteFollow(mBravoObj.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                adapterRecentPostDetail.updateFollowing(false);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        }, FragmentBravoDetail.this);
    }

    private void requestToPutFollow(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutFollow(obBravo.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {

                AIOLog.d("response putFollow :===>" + response);
                adapterRecentPostDetail.updateFollowing(true);
                showDialogFollowingOK(mBravoObj.Full_Name);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.e("errorMessage");
            }
        }, FragmentBravoDetail.this);
    }

    // private static final long TIME_TO_FINISH = 14500;
    // private Movie mMovie;
    // private InputStream mInputStream = null;
    // private long mMovieStart;

    public void showDialogFollowingOK(String fullName) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        View dialog_view = inflater.inflate(R.layout.dialog_following, null);
        Button btnOK = (Button) dialog_view.findViewById(R.id.btn_ok);
        FrameLayout frameLoop = (FrameLayout) dialog_view.findViewById(R.id.flower_loop);
        View view = new GIFView(getActivity());
        view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
        // mInputStream = getActivity().getResources().openRawResource(R.drawable.flower_anim);
        // mMovie = Movie.decodeStream(mInputStream);
        frameLoop.addView(view);
        TextView txtContent = (TextView) dialog_view.findViewById(R.id.txt_following_content);
        txtContent.setText(getActivity().getResources().getString(R.string.profile_follow_alert).replace("%s", fullName));
        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setContentView(dialog_view);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    private class GIFView extends View {

        public GIFView(Context context) {
            super(context);
            mInputStream = context.getResources().openRawResource(R.drawable.flower_anim);
            mMovie = Movie.decodeStream(mInputStream);
        }

        private Movie       mMovie;
        private InputStream mInputStream = null;
        private long        mMovieStart;

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            long now = android.os.SystemClock.uptimeMillis();
            if (mMovieStart == 0) {
                mMovieStart = now;
            }
            int relTime = (int) ((now - mMovieStart) % mMovie.duration());
            mMovie.setTime(relTime);
            double scalex = (double) this.getWidth() / (double) mMovie.width();
            double scaley = (double) this.getHeight() / (double) mMovie.height();
            canvas.scale((float) scalex, (float) scaley);
            mMovie.draw(canvas, 0, 0, paint);
            this.invalidate();
        }
    }

    private void requestGetMyListItem() {
        BravoRequestManager.getInstance(getActivity()).requestGetMyListItem(mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestFollowingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetMylistItem obGetMylistItem;
                obGetMylistItem = gson.fromJson(response.toString(), ObGetMylistItem.class);
                if (obGetMylistItem == null)
                    return;
                else {
                    adapterRecentPostDetail.updateSave(obGetMylistItem.valid == 1 ? true : false);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        });
    }

    private void requestGetLikeItem() {
        BravoRequestManager.getInstance(getActivity()).requestGetLikeItem(mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestLikingCheck:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetLikeItem mObGetLikeItem;
                mObGetLikeItem = gson.fromJson(response.toString(), ObGetLikeItem.class);
                if (mObGetLikeItem == null)
                    return;
                else {
                    adapterRecentPostDetail.updateLike(mObGetLikeItem.valid == 1 ? true : false);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        });
    }

    private void requestGetLiked() {
        BravoRequestManager.getInstance(getActivity()).requestGetLiked(mBravoObj.Spot_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("ObGetSpot:" + response);
                Gson gson = new GsonBuilder().serializeNulls().create();
                ObGetSpot mObGetSpot;
                mObGetSpot = gson.fromJson(response.toString(), ObGetSpot.class);
                if (mObGetSpot == null)
                    return;
                else {
                    AIOLog.e("Spot.data" + mObGetSpot.data);
                    adapterRecentPostDetail.updateLikedandSaved(mObGetSpot.data);
                }
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        });
    }

    private void requestDeleteMyListItem(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestDeleteMyListItem(mBravoObj.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response requestDeleteMyListItem :===>" + response);
                adapterRecentPostDetail.updateSave(false);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage: " + errorMessage);
            }
        }, FragmentBravoDetail.this);
    }

    private void requestToPutMyListItem(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutMyListItem(obBravo.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                adapterRecentPostDetail.updateSave(true);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("response error");
            }
        }, FragmentBravoDetail.this);
    }

    private void requestDeleteLike(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestDeleteLike(obBravo.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                adapterRecentPostDetail.updateLike(false);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        }, FragmentBravoDetail.this);
    }

    private void requestToPutLike(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutLike(obBravo.Bravo_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                adapterRecentPostDetail.updateLike(true);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        }, FragmentBravoDetail.this);
    }

    private void requestToPutReport(ObBravo obBravo) {
        BravoRequestManager.getInstance(getActivity()).requestToPutReport(obBravo.User_ID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("response putReport :===>" + response);
                DialogUtility.showDialogReportOk(getActivity());
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        }, FragmentBravoDetail.this);
    }

    private void requestToPostComment(String commentText) {
        if (mBravoObj == null)
            return;
        BravoRequestManager.getInstance(getActivity()).requestToPostComment(getActivity(), commentText, mBravoObj.Bravo_ID, new IRequestListener() {
            @Override
            public void onResponse(String response) {
                AIOLog.d("response putFollow :===>" + response);
                requestGetComments();
            }

            @Override
            public void onErrorResponse(String errorMessage) {
                AIOLog.d("errorMessage:" + errorMessage);
            }
        }, FragmentBravoDetail.this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !isBackStatus() && NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            adapterRecentPostDetail.updateMapView();
            adapterRecentPostDetail.removeAllComments();
            listviewRecentPostDetail.setSelection(0);
            requestGetBravo();
            requestGetFollowingCheck();
            requestGetMyListItem();
            requestGetComments();
            requestGetLikeItem();
        }
    }

    @Override
    public void goToCallSpot() {
        DialogUtility.showDialogCallSpot(getActivity(), mBravoObj);
    }

    @Override
    public void goToShare() {
        DialogUtility.showDialogShare(getActivity(), mHomeActionListener, mBravoObj);
    }

    @Override
    public void goToFragment(int fragmentID) {
        if (fragmentID == HomeActivity.FRAGMENT_MAP_VIEW_ID) {
            mHomeActionListener.goToMapView(mBravoObj.Spot_Latitude, mBravoObj.Spot_Longitude,
                    FragmentMapView.MAKER_BY_LOCATION_SPOT);
            return;
        }
        mHomeActionListener.goToFragment(fragmentID);
    }

    @SuppressWarnings("static-access")
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        AIOLog.d("requestCode:" + requestCode + ", resultCode:" + resultCode + ", data:" + data);
        switch (requestCode) {
        case REQUEST_CODE_CAMERA:
            if (resultCode == getActivity().RESULT_OK) {
                if (mCapturedImageURI == null) {
                    AIOLog.d("mCapturedImageURI is null");
                    if (data == null || data.getExtras() == null)
                        return;
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    if (photo == null)
                        return;
                    else {
                        cropImageFromUri(data.getData());
                        return;
                    }
                }

                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(mCapturedImageURI, projection, null, null, null);
                if (cursor == null) {
                    AIOLog.d("cursor is null");
                    return;
                }
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                // THIS IS WHAT YOU WANT!
                String capturedImageFilePath = cursor.getString(column_index_data);

                File file = new File(capturedImageFilePath);
                if (file.exists()) {
                    Uri fileUri = Uri.fromFile(file);
                    cropImageFromUri(fileUri);
                }
            }
            break;
        case REQUEST_CODE_GALLERY:
            if (resultCode == getActivity().RESULT_OK) {
                AIOLog.d("data=" + data);
                if (data == null) {
                    AIOLog.d("Opps!Can not get data from gallery.");
                    return;
                }

                Uri uri = data.getData();
                if (uri == null)
                    return;
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                if (getActivity().getContentResolver() == null)
                    return;
                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor == null)
                    return;
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imagePath = cursor.getString(columnIndex);
                cursor.close();

                File file = new File(imagePath);
                if (file.exists()) {
                    Uri fileUri = Uri.fromFile(file);
                    cropImageFromUri(fileUri);
                } else {

                    AIOLog.d("file don't exist !");
                }
            }
            break;
        case CROP_FROM_CAMERA:
            if (resultCode == getActivity().RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    updateBravoImage(photo);
                }
            }
            break;
        default:
            return;
        }
    }

    private void updateBravoImage(Bitmap photo) {
        BravoRequestManager.getInstance(getActivity()).requestToUpdateImageForBravo(photo, mBravoObj.Bravo_ID, FragmentBravoDetail.this,
                new IRequestListener() {

                    @Override
                    public void onResponse(String response) {
                        AIOLog.d("updateBravoImage: " + response);
                        requestGetBravo();
                    }

                    @Override
                    public void onErrorResponse(String errorMessage) {
                        requestGetBravo();
                    }
                });
    }

    private void cropImageFromUri(Uri uri) {
        AIOLog.d("uri:" + uri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

        int size = list.size();
        AIOLog.d("size:" + size);
        if (size == 0) {
            showToast("Can not crop image");
            return;
        } else {
            intent.setData(uri);
            intent.putExtra("outputX", 1500);
            intent.putExtra("outputY", 1500);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            if (size >= 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            }
        }
    }

    @Override
    public void goToReport() {
        DialogUtility.showDialogReport(getActivity(), new IDialogListener() {

            @Override
            public void onClickPositve() {
                requestToPutReport(mBravoObj);
            }

            @Override
            public void onClickNegative() {

            }

            @Override
            public void onClickCancel() {

            }
        });
    }

    @Override
    public void goToFollow(boolean isFollow) {
        if (isFollow)
            requestToPutFollow(mBravoObj);
        else
            DialogUtility.showDialogStopFollowing(getActivity(), new IDialogListener() {

                @Override
                public void onClickPositve() {
                    requestDeleteFollow(mBravoObj);
                }

                @Override
                public void onClickNegative() {

                }

                @Override
                public void onClickCancel() {

                }
            });
    }

    @Override
    public void goToSubmitComment(String commentText) {
        requestToPostComment(commentText);
    }

    @Override
    public void goToSave(boolean isSave) {
        if (isSave)
            requestToPutMyListItem(mBravoObj);
        else
            requestDeleteMyListItem(mBravoObj);
    }

    @Override
    public void goToCoverImage() {
        mHomeActionListener.goToCoverImage(mBravoObj);
    }

    @Override
    public void goToUserDataTab(String useId) {
        mHomeActionListener.goToUserData(useId);
    }

    @Override
    public void choosePicture() {
        DialogUtility.showDialogChooseImage(getActivity(), new IDialogListener() {

            @Override
            public void onClickPositve() {
                // when user click camera to get image
                try {
                    String fileName = "cover" + Calendar.getInstance().getTimeInMillis() + ".jpg";
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } catch (Exception e) {
                    AIOLog.e("exception:" + e.getMessage());
                }
            }

            @Override
            public void onClickNegative() {
                // when user click gallery to get image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            }

            @Override
            public void onClickCancel() {

            }
        });
    }

    @Override
    public void goToLiked() {
        mHomeActionListener.goToLiked(mBravoObj.Spot_ID);
    }

    @Override
    public void goToSaved() {
        mHomeActionListener.goToSaved(mBravoObj.Spot_ID);
    }

    @Override
    public void goToLike(boolean isLike) {
        if (isLike)
            requestToPutLike(mBravoObj);
        else
            requestDeleteLike(mBravoObj);
    }

    @Override
    public void goToSpotDetail() {
        mHomeActionListener.goToSpotDetail(mSpot);
    }

    public void updateInfo() {
        if (NetworkUtility.getInstance(getActivity()).isNetworkAvailable()) {
            adapterRecentPostDetail.updateMapView();
            requestGetBravo();
            requestGetFollowingCheck();
            requestGetMyListItem();
            requestGetComments();
            requestGetLikeItem();
        }
    }

    @Override
    public void deleteComment(String commentID) {
        if (mObGetComments == null)
            return;
        for (int i = 0; i < mObGetComments.data.size(); i++) {
            if (mObGetComments.data.get(i).commentID.equals(commentID)) {
                adapterRecentPostDetail.removeComment(i);
                break;
            }
        }

        BravoRequestManager.getInstance(getActivity()).requestToDeleteComment(commentID, new IRequestListener() {

            @Override
            public void onResponse(String response) {
                AIOLog.d("requestToDeleteComment:" + response);
            }

            @Override
            public void onErrorResponse(String errorMessage) {
            }
        });
    }
}
