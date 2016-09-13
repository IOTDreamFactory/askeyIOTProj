package iotdf.iotgateway.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;

import iotdf.iotgateway.ChooseDevice;
import iotdf.iotgateway.ConServ.Myservice;
import iotdf.iotgateway.R;

public class DataTest extends AppCompatActivity implements View.OnClickListener {
    private Button Button_back;
    private Button Button_auto;
    private Button ButtonStart;
    private Button ButtonKill;
    private String TAG=null;
    private Boolean flag;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);
        Button_back = (Button) findViewById(R.id.button_back);
        Button_auto = (Button) findViewById(R.id.button_auto);
        ButtonStart = (Button) findViewById(R.id.button_start);
        ButtonKill = (Button) findViewById(R.id.button_kill);
        Button_back.setOnClickListener(this);
        Button_auto.setOnClickListener(this);
        ButtonStart.setOnClickListener(this);
        ButtonKill.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                Intent intent = new Intent(DataTest.this, ChooseDevice.class);
                startActivity(intent);
                break;
            case R.id.button_auto:
                DataService mDataService1 = new DataService(DataTest.this);
                Data data1 = new Data();
                Date date = new Date();
                long datetime = date.getTime();
                for (int j = 1; j <= 10; j++) {
                    for (int i = 0; i < 13; i++) {

                        data1.setBrightness((float) Math.random() * 100f + "");
                        data1.setHumidity((float) Math.random() * 100f + "");
                        data1.setId("传感器" + j);
                        data1.setTemp((float) Math.random() * 100f + "");
                        data1.setTime(datetime + i);
                        data1.setWater((float) Math.random() * 100f + "");
                        mDataService1.InsertData(data1);
                    }
                }
                break;
            case R.id.button_start:
                Intent intent1 = new Intent(DataTest.this, Myservice.class);
                startService(intent1);
//                bindService();
                System.out.println("startService");
                break;
            case R.id.button_kill:
//                unBindService();
                break;
        }

    }

//    private void bindService() {
//        Intent intent = new Intent(DataTest.this, Myservice.class);
//        Log.i(TAG, "bindService()");
//        bindService(intent, conn, Context.BIND_AUTO_CREATE);
//    }
//
//    private void unBindService() {
//        Log.i(TAG, "unBindService() start....");
//        if (flag == true) {
//            Log.i(TAG, "unBindService() flag");
//            unbindService(conn);
//            flag = false;
//        }
//    }
//
//    private ServiceConnection conn = new ServiceConnection() {
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            // TODO Auto-generated method stub
//            Log.i(TAG, "onServiceDisconnected()");
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            // TODO Auto-generated method stub
//            Log.i(TAG, "onServiceConnected()");
//            Myservice.MyBinder binder = (Myservice.MyBinder) service;
//            Myservice bindService = binder.getservice();
//            bindService.MyMethod();
//            flag = true;
//        }
//    };
}
