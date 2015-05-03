package com.visva.android.app.funface.view.widget;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.visva.android.app.funface.utils.MultiTouchController.PositionAndScale;

public class FaceView {
    private static final int   UI_MODE_ANISOTROPIC_SCALE = 2;

    private Drawable           drawable;

    private int                width, height, displayWidth, displayHeight;

    private float              centerX, centerY, scaleX, scaleY, angle;

    private float              minX, maxX, minY, maxY;

    private static final float SCREEN_MARGIN             = 100;

    public FaceView(int resId, Resources res, int eyeDistance) {
        this.drawable = res.getDrawable(resId);
        this.width = eyeDistance;
        this.height = eyeDistance;
        Log.d("KieuThang", "drawable width:" + width + ",height:" + height);
        getMetrics(res);
    }

    private void getMetrics(Resources res) {
        DisplayMetrics metrics = res.getDisplayMetrics();
        this.displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.max(metrics.widthPixels,
                metrics.heightPixels) : Math.min(metrics.widthPixels, metrics.heightPixels);
        this.displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math.min(metrics.widthPixels,
                metrics.heightPixels) : Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    /** Called by activity's onPause() method to free memory used for loading the images */
    public void unload() {
        this.drawable = null;
    }

    /** Set the position and scale of an image in screen coordinates */
    public boolean setPos(PositionAndScale newImgPosAndScale, int uiMode) {
        return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), (uiMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale
                .getScaleX() : newImgPosAndScale.getScale(), (uiMode & UI_MODE_ANISOTROPIC_SCALE) != 0 ? newImgPosAndScale.getScaleY()
                : newImgPosAndScale.getScale(), newImgPosAndScale.getAngle());
        // FIXME: anisotropic scaling jumps when axis-snapping
        // FIXME: affine-ize
        // return setPos(newImgPosAndScale.getXOff(), newImgPosAndScale.getYOff(), newImgPosAndScale.getScaleAnisotropicX(),
        // newImgPosAndScale.getScaleAnisotropicY(), 0.0f);
    }

    /** Set the position and scale of an image in screen coordinates */
    public boolean setPos(float centerX, float centerY, float scaleX, float scaleY, float angle) {
        float ws = width * scaleX;
        float hs = height * scaleY;
        float newMinX = centerX - ws;
        float newMinY = centerY - hs;
        float newMaxX = centerX + ws;
        float newMaxY = centerY + hs;
        if (newMinX > displayWidth - SCREEN_MARGIN || newMaxX < SCREEN_MARGIN || newMinY > displayHeight - SCREEN_MARGIN
                || newMaxY < SCREEN_MARGIN)
            return false;
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
        canvas.save();
        float dx = (maxX + minX) / 2;
        float dy = (maxY + minY) / 2;
        drawable.setBounds((int) minX, (int) minY, (int) maxX, (int) maxY);
        canvas.translate(dx, dy);
        canvas.rotate(angle * 180.0f / (float) Math.PI);
        canvas.translate(-dx, -dy);
        drawable.draw(canvas);
        canvas.restore();
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

}