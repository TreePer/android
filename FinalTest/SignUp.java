package com.example.finaltest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {

    EditText id, pwd, name, company;
    Button insert, cancle;
    String sId, sPwd, sName, sCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        insert = (Button)findViewById(R.id.btnSignUpSubmit);
        cancle = (Button)findViewById(R.id.btnSignUpCancle);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void signUp() {

        id = (EditText)findViewById(R.id.txtSignUpId);
        pwd = (EditText)findViewById(R.id.txtSignUpPwd);
        name = (EditText)findViewById(R.id.txtSignUpName);
        company = (EditText)findViewById(R.id.txtSignUpCompany);

        sId = id.getText().toString();
        sPwd = pwd.getText().toString();
        sName = name.getText().toString();
        sCompany = company.getText().toString();


        new Thread() {
            @Override
            public void run() {
                try {

                    URL setURL = new URL("http://10.0.2.2/insert.php/");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("id=").append(sId).append("&pwd=").append(sPwd).append("&name=").append(sName).append("&company=").append(sCompany);
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
