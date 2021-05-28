package com.example.finaltestAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogIn extends AppCompatActivity {
    Button login;
    EditText id, pwd;
    String checkId, checkPwd, x, y, admin;
    boolean check;
    Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        handler = new Handler();

        id = (EditText) findViewById(R.id.txtLoginId);
        pwd = (EditText) findViewById(R.id.txtLoginPwd);

        login = (Button)findViewById(R.id.btnLogInSubmit);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkId = id.getText().toString();
                checkPwd = pwd.getText().toString();

                loginCheck(checkId, checkPwd);

            }
        });
    }

    private void loginCheck (String checkId, String checkPwd) {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/loginadmin.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(checkId);
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
                    x = result[0];
                    y = result[1];
                    admin = result[2];

                    if(admin.equals("1") || admin.equals("2")) {
                        if ((checkId.equals(x)) && (checkPwd.equals(y))) {
                            check = true;
                        } else {
                            check = false;
                        }
                    }
                    else {
                        check = false;
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if(check) {
                                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                intent.putExtra("id", checkId);
                                intent.putExtra("admin", admin);
                                startActivity(intent);
                            }
                            else {
                                LogIn.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LogIn.this, "아이디, 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                });
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
