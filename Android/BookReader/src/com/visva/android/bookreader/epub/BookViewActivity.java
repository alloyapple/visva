package com.visva.android.bookreader.epub;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.skytree.epub.ClickListener;
import com.skytree.epub.ContentListener;
import com.skytree.epub.Highlight;
import com.skytree.epub.HighlightListener;
import com.skytree.epub.Highlights;
import com.skytree.epub.MediaOverlayListener;
import com.skytree.epub.NavPoint;
import com.skytree.epub.NavPoints;
import com.skytree.epub.PageInformation;
import com.skytree.epub.PageMovedListener;
import com.skytree.epub.PageTransition;
import com.skytree.epub.PagingInformation;
import com.skytree.epub.PagingListener;
import com.skytree.epub.Parallel;
import com.skytree.epub.ReflowableControl;
import com.skytree.epub.SearchListener;
import com.skytree.epub.SearchResult;
import com.skytree.epub.SelectionListener;
import com.skytree.epub.Setting;
import com.skytree.epub.StateListener;
import com.skytree.epub.State;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import com.skytree.epub.Parallel;

class ContentHandler implements ContentListener {
	public long getLength(String baseDirectory,String contentPath) {
		String path = baseDirectory + "/" + contentPath;
		File file = new File(path);
		if (file.exists()) return file.length(); 
		else return 0;
	}
	
	public boolean isExists(String baseDirectory,String contentPath) {
		String path = baseDirectory + "/" + contentPath;
		File file = new File(path);
		if (file.exists()) return true;
		else return false;
	}
	
	public long getLastModified(String baseDirectory,String contentPath) {
		String path = baseDirectory + "/" + contentPath;
		File file = new File(path);
		if (file.exists()) return file.lastModified();
		else return 0;		
	}
	
	public InputStream getInputStream(String baseDirectory,String contentPath) {
		String path = baseDirectory + "/" + contentPath;
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			return fis;
		}catch(Exception e) {
			return null;
		}		
	}
}

public class BookViewActivity extends Activity {
	ReflowableControl rv;
	RelativeLayout ePubView;
	Button debugButton0;
	Button debugButton1;
	Button debugButton2;
	Button debugButton3;
	Button debugButton4;

	Button markButton;
	
	Parallel currentParallel;
	boolean autoStartPlayingWhenNewPagesLoaded = true;
	boolean autoMoveChapterWhenParallesFinished = true;
	boolean isAutoPlaying = true;

	final private String TAG = "EPub";
	Highlights highlights;
	ArrayList <PagingInformation> pagings = new ArrayList<PagingInformation>();
	int temp = 20;
	
