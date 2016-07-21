package iotdf.iotgateway;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import iotdf.iotgateway.DeviceFragments.Chart;
import iotdf.iotgateway.DeviceFragments.Control;
import iotdf.iotgateway.DeviceFragments.History;

public class DevicePanel extends FragmentActivity implements TabHost.OnTabChangeListener {

    private TabHost mTabHost;

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

        setupTabs();
    }

    @Override
    public void onTabChanged(String tag) {
        Fragment frag = null;
        int contentViewID = 0;
        if (tag.equalsIgnoreCase("图表")) {
            frag = new Chart(); //自定义继承Fragment的UI，放了一个简单的显示文本标题的控件。
            contentViewID = R.id.tab1;
        }  else if (tag.equalsIgnoreCase("操作")){
            frag =new Control();
            contentViewID=R.id.tab3;
        }else if (tag.equalsIgnoreCase("记录")) {
            frag = new History();
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
        String[] title = new String[] { "图表", "记录", "操作" };
        int[] tabIds = new int[] { R.id.tab1, R.id.tab2, R.id.tab3 };
        for (int i = 0; i < title.length; i++) {
            Button button = new Button(this);
            button.setText(title[i]);
            button.setBackgroundDrawable(this.getResources().getDrawable(
                    R.drawable.tab_lable));  //自定义按钮样式
            mTabHost.addTab(mTabHost.newTabSpec(title[i]).setIndicator(button)
                    .setContent(tabIds[i]));
        }

        mTabHost.setOnTabChangedListener(this);
    }
}
