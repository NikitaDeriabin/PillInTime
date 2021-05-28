package com.example.pillintime.Managers;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillintime.Adapter.ReminderAdapter;
import com.example.pillintime.AddReminderActivity;
import com.example.pillintime.Models.Reminder;
import com.example.pillintime.Models.User;
import com.example.pillintime.R;
import com.example.pillintime.WorkSpaceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.content.ContentValues.TAG;

public class UserManager {

    private User user;
    private Reminder reminder;

    private FirebaseUser currUser;
    private DatabaseReference reference;
    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private String userID;

    Uri imageUri;



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

    public String getFileExtension(Uri uri, ContentResolver cR){
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFileAndUpdateReminder(Uri imageUri, Reminder reminderToUpload, ContentResolver cR){
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        this.imageUri = imageUri;

        if(this.imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(this.imageUri, cR));

            fileReference.putFile(this.imageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(taskSnapshot.getMetadata() != null){
                                if(taskSnapshot.getMetadata().getReference() != null){
                                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // reminderToUpload.setImg(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                            reminderToUpload.setImg(uri.toString());
                                            updateReminder(reminderToUpload);
                                        }
                                    });
                                }
                            }
                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            updateReminder(reminderToUpload);
                        }
                    });
        }
        else{
            updateReminder(reminderToUpload);
        }
    }

    public void uploadFileAndUpdate(Uri imageUri, Reminder reminderToUpload, ContentResolver cR){
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        this.imageUri = imageUri;

        if(this.imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(this.imageUri, cR));

            fileReference.putFile(this.imageUri).
                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if(taskSnapshot.getMetadata() != null){
                            if(taskSnapshot.getMetadata().getReference() != null){
                                Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // reminderToUpload.setImg(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                                        reminderToUpload.setImg(uri.toString());
                                        update(reminderToUpload);
                                    }
                                });
                            }
                        }
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        update(reminderToUpload);
                    }
                });
        }
        else{
            update(reminderToUpload);
        }
    }

    public void updateReminder(Reminder toUpdateReminder){
        reminder.setAll(toUpdateReminder);

        initialize();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    for (Reminder rem: userProfile.reminderList) {
                        if(rem.getId() == reminder.getId()) {
                            rem.setAll(reminder);
                            System.out.println("********************************************************");
                            System.out.println("reminder : " + reminder.getId() + " rem : " + rem.getId());
                            break;
                        }
                    }
                    snapshot.getRef().setValue(userProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "called onCancelled in UPDATING data", error.toException());
            }
        });
    }

    //it is add to database
    public void update(Reminder toUpdateReminder){
        reminder.setAll(toUpdateReminder);

        initialize();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    if(userProfile.reminderList.size() != 0){
                        reminder.setId(userProfile.reminderList.get(0).getId() + 1);
                    } else {
                        reminder.setId(1);
                    }
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

    public void deleteReminder(Reminder toDeleteReminder){
        reminder.setAll(toDeleteReminder);

        initialize();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null) {
                    //userProfile.reminderList.remove(reminder);

                    for (Reminder rem : userProfile.reminderList) {
                        if (rem.getId() == reminder.getId()) {
                            userProfile.reminderList.remove(rem);
                            if(reminder.getImg() != null && reminder.getImg().trim().length() != 0){
                                StorageReference imageRef = mStorage.getReferenceFromUrl(reminder.getImg());
                                imageRef.delete();
                            }
                            break;
                        }
                    }
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
        mStorage = FirebaseStorage.getInstance();
    }
}
