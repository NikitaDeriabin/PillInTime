package com.example.pillintime;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {

    private FloatingActionButton mAddReminderBtn;
    ReminderAdapter reminderAdapter;
    RecyclerView recyclerView;
    UserManager userManager;
    List<Reminder> reminderList = new ArrayList<Reminder>();

    private FirebaseUser currUser;
    private DatabaseReference reference;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mAddReminderBtn = (FloatingActionButton) view.findViewById(R.id.add_reminder_btn);

        recyclerView = view.findViewById(R.id.home_recycler_view);

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = currUser.getUid();

        mAddReminderBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddReminderActivity.class);
                startActivity(intent);
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
                    System.out.println(reminderList.size());

                    setAdapter(reminderList);
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
       reminderAdapter = new ReminderAdapter(getActivity().getApplicationContext(), reminderList);
       recyclerView.setAdapter(reminderAdapter);
    }

    //вызывается когда фрагмент становится виден пользователю
    @Override
    public void onResume() {
        super.onResume();
        // добавить userManager.read() и потом setAdapter потестить как будет работать
    }



}
