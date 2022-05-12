package com.example.myapplication.db;

import com.example.myapplication.adapter.ListItem;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItem> list);
}