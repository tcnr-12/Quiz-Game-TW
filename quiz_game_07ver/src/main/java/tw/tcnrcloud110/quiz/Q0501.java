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

import androidx.annotation.NonNull;
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

public class Q0501 extends AppCompatActivity {

    private LinearLayout li01;
    private TextView mTxtResult,mDesc, t_count;
    private RecyclerView recyclerView;
    private TextView u_loading;
    private SwipeRefreshLayout laySwipe;
    private String ul="https://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvQualityFarm.aspx";
    private ArrayList<Map<String, Object>> mList;
    private int total;
    private int t_total;
    private int nowposition;
    private Button b001,b002,b003,b004;
    private Intent intent = new Intent();
    private Uri uri;
    private Intent it;
    private Intent intent52= new Intent();
    private Intent intent53= new Intent();
    private Intent intent54= new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0501main);
        //-------------------------??????Http???cache????????????
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //-------------------------
        setupViewComponent();
    }

    private void setupViewComponent() {
        //------------------------------------------------------Intent
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        //-------------------------------------------------------------
        li01 = (LinearLayout) findViewById(R.id.q0501_ll02);
        li01.setVisibility(View.GONE);//??????????????????scrollbar????????????"??????"
        mTxtResult = (TextView)findViewById(R.id.q0501_t001);
        mDesc =  (TextView)findViewById(R.id.q0501_t002);

        mDesc.setMovementMethod(ScrollingMovementMethod.getInstance());//???????????????????????????
        mDesc.scrollTo(0,0);//?????????textview?????????????????????(?????????)

        recyclerView =(RecyclerView) findViewById(R.id.q0501_recyclerView);
        t_count = (TextView)findViewById(R.id.q0501_count);

        b001=(Button)findViewById(R.id.q0501_b001);//????????????
        b002=(Button)findViewById(R.id.q0501_b002);//????????????
        b003=(Button)findViewById(R.id.q0501_b003);//????????????
        b004=(Button)findViewById(R.id.q0501_b004);//????????????

        b001.setOnClickListener(b001On);
        b002.setOnClickListener(b001On);
        b003.setOnClickListener(b001On);
        b004.setOnClickListener(b001On);

        // ?????????????????? ?????????????????????????????????
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels /3;

        b001.getLayoutParams().width=buttonwidth;
        b002.getLayoutParams().width=buttonwidth;
        b003.getLayoutParams().width=buttonwidth;

        //-----------------recyclerView??????????????????---------
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @org.jetbrains.annotations.NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                li01.setVisibility(View.GONE);
            }

            @Override
            public void onScrolled(@NonNull @org.jetbrains.annotations.NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //-------------??????"???????????????"textview----------
        u_loading = (TextView)findViewById(R.id.q0501_t003);
        u_loading.setVisibility(View.GONE);
        //-------------------------------------
        laySwipe = (SwipeRefreshLayout)findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setSize(SwipeRefreshLayout.LARGE);
        // ????????????????????????????????????????????????
        laySwipe.setDistanceToTriggerSync(400);
        // ???????????????????????????
        laySwipe.setProgressBackgroundColorSchemeColor(getColor(android.R.color.background_light));
        // ??????????????????????????????????????????1????????????
        laySwipe.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple,
                android.R.color.holo_orange_dark);

/*        setProgressViewOffset : ?????????????????????????????????
        ?????????????????????????????????????????????
        ??????????????????????????????????????????????????????????????????
        ??????????????????????????????????????????????????????????????????*/
        laySwipe.setProgressViewOffset(true, 0, 50);
