package com.slobx.slobodan.weathermob;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private MyPagerAdapter mPageAdapter;
    public static Tab1 tab1;
    public static Tab2 tab2;
    public static Tab3 tab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPageAdapter = new MyPagerAdapter(getSupportFragmentManager(), this);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPageAdapter);
        mPager.setCurrentItem(1);
        mTabLayout.setTabsFromPagerAdapter(mPageAdapter);

        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        
    }




    class MyPagerAdapter extends FragmentStatePagerAdapter {

        private FragmentManager mFragmentManager;
        private Map<Integer, String> mFragmentTags;
        private Context mContext;

        public MyPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mFragmentManager = fm;
            mContext = context;
            mFragmentTags = new HashMap<Integer, String>();

        }


        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                tab1 = new Tab1();
                return tab1;
            } else if (position == 1) {
                tab2 = new Tab2();
                return tab2;
            } else if (position == 2) {
                 tab3 = new Tab3();
                 return tab3;
            } else if (position == 3) {
                Tab4 tab4 = new Tab4();
                return tab4;
            }


            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {


            if (position == 0) {
                return "Change city";
            }else if (position == 1) {
                return "Today";
            } else if (position == 2) {
                return "3 hour forecast";
            } else if (position == 3) {
                return "7 days forecast";
            }

            return "N/A";
        }

        public Object instantiateItem(ViewGroup container, int position){
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position){
            String tag = mFragmentTags.get(position);
            if (tag == null){
                return  null;
            }
            return mFragmentManager.findFragmentByTag(tag);
        }




    }
}
