package com.danieljrodrigues.drinkwaterreminderjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button notifyBtn;
    private EditText intervalInput;

    private int hour;
    private int minute;
    private int interval;

    private boolean activated = false;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timePicker = findViewById(R.id.timePicker);
        notifyBtn = findViewById(R.id.notifyBtn);
        intervalInput = findViewById(R.id.textNumberInterval);
        timePicker.setIs24HourView(true);

        preferences = getSharedPreferences("db", Context.MODE_PRIVATE);

        activated = preferences.getBoolean("activated", false);

        if (activated) {
            notifyBtn.setText(R.string.pause);
            notifyBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkPrimary));

            int interval = preferences.getInt("interval", 0);
            int hour = preferences.getInt("hour", timePicker.getCurrentHour());
            int minute = preferences.getInt("minute", timePicker.getCurrentMinute());

            intervalInput.setText(String.valueOf(interval));
            timePicker.setCurrentHour(hour);
            timePicker.setCurrentMinute(minute);
        }
    }

    public void notifyClick(View view) {
        String inputContent = intervalInput.getText().toString();

        if(inputContent.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        interval = Integer.parseInt(inputContent);

        if(!activated) {
            notifyBtn.setText(R.string.pause);
            notifyBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.darkPrimary));
            activated = true;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", true);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);
            editor.apply();
        } else {
            notifyBtn.setText(R.string.notify);
            notifyBtn.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.primary));
            activated = false;

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", false);
            editor.remove("interval");
            editor.remove("hour");
            editor.remove("minute");
            editor.apply();
        }
    }
}