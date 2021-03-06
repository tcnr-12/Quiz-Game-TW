package tw.tcnrcloud110.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Q0513 extends AppCompatActivity implements View.OnClickListener {

    String TAG = "tcnr16";
    private TextView count_t;
    private EditText b_id, b_text1,  b_text2,b_text3;

    private TextView tvTitle;
    private Button btNext, btPrev, btTop, btEnd;
    private ArrayList<String> recSet;
    private int index = 0;
    String msg = null;


    private Button btEdit, btDel;
    String ttext1, ttext2, ttext3;

    private Spinner mSpnName;
    int up_item = 0;
    //------------------------------
    protected static final int BUTTON_POSITIVE = -1;
    protected static final int BUTTON_NEGATIVE = -2;
    private Button btAdd, btAbandon, btquery, btcancel, btreport;

    private String[] MYCOLUMN = new String[]{"id", "text1", "text2", "text3"};
    int tcount;

    // ------------------
    private RelativeLayout b_Relbutton;
    private RelativeLayout brelative01;
    private LinearLayout blinear02;
    private ListView listView;
    private TextView bsubTitle;
    //===============
    private DbHelper dbHper;
    private static final String DB_FILE = "QuizeGame.db";
    private static final String DB_TABLE = "q0513";
    private static final int DBversion = 1;
    //-----------------
    private String sqlctl;
    private String tid;
    private String s_id;
    private String taddress;
    private int old_index = 0;

    private MenuItem b_m_add, b_m_query, b_m_batch, b_m_list, b_m_mysql, b_m_edit_start, b_m_edit_stop, b_m_return;
    private Menu menu;
    private boolean touch_flag;


    // ----------????????????--------------------------------
    private Long startTime;
    private Handler handler = new Handler();

    int autotime = 60;// ?????????????????? ????????????MySQL??????
    //------------------------------
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private TextView nowtime;  //???????????????????????????
    int update_time = 0;
    private String str;
    // --------------------------------------------------------
    private boolean runAuto_flag = false;  //Runable updateTime ??????
    private TextView b_editon;
    private String stHead;
    private boolean edittype_flag = false;  //????????????
    private String showip; //????????????ip
    // --------------------------------------------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        enableStrictMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0513);
        //--------??????????????????
        startTime = System.currentTimeMillis();
        // -----------------
        setupViewComponent();
    }
    public static void enableStrictMode(Context context) {
        //-------------????????????????????????????????????------------------------------
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
        tvTitle = (TextView) findViewById(R.id.tvIdTitle);
        b_id = (EditText) findViewById(R.id.edid);
        b_text1 = (EditText) findViewById(R.id.edt_text1);
        b_text2 = (EditText) findViewById(R.id.edt_text2);
        b_text3 = (EditText) findViewById(R.id.edt_text3);
        count_t = (TextView) findViewById(R.id.count_t);

        btNext = (Button) findViewById(R.id.btIdNext);
        btPrev = (Button) findViewById(R.id.btIdPrev);
        btTop = (Button) findViewById(R.id.btIdtop);
        btEnd = (Button) findViewById(R.id.btIdend);

        btEdit = (Button) findViewById(R.id.btnupdate);
        btDel = (Button) findViewById(R.id.btIdDel);

        //-----------------------
        btAdd = (Button) findViewById(R.id.btnAdd);
        btAbandon = (Button) findViewById(R.id.btnabandon);
        btquery = (Button) findViewById(R.id.btnquery);
        btcancel = (Button) findViewById(R.id.btidcancel);
        btreport = (Button) findViewById(R.id.btnlist);

        brelative01 = (RelativeLayout) findViewById(R.id.relative01);
        blinear02 = (LinearLayout) findViewById(R.id.linear02);

        listView = (ListView) findViewById(R.id.listView);

        b_Relbutton = (RelativeLayout) findViewById(R.id.Relbutton);
        listView = (ListView) findViewById(R.id.listView);
        bsubTitle = (TextView) findViewById(R.id.subTitle);
        b_editon = (TextView) findViewById(R.id.edit_on);

        tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Teal));
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
        //-----------------
        mSpnName = (Spinner) this.findViewById(R.id.spnName);
        //---------------------------------------????????????layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        b_id.getLayoutParams().height=displayMetrics.heightPixels /40*2;
        b_text1.getLayoutParams().height=displayMetrics.heightPixels /40*2;
        b_text2.getLayoutParams().height=displayMetrics.heightPixels /40*6;
        b_text3.getLayoutParams().height=displayMetrics.heightPixels /40*2;

        b_id .getLayoutParams().width=displayMetrics.widthPixels/40*30;
        b_text1 .getLayoutParams().width=displayMetrics.widthPixels/40*30;
        b_text2 .getLayoutParams().width=displayMetrics.widthPixels/40*30;
        b_text3.getLayoutParams().width=displayMetrics.widthPixels/40*30;

        btNext.getLayoutParams().width=displayMetrics.widthPixels/40*9;
        btPrev.getLayoutParams().width=displayMetrics.widthPixels/40*9;
        btTop.getLayoutParams().width=displayMetrics.widthPixels/40*9;
        btEnd.getLayoutParams().width=displayMetrics.widthPixels/40*9;
        //---------??????layout ??????---------------
        u_layout_def();
        //-----------------------
        btNext.setOnClickListener(this);
        btPrev.setOnClickListener(this);
        btTop.setOnClickListener(this);
        btEnd.setOnClickListener(this);

        btEdit.setOnClickListener(this);
        btDel.setOnClickListener(this);

        btAdd.setOnClickListener(this);
        btAbandon.setOnClickListener(this);
        btquery.setOnClickListener(this);
        btcancel.setOnClickListener(this);
        btreport.setOnClickListener(this);

