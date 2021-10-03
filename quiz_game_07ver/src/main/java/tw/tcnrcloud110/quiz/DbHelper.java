package tw.tcnrcloud110.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

////----------------------------------------------------------
//建構式參數說明：
//context 可以操作資料庫的內容本文，一般可直接傳入Activity物件。
//name 要操作資料庫名稱，如果資料庫不存在，會自動被建立出來並呼叫onCreate()方法。
//factory 用來做深入查詢用，入門時用不到。
//version 版本號碼。
////-----------------------

public class DbHelper extends SQLiteOpenHelper {

    public String sCreateTableCommand;
    public static final int VERSION = 1;// ---料庫版本，資料結構改變的時候要更改這個數字，通常是+1
    String TAG = "tcnrClound=";
    //=============================================
    //-----------------------資料庫名稱
    private static final String DB_FILE = "QuizeGame.db";
    //-----------------------資料表名稱料
    private static final String DB_TABLE_Q0200 = "Q0200";
    private static final String DB_TABLE_Q0300 = "Q0300";
    private static final String DB_TABLE_Q0312_1 = "q0312_1";//初 rank
    private static final String DB_TABLE_Q0312_2 = "q0312_2";//中 rank
    private static final String DB_TABLE_Q0312_3 = "q0312_3";//高 rank
    private static final String DB_TABLE_Q0401= "Q0401";
    private static final String DB_TABLE_Q0411 = "Q0411";
    private static final String DB_TABLE_Q0501 = "Q0501";
    private static final String DB_TABLE_Q0513 = "q0513";
    private static final String DB_TABLE_Q0600 = "Q0600";
    private static final String DB_TABLE_Q0601 = "Q0601";
    private static SQLiteDatabase database;//--------------------庫物件，固定的欄位變數
    //======================================================
    private  static final String Creat_Table_Q0200 = "CREATE TABLE " + DB_TABLE_Q0200
            + "(" + " ID INTEGER PRIMARY KEY," + "Email TEXT," + "FirstName TEXT,"
            + "LastName TEXT,"+ "UserImage TEXT);";

    private static final String Creat_Table_Q0300 =
            "CREATE TABLE " + DB_TABLE_Q0300 + " ("
                    + "id INTEGER PRIMARY KEY,question VARCHAR(200) NOT NULL,answer VARCHAR(50),wrong_answer1 VARCHAR(50),wrong_answer2 VARCHAR(50),wrong_answer3 VARCHAR(50));";

    private static final String Creat_Table_Q0312_1=
            "CREATE TABLE "+DB_TABLE_Q0312_1+" ("
                    +"id INTEGER PRIMARY KEY,name VARCHAR(50) NOT NULL,mail VARCHAR(50) NOT NULL,score INT(20) NOT NULL );";

    private static final String Creat_Table_Q0312_2=
            "CREATE TABLE "+DB_TABLE_Q0312_2+" ("
                    +"id INTEGER PRIMARY KEY,name VARCHAR(50) NOT NULL,mail VARCHAR(50) NOT NULL,score INT(20) NOT NULL );";

    private static final String Creat_Table_Q0312_3=
            "CREATE TABLE "+DB_TABLE_Q0312_3+" ("
                    +"id INTEGER PRIMARY KEY,name VARCHAR(50) NOT NULL,mail VARCHAR(50) NOT NULL,score INT(20) NOT NULL );";

    private static final String Creat_Table_Q0401 = "CREATE TABLE " + DB_TABLE_Q0401 + " ( "
            + "id INTEGER PRIMARY KEY," + "title TEXT NOT NULL," + "link TEXT," + "description TEXT);";

    private  static final String Creat_Table_Q0411 = "CREATE TABLE " + DB_TABLE_Q0411
            + "(" + " ID INTEGER PRIMARY KEY," + "Email TEXT," + "FirstName TEXT," + "LastName TEXT,"+ "UID TEXT);";

