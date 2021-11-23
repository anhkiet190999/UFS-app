package com.example.ufs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class bag extends AppCompatActivity {

    private ListView listData;
    private TextView total;
    private Button checkout;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;

    private ArrayList<Order> bag = new ArrayList<>();

    private ListView mListView;
    private float total_price = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);

        mListView = findViewById(R.id.listData);
        total = findViewById(R.id.total);

        mDatabase = FirebaseDatabase.getInstance().getReference("Order").child(mAuth.getCurrentUser().getUid());
        ValueEventListener eventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bag.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Order o = ds.getValue(Order.class);
                        bag.add(o);
                        total_price += o.getTotal_price();
                    }
                    DecimalFormat df2 = new DecimalFormat("####.##");
                    df2.format(total_price);
                    BagAdapter adapter = new BagAdapter(bag.this, R.layout.adapter_view_bag, bag);
                    mListView.setAdapter(adapter);

                    total.setText(Float.toString(total_price).trim());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener1);
    }
}