//        //===================================
        startTime = System.currentTimeMillis();        // ??????????????????
//        ************************************
        if (runAuto_flag == false) {
            handler.postDelayed(updateTimer, 500);  // ??????Delay?????????
            runAuto_flag = true;
        }
        java.sql.Date curDate = new java.sql.Date(System.currentTimeMillis()); // ????????????????????
        str = formatter.format(curDate);
//        nowtime.setText(getString(R.string.q0513_now_time) + str);
        initDB();
        showRec(index);
        u_setspinner();
        stHead = "??????????????????" + (index + 1) + " / " + tcount + " ???";
        tvTitle.setText(stHead);
        b_id.setTextColor(ContextCompat.getColor(this, R.color.big_button_text));
        // -------------------------
        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);

        }
    // ????????????????????????
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            old_index = mSpnName.getSelectedItemPosition();
            Long spentTime = System.currentTimeMillis() - startTime;
            String hours = String.format("%02d", (spentTime / 1000) / 60 / 60);  // ???????????????????????????
            String minius = String.format("%02d", ((spentTime / 1000) / 60) % 60);  // ???????????????????????????
            String seconds = String.format("%02d", (spentTime / 1000) % 60);          // ????????????????????????
            handler.postDelayed(this, autotime * 1000); // ?????????????????????
            // -------????????????MySQL
            dbmysql();
            recSet = dbHper.getRecSet_Q0513();  //????????????SQLite
            u_setspinner();  //????????????spinner??????
            index = old_index;
            showRec(index); //??????spainner ???????????????????????????
            //-------------------------------------------------------------------------------
            ++update_time;
        }
    };

    private void u_setspinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        for (int i = 0; i < recSet.size(); i++) {
            String[] fld = recSet.get(i).split("#");
            adapter.add( fld[0] + ". " +fld[1] + " " );
//            adapter.add(fld[0] + " " + fld[1] + " " + fld[2] + " " + fld[3]);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnName.setAdapter(adapter);

        mSpnName.setOnItemSelectedListener(mSpnNameOnItemSelLis);
        //        mSpnName.setSelection(index, true); //spinner ?????????????????????
    }
    private void u_layout_def() {
        btAdd.setVisibility(View.INVISIBLE);
        btAbandon.setVisibility(View.INVISIBLE);
        btquery.setVisibility(View.INVISIBLE);
        btEdit.setVisibility(View.VISIBLE);
        btDel.setVisibility(View.VISIBLE);
        brelative01.setVisibility(View.VISIBLE);
        blinear02.setVisibility(View.INVISIBLE);
        btreport.setVisibility(View.INVISIBLE);
        b_id.setEnabled(false);
        //-----------------------
        b_Relbutton.setVisibility(View.INVISIBLE);
        b_editon.setVisibility(View.INVISIBLE);
        //-----------------------
    }


    private Spinner.OnItemSelectedListener mSpnNameOnItemSelLis = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView parent, View view, int position,
                                   long id) {
            index = position;
            int iSelect = mSpnName.getSelectedItemPosition(); //???????????????
            String[] fld = recSet.get(iSelect).split("#");
            String s = "????????????" + recSet.size() + " ???," + "?????????  " + String.valueOf(iSelect + 1) + "???"; //?????????0
            tvTitle.setText(s);
            b_id.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.big_button_text));
            b_id.setText(fld[0]);
            b_text1.setText(fld[1]);
            b_text2.setText(fld[2]);
            b_text3.setText(fld[3]);
            //-------???????????????item---
            index = iSelect;
            // -----????????????????????????---------------
            if(btAdd.getVisibility() == View.VISIBLE){
                b_id.setHint("???????????????");
                b_text1.setHint("*");
                b_text2.setHint("*");
                b_id.setText("");
                b_text1.setText("");
                b_text2.setText("");
                b_text3.setText("");

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            b_id.setText("");
            b_text1.setText("");
            b_text2.setText("");
            b_text3.setText("");
        }
    };


    @Override
    public void onClick(View v) {
        int rowsAffected;
        Uri uri;
        String s_id;
        String whereClause;
        String[] selectionArgs;

        switch (v.getId()) {
            case R.id.btIdNext:
                ctlNext();
                break;
            case R.id.btIdPrev:
                ctlPrev();
                break;
            case R.id.btIdtop:
                ctlFirst();
                break;
            case R.id.btIdend:
                ctlLast();
                break;
            //------------------------------------
            case R.id.btnupdate:
                // ????????????
                old_index=index;
//                  old_id =b_id;
                mysql_update(); // ??????MySQL??????
                dbmysql();
                //-------------------------------------
                recSet = dbHper.getRecSet_Q0513();
                u_setspinner();
//------------------------
//                ??? old_id ?????? index
//--------------------------------
                index=old_index;   //???????????????
                showRec(index);
                msg = "??? " + (index + 1) + " ?????????  ????????? ! " ;
//                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
            //------------------------------------
            case R.id.btIdDel:
                // ???????????? --???????????????
                MyAlertDialog myAltDlg = new MyAlertDialog(this);
                myAltDlg.getWindow().setBackgroundDrawableResource(R.color.Yellow);
                myAltDlg.setTitle("????????????");
                myAltDlg.setMessage("????????????????????????\n????????????????");
                myAltDlg.setCancelable(false);
                myAltDlg.setIcon(R.drawable.c_dialog_info);
                myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "????????????", aldBtListener);
                myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "????????????", aldBtListener);
                myAltDlg.show();
                break;
            //-----------------------
            case R.id.btnAdd: //???????????????
                // ??????name????????????????????????
                ttext1 = b_text1.getText().toString().trim();
                ttext2 = b_text2.getText().toString().trim();
                ttext3 = b_text3.getText().toString().trim();

                if (ttext1.equals("") || ttext2.equals("")) {
                    Toast.makeText(getApplicationContext(), "???????????????????????? !", Toast.LENGTH_SHORT).show();
                    return;
                }
                //-------???????????????MySQL-------------------------------
                mysql_insert();
                dbmysql();
                //----------------------------------------
                msg = null;
