package com.example.ufs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BagAdapter extends ArrayAdapter<Order> {
    private static final String TAG = "BagAdapter";
    private Context mContext;
    int mResource;

    public BagAdapter(Context context, int resource, ArrayList<Order> bag){
        super(context, resource, bag);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String foodName = getItem(position).getFoodName();
        int quantity = getItem(position).getQuantity();
        float total_price = getItem(position).getTotal_price();


        Order order = new Order(foodName, quantity, total_price);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvFoodName = (TextView) convertView.findViewById(R.id.foodName);
        TextView tvQuantity = (TextView) convertView.findViewById(R.id.quantity);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.price);

        tvFoodName.setText(foodName);
        tvQuantity.setText(Integer.toString(quantity));
        tvPrice.setText(Float.toString(total_price));
        return convertView;
    }
}
