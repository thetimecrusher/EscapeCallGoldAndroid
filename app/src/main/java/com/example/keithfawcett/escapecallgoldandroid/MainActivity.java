package com.example.keithfawcett.escapecallgoldandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    Context mContext = this;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton chooseCallerButton = (ImageButton) findViewById(R.id.chooseCaller);
        ImageButton createCallerButton = (ImageButton) findViewById(R.id.createCaller);

        chooseCallerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CallSettings.class);
                startActivity(intent);
            }
        });

        createCallerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CustomCaller.class);
                startActivity(intent);
            }
        });


    }
}