//                // -------------------------

                long rowID = dbHper.RecCount_Q0513();
                if (rowID != -1) {
                    index = dbHper.RecCount_Q0513() - 1;
                    u_setspinner();
                    showRec(index);
                    ctlLast();  //????????????????????????
                    msg = "????????????  ?????? ! \n" + "????????????????????? " + dbHper.RecCount_Q0513() + " ????????? !";
                } else {
                    msg = "????????????  ?????? !";
                }

//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                count_t.setText("??????:" + Integer.toString(dbHper.RecCount_Q0513()) + "???");
//                setupViewComponent();
                u_insert();
                break;
            //------------------------------------
            case R.id.btnabandon: //???????????????
                mSpnName.setEnabled(true);
                u_menu_edit_main(); //??????????????????
                break;
            //------------------------------------
            case R.id.btnquery: //???????????????
                ttext1 = b_text1.getText().toString().trim();
                ttext2 = b_text2.getText().toString().trim();
                ttext3 = b_text3.getText().toString().trim();
                msg = null;
                recSet = dbHper.getRecSet_query_Q0513(ttext1, ttext2, ttext3);
//                Toast.makeText(getApplicationContext(), "??????????????? ??? " + recSet.size() + " ???", Toast.LENGTH_SHORT).show();
                u_setspinner();
                break;
            case R.id.btnlist: //???????????????
                ttext1 = b_text1.getText().toString().trim();
                ttext2 = b_text2.getText().toString().trim();
                ttext3 = b_text3.getText().toString().trim();
                msg = null;
                recSet = dbHper.getRecSet_query_Q0513(ttext1, ttext2, ttext3);

