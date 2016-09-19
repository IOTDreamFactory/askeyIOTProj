package iotdf.iotgateway;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Slider;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import iotdf.iotgateway.ConSens.LocalService;
import iotdf.iotgateway.DeviceFragments.InputFrag;
import iotdf.iotgateway.RestComponents.mathhelper;

public class ControlPanel extends AppCompatActivity implements InputFrag.inputListener ,Slider.OnPositionChangeListener, CompoundButton.OnCheckedChangeListener {
    private LocalService mBoundService;
    private Boolean mIsBound;
    private String str;
    private String arduinoNum;
    @InjectView(R.id.switch1)
    SwitchCompat switchCompat;
    @InjectView(R.id.tv_time)
    TextView Tv_time;
    @InjectView(R.id.Sliderview)
    Slider slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        ButterKnife.inject(this);
        doBindService();
        Bundle bundle = this.getIntent().getExtras();
        ArrayList position = bundle.getParcelableArrayList("position");
        slider.setOnPositionChangeListener(this);
        switchCompat.setOnCheckedChangeListener(this);
        for (Object Pos:position
                ) {
            str=str+","+Pos;
        }
        final String[] postionInfo=str.split(",");
        arduinoNum=postionInfo[1].replaceAll("\\D","");//传感节点编号,过滤掉所有非数字字符
        switch(arduinoNum){
            case "0":arduinoNum="000";break;
            case "1":arduinoNum="001";break;
            case "2":arduinoNum="010";break;
            case "3":arduinoNum="011";break;
            case "4":arduinoNum="100";break;
            case "5":arduinoNum="101";break;
            case "6":arduinoNum="110";break;
            case "7":arduinoNum="111";break;
        }
    }
    @OnClick({R.id.back,R.id.imageButton,R.id.imageButton2,R.id.imageButton3,R.id.imageButton4,R.id.reboot})
    public void onclick(View view)
    {
        switch (view.getId()){
            case R.id.back:
                Intent intent=new Intent(ControlPanel.this,ChooseDevice.class);
                startActivity(intent);
                finish();
                break;
            case R.id.imageButton:
                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000010101011"),arduinoNum);
                break;
            case R.id.imageButton2:
                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000111101011"),arduinoNum);
                break;
            case R.id.imageButton3:
                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000001111001011"),arduinoNum);
                break;
            case R.id.imageButton4:
                InputFrag dialog = new InputFrag();
                dialog.show(getSupportFragmentManager(), "loginDialog");
                break;
            case R.id.reboot:
                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"00000100000000000011011"),arduinoNum);
                System.out.println("重启");
                break;
        }
    }

    @Override
    public void onInputComplete(String inputValue) {
        mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"0000000"+mathhelper.completeBin(Integer.toBinaryString(Integer.parseInt(inputValue)))+"1011"),arduinoNum);
    }
    void doBindService(){
        Intent intent=new Intent(ControlPanel.this,LocalService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        mIsBound=true;
    }
    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((LocalService.LocalBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    @Override
    public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
        int posInt;
        if(newValue>oldValue)
            posInt=100;
        else
            posInt=0;
        mBoundService.send(mathhelper.str2HexStr("101001"+arduinoNum+"0000000"+mathhelper.completeBin(Integer.toBinaryString(posInt))+"1011"),arduinoNum);
        System.out.println(mathhelper.str2HexStr("101001"+arduinoNum+mathhelper.completeBin(Integer.toBinaryString(newValue))));
        System.out.println(newValue+"");
        System.out.println(mathhelper.completeBin(Integer.toBinaryString(newValue)));
        String testStr="101001"+arduinoNum+"0000000"+mathhelper.completeBin(Integer.toBinaryString(newValue))+"1011";
        System.out.println("长度："+mathhelper.completeBin(Integer.toBinaryString(newValue)).length());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean switchState) {
        if(switchState){
            mBoundService.send(mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000001011"),arduinoNum);
            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000001011"));
            Toast toast3=Toast.makeText(this,"电机反转", Toast.LENGTH_SHORT);
            toast3.show();
        }
        else{
            mBoundService.send(mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000011011"),arduinoNum);
            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000011011"));
            Toast toast3=Toast.makeText(this,"电机正转",Toast.LENGTH_SHORT);
            toast3.show();
        }
    }
}
