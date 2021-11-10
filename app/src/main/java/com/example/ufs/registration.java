package com.example.ufs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class registration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editUsername, editPassword, editStudentid, editPhone, editEmail;
    private Button create;
    //private Switch switchisVendor;
    private ProgressBar progressBar;
    //private boolean isVendor = false;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            Toast.makeText(registration.this, "You are already logged in!", Toast.LENGTH_LONG).show();
            String userId = mAuth.getCurrentUser().getUid();

            mDatabase = FirebaseDatabase.getInstance().getReference()
                    .child("Vendor").child(userId);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {

                        Intent restaurantIntent = new Intent(getApplicationContext(), restaurant.class );
                        startActivity(restaurantIntent);
                    }else{

                        //redirect to another activity!
                        Intent homeIntent = new Intent(getApplicationContext(), home.class );
                        startActivity(homeIntent);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("firebase", "Error getting data");
                }
            };
            mDatabase.addListenerForSingleValueEvent(eventListener);
        }
        create = findViewById(R.id.create);
        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        editStudentid = findViewById(R.id.studentId);
        editPhone = findViewById(R.id.phonenumber);
        editEmail = findViewById(R.id.email);
        //switchisVendor = findViewById(R.id.isVendors);
        progressBar = findViewById(R.id.progress_circular);
        /*switchisVendor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               if(b){
                   isVendor = true;
                   Context context = getApplicationContext();
                   CharSequence text = "is vendor";
                   int duration = Toast.LENGTH_SHORT;
                   Toast toast = Toast.makeText(context, text, duration);
                   toast.show();
               }else{
                   isVendor = false;
               }
           }
       });*/


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    private void registerUser() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String studentID = editStudentid.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if(username.isEmpty()){
            editUsername.setError("user name is required!");
            editUsername.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editEmail.setError("email is required!");
            editEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("please provide valid email!");
            editEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editPassword.setError("password is required!");
            editPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editPassword.setError("password has to to be at least 6 characters");
            editPassword.requestFocus();
            return;
        }
        if(studentID.isEmpty()){
            editStudentid.setError("Student Id is required!");
            editStudentid.requestFocus();
            return;
        }
        if(phone.isEmpty()){
            editPhone.setError("phone is required!");
            editPhone.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(username, email, password, studentID, phone);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(registration.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        mAuth.signOut();
                                        //redirect to login activity!
                                        Intent loginIntent = new Intent(getApplicationContext(), login.class );
                                        startActivity(loginIntent);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}