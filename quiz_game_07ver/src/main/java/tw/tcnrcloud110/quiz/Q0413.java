package tw.tcnrcloud110.quiz;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Q0413 extends AppCompatActivity {
    private String enterFruit; // 接收Q0411傳來的值, 搜尋的水果名稱
    private final String TAG = "tcnr13=>"; // 識別碼
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private Button b001;
    private Intent intent = new Intent();
    private String minguo; // 民國年
    private String str;
    private int n;
    private String date;
    private int nn;
    private String market; // 接收Q0412傳來的值, 標題名稱 Search 台北
    private String marketName; // substring() 後的值(縣市名稱)
    private RecyclerView recyclerView;
    private SwipeRefreshLayout laySwipe;
    private ArrayList<Map<String, Object>> mList;
    private int nowposition;
    private TextView t_count;
    private int total;
    private int t_total;
    private Dialog mydialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0413);
        setupViewComponent();

    }

    private void setupViewComponent() {

        // 設定class標題
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title"); // name: 必須跟呼叫的main class設定的一樣
        enterFruit = intent.getStringExtra("fruit_name"); // 接收從Q0411, Q0412傳來的值, 蔬果名稱
        market = intent.getStringExtra("market_name"); // 接收從Q0412傳來的值, 縣市市場名稱
        minguo = intent.getStringExtra("minguo"); // 接收從Q0411, Q0412傳來的值, 民國
        date = intent.getStringExtra("date"); // 接收從Q0411, Q0412傳來的值, 日期

        // 判斷有沒有從Q0412傳來縣市名稱
        if (market != null) { // 如果 market 不是空值
            marketName = market.substring(7); // "Serach 台北", 只需要第7個之後的字串(從0開始)
        }

        this.setTitle(mode_title);

//        // 抓取當前時間
//        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy");
//        SimpleDateFormat displayFormat1 = new SimpleDateFormat("MM.dd");
//        Date curDate = new Date(System.currentTimeMillis());
//        str = displayFormat.format(curDate); // 西元年
//        date = displayFormat1.format(curDate); // 月份日期
//
//        // 轉換民國時間
//        n = Integer.parseInt(str); // 字串轉整數
//        nn = n - 1911; // 2021 - 1911 = 110
//        minguo = Integer.toString(nn); // 整數轉字串

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView); // recyclerView
        t_count = (TextView) findViewById(R.id.count); // 統計資料數量

        //--------------------RecyclerView設定 上下滑動------------------
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //-------------------------------------
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setSize(SwipeRefreshLayout.LARGE);
        // 設置下拉多少距離之後開始刷新數據
        laySwipe.setDistanceToTriggerSync(500);
        // 設置進度條背景顏色
        laySwipe.setProgressBackgroundColorSchemeColor(getColor(android.R.color.background_light));
        // 設置刷新動畫的顏色，可以設置1或者更多
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark);

/*        setProgressViewOffset : 設置進度圓圈的偏移量。
        第一個參數表示進度圈是否縮放，
        第二個參數表示進度圈開始出現時距頂端的偏移，
        第三個參數表示進度圈拉到最大時距頂端的偏移。*/
        laySwipe.setProgressViewOffset(true, 0, 50);
