package csci571.hw9.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.gson.Gson;
import csci571.hw9.InfoActivity;
import csci571.hw9.adapters.FavoriteItemAdapter;
import csci571.hw9.databinding.FavoriteDataBinding;
import csci571.hw9.model.PrefHelper;
import csci571.hw9.schema.SearchEventSchema;
import csci571.hw9.viewmodel.ResultViewModel;
import csci571.hw9.R;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class FavoriteFragment extends Fragment {

    private static FavoriteFragment mFragment;
    private ResultViewModel mViewModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    public static FavoriteFragment getInstance() {
        return new FavoriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        FavoriteDataBinding binding = DataBindingUtil.inflate(inflater,R.layout.favorite_fragment,container,false);
        binding.setPrefHelper(PrefHelper.getInstance());
        final FavoriteItemAdapter adapter = new FavoriteItemAdapter();
        adapter.setData(PrefHelper.getInstance().getAll());
        adapter.setViewModel(mViewModel);
        binding.favList.setAdapter(adapter);
        binding.favList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDisposable.add(
        PrefHelper.getInstance().prefChangeSource.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (!PrefHelper.getInstance().isEmpty.get()) {
                    adapter.setData(PrefHelper.getInstance().getAll());
                }
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
        mDisposable.add(
            mViewModel.toastSource.subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                }
            }));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onDestroy() {
        mDisposable.dispose();
        super.onDestroy();
    }
    public static void destroyInstance() {
        mFragment = null;
    }
}
