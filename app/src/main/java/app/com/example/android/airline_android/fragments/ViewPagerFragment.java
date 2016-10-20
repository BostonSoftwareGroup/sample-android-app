package app.com.example.android.airline_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.com.example.android.airline_android.ApplicationConstants;
import app.com.example.android.airline_android.R;
import app.com.example.android.airline_android.model.Airline;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class ViewPagerFragment extends Fragment {
    @BindView(R.id.vpPager)
    ViewPager vpPager;

    @State
    ArrayList<Airline> favouriteAirlineList;

    private static final int MAIN_TAB_POSITION = 0;
    private static final int FAVOURITE_TAB_POSITION = 1;
    private static final String MAIN_FRAGMENT_TITLE = "airline-android";

    private Context context;

    public FragmentStatePagerAdapter adapterViewPager;

    public static ViewPagerFragment newInstance(Context context, ArrayList<Airline> favouriteAirlineList) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.context = context;
        fragment.favouriteAirlineList = favouriteAirlineList;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.bind(this, view);
        Icepick.restoreInstanceState(this, savedInstanceState);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int NUM_PAGES = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case MAIN_TAB_POSITION:
                    return MainFragment.newInstance(context);
                case FAVOURITE_TAB_POSITION:
                    return MainFragment.newInstance(context, favouriteAirlineList, false, MAIN_FRAGMENT_TITLE);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == MAIN_TAB_POSITION) {
                return ApplicationConstants.MAIN_TAB_LABEL;
            } else if (position == FAVOURITE_TAB_POSITION) {
                return ApplicationConstants.FAVOURITE_TAB_LABEL;
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
