package com.example.ufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class registration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editUsername, editPassword, editStudentid, editPhone, editEmail;
    private Switch switchisVendor;
    private boolean isVendor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        editStudentid = findViewById(R.id.studentId);
        editPhone = findViewById(R.id.phonenumber);
        editEmail = findViewById(R.id.email);
        switchisVendor = findViewById(R.id.isVendors);
        switchisVendor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
       });
    }

    private void registerUser() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String studentID = editStudentid.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        boolean isVendor = switchisVendor.isChecked();

    }
}