    private static final String Creat_Table_Q0501 = "CREATE TABLE " + DB_TABLE_Q0501 + " ( "
            + "id INTEGER PRIMARY KEY," + "name TEXT NOT NULL," + "tel TEXT," + "text1 TEXT," + "text2 TEXT," + "email TEXT) ;" ;

    private static final String Creat_Table_Q0513 = "CREATE TABLE " + DB_TABLE_Q0513  + " ( "
            + "id INTEGER PRIMARY KEY," + "text1 TEXT NOT NULL," + "text2 TEXT,"
            + "text3 TEXT);";

    private  static final String Creat_Table_Q0600 = "CREATE TABLE " + DB_TABLE_Q0600
            + "(" + " ID INTEGER PRIMARY KEY," + "Category TEXT," + "Title TEXT," + "Content TEXT,"
            + "FirstName TEXT," + "Email TEXT,"+ "UserImage TEXT,"+ "SetTime TEXT,"+ "UpdateTime TEXT,"+ "Respond TEXT);";

    private  static final String Creat_Table_Q0601 = "CREATE TABLE " + DB_TABLE_Q0601
            + "(" + " ID INTEGER PRIMARY KEY," + "Forum_id TEXT," + "Content TEXT," + "FirstName TEXT,"
            + "Email TEXT,"+ "UserImage TEXT,"+ "SetTime TEXT);";

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DbHelper(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_FILE, null, 1);
        sCreateTableCommand = "";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 執行 新增 DB TABLE 命令
        db.execSQL(Creat_Table_Q0200);
        db.execSQL(Creat_Table_Q0300);
        db.execSQL(Creat_Table_Q0312_1);
        db.execSQL(Creat_Table_Q0312_2);
        db.execSQL(Creat_Table_Q0312_3);
        db.execSQL(Creat_Table_Q0401);
        db.execSQL(Creat_Table_Q0411);
        db.execSQL(Creat_Table_Q0501);
        db.execSQL(Creat_Table_Q0513);
        db.execSQL(Creat_Table_Q0600);
        db.execSQL(Creat_Table_Q0601);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE_Q0200);
        db.execSQL("DROP     TABLE     IF    EXISTS    " + DB_TABLE_Q0300);
        db.execSQL("DROP     TABLE     IF    EXISTS    " + DB_TABLE_Q0312_1);
        db.execSQL("DROP     TABLE     IF    EXISTS    " + DB_TABLE_Q0312_2);
        db.execSQL("DROP     TABLE     IF    EXISTS    " + DB_TABLE_Q0312_3);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_Q0401);
        db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE_Q0411);
        db.execSQL("DROP     TABLE     IF    EXISTS    " + DB_TABLE_Q0501);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_Q0513 );
        db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE_Q0600);
        db.execSQL(" DROP TABLE IF EXISTS " + DB_TABLE_Q0601);
        onCreate(db);
    }

//================================Q0200=================================
public ArrayList<String> getRecSet_Q0200() {
    SQLiteDatabase db = getReadableDatabase();
    String sql = "SELECT * FROM " + DB_TABLE_Q0200 +" ORDER BY id DESC";
    Cursor recSet = db.rawQuery(sql, null);
    ArrayList<String> recAry = new ArrayList<String>();
    //----------------------------
    //Log.d(TAG, "recSet=" + recSet);
    int columnCount = recSet.getColumnCount();
    recSet.moveToFirst();
    String fldSet = "";
    if (recSet.getCount() != 0){//-----------判斷資料如果 不是0比 才執行抓資料
        for (int i = 0; i < columnCount; i++)
            fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
        recAry.add(fldSet);
    }
    while (recSet.moveToNext()) {
        fldSet = "";
        for (int i = 0; i < columnCount; i++) {
            fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
        }
        recAry.add(fldSet);
    }
    //------------------------
    recSet.close();
    db.close();
    //Log.d(TAG, "recAry=" + recAry);
    return recAry;
}

