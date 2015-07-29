package com.slobx.slobodan.weathermob;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Tab1 extends Fragment {


    private static final String PREFS_KEY = "prefs";
    AutoCompleteTextView acTextView;
    TextView chosenCityTextView;
    ImageView choseCityImageVIew, deleteCity;
    ListView listView;
    MyAdapter adapter;
    ArrayList<String> chosenCityArrayList = new ArrayList<>();
    int mSelectedItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab1, container, false);

        acTextView = (AutoCompleteTextView) v.findViewById(R.id.autoComplete);
        acTextView.setAdapter(new SuggestionAdapter(getActivity(), acTextView.getText().toString()));

        acTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                String city = selection.substring(0, selection.indexOf(",")).replaceAll("\\s+", "");
                SharedPreferences pref = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                pref.edit().putString("city", city).commit();
                if (!chosenCityArrayList.contains(city)) {
                    chosenCityArrayList.add(city);
                }
                adapter.notifyDataSetChanged();
                acTextView.setText("");
                acTextView.dismissDropDown();


            }
        });

        listView = (ListView) v.findViewById(R.id.my_list_tab1);
        adapter = new MyAdapter(getActivity(), chosenCityArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = position;
                adapter.notifyDataSetChanged();
                SharedPreferences pref = getActivity().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
                pref.edit().putString("city", chosenCityArrayList.get(mSelectedItem)).commit();
            }
        });



        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        acTextView.setText("");
        acTextView.dismissDropDown();
    }

    @Override
    public void onResume() {
        super.onResume();
        acTextView.setText("");
        acTextView.dismissDropDown();
    }

    class MyAdapter extends BaseAdapter {
        ArrayList<String> chosenCityArrayListInAdapter = new ArrayList<>();

        public MyAdapter(Context context, ArrayList<String> chosenCityArrayList) {
            this.chosenCityArrayListInAdapter = chosenCityArrayList;
        }

        @Override
        public int getCount() {
            return chosenCityArrayListInAdapter.size();
        }

        @Override
        public Object getItem(int position) {
            return chosenCityArrayListInAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return chosenCityArrayListInAdapter.indexOf(chosenCityArrayListInAdapter.get(position));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View rowView = inflater.inflate(R.layout.row_tab1, null);


            chosenCityTextView = (TextView) rowView.findViewById(R.id.chosen_city);
            choseCityImageVIew = (ImageView) rowView.findViewById(R.id.chosen_img);
            choseCityImageVIew.setVisibility(View.INVISIBLE);
            deleteCity = (ImageView) rowView.findViewById(R.id.delete_img);
            deleteCity.setImageResource(R.drawable.x_img);



            String chosenCity = chosenCityArrayListInAdapter.get(position);
            chosenCityTextView.setText(chosenCity);

            if(position == mSelectedItem){
                choseCityImageVIew.setVisibility(View.VISIBLE);
                choseCityImageVIew.setImageResource(R.drawable.chosen);
            }



            return rowView;
        }
    }
}


