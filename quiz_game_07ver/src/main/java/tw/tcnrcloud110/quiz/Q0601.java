package tw.tcnrcloud110.quiz;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

public class Q0601 extends AppCompatActivity implements View.OnClickListener{

    private Intent intent = new Intent();
    private Button b001;
    private ImageButton ibtn01,img002;
    private EditText e001;
    private TableRow tab01,tab02;
    private TextView tv01, tv02, tv03;
    private Menu menu;
    //---------------------------------
    private String mode00, mode01, mode02, mode03, mode04, mode05, mode06,mode07;
    private String e_forum_id, e_content, e_firstname, e_email, e_userimage;
    private String tid, sqlctl, s_id;
    private String msg= null;
    private int rowsAffected;
    String TAG = "tcnr12=";
    //------------------------Google 會員登入---------------
    private GoogleSignInAccount ACC;
    //------------------------------------------------------------------DataBase
    private DbHelper dbHper;
    private static final String DB_FILE = "QuizeGame.db";
    private static final int DB_version = 1;
    private ArrayList<String> recSet_Q0601, recSet_Q0200;
    private String[] id00;
    private int view01 = 0;
    // ----------------------定時更新------------------------
    private Long startTime;
    private Handler handler = new Handler();

    int autotime = 10;// 要幾秒的時間 更新匯入MySQL資料
    //----------------
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    int update_time = 0;
    private String str;
    //----------------
    private boolean runAuto_flag = false;  //Runable updateTime 狀態


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(this);//---------------------使用暫存堆疊，需要用此方法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0601);
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
        // -----------------------------------------------------------------設定class標題
        Intent intent = this.getIntent();
        mode00 = intent.getStringExtra("forum_id");
        mode01 = intent.getStringExtra("sub");
        mode02 = intent.getStringExtra("item");
        mode03 = intent.getStringExtra("settime");
        mode04 = intent.getStringExtra("respond");
        mode05 = intent.getStringExtra("firstname");
        mode06 = intent.getStringExtra("item_text");
        mode07 = intent.getStringExtra("email");
        this.setTitle(mode01);
        //------------------------------------------------------------------宣告
        b001 = (Button) findViewById(R.id.q0601_b001);
        e001 = (EditText)findViewById(R.id.q0601_e001);
        img002 = (ImageButton)findViewById(R.id.q0601_img002);

        img002.setOnClickListener(updataOn);
        b001.setOnClickListener(this);

        //------------------------------------------------------------------抓取螢幕尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        e001.getLayoutParams().width = displayMetrics.widthPixels / 20 * 14;
        b001.getLayoutParams().width = displayMetrics.widthPixels / 20 * 5;
        //====================設執行緒=======================
        startTime = System.currentTimeMillis();// 取得目前時間
        if ( runAuto_flag = false) {
            handler.postDelayed(updateTimer, 500);  // 設定Delay的時間
            runAuto_flag = true;
        }

        showRec();
    }

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
            dbmysql_Q0601();
            recSet_Q0601 = dbHper.getRecSet_Q0601();
            showRec();
            //----------------
            ++update_time;
            //------ 宣告鈴聲 ---------------------------
