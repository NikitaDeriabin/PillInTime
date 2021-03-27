package com.example.pillintime.ReminderAttrs;


public class DaysOfWeek {
    public boolean MONDAY, TUSEDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

    public DaysOfWeek(){
        MONDAY = false;
        TUSEDAY = false;
        WEDNESDAY = false;
        THURSDAY = false;
        FRIDAY = false;
        SATURDAY = false;
        SUNDAY = false;
    }

    public void setAll(DaysOfWeek daysOfWeek){
        this.MONDAY = daysOfWeek.MONDAY;
        this.TUSEDAY = daysOfWeek.TUSEDAY;
        this.WEDNESDAY = daysOfWeek.WEDNESDAY;
        this.THURSDAY = daysOfWeek.THURSDAY;
        this.FRIDAY = daysOfWeek.FRIDAY;
        this.SATURDAY = daysOfWeek.SATURDAY;
        this.SUNDAY = daysOfWeek.SUNDAY;
    }
}
