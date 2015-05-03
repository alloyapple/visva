/**
 * PhotoSorterView.java
 * 
 * (c) Luke Hutchison (luke.hutch@mit.edu)
 * 
 * TODO: Add OpenGL acceleration.
 * 
 * --
 * 
 * Released under the MIT license (but please notify me if you use this code, so that I can give your project credit at
 * http://code.google.com/p/android-multitouch-controller ).
 * 
 * MIT license: http://www.opensource.org/licenses/MIT
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */
package com.visva.android.app.funface.view.widget;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.visva.android.app.funface.utils.MultiTouchController;
import com.visva.android.app.funface.utils.MultiTouchController.MultiTouchObjectCanvas;
import com.visva.android.app.funface.utils.MultiTouchController.PointInfo;
import com.visva.android.app.funface.utils.MultiTouchController.PositionAndScale;

public class FaceViewGroup extends View implements MultiTouchObjectCanvas<FaceView> {

    private ArrayList<FaceView>            mImages                    = new ArrayList<FaceView>();

    // --

    private MultiTouchController<FaceView> multiTouchController       = new MultiTouchController<FaceView>(this);

    // --

    private PointInfo                      currTouchPoint             = new PointInfo();

    private boolean                        mShowDebugInfo             = true;

    private static final int               UI_MODE_ROTATE             = 1, UI_MODE_ANISOTROPIC_SCALE = 2;

    private int                            mUIMode                    = UI_MODE_ROTATE;

    // --

    private Paint                          mLinePaintTouchPointCircle = new Paint();

    // ---------------------------------------------------------------------------------------------------

    public FaceViewGroup(Context context) {
        this(context, null);
    }

    public FaceViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mLinePaintTouchPointCircle.setColor(Color.YELLOW);
        mLinePaintTouchPointCircle.setStrokeWidth(5);
        mLinePaintTouchPointCircle.setStyle(Style.STROKE);
        mLinePaintTouchPointCircle.setAntiAlias(true);
        setBackgroundColor(Color.TRANSPARENT);
    }

    /** Called by activity's onResume() method to load the images */
    public void loadImages(Context context) {
        Resources res = context.getResources();
        int n = mImages.size();
        //for (int i = 0; i < n; i++)
         //   mImages.get(i).load(res);
    }

    /** Called by activity's onPause() method to free memory used for loading the images */
    public void unloadImages() {
        int n = mImages.size();
        for (int i = 0; i < n; i++)
            mImages.get(i).unload();
    }

    // ---------------------------------------------------------------------------------------------------

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int n = mImages.size();
        for (int i = 0; i < n; i++)
            mImages.get(i).draw(canvas);
        if (mShowDebugInfo)
            drawMultitouchDebugMarks(canvas);
    }

    // ---------------------------------------------------------------------------------------------------

    public void trackballClicked() {
        mUIMode = (mUIMode + 1) % 3;
        invalidate();
    }

    private void drawMultitouchDebugMarks(Canvas canvas) {
        if (currTouchPoint.isDown()) {
            float[] xs = currTouchPoint.getXs();
            float[] ys = currTouchPoint.getYs();
            float[] pressures = currTouchPoint.getPressures();
            int numPoints = Math.min(currTouchPoint.getNumTouchPoints(), 2);
            for (int i = 0; i < numPoints; i++)
                canvas.drawCircle(xs[i], ys[i], 50 + pressures[i] * 80, mLinePaintTouchPointCircle);
            if (numPoints == 2)
                canvas.drawLine(xs[0], ys[0], xs[1], ys[1], mLinePaintTouchPointCircle);
        }
    }

    // ---------------------------------------------------------------------------------------------------

    /** Pass touch events to the MT controller */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return multiTouchController.onTouchEvent(event);
    }

    /** Get the image that is under the single-touch point, or return null (canceling the drag op) if none */
    public FaceView getDraggableObjectAtPoint(PointInfo pt) {
        float x = pt.getX(), y = pt.getY();
        int n = mImages.size();
        for (int i = n - 1; i >= 0; i--) {
            FaceView im = mImages.get(i);
            if (im.containsPoint(x, y))
                return im;
        }
        return null;
    }

    /**
     * Select an object for dragging. Called whenever an object is found to be under the point (non-null is returned by getDraggableObjectAtPoint())
     * and a drag operation is starting. Called with null when drag op ends.
     */
    public void selectObject(FaceView FaceView, PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        if (FaceView != null) {
            // Move image to the top of the stack when selected
            mImages.remove(FaceView);
            mImages.add(FaceView);
        } else {
            // Called with FaceView == null when drag stops.
        }
        invalidate();
    }

    /** Get the current position and scale of the selected image. Called whenever a drag starts or is reset. */
    public void getPositionAndScale(FaceView FaceView, PositionAndScale objPosAndScaleOut) {
        // FIXME affine-izem (and fix the fact that the anisotropic_scale part requires averaging the two scale factors)
        objPosAndScaleOut.set(FaceView.getCenterX(), FaceView.getCenterY(), (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
                (FaceView.getScaleX() + FaceView.getScaleY()) / 2, (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, FaceView.getScaleX(),
                FaceView.getScaleY(),
                (mUIMode & UI_MODE_ROTATE) != 0, FaceView.getAngle());
    }

    /** Set the position and scale of the dragged/stretched image. */
    public boolean setPositionAndScale(FaceView FaceView, PositionAndScale newFaceViewPosAndScale, PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        boolean ok = FaceView.setPos(newFaceViewPosAndScale, mUIMode);
        if (ok)
            invalidate();
        return ok;
    }

    public void addFace(FaceView faceView, Context context) {
        // TODO Auto-generated method stub
        int size = mImages.size();
        mImages.add(faceView);
       // mImages.get(size).load(context.getResources());
    }
}