	public void makeLayout() {		
		highlights = new Highlights();
		String fileName = new String();
		Bundle bundle = getIntent().getExtras();
		fileName = bundle.getString("BOOKNAME");
		

		ePubView = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		ePubView.setLayoutParams(rlp);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		rv = new ReflowableControl(this);
		Bitmap pagesStack = 	BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/images/PagesStack.png");
		Bitmap pagesCenter = 	BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/images/PagesCenter.png");		
		rv.setPagesStackImage(pagesStack);
		rv.setPagesCenterImage(pagesCenter);
		Bitmap backgroundForLandscape 	= 	BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/images/Pad-Landscape.png");
		Bitmap backgroundForPortrait 	= 	BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/images/Pad-Portrait.png");		
		rv.setBackgroundForLandscape(backgroundForLandscape, 	new Rect(56,12,2048-56,1536-12)); 	// Android Rect - left,top,right,bottom
		rv.setBackgroundForPortrait(backgroundForPortrait, 		new Rect(0,12,1024-56,1496-12)); 		// Android Rect - left,top,right,bottom
		rv.setBaseDirectory(getFilesDir() + "/books");
		rv.setBookName(fileName);
		rv.setDoublePagedForLandscape(true);
		rv.setFont("Serif", 22);
		rv.setLineSpacing(135); // the value is supposed to be percent(%).
		rv.setHorizontalGapRatio(0.30);
		rv.setVerticalGapRatio(0.1);
		rv.setHighlightListener(new HighlightDelegate());		
		rv.setPageMovedListener(new PageMovedDelegate());
		rv.setSelectionListener(new SelectionDelegate());
		rv.setPagingListener(new PagingDelegate());
		rv.setSearchListener(new SearchDelegate());
		rv.setStateListener(new StateDelegate());
		rv.setClickListener(new ClickDelegate());
		rv.setLayoutParams(params);		
		ContentHandler contentListener = new ContentHandler();
		rv.setContentListener(contentListener);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.width = LayoutParams.FILL_PARENT; // 400;
		params.height = LayoutParams.FILL_PARENT; // 600;
		rv.useDOMForHighlight(false);
		rv.setGlobalPaging(false);
		rv.setNavigationAreaWidthRatio(0.2f); // both left and right side.
//		rv.lockRotation();		
		rv.setMediaOverlayListener(new MediaOverlayDelegate());
		rv.setSequenceBasedForMediaOverlay(false);
		
		rv.setStartPositionInBook(0.034772f);
		
		// If you want to get the license key for commercial or non commercial use, please email us (skytree21@gmail.com). 
		// Without the proper license key, watermark message will be shown in background. 
		rv.setLicenseKey("0000-0000-0000-0000");	

		int transitionType = bundle.getInt("transitionType");
		if (transitionType==0) {
			rv.setPageTransition(PageTransition.None);
		}else if (transitionType==1) {
			rv.setPageTransition(PageTransition.Slide);
		}else if (transitionType==2) {
			rv.setPageTransition(PageTransition.Curl);
		}

//		rv.setCurlQuality(0.25f);
		
		ePubView.addView(rv);
	
		this.makeUI();
		this.hideMediaUI();

		setContentView(ePubView);
	}
	
	public void makeUI() {
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
		

		RelativeLayout.LayoutParams markButtonParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		markButton = new Button(this);
		markButton.setText("Highlight");
		markButtonParam.leftMargin = (int) (240 * density);
		markButtonParam.topMargin = (int) (5 * density);
		markButtonParam.width = (int) (70 * density);
		markButtonParam.height = (int) (35 * density);
		markButton.setLayoutParams(markButtonParam);
		markButton.setId(8089);
		markButton.setOnClickListener(listener);
		markButton.setVisibility(View.INVISIBLE);
		ePubView.addView(markButton);		
	}

	public void onCreate(Bundle savedInstanceState) {
		debug("onCreate");	
		super.onCreate(savedInstanceState);
		this.makeLayout();
	}

	private OnClickListener listener = new OnClickListener() {
		public void onClick(View arg) {
			if (arg.getId() == 8080) {
				playPrev();
			} else if (arg.getId() == 8081) {
				playAndPause();
			} else if (arg.getId() == 8082) {
				stopPlaying();		
			} else if (arg.getId() == 8083) {
				playNext();				
			} else if (arg.getId() == 8084) {
				finish();				
			} else if (arg.getId() == 8089) {
				hideButton();
				mark();
			}
		}
	};
	
	private void displayNavPoints() {
		NavPoints nps = rv.getNavPoints();
		for (int i=0; i<nps.getSize(); i++) {
			NavPoint np = nps.getNavPoint(i);
			debug(""+i+":"+np.text);
		}
		
		// modify one NavPoint object at will
		NavPoint onp = nps.getNavPoint(1);
		onp.text = "preface - it is modified";
		
		for (int i=0; i<nps.getSize(); i++) {
			NavPoint np = nps.getNavPoint(i);
			debug(""+i+":"+np.text+"   :"+np.sourcePath);
		}	
	}
	
	private void mark() {
		rv.markSelection(0x66FF0000,"");
		
	}

	private void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	class ClickDelegate implements ClickListener {
		public void onClick(int x,int y) {
			Log.w("EPub","Click Detected at "+x+":"+y);
		}
		
		public void onImageClicked(int x,int y,String src) {
			Log.w("EPub","Click on Image Detected at "+x+":"+y+" src:"+src);
		}
		
		public void onLinkClicked(int x,int y, String href) {
			Log.w("EPub","Click on Link Detected at "+x+":"+y+" href:"+href);
		}
	}
	
