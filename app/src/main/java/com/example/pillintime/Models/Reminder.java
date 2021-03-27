package com.example.pillintime.Models;

import com.example.pillintime.ReminderAttrs.AlarmTime;
import com.example.pillintime.ReminderAttrs.DaysOfWeek;
import com.example.pillintime.ReminderAttrs.ReminderDate;

import java.util.ArrayList;
import java.util.List;

public class Reminder {

    private String name;
    private String img;
    private String note;

    private ReminderDate startReminderDay;
    private ReminderDate finishReminderDay;

    private int amountDay;

    private DaysOfWeek daysOfWeek;
    private List<AlarmTime> alarmTimeList;

    public Reminder(){
        name = "";
        img = "";
        note = "";
        startReminderDay = new ReminderDate();
        finishReminderDay = new ReminderDate();
        amountDay = 0;
        daysOfWeek = new DaysOfWeek();
        alarmTimeList = new ArrayList<AlarmTime>();
    }

    public void setAll(Reminder reminder){
        this.name = reminder.getName();
        this.img = reminder.getImg();
        this.note = reminder.getNote();
        this.startReminderDay.setAll(reminder.startReminderDay);
        this.finishReminderDay.setAll(reminder.finishReminderDay);
        this.amountDay = reminder.getAmountDay();
        this.daysOfWeek.setAll(reminder.daysOfWeek);
        setAlarmTimeList(reminder.alarmTimeList);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getAmountDay() {
        return amountDay;
    }

    public void setAmountDay(int amountDay) {
        this.amountDay = amountDay;
    }

    public DaysOfWeek getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(DaysOfWeek daysOfWeek) {
        this.daysOfWeek.setAll(daysOfWeek);
    }

    public ReminderDate getStartReminderDay() {
        return startReminderDay;
    }

    public void setStartReminderDay(ReminderDate startReminderDay) {
        this.startReminderDay.setAll(startReminderDay);
   }

    public ReminderDate getFinishReminderDay() {
        return finishReminderDay;
    }

    public void setFinishReminderDay(ReminderDate finishReminderDay) {
        this.finishReminderDay.setAll(finishReminderDay);
    }

    public List<AlarmTime> getAlarmTimeList() {
        return alarmTimeList;
    }

    public void setAlarmTimeList(List<AlarmTime> alarmTimeList) {
        this.alarmTimeList.clear();
        this.alarmTimeList.addAll(alarmTimeList);
    }

    public void addToAlarmTimeList(AlarmTime alarmTime){
        this.alarmTimeList.add(alarmTime);
    }
}
