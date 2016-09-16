package iotdf.iotgateway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iotdf.iotgateway.ConServ.ServerRequest;
import iotdf.iotgateway.user.User;
import iotdf.iotgateway.user.UserService;

public class Register extends AppCompatActivity {
    public interface UsernameListener{
        void getUsername(String username);
    }
    private EditText UserName;
    private EditText PWD;
    private ImageButton Submit;
    private ImageButton Back;
    private Context context;
    private android.content.Intent Intent;
    private int flag=0;
    List<NameValuePair> params;
    String RegisterURL="http://218.199.150.207:8080/register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        UserName=(EditText)findViewById(R.id.ET_USR);
        PWD=(EditText)findViewById(R.id.ET_PWD);
        Submit=(ImageButton)findViewById(R.id.ImageButton_submit);
        Back=(ImageButton)findViewById(R.id.sign_back);
        Submit.setOnClickListener(ocl);
        Back.setOnClickListener(ocl);
        UserName.addTextChangedListener(new EditChangedListener());
        PWD.addTextChangedListener(new EditChangedListener2());
        Submit.setEnabled(false);
    }
    View.OnClickListener ocl=new View.OnClickListener(){

        public void onClick(View v){
            switch (v.getId()){
                case R.id.ImageButton_submit:
                    params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("username", UserName.getText().toString()));
                    params.add(new BasicNameValuePair("password", PWD.getText().toString()));
                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON(RegisterURL,params);
                    //JSONObject json = sr.getJSON("http://192.168.56.1:8080/register",params);

                    if(json != null){
                        try{
                            String jsonstr = json.getString("response");
                            Toast.makeText(getApplication(),jsonstr,Toast.LENGTH_LONG).show();

                            Log.d("Hello", jsonstr);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    UserService uService=new UserService(Register.this);
                    User user=new User();
                    user.setUsername(UserName.getText().toString());
                    user.setPassword(PWD.getText().toString());
                    uService.register(user);
                    SharedPreferences preferences=getSharedPreferences("name", MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("username",UserName.getText().toString());
                    editor.commit();
                    Intent=new Intent(Register.this,MainActivity.class);
                    startActivity(Intent);
                    finish();
                    Toast.makeText(Register.this,"注册成功!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sign_back:
                    Intent=new Intent(Register.this,MainActivity.class);
                    startActivity(Intent);
                    finish();
                    break;
            }
        }
    };
    class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int UserNameStart;//光标开始位置
        private int UserNameEnd;//光标结束位置
        private final int charMaxNum = 12;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp=s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            UserService mService=new UserService(Register.this);
            UserNameStart = UserName.getSelectionStart();
            UserNameEnd = UserName.getSelectionEnd();
            if (temp.length() > charMaxNum)
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
            else if(temp.length()==0)
                Toast.makeText(getApplicationContext(), "不能为空！！", Toast.LENGTH_SHORT).show();
            else if (mService.registerCheck(s.toString()))
            {Toast.makeText(getApplicationContext(),"该用户名不可用",Toast.LENGTH_SHORT).show();
                flag=0;}
            else flag=1;
        }
    };
    class EditChangedListener2 implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int Start;//光标开始位置
        private int End;//光标结束位置
        private final int charMaxNum = 12;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp=s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Start = PWD.getSelectionStart();
            End = PWD.getSelectionEnd();
            if (temp.length() > charMaxNum)
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
            else if(temp.length()==0)
                Toast.makeText(getApplicationContext(), "不能为空！！", Toast.LENGTH_SHORT).show();
            else if (flag==1)
                Submit.setEnabled(true);

        }
    };
}
