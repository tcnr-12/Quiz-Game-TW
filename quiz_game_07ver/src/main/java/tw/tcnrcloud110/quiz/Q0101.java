package tw.tcnrcloud110.quiz;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Q0101 extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout lay03;
    private MediaPlayer startmusic;
    private Intent intent = new Intent();
    // ----------------------定時更新------------------------
    private Handler handler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0101);
        setupViewComponent();
        //設定隱藏標題
        getSupportActionBar().hide();
        //設定隱藏狀態
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void setupViewComponent() {
        lay03 = (LinearLayout)findViewById(R.id.q0101_lay03);
        lay03.setOnClickListener(this);

        // --start時片頭音樂-----
        startmusic = MediaPlayer.create(Q0101.this, R.raw.backmusic);
        startmusic.start();

        // ---開機動畫---
        RelativeLayout lay01 = (RelativeLayout)findViewById(R.id.q0101_lay01);
        LinearLayout lay02 = (LinearLayout)findViewById(R.id.q0101_lay02);
        ImageView img001 = (ImageView)findViewById(R.id.q0101_img001);
        TextView t001 = (TextView)findViewById(R.id.q0101_t001);
        lay01.setAnimation(AnimationUtils.loadAnimation(this, R.anim.q0101_anim_alpha_in_01));
        img001.setAnimation(AnimationUtils.loadAnimation(this, R.anim.q0101_anim_alpha_in_01));
        lay02.setAnimation(AnimationUtils.loadAnimation(this, R.anim.q0101_anim_alpha_in_02));
        lay03.setAnimation(AnimationUtils.loadAnimation(this, R.anim.q0101_anim_alpha_in_03));
        t001.setAnimation(AnimationUtils.loadAnimation(this, R.anim.q0101_anim_alpha_in_item01));

        //====================設執行緒=======================
        handler.postDelayed(updateTimer, 27000);  // 設定Delay的時間
        //-------------------------
    }

    //==========================設定執行續========================
    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {

            if (startmusic.isPlaying()) { startmusic.stop(); }
            intent.putExtra("class_title", getString(R.string.q0100_b001));
            intent.setClass(Q0101.this, Q0100.class);
            startActivity(intent);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.q0101_lay03:
                if (startmusic.isPlaying()) { startmusic.stop(); }
                //-----------------
                handler.removeCallbacks(updateTimer);
                //-----------------
                intent.putExtra("class_title", getString(R.string.q0100_b001));
                intent.setClass(Q0101.this, Q0100.class);
                startActivity(intent);
                break;
         }
    };

    //===========================生命週期==========================
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (startmusic.isPlaying()) { startmusic.stop(); }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (startmusic.isPlaying()) { startmusic.stop(); }
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();  禁用返回鍵
    }
}