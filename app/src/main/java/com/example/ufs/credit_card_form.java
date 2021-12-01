package com.example.ufs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class credit_card_form extends AppCompatActivity {
    private Button backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_form);

        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goBack();
            }
        });
    }

    //goes back to choosing payment
    public void goBack()
    {
        Intent back = new Intent(this, payment.class);
        startActivity(back);
    }
}