package iotdf.iotgateway;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import iotdf.iotgateway.user.*;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText UserName;
    private EditText PWD;


    private Button startService;

    private Button stopService;
    private Button Login;
    private Button Regist;
    private android.content.Intent Intent;

    private String mDeviceID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mDeviceID = Secure.getString(this.getContentResolver(),
        // Secure.ANDROID_ID);
        mDeviceID = "zero";
        ((TextView) findViewById(R.id.target_text)).setText(mDeviceID);
        startService = (Button) findViewById(R.id.startService);
        stopService = (Button) findViewById(R.id.stopService);
        Login=(Button)findViewById(R.id.button_login);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        Login=(Button)findViewById(R.id.button_login);
        Regist=(Button) findViewById(R.id.button_sign);
        UserName=(EditText)findViewById(R.id.LoginUser);
        PWD=(EditText)findViewById(R.id.LoginPassword);
        Login.setOnClickListener(this);
        Regist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
/*            case R.id.startService:
                SharedPreferences.Editor editor = getSharedPreferences(PushService2.TAG,
                        MODE_PRIVATE).edit();
                editor.putString(PushService2.PREF_DEVICE_ID, mDeviceID);
                editor.commit();
                PushService2.actionStart(getApplicationContext());
                startService.setEnabled(false);
                stopService.setEnabled(true);
                break;
            case R.id.stopService:
                PushService2.actionStop(getApplicationContext());
                startService.setEnabled(true);
                stopService.setEnabled(false);
                break;*/
            case R.id.button_login:
                UserService uService=new UserService(MainActivity.this);
                boolean flag=uService.login(UserName.getText().toString(), PWD.getText().toString());
                //判断用户输入的用户名和密码是否与设置的值相同,必须要有toString()
                if(flag){
                    System.out.println("你点击了按钮");
                    //创建Intent对象，传入源Activity和目的Activity的类对象
                    Intent = new Intent(MainActivity.this, ChooseDevice.class);
                    //启动Activity
                    startActivity(Intent);
                }else{
                    //登录信息错误，通过Toast显示提示信息
                    Toast.makeText(MainActivity.this,"用户登录信息错误" , Toast.LENGTH_SHORT).show();}
                break;
            case R.id.button_sign:
                Intent=new Intent(MainActivity.this,Regist.class);
                startActivity(Intent);
                break;
            default:
                break;
        }
    }
}