package iotdf.iotgateway.ConServ;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.ApplicationTestCase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import iotdf.iotgateway.ChooseDevice;
import iotdf.iotgateway.R;

public class ConSetting extends AppCompatActivity implements View.OnClickListener {
    String host, pub, sub;
    TextView textsub;
    EditText text, uri;
    private Button button_accept;
    private Button button_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_setting);
        text = (EditText) findViewById(R.id.pub);
        //  pub= text.getText().toString();
        uri = (EditText) findViewById(R.id.uri);
//        button_accept = (Button) findViewById(R.id.button_accept);
        button_back = (Button) findViewById(R.id.button_back);
        // host=uri.getText().toString();
        Button star = (Button) findViewById(R.id.star);
        Button stop = (Button) findViewById(R.id.stop);
        textsub = (TextView) findViewById(R.id.sub);
        star.setOnClickListener(new starclick());
        stop.setOnClickListener(new stopclick());
//        button_accept.setOnClickListener(this);
        button_back.setOnClickListener(this);
    }



    class starclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            pub = text.getText().toString();
            host = uri.getText().toString();
            Intent intent = new Intent();
            // Bundle bundle=new Bundle();
            //Bundle bundle1=new Bundle();
            //bundle1.putString("pub",pub);
            //bundle.putString("uri",hosturi);
            //intent.putExtras(bundle);
            //intent.putExtras(bundle1);
            intent.setClass(ConSetting.this, Myservice.class);
            intent.putExtra("pub", pub);
            intent.putExtra("host", host);
            startService(intent);
        }
    }

    class stopclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(ConSetting.this, Myservice.class);
            stopService(intent);
        }
    }

    class receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent i) {
            Bundle bundle2 = i.getExtras();
            sub = bundle2.getString("sub");
            textsub.setText(sub);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            /*case R.id.button_accept:
                Intent intent=new Intent();
                startActivity(intent);
                break;*/
            case R.id.button_back:
                Intent intent=new Intent(ConSetting.this, ChooseDevice.class);
                startActivity(intent);
                break;
        }
    }
}


