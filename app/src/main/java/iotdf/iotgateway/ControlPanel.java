package iotdf.iotgateway;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
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
    @OnClick({R.id.back,R.id.imageButton,R.id.imageButton2,R.id.imageButton3,R.id.imageButton4})
    public void onclick(View view)
    {
        switch (view.getId()){
            case R.id.back:
                Intent intent=new Intent(ControlPanel.this,ChooseDevice.class);
                startActivity(intent);
                break;
            case R.id.imageButton:
                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000010101011"));
                break;
            case R.id.imageButton2:
                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000000111101011"));
                break;
            case R.id.imageButton3:
                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"00000000000001111001011"));
                break;
            case R.id.imageButton4:
                InputFrag dialog = new InputFrag();
                dialog.show(getSupportFragmentManager(), "loginDialog");
                break;
        }
    }

    @Override
    public void onInputComplete(String inputValue) {
        mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum+"0000000"+mathhelper.completeBin(Integer.toBinaryString(Integer.parseInt(inputValue)))+"1011"));
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
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((LocalService.LocalBinder)service).getService();
            // Tell the user about this for our demo.

        }
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;

        }
    };

    @Override
    public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
        mBoundService.send(mathhelper.str2HexStr("101001"+arduinoNum+"0000000"+mathhelper.completeBin(Integer.toBinaryString(newValue))+"1011"));
        System.out.println(mathhelper.str2HexStr("101001"+arduinoNum+mathhelper.completeBin(Integer.toBinaryString(newValue))));
        System.out.println(newValue+"");
        System.out.println(mathhelper.completeBin(Integer.toBinaryString(newValue)));
        String testStr="101001"+arduinoNum+"0000000"+mathhelper.completeBin(Integer.toBinaryString(newValue))+"1011";
        System.out.println("长度："+mathhelper.completeBin(Integer.toBinaryString(newValue)).length());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean switchState) {
        if(switchState){
            mBoundService.send(mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000001011"));
            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000001011"));
            Toast toast3=Toast.makeText(this,"电机反转", Toast.LENGTH_SHORT);
            toast3.show();
        }
        else{
            mBoundService.send(mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000011011"));
            System.out.println("发送："+mathhelper.str2HexStr("101001"+arduinoNum+"01000000000000000011011"));
            Toast toast3=Toast.makeText(this,"电机正转",Toast.LENGTH_SHORT);
            toast3.show();
        }
    }
}
class mathhelper
{
    public static String str2HexStr(String integer) {
        StringBuffer integerSum = new StringBuffer();
        int loop = 0; // 循环次数
        if (integer.length() % 4 == 0) {
            loop = integer.length() / 4;
        } else {
            loop = integer.length() / 4 + 1;
        }
        String binary = "";
        for (int i = 1; i <= loop; i++) {
            if (i != loop) {
                binary = integer.substring(integer.length() - i * 4,
                        integer.length() - i * 4 + 4);
            } else {
                binary = mathhelper.appendZero(
                        integer.substring(0, integer.length() - (i - 1) * 4),
                        4, true);
            }
            integerSum.append(mathhelper.toHex(String.valueOf(mathhelper
                    .binaryIntToDecimalis(binary))));
        }
        return integerSum.reverse().toString();
    }
    public static String appendZero(String str, int len, boolean flag) {
        String zero = "0";
        if (null == str || str.length() == 0) {
            return "";
        }
        if (str.length() >= len) {
            return str;
        }
        for (int i = str.length(); i < len; i++) {
            if (flag) {
                str = zero + str;
            } else {
                str += zero;
            }
        }
        return str;
    }
    public static String toHex(String hex) {
        String str = "";
        switch(Integer.parseInt(hex)){
            case 10 : str = "a"; break;
            case 11 : str = "b"; break;
            case 12 : str = "c"; break;
            case 13 : str = "d"; break;
            case 14 : str = "e"; break;
            case 15 : str = "f"; break;
            default : str = hex;
        }
        return str;
    }
    public static int binaryIntToDecimalis(String inteter) {
        int inteterSum = 0;
        for (int i = inteter.length(); i > 0; i--) {
            int scale = 2;
            if (inteter.charAt(-(i - inteter.length())) == '1') {
                if (i != 1) {
                    for (int j = 1; j < i - 1; j++) {
                        scale *= 2;
                    }
                } else {
                    scale = 1;
                }
            } else {
                scale = 0;
            }
            inteterSum += scale;
        }
        return inteterSum;
    }
    public static String completeBin(String Bin){
        ArrayList<String> complete0=new ArrayList<>();
        if(Bin.length()<12)
        {
            for (int i=0;i<12-Bin.length();i++)
                complete0.add("0");
            Bin= TextUtils.join(",",complete0).replaceAll("\\D","")+Bin;
        }
        return Bin;
    }
 /*   public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
 */
}
