package com.example.pillintime.Managers;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.pillintime.AddReminderActivity;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.Models.User;
import com.example.pillintime.R;
import com.example.pillintime.WorkSpaceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class UserManager {

    private User user;
    private Reminder reminder;

    private FirebaseUser currUser;
    private DatabaseReference reference;
    private String userID;

    public UserManager() {
        user = new User();
        reminder = new Reminder();
    }

    //нерабочий метод, разобратся как вернуть данные из асинхронного onDataChange
    public User getUser(){
        initialize();

        final User userToReturn = new User();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                System.out.println("/////////////////////////////////////////////////////////////////");
                System.out.println(userToReturn.reminderList.size());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "called onCancelled in READING data", error.toException());
            }
        });

        System.out.println("/////////////////////////////////////////////////////////////////");
        System.out.println(userToReturn.reminderList.size());

        return userToReturn;
    }

    public void update(Reminder toUpdateReminder){
        reminder.setAll(toUpdateReminder);

        initialize();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    userProfile.reminderList.add(0, reminder);
                    snapshot.getRef().setValue(userProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "called onCancelled in UPDATING data", error.toException());
            }
        });

    }

    private void initialize(){
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = currUser.getUid();

    }
}
