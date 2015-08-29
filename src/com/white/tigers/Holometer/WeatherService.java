package com.white.tigers.Holometer;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
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
public class WeatherService {
    private static final String SERVICE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    private Context context;

    public WeatherService(Context context) {
        this.context = context;
    }

    public String getWeatherData(Location location) {
        String cityAndCountry = this.getCityAndCountry(location);

        String weatherData = null;
        HttpURLConnection connection = null;
        InputStream is = null;

        try {
            connection = (HttpURLConnection) (new URL(String.format(SERVICE_URL, cityAndCountry))).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            StringBuffer buffer = new StringBuffer();
            is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            weatherData = buffer.toString();
        } catch (Exception ex) {
            Log.i("test", ex.getMessage());
            /*
             * DO NOTHING
             */
        } finally {
            try {
                is.close();
                connection.disconnect();
            } catch (Throwable t) {
                Log.i("test", t.getMessage());
                /*
                 * DO NOTHING
                 */
            }
        }

        return weatherData;
    }

    private JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(tagName);
    }

    private float getFloat(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    private String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    private int getInt(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(tagName);
    }

    private String getCityAndCountry(Location location) {
        String cityAndCountry = "";

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList.size() > 0) {
                cityAndCountry = String.format("%s,%s", addressList.get(0).getLocality(), addressList.get(0).getCountryCode());
            }
        } catch (Exception ex) {
            /*
             * DO NOTHING
             */
        }

        return cityAndCountry;
    }

    public Weather parseWeatherData(String weatherData)
    {
        Weather weather = new Weather();
        try
        {
            JSONObject jsonWeatherObject = new JSONObject((String) weatherData);

//            JSONArray weatherObjs = (JSONArray)getObject("weather", jsonWeatherObject);
//
//            if(weatherObjs.length() > 0)
//            {
//                weather.setDescription(getString("description", (JSONObject)weatherObjs.get(0)));
//            }

            JSONObject mainObj = getObject("main", jsonWeatherObject);

            weather.setTemp(getFloat("temp", mainObj));
            weather.setHumidity(getFloat("humidity", mainObj));
            weather.setPressure(getFloat("pressure", mainObj));
            weather.setMinTemp(getFloat("temp_min", mainObj));
            weather.setMaxTemp(getFloat("temp_max", mainObj));
        }
        catch (Exception ex)
        {
            Log.i("tag", ex.getMessage());
        }

        return weather;
    }
}