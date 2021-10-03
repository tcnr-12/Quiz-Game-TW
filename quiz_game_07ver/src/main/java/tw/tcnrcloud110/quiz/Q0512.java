package tw.tcnrcloud110.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class Q0512 extends AppCompatActivity {

    private LinearLayout li01;
    private TextView mTxtResult;
    private TextView mDesc;
    private RecyclerView recyclerView;
    private TextView t_count;
    private TextView u_loading;
    private SwipeRefreshLayout laySwipe;

    private String ul = "https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAttractions.aspx";
    private ArrayList<Map<String, Object>> mList;
    private int total;
    private int t_total;
    private int nowposition;
    private Button b001,b002;

    private Intent intent = new Intent();
    private Uri uri;
    private Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0512);
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
        mTxtResult = (TextView) findViewById(R.id.q0512_name); // 名稱
        mDesc = (TextView) findViewById(R.id.q0512_descr); // 說明

        mDesc.setMovementMethod(ScrollingMovementMethod.getInstance()); // 說明欄位固定滑動大小
        mDesc.scrollTo(0, 0);//textview 回頂端

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView); // recyclerView
        t_count = (TextView) findViewById(R.id.count); // 統計資料數量
        //-------------------------------------------------------
        b001=(Button)findViewById(R.id.q0512_b001);//導航功能
        b002=(Button)findViewById(R.id.q0512_b002);//打電話
        b001.setOnClickListener(b001On);
        b002.setOnClickListener(b001On);
        //---------------------------------------動態調整layout
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mTxtResult.getLayoutParams().width=displayMetrics.widthPixels/40*18;
        b001.getLayoutParams().width=displayMetrics.widthPixels/40*11;
        b002.getLayoutParams().width=displayMetrics.widthPixels/40*11;

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
            MyAlertDialog myAltDlg = new MyAlertDialog(Q0512.this);
            myAltDlg.setTitle(getString(R.string.q0512_dialog_title));
            myAltDlg.setMessage(getString(R.string.q0512_dialog_t001) + getString(R.string.q0512_dialog_b001));
            myAltDlg.setIcon(android.R.drawable.ic_menu_rotate);
            myAltDlg.setCancelable(false); // 是否可以點空白地方 來取消 轉圈圈
            myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.q0512_dialog_positive), altDlgOnClkPosiBtnLis);// "是" 按鍵
            myAltDlg.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.q0512_dialog_neutral), altDlgOnClkNeutBtnLis); // "取消" 按鍵
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
//            mTxtResult.setText(getString(R.string.q0512_name) + "");
            mTxtResult.setText("");
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
                    Toast.makeText(getApplicationContext(), getString(R.string.q0512_loadover), Toast.LENGTH_SHORT).show();
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
        final ArrayList<Q0512_Post> mData = new ArrayList<>();
        for (Map<String, Object> m : mList) {
            if (m != null) {
                String Name = m.get("Name").toString().trim(); //名稱
                String Coordinate = m.get("Coordinate").toString().trim(); // Coordinate
                String Tel = m.get("Tel").toString().trim(); // 電話
                String Address = m.get("Address").toString().trim(); //
                String Photo = m.get("Photo").toString().trim(); //圖片
                if (Photo.isEmpty() || Photo.length() < 1) {
                    Photo = "https://tcnr2021a13.000webhostapp.com/post_img/nopic1.jpg";
                }
                String Introduction = m.get("Introduction").toString().trim(); //產品特色
//                String Zipcode = m.get("Zipcode").toString().trim(); //描述
//************************************************************
                mData.add(new Q0512_Post(Name, Coordinate, Tel, Photo, Address, Introduction));
//************************************************************
            } else {
                return;
            }
        }

        Q0512_RecyclerAdapter adapter = new Q0512_RecyclerAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
// ************************************
        adapter.setOnItemClickListener(new Q0512_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                li01.setVisibility(View.VISIBLE);
//                Toast.makeText(M2205.this, "onclick" + mData.get(position).hotelName.toString(), Toast.LENGTH_SHORT).show();
//                mTxtResult.setText(getString(R.string.q0512_name) + mData.get(position).Name);
                mTxtResult.setText(mData.get(position).Name);
                mDesc.setText(mData.get(position).Introduction);
                mDesc.scrollTo(0, 0); //textview 回頂端
                nowposition = position;
                t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");

            }
        });
//********************************* ****
        recyclerView.setAdapter(adapter);
    }
    //-------------------------------------------------
