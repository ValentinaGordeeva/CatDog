package com.example.catdog;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class Notification extends AppCompatActivity {

    private EditText editTextTitle;
    private EditText editTextMessage;


    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);

        Button buttonCreateNotification = findViewById(R.id.button_create_notification);


        buttonCreateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString();
                String message = editTextMessage.getText().toString();
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Notification.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Выбрана дата


                                Calendar calendar = Calendar.getInstance();
                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        Notification.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                // Создать уведомление с выбранной датой и временем
                                                Calendar calendar = Calendar.getInstance();
                                                calendar.set(year, month, dayOfMonth, hourOfDay, minute);
                                                long notificationTimeInMillis = calendar.getTimeInMillis();

                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(Notification.this, "default")
                                                        .setSmallIcon(R.drawable.notification)
                                                        .setContentTitle(title)
                                                        .setContentText(message)
                                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                        .setAutoCancel(true)
                                                        .setWhen(notificationTimeInMillis);

                                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Notification.this);
                                                notificationManager.notify(NOTIFICATION_ID, builder.build());

                                                Toast.makeText(Notification.this, "Уведомление создано", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        },
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE),
                                        true
                                );
                                timePickerDialog.show();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();
            }
        });
    }
}
