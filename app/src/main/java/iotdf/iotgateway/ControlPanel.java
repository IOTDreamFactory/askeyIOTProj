package iotdf.iotgateway;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import iotdf.iotgateway.ConSens.LocalService;
import iotdf.iotgateway.DeviceFragments.InputFrag;

public class ControlPanel extends AppCompatActivity implements InputFrag.inputListener {
    private LocalService mBoundService;

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
    void doBindService(){
        Intent intent=new Intent(ControlPanel.this,LocalService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
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
}
