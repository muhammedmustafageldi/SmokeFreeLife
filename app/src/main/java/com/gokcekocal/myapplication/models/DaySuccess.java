package com.gokcekocal.myapplication.models;

public class DaySuccess {

    private String title;
    private int imageGray;
    private int imageGold;
    private int dayNumber;

    public DaySuccess(String title, int imageGray, int imageGold, int dayNumber) {
        this.title = title;
        this.imageGray = imageGray;
        this.imageGold = imageGold;
        this.dayNumber = dayNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageGray() {
        return imageGray;
    }

    public void setImageGray(int imageGray) {
        this.imageGray = imageGray;
    }

    public int getImageGold() {
        return imageGold;
    }

    public void setImageGold(int imageGold) {
        this.imageGold = imageGold;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }
}
