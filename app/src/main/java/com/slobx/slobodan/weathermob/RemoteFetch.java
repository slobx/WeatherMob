package com.slobx.slobodan.weathermob;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoteFetch {

    private static final String OPEN_WEATHER_TODAY_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";
    private static final String OPEN_WEATHER_HOURLY_API =
            "http://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&cnt=15";
    private static final String OPEN_WEATHER_TWO_WEEKS_API =
            "http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&mode=json&units=metric&cnt=14";



    public static JSONObject getJSON(Context context, String city, int index){
        try {
            URL url;
            switch (index){
                case 1:
                    url = new URL(String.format(OPEN_WEATHER_TODAY_API, city));
                    break;
                case 2:
                    url = new URL(String.format(OPEN_WEATHER_HOURLY_API, city));
                    break;
                case 3:
                    url = new URL(String.format(OPEN_WEATHER_TWO_WEEKS_API, city));
                    break;
                default:
                    url = new URL(String.format(OPEN_WEATHER_TODAY_API, city));
            }

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.API_KEY));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
}
