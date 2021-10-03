//package tw.gov.tcnrcloud110;
//
//import android.Manifest;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.ColorDrawable;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import static android.Manifest.permission.ACCESS_FINE_LOCATION;
//
//public class Q0500 extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
//    private GoogleMap map;
//    static LatLng VGPS = new LatLng(24.172127, 120.610313);
//    int mapzoom = 12;
//    private static String[][] locations = {
//            {"中區職訓", "24.172127,120.610313"},
//            {"東海大學路思義教堂", "24.179051,120.600610"},
//            {"台中公園湖心亭", "24.144671,120.683981"},
//            {"台中火車站", "24.136829,120.685011"},
//            {"國立科學博物館", "24.1579361,120.6659828"}};
//
//    private static String[] mapType = {
//            "街道圖",
//            "衛星圖",
//            "地形圖",
//            "混和圖",
//            "開啟路況",
//            "關閉路況"};
//
//    //----------
//    String TAG = "chris=>";
//    private Spinner mSpnLocation,mSpnMapType;
//    private double dLat,dLon;
//    private BitmapDescriptor image_des;//圖標 顯示
//    private int icosel=1;  //圖示旗標
//    //-------------------------------------
//    private LocationManager locationManager;
//    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=101;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.q0500);
//        //------------------設定MapFragment---------------------
//        SupportMapFragment mapFragment;
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        //---------------------------------------
//        u_checkgps();//檢查GPS開啟沒
//        setupViewComponent();
//    }
//
//
//    private void setupViewComponent() {
//        Intent intent=this.getIntent();
//        String mode_title = intent.getStringExtra("class_title");
//        this.setTitle(mode_title);
//        //----------------------------------------------------------
//        mSpnLocation=(Spinner)this.findViewById(R.id.spnLocation);
//        mSpnMapType=(Spinner)this.findViewById(R.id.spnMapType);
//        //------------
//        icosel=1;//設定圖示初始值
//        //************
//        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
//
//        for(int i=0;i<locations.length;i++)
//            adapter.add(locations[i][0]);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpnLocation.setAdapter(adapter);
//
//        //指定事件處理物件
//        mSpnLocation.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                setMapLocation();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        //----------------
//        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
//        for (int i=0;i<mapType.length;i++)
//            adapter.add(mapType[i]);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mSpnMapType.setAdapter(adapter);
//        //---------設定ARGB透明度---------
//        //        mSpnMapType.setPopupBackgroundDrawable(new ColorDrawable(0x00FFFFFF)); //全透明
//        mSpnMapType.setPopupBackgroundDrawable(new ColorDrawable(0xF2FFFFFF)); //50%透明
//        mSpnLocation.setPopupBackgroundDrawable(new ColorDrawable(0xF2FFFFFF)); //50%透明
//        //        # ARGB依次代表透明度（alpha）、紅色(red)、綠色(green)、藍色(blue)
////        100% — FF       95% — F2        90% — E6        85% — D9
////        80% — CC        75% — BF        70% — B3        65% — A6
////        60% — 99        55% — 8C        50% — 80        45% — 73
////        40% — 66        35% — 59        30% — 4D        25% — 40
////        20% — 33        15% — 26        10% — 1A         5% — 0D         0% — 00
//        //---------------
//        mSpnMapType.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);//道路地圖
//                        break;
//                    case 1:
//                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);//衛星空照地圖
//                        break;
//                    case 2:
//                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);//地型圖
//                        break;
//                    case 3:
//                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);//道路地圖混和空照圖
//                        break;
//                    case 4:
//                        map.setTrafficEnabled(true);//開啟路況
//                        break;
//                    case 5:
//                        map.setTrafficEnabled(false);//關閉路況
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//    }
//
//    private void setMapLocation() {
//        showioc();//刷新景點
//        int iSelect = mSpnLocation.getSelectedItemPosition();
//        String[] sLocation = locations[iSelect][1].split(",");
//        dLat = Double.parseDouble(sLocation[0]);//南北緯
//        dLon = Double.parseDouble(sLocation[1]);//東西經
//        String vtitle = locations[iSelect][0];
//        //------設定所選位置之當地圖示-----//
//        image_des= BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);//使用系統水滴
//        VGPS = new LatLng(dLat,dLon);
//        map.addMarker((new MarkerOptions().position(VGPS).title(vtitle).snippet("座標:"+dLat+","+dLon).icon(image_des)));//顯示圖標文字
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS,mapzoom));
//    }
//
//    private void showioc() {
//        //將所有景點位置顯示
//        for(int i=0;i<locations.length;i++){
//            String[] sLocation = locations[i][1].split(",");
//            double dLat = Double.parseDouble(sLocation[0]);//南北緯
//            double dLon = Double.parseDouble(sLocation[1]);//東西經
//            String vtitle = locations[i][0];
//
//            //------設定所選位置之當地圖片-----//
//            switch (icosel){
//                case 0:
//                    image_des=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
//                    break;
//                case 1:
//                    //運用巨集
//                    //drawable裡面要有照片? t01, t02??
//                    String idName = "t" + String.format("%02d", i + 1);
//                    int resID = getResources().getIdentifier(idName, "drawable", getPackageName());
//                    BitmapDescriptor des = BitmapDescriptorFactory.fromResource(resID);// 使用照片
//                    break;
//            }
//            VGPS = new LatLng(dLat,dLon);//更新成欲顯示的地圖座標
//            map.addMarker(new MarkerOptions().position(VGPS).title(vtitle).snippet("座標:"+dLat+","+dLon).icon(image_des));//顯示圖標文字
//        }
//
//    }
//
//
//
////---------------------------------------------------
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        map =googleMap;
//        //mUiSettings=map.getUiSettings();
//
//        //開啟Google Map拖曳功能
//        map.getUiSettings().setScrollGesturesEnabled(true);
////        右下角的導覽及開啟 Google Map功能
//        map.getUiSettings().setMapToolbarEnabled(true);
////        左上角顯示指北針，要兩指旋轉才會出現
//        map.getUiSettings().setCompassEnabled(true);
////        右下角顯示縮放按鈕的放大縮小功能
//        map.getUiSettings().setZoomControlsEnabled(true);
//
////********************************************
//        map.addMarker(new MarkerOptions().position(VGPS).title("中區職訓"));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS,mapzoom));
//        //-----取得定位許可-------API20-----------
//        if(ActivityCompat.checkSelfPermission(this,ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(getApplicationContext(),"GPS定位權限為允許",Toast.LENGTH_LONG).show();
//            // TODO: Consider calling ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //   int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            //----顯示我的位置ICO-------
//        }else{
//            //----顯示我的位置ICO---
//            map.setMyLocationEnabled(true);
//            return;
//        }
//    }
//
////---------提示使用者開啟GPS定位------------------------------
//    private void u_checkgps() {
//        //取得系統服務的LocationManager物件
//        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
//        //檢查是否有啟用GPS
//        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            //顯示對話方塊，啟用GPS
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("定位管理").setMessage("GPS目前狀態是尚未啟用.\n"+"請問你是否現在就設定啟用GPS?").setPositiveButton("啟用", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //使用Intent物件啟動設定程式、來更改GPS設定
//                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    startActivity(i);
//
//                }
//            })
//             .setNegativeButton("不啟用",null).create().show();
//        }
//
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }
//
//    //---LocationListener內建方法----
//    @Override
//    public void onLocationChanged(@NonNull Location location) {
//
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(@NonNull String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(@NonNull String provider) {
//
//    }
//
//    //---------------------------
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.q0500,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        switch (item.getItemId()){
//            case R.id.item1:
//                map.clear();
//                if(icosel<1){
//                    icosel=1; //用照片顯示
//                    showioc();
//                }else
//                    icosel=0; //用水滴顯示
//                showioc();
//                break;
//            case R.id.action_settings:
//                this.finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}//maxend

package tw.tcnrcloud110.quiz;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Q0500_map extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener{
    private GoogleMap map;
    static LatLng VGPS = new LatLng(24.172127, 120.610313);
    int mapzoom = 12;
    private static String[][] locations = {
            {"我的位置", "24.131956762088787, 120.66218417219653"},
            {"中區職訓", "24.172127,120.610313"},
            {"東海大學路思義教堂", "24.179051,120.600610"},
            {"台中公園湖心亭", "24.144671,120.683981"},
            {"台中火車站", "24.136829,120.685011"},
            {"國立科學博物館", "24.1579361,120.6659828"}};

    private static String[] mapType = {
            "街道圖",
            "衛星圖",
            "地形圖",
            "混和圖",
            "開啟路況",
            "關閉路況"};

    //----------
    String TAG = "chris=>";
    private Spinner mSpnLocation,mSpnMapType;
    private double dLat,dLon; //latitude, longitude
    private BitmapDescriptor image_des;//圖標 顯示
    private int icosel=1;  //圖示旗標
    //--------GPS---------------------
    private LocationManager locationManager;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=101;
    //private Location currentlocation;
    private String provider=null; // 提供資料
    long minTime = 5000;// ms
    float minDist = 5.0f;// meter
    private TextView txtOutput,tmsg;

    //-----------------所需要申請的權限數組---------------
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private List<String> permissionsList = new ArrayList<String>();
    //申請權限後的返回碼
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private Marker markerMe;
    private Intent it=new Intent();




    //----------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0500_map);
        checkRequiredPermission(this);     //  檢查SDK版本, 確認是否獲得權限. Map0.1 do not have this
        //------------------設定MapFragment---------------------
        SupportMapFragment mapFragment;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //---------------------------------------
        u_checkgps();// "檢查"GPS開啟沒，提示開啟
        setupViewComponent();
    }

    private void checkRequiredPermission(Q0500_map q0500) {
        for(String permission: permissionsArray){
            if(ContextCompat.checkSelfPermission(q0500, permission)!=PackageManager.PERMISSION_GRANTED){
                permissionsList.add(permission);
            }
        }

        //???
        if(permissionsList.size()!=0){
            ActivityCompat.requestPermissions(q0500,permissionsList.toArray(new String[permissionsList.size()]),REQUEST_CODE_ASK_PERMISSIONS
            );
        }
    }


    private void setupViewComponent() {
        //---------------------------------------
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        //---------------------------------------
        mSpnLocation=(Spinner)this.findViewById(R.id.spnLocation);
        mSpnMapType=(Spinner)this.findViewById(R.id.spnMapType);
        //------------
        txtOutput=(TextView)findViewById(R.id.txtOutput);
        tmsg=(TextView)findViewById(R.id.msg);
        //------------
        icosel=0;//設定圖示初始值, 與map0.1的1不同
        //************
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);

        for(int i=0;i<locations.length;i++)
            adapter.add(locations[i][0]);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnLocation.setAdapter(adapter);

        //指定事件處理物件
        mSpnLocation.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0)
                    setMapLocation();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //----------------
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        for (int i=0;i<mapType.length;i++)
            adapter.add(mapType[i]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnMapType.setAdapter(adapter);
        //---------設定ARGB透明度---------
        //        mSpnMapType.setPopupBackgroundDrawable(new ColorDrawable(0x00FFFFFF)); //全透明
        mSpnMapType.setPopupBackgroundDrawable(new ColorDrawable(0xF2FFFFFF)); //50%透明
        mSpnLocation.setPopupBackgroundDrawable(new ColorDrawable(0xF2FFFFFF)); //50%透明
        //        # ARGB依次代表透明度（alpha）、紅色(red)、綠色(green)、藍色(blue)
//        100% — FF       95% — F2        90% — E6        85% — D9
//        80% — CC        75% — BF        70% — B3        65% — A6
//        60% — 99        55% — 8C        50% — 80        45% — 73
//        40% — 66        35% — 59        30% — 4D        25% — 40
//        20% — 33        15% — 26        10% — 1A         5% — 0D         0% — 00
        //---------------
        mSpnMapType.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);//道路地圖
                        break;
                    case 1:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);//衛星空照地圖
                        break;
                    case 2:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);//地型圖
                        break;
                    case 3:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);//道路地圖混和空照圖
                        break;
                    case 4:
                        map.setTrafficEnabled(true);//開啟路況
                        break;
                    case 5:
                        map.setTrafficEnabled(false);//關閉路況
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setMapLocation() {
        if(map !=null)
            map.clear();//這跟刷新景點一樣嗎?
        showioc();//刷新景點
        int iSelect = mSpnLocation.getSelectedItemPosition();
        String[] sLocation = locations[iSelect][1].split(",");
        dLat = Double.parseDouble(sLocation[0]);//南北緯
        dLon = Double.parseDouble(sLocation[1]);//東西經
        String vtitle = locations[iSelect][0];
        //------設定所選位置之當地圖示-----//
        image_des= BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);//使用系統水滴
        VGPS = new LatLng(dLat,dLon);
        map.addMarker((new MarkerOptions().position(VGPS).title(vtitle).snippet("座標:"+dLat+","+dLon).icon(image_des)));//顯示圖標文字
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS,mapzoom));
    }

    private void showioc() {
        if(map !=null)
            map.clear();//這跟刷新景點一樣嗎?
        //將所有景點位置顯示
        for(int i=1;i<locations.length;i++){ //i=1，不顯示0
            String[] sLocation = locations[i][1].split(",");
            double dLat = Double.parseDouble(sLocation[0]);//南北緯
            double dLon = Double.parseDouble(sLocation[1]);//東西經
            String vtitle = locations[i][0];

            //------設定所選位置之當地圖片-----//
            switch (icosel){
                case 0:
                    image_des=BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
                    break;
                case 1:
                    //運用巨集
                    //drawable裡面要有照片? t01, t02??
                    String idName = "t" + String.format("%02d", i + 1);
                    int resID = getResources().getIdentifier(idName, "drawable", getPackageName());
                    BitmapDescriptor des = BitmapDescriptorFactory.fromResource(resID);// 使用照片
                    break;
            }
            VGPS = new LatLng(dLat,dLon);//更新成欲顯示的地圖座標
            map.addMarker(new MarkerOptions().position(VGPS).title(vtitle).snippet("座標:"+dLat+","+dLon).icon(image_des));//顯示圖標文字
        }

    }



    //---------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map =googleMap;
        //mUiSettings=map.getUiSettings();

        //開啟Google Map拖曳功能
        map.getUiSettings().setScrollGesturesEnabled(true);