//===========???SQLite ??????=============
                List<Map<String, Object>> mList;
                mList = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < recSet.size(); i++) {
                    Map<String, Object> item = new HashMap<String, Object>();
                    String[] fld = recSet.get(i).split("#");
                    item.put("txtView", "id:" + fld[0] + "\n??????:" + fld[1] + "\n??????:" + fld[2] + "\n??????:" + fld[3]);
                    mList.add(item);
                }
                //=========??????listview========
                brelative01.setVisibility(View.INVISIBLE);
                blinear02.setVisibility(View.VISIBLE);
//...................................................................................
                SimpleAdapter adapter = new SimpleAdapter(
                        this,
                        mList,
                        R.layout.q0513_list_item,
                        new String[]{ "txtView"},
                        new int[]{ R.id.txtView}
                );
                listView.setAdapter(adapter);
                listView.setTextFilterEnabled(true);
                listView.setOnItemClickListener(listviewOnItemClkLis);
                break;
            //------------------------------------
            case R.id.btidcancel:  //????????????
                old_index = mSpnName.getSelectedItemPosition();
                dbmysql();
                recSet = dbHper.getRecSet_Q0513();  //????????????SQLite
                u_setspinner();  //????????????spinner??????
                //-----------------------------------
                u_menu_main();
                edittype_flag = false;
                index = old_index;
                showRec(index); //??????spainner ???????????????????????????
                //********************************************
                if (runAuto_flag == false ) {
                    handler.post(updateTimer);  //???????????????
                    runAuto_flag = true;
                }
                //********************************************

               break;

        }
    }
    private void mysql_update() {
        s_id = b_id.getText().toString().trim();
        ttext1 = b_text1.getText().toString().trim();
        ttext2 = b_text2.getText().toString().trim();
        ttext3 = b_text3.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(s_id);
        nameValuePairs.add(ttext1);
        nameValuePairs.add(ttext2);
        nameValuePairs.add( ttext3);
        try {
            Thread.sleep(100); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Q0513_DBConnector.executeUpdate( nameValuePairs);
//-----------------------------------------------
    }

    private void mysql_insert() {
        //        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(ttext1);
        nameValuePairs.add(ttext2);
        nameValuePairs.add(ttext3);
        try {
            Thread.sleep(500); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Q0513_DBConnector.executeInsert(nameValuePairs);  //??????????????????
//-----------------------------------------------
    }

    private ListView.OnItemClickListener listviewOnItemClkLis = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String s = "???????????? " + Integer.toString(position + 1) + "???"
                    + ((TextView) view.findViewById(R.id.txtView))
                    .getText()
                    .toString();
            bsubTitle.setText(s);
        }
    };
    // ---------------------------------------------
    private DialogInterface.OnClickListener aldBtListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:

                    tid = b_id.getText().toString().trim();
                    old_index=index;
                    // ---------------------------
                    mysql_del();// ??????MySQL??????
                    dbmysql();


                    index=old_index;
                    u_setspinner();
                    if (index == dbHper.RecCount_Q0513()) {
                        index--;
                    }
                    showRec(index);
