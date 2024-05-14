package com.example.iusovkursovoi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddDrugIntake extends AppCompatActivity {

    private ImageButton btnBack;
    private EditText etMedicine;
    private EditText etDate;
    private EditText etTimeSpan;
    private EditText etCount;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug_intake);
        btnBack = findViewById(R.id.btnBack);
        etMedicine = findViewById(R.id.etMedicine);
        etDate = findViewById(R.id.etDate);
        etTimeSpan = findViewById(R.id.etTimeSpan);
        etCount = findViewById(R.id.etCount);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent alarmIntent = new Intent(AddDrugIntake.this, MyBroadcastReceiver.class);
                Calendar calendar = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();

                calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, 4);
                calendar.set(Calendar.MINUTE, 53);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddDrugIntake.this, 0,
                        alarmIntent, PendingIntent.FLAG_MUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 2000, pendingIntent);
                Toast.makeText(AddDrugIntake.this, "Alarm set", Toast.LENGTH_LONG).show();
                try{
                    String date = getDate(etDate.getText().toString());
                    if(etMedicine.getText().toString().isEmpty() | Integer.parseInt(etTimeSpan.getText().toString()) < 1 | date.isEmpty() | Integer.parseInt(etCount.getText().toString()) < 1){
                        Toast.makeText(getBaseContext(),"Не заполнены обязательные данные.",Toast.LENGTH_LONG).show();
                        return;
                    }
                    SQLiteDatabase db = getBaseContext().openOrCreateDatabase("base.db", MODE_PRIVATE,null);
                    db.execSQL("INSERT OR IGNORE INTO DrugIntakes VALUES (NULL,'"+ etMedicine.getText().toString() +"', '"+date+"', '"+Integer.parseInt(etTimeSpan.getText().toString())+"', '"+Integer.parseInt(etCount.getText().toString())+"')");
                    finish();
                }catch (Exception e){
                    Toast.makeText(getBaseContext(),"Некорректные данные.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private String getDate(String dateString){//получение текущей даты в формате
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter stringFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try{
            return (LocalDate.parse(dateString, stringFormatter)).format(formatter);
        }catch (Exception e){
            return "";
        }
    }
}