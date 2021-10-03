package tw.tcnrcloud110.quiz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Q0200 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "tcnrcloud=>";
    private static final int RC_SIGN_IN = 9001;
    private static final String DB_FILE = "friends.db";
    private static final int DB_version = 1;
    private static final String DB_TABLE_Q0312_1 = "q0312_1";//初 rank
    private static final String DB_TABLE_Q0312_2 = "q0312_2";//中 rank
    private static final String DB_TABLE_Q0312_3 = "q0312_3";//高 rank
    //public boolean account_state=false;
    public String account_Name;
    //------------------------DataBase----------------------
    public String account_Mail;
    private TextView mStatusTextView;
    private GoogleSignInClient mGoogleSignInClient;
    private Uri User_IMAGE;
    private CircleImgView img;
    private DbHelper dbHper;
    private ArrayList<String> recSet;
    CheckUser cu ;

    //--------------------------------------------
    public static Bitmap getBitmapFromURL(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0200);
        setupViewComponent();
        initDB();
    }

    private void setupViewComponent() {
        //---------------------------------------
        Intent intent = this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
        //---------------------------------------宣告
        mStatusTextView = findViewById(R.id.status);
        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // --START configure_signin--
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .build();
        // --END configure_signin--

        // --START build_client--
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //--END build_client--

        // --START customize_button--
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // --END customize_button--
    }

    private void initDB() {
        if (dbHper == null) {
            dbHper = new DbHelper(this, DB_FILE, null, DB_version);
        }
//        recSet = dbHper_M.getRecSet();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //account_state=true;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //--START_EXCLUDE--
                        updateUI(null);
                        // [END_EXCLUDE]
                        img.setImageResource(R.drawable.googleg_color); //還原圖示
                        //account_state=false;
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // --START_EXCLUDE--
                        updateUI(null);
                        // --END_EXCLUDE--
                        img.setImageResource(R.drawable.googleg_color); //還原圖示
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        cu=new CheckUser(this);
        // Check if the user is already signed in and all required scopes are granted
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && GoogleSignIn.hasPermissions(account, new Scope(Scopes.DRIVE_APPFOLDER))) {
            updateUI(account);


        } else {
            updateUI(null);

        }
    }
    //--END onActivityResult--

    // --START onActivityResult--
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // --TART handleSignInResult--
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);

        }
    }

    // --END handleSignInResult--
