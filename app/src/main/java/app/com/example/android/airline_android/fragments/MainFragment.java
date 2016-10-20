package app.com.example.android.airline_android.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.com.example.android.airline_android.ApplicationConstants;
import app.com.example.android.airline_android.MainActivity;
import app.com.example.android.airline_android.R;
import app.com.example.android.airline_android.adapter.AirlineListAdapter;
import app.com.example.android.airline_android.model.Airline;
import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainFragment extends Fragment {
    @BindView(R.id.airline_recyclerview)
    RecyclerView airlineRecyclerView;
    @BindView(R.id.empty_favourite_textview)
    TextView emptyFavouriteTextView;

    @State
    ArrayList<Airline> airlineList;
    @State
    ArrayList<Airline> favouriteAirlineList;
    @State
    boolean showAllAirlines = true;
    @State
    String title;

    private static String jsonStr = "";

    private Context context;

    public static MainFragment newInstance(Context context, ArrayList<Airline> favouriteAirlineList, boolean showAllAirlines, String title) {
        MainFragment fragment = MainFragment.newInstance(context);
        fragment.title = title;
        fragment.setShowMode(showAllAirlines);
        fragment.favouriteAirlineList = favouriteAirlineList;
        return fragment;
    }

    public static MainFragment newInstance(Context context) {
        MainFragment fragment = new MainFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        setAirlineRecyclerView(favouriteAirlineList);
        setEmptyFavouriteLayout();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).setActionBarTitle(title, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (showAllAirlines) {
            new AsyncTaskParseJson().execute(ApplicationConstants.JSON_URL);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    public void setShowMode(boolean showAllAirlines) {
        this.showAllAirlines = showAllAirlines;
    }

    private ArrayList<Airline> convertJsonToObject(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, new TypeToken<List<Airline>>() {
        }.getType());
    }

    private void setAirlineRecyclerView(ArrayList<Airline> airlineList) {
        AirlineListAdapter adapter = new AirlineListAdapter(this.getContext(), airlineList);
        airlineRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        airlineRecyclerView.setAdapter(adapter);
    }

    private void setEmptyFavouriteLayout() {
        if (!showAllAirlines) {
            if (favouriteAirlineList == null || favouriteAirlineList.size() == 0) {
                emptyFavouriteTextView.setVisibility(View.VISIBLE);
            } else {
                emptyFavouriteTextView.setVisibility(View.GONE);
            }
        }
    }

    public class AsyncTaskParseJson extends AsyncTask<String, String, String> {
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder resultJson = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream input = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                String line;
                while ((line = reader.readLine()) != null) {
                    resultJson.append
                            (line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            jsonStr = resultJson.toString();
            return resultJson.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            airlineList = convertJsonToObject(jsonStr);
            setAirlineRecyclerView(airlineList);
        }
    }
}
