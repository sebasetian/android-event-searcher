package csci571.hw9;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.util.Log;
import csci571.hw9.Model.WebServices;
import csci571.hw9.Schema.SearchEventSchema;
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
                Log.d("MainViewModel", "accept");
                isLoading.set(false);
            }
        }));
    }

    @Override
    public void onCleared() {
        mDisposable.dispose();
    }

}
