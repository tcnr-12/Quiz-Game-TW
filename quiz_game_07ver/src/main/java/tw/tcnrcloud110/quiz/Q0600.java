package tw.tcnrcloud110.quiz;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

public class Q0600 extends AppCompatActivity implements View.OnClickListener {

    private Intent intent = new Intent();
    private Button b002,b003,btn;
    private TextView tv01,tv02,tv03;
    private ImageButton ibtn01;
    private TableRow tab01,tab02;
    private Spinner s001;
    private Menu menu;
    //----------------------------
    private String msg= null;
    private String sqlctl;
    private String s_category,tid,s_id;
    private int rowsAffected;
    private int view01=0;
    private int view02=0;
    String TAG = "tcnr12=";
    //------------------------Google 會員登入---------------
    private GoogleSignInAccount ACC = null;
    //------------------------DataBase----------------------
    private DbHelper dbHper;
    private static final String DB_FILE = "QuizeGame.db";
    private static final int DB_version = 1;
    private ArrayList<String> recSet_Q0200,recSet_Q0600, recSet_Q0601;
    private String[] id00;
    // ----------------------定時更新------------------------
    private Long startTime;
    private Handler handler = new Handler();

    int autotime = 10;// 要幾秒的時間 更新匯入MySQL資料
    //----------------
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    int update_time = 0;
    //----------------
    private boolean runAuto_flag = false;  //Runable updateTime 狀態


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//---------------------使用暫存堆疊，需要用此方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0600);
        initDB();
        setupViewComponent();
    }

    private void enableStrictMode(Context context) {
        //-------------抓取遠端資料庫設定執行續---------------
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

    private void setupViewComponent() {
        //====================設定class標題===================
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);

        //====================宣告監聽======================
        b002 = (Button) findViewById(R.id.q0600_b002);
        b003 = (Button) findViewById(R.id.q0600_b003);
        s001 = (Spinner)findViewById(R.id.q0600_s001);

            //===================設定 spinner item 選項=============
            ArrayAdapter<CharSequence> adapSpinner = ArrayAdapter
                    .createFromResource(this, R.array.q0600_a001, android.R.layout.simple_spinner_item);
            adapSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s001.setAdapter(adapSpinner);
            //------------------------------設監聽
        s001.setOnItemSelectedListener(spinner_item);//---準備 Listener a001Lis 需再設定 Listener

        b002.setOnClickListener(this);
        b003.setOnClickListener(this);

        //====================抓取螢幕尺寸====================
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        s001.getLayoutParams().width=displayMetrics.widthPixels/20*12;

        //====================設執行緒=======================
        startTime = System.currentTimeMillis();// 取得目前時間
        if ( runAuto_flag = false){
            handler.postDelayed(updateTimer, 100);  // 設定Delay的時間
            runAuto_flag = true;
        }
    }

    private Spinner.OnItemSelectedListener spinner_item = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            s_category = parent.getSelectedItem().toString();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
