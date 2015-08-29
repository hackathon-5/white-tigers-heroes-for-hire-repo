package com.white.tigers.Holometer;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by jamesflesher on 8/29/15.
 */
public class WeatherService
{
    private Context context;
    private static final String SERVICE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s";
    private static String IMG_URL = "http://openweathermap.org/img/w/";

    public WeatherService(Context context)
    {
        this.context = context;
    }

    public Weather getWeather(Location location)
    {
        Weather weather = new Weather();

        String weatherData = getWeatherData(location);
        if(weatherData != null)
        {
            try
            {
                JSONObject jsonWeatherObject = new JSONObject(weatherData);

                JSONObject weatherObj = getObject("weather", jsonWeatherObject);
                weather.setDescription(getString("description", weatherObj));

                JSONObject mainObj = getObject("main", jsonWeatherObject);

                weather.setTemp(getFloat("temp", mainObj));
                weather.setHumidity(getFloat("humidity", mainObj));
                weather.setPressure(getFloat("pressure", mainObj));
                weather.setMinTemp(getFloat("temp_min", mainObj));
                weather.setMaxTemp(getFloat("temp_max", mainObj));

            }
            catch(JSONException je)
            {

            }

        }

        return weather;
    }

    private JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException
    {
        return jsonObject.getJSONObject(tagName);
    }

    private float getFloat(String tagName, JSONObject jsonObject) throws JSONException
    {
        return (float)jsonObject.getDouble(tagName);
    }

    private String getString(String tagName, JSONObject jsonObject) throws JSONException
    {
        return jsonObject.getString(tagName);
    }

    private int getInt(String tagName, JSONObject jsonObject) throws JSONException
    {
        return jsonObject.getInt(tagName);
    }

    private String getWeatherData(Location location)
    {
        String weatherData = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        try
        {
            String cityAndCountry = this.getCityAndCountry(location);
            connection = (HttpURLConnection)(new URL(String.format(SERVICE_URL, cityAndCountry))).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            StringBuffer buffer = new StringBuffer();
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while((line = reader.readLine()) != null)
            {
                buffer.append(line + "\r\n");
            }

            weatherData =  buffer.toString();
        }
        catch(Exception ex)
        {
            /*
             * DO NOTHING
             */
        }
        finally
        {
            try
            {
                is.close();
                connection.disconnect();
            }
            catch(Throwable t)
            {
                /*
                 * DO NOTHING
                 */
            }
        }

        return weatherData;
    }
    private String getCityAndCountry(Location location)
    {
        String cityAndCountry = "";

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList.size() > 0) {
                cityAndCountry = addressList.get(0).getLocality() + addressList.get(0).getCountryCode();
            }
        }
        catch(Exception ex)
        {
            /*
             * DO NOTHING
             */
        }

        return cityAndCountry;
    }

//    public byte[] getImage(String code)
//    {
//
//    }
}
