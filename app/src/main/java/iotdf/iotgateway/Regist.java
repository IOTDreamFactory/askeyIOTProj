package iotdf.iotgateway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import iotdf.iotgateway.user.*;

public class Regist extends AppCompatActivity {
    private EditText UserName;
    private EditText PWD;
    private Button Submit;
    private ImageButton Back;
    private Context context;
    private android.content.Intent Intent;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        UserName=(EditText)findViewById(R.id.SignUserEdit);
        PWD=(EditText)findViewById(R.id.SignPassEdit);
        Submit=(Button)findViewById(R.id.button_login);
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
                case R.id.button_login:
                    UserService uService=new UserService(Regist.this);
                    User user=new User();
                    user.setUsername(UserName.getText().toString());
                    user.setPassword(PWD.getText().toString());
                    uService.register(user);
                    Intent=new Intent(Regist.this,MainActivity.class);
                    startActivity(Intent);
                    Toast.makeText(Regist.this,"注册成功!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sign_back:
                    Intent=new Intent(Regist.this,MainActivity.class);
                    startActivity(Intent);
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
            UserService mService=new UserService(Regist.this);
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
