package com.example.ufs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class vendorRegister extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editName, editEmail, editPassword, editLicence, editPhone;
    private Button create;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_register);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            Toast.makeText(vendorRegister.this, "You are already logged in!", Toast.LENGTH_LONG).show();
            Intent homeIntent = new Intent(getApplicationContext(), home.class );
            startActivity(homeIntent);
        }

        create = findViewById(R.id.create);
        editName = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        editLicence = findViewById(R.id.studentId);
        editPhone = findViewById(R.id.phonenumber);
        editEmail = findViewById(R.id.email);
        progressBar = findViewById(R.id.progress_circular);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerVendor();
            }
        });
    }

    private void registerVendor() {
        String name = editName.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String licence = editLicence.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if(name.isEmpty()){
            editName.setError("user name is required!");
            editName.requestFocus();
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
        if(licence.isEmpty()){
            editLicence.setError("Student Id is required!");
            editLicence.requestFocus();
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
                            Restaurant vendor = new Restaurant(name, email, password, licence, phone);

                            FirebaseDatabase.getInstance().getReference("Restaurant")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(vendor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(vendorRegister.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
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