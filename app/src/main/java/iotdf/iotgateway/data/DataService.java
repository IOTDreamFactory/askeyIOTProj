package iotdf.iotgateway.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataService {
    private DatabaseHelper dbHelper;
    private Context context;
    public DataService(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    private long LastUDTime;

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
        String sql="select arduinoNum,"+sensor+",time from EnvData where arduinoNum='"+arduinoNum+"'and "+sensor+" is not null order by time desc";
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
    public ArrayList updateData(String arduinoNum,long UDTime){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select * from EnvData where arduinoNum='"+arduinoNum+"' and time > "+UDTime;
        Cursor cursor =sdb.rawQuery(sql,null);
        ArrayList<Map> HistoryData=new ArrayList<Map>();
        Map<String, Object> item;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            item = new HashMap<String, Object>();
            item.put("arduinoNum",cursor.getString(cursor.getColumnIndex("arduinoNum")));
            item.put("humidity",cursor.getString(cursor.getColumnIndex("humidity")));
            item.put("water",cursor.getString(cursor.getColumnIndex("water")));
            item.put("brightness",cursor.getString(cursor.getColumnIndex("brightness")));
            item.put("temp",cursor.getString(cursor.getColumnIndex("temp")));
            item.put("time", cursor.getString(cursor.getColumnIndex("time")));
            System.out.println("Data");
            HistoryData.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        return HistoryData;
    }


    public long getLastUDTime(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("UDTime",Context.MODE_PRIVATE);
        LastUDTime=sharedPreferences.getLong("LastUDTime",0);
        System.out.println("上次上传数据的时间是"+LastUDTime);
        return LastUDTime;
    }

    public void setLastUDTime(Context context){
        Date date = new Date();
        long datetime = date.getTime();
        SharedPreferences sharedPreferences=context.getSharedPreferences("UDTime",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("LastUDTime",datetime);
        System.out.println("本次上传数据的时间是"+datetime);
    }
}