//                    mSpnName.setSelection(index, true); //spinner ?????????????????????
//                }
                    msg = "???????????????" ;
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
                case BUTTON_NEGATIVE:
                    msg = "???????????????????????? !";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void mysql_del() {
        //---------
        s_id = b_id.getText().toString().trim();
        ArrayList<String> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(sqlctl);
        nameValuePairs.add(s_id);
        try {
            Thread.sleep(100); //  ??????Thread ??????0.5???
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//-----------------------------------------------
        String result = Q0513_DBConnector.executeDelet(nameValuePairs);   //????????????
//-----------------------------------------------
    }
    private void initDB() {
        if (dbHper == null)
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);
        //-----****???????????? ??????????????? SQLite ????????? ??????****------
        dbmysql();
        //-----------
        recSet = dbHper.getRecSet_Q0513();
    }
    private void showRec(int index) {
        msg = "";
        if (recSet.size() != 0) {
            String stHead = "?????????????????? " + (index + 1) + " ??? / ??? " + recSet.size() + " ???";
            msg = getString(R.string.q0513_count_t) + recSet.size() + "???";
            tvTitle.setBackgroundColor(ContextCompat.getColor(this, R.color.Teal));
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Yellow));
            tvTitle.setText(stHead);

            String[] fld = recSet.get(index).split("#");
            b_id.setTextColor(ContextCompat.getColor(this, R.color.big_button_text));
            b_id.setBackgroundColor(ContextCompat.getColor(this, R.color.background));
            b_id.setText(fld[0]);
            b_text1.setText(fld[1]);
            b_text2.setText(fld[2]);
            b_text3.setText(fld[3]);
            mSpnName.setSelection(index, true); //spinner ?????????????????????
        } else {
            String stHead = "???????????????0 ???";
            msg = getString(R.string.q0513_count_t) + "0???";
            tvTitle.setTextColor(ContextCompat.getColor(this, R.color.Blue));
            tvTitle.setText(stHead);
            b_id.setText("");
            b_text1.setText("");
            b_text2.setText("");
            b_text3.setText("");
        }
        count_t.setText(msg);
    }
    //------------------------------------------------
    private void ctlFirst() {
        // ?????????
        index = 0;
        showRec(index);
    }

    private void ctlPrev() {
        // ?????????
        index--;
        if (index < 0)
            index = recSet.size() - 1;
        showRec(index);
    }

    private void ctlNext() {
        // ?????????
        index++;
        if (index >= recSet.size())
            index = 0;
        showRec(index);
    }


    private void ctlLast() {
        // ????????????
        index = recSet.size() - 1;
        showRec(index);
    }
