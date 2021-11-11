package com.example.ufs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class changeMenu extends AppCompatActivity implements add_item.addItemListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView name;
    private Button add, remove;
    public ArrayList<Food> menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_menu);
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.name);
        add = findViewById(R.id.add);
        remove = findViewById(R.id.remove);

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

            mDatabase = mDatabase.child("Menu");
            ValueEventListener eventListener1 = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //menu.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        String itemName = ds.getKey();
                        String price = ds.getValue(String.class);
                        Toast.makeText(changeMenu.this, itemName + price, Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            mDatabase.addListenerForSingleValueEvent(eventListener1);


        }else{
            Toast.makeText(changeMenu.this, "error to get vendor information", Toast.LENGTH_LONG).show();
            Intent restaurantIntent = new Intent(getApplicationContext(), restaurant.class );
            startActivity(restaurantIntent);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();


            }
        });

    }
    public void openDialog(){
        add_item addItem = new add_item();
        addItem.show(getSupportFragmentManager(), "new item");
    }

    @Override
    public void applyText(String itemName, String price){
        //Food f = new Food(itemName, price);

        FirebaseDatabase.getInstance().getReference("Store")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Menu")
                .child(itemName).setValue(price).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(changeMenu.this, "added", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(changeMenu.this, "fail to add", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}