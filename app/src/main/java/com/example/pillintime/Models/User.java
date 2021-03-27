package com.example.pillintime.Models;

import com.example.pillintime.ReminderAttrs.AlarmTime;
import com.example.pillintime.ReminderAttrs.ReminderDate;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName, email, password;

    public List<Reminder> reminderList;

    public User() {
        this.reminderList = new ArrayList<Reminder>();
        //testSetReminder();
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.reminderList = new ArrayList<Reminder>();
    }

    //delete this later
    private void testSetReminder(){
        Reminder reminder = new Reminder();
        reminder.setName("Test1");
        reminder.getDaysOfWeek().MONDAY = true;
        reminder.getDaysOfWeek().FRIDAY = true;

        ReminderDate remDate = new ReminderDate();
        remDate.setDay(10);
        remDate.setMonth(12);
        remDate.setYear(2012);

        reminder.setStartReminderDay(remDate);
        reminder.setAmountDay(11);

        ArrayList<AlarmTime> alTime = new ArrayList<AlarmTime>();
        alTime.add(new AlarmTime(2, 3));
        alTime.add(new AlarmTime(12, 3));
        alTime.add(new AlarmTime(14, 3));

        reminder.setAlarmTimeList(alTime);

        reminderList.add(reminder);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
