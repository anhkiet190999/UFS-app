package com.example.ufs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class add_item extends AppCompatDialogFragment {
    private EditText edit_item;
    private EditText edit_price;
    private addItemListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_item, null);

        builder.setView(view)
                .setTitle("New Item")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String itemName = edit_item.getText().toString().trim();
                        String price = edit_price.getText().toString().trim();

                        listener.applyText(itemName, price);
                    }
                });

        edit_item = view.findViewById(R.id.edit_itemName);
        edit_price = view.findViewById(R.id.edit_price);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            listener = (addItemListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement addItemDialogListener");
        }

    }
    public interface addItemListener{
        void applyText(String itemName, String price);
    }
}
