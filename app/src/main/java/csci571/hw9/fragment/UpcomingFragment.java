package csci571.hw9.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private InfoViewModel mViewModel;
    public UpcomingFragment() {

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
        UpcomingEventDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(InfoViewModel.class);
        binding.setViewModel(mViewModel);
        RecyclerView rView = binding.cards;
        UpcomingEventAdapter adapter = new UpcomingEventAdapter();
        mViewModel.setUpcomingAdapter(adapter);
        rView.setAdapter(adapter);
        rView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.sortDirSpinner.setEnabled(false);
        binding.setFragment(this);
        mViewModel.sortOptionIdx.setValue(0);
        mViewModel.sortDirIdx.setValue(0);
        mViewModel.sortOptionIdx.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d("sortOptionIdx", "onChanged: " + mViewModel.sortOptionIdx.getValue());
                onSelected();
            }
        });
        mViewModel.sortDirIdx.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                onSelected();
            }
        });
        return binding.getRoot();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onSelected() {
        if (mViewModel.sortOptionIdx.getValue() != null) {
            Log.d("", "onSelected: " + mViewModel.sortOptionIdx.getValue());
            if (mViewModel.sortOptionIdx.getValue() == 0) {
                this.getView().findViewById(R.id.sortDirSpinner).setEnabled(false);
            } else {
                this.getView().findViewById(R.id.sortDirSpinner).setEnabled(true);
            }
            mViewModel.sortList();
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