//        右下角的導覽及開啟 Google Map功能
        map.getUiSettings().setMapToolbarEnabled(true);
//        左上角顯示指北針，要兩指旋轉才會出現
        map.getUiSettings().setCompassEnabled(true);
//        右下角顯示縮放按鈕的放大縮小功能
        map.getUiSettings().setZoomControlsEnabled(true);

//********************************************
        map.addMarker(new MarkerOptions().position(VGPS).title("中區職訓"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(VGPS,mapzoom));
        //-----取得定位許可-------API20-----------
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"GPS定位權限為允許",Toast.LENGTH_LONG).show();

            //----顯示我的位置ICO-------
            map.setMyLocationEnabled(true);
        }else{

            Toast.makeText(getApplicationContext(), "GPS定位權限未允許", Toast.LENGTH_LONG).show();
        }
    }

    //---------"提示"使用者開啟GPS定位------------------------------
    private void u_checkgps() {
        //取得系統服務的LocationManager物件
        locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        //檢查是否有啟用GPS
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //顯示對話方塊，啟用GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("定位管理").setMessage("GPS目前狀態是尚未啟用.\n"+"請問你是否現在就設定啟用GPS?").setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //使用Intent物件啟動設定程式、來更改GPS設定
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);

                }
            })
                    .setNegativeButton("不啟用",null).create().show();
        }

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    //---------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0500_map,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){  //(item.getItemId())
            case R.id.item1:
                map.clear();
                if(icosel<1){
                    icosel=1; //用照片顯示
                    showioc();
                }else
                    icosel=0; //用水滴顯示
                showioc();
                break;
            case R.id.action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //-----------GPS------------
    //ononMyLocationButtonClick 位置變更狀態監視/
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getApplicationContext(), "返回GPS目前位置", Toast.LENGTH_LONG).show();
        return true;

    }

    //檢查GPS 是否開啟  和u_checkup有點不同
    private boolean initLocationProvider(){
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            provider=LocationManager.GPS_PROVIDER;
            return true;
        }
        return false;
    }

    private  void nowaddress(){
        //取得上次已知的位置
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

            Location location = locationManager.getLastKnownLocation(provider);
            updateWithNewLocation(location);
            return;
        }

