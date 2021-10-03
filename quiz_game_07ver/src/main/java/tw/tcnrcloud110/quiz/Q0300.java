package tw.tcnrcloud110.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
//import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;




public class Q0300 extends AppCompatActivity implements View.OnClickListener{
    //private TextView tv;
    private final String TAG="tcnr05=>";
    private Button btn;
    private final Intent intent01=new Intent();
    private RadioGroup rb01;
    private RadioButton rb001,rb002,rb003;
    private RelativeLayout lay01;
    //MediaPlayer startmusic;
    private static final String DB_FILE = "QuizeGame.db";
    //private static final String DB_TABLE = "quiz";
    private static final int DBversion = 1;
    private DbHelper dbHper;

    private String ser_msg="";
    private ArrayList<String> recSet;
    private RelativeLayout lay02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        enableStrictMode(Q0300.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0300);
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
    //====================================
    private void setupViewComponent(){
        //---------------------------------------
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        //---------------------------------------宣告
//        setTitle(getString(R.string.q0300_t001));
        InitDB();
        Log.d(TAG, "InitDB");
        dbmysql();
        Log.d(TAG, "dbmysql");
        //lay01 = (RelativeLayout) findViewById(R.id.q0300_rl000);
        rb01 = (RadioGroup) findViewById(R.id.q0300_rbg001);
        rb001=findViewById(R.id.q0300_rb001);

        rb002=findViewById(R.id.q0300_rb002);
        rb003=findViewById(R.id.q0300_rb003);
//        startmusic = MediaPlayer.create(Q0300.this, R.raw.q0300_bg03);
//        music_set();
//        startmusic.start();
        lay01 = (RelativeLayout) findViewById(R.id.q0300_rl001);
        lay01.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_alpha_in));


        // 動態調整高度 抓取使用裝置自身的尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels /2;
        int buttondisth = displayMetrics.heightPixels *2/8;
        int buttondistw = displayMetrics.widthPixels /4;
        //int quizbuttonwidth = displayMetrics.widthPixels /4;
        int quizbuttondistw = displayMetrics.widthPixels /4/4;

        // ----巨集的參考物件
        TextView t001=(TextView)findViewById(R.id.q0300_t001);
        Button objb001 = (Button) findViewById(R.id.q0300_objb001); // 取出參考物件
        objb001.setVisibility(View.GONE); // 設定參考物件隱藏不佔空間
        //
        //int idt=t001.getId();
        int idt=t001.getId();
        // ----巨集
        int id;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                buttonwidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //lp.addRule(RelativeLayout.BELOW, idt);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, 180);
//        lp.setMargins(buttondistw, buttondisth, 0,0 ); // 設定物件之間距離
        lp.setMargins(quizbuttondistw, 0, 0,0 );
        String microNo;
        //rb001.setLayoutParams(lp);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(buttonwidth/2, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(displayMetrics.widthPixels/20, 0, 0, 0);
