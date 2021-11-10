package com.example.ufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            /*Toast.makeText(MainActivity.this, "Nice to see you again!", Toast.LENGTH_LONG).show();
            Intent homeIntent = new Intent(getApplicationContext(), home.class );
            startActivity(homeIntent);*/
            mAuth.signOut();
        }
        Button register = findViewById(R.id.register);
        Button account = findViewById(R.id.account);
        Button vendor = findViewById(R.id.vendor);

        register.setOnClickListener(view -> {
            Intent registerIntent = new Intent(getApplicationContext(), registration.class );
            startActivity(registerIntent);
        });

        account.setOnClickListener(view -> {
            Intent loginIntent = new Intent(getApplicationContext(), login.class );
            startActivity(loginIntent);
        });

        vendor.setOnClickListener(view -> {
            Intent vendorRegisterIntent = new Intent(getApplicationContext(), vendorRegister.class );
            startActivity(vendorRegisterIntent);
        });

    }
}