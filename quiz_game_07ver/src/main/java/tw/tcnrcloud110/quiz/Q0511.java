package tw.tcnrcloud110.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

public class Q0511 extends AppCompatActivity {

    private LinearLayout li01;
    private TextView mTxtResult;
    private TextView mDesc;
    private RecyclerView recyclerView;
    private TextView t_count;
    private TextView u_loading;
    private SwipeRefreshLayout laySwipe;
    private String ul = "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx";
    private ArrayList<Map<String, Object>> mList;
    private int total;
    private int t_total;
    private int nowposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0511);
        setupViewComponent();
    }

    private void setupViewComponent() {
        //---------------------------------------
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        //---------------------------------------
        li01 = (LinearLayout) findViewById(R.id.li01);
        li01.setVisibility(View.GONE); // 用來代替 progress bar 的 linearlayout, 因為 google 建議我們把progress bar 寫出來, 所以先用這個代替他在取消
        mTxtResult = (TextView) findViewById(R.id.q0511_name); // 名稱
        mDesc = (TextView) findViewById(R.id.q0511_descr); // 說明

        mDesc.setMovementMethod(ScrollingMovementMethod.getInstance()); // 說明欄位固定滑動大小
        mDesc.scrollTo(0, 0);//textview 回頂端

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView); // recyclerView
        t_count = (TextView) findViewById(R.id.count); // 統計資料數量

        //--------------------RecyclerView設定 上下滑動------------------
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                li01.setVisibility(View.GONE); // 取消 數據載入中的view
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        //--------------設定下載中-----------
        u_loading = (TextView) findViewById(R.id.u_loading);
        u_loading.setVisibility(View.GONE);
        //-------------------------------------
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setSize(SwipeRefreshLayout.LARGE);
        // 設置下拉多少距離之後開始刷新數據
        laySwipe.setDistanceToTriggerSync(200);
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
            //-------------------------------------對話盒
            mTxtResult.setText("");
            MyAlertDialog myAltDlg = new MyAlertDialog(Q0511.this);
            myAltDlg.setTitle(getString(R.string.q0511_dialog_title));
            myAltDlg.setMessage(getString(R.string.q0511_dialog_t001) + getString(R.string.q0511_dialog_b001));
            myAltDlg.setIcon(android.R.drawable.ic_menu_rotate);
            myAltDlg.setCancelable(false); // 是否可以點空白地方 來取消 轉圈圈
            myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.q0511_dialog_positive), altDlgOnClkPosiBtnLis);// "是" 按鍵
            myAltDlg.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.q0511_dialog_neutral), altDlgOnClkNeutBtnLis); // "取消" 按鍵
            myAltDlg.show();
//------------------------------------
        }
    };

    // 監聽 按下 "是"
    private DialogInterface.OnClickListener altDlgOnClkPosiBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //-----------------開始執行下載----------------
            laySwipe.setRefreshing(true);
            u_loading.setVisibility(View.VISIBLE);
            mTxtResult.setText(getString(R.string.q0511_name) + "");
            mDesc.setText("");
            mDesc.scrollTo(0, 0);//textview 回頂端, 文字介紹 回到最左上
            //------------------------------------------------------
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // =================================
                    setDatatolist();
                    // =================================
                    //----------SwipeLayout 結束 --------
                    //可改放到最終位置 u_importopendata()
                    u_loading.setVisibility(View.GONE);
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.q0511_loadover), Toast.LENGTH_SHORT).show();
                }
            }, 1000); // 延遲1秒, 否則轉圈圈 會馬上消失
        }
    };

    // 監聽 按下 "取消"
    private DialogInterface.OnClickListener altDlgOnClkNeutBtnLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            u_loading.setVisibility(View.GONE); // 取消 數據載入中的view
            laySwipe.setRefreshing(false); // 取消轉圈圈
        }
    };

    private void setDatatolist() { //放JSON 到 RecyclerView
        //==================================
        u_importopendata();  //下載Opendata
        //==================================
        //設定Adapter
        final ArrayList<Q0511_Post> mData = new ArrayList<>();
        for (Map<String, Object> m : mList) {
            if (m != null) {
                String Name = m.get("Name").toString().trim(); //名稱
                String ProduceOrg = m.get("ProduceOrg").toString().trim(); // 出品單位
                String ContactTel = m.get("ContactTel").toString().trim(); // 電話
                String SalePlace = m.get("SalePlace").toString().trim(); //
                String Column1 = m.get("Column1").toString().trim(); //圖片
                if (Column1.isEmpty() || Column1.length() < 1) {
                    Column1 = "https://tcnr2021a13.000webhostapp.com/post_img/nopic1.jpg";
                }
                String Feature = m.get("Feature").toString().trim(); //產品特色
//                String Zipcode = m.get("Zipcode").toString().trim(); //描述
//************************************************************
                mData.add(new Q0511_Post(Name, ProduceOrg, ContactTel, Column1, SalePlace, Feature));
//************************************************************
            } else {
                return;
            }
        }

        Q0511_RecyclerAdapter adapter = new Q0511_RecyclerAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// ************************************
        adapter.setOnItemClickListener(new Q0511_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                li01.setVisibility(View.VISIBLE);
//                Toast.makeText(M2205.this, "onclick" + mData.get(position).hotelName.toString(), Toast.LENGTH_SHORT).show();
                mTxtResult.setText(getString(R.string.q0511_name) + mData.get(position).Name);
                mDesc.setText(mData.get(position).Feature);
                mDesc.scrollTo(0, 0); //textview 回頂端
                nowposition = position;
                t_count.setText(getString(R.string.q0511_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");

            }
        });
//********************************* ****
        recyclerView.setAdapter(adapter);
    }
    //-------------------------------------------------
