package tw.tcnrcloud110.quiz;



import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class CheckUser {
    private static Boolean account_state=false;
    private static String account_mail="";
    private static String account_name="";

    public CheckUser(Context context){
        //建構時會確認登入，不呼叫建構的話，每次開啟程式會預設 account_state=false ，進行登入動作才會 true

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(context);
        if(signInAccount!=null){
            account_state=true;
            account_mail=signInAccount.getEmail();
            account_name=signInAccount.getFamilyName() + signInAccount.getGivenName();
        }else{
            account_state=false;
            account_mail="";
            account_name="";
        }
    }
//    public  void setAccount_state(Boolean state){
//        account_state =state;
//    }
    public static Boolean getAccount_state() {
        //使用 static Boolean getAccount_state()  靜態類別 可不建構即可呼叫(預設false)
        return account_state;
    }
    public static String getAccount_mail() {

        return account_mail;
    }
    public static String getAccount_name() {

        return account_name;
    }
}
