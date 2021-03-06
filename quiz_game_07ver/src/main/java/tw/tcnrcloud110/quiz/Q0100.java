package tw.tcnrcloud110.quiz;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ajts.androidmads.fontutils.FontUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Q0100 extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();
    private ArrayList<String> recSet_Q0600_cate = null;
    //===================??????API=====================
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "1115b355ff21fafd11a06d258462fb31";
    public static String lat = "24.1469";
    public static String lon = "120.6839";
    public static String lang = "zh_tw";
    private TextView weatherData;
    private ImageView weatherimg;
    private String iconurl;
    private LocationManager manager;
    private Location currentLocation;
    //------------------??????????????????????????????----------------
    private static final String[][] permissionsArray = new String[][]{
            {Manifest.permission.ACCESS_FINE_LOCATION, ""},
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, ""},
            {Manifest.permission.CALL_PHONE, ""}};
    private List<String> permissionsList = new ArrayList<String>();
    //------------------???????????????????????????------------------
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    //--------------------------------------------------------
    private String bestgps;
    //---------------------???????????????????????????---------------
    int minTime = 5000; // ??????
    float minDistance = 5; // ??????
    private String TAG = "tcnr_cloud=>";
    //-----------------------DataBase-----------------------
    private DbHelper dbHper;
    private static final String DB_FILE = "QuizeGame.db";
    private static final int DB_version = 1;
    // ---------------------?????????---------------------------
    private Long startTime;
    private Handler handler = new Handler();
    int autotime = 60;// ?????????????????? ????????????MySQL??????
    int update_time = 0;
    //---------------------WebView------------------------
    private WebView webView;
    private static final String MAP_URL = "file:///android_asset/1.html";// ?????????html??????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//---???????????????????????????????????????
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0100);
        setupViewComponent();
        initDB();
        //------------------???????????????????????????
        checkRequiredPermission(this);
        u_checkgps();
        //------------------????????????API
        //updatePosition();
        //getCurrentData();
        //------------------
        setWebView();
    }

    private void enableStrictMode(Context context) {
        //-------------????????????????????????????????????---------------
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().
                detectLeakedSqlLiteObjects().
                penaltyLog().
                penaltyDeath().
                build());
    }

    private void setWebView() {
        webView = (WebView) findViewById(R.id.q0100_webview);
        webView.setBackgroundColor(getColor(R.color.background));
        //---------------------------------------webview????????????
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(MAP_URL);
    }

    private void setupViewComponent() {
        //---------------------------------------Intent
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        //---------------------------------------??????
        ImageView img002 = (ImageView)findViewById(R.id.q0100_img002);
        ImageView img003 = (ImageView)findViewById(R.id.q0100_img003);
        ImageView img004 = (ImageView)findViewById(R.id.q0100_img004);
        ImageView img005 = (ImageView)findViewById(R.id.q0100_img005);

        //---------------------------------------??????
        img002.setOnClickListener(this);
        img003.setOnClickListener(this);
        img004.setOnClickListener(this);
        img005.setOnClickListener(this);

        //===================??????API=====================
        weatherData = (TextView) findViewById(R.id.q0100_status);
        weatherData.setMovementMethod(ScrollingMovementMethod.getInstance());//-----TextView??????????????????
        weatherimg = (ImageView) findViewById(R.id.q0100_img001);

        //---------------------------------------????????????layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        LinearLayout lay01 = (LinearLayout)findViewById(R.id.q0100_lay01);
        LinearLayout lay02 = (LinearLayout)findViewById(R.id.q0100_lay02);
        LinearLayout lay03 = (LinearLayout)findViewById(R.id.q0100_lay03);
        LinearLayout lay04 = (LinearLayout)findViewById(R.id.q0100_lay04);
        ImageView img001 = (ImageView)findViewById(R.id.q0100_img001);

//        lay01.getLayoutParams().height=displayMetrics.heightPixels /37*9;
        //lay02.getLayoutParams().height=displayMetrics.heightPixels /40*8;
        lay03.getLayoutParams().height=displayMetrics.heightPixels /40*9;
        lay04.getLayoutParams().height=displayMetrics.heightPixels /40*9;
        img001.getLayoutParams().height=displayMetrics.heightPixels /40*6;
        img002.getLayoutParams().width=displayMetrics.widthPixels/40*19;
        img003.getLayoutParams().width=displayMetrics.widthPixels/40*19;
        img004.getLayoutParams().width=displayMetrics.widthPixels/40*19;
        img005.getLayoutParams().width=displayMetrics.widthPixels/40*19;
        //tab01.getLayoutParams().height=displayMetrics.heightPixels /9;
        weatherData.getLayoutParams().width=displayMetrics.widthPixels/10*6;

        //====================????????????=======================
        startTime = System.currentTimeMillis();// ??????????????????
        handler.postDelayed(updateTimer, 500);  // ??????Delay?????????
        //-------------------------
    }

    //==========================???????????????========================
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, autotime * 1000); // ?????????????????????
            // -------????????????MySQL
            dbmysql();
            recSet_Q0600_cate = dbHper.getRecSet_query_Q0600("??????");
            showRec();
            //----------------
            ++update_time;
            //------ ???????????? ---------------------------
            //ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100); // 100=max
            //toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 500);
            //toneG.release();
            // --------------------------------------------------------
        }
    };

    // ========================??????MySQL ??????========================
    private void dbmysql() {
        String sqlctl = "SELECT * FROM Q0600";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0600_DBConnector.executeQuery_Q0600(nameValuePairs);

            JSONArray jsonArray = new JSONArray(result);
            // ------
            if (jsonArray.length() > 0) { // ------------------------------MySQL ?????????????????????
                int rowsAffected = dbHper.clearRec_Q0600();// --------?????????,????????????SQLite??????
                // ??????JASON ????????????????????????
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) ?????????????????? --?????? jsonObject ????????????("key","value")
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // ??????????????????
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // ???????????????????????????
                        // -------------------------------------------------------------------
                    }
                    // ---(2) ????????????????????????---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------??????SQLite----------------------
                    long rowID = dbHper.insertRec_m_Q0600(newRow);
                }
                //Toast.makeText(getApplicationContext(), "????????? " + Integer.toString(jsonArray.length()) + " ?????????", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void showRec() {
        if (recSet_Q0600_cate.size() != 0){

            LinearLayout lay05 = (LinearLayout)findViewById(R.id.q0100_lay05);
            lay05.removeAllViews();//-------??????layout

            for (int i = 1 ; i <= recSet_Q0600_cate.size() ; i++){
                String[] ann = recSet_Q0600_cate.get(i-1).split("#");
                //******************????????????***************
                String timeStr = ann[7]; // ????????????
                SimpleDateFormat timeZone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timeZone.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date date = null;  // ????????????????????????????????????Date??????
                    try {
                        date = timeZone.parse(timeStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                SimpleDateFormat timeZone_TW = new SimpleDateFormat("yyyy-MM-dd");
                timeZone_TW.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                String date_TW = timeZone_TW.format(date);
                //******************************************
                String settime =date_TW;
                String title;
                if (ann[2].length() >= 10){
                     title = ann[2].substring(0,10)+"...";
                }else{
                     title = ann[2];
                }
                //------------------------------
                TextView tv01 = new TextView(this);
                tv01.setId(i);
                tv01.setText(settime+" : "+title);
                tv01.setTextColor(getColor(R.color.second_title_text));
                tv01.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

                lay05.addView(tv01);
            }
        }
    }

    private void initDB() {
        if (dbHper == null){
            dbHper = new DbHelper(this, DB_FILE, null, DB_version);
        }
        dbmysql();
    }

//====================intent====================
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.q0100_img002:
                intent.putExtra("class_title", getString(R.string.q0100_b003));
                intent.setClass(Q0100.this, Q0300.class);
                break;
            case R.id.q0100_img003:
                intent.putExtra("class_title", getString(R.string.q0100_b006));
                intent.setClass(Q0100.this, Q0600.class);
                break;
            case R.id.q0100_img004:
                intent.putExtra("class_title", getString(R.string.q0100_b002));
                intent.setClass(Q0100.this, Q0400.class);
                break;
            case R.id.q0100_img005:
                intent.putExtra("class_title", getString(R.string.q0100_b004));
                intent.setClass(Q0100.this, Q0500.class);
                break;
        }
        startActivity(intent);
    }

    //===================??????API=====================
    private void getCurrentData() {
//********??????????????????????????????*****************************
        final ProgressDialog pd = new ProgressDialog(Q0100.this);

        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setTitle("Loading");
        pd.setMessage("???????????????...");
        pd.setIndeterminate(false);
        pd.show();
//***************************************************************
/*
        Retrofit ???????????? Square ????????????????????? RESTful API ???????????????????????????????????????
        ???????????????????????? okHttp ???okHttp ???????????? okHttp ?????????
        Retrofit ???????????????????????????????????? API ???????????? JSON?????????????????????????????????????????? Converter???
        ?????????????????? Server ????????????????????????????????? okHttpClient ?????? Interceptor???
        ??? Retrofit 1.9.0 ??? Interceptor ?????????????????????
*/
//***************************************************************

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Q0100_WeatherServer service = retrofit.create(Q0100_WeatherServer.class);
        Call<Q0100_WeatherResponse> call = service.getCurrentWeatherData(lat ,lon ,lang, AppId);
        call.enqueue(new Callback<Q0100_WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<Q0100_WeatherResponse> call, @NonNull Response<Q0100_WeatherResponse> response) {
                if (response.code() == 200) {
                    Q0100_WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;
                    //getString(R.string.q0100_country) +weatherResponse.sys.country
                    //getString(R.string.q0100_Pressure) +weatherResponse.main.pressure
                    //getString(R.string.q0100_weather_lat) + (lat)
                    //getString(R.string.q0100_weather_lon) + (lon)
                    String stringBuilder =
                            getString(R.string.q0100_areaname) + weatherResponse.name +
                            "\n" +
                            getString(R.string.q0100_Temperature) +
                            // ---------------    K?????????????????C??-------------------
                            (int) (Float.parseFloat("" + weatherResponse.main.temp) - 273.15) +
                            "\n" +
                            getString(R.string.q0100_Temperature_Min) +
                            (int) (Float.parseFloat("" + weatherResponse.main.temp_min) - 273.15) +
                            "\n" +
                            getString(R.string.q0100_Temperature_Max) +
                            (int) (Float.parseFloat("" + weatherResponse.main.temp_max) - 273.15) +
                            "\n" +
                            getString(R.string.q0100_Humidity) + weatherResponse.main.humidity ;
                    weatherData.setText(stringBuilder);  //??????
                    //====????????????==============
                    //weatherLat.setText(getString(R.string.weather_lat) + (lat));
                    //weatherLon.setText(getString(R.string.weather_lon) + (lon));
                    //======?????? Internet ??????==================
                    int b_id = weatherResponse.weather.get(0).id;
                    String b_main = weatherResponse.weather.get(0).main;
                    String b_description = weatherResponse.weather.get(0).description;
                    String b_icon = weatherResponse.weather.get(0).icon;
                    iconurl = "https://openweathermap.org/img/wn/" + b_icon + "@2x.png";
                    //iconurl = "https://openweathermap.org/img/wn/" + b_icon + "@2x.png";
                    //https://openweathermap.org/img/wn/50n@2x.png
                    int cc = 1;
                    String weather = b_description;
                    // +"\n" +getString(R.string.w_icon) +"\n" +iconurl
                    //=========================
                    TextView content = (TextView)findViewById(R.id.q0100_t003);
                    content.setText(weather);

//*****************?????? AyncTask  ??????????????????**********************************
//                    AsyncTask????????????????????????????????????????????????????????????????????????????????????????????????????????????UI????????????
//                    Android 4.0 ?????????????????????????????????????????????????????????????????????(Main Thread)?????????
//                    ??????????????????UI?????????(UI Thread)???????????????UI?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                    ?????????????????????????????????????????????App???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
//                    AsyncTask<Params, Progress, Result>?????????????????????????????????????????????????????????
//                    ???????????????????????????????????????????????????????????????
//                    Params ??? ?????????????????????????????????????????????
//                    Progress ??? ????????????????????????????????????????????????
//                    Result ??? ?????????????????????????????????????????????????????????????????????????????????
//                    ?????????AsyncTask?????????????????????
//
//                    onPreExecute ??? ???????????????????????????????????????????????????
//                    doInBackground ??? ?????????????????????????????????
//                    onProgressUpdate ??? ????????????????????????publishProgress??????????????????????????????????????????????????????
//                    onPostExecute ??? ??????????????????????????????????????????
//                    https://developer.android.com/reference/android/os/AsyncTask
////This class was deprecated in API level 30.
////Use the standard java.util.concurrent or Kotlin concurrency utilities instead.

                    new AsyncTask<String, Void, Bitmap>() {

                        @Override
                        protected Bitmap doInBackground(String... strings) {
                            String url = iconurl;
                            return getBitmapFromURL(url);
                        }
                        @Override
                        protected void onPostExecute(Bitmap result) //???doinbackground?????????
                        {
                            weatherimg.setImageBitmap(result);
                            pd.cancel();
                            super.onPostExecute(result);
                        }
                    }.execute(iconurl);
//      ***************************************************************************
/*+++++++++++++++++++++
+    ??????Picasso????????????                +
+++++++++++++++++++++*/
////-----------    implementation 'com.squareup.picasso:picasso:2.71828'
//                    Picasso.get()
//                            .load(iconurl)
//                            .into(weatherimg);
//                    pd.cancel();
//////-----------------------------------------------------------
// **********************************************************************************
                }
            }
            @Override
            public void onFailure(@NonNull Call<Q0100_WeatherResponse> call, @NonNull Throwable t) {
                weatherData.setText(t.getMessage());
            }
        });
    }

    //----------------??????????????????????????????Bitmap-----------------
    private static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //============================================
    // ?????????????????????
    private void updatePosition() {

        if (currentLocation == null) {
            lat = "24.1469";
            lon = "120.6839";

        } else {
            lat = Double.toString(currentLocation.getLatitude());
            lon = Double.toString(currentLocation.getLongitude());
        }
    }

    //------------------------------------------------
    private void u_checkgps() {
        // ?????????????????????LocationManager??????
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // ?????????????????????GPS
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // ????????????????????????GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("????????????")
                    .setMessage("GPS???????????????????????????.\n"
                            + "????????????????????????????????????GPS?")
                    .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // ??????Intent?????????????????????????????????GPS??????
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("?????????", null).create().show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            updatePosition();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
        @Override
        public void onProviderEnabled(String provider) {
        }
        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    //=================================================
    private void checkRequiredPermission(final Activity activity) {
        permissionsArray[0][1]=getString(R.string.dialog_msg1);
        permissionsArray[1][1]=getString(R.string.dialog_msg2);
        permissionsArray[2][1]=getString(R.string.dialog_msg3);
        //String permission_check= String[i][0] permission;
        for (int i = 0; i < permissionsArray.length; i++) {
            if (ContextCompat.checkSelfPermission(activity
                    , permissionsArray[i][0]) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permissionsArray[i][0]);
            }
        }
        if (permissionsList.size() != 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    /*** request???????????????*/
    private void requestNeededPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
    }

    //??????????????????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), permissionsArray[i][1] + "??????????????????!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "?????????????????? " + permissionsArray[i][1], Toast.LENGTH_LONG).show();
                        //------------------
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Q0100_Util.showDialog(this, R.string.q0100_dialog_msg1, android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestNeededPermission();
                                }
                            });
                        } else {
                            // ???????????????request
                            requestNeededPermission();
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    //===========================????????????==========================
    @Override
    protected void onResume() {
        super.onResume();
        // ??????????????????????????????
        Criteria criteria = new Criteria();
        bestgps = manager.getBestProvider(criteria, true);

        try {
            if (bestgps != null) { // ???????????????????????????,???????????????
                currentLocation = manager.getLastKnownLocation(bestgps);
                manager.requestLocationUpdates(bestgps, minTime, minDistance, listener);
            } else { // ???????????????????????????,???????????????
                currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTime, minDistance, listener);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "GPS????????????..." + e.getMessage());
        }
        updatePosition(); // ????????????
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimer);
        //---------------------
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(updateTimer);
        //---------------------
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();  ???????????????
    }

    //===========================Menu==========================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0100, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.q0100_m001:
                intent.putExtra("class_title",getString(R.string.q0100_m001));
                intent.setClass( Q0100.this, Q0200.class);
                startActivity(intent);
                break;
            case R.id.q0100_m002:
                new AlertDialog.Builder(Q0100.this)
                        .setTitle(getString(R.string.q0100_menu_about))
                        .setMessage(getString(R.string.q0100_menu_message))
                        .setCancelable(false)
                        .setIcon(android.R.drawable.btn_radio)
                        .setPositiveButton(getString(R.string.q0100_menu_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            case R.id.q0100_action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}