//---------------------------------
    private void updateUI(GoogleSignInAccount account) {
        GoogleSignInAccount aa = account;
        int aaa = 1;
        String g_Email = "";
        String g_GivenName = "";
        String g_FamilyName = "";
        if (account != null) {

//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
//            String g_DisplayName=account.getDisplayName(); //暱稱
            g_Email = account.getEmail();  //信箱
            g_GivenName = account.getGivenName(); //Firstname
            g_FamilyName = account.getFamilyName(); //Last name

            mStatusTextView.setText(getString(R.string.q0200_signed_in_fmt, account.getDisplayName()) + "\n Email:" +
                    account.getEmail() + "\n Firstname:" +
                    account.getGivenName() + "\n Last name:" +
                    account.getFamilyName()
            );

            //-------------------------
//            String g_id=account.getId();
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_button).setVisibility(View.GONE);
            //---------------------------------------------------------------------------------------
//            mysql_insert(g_Email, g_GivenName, g_FamilyName);
            // mysql_insert(DB_TABLE_Q0312_1 ,account_Name, account_Mail, 0);
            //insert(g_GivenName, g_Email);

            //account_state=true;
            account_Name = account.getFamilyName() + account.getGivenName();
            account_Mail = account.getEmail();

            //insert(account_Mail,account_Name);
            mysql_insert_q0312(DB_TABLE_Q0312_1, account_Name, account_Mail, 0);
            mysql_insert_q0312(DB_TABLE_Q0312_2, account_Name, account_Mail, 0);
            mysql_insert_q0312(DB_TABLE_Q0312_3, account_Name, account_Mail, 0);
            TextView t001 = (TextView) findViewById(R.id.title_text);
            t001.setVisibility(View.GONE);
            cu=new CheckUser(this);

            //-------改變圖像--------------
            User_IMAGE = account.getPhotoUrl();
            if (User_IMAGE == null) {
//                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//                findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
                return;
            }
            img = (CircleImgView) findViewById(R.id.google_icon);


//           String ss="http://................."        ;
//            Bitmap bbb = getBitmapFromURL(String ss);
//            img.setImageBitmap(bbb);


            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... params) {
                    String url = params[0];
                    return getBitmapFromURL(url);
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    img.setImageBitmap(result);
                    super.onPostExecute(result);
                }
            }.execute(User_IMAGE.toString().trim());
        } else {
            mStatusTextView.setText(R.string.q0200_status_signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            //account_state=false;
            TextView t001 = (TextView) findViewById(R.id.title_text);
            t001.setVisibility(View.VISIBLE);
            cu=new CheckUser(this);
        }
    }

    private void mysql_insert_q0312(String table, String name, String mail, int score) {
        //        sqlctl = "SELECT * FROM member ORDER BY id ASC";
        ArrayList<Object> nameValuePairs = new ArrayList<>();
        //        nameValuePairs.add(sqlctl);
        nameValuePairs.add(table);
        nameValuePairs.add(name);
        nameValuePairs.add(mail);
        nameValuePairs.add(score);

        try {
            Thread.sleep(500); //  延遲Thread 睡眠0.5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //-----------------------------------------------
        String exitaccount = Q0300_connectDB.executefind_Q0312(nameValuePairs);
        Log.d(TAG, exitaccount);
        //Log.d(TAG,Integer.toString(exitaccount.length()));
        if (exitaccount.length() < 10) {//如果不存在
            String result = Q0300_connectDB.executeInsert_Q0312(nameValuePairs);  //真正執行新增
            Log.d(TAG, result);
        }

        //-----------------------------------------------
    }


    //-------------------------------------------生命週期----------------------------------------
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    //-----------------------------------Menu-----------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0300, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.q0300_m_back:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chk_httpstate() {
        //**************************************************
//*       檢查連線狀況
//**************************************************
        //存取類別成員 DBConnector01.httpstate 判定是否回應 200(連線要求成功)
        String ser_msg = "";
        int servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
        if (Q0300_connectDB.httpstate == 200) {
            ser_msg = "伺服器匯入資料(code:" + Q0300_connectDB.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Navy);
//                Toast.makeText(getBaseContext(), "由伺服器匯入資料 ",
//                        Toast.LENGTH_SHORT).show();
        } else {
            int checkcode = Q0300_connectDB.httpstate / 100;
            switch (checkcode) {
                case 1:
                    ser_msg = "資訊回應(code:" + Q0300_connectDB.httpstate + ") ";
                    break;
                case 2:
                    ser_msg = "已經完成由伺服器會入資料(code:" + Q0300_connectDB.httpstate + ") ";
                    break;
                case 3:
                    ser_msg = "伺服器重定向訊息，請稍後在試(code:" + Q0300_connectDB.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 4:
                    ser_msg = "用戶端錯誤回應，請稍後在試(code:" + Q0300_connectDB.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
                case 5:
                    ser_msg = "伺服器error responses，請稍後在試(code:" + Q0300_connectDB.httpstate + ") ";
                    servermsgcolor = ContextCompat.getColor(this, R.color.Red);
                    break;
            }
//                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
        }
        if (Q0300_connectDB.httpstate == 0) {
            ser_msg = "遠端資料庫異常(code:" + Q0300_connectDB.httpstate + ") ";
            servermsgcolor = ContextCompat.getColor(this, R.color.Red);
        }
        Toast.makeText(this, ser_msg, Toast.LENGTH_SHORT).show();
//        b_servermsg.setText(ser_msg);
//        b_servermsg.setTextColor(servermsgcolor);

        //-------------------------------------------------------------------
    }


}