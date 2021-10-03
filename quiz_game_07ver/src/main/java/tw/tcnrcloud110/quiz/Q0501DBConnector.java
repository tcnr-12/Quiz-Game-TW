package tw.tcnrcloud110.quiz;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Q0501DBConnector {
    public static int httpstate;
    //--------------------------------------------------------
    private static String postUrl;
    private static String myResponse;
    static String result = null;
    private static OkHttpClient client = new OkHttpClient();

    //---------------------------------------------------------

    //----Hosting-----
    static String connect_ip ="https://tcnrcloud110a.com/110A2/android_mysql_connect/q0501_android_connect_db.php";

//    static String connect_ip = "https://tcnr2021a11.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";

//    static String connect_ip = "https://tcnr2021a11.000webhostapp.com/android_mysql_connect/android_insert_db.php";

    //static String connect_ip = "https://tcnr2021a11.000webhostapp.com/android_mysql_connect/android_connect_db_new.php";
    //static String connect_ip = "https://oldpa88.com/android_mysql_connect/android_connect_db.php";
//班長-------000webhost 維尼-------
//    static String connect_ip = "https://tcnr2021a06.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//副班長 佳佳
//    static String connect_ip = "https://tcnr2021a23.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//學藝 瑞軍
//    static String connect_ip = "https://tcnr2021a13.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//02 晨霖
//    static String connect_ip = "https://tcnr2021a02.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";
//05  育誠
//    static String connect_ip = "https://tcnr2021a05.000webhostapp.com/android_mysql_connect/android_connect_db_all.php";

    public static String executeQuery_Q0501(ArrayList<String> query_string) { //search mysql's wordpress
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
//        String query_0 = "SELECT * FROM XXXX where name = 'text' ";
        //**********ok http's order
        //?0812
        FormBody body = new FormBody.Builder()
                .add("selefunc_string","query")
                .add("query_string", query_0)
                .build(); //build a "Q", fong bou
//--------------
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build(); //throw out a fong bou
//---====================================================
        // ===========================================
        // 使用httpResponse的方法取得http 狀態碼設定給httpstate變數
        httpstate = 0;   //設 httpcode初始值
        // ===========================================
        try (Response response = client.newCall(request).execute()) {
            httpstate = response.code();
            // ===========================================
            return response.body().string();//傳回去
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String executeInsert_Q0501(ArrayList<String> query_string) {
        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","insert")
                .add("name", query_0)
                .add("tel", query_1)
                .add("text1", query_2)
                .add("text2", query_3)
                .add("email", query_4)
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

    public static String executeDelet_Q0501(ArrayList<String> query_string) {
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

    public static String executeUpdate_Q0501(ArrayList<String> query_string) {
//        OkHttpClient client = new OkHttpClient();
        postUrl=connect_ip ;
        //--------------
        String query_0 = query_string.get(0);
        String query_1 = query_string.get(1);
        String query_2 = query_string.get(2);
        String query_3 = query_string.get(3);
        String query_4 = query_string.get(4);

        FormBody body = new FormBody.Builder()
                .add("selefunc_string","update")
                .add("id", query_0)
                .add("name", query_1)
                .add("tel", query_2)
                .add("text1", query_3)
                .add("text2", query_4)
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
}