	ProgressDialog dialog;
	class StateDelegate implements StateListener {
		public void onStateChanged(State state) {
			if (state==State.LOADING) {
				showToast("Loading...");
//				Context context = getApplicationContext();				
//				if (context != null) {
//				    dialog = ProgressDialog.show(context, "","Loading", true);
//				}
//				ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "","Please wait for few seconds...", true);
			}else if (state==State.ROTATING) {
				showToast("Rotating...");
			}else if (state==State.BUSY) {
				showToast("Busy...");				
			}else if (state==State.NORMAL) {
				showToast("Normal...");
//				if (dialog!=null) dialog.dismiss();
//				dialog = null;
			}
		}		
	}

	class HighlightDelegate implements HighlightListener {
		public void onHighlightDeleted(Highlight highlight) {
			for (int index = 0; index < highlights.getSize(); index++) {
				Highlight temp = highlights.getHighlight(index);				
				if (temp.chapterIndex == highlight.chapterIndex
						&& temp.startIndex == highlight.startIndex
						&& temp.endIndex == highlight.endIndex
						&& temp.startOffset == highlight.startOffset
						&& temp.endOffset == highlight.endOffset) {
					highlights.removeHighlight(index);
				}
			}
		}

		public void onHighlightInserted(Highlight highlight) {
			highlights.addHighlight(highlight);
		}

		public void onHighlightHit(Highlight highlight, int x, int y) {
			debug(highlight.text);
		}

		public Highlights getHighlightsForChapter(int chapterIndex) {
			Highlights results = new Highlights();
			for (int index = 0; index < highlights.getSize(); index++) {
				Highlight highlight = highlights.getHighlight(index);
				if (highlight.chapterIndex == chapterIndex) {
					results.addHighlight(highlight);
				}
			}
			return results;
		}
	}
	
	class PagingDelegate implements PagingListener {
		public void onPagingStarted(int bookCode) {
			showToast("Global Pagination Started.");
		}

		public void onPaged(PagingInformation pagingInformation) {
			pagings.add(pagingInformation);
			showToast("Paging for "+pagingInformation.chapterIndex+" is Finished. The numberOfPages :"+pagingInformation.numberOfPagesInChapter);
		}

		public void onPagingFinished(int bookCode) {
			showToast("Global Pagination Finished.");
		}

		public int getNumberOfPagesForPagingInformation(PagingInformation pagingInformation) {
			for (int i=0; i<pagings.size(); i++) {
				PagingInformation pgi = pagings.get(i);
				if (pgi.equals(pagingInformation)) {
					Log.w("EPub","PagingInformation found !!");
					return pgi.numberOfPagesInChapter;
				}
			}
			return 0;
		}
	}

	class PageMovedDelegate implements PageMovedListener {
		public void onPageMoved(PageInformation pi) {
			String msg = String.format("chapterIndex:%d pageIndex:%d numberOfPages :%d positionInBook:%f    ",
					pi.chapterIndex,pi.pageIndex, pi.numberOfPagesInChapter,pi.pagePositionInBook);
			Log.w("EPub",msg);
		}
		
		public void onChapterLoaded(int chapterIndex) {
			if (rv.isMediaOverlayAvailable()) {
				showMediaUI();
		        if (autoStartPlayingWhenNewPagesLoaded) {
		            if (isAutoPlaying) {
		            	rv.playFirstParallelInPage();
		            	debugButton1.setText("Pause");
		            }
		        }
			}else {
				hideMediaUI();
			}			
		}
	}
	

	class SearchDelegate implements SearchListener {
		public void onKeySearched(SearchResult searchResult) {
			debug("chapterIndex"+searchResult.chapterIndex+" pageIndex:" + searchResult.pageIndex + " startOffset:"
					+ searchResult.startOffset + " tag:" + searchResult.nodeName
					+ " text:" + searchResult.text);
		}

