package com.example.simplecruise;

public class BookingItem {

    private String cruiseStart;
    private String userEmail;
    private int people;

    public BookingItem(){}

    public BookingItem(String cruiseStart, String userEmail, int people){
        this.cruiseStart = cruiseStart;
        this.userEmail = userEmail;
        this.people = people;
    }

    public String getCruiseStart() {
        return cruiseStart;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
