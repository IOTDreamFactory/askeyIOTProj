package iotdf.iotgateway;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class ChooseDevice extends Activity{
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_device);
        expandableListView=(ExpandableListView)findViewById(R.id.expanded_menu);
    }

}