package com.slobx.slobodan.weathermob;

/**
 * Created by Slobodan on 7/26/2015.
 */
public class HourlyForecast {


    private String time;
    private String precipitation;
    private String temperature;

    private int weatherIconId;

    public HourlyForecast() {
    }


    public HourlyForecast(String time, String precipitation, String temperature, int weatherIconId) {
        this.time = time;
        this.precipitation = precipitation;
        this.temperature = temperature;

        this.weatherIconId = weatherIconId;

    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWeatherIconId() {
        return weatherIconId;
    }

    public void setWeatherIconId(int weatherIconId) {
        this.weatherIconId = weatherIconId;
    }


}
