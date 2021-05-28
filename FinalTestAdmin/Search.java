package com.example.finaltestAdmin;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

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

public class Search extends AppCompatActivity {
    String year, month, sDay, sNight, sSum;
    String work[];
    String id[];
    String dbname[];
    String total[];
    int day, night, sum;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        handler = new Handler();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat monFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        year = yearFormat.format(currentTime);
        month = monFormat.format(currentTime);
        readId();

    }

    private void readWork() {
        ListView lvWork = (ListView)findViewById(R.id.lvWork);
        workAdapter adapter = new workAdapter();
        new Thread() {
            public void run() {
                try {
                    total = new String[id.length];
                    for(int x = 0; x < id.length; x++) {
                        URL setURL = new URL("http://10.0.2.2/searchworkadmin.php");
                        HttpURLConnection http;
                        http = (HttpURLConnection) setURL.openConnection();
                        http.setDefaultUseCaches(false);
                        http.setDoInput(true);
                        http.setRequestMethod("POST");
                        http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("mon=").append(month).append("&year=").append(year);
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

                        work = result[x].split(",");
                        day = 0;
                        night = 0;
                        sum = 0;

                        for(int j = 0; j < work.length; j++) {
                            if(work[j].equals("A")) {
                                day += 1;
                            }
                            else if(work[j].equals("B")) {
                                night += 1;
                            }
                        }

                        day = day * 8;
                        night = night * 12;

                        sum = (day) + (night);

                        sDay = String.valueOf(day);
                        sNight = String.valueOf(night);
                        sSum = String.valueOf(sum);

                        total[x] = "주간 : " + sDay + "시간" + " 야간 : " + sNight + "시간" + " 총 : " + sSum;
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int z = 0; z < dbname.length; z++){
                                lvWork.setAdapter(adapter);
                                adapter.addItem(new workList(dbname[z], total[z]));
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }

    private void readName() {
        new Thread() {
            public void run() {
                try {
                    dbname = new String[id.length];
                    for (int i = 0; i < id.length; i++) {
                        URL setURL = new URL("http://10.0.2.2/mypagesearchadmin.php");
                        HttpURLConnection http;
                        http = (HttpURLConnection) setURL.openConnection();
                        http.setDefaultUseCaches(false);
                        http.setDoInput(true);
                        http.setRequestMethod("POST");
                        http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("name=").append(id[i]);
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

                        dbname[i] = resultData;
                    }

                    readWork();

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }

    private void readId() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/searchworkidadmin.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("mon=").append(month).append("&year=").append(year);
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
                    id = resultData.split("/");

                    readName();
                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }

}

