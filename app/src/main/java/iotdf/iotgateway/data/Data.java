package iotdf.iotgateway.data;

/**
 * Created by Administrator on 2016/7/3 0003.
 */
public class Data {
    private String arduinoNum;
    private String humidity;
    private String water;
    private String brightness;
    private String temp;
    private long time;

    public String getId() {
        return arduinoNum;
    }

    public void setId(String arduinoNum) {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
