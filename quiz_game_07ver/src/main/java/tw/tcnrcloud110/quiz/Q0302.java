package tw.tcnrcloud110.quiz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


//import java.util.List;
//import java.util.Map;


public class Q0302 extends AppCompatActivity implements View.OnClickListener {
    //private Intent intent01=new Intent();
    //private List<Map<String, Object>> mList;
    private static final String DB_FILE = "QuizeGame.db";
    //private static final String DB_TABLE = "quiz";
    private static final int DBversion = 1;
    private final double qscore = 100;
    private final double[] qbonus = {1.0, 1.1, 1.2, 1.3, 1.4};
    private final int max = 200;
    private final int[] qtime = {200, 180, 160, 150};
    private final Handler handler = new Handler();
    //private final Handler qhandler = new Handler();
    private final int questnum = 10;
    private final DecimalFormat df = new DecimalFormat("000000");
    private final String TAG = "tcnr05=>";
    private TextView t001, t002, t003, t004;
    //private GoogleSignInAccount account;
    private String account_name;
    private String account_mail;
    private RelativeLayout lay01;
    private Button b001, b002, b003, b004;
    private Button b005;
    private ProgressBar pb001;
    private MediaPlayer bgmmusic, startmusic, finalmusic, correctmusic, errormusic;
    //private  String[] ans={"1","2","3","4","5"};
    private String[] ans;
    private double tscore = 0;
    private int count = 0;
    private int stateflag = 0;
    private pb_go pgwork;
    //private  String[] quest;
    private int qstate = 0;
    private DbHelper dbHper;
    private ArrayList<String> recSet;
    private int[] arr;
    private int[] ansb;
    private final View.OnClickListener starton = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_LONG).show();

            if (v.getId() == R.id.q0301_b005) {
                handler.removeCallbacksAndMessages(null);
                pgwork = new pb_go();
                pb001.setMax(qtime[qstate]);
                pgwork.setMax(qtime[qstate]);
                pgwork.setHandler(handler);
                pgwork.setProgressBar(pb001);

                //case R.id.q0301_b005:
                //qhandler.removeCallbacksAndMessages(null);
                if (stateflag == 0 && count < ans.length) {//START
                    Log.d(TAG, "start");
                    bgmmusic.stop();
                    b005.setVisibility(View.INVISIBLE);
                    tscore = 0;
                    qstate = 0;
                    t001.setText(df.format(tscore));
                    t002.setText("");
                    t003.setText("");
                    //t004.setText(quest[0]);
                    setquest(0);
                    Log.d(TAG, "setquest");
                    stateflag = 1;
                    if (startmusic.isPlaying()) {
                        startmusic.stop();
                        try {
                            startmusic.prepare();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d(TAG, "music");
                    startmusic.start();
                    pgwork.start();
                    pgwork.checkAccess();
                    Log.d(TAG, "pgwork");
                }
                //break;
            }
//            else if(stateflag == 0 && count==ans.length-1){
//                stateflag=0;
//                if (finalmusic.isPlaying()) {
//                    finalmusic.stop();
//                    try {
//                        finalmusic.prepare();
//                    } catch (IllegalStateException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

        }
    };
    private MenuItem item1;
    private TextView t005, t006;
    private int combo = 0;
    private TextView t007, t008;
    //    private int life=3;
    private ImageView life1, life2, life3;
    private TextView t009;
    private boolean account_state;
    private final DialogInterface.OnClickListener altOn = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startmusic.stop();
            finalmusic.stop();
            bgmmusic.stop();
            pgwork.stopThread();
            //qhandler.removeCallbacksAndMessages(null);
            handler.removeCallbacksAndMessages(null);
            switch (which) {
                case DialogInterface.BUTTON_NEUTRAL:
                    if (account_state) {
                        //dbHper.updateRec_Q0312_2(account.getFamilyName()+account.getGivenName(),account.getEmail(),(int)tscore);
                        //        sqlctl = "SELECT * FROM member ORDER BY id ASC";
                        ArrayList<Object> nameValuePairs = new ArrayList<>();
                        //        nameValuePairs.add(sqlctl);
                        nameValuePairs.add("q0312_2");
                        nameValuePairs.add(account_name);
                        nameValuePairs.add(account_mail);
                        nameValuePairs.add((int) tscore);
                        String result = Q0300_connectDB.executeupdate_Q0312(nameValuePairs);
                        Log.d(TAG, result);
                        try {
                            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //-----------------------------------------------

                    }
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    startmusic.stop();
                    finalmusic.stop();
                    bgmmusic.stop();
                    pgwork.stopThread();
                    //qhandler.removeCallbacksAndMessages(null);
                    handler.removeCallbacksAndMessages(null);
                    if (dbHper != null) {
                        dbHper.close();
                        dbHper = null;
                    }
                    setupViewComponent();
                    break;

                case DialogInterface.BUTTON_POSITIVE:
                    startmusic.stop();
                    finalmusic.stop();
                    bgmmusic.stop();
                    pgwork.stopThread();
                    //qhandler.removeCallbacksAndMessages(null);
                    handler.removeCallbacksAndMessages(null);
                    finish();
                    break;
            }

        }
    };

    //    private ArrayList<String> quest=new ArrayList<String>();;
