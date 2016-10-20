package app.com.example.android.airline_android.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.example.android.airline_android.ApplicationConstants;
import app.com.example.android.airline_android.R;
import app.com.example.android.airline_android.model.Airline;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AirlineListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private OnAirlineClickedListener listener;

    public ArrayList<Airline> airlineList;

    public AirlineListAdapter(Context context, ArrayList<Airline> airlineList) {
        this.context = context;
        this.airlineList = airlineList;
        this.listener = (OnAirlineClickedListener)context;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View airlineListView = inflater.inflate(R.layout.item_airline, parent, false);

        return new AirlineViewHolder(airlineListView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Airline airline = airlineList.get(position);
        AirlineViewHolder airlineViewHolder = (AirlineViewHolder) holder;
        airlineViewHolder.bindView(airline);
    }

    @Override
    public int getItemCount() {
        if (airlineList == null) {
            return 0;
        }
        return airlineList.size();
    }

    public class AirlineViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.airline_cardview)
        CardView airlineCardView;
        @BindView(R.id.airline_imageview)
        ImageView airlineImageView;
        @BindView(R.id.airline_textview)
        TextView airLineTextView;

        private Airline selectedAirline;

        public AirlineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Airline selectedAirline) {
            this.selectedAirline = selectedAirline;

            airLineTextView.setText(selectedAirline.getName());
            Picasso.with(context)
                    .load(selectedAirline.getFormedLogoUrl())
                    .resize(ApplicationConstants.LOGO_WIDTH, ApplicationConstants.LOGO_HEIGHT)
                    .centerCrop()
                    .into(airlineImageView);
        }

        @OnClick(R.id.airline_cardview)
        public void onItemClicked() {
            listener.onAirlineSeletedListener(selectedAirline);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnAirlineClickedListener {
        void onAirlineSeletedListener(Airline selectedAirline);
    }
}
