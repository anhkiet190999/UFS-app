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

public class home extends AppCompatActivity {
    private Button logout, search;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;
    private DatabaseReference mref = FirebaseDatabase.getInstance().getReference("User");
    ImageButton order, bag;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressBar = findViewById(R.id.progress_circular);
        logout = findViewById(R.id.logoutButon);
        order = findViewById(R.id.order);
        bag = findViewById(R.id.bag);
        search = findViewById(R.id.search);
        text = findViewById(R.id.name);

        mref = mref.child(mAuth.getCurrentUser().getUid());

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String name = dataSnapshot.child("username").getValue().toString().trim();
                    text.setText("hello, " + name);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", "Error getting data");
            }
        };

        mref.addValueEventListener(eventListener);


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

        order.setOnClickListener(view -> {
            Intent placeOrderIntent = new Intent(getApplicationContext(), place_order.class );
            startActivity(placeOrderIntent);
        });

        bag.setOnClickListener(view -> {
            Intent bagIntent = new Intent(getApplicationContext(), bag.class );
            startActivity(bagIntent);
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(getApplicationContext(), search.class );
                startActivity(searchIntent);
            }
        });
    }
}