//==========================設定執行續========================
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            Long spentTime = System.currentTimeMillis() - startTime;
            String hours = String.format("%02d", (spentTime / 1000) / 60 / 60);  // 計算目前已過分鐘數
            String minius = String.format("%02d", ((spentTime / 1000) / 60) % 60);  // 計算目前已過分鐘數
            String seconds = String.format("%02d", (spentTime / 1000) % 60);          // 計算目前已過秒數
            handler.postDelayed(this, autotime * 1000); // 真正延遲的時間
            // -------執行匯入MySQL
            dbmysql();
            recSet_Q0600 = dbHper.getRecSet_Q0600();
            showRec();
            //----------------
            ++update_time;
            //------ 宣告鈴聲 ---------------------------
            //ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100); // 100=max
            //toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 500);
            //toneG.release();
            // --------------------------------------------------------
        }
    };

    // ========================讀取MySQL 資料========================
    private void dbmysql() {
        sqlctl = "SELECT * FROM Q0600";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0600_DBConnector.executeQuery_Q0600(nameValuePairs);

            JSONArray jsonArray = new JSONArray(result);
            // ------
            if (jsonArray.length() > 0) { // ------------------------------MySQL 連結成功有資料
                int rowsAffected = dbHper.clearRec_Q0600();// --------匯入前,刪除所有SQLite資料
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value == null) {
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value.toString()); // 動態找出有幾個欄位
                        // -------------------------------------------------------------------
                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite----------------------
                    long rowID = dbHper.insertRec_m_Q0600(newRow);
                }
                    //Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    //===============================SQLite==========================
    private void initDB() {
        if (dbHper == null){
            dbHper = new DbHelper(this, DB_FILE, null, DB_version);
        }
        recSet_Q0600 = dbHper.getRecSet_Q0600();
    }

    private void check_SignIn() {
        // Check if the user is already signed in and all required scopes are granted
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(Scopes.DRIVE_APPFOLDER))) {
            ACC =account;
        } else {
            ACC = null;
        }
    }

    //===========================渲染畫面===========================
    private void showRec(){
        if (recSet_Q0600.size() != 0)   {
            //------------------------------------------------------------------物件隱藏
            LinearLayout mlay02 = (LinearLayout) findViewById(R.id.q0600_lay01);

            mlay02.removeAllViews();//---------------------------------------清空layout

            TableRow objt001 = (TableRow)findViewById(R.id.q0600_tab02);//--------取出參考物件
            objt001.setVisibility(View.GONE);                                                      // -------設定參考物件隱藏不佔空間
            TextView objt002 = (TextView) findViewById(R.id.q0600_t002);//分類
            objt002.setVisibility(View.GONE);
            TextView objt003 = (TextView) findViewById(R.id.q0600_b001);//項目
            objt003.setVisibility(View.GONE);
            TableRow objt004 = (TableRow) findViewById(R.id.q0600_tab03);
            objt004.setVisibility(View.GONE);
            TextView objt005 = (TextView) findViewById(R.id.q0600_t003);//時間
            objt005.setVisibility(View.GONE);
            TextView objt006 = (TextView) findViewById(R.id.q0600_t004);//回應
            objt006.setVisibility(View.GONE);
            ImageButton objt007 = (ImageButton) findViewById(R.id.q0600_img002);//刪除
            objt007.setVisibility(View.GONE);
            //=======================巨集========================
            try {
                for (int i = 1; i <=recSet_Q0600.size(); i++) {
                    // -----------------------------------------------產生新的TextView layout
                    tab01 = new TableRow(this);
                    tv01 = new TextView(this);
                    btn = new Button(this);
                    tab02 = new TableRow(this);
                    tv02 = new TextView(this);
                    tv03 = new TextView(this);
                    ibtn01 = new ImageButton(this);
                    // -----------------------------------------------設定新TextView的ID
                    tab01.setId(i);
                    tv01.setId(i);
                    btn.setId(i);
                    tab02.setId(i);
                    tv02.setId(i);
                    tv03.setId(i);
                    ibtn01.setId(i);
                    //-----------------------------------------------取得陣列內容
                    id00 = recSet_Q0600.get(i-1).split("#");
                    String id01 = id00[1];//分類
                    String id02 = id00[2];//標題
                    String id06 = id00[3];//內文
                    String id05 = id00[4];//會員
                    //******************時區轉換***************
                    String timeStr = id00[7]; // 主機時間
                    SimpleDateFormat timeZone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    timeZone.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date date = timeZone.parse(timeStr);  // 將字符串時間按時間解析成Date對象

                    SimpleDateFormat timeZone_TW = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    timeZone_TW.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                    String date_TW = timeZone_TW.format(date);
                    //******************************************
                    String id03 =date_TW+" 發文";//時間
                    String id04 = id00[9]+" 回應";//回應

                    //--------------------------設定屬性
                    tv01.setText(id01);
                    tv01.setTextColor(objt002.getCurrentTextColor());                                               // 以objt002字體顏色為依據
                    tv01.setGravity(objt002.getGravity());                                                                     // 以objt002字體靠右靠左
                    tv01.setTextSize(TypedValue.COMPLEX_UNIT_PX, objt002.getTextSize());      // 以objt002設定文字大小
                    tv01.setBackground(objt002.getBackground());                                                   // 以objt002設定背景
                    tv01.setLayoutParams(objt002.getLayoutParams());                                             //以objt002設定Layout
                    //--------------------
                    btn.setText(id02);
                    btn.setTextColor(objt003.getCurrentTextColor());
                    btn.setGravity(objt003.getGravity());
                    btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, objt003.getTextSize());
                    btn.setBackground(objt003.getBackground());
                    btn.setLayoutParams(objt003.getLayoutParams());
                    //--------------------
                    tv02.setText(id03);
                    tv02.setTextColor(objt005.getCurrentTextColor());
                    tv02.setGravity(objt005.getGravity());
                    tv02.setTextSize(TypedValue.COMPLEX_UNIT_PX, objt005.getTextSize());
                    tv02.setBackground(objt005.getBackground());
                    tv02.setLayoutParams(objt005.getLayoutParams());
                    //--------------------
                    tv03.setText(id04);
                    tv03.setTextColor(objt006.getCurrentTextColor());
                    tv03.setGravity(objt006.getGravity());
                    tv03.setTextSize(TypedValue.COMPLEX_UNIT_PX, objt006.getTextSize());
                    tv03.setBackground(objt006.getBackground());
                    tv03.setLayoutParams(objt006.getLayoutParams());
                    //--------------------
                    tab01.setLayoutParams(objt001.getLayoutParams());
                    tab02.setLayoutParams(objt004.getLayoutParams());
                    tab02.setGravity(Gravity.RIGHT);
                    //--------------------
                    ibtn01.setBackground(objt007.getBackground());
                    ibtn01.setLayoutParams(objt007.getLayoutParams());

                    mlay02.addView(tab01);
                    mlay02.addView(tab02);
                    tab01.addView(tv01);
                    tab01.addView(btn);
                    tab02.addView(tv02);
                    tab02.addView(tv03);
                    tab02.addView(ibtn01);

                    btn.setOnClickListener(clkOn);
                    ibtn01.setOnClickListener(delOn);

                   switch (view01){
                       case 0://------------------------------取消編輯、預設值
                           ibtn01.setVisibility(View.GONE);
                           b002.setVisibility(View.VISIBLE);
                           b003.setClickable(true);
                           btn.setClickable(true);
                           s001.setClickable(true);
                           break;
                       case 1://------------------------------使用編輯
                           ibtn01.setVisibility(View.VISIBLE);
                           b002.setVisibility(View.GONE);
                           b003.setClickable(false);
                           btn.setClickable(false);
                           s001.setClickable(false);
                           break;
                   }
                }
            } catch (Exception e) {
                return;
            }
        } else {
                //Toast.makeText(getApplicationContext(), "無資料", Toast.LENGTH_SHORT).show();
        }
    }

