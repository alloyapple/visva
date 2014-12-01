package com.sharebravo.bravo.view.lib.gifanimation;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.view.View;

import com.sharebravo.bravo.R;

public class GifView extends View{

    public GifView(Context context) {
        super(context);
        mInputStream = context.getResources().openRawResource(R.drawable.flower_loop);
        mMovie = Movie.decodeStream(mInputStream);
    }

    private Movie       mMovie;
    private InputStream mInputStream = null;
    private long              mMovieStart;

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
        //double scalex = (double) this.getWidth() / (double) mMovie.width();
        // double scaley = (double) this.getHeight() / (double) mMovie.height();
        //canvas.scale((float) scalex, (float) scalex);
        mMovie.draw(canvas, 0, 70, paint);
        this.invalidate();
    }
}
