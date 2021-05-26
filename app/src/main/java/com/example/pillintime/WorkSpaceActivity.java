package com.example.pillintime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillintime.Managers.UserManager;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.Models.User;
import com.example.pillintime.ReminderAttrs.AlarmTime;
import com.example.pillintime.ReminderAttrs.ReminderDate;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class  WorkSpaceActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    ReminderDate setCalendarDate = new ReminderDate();
    HomeFragment homeFragment = new HomeFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_space);

        //set calendar 
        Calendar calendar = Calendar.getInstance();
        setCalendarDate.setYear(calendar.get(Calendar.YEAR));
        setCalendarDate.setMonth(calendar.get(Calendar.MONTH) + 1);
        setCalendarDate.setDay(calendar.get(Calendar.DAY_OF_MONTH));



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // saving data when rotate phone
        if(savedInstanceState == null) {
            transferData(setCalendarDate, homeFragment);

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                   homeFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        //write userName and email in drawer_header
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        View v = navigationView.getHeaderView(0);
        final TextView userNameTextView = (TextView) v.findViewById(R.id.userNameHeaderField);
        final TextView userEmailTextView = (TextView) v.findViewById(R.id.userEmailHeaderField);

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String userName = userProfile.getUserName();
                    String email = userProfile.getEmail();
                    userNameTextView.setText(userName);
                    userEmailTextView.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WorkSpaceActivity.this, "Oops, smth wrong happened!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_home:
                transferData(setCalendarDate, homeFragment);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        homeFragment).commit();
                break;
            case R.id.nav_medication:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MedicationFragment()).commit();
                break;
            case R.id.nav_doctors:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DoctorsFragment()).commit();
                break;
            case R.id.nav_chart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChartFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(WorkSpaceActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.nav_about:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AboutFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_LONG).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // handler for back click. Closing drawer, not activity
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    //maybe delete
    public void addReminderBtnClick(View v){
        switch (v.getId()){
            case R.id.add_reminder_btn:
                Intent intent = new Intent();
                intent.setClassName("WorkSpaceActivity", "AddReminderActivity");
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_day_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.calendar){
            selectDate();
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectDate(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               // set_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                setCalendarDate.setYear(year);
                setCalendarDate.setMonth(month + 1);
                setCalendarDate.setDay(dayOfMonth);

                if(homeFragment != null){
                    try{
                    homeFragment.updateCalendarDate(setCalendarDate.getDay(), setCalendarDate.getMonth(),
                            setCalendarDate.getYear());}
                    catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }, setCalendarDate.getYear(), setCalendarDate.getMonth() - 1, setCalendarDate.getDay());
        datePickerDialog.show();
    }

    private void transferData(ReminderDate date, Fragment fragment){
        Bundle bundle = new Bundle();
        int day = date.getDay();
        int month = date.getMonth();
        int year = date.getYear();

        bundle.putInt("day", day);
        bundle.putInt("month", month);
        bundle.putInt("year", year);

        fragment.setArguments(bundle);
    }
}