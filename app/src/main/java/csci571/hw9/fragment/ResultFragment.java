package csci571.hw9.fragment;

import static android.content.ContentValues.TAG;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import csci571.hw9.adapters.ResultViewAdapter;
import csci571.hw9.viewmodel.MainViewModel;
import csci571.hw9.R;
import csci571.hw9.databinding.ResultDataBinding;

/**
 * A fragment representing a list of Items. <p /> Activities containing this fragment MUST implement
 * the {@link OnListFragmentInteractionListener} interface.
 */
public class ResultFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;
    private MainViewModel mViewModel;
    public ResultDataBinding resultDataBinding;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
     * screen orientation changes).
     */
    public ResultFragment() {
    }

    @SuppressWarnings("unused")
    public static ResultFragment newInstance(int columnCount) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        resultDataBinding = DataBindingUtil.inflate(inflater,R.layout.result_item_list,container,false);
        View view = resultDataBinding.getRoot();
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.init();
        resultDataBinding.setViewModel(mViewModel);
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        ResultViewAdapter adapter = new ResultViewAdapter();
        mViewModel.setmResultViewAdapter(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.d("ResultFragment", "onCreateView: ");
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                           + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     * <p/>
     * See the Android Training lesson <a href= "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {

        // TODO: Update argument type and name

    }
}
