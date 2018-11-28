package csci571.hw9.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import csci571.hw9.R;
import csci571.hw9.schema.Attractions;
import csci571.hw9.schema.SearchEventSchema;
import csci571.hw9.viewmodel.InfoViewModel;
import java.util.Locale;

public class EventInfoFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private static EventInfoFragment mFragment;
    private InfoViewModel mViewModel;
    public EventInfoFragment() {
        // Required empty public constructor
    }

    public static EventInfoFragment getInstance() {
        if (mFragment == null) mFragment = new EventInfoFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(InfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
        initText(view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                           + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void initText(View view) {
        SearchEventSchema event = mViewModel.getSearchEvent();


        TextView ArtistListText = view.findViewById(R.id.ArtistListText);
        StringBuilder artists = new StringBuilder();
        String divider = "";
        if (event._embedded != null && event._embedded.attractions != null) {
            for (Attractions attr : event._embedded.attractions) {
                artists.append(divider).append(attr.name);
                divider = " | ";
            }
        }
        if (artists.length() == 0) {
            ArtistListText.setText("N/A");
        } else {
            ArtistListText.setText(artists.toString());
        }


        TextView VenueText = view.findViewById(R.id.VenueText);
        if (event._embedded.venues.length == 0) {
            VenueText.setVisibility(View.GONE);
            view.findViewById(R.id.VenueTitle).setVisibility(View.GONE);
        } else {
            VenueText.setText(event._embedded.venues[0].name);
        }

        TextView TimeText = view.findViewById(R.id.TimeText);
        if (event.dates == null || event.dates.start == null || event.dates.start.localDate == null ||
            event.dates.start.localDate.length() == 0) {
            TimeText.setVisibility(View.GONE);
            view.findViewById(R.id.TimeTitle).setVisibility(View.GONE);
        } else {
            String time = event.dates.start.localDate;
            if (event.dates.start.localTime != null && event.dates.start.localTime.length() != 0) {
                time += " " + event.dates.start.localTime;
            }
            TimeText.setText(time);
        }

        TextView CateText = view.findViewById(R.id.CategoryText);
        if (event.classifications.length == 0) {
            CateText.setVisibility(View.GONE);
            view.findViewById(R.id.CategoryTitle).setVisibility(View.GONE);
        } else {
            String str = event.classifications[0].segment.name;
            if(event.classifications[0].genre != null) {
                str += " | " + event.classifications[0].genre.name;
            }
            CateText.setText(str);
        }

        TextView PriceText = view.findViewById(R.id.PriceText);
        if (event.priceRanges == null || event.priceRanges.length == 0) {
            PriceText.setVisibility(View.GONE);
            view.findViewById(R.id.PriceTitle).setVisibility(View.GONE);
        } else {
            String str = "";
            if (event.priceRanges[0].min != 0) {
                str += "$" + String.format(Locale.US,"%.2f",event.priceRanges[0].min);
                if (event.priceRanges[0].max != 0) {
                    str += " ~ $" + String.format(Locale.US,"%.2f",event.priceRanges[0].max);
                }
                PriceText.setText(str);
            } else if (event.priceRanges[0].max != 0) {
                str += "$" + String.format(Locale.US,"%.2f",(Double)event.priceRanges[0].max);
                PriceText.setText(str);
            } else {
                PriceText.setVisibility(View.GONE);
                view.findViewById(R.id.PriceTitle).setVisibility(View.GONE);
            }
        }

        TextView StatusText = view.findViewById(R.id.StatusText);
        if (event.dates == null || event.dates.status == null || event.dates.status.code == null) {
            StatusText.setVisibility(View.GONE);
            view.findViewById(R.id.StatusTitle).setVisibility(View.GONE);
        } else {
            StatusText.setText(event.dates.status.code);
        }

        TextView TicketText = view.findViewById(R.id.TicketmasterUrl);
        if (event.url == null) {
            TicketText.setVisibility(View.GONE);
            view.findViewById(R.id.TimeTitle).setVisibility(View.GONE);
        } else {
            TicketText.setClickable(true);
            TicketText.setMovementMethod(LinkMovementMethod.getInstance());
            String url = "<a href='" + event.url + "'> Ticketmaster</a>";
            TicketText.setText(Html.fromHtml(url));
        }

        TextView SeatMapText = view.findViewById(R.id.SeatMapUrl);
        if (event.seatmap == null || event.seatmap.staticUrl == null) {
            SeatMapText.setVisibility(View.GONE);
            view.findViewById(R.id.SeatMapTitle).setVisibility(View.GONE);
        } else {
            SeatMapText.setClickable(true);
            SeatMapText.setMovementMethod(LinkMovementMethod.getInstance());
            String url = "<a href='" + event.seatmap.staticUrl + "'> View Here</a>";
            SeatMapText.setText(Html.fromHtml(url));
        }

    }

    public static void destroyInstance() {
        mFragment = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
