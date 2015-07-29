package com.slobx.slobodan.weathermob;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Tab3 extends Fragment {
    Typeface weatherFont;

    TextView timeField;
    TextView precipitationField;
    TextView temperatureField;
    TextView weatherIcon;
    ListView listView;
    MyAdapter adapter;
    View rootView;
    SharedPreferences pref;

    private static final String PREFS_KEY = "prefs";

    ArrayList<HourlyForecast> hourlyForecastArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab3, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pref = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        String city = pref.getString("city", "Belgrade");
        new JSONParse().execute(city);
    }

    private void renderWeather(JSONObject json) {
        try {

            String precipitation = "N/A";

            JSONArray listObjects = json.getJSONArray("list");
            for (int i = 0; i < listObjects.length(); i++) {
                JSONObject JSONobj1 = listObjects.getJSONObject(i);

                DateFormat df = DateFormat.getDateTimeInstance();
                String time = df.format(new Date(JSONobj1.getLong("dt") * 1000));

                if (JSONobj1.has("rain")) {
                    JSONObject JSONObj2 = JSONobj1.getJSONObject("rain");
                    if (JSONObj2.has("3h"))
                    precipitation = JSONObj2.getString("3h");
                }

                String temperature = JSONobj1.getJSONObject("main").getString("temp");

                int weatherIconId = JSONobj1.getJSONArray("weather").getJSONObject(0).getInt("id");

                HourlyForecast hourlyForecast = new HourlyForecast(time, precipitation, temperature, weatherIconId);
                hourlyForecastArrayList.add(hourlyForecast);


            }
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            Log.e("MYAPP", errors.toString(), e);
        }
    }

    private void setWeatherIcon(int actualId, int day_night) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (day_night == 1) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else if (day_night == 2) {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch (id) {
                case 2:
                    icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }


    class MyAdapter extends BaseAdapter {
        ArrayList<HourlyForecast> hourlyForecastArrayListInAdapter = new ArrayList<>();

        public MyAdapter(Context context, ArrayList<HourlyForecast> hourlyForecasts) {
            hourlyForecastArrayListInAdapter = hourlyForecasts;
        }

        @Override
        public int getCount() {
            return hourlyForecastArrayListInAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return hourlyForecastArrayListInAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return hourlyForecastArrayListInAdapter.indexOf(hourlyForecastArrayListInAdapter.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View rowView = inflater.inflate(R.layout.row_hourly, null);


            timeField = (TextView) rowView.findViewById(R.id.time_hourly_field);
            weatherIcon = (TextView) rowView.findViewById(R.id.weather_hourly_icon);
            precipitationField = (TextView) rowView.findViewById(R.id.precipitation_hourly_field);
            temperatureField = (TextView) rowView.findViewById(R.id.temperature_hourly_field);

            weatherIcon.setTypeface(weatherFont);

            HourlyForecast forecastInAdapter = hourlyForecastArrayListInAdapter.get(position);
            timeField.setText(forecastInAdapter.getTime());
            precipitationField.setText(forecastInAdapter.getPrecipitation());
            float temp = Float.parseFloat(forecastInAdapter.getTemperature());
            temperatureField.setText(String.format("%.0f", temp)+ "\u2103");


            setWeatherIcon(forecastInAdapter.getWeatherIconId(), 1);


            return rowView;
        }
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONObject json = RemoteFetch.getJSON(getActivity(), args[0], 2);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            renderWeather(json);

            listView = (ListView) rootView.findViewById(R.id.my_list_tab3);
            MyAdapter adapter = new MyAdapter(getActivity(), hourlyForecastArrayList);
            listView.setAdapter(adapter);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
