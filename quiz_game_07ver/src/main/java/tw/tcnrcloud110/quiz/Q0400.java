package tw.tcnrcloud110.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class Q0400 extends AppCompatActivity {

    private Intent intent = new Intent();
    private ImageView imb001;
    private ImageView imb002;
    private ImageView imb003;
    private ImageView imb004;
    private ImageView imb005;
    private ImageView imb006;
    private MediaPlayer startmusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0400);
        setupViewcomponent();

    }

    private void setupViewcomponent() {

        // --開啟時片頭音樂-----
        startmusic = MediaPlayer.create(Q0400.this, R.raw.qmusic);
        startmusic.start();
//        ScrollView q0400_Linlay001 = new ScrollView.OnClickListener.findViewById(R.id.q0400_Linlay001);
        // 動態調整高度 抓取使用裝置自身的尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels /2;
        int buttondisth = displayMetrics.heightPixels /15;
        int buttondistw = displayMetrics.widthPixels /4;
        int quizbuttonwidth = displayMetrics.widthPixels /4;
        int quizbuttondistw = displayMetrics.widthPixels /4/4;
        //之後有返回鍵才會用到
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);


        // ----巨集的參考物件

        imb001 = (ImageView) findViewById(R.id.q0400_imb001); // 取出參考物件
        imb002 = (ImageView)findViewById(R.id.q0400_imb002);
        imb003 = (ImageView)findViewById(R.id.q0400_imb003);
        imb004 = (ImageView)findViewById(R.id.q0400_imb004);
        imb005 = (ImageView)findViewById(R.id.q0400_imb005);
        imb006 = (ImageView)findViewById(R.id.q0400_imb006);

        imb001.setOnClickListener(sele_btn);
        imb002.setOnClickListener(sele_btn);
        imb003.setOnClickListener(sele_btn);
        imb004.setOnClickListener(sele_btn);
        imb005.setOnClickListener(sele_btn);
        imb006.setOnClickListener(sele_btn);
    }
    private View.OnClickListener sele_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 在 v 這個layout 選擇哪個按鈕被使用
            switch (v.getId()) {


                case R.id.q0400_imb001://改良品種
                    intent.putExtra("class_title", getString(R.string.q0400_imb001));
                    intent.setClass(Q0400.this, Q0401.class);
                    startActivity(intent);
                    break;
                case R.id.q0400_imb002://病蟲
                    intent.putExtra("class_title", getString(R.string.q0400_imb002));
                    intent.setClass(Q0400.this, Q0401a.class);
                    startActivity(intent);
                    break;
                case R.id.q0400_imb003://農業新聞
                    intent.putExtra("class_title", getString(R.string.q0400_imb003));
                    intent.setClass(Q0400.this, Q0421.class);
                    startActivity(intent);
                    break;
                case R.id.q0400_imb004://
                    intent.putExtra("class_title", getString(R.string.q0400_imb004));
                    intent.setClass(Q0400.this, Q0401_Main.class);
                    startActivity(intent);
                    break;

                case R.id.q0400_imb005://產地
                    intent.putExtra("class_title", getString(R.string.q0400_imb005));
                    intent.setClass(Q0400.this, Q0411.class);
                    startActivity(intent);
                    break;

                case R.id.q0400_imb006://育苗場
                    intent.putExtra("class_title", getString(R.string.q0400_imb006));
                    intent.setClass(Q0400.this, Q0422.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//         Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.q0400_music_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.q0400_menu_playmusic:
                if (startmusic.isPlaying()) {

                }else{
                    startmusic = MediaPlayer.create(Q0400.this, R.raw.qmusic);
                    startmusic.start();
                }
                break;
            case R.id.menu_stopmusic:
                if (startmusic.isPlaying()) {
                    startmusic.stop();
                }
                break;
            case R.id.q0400_menu_about:
                new AlertDialog.Builder(Q0400.this)
                        .setTitle(getString(R.string.q0400_menu_about))
                        .setMessage(getString(R.string.q0400_menu_message))
                        .setCancelable(false)
                        .setIcon(android.R.drawable.star_big_on)
                        .setPositiveButton(getString(R.string.q0400_menu_ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .show();

                break;
            case R.id.action_settings:
                if (startmusic.isPlaying()) startmusic.stop();
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}