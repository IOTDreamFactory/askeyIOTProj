package iotdf.iotgateway.ConServ;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import iotdf.iotgateway.data.DataService;

/**
 * Created by hh123 on 2016/7/4.
 */
public class Myservice extends Service {
    String host;// = "tcp://218.199.150.148:1883";
    String userName = "one";
    String passWord = "password";
    String pub;
    String sub;
    String TAG=null;
    Message msg = new Message();
    private Handler handler;
    private MqttClient client;
    private String myTopic = "test";
    private MqttConnectOptions options;
    private ScheduledExecutorService scheduler;

//    @Override
//    public void onStart(Intent intent, int startId) {
//        super.onStart(intent, startId);
//        myTopic=intent.getStringExtra("Username");
//    }

    public void onCreate() {
        super.onCreate();
    }
        private void startReconnect() {                                //如果未连接上启动connect
            scheduler = Executors.newSingleThreadScheduledExecutor();//创建一个单线程池
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if(!client.isConnected()) {
                        connect();
                    }
                }
            }, 30 * 1000, 31 * 1000, TimeUnit.MILLISECONDS);
        }
        private void init() {
            try {
                //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
                client = new MqttClient(host, "test",
                        new MemoryPersistence());
                //MQTT的连接设置
                options = new MqttConnectOptions();
                //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
                options.setCleanSession(true);
                //设置连接的用户名
                options.setUserName(userName);
                //设置连接的密码
                options.setPassword(passWord.toCharArray());
                // 设置超时时间 单位为秒
                options.setConnectionTimeout(10);
                // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
                options.setKeepAliveInterval(20);
                client.setTimeToWait(30);/*设置一个动作等待时间*/
                //设置回调
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        //连接丢失后，一般在这里面进行重连
                        System.out.println("connectionLost----------");
                    }
                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        //publish后会执行到这里
                        System.out.println("deliveryComplete---------"
                                + token.isComplete());
                    }
                    @Override
                    public void messageArrived(String topicName, MqttMessage message)
                            throws Exception {
                        //subscribe后得到的消息会执行到这里面
                        System.out.println("messageArrived----------");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i=new Intent();
                                i.putExtra("sub",sub);
                                i.setAction("iotdf.iotgateway.ConServ.Myservice");
                                sendBroadcast(i);
                            }
                        }).start();
                        msg.what = 1;
                        msg.obj = topicName+"---"+message.toString();
                        sub=message.toString();
                        handler.sendMessage(msg);
                    }
                });
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void connect() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        client.connect(options);;
                        msg.what = 2;
//                        handler.sendMessage(msg);
                        if(handler.obtainMessage(msg.what, msg.obj) != null){
                            Message _msg = new Message();
                            _msg.what = msg.what;
                            _msg.obj= msg.obj;
                            msg = _msg;
//			return;
                        }
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.what = 3;
                        if(handler.obtainMessage(msg.what, msg.obj) != null){
                            Message _msg = new Message();
                            _msg.what = msg.what;
                            _msg.obj= msg.obj;
                            msg = _msg;
//			return;
                        }
                        handler.sendMessage(msg);
                    }
                }
            }).start();
        }
    public void MyMethod(){
        Log.i(TAG, "BindService-->MyMethod()");
    }
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
//        host=intent.getStringExtra("host");
        myTopic=intent.getStringExtra("Username");
        host="tcp://218.199.150.207:1883";
//        pub =intent.getStringExtra("pub");
//        测试
        DataService mDataService=new DataService(Myservice.this);
        ArrayList<Map> HistoryList=mDataService.updateData("传感节点1",mDataService.getLastUDTime(this));
        mDataService.setLastUDTime(this);
        pub=HistoryList.toString();
//        pub= "test1";
        //        测试
        init();
        handler = new Handler(){                             //重写handlermessage 从线程传来数据
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 1) {
                   // Toast.makeText(Myservice.this, (String) msg.obj,
                     //       Toast.LENGTH_SHORT).show();
                 // new Thread(new Runnable() {

                 //       public void run() {
                  //          Intent i=new Intent();
                   //         i.putExtra("sub",sub);
                     //       i.setAction("example.hh123.iot.Myservice");
                       //     sendBroadcast(i);
                       // }
                   // }).start();
                    System.out.println("-----------------------------");
                } else if(msg.what == 2) {
                    System.out.println(" has connect");
                 //   Toast.makeText(Myservice.this, "连接成功", Toast.LENGTH_SHORT).show();
                    try {
                        client.subscribe(myTopic, 1);//接受
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(pub!=null) {
                        try {
                            client.publish(myTopic, pub.getBytes(), 1, false);//发送
//                            int i=1;
//                            isArrival(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if(msg.what == 3) {
                    //Toast.makeText(Myservice.this, "连接失败，系统正在重连", Toast.LENGTH_SHORT).show();
                System.out.println("failed");
                }
            }
        };
        startReconnect();
        //return super.onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }
    @Override
        public void onDestroy() {
            super.onDestroy();
            try {
               // scheduler.shutdown();
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
//    public boolean isArrival(int arrivestatus){
//        if(arrivestatus==1)
//        {
//            return true;
//        }
//        return false;
//    }
    public IBinder onBind(Intent intent) {
        return null;
    }
//    public class MyBinder extends Binder{
//        public Myservice getservice(){
//            return  Myservice.this;
//        }
//    }
}
