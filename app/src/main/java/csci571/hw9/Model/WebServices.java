package csci571.hw9.Model;

import android.database.Observable;
import android.databinding.BaseObservable;
import android.util.Log;
import csci571.hw9.Schema.AutoCompleteSchema;
import csci571.hw9.Schema.LocationSchema;
import csci571.hw9.Schema.SearchEventSchema;
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
    public void postFrom(FormPostData data) {
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