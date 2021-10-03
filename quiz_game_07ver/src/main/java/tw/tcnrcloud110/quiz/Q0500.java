package tw.tcnrcloud110.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Q0500 extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "tcnr13=>"; // 識別碼
    private Intent intent = new Intent();
    private String mode_title; // 接收從Q4011傳來的值, 標題名稱 Search 台北
    private ImageView img001;
    private ImageView img002;
    private ImageView img003;
    private ImageView img004;
    private ImageView img005;
    private ImageView img006;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0500);
        setupViewComponent();
    }

    private void setupViewComponent() {

        // 設定class標題
        Intent intent = this.getIntent();
        mode_title = intent.getStringExtra("class_title"); // name: 必須跟呼叫的main class設定的一樣
        this.setTitle(mode_title);
        img001 = (ImageView) findViewById(R.id.q0500_img001);
        img002 = (ImageView) findViewById(R.id.q0500_img002);
        img003 = (ImageView) findViewById(R.id.q0500_img003);
        img004 = (ImageView) findViewById(R.id.q0500_img004);
        img005 = (ImageView) findViewById(R.id.q0500_img005);
        img006 = (ImageView) findViewById(R.id.q0500_img006);

        img001.setOnClickListener(this);
        img002.setOnClickListener(this);
        img003.setOnClickListener(this);
        img004.setOnClickListener(this);
//        img005.setOnClickListener(this);
//        img006.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.q0500_img001:
                intent.putExtra("class_title", getString(R.string.q0500_new01));
                intent.setClass(Q0500.this, Q0513.class);
                break;

            case R.id.q0500_img002:
                intent.putExtra("class_title", getString(R.string.q0500_new02));
                intent.setClass(Q0500.this, Q0501.class);
                break;

            case R.id.q0500_img003:
                intent.putExtra("class_title", getString(R.string.q0500_new03));
                intent.setClass(Q0500.this, Q0512.class);
                break;

            case R.id.q0500_img004:
                intent.putExtra("class_title", getString(R.string.q0500_new04));
                intent.setClass(Q0500.this, Q0511.class);
                break;

            case R.id.q0500_img005:
                intent.putExtra("class_title", getString(R.string.q0500_new05));
                intent.setClass(Q0500.this, Q0500_map.class);
                break;

            case R.id.q0500_img006:
                intent.putExtra("class_title", getString(R.string.q0500_new06));
                intent.setClass(Q0500.this, Q0500_map.class);
                break;
            default:
        }
        startActivity(intent);
    }

    // 在Logcat監看生命週期，(Verbose = v, Debug = d, info = i, Warn = w, Error = e, Assert = a.
    // 在Logcat右邊下拉選單選擇，Edit Filter Configuration，把Filter Name 跟 Log Tag 填入。

    // Log = 歷史檔, d = debug
    // 輸入onstart + TAB
    // 或是滑鼠右鍵 > Generate > Override Methods 可以一次創建多個 Methods
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() Q0412");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() Q0412");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() Q0412");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() Q0412");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart() Q0412");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed() Q0412");
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() Q0412");
    }

    //-----------------------------------Menu-----------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}