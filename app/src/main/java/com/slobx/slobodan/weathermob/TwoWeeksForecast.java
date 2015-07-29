package com.slobx.slobodan.weathermob;

public class TwoWeeksForecast {

    private String time;
    private String minTemperature;
    private String maxTemperature;
    private String pressure;

    private int weatherIconId;

    public TwoWeeksForecast() {
    }


    public TwoWeeksForecast(String time, String minTemperature, String maxTemperature, String pressure, int weatherIconId) {
        this.time = time;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.pressure = pressure;
        this.weatherIconId = weatherIconId;

    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public int getWeatherIconId() {
        return weatherIconId;
    }

    public void setWeatherIconId(int weatherIconId) {
        this.weatherIconId = weatherIconId;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

}
