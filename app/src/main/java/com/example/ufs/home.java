package com.example.ufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class home extends AppCompatActivity {
    private Button logout;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.progress_circular);
        logout = findViewById(R.id.logoutButon);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                mAuth = FirebaseAuth.getInstance();
                Toast.makeText(home.this, "Logging out!", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                progressBar.setVisibility(View.GONE);
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class );
                startActivity(mainIntent);
            }
        });
    }
}