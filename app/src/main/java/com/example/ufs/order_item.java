package com.example.ufs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class order_item extends AppCompatDialogFragment {
    private ImageButton minus, plus;
    private TextView quantity_v, price_v;
    private orderItemListener listener;
    public String foodName;
    public float price = 0;

    public int quantity = 0;
    public float total_price = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.order_dialog, null);

        builder.setView(view)
                .setTitle(foodName)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyOrder(quantity, total_price);
                    }
                });

        minus = view.findViewById(R.id.minus);
        plus = view.findViewById(R.id.plus);
        quantity_v = view.findViewById(R.id.quantity);
        price_v = view.findViewById(R.id.price);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantity >0){
                    quantity--;
                    total_price = (float)quantity*price;
                    quantity_v.setText(Integer.toString(quantity));
                    DecimalFormat df2 = new DecimalFormat("####.##");
                    df2.format(total_price);
                    price_v.setText(Float.toString(total_price).trim());
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity++;
                total_price = (float)quantity*price;
                quantity_v.setText(Integer.toString(quantity));
                DecimalFormat df2 = new DecimalFormat("####.##");
                df2.format(total_price);
                price_v.setText(Float.toString(total_price).trim());
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (orderItemListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement orderItemDialogListener");
        }

    }
    public interface orderItemListener{
        void applyOrder(int quantity, float total_price);
    }

    public order_item(String foodName, float price) {
        this.foodName = foodName;
        this.price = price;
    }
}
