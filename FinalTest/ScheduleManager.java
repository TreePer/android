package com.example.finaltest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleManager extends AppCompatActivity {
    TextView mon, time, workSum;
    String id, month, year, sDay, sNight, sSum;
    Handler handler;
    String work[];
    int day, night, sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedulemanager);

        Intent get = getIntent();
        id = get.getStringExtra("id");

        handler = new Handler();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat monFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        year = yearFormat.format(currentTime);
        month = monFormat.format(currentTime);

        mon = (TextView)findViewById(R.id.txtMon);
        time = (TextView)findViewById(R.id.txtTime);
        workSum = (TextView)findViewById(R.id.txtWorkSum);

        readWork();


    }

    private void readWork() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/searchwork.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id).append("&mon=").append(month).append("&year=").append(year);
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "euc-kr");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "euc-kr");

                    final BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                    }

                    String resultData = builder.toString();
                    String result[] = resultData.split("/");

                    work = result[2].split(",");
                    day = 0;
                    night = 0;
                    sum = 0;

                    for(int i = 0; i < work.length; i++) {
                        if(work[i].equals("A")) {
                            day += 1;
                        }
                        else if(work[i].equals("B")) {
                            night += 1;
                        }
                    }

                    sum = (day * 8) + (night * 12);

                    sDay = String.valueOf(day * 8);
                    sNight = String.valueOf(night * 12);
                    sSum = String.valueOf(sum);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mon.setText(month);
                            time.setText("주간 : " + sDay + "시간 " + "야간 : " + sNight + "시간");
                            workSum.setText(sSum + "시간");
                        }
                    });


                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }
}
