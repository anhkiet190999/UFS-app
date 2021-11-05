package com.example.ufs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editUsername, editPassword;
    private Button login;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private int count;
    private boolean res;
    private TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            Toast.makeText(login.this, "You are already logged in!", Toast.LENGTH_LONG).show();

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

            Intent homeIntent = new Intent(getApplicationContext(), home.class );
            startActivity(homeIntent);
        }

        editUsername = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress_circular);
        forgot = findViewById(R.id.forgotPassword);
        count = 0;

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link to the user
                        String mail = resetMail.getText().toString().trim();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(login.this, "Reset Link Sent To Your Email", Toast.LENGTH_LONG).show();
                                count = 0;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error! Reset Link is Not Sent " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the dialog
                    }
                });
                passwordResetDialog.create().show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count>2){
                    //redirect to forgot password activity
                    reset_password();
                }
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                if(username.isEmpty()){
                    editUsername.setError("user name is required!");
                    editUsername.requestFocus();
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
                progressBar.setVisibility(View.VISIBLE);

                //authenticate the user
                mAuth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    String userId = mAuth.getCurrentUser().getUid();

                                    mDatabase = FirebaseDatabase.getInstance().getReference()
                                            .child("Vendor").child(userId);

                                    ValueEventListener eventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {
                                                Toast.makeText(login.this, "Vendor has been logged in successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                Intent restaurantIntent = new Intent(getApplicationContext(), restaurant.class );
                                                startActivity(restaurantIntent);
                                            }else{
                                                Toast.makeText(login.this, "User has been logged in successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                //redirect to another activity!
                                                Intent homeIntent = new Intent(getApplicationContext(), home.class );
                                                startActivity(homeIntent);
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        }
                                    };
                                    mDatabase.addListenerForSingleValueEvent(eventListener);

                                }else{
                                    count+= 1;
                                    Toast.makeText(getApplicationContext(), "Failed to log in! Try again! you have " + (3 - count) + " times left" ,Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

    }
    private void reset_password(){
        EditText resetMail = new EditText(login.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(login.getContext());
        passwordResetDialog.setTitle("Reset Password ?");
        passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //extract the email and send reset link to the user
                String mail = resetMail.getText().toString().trim();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(login.this, "Reset Link Sent To Your Email", Toast.LENGTH_LONG).show();
                        count = 0;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, "Error! Reset Link is Not Sent " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close the dialog
            }
        });
        passwordResetDialog.create().show();
    }

}