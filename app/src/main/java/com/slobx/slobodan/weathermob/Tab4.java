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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Tab4 extends Fragment {
    Typeface weatherFont;

    TextView timeField;
    TextView pressureField;
    TextView minTemperatureField;
    TextView maxTemperatureField;
    TextView weatherIcon;
    ListView listView;
    View rootView;
    SharedPreferences pref;

    private static final String PREFS_KEY = "prefs";

    ArrayList<TwoWeeksForecast> twoWeeksForecastArrayList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab4, container, false);

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
            String pressure = "N/A";

            JSONArray listObjects = json.getJSONArray("list");
            for (int i = 0; i < listObjects.length(); i++) {
                JSONObject JSONobj1 = listObjects.getJSONObject(i);

                DateFormat df = DateFormat.getDateTimeInstance();
                String getTime = df.format(new Date(JSONobj1.getLong("dt") * 1000));
                String time = getTime.substring(0, 12);



                if (JSONobj1.getString("pressure")!=null)
                    pressure = JSONobj1.getString("pressure");

                String minTemperature = JSONobj1.getJSONObject("temp").getString("min");
                String maxTemperature = JSONobj1.getJSONObject("temp").getString("max");

                int weatherIconId = JSONobj1.getJSONArray("weather").getJSONObject(0).getInt("id");

                TwoWeeksForecast twoWeeksForecast = new TwoWeeksForecast(time, minTemperature, maxTemperature, pressure, weatherIconId);
                twoWeeksForecastArrayList.add(twoWeeksForecast);


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
        ArrayList<TwoWeeksForecast> twoWeeksForecastArrayListInAdapter = new ArrayList<>();

        public MyAdapter(Context context, ArrayList<TwoWeeksForecast> twoWeeksForecasts) {
            twoWeeksForecastArrayListInAdapter = twoWeeksForecasts;
        }

        @Override
        public int getCount() {
            return twoWeeksForecastArrayListInAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return twoWeeksForecastArrayListInAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return twoWeeksForecastArrayListInAdapter.indexOf(twoWeeksForecastArrayListInAdapter.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View rowView = inflater.inflate(R.layout.row_two_weeks, null);


            timeField = (TextView) rowView.findViewById(R.id.time_two_weeks_field);
            weatherIcon = (TextView) rowView.findViewById(R.id.weather_two_weeks_icon);
            pressureField = (TextView) rowView.findViewById(R.id.pressure_two_weeks_field);
            minTemperatureField = (TextView) rowView.findViewById(R.id.min_temperature_two_weeks_field);
            maxTemperatureField = (TextView) rowView.findViewById(R.id.max_temperature_two_weeks_field);

            weatherIcon.setTypeface(weatherFont);

            TwoWeeksForecast forecastInAdapter = twoWeeksForecastArrayListInAdapter.get(position);
            timeField.setText(forecastInAdapter.getTime());
            pressureField.setText(forecastInAdapter.getPressure());
            float minTemp = Float.parseFloat(forecastInAdapter.getMinTemperature());
            float maxTemp = Float.parseFloat(forecastInAdapter.getMaxTemperature());
            minTemperatureField.setText("\u25BC"+(String.format("%.0f", minTemp) + "\u2103"));
            maxTemperatureField.setText("\u25B2"+(String.format("%.0f", maxTemp) + "\u2103"));


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
            JSONObject json = RemoteFetch.getJSON(getActivity(), args[0], 3);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            renderWeather(json);

            listView = (ListView) rootView.findViewById(R.id.my_list_tab4);
            MyAdapter adapter = new MyAdapter(getActivity(), twoWeeksForecastArrayList);
            listView.setAdapter(adapter);

        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
