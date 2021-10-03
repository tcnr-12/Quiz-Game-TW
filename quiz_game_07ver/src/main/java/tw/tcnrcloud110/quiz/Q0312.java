package tw.tcnrcloud110.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Q0312 extends AppCompatActivity{
    private static final String DB_FILE = "QuizeGame.db";
    //private static final String DB_TABLE = "quiz";
    private static final int DBversion = 1;
    private final String TAG = "tcnr05=>";
    private ListView listView;
//    private Button b001;
    private Spinner mSpnName;
    private DbHelper dbHper;
    private ArrayList<String> recSet;
    private TextView t002;
    private final Spinner.OnItemSelectedListener mSpnNameOnItemSelLis = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position,
                                   long id) {
            //-----------------------------
            //int index = position;
            //-----------------------------
            int iSelect = mSpnName.getSelectedItemPosition(); //找到按何項
            switch (iSelect) {
                case 0:
                    dbmysql_q0312_1();
                    recSet = dbHper.getRecSet_Q0312_1();
                    break;
                case 1:
                    dbmysql_q0312_2();
                    recSet = dbHper.getRecSet_Q0312_2();
                    break;
                case 2:
                    dbmysql_q0312_3();
                    recSet = dbHper.getRecSet_Q0312_3();
                    break;
            }
            //String[] fld = recSet.get(iSelect).split("#");
//            stHead = "顯示資料：第" + String.valueOf(iSelect + 1) + " / " + recSet.size() + " 筆";
//            tvTitle.setText(stHead);
//            b_id.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.Red));
            //b_id.setText(fld[0]);
//            b_name.setText(fld[1]);
//            b_grp.setText(fld[2]);
//            b_address.setText(fld[3]);
            //===========取SQLite 資料=============
            Log.d(TAG, "recSet.size" + recSet.size());
            List<Map<String, Object>> mList;
            mList = new ArrayList<Map<String, Object>>();
            int rank = 1;
            for (int i = 0; i < recSet.size(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                String[] fld = recSet.get(i).split("#");
                if (i > 0) {
                    String[] fld_t = recSet.get(i - 1).split("#");
                    if (Integer.parseInt(fld_t[3]) > Integer.parseInt(fld[3])) {
                        rank++;
                    }
                }
                if (rank > 10) {
                    break;
                }
                item.put("q0312_lit001", Integer.toString(rank));
                item.put("q0312_lit002", fld[1]);
//                item.put("q0312_lit003","mail:" +fld[2]);
                item.put("q0312_lit003", fld[3]);
                mList.add(item);
            }
            //=========設定listview========

//...................................................................................
            SimpleAdapter adapter = new SimpleAdapter(
                    Q0312.this,
                    mList,
                    R.layout.q0312_list,
                    new String[]{"q0312_lit001", "q0312_lit002", "q0312_lit003"},
                    new int[]{R.id.q0312_lit001, R.id.q0312_lit002, R.id.q0312_lit003}
            );
            Log.d(TAG, "SPN" + adapter.getCount());
            listView.setAdapter(adapter);
            //listView.setTextFilterEnabled(true);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0312);
        setupViewComponent();
    }

    private void enableStrictMode(Context context) {
        Log.d(TAG, "enableStrictMode");
        //-------------抓取遠端資料庫設定執行續------------------------------
        StrictMode.setThreadPolicy(new
                StrictMode.
                        ThreadPolicy.Builder().
                detectDiskReads().
                detectDiskWrites().
                detectNetwork().
                penaltyLog().
                build());
        StrictMode.setVmPolicy(
                new
                        StrictMode.
                                VmPolicy.
                                Builder().
                        detectLeakedSqlLiteObjects().
                        penaltyLog().
                        penaltyDeath().
                        build());

    }

    private void setupViewComponent() {
        enableStrictMode(Q0312.this);
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        listView = (ListView) findViewById(R.id.q0312_lv001);
        int ranktexth = displayMetrics.heightPixels * 6 / 10;
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                ranktexth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        listView.getLayoutParams().height = ranktexth;
        //t001.setLayoutParams(lp);
        mSpnName = (Spinner) findViewById(R.id.q0312_s001);
        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
//        b001 = (Button) findViewById(R.id.q0312_b001);
//        b001.setOnClickListener(this);
        t002 = (TextView) findViewById(R.id.q0312_t004);
        t002.setVisibility(View.GONE);

        initDB();
    }

    private void initDB() {
        if (dbHper == null)
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
        //-----------

        dbmysql_q0312_2();
        dbmysql_q0312_3();
        dbmysql_q0312_1();
        //-----------
        recSet = dbHper.getRecSet_Q0312_1(); //重新載入SQLite
    }

    private void dbmysql_q0312_1() {
        //String sqlctl = "SELECT * FROM quiz ORDER BY id ASC";//DESC
        String sqlctl = "SELECT * FROM q0312_1 WHERE score > 0 ORDER BY score DESC LIMIT 10";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0300_connectDB.executeQuery_Q0312(nameValuePairs);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            //==========================================
            //chk_httpstate();  //檢查 連結狀態
            //==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                //Toast.makeText(getApplicationContext(), "MySQL 連結成功有資料 ", Toast.LENGTH_SHORT).show();
                int rowsAffected = dbHper.clearRec_Q0312_1();                 // 匯入前,刪除所有SQLite資料\
                Log.d(TAG, "clearRec:" + rowsAffected);
                Log.d(TAG, "dbmysql_q0312_1 array " + jsonArray.length());
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        Log.d(TAG, "key:" + key);

                        String value = jsonData.getString(key); // 取出欄位的值

                        if (value.equals(null)) {//== null
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        if (key.equals("id") || key.equals("score")) {
                            int intvalue = Integer.parseInt(value);
                            newRow.put(key, intvalue); // 動態找出有幾個欄位
                            Log.d(TAG, "value:" + value);
                        } else {
                            newRow.put(key, value); // 動態找出有幾個欄位
                            Log.d(TAG, "value:" + value);
                        }


                        // -------------------------------------------------------------------
                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_m_Q0312_1(newRow);
                    Log.d(TAG, "insert rowID 1:" + rowID);
                }
//                Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "匯入");
                // ---------------------------
            } else {
//                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet_Q0312_1();  //重新載入SQLite
            //u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, "dbmysql_q0312_1" + e.toString());
        }
    }

    private void dbmysql_q0312_2() {
        //String sqlctl = "SELECT * FROM quiz ORDER BY id ASC";
        String sqlctl = "SELECT * FROM q0312_2 WHERE score > 0 ORDER BY score DESC LIMIT 10";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0300_connectDB.executeQuery_Q0312(nameValuePairs);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            //==========================================
            //chk_httpstate();  //檢查 連結狀態
            //==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                //Toast.makeText(getApplicationContext(), "MySQL 連結成功有資料 ", Toast.LENGTH_SHORT).show();
                int rowsAffected = dbHper.clearRec_Q0312_2();                 // 匯入前,刪除所有SQLite資料
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value.equals(null)) {//== null
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value); // 動態找出有幾個欄位
                        // -------------------------------------------------------------------
                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_m_Q0312_2(newRow);

                }
