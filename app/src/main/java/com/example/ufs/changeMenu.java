package com.example.ufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class changeMenu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_menu);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);

        if(mAuth.getCurrentUser() != null) {
            String vendorId = mAuth.getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Store").child(vendorId);

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {

                       Store store = dataSnapshot.getValue(Store.class);
                       name.setText(store.name);

                    }else{
                        //redirect to another activity!
                        Toast.makeText(changeMenu.this, "error", Toast.LENGTH_LONG).show();
                        Intent restaurantIntent = new Intent(getApplicationContext(), restaurant.class );
                        startActivity(restaurantIntent);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("firebase", "Error getting data");
                }
            };
            mDatabase.addValueEventListener(eventListener);
        }



    }
}