package iotdf.iotgateway;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;

import iotdf.iotgateway.DeviceFragments.statusFrag;
import iotdf.iotgateway.RestComponents.MyBaseActivity;
import iotdf.iotgateway.data.DataService;
import iotdf.iotgateway.data.DataTest;

public class ChooseDevice extends MyBaseActivity implements View.OnClickListener {
    private AnimatedExpandableListView listView;
    private XRefreshView xRefreshView;
    public static long lastRefreshTime;
    private Button B_DataTest;
    private BoomMenuButton boomMenuButton;
    private boolean init=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        Bundle bundle1 = this.getIntent().getExtras();
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);
        B_DataTest=(Button)findViewById(R.id.Button_DataTest);
        B_DataTest.setOnClickListener(this);
        xRefreshView=(XRefreshView)findViewById(R.id.custom_view);
        xRefreshView.setAutoRefresh(false);
        xRefreshView.restoreLastRefreshTime(lastRefreshTime);
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
        final MyExpandableListAdapter adapter = new MyExpandableListAdapter(ChooseDevice.this);

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
                    Intent intent=new Intent(ChooseDevice.this,ControlPanel.class);
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
                            Intent intent0=new Intent(ChooseDevice.this,MainActivity.class);
                            startActivity(intent0);
                            finish();
                        }
                        else if(buttonIndex==1){
                            statusFrag statusFrag=new statusFrag();
                            statusFrag.show(getFragmentManager(),"status");
                        }
                    }
                });
    }

/*    private String[] Colors = {
            "#92aed3",
            "#5587c0",
            "#375069",
            "#3e5a98",
            "#3c3c3c",
            "#81a8b8",
            "#e8f3f8",
            "#c2cbce",
            "#dbe6ec"};
    public int GetRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }*/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Button_DataTest:Intent intent=new Intent(ChooseDevice.this, DataTest.class);
                startActivity(intent);
                break;
        }
    }


    public class MyExpandableListAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;
        public MyExpandableListAdapter(Context context){
            inflater=LayoutInflater.from(context);
        }
        int[] logos = new int[]{
                R.drawable.ico_temp,
                R.drawable.ico_humi,
                R.drawable.ico_water,
                R.drawable.ico_bright,
        };
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
