package com.danieljrodrigues.drinkwaterreminderjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button notifyBtn;
    private EditText intervalInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.timePicker);
        notifyBtn = findViewById(R.id.notiyBtn);
        intervalInput = findViewById(R.id.textNumberInterval);

        timePicker.setIs24HourView(true);
    }
}