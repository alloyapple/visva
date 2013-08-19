package com.visva.android.bookreader.pdfreader;

import java.io.File;

public class Item {

	private int viewIcon;
	private File file;
    private String name, date, info;
    private int deleteIcon;
    
    public Item() {};
    
    public Item(int icon, String name, String date, String info, File file, int deleteIcon){
    	this.viewIcon = icon;
    	this.name = name;
    	this.date = date;
    	this.info = info;
    	this.file = file;
    	this.deleteIcon = deleteIcon;
    }
    
    public int getViewIcon(){
    	return viewIcon;
    }
    
    public int getDeleteIcon(){
    	return deleteIcon;
    }
    
    public String getName(){
    	return name;
    }
    
    public String getDate(){
    	return date;
    }
    public String getInfo(){
    	return info;
    }
    
    public File getFile(){
    	return file;
    }
    
}