//=====================
        onSwipeToRefresh.onRefresh();  //??????app???????????????????????????onSwipe????????????b001On
    }
    //????????????
    private  final SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mTxtResult.setText("");
            MyAlertDialog myAltDlg = new MyAlertDialog(Q0501.this);
            myAltDlg.setTitle(getString(R.string.q0501_dialog_title));
            myAltDlg.setMessage(getString(R.string.q0501_dialog_t001) + getString(R.string.q0501_dialog_b001));
            myAltDlg.setIcon(android.R.drawable.ic_menu_rotate);
            myAltDlg.setCancelable(false);
            myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.q0501_dialog_positive), altDlgOnClkPosiBtnLis);
            myAltDlg.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.q0501_dialog_neutral), altDlgOnClkNeutBtnLis);
            myAltDlg.show();
        }
    };

    //?????????????????????"????????????"
    private DialogInterface.OnClickListener altDlgOnClkPosiBtnLis=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which){
            laySwipe.setRefreshing(true);
            u_loading.setVisibility(View.VISIBLE);//textview
            mTxtResult.setText(getString(R.string.q0501_name) + "");
            mDesc.setText("");
            mDesc.scrollTo(0, 0);//textview ????????????????????????(??????)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setDatatolist();
                    u_loading.setVisibility(View.GONE);//textview gone
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.q0501_loadover), Toast.LENGTH_SHORT).show();
                }
            },10000);//???????????????10???
        }
    };

    private void setDatatolist() {
        //???JSON ??? RecyclerView
        u_importopendata();  //??????Opendata
        //??????Adapter
        final ArrayList<Q0501_Post> mData = new ArrayList<>();
        for (Map<String, Object> m : mList) {
            if (m != null) {
                String FarmNm_CH = m.get("FarmNm_CH").toString().trim(); //??????
                String Photo = m.get("Photo").toString().trim(); //??????
                if (Photo.isEmpty() || Photo.length() < 1) {
                    Photo = "https://tcnr2021a11.000webhostapp.com/post_img/nopic1.jpg";
                }
                String Address_CH = m.get("Address_CH").toString().trim(); //??????
                String Feature_CH = m.get("Feature_CH").toString().trim(); //??????
                String PCode = m.get("PCode").toString().trim(); //??????
                String Longitude = m.get("Longitude").toString().trim(); //??????
                String Latitude = m.get("Latitude").toString().trim(); //??????
                String WebURL = m.get("WebURL").toString().trim();
                String TEL = m.get("TEL").toString().trim();
                mData.add(new Q0501_Post(FarmNm_CH, Photo, Address_CH, Feature_CH, PCode, Longitude, Latitude, WebURL, TEL));

            } else {
                return;
            }
        }
        Q0501_RecyclerAdapter adapter = new Q0501_RecyclerAdapter(this, mData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new Q0501_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                li01.setVisibility(View.VISIBLE);
                mTxtResult.setText(getString(R.string.q0501_name) + mData.get(position).FarmNm_CH);
                mDesc.setText(mData.get(position).Feature_CH);
                mDesc.scrollTo(0, 0); //textview ?????????
                nowposition = position;
                t_count.setText(getString(R.string.q0501_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void u_importopendata() {//??????Opendata
        try{
//-----------------------------------------------------
            String Task_opendata = new TransTask().execute(ul).get();   //???????????? - ???????????????????????????????????????Open data??????)
            mList = new ArrayList<Map<String, Object>>();
            JSONArray info = new JSONArray(Task_opendata);
            total = 0;
            t_total = info.length(); //?????????
//------JSON ??????----------------------------------------
            info = sortJsonArray(info);
            total = info.length(); //????????????
            t_count.setText(getString(R.string.q0501_ncount) + total + "/" + t_total);
//----------------------------------------------------------
            //??????????????????
            total = info.length();
            t_count.setText(getString(R.string.q0501_ncount) + total);
            for (int i = 0; i < info.length(); i++) {
                Map<String, Object> item = new HashMap<String, Object>();
                String FarmNm_CH = info.getJSONObject(i).getString("FarmNm_CH");
                String Feature_CH = info.getJSONObject(i).getString("Feature_CH");
                String Address_CH = info.getJSONObject(i).getString("Address_CH");
                String Photo = info.getJSONObject(i).getString("Photo");
                String PCode = info.getJSONObject(i).getString("PCode"); //????????????
                String Longitude = info.getJSONObject(i).getString("Longitude");
                String Latitude = info.getJSONObject(i).getString("Latitude");
                String WebURL = info.getJSONObject(i).getString("WebURL");
                String TEL = info.getJSONObject(i).getString("TEL");
                item.put("FarmNm_CH", FarmNm_CH);
                item.put("Feature_CH", Feature_CH);
                item.put("Address_CH", Address_CH);
                item.put("Photo", Photo);
                item.put("PCode", PCode);
                item.put("WebURL", WebURL);
                item.put("Longitude",Longitude);
                item.put("Latitude",Latitude);
                item.put("TEL", TEL);
                mList.add(item);
            }
            t_count.setText(getString(R.string.q0501_ncount) + total + "/" + t_total);
        }catch (JSONException e){
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//----------SwipeLayout ?????? --------
    }

    private JSONArray sortJsonArray(JSONArray jsonArray) {//county??????????????????method
        //County??????????????????Method
        final ArrayList<JSONObject> json = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {  //???????????????ArrayList json???
            try {
                if(
                        jsonArray.getJSONObject(i).getString("PCode").trim().length()>0 &&
                                jsonArray.getJSONObject(i).getString("Photo").trim().length()>0
                                &&!jsonArray.getJSONObject(i).getString("Photo").trim().trim().equals("null")
                                &&jsonArray.getJSONObject(i).getString("WebURL").trim().length()>0
                                &&!jsonArray.getJSONObject(i).getString("WebURL").trim().trim().equals("null")
                                &&jsonArray.getJSONObject(i).getString("WebURL").trim().substring(0,4).equals("http")
                )
                {
                    json.add(jsonArray.getJSONObject(i));
                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
            }
        }
        //---------------------------------------------------------------
        Collections.sort(json, new Comparator<JSONObject>() {
                    @Override
                    public int compare(JSONObject jsonOb1, JSONObject jsonOb2) {
                        // ?????????key ??????
                        String lidCounty = "", ridCounty = "";

                        try {
                            lidCounty = jsonOb1.getString("PCode");
                            ridCounty = jsonOb2.getString("PCode");

                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }
                        return lidCounty.compareTo(ridCounty);
                    }
                }
        );
        return new JSONArray(json);//????????????????????????array
    }

    private DialogInterface.OnClickListener altDlgOnClkNeutBtnLis=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //???????????????
            u_loading.setVisibility(View.GONE);//?????????.????????????
            laySwipe.setRefreshing(false);//??????????????????
        }
    };

    //----------------------------------------------
    private class TransTask extends AsyncTask<String,Void,String> {
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
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????string
    }
    //----------------------------------------------
    private View.OnClickListener b001On=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            u_importopendata();  //??????Opendata
            final ArrayList<Q0501_Post> mBtndata = new ArrayList<>();
            for (Map<String, Object> b : mList) {
                if (b != null) {
                    String Address_CH = b.get("Address_CH").toString().trim(); //??????
                    String Longitude = b.get("Longitude").toString().trim(); //??????
                    String Latitude = b.get("Latitude").toString().trim(); //??????
                    String WebURL = b.get("WebURL").toString().trim(); //??????
                    String TEL = b.get("TEL").toString().trim(); //??????
                    String FarmNm_CH=b.get("FarmNm_CH").toString().trim();//????????????
                    mBtndata.add(new Q0501_BtnPost(Address_CH,Longitude, Latitude, WebURL, TEL, FarmNm_CH));
                } else {
                    return;
                }
            }
            switch (v.getId()){
                case R.id.q0501_b001:
                    String btnweb=mBtndata.get(nowposition).WebURL;
                    uri= Uri.parse(btnweb);
                    it=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(it);
                    break;
                case R.id.q0501_b002://??????
                    String btntel=mBtndata.get(nowposition).TEL;
                    uri = Uri.parse("tel:"+btntel);
                    it = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(it);
                    break;
                case R.id.q0501_b003://??????
                    String btnlat=mBtndata.get(nowposition).Latitude;
                    String btnlong=mBtndata.get(nowposition).Longitude;
                    uri = Uri.parse("https://maps.google.com/maps?f=d&saddr=&daddr="+btnlat+","+btnlong+"&hl=tw");
                    it = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(it);
                    break;
                case R.id.q0501_b004:
                    intent.putExtra("class_title",getString(R.string.q0501_t900));
                    intent.setClass(Q0501.this, Q0501c001.class);
                    String c001_farmname = mBtndata.get(nowposition).FarmNm_CH;
                    String c001_tel = mBtndata.get(nowposition).TEL;
                    intent.putExtra("c001_farmname", c001_farmname);
                    intent.putExtra("c001_tel", c001_tel);
                    startActivity(intent);
                    break;
            }
        }
    };



    //------------------------------------------Menu--------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.q0501_menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                this.finish();
                break;
            case R.id.q0501_menu_top:
                nowposition = 0; // ???????????????
                recyclerView.scrollToPosition(nowposition); // ?????????N?????????
                t_count.setText(getString(R.string.q0501_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;
            case R.id.q0501_menu_next:
                nowposition = nowposition + 100; // N+100?????????
                if (nowposition > total - 1) {
                    nowposition = total - 1;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0501_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;
            case R.id.q0501_menu_back:
                nowposition = nowposition - 100; // N-100?????????
                if (nowposition < 0) {
                    nowposition = 0;
                }
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0501_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;
            case R.id.q0501_menu_end:
                nowposition = total - 1; // ????????????????????????
                recyclerView.scrollToPosition(nowposition);
                t_count.setText(getString(R.string.q0501_ncount) + total + "/" + t_total + "   (" + (nowposition + 1) + ")");
                break;
            case R.id.q0501_menu_load:
                onSwipeToRefresh.onRefresh();  //????????????????????????
                break;
            case R.id.q0501_menu_r001://?????????  with ??????
                //Toast.makeText(getApplicationContext(), "?????????", Toast.LENGTH_SHORT).show();
                intent52.putExtra("class_title",getString(R.string.q0501_t901));
                intent52.setClass(Q0501.this, Q0501c002.class);
                startActivity(intent52);
                break;
            case R.id.q0501_menu_u001://????????????
                intent53.putExtra("class_title",getString(R.string.q0501_t903));
                intent53.setClass(Q0501.this, Q0501c003.class);
                startActivity(intent53);
                break;

            case R.id.q0501_menu_log01://google??????
                intent54.putExtra("class_title",getString(R.string.q0501_menu_log01));
                intent54.setClass(Q0501.this, Q0200.class);
                startActivity(intent54);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
