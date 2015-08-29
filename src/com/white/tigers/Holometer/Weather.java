package com.white.tigers.Holometer;

/**
 * Created by jamesflesher on 8/29/15.
 */
public class Weather
{
    private String condition;
    private float temp;
    private float humidity;
    private float pressure;
    private float minTemp;
    private float maxTemp;
    private byte[] image;

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public float getTemp() { return temp; }
    public void setTemp(float temp) { this.temp = temp; }

    public float getHumidity() { return humidity; }
    public void setHumidity(float humidity) { this.humidity = humidity; }

    public float getPressure() { return pressure; }
    public void setPressure(float pressure) { this.pressure = pressure; }

    public float getMinTemp() { return minTemp; }
    public void setMinTemp(float minTemp) { this.minTemp = minTemp; }

    public float getMaxTemp() { return maxTemp; }
    public void setMaxTemp(float maxTemp) { this.maxTemp = maxTemp; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public Weather()
    {}
}