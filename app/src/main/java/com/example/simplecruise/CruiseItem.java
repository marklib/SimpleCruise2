package com.example.simplecruise;

public class CruiseItem {
    private String start;
    private String end;
    private String date;
    private int imageResource;
    private int people;

    public CruiseItem(){}

    CruiseItem(String start, String end, String date, int imageResource) {
        this.start = start;
        this.end = end;
        this.date = date;
        this.imageResource = imageResource;
        this.people = 1;
    }

    CruiseItem(String start, String end, String date, int imageResource, int people) {
        this.start = start;
        this.end = end;
        this.date = date;
        this.imageResource = imageResource;
        this.people = people;
    }

    public String getStart(){return this.start;}
    public String getEnd(){return this.end;}
    public String getDate(){return this.date;}
    public int getImageResource(){return this.imageResource;}
    public int getPeople(){return this.people;}
    public void setPeople(int people){
        this.people = people;
    }
}