// 監聽 GPS Listener----------------------------------
// long minTime = 5000;// ms
// float minDist = 5.0f;// meter
//---網路和GPS來取得定位，因為GPS精準度比網路來的更好，所以先使用網路定位、後續再用GPS定位，如果兩者皆無開啟，則跳無法定位的錯誤訊息

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location location=null;
        if(!(isGPSEnabled || isNetworkEnabled)){
            tmsg.setText("GPS未開啟");
        }else {
            if(isNetworkEnabled){//
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDist,locationListener);
                location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                tmsg.setText("使用網路GPS");
            }
        }

    }

    private void updateWithNewLocation(Location location) {
        String where="";
        if(location!=null){
            double lng = location.getLongitude();// 經度
            double lat = location.getLatitude();// 緯度
            float speed = location.getSpeed();// 速度
            long time = location.getTime();// 時間
            String timeString = getTimeString(time);
            where = "經度: " + lng + "\n緯度: " + lat + "\n速度: " + speed + "\n時間: " + timeString + "\nProvider: "
                    + provider;
            // 標記"我的位置"
            //showMarkerMe(lat, lng);
            //cameraFocusOnMe(lat, lng);

        }else {

        }
        // 位置改變顯示
        txtOutput.setText(where);

    }

    private String getTimeString(long timeInMilliseconds) {  //long time
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(timeInMilliseconds);
    }

    private void cameraFocusOnMe(double lat,double lng){
        CameraPosition camPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(map.getCameraPosition().zoom)
                .build();
        /* 移動地圖鏡頭 */
        map.animateCamera(CameraUpdateFactory.newCameraPosition(camPosition));
        tmsg.setText("目前Zoom:" + map.getCameraPosition().zoom);

    }

    private void showMarkerMe(double lat, double lng) {
        if(markerMe!=null)
            markerMe.remove();

        //--------------
        int resID = getResources().getIdentifier("t01", "drawable", getPackageName());
        //---------------
        dLat = lat; // 南北緯
        dLon = lng; // 東西經
        String vtitle = "GPS位置:";
        String vsnippet = "座標:" + String.valueOf(dLat) + "," + String.valueOf(dLon);
        VGPS=new LatLng(lat,lng); //更新成欲顯示的地圖座標
        MarkerOptions markerOpt=new MarkerOptions();
        markerOpt.position(new LatLng(lat,lng));
        markerOpt.title(vtitle);
        markerOpt.snippet(vsnippet);
        if(icosel==0){
            markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        }else {
            image_des=BitmapDescriptorFactory.fromResource(resID);//使用照片
            markerOpt.icon(image_des);

        }
        markerMe = map.addMarker(markerOpt);
        locations[0][1] = lat + "," + lng;
    }

    //---生命週期

    @Override
    protected void onStart() {
        super.onStart();
        if (initLocationProvider()) {
            nowaddress();
        } else {
            txtOutput.setText("GPS未開啟,請先開啟定位！");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    /*** 位置變更狀態監視*/
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            updateWithNewLocation(location);
            tmsg.setText("目前Zoom:"+map.getCameraPosition().zoom);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            updateWithNewLocation(null);
            tmsg.setText("GPS close");
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            tmsg.setText("GPS Enabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status){
                case LocationProvider.OUT_OF_SERVICE:
                    tmsg.setText("Out of Service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    tmsg.setText("Temporarily Unavailable");
                    break;
                case LocationProvider.AVAILABLE:
                    tmsg.setText("Available");
                    break;

            }
        }
    };
}//maxend