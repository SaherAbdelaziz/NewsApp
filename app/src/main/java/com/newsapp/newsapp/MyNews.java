package com.newsapp.newsapp;

/**
 * Created by SaherOs on 2/23/2018.
 */

public class MyNews {
    private String myTitle;
    private String myType;
    private String myDate;
    private String mySection;
    private String myUrl;
    private String[] firstName;

    public MyNews(String title, String type, String date, String section, String url, String[] author) {
        this.myTitle = title;
        this.myType = type;
        this.myDate = date;
        this.mySection = section;
        this.myUrl = url;
        this.firstName = author;

    }

    public String getMyTitle() {
        return myTitle;
    }

    public String getMyType() {
        return myType;
    }

    public String getMyDate() {
        return myDate;
    }

    public String getMySection() {
        return mySection;
    }

    public String getMyUrl() {
        return myUrl;
    }

    public String getFirstName() {
        String s = "";
        for (int i = 0; i < firstName.length; i++) {
            if (i == firstName.length - 1)
                s += firstName[i];
            else
                s += firstName[i] + ", ";
        }
        return s;
    }
}
