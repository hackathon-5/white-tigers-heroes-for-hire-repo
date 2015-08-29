package com.white.tigers.Holometer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Holometer extends Activity implements RetrieveWeatherDataCallback, RetrieveSpeedLimitDataCallback {
    /**
     * Called when the activity is first created.
     */

    private WeatherService weatherService;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Weather currentWeather;
    private List<Integer> speedLimits = new ArrayList<>();
    private List<Integer> weatherIcons = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Set speed limits
        speedLimits.add(R.drawable.fast25);
        speedLimits.add(R.drawable.fast65);
        speedLimits.add(R.drawable.fast80);

        // Set weather icon list.
        weatherIcons.add(R.drawable.w1);
        weatherIcons.add(R.drawable.w2);
        weatherIcons.add(R.drawable.w3);
        weatherIcons.add(R.drawable.w4);
        weatherIcons.add(R.drawable.w5);
        weatherIcons.add(R.drawable.w6);
        weatherIcons.add(R.drawable.w7);
        weatherIcons.add(R.drawable.w8);

        weatherService = new WeatherService(getApplicationContext());

        // Set Weather
        setWeather(R.drawable.w1);

        setSpeedLimit(R.drawable.fast65);

        // Demo speed limit.
        new DemoSpeedLimitDataTask(this).execute(1);

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
     *
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


    private void updateLocation(Location location) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new RetrieveWeatherDataTask(this, getApplicationContext()).execute(location);

            TextView headingFront = (TextView) findViewById(R.id.headingFront);
            TextView headingBack = (TextView) findViewById(R.id.headingBack);

            headingFront.setText(getDirection(location.getBearing()));
            headingBack.setText(getDirection(location.getBearing()));
        } else {
            Log.i("tag", "NOT CONNECTED");
        }
    }

    @Override
    public void onWeatherRetrieved(Weather weather) {
        currentWeather = weather;
        TextView tempFront = (TextView) findViewById(R.id.tempFront);
        TextView tempBack = (TextView) findViewById(R.id.tempBack);

        tempFront.setText(String.valueOf(convertKelvinToFarenheit(currentWeather.getTemp())));
        tempBack.setText(String.valueOf(convertKelvinToFarenheit(currentWeather.getTemp())));

        // Update weather icon.
        ImageView topWeather = (ImageView) findViewById(R.id.topWeather);
        ImageView bottomWeather = (ImageView) findViewById(R.id.bottomWeather);

        topWeather.setImageBitmap(decodeResource(getResources(), weatherIcons.get(String.valueOf(weather.getCondition()))));
        bottomWeather.setImageBitmap(decodeResource(getResources(), speedLimits.get(String.valueOf(weather.getCondition())));

    }

    @Override
    public void onSpeedRetrieved() {
        ImageView topSpeed = (ImageView) findViewById(R.id.topSpeedLimit);
        ImageView bottomSpeed = (ImageView) findViewById(R.id.bottomSpeedLimit);

        // Randomize speed limit
        Random random;
        try {
            random = new Random();
            int randomSpeedSign = random.nextInt(speedLimits.size());

            topSpeed.setImageBitmap(decodeResource(getResources(), speedLimits.get(randomSpeedSign)));
            bottomSpeed.setImageBitmap(decodeResource(getResources(), speedLimits.get(randomSpeedSign)));

        } catch (Throwable e) {
            e.printStackTrace();
        }
        new DemoSpeedLimitDataTask(this).execute(1);
    }

    private static Bitmap decodeResource(Resources res, int id) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeResource(res, id, options);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
                // If an OutOfMemoryError occurred, we continue with for loop and next inSampleSize value

            }
        }
        return bitmap;
    }

    private int convertKelvinToFarenheit(float kelvin) {
        return Math.round((((kelvin - 273.15f) * 1.8f) + 32));
    }

    private String getDirection(float bearing) {
        String direction = "N";

        if (bearing >= 0 && bearing > 22.5) direction = "N";
        else if (bearing >= 22.5 && bearing > 67.5) direction = "NE";
        else if (bearing >= 67.5 && bearing > 112.5) direction = "E";
        else if (bearing >= 112.5 && bearing > 157.5) direction = "SE";
        else if (bearing >= 157.5 && bearing > 202.5) direction = "S";
        else if (bearing >= 202.5 && bearing > 247.5) direction = "SW";
        else if (bearing >= 247.5 && bearing > 292.5) direction = "W";
        else if (bearing >= 292.5 && bearing > 337.5) direction = "NW";
        else direction = "N";

        return direction;
    }
}