package com.example.pillintime;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillintime.Adapter.ReminderAdapter;
import com.example.pillintime.Managers.UserManager;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.Models.User;
import com.example.pillintime.Notification.AlarmBroadcast;
import com.example.pillintime.ReminderAttrs.AlarmTime;
import com.example.pillintime.ReminderAttrs.ReminderDate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private FloatingActionButton mAddReminderBtn;
    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;
    UserManager userManager;
    ReminderDate chosenDate = new ReminderDate();
    List<Reminder> reminderList = new ArrayList<Reminder>();
    List<Reminder> reminderListInBound  = new ArrayList<Reminder>();

    private FirebaseUser currUser;
    private DatabaseReference reference;
    private String userID;

    int calendar_day;
    int calendar_month;
    int calendar_year;
    boolean refreshFlag = false;

    private ReminderAdapter.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAddReminderBtn = (FloatingActionButton) view.findViewById(R.id.add_reminder_btn);

        //getting calendar date from WorkSpaceActivity
        getCalendarDate();

        recyclerView = view.findViewById(R.id.home_recycler_view);

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = currUser.getUid();

        mAddReminderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddReminderActivity.class);
                startActivity(intent);
                refreshFlag = true;
            }
        });

        //reading data from current user
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user != null){
                    //удалить sout
                    System.out.println("///////////////////////////////////////////////////");
                    System.out.println(user.reminderList.size());


                    reminderList.clear();
                    reminderList.addAll(user.reminderList);
                    getBoundReminders(reminderList);
                    //System.out.println(reminderList.size());

                    //setAdapter(reminderList);
                    setAdapter(reminderListInBound);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "called onCancelled in READING data", error.toException());
            }
        });

        return view;
    }

    private void setAdapter(List<Reminder> reminderList){
       setOnClickRecyclerItemListener();
       reminderAdapter = new ReminderAdapter(getActivity().getApplicationContext(), reminderList, listener, chosenDate);
       recyclerView.setAdapter(reminderAdapter);
    }

    private void setOnClickRecyclerItemListener(){
        listener = new ReminderAdapter.RecyclerViewClickListener(){
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateReminderActivity.class);


                intent.putExtra("id", String.valueOf(reminderListInBound.get(position).getId()));
                intent.putExtra("img", String.valueOf(reminderListInBound.get(position).getImg()));
                intent.putExtra("medicineTitle", String.valueOf(reminderListInBound.get(position).getName()));
                intent.putExtra("note", String.valueOf(reminderListInBound.get(position).getNote()));
                intent.putExtra("amount", String.valueOf(reminderListInBound.get(position).getAmountDay()));
                intent.putExtra("startDate_day", String.valueOf(reminderListInBound.get(position).getStartReminderDay().getDay()));
                intent.putExtra("startDate_month", String.valueOf(reminderListInBound.get(position).getStartReminderDay().getMonth()));
                intent.putExtra("startDate_year", String.valueOf(reminderListInBound.get(position).getStartReminderDay().getYear()));
                intent.putExtra("time_hours", String.valueOf(reminderListInBound.get(position).getAlarmTimeList().get(0).getHours()));
                intent.putExtra("time_minutes", String.valueOf(reminderListInBound.get(position).getAlarmTimeList().get(0).getMinutes()));

                int counter = 0;
                for (ReminderDate date: reminderListInBound.get(position).getReminderDateList()) {
                    intent.putExtra("day" + String.valueOf(counter), String.valueOf(date.getDay()));
                    intent.putExtra("month" + String.valueOf(counter), String.valueOf(date.getMonth()));
                    intent.putExtra("year" + String.valueOf(counter), String.valueOf(date.getYear()));
                    if(date.getIsTaken() == true) {
                        intent.putExtra("isTaken" + String.valueOf(counter), String.valueOf(1));
                    } else{
                        intent.putExtra("isTaken" + String.valueOf(counter), String.valueOf(0));
                    }
                    counter++;
                }

                startActivity(intent);
                refreshFlag = true;
            }

            @Override
            public void onSkipBtnClick(View v, int position, RelativeLayout listingRowLayout) {
                listingRowLayout.setBackgroundColor(getResources().getColor(R.color.purple_200));
                boolean flag = reminderListInBound.get(position).needToUpdateReminderDateList(chosenDate, false);
                if(flag == true){
                    userManager = new UserManager();
                    userManager.updateReminder(reminderListInBound.get(position));
                }
            }

            @Override
            public void onTakeBtnClick(View v, int position, RelativeLayout listingRowLayout) {
                listingRowLayout.setBackgroundColor(getResources().getColor(R.color.ripple_effect));
                boolean flag = reminderListInBound.get(position).needToUpdateReminderDateList(chosenDate, true);
                if(flag == true){
                    userManager = new UserManager();
                    userManager.updateReminder(reminderListInBound.get(position));
                }
            }
        };
    }

    //вызывается когда фрагмент становится виден пользователю
    @Override
    public void onResume() {
        super.onResume();
        if(refreshFlag) {
            getFragmentManager().beginTransaction().detach(HomeFragment.this).attach(HomeFragment.this).commit();
            refreshFlag = false;
        }
    }
    public void updateCalendarDate(int day, int month, int year){
        chosenDate.setDay(day);
        chosenDate.setMonth(month);
        chosenDate.setYear(year);
        //System.out.println(calendar_day + " " + calendar_month + " " + calendar_month + "\n\n\n");

        getBoundReminders(reminderList);
        setAdapter(reminderListInBound);
    }


    private void getCalendarDate(){
        try {
            if(chosenDate.getDay() == 0){
                Bundle bundle = this.getArguments();
                chosenDate.setDay(bundle.getInt("day"));
                chosenDate.setMonth(bundle.getInt("month"));
                chosenDate.setYear(bundle.getInt("year"));
            }
        }
        catch (Exception ex){
            System.out.println("ERROR");
        }
    }

    private void getBoundReminders(List<Reminder> reminderList){
        reminderListInBound.clear();
        for (Reminder reminder :reminderList) {
            try {
                ReminderDate startDate = reminder.getStartReminderDay();
                int amountDay = reminder.getAmountDay();

                Calendar start = Calendar.getInstance();
                start.clear();
                start.set(startDate.getYear(), startDate.getMonth() - 1, startDate.getDay());
                Calendar finish = (Calendar) start.clone();
                finish.add(Calendar.DATE, amountDay);

                Calendar chosen = Calendar.getInstance();
                chosen.clear();
                chosen.set(chosenDate.getYear(), chosenDate.getMonth() - 1, chosenDate.getDay());

                if ((chosen.after(start) && chosen.before(finish)) ||
                        (chosen.equals(start) || chosen.equals(finish))) {
                    reminderListInBound.add(reminder);
                }
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }
}