//    private ArrayList<String> questans=new ArrayList<String>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0301);
        setupViewComponent();

    }

    private void setupViewComponent() {
        count = 0;
        qstate = 0;
        stateflag = 0;
        tscore = 0;
        combo = 0;
        //life=3;
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        Log.d(TAG, "setupViewComponent");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int buttonwidth = displayMetrics.widthPixels * 4 / 9;
        //int buttondisth = displayMetrics.heightPixels /20;
        int buttonheight = displayMetrics.heightPixels / 8;
        int questtexth = displayMetrics.heightPixels * 2 / 7;
        //int buttondistw = displayMetrics.widthPixels /5;
        lay01 = findViewById(R.id.q0301_rl001);
        lay01.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_trans_in));
        t001 = findViewById(R.id.q0301_t001);
        t002 = findViewById(R.id.q0301_t002);
        t003 = findViewById(R.id.q0301_t003);
        t004 = findViewById(R.id.q0301_t004);
        t004.setText("");

        t005 = findViewById(R.id.q0301_t005);
        t006 = findViewById(R.id.q0301_t006);
        t007 = findViewById(R.id.q0301_t007);
        t007.setText("");
        t009 = findViewById(R.id.q0301_t009);
        t009.setVisibility(View.INVISIBLE);
        life1 = findViewById(R.id.q0301_life01);
        life2 = findViewById(R.id.q0301_life02);
        life3 = findViewById(R.id.q0301_life03);
        life1.setVisibility(View.INVISIBLE);
        life2.setVisibility(View.INVISIBLE);
        life3.setVisibility(View.INVISIBLE);
        t004.getLayoutParams().height = questtexth;
        t005.setText("");
        t006.setText("");

        pb001 = findViewById(R.id.q0301_pb001);


        //pgwork.start();
        //pgwork.checkAccess();
        b001 = findViewById(R.id.q0301_b001);
        b002 = findViewById(R.id.q0301_b002);
        b003 = findViewById(R.id.q0301_b003);
        b004 = findViewById(R.id.q0301_b004);

        t002.getLayoutParams().width = displayMetrics.widthPixels / 3;
        t003.getLayoutParams().width = displayMetrics.widthPixels / 3;

        b001.getLayoutParams().width = buttonwidth;
        b002.getLayoutParams().width = buttonwidth;
        b003.getLayoutParams().width = buttonwidth;
        b004.getLayoutParams().width = buttonwidth;
        b001.getLayoutParams().height = buttonheight;
        b002.getLayoutParams().height = buttonheight;
        b003.getLayoutParams().height = buttonheight;
        b004.getLayoutParams().height = buttonheight;
        b001.setOnClickListener(this);
        b002.setOnClickListener(this);
        b003.setOnClickListener(this);
        b004.setOnClickListener(this);
        b005 = findViewById(R.id.q0301_b005);
        b005.setVisibility(View.VISIBLE);
        //b005.setMaxWidth(buttonwidth);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                buttonwidth, buttonheight);

        lp.addRule(RelativeLayout.BELOW, t001.getId());
        lp.setMargins(displayMetrics.widthPixels * 3 / 11, displayMetrics.heightPixels / 3, 0, 0);
        b005.setLayoutParams(lp);
