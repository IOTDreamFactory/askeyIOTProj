package iotdf.iotgateway;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.ThemeManager;
import com.rey.material.app.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import iotdf.iotgateway.DeviceFragments.Chart;
import iotdf.iotgateway.DeviceFragments.History;

public class DevicePanel extends ActionBarActivity {
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.tabs) PagerSlidingTabStrip tabs;
    @InjectView(R.id.pager) ViewPager pager;
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
    @OnClick({R.id.calendar,R.id.time,R.id.back})
    public void onClick(View view)
    {
        Dialog.Builder builder = null;
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        switch (view.getId()){
            case R.id.calendar:

                builder = new DatePickerDialog.Builder(R.style.mDatePicker){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                        String date = dialog.getFormattedDate(SimpleDateFormat.getDateInstance());
                        Toast.makeText(DevicePanel.this, "Date is " + date, Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(DevicePanel.this, "Cancelled" , Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");

                break;
            case R.id.back:
                Intent intent=new Intent(DevicePanel.this,ChooseDevice.class);
                startActivity(intent);
                break;
            case R.id.time:
                builder = new TimePickerDialog.Builder( R.style.mTimePicker_Light,24,00){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        TimePickerDialog dialog = (TimePickerDialog)fragment.getDialog();
                        Toast.makeText(DevicePanel.this, "Time is " + dialog.getFormattedTime(SimpleDateFormat.getTimeInstance()), Toast.LENGTH_SHORT).show();
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(DevicePanel.this, "Cancelled" , Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");
                break;
        }
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
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

