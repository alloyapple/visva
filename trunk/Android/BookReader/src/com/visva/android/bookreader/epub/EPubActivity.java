package com.visva.android.bookreader.epub;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.skytree.epub.*;

public class EPubActivity extends Activity {
    /** Called when the activity is first created. */
	Button debugButton0;
	Button debugButton1;
	Button debugButton2;
	Button debugButton3;
	
	Button debugButton4;
	Button debugButton5;
	Button debugButton6;
    RelativeLayout ePubView; 
    int transitionType = 2;
    final private String TAG = "EPub"; 
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float density = metrics.density;
        
		ePubView = new RelativeLayout(this);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		ePubView.setLayoutParams(rlp);		
        
        RelativeLayout.LayoutParams debugButton0Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton0 = new Button(this);
        debugButton0.setText("Book");        
        debugButton0Param.leftMargin = 	(int)(10*density);
        debugButton0Param.topMargin = 	(int)(5*density);
        debugButton0Param.width = 		(int)(90*density);
        debugButton0Param.height = 		(int)(35*density);
        debugButton0.setLayoutParams(debugButton0Param);
        debugButton0.setId(8080);
        debugButton0.setOnClickListener(listener);
        debugButton0.setVisibility(1);        
        ePubView.addView(debugButton0);
        
        RelativeLayout.LayoutParams debugButton1Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton1 = new Button(this);
        debugButton1.setText("Magazine ");        
        debugButton1Param.leftMargin = 	(int)(100*density);
        debugButton1Param.topMargin = 	(int)(5*density);
        debugButton1Param.width = 		(int)(90*density);
        debugButton1Param.height = 		(int)(35*density);
        debugButton1.setLayoutParams(debugButton1Param);
        debugButton1.setId(8081);
        debugButton1.setOnClickListener(listener);
        ePubView.addView(debugButton1);
        
        RelativeLayout.LayoutParams debugButton2Param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton2 = new Button(this);
        debugButton2.setText("Install");        
        debugButton2Param.leftMargin = 	(int)(220*density);
        debugButton2Param.topMargin = 	(int)(5*density);
        debugButton2Param.width = 		(int)(90*density);
        debugButton2Param.height = 		(int)(35*density);
        debugButton2.setLayoutParams(debugButton2Param);
        debugButton2.setId(8082);
        debugButton2.setOnClickListener(listener);
        ePubView.addView(debugButton2);
        
        
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton3 = new Button(this);
        debugButton3.setText("None");        
        param.leftMargin = 	(int)(10*density);
        param.topMargin = 	(int)(150*density);
        param.width = 		(int)(90*density);
        param.height = 		(int)(35*density);
        debugButton3.setLayoutParams(param);
        debugButton3.setId(8090);
        debugButton3.setOnClickListener(listener);
        debugButton3.setVisibility(1);        
        ePubView.addView(debugButton3);
        
        param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton4 = new Button(this);
        debugButton4.setText("Slide");        
        param.leftMargin = 	(int)(10*density);
        param.topMargin = 	(int)(190*density);
        param.width = 		(int)(90*density);
        param.height = 		(int)(35*density);
        debugButton4.setLayoutParams(param);
        debugButton4.setId(8091);
        debugButton4.setOnClickListener(listener);
        debugButton4.setVisibility(1);        
        ePubView.addView(debugButton4);
        
