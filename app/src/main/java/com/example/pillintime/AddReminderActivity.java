package com.example.pillintime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pillintime.Adapter.ReminderAdapter;
import com.example.pillintime.Managers.UserManager;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.Notification.AlarmBroadcast;
import com.example.pillintime.Notification.AlertReceiver;
import com.example.pillintime.ReminderAttrs.AlarmTime;
import com.example.pillintime.ReminderAttrs.ReminderDate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddReminderActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_IMAGE_REQUEST = 1;

    TextView btn_date, btn_time, btn_numPicker;
    TextView set_time, set_date;
    EditText medicineTitle;
    EditText medicineNote;
    Button btn_sbmt;
    Button btn_cancel;
    String timeToNotify;

    UserManager userManager;
    ReminderDate startReminderDate = new ReminderDate();
    AlarmTime alarmTime = new AlarmTime();
    Reminder reminder = new Reminder();
    NumberPicker numberPicker;
    ImageView reminderImageView;
    Uri imageUri;


    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);


        btn_date = findViewById(R.id.date_text);
        btn_time = findViewById(R.id.time_text);
        btn_numPicker = findViewById(R.id.period_days_text);
        medicineTitle = findViewById(R.id.reminder_title);
        medicineNote = findViewById(R.id.reminder_note);
        btn_sbmt = findViewById(R.id.add_reminder_submit);
        btn_cancel = findViewById(R.id.add_reminder_cancel);
        set_date = findViewById(R.id.set_date);
        set_time = findViewById(R.id.set_time);
        numberPicker = findViewById(R.id.set_period_days);
        reminderImageView = findViewById(R.id.reminder_image);




        btn_date.setOnClickListener(this);
        btn_time.setOnClickListener(this);
        btn_sbmt.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_numPicker.setOnClickListener(this);
        numberPicker.setOnClickListener(this);
        reminderImageView.setOnClickListener(this);


        recyclerView =  findViewById(R.id.home_recycler_view);


    }


    @Override
    public void onClick(View view) {
        if(view == btn_date){
            selectDate();
        }
        else if(view == btn_time){
            selectTime();

        }
        else if(view == numberPicker){
            selectPeriodOfDays();
        }
        else if(view == reminderImageView){
            openFileChooser();
        }
        else if(view == btn_sbmt){
            submit();
        }
        else if(view == btn_cancel){
            cancel();
        }

    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //получить результат после выбора изображения в галерии
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(reminderImageView);

            //задать изображение, реализация без Picasso
            //reminderImageView.setImageURI(imageUri);
        }

    }

    private void selectPeriodOfDays(){
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(365);


        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                reminder.setAmountDay(newVal);
            }
        });
    }

    private void cancel(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Quit without saving?");
        dialog.setMessage("Do you want to quit without saving?");

        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogI, int which) { dialogI.dismiss(); }
        });

        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackPressed();
            }
        });


        dialog.show();

    }

    private void submit(){
        String reminderTitleStr = medicineTitle.getText().toString().trim();
        if(reminderTitleStr.isEmpty()){
            Toast.makeText(this, "Please enter medicine title", Toast.LENGTH_LONG).show();
        } else {
            if(set_date.getText().toString().equals("") || set_time.getText().toString().equals("")){
                Toast.makeText(this, "Please select date and time for reminder", Toast.LENGTH_LONG).show();
            } else if(reminder.getAmountDay() <= 0){
                Toast.makeText(this, "Please choose the period of days", Toast.LENGTH_LONG).show();
            } else {
                userManager = new UserManager();

                String reminderTitle = medicineTitle.getText().toString().trim();
                String reminderNoteStr = medicineNote.getText().toString().trim();
                String date = set_date.getText().toString().trim();
                String time = set_time.getText().toString().trim();


                //set parameters
                reminder.setName(reminderTitle);
                reminder.setNote(reminderNoteStr);
                reminder.setStartReminderDay(startReminderDate);

                //возможно недобавится нужно проверить
                reminder.getAlarmTimeList().add(alarmTime);

                reminder.setReminderDateListByAmountOfDays(startReminderDate, reminder.getAmountDay());

                //updating data for user
                //upload image to firebase and set reminder.img
                userManager.uploadFileAndUpdate(imageUri, reminder, getContentResolver());

                //setAlarm(reminderTitle, date, time);

                setDateAndTimeForNotification(reminder);
                Toast.makeText(this, "The reminder was added!", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void selectDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                set_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                startReminderDate.setYear(year);
                startReminderDate.setMonth(month + 1);
                startReminderDate.setDay(dayOfMonth);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void selectTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeToNotify = hourOfDay + ":" + minute;
                set_time.setText(FormatTime(hourOfDay, minute));

                alarmTime.setHours(hourOfDay);
                alarmTime.setMinutes(minute);
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }



    public String FormatTime(int hour, int minute){
        String time;
        time = "";
        String formMinute;

        if (minute / 10 == 0){
            formMinute = "0" + minute;
        } else {
            formMinute = "" + minute;
        }

        if(hour == 0){
            time = "12" + ":" + formMinute + " AM";
        }else if (hour < 12){
            time = hour + ":" + formMinute + " AM";
        }else if (hour == 12){
            time = hour + ":" + formMinute + " PM";
        }else {
            int tmp = hour - 12;
            time = tmp + ":" + formMinute + " PM";
        }

        return time;
    }

    public void setDateAndTimeForNotification(Reminder reminder){

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, reminder.getStartReminderDay().getYear());
        c.set(Calendar.MONTH, reminder.getStartReminderDay().getMonth() - 1);
        c.set(Calendar.DAY_OF_MONTH, reminder.getStartReminderDay().getDay());
        c.set(Calendar.HOUR_OF_DAY, reminder.getAlarmTimeList().get(0).getHours());
        c.set(Calendar.MINUTE, reminder.getAlarmTimeList().get(0).getMinutes());
        c.set(Calendar.SECOND, 0);

        for(int i = 0; i < reminder.getAmountDay(); i++){
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            AlertReceiver.reminder = reminder;
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, reminder.getId() * 100 + i, intent, 0);

            if(c.after(Calendar.getInstance())){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            }

            c.add(Calendar.DATE, 1);

        }

    }

    private void setAlarm(String text, String date, String time){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("reminderTitle", text);
        //intent.putExtra("time", date);
        //intent.putExtra("date", time);

        intent.putExtra("time", time);
        intent.putExtra("date", date);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String dateAndTime = date + " " + timeToNotify;
        //DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        DateFormat formatter = new SimpleDateFormat("d/M/yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateAndTime);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}