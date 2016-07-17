package iotdf.iotgateway;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iotdf.iotgateway.user.*;

public class Regist extends AppCompatActivity {
    private EditText UserName;
    private EditText PWD;
    private Button Submit;
    private Button Back;
    private Context context;
    private android.content.Intent Intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        UserName=(EditText)findViewById(R.id.editText_UserName);
        PWD=(EditText)findViewById(R.id.editText_Pwd);
        Submit=(Button)findViewById(R.id.button_submit);
        Back=(Button)findViewById(R.id.button_back);
        Submit.setOnClickListener(ocl);
        Back.setOnClickListener(ocl);
    }
    View.OnClickListener ocl=new View.OnClickListener(){

        public void onClick(View v){
            switch (v.getId()){
                case R.id.button_submit:
                    UserService uService=new UserService(Regist.this);
                    User user=new User();
                    user.setUsername(UserName.getText().toString());
                    user.setPassword(PWD.getText().toString());
                    uService.register(user);
                    Intent=new Intent(Regist.this,MainActivity.class);
                    startActivity(Intent);
                    Toast.makeText(Regist.this,"注册成功!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button_back:
                    Intent=new Intent(Regist.this,MainActivity.class);
                    startActivity(Intent);
                    break;
            }
        }
    };
}
