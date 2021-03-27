package com.example.pillintime.ReminderAttrs;


public class ReminderDate {
    private int year, month, day;

    //добавить конструктор для заполнения дати сегодняшним числом
    public ReminderDate(){
        this.year = 0;
        this.month = 0;
        this.day = 0;
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
    }

    public String getDateStr(){
        String date = this.day + "/" + this.month + "/" + this.year;
        return date.toString();
    }
}
