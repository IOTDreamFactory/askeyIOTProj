package iotdf.iotgateway;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;

import iotdf.iotgateway.data.DataService;
import iotdf.iotgateway.data.DataTest;

public class ChooseDevice extends Activity implements View.OnClickListener {
    private AnimatedExpandableListView listView;
    private XRefreshView xRefreshView;
    private Resources resources;
    public static long lastRefreshTime;
    private Button B_DataTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        Bundle bundle1 = this.getIntent().getExtras();
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
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (listView.isGroupExpanded(groupPosition)) {
                    listView.collapseGroupWithAnimation(groupPosition);
                } else {
                    listView.expandGroupWithAnimation(groupPosition);
                }
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                ArrayList position=new ArrayList();
                position.add(adapter.getGroup(groupPosition));
                position.add(adapter.getChild(groupPosition,childPosition));
                bundle.putParcelableArrayList("position",position);
                intent.putExtras(bundle);
                intent.setClass(ChooseDevice.this,DevicePanel.class);
                startActivity(intent);
                return true;
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();
        onCreate(null);
    }

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
        private String[][] arms = new String[mDataService.PointNum().size()][4];
        private String[] armTypes=(String[])mDataService.PointNum().toArray(new String[mDataService.PointNum().size()]);
        public void getArms(){
            for(int i=0;i<mDataService.PointNum().size();i++)
            {
                arms[i][0]="温度传感器";
                arms[i][1]="湿度传感器";
                arms[i][2]="水份传感器";
                arms[i][3]="光照传感器";
            }
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

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
        public boolean isChildSelectable(int i, int i1) {
            return true;
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
            ImageView logo = (ImageView)convertView.findViewById(R.id.node_icon);
            logo.setImageResource(logos[childPosition]);
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return 4;
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
