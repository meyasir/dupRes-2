package com.darewrorestaurants.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darewrorestaurants.R;

/**
 * Created by Jaffar on 2018-02-14.
 */

public class OrdersHistoryFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static int int_items = 2 ;

    Activity activity;

    public OrdersHistoryFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_orders_history, container, false);
        viewPager = (ViewPager)rootView. findViewById(R.id.view_pager);
        tabLayout = (TabLayout)rootView. findViewById(R.id.tabs);

        viewPager.setAdapter(new MyAdapter(getFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return rootView;
    }

    private class MyAdapter extends FragmentStatePagerAdapter {

        MyAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new TodayOrdersFragment();
                case 1 : return new TodayDeliveryFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        /**
         * This method returns the title of the tab according to the position.
         */

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 :
                    return "TODAY ORDERS";
                case 1 :
                    return "Request History";
            }
            return null;
        }

    }
}
