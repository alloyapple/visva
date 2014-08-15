package com.samsung.android.alwayssocial.object.facebook;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.alwayssocial.object.ResponseBase;

public class FacebookUser extends ResponseBase{
    @SerializedName("picture")
    public Picture picture;
    
    @SerializedName("id")
    public String id;
    
    @SerializedName("name")
    public String name;
    
    public class Picture {
        
        @SerializedName("data")
        public Data data;
        
        public class Data
        {
            @SerializedName("is_silhouette")
            public boolean isSilhouette;
            
            @SerializedName("url")
            public String url;
        }
    }
}
