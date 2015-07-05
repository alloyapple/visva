package com.visva.android.app.funface.view.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.visva.android.app.funface.R;
import com.visva.android.app.funface.constant.FunFaceConstant;
import com.visva.android.app.funface.log.AIOLog;
import com.visva.android.app.funface.utils.StringUtility;
import com.visva.android.app.funface.utils.Utils;
import com.visva.android.app.funface.utils.MultiTouchController.PositionAndScale;

public class FaceView implements Cloneable {
    private static final float SCREEN_MARGIN_TOP         = 70;
    private static final int   UI_MODE_ANISOTROPIC_SCALE = 2;
    private int                faceId;
    private int                resId;
    private Drawable           drawable;
    private int                width, height, mScreenWidth, mScreenHeight;
    private float              centerX, centerY, scaleX, scaleY, angle;
    private float              minX, maxX, minY, maxY;
    private boolean            isVisible                 = true;
    private int                mHeightOfLayoutEffect;
    private Context            mContext;

    private Paint              mPaint;
    private String             mText;

    public FaceView(Context context, int resId, int eyeDistance, int id) {
        this.mContext = context;
        this.resId = resId;
        this.faceId = id;
        this.drawable = context.getResources().getDrawable(resId);
        this.width = eyeDistance;
        this.height = eyeDistance;
        getMetrics(context.getResources());
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    private void getMetrics(Resources res) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        this.mScreenWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
        this.mScreenHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
        mHeightOfLayoutEffect = (int) res.getDimensionPixelSize(R.dimen.layout_effect_height);
    }

    /** Called by activity's onPause() method to free memory used for loading the images */
    public void unload() {
        this.drawable = null;
    }

    public void load(Context context, int resId) {
        this.resId = resId;
        this.drawable = context.getResources().getDrawable(resId);
    }

    /** Set the position and scale of an image in screen coordinates */
    public boolean setPos(PositionAndScale newImgPosAndScale, int uiMode) {
        return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), (uiMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale
                .getScaleX() : newImgPosAndScale.getScale(), (uiMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale.getScaleY()
                : newImgPosAndScale.getScale(), newImgPosAndScale.getAngle());
    }

    /** Set the position and scale of an image in screen coordinates */
    public boolean setPos(float centerX, float centerY, float scaleX, float scaleY, float angle) {
        float ws = width * scaleX;
        float hs = height * scaleY;
        float newMinX = centerX - ws;
        float newMinY = centerY - hs;
        float newMaxX = centerX + ws;
        float newMaxY = centerY + hs;
        Log.d("KieuThang", "newMinX > mScreenWidth - 3 * ws / 2 :"+(newMinX > mScreenWidth - 3 * ws / 2 ));
        Log.d("KieuThang", "newMaxX < 3 * ws / 2 :"+(newMaxX < 3 * ws / 2));
        Log.d("KieuThang", "newMinY > mScreenHeight - 3 * hs / 2 - mHeightOfLayoutEffect :"+(newMinY > mScreenHeight - 3 * hs / 2 - mHeightOfLayoutEffect));
        Log.d("KieuThang", "newMaxY < hs + SCREEN_MARGIN_TOP :"+(newMaxY < hs + SCREEN_MARGIN_TOP));
//        if (newMinX > mScreenWidth - 3 * ws / 2 || newMaxX < 3 * ws / 2 || newMinY > mScreenHeight - 3 * hs / 2 - mHeightOfLayoutEffect
//                || newMaxY < hs + SCREEN_MARGIN_TOP)
//            return false;
        this.centerX = centerX;
        this.centerY = centerY;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.angle = angle;
        this.minX = newMinX;
        this.minY = newMinY;
        this.maxX = newMaxX;
        this.maxY = newMaxY;
        return true;
    }

    /** Return whether or not the given screen coords are inside this image */
    public boolean containsPoint(float scrnX, float scrnY) {
        // FIXME: need to correctly account for image rotation
        return (scrnX >= minX && scrnX <= maxX && scrnY >= minY && scrnY <= maxY);
    }

    public void draw(Canvas canvas) {
        if (drawable != null) {
            if (isVisible) {
                drawable.setAlpha(0xff);
            } else {
                drawable.setAlpha(0x00);
            }
            canvas.save();
            float dx = (maxX + minX) / 2;
            float dy = (maxY + minY) / 2;
            drawable.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
            canvas.translate(dx, dy);
            canvas.rotate(angle * 180.0f / (float) Math.PI);
            canvas.translate(-dx, -dy);
            drawable.draw(canvas);
            if (!StringUtility.isEmpty(mText) && isVisible) {
                mPaint.setTextSize(Math.abs((maxY - minY) / 8));
                float textLength = mPaint.measureText(mText);
                int x = (int) (dx - textLength / 2);
                x = x > 0 ? x : 0;
                Log.d("KieuThang", "x:" + x + ",dy:" + dy + ", textSize:" + (maxY - minY));
                canvas.drawText(mText, x, dy, mPaint);
            }
            canvas.restore();
        }
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public float getAngle() {
        return angle;
    }

    // FIXME: these need to be updated for rotation
    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setY(int y) {
        centerY = y;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getText() {
        return mText;
    }

    public void setVisible(int visible) {
        switch (visible) {
        case View.GONE:
            isVisible = false;
            break;

        default:
            isVisible = true;
            break;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setPosition(PointF midPoint, float mRatioX, float mRatioY, int bitmapWidth, int bitmapHeight, int realImageHeight) {
        float scaleX = 1.0F;
        float scaleY = 1.0F;
        float centerX = midPoint.x * mRatioX;
        float centerY = midPoint.y * mRatioY;
        if (bitmapWidth >= bitmapHeight)
            centerY = getCenterYOfFace(midPoint, bitmapWidth, bitmapHeight, realImageHeight);
        float angle = Utils.getInitAngle();

        setPos(centerX, centerY, scaleX, scaleY, angle);
    }

    private float getCenterYOfFace(PointF midPoint, int bitmapWidth, int bitmapHeight, int realImageHeight) {
        int faceSizeMargin = (int) mContext.getResources().getDimension(R.dimen.face_margin);
        PointF resizedMidPoint = new PointF();
        AIOLog.d(FunFaceConstant.TAG, "heightOfImage:" + realImageHeight);
        float ratio = (float) bitmapWidth / mScreenWidth;
        float displayedImageHeight = (float) bitmapHeight / ratio;
        resizedMidPoint.x = midPoint.x / ratio;
        resizedMidPoint.y = midPoint.y / ratio;
        return ((float) realImageHeight - displayedImageHeight) / 2 + resizedMidPoint.y - faceSizeMargin;
    }
}