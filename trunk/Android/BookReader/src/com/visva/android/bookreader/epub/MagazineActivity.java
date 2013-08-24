package com.visva.android.bookreader.epub;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.skytree.epub.CacheListener;
import com.skytree.epub.ClickListener;
import com.skytree.epub.ContentListener;
import com.skytree.epub.FixedControl;
import com.skytree.epub.MediaOverlayListener;
import com.skytree.epub.PageInformation;
import com.skytree.epub.PageMovedListener;
import com.skytree.epub.PageTransition;
import com.skytree.epub.Parallel;
import com.skytree.epub.Setting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MagazineActivity extends Activity {
	RelativeLayout ePubView;
	FixedControl fv;
	Button debugButton0;
	Button debugButton1;
	Button debugButton2;
	Button debugButton3;
	Button debugButton4;
	
	Parallel currentParallel;
	boolean autoStartPlayingWhenNewPagesLoaded = false;
	boolean autoMovePageWhenParallesFinished = false;
	boolean isAutoPlaying = false;

	
	private void debug(String msg) {
		Log.w("EPub",msg);
	}
	
    public void onCreate(Bundle savedInstanceState) {
        String fileName = new String();
		Bundle bundle = getIntent().getExtras();
		fileName = bundle.getString("BOOKNAME");
		super.onCreate(savedInstanceState); 		
		
		ePubView = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		ePubView.setLayoutParams(rlp);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height        
		fv = new FixedControl(this);
		Bitmap pagesStack = 	BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/images/PagesStack.png");
		Bitmap pagesCenter = 	BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/images/PagesCenter.png");		
		fv.setPagesCenterImage(pagesCenter);
		fv.setPagesStackImage(pagesStack);
		fv.setContentListener(new ContentHandler());
		fv.setContentListenerForCache(new ContentHandler());
		fv.setCacheListener(new CacheDelegate());
		fv.setBaseDirectory(getFilesDir() + "/books");
        fv.setBookName(fileName);
        fv.setLayoutParams(params);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.width =  LayoutParams.FILL_PARENT;	// 400;
        params.height = LayoutParams.FILL_PARENT;	// 600;
        ClickDelegate cd = new ClickDelegate();
        fv.setClickListener(cd);
        PageMovedDelegate pd = new PageMovedDelegate();
        fv.setPageMovedListener(pd);
        fv.setTimeForRendering(1000);
        fv.setNavigationAreaWidthRatio(0.2f);
        fv.setMediaOverlayListener(new MediaOverlayDelegate());
        fv.setSequenceBasedForMediaOverlay(false);

        // If you want to get the license key for commercial or non commercial use, please don't hesitate to email us. (skytree21@gmail.com). 
		// Without the proper license key, watermark message(eg.'unlicensed') may be shown in background. 
		fv.setLicenseKey("0000-0000-0000-0000");	
        
		int transitionType = bundle.getInt("transitionType");
		if (transitionType==0) {
			fv.setPageTransition(PageTransition.None);
		}else if (transitionType==1) {
			fv.setPageTransition(PageTransition.Slide);
		}else if (transitionType==2) {
			fv.setPageTransition(PageTransition.Curl);
		}

        ePubView.addView(fv);

        this.makeUI();        
        setContentView(ePubView);
    }
    
    void makeUI() {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
    	
		RelativeLayout.LayoutParams debugButton0Param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		debugButton0 = new Button(this);
		debugButton0.setText("|<");
		debugButton0Param.leftMargin = (int) (20 * density);
		debugButton0Param.topMargin = (int) (5 * density);
		debugButton0Param.width = (int) (55 * density);
		debugButton0Param.height = (int) (35 * density);
		debugButton0.setLayoutParams(debugButton0Param);
		debugButton0.setId(8080);
		debugButton0.setOnClickListener(listener);
		ePubView.addView(debugButton0);

		RelativeLayout.LayoutParams debugButton1Param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		debugButton1 = new Button(this);
		debugButton1.setText("Play");
		debugButton1Param.leftMargin = (int) (80 * density);
		debugButton1Param.topMargin = (int) (5 * density);
		debugButton1Param.width = (int) (55 * density);
		debugButton1Param.height = (int) (35 * density);
		debugButton1.setLayoutParams(debugButton1Param);
		debugButton1.setId(8081);
		debugButton1.setOnClickListener(listener);
		ePubView.addView(debugButton1);
		
		RelativeLayout.LayoutParams debugButton2Param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		debugButton2 = new Button(this);
		debugButton2.setText("Stop");
		debugButton2Param.leftMargin = (int) (140 * density);
		debugButton2Param.topMargin = (int) (5 * density);
		debugButton2Param.width = (int) (55 * density);
		debugButton2Param.height = (int) (35 * density);
		debugButton2.setLayoutParams(debugButton2Param);
		debugButton2.setId(8082);
		debugButton2.setOnClickListener(listener);
		ePubView.addView(debugButton2);
		
		RelativeLayout.LayoutParams debugButton3Param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		debugButton3 = new Button(this);
		debugButton3.setText(">|");
		debugButton3Param.leftMargin = (int) (200 * density);
		debugButton3Param.topMargin = (int) (5 * density);
		debugButton3Param.width = (int) (55 * density);
		debugButton3Param.height = (int) (35 * density);
		debugButton3.setLayoutParams(debugButton3Param);
		debugButton3.setId(8083);
		debugButton3.setOnClickListener(listener);
		ePubView.addView(debugButton3);

		RelativeLayout.LayoutParams debugButton4Param = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		debugButton4 = new Button(this);
		debugButton4.setText("Exit");
		debugButton4Param.leftMargin = (int) (280 * density);
		debugButton4Param.topMargin = (int) (5 * density);
		debugButton4Param.width = (int) (70 * density);
		debugButton4Param.height = (int) (35 * density);
		debugButton4.setLayoutParams(debugButton4Param);
		debugButton4.setId(8084);
		debugButton4.setOnClickListener(listener);
		ePubView.addView(debugButton4);    	
    }
    
	private OnClickListener listener=new OnClickListener(){
		public void onClick(View arg) {			
			if (arg.getId() == 8080) {
//				playPrev();
//				rv.gotoPageByPagePositionInBook(0.67);
				fv.debug0("");
			} else if (arg.getId() == 8081) {
				playAndPause();
			} else if (arg.getId() == 8082) {
				stopPlaying();		
			} else if (arg.getId() == 8083) {
				playNext();
			} else if (arg.getId() == 8084) {
				finish();				
			}	    
		}
	};
	
	class MediaOverlayDelegate implements MediaOverlayListener {
		@Override
		public void onParallelStarted(Parallel parallel) {
			// TODO Auto-generated method stub
			fv.changeElementColor("#FFFF00",parallel.hash,parallel.pageIndex);
			currentParallel = parallel;			
		}

		@Override
		public void onParallelEnded(Parallel parallel) {
			// TODO Auto-generated method stub
			fv.restoreElementColor();			
		}

		@Override
		public void onParallelsEnded() {
			// TODO Auto-generated method stub
		    fv.restoreElementColor();
		    if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = true;
		    if (autoMovePageWhenParallesFinished) {
		        fv.gotoNextPage();
		    }
		}		
	}

	
	void playAndPause() {
		if (fv.isPlayingPaused()) {
	        if (!fv.isPlayingStarted()) {
	            fv.playFirstParallel();	        	
	            if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = true;
	        }else {
	            fv.resumePlayingParallel();	        	
	            if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = true;
	        }        
	    
	    }else {	        
	        fv.pausePlayingParallel();
	        if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = false;
	    }		
	}
	
	void stopPlaying() {
//	    [button1 setTitle:@"Play" forState:UIControlStateNormal];
	    fv.stopPlayingParallel();
	    fv.restoreElementColor();
	    if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = false;
	}
	
	void playPrev() {
	    fv.restoreElementColor();
	    if (currentParallel.parallelIndex==0) {
	        if (autoMovePageWhenParallesFinished) fv.gotoPrevPage();
	    }else {
	        fv.playPrevParallel();
	    }
	}

	void playNext() {
	    fv.restoreElementColor();
		fv.playNextParallel();
	}
	
	public void onStop() {
		super.onStop();
		debug("onStop");
	}
	
	public void onPause() {
		super.onPause();
		debug("onPause");
	}
	
	public void onDestory() {
		super.onDestroy();
		debug("onDestory");		
	}
	
	void showMediaUI() {
		debugButton0.setVisibility(View.VISIBLE);
		debugButton1.setVisibility(View.VISIBLE);
		debugButton2.setVisibility(View.VISIBLE);
		debugButton3.setVisibility(View.VISIBLE);
	}
	
	void hideMediaUI() {
		debugButton0.setVisibility(View.INVISIBLE);
		debugButton1.setVisibility(View.INVISIBLE);
		debugButton2.setVisibility(View.INVISIBLE);
		debugButton3.setVisibility(View.INVISIBLE);
		debugButton0.setVisibility(View.GONE);
		debugButton1.setVisibility(View.GONE);
		debugButton2.setVisibility(View.GONE);
		debugButton3.setVisibility(View.GONE);		
	}
	
	class PageMovedDelegate implements PageMovedListener {
		public void onPageMoved(PageInformation pi) {
			String msg = String.format("pn:%d/tn:%d ps:%f",pi.pageIndex,pi.numberOfPagesInChapter,pi.pagePositionInBook);
			Setting.debug(msg);			
	        if (fv.isMediaOverlayAvailable()) {
	            showMediaUI();
	            if (isAutoPlaying) {
	                fv.playFirstParallel();
	            }
	        }else {
	            hideMediaUI();
	        }
		}
		
		public void onChapterLoaded(int chapterIndex) {
			// do nothing in FixedLayout. 
		}
	}

}

class CacheDelegate implements CacheListener {
	public void onCachingStarted(int index) {
		Setting.debug("Caching task started.");
	}
	public void onCachingFinished(int index) {
		Setting.debug("Cacing task ended");
	}
	public void onCached(int index,String path) {
		Setting.debug("PageIndex "+index+" is cached to "+path);
	}
}



class ClickDelegate implements ClickListener {
	public void onClick(int x,int y) {
		Setting.debug("Click detected at "+x+":"+y);
	}
	public void onImageClicked(int x,int y,String src) {}
	public void onLinkClicked(int x,int y, String href) {}
}