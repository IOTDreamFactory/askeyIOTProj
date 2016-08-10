package iotdf.iotgateway.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataService {
    private DatabaseHelper dbHelper;
    private Context context;
    public DataService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    //插入信息
    public boolean InsertData(Data data){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="insert into EnvData(arduinoNum,humidity,water,brightness,temp,time) values(?,?,?,?,?,?)";
        Object obj[]={data.getId(),data.getHumidity(),data.getWater(),data.getBrightness(),data.getTemp(),data.getTime()};
        sdb.execSQL(sql,obj);
        return true;
    }
    //输出最近的信息
    public ArrayList HistoryData(String sensor,String arduinoNum){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select arduinoNum,"+sensor+",time from EnvData where arduinoNum='"+arduinoNum+"'and "+sensor+" is not null";
        Cursor cursor =sdb.rawQuery(sql,null);
        ArrayList<Map> HistoryData=new ArrayList<Map>();
        Map<String, Object> item;
        cursor.moveToFirst();
       while (!cursor.isAfterLast()) {
           item = new HashMap<String, Object>();
           item.put("arduinoNum",cursor.getString(cursor.getColumnIndex("arduinoNum")));
           item.put(sensor,cursor.getString(cursor.getColumnIndex(sensor)));
           item.put("time", cursor.getString(cursor.getColumnIndex("time")));
           System.out.println("Data:"+cursor.getColumnIndex(arduinoNum)+cursor.getColumnIndex(sensor)+cursor.getColumnIndex("time"));
           HistoryData.add(item);
           cursor.moveToNext();
       }
        cursor.close();
        return HistoryData;
    }

    public ArrayList PointNum(){//节点数
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select distinct arduinoNum from EnvData";
        Cursor cursor =sdb.rawQuery(sql,null);
        ArrayList<String> Num=new ArrayList<String>();
        cursor.moveToFirst();  // 重中之重
        while(!cursor.isAfterLast()){
            Num.add(cursor.getString(0)+"");
            cursor.moveToNext();
        }
        cursor.close();
        return Num;
    }

    public ArrayList initNumberTabs(String sensor,String arduinoNum){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select "+sensor+" from EnvData where arduinoNum='"+arduinoNum+"'and "+sensor+" is not null";
        Cursor cursor =sdb.rawQuery(sql,null);
        ArrayList<String> NumbersTab=new ArrayList<String>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            NumbersTab.add(cursor.getString(0)+"");
            cursor.moveToNext();
        }
        cursor.close();
        return NumbersTab;
    }
    public int pointNum(String sensor,String arduinoNum){//图表点数
        int Num=0;
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select count("+sensor+") from EnvData where arduinoNum='"+arduinoNum+"'";
        Cursor cursor =sdb.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Num=cursor.getInt(0);
            cursor.moveToNext();
        }
        cursor.close();
        return Num;
    }
}
