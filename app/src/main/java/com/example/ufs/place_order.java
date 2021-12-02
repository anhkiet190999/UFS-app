package com.example.ufs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class place_order extends AppCompatActivity implements order_item.orderItemListener{

    DatabaseReference mref;
    private ListView listdata;
    private TextView text, text1, text2, numItem_v, total_v;
    private Button viewBag;
    public int total_item = 0;
    public float total = 0;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Order");

    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<Food> menu = new ArrayList<>();
    public ArrayList<Order> bag = new ArrayList<>();

    public String restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        listdata = findViewById(R.id.listData);
        text = findViewById(R.id.text);
        viewBag = findViewById(R.id.bag);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        numItem_v = findViewById(R.id.numItem);
        total_v = findViewById(R.id.total);

        mref = FirebaseDatabase.getInstance().getReference("Store");

        ValueEventListener even = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                menu.clear();
                populateRestaurantNames(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mref.addListenerForSingleValueEvent(even);

        viewBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = mAuth.getCurrentUser().getUid().toString().trim();
                mDatabase.child(userId).child("bag").setValue(bag);
                mDatabase.child(userId).child("restaurant").setValue(restaurant);

                Intent bagIntent = new Intent(getApplicationContext(), bag.class );
                startActivity(bagIntent);
            }
        });
    }


    private void populateRestaurantNames(DataSnapshot snapshot) {
        Log.d("Store", "reading data");
        //ArrayList<String> names = new ArrayList<>();
        if (snapshot.exists()) {
            viewBag.setVisibility(View.GONE);
            text1.setVisibility(View.INVISIBLE);
            text2.setVisibility(View.INVISIBLE);
            numItem_v.setVisibility(View.INVISIBLE);
            total_v.setVisibility(View.INVISIBLE);
            names.clear();
            for (DataSnapshot ds : snapshot.getChildren()) {
                String name = ds.child("name").getValue(String.class);
                names.add(name);
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            listdata.setAdapter(adapter);

            listdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    restaurant = listdata.getItemAtPosition(i).toString().trim();
                    goToRestaurant(restaurant);
                }
            });

        } else {
            Log.d("Store", "no data found");
        }
    }

    private void goToRestaurant(String restaurant){
        Query query = mref.orderByChild("name").equalTo(restaurant);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    menu.clear();
                    for(DataSnapshot ds :snapshot.getChildren()){
                        for(DataSnapshot ds1 :ds.child("Menu").getChildren()){
                            String item = ds1.child("item").getValue(String.class);
                            String price = ds1.child("price").getValue(String.class);
                            Food food = new Food(item, price);
                            menu.add(food);
                        }
                    }
                    text.setText(restaurant);
                    FoodListAdapter adapter = new FoodListAdapter(place_order.this, R.layout.adapter_view_menu, menu);
                    listdata.setAdapter(adapter);
                    viewBag.setVisibility(View.VISIBLE);
                    text1.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.VISIBLE);
                    numItem_v.setVisibility(View.VISIBLE);
                    total_v.setVisibility(View.VISIBLE);

                    listdata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Food f = (Food) listdata.getItemAtPosition(i);
                            openOrderDialog(f.item, Float.parseFloat(f.price));
                        }
                    });
                }else{ //this is the case when search by food
                    Log.d("Store", "error getting data");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openOrderDialog(String foodName, float price){
        order_item orderItem = new order_item(foodName, price, bag);
        orderItem.show(getSupportFragmentManager(), "order item");
    }

    @Override
    public void applyOrder(String foodName, int quantity, float total_price){
        total_item += quantity;
        total += total_price;
        DecimalFormat df2 = new DecimalFormat("####.##");
        df2.format(total);
        numItem_v.setText(Integer.toString(total_item));
        total_v.setText(Float.toString(total).trim());

        if(quantity != 0){
            Order o = new Order(foodName, quantity, total_price);
            boolean contain = false;
            for(Order order : bag){
                if(order.getFoodName() == o.getFoodName()){
                    order.setQuantity(o.quantity);
                    contain = true;
                    break;
                }
            }
            if(!contain){
                bag.add(o);
            }
        }

    }
}