//        b005.getLayoutParams().width=displayMetrics.widthPixels /5;
//        b005.getLayoutParams().height=displayMetrics.1widthPixels /5;

        bgmmusic = MediaPlayer.create(Q0302.this, R.raw.q0300_bg03);
        startmusic = MediaPlayer.create(Q0302.this, R.raw.q0300_bg02);
        finalmusic = MediaPlayer.create(Q0302.this, R.raw.q0300_bg01);
        correctmusic = MediaPlayer.create(Q0302.this, R.raw.q0300_c01);
        errormusic = MediaPlayer.create(Q0302.this, R.raw.q0300_e01);

        bgmmusic.start();
        //startmusic.start();
        //loadquest();
        InitDB();
        Log.d(TAG, "InitDB");
//        dbmysql();
//        Log.d(TAG, "dbmysql");

        ans = new String[questnum];

        handler.post(new Runnable() {
            @Override
            public void run() {

                loadquest();
                Log.d(TAG, "loadquest");
            }
        });

//        setquest(0);
        b005.setOnClickListener(starton);

    }

    private void InitDB() {

        if (dbHper == null) {
            dbHper = new DbHelper(this, DB_FILE, null, DBversion);//factory:null不用連線

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
        recSet = dbHper.getRecSet_Q0300();
        //Toast.makeText(getApplicationContext(), Integer.toString(recSet.size()), Toast.LENGTH_LONG).show();

    }

    private void loadquest() {
        Log.d(TAG, "loadquest");
        final ProgressDialog progdlg = new ProgressDialog(this);
        progdlg.setTitle(getString(R.string.q0301_title));
        progdlg.setTitle(getString(R.string.q0301_message));
        progdlg.setIcon(android.R.drawable.btn_star_big_on);
        progdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progdlg.setCancelable(false);
        progdlg.setMax(questnum);
        progdlg.getWindow().setBackgroundDrawableResource(R.color.light_blue);
        progdlg.show();


        //int p=0;
        //progdlg.setProgress(p);


        //mList = new ArrayList<Map<String, Object>>();
        if (recSet.size() >= questnum) {
            arr = new int[questnum];
            ansb = new int[questnum];

            for (int i = 0; i < 10; i++) {
                ansb[i] = (int) (Math.random() * 4) + 1;
                arr[i] = (int) (Math.random() * recSet.size());
                for (int j = 0; j < i; j++) {
                    if (arr[j] == arr[i]) {
                        i--;
                        break;
                    }
                }
            }


        }

        progdlg.cancel();
//                while(p<questnum){
//                    p++;
//                    progdlg.setProgress(p);
//
//                }


    }

    private void setquest(int index) {
        //Toast.makeText(getApplicationContext(), "setquest", Toast.LENGTH_LONG).show();
        Log.d(TAG, "setquest");
        //Map<String, Object> item = new HashMap<String, Object>();
        String[] fld = recSet.get(arr[index]).split("#");
        ans[index] = fld[2];
        switch (ansb[index]) {
            case 1:

                t004.setText("("+(index+1)+")."+fld[1]);
                b001.setText(fld[2]);
                b002.setText(fld[3]);
                b003.setText(fld[4]);
                b004.setText(fld[5]);
                break;
            case 2:

                t004.setText("("+(index+1)+")."+fld[1]);
                b001.setText(fld[5]);
                b002.setText(fld[2]);
                b003.setText(fld[3]);
                b004.setText(fld[4]);
                break;
            case 3:

                t004.setText("("+(index+1)+")."+fld[1]);
                b001.setText(fld[4]);
                b002.setText(fld[5]);
                b003.setText(fld[2]);
                b004.setText(fld[3]);
                break;
            case 4:

                t004.setText("("+(index+1)+")."+fld[1]);
                b001.setText(fld[3]);
                b002.setText(fld[4]);
                b003.setText(fld[5]);
                b004.setText(fld[2]);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        handler.removeCallbacksAndMessages(null);
        if (count == ans.length - 2) {
            startmusic.stop();
            finalmusic.start();
        }


        if (count < ans.length && stateflag == 1) {

            switch (v.getId()) {
                case R.id.q0301_b001:
                    if (ans[count].equals(b001.getText().toString())) {
                        setmusic();
                        correctmusic.start();
                        //qhandler.removeCallbacksAndMessages(null);
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + qscore);

                        tscore = tscore + qbonus[qstate] * qscore;
                        t001.setText(df.format(tscore));
                        if (qstate < 3) qstate++;
//                        if(life<=3)life=3;
                        combo++;


                        t005.setText(Integer.toString(combo));
                        t006.setText(getString(R.string.q0301_t006));

                        t007.setText(getString(R.string.q0301_t007));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");
                                t007.setText("");

                            }
                        }, 1000);

                    } else {
                        setmusic();
                        errormusic.start();
                        qstate = 0;
                        combo = 0;
//                        life--;
                        t005.setText("");
                        t006.setText("");
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + "000");

                        t007.setText(getString(R.string.q0301_t008));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");

                                t007.setText("");
                            }
                        }, 1000);
                    }

                    break;
                case R.id.q0301_b002:
                    if (ans[count].equals(b002.getText().toString())) {
                        setmusic();
                        correctmusic.start();
                        //qhandler.removeCallbacksAndMessages(null);
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + qscore);

                        tscore = tscore + qbonus[qstate] * qscore;
                        t001.setText(df.format(tscore));
                        if (qstate <= 3) qstate++;
