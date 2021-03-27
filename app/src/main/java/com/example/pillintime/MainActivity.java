package com.example.pillintime;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.pillintime.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnSignUp;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference users;

    RelativeLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        root = findViewById(R.id.root_element);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        //delete this
        //startActivity(new Intent(MainActivity.this, WorkSpaceActivity.class));


        //event handler for buttons
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpWindow();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }
        });
    }

    //pop-up window for authentication
    private void showSignInWindow(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign In");
        dialog.setMessage("Enter authentication data");

        //get the template for the popup
        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);

        final MaterialEditText email = sign_in_window.findViewById(R.id.emailField);
        final MaterialEditText password = sign_in_window.findViewById(R.id.passwordField);

        //buttons for cancel sign up
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                dialogI.dismiss();
            }
        });

        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(TextUtils.isEmpty(email.getText().toString())){
                    //pop-up window
                    Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length() <= 4){
                    //pop-up window
                    Snackbar.make(root, "Enter your password. Length must be greater than 4",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                User user = new User();
                                user.setUserName(authResult.getAdditionalUserInfo().getUsername());
                                user.setEmail(authResult.getUser().getEmail());

                                Intent intent = new Intent(MainActivity.this, WorkSpaceActivity.class);



                                startActivity(new Intent(MainActivity.this, WorkSpaceActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(root, "Authentication error." + e.getMessage(),
                                        Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        });

        dialog.show();

    }

    //pop-up window for register
    private void showSignUpWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign Up");
        dialog.setMessage("Enter registration data");

        //get the template for the popup
        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_up_window = inflater.inflate(R.layout.sign_up_window, null);
        dialog.setView(sign_up_window);

        final MaterialEditText email = sign_up_window.findViewById(R.id.emailField);
        final MaterialEditText password = sign_up_window.findViewById(R.id.passwordField);
        final MaterialEditText userName = sign_up_window.findViewById(R.id.userNameField);

        //buttons for cancel sign up
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogI, int which) {
                dialogI.dismiss();
            }
        });

        //button for submit sign up
        dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                if(TextUtils.isEmpty(email.getText().toString())){
                    //pop-up window
                    Snackbar.make(root, "Enter your email", Snackbar.LENGTH_SHORT).show();
                    //return;
                }
                else if(TextUtils.isEmpty(userName.getText().toString())){
                    //pop-up window
                    Snackbar.make(root, "Enter your username", Snackbar.LENGTH_SHORT).show();
                    //return;
                }
                else if(password.getText().toString().length() < 5){
                    //pop-up window
                    Snackbar.make(root, "Enter your password. Length must be greater than 5",
                            Snackbar.LENGTH_SHORT).show();
                    //return;
                }

                //Sign up user

                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setUserName(userName.getText().toString());
                                user.setPassword(password.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "User was added!",
                                                        Snackbar.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(root, "Register error. " + e.getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });



            }
        });

        dialog.show();

    }
}