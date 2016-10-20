package app.com.example.android.airline_android.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.example.android.airline_android.ApplicationConstants;
import app.com.example.android.airline_android.MainActivity;
import app.com.example.android.airline_android.R;
import app.com.example.android.airline_android.model.Airline;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import icepick.Icepick;
import icepick.State;

public class AirlineDetailFragment extends Fragment {
    @BindView(R.id.airline_logo_imageview)
    ImageView airlineLogoImageView;
    @BindView(R.id.airline_name_textview)
    TextView airlineNameTextView;
    @BindView(R.id.airline_site_imageview)
    ImageView airlineSiteImageView;
    @BindView(R.id.airline_site_textview)
    TextView airlineSiteTextView;
    @BindView(R.id.airline_phone_imageview)
    ImageView airlinePhoneImageView;
    @BindView(R.id.airline_phone_textview)
    TextView airlinePhoneTextView;
    @BindView(R.id.favourite_mark_floatbutton)
    FloatingActionButton favouriteFloatingButton;

    @State
    Airline selectedAirline;
    @State
    ArrayList<Airline> favouriteAirlineLists;
    @State
    String title;
    @State
    boolean hasAirline;

    private static final String SITE_PREFIX = "http://";
    private static final String TEL_PREFIX = "tel:";

    private OnAddOrRemoveAirlineListener onAddOrRemoveAirlineListener;
    private Context context;

    public static AirlineDetailFragment newInstance(Context context, Airline airline, ArrayList<Airline> favouriteAirlineLists) {
        AirlineDetailFragment fragment = new AirlineDetailFragment();
        fragment.context = context;
        fragment.favouriteAirlineLists = favouriteAirlineLists;
        fragment.selectedAirline = airline;
        fragment.title = airline.getName();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_airline_detail, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        onAddOrRemoveAirlineListener = (OnAddOrRemoveAirlineListener)getContext();
        displayAirlineDetail(selectedAirline);
        ((MainActivity) getActivity()).setActionBarTitle(title, true);
        Icepick.restoreInstanceState(this, saveInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void displayAirlineDetail(Airline airline) {
        airlineNameTextView.setText(airline.getName());
        airlineSiteTextView.setText(airline.getSite());
        airlinePhoneTextView.setText(airline.getPhone());

        hasAirline = foundAirline(airline);
        setFloatButtonColor();

        Picasso.with(this.getContext())
                .load(airline.getFormedLogoUrl())
                .resize(ApplicationConstants.LOGO_WIDTH, ApplicationConstants.LOGO_HEIGHT)
                .centerCrop()
                .into(airlineLogoImageView);
    }

    private void setFloatButtonAction() {
        String toastString = hasAirline ? String.format(getResources().getString(R.string.remove_toast_string), selectedAirline.getDefaultName()) :
                String.format(getResources().getString(R.string.add_toast_string), selectedAirline.getDefaultName());
        Toast.makeText(getContext(), toastString, Toast.LENGTH_SHORT).show();
        hasAirline = !hasAirline;
    }

    private void setFloatButtonColor() {
        int floatBackgoundColor = hasAirline ? ContextCompat.getColor(getContext(), R.color.colorOrange) : ContextCompat.getColor(getContext(), R.color.colorWhite);
        int floatTintColor = hasAirline ? ContextCompat.getColor(getContext(), R.color.colorWhite) : ContextCompat.getColor(getContext(), R.color.colorOrange);
        favouriteFloatingButton.setBackgroundTintList(ColorStateList.valueOf(floatBackgoundColor));
        favouriteFloatingButton.setColorFilter(floatTintColor);
    }

    private boolean foundAirline(Airline currentAirline) {
        for (Airline airline : favouriteAirlineLists) {
            if (airline.getName().equals(currentAirline.getName())) {
                return true;
            }
        }
        return false;
    }

    private void deleteAirline(Airline currentAirline) {
        Airline deleteAirline = null;
        for (Airline airline : favouriteAirlineLists) {
            if (airline.getName().equals(currentAirline.getName())) {
                deleteAirline = airline;
            }
        }
        favouriteAirlineLists.remove(deleteAirline);
    }

    @OnClick(R.id.airline_phone_detail)
    public void invokePhoneCall() {
        try {
            String dialString = TEL_PREFIX + selectedAirline.getPhone();
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dialString)));
        } catch (ActivityNotFoundException e) {
            Log.e("Dialing", "call Failed!", e);
        }
    }

    @OnClick(R.id.airline_site_detail)
    public void gotoWebsite() {
        try {
            String webUrl = selectedAirline.getSite();
            if (!webUrl.startsWith(SITE_PREFIX) && !webUrl.startsWith(SITE_PREFIX))
                webUrl = SITE_PREFIX + webUrl;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e("Redirecting", "Redirect to website Failed!", e);
        }
    }

    @OnClick(R.id.favourite_mark_floatbutton)
    public void onClickFavouriteAction() {
        if (hasAirline) {
            deleteAirline(selectedAirline);
        } else {
            favouriteAirlineLists.add(selectedAirline);
        }
        setFloatButtonAction();
        setFloatButtonColor();
        onAddOrRemoveAirlineListener.updateFavouriteList(favouriteAirlineLists);
    }

    public interface OnAddOrRemoveAirlineListener {
        void updateFavouriteList(ArrayList<Airline> newFavourites);
    }
}
