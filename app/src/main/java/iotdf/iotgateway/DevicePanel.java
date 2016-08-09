package iotdf.iotgateway;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iotdf.iotgateway.ConSens.LocalService;
import iotdf.iotgateway.DeviceFragments.Chart;
import iotdf.iotgateway.DeviceFragments.Control;
import iotdf.iotgateway.DeviceFragments.FragmentCallback;
import iotdf.iotgateway.DeviceFragments.History;

public class DevicePanel extends FragmentActivity implements TabHost.OnTabChangeListener,FragmentCallback {

    private LocalService mBoundService;
    private Boolean mIsBound;
    private String NodeId="NodeID:";
    private String Connect="Connection:";
    private String SenorType="";
    private String[] title = new String[] { "图表", "记录", "操作" };
    private boolean historyFlag=false;
    private boolean controlFlag=false;
    int[] tabIds = new int[] { R.id.tab1, R.id.tab2, R.id.tab3 };
    int[] logos = new int[]{
            R.drawable.ico_chart,
            R.drawable.ico_history,
            R.drawable.ico_control
    };

    private TabHost mTabHost;
    private Bundle savedInstanceState1;
    private String str=new String();

    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstanceState1=savedInstanceState;
        this.setContentView(R.layout.activity_device_panel);
        doBindService();
        updateIn();
        tv = (TextView) findViewById(R.id.first_page_info);
        tv.setTextColor(Color.WHITE);
        Bundle bundle = this.getIntent().getExtras();
        ArrayList position = bundle.getParcelableArrayList("position");

        for (Object Pos:position
             ) {
            str=str+","+Pos;
        }
        tv.setText(str);

        setupTabs();
    }

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        //Intent intent=new Intent(LocalServiceDemoActivity.this,LocalService.class);
        Intent intent=new Intent(DevicePanel.this,LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }


    @Override
    public void onTabChanged(String tag) {
        Fragment frag = null;
        int contentViewID = 0;
        if (tag.equalsIgnoreCase("图表")&&savedInstanceState1==null) {
            /*frag = new Chart(); //自定义继承Fragment的UI，放了一个简单的显示文本标题的控件。*/
            contentViewID = R.id.tab1;
        }  else if (tag.equalsIgnoreCase("操作")&&savedInstanceState1==null){
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.tab3, Control.newInstance(str)).commit();
            contentViewID=R.id.tab3;*/
            if(!controlFlag)
            {
                Fragment control = new Control();
                Bundle bundle=new Bundle();
                bundle.putString("Position",str);
                control.setArguments(bundle);
                FragmentManager manager = this.getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.add(R.id.tab3, control, "操作");
                trans.commit();
                controlFlag=true;
            }
            contentViewID = R.id.tab3;
        }else if (tag.equalsIgnoreCase("记录")&&savedInstanceState1==null) {
            /*getSupportFragmentManager().beginTransaction()
                    .add(R.id.tab2, History.newInstance(str)).commit();
            contentViewID = R.id.tab2;*/
            if(!historyFlag) {
                Fragment history = new History();
                Bundle bundle = new Bundle();
                bundle.putString("Position", str);
                history.setArguments(bundle);
                FragmentManager manager = this.getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.add(R.id.tab2, history, "记录");
                trans.commit();
                historyFlag=true;
            }
            contentViewID = R.id.tab2;
        }

        if (frag == null)
            return;

        FragmentManager manager = this.getSupportFragmentManager();

        if (manager.findFragmentByTag(tag) == null) {
            FragmentTransaction trans = manager.beginTransaction();
            trans.replace(contentViewID, frag, tag);
            trans.commit();
        }

    }

    private void setupTabs() {
        mTabHost = (TabHost) this.findViewById(R.id.tabhost);
        mTabHost.setup();
        // 生成底部自定义样式的按钮

        for (int i = 0; i < title.length; i++) {
            View vv = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);

            ImageView icon = (ImageView) vv.findViewById(R.id.tabIcon);
            icon.setImageResource(logos[i]);
            TextView op = (TextView) vv.findViewById(R.id.tabTitle);
            op.setText(title[i]);
            op.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tab_lable));
            //Button button = (Button)vv.findViewById(R.id.tabButton);
            //button.setText(title[i]);
            //button.setTextColor(Color.WHITE);
            //button.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.tab_lable));
            /*
            Button button = new Button(this);
            button.setText(title[i]);
            button.setTextColor(Color.WHITE);
            button.setBackgroundDrawable(this.getResources().getDrawable(
                    R.drawable.tab_lable));  //自定义按钮样式
                    */
            mTabHost.addTab(mTabHost.newTabSpec(title[i]).setIndicator(vv)
                    .setContent(tabIds[i]));

            //ImageView logo = new ImageView(this);
            //logo.setImageResource(logos[i]);
        }
        Fragment chart = new Chart();
        Bundle bundle=new Bundle();
        bundle.putString("Position",str);
        chart.setArguments(bundle);
        FragmentManager manager = this.getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.add(R.id.tab1, chart, "图表");
        trans.commit();
/*        getSupportFragmentManager().beginTransaction()
                .add(R.id.tab2, History.newInstance(str)).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tab3, Control.newInstance(str)).commit();*/
        mTabHost.setOnTabChangedListener(this);
    }

    private void updateIn(){
        /*
        NodeID+=id.toString();
        swtich(type)
        case X:SenorType+="";
        Connect+="Normal";
         */
        TextView v1 = (TextView)findViewById(R.id.NodeID);
        TextView v2 = (TextView)findViewById(R.id.senorType);
        TextView v3 = (TextView)findViewById(R.id.senorConnect);
        ImageView i1 = (ImageView)findViewById(R.id.senorBG);
        Bundle bundle=this.getIntent().getExtras();
        ArrayList<Parcelable> l = bundle.getParcelableArrayList("position");
        String[] s = new String[l.size()];
        l.toArray(s);
        String reg="[^0-9]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(s[0]);
        //System.out.println("NodeId:"+m.replaceAll("").trim());
        String id = m.replaceAll("").trim();
        if(Integer.parseInt(id)<10)
            NodeId+="0";
        NodeId+=id;
        if(s[1].equals("光照传感器")){
            SenorType="Illumination";
            i1.setBackground(getResources().getDrawable(R.drawable.top_sun));

        }

        else if (s[1].equals("温度传感器")){
            SenorType="Temperature";
            v2.setTextSize(15);
            i1.setBackground(getResources().getDrawable(R.drawable.top_temp));
        }
        else if (s[1].equals("湿度传感器")){
            SenorType="Humidity";
            i1.setBackground(getResources().getDrawable(R.drawable.top_humi));
        }

        else if (s[1].equals("水份传感器")){
            SenorType="WaterContent";
            v2.setTextSize(14);
            i1.setBackground(getResources().getDrawable(R.drawable.top_water));
        }

        Connect+="Normal";

        v1.setText(NodeId);
        v2.setText(SenorType);
        v3.setText(Connect);
    }

    //conn
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

    @Override
    public void CallBack(String DataStr) {
        mBoundService.send(DataStr);
    }
}
