package csci571.hw9.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.util.Log;
import csci571.hw9.adapters.ArtistItemAdapter;

import csci571.hw9.adapters.UpcomingEventAdapter;
import csci571.hw9.model.WebServices;
import csci571.hw9.schema.ArtistInfo;
import csci571.hw9.schema.Attractions;
import csci571.hw9.schema.SearchEventSchema;
import csci571.hw9.schema.SongkickEvent;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InfoViewModel extends ViewModel {
    private WebServices mWebservice = WebServices.getInstance();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private SearchEventSchema mSearchEvent;

    private List<ArtistInfo> mArtists = new ArrayList<>();
    private List<SongkickEvent> upcomingEvents = new ArrayList<>();
    private int artistCount;
    private ArtistItemAdapter mArtAdapter;
    private UpcomingEventAdapter mUpcomingAdapter;

    public final List<String> SORT_OPTION_LIST = new ArrayList<>(Arrays.asList("Default", "Event Name", "Time", "Artist", "Type"));
    public final List<String> SORT_DIR_LIST = new ArrayList<>(Arrays.asList("Ascending","Descending"));
    public ObservableBoolean isArtistLoading = new ObservableBoolean(true);
    public ObservableBoolean isUpcomingLoading = new ObservableBoolean(true);
    public ObservableBoolean isNoUpcomingEvent = new ObservableBoolean(false);
    public ObservableBoolean isNoArtist = new ObservableBoolean(false);
    public ObservableInt sortOptionIdx = new ObservableInt(0);
    public ObservableInt sortDirIdx = new ObservableInt(0);

    public void init(SearchEventSchema SearchEvent) {
        mSearchEvent = SearchEvent;
        mArtists = new ArrayList<>();
        artistCount = 0;
        mDisposable.add(mWebservice.artistSource.subscribe(new Consumer<ArtistInfo>() {
            @Override
            public void accept(ArtistInfo artistInfo) throws Exception {
                if (artistInfo.name.length() > 0) mArtists.add(artistInfo);
                artistCount++;
                if (artistCount == mSearchEvent._embedded.attractions.length)
                    if (mArtAdapter != null) {
                        if (mArtists.size() > 0) {
                            mArtAdapter.setData(mArtists);
                            isNoArtist.set(false);
                        } else {
                            isNoArtist.set(true);
                        }
                        isArtistLoading.set(false);
                    }
            }
        }));
        mDisposable.add(mWebservice.upcomingEventSource.subscribe(
            new Consumer<List<SongkickEvent>>() {
                @Override
                public void accept(List<SongkickEvent> songkickEvents) throws Exception {
                    upcomingEvents = songkickEvents;
                    if (upcomingEvents.size() > 0) {
                        mUpcomingAdapter.setEvents(upcomingEvents);
                        isNoUpcomingEvent.set(false);
                    } else {
                        isNoUpcomingEvent.set(true);
                    }
                    isUpcomingLoading.set(false);
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

        if (mSearchEvent._embedded.venues != null && mSearchEvent._embedded.venues.length > 0) {
            mWebservice.searchVenueUpcomingEvent(mSearchEvent._embedded.venues[0].name);
        }

    }

    public SearchEventSchema getSearchEvent() {
        if (mSearchEvent == null) throw new RuntimeException("mSearchEvent is null");
        return mSearchEvent;
    }

    @Override
    public void onCleared() {
        mDisposable.dispose();
    }
    public void setArtistAdapter(ArtistItemAdapter adapter) {
        mArtAdapter = adapter;
        mArtAdapter.setViewModel(this);
        if (artistCount == mSearchEvent._embedded.attractions.length)
            if (mArtists.size() > 0) {
                mArtAdapter.setData(mArtists);
                isNoArtist.set(false);
            } else {
                isNoArtist.set(true);
            }

    }
    public void setUpcomingAdapter(UpcomingEventAdapter adapter) {
        mUpcomingAdapter = adapter;
        if (upcomingEvents.size() > 0) {
            mUpcomingAdapter.setEvents(upcomingEvents);
            isNoUpcomingEvent.set(false);
        } else {
            isNoUpcomingEvent.set(true);
        }
    }
    public void sortList(int sortIdx,int dirIdx) {
        if(upcomingEvents.size() > 0) {
            List<SongkickEvent> sortedList = new ArrayList<>(upcomingEvents);
            switch (SORT_OPTION_LIST.get(sortIdx)) {
                case "Event Name" : {
                    sortedByName(sortedList,dirIdx);
                    break;
                }
                case "Time" : {
                    sortedByTime(sortedList,dirIdx);
                    break;
                }
                case "Artist" : {
                    sortedByArtist(sortedList,dirIdx);
                    break;
                }
                case "Type" : {
                    sortedByType(sortedList,dirIdx);
                    break;
                }
                default: {
                    break;
                }
            }
            mUpcomingAdapter.setEvents(sortedList);
        }
    }
    private void sortedByName(List<SongkickEvent> list, final int dirIdx) {
        Collections.sort(list, new Comparator<SongkickEvent>() {
            @Override
            public int compare(SongkickEvent o1, SongkickEvent o2) {
                if(SORT_DIR_LIST.get(dirIdx).equals("Ascending")) return o1.displayName.compareTo(o2.displayName);
                else return o2.displayName.compareTo(o1.displayName);
            }
        });
    }
    private void sortedByTime(List<SongkickEvent> list,final int dirIdx) {
        Collections.sort(list, new Comparator<SongkickEvent>() {
            @Override
            public int compare(SongkickEvent o1, SongkickEvent o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.US);
                Date d1 = new Date();
                Date d2 = new Date();
                try {
                    d1 = format.parse(o1.start.date + " " + o1.start.time);
                    d2 = format.parse(o2.start.date + " " + o2.start.time);
                } catch(ParseException e) {
                    Log.d("sortedByTime", "compare: parse error");
                    e.printStackTrace();
                }
                if(SORT_DIR_LIST.get(dirIdx).equals("Ascending")) return (int)(d1.getTime() - d2.getTime());
                else return (int)(d2.getTime() - d1.getTime());
            }
        });
    }
    private void sortedByArtist(List<SongkickEvent> list,final int dirIdx) {
        Collections.sort(list, new Comparator<SongkickEvent>() {
            @Override
            public int compare(SongkickEvent o1, SongkickEvent o2) {
                if(SORT_DIR_LIST.get(dirIdx).equals("Ascending")) return o1.performance[0].displayname.compareTo(o2.performance[0].displayname);
                else return o2.performance[0].displayname.compareTo(o1.performance[0].displayname);
            }
        });
    }
    private void sortedByType(List<SongkickEvent> list,final int dirIdx) {
        Collections.sort(list, new Comparator<SongkickEvent>() {
            @Override
            public int compare(SongkickEvent o1, SongkickEvent o2) {
                if(SORT_DIR_LIST.get(dirIdx).equals("Ascending")) return o1.type.compareTo(o2.type);
                else return o2.type.compareTo(o1.type);
            }
        });
    }
}
