package csci571.hw9.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.util.Log;
import csci571.hw9.model.WebServices;
import csci571.hw9.schema.SearchEventSchema;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.List;

public class MainViewModel extends ViewModel {
    private WebServices mWebservice = WebServices.getInstance();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    public void init() {

        mDisposable.add(mWebservice.searchEventsSource.subscribe(new Consumer<List<SearchEventSchema>>() {
            @Override
            public void accept(List<SearchEventSchema> searchEventSchemas) throws Exception {
                Log.d("MainViewModel", "accept " + searchEventSchemas.get(0).name);
                isLoading.set(false);
            }
        }));
    }

    @Override
    public void onCleared() {
        mDisposable.dispose();
    }

}
