package iotdf.iotgateway;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import iotdf.iotgateway.DeviceFragments.Chart;
import iotdf.iotgateway.DeviceFragments.History;

public class DevicePanel extends ActionBarActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @InjectView(R.id.pager)
    ViewPager pager;
    private String str=new String();
    private MyPagerAdapter myPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_panel);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);
        tabs.setUnderlineHeight(-1);
        tabs.setViewPager(pager);
        tabs.setBackgroundColor(getResources().getColor(R.color.color81a8b8));
        tabs.setTextColor(getResources().getColor(R.color.colore8f3f8));
        tabs.setIndicatorColor(getResources().getColor(R.color.colore8f3f8));
        tabs.setShouldExpand(true);
        pager.setCurrentItem(1);
        Bundle bundle = this.getIntent().getExtras();
        ArrayList position = bundle.getParcelableArrayList("position");

        for (Object Pos:position
                ) {
            str=str+","+Pos;
        }
    }
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"折线图", "历史数据"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            if(0==position){
                Fragment chart = new Chart();
                Bundle bundle=new Bundle();
                bundle.putString("Position",str);
                chart.setArguments(bundle);
                return chart;
            }
            else if(1==position){
                Fragment history = new History();
                Bundle bundle = new Bundle();
                bundle.putString("Position", str);
                history.setArguments(bundle);
                return history;
            }
            else
                return null;
        }
    }
}

