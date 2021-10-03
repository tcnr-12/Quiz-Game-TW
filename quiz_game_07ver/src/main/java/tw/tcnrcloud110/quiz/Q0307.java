package tw.tcnrcloud110.quiz;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Q0307 {
    private static String TAG="tcnr05=>";
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    //-------000webhost -------
    //static String connect_ip = "https://oldpa88.com/android_mysql_connect/android_connect_db.php";
    //static String connect_ip ="https://tcnr2021a05.000webhostapp.com/android_mysql_connect/android_connect_db.php";
    // MY=============================
    //static String connect_ip ="https://tcnr2021a05.000webhostapp.com/android_mysql_connect/android_insert_db.php";
    //my all
    static String connect_ip ="https://tcnr2021a05.000webhostapp.com/android_mysql_connect/android_connect_quiz.php";
    public static int httpstate;
    public static String executeQuery(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        //selefunc_string for switch case in php
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build();

//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        //=============================

        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        // ===========================================
        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            // ===========================================
            Log.d(TAG, "Q0307response");
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Q0307error");
        }
//        //=============================
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG, "Q0307");
        return result;
    }
}
