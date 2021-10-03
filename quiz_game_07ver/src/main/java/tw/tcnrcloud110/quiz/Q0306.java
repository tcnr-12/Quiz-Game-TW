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
public class Q0306 extends SQLiteOpenHelper {
    String TAG="tcnr05=>";
    public String sCreateTableCommand;
    // 資料庫名稱
    private static final String DB_FILE = "q0300.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 1;    // 資料表名稱
    private static final String DB_TABLE = "quiz";    // 資料庫物件，固定的欄位變數
    private static final String crTBsql=
            "CREATE TABLE "+DB_TABLE+" ("
                    +"id INTEGER PRIMARY KEY,question TEXT NOT NULL,answer TEXT,wrong_answer1 TEXT,wrong_answer2 TEXT,wrong_answer3 TEXT);";
    public Q0306(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        //super(context,name,factory,version);
        super(context, DB_FILE, null, version);
        sCreateTableCommand="";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String crTBsql="SELECT id,name,grp from member where id>100";
        db.execSQL(crTBsql);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d(TAG,"onUpgrade");
        db.execSQL("DROP     TABLE     IF    EXISTS    " + DB_TABLE);
        onCreate(db);
    }
    private static SQLiteDatabase database;

    //----------------------------------------------
    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context){
        if (database == null || !database.isOpen())  {
            database = new Q0306(context, DB_FILE, null, VERSION)
                    .getWritableDatabase();
        }
        return database;
    }

    public long insertRec(String q1, String a1, String wa1, String wa2, String wa3) {
        SQLiteDatabase db =getWritableDatabase();
        ContentValues rec=new ContentValues();
        rec.put("question",q1);
        rec.put("answer",a1);
        rec.put("wrong_answer1",wa1);
        rec.put("wrong_answer2",wa2);
        rec.put("wrong_answer3",wa3);
        long rowID=db.insert(DB_TABLE,null,rec);//SQLite新增語法

        db.close();
        return rowID;
    }

    public int RecCount() {
        SQLiteDatabase db =getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);//select
        return recSet.getCount();//回傳筆數
    }

    public String FindRec(String tname) {
        SQLiteDatabase db = getReadableDatabase();
        String fldSet = "ans=";
        String sql = "SELECT * FROM " + DB_TABLE +  " WHERE question LIKE ? ORDER BY id ASC";//ASC遞增排序 DESC遞減
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

    public ArrayList<String> getRecSet() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
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
//    public void createTB() {
        // 批次新增
//        int maxrec = 20;
//        SQLiteDatabase db = getWritableDatabase();
//        for (int i = 0; i < maxrec; i++) {
//            ContentValues newRow = new ContentValues();
//            newRow.put("name", "路人" +  u_chinayear(i));
//            newRow.put("grp", "第" + u_chinano((int) (Math.random() * 4 + 1)) + "組");
//            newRow.put("address", "台中市西區工業一路" + (100 + i) + "號");
//            db.insert(DB_TABLE, null, newRow);  //insert db
//        }
//        db.close();

//    }
//    private String u_chinano(int input_i) {
//        String c_number = "";
//        String china_no[]={"零","一","二","三","四","五","六","七","八","九"};
//        c_number = china_no[input_i % 10];
//
//        return c_number;
//    }
//    private String u_chinayear(int input_i) {
//        String c_number = "";
//        String china_no[]={"甲","乙","丙","丁","戊","己","庚","辛","壬","癸"};
//        c_number = china_no[input_i % 10];
//
//        return c_number;
//    }

    public int clearRec() {
        SQLiteDatabase db =getWritableDatabase();
        String sql="SELECT * FROM "+DB_TABLE;
        Cursor c1=db.rawQuery(sql,null);
        //Cursor c1=db.exeSQL("");
        //Cursor c2=db.rawQuery();
        //Cursor c3=db.insert();
        //Cursor c4=db.update();
        //Cursor c5=db.delete();
        if(c1.getCount()>0){
            //String whereClause:"id<0"
            int rowsAffected=db.delete(DB_TABLE,"1",null);//
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

    public int deleteRec(String b_id) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0){
            String whereClause = "id = '" + b_id + "'";//d='b_id'
            int rowsAffected = db.delete(DB_TABLE, whereClause, null);//delete one row
            recSet.close();
            db.close();
            return rowsAffected;
        } else  {
            recSet.close();
            db.close();
            return -1;
        }


    }

    public int updateRec(String b_id, String b_name, String b_grp, String b_address) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + DB_TABLE;
        Cursor recSet = db.rawQuery(sql, null);
        if (recSet.getCount() != 0){
            ContentValues rec = new ContentValues();
//   rec.put("id", b_id);
            rec.put("question", b_name);
            rec.put("grp", b_grp);
            rec.put("address", b_address);
            String whereClause = "id ='" + b_id + "'";

            int rowsAffected = db.update(DB_TABLE, rec, whereClause, null);
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
    public long insertRec_m(ContentValues rec) {
        SQLiteDatabase db = getWritableDatabase();
        long rowID = db.insert(DB_TABLE, null, rec);
        Log.d(TAG, "Q0306insertRec_m");
        db.close();
        return rowID;
    }

}