//                        if(life<=3)life=3;
                        combo++;


                        t005.setText(Integer.toString(combo));
                        t006.setText(getString(R.string.q0301_t006));

                        t007.setText(getString(R.string.q0301_t007));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");
                                t007.setText("");

                            }
                        }, 1000);

                    } else {
                        setmusic();
                        errormusic.start();
                        qstate = 0;
                        combo = 0;
//                        life--;
                        t005.setText("");
                        t006.setText("");
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + "000");

                        t007.setText(getString(R.string.q0301_t008));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");

                                t007.setText("");
                            }
                        }, 1000);
                    }

                    break;
                case R.id.q0301_b003:
                    if (ans[count].equals(b003.getText().toString())) {
                        setmusic();
                        correctmusic.start();
                        //qhandler.removeCallbacksAndMessages(null);
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + qscore);

                        tscore = tscore + qbonus[qstate] * qscore;
                        t001.setText(df.format(tscore));
                        if (qstate <= 3) qstate++;
//                        if(life<=3)life=3;
                        combo++;


                        t005.setText(Integer.toString(combo));
                        t006.setText(getString(R.string.q0301_t006));

                        t007.setText(getString(R.string.q0301_t007));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");
                                t007.setText("");

                            }
                        }, 1000);

                    } else {
                        setmusic();
                        errormusic.start();
                        qstate = 0;
                        combo = 0;
//                        life--;
                        t005.setText("");
                        t006.setText("");
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + "000");

                        t007.setText(getString(R.string.q0301_t008));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");

                                t007.setText("");
                            }
                        }, 1000);
                    }

                    break;
                case R.id.q0301_b004:
                    if (ans[count].equals(b004.getText().toString())) {
                        setmusic();
                        correctmusic.start();
                        //qhandler.removeCallbacksAndMessages(null);
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + qscore);

                        tscore = tscore + qbonus[qstate] * qscore;
                        t001.setText(df.format(tscore));
                        if (qstate <= 3) qstate++;
//                        if(life<=3)life=3;
                        combo++;


                        t005.setText(Integer.toString(combo));
                        t006.setText(getString(R.string.q0301_t006));

                        t007.setText(getString(R.string.q0301_t007));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");
                                t007.setText("");

                            }
                        }, 1000);

                    } else {
                        setmusic();
                        errormusic.start();
                        qstate = 0;
                        combo = 0;
//                        life--;

                        t005.setText("");
                        t006.setText("");
                        t002.setText("x" + qbonus[qstate]);
                        t003.setText("+" + "000");

                        t007.setText(getString(R.string.q0301_t008));

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t002.setText("");
                                t003.setText("");

                                t007.setText("");
                            }
                        }, 1000);
                    }

                    break;


            }
            count++;
            if (count < ans.length) {
//                if (pgwork.isAlive()) {
//                    //pgwork.stopThread();
//                }
                setquest(count);
                //pgwork = new pb_go();
                pb001.setMax(qtime[qstate]);
                pgwork.reset(qtime[qstate]);

//                pgwork.setHandler(handler);
//                pgwork.setProgressBar(pb001);
//                pgwork.start();
//                pgwork.checkAccess();

            } else {//最後一題之後要結束
                if (pgwork.isAlive()) {
                    pgwork.stopThread();

                }
                pb001.setProgress(0);
                stateflag = 0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishquiz();
                    }
                });
            }
