package com.example.simplecruise;

public class CruiseItem {
    private String start;
    private String end;
    private String date;
    private int imageResource;

    public CruiseItem(){}

    CruiseItem(String start, String end, String date, int imageResource) {
        this.start = start;
        this.end = end;
        this.date = date;
        this.imageResource = imageResource;
    }

    public String getStart(){return this.start;}
    public String getEnd(){return this.end;}
    public String getDate(){return this.date;}
    public int getImageResource(){return imageResource;}

}
