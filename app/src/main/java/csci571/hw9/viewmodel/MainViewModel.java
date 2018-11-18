package csci571.hw9.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.util.Log;
import csci571.hw9.adapters.ResultViewAdapter;
import csci571.hw9.model.WebServices;
import csci571.hw9.schema.SearchEventSchema;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.List;

public class MainViewModel extends ViewModel {
    private WebServices mWebservice = WebServices.getInstance();
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private ResultViewAdapter mResultViewAdapter;
    private List<SearchEventSchema> mSearchEvents;
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public void init() {
        mDisposable.add(mWebservice.searchEventsSource.subscribe(new Consumer<List<SearchEventSchema>>() {
            @Override
            public void accept(List<SearchEventSchema> searchEventSchemas) throws Exception {
                mSearchEvents = searchEventSchemas;
                if (mResultViewAdapter != null) {
                    Log.d("MainViewModel", "accept: setData");
                    mResultViewAdapter.setData(mSearchEvents);
                }
                Log.d("MainViewModel", "accept: ");
                isLoading.set(false);
            }
        }));
    }


    @Override
    public void onCleared() {
        mDisposable.dispose();
    }

    public void switchFav(SearchEventSchema event) {

    }

    public ResultViewAdapter getmResultViewAdapter() {
        return mResultViewAdapter;
    }

    public void setmResultViewAdapter(ResultViewAdapter mResultViewAdapter) {
        this.mResultViewAdapter = mResultViewAdapter;
        mResultViewAdapter.setViewModel(this);
        if (mSearchEvents != null) {
            mResultViewAdapter.setData(mSearchEvents);
            mResultViewAdapter.notifyDataSetChanged();
        }
        Log.d("MainViewModel", "setmResultViewAdapter: ");
    }

}
