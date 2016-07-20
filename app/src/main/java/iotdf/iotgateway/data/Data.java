package iotdf.iotgateway.data;

/**
 * Created by Administrator on 2016/7/3 0003.
 */
public class Data {
    private int arduinoNum;
    private String humidity;
    private String water;
    private String brightness;
    private String temp;
    private String time;

    public int getId() {
        return arduinoNum;
    }

    public void setId(int arduinoNum) {
        this.arduinoNum = arduinoNum;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getBrightness() {
        return brightness;
    }

    public void setBrightness(String brightness) {
        this.brightness = brightness;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