//            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100); // 100=max
//            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 500);
//            toneG.release();
            // --------------------------------------------------------
        }
    };

    // ===========================讀取MySQL 資料========================
    private void dbmysql_Q0601() {
        sqlctl = "SELECT * FROM Q0601 ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0601_R_DBConnector.executeQuery_R(nameValuePairs);

            JSONArray jsonArray = new JSONArray(result);
            // ------
            if (jsonArray.length() > 0) { // ------------------------------MySQL 連結成功有資料
                int rowsAffected = dbHper.clearRec_Q0601();// ------------匯入前,刪除所有SQLite資料
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
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_m_Q0601(newRow);
                }
                    //Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void initDB(){
        if (dbHper == null){
            dbHper = new DbHelper(this, DB_FILE, null,DB_version);
        }
        recSet_Q0601 = dbHper.getRecSet_Q0601();
        recSet_Q0200 = dbHper.getRecSet_Q0200();
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

    private void showRec(){
        switch (view01) {
            case 0://------------------------------取消編輯、預設值
                    if (ACC != null){
                        e001.setVisibility(View.VISIBLE);
                        b001.setVisibility(View.VISIBLE);
                        img002.setVisibility(View.GONE);
                    }else{
                        e001.setVisibility(View.GONE);
                        b001.setVisibility(View.GONE);
                        img002.setVisibility(View.GONE);
                    }
                break;
            case 1://------------------------------使用編輯
                String user = ACC.getEmail();
                    if(mode07.equals(user)){
                        img002.setVisibility(View.VISIBLE);
                    }else{
                        img002.setVisibility(View.GONE);
                    }
                e001.setVisibility(View.GONE);
                b001.setVisibility(View.GONE);
                break;
        }
        //---------------------------------------------------------------------物件隱藏
        LinearLayout mlay02 = (LinearLayout) findViewById(R.id.q0601_lay03);

        mlay02.removeAllViews();//-------清空layput

        TextView objt001 = (TextView) findViewById(R.id.q0601_t001);
        objt001.setText(mode01);
        TextView objt002 = (TextView) findViewById(R.id.q0601_t002);
        objt002.setText(mode02);
        TextView objt003 = (TextView) findViewById(R.id.q0601_t003);
        objt003.setText(mode03+" 發文");
        TextView objt004 = (TextView) findViewById(R.id.q0601_t004);
        objt004.setText(mode04+" 回應");
        TextView objt005 = (TextView) findViewById(R.id.q0601_t005);
        objt005.setText(mode05);
        TextView objt006 = (TextView) findViewById(R.id.q0601_t006);
        objt006.setText(mode06);
        TableRow objt007 = (TableRow) findViewById(R.id.q0601_tab04);
        objt007.setVisibility(View.GONE);
        TextView objt008 = (TextView) findViewById(R.id.q0601_t008);
        objt008.setVisibility(View.GONE);
        TextView objt009 = (TextView) findViewById(R.id.q0601_t009);
        objt009.setVisibility(View.GONE);
        TextView objt010 = (TextView) findViewById(R.id.q0601_t010);
        objt010.setVisibility(View.GONE);
        TableRow objt011 = (TableRow) findViewById(R.id.q0601_tab05);
        objt011.setVisibility(View.GONE);
        ImageButton objt012 = (ImageButton) findViewById(R.id.q0601_img001);
        objt012.setVisibility(View.GONE);

            //======================巨集=========================
            try {
                for (int i = 1; i <=recSet_Q0601.size(); i++) {

                    id00 = recSet_Q0601.get(i-1).split("#");
                    String id04 = id00[1];

                    if (id04.equals(mode00)){
                        // -----------------------------------------------產生新的TextView layout
                        tab01 = new TableRow(this);
                        tv01 = new TextView(this);
                        tv02 = new TextView(this);
                        tv03 = new TextView(this);
                        tab02 = new TableRow(this);
                        ibtn01 = new ImageButton(this);
                        // -----------------------------------------------設定新TextView的ID
                        tab01.setId(i);
                        tv01.setId(i);
                        tv02.setId(i);
                        tv03.setId(i);
                        tab02.setId(i);
                        ibtn01.setId(i);
                        //------------------------------------------------
                        id00 = recSet_Q0601.get(i-1).split("#");
                        String id01 = id00[3];
                        String id02 = id00[2];
                        //------------------時區轉換--------------------
                        String timeStr = id00[6]; // 主機時間
                        SimpleDateFormat timeZone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        timeZone.setTimeZone(TimeZone.getTimeZone("GMT"));
                        Date date = timeZone.parse(timeStr);  // 將字符串時間按時間解析成Date對象

                        SimpleDateFormat timeZone_TW = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        timeZone_TW.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                        String date_TW = timeZone_TW.format(date);
                        //-----------------------------------------------
                        String id03 = date_TW+" 發文";
                        //--------------------------設定屬性
                        tv01.setText(id01);
                        tv01.setTextColor(objt008.getCurrentTextColor());                                               // 以objt002字體顏色為依據
                        tv01.setGravity(objt008.getGravity());                                                                        // 以objt002字體靠右靠左
                        tv01.setTextSize(TypedValue.COMPLEX_UNIT_PX, objt008.getTextSize());  // 以objt002設定文字大小
                        tv01.setBackground(objt008.getBackground());                                                   // 以objt002設定背景
                        tv01.setLayoutParams(objt008.getLayoutParams());                                          //以objt002設定Layout
                        //--------------------
                        tv02.setText(id02);
                        tv02.setTextColor(objt009.getCurrentTextColor());
                        tv02.setGravity(objt009.getGravity());
                        tv02.setTextSize(TypedValue.COMPLEX_UNIT_PX, objt009.getTextSize());
                        tv02.setBackground(objt009.getBackground());
                        tv02.setLayoutParams(objt009.getLayoutParams());
                        //--------------------
                        tv03.setText(id03);
                        tv03.setTextColor(objt010.getCurrentTextColor());
                        tv03.setGravity(objt010.getGravity());
                        tv03.setTextSize(TypedValue.COMPLEX_UNIT_PX, objt010.getTextSize());
                        tv03.setBackground(objt010.getBackground());
                        tv03.setLayoutParams(objt010.getLayoutParams());
                        //--------------------
                        tab01.setLayoutParams(objt007.getLayoutParams());
                        tab02.setLayoutParams(objt011.getLayoutParams());
                        tab02.setGravity(Gravity.RIGHT);

                        ibtn01.setLayoutParams(objt012.getLayoutParams());
                        ibtn01.setBackground(objt012.getBackground());

                        mlay02.addView(tab01);
                        mlay02.addView(tab02);
                        tab01.addView(tv01);
                        tab01.addView(tv02);
                        tab02.addView(tv03);
                        tab02.addView(ibtn01);

                        ibtn01.setOnClickListener(delOn_R);
                        switch (view01) {
                            case 0://------------------------------取消編輯、預設值
                                ibtn01.setVisibility(View.GONE);
                                break;
                            case 1://------------------------------使用編輯
                                ibtn01.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                return;
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.q0601_b001:
                String user = ACC.getEmail();
                String name = ACC.getGivenName();
                String img = ACC.getPhotoUrl().toString().trim();
                e_forum_id = mode00;
                e_content = e001.getText().toString().trim();
                e_firstname = name;
                e_email = user;
                e_userimage = img;
                //----------------------檢查輸入欄位是否空白
                if (e_content.equals("")){
                    Toast.makeText(getApplicationContext(), "資料空白無法新增 !", Toast.LENGTH_SHORT).show();
                    return;
                }
                String msg = null;
                //--------真正執行SQL---------
                long rowID = dbHper.insertRec_Q0601(e_forum_id, e_content, e_firstname, e_email, e_userimage);
                //--------執行MySQL-----------
                mysql_insert_Q0601();

                if (rowID != -1) {
                    msg = "新增成功 !";
                    e001.setText("");
                } else {
                    msg = "新增失敗 !";
                }
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                //-------------------------------------
                dbmysql_Q0601();
                //-------------------------------------
                ArrayList<String> count = dbHper.getRecSet_query_Q0601(mode00);
                int cont = count.size();
                String cont_s = Integer.toString(cont);
                mysql_count(cont_s);

                recSet_Q0601 = dbHper.getRecSet_Q0601();
                showRec();
                //-------------------------------------
                break;
        }
    }



    //-------留言刪除對話盒
    private View.OnClickListener delOn_R = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int iii = (v.getId()); // 下層巨集
            id00 = recSet_Q0601.get(iii-1).split("#");

            MyAlertDialog myAltDlg = new MyAlertDialog(Q0601.this);
            myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
            myAltDlg.setTitle("刪除留言");
            myAltDlg.setMessage("留言刪除無法復原\n確定要刪除嗎?");
            myAltDlg.setCancelable(false);
            myAltDlg.setIcon(android.R.drawable.ic_delete);
            myAltDlg.setButton(BUTTON_POSITIVE, "確定刪除", del_choose_R);
            myAltDlg.setButton(BUTTON_NEGATIVE, "取消刪除", del_choose_R);
            myAltDlg.show();
        }
    };
    //---------是否刪除
    private  DialogInterface.OnClickListener del_choose_R = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:  // 確定刪除
                    tid = id00[0];
                    rowsAffected = (int) dbHper.deleteRec_Q0601(tid);  // delete record
                    // ---------------------------
                    mysql_del_Q0601();// 執行MySQL刪除
                    dbmysql_Q0601();
                    // ---------------------------
                    ArrayList<String> count = dbHper.getRecSet_query_Q0601(mode00);
                    int cont = count.size();
                    String cont_s = Integer.toString(cont);
                    mysql_count(cont_s);

                    if (rowsAffected == -1) {
                        msg = "留言不存在, 無法刪除 !";
                    } else if (rowsAffected == 0) {
                        msg = "留言不存在, 無法刪除 !";
                    } else {
                        msg = "留言已刪除 !";
                        //---------------------------------
                        recSet_Q0601 = dbHper.getRecSet_Q0601();
                        view01 = 0;
                        showRec();
                        menu.setGroupVisible(R.id.q0600_group1, true);
                        menu.setGroupVisible(R.id.q0600_group2, false);
                        //---------------------------------
                    }
                    Toast.makeText(Q0601.this, msg, Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_NEGATIVE:
                    msg = "放棄刪除 !";
                    Toast.makeText(Q0601.this, msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //--------更新頁面
    private View.OnClickListener updataOn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String forum_id = mode00;
            String sub = mode01;
            String item = mode02;
            String date = mode03;
            String respond = mode04;
            String member = mode05;
            String item_text = mode06;
            //--------------------------
            intent.putExtra("forum_id", forum_id);
            intent.putExtra("sub", sub);
            intent.putExtra("item", item);
            intent.putExtra("date", date);
            intent.putExtra("respond", respond);
            intent.putExtra("member", member);
            intent.putExtra("item_text", item_text);
            intent.putExtra("class_title", getString(R.string.q0600_t004));
            intent.setClass(Q0601.this, Q0602.class);
            startActivity(intent);
        }
    };

    private void mysql_insert_Q0601() {
//        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        //        nameValuePairs.add(sqlctl);
        nameValuePairs.add(e_forum_id);
        nameValuePairs.add(e_content);
        nameValuePairs.add(e_firstname);
        nameValuePairs.add(e_email);
        nameValuePairs.add(e_userimage);
        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------
        String result = Q0601_R_DBConnector.executeInsert_R(nameValuePairs);  //真正執行新增
        //-----------------------------------------------
    }

    private void mysql_del_Q0601() {
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
        String result = Q0601_R_DBConnector.executeDelet(nameValuePairs);   //執行刪除
        //-----------------------------------------------
    }

    private void mysql_count(String cont) {
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(mode00);
        nameValuePairs.add(cont);

        try {
            Thread.sleep(100); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------
        String result = Q0600_DBConnector.executeUpdate_count_Q0600( nameValuePairs);
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
        if (runAuto_flag == false) {
            handler.post(updateTimer);  //開啟執行續
            runAuto_flag = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    //============================Menu===========================
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
                if (ACC !=null){

                    if (runAuto_flag == true){
                        handler.removeCallbacks(updateTimer);//-----關閉執行續
                        runAuto_flag = false;
                    }
                    //---------------------
                    String user = ACC.getEmail();
                    msg = null;
                    recSet_Q0601 = dbHper.getRecSet_user_Q0601(user);
                    //Toast.makeText(getApplicationContext(), "共 " + recSet_Q0601.size() + " 筆討論", Toast.LENGTH_SHORT).show();
                    //---------------------
                    view01 = 1;
                    showRec();
                    menu.setGroupVisible(R.id.q0600_group1, false);
                    menu.setGroupVisible(R.id.q0600_group2, true);
                }else {
                    new AlertDialog.Builder(Q0601.this)
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
                intent.setClass( Q0601.this, Q0200.class);
                startActivity(intent);
                break;
            case R.id.q0600_action_back:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}