//                Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "匯入");
                // ---------------------------
            } else {
//                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet_Q0312_1();  //重新載入SQLite
            //u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    private void dbmysql_q0312_3() {
        //String sqlctl = "SELECT * FROM quiz ORDER BY id ASC";
        String sqlctl = "SELECT * FROM q0312_3 WHERE score > 0 ORDER BY score DESC LIMIT 10";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0300_connectDB.executeQuery_Q0312(nameValuePairs);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            //==========================================
            //chk_httpstate();  //檢查 連結狀態
            //==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                //Toast.makeText(getApplicationContext(), "MySQL 連結成功有資料 ", Toast.LENGTH_SHORT).show();
                int rowsAffected = dbHper.clearRec_Q0312_3();                 // 匯入前,刪除所有SQLite資料
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value.equals(null)) {//== null
                            continue;
                        } else if ("".equals(value.trim())) {
                            continue;
                        } else {
                            jsonData.put(key, value.trim());
                        }
                        // ------------------------------------------------------------------
                        newRow.put(key, value); // 動態找出有幾個欄位
                        // -------------------------------------------------------------------
                    }
                    // ---(2) 使用固定已知欄位---------------------------
                    // newRow.put("id", jsonData.getString("id").toString());
                    // newRow.put("name",
                    // jsonData.getString("name").toString());
                    // newRow.put("grp", jsonData.getString("grp").toString());
                    // newRow.put("address", jsonData.getString("address")
                    // -------------------加入SQLite---------------------------------------
                    long rowID = dbHper.insertRec_m_Q0312_3(newRow);

                }
//                Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "匯入");
                // ---------------------------
            } else {
//                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet_Q0312_1();  //重新載入SQLite
            //u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

//    @Override
//    public void onClick(View v) {
//        this.finish();
//    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    //===========menu==============
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.q0300, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent01;
        switch (item.getItemId()) {
            case R.id.q0300_m_back:
//                Toast.makeText(getApplicationContext(), "施工中", Toast.LENGTH_SHORT).show();

//                intent01=new Intent(M1405query.this,M1405.class);
//                //intent01.putExtra("title", getString(R.string.m_add));
//                startActivity(intent01);
                this.finish();
                break;


        }


        return super.onOptionsItemSelected(item);
    }

}