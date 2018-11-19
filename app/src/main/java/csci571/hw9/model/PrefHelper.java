package csci571.hw9.model;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import csci571.hw9.schema.SearchEventSchema;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrefHelper {
    private static PrefHelper prefHelper;
    private SharedPreferences mPref;
    private static final String NAME = "fav";
    private Gson gson = new Gson();
    PrefHelper(SharedPreferences pref) {
        mPref = pref;
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
    public SharedPreferences getPref() {
        return mPref;
    }
}