//*****Q0200新增一筆資料*****
    public long insertRec_Q0200(String b_email, String b_fname, String b_lname) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues rec = new ContentValues();

    rec.put("Email", b_email);
    rec.put("FirstName", b_fname);
    rec.put("LastName", b_lname);
    rec.put("UserImage", "");
    long rowID = db.insert(DB_TABLE_Q0200, null, rec); // SQLite 新增寫法
    db.close();
    return rowID;
}
    //*****Q0200清空資料*****
    public int clearRec_Q0200() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0200;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            //			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Q0200, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    //================================Q0300============================
    public int RecCount_Q0300() {
        SQLiteDatabase db =getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0300;
        //用Cursor做一個封包
        Cursor recSet = db.rawQuery(sql, null);//select
        int rcount=0;
        rcount=recSet.getCount();
        recSet.close();
        return rcount;//回傳筆數
    }

    public String FindRec_Q0300(String tname) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = "ans=";
        String sql = "SELECT * FROM " + DB_TABLE_Q0300 +  " WHERE question LIKE ? ORDER BY id ASC";//ASC遞增排序 DESC遞減
        String[] args = {"%" + tname + "%"};
        Cursor recSet = db.rawQuery(sql, args); //
        int columnCount = recSet.getColumnCount();
        //===================
        if (recSet.getCount() != 0) {
            recSet.moveToFirst();
            fldSet = recSet.getString(0) + " "
                    + recSet.getString(1) + " "
                    + recSet.getString(2) + " "
                    + recSet.getString(3) + "\n";

            while (recSet.moveToNext()) {
                for (int i = 0; i < columnCount; i++) {
                    fldSet += recSet.getString(i) + " ";
                }
                fldSet += "\n";
            }
        }
        recSet.close();
        db.close();
        return fldSet;
    }

//    public ArrayList<String> getRecSet_Q0300() {
//        SQLiteDatabase db = getReadableDatabase();
//        String sql = "SELECT * FROM " + DB_TABLE_Q0300;
//        Cursor recSet = db.rawQuery(sql, null);
//        ArrayList<String> recAry = new ArrayList<String>();
//        //----------------------------
//        //        Log.d(TAG, "recSet=" + recSet);
//        int columnCount = recSet.getColumnCount();
//        recSet.moveToFirst();
//        String fldSet = "";
//        if(recSet.getCount()>0){ // 判斷資料如果 不是0比 才執行抓資料
//            for (int i = 0; i < columnCount; i++)
//                fldSet += recSet.getString(i) + "#"; //用#做區隔
//            recAry.add(fldSet);
//        }
//
//        while (recSet.moveToNext()) {
//            fldSet = "";
//            for (int i = 0; i < columnCount; i++) {
//                fldSet += recSet.getString(i) + "#"; //
//            }
//            recAry.add(fldSet);
//        }
//        //------------------------
//        recSet.close();
//        db.close();
//        //        Log.d(TAG, "recAry=" + recAry);
//        return recAry;
//    }

    public int deleteRec_Q0300(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0300;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0){
            String whereClause = "id = '" + b_id + "'";//d='b_id'
            int rowsAffected = db.delete(DB_TABLE_Q0300, whereClause, null);//delete one row
            recSet.close();
            db.close();
            return rowsAffected;
        } else  {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int updateRec_Q0300(String b_id, String b_name, String b_grp, String b_address) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0300;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0){
            ContentValues rec = new ContentValues();
//   rec.put("id", b_id);
            rec.put("question", b_name);
            rec.put("grp", b_grp);
            rec.put("address", b_address);
            String whereClause = "id ='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE_Q0300, rec, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else    {
            recSet.close();
            db.close();
            return -1;
        }
    }
    //    ContentValues values
//    public long insertRec_m_Q0300(ContentValues rec) {
//        SQLiteDatabase db = getWritableDatabase();
//        long rowID = db.insert(DB_TABLE_Q0300, null, rec);
//        Log.d(TAG, "q0300_quiz_insertRec_m");
//        db.close();
//        return rowID;
//    }

    public ArrayList<String> getRecSet_Q0300() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0300;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