//            switch (life){
//
//                case 1:
//                    life1.setVisibility(View.VISIBLE);
//                    life2.setVisibility(View.INVISIBLE);
//                    life3.setVisibility(View.INVISIBLE);
//                    break;
//                case 2:
//                    life1.setVisibility(View.VISIBLE);
//                    life2.setVisibility(View.VISIBLE);
//                    life3.setVisibility(View.INVISIBLE);
//                    break;
//                case 3:
//                    life1.setVisibility(View.VISIBLE);
//                    life2.setVisibility(View.VISIBLE);
//                    life3.setVisibility(View.VISIBLE);
//                    break;
//                default:
//                    life1.setVisibility(View.INVISIBLE);
//                    life2.setVisibility(View.INVISIBLE);
//                    life3.setVisibility(View.INVISIBLE);
//                    break;
//            }
//            if(life<=0){
//                if(pgwork.isAlive()){
//                    pgwork.stopThread();
//                }
//
//                pb001.setProgress(0);
//                stateflag=0;
//                finishquiz();
//            }


        }

    }

    protected void timeout() {
        Log.d(TAG, "timeout");
        handler.removeCallbacksAndMessages(null);
        setmusic();
        errormusic.start();
        qstate = 0;
        combo = 0;
//        life--;
//        switch (life){
//
//            case 1:
//                life1.setVisibility(View.VISIBLE);
//                life2.setVisibility(View.INVISIBLE);
//                life3.setVisibility(View.INVISIBLE);
//                break;
//            case 2:
//                life1.setVisibility(View.VISIBLE);
//                life2.setVisibility(View.VISIBLE);
//                life3.setVisibility(View.INVISIBLE);
//                break;
//            case 3:
//                life1.setVisibility(View.VISIBLE);
//                life2.setVisibility(View.VISIBLE);
//                life3.setVisibility(View.VISIBLE);
//                break;
//            default:
//                life1.setVisibility(View.INVISIBLE);
//                life2.setVisibility(View.INVISIBLE);
//                life3.setVisibility(View.INVISIBLE);
//                break;
//        }
        t005 = findViewById(R.id.q0301_t005);
        t006 = findViewById(R.id.q0301_t006);
        t005.setText("");
        t006.setText("");
        t002.setText("x" + qbonus[qstate]);
        t003.setText("+000");

        t007.setText(getString(R.string.q0301_t008));
//        t002.setVisibility(View.INVISIBLE);
//        t003.setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                t002.setText("");
                t003.setText("");
                t007.setText("");
            }
        }, 1000);
        if (count == ans.length - 2) {
            startmusic.stop();
            finalmusic.start();
        }
        count++;
        if (stateflag == 1 && count < ans.length) {
            //t004.setText(quest[count]);
            setquest(count);
//            if (pgwork.isAlive()) {
//                //pgwork.stopThread();
//            }
                // = new pb_go();
                pb001.setMax(qtime[qstate]);
                pgwork.reset(qtime[qstate]);
//                pgwork.setHandler(handler);
//                pgwork.setProgressBar(pb001);
//                pgwork.start();
//                pgwork.checkAccess();


        } else {//最後一題之後要結束
            if (pgwork.isAlive()) {
                pgwork.stopThread();
            }

            pb001.setProgress(0);
            stateflag = 0;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finishquiz();
                }
            });
        }
