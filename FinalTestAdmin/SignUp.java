package com.example.finaltestAdmin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class SignUp extends AppCompatActivity {

    EditText id, pwd, name, company;
    Button insert, cancle, gps;
    String sId, sPwd, sName, sCompany;
    private GpsTracker gpsTracker;
    double latitude, longitude;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String REQUIRED_PERMISSIONS[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        insert = (Button)findViewById(R.id.btnSignUpSubmit);
        cancle = (Button)findViewById(R.id.btnSignUpCancle);
        gps = (Button)findViewById(R.id.btnSignUpGPS);

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker = new GpsTracker(SignUp.this);

                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

                String addr = getCurrentAddress(latitude, longitude);

                Toast.makeText(SignUp.this, addr, Toast.LENGTH_SHORT).show();
            }
        });

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
                signUpCompany();
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

                    URL setURL = new URL("http://10.0.2.2/insertadmin.php/");
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

    private void signUpCompany() {
        new Thread() {
            @Override
            public void run() {
                try {

                    URL setURL = new URL("http://10.0.2.2/insertcompany.php/");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name=").append(sCompany).append("&latitude=").append(latitude).append("&longitude=").append(longitude);
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

    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String permissions[], @NonNull int grandResults[]) {
        if(permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
            }
            else {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(SignUp.this, "권한이 없습니다. 설정에서 권한을 허용해야 합니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(SignUp.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(SignUp.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignUp.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(SignUp.this, "이 기능을 사용하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(SignUp.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
            else {
                ActivityCompat.requestPermissions(SignUp.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
        } catch (IOException ioException) {
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_SHORT).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_SHORT).show();
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_SHORT).show();
            return "주소미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString() + "\n";

    }

    private void showDialogforLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("위치서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent callGPASettingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPASettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus()) {
                    if(checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화");
                        checkRunTimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void startWork() {


    }
}
