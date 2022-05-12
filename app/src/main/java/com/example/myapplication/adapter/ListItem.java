package com.example.myapplication.adapter;

import java.io.Serializable;

public class ListItem implements Serializable {
    private String title;
    private String desc;
    private String uri = "empty";
    private int id = 0;

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getUri() {
        return uri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
