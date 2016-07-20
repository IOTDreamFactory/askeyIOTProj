package iotdf.iotgateway.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class DataService {
    private DatabaseHelper dbHelper;
    private Context context;
    public DataService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    //遍历表
    public ArrayList ergodicTable(){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select * from EnvData";
        Cursor cursor =sdb.rawQuery(sql,null);
        ArrayList<Map<String, Object>> coll = new ArrayList<Map<String, Object>>();
        Map<String, Object> item;
        cursor.moveToFirst();  // 重中之重，千万不能忘了
        while(!cursor.isAfterLast()){
            item = new HashMap<String, Object>();
            item.put("id", cursor.getString(0));
            item.put("humidity", cursor.getString(1));
            item.put("water", cursor.getString(2));
            item.put("brightness", cursor.getString(3));
            item.put("temp", cursor.getString(4));
            item.put("time", cursor.getString(5));
            coll.add(item);
            cursor.moveToNext();
        }
        return coll;
    }
    //插入信息
    public boolean InsertData(Data data){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="insert into EnvData(id,humidity,water,brightness,temp,time) values(?,?,?,?,?,?)";
        Object obj[]={data.getId(),data.getHumidity(),data.getWater(),data.getBrightness(),data.getTemp(),data.getTime()};
        sdb.execSQL(sql,obj);
        return true;
    }
    //输出最近的信息
    public Map LatestData(){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select * from EnvData";
        Cursor cursor =sdb.rawQuery(sql,null);
        Map<String, Object> item;
        cursor.moveToLast();
        item = new HashMap<String, Object>();
        item.put("id", cursor.getString(0));
        item.put("humidity", cursor.getString(1));
        item.put("water", cursor.getString(2));
        item.put("brightness", cursor.getString(3));
        item.put("temp", cursor.getString(4));
        item.put("time", cursor.getString(5));
        return item;
    }
}
