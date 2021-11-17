package com.example.ufs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;

public class search extends AppCompatActivity {

    DatabaseReference mref;
    private ListView listdata;
    private AutoCompleteTextView txtSearch;
    private TextView textName;
    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<Food> menu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mref = FirebaseDatabase.getInstance().getReference("Store");
        listdata = (ListView) findViewById(R.id.listData);
        txtSearch = (AutoCompleteTextView) findViewById(R.id.txtSearch);
        textName = (TextView) findViewById(R.id.name);

        ValueEventListener even = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearchNames(snapshot);
                populateSearchFoods(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mref.addListenerForSingleValueEvent(even);
    }

    private void populateSearchNames(DataSnapshot snapshot){
        Log.d("Store", "reading data");
        //ArrayList<String> names = new ArrayList<>();
        if(snapshot.exists()){
            for(DataSnapshot ds :snapshot.getChildren()){
                String name = ds.child("name").getValue(String.class);
                names.add(name);
            }
            //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            //txtSearch.setAdapter(adapter);

        }else{
            Log.d("Store", "no data found");
        }
    }

    private void populateSearchFoods(DataSnapshot snapshot){
        Log.d("Menu", "reading data");
        //ArrayList<String> foods = new ArrayList<>();
        if(snapshot.exists()){
            for(DataSnapshot ds :snapshot.getChildren()){
               for(DataSnapshot ds1 :ds.child("Menu").getChildren()){
                   String food = ds1.child("item").getValue(String.class);
                   names.add(food);
               }
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            txtSearch.setAdapter(adapter);

            txtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    menu.clear();
                    String search = txtSearch.getText().toString().trim();
                    searchFood(search);
                }
            });

        }else{
            Log.d("Menu", "no data found");
        }
    }

    private void searchFood(String search){
        Query query = mref.orderByChild("name").equalTo(search);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot ds :snapshot.getChildren()){
                        for(DataSnapshot ds1 :ds.child("Menu").getChildren()){
                            String item = ds1.child("item").getValue(String.class);
                            String price = ds1.child("price").getValue(String.class);
                            Food food = new Food(item, price);
                            menu.add(food);

                        }
                    }
                    textName.setText(search);
                    FoodListAdapter adapter = new FoodListAdapter(search.this, R.layout.adapter_view_menu, menu);
                    listdata.setAdapter(adapter);
                }else{ //this is the case when search by food
                   // Toast.makeText(search.this, "search by food", Toast.LENGTH_LONG).show();
                    SearchByFoodName(search);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
    private void SearchByFoodName(String search){
        ValueEventListener even = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String restaurantName = null;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot ds1 : ds.child("Menu").getChildren()) {
                            String food = ds1.child("item").getValue(String.class);
                            if(food.equals(search)){
                                restaurantName = ds.child("name").getValue(String.class);
                                break;
                            }
                        }
                    }
                    searchFood(restaurantName);
                }   
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        mref.addListenerForSingleValueEvent(even);
        
    }
}






























