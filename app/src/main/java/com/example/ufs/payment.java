package com.example.ufs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class payment extends AppCompatActivity {

    private Button credButton;
    private Button studButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        studButton = findViewById(R.id.studID);
        studButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openStudID();
            }
        });

        credButton = findViewById(R.id.credit);
        credButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openCreditCard();
            }
        });
    }

    //opens credit card form activity
    public void openCreditCard()
    {
        Intent card = new Intent(this,credit_card_form.class);
        startActivity(card);
    }

    //opens student ID activity
    public void openStudID()
    {
       Intent netID = new Intent(this, student_id.class);
       startActivity(netID);
    }
}