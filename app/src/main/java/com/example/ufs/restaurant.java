package com.example.ufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class restaurant extends AppCompatActivity {
    private Button logout;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Store");
    private ProgressBar progressBar;
    private ImageButton menu;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        progressBar = findViewById(R.id.progress_circular);
        logout = findViewById(R.id.logoutButon);
        menu = findViewById(R.id.menu);
        text = findViewById(R.id.name);

        mref = mref.child(mAuth.getCurrentUser().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue().toString().trim();
                    text.setText("Restaurant: " + name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "Error getting data");
            }
        };

        mref.addValueEventListener(eventListener);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(getApplicationContext(), changeMenu.class );
                startActivity(menuIntent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                //mAuth = FirebaseAuth.getInstance();
                Toast.makeText(restaurant.this, "Logging out!", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                progressBar.setVisibility(View.GONE);
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class );
                startActivity(mainIntent);
            }
        });
    }
}