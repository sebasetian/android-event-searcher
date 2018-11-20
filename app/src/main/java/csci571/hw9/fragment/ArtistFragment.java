package csci571.hw9.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import csci571.hw9.R;
import csci571.hw9.adapters.ArtistItemAdapter;
import csci571.hw9.databinding.ArtistItemDataBinding;
import csci571.hw9.databinding.ArtistListDataBinding;
import csci571.hw9.viewmodel.InfoViewModel;

public class ArtistFragment extends Fragment {
    private static ArtistFragment mFragment;
    private OnFragmentInteractionListener mListener;
    public InfoViewModel mViewModel;
    public ArtistFragment() {
        // Required empty public constructor
    }
    public static ArtistFragment newInstance() {
        if (mFragment == null) mFragment = new ArtistFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArtistListDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.recycler_artist_list,container,false);
        mViewModel = ViewModelProviders.of(getActivity()).get(InfoViewModel.class);
        View view = binding.getRoot();
        binding.setViewModel(mViewModel);

        RecyclerView rview = view.findViewById(R.id.artistListView);
        ArtistItemAdapter adapter = new ArtistItemAdapter();
        mViewModel.setArtistAdapter(adapter);
        rview.setAdapter(adapter);
        rview.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
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
    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     * <p>
     * See the Android Training lesson <a href= "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