//        if(life<=0){
//            if(pgwork.isAlive()){
//                pgwork.stopThread();
//            }
//
//            pb001.setProgress(0);
//            stateflag=0;
//            finishquiz();
//        }


    }

    private void setmusic() {
        if (correctmusic.isPlaying()) {
            correctmusic.stop();
            try {
                correctmusic.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (errormusic.isPlaying()) {
            errormusic.stop();
            try {
                errormusic.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //===============生命週期
    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
//        account = GoogleSignIn.getLastSignedInAccount(this);
//        account_state = account != null;
        CheckUser cu = new CheckUser(this);
        account_state = CheckUser.getAccount_state();
        account_mail = CheckUser.getAccount_mail();
        account_name = CheckUser.getAccount_name();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startmusic.stop();
        finalmusic.stop();
        bgmmusic.stop();
        bgmmusic.release();
        startmusic.release();
        finalmusic.release();
        correctmusic.release();
        errormusic.release();
        pgwork.stopThread();
        //handler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        //this.finish();
    }

    @Override
    public void onBackPressed() {


//        super.onBackPressed();
//        startmusic.stop();
//        finalmusic.stop();
//        bgmmusic.stop();
//        pgwork.stopThread();
//        //qhandler.removeCallbacksAndMessages(null);
//        handler.removeCallbacksAndMessages(null);
//        if (dbHper != null) {
//            dbHper.close();
//            dbHper = null;
//        }
//        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        startmusic.stop();
        finalmusic.stop();
        bgmmusic.stop();
        pgwork.stopThread();
        //qhandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        startmusic.stop();
        finalmusic.stop();
        bgmmusic.stop();
        pgwork.stopThread();
        //qhandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        this.finish();
    }

    private void finishquiz() {

        MyAlertDialog myAltDlg = new MyAlertDialog(Q0302.this);

        myAltDlg.setTitle(getString(R.string.q0301_finish1));
        if (account_state) {
            myAltDlg.setMessage(getString(R.string.q0301_finish2) +
                    t001.getText());
        } else {
            myAltDlg.setMessage(getString(R.string.q0301_finish2) +
                    t001.getText() + "\n" + getString(R.string.q0301_t010));
        }

        myAltDlg.setIcon(android.R.drawable.star_big_on);
        myAltDlg.setCancelable(false);

        myAltDlg.setButton(DialogInterface.BUTTON_POSITIVE,
                getString(R.string.q0301_positive), altOn);
        myAltDlg.setButton(DialogInterface.BUTTON_NEGATIVE,
                getString(R.string.q0301_negative), altOn);
        if (account_state) {
            myAltDlg.setButton(DialogInterface.BUTTON_NEUTRAL,
                    getString(R.string.q0301_neutral), altOn);
        }
        myAltDlg.getWindow().setBackgroundDrawableResource(R.color.light_blue);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.30);

        Window window = myAltDlg.getWindow();
        window.setLayout(width, height);
        if (hasWindowFocus()) {
            myAltDlg.show();
        }


    }

    /////////
    public class pb_go extends Thread {
        private int max = 200;
        //private final int[] qtime = {200, 180, 160, 150};
        // private final int qstate = 0;
        protected int stateflag = 0;
        protected int iDiffSec;
        private Handler mHandler;
        private ProgressBar mProBar;
        private Calendar begin ,now;
        private int tmp_time=0;
        private final Runnable setpb = new Runnable() {
            public void run() {
                mProBar.setProgress(iDiffSec);
            }
        };


        public void run() {
            Log.d(TAG, "pb_go");
            begin = Calendar.getInstance();
            //stateflag=1;
            //mHandler.post(setmax);
            do {

                now = Calendar.getInstance();

                iDiffSec = max - (60 * (now.get(Calendar.MINUTE)
                        - begin.get(Calendar.MINUTE)) + now.get(Calendar.SECOND)
                        - begin.get(Calendar.SECOND)) * 10;
                if(tmp_time != iDiffSec && stateflag == 1){
                    if(iDiffSec >= 0) {
                        mHandler.post(setpb);
                    }
                }
                if(stateflag == 0){
                    break;
                }
                if (iDiffSec < 0) {


                    //mHandler.removeCallbacksAndMessages(null);

                    timeout();

                    //break;
                }


                tmp_time=iDiffSec;
                try{
                    this.sleep(100);
                }catch (Exception e){
                    Log.d(TAG, e.toString());
                }
            } while (true);

            //Log.d(TAG, "end");
            //stopThread();
            //interrupt();
        }

        void setProgressBar(ProgressBar proBar) {
            mProBar = proBar;
        }

        void setHandler(Handler h) {
            mHandler = h;
            stateflag = 1;

        }
        void setMax(int num) {

            max=num;

        }
        void reset(int num) {
            //stateflag = 0;

            max=num;
            begin = Calendar.getInstance();
            //now = Calendar.getInstance();
            stateflag = 1;

//            try{
//                this.sleep(100);
//            }catch (Exception e){
//                Log.d(TAG, e.toString());
//            }
        }

        void stopThread() {

            mHandler.removeCallbacksAndMessages(null);
            stateflag = 0;

            this.interrupt();

//            this.wait();
            //interrupt();
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
                //        startmusic.stop();
                finalmusic.stop();
                bgmmusic.stop();
                pgwork.stopThread();
                //qhandler.removeCallbacksAndMessages(null);
                handler.removeCallbacksAndMessages(null);
                if (dbHper != null) {
                    dbHper.close();
                    dbHper = null;
                }
                finish();

                break;


        }




        return super.onOptionsItemSelected(item);
    }


}
