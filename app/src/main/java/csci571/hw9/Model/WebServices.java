package csci571.hw9.Model;

import android.util.Log;
import csci571.hw9.Schema.AutoCompleteSchema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class WebServices {
    private final String BASE_URL = "https://csci571hw8shihyaol.azurewebsites.net/";
    public List<String> autocomplete(String keyword) {
        if (keyword == null || keyword.length() == 0) return new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        AutoCompleteAPI api = retrofit.create(AutoCompleteAPI.class);
        try {
            Response<List<AutoCompleteSchema>> res = api.getAutoCompleteWord(keyword).execute();
            if (res.isSuccessful()) {
                List<String> names = new ArrayList<>();
                List<AutoCompleteSchema> schemas = res.body();
                if (schemas != null) {
                    for (AutoCompleteSchema schema : schemas) {
                        names.add(schema.getName());
                    }
                }
                return names;
            } else {
                Log.d("auto-complete", "autocomplete: request failed");
                return new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

interface AutoCompleteAPI {

    @GET("auto-complete/{keyword}")
    Call<List<AutoCompleteSchema>> getAutoCompleteWord(@Path("keyword") String keyword);
}