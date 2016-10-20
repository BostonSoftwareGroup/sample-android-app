package app.com.example.android.airline_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.ArrayList;

import app.com.example.android.airline_android.adapter.AirlineListAdapter;
import app.com.example.android.airline_android.fragments.AirlineDetailFragment;
import app.com.example.android.airline_android.fragments.ViewPagerFragment;
import app.com.example.android.airline_android.model.Airline;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainActivity extends AppCompatActivity implements AirlineListAdapter.OnAirlineClickedListener, AirlineDetailFragment.OnAddOrRemoveAirlineListener {
    @BindView(R.id.fragment)
    FrameLayout frameLayout;

    @State
    ArrayList<Airline> favouriteAirlineLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            replaceFragment(R.id.fragment, ViewPagerFragment.newInstance(this, favouriteAirlineLists), true);
        }
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    public void setActionBarTitle(String title, boolean enableHomeAsUp) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);
            getSupportActionBar().setDisplayHomeAsUpEnabled(enableHomeAsUp);
            getSupportActionBar().setTitle(title);
        }
    }

    private void replaceFragment(int layout, Fragment newFragment, boolean isInit) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.hyperspace_out, R.anim.hyperspace_in, R.anim.slide_out_right);
        if (isInit) {
            fragmentTransaction.add(layout, newFragment);
        } else {
            fragmentTransaction.replace(layout, newFragment);
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onAirlineSeletedListener(Airline selectedAirline) {
        AirlineDetailFragment fragment = AirlineDetailFragment.newInstance(this, selectedAirline, favouriteAirlineLists);
        replaceFragment(R.id.fragment, fragment, false);
    }

    @Override
    public void updateFavouriteList(ArrayList<Airline> favouriteAirlinesList) {
        this.favouriteAirlineLists = favouriteAirlinesList;
    }
}
