package com.example.finaltestAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class userUpdate extends AppCompatActivity {

    EditText pwd, name, team;
    TextView tId, company;
    Button update;
    Switch auth;
    String sPwd, sName, sTeam, admin, id, sCom;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo);

        handler = new Handler();

        Intent get = getIntent();
        id = get.getStringExtra("id");
        admin = get.getStringExtra("admin");

        tId = (TextView)findViewById(R.id.txtUserId);
        pwd = (EditText)findViewById(R.id.txtUserPwd);
        name = (EditText)findViewById(R.id.txtUserName);
        company = (TextView)findViewById(R.id.txtUserCom);
        team = (EditText)findViewById(R.id.txtUserTeam);
        auth = (Switch)findViewById(R.id.switchUser);

        dataSearch(id);

        if(admin.equals("1")) {
            auth.setVisibility(View.GONE);
        }
        auth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    admin = "1";
                }
                else {
                    admin = "0";
                }
            }
        });


        update = (Button)findViewById(R.id.btnUserSubmit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPwd = pwd.getText().toString();
                sName = name.getText().toString();
                sTeam = team.getText().toString();
                sCom = company.getText().toString();
                dataUpdate();
                Toast.makeText(getApplicationContext(),"수정되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dataUpdate() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/updateuser.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id).append("/").append(sName).append("/").append(sCom).append("/").append(admin).append("/").append(sTeam);
                    OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "euc-kr");
                    outStream.write(buffer.toString());
                    outStream.flush();
                    InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "euc-kr");

                    BufferedReader reader = new BufferedReader(tmp);

                    while (reader.readLine() != null) {
                        System.out.println(reader.readLine());
                    }

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();
    }

    private void dataSearch(String id) {

        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/updateadmin.php");
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

                    String resultData = builder.toString();
                    final String result[] = resultData.split("/");

                    admin = result[5];

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tId.setText(id);
                            name.setText(result[2]);
                            company.setText(result[3]);
                            team.setText(result[4]);
                            if(admin.equals("1")) {
                                auth.setChecked(true);
                            }
                            else {
                                auth.setChecked(false);
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }
}
