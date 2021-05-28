package com.example.pillintime.Models;

import com.example.pillintime.Managers.UserManager;
import com.example.pillintime.ReminderAttrs.AlarmTime;
import com.example.pillintime.ReminderAttrs.DaysOfWeek;
import com.example.pillintime.ReminderAttrs.ReminderDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Reminder {

    private int id;
    private String name;
    private String img;
    private String note;

    private ReminderDate startReminderDay;
    private ReminderDate finishReminderDay;

    private int amountDay;

    private DaysOfWeek daysOfWeek;
    private List<AlarmTime> alarmTimeList;
    private List<ReminderDate> mReminderDateList;

    public Reminder(){
        name = "";
        img = "";
        note = "";
        startReminderDay = new ReminderDate();
        finishReminderDay = new ReminderDate();
        amountDay = 0;
        daysOfWeek = new DaysOfWeek();
        alarmTimeList = new ArrayList<AlarmTime>();
        mReminderDateList = new ArrayList<ReminderDate>();
    }

    public void setAll(Reminder reminder){
        this.id = reminder.getId();
        this.name = reminder.getName();
        this.img = reminder.getImg();
        this.note = reminder.getNote();
        this.startReminderDay.setAll(reminder.startReminderDay);
        this.finishReminderDay.setAll(reminder.finishReminderDay);
        this.amountDay = reminder.getAmountDay();
        this.daysOfWeek.setAll(reminder.daysOfWeek);
        this.mReminderDateList = reminder.getReminderDateList();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ReminderDate> getReminderDateList() {
        return mReminderDateList;
    }

    public void setReminderDateList(List<ReminderDate> reminderDateList) {
        mReminderDateList.clear();
        mReminderDateList.addAll(reminderDateList);
    }

    public void updateReminderDateListByAmountOfDays(ReminderDate remDate, int amountOfDay){
        System.out.println("()()*()*()*()&)(*(*)(*)(*");
        System.out.println(mReminderDateList.size());

        List<ReminderDate> tempReminderDateList = new ArrayList<ReminderDate>();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, remDate.getYear());
        c.set(Calendar.MONTH, remDate.getMonth() - 1);
        c.set(Calendar.DAY_OF_MONTH, remDate.getDay());

        ReminderDate reminderDate = new ReminderDate();
        reminderDate.setAll(remDate);
        reminderDate.setIsTaken(false);

        for(int i = 0; i < amountOfDay; i++){
            ReminderDate temp = new ReminderDate();
            temp.setAll(reminderDate);

            tempReminderDateList.add(temp);

            c.add(Calendar.DATE, 1);

            reminderDate.setIsTaken(false);
            reminderDate.setYear(c.get(Calendar.YEAR));
            reminderDate.setMonth(c.get(Calendar.MONTH) + 1);
            reminderDate.setDay(c.get(Calendar.DAY_OF_MONTH));
        }

        for (ReminderDate date1: tempReminderDateList) {
            for (ReminderDate date2: mReminderDateList) {
                if(date1.getDay() == date2.getDay() && date1.getMonth() == date2.getMonth()
                && date1.getYear() == date2.getYear()){
                    date1.setIsTaken(date2.getIsTaken());
                    break;
                }
            }
        }

        mReminderDateList.clear();
        mReminderDateList.addAll(tempReminderDateList);
    }

    public void setReminderDateListByAmountOfDays(ReminderDate remDate, int amountOfDay){
        mReminderDateList.clear();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, remDate.getYear());
        c.set(Calendar.MONTH, remDate.getMonth() - 1);
        c.set(Calendar.DAY_OF_MONTH, remDate.getDay());

        ReminderDate reminderDate = new ReminderDate();
        reminderDate.setAll(remDate);
        reminderDate.setIsTaken(false);

        for(int i = 0; i < amountOfDay; i++){
            ReminderDate temp = new ReminderDate();
            temp.setAll(reminderDate);

            mReminderDateList.add(temp);

            c.add(Calendar.DATE, 1);

            reminderDate.setIsTaken(false);
            reminderDate.setYear(c.get(Calendar.YEAR));
            reminderDate.setMonth(c.get(Calendar.MONTH) + 1);
            reminderDate.setDay(c.get(Calendar.DAY_OF_MONTH));
        }
    }

    public boolean needToUpdateReminderDateList(ReminderDate chosenDate, boolean flag){
        for (ReminderDate date: mReminderDateList) {
            if(date.getDay() == chosenDate.getDay() && date.getMonth() == chosenDate.getMonth()
            && date.getYear() == chosenDate.getYear()){
                if(date.getIsTaken() != flag){
                    date.setIsTaken(flag);
                    return true;
                }
            }
        }
        return false;
    }
}