//        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";
        if(recSet.getCount()>0){ // 判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; //用#做區隔
            recAry.add(fldSet);
        }

        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#"; //
            }
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
//        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

    public ArrayList<String> getRecSet_Q0312_1() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0312_1+" ORDER BY score DESC";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
//        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";
        if(recSet.getCount()>0){ // 判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; //用#做區隔
            recAry.add(fldSet);
        }

        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#"; //
            }
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
//        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

    public ArrayList<String> getRecSet_Q0312_2() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0312_2+" ORDER BY score DESC";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
//        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";
        if(recSet.getCount()>0){ // 判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; //用#做區隔
            recAry.add(fldSet);
        }

        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#"; //
            }
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
//        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

    public ArrayList<String> getRecSet_Q0312_3() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0312_3+" ORDER BY score DESC";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
//        Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";
        if(recSet.getCount()>0){ // 判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; //用#做區隔
            recAry.add(fldSet);
        }

        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#"; //
            }
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
//        Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

    public int clearRec_Q0300() {
        SQLiteDatabase db =getWritableDatabase();
        String sql="SELECT * FROM "+DB_TABLE_Q0300;
        Cursor c1=db.rawQuery(sql,null);
        //Cursor c1=db.exeSQL("");
        //Cursor c2=db.rawQuery();
        //Cursor c3=db.insert();
        //Cursor c4=db.update();
        //Cursor c5=db.delete();
        if(c1.getCount()>0){
            //String whereClause:"id<0"
            int rowsAffected=db.delete(DB_TABLE_Q0300,"1",null);//
            //From the documentation of SQLiteDatabase delete method
            //To remove all rows and get a count pass "1" as the whereClause
            db.close();
            c1.close();
            return  rowsAffected;
        }else{
            db.close();
            c1.close();
            return  -1;
        }
    }

    public int clearRec_Q0312_1() {
        SQLiteDatabase db =getWritableDatabase();
        String sql="SELECT * FROM "+DB_TABLE_Q0312_1;
        Cursor c1=db.rawQuery(sql,null);
        //Cursor c1=db.exeSQL("");
        //Cursor c2=db.rawQuery();
        //Cursor c3=db.insert();
        //Cursor c4=db.update();
        //Cursor c5=db.delete();
        if(c1.getCount()>0){
            //String whereClause:"id<0"
            int rowsAffected=db.delete(DB_TABLE_Q0312_1,"1",null);//
            //From the documentation of SQLiteDatabase delete method
            //To remove all rows and get a count pass "1" as the whereClause
            db.close();
            c1.close();
            return  rowsAffected;
        }else{
            db.close();
            c1.close();
            return  -1;
        }
    }

    public int clearRec_Q0312_2() {
        SQLiteDatabase db =getWritableDatabase();
        String sql="SELECT * FROM "+DB_TABLE_Q0312_2;
        Cursor c1=db.rawQuery(sql,null);
        //Cursor c1=db.exeSQL("");
        //Cursor c2=db.rawQuery();
        //Cursor c3=db.insert();
        //Cursor c4=db.update();
        //Cursor c5=db.delete();
        if(c1.getCount()>0){
            //String whereClause:"id<0"
            int rowsAffected=db.delete(DB_TABLE_Q0312_2,"1",null);//
            //From the documentation of SQLiteDatabase delete method
            //To remove all rows and get a count pass "1" as the whereClause
            db.close();
            c1.close();
            return  rowsAffected;
        }else{
            db.close();
            c1.close();
            return  -1;
        }
    }

    public int clearRec_Q0312_3() {
        SQLiteDatabase db =getWritableDatabase();
        String sql="SELECT * FROM "+DB_TABLE_Q0312_3;
        Cursor c1=db.rawQuery(sql,null);
        //Cursor c1=db.exeSQL("");
        //Cursor c2=db.rawQuery();
        //Cursor c3=db.insert();
        //Cursor c4=db.update();
        //Cursor c5=db.delete();
        if(c1.getCount()>0){
            //String whereClause:"id<0"
            int rowsAffected=db.delete(DB_TABLE_Q0312_3,"1",null);//
            //From the documentation of SQLiteDatabase delete method
            //To remove all rows and get a count pass "1" as the whereClause
            db.close();
            c1.close();
            return  rowsAffected;
        }else{
            db.close();
            c1.close();
            return  -1;
        }
    }

    public long insertRec_m_Q0300(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0300, null, rec);
        Log.d(TAG, "q0300_quiz_insertRec_m");
        db.close();
        return rowID;
    }

    public long insertRec_m_Q0312_1(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0312_1, null, rec);
        Log.d(TAG, "q0312_1_quiz_insertRec_m");
        db.close();
        return rowID;
    }

    public long insertRec_m_Q0312_2(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0312_2, null, rec);
        Log.d(TAG, "q0312_2_quiz_insertRec_m");
        db.close();
        return rowID;
    }

    public long insertRec_m_Q0312_3(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0312_3, null, rec);
        Log.d(TAG, "q0312_3_quiz_insertRec_m");
        db.close();
        return rowID;
    }

