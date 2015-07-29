package com.slobx.slobodan.weathermob;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonParse {
    double current_latitude,current_longitude;
    public JsonParse(){}
    public JsonParse(double current_latitude,double current_longitude){
        this.current_latitude=current_latitude;
        this.current_longitude=current_longitude;
    }
    public List<CityList> getParseJsonWCF(String sName)
    {
        List<CityList> ListData = new ArrayList<CityList>();
        try {
            String temp=sName.replace(" ", "%20");
            URL js = new URL(String.format("http://api.openweathermap.org/data/2.5/find?mode=json&type=like&q=%s&cnt=10", temp));
            URLConnection jc = js.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(jc.getInputStream()));
            String line = reader.readLine();
            JSONObject jsonResponse = new JSONObject(line);
            JSONArray jsonArray = jsonResponse.getJSONArray("list");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject r = jsonArray.getJSONObject(i);
                JSONObject g = r.getJSONObject("sys");

                ListData.add(new CityList(r.getString("id"),r.getString("name"), g.getString("country")));
            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ListData;

    }

}