//    {
//        "ID": "100",
//            "Name": "黃金小鎮休閒農業區",
//            "Tel": "(037) 237-698",
//            "Introduction": "公館鄉位於苗栗縣中央偏西，介於八角崠山賣和後龍溪之間，境內地勢以東部較高，從八角崠山脈脈向後龍溪方向降低，成為苗栗河谷平原，視野寬闊。黃金小鎮區域範圍涵蓋公館鄉以南的四個村，沿線經過館南村、福星村、福基村、石墻村等村落。每至農忙時期，田野間的稻穗呈現一片澄黃，到了5至7月時，台6線兩旁的阿勃勒樹盛開，枝頭一片燦爛金黃，美不勝收，使得黃金小鎮之名不脛而走。經行政院農業委員會98年初公告劃定，黃金小鎮休閒農業區正式成立。\r\r 黃金小鎮休閒農業區仍保有客家村的農業生產，主要作物除了水稻外，還有柿子、芋頭、草苺、福菜、百香果等，早期亦以製陶聞名，尤其是全台唯一的紅棗，近年來更打出知名度，而稻草編織和竹藝也是這裡的特色產業。每到農忙時期，田野間的稻穗呈現一片片黃澄澄，當金黃色的夕陽照耀著大地，更顯美麗；除了美景外，過去典型農業型態漸而轉為多元精緻化，沿線居民結合了陶藝、竹編、觀光農園、特色餐飲、庭園景觀、花卉農場等，讓地方產業更具特色。\r\r 強調健康養生的黃金小鎮，目前已規劃多條自行車道，非常適合慢活樂遊，欣賞田園風光，區內也有多家以紅棗養生或是客家美食為特色的田園餐廳或小吃店。本區連外交通非常便利，且一年到頭有許多產業文化活動和客家節慶民俗活動，不論是開車、搭火車或是國道客運，都能讓遊客方便前往。\r 【如欲安排前往建議事先電話聯繫確認】",
//            "TrafficGuidelines": "",
//            "Address": "苗栗縣公館鄉福基村6鄰154號",
//            "OpenHours": "",
//            "City": "苗栗縣",
//            "Town": "公館鄉",
//            "Coordinate": "24.482159,120.831767",
//            "Photo": "https://ezgo.coa.gov.tw/Uploads/opendata/FUNTHEME01/APPLY_D/20151111165433.jpg"
//    },
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
            t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total);
            //----------------------------------------------------------
            //-----開始逐筆轉換-----
            total = m_JSONArry.length();
            t_count.setText(getString(R.string.q0512_ncount) + total);
            for (int i = 0; i < m_JSONArry.length(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();

                JSONObject jsonData = m_JSONArry.getJSONObject(i);
                String Name = m_JSONArry.getJSONObject(i).getString("Name");
                String Introduction = m_JSONArry.getJSONObject(i).getString("Introduction");
                String Address = m_JSONArry.getJSONObject(i).getString("Address");
                String Coordinate = m_JSONArry.getJSONObject(i).getString("Coordinate");
                String Tel = m_JSONArry.getJSONObject(i).getString("Tel");
                String Photo = m_JSONArry.getJSONObject(i).getString("Photo");

                item.put("Name", Name);
                item.put("Coordinate", Coordinate);
                item.put("Tel", Tel);
                item.put("Introduction", Introduction);
                item.put("Address", Address);
                item.put("Photo", Photo);

                mList.add(item);
//-------------------
            }
            t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total);

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
                    lid = t1.getString("Tel");
                    rid = t2.getString("Tel");
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

    //==========================================================
    private View.OnClickListener b001On=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            u_importopendata();  //下載Opendata
            //==================================
            //設定Adapter
            final ArrayList<Q0512_Post> mData = new ArrayList<>();
            for (Map<String, Object> m : mList) {
                if (m != null) {
                    String Name = m.get("Name").toString().trim(); //名稱
                    String Coordinate = m.get("Coordinate").toString().trim(); // Coordinate
                    String Tel = m.get("Tel").toString().trim(); // 電話
                    String Address = m.get("Address").toString().trim(); //
                    String Photo = m.get("Photo").toString().trim(); //圖片
                    if (Photo.isEmpty() || Photo.length() < 1) {
                        Photo = "https://tcnr2021a13.000webhostapp.com/post_img/nopic1.jpg";
                    }
                    String Introduction = m.get("Introduction").toString().trim(); //產品特色
//                String Zipcode = m.get("Zipcode").toString().trim(); //描述
//************************************************************
                    mData.add(new Q0512_Post(Name, Coordinate, Tel, Photo, Address, Introduction));
//************************************************************
                } else {
                    return;
                }
            }


            switch (v.getId()){



                case R.id.q0512_b001://地圖
                    String btn_C=mData.get(nowposition).Coordinate;

                    uri = Uri.parse("https://maps.google.com/maps?f=d&saddr=&daddr="+btn_C+"&hl=tw");
                    it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                    break;

                case R.id.q0512_b002://電話
                    String btntel=mData.get(nowposition).Tel;
                    uri = Uri.parse("tel:"+btntel);
                    it = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(it);
                    break;

            }
        }

    };

    //==========================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0512_main, menu);
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
                t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_next:
                nowposition = nowposition + 100; // N+100筆資料
                if (nowposition > total - 1) {
                    nowposition = total - 1;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_back:
                nowposition = nowposition - 100; // N-100筆資料
                if (nowposition < 0) {
                    nowposition = 0;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_end:
                nowposition = total - 1; // 跳到最後一筆資料
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0512_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;

            case R.id.menu_load:
                onSwipeToRefresh.onRefresh();  //開始轉圈下載資料
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}//----------END-------------