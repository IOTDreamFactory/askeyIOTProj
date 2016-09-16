package iotdf.iotgateway;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iotdf.iotgateway.ConSens.LocalService;
import iotdf.iotgateway.ConServ.ServerRequest;
import iotdf.iotgateway.RestComponents.MyBaseActivity;

public class MainActivity extends MyBaseActivity implements View.OnClickListener, iotdf.iotgateway.Register.UsernameListener {
    


    private EditText UserName;
    private EditText PWD;
    private Button startService;
    private Button stopService;
    private Button Login;
    private ImageButton Register;
    private android.content.Intent Intent;
/*    private long exitTime = 0;*/
    private String mDeviceID;
    private Bundle bundle;
    private String getUsername=null;
    List<NameValuePair> params;
    SharedPreferences pref;
    String LoginURL="http://218.199.150.207:8080/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // mDeviceID = Secure.getString(this.getContentResolver(),
        // Secure.ANDROID_ID);
        mDeviceID = "zero";
        Login=(Button)findViewById(R.id.button_login);
        Register=(ImageButton) findViewById(R.id.button_register);
        UserName=(EditText)findViewById(R.id.ETusr);
        PWD=(EditText)findViewById(R.id.ETpwd);
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
        Intent intent1=new Intent(MainActivity.this, LocalService.class);
        startService(intent1);
        SharedPreferences preferences = getSharedPreferences("name", MODE_PRIVATE);
        if(preferences.getString("username",null)!=null){
        UserName.setText(preferences.getString("username",null));
        UserName.setHint(preferences.getString("username",null));
        }
/*        bundle=this.getIntent().getExtras();
        if(bundle.getString("Username")!=null){
        getUsername=bundle.getString("Username");
        UserName.setText(getUsername);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", UserName.getText().toString()));
                params.add(new BasicNameValuePair("password", PWD.getText().toString()));
                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON(LoginURL,params);
                if(json != null) {
                    try {
                        String jsonstr = json.getString("response");
                        if (jsonstr!=null) {
//                            String token = json.getString("token");
//                            String grav = json.getString("grav");
//                            SharedPreferences.Editor edit = pref.edit();
//                            //Storing Data using SharedPreferences
//                            edit.putString("token", token);
//                            edit.putString("grav", grav);
//                            edit.commit();
                            Intent intent = new Intent(MainActivity.this, ChooseDevice.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("Username",UserName.getText().toString());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }

                        Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                UserService uService=new UserService(MainActivity.this);
//                boolean flag=uService.login(UserName.getText().toString(), PWD.getText().toString());
//                //判断用户输入的用户名和密码是否与设置的值相同,必须要有toString()
//                if(flag){
//                    System.out.println("你点击了按钮");
//                    //创建Intent对象，传入源Activity和目的Activity的类对象
//                    Intent = new Intent(MainActivity.this, ChooseDevice.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Username",UserName.getText().toString());
//                    Intent.putExtras(bundle);
//                    //Intent.putExtra("Username",UserName.getText());
//                    //启动Activity
//
//                    startActivity(Intent);
//                    finish();
//                }else{
//                    //登录信息错误，通过Toast显示提示信息
//                    Toast.makeText(MainActivity.this,"用户登录信息错误" , Toast.LENGTH_SHORT).show();
//                }
//                }
                break;
            case R.id.button_register:
                Intent=new Intent(MainActivity.this,Register.class);
                startActivity(Intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getUsername(final String username) {
        MainActivity.this.runOnUiThread(new Runnable() {

            @Override

            public void run() {
                UserName.setText(username);
            }
        });
    }
}