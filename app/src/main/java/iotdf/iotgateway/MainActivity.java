package iotdf.iotgateway;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

public class MainActivity extends MyBaseActivity implements View.OnClickListener, iotdf.iotgateway.Register.UsernameListener, CompoundButton.OnCheckedChangeListener {
    


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
    private RelativeLayout relativeLayout;
    List<NameValuePair> params;
    SharedPreferences pref;
    String LoginURL="http://218.199.150.207:8080/login";
    private CheckBox checkBox;
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
        relativeLayout=(RelativeLayout)findViewById(R.id.relativeLayout_main);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        relativeLayout.setOnTouchListener(onTouchListener);
        UserName.setOnFocusChangeListener(onFocusChangeListener);
        PWD.setOnFocusChangeListener(onFocusChangeListener);
        checkBox.setOnCheckedChangeListener(this);
        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
        Intent intent1=new Intent(MainActivity.this, LocalService.class);
        startService(intent1);
        SharedPreferences preferences = getSharedPreferences("name", MODE_PRIVATE);
        if(preferences.getString("username",null)!=null){
        UserName.setText(preferences.getString("username",null));
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
                if(checkBox.isChecked()){
                    Intent intent = new Intent(MainActivity.this, ChooseDevice.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", "离线用户");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", UserName.getText().toString()));
                    params.add(new BasicNameValuePair("password", PWD.getText().toString()));
                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON(LoginURL, params);
                    if (json != null) {
                        try {
                            String jsonstr = json.getString("response");
                            System.out.println(jsonstr);
                            if (jsonstr != null) {
                                Intent intent = new Intent(MainActivity.this, ChooseDevice.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("Username", UserName.getText().toString());
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }

                            Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(UserName.getText().toString()!=null){
                        SharedPreferences preferences=getSharedPreferences("name", MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("username",UserName.getText().toString());
                        editor.commit();}
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
                }
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

    /**
     * 给布局加触摸监听，当点击EditText之外的地方时
     * EditText失去焦点
     */
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            relativeLayout.setFocusable(true);
            relativeLayout.setFocusableInTouchMode(true);
            relativeLayout.requestFocus();//布局获得焦点
            return false;
        }
    };

    /**
     * 下面的OnFocusChangeListener的作用主要是
     * 点击EditText时获取焦点并隐藏hint值
     * */
    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            // TODO Auto-generated method stub

            switch (v.getId()) {
                case R.id.ET_USR:
                    setHintEt(UserName,hasFocus);
                    break;
                case R.id.ET_PWD:
                    setHintEt(PWD,hasFocus);
                    break;
                default:
                    break;
            }
        }
    };
    private void setHintEt(EditText et,boolean hasFocus){
        String hint;
        if(hasFocus){
            hint = et.getHint().toString();
            et.setTag(hint);
            et.setHint("");
        }else{
            hint = et.getTag().toString();
            et.setHint(hint);
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

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        checkBox.setChecked(b);
    }
}