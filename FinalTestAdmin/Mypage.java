package com.example.finaltestAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mypage extends AppCompatActivity {

    EditText pwd, name;
    TextView tId, company;
    Button update, delete;
    String sPwd, sName;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        handler = new Handler();

        Intent get = getIntent();
        String id = get.getStringExtra("id");

        tId = (TextView)findViewById(R.id.txtMypageId);
        pwd = (EditText)findViewById(R.id.txtMypagePwd);
        name = (EditText)findViewById(R.id.txtMypageName);
        company = (TextView)findViewById(R.id.txtMypageCom);

        dataSearch(id);


        update = (Button)findViewById(R.id.btnMypageSubmit);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sPwd = pwd.getText().toString();
                sName = name.getText().toString();
                dataUpdate(id, sPwd, sName);
            }
        });

        delete = (Button)findViewById(R.id.btnMypageDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataDelete(id);
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
                Toast.makeText(getApplicationContext(), "탈퇴되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }




    private void dataUpdate(String id, String sPwd, String sName) {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/update.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id).append("/").append(sPwd).append("/").append(sName);
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

    private void dataDelete(String id) {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/delete.php");
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
                    URL setURL = new URL("http://10.0.2.2/mypagesearch.php");
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

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tId.setText(id);
                            name.setText(result[2]);
                            company.setText(result[3]);
                        }
                    });

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }

}
