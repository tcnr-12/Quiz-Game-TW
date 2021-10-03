package tw.tcnrcloud110.quiz;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Q0513_DBConnector {

    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();
    public static int httpstate = 0 ;
//---------------------------------------------------------
//-------Hosting 第2組-------
static String connect_ip ="https://tcnrcloud110a.com/110A2/android_mysql_connect/q0513_connect_db.php";




    //----------------------------------------------------------------------------------------
    public static String executeQuery(ArrayList<String> query_string) { //MYSQL查詢語法
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build();
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
        // ======++++++++++++++++++++====================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        //-------------------------------------------
        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            // -------------------------------------
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ========+++++++++++++++========================
        return result;
    }



    public static String executeInsert(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("text1", query_0)
                .add("text2", query_1)
                .add("text3", query_2)
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
//刪除資料
    public static String executeDelet(ArrayList<String> query_string) {
        //        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","delete")
                .add("id", query_0)
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
    //---更新資料--------------------------------------------------------------
    public static String executeUpdate(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update")
                .add("id", query_0)
                .add("text1", query_1)
                .add("text2", query_2)
                .add("text3", query_3)
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
//==========================

}//---------END----------