//=====================
        onSwipeToRefresh.onRefresh();  // 一開始轉圈下載資料
        //-------------------------
    }

    // 監聽 下拉重整頁面
    private final SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //-----------------開始執行下載----------------
            laySwipe.setRefreshing(true);
            //------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // =================================
                    setDatatolist();
                    // =================================
                    //----------SwipeLayout 結束 --------
                    //可改放到最終位置 u_importopendata()
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.q0413_t002), Toast.LENGTH_SHORT).show();
                }
            }, 1000); // 延遲1秒, 否則轉圈圈 會馬上消失
        }
    };

    private void setDatatolist() { //放JSON 到 RecyclerView
        //==================================
        u_importopendata();  //下載Opendata
        //==================================
        //設定Adapter
        final ArrayList<Q0414> mData = new ArrayList<>();
        for (Map<String, Object> m : mList) {
            if (m != null) {
                String TransDate = m.get("TransDate").toString().trim();
                String CropName = m.get("CropName").toString().trim();
                String MarketName = m.get("MarketName").toString().trim();
                String Upper_Price = m.get("Upper_Price").toString().trim();
                String Middle_Price = m.get("Middle_Price").toString().trim();
                String Lower_Price = m.get("Lower_Price").toString().trim();
                String Avg_Price = m.get("Avg_Price").toString().trim();
                String Trans_Quantity = m.get("Trans_Quantity").toString().trim();
//************************************************************
                mData.add(new Q0414(TransDate, CropName, MarketName, Upper_Price, Middle_Price, Lower_Price, Avg_Price, Trans_Quantity));
            } else {
                return;
            }
        }

        Q0415 adapter = new Q0415(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// ************************************
        adapter.setOnItemClickListener(new Q0415.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                nowposition = position;
                t_count.setText(getString(R.string.q0413_t003) + total + " (" + (nowposition + 1) + ")");

//                Q0416 myAltDlg = new Q0416(Q0413.this);
//                myAltDlg.getWindow().setBackgroundDrawableResource(R.color.green_softness);
//                myAltDlg.setTitle("新增一筆備註");
//                myAltDlg.setMessage("記錄這筆價格以及任何你所想的");
//                myAltDlg.setCancelable(false); // 返回鍵關閉
//                myAltDlg.setIcon(android.R.drawable.checkbox_on_background);
//
//                myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, "確定", altDlgOnClkPosiBtnLis);
//                myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", altDlgOnClkNeutBtnLis);
//                myAltDlg.show();

                // 點擊 RecyclerView 的 item 彈出一個 Dialog
                mydialog = new Dialog(Q0413.this);
                mydialog.setTitle(getString(R.string.q0413_t004));
                mydialog.setCancelable(false);
                mydialog.setContentView(R.layout.q0415);  //選擇layout

                // Dialog 的按鍵
                Button loginBtnOK = (Button) mydialog.findViewById(R.id.btnOK);
                Button loginBtnCancel = (Button) mydialog.findViewById(R.id.btnCancel);

                TextView t001 = mydialog.findViewById(R.id.q0415_t001);
                TextView t002 = mydialog.findViewById(R.id.q0415_t002);
                TextView t003 = mydialog.findViewById(R.id.q0415_t003);
                TextView t004 = mydialog.findViewById(R.id.q0415_t004);
                TextView t005 = mydialog.findViewById(R.id.q0415_t005);
                TextView t006 = mydialog.findViewById(R.id.q0415_t006);
                TextView t007 = mydialog.findViewById(R.id.q0415_t007);
                TextView t008 = mydialog.findViewById(R.id.q0415_t008);

                // 傳 item 的值給 Dialog
                t001.setText(mData.get(position).TransDate);
                t002.setText(getString(R.string.q0414_t002) + mData.get(position).CropName);
                t003.setText(getString(R.string.q0414_t003) + mData.get(position).MarketName);
                t004.setText(getString(R.string.q0414_t004) + mData.get(position).Upper_Price);
                t005.setText(getString(R.string.q0414_t005) + mData.get(position).Middle_Price);
                t006.setText(getString(R.string.q0414_t006) + mData.get(position).Lower_Price);
                t007.setText(getString(R.string.q0414_t007) + mData.get(position).Avg_Price);
                t008.setText(getString(R.string.q0414_t008) + mData.get(position).Trans_Quantity);

                mydialog.show();

                // 確認按鍵
                loginBtnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mydialog.dismiss();
                    }
                });

                // 取消按鍵
                loginBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mydialog.dismiss();
                    }
                });
            }
        });
