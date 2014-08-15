package com.samsung.android.alwayssocial.object.facebook;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.samsung.android.alwayssocial.object.facebook.FacebookFeedWrapper.User;

public class FacebookPage {
    @SerializedName("category")
    private String category;

    @SerializedName("name")
    private String name;

    @SerializedName("created_time")
    private String created_time;

    @SerializedName("id")
    private String id;
    
    @SerializedName("likes")
    private int likes;
    
    @SerializedName("description")
    private String description;
    
    //use for friendlists
    @SerializedName("list_type")
    private String list_type;
    
    @SerializedName("members")
    private Members members;
    
    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    public class Members{
        @SerializedName("data")
        private ArrayList<User> data;

        public ArrayList<User> getData() {
            return data;
        }

        public void setData(ArrayList<User> data) {
            this.data = data;
        }
        
    }

    @SerializedName("category_list")
    private ArrayList<CategoryList> category_list = new ArrayList<FacebookPage.CategoryList>();

    public String getCategory() {
        if(null == category)
            return "";
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        if(null == name)
            return "";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getDescription() {
        if(null == description)
            return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getList_type() {
        if(null == list_type)
            return "";
        return list_type;
    }

    public void setList_type(String list_type) {
        this.list_type = list_type;
    }

    public ArrayList<CategoryList> getCategory_list() {
        return category_list;
    }

    public void setCategory_list(ArrayList<CategoryList> category_list) {
        this.category_list = category_list;
    }

    public class CategoryList {

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
    }

}
