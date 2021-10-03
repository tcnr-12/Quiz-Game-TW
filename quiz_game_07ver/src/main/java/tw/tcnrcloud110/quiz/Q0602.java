package tw.tcnrcloud110.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;

import java.util.ArrayList;

public class Q0602 extends AppCompatActivity implements View.OnClickListener{

    private Intent intent = new Intent();
    private EditText e001,e002;
    private Button b001;
    private Spinner s001;
    private String e_category, e_title, e_content, e_firstname, e_mail, e_userimage, e_updatetime, e_respond;
    private String mode00,mode01,mode02,mode03,mode04,mode05,mode06,mode_title;
    private String msg= null;
    private String  s_id, s_category;
    private int rowsAffected;
    String TAG = "tcnr12=";
    //------------------------Google 會員登入---------------
    private GoogleSignInAccount ACC;
    //----------------------------DataBase------------------
    private DbHelper dbHper;
    private static final String DB_FILE = "QuizeGame.db";
    private static final int DB_version = 1;
    private int index = 0;
    private ArrayList<String> recSet_Q0600, recSet_Q0200;
    private String[] id00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0602);
        initDB();
        setupViewComponent();
    }

    private void setupViewComponent() {
        // -----------------------------------------------------------------設定class標題
        Intent intent = this.getIntent();
        mode00 = intent.getStringExtra("forum_id");
        mode01 = intent.getStringExtra("sub");
        mode02 = intent.getStringExtra("item");
        mode03 = intent.getStringExtra("date");
        mode04 = intent.getStringExtra("respond");
        mode05 = intent.getStringExtra("member");
        mode06 = intent.getStringExtra("item_text");
        mode_title = intent.getStringExtra("class_title");

        if (mode_title.equals("新增討論")){
            this.setTitle(mode_title);
        }else{
            this.setTitle(mode_title+" : "+mode01);
        }
        //---------------------------------------------------宣告、監聽
        s001 = (Spinner)findViewById(R.id.q0602_s001);
        e001 = (EditText)findViewById(R.id.q0602_e001);
        e002 = (EditText)findViewById(R.id.q0602_e002);
        b001 = (Button)findViewById(R.id.q0602_b001);
            // ---------------設定 spinner item 選項
            ArrayAdapter<CharSequence> adapSpinner = ArrayAdapter
                    .createFromResource(this, R.array.q0602_a001, android.R.layout.simple_spinner_item);
            adapSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s001.setAdapter(adapSpinner);
            //--------------------------------------
        if (mode_title.equals("新增討論")){

        }else{
            TextView t001 = (TextView)findViewById(R.id.q0602_t001);
            b001.setText("確定更新");
            e001.setText(mode02);
            e002.setText(mode06);
            t001.setVisibility(View.GONE);
            s001.setVisibility(View.GONE);
        }
        s001.setOnItemSelectedListener(spinner_item);// 準備 Listener a001Lis 需再設定 Listener
        b001.setOnClickListener(this);
    }
    private Spinner.OnItemSelectedListener spinner_item = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mode_title.equals("新增討論")){
                s_category = parent.getSelectedItem().toString();
            }else{
                s_category = mode01;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void initDB(){
        if (dbHper == null){
            dbHper = new DbHelper(this, DB_FILE, null,DB_version);
        }
        recSet_Q0600 = dbHper.getRecSet_Q0600();
        recSet_Q0200 = dbHper.getRecSet_Q0200();
    }

    private void check_SignIn() {
        // Check if the user is already signed in and all required scopes are granted
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(Scopes.DRIVE_APPFOLDER))) {
            ACC =account;
        } else {
            ACC = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.q0602_b001:
                if (mode_title.equals("新增討論")){
                    String user = ACC.getEmail();
                    String name = ACC.getGivenName();
                    String img = ACC.getPhotoUrl().toString().trim();
                    //=============================執行新增
                    e_category = s_category.trim();
                    e_title = e001.getText().toString().trim();
                    e_content = e002.getText().toString().trim();
                    e_firstname = name;
                    e_mail =  user;
                    e_userimage = img;
                    e_updatetime = "0";
                    e_respond = "0";

                    //----------------------檢查輸入欄位是否空白
                    if (e_title.equals("") || e_content.equals("")){
                        Toast.makeText(getApplicationContext(), "欄位空白無法新增 !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String msg = null;
                    //--------執行SQL-------------
                    long rowID = dbHper.insertRec_Q0600(e_category, e_title, e_content, e_firstname, e_mail,e_userimage,e_updatetime,e_respond);
                    //-------增加到MySQL---------
                    mysql_insert();
                    //------------------------------
                    if (rowID != -1) {
                        msg = "新增成功 !";
                        this.finish();
                    } else {
                        msg = "新增失敗 !";
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    this.finish();
                }else{
                    //=============================執行更新
                    s_id = mode00;
                    e_title = e001.getText().toString().trim();
                    e_content = e002.getText().toString().trim();

                    ArrayList<String> nameValuePairs = new ArrayList<>();
                    nameValuePairs.add(s_id);
                    nameValuePairs.add(e_title);
                    nameValuePairs.add(e_content);
                    try {
                        Thread.sleep(100); //  延遲Thread 睡眠0.5秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //-----------------------------------------------
                    String result = Q0600_DBConnector.executeUpdate_Q0600( nameValuePairs);
                    //-----------------------------------------------
                    rowsAffected = dbHper.updateRec_Q0600(mode00, e_title, e_content);//傳回修改筆數
                    if (rowsAffected == -1) {
                        msg = "討論不存在, 無法修改 !";
                    } else if (rowsAffected == 0) {
                        msg = "討論不存在, 無法修改 !";
                    } else {
                        msg = "更新成功";
                    }
                    Toast.makeText(Q0602.this, msg, Toast.LENGTH_SHORT).show();
                    // ------------------------------------------
                    intent.putExtra("sub", mode01);
                    intent.putExtra("item", e_title);
                    intent.putExtra("settime", mode03);
                    intent.putExtra("respond", mode04);
                    intent.putExtra("firstname", mode05);
                    intent.putExtra("item_text", e_content);
                    intent.putExtra("forum_id", mode00);
                    intent.setClass(Q0602.this, Q0601.class);
                    startActivity(intent);
                }
                break;
        }
    }
//================================寫入MySQL===========================
    private void mysql_insert() {
//        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<String> nameValuePairs = new ArrayList<>();
        //        nameValuePairs.add(sqlctl);
        nameValuePairs.add(e_category);
        nameValuePairs.add(e_title);
        nameValuePairs.add(e_content);
        nameValuePairs.add(e_firstname);
        nameValuePairs.add(e_mail);
        nameValuePairs.add(e_userimage);
        nameValuePairs.add(e_updatetime);
        nameValuePairs.add(e_respond);
        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------
        String result = Q0600_DBConnector.executeInsert_Q0600(nameValuePairs);  //真正執行新增
        //-----------------------------------------------
    }

    //------------------------------生命週期---------------------------
    @Override
    protected void onStart() {
        super.onStart();
        check_SignIn();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dbHper != null) {
            dbHper.close();
            dbHper = null;
        }
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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