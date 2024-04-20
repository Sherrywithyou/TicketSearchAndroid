package com.example.wanlonglin.hw9;

public class ModeHolder {
    private int data;
    public int getData() {return data;}
    public void setData(int data) {this.data = data;}

    private static final ModeHolder holder = new ModeHolder();
    public static ModeHolder getInstance() {return holder;}
}