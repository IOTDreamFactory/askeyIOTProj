package iotdf.iotgateway.ConSens;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

import iotdf.iotgateway.data.Data;
import iotdf.iotgateway.data.DataService;


public class LocalService extends Service {
    InputStream inputstream;//创建输入数据流
    OutputStream outputStream;//创建输出数据流
    ServerSocket serverSocket;//创建ServerSocket对象
    Socket clicksSocket;//连接通道，创建Socket对象
    String bufStr="";
    int count=1;
    private NotificationManager mNM;
    String send="1234";
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */

    public void send(String send){
        this.send=send;
        sendThread sendThread=new sendThread();
        sendThread.start();
    }
    public class LocalBinder extends Binder {
       public LocalService getService() {
            return LocalService.this;
        }
    }
/*处理接收到的消息*/
    Handler handler=new Handler(){
        public void handleMessage(Message msg)
        {
            if(msg.obj.toString().startsWith("1010")&&msg.obj.toString().endsWith("1011"))
            {
                System.out.println(count++);
                DataService mDataService=new DataService(LocalService.this);
                Data data=new Data();
                java.util.Date date = new java.util.Date();
                long datetime = date.getTime();
                String Type=msg.obj.toString().substring(4,6);
                String NodeID=msg.obj.toString().substring(6,9);
                String SensorType=msg.obj.toString().substring(9,12);
                String More=msg.obj.toString().substring(12,15);
                String MainData=msg.obj.toString().substring(16,28);
                NodeID=Integer.parseInt(NodeID,2)+"";
                More=Integer.parseInt(More,2)+"";
                MainData=Integer.parseInt(MainData,2)+"";
                System.out.println("Type:"+Type);
                System.out.println("NodeID:"+NodeID);
                System.out.println("SenSorType:"+SensorType);
                System.out.println("More:"+More);
                System.out.println("Maindata:"+MainData);
                if(Type.equals("00")){
                    if(SensorType.equals("000"))
                    {
                        SensorType="brightness";
                        MainData=Integer.parseInt(MainData)*10+"";
                    data.setBrightness(MainData);}
                    else if(SensorType.equals("001"))
                    {
                        SensorType="temp";
                    data.setTemp(MainData);
                    }
                    else if(SensorType.equals("010"))
                    {
                        SensorType="humidity";
                    data.setHumidity(MainData);
                    }
                    else if(SensorType.equals("011"))
                    {
                        SensorType="water";
                    data.setWater(MainData);
                    }
                    data.setTime(datetime);
                    data.setId("传感节点"+NodeID);
                    mDataService.InsertData(data);
                }

            }
        }
    };

    @Override
    public void onCreate() {
        ServerSocket_thread serversocket_thread = new ServerSocket_thread();
        serversocket_thread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // send=intent.getStringExtra("send");
        //mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        // Display a notification about us starting.  We put an icon in the status bar.
        //showNotification();
        //ServerSocket_thread serversocket_thread = new ServerSocket_thread();
        //serversocket_thread.start();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */

    class ServerSocket_thread extends Thread
    {
        public void run()//重写Thread的run方法
        {
            System.out.print(send+"\n");
            try
            {
                /*int port =Integer.valueOf(portEditText.getText().toString());//获取portEditText中的端口号*/
                serverSocket = new ServerSocket(12345);//监听port端口，这个程序的通信端口就是port了
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            while (true)
            {
                try
                {
                    System.out.print(send+"\n");
                    System.out.print("seversocket_thread启动"+"\n");
                    //监听连接 ，如果无连接就会处于阻塞状态，一直在这等着
                    clicksSocket = serverSocket.accept();
                    inputstream = clicksSocket.getInputStream();
                    //启动接收线程
                    outputStream=clicksSocket.getOutputStream();
                    Receive_Thread receive_Thread = new Receive_Thread();

                    receive_Thread.start();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    class sendThread extends Thread{
        public void run(){
            while (send!=null){
                try {
                    outputStream.write(send.getBytes());
                    outputStream.flush();
                    System.out.print(send + "\n");
                    send=null;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     *
     * 接收线程
     *
     */
    class Receive_Thread extends Thread//继承Thread
    {
        public void run()//重写run方法
        {
            while (true)
            {
                try
                {
					/*int count=inputstream.available();*/
                    final byte[] buf = new byte[4];
					/*final int len = inputstream.read(buf);*/
                    inputstream.read(buf);
                    System.out.println(buf);
                    System.out.print("\n");

/*                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
							*//*receiveEditText.setText(new String(buf,0,len));*//*
                            receiveEditText.setText(receiveEditText.getText().toString()+mathhelper.str2HexStr(buf));
                        }
                    });*/
                    String bb;
                    /*bb=mathhelper.str2HexStr(buf);*/
                    bb=binary(buf,2);
                    System.out.print("Receive_Thread启动"+bb+"\n");
                    Message msg=new Message();
                    msg.obj=bb;
                    handler.sendMessage(msg);

                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
    /**
     * 发送消息按钮事件
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }
}