//--------------------????????????------------------------

    @Override
    protected void onPause() {
        super.onPause();
        if (runAuto_flag == true) {
            handler.removeCallbacks(updateTimer); //???????????????
            runAuto_flag = false;
        }
        }

    @Override
    protected void onResume() {
        super.onResume();
        //********************************************
        if (runAuto_flag == false && edittype_flag == false) {
            handler.post(updateTimer);  //???????????????
            runAuto_flag = true;
        }
        //********************************************

    }
    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(updateTimer);
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
  
    private void u_insert() {
        btAdd.setVisibility(View.VISIBLE);
        btAbandon.setVisibility(View.VISIBLE);
        btEdit.setVisibility(View.INVISIBLE);
        btDel.setVisibility(View.INVISIBLE);
        b_id.setEnabled(false);
        b_id.setHint("?????????");
        b_text1.setHint("*");
        b_text2.setHint("*");
        b_id.setText("");
        b_text1.setText("");
        b_text2.setText("");
        b_text3.setText("");

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) { //???????????????
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

/// ??????MySQL ??????
private void dbmysql() {
    sqlctl = "SELECT * FROM q0513 ORDER BY id ASC";
    ArrayList<String> nameValuePairs = new ArrayList<>();
    nameValuePairs.add(sqlctl);
    try {
        String result = Q0513_DBConnector.executeQuery(nameValuePairs);
        /**************************************************************************
         * SQL ??????????????????????????????JSONArray
         * ?????????????????????????????????JSONObject?????? JSONObject
         * jsonData = new JSONObject(result);
         **************************************************************************/
        JSONArray jsonArray = new JSONArray(result);
        // -------------------------------------------------------
        if (jsonArray.length() > 0) { // MySQL ?????????????????????
            int rowsAffected = dbHper.clearRec_Q0513();                 // ?????????,????????????SQLite??????,??????????????????
            // ??????JASON ????????????????????????
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                ContentValues newRow = new ContentValues();
                // --(1) ?????????????????? --?????? jsonObject ????????????("key","value")-----------------------
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
                // -------------------??????SQLite---------------------------------------
                long rowID = dbHper.insertRec_m_Q0513(newRow);
//                Toast.makeText(getApplicationContext(), "????????? " + Integer.toString(jsonArray.length()) + " ?????????", Toast.LENGTH_SHORT).show();
            }
            // ---------------------------
        } else {
            Toast.makeText(getApplicationContext(), "????????????????????????", Toast.LENGTH_LONG).show();
        }
        recSet = dbHper.getRecSet_Q0513();  //????????????SQLite
        u_setspinner();
        // --------------------------------------------------------
    } catch (Exception e) {
        Log.d(TAG, e.toString());
    }
}



    //*********************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0513, menu);
        this.menu = menu;
//
        b_m_add = menu.findItem(R.id.m_add);
        b_m_query = menu.findItem(R.id.m_query);
        b_m_list = menu.findItem(R.id.m_list);
        b_m_edit_start = menu.findItem(R.id.m_edit_start);
        b_m_edit_stop = menu.findItem(R.id.m_edit_stop);
        b_m_return = menu.findItem(R.id.m_return);
