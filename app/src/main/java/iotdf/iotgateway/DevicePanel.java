package iotdf.iotgateway;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DevicePanel extends AppCompatActivity {

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_device_panel);
        tv = (TextView) findViewById(R.id.first_page_info);
        Bundle bundle = this.getIntent().getExtras();
        ArrayList position = bundle.getParcelableArrayList("position");
        String str=new String();
        for (Object Pos:position
             ) {
            str=str+" "+Pos;
        }
        tv.setText(str);
    }
}
