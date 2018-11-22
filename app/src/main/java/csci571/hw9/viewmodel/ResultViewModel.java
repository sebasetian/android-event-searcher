package csci571.hw9.viewmodel;

import android.arch.lifecycle.ViewModel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;
import com.google.gson.Gson;
import csci571.hw9.adapters.ResultViewAdapter;
import csci571.hw9.model.PrefHelper;
import csci571.hw9.model.WebServices;
import csci571.hw9.schema.SearchEventSchema;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import java.util.List;

public class ResultViewModel extends ViewModel {
    private WebServices mWebservice = WebServices.getInstance();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private ResultViewAdapter mResultViewAdapter;
    private List<SearchEventSchema> mSearchEvents;
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public ObservableBoolean isNoData = new ObservableBoolean(false);
    public PublishSubject<String> toastSource = PublishSubject.create();
    public PublishSubject<SearchEventSchema> infoSource = PublishSubject.create();

    public void init() {
        mDisposable.add(mWebservice.searchEventsSource.subscribe(new Consumer<List<SearchEventSchema>>() {
            @Override
            public void accept(List<SearchEventSchema> searchEventSchemas) throws Exception {
                mSearchEvents = searchEventSchemas;
                Log.d("ResultViewModel", "accept: ");
                if (mResultViewAdapter != null) {
                    if (mSearchEvents.size() > 0) {
                        mResultViewAdapter.setData(mSearchEvents);
                        isNoData.set(false);
                    }
                    else isNoData.set(true);
                }
                isLoading.set(false);
            }
        }));
    }


    @Override
    public void onCleared() {
        mDisposable.dispose();
    }

    public void makeToast(String text) {
        toastSource.onNext(text);
    }
    public void goToInfo(SearchEventSchema event) {
        infoSource.onNext(event);
    }

    public void setResultViewAdapter(ResultViewAdapter mResultViewAdapter) {
        this.mResultViewAdapter = mResultViewAdapter;
        mResultViewAdapter.setViewModel(this);
        if (mSearchEvents != null) {
            if (mSearchEvents.size() > 0) {
                mResultViewAdapter.setData(mSearchEvents);
                isNoData.set(false);
            }
            else isNoData.set(true);
        }

    }

}
