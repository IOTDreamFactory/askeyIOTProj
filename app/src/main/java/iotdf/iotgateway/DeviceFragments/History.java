package iotdf.iotgateway.DeviceFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iotdf.iotgateway.R;
import iotdf.iotgateway.data.DataService;

public class History extends  Fragment implements View.OnClickListener {

    ListView nlistView;
    View view;
    View olderSelectView = null;
    String postion;
    String arduinoNum;
    String sensor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle=getArguments();
        postion=bundle.getString("Position");
        System.out.println(postion);
        String[] postionInfo=postion.split(",");
        arduinoNum=postionInfo[1];//传感节点编号
        /*获取传感器号*/
        if(postion.contains("温度"))
            sensor = "temp";
        else if(postion.contains("水份"))
            sensor="water";
        else if(postion.contains("光照"))
            sensor="brightness";
        else if (postion.contains("湿度"))
            sensor="humidity";
        if(view==null)
            view = inflater.inflate(R.layout.fragment_history, null);

        nlistView = (ListView)view.findViewById(R.id.historyList);
        SimpleAdapter adapter = new SimpleAdapter(view.getContext(),getData(),R.layout.history_list,
                new String[]{"time","status","info"},
                new int[]{R.id.time,R.id.status,R.id.info});
        nlistView.setAdapter(adapter);


        updateList();
        adapter.notifyDataSetChanged();
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((ViewGroup)view.getParent()).removeView(view);
    }

    public static Fragment newInstance(String arg){
        History fragment = new History();
        Bundle bundle = new Bundle();
        bundle.putString("Positon", arg);
        fragment.setArguments(bundle);
        return fragment;
    }
    private void updateList(){
        int First = nlistView.getFirstVisiblePosition();
        int Last = nlistView.getLastVisiblePosition();
        for(int i=First;i<=Last;i++){
            View testView = nlistView.getChildAt(i-First);
            nlistView.getAdapter().getView(i,testView,nlistView);
            TextView listStatus = (TextView)testView.findViewById(R.id.status);
            TextView listInfo = (TextView)testView.findViewById(R.id.info);
            if(listStatus.toString().equals("过高")){
                listStatus.setTextColor(Color.RED);
                listInfo.setTextColor(Color.RED);
            }
            else if(listStatus.toString().equals("过低")){
                listStatus.setTextColor(Color.GREEN);
                listInfo.setTextColor(Color.GREEN);
            }
        }

    }
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        DataService mDataService=new DataService(getActivity());
        System.out.println(sensor+arduinoNum);
        ArrayList<Map> HistoryList=mDataService.HistoryData(sensor,arduinoNum);
        for ( Map<String, Object> Datamap : HistoryList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("time", TimeUtils.times((String)Datamap.get("time")));
            map.put("status", "正常");
            if(postion.contains("光照"))
                map.put("info",Datamap.get(sensor)+"LX");
            else if(postion.contains("水份")||postion.contains("湿度"))
                map.put("info",Datamap.get(sensor)+"%");
            else if(postion.contains("温度"))
                map.put("info",Datamap.get(sensor)+"℃");
            System.out.println(Datamap.get("time")+" "+Datamap.get(sensor));
            list.add(map);
        }

        return list;
    }

    @Override
    public void onClick(View view) {

    }
}

class TimeUtils {
    /**
     * 时间戳转为年月日，时分秒
     * @return
     */
    public static String times(String string) {
        Long dateTaken=Long.valueOf(string);
        return android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", dateTaken).toString();
    }
}