//        ========================
        u_menu_main();
        return true;
    }

    private void u_menu_main() {
        brelative01.setVisibility(View.VISIBLE);
        blinear02.setVisibility(View.INVISIBLE);
        menu.setGroupVisible(R.id.m_group1, true);
        menu.setGroupVisible(R.id.m_group2, false);
        menu.setGroupVisible(R.id.m_group3, false);
        b_Relbutton.setVisibility(View.INVISIBLE);
        mSpnName.setVisibility(View.VISIBLE);
        b_editon.setVisibility(View.INVISIBLE);
        btEdit.setVisibility(View.INVISIBLE);
        btDel.setVisibility(View.INVISIBLE);
        u_button_ontouch();
    }

    private void u_menu_edit_main() {
//*************************************
        if (runAuto_flag == true) {
            handler.removeCallbacks(updateTimer); //???????????????
            runAuto_flag = false;
        }
        menu.setGroupVisible(R.id.m_group1, false);
        menu.setGroupVisible(R.id.m_group2, true);
        menu.setGroupVisible(R.id.m_group3, false);
        //--------------------------
        b_Relbutton.setVisibility(View.VISIBLE);
        b_editon.setVisibility(View.VISIBLE);
        btAdd.setVisibility(View.INVISIBLE);
        btAbandon.setVisibility(View.INVISIBLE);
        btquery.setVisibility(View.INVISIBLE);
        btreport.setVisibility(View.INVISIBLE);
        btEdit.setVisibility(View.VISIBLE);
        btDel.setVisibility(View.VISIBLE);
        mSpnName.setVisibility(View.VISIBLE);

        u_button_ontouch();
        touch_flag = true;  //??????ontuchevent
        index = mSpnName.getSelectedItemPosition(); // ???????????????
        //        mSpnName.setEnabled(false);
        showRec(index); //??????spainner ???????????????????????????
    }

    private void u_button_ontouch() {
        btTop.setVisibility(View.VISIBLE);
        btNext.setVisibility(View.VISIBLE);
        btPrev.setVisibility(View.VISIBLE);
        btEnd.setVisibility(View.VISIBLE);
    }

    private void u_menu_return() {
        menu.setGroupVisible(R.id.m_group1, false);
        menu.setGroupVisible(R.id.m_group2, true);
        menu.setGroupVisible(R.id.m_group3, false);
    }

    private void stop_edit() {
        mSpnName.setEnabled(true);
        old_index = mSpnName.getSelectedItemPosition();
        u_menu_main();
        edittype_flag = false;
        // -------????????????MySQL
        dbmysql();
        recSet = dbHper.getRecSet_Q0513();  //????????????SQLite
        u_setspinner();  //????????????spinner??????
        index = old_index;
        showRec(index); //??????spainner ???????????????????????????
        if (runAuto_flag == false && edittype_flag == false) {
            handler.post(updateTimer);  //???????????????
            runAuto_flag = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it = new Intent();
        switch (item.getItemId()) {
            case R.id.m_add://??????
                btTop.setVisibility(View.VISIBLE);
                btNext.setVisibility(View.VISIBLE);
                btPrev.setVisibility(View.VISIBLE);
                btEnd.setVisibility(View.VISIBLE);
                mSpnName.setVisibility(View.VISIBLE);
                touch_flag = false;  //??????ontuchevent
                u_menu_return();
                u_insert();
                break;
            case R.id.m_query://??????
                btAdd.setVisibility(View.INVISIBLE);
                btAbandon.setVisibility(View.VISIBLE);
                btEdit.setVisibility(View.INVISIBLE);
                btDel.setVisibility(View.INVISIBLE);
                btquery.setVisibility(View.VISIBLE);
                b_id.setEnabled(false);
                b_id.setText("");
                b_text1.setText("");
                b_text2.setText(" ");
                b_text3.setText(" ");
                b_id.setHint("(????????????????????????)");
                break;
            case R.id.m_list://??????

                btAdd.setVisibility(View.INVISIBLE);
                btAbandon.setVisibility(View.VISIBLE);
                btEdit.setVisibility(View.INVISIBLE);
                btDel.setVisibility(View.INVISIBLE);
                btquery.setVisibility(View.INVISIBLE);
                btreport.setVisibility(View.VISIBLE);
                brelative01.setVisibility(View.VISIBLE);
                blinear02.setVisibility(View.INVISIBLE);
                b_id.setEnabled(false);
                b_id.setText("");
                b_text1.setText("");
                b_text2.setText("");
                b_text3.setText("");
                b_id.setHint("?????????");
                break;

            case R.id.m_return:
                btAbandon.performClick(); //??????????????????
                break;

            case R.id.action_settings:
                this.finish();
                // finish()??????????????? Activity???????????????????????????????????? android ?????????????????????
                // exit()???????????????????????? Activity???????????????????????? Activity ???????????????
                // killProcess()???????????????????????? Activity????????????????????????Activity ?????????????????
                // restartPackage()??????????????? App????????? service ????????? Activity ?????????
//                btAbandon.performClick();
                break;

            case R.id.m_edit_start:  //????????????
                u_menu_edit_main();
                edittype_flag = true;
                break;

            case R.id.m_edit_stop: //????????????
                stop_edit(); //??????????????????
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}//-----------END