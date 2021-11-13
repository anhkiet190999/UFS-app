package com.example.ufs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FoodListAdapter extends ArrayAdapter<Food> {
    private static final String TAG = "FoodListAdapter";
    private Context mContext;
    int mResource;

    public FoodListAdapter(Context context, int resource, ArrayList<Food> foods){
        super(context, resource, foods);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String item = getItem(position).getItem();
        String price = getItem(position).getPrice();

        Food food = new Food(item, price);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvItem = (TextView) convertView.findViewById(R.id.item);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.price);

        tvItem.setText(item);
        tvPrice.setText(price);
        return convertView;
    }
}
