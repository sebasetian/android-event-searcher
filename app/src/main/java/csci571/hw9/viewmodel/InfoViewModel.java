package csci571.hw9.viewmodel;

import android.arch.lifecycle.ViewModel;
import csci571.hw9.model.PrefHelper;
import csci571.hw9.model.WebServices;
import csci571.hw9.schema.SearchEventSchema;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class InfoViewModel extends ViewModel {
    private WebServices mWebservice = WebServices.getInstance();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private SearchEventSchema mSearchEvent;

    public void switchFav(SearchEventSchema event) {
        PrefHelper helper = PrefHelper.getInstance();
        helper.switchPref(event.id,event);
    }
    public void init(SearchEventSchema SearchEvent) {
        mSearchEvent = SearchEvent;
    }

    public SearchEventSchema getSearchEvent() {
        if (mSearchEvent == null) throw new RuntimeException("mSearchEvent is null");
        return mSearchEvent;
    }

    @Override
    public void onCleared() {
        mDisposable.dispose();
    }
}
