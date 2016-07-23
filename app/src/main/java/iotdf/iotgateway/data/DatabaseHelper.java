package iotdf.iotgateway.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String name="data.db";
    static int dbVersion=1;
    public DatabaseHelper(Context context) {
        super(context, name, null, dbVersion);
    }
    //只在创建的时候用一次
    public void onCreate(SQLiteDatabase db) {
        String Envdatasql="create table EnvData(id integer primary key autoincrement,time long,arduinoNum varchar(20),brightness varchar(20),humidity varchar(20),water varchar(20),temp varchar(20))" ;
        db.execSQL(Envdatasql);
        String Usersql="create table user(id integer primary key autoincrement,username varchar(20),password varchar(20));" ;
        db.execSQL(Usersql);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
