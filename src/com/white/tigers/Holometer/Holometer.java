package com.white.tigers.Holometer;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Holometer extends Activity implements RetrieveWeatherDataCallback {
    /**
     * Called when the activity is first created.
     */

    WeatherService weatherService;
    LocationManager locationManager;
    LocationListener locationListener;
    Weather currentWeather;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        weatherService = new WeatherService(getApplicationContext());

        // Set Weather
        setWeather(R.drawable.rain_white);

        setSpeedLimit(R.drawable.fast65);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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


    private void updateLocation(Location location)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
            new RetrieveWeatherDataTask(this, getApplicationContext()).execute(location);
        }
        else
        {
            Log.i("tag", "NOT CONNECTED");
        }
    }

    @Override
    public void onWeatherRetrieved(Weather weather) {
        currentWeather = weather;
        TextView tempFront = (TextView)findViewById(R.id.tempFront);
        TextView tempBack = (TextView)findViewById(R.id.tempBack);

        tempFront.setText(String.valueOf(convertKelvinToFarenheit(currentWeather.getTemp())));
        tempBack.setText(String.valueOf(convertKelvinToFarenheit(currentWeather.getTemp())));
    }

    private int convertKelvinToFarenheit(float kelvin)
    {
        return Math.round((((kelvin - 273.15f) * 1.8f) + 32));
    }
}