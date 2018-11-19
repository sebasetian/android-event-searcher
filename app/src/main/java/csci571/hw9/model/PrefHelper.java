package csci571.hw9.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.google.gson.Gson;
import csci571.hw9.schema.SearchEventSchema;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrefHelper {
    private static PrefHelper prefHelper;
    private SharedPreferences mPref;
    private static final String NAME = "fav";
    private Gson gson = new Gson();
    public PublishSubject<String> prefChangeSource = PublishSubject.create();
    private OnSharedPreferenceChangeListener listener = new OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            prefChangeSource.onNext(key);
        }
    };
    PrefHelper(SharedPreferences pref) {
        mPref = pref;
        mPref.registerOnSharedPreferenceChangeListener(listener);
    }
    public static void initInstance(Context context) {
        if (prefHelper == null) {
            SharedPreferences pref = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
            prefHelper = new PrefHelper(pref);
        }
    }
    public static PrefHelper getInstance() {
        return prefHelper;
    }
    public void put(String key,SearchEventSchema val)
    {
        mPref.edit().putString(key,gson.toJson(val)).apply();
    }
    public void delete(String key) {
        mPref.edit().remove(key).apply();
    }
    public SearchEventSchema get(String key) {
        String val = mPref.getString(key,null);
        if (val == null) return null;
        return gson.fromJson(val,SearchEventSchema.class);
    }
    public List<SearchEventSchema> getAll() {
        List<SearchEventSchema> list = new ArrayList<>();
        Map<String,?> map = mPref.getAll();
        for (Map.Entry<String,?> en: map.entrySet()) {
            if (en.getValue() instanceof String) {
                list.add(gson.fromJson((String) en.getValue(),SearchEventSchema.class));
            }
        }
        return list;
    }
    public boolean contains(String key) {
        return mPref.contains(key);
    }
    public void switchPref(String key,SearchEventSchema val) {
        if (contains(key)) delete(key);
        else put(key,val);
    }
}
