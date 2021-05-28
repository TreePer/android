package com.example.finaltest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import androidx.gridlayout.widget.GridLayout;

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

public class Schedule extends AppCompatActivity {
    String id, team, year, month, worktype;
    String workSchedule[];
    String day[] = new String[31];
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat monFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        year = yearFormat.format(currentTime);
        month = monFormat.format(currentTime);

        Intent get = getIntent();
        id = get.getStringExtra("id");

        for(int i = 0; i < 31; i++) {
            x = i + 1;
            day[i] = String.valueOf(x);
        }

        getTeam();

    }

    private void getTeam() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/teamsearch.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id);
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

                    team = builder.toString();
                    searchWork();

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();


    }

    private void searchWork() {

        GridView gridView = (GridView)findViewById(R.id.scheduleGrid);
        CustomAdapter adapter = new CustomAdapter();

        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/worksearch.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(team);
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
                    String result[] = resultData.split(",");
                    workSchedule = new String[result.length];

                    for(int i = 0; i < result.length; i++) {
                        workSchedule[i] = result[i];
                    }

                    for(int i = 0; i < workSchedule.length; i++) {
                        if(workSchedule[i].equals("A")) {
                            worktype = "주간";
                        }
                        else if(workSchedule[i].equals("B")) {
                            worktype = "야간";
                        }
                        else {
                            worktype = "휴무";
                        }
                        adapter.addItem(new workItem(day[i], worktype));
                        gridView.setAdapter(adapter);
                    }


                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }
}
