package tw.tcnrcloud110.quiz;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Q0300_connectDB {
    private static String TAG="tcnr05=>";
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    //-------000webhost -------
    //static String connect_ip_Q0300 = "https://oldpa88.com/android_mysql_connect/android_connect_db.php";
    //static String connect_ip_Q0300 ="https://tcnr2021a05.000webhostapp.com/android_mysql_connect/android_connect_db.php";
    // MY=============================
    //static String connect_ip_Q0300 ="https://tcnr2021a05.000webhostapp.com/android_mysql_connect/android_insert_db.php";
    //my all
    //static String connect_ip_Q0300 ="https://tcnr2021a05.000webhostapp.com/05/android_connect_quiz.php";
    static String connect_ip_Q0300 ="https://tcnrcloud110a.com/110A2/android_mysql_connect/q0300_connect_quiz.php";
    static String connect_ip_Q0312 ="https://tcnrcloud110a.com/110A2/android_mysql_connect/q0312_connect_rank.php";
//    static String connect_ip_Q0300 ="https://tcnr2021a05.000webhostapp.com/05/android_connect_quiz.php";
//    static String connect_ip_Q0312 ="https://tcnr2021a05.000webhostapp.com/05/android_connect_rank.php";
    public static int httpstate;
    public static String executeQuery_Q0300(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        
        postUrl=connect_ip_Q0300 ;
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
            Log.d(TAG, "Q0300response");
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Q0300error");
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
    public static String executefind_Q0312(ArrayList<Object> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip_Q0312 ;
        //--------------
        String query_0 = query_string.get(0).toString();//table
        String query_1 = query_string.get(1).toString();//name
        String query_2 = query_string.get(2).toString();//mail
        String query_3 = query_string.get(3).toString();//score


        FormBody body = new FormBody.Builder()
                .add("selefunc_string","find")
                .add("table", query_0)

                .add("mail", query_2)

                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String executeupdate_Q0312(ArrayList<Object> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip_Q0312 ;
        //--------------
        String query_0 = query_string.get(0).toString();
        String query_1 = query_string.get(1).toString();
        String query_2 = query_string.get(2).toString();
        String query_3 = query_string.get(3).toString();
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update")
                .add("table", query_0)
                .add("name", query_1)
                .add("mail", query_2)
                .add("score", query_3)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String executeInsert_Q0312(ArrayList<Object> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip_Q0312 ;
        //--------------
        String query_0 = query_string.get(0).toString();
        String query_1 = query_string.get(1).toString();
        String query_2 = query_string.get(2).toString();
        String query_3 = query_string.get(3).toString();
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("table", query_0)
                .add("name", query_1)
                .add("mail", query_2)
                .add("score", query_3)
                .build();
        //--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeQuery_Q0312(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();

        postUrl=connect_ip_Q0312 ;
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
            Log.d(TAG, "Q0312response");
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Q0312error");
        }
//        //=============================
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG, "Q0312");
        return result;
    }
}
