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
import lecho.lib.hellocharts.animation.ChartAnimationListener;
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
    private final int COLOR_WHITE = Color.parseColor("#FFFFFF");
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
        // Reset viewport height range to (0,100)
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
            line.setColor(COLOR_WHITE);
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
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    /**
     * Adds lines to data, after that data should be set again with
     * {@link LineChartView#setLineChartData(LineChartData)}. Last 4th line has non-monotonically x values.
     */
    private void addLineToData() {
        if (data.getLines().size() >= maxNumberOfLines) {
            Toast.makeText(getActivity(), "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            ++numberOfLines;
        }

        generateData();
    }

    private void toggleLines() {
        hasLines = !hasLines;

        generateData();
    }

    private void togglePoints() {
        hasPoints = !hasPoints;

        generateData();
    }

    private void toggleCubic() {
        isCubic = !isCubic;

        generateData();

        if (isCubic) {
            // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
            // go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
            // parameter or just set top and bottom values manually.
            // In this example I know that Y values are within (0,100) range so I set viewport height range manually
            // to (-5, 105).
            // To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
            // modifying viewport.
            // Remember to set viewport after you call setLineChartData().
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = -5;
            v.top = 105;
            // You have to set max and current viewports separately.
            chart.setMaximumViewport(v);
            // I changing current viewport with animation in this case.
            chart.setCurrentViewportWithAnimation(v);
        } else {
            // If not cubic restore viewport to (0,100) range.
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 100;

            // You have to set max and current viewports separately.
            // In this case, if I want animation I have to set current viewport first and use animation listener.
            // Max viewport will be set in onAnimationFinished method.
            chart.setViewportAnimationListener(new ChartAnimationListener() {

                @Override
                public void onAnimationStarted() {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationFinished() {
                    // Set max viewpirt and remove listener.
                    chart.setMaximumViewport(v);
                    chart.setViewportAnimationListener(null);

                }
            });
            // Set current viewpirt with animation;
            chart.setCurrentViewportWithAnimation(v);
        }

    }

    private void toggleFilled() {
        isFilled = !isFilled;

        generateData();
    }

    private void togglePointColor() {
        pointsHaveDifferentColor = !pointsHaveDifferentColor;

        generateData();
    }

    private void setCircles() {
        shape = ValueShape.CIRCLE;

        generateData();
    }

    private void setSquares() {
        shape = ValueShape.SQUARE;

        generateData();
    }

    private void setDiamonds() {
        shape = ValueShape.DIAMOND;

        generateData();
    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            chart.setValueSelectionEnabled(hasLabelForSelected);
        }

        generateData();
    }



    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;

        chart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
        }

        generateData();
    }

    private void toggleAxes() {
        hasAxes = !hasAxes;

        generateData();
    }

    private void toggleAxesNames() {
        hasAxesNames = !hasAxesNames;

        generateData();
    }

    /**
     * To animate values you have to change targets values and then call {@link lecho.lib.hellocharts.view.Chart#startDataAnimation()}
     * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
     * {@link LineChartView#setLineChartData(LineChartData)} again.
     */
    private void prepareDataAnimation() {
        for (Line line : data.getLines()) {
            for (PointValue value : line.getValues()) {
                // Here I modify target only for Y values but it is OK to modify X targets as well.
                value.setTarget(value.getX(), (float) Math.random() * 100);
            }
        }
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