//=============================監聽============================
    //--------下一層
    private TextView.OnClickListener clkOn = new TextView.OnClickListener() {
        @Override
        public void onClick(View v) {
            int ii = (v.getId()); // 下層巨集
            id00 = recSet_Q0600.get(ii-1).split("#");
            String forum_id = id00[0];
            String sub = id00[1];
            String item = id00[2];
            String item_text = id00[3];
            String firstname = id00[4];
            String email = id00[5];
            //------------------時區轉換--------------------
            String timeStr = id00[7]; // 主機時間
            SimpleDateFormat timeZone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            timeZone.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = null;  // 將字符串時間按時間解析成Date對象
            try {
                date = timeZone.parse(timeStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat timeZone_TW = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            timeZone_TW.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
            String date_TW = timeZone_TW.format(date);
            //-----------------------------------------------
            String settime = date_TW;
            String respond = id00[9];
            // ------------------------------------------
            intent.putExtra("sel", ii);
            intent.putExtra("forum_id", forum_id);
            intent.putExtra("sub", sub);
            intent.putExtra("item", item);
            intent.putExtra("item_text", item_text);
            intent.putExtra("firstname", firstname);
            intent.putExtra("email", email);
            intent.putExtra("settime", settime);
            intent.putExtra("respond", respond);

            intent.setClass(Q0600.this, Q0601.class);
            startActivity(intent);
        }
    };
    //-------對話盒
    private View.OnClickListener delOn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int iii = (v.getId()); // 下層巨集
            id00 = recSet_Q0600.get(iii-1).split("#");

            MyAlertDialog myAltDlg = new MyAlertDialog(Q0600.this);
            myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
            myAltDlg.setTitle("刪除討論");
            myAltDlg.setMessage("討論刪除無法復原\n確定要刪除嗎?");
            myAltDlg.setCancelable(false);
            myAltDlg.setIcon(android.R.drawable.ic_delete);
            myAltDlg.setButton(BUTTON_POSITIVE, "確定刪除", del_choose);
            myAltDlg.setButton(BUTTON_NEGATIVE, "取消刪除", del_choose);
            myAltDlg.show();
        }
    };
    //---------是否刪除
    private  DialogInterface.OnClickListener del_choose = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:  // 確定刪除
                    tid = id00[0];
                    rowsAffected = (int) dbHper.deleteRec_Q0600(tid);  // delete record
                    // ---------------------------
                    mysql_del_Q0600();// 執行MySQL刪除
                    dbmysql();
                    // ---------------------------
                    recSet_Q0601 = dbHper.getRecSet_query_Q0601(tid);
                    for (int i = 1; i <=recSet_Q0601.size(); i++){
                        String[] q61 = recSet_Q0601.get(i-1).split("#");
                        String f_id = q61[0];
                        mysql_del_Q0601(f_id);// 執行MySQL刪除
                    }
                    //----------------------------
                    if (rowsAffected == -1) {
                        msg = "討論不存在, 無法刪除 !";
                    } else if (rowsAffected == 0) {
                        msg = "討論不存在, 無法刪除 !";
                    } else {
                        msg = "討論已刪除 !";
                        recSet_Q0600 = dbHper.getRecSet_Q0600();
                        view01 = 0;
                        showRec();
                        menu.setGroupVisible(R.id.q0600_group1, true);
                        menu.setGroupVisible(R.id.q0600_group2, false);
                    }
                    Toast.makeText(Q0600.this, msg, Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_NEGATIVE:
                    msg = "放棄刪除 !";
                    Toast.makeText(Q0600.this, msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //--------新增、spinner搜尋
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.q0600_b002:
                if (ACC != null){
                    intent.putExtra("class_title", getString(R.string.q0600_t003));
                    intent.setClass(Q0600.this, Q0602.class);
                    startActivity(intent);
                }else{
                    new AlertDialog.Builder(Q0600.this)
                            .setTitle(getString(R.string.q0100_dialog_title))
                            .setMessage(getString(R.string.q0100_dialog_message))
                            .setCancelable(false)
                            .setIcon(android.R.drawable.btn_radio)
                            .setPositiveButton(getString(R.string.q0100_dialog_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                break;
            case R.id.q0600_b003:
                String e_category = s_category;
                msg = null;
                recSet_Q0600 = dbHper.getRecSet_query_Q0600(e_category);
                if (e_category.equals("最新")){
                    handler.post(updateTimer);//-----開啟執行續
                    runAuto_flag = true;
                    view02 = 0;
                }else{
                    handler.removeCallbacks(updateTimer);//-----關閉執行續
                    runAuto_flag = false;
                    Toast.makeText(getApplicationContext(), "共 " + recSet_Q0600.size() + " 筆討論", Toast.LENGTH_SHORT).show();
                    view02 = 1;
                }
                showRec();
                break;
        }
    }

    //===============================刪除MySQL==============================
    private void mysql_del_Q0600() {
        s_id = tid;
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_id);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------
        String result = Q0600_DBConnector.executeDelet_Q0600(nameValuePairs);   //執行刪除
        //-----------------------------------------------
    }

    private void mysql_del_Q0601(String f_id) {
        ArrayList<String> nameValuePairs = new ArrayList<>();
        //nameValuePairs.add(sqlctl);
        nameValuePairs.add(f_id);
        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------
        String result = Q0601_R_DBConnector.executeDelet(nameValuePairs);   //執行刪除
        //-----------------------------------------------
    }

    //===========================生命週期==========================

    @Override
    protected void onStart() {
        super.onStart();
        check_SignIn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(updateTimer);
        runAuto_flag = false;
        //---------------------
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimer);
        runAuto_flag = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHper == null){
            dbHper = new DbHelper(this, DB_FILE, null, DB_version);
        }
        //-------------------------
        if (runAuto_flag == false) {
            if (view02 == 0){
                handler.post(updateTimer);  //開啟執行續
                runAuto_flag = true;
            }
            if (view01 == 1){
                view01 = 0;
                recSet_Q0600 = dbHper.getRecSet_Q0600();
                showRec();
                //----------------------
                handler.post(updateTimer);//-----開啟執行續
                runAuto_flag = true;
                menu.setGroupVisible(R.id.q0600_group1, true);
                menu.setGroupVisible(R.id.q0600_group2, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    //===========================Menu==========================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0600_main, menu);
        this.menu = menu;
            menu.setGroupVisible(R.id.q0600_group1, true);
            menu.setGroupVisible(R.id.q0600_group2, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.q0600_m001://-----啟用編輯
                if (ACC != null){

                        if (runAuto_flag == true){
                            handler.removeCallbacks(updateTimer);//-----關閉執行續
                            runAuto_flag = false;
                        }
                        //---------------------
                        String user = ACC.getEmail();
                        msg = null;
                        recSet_Q0600 = dbHper.getRecSet_user_Q0600(user);
                        Toast.makeText(getApplicationContext(), "共 " + recSet_Q0600.size()
                                + " 筆討論", Toast.LENGTH_SHORT).show();
                        //---------------------
                        view01 = 1;
                        showRec();
                    menu.setGroupVisible(R.id.q0600_group1, false);
                    menu.setGroupVisible(R.id.q0600_group2, true);
                }else {
                    new AlertDialog.Builder(Q0600.this)
                            .setTitle(getString(R.string.q0100_dialog_title))
                            .setMessage(getString(R.string.q0100_dialog_message))
                            .setCancelable(false)
                            .setIcon(android.R.drawable.btn_radio)
                            .setPositiveButton(getString(R.string.q0100_dialog_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                break;
            case R.id.q0600_m002://-----取消編輯
                if (runAuto_flag == false){
                    handler.post(updateTimer);//-----開啟執行續
                    runAuto_flag = true;
                }
                //----------------------
                view01 = 0;
                showRec();
                menu.setGroupVisible(R.id.q0600_group1, true);
                menu.setGroupVisible(R.id.q0600_group2, false);
                break;
            case R.id.q0600_m003:
                intent.putExtra("class_title",getString(R.string.q0100_m001));
                intent.setClass( Q0600.this, Q0200.class);
                startActivity(intent);
                break;
            case R.id.q0600_action_back:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}