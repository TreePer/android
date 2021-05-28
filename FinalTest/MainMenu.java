package com.example.finaltest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.List;


public class MainMenu extends AppCompatActivity {

    Button schedule, start, manager, mypage, logout;
    String id, company, team, mon, year, day, work, hour, min;
    String workSchedule[];
    String workDay[];
    int intMin;
    private GpsTracker gpsTracker;
    double latitude, longitude, comLatitude, comLongitude;
    Handler handler;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String REQUIRED_PERMISSIONS[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        handler = new Handler();

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat hourFormat = new SimpleDateFormat("kk", Locale.getDefault());
        SimpleDateFormat minFormat = new SimpleDateFormat("mm", Locale.getDefault());

        year = yearFormat.format(currentTime);
        mon = monFormat.format(currentTime);
        day = dayFormat.format(currentTime);
        hour = hourFormat.format(currentTime);
        min = minFormat.format(currentTime);

        intMin = Integer.parseInt(min);

        Intent get = getIntent();
        id = get.getStringExtra("id");

        schedule = (Button)findViewById(R.id.btnSchedule);
        start = (Button)findViewById(R.id.btnStart);
        manager = (Button)findViewById(R.id.btnManager);
        mypage = (Button)findViewById(R.id.btnMypage);
        logout = (Button)findViewById(R.id.btnLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        if(!checkLocationServicesStatus()) {
            showDialogforLocationServiceSetting();
        } else {
            checkRunTimePermission();
        }


        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Schedule.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ScheduleManager.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Mypage.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gpsTracker = new GpsTracker(MainMenu.this);

                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

                getCompany();

            }
        });

    }

    private void getCompany() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/companysearch.php");
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

                    company = builder.toString();
                    getTeam();

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();
    }

    private void getGps() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/gpssearch.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(company);
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

                    comLatitude = Double.parseDouble(result[0]);
                    comLongitude = Double.parseDouble(result[1]);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            if((latitude - 0.0015 <= comLatitude) && (comLatitude <= latitude + 0.0015) && (longitude - 0.0015 <= comLongitude) && (comLongitude <= longitude + 0.0015)) {
                                MainMenu.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchWork();
                                        readWork();
                                    }
                                });
                            }
                            else {
                                MainMenu.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainMenu.this, "현재 위치가 회사가 아닙니다.", Toast.LENGTH_SHORT).show();
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
                    getGps();

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();


    }

    private void searchWork() {
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

                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

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
                    buffer.append("name").append("=").append(id).append("&mon=").append(mon).append("&year=").append(year);
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

                    if (resultData != "") {
                        work = result[2];
                        workDay = work.split(",");
                    }

                    if(result[0].equals(year) && result[1].equals(mon)) {
                        updateWork();
                    }
                    else {
                        insertWork();
                    }



                } catch (Exception e) {
                    Log.e("", "", e);
                }
            }
        }.start();

    }

    private void insertWork() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/insertwork.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("id").append("=").append(id).append("&team=").append(team).append("&schedule=");

                    for(int i = 0; i < workSchedule.length - 1; i++) {
                        buffer.append(workSchedule[i]).append(",");
                    }
                    buffer.append(workSchedule[workSchedule.length - 1]);

                    buffer.append("&check=");
                    int inDay = Integer.parseInt(day);

                    for(int i = 0; i < inDay - 2; i++) {
                        buffer.append("C").append(",");
                    }
                    buffer.append("C");
                    if(workSchedule[inDay - 1].equals("A")) {
                        if(((hour.equals("07")) && (55<= intMin) && (intMin <= 59)) || ((hour.equals("08")) && (intMin <= 05))) {
                            buffer.append(",").append("A");
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            
                        }
                    }
                    else if (workSchedule[inDay - 1].equals("B")){
                        if(((hour.equals("21")) && (55<= intMin) && (intMin <= 59)) || ((hour.equals("22")) && (intMin <= 05))) {
                            buffer.append(",").append("B");
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else {
                        MainMenu.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainMenu.this, "출근 일정이 등록되지않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    buffer.append("&mon=").append(mon).append("&year=").append(year);

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
    private void updateWork() {
        new Thread() {
            public void run() {
                try {
                    URL setURL = new URL("http://10.0.2.2/updatework.php");
                    HttpURLConnection http;
                    http = (HttpURLConnection) setURL.openConnection();
                    http.setDefaultUseCaches(false);
                    http.setDoInput(true);
                    http.setRequestMethod("POST");
                    http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("name").append("=").append(id).append("/").append(mon).append("/").append(year).append("/").append(work);

                    int inDay = Integer.parseInt(day);
                    workDay = new String[inDay];
                    if(workSchedule[inDay - 1].equals("A") && workDay[inDay - 1] == null) {
                        if(((hour.equals("07")) && (55<= intMin) && (intMin <= 59)) || ((hour.equals("08")) && (intMin <= 05))) {
                            buffer.append(",").append("A");
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                    else if (workSchedule[inDay - 1].equals("B") && workDay[inDay - 1] == null){
                        if(((hour.equals("21")) && (55<= intMin) && (intMin <= 59)) || ((hour.equals("22")) && (intMin <= 05))) {
                            buffer.append(",").append("B");
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            MainMenu.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainMenu.this, "출근 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else if (workDay[inDay - 1] != null) {
                        MainMenu.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainMenu.this, "이미 출근했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        MainMenu.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainMenu.this, "출근 일정이 등록되지않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

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
                    Toast.makeText(MainMenu.this, "권한이 없습니다. 설정에서 권한을 허용해야 합니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainMenu.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainMenu.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainMenu.this, REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(MainMenu.this, "이 기능을 사용하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(MainMenu.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
            else {
                ActivityCompat.requestPermissions(MainMenu.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
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