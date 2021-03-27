package com.example.pillintime.ReminderAttrs;

public class AlarmTime {
    private int hours, minutes;
    private boolean taken = false, missed = false;

    //добавить конструктор для заполнения данных текущим временем
    public AlarmTime(){
        this.hours = 0;
        this.minutes = 0;
    }

    public AlarmTime(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public boolean getTaken() {return taken; }

    public void setTaken(boolean taken) {this.taken = taken; }

    public boolean getMissed() {return missed; }

    public void setMissed(boolean missed) {this.missed = missed; }

    public String getAlarmTimeStr(){
        String time = FormatTime(this.hours, this.minutes);
        return time.toString();
    }

    private String FormatTime(int hour, int minute) {
        String time;
        time = "";
        String formMinute;

        if (minute / 10 == 0) {
            formMinute = "0" + minute;
        } else {
            formMinute = "" + minute;
        }

        if (hour == 0) {
            time = "12" + ":" + formMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formMinute + " AM";
        } else if (hour == 12) {
            time = hour + ":" + formMinute + " PM";
        } else {
            int tmp = hour - 12;
            time = tmp + ":" + formMinute + " PM";
        }

        return time;
    }
}