//        rb001.getLayoutParams().width=buttonwidth/2;
        rb001.setLayoutParams(params);
        rb002.setLayoutParams(params);
        rb003.setLayoutParams(params);
        rb001.setChecked(true);
        //resetrb();


        try {
            for (int i = 4; i <= 7; i++)   {// n個按鈕
                btn = new Button(this); //  繼承button
                btn.setId(i); // 寫入配置碼ID 代號
                lp = new RelativeLayout.LayoutParams(
                        buttonwidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.BELOW, idt);
//                lp.addRule(RelativeLayout.START_OF, idt);
                lp.setMargins(buttondistw-20, buttondisth, 0,0 ); // 設定物件之間距離
                buttondisth = displayMetrics.heightPixels /20;
                // %02d執行十進制整數轉換d，格式化補零，寬度為2。 因此，一個int參數，它的值是7
                // ，將被格式化為"07"作為一個String
                microNo = String.format("%03d", i);
//                Log.d(TAG,             microNo                );
                // 取得string 裏頭相對應的ID 碼，"string"代表是抓字串
                id = getResources().getIdentifier("q0300_b" + microNo, "string", getPackageName());
                // --------------------------------------------
                if (id == 0) {
                    break; // 假如 getIdentifier 找不到滿足資料, 會傳回 0 , 所以中斷迴圈
                }
                // --------------------------------------------
                btn.setText(id); // 將產生的物件填入文字.


                btn.setLayoutParams(lp); // 套用格式
                btn.setTextColor(objb001.getCurrentTextColor()); // 以objt001字體顏色為依據 (繼承)
                btn.setGravity(objb001.getGravity()); // 以objb001字體靠右靠左
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, objb001.getTextSize()); // 設定文字大小
                btn.setBackground(objb001.getBackground()); // 設定背景
                btn.setPadding(objb001.getPaddingLeft(),
                        objb001.getPaddingTop(),
                        objb001.getPaddingRight(),
                        objb001.getPaddingBottom()); // 設定文字內間距 left,top,right,bottom
                lay01.addView(btn);// 顯示textview物件
                //lp.removeRule(RelativeLayout.BELOW);

                idt=btn.getId();

                btn.setOnClickListener(this);
            }
        } catch (Exception e) {
            //Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
            return; // for 迴圈異常中斷到此跳出.
        }
        //rb01.setOnCheckedChangeListener(rb01on);

    }
    //    public void resetrb(){
