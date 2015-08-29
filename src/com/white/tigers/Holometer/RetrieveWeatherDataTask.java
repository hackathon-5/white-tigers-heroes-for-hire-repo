package com.white.tigers.Holometer;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

/**
 * Created by jamesflesher on 8/29/15.
 */
public class RetrieveWeatherDataTask extends AsyncTask
{
    WeatherService weatherService;
    RetrieveWeatherDataCallback callback;

    public RetrieveWeatherDataTask(RetrieveWeatherDataCallback callback, Context context)
    {
        this.callback = callback;
        weatherService = new WeatherService(context);
    }

    @Override
    protected Object doInBackground(Object[] params)
    {
        return weatherService.getWeatherData((Location)params[0]);
    }

    @Override
    protected void onPostExecute(Object weatherData)
    {
        if(weatherData != null)
        {
            callback.onWeatherRetrieved(weatherService.parseWeatherData((String)weatherData));
        }
    }
}