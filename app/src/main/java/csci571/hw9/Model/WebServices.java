package csci571.hw9.Model;

import android.database.Observable;
import android.databinding.BaseObservable;
import android.util.Log;
import csci571.hw9.Schema.AutoCompleteSchema;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class WebServices extends BaseObservable {
    private final String BASE_URL = "https://csci571hw8shihyaol.azurewebsites.net/";
    public PublishSubject<List<String>> autoCompleteSource = PublishSubject.create();
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
                            names.add(schema.getName());
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
}

interface AutoCompleteAPI {

    @GET("auto-complete/{keyword}")
    Call<List<AutoCompleteSchema>> getAutoCompleteWord(@Path("keyword") String keyword);
}