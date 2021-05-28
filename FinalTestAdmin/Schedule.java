package com.example.finaltestAdmin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Schedule extends AppCompatActivity {
    Spinner yearSpin, monSpin, teamSpin, workSpin;
    Button submit;
    String year, mon, team, work;
    String date[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        submit = (Button)findViewById(R.id.btnSchSub);

        yearSpin = (Spinner)findViewById(R.id.spinYear);
        monSpin = (Spinner)findViewById(R.id.spinMon);
        teamSpin = (Spinner)findViewById(R.id.spinTeam);
        workSpin = (Spinner)findViewById(R.id.spinWork);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.date_year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpin.setAdapter(yearAdapter);
        ArrayAdapter monAdapter = ArrayAdapter.createFromResource(this, R.array.date_mon, android.R.layout.simple_spinner_item);
        monAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monSpin.setAdapter(monAdapter);
        ArrayAdapter teamAdapter = ArrayAdapter.createFromResource(this, R.array.team, android.R.layout.simple_spinner_item);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpin.setAdapter(teamAdapter);
        ArrayAdapter workAdapter = ArrayAdapter.createFromResource(this, R.array.work, android.R.layout.simple_spinner_item);
        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workSpin.setAdapter(workAdapter);
        yearSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        monSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mon = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        workSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                work = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        teamSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                team = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertWork();

            }
        });


    }

    private void insertWork() {
        if(work.equals("주야야")) {
            if((mon.equals("01")) || (mon.equals("03")) || (mon.equals("05")) || (mon.equals("07")) || (mon.equals("08")) || (mon.equals("10")) || (mon.equals("12"))) {
                date = new String[31];
                for(int i = 0; i < 7; i++) {
                    date[i] = "A";
                }
                for (int i = 7; i < 14; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for (int i = 14; i < 21; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 21; i < 28; i++) {
                    date[i] = "A";
                }
                for (int i = 28; i < 31; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }

            }
            else if ((mon.equals("04")) || (mon.equals("06")) || (mon.equals("09")) || (mon.equals("11"))) {
                date = new String[30];
                for(int i = 0; i < 7; i++) {
                    date[i] = "A";
                }
                for (int i = 7; i < 14; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for (int i = 14; i < 21; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 21; i < 28; i++) {
                    date[i] = "A";
                }
                for (int i = 28; i < 30; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }

            }
            else {
                date = new String[28];
                for(int i = 0; i < 7; i++) {
                    date[i] = "A";
                }
                for (int i = 7; i < 14; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for (int i = 14; i < 21; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 21; i < 28; i++) {
                    date[i] = "A";
                }

            }

        }
        else if (work.equals("야주야")) {

            if((mon.equals("01")) || (mon.equals("03")) || (mon.equals("05")) || (mon.equals("07")) || (mon.equals("08")) || (mon.equals("10")) || (mon.equals("12"))) {
                date = new String[31];
                for (int i = 0; i < 7; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for(int i = 7; i < 14; i++) {
                    date[i] = "A";
                }
                for (int i = 14; i < 21; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 21; i < 28; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for (int i = 28; i < 31; i++) {
                    date[i] = "A";
                }

            }
            else if ((mon.equals("04")) || (mon.equals("06")) || (mon.equals("09")) || (mon.equals("11"))) {
                date = new String[30];
                for (int i = 0; i < 7; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for(int i = 7; i < 14; i++) {
                    date[i] = "A";
                }
                for (int i = 14; i < 21; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 21; i < 28; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for (int i = 28; i < 30; i++) {
                    date[i] = "A";
                }

            }
            else {
                date = new String[28];
                for (int i = 0; i < 7; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for(int i = 7; i < 14; i++) {
                    date[i] = "A";
                }
                for (int i = 14; i < 21; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 21; i < 28; i++) {
                    if ((i % 2) == 1) {
                        date[i] = "B";
                    } else {
                        date[i] = "C";
                    }
                }
                    date[27] = "A";
                }


        }
        else{

            if((mon.equals("01")) || (mon.equals("03")) || (mon.equals("05")) || (mon.equals("07")) || (mon.equals("08")) || (mon.equals("10")) || (mon.equals("12"))) {
                date = new String[31];
                for (int i = 0; i < 7; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 7; i < 14; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for(int i = 14; i < 21; i++) {
                    date[i] = "A";
                }

                for (int i = 21; i < 28; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 28; i < 31; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }

            }
            else if ((mon.equals("04")) || (mon.equals("06")) || (mon.equals("09")) || (mon.equals("11"))) {
                date = new String[30];
                for (int i = 0; i < 7; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 7; i < 14; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for(int i = 14; i < 21; i++) {
                    date[i] = "A";
                }

                for (int i = 21; i < 28; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 28; i < 30; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }

            }
            else {
                for (int i = 0; i < 7; i++) {
                    date = new String[28];
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }
                for (int i = 7; i < 14; i++) {
                    if((i % 2) == 1) {
                        date[i] = "B";
                    }
                    else {
                        date[i] = "C";
                    }
                }
                for(int i = 14; i < 21; i++) {
                    date[i] = "A";
                }

                for (int i = 21; i < 28; i++) {
                    if((i % 2) == 1) {
                        date[i] = "C";
                    }
                    else {
                        date[i] = "B";
                    }
                }

                date[27] = "C";

                }

        }

        new Thread() {
            @Override
            public void run() {
                try {

                    URL setURL = new URL("http://10.0.2.2/insertschedule.php/");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("mon=").append(mon).append("&team=").append(team).append("&year=").append(year).append("&work=");
                    for(int i = 0; i < date.length - 1; i++) {
                        buffer.append(date[i]).append(",");
                    }
                    buffer.append(date[date.length - 1]);
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "euc-kr");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "euc-kr");
                    final BufferedReader reader = new BufferedReader(tmp);
                    while(reader.readLine() != null) {
                        System.out.println(reader.readLine());
                    }
                }
                catch (Exception e) {
                    Log.e("", "", e);
                }
            }

        }.start();
    }
}
