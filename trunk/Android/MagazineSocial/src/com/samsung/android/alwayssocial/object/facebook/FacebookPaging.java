package com.samsung.android.alwayssocial.object.facebook;
import com.google.gson.annotations.SerializedName;

public class FacebookPaging {
    @SerializedName("previous")
    public String previous;

    @SerializedName("next")
    private String next;

    public FacebookPaging(){
        
    }
    
    public FacebookPaging (String nextPage) {
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