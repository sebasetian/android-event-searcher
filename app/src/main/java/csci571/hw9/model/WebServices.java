package csci571.hw9.model;

import android.databinding.BaseObservable;
import android.util.Log;
import csci571.hw9.schema.ArtistInfo;
import csci571.hw9.schema.AutoCompleteSchema;
import csci571.hw9.schema.CustomImg;
import csci571.hw9.schema.LocationSchema;
import csci571.hw9.schema.SearchEventSchema;
import csci571.hw9.schema.SongkickEvent;
import csci571.hw9.schema.SongkickVenueInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class WebServices extends BaseObservable {
    private static WebServices instance;
    private final String BASE_URL = "https://csci571hw8shihyaol.azurewebsites.net/";
    public PublishSubject<List<String>> autoCompleteSource = PublishSubject.create();
    public PublishSubject<LocationSchema> locationSource = PublishSubject.create();
    public PublishSubject<List<SearchEventSchema>> searchEventsSource = PublishSubject.create();
    public PublishSubject<ArtistInfo> artistSource = PublishSubject.create();
    public PublishSubject<List<CustomImg>> imgSource = PublishSubject.create();
    public PublishSubject<Integer> venueInfoSource = PublishSubject.create();
    public PublishSubject<List<SongkickEvent>> upcomingEventSource = PublishSubject.create();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    public static synchronized WebServices getInstance(){
        if (instance == null) {
            instance = new WebServices();
        }
        return instance;
    }
    public void autocomplete(String keyword) {
        if (keyword == null || keyword.length() == 0) return;
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
        AutoCompleteAPI api = retrofit.create(AutoCompleteAPI.class);

        api.getAutoCompleteWord(keyword).enqueue(
            new Callback<List<AutoCompleteSchema>>() {
                @Override
                public void onResponse(Call<List<AutoCompleteSchema>> call,
                                       Response<List<AutoCompleteSchema>> response) {
                    List<String> names = new ArrayList<>();
                    List<AutoCompleteSchema> schemas = response.body();
                    if (schemas != null) {
                        for (AutoCompleteSchema schema : schemas) {
                            names.add(schema.name);
                        }
                    }
                    autoCompleteSource.onNext(names);
                }

                @Override
                public void onFailure(Call<List<AutoCompleteSchema>> call, Throwable t) {
                    Log.d("webservice", "autocomplete: request failed");
                    t.printStackTrace();
                }
            });

    }
    public void getLocationFromAddress(String address) {
        if (address == null || address.length() == 0) return;
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
        LocationAPI api = retrofit.create(LocationAPI.class);

        api.getLocation(address).enqueue(new Callback<LocationSchema>() {
            @Override
            public void onResponse(Call<LocationSchema> call, Response<LocationSchema> response) {
                LocationSchema schema = response.body();
                if (schema != null) {
                    locationSource.onNext(schema);
                }
            }

            @Override
            public void onFailure(Call<LocationSchema> call, Throwable t) {
                Log.d("webservice", "getLocationFromAddress: request failed");
                t.printStackTrace();
            }
        });
    }

    public void getArtist(final String name) {
        Log.d("webservice", "getArtist: ");
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        SpotifyAPI api = retrofit.create(SpotifyAPI.class);

        api.getArtist(name).enqueue(new Callback<List<ArtistInfo>>() {
            @Override
            public void onResponse(Call<List<ArtistInfo>> call,
                                   Response<List<ArtistInfo>> response) {
                if(response.body() == null) artistSource.onNext(new ArtistInfo());
                else {
                    for (ArtistInfo artist:response.body()) {
                        if (artist.name.equals(name)) {
                            searchImg(artist);
                            return;
                        }
                    }
                    artistSource.onNext(new ArtistInfo());
                }
            }

            @Override
            public void onFailure(Call<List<ArtistInfo>> call, Throwable t) {

            }
        });
        
    }
    public void searchImg(final ArtistInfo artistInfo) {
        Log.d("webservice", "searchImg: ");
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        ImgSearchAPI api = retrofit.create(ImgSearchAPI.class);

        api.searchImg(artistInfo.name).enqueue(new Callback<List<CustomImg>>() {
            @Override
            public void onResponse(Call<List<CustomImg>> call, Response<List<CustomImg>> response) {
                artistInfo.Imgs = response.body();
                artistSource.onNext(artistInfo);
            }

            @Override
            public void onFailure(Call<List<CustomImg>> call, Throwable t) {

            }
        });
    }

    public void searchVenueId(final String name) {
        Log.d("webservice", "searchVenueId: ");
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        FindVenueIdAPI api = retrofit.create(FindVenueIdAPI.class);

        api.searchVenueId(name).enqueue(new Callback<List<SongkickVenueInfo>>() {
            @Override
            public void onResponse(Call<List<SongkickVenueInfo>> call,
                                   Response<List<SongkickVenueInfo>> response) {
                if(response.body() != null) {
                    for (SongkickVenueInfo venue: response.body()) {
                        if(venue.displayName.equals(name)) {
                            venueInfoSource.onNext(venue.id);
                            return;
                        }
                    }
                    venueInfoSource.onNext(-1);
                }
            }

            @Override
            public void onFailure(Call<List<SongkickVenueInfo>> call, Throwable t) {
                Log.d("webservice", "searchVenueId: request failed");
                t.printStackTrace();
            }
        });
    }

    public void searchUpcomingEvent(int id) {
        Log.d("webservice", "searchUpcomingEvent: ");
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        UpcomingEventAPI api = retrofit.create(UpcomingEventAPI.class);

        api.searchUpcomingEvent(id).enqueue(new Callback<List<SongkickEvent>>() {
            @Override
            public void onResponse(Call<List<SongkickEvent>> call,
                                   Response<List<SongkickEvent>> response) {
                List<SongkickEvent> list = new ArrayList<>();
                if (response.body() != null) {
                    for (int i = 0; i < Math.min(response.body().size(),5); i++) {
                        list.add(response.body().get(i));
                    }
                }
                upcomingEventSource.onNext(list);
            }

            @Override
            public void onFailure(Call<List<SongkickEvent>> call, Throwable t) {
                Log.d("webservice", "searchUpcomingEvent: request failed");
                t.printStackTrace();
            }
        });
    }
    public void postFrom(FormPostData data) {
        Log.d("webservice", "postFrom:");
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
        EventSearchAPI api = retrofit.create(EventSearchAPI.class);
        api.postForm(data).enqueue(new Callback<List<SearchEventSchema>>() {
            @Override
            public void onResponse(Call<List<SearchEventSchema>> call,
                                   Response<List<SearchEventSchema>> response) {
                List<SearchEventSchema> list = new ArrayList<>();
                Log.d("webservice", "postFrom: onResponse");

                if (response.body() != null) {
                    list.addAll(response.body());
                }
                searchEventsSource.onNext(list);
            }

            @Override
            public void onFailure(Call<List<SearchEventSchema>> call, Throwable t) {
                Log.d("webservice", "postFrom: request failed");
                t.printStackTrace();
            }
        });
    }

}

interface AutoCompleteAPI {

    @GET("auto-complete/{keyword}")
    Call<List<AutoCompleteSchema>> getAutoCompleteWord(@Path("keyword") String keyword);
}
interface LocationAPI {

    @GET("geo/{address}")
    Call<LocationSchema> getLocation(@Path("address") String address);
}
interface EventSearchAPI {

    @POST("form/")
    Call<List<SearchEventSchema>> postForm(@Body FormPostData data);

}
interface SpotifyAPI {

    @GET("spotify/{name}")
    Call<List<ArtistInfo>> getArtist(@Path("name") String name);
}
interface ImgSearchAPI {

    @GET("img-search/{name}")
    Call<List<CustomImg>> searchImg(@Path("name") String name);
}
interface FindVenueIdAPI{

    @GET("find-venue-id/{name}")
    Call<List<SongkickVenueInfo>> searchVenueId(@Path("name") String name);
}
interface UpcomingEventAPI {

    @GET("find-venue-upcoming-event/{id}")
    Call<List<SongkickEvent>> searchUpcomingEvent(@Path("id") int id);
}