package iotdf.iotgateway.DeviceFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import iotdf.iotgateway.R;
import iotdf.iotgateway.data.DataService;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


public class Chart extends Fragment {

    public static Chart newInstance(){
        Chart chart=new Chart();
        return chart;
    }

    private LineChartView chart;
    private LineChartData data;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 1;
    private int numberOfPoints = 0;

    float[][] randomNumbersTab ;

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = false;
    private boolean pointsHaveDifferentColor;

    private final static String TAG="Test";

    private String postion;
    private String[] postionInfo;
    private String arduinoNum;
    private String sensor;
    private TextView tv_Position;
    private float[] NumbersTab;
    private final int COLOR_WHITE = Color.parseColor("#000000");
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        if(rootView==null)
            rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            tv_Position=(TextView) rootView.findViewById(R.id.tv_position) ;

            postion=bundle.getString("Position");
            checkPosition(postion);
            tv_Position.setText(arduinoNum);
        }
        // Generate some random values.
        getPointNum();
        initChartValue();
        generateValues();
        generateData();
        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);
        resetViewport();
        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ((ViewGroup)rootView.getParent()).removeView(rootView);
    }
    // MENU
    private void getPointNum(){
        DataService mDataService=new DataService(getActivity());
        numberOfPoints=mDataService.pointNum(sensor,arduinoNum);
        randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
        NumbersTab=new float[numberOfPoints];
    }

    private void generateValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j]=NumbersTab[j];
            }
        }
    }

    private void checkPosition(String postion){
        if(postion.contains("温度"))
            sensor = "temp";
        else if(postion.contains("水份"))
            sensor="water";
        else if(postion.contains("光照"))
            sensor="brightness";
        else if (postion.contains("湿度"))
            sensor="humidity";
        else
            sensor=null;
        postionInfo=postion.split(",");
        arduinoNum=postionInfo[1];
    }


    private void initChartValue(){
        DataService mDataService=new DataService(getActivity());
        Log.d(TAG, "onCreateView: "+numberOfPoints);
        String[] NumbersTabStr=(String[])mDataService.initNumberTabs(sensor,arduinoNum).toArray(new String[numberOfPoints]);
        System.out.println("NumberofPoints:"+numberOfPoints);
        for (int i=0;i<numberOfPoints;i++)
        {NumbersTab[i]=Float.parseFloat(NumbersTabStr[i]);
            System.out.println("NumbersTab:"+NumbersTab[i]);
        }
    }

    private void reset() {
        numberOfLines = 1;

        hasAxes = true;
        hasAxesNames = true;
        hasLines = true;
        hasPoints = true;
        shape = ValueShape.CIRCLE;
        isFilled = false;
        hasLabels = false;
        isCubic = false;
        hasLabelForSelected = false;
        pointsHaveDifferentColor = true;

        chart.setValueSelectionEnabled(hasLabelForSelected);
        resetViewport();
    }

    private void resetViewport() {
        final Viewport v = new Viewport(chart.getMaximumViewport());
        v.bottom = 0;
        v.top=100;
        v.left = 0;
        v.right = numberOfPoints - 1;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateData() {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < numberOfLines; ++i) {

            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            Line line = new Line(values);
            //line.setColor(ChartUtils.COLORS[i]);
            line.setColor(getResources().getColor(R.color.color81a8b8));
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            line.setPointColor(ChartUtils.COLORS[i]);
            if (pointsHaveDifferentColor){
                //line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                line.setPointColor(COLOR_WHITE);
            }
            lines.add(line);
        }

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                /*axisX.setName("Axis X");
                axisY.setName("Axis Y");*/
            }
            axisX.setLineColor(getResources().getColor(R.color.color81a8b8));
            axisY.setLineColor(getResources().getColor(R.color.color81a8b8));
            axisY.setTextColor(getResources().getColor(R.color.color81a8b8));
            axisX.setTextColor(getResources().getColor(R.color.color81a8b8));
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }


}

