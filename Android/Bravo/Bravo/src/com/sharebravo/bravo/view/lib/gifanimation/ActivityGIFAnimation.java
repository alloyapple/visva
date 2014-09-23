package com.sharebravo.bravo.view.lib.gifanimation;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.sharebravo.bravo.R;

@SuppressLint("DrawAllocation")
public class ActivityGIFAnimation extends GraphicsActivity {
    private static final long TIME_TO_FINISH = 14500;
    private Movie             mMovie;
    private InputStream       mInputStream   = null;
    private long              mMovieStart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new GIFView(this);
        setContentView(view);

        mInputStream = getResources().openRawResource(R.drawable.bravo_jump);
        mMovie = Movie.decodeStream(mInputStream);
    }

    private class GIFView extends View {

        public GIFView(Context context) {
            super(context);
            mInputStream = context.getResources().openRawResource(R.drawable.bravo_jump);
            mMovie = Movie.decodeStream(mInputStream);
        }

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
            if (relTime > TIME_TO_FINISH) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
            mMovie.setTime(relTime);
            double scalex = (double) this.getWidth() / (double) mMovie.width();
            double scaley = (double) this.getHeight() / (double) mMovie.height();
            canvas.scale((float) scalex, (float) scaley);
            mMovie.draw(canvas, 0, -20, paint);
            this.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}