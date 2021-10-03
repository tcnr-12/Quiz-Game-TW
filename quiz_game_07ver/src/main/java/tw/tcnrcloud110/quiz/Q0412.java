package tw.tcnrcloud110.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Q0412 extends AppCompatActivity {
    private final String TAG = "tcnr13=>"; // 識別碼
    private Button b001;
    private Intent intent = new Intent();
    private ImageButton imb001;
    private ImageButton imb002;
    private ImageButton imb003;
    private ImageButton imb004;
    private String mode_title; // 接收從Q4011傳來的值, 標題名稱 Search 台北
    private String minguo;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0412);
        setupViewComponent();
    }

    private void setupViewComponent() {
//        b001 = (Button) findViewById(R.id.q0412_b001);

        imb001 = (ImageButton) findViewById(R.id.q0412_imb001);
        imb002 = (ImageButton) findViewById(R.id.q0412_imb002);
        imb003 = (ImageButton) findViewById(R.id.q0412_imb003);
        imb004 = (ImageButton) findViewById(R.id.q0412_imb004);

//        b001.setOnClickListener(b001on);

        imb001.setOnClickListener(imb001on);
        imb002.setOnClickListener(imb001on);
        imb003.setOnClickListener(imb001on);
        imb004.setOnClickListener(imb001on);

        // 設定class標題
        Intent intent = this.getIntent();
        mode_title = intent.getStringExtra("class_title"); // name: 必須跟呼叫的main class設定的一樣
        minguo = intent.getStringExtra("minguo"); // 接收從Q0411傳來的值, 民國
        date = intent.getStringExtra("date"); // 接收從Q0411傳來的值, 日期
        this.setTitle(mode_title);
    }

    // 監聽 返回按鍵 回到Produce
//    private View.OnClickListener b001on = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.q0412_b001:
//
//                    // 設定呼叫別之CLASS
//                    // name: 必須跟被呼叫的 class 設定的一樣
//                    intent.putExtra("class_title", getString(R.string.q0411_t001)); // intent 會把 string.q0411_t001 放進到 name = "classs_title"
//                    intent.setClass(Q0412.this, Q0411.class);
//                    break;
//            }
//            startActivity(intent);
//        }
//    };

    // 監聽 蔬果圖片 查詢當日價格
    private View.OnClickListener imb001on = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.q0412_imb001:

                    // 設定呼叫別之CLASS
                    // name: 必須跟被呼叫的 class 設定的一樣
                    intent.putExtra("class_title", getString(R.string.q0411_t002)); // intent 會把 string.q0411_t002 放進到 name = "classs_title"
                    intent.setClass(Q0412.this, Q0413.class);
                    intent.putExtra("market_name", mode_title); // 傳值, 輸入標題的縣市
                    intent.putExtra("fruit_name", getString(R.string.q0412_b002)); // 傳值, 輸入蔬果名稱
                    intent.putExtra("minguo", minguo); // 傳值, 民國
                    intent.putExtra("date", date); // 傳值, 日期
                    break;
                case R.id.q0412_imb002:
                    intent.putExtra("class_title", getString(R.string.q0411_t002)); // intent 會把 string.q0411_t002 放進到 name = "classs_title"
                    intent.setClass(Q0412.this, Q0413.class);
                    intent.putExtra("market_name", mode_title); // 傳值, 輸入標題的縣市
                    intent.putExtra("fruit_name", getString(R.string.q0412_b003)); // 傳值, 輸入蔬果名稱
                    intent.putExtra("minguo", minguo); // 傳值, 民國
                    intent.putExtra("date", date); // 傳值, 日期
                    break;
                case R.id.q0412_imb003:
                    intent.putExtra("class_title", getString(R.string.q0411_t002)); // intent 會把 string.q0411_t002 放進到 name = "classs_title"
                    intent.setClass(Q0412.this, Q0413.class);
                    intent.putExtra("market_name", mode_title); // 傳值, 輸入標題的縣市
                    intent.putExtra("fruit_name", getString(R.string.q0412_b004)); // 傳值, 輸入蔬果名稱
                    intent.putExtra("minguo", minguo); // 傳值, 民國
                    intent.putExtra("date", date); // 傳值, 日期
                    break;
                case R.id.q0412_imb004:
                    intent.putExtra("class_title", getString(R.string.q0411_t002)); // intent 會把 string.q0411_t002 放進到 name = "classs_title"
                    intent.setClass(Q0412.this, Q0413.class);
                    intent.putExtra("market_name", mode_title); // 傳值, 輸入標題的縣市
                    intent.putExtra("fruit_name", getString(R.string.q0412_b005)); // 傳值, 輸入蔬果名稱
                    intent.putExtra("minguo", minguo); // 傳值, 民國
                    intent.putExtra("date", date); // 傳值, 日期
                    break;
                default:
            }
            startActivity(intent);
        }
    };

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() Q0412");
    }
}