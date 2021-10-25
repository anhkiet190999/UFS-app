package com.example.ufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button register = findViewById(R.id.register);
        Button account = findViewById(R.id.account);

        register.setOnClickListener(view -> {
            /*Context context = getApplicationContext();
            CharSequence text = "Registering";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/
            Intent registerIntent = new Intent(getApplicationContext(), registration.class );
            startActivity(registerIntent);
        });

        account.setOnClickListener(view -> {
            Intent loginIntent = new Intent(getApplicationContext(), login.class );
            startActivity(loginIntent);
        });

    }
}