        param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); // width,height
        debugButton5 = new Button(this);
        debugButton5.setText("Curl");        
        param.leftMargin = 	(int)(10*density);
        param.topMargin = 	(int)(230*density);
        param.width = 		(int)(90*density);
        param.height = 		(int)(35*density);
        debugButton5.setLayoutParams(param);
        debugButton5.setId(8092);
        debugButton5.setOnClickListener(listener);
        debugButton5.setVisibility(1);        
        ePubView.addView(debugButton5);        
        
        setContentView(ePubView);        
    }
	
	
	private void startBookView(String fileName) {
        String baseDirectory = getFilesDir() + "/books";
        ContentHandler contentListener = new ContentHandler();
        BookInformation bi = new BookInformation(fileName,baseDirectory,contentListener);		
		debug(bi.creator);
		Intent intent;
		if (!bi.isFixedLayout) {
			intent = new Intent(EPubActivity.this,BookViewActivity.class);
		}else {
			intent = new Intent(EPubActivity.this,MagazineActivity.class);
		}
		intent.putExtra("BOOKNAME",fileName);
		intent.putExtra("transitionType",transitionType);
		startActivity(intent);
	}
	
	private OnClickListener listener=new OnClickListener(){
		public void onClick(View arg) {			
	        if (arg.getId()==8080) {	        	
	        	startBookView("Mobydick.epub");
	        }else if (arg.getId()==8081) {
	        	startBookView("Doctor.epub");
	        }else if (arg.getId()==8082) {
	        	installSamples();	        	
	        }else if (arg.getId()==8090) {
	        	transitionType = 0;	        	
	        }else if (arg.getId()==8091) {
	        	transitionType = 1;	        	
	        }else if (arg.getId()==8092) {
	        	transitionType = 2;
	        }
	    }
	};
	
	private void installBook(String fileName) {
        if (this.fileExists(fileName)){
        	Log.d(TAG, fileName+ " already exist. try to delete old file.");
        	this.deleteFile(fileName);
        }
        this.copyToDevice(fileName);
        this.unzipBook(fileName);		
	}
	
	private void installSamples() {
//		if (this.isSampleInstalled()) return; 
		
        if (!this.makeDirectory("scripts")) {
        	debug("faild to make scripts directory");
        }
        
        if (!this.makeDirectory("images")) {
        	debug("faild to make images directory");
        }
        
        copyFileToFolder("PagesCenter.png","images");
        copyFileToFolder("PagesStack.png","images");
        copyFileToFolder("Pad-Landscape.png","images");
        copyFileToFolder("Pad-Portrait.png","images");
        copyFileToFolder("Phone-Landscape.png","images");
        copyFileToFolder("Phone-Portrait.png","images");

        
        if (!this.makeDirectory("downloads")) {
        	debug("faild to make downloads directory");
        }
//        
        if (!this.makeDirectory("books")) {
        	debug("faild to make books directory");
        }
        
        this.installBook("Mobydick.epub");
        this.installBook("Doctor.epub");
        
        SharedPreferences pref = getSharedPreferences("EPubTest",0);
        SharedPreferences.Editor edit = pref.edit();
        
        edit.putBoolean("isSamplesInstalled", true);
        
        edit.commit();
	}
	
	private boolean isSampleInstalled() {
        SharedPreferences pref = getSharedPreferences("EPubTest",0);        
        return pref.getBoolean("isSamplesInstalled",false);
	}

	
	private void showToast(String msg) {
		Toast toast = Toast.makeText( this,msg,Toast.LENGTH_SHORT);			
		toast.show();	
	}
	
	
	public void debug(String msg) {
//		if (Setting.isDebug()) {
			Log.d("EPub", msg);
//		}
	}
	
	public boolean  deleteFile(String fileName) {
		boolean res;
		File file = new File(getFilesDir() + "/downloads/"+fileName);
		res = file.delete();
		return res;		
	}
	
	public boolean fileExists(String fileName) {
		boolean res;
		File file = new File(getFilesDir() + "/downloads/"+fileName);
		debug(file.getAbsolutePath());
		
		if (file.exists()) res = true;
		else  res = false;
		return res;		
	}
	
	public boolean makeDirectory(String dirName) {
		boolean res;		
		String filePath = new String(getFilesDir().getAbsolutePath() + "/"+dirName);
		debug(filePath);
		File file = new File(filePath);
		if (!file.exists()) {
			res = file.mkdirs();
		}else {
			res = false;		
		}
		return res;	
	}
	
	public void copyToDevice(String fileName) {			      
		if (!this.fileExists(fileName)){
	          try
	          {
	        	  InputStream localInputStream = getAssets().open(fileName);	        	  
	        	  FileOutputStream localFileOutputStream = new FileOutputStream(getFilesDir().getAbsolutePath() + "/downloads/"+fileName);

	        	  byte[] arrayOfByte = new byte[1024];
	        	  int offset;
	        	  while ((offset = localInputStream.read(arrayOfByte))>0)
	        	  {
	        		  localFileOutputStream.write(arrayOfByte, 0, offset);	              
	        	  }
	        	  localFileOutputStream.close();
	        	  localInputStream.close();
	        	  Log.d(TAG, fileName+" copied to phone");	            
	          }
	          catch (IOException localIOException)
	          {
	              localIOException.printStackTrace();
	              Log.d(TAG, "failed to copy");
	              return;
	          }
	      }
	      else {
	          Log.d(TAG, fileName+" already exist");
	      }	         
	}
	
	public void copyFileToFolder(String fileName,String folder) {			      
//		if (!this.fileExists(fileName)){
	          try
	          {
	        	  InputStream localInputStream = getAssets().open(fileName);	        	  
	        	  FileOutputStream localFileOutputStream = new FileOutputStream(getFilesDir().getAbsolutePath() + "/"+folder+"/"+fileName);
	        	  Log.e("file local", "localFIle "+getFilesDir().getAbsolutePath() + "/"+folder+"/"+fileName);
	        	  byte[] arrayOfByte = new byte[1024];
	        	  int offset;
	        	  while ((offset = localInputStream.read(arrayOfByte))>0)
	        	  {
	        		  localFileOutputStream.write(arrayOfByte, 0, offset);	              
	        	  }
	        	  localFileOutputStream.close();
	        	  localInputStream.close();
	        	  Log.d(TAG, fileName+" copied to phone");	            
	          }
	          catch (IOException localIOException)
	          {
	              localIOException.printStackTrace();
	              Log.d(TAG, "failed to copy");
	              return;
	          }
//	      }
//	      else {
//	          Log.d(TAG, fileName+" already exist");
//	      }	         
	}

	
	public void copyToScripts(String fileName) {			      
//		if (!this.fileExists(fileName)){
	          try
	          {
	        	  InputStream localInputStream = getAssets().open(fileName);	        	  
	        	  FileOutputStream localFileOutputStream = new FileOutputStream(getFilesDir().getAbsolutePath() + "/scripts/"+fileName);

	        	  byte[] arrayOfByte = new byte[1024];
	        	  int offset;
	        	  while ((offset = localInputStream.read(arrayOfByte))>0)
	        	  {
	        		  localFileOutputStream.write(arrayOfByte, 0, offset);	              
	        	  }
	        	  localFileOutputStream.close();
	        	  localInputStream.close();
	        	  Log.d(TAG, fileName+" copied to phone");	            
	          }
	          catch (IOException localIOException)
	          {
	              localIOException.printStackTrace();
	              Log.d(TAG, "failed to copy");
	              return;
	          }
//	      }
//	      else {
//	          Log.d(TAG, fileName+" already exist");
//	      }	         
	}
	
	
	public String removeExtention(String filePath) {
	    // These first few lines the same as Justin's
	    File f = new File(filePath);

	    // if it's a directory, don't remove the extention
	    if (f.isDirectory()) return filePath;

	    String name = f.getName();

	    // Now we know it's a file - don't need to do any special hidden
	    // checking or contains() checking because of:
	    final int lastPeriodPos = name.lastIndexOf('.');
	    if (lastPeriodPos <= 0)
	    {
	        // No period after first character - return name as it was passed in
	        return filePath;
	    }
	    else
	    {
	        // Remove the last period and everything after it
	        File renamed = new File(f.getParent(), name.substring(0, lastPeriodPos));
	        return renamed.getPath();
	    }
	}
	
	public void unzipBook(String fileName) {
	    String targetDir = new String(getFilesDir().getAbsolutePath() + "/books/" + fileName);
		targetDir = this.removeExtention(targetDir);
		String filePath = new String(getFilesDir().getAbsolutePath() + "/downloads/");

	    Unzip unzip = new Unzip(fileName, filePath, targetDir);
	    unzip.unzip();
	}
	
}