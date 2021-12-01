package com.example.ufs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class change_order extends AppCompatDialogFragment {
    private Button remove, modify;
    public Order order;
    public ArrayList<Order> bag = new ArrayList<>();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_order, null);

        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        remove = view.findViewById(R.id.remove);
        modify = view.findViewById(R.id.modify);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Order").child(mAuth.getCurrentUser().getUid());


                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            bag.clear();
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                Order o = ds.getValue(Order.class);
                                if(o.getFoodName() != order.getFoodName()){
                                    bag.add(o);
                                }
                            }
                            //String userId = mAuth.getCurrentUser().getUid().toString().trim();
                            mDatabase.setValue(bag);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("firebase", "Error getting data");
                    }
                };

                mDatabase.addValueEventListener(eventListener);

            }
        });
        return builder.create();
    }

    public change_order(Order order) {
       this.order = order;
    }
}
