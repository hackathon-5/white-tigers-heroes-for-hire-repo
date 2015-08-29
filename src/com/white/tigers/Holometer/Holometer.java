package com.white.tigers.Holometer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class Holometer extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set Weather
        setWeather(R.drawable.rain_white);

        setSpeedLimit(R.drawable.fast65);

    }


    /**
     * Set speed limit.
     * @param speedLimit
     */
    private void setSpeedLimit(int speedLimit) {
        ImageView topSpeed = (ImageView) findViewById(R.id.topSpeedLimit);
        ImageView bottomSpeed = (ImageView) findViewById(R.id.bottomSpeedLimit);

        topSpeed.setImageResource(speedLimit);
        bottomSpeed.setImageResource(speedLimit);
    }


    /**
     * Set Weather
     *
     * @param weather
     */
    private void setWeather(int weather) {
        ImageView topWeather = (ImageView) findViewById(R.id.topWeather);
        ImageView bottomWeather = (ImageView) findViewById(R.id.bottomWeather);

        topWeather.setImageResource(weather);
        bottomWeather.setImageResource(weather);
    }
}