//********************************* ****
        recyclerView.setAdapter(adapter);
    }

    // 監聽 按下 "是"
    private DialogInterface.OnClickListener altDlgOnClkPosiBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    // 監聽 按下 "取消"
    private DialogInterface.OnClickListener altDlgOnClkNeutBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    private void u_importopendata() { // 下載 opendata

        // API 網址
        String ul = "https://data.coa.gov.tw/api/v1/AgriProductsTransType/?Start_time="
                + minguo + "." + date + "&End_time=" + minguo + "." + date + "&CropName=" + enterFruit + "&MarketName=" + marketName;

        try {
            //-------------------------------
            String Task_opendata = new TransTask().execute(ul).get();   // 農產品交易行情
            //-------解析 json   帶有多層結構(共有3層)---------------------------
            mList = new ArrayList<Map<String, Object>>();
            JSONObject json_obj1 = new JSONObject(Task_opendata);
            JSONArray info = json_obj1.getJSONArray("Data");
            total = 0;
            t_total = info.length(); //總筆數
            //------JSON 排序----------------------------------------
            info = sortJsonArray(info);
            total = info.length(); //有效筆數
//            t_count.setText(getString(R.string.q0413_t003) + total + "/" + t_total);
            //----------------------------------------------------------
            //-----開始逐筆轉換-----
            total = info.length();
//            t_count.setText(getString(R.string.q0413_t003) + total);
            for (int i = 0; i < info.length(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();

                String TransDate = info.getJSONObject(i).getString("TransDate");
                String CropName = info.getJSONObject(i).getString("CropName");
                String MarketName = info.getJSONObject(i).getString("MarketName");
                String Upper_Price = info.getJSONObject(i).getString("Upper_Price");
                String Middle_Price = info.getJSONObject(i).getString("Middle_Price");
                String Lower_Price = info.getJSONObject(i).getString("Lower_Price");
                String Avg_Price = info.getJSONObject(i).getString("Avg_Price");
                String Trans_Quantity = info.getJSONObject(i).getString("Trans_Quantity");

                item.put("TransDate", TransDate);
                item.put("CropName", CropName);
                item.put("MarketName", MarketName);
                item.put("Upper_Price", Upper_Price);
                item.put("Middle_Price", Middle_Price);
                item.put("Lower_Price", Lower_Price);
                item.put("Avg_Price", Avg_Price);
                item.put("Trans_Quantity", Trans_Quantity);
                mList.add(item);
//-------------------
            }
            t_count.setText(getString(R.string.q0413_t003) + total);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//----------SwipeLayout 結束 --------
    }

    private JSONArray sortJsonArray(JSONArray jsonArray) {
        //County自定義的排序Method
        final ArrayList<JSONObject> json = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {  //將資料存入ArrayList json中
            try {
                json.add(jsonArray.getJSONObject(i));
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }
        //---------------------------------------------------------------
        Collections.sort(json, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
                        // 用多重key 排序
                        String lidCounty = "", ridCounty = "";
//                String lidStatus="",ridStatus="";
//                String lidPM25="",ridPM25="";
                        try {
                            lidCounty = jsonOb1.getString("TransDate");
                            ridCounty = jsonOb2.getString("TransDate");
//                    lidStatus = jsonOb1.getString("Status");
//                    ridStatus = jsonOb2.getString("Status");
//                    整數判斷方法
//                    if(!jsonOb1.getString("PM2.5").isEmpty()&&!jsonOb2.getString("PM2.5").isEmpty()
//                            &&!jsonOb1.getString("PM2.5").equals("ND")&&!jsonOb2.getString("PM2.5").equals("ND")){
//                        lidPM25=String.format("%02d",Integer.parseInt(jsonOb1.getString("PM2.5")));
//                        ridPM25=String.format("%02d",Integer.parseInt(jsonOb2.getString("PM2.5")));
//                    }else{
//                        lidPM25="0";
//                        ridPM25="0";
//                    }
                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }
//                return lidCounty.compareTo(ridCounty)+lidStatus.compareTo(ridStatus);
                        return lidCounty.compareTo(ridCounty);
                    }
                }
        );
        return new JSONArray(json);//回傳排序縣市後的array
    }

    // google 用的 TransTask
    private class TransTask extends AsyncTask<String, Void, String> {
        String ans;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while (line != null) {
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ans = sb.toString();
            //------------
            return ans;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseJson(s);
        }

        private void parseJson(String s) {
        }
    }

    //==========================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0411, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_top:
                nowposition = 0; // 第一筆資料
                recyclerView.scrollToPosition(nowposition); // 跳到第N筆資料
                t_count.setText(getString(R.string.q0413_t003) + total + " (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_next:
                nowposition = nowposition + 100; // N+100筆資料
                if (nowposition > total - 1) {
                    nowposition = total - 1;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0413_t003) + total + " (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_back:
                nowposition = nowposition - 100; // N-100筆資料
                if (nowposition < 0) {
                    nowposition = 0;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0413_t003) + total + " (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_end:
                nowposition = total - 1; // 跳到最後一筆資料
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0413_t003) + total + " (" + (nowposition + 1) + ")");
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    // Log = 歷史檔, d = debug
    // 輸入onstart + TAB
    // 或是滑鼠右鍵 > Generate > Override Methods 可以一次創建多個 Methods
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() Q0413");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() Q0413");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() Q0413");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() Q0413");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() Q0413");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // 禁用返回鍵
        Log.d(TAG, "onBackPressed() Q0413");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() Q0413");
    }
}

