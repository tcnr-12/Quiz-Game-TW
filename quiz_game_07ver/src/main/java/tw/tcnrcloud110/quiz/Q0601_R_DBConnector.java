package tw.tcnrcloud110.quiz;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Q0601_R_DBConnector {

    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();

    static String connect_ip = "https://tcnrcloud110a.com/110A2/android_mysql_connect/q0601_connect_db.php";

    //=========================================================
    public static String executeQuery_R(ArrayList<String> query_string) {//---MySQL查詢語法
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //----------------建立一個封包---------------
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build();
        //--------------------執行---------------------
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

    public static String executeInsert_R(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("Forum_id", query_0)
                .add("Content", query_1)
                .add("FirstName", query_2)
                .add("Email", query_3)
                .add("UserImage", query_4)
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

    public static String executeDelet(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","delete")
                .add("ID", query_0)
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
}