package com.example.finaltestAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ScheduleManager extends AppCompatActivity {
    String dbId[];
    String dbName[];
    Handler handler;
    String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedulemanager);
        Intent get = getIntent();
        admin = get.getStringExtra("admin");

        handler = new Handler();

        getName();
    }

    private void getName() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/getname.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "euc-kr");
                    final BufferedReader reader = new BufferedReader(tmp);
                    StringBuilder builder = new StringBuilder();
                    String str;

                    while ((str = reader.readLine()) != null) {
                        builder.append(str);
                    }


                    String resultData = builder.toString();
                    String result[] = resultData.split("/");

                    dbName = new String[result.length];

                    for(int i = 0; i < result.length; i++) {
                        dbName[i] = result[i];
                    }

                    getUserId();

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }

    private void getUserId() {
        ListView listUser =(ListView)findViewById(R.id.lvUser);
        userAdapter adapter = new userAdapter();
        new Thread() {
            public void run() {
                try {
                        URL setURL = new URL("http://10.0.2.2/getId.php");
                        HttpURLConnection http;
                        http = (HttpURLConnection) setURL.openConnection();
                        http.setDefaultUseCaches(false);
                        http.setDoInput(true);
                        http.setRequestMethod("POST");
                        http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                        InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "euc-kr");
                        final BufferedReader reader = new BufferedReader(tmp);
                        StringBuilder builder = new StringBuilder();
                        String str;

                        while ((str = reader.readLine()) != null) {
                            builder.append(str);
                        }


                        String resultData = builder.toString();
                        String result[] = resultData.split("/");

                        dbId = new String[result.length];

                        for(int i = 0; i < result.length; i++) {
                            dbId[i] = result[i];
                        }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            for (int z = 0; z < dbId.length; z++){
                                listUser.setAdapter(adapter);
                                adapter.addItem(new userList(dbName[z], dbId[z]));
                            }
                        }
                    });

                        listUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), userUpdate.class);
                                intent.putExtra("id", dbId[position]);
                                intent.putExtra("admin", admin);

                                startActivity(intent);
                            }
                        });


                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();
    }


}

