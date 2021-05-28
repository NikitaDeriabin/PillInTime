package com.example.pillintime.ReminderAttrs;


import androidx.annotation.Nullable;

public class ReminderDate {
    private int year, month, day;
    private boolean mIsTaken;

    //добавить конструктор для заполнения дати сегодняшним числом
    public ReminderDate(){
        this.year = 0;
        this.month = 0;
        this.day = 0;
        mIsTaken = false;
    }

    public ReminderDate(int year, int month, int day){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setAll(ReminderDate reminderDate){
        this.year = reminderDate.getYear();
        this.month = reminderDate.getMonth();
        this.day = reminderDate.getDay();
        this.mIsTaken = reminderDate.getIsTaken();
    }

    public String getDateStr(){
        String date = this.day + "/" + this.month + "/" + this.year;
        return date.toString();
    }

    public boolean getIsTaken() {
        return mIsTaken;
    }

    public void setIsTaken(boolean taken) {
        mIsTaken = taken;
    }
}
