package com.samsung.android.alwayssocial.object.facebook;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class FacebookPhoto extends FacebookFeedWrapper{
    
    @SerializedName("height")
    private String height;
    @SerializedName("width")
    private String width;
    
    @SerializedName("images")
    private ArrayList<Image> image;
    
    public class Image{
        @SerializedName("height")
        private String height;
        @SerializedName("width")
        private String width;
        @SerializedName("source")
        private String source;
        public String getHeight() {
            return height;
        }
        public void setHeight(String height) {
            this.height = height;
        }
        public String getWidth() {
            return width;
        }
        public void setWidth(String width) {
            this.width = width;
        }
        public String getSource() {
            return source;
        }
        public void setSource(String source) {
            this.source = source;
        }
        
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public ArrayList<Image> getImage() {
        return image;
    }

    public void setImage(ArrayList<Image> image) {
        this.image = image;
    }     

}