//===============================Q0401==========================
public long insertRec_Q0401(String b_name, String b_grp, String b_address) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues rec = new ContentValues();
    rec.put("title", b_name);
    rec.put("link", b_grp);
    rec.put("description", b_address);
    long rowID = db.insert(DB_TABLE_Q0401, null, rec);
    db.close();
    return rowID;
}

    //    ContentValues values
    public long insertRec_m_Q0401(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0401, null, rec);
        db.close();
        return rowID;
    }

    public int RecCount_Q0401() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0401;
        Cursor recSet = db.rawQuery(sql, null);
        int count = recSet.getCount();
        recSet.close();
        return count;
    }

    public ArrayList<String> getRecSet_Q0401() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0401;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
//

    public int clearRec_Q0401() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0401;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Q0401, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }


    public int updateRec_Q0401(String b_id, String b_name, String b_grp, String b_address) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0401;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
//			rec.put("id", b_id);
            rec.put("title", b_name);
            rec.put("link", b_grp);
            rec.put("description", b_address);

            String whereClause = "id='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE_Q0401, rec, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int deleteRec_Q0401(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0401;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            String whereClause = "id='" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE_Q0401, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public ArrayList<String> getRecSet_query_Q0401(String tname, String tgrp, String taddr) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0401 +
                " WHERE title LIKE ? AND link LIKE ? AND description LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + tname.toString() + "%",
                "%" + tgrp.toString() + "%",
                "%" + taddr.toString() + "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    //===============================Q0501==========================
    public int clearRec_Q0501() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0501;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Q0501, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public long insertRec_m_Q0501(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0501, null, rec);
        db.close();
        return rowID;
    }

    public int RecCount_Q0501() {
        SQLiteDatabase db=getWritableDatabase();
        String sql="SELECT * FROM "+DB_TABLE_Q0501;
        Cursor recSet=db.rawQuery(sql,null);
        int count = recSet.getCount();
        recSet.close();
        return count;
    }

    public ArrayList<String> getRecSet_query_Q0501(String t_name, String t_tel, String t_text1, String t_text2) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE name LIKE ? AND tel LIKE ? AND text1 LIKE ? AND text2 LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + t_name.toString() + "%",
                "%" + t_tel.toString() + "%",
                "%" + t_text1.toString() + "%",
                "%" + t_text2.toString() + "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_Q0501() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0501;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        //recSet.moveToFirst();

        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_query_Q0501c002n(String t_name) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE name LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + t_name.toString() + "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 1; i < columnCount; i=i+4)
                fldSet += recSet.getString(i);
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_query_Q0501c002t(String t_tel) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE tel LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + t_tel.toString() + "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 2; i < columnCount; i=i+4)
                fldSet += recSet.getString(i);
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_query_Q0501c002q(String t_tname, String t_ttel, String t_ttext1, String t_ttext2, String e_temail) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0501 +
                " WHERE name LIKE ? AND tel LIKE ? AND text1 LIKE ? AND text2 LIKE ? AND email='"+e_temail+"' ORDER BY id ASC";
        String[] args = new String[]{"%" + t_tname.toString() + "%",
                "%" + t_ttel.toString() + "%",
                "%" + t_ttext1.toString() + "%",
                "%" + t_ttext2.toString() + "%"};
        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

