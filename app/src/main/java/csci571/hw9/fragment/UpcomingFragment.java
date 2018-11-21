package csci571.hw9.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import csci571.hw9.R;
import csci571.hw9.adapters.ArtistItemAdapter;
import csci571.hw9.adapters.UpcomingEventAdapter;
import csci571.hw9.databinding.UpcomingEventDataBinding;
import csci571.hw9.schema.SongkickEvent;
import csci571.hw9.viewmodel.InfoViewModel;

public class UpcomingFragment extends Fragment {

    private static UpcomingFragment mFragment;
    private OnFragmentInteractionListener mListener;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    public static UpcomingFragment newInstance() {
        if (mFragment == null) mFragment = new UpcomingFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UpcomingEventDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_upcoming,container,false);
        final InfoViewModel viewModel = ViewModelProviders.of(getActivity()).get(InfoViewModel.class);
        binding.setViewModel(viewModel);
        RecyclerView rView = binding.cards;
        final UpcomingEventAdapter adapter = new UpcomingEventAdapter();
        viewModel.setUpcomingAdapter(adapter);
        rView.setAdapter(adapter);
        return binding.getRoot();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public static void destroyInstance() {
        mFragment = null;
    }
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void toSongkickPage(SongkickEvent event) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.uri));
        startActivity(intent);
    }
}
