package csci571.hw9.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.util.Log;
import android.view.View;
import csci571.hw9.adapters.ArtistItemAdapter;
import csci571.hw9.model.PrefHelper;
import csci571.hw9.model.WebServices;
import csci571.hw9.schema.ArtistInfo;
import csci571.hw9.schema.Attractions;
import csci571.hw9.schema.SearchEventSchema;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

public class InfoViewModel extends ViewModel {
    private WebServices mWebservice = WebServices.getInstance();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private SearchEventSchema mSearchEvent;
    public ObservableBoolean isLoading = new ObservableBoolean(true);
    private List<ArtistInfo> artists = new ArrayList<>();
    private int artistCount;
    private ArtistItemAdapter mAdapter;

    public void init(SearchEventSchema SearchEvent) {
        mSearchEvent = SearchEvent;
        artists = new ArrayList<>();
        artistCount = 0;
        mDisposable.add(mWebservice.artistSource.subscribe(new Consumer<ArtistInfo>() {
            @Override
            public void accept(ArtistInfo artistInfo) throws Exception {
                if (artistInfo.name.length() > 0) artists.add(artistInfo);
                artistCount++;
                if (artistCount == mSearchEvent._embedded.attractions.length)
                    if (mAdapter != null) {
                        mAdapter.setData(artists);
                        isLoading.set(false);
                    }
            }
        }));
        for(Attractions attr:mSearchEvent._embedded.attractions) {
            if (attr.classifications[0].segment.name.equals("Music")) {
                mWebservice.getArtist(attr.name);
            } else {
                ArtistInfo artist = new ArtistInfo();
                artist.name = attr.name;
                artist.setNonMusic();
                mWebservice.searchImg(artist);
            }
        }

    }

    public SearchEventSchema getSearchEvent() {
        if (mSearchEvent == null) throw new RuntimeException("mSearchEvent is null");
        return mSearchEvent;
    }

            public int isMusic() {
                if (mSearchEvent.classifications[0].segment.name.equals("Music")) {
                    return View.VISIBLE;
                } else {
                    return View.GONE;
                }
    }
    @Override
    public void onCleared() {
        mDisposable.dispose();
    }
    public void setArtistAdapter(ArtistItemAdapter adapter) {
        mAdapter = adapter;
        mAdapter.setViewModel(this);
        if (artistCount == mSearchEvent._embedded.attractions.length)
            if (mAdapter != null) {
                mAdapter.setData(artists);
            }
    }
}