//==============================Q0513=======================
public long insertRec_Q0513 (String b_name, String b_grp, String b_address) {
    SQLiteDatabase db = getWritableDatabase();
    ContentValues rec = new ContentValues();
    rec.put("text1", b_name);
    rec.put("text2", b_grp);
    rec.put("text3", b_address);
    long rowID = db.insert(DB_TABLE_Q0513 , null, rec);
    db.close();
    return rowID;
}

    //    ContentValues values
    public long insertRec_m_Q0513(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0513 , null, rec);
        db.close();
        return rowID;
    }

    public int RecCount_Q0513() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0513 ;
        Cursor recSet = db.rawQuery(sql, null);
        int count = recSet.getCount();
        recSet.close();
        return count;
    }

    public ArrayList<String> getRecSet_Q0513() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0513 ;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
//

    public int clearRec_Q0513() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0513 ;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
//			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Q0513 , "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }


    public int updateRec_Q0513(String b_id, String b_name, String b_grp, String b_address) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0513 ;
        Cursor recSet = db.rawQuery(sql, null);

        if (recSet.getCount() != 0) {
            ContentValues rec = new ContentValues();
//			rec.put("id", b_id);
            rec.put("text1", b_name);
            rec.put("text2", b_grp);
            rec.put("text3", b_address);

            String whereClause = "id='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE_Q0513 , rec, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public int deleteRec_Q0513(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0513 ;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            String whereClause = "id='" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE_Q0513 , whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    public ArrayList<String> getRecSet_query_Q0513(String tname, String tgrp, String taddr) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0513  +
                " WHERE text1 LIKE ? AND text2 LIKE ? AND text3 LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + tname.toString() + "%",
                "%" + tgrp.toString() + "%",
                "%" + taddr.toString() + "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

//================================Q0600=================================
    //*****Q0600新增一筆資料*****
    public long insertRec_Q0600(String db_category, String db_title, String db_content,
                                String db_firstname, String db_mail, String db_userimage, String db_updatetime, String db_respond) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();

        rec.put("Category", db_category);
        rec.put("Title", db_title);
        rec.put("Content", db_content);
        rec.put("FirstName", db_firstname);
        rec.put("Email", db_mail);
        rec.put("UserImage", db_userimage);
        rec.put("UpdateTime", db_updatetime);
        rec.put("Respond", db_respond);
        long rowID = db.insert(DB_TABLE_Q0600, null, rec); // -----SQLite 新增寫法
        db.close();
        return rowID;
    }

    //*****Q0600修改用抓資料*****
    public ArrayList<String> getRecSet_Q0600() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0600 +" ORDER BY id DESC";
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        //Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";
        if (recSet.getCount() != 0){//-----------判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
            recAry.add(fldSet);
        }
        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
            }
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        //Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

    //*****Q0600刪除一筆資料*****
    public Object deleteRec_Q0600(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0600;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0){
            String whereClause = "id = '" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE_Q0600, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else  {
            recSet.close();
            db.close();
            return -1;
        }
    }

    //*****Q0600更新一筆資料*****
    public int updateRec_Q0600(String b_id, String b_title, String b_content) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0600;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0){
            ContentValues rec = new ContentValues();
            //   rec.put("id", b_id);
            rec.put("Title", b_title);
            rec.put("Content", b_content);
            String whereClause = "ID ='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE_Q0600, rec, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else    {
            recSet.close();
            db.close();
            return -1;
        }
    }

    //*****Q0600清空資料*****
    public int clearRec_Q0600() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0600;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
            //			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Q0600, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    //*****Q0600寫入DB_FILE*****
    public long insertRec_m_Q0600(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0600, null, rec);
        db.close();
        return rowID;
    }

    //*****Q0600搜尋e_category*****
    public ArrayList<String> getRecSet_query_Q0600(String e_category) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0600 +
                " WHERE Category LIKE ? ORDER BY id DESC";
        String[] args = new String[]{"%" + e_category+ "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
    //*****Q0600搜尋user Email*****
    public ArrayList<String> getRecSet_user_Q0600(String user) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0600 +
                " WHERE Email LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + user+ "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

//================================Q0601==============================
    //*****Q0601新增一筆資料*****
    public long insertRec_Q0601(String db_forum_id, String db_content, String db_firstname, String db_email, String db_userimage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues rec = new ContentValues();

        rec.put("Forum_id", db_forum_id);
        rec.put("Content", db_content);
        rec.put("FirstName", db_firstname);
        rec.put("Email", db_email);
        rec.put("UserImage", db_userimage);
        long rowID = db.insert(DB_TABLE_Q0601, null, rec); // -----SQLite 新增寫法
        db.close();
        return rowID;
    }

    //*****Q0601修改用抓資料*****
    public ArrayList<String> getRecSet_Q0601() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0601;
        Cursor recSet = db.rawQuery(sql, null);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        //Log.d(TAG, "recSet=" + recSet);
        int columnCount = recSet.getColumnCount();
        recSet.moveToFirst();
        String fldSet = "";
        if (recSet.getCount() != 0){//-----------判斷資料如果 不是0比 才執行抓資料
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
            recAry.add(fldSet);
        }
        while (recSet.moveToNext()) {
            fldSet = "";
            for (int i = 0; i < columnCount; i++) {
                fldSet += recSet.getString(i) + "#"; // 欄位跟欄位 用 # 做區隔
            }
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        //Log.d(TAG, "recAry=" + recAry);
        return recAry;
    }

    //*****Q0601刪除一筆資料*****
    public Object deleteRec_Q0601(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0601;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0){
            String whereClause = "id = '" + b_id + "'";
            int rowsAffected = db.delete(DB_TABLE_Q0601, whereClause, null);
            recSet.close();
            db.close();
            return rowsAffected;
        } else  {
            recSet.close();
            db.close();
            return -1;
        }
    }

    //*****Q0601清空資料*****
    public int clearRec_Q0601() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE_Q0601;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0) {
        //			String whereClause = "id < 0";
            int rowsAffected = db.delete(DB_TABLE_Q0601, "1", null);
            // From the documentation of SQLiteDatabase delete method:
            // To remove all rows and get a count pass "1" as the whereClause.
            recSet.close();
            db.close();
            return rowsAffected;
        } else {
            recSet.close();
            db.close();
            return -1;
        }
    }

    //*****Q0601寫入DB_FILE*****
    public long insertRec_m_Q0601(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE_Q0601, null, rec);
        db.close();
        return rowID;
    }
    //*****Q0601搜尋user Email*****
    public ArrayList<String> getRecSet_user_Q0601(String user) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0601 +
                " WHERE Email LIKE ? ORDER BY id ASC";
        String[] args = new String[]{"%" + user+ "%"};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }

    public ArrayList<String> getRecSet_query_Q0601(String f_id) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM " + DB_TABLE_Q0601 +
                " WHERE Forum_id LIKE ? ORDER BY id ASC";
        String[] args = new String[]{ f_id};

        Cursor recSet = db.rawQuery(sql, args);
        ArrayList<String> recAry = new ArrayList<String>();
        //----------------------------
        int columnCount = recSet.getColumnCount();
        while (recSet.moveToNext()) {
            String fldSet = "";
            for (int i = 0; i < columnCount; i++)
                fldSet += recSet.getString(i) + "#";
            recAry.add(fldSet);
        }
        //------------------------
        recSet.close();
        db.close();
        return recAry;
    }
}