//////***************************************************************
////    String url = "https://gis.taiwan.net.tw/XMLReleaseALL_public/hotel_C_f.json";  //旅館民宿 - 觀光資訊資料庫
////    Id、Name、Grade、Add、Zipcode、Region、Town、Tel、Fax、Gov、Picture1、Picture2、Picture3、Px、Py、Class、Map、TotalN_oms、Lowest_ice、Ceilin_ice、TaiwanHost、Indust_ail、TotalN_ple、Access_oms、Public_ets、Liftin_ent、Parkin_ace
////    String url = "https://datacenter.taichung.gov.tw/swagger/OpenData/c60986c5-03fb-49b9-b24f-6656e1de02aa";//台中市景點資料
///*# 本例「旅館民宿-觀光資訊資料庫」之片段結構如下：
//# 1.真正的旅館民宿資料位於"Info"這個屬性內，且其結構就是一個list[],
//# 2."Info"又位於"Infos"上層結構內，而"Infos"又位於最外圍上層之"XML_Head"
//#   內，因此存取第一筆旅館民宿資料語法為：
//#   hotelData["XML_Head"]["Infos"]["Info"][0]
//# 3.註：hotelData為透過json.load方法取得之旅館民宿json資料集   */

    /*************************
     ID、Name、Feature、ContactTel、SalePlace、ProduceOrg、Column1
     {
     "ID": "100",
     "Name": "銅鑼杭菊美人茶",
     "Feature": "苗栗縣銅鑼地區於民國50年代開始種植杭菊，由中藥材變農村觀光休閒。今花茶系列飲品加入東方美人茶，沖泡後更有意想不到之風味。\r\n",
     "SalePlace": "苗栗縣銅鑼鄉永樂路22號",
     "ProduceOrg": "銅鑼鄉農會",
     "SpecAndPrice": "金菊75g、杭白菊75g、東方美人茶75g",
     "OrderUrl": "",
     "ContactTel": "(037) 981-531 #18",
     "Column1": "https://ezgo.coa.gov.tw/Uploads/opendata/BuyItem/APPLY_D/20151026161106.jpg"
     },
     *************************   */
//==========================================================
    private void u_importopendata() { // 下載 opendata
        try {
            //-------------------------------
            String Task_opendata = new TransTask().execute(ul).get();   //網址放最上
            //-------解析 json   ---------------------------
            mList = new ArrayList<Map<String, Object>>();
            JSONArray m_JSONArry = new JSONArray(Task_opendata);

            total = 0;
            t_total = m_JSONArry.length(); //總筆數
            //------JSON 排序----------------------------------------
            m_JSONArry = sortJsonArray(m_JSONArry);
            total = m_JSONArry.length(); //有效筆數
            t_count.setText(getString(R.string.q0511_ncount) + total + "/" + t_total);
            //----------------------------------------------------------
            //-----開始逐筆轉換-----
            total = m_JSONArry.length();
            t_count.setText(getString(R.string.q0511_ncount) + total);
            for (int i = 0; i < m_JSONArry.length(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();

                JSONObject jsonData = m_JSONArry.getJSONObject(i);
                String Name = m_JSONArry.getJSONObject(i).getString("Name");
                String Feature = m_JSONArry.getJSONObject(i).getString("Feature");
                String SalePlace = m_JSONArry.getJSONObject(i).getString("SalePlace");
                String ProduceOrg = m_JSONArry.getJSONObject(i).getString("ProduceOrg");
                String ContactTel = m_JSONArry.getJSONObject(i).getString("ContactTel");
                String Column1 = m_JSONArry.getJSONObject(i).getString("Column1");

                item.put("Name", Name);
                item.put("ProduceOrg", ProduceOrg);
                item.put("ContactTel", ContactTel);
                item.put("Feature", Feature);
                item.put("SalePlace", SalePlace);
                item.put("Column1", Column1);

                mList.add(item);
//-------------------
            }
            t_count.setText(getString(R.string.q0511_ncount) + total + "/" + t_total);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//----------SwipeLayout 結束 --------
    }


 public static JSONArray sortJsonArray(JSONArray array) {
        ArrayList<JSONObject> jsons = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject t1, JSONObject t2) {
                String lid = "";
                String rid = "";
                try {
                    lid = t1.getString("ProduceOrg");
                    rid = t2.getString("ProduceOrg");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lid.compareTo(rid);
            }
        });
        return new JSONArray(jsons);
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();  禁用返回鍵
    }

    //==========================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0511_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        li01.setVisibility(View.GONE);
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                break;
            case R.id.menu_top:
                nowposition = 0; // 第一筆資料
                recyclerView.scrollToPosition(nowposition); // 跳到第N筆資料
                t_count.setText(getString(R.string.q0511_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_next:
                nowposition = nowposition + 100; // N+100筆資料
                if (nowposition > total - 1) {
                    nowposition = total - 1;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0511_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_back:
                nowposition = nowposition - 100; // N-100筆資料
                if (nowposition < 0) {
                    nowposition = 0;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0511_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_end:
                nowposition = total - 1; // 跳到最後一筆資料
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0511_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_load:
                onSwipeToRefresh.onRefresh();  //開始轉圈下載資料
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //*********************************************************************
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
}