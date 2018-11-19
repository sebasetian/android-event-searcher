package csci571.hw9.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import csci571.hw9.InfoActivity;
import csci571.hw9.adapters.ResultViewAdapter;
import csci571.hw9.schema.SearchEventSchema;
import csci571.hw9.viewmodel.ResultViewModel;
import csci571.hw9.R;
import csci571.hw9.databinding.ResultDataBinding;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


public class ResultFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;
    private ResultViewModel mViewModel;
    public ResultDataBinding resultDataBinding;
    public CompositeDisposable mDisposable = new CompositeDisposable();
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
        Log.d("ResultFragment", "onCreate: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        resultDataBinding = DataBindingUtil.inflate(inflater,R.layout.result_item_list,container,false);
        View view = resultDataBinding.getRoot();
        mViewModel = ViewModelProviders.of(getActivity()).get(ResultViewModel.class);
        mViewModel.init();
        Log.d("ResultFragment", "onCreateView: " + getActivity());
        mDisposable.add(
        mViewModel.toastSource.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }
        }));
        mDisposable.add(
            mViewModel.infoSource.subscribe(new Consumer<SearchEventSchema>() {
                @Override
                public void accept(SearchEventSchema searchEventSchema) throws Exception {
                    Gson gson = new Gson();
                    Intent intent = new Intent(getActivity(), InfoActivity.class);
                    intent.putExtra("EVENT",gson.toJson(searchEventSchema));
                    startActivity(intent);
                }
            }));
        resultDataBinding.setViewModel(mViewModel);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        ResultViewAdapter adapter = new ResultViewAdapter();
        mViewModel.setResultViewAdapter(adapter);
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
        Log.d("ResultFragment", "onDetach: ");

    }
    public interface OnListFragmentInteractionListener {

        // TODO: Update argument type and name

    }
    @Override
    public void onDestroy() {
        mDisposable.dispose();
        Log.d("ResultFragment", "onDestroy: ");
        super.onDestroy();
    }
}
