package com.visva.android.socialstackwidget.object.facebookobject;
import com.google.gson.annotations.SerializedName;

public class FacebookPagingObject {
    @SerializedName("previous")
    public String previous;

    @SerializedName("next")
    private String next;

    public FacebookPagingObject(){
        
    }
    
    public FacebookPagingObject (String nextPage) {
        next = nextPage;
    }
    
    
    public String getUntil() {
        if (null != next) {
            if (next.contains("until")) {
                return next.substring(next.indexOf("until") + 6, next.length());
            }
        }
        return null;
    }

    public String getNextPage() {
        if (next == "")
            return "";
        else
            return next;
    }
    
    public void setNextPage (String next){
        this.next = next;
    }
}