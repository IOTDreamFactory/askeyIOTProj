package iotdf.iotgateway.data;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import iotdf.iotgateway.ChooseDevice;
import iotdf.iotgateway.R;

public class DataTest extends AppCompatActivity implements View.OnClickListener {
    private EditText ET_arduinoNum;
    private EditText ET_time;
    private EditText ET_temp;
    private EditText ET_humidity;
    private EditText ET_water;
    private EditText ET_brightness;
    private Button Button_accept;
    private Button Button_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_test);
        ET_arduinoNum=(EditText)findViewById(R.id.ET_arduinoNum);
        ET_time=(EditText)findViewById(R.id.ET_Time);
        ET_temp=(EditText)findViewById(R.id.ET_Temperature);
        ET_humidity=(EditText)findViewById(R.id.ET_Humidity);
        ET_water=(EditText)findViewById(R.id.ET_Water);
        ET_brightness=(EditText)findViewById(R.id.ET_Brightness);
        Button_accept=(Button) findViewById(R.id.button_accept);
        Button_back=(Button)findViewById(R.id.button_back);
        ET_arduinoNum.setOnClickListener(this);
        ET_time.setOnClickListener(this);
        ET_temp.setOnClickListener(this);
        ET_humidity.setOnClickListener(this);
        ET_water.setOnClickListener(this);
        ET_brightness.setOnClickListener(this);
        Button_accept.setOnClickListener(this);
        Button_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ET_arduinoNum:ET_arduinoNum.setHint(null);break;
            case R.id.ET_Brightness:ET_brightness.setHint(null);break;
            case R.id.ET_Temperature:ET_temp.setHint(null);break;
            case R.id.ET_Time:ET_time.setHint(null);break;
            case R.id.ET_Water:ET_water.setHint(null);break;
            case R.id.ET_Humidity:ET_humidity.setHint(null);break;
            case R.id.button_accept:
                DataService mDataService=new DataService(DataTest.this);
                Data data=new Data();
                data.setBrightness(ET_brightness.getText().toString());
                data.setHumidity(ET_humidity.getText().toString());
                data.setId(Integer.parseInt(ET_arduinoNum.getText().toString()));
                data.setTemp(ET_time.getText().toString());
                data.setTime(ET_time.getText().toString());
                data.setWater(ET_water.getText().toString());
                mDataService.InsertData(data);
                Toast.makeText(DataTest.this,"输入数据成功",Toast.LENGTH_SHORT).show();
            case R.id.button_back:
                Intent intent=new Intent(DataTest.this, ChooseDevice.class);
                startActivity(intent);
                break;
        }
    }
}
