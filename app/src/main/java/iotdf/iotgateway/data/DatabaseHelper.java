package iotdf.iotgateway.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String name="user.db";
    static int dbVersion=1;
    public DatabaseHelper(Context context) {
        super(context, name, null, dbVersion);
    }
    //只在创建的时候用一次
    public void onCreate(SQLiteDatabase db) {
        String sql="create table EnvData(arduinoNum integer primary key autoincrement,brightness varchar(20),humidity varchar(20),water varchar(20),time varchar(20),temp varchar(20))" ;
        db.execSQL(sql);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
