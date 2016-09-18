package iotdf.iotgateway;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iotdf.iotgateway.ConSens.LocalService;
import iotdf.iotgateway.ConServ.ServerRequest;
import iotdf.iotgateway.DeviceFragments.statusFrag;
import iotdf.iotgateway.RestComponents.MyBaseActivity;
import iotdf.iotgateway.RestComponents.mathhelper;
import iotdf.iotgateway.data.DataService;
import iotdf.iotgateway.data.DataTest;

public class ChooseDevice extends MyBaseActivity implements View.OnClickListener {
    private AnimatedExpandableListView listView;
    private XRefreshView xRefreshView;
    public static long lastRefreshTime;
    private Button B_DataTest;
    private BoomMenuButton boomMenuButton;
    private boolean init=false;
    private LocalService mBoundService;
    private String username;
    private String[] arduinoNum={"000","001","010","011","100","101","110","111"};
    private String theUrl="http://218.199.150.207";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        setContentView(R.layout.activity_choose_device);
        Intent intent=new Intent(ChooseDevice.this,LocalService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
        Bundle bundle1 = this.getIntent().getExtras();
        username=bundle1.getString("Username");
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);
        B_DataTest=(Button)findViewById(R.id.Button_DataTest);
        B_DataTest.setOnClickListener(this);
        xRefreshView=(XRefreshView)findViewById(R.id.custom_view);
        xRefreshView.setAutoRefresh(true);
        xRefreshView.restoreLastRefreshTime(lastRefreshTime);
        final MyExpandableListAdapter adapter = new MyExpandableListAdapter(ChooseDevice.this);
        xRefreshView.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xRefreshView.stopRefresh();
                        lastRefreshTime = xRefreshView.getLastRefreshTime();
                    }
                }, 1000);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
//                Intent intent=new Intent(ChooseDevice.this,ChooseDevice.class);
//                startActivity(intent);
            }

            @Override
            public void onLoadMore(boolean isSilence) {

            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double offset, int offsetY) {

            }
        });

        listView = (AnimatedExpandableListView) findViewById(R.id.expandlist);


        listView.setAdapter(adapter);
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(childPosition==4)
                {
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    ArrayList position=new ArrayList();
                    position.add(adapter.getGroup(groupPosition));
                    position.add(adapter.getChild(groupPosition,childPosition));
                    bundle.putParcelableArrayList("position",position);
                    intent.putExtras(bundle);
                    intent.setClass(ChooseDevice.this,ControlPanel.class);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent();
                    Bundle bundle=new Bundle();
                    ArrayList position=new ArrayList();
                    position.add(adapter.getGroup(groupPosition));
                    position.add(adapter.getChild(groupPosition,childPosition));
                    bundle.putParcelableArrayList("position",position);
                    intent.putExtras(bundle);
                    intent.setClass(ChooseDevice.this,DevicePanel.class);
                    startActivity(intent);
                }
                return true;
            }
        });


    }
    private void byeMqtt(){
        String  url="http://218.199.150.207:8080/exit";
        List<NameValuePair> params;
        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("requesttype", "exit"));
        ServerRequest sr = new ServerRequest();
        JSONObject json = sr.getJSON(url,params);
//        HttpPost httpPost = new HttpPost(url);
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("request-type", "exit"));
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        byeMqtt();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        int[][] subButtonColors = new int[3][2];
        Drawable[] drawables={ContextCompat.getDrawable(this, R.drawable.logout),ContextCompat.getDrawable(this, R.drawable.viewfixed),ContextCompat.getDrawable(this, R.drawable.download)};
        for (int i = 0; i < 3; i++) {
            subButtonColors[i][1] = ContextCompat.getColor(this, R.color.color81a8b8);
            subButtonColors[i][0] = Util.getInstance().getPressedColor(subButtonColors[i][1]);
        }

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .dim(DimType.DIM_9)
                .subButtonTextColor(ContextCompat.getColor(this, R.color.color81a8b8))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .addSubButton(drawables[0], subButtonColors[0], "注销账号")
                .addSubButton(drawables[1], subButtonColors[0], "查看状态")
                .addSubButton(drawables[2], subButtonColors[0], "获取数据")
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA)
                .place(PlaceType.HAM_3_1)
                .init(boomMenuButton)
                .setOnSubButtonClickListener(new BoomMenuButton.OnSubButtonClickListener(){
                    @Override
                    public void onClick(int buttonIndex){
                        if(buttonIndex==0){
                            byeMqtt();
                            Intent intent0=new Intent(ChooseDevice.this,MainActivity.class);
                            startActivity(intent0);
                            finish();
                        }
                        else if(buttonIndex==1){
                            statusFrag statusFrag=new statusFrag();
                            statusFrag.show(getFragmentManager(),"status");
                        }else if(buttonIndex==2){
                            for(int i=0;i<8;i++){
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mBoundService.send(mathhelper.str2HexStr("101010"+arduinoNum[i]+"00000010000000000011011"));
                            }
                        }
                    }
                });
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((LocalService.LocalBinder)service).getService();
        }
        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Button_DataTest:
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("Username",username);
                intent.putExtras(bundle);
                intent.setClass(ChooseDevice.this, DataTest.class);
                startActivity(intent);
                break;
        }
    }


    public class MyExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;
        public MyExpandableListAdapter(Context context){
            inflater=LayoutInflater.from(context);
        }
        DataService mDataService=new DataService(ChooseDevice.this);
        private String[][] arms = new String[mDataService.PointNum().size()][5];
        private String[] armTypes=(String[])mDataService.PointNum().toArray(new String[mDataService.PointNum().size()]);
        public void getArms(){
            for(int i=0;i<mDataService.PointNum().size();i++)
            {
                arms[i][0]="温度传感器";
                arms[i][1]="湿度传感器";
                arms[i][2]="水份传感器";
                arms[i][3]="光照传感器";
                arms[i][4]="控制该节点";
            }
        }

        @Override
        public int getGroupCount() {
            return armTypes.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return arms[groupPosition].length;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return armTypes[groupPosition];
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return arms[groupPosition][childPosition];
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            getArms();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ChooseDevice.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_parent, parent,false);
            }
            TextView textView = getTextView();
            textView=(TextView)convertView.findViewById(R.id.parent_textview_ein);
            textView.setText(getGroup(groupPosition).toString());
            ImageView parentIndicator=(ImageView)convertView.findViewById(R.id.parent_imageview);
            if(!isExpanded)
                parentIndicator.setImageResource(R.drawable.parentindicator_close);
            else
                parentIndicator.setImageResource(R.drawable.parentindicator_open);
            return convertView;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ChooseDevice.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_children, parent,false);
            }
            TextView textView = (TextView)convertView.findViewById(R.id.second_textview) ;
            textView.setText(getChild(groupPosition, childPosition).toString());
/*            ImageView logo = (ImageView)convertView.findViewById(R.id.node_icon);
            logo.setImageResource(logos[childPosition]);*/
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return  arms[groupPosition].length;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return 0;
        }

        private TextView getTextView() {
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 64);
            TextView textView = new TextView(ChooseDevice.this);
            textView.setLayoutParams(lp);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            textView.setPadding(100, 0, 0, 0);
            return textView;
        }



    }
}
