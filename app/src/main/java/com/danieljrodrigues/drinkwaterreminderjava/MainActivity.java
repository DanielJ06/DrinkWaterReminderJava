package com.danieljrodrigues.drinkwaterreminderjava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.danieljrodrigues.drinkwaterreminderjava.util.NotificationPublisher;

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

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("activated", true);
            editor.putInt("interval", interval);
            editor.putInt("hour", hour);
            editor.putInt("minute", minute);
            editor.apply();

            Intent notificationIntent = new Intent(
                MainActivity.this, NotificationPublisher.class
            );
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION, "Hora de beber água");

            PendingIntent broadcast = PendingIntent.getBroadcast(
                MainActivity.this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );

            long futureInMillis = SystemClock.elapsedRealtime() + (interval * 1000);

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, broadcast);

            activated = true;
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