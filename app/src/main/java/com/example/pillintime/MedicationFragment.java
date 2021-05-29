package com.example.pillintime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillintime.Adapter.MedicationAdapter;
import com.example.pillintime.Adapter.ReminderAdapter;
import com.example.pillintime.Managers.UserManager;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.Models.User;
import com.example.pillintime.ReminderAttrs.ReminderDate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MedicationFragment extends Fragment {

    MedicationAdapter reminderAdapter;
    RecyclerView recyclerView;
    UserManager userManager;
    List<Reminder> reminderList = new ArrayList<Reminder>();

    boolean refreshFlag = false;

    private FirebaseUser currUser;
    private DatabaseReference reference;
    private String userID;

    private MedicationAdapter.RecyclerViewClickListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_medication, container, false);
        recyclerView = view.findViewById(R.id.medication_recycler_view);

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = currUser.getUid();

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
                    System.out.println("///////////////////////////////////////////////////");

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
        setOnClickRecyclerItemListener();
        reminderAdapter = new MedicationAdapter(getActivity().getApplicationContext(), reminderList, listener);
        recyclerView.setAdapter(reminderAdapter);
    }

    private void setOnClickRecyclerItemListener(){
        listener = new MedicationAdapter.RecyclerViewClickListener(){
            @Override
            public void onClickReminder(View v, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), UpdateReminderActivity.class);

                intent.putExtra("id", String.valueOf(reminderList.get(position).getId()));
                intent.putExtra("img", String.valueOf(reminderList.get(position).getImg()));
                intent.putExtra("medicineTitle", String.valueOf(reminderList.get(position).getName()));
                intent.putExtra("note", String.valueOf(reminderList.get(position).getNote()));
                intent.putExtra("amount", String.valueOf(reminderList.get(position).getAmountDay()));
                intent.putExtra("startDate_day", String.valueOf(reminderList.get(position).getStartReminderDay().getDay()));
                intent.putExtra("startDate_month", String.valueOf(reminderList.get(position).getStartReminderDay().getMonth()));
                intent.putExtra("startDate_year", String.valueOf(reminderList.get(position).getStartReminderDay().getYear()));
                intent.putExtra("time_hours", String.valueOf(reminderList.get(position).getAlarmTimeList().get(0).getHours()));
                intent.putExtra("time_minutes", String.valueOf(reminderList.get(position).getAlarmTimeList().get(0).getMinutes()));

                int counter = 0;
                for (ReminderDate date: reminderList.get(position).getReminderDateList()) {
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
            public void onClickDeleteIcon(View v, int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete?");
                dialog.setMessage("Do you want to remove this?");

                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogI, int which) { dialogI.dismiss(); }
                });

                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userManager = new UserManager();
                        userManager.deleteReminder(reminderList.get(position));
                        dialog.dismiss();
                        refreshFlag = true;
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        onResume();
                    }
                });

                dialog.show();
            }

            @Override
            public void onClickThumbnailIcon(View v, int position) {

                if(reminderList.get(position).getImg() != null &&
                reminderList.get(position).getImg().trim().length() != 0) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), ImageViewActivity.class);
                    intent.putExtra("imgStr", String.valueOf(reminderList.get(position).getImg()));

                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "This reminder hasn't image!", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if(refreshFlag) {
            getFragmentManager().beginTransaction().detach(MedicationFragment.this).attach(MedicationFragment.this).commit();
            refreshFlag = false;
        }
    }
}