		public void onSearchFinishedForChapter(SearchResult searchResult) {
			debug("Searching for Chapter:"+searchResult.chapterIndex+" is finished. ");
			rv.pauseSearch();
		}

		public void onSearchFinished(SearchResult searchResult) {
			debug("Searching is finished. ");
		}
	}
	
	private void moveButton(int x, int y) {
		RelativeLayout.LayoutParams markButtonParam = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
		markButtonParam.leftMargin = x;
		markButtonParam.topMargin = y;
		markButton.setLayoutParams(markButtonParam);
	}

	private void showButton() {
		markButton.setVisibility(View.VISIBLE);
	}

	private void hideButton() {
		markButton.setVisibility(View.INVISIBLE);
		markButton.setVisibility(View.GONE);
	}

	class SelectionDelegate implements SelectionListener {
		// startX, startY are the coordinate of start selection bar. endX,endY
		// are the coodinate for end selection bar. selectedText is the text
		// which user select now.
		public void selectionStarted(int startX, int startY, int endX,
				int endY, String selectedText) {
			Log.w("EPub", "selectionStarted");
			hideButton();
		}; // in case user touches down selection bar, normally hide custom
			// view.

		public void selectionChanged(int startX, int startY, int endX,
				int endY, String selectedText) {
			Log.w("EPub", "selectionChanged :"+selectedText);
			hideButton();
		}; // this may happen when user dragging selection.

		public void selectionEnded(int startX, int startY, int endX, int endY,
				String selectedText) {
			Log.w("EPub", "selectionEnded");
			if ((endY + 30 + markButton.getHeight()) < ePubView.getHeight())
				moveButton(endX, endY + 30);
			else
				moveButton(startX, startY - 30 - markButton.getHeight());
			showButton();
		}; // in case user touches up selection bar,custom menu view has to be
			// shown near endX,endY.

		public void selectionCancelled() {
			Log.w("EPub", "selectionCancelled");
			hideButton();
		} // selection cancelled by user.
	}

	public void debug(String msg) {
		if (Setting.isDebug()) {
			Log.d(Setting.getTag(), msg);
		}
	}
	
	class MediaOverlayDelegate implements MediaOverlayListener {
		@Override
		public void onParallelStarted(Parallel parallel) {
			// TODO Auto-generated method stub
//			Log.w("EPub","onParallelStarted");
			currentParallel = parallel;			
			if (rv.pageIndexInChapter()!=parallel.pageIndex) {
				if (autoMoveChapterWhenParallesFinished) rv.gotoPageInChapter(parallel.pageIndex);
			}
			rv.changeElementColor("#FFFF00",parallel.hash);
		}

		@Override
		public void onParallelEnded(Parallel parallel) {
			// TODO Auto-generated method stub
//			Log.w("EPub","onParallelEnded");
			rv.restoreElementColor();			
		}

		@Override
		public void onParallelsEnded() {
			// TODO Auto-generated method stub
		    rv.restoreElementColor();
		    if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = true;
		    if (autoMoveChapterWhenParallesFinished) {
		    	Log.w("EPub","onParallesEnded");
		    	rv.gotoNextChapter();
		    }
		}		
	}
	
	// MediaOverlay Utilites
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
	
	
	void playAndPause() {
		if (rv.isPlayingPaused()) {
			debugButton1.setText("Pause");
	        if (!rv.isPlayingStarted()) {
	            rv.playFirstParallelInPage();	        	
	            if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = true;
	        }else {
	            rv.resumePlayingParallel();	        	
	            if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = true;
	        }        
	    
	    }else {
	    	debugButton1.setText("Play");
	        rv.pausePlayingParallel();
	        if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = false;
	    }		
	}
	
	void stopPlaying() {
//	    [button1 setTitle:@"Play" forState:UIControlStateNormal];
	    rv.stopPlayingParallel();
	    rv.restoreElementColor();
	    if (autoStartPlayingWhenNewPagesLoaded) isAutoPlaying = false;
	}
	
	void playPrev() {
	    rv.playPrevParallel();
	}

	void playNext() {
	    rv.playNextParallel();
	}
}

