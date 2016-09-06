package iotdf.iotgateway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import iotdf.iotgateway.DeviceFragments.InputFrag;

public class ControlPanel extends AppCompatActivity implements InputFrag.inputListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
        ButterKnife.inject(this);
    }
    @OnClick({R.id.back,R.id.imageButton,R.id.imageButton2,R.id.imageButton3,R.id.imageButton4})
    public void onclick(View view)
    {
        switch (view.getId()){
            case R.id.back:
                Intent intent=new Intent(ControlPanel.this,ChooseDevice.class);
                startActivity(intent);
                break;
            case R.id.imageButton:break;
            case R.id.imageButton2:break;
            case R.id.imageButton3:break;
            case R.id.imageButton4:
                InputFrag dialog = new InputFrag();
                dialog.show(getSupportFragmentManager(), "loginDialog");
                break;
        }
    }

    @Override
    public void onInputComplete(String inputValue) {

    }
}