//        rb001.setChecked(false);
//        rb002.setChecked(false);
//        rb003.setChecked(false);
//    }
    private void InitDB(){

        if(dbHper==null){
            dbHper=new DbHelper(this,DB_FILE,null,DBversion);//factory:null不用連線

        }

//        recSet=dbHper.getRecSet();
//        if(recSet.size()<=0){
//            dbHper.insertRec("以下選項不是在描述台灣水蜜桃的桃台農2號夏蜜?","果重平均200公克以下","有茸毛。"	,"果皮紅，底色乳黃",	"香氣濃、糖度高、酸度低。");
//            dbHper.insertRec("下列選項何者是在描述芒果台農3號?","果實呈長橢圓","果皮為紅色","果肉纖維量多","果實呈圓形");
//            dbHper.insertRec("以下選項不是在描述青蔥台農選1號?","辛香味比三星蔥濃郁","本品種適合於秋冬季栽培。"	,"平地栽培於春季較易開花",	"食用口感比北蔥硬脆");
//            dbHper.insertRec("下列選項何者不是甘藷台農73號的特徵?","肉色為黃色","本品種為第一個台灣自行育成之紫肉甘藷品種。"	,"塊根收量極為穩定",	"富含cyanidin及peonidin等花青素");
//            dbHper.insertRec("農業試驗所嘉義分所以「黑葉」品種為母本，「玉荷包」品種為父本，以人工雜交授粉的荔枝台農1號翠玉的採收期為何?",
//                    "台灣南部約5月下旬","台灣南部約2月下旬。"	,"台灣中部約2月下旬",	"台灣中部約5月下旬。");
//            dbHper.insertRec("以下有關水蜜桃台農11號杏桃的敘述何者錯誤?","台灣中部以北低海拔山區不適合種植","果皮橘黃，果肉黃色。"	,"果肉為不溶質，硬度高，不易損傷",	"果實成熟期在5月中旬");
//            dbHper.insertRec("具有早生、耐熱的特性及優點的花椰菜鳳山2號，以下敘述何者錯誤?","栽培天期約31-35天","可在日溫35至45℃下生長。"	,"適合台灣5-9月栽培",	"能在設施內高溫環境下自然結球。");
//            dbHper.insertRec("以下選項不是在描述棗台農13號雪麗?","甜中帶酸、澀度低","產期較早，果實成熟較不會因高溫導致黃熟過快。"	,"果皮表面具臘質、光亮、無褐斑",	"著果量適當，可減少疏果工作");
//            dbHper.insertRec("下列敘述何者為櫻花台農3號大白的特性?","無雄蕊，雌蕊葉化","花萼綠色、有絨毛。"	,"平均花瓣數18片",	"平均花朵直徑2.9公分");
//            dbHper.insertRec("以下敘述不是芥藍鳳山1號的特性?","葉色為濃綠色、葉片較厚","全株纖維少，口感細緻。"	,"比起過去商業品種，較具甜味而不苦",	"可適應高溫、高濕環境栽培");
//            dbHper.insertRec("下列作物何者的產期沒有夏季?","枇杷","水蜜桃","荔枝","蓮霧");
//            dbHper.insertRec("下列哪一月份適逢紅龍果的產期?","8月","5月","2月","12月");
//            dbHper.insertRec("下列關於芭樂的產期敘述何者正確?","全年","3~5月","6~8月","7~10月");
//        }
        recSet=dbHper.getRecSet_Q0300();
        //Toast.makeText(getApplicationContext(), Integer.toString(recSet.size()), Toast.LENGTH_LONG).show();

    }
    //    private RadioGroup.OnCheckedChangeListener rb01on = new RadioGroup.OnCheckedChangeListener() {
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            switch (checkedId){
//                case R.id.q0300_rb001:
//
////                    intent01.putExtra("class_title", getString(R.string.q0300_b001));
////                    intent01.setClass(Q0300.this, Q0301.class);
////                    startActivity(intent01);
//                    //resetrb();
//                    break;
//                case R.id.q0300_rb002:
//                    //resetrb();
////                    intent01.putExtra("class_title", getString(R.string.q0300_b002));
////                    intent01.setClass(Q0300.this, Q0302.class);
////                    startActivity(intent01);
//                    //resetrb();
//                    break;
//                case R.id.q0300_rb003:
//                    //resetrb();
////                    intent01.putExtra("class_title", getString(R.string.q0300_b003));
////                    intent01.setClass(Q0300.this, Q0303.class);
////                    startActivity(intent01);
//                    //resetrb();
//                    break;
//
//            }
//
//        }
//    };
//    public void music_set() {
//        if (startmusic.isPlaying()) {
//            startmusic.stop();
//            try {
//                startmusic.prepare();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    private void dbmysql() {
        //String sqlctl = "SELECT * FROM quiz ORDER BY id ASC";
        String sqlctl ="SELECT * FROM q0300 ORDER BY q0300.id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(sqlctl);
        try {
            String result = Q0300_connectDB.executeQuery_Q0300(nameValuePairs);
            /**************************************************************************
             * SQL 結果有多筆資料時使用JSONArray
             * 只有一筆資料時直接建立JSONObject物件 JSONObject
             * jsonData = new JSONObject(result);
             **************************************************************************/
            //==========================================
            chk_httpstate();  //檢查 連結狀態
            //==========================================
            JSONArray jsonArray = new JSONArray(result);
            // -------------------------------------------------------
            if (jsonArray.length() > 0) { // MySQL 連結成功有資料
                //Toast.makeText(getApplicationContext(), "MySQL 連結成功有資料 ", Toast.LENGTH_SHORT).show();
                int rowsAffected = dbHper.clearRec_Q0300();                 // 匯入前,刪除所有SQLite資料
                // 處理JASON 傳回來的每筆資料
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    ContentValues newRow = new ContentValues();
                    // --(1) 自動取的欄位 --取出 jsonObject 每個欄位("key","value")-----------------------
                    Iterator itt = jsonData.keys();
                    while (itt.hasNext()) {
                        String key = itt.next().toString();
                        String value = jsonData.getString(key); // 取出欄位的值
                        if (value.equals(null) ) {//== null
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
                    long rowID = dbHper.insertRec_m_Q0300(newRow);

                }
                Toast.makeText(getApplicationContext(), "共匯入 " + Integer.toString(jsonArray.length()) + " 筆資料", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "匯入");
                // ---------------------------
            } else {
                Toast.makeText(getApplicationContext(), "主機資料庫無資料", Toast.LENGTH_LONG).show();
            }
            recSet = dbHper.getRecSet_Q0300();  //重新載入SQLite
            //u_setspinner();
            // --------------------------------------------------------
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }
    private void chk_httpstate() {
        //**************************************************
//*       檢查連線狀況
//**************************************************
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)

        int servermsgcolor= ContextCompat.getColor(this, R.color.Navy);;
        if (Q0300_connectDB.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + Q0300_connectDB.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
//                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",
//                        Toast.LENGTH_SHORT).show();
        } else {
            int checkcode = Q0300_connectDB.httpstate / 100;
            switch (checkcode) {
                case 1:
                    ser_msg = "資訊回應(code:" + Q0300_connectDB.httpstate + ") ";
                    break;
                case 2:
                    ser_msg = "已經完成由伺服器會入資料(code:" + Q0300_connectDB.httpstate + ") ";
                    break;
                case 3:
                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + Q0300_connectDB.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + Q0300_connectDB.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + Q0300_connectDB.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
            }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }
        if (Q0300_connectDB.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + Q0300_connectDB.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Red);
        }
        Toast.makeText(this, ser_msg, Toast.LENGTH_SHORT).show();
//        b_servermsg.setText(ser_msg);
//        b_servermsg.setTextColor(servermsgcolor);

        //-------------------------------------------------------------------
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
//            case R.id.q0300_rb001:
//                intent01.putExtra("class_title", "初級");
//                intent01.setClass(Q0300.this, Q0301.class);
//                startActivity(intent01);
//                break;
//            case R.id.q0300_rb002:
//                intent01.putExtra("class_title", "中級");
//                intent01.setClass(Q0300.this, Q0302.class);
//                startActivity(intent01);
//                break;
//            case R.id.q0300_rb003:
//                intent01.putExtra("class_title", "高級");
//                intent01.setClass(Q0300.this, Q0303.class);
//                startActivity(intent01);
//                break;
            case 4:
                if(rb001.isChecked()){
                    intent01.putExtra("class_title", getString(R.string.q0300_b001));
                    intent01.setClass(Q0300.this, Q0301.class);
                    startActivity(intent01);
                }
                if(rb002.isChecked()){
                    intent01.putExtra("class_title", getString(R.string.q0300_b002));
                    intent01.setClass(Q0300.this, Q0302.class);
                    startActivity(intent01);
                }
                if(rb003.isChecked()){
                    intent01.putExtra("class_title", getString(R.string.q0300_b003));
                    intent01.setClass(Q0300.this, Q0303.class);
                    startActivity(intent01);
                }

                break;
            case 5:
                intent01.putExtra("class_title", getString(R.string.q0300_b005));
                intent01.setClass(Q0300.this, Q0311.class);
                startActivity(intent01);
                break;
            case 6:
                intent01.putExtra("class_title", getString(R.string.q0300_b006));
                intent01.setClass(Q0300.this, Q0312.class);
                startActivity(intent01);
                break;
            case 7:
                intent01.putExtra("class_title", getString(R.string.q0300_b007));
                intent01.setClass(Q0300.this, Q0200.class);
                startActivity(intent01);
                break;


        }



    };
//========生命週期============



    @Override
    protected void onResume() {
        super.onResume();
        //this.setupViewComponent();
        //resetrb();
        Toast.makeText(getApplicationContext(), "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        music_set();
        //this.finish();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        music_set();

    }
    @Override
    protected void onStop() {
        super.onStop();
//        music_set();
        Toast.makeText(getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
        //this.finish();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
//        music_set();
        //this.finish();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
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
        switch(item.getItemId()){
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