package com.bootcamp.amberved.googleimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by amber_ved on 2/14/15.
 */
public class GoogleImageResults {

    public GoogleImageResults (JSONObject jsonObject)
    {
        try {
            
            this.url = jsonObject.getString("url");
            this.tbUrl = jsonObject.getString("tbUrl");
            this.title = jsonObject.getString("title");
            this.height = jsonObject.getString("height");
            this.width = jsonObject.getString("width");
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
    }
    
    public static ArrayList<GoogleImageResults> fromJSONArray(JSONArray array)
    {
        ArrayList<GoogleImageResults> results = new ArrayList<GoogleImageResults>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new GoogleImageResults(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
    
    public String getUrl() {
        return url;
    }

    public String getTbUrl() {
        return tbUrl;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }

    public void setTbUrl(String tbUrl) {
        this.tbUrl = tbUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    private String url;
    private String tbUrl;
    private String title;
    private String width